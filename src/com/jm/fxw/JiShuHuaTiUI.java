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
import android.widget.Button;
import android.widget.ListView;

import com.baidu.mobstat.StatService;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.News;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.NewsAdapter;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

public class JiShuHuaTiUI extends Activity implements OnClickListener,
		OnScrollListener, OnItemClickListener {
	private ListView ListView;
	private NewsAdapter adapter;
	private List<News> mlist = new ArrayList<News>();
	private int page = 1;
	private int pageCount = 0;
	private SessionManager sm;
	private boolean isloading = false;
	private boolean showlast = false;
	private String condition = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.jishuhuati);
		init();
		new GetTongchengListTask().execute();
	}

	@Override
	protected void onResume() {
		StatService.onResume(this);
		super.onResume();

	}

	@Override
	protected void onPause() {

		StatService.onPause(this);
		super.onPause();
	}

	private void init() {

		findViewById(R.id.btn_faxingshi).setOnClickListener(this);
		findViewById(R.id.btn_geren).setOnClickListener(this);
		findViewById(R.id.btn_dianpu).setOnClickListener(this);
		sm = SessionManager.getInstance();
		ListView = (ListView) findViewById(R.id.my_zhoubianlistview);
		ListView.setOnItemClickListener(this);
		adapter = new NewsAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnScrollListener(this);

		ResetButtonBg();
		((Button) findViewById(R.id.btn_faxingshi))
				.setTextColor(Constant.color_RoseRed);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_rightTop).setOnClickListener(this);

	}

	private void ResetButtonBg() {

		((Button) findViewById(R.id.btn_faxingshi))
				.setTextColor(Constant.color_Black);
		((Button) findViewById(R.id.btn_geren))
				.setTextColor(Constant.color_Black);
		((Button) findViewById(R.id.btn_dianpu))
				.setTextColor(Constant.color_Black);
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
			map.put("uid", sm.getUserId());
			map.put("condition", condition);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_SKILLLIST + "&page="
					+ page, getInfoInqVal());

		}

		protected void onPostExecute(Response result) {

			isloading = false;
			adapter.setProgress(false);
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {

				pageCount = Integer.parseInt(result.getString("page_count"));
				mlist = result.getList("skill_list", new News());
				if (mlist == null || mlist.size() == 0) {

					TispToastFactory.getToast(JiShuHuaTiUI.this,
							result.getMsg()).show();
					return;
				}
				showlast = false;
				adapter.appendNewsList(mlist);
				adapter.notifyDataSetChanged();

			} else {
				TispToastFactory.getToast(JiShuHuaTiUI.this, result.getMsg())
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
			ResetButtonBg();
			((Button) v).setTextColor(Constant.color_RoseRed);
			adapter.clear();

			condition = "";
			page = 1;
			pageCount = 0;
			new GetTongchengListTask().execute();
			break;
		case R.id.btn_geren:
			ResetButtonBg();
			((Button) v).setTextColor(Constant.color_RoseRed);
			adapter.clear();
			condition = "hot";
			page = 1;
			pageCount = 0;
			new GetTongchengListTask().execute();
			break;
		case R.id.btn_dianpu:
			adapter.clear();
			condition = "my";
			ResetButtonBg();
			((Button) v).setTextColor(Constant.color_RoseRed);
			page = 1;
			pageCount = 0;
			new GetTongchengListTask().execute();
			break;
		case R.id.btn_rightTop:
			StartActivityContController.goPage(JiShuHuaTiUI.this,
					PublicJiShuHuaTi.class, true);
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
					new GetTongchengListTask().execute();
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
		News type = adapter.getUserList().get(position);
		Intent intent = new Intent(JiShuHuaTiUI.this, HuaTiInfoUI.class);
		intent.putExtra("hid", type.news_id);
		startActivity(intent);
	}
}
