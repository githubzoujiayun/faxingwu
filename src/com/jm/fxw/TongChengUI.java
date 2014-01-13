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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.ZhouBian;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.ZhouBianAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;
import com.jm.util.WidgetUtil;

public class TongChengUI extends Activity implements OnClickListener,
		OnScrollListener, OnItemClickListener {
	private ListView ListView;
	private ZhouBianAdapter adapter;
	private List<ZhouBian> mlist = new ArrayList<ZhouBian>();
	private int page = 1;
	private int pageCount = 0;
	private boolean isloading = false;
	private boolean showlast = false;
	private String condition = "2";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tongchenglist);
		init();
		changeCondition("2", findViewById(R.id.btn_faxingshi));
	}



	private void init() {

		findViewById(R.id.btn_faxingshi).setOnClickListener(this);
		findViewById(R.id.btn_geren).setOnClickListener(this);
		findViewById(R.id.btn_search).setOnClickListener(this);
		ListView = (ListView) findViewById(R.id.my_zhoubianlistview);
		adapter = new ZhouBianAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnScrollListener(this);
		ListView.setOnItemClickListener(this);
		((TextView) findViewById(R.id.tv_rightTop)).setText(SessionManager
				.getInstance().getCity());
		findViewById(R.id.btn_leftTop).setOnClickListener(this);

	}

	/*
	 * 读取同城列表
	 */
	class GetTongchengListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			if (mlist == null || mlist.size() == 0) {
				adapter.setProgress(true);
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", SessionManager.getInstance().getUserId());
			map.put("city", SessionManager.getInstance().getCity());
			map.put("type", condition);
			map.put("lng", SessionManager.getInstance().getLng());
			map.put("lat", SessionManager.getInstance().getLat());
			map.put("search", ((EditText) findViewById(R.id.et_keys)).getText()
					.toString().trim());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_NEAR + "&page=" + page,
					getInfoInqVal());

		}

		protected void onPostExecute(Response result) {

			isloading = false;
			adapter.setProgress(false);
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {

				pageCount = Integer.parseInt(result.getString("page_count"));
				mlist = result.getList("user_info", new ZhouBian());
				if (mlist == null || mlist.size() == 0) {
					TispToastFactory
							.getToast(TongChengUI.this, result.getMsg()).show();
					return;
				}
				showlast = false;
				adapter.appendUserList(mlist);
				adapter.notifyDataSetChanged();

			} else {
				TispToastFactory.getToast(TongChengUI.this, result.getMsg())
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
		case R.id.btn_faxingshi:
			changeCondition("2", v);
			break;
		case R.id.btn_geren:
			changeCondition("1", v);
			break;

		case R.id.btn_search:
			adapter.clear();
			page = 1;
			pageCount = 0;
			new GetTongchengListTask().execute();
			break;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (adapter.getUserList() == null || position < 0
				|| position >= adapter.getUserList().size()) {
			LogUtil.e("position = " + position);
			return;
		}
		ZhouBian user = adapter.getUserList().get(position);
		Intent intent = new Intent(TongChengUI.this, HisInfoUI.class);
		intent.putExtra("uid", user.uid);
		intent.putExtra("type", user.type);
		startActivity(intent);

	}

	private void changeCondition(String s, View v) {
		List<View> blist = new ArrayList<View>();
		blist.add(findViewById(R.id.btn_faxingshi));
		blist.add(findViewById(R.id.btn_geren));
		WidgetUtil.ResetAllButton(blist);
		WidgetUtil.setChangeButton(v);
		adapter.clear();
		condition = s;
		page = 1;
		pageCount = 0;
		new GetTongchengListTask().execute();
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
					new GetTongchengListTask().execute();
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
