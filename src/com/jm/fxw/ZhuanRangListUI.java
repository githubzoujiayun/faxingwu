package com.jm.fxw;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.ZhuanRang;
import com.jm.finals.Constant;
import com.jm.sort.ZhuanRangAdapter;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

public class ZhuanRangListUI extends Activity implements OnClickListener,
		OnScrollListener, OnItemClickListener {
	private ListView ListView;
	private ZhuanRangAdapter adapter;
	private List<ZhuanRang> mlist;
	private int page = 1;
	private int pageCount = 0;
	private boolean isloading = false;
	private boolean showlast = false;

	private static final String[] jobs = { "店面大小", "0-30m²", "30-50m²",
			"50-100m²", "100-200m²", "200-300m²", "300-400m²" };
	private ArrayAdapter<String> jobs_spadapter;
	private Spinner sp_job;
	private String str_job = "";

	private String str_money = "";
	private static final String[] money = { "装修风格", "欧美风格", "日韩风格", "简洁风格",
			"复古风格", "精品风格", "个性风格" };
	private ArrayAdapter<String> money_spadapter;
	private Spinner sp_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shalongzhuanranglist);
		init();

		sp_job = (Spinner) findViewById(R.id.sp_job);
		sp_money = (Spinner) findViewById(R.id.sp_money);
		// 将可选内容与ArrayAdapter连接起来
		jobs_spadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, jobs);

		money_spadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, money);
		// 设置下拉列表的风格
		jobs_spadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		money_spadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter 添加到spinner中
		sp_job.setAdapter(jobs_spadapter);

		sp_money.setAdapter(money_spadapter);

		// 设置默认值

		new GetJobsListTask().execute();
	}

	private void getJobs() {
		page = 1;
		pageCount = 0;
		isloading = false;
		showlast = false;
		adapter.clear();
		new GetJobsListTask().execute();
	}

	class JobSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			str_job = jobs[arg2].substring(0, jobs[arg2].length() - 2);
			getJobs();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class MoenySelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			str_money = money[arg2];
			getJobs();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobileProbe.onResume(this, "转让列表");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobileProbe.onPause(this, "转让列表");
	}

	private void init() {
		ListView = (ListView) findViewById(R.id.my_newslistview);
		ListView.setOnItemClickListener(this);
		adapter = new ZhuanRangAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnScrollListener(this);

		findViewById(R.id.btn_rightTop).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);

	}

	/*
	 * 读取同城列表
	 */
	class GetJobsListTask extends AsyncTask<String, Integer, Response> {

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
			if (!"店面大小".equals(str_job)) {
				map.put("acreage", str_job);
			}
			if (!"装修风格".equals(str_money)) {
				map.put("style", str_money);
			}
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_SHALONGZHUANRANGIST
					+ "&page=" + page, getInfoInqVal());

		}

		protected void onPostExecute(Response result) {

			isloading = false;
			adapter.setProgress(false);
			if (result == null) {
				return;
			}

			// 添加事件Spinner事件监听
			sp_job.setOnItemSelectedListener(new JobSelectedListener());
			sp_money.setOnItemSelectedListener(new MoenySelectedListener());
			if (result.isSuccessful()) {

				pageCount = Integer.parseInt(result.getString("page_count"));
				mlist = result.getList("transfer_list", new ZhuanRang());
				if (mlist == null || mlist.size() == 0) {
					TispToastFactory.getToast(ZhuanRangListUI.this,
							result.getMsg()).show();
					return;
				}
				showlast = false;
				adapter.appendZhuanRangList(mlist);
				adapter.notifyDataSetChanged();

			} else {
				TispToastFactory
						.getToast(ZhuanRangListUI.this, result.getMsg()).show();
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
		case R.id.btn_rightTop:
			StartActivityContController.goPage(ZhuanRangListUI.this,
					PublicZhuanRangUI.class, true);
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (showlast) {
			return;
		}
		if (totalItemCount - (firstVisibleItem + visibleItemCount) <= 6) {

			LogUtil.d("正在显示" + page + "页" + "总页数为" + pageCount + "页");

			if (page < pageCount) {
				if (!isloading) {
					LogUtil.d("即将加载" + ++page + "页");
					isloading = true;
					getJobs();
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (adapter.getUserList() == null || position < 0
				|| position >= adapter.getUserList().size()) {
			LogUtil.e("position = " + position);
			return;
		}
		ZhuanRang type = adapter.getUserList().get(position);
		Intent intent = new Intent(ZhuanRangListUI.this, ZhuanRangInfoUI.class);
		intent.putExtra("sid", type.id);
		startActivity(intent);
	}
}
