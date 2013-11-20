package com.jm.fxw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Hair;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.HairAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

/**
 * 作品列表页
 */
public class WorkListUI extends Activity implements OnClickListener,
		OnScrollListener, OnItemClickListener {
	private GridView ListView;
	private HairAdapter adapter;
	private List<Hair> mlist = new ArrayList<Hair>();
	private int page = 1;
	private int pageCount = 0;
	private SessionManager sm;
	private boolean isloading = false;
	private boolean showlast = false;

	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hairlist);
		init();
		new GetHairListTask().execute();
	}

	private void init() {
		sm = SessionManager.getInstance();
		Intent i = getIntent();
		uid = i.getStringExtra("uid");
		if (uid != null && !uid.equals("")) {
			if (sm.getUserId().equals(uid)) {
				((TextView) findViewById(R.id.tv_mainhead)).setText("我的作品");
			} else {

				((TextView) findViewById(R.id.tv_mainhead)).setText("TA的作品");
			}
		} else {
			finish();
		}
		ListView = (GridView) findViewById(R.id.my_hairgridview);
		adapter = new HairAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);
		ListView.setOnScrollListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "作品列表页面");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "作品列表页面");
	}

	/*
	 * 读取发型列表
	 */
	class GetHairListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (mlist == null || mlist.size() == 0) {
				adapter.setProgress(true);
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_MYWORKLIST_LIST + "&page="
					+ page, getInfoInqVal());

		}

		protected void onPostExecute(Response result) {

			isloading = false;
			adapter.setProgress(false);
			if (result == null) {

				LogUtil.e("can't get typelist");
				return;
			} else {
				mlist = result.getList("works_info", new Hair());
				if (mlist == null || mlist.size() == 0) {

					TispToastFactory.getToast(WorkListUI.this, "暂无数据");
					return;
				}
				pageCount = Integer.parseInt(result.getString("page_count"));
				showlast = false;
				adapter.appendHairList(mlist);
				adapter.appendHList(result.getList("image_list", new Hair()));
				adapter.notifyDataSetChanged();

			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_leftTop:
			// 打开分类
			this.finish();
			break;
		}
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
		Intent intent = new Intent(WorkListUI.this, HairInfoUI.class);
		intent.putExtra("hlist", (Serializable) adapter.getList());
		intent.putExtra("id", hair.getId());
		startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
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
