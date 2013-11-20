package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jishuhuati);
		init();
		new GetTongchengListTask().execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "热点话题页面");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "热点话题页面");
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
				.setBackgroundResource(R.drawable.left_bg1);
		((Button) findViewById(R.id.btn_faxingshi)).setTextColor(Color.rgb(240,28,97));
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_rightTop).setOnClickListener(this);

	}

	private void ResetButtonBg() {

		((Button) findViewById(R.id.btn_faxingshi))
				.setBackgroundResource(R.drawable.left_bg);
		((Button) findViewById(R.id.btn_faxingshi)).setTextColor(Color.rgb(0,
				0, 0));
		((Button) findViewById(R.id.btn_geren))
				.setBackgroundResource(R.drawable.center_bg);
		((Button) findViewById(R.id.btn_geren))
				.setTextColor(Color.rgb(0, 0, 0));
		((Button) findViewById(R.id.btn_dianpu))
				.setBackgroundResource(R.drawable.right_bg);
		((Button) findViewById(R.id.btn_dianpu)).setTextColor(Color
				.rgb(0, 0, 0));
	}

	/*
	 * 读取同城列表
	 */
	class GetTongchengListTask extends AsyncTask<String, Integer, Response> {

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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_leftTop:
			// 打开分类
			this.finish();
			break;
		case R.id.btn_faxingshi:
			ResetButtonBg();
			v.setBackgroundResource(R.drawable.left_bg1);
			((Button) v).setTextColor(Color.rgb(240,28,97));
			adapter.clear();

			condition = "";
			page = 1;
			pageCount = 0;
			new GetTongchengListTask().execute();
			break;
		case R.id.btn_geren:
			ResetButtonBg();
			v.setBackgroundResource(R.drawable.center_bg1);
			((Button) v).setTextColor(Color.rgb(240,28,97));
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
			v.setBackgroundResource(R.drawable.right_bg1);
			((Button) v).setTextColor(Color.rgb(240,28,97));
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
					new GetTongchengListTask().execute();
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
		News type = adapter.getUserList().get(position);
		Intent intent = new Intent(JiShuHuaTiUI.this, HuaTiInfoUI.class);
		intent.putExtra("hid", type.news_id);
		startActivity(intent);
	}
}
