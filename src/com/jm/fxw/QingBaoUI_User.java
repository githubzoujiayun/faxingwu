package com.jm.fxw;

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
import android.widget.ListView;

import com.baidu.location.LocationClient;
import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.News;
import com.jm.finals.Constant;
import com.jm.sort.NewsAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;
import com.jm.util.WidgetUtil;

public class QingBaoUI_User extends Activity implements OnClickListener,
		OnScrollListener, OnItemClickListener {

	public LocationClient mLocationClient = null;

	private ListView ListView;
	private NewsAdapter adapter;
	private List<News> mlist = new ArrayList<News>();
	private int page = 1;
	private int pageCount = 0;
	private String news_type = "4";
	private boolean isloading = false;
	private boolean showlast = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.qingbao_user);
		init();

		ListView = (ListView) findViewById(R.id.lv_faxingshi_listview);
		adapter = new NewsAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);
		ListView.setOnScrollListener(this);
		changeCondition("4", findViewById(R.id.btn_quanbu));

	}

	@Override
	protected void onResume() {
		MobileProbe.onResume(this, "用户情报站");
		super.onResume();

	}

	@Override
	protected void onPause() {

		MobileProbe.onPause(this, "用户情报站");
		super.onPause();
	}

	private void init() {
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_quanbu).setOnClickListener(this);
		findViewById(R.id.btn_tongcheng).setOnClickListener(this);
		findViewById(R.id.btn_tuijian).setOnClickListener(this);
		findViewById(R.id.btn_guanzhu).setOnClickListener(this);
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
						TispToastFactory.getToast(QingBaoUI_User.this,
								result.getMsg()).show();
						return;
					}
					adapter.appendNewsList(mlist);
				}
				adapter.notifyDataSetChanged();
			} else {
				TispToastFactory.getToast(QingBaoUI_User.this, result.getMsg())
						.show();
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_quanbu:
			changeCondition("4", v);
			break;
		case R.id.btn_tongcheng:
			changeCondition("5", v);
			break;
		case R.id.btn_tuijian:
			changeCondition("2", v);
			break;
		case R.id.btn_guanzhu:
			changeCondition("1", v);
			break;
		case R.id.btn_leftTop:
			finish();
			break;
		}
	}

	private void changeCondition(String condition, View v) {

		List<View> blist = new ArrayList<View>();
		blist.add(findViewById(R.id.btn_quanbu));
		blist.add(findViewById(R.id.btn_tongcheng));
		blist.add(findViewById(R.id.btn_tuijian));
		blist.add(findViewById(R.id.btn_guanzhu));
		WidgetUtil.ResetAllButton(blist);
		WidgetUtil.setChangeButton(v);
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
		Intent intent = new Intent(QingBaoUI_User.this, QingBaoInfo.class);
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
