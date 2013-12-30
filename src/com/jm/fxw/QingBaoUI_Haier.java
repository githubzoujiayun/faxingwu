package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.baidu.location.LocationClient;
import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.News;
import com.jm.finals.Constant;
import com.jm.sort.NewsAdapter;
import com.jm.util.ButtonsUtil;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

public class QingBaoUI_Haier extends Activity implements OnClickListener,
		OnScrollListener, OnItemClickListener {

	public LocationClient mLocationClient = null;

	// //////////////////////////////////
	private SharedPreferences share;
	private SharedPreferences.Editor editor;
	private ListView ListView;
	private NewsAdapter adapter;
	private List<News> mlist = new ArrayList<News>();
	private int page = 1;
	private int pageCount = 0;
	private String news_type = "4";
	private boolean isloading = false;
	private boolean showlast = false;
	private String pubtype = "2";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.qingbao_haier);
		init();

		share = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
		editor = share.edit();

		ListView = (ListView) findViewById(R.id.lv_faxingshi_listview);
		adapter = new NewsAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);
		ListView.setOnScrollListener(this);

		changeCondition("2", findViewById(R.id.btn_mingrentang));

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobileProbe.onResume(this, "发型师情报页面");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobileProbe.onPause(this, "发型师情报页面");
	}

	private void init() {
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_rightTop).setOnClickListener(this);
		findViewById(R.id.btn_mingrentang).setOnClickListener(this);
		findViewById(R.id.btn_hangyexinwen).setOnClickListener(this);
		findViewById(R.id.btn_mingdianzhanshi).setOnClickListener(this);
		findViewById(R.id.btn_shalongzhuanrang).setOnClickListener(this);
	}

	/*
	 * 读取发型师列表
	 */
	class GetHairListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			if (adapter.getUserList() == null
					|| adapter.getUserList().size() == 0) {
				adapter.setProgress(true);
				LogUtil.e("设置显示读取进度条");
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("news_type", news_type);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_ALL_NEWSLIST + "&page="
					+ page, getInfoInqVal());

		}

		protected void onPostExecute(Response result) {

			isloading = false;
			adapter.setProgress(false);
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				showlast = false;
				if (!"".equals(result.getString("userinfo"))) {
					pageCount = Integer
							.parseInt(result.getString("page_count"));
					mlist = result.getList("news_list", new News());
					if (mlist == null || mlist.size() == 0) {
						TispToastFactory.getToast(QingBaoUI_Haier.this,
								result.getMsg()).show();
						return;
					}
					adapter.appendNewsList(mlist);
				}
				adapter.notifyDataSetChanged();
			} else {
				TispToastFactory
						.getToast(QingBaoUI_Haier.this, result.getMsg()).show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		Map<String, String> map;
		switch (v.getId()) {
		case R.id.btn_mingrentang:
			changeCondition("2", v);
			break;
		case R.id.btn_hangyexinwen:
			changeCondition("3", v);
			break;
		case R.id.btn_mingdianzhanshi:
			changeCondition("1", v);
			break;
		case R.id.btn_shalongzhuanrang:

			StartActivityContController.goPage(this, ZhuanRangListUI.class,
					true);
			break;
		case R.id.btn_leftTop:
			finish();
			break;

		case R.id.btn_rightTop:
			map = new HashMap<String, String>();
			map.put("pubtype", pubtype);
			StartActivityContController.goPage(this, PublicNewsList.class,
					true, map);
		}
	}

	private void changeCondition(String condition, View v) {

		List<View> blist = new ArrayList<View>();
		blist.add(findViewById(R.id.btn_mingrentang));
		blist.add(findViewById(R.id.btn_hangyexinwen));
		blist.add(findViewById(R.id.btn_mingdianzhanshi));
		blist.add(findViewById(R.id.btn_shalongzhuanrang));
		ButtonsUtil.ResetAllButton(blist);
		ButtonsUtil.setChangeButton(v);
		pubtype = condition;
		adapter.clear();
		this.news_type = condition;
		page = 1;
		pageCount = 0;
		new GetHairListTask().execute();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (adapter.getUserList() == null || position < 0
				|| position >= adapter.getUserList().size()) {
			LogUtil.e("position = " + position);
			return;
		}
		News type = adapter.getUserList().get(position);
		Intent intent = new Intent(QingBaoUI_Haier.this, QingBaoInfo.class);
		intent.putExtra("nid", type.news_id);
		startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (showlast) {
			return;
		}
		if (totalItemCount - (firstVisibleItem + visibleItemCount) <= 6) {

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

	}

}
