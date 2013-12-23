package com.jm.fxw;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.GridView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.data.DatabaseHelper;
import com.jm.entity.DongTai;
import com.jm.entity.Hair;
import com.jm.finals.Constant;
import com.jm.sort.DongTaiAdapter;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

public class DongTaiUI extends OrmLiteBaseActivity<DatabaseHelper> implements
		OnClickListener, OnScrollListener {
	private GridView ListView;
	private DongTaiAdapter adapter;
	private List<DongTai> mlist = new ArrayList<DongTai>();
	private List<Hair> hlist = new ArrayList<Hair>();
	private int page = 1;
	private int pageCount = 0;
	private String condition = "add_time";
	private boolean isloading = false;
	private boolean showlast = false;
	private boolean baseDate = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dongtailist);
		ListView = (GridView) findViewById(R.id.staggeredGridView);
		adapter = new DongTaiAdapter(this,getWindow().getWindowManager().getDefaultDisplay().getWidth());
		ListView.setAdapter(adapter);
		ListView.setOnScrollListener(this);
		init();
		getDataFromDataBase();
		new GetDongTaiListTask().execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobileProbe.onResume(this, "动态列表页面");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobileProbe.onPause(this, "动态列表页面");
	}

	private void getDataFromDataBase() {
		List<DongTai> baseHairList = null;
		DatabaseHelper db = getHelper();
		try {
			baseHairList = db.getDongTaiList();
		} catch (SQLException e) {
		}
		if (baseHairList.isEmpty()) {
		} else {
			adapter.setHairList(baseHairList);
			adapter.notifyDataSetChanged();
		}

	}

	private void init() {
		findViewById(R.id.btn_zuixin).setOnClickListener(this);
		findViewById(R.id.btn_tongcheng).setOnClickListener(this);
		findViewById(R.id.btn_tuijian).setOnClickListener(this);
		ResetButtonBg();
		((Button) findViewById(R.id.btn_zuixin))
				.setBackgroundResource(R.drawable.left_bg1);

		((Button) findViewById(R.id.btn_zuixin)).setTextColor(Color.rgb(240,
				28, 97));
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_rightTop).setOnClickListener(this);

	}

	/*
	 * 读取动态列表
	 */
	class GetDongTaiListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			if (adapter.getHairList() == null
					|| adapter.getHairList().size() == 0) {
				adapter.setProgress(true);
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("condition", condition);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_DONGTAI + "&page=" + page,
					getInfoInqVal());

		}

		protected void onPostExecute(Response result) {
			isloading = false;
			adapter.setProgress(false);
			if (result.isSuccessful()) {
				if (baseDate) {
					page = 1;
					pageCount = 0;
					adapter.clear();
					baseDate = false;
				}
				try {

					pageCount = Integer
							.parseInt(result.getString("page_count"));
				} catch (Exception e) {
					LogUtil.e(e.toString());
				}

				if (!result.getString("works_info").equals("")) {
					mlist = result.getList("works_info", new DongTai());
					hlist = result.getList("image_list", new Hair());
				}
				if (mlist == null || mlist.size() == 0) {
					TispToastFactory.getToast(DongTaiUI.this, "暂无数据").show();
					return;
				} else {
					for (int i = 0; i < mlist.size(); i++) {
						DongTai hair = mlist.get(i);
						if (hair.getWork_id() != null
								&& !hair.getWork_id().equals("")) {
							try {
								if (getHelper().getDongTai(hair.getWork_id()) == null) {
									getHelper().getDongTaiDao().create(hair);
								} else {
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								LogUtil.e(e.toString());
							}
						}
					}
				}
				showlast = false;
				adapter.appendList(mlist);
				adapter.appendHairList(hlist);
				adapter.notifyDataSetChanged();

			} else {
				TispToastFactory.getToast(DongTaiUI.this, result.getMsg())
						.show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_leftTop:
			// 打开分类
			this.finish();
			break;
		case R.id.btn_rightTop:

			StartActivityContController.goPage(this, PublicPortfolio.class,
					true);
			break;
		case R.id.btn_zuixin:
			changeBtn("add_time", v);
			break;
		case R.id.btn_tongcheng:
			changeBtn("comment_num", v);
			break;
		case R.id.btn_tuijian:
			changeBtn("collect_num", v);
			break;
		}
	}

	private void changeBtn(String urnAllDongtai, View v) {
		ResetButtonBg();
		((Button) v).setBackgroundResource(R.drawable.left_bg1);
		((Button) v).setTextColor(Color.rgb(240, 28, 97));
		adapter.clear();
		mlist.clear();
		hlist.clear();
		condition = urnAllDongtai;
		page = 1;
		pageCount = 0;
		new GetDongTaiListTask().execute();

	}

	private void ResetButtonBg() {

		((Button) findViewById(R.id.btn_zuixin))
				.setBackgroundResource(R.drawable.left_bg);

		((Button) findViewById(R.id.btn_zuixin)).setTextColor(Color
				.rgb(0, 0, 0));
		((Button) findViewById(R.id.btn_tongcheng))
				.setBackgroundResource(R.drawable.center_bg);
		((Button) findViewById(R.id.btn_tongcheng)).setTextColor(Color.rgb(0,
				0, 0));
		((Button) findViewById(R.id.btn_tuijian))
				.setBackgroundResource(R.drawable.right_bg);
		((Button) findViewById(R.id.btn_tuijian)).setTextColor(Color.rgb(0, 0,
				0));
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (showlast) {
			return;
		}
		if (totalItemCount - (firstVisibleItem + visibleItemCount) <= 0) {

			LogUtil.d("正在显示" + page + "页" + "总页数为" + pageCount + "页");
			if (page < pageCount) {
				if (!isloading) {
					LogUtil.d("即将加载" + ++page + "页");
					isloading = true;
					new GetDongTaiListTask().execute();
				}
			}
		}

	}
}
