package com.jm.fxw;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.data.DatabaseHelper;
import com.jm.entity.Hair;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.HairAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

public class ZhaoFaXingListUI extends OrmLiteBaseActivity<DatabaseHelper>
		implements OnClickListener, OnScrollListener, OnItemClickListener {
	private TextView tv_mainhead;
	private GridView ListView;
	private HairAdapter adapter;
	private List<Hair> mlist = new ArrayList<Hair>();
	private int page = 1;
	private int pageCount = 0;
	private String type;
	private String condition = "new";
	private SessionManager sm;
	private boolean isloading = false;
	private boolean showlast = false;

	private boolean baseDate = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhaofaxinglist);
		init();
		sm = SessionManager.getInstance();
		ListView = (GridView) findViewById(R.id.zhaofaxing_hairgridview);
		adapter = new HairAdapter(this, getWindow().getWindowManager()
				.getDefaultDisplay().getWidth());
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);
		ListView.setOnScrollListener(this);
		getDataFromDataBase();
		new GetHairListTask().execute();

	}

	@Override
	protected void onResume() {

		super.onResume();
		MobileProbe.onResume(this, "找发型页面");
	}

	@Override
	protected void onPause() {

		super.onPause();
		MobileProbe.onPause(this, "找发型页面");
	}

	private void getDataFromDataBase() {

		List<Hair> baseHairList = null;
		DatabaseHelper db = getHelper();
		try {
			baseHairList = db.getHairsList(type);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LogUtil.e("SQLException + " + e.toString());
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
		((Button) findViewById(R.id.btn_zuixin)).setTextColor(Color.rgb(240,
				28, 97));
		tv_mainhead = (TextView) findViewById(R.id.tv_mainhead);
		Intent i = getIntent();
		type = i.getStringExtra("type");

		if ("1".equals(type)) {
			tv_mainhead.setText("短发");
		}

		else if ("2".equals(type)) {
			tv_mainhead.setText("中发");

		} else if ("3".equals(type)) {
			tv_mainhead.setText("长发");

		} else if ("4".equals(type)) {
			tv_mainhead.setText("盘发");

		} else if ("5".equals(type)) {
			tv_mainhead.setText("男发");

		} else if ("6".equals(type)) {
			tv_mainhead.setText("全部");

		} else if ("7".equals(type)) {
			tv_mainhead.setText("收藏榜");
			findViewById(R.id.lin_fenlei).setVisibility(View.GONE);
		}
		findViewById(R.id.btn_leftTop).setOnClickListener(this);

	}

	/*
	 * 读取发型列表
	 */
	class GetHairListTask extends AsyncTask<String, Integer, Response> {

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
			map.put("uid", sm.getUserId());
			map.put("cid", type);
			map.put("condition", condition);
			map.put("city_name", sm.getCity());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_ZHAOFAXING_LIST + "&page="
					+ page, getInfoInqVal());

		}

		protected void onPostExecute(Response result) {

			isloading = false;
			adapter.setProgress(false);

			if (result == null) {
				return;
			}
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
					mlist = result.getList("works_info", new Hair());
				}
				if (mlist == null || mlist.size() == 0) {
					TispToastFactory.getToast(ZhaoFaXingListUI.this, "暂无数据")
							.show();
				} else {
					for (int i = 0; i < mlist.size(); i++) {
						Hair hair = mlist.get(i);
						hair.setType(type);
						String id = hair.getId() + "";
						if (id != null && !id.equals("")) {
							try {
								if (getHelper().getHair(id, type) == null) {
									getHelper().getHairDao().create(hair);
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
				adapter.appendHairList(mlist);
				adapter.appendHList(result.getList("image_list", new Hair()));
				adapter.notifyDataSetChanged();
			} else {
				TispToastFactory.getToast(ZhaoFaXingListUI.this,
						result.getMsg()).show();
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
		case R.id.btn_zuixin:
			changeCondition("new", v);
			break;
		case R.id.btn_tongcheng:
			changeCondition("city", v);
			break;
		case R.id.btn_tuijian:
			changeCondition("recommend", v);
			break;
		}
	}

	private void changeCondition(String condition, View v) {
		ResetButtonBg();
		((Button) v).setTextColor(Color.rgb(240, 28, 97));
		adapter.clear();
		this.mlist.clear();
		this.condition = condition;
		page = 1;
		pageCount = 0;
		new GetHairListTask().execute();
	}

	private void ResetButtonBg() {
		((Button) findViewById(R.id.btn_zuixin)).setTextColor(Color
				.rgb(0, 0, 0));
		((Button) findViewById(R.id.btn_tongcheng)).setTextColor(Color.rgb(0,
				0, 0));
		((Button) findViewById(R.id.btn_tuijian)).setTextColor(Color.rgb(0, 0,
				0));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (adapter.getHairList() == null || position < 0
				|| position >= adapter.getHairList().size()) {
			LogUtil.e("position = " + position);
			return;
		}

		Hair hair = adapter.getHairList().get(position);
		Intent intent = new Intent(ZhaoFaXingListUI.this, HairInfoUI.class);
		intent.putExtra("hlist", (Serializable) adapter.getList());
		intent.putExtra("id", hair.getId());
		startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub{
		if (showlast) {
			return;
		}
		if (totalItemCount - (firstVisibleItem + visibleItemCount) <= 0) {

			LogUtil.d("正在显示" + page + "页" + "总页数为" + pageCount + "页");
			if (page < pageCount) {
				if (!isloading) {
					LogUtil.d("即将加载" + ++page + "页");
					isloading = true;
					new GetHairListTask().execute();
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}
}
