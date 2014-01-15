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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.citylist.CityList;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Job;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.JobAdapter;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

public class JobListUI extends Activity implements OnClickListener,
		OnScrollListener, OnItemClickListener {
	private ListView ListView;
	private JobAdapter adapter;
	private List<Job> mlist = new ArrayList<Job>();
	private int page = 1;
	private int pageCount = 0;
	private boolean isloading = false;
	private boolean showlast = false;

	private static final String[] jobs = { "职位", "美发学徒", "美发助理", "发型师", "烫染技师",
			"技术总监", "美发店长" };
	private ArrayAdapter<String> jobs_spadapter;
	private Spinner sp_job;
	private String str_job = "";

	private String str_money = "";
	private static final String[] money = { "薪资", "0-1500", "1500-3000",
			"3000-4000", "4000-6000", "6000-8000", "8000-10000" };
	private ArrayAdapter<String> money_spadapter;
	private Spinner sp_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joblist);
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
			str_job = jobs[arg2];
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

		MobileProbe.onResume(this, "招聘列表");
		super.onResume();
		((Button) findViewById(R.id.btn_city)).setText(SessionManager
				.getInstance().getCity());
		getJobs();
	}

	@Override
	protected void onPause() {

		MobileProbe.onPause(this, "招聘列表");
		super.onPause();
	}

	private void init() {
		ListView = (ListView) findViewById(R.id.my_newslistview);
		ListView.setOnItemClickListener(this);
		adapter = new JobAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnScrollListener(this);

		findViewById(R.id.btn_rightTop).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_city).setOnClickListener(this);

	}

	/*
	 * 读取同城列表
	 */
	class GetJobsListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			if (mlist == null || mlist.size() == 0) {
				adapter.setProgress(true);
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			if (!"职位".equals(str_job)) {
				map.put("job", str_job);
			}
			if (!"薪资".equals(str_money)) {
				map.put("money", str_money);
			}
			map.put("city", SessionManager.getInstance().getCity());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(
					Constant.URN_JOB_LIST + "&page=" + page, getInfoInqVal());

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
				mlist = result.getList("jobs_list", new Job());
				if (mlist == null || mlist.size() == 0) {
					TispToastFactory.getToast(JobListUI.this, result.getMsg())
							.show();
					return;
				}
				showlast = false;
				adapter.appendJobList(mlist);
				adapter.notifyDataSetChanged();

			} else {
				TispToastFactory.getToast(JobListUI.this, result.getMsg())
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
			StartActivityContController.goPage(JobListUI.this,
					PublicJobUI.class, true);
			break;
		case R.id.btn_city:

			startActivity(new Intent(JobListUI.this, CityList.class));
			break;
		}
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
					new GetJobsListTask().execute();
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (adapter.getUserList() == null || position < 0
				|| position >= adapter.getUserList().size()) {
			LogUtil.e("position = " + position);
			return;
		}
		Job type = adapter.getUserList().get(position);
		Intent intent = new Intent(JobListUI.this, ZhaoPinInfoUI.class);
		intent.putExtra("sid", type.id);
		startActivity(intent);
	}
}
