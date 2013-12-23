package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.ZhouBian;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.ZhouBianAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

public class TongChengUI extends Activity implements OnClickListener,
		OnScrollListener {
	private ListView ListView;
	private ZhouBianAdapter adapter;
	private List<ZhouBian> mlist = new ArrayList<ZhouBian>();
	private int page = 1;
	private int pageCount = 0;
	private SessionManager sm;
	private boolean isloading = false;
	private boolean showlast = false;
	private String condition = "hairstylist";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tongchenglist);
		init();
		new GetTongchengListTask().execute();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		MobileProbe.onResume(this, "周边页面");
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		MobileProbe.onPause(this, "周边页面");
	}

	private void init() {
	
		findViewById(R.id.btn_faxingshi).setOnClickListener(this);
		findViewById(R.id.btn_geren).setOnClickListener(this);
		findViewById(R.id.btn_dianpu).setOnClickListener(this);
		findViewById(R.id.btn_search).setOnClickListener(this);
		sm = SessionManager.getInstance();
		ListView = (ListView) findViewById(R.id.my_zhoubianlistview);
		adapter = new ZhouBianAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnScrollListener(this);

		ResetButtonBg();
		((Button) findViewById(R.id.btn_faxingshi))
				.setBackgroundResource(R.drawable.left_bg1);
		((Button) findViewById(R.id.btn_faxingshi)).setTextColor(Color.rgb(240,
				28, 97));
		((TextView) findViewById(R.id.tv_rightTop)).setText(sm.getCity());
		findViewById(R.id.btn_leftTop).setOnClickListener(this);

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
			
			super.onPreExecute();
			if (mlist == null || mlist.size() == 0) {
				adapter.setProgress(true);
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("city", sm.getCity());
			map.put("condition", condition);
			map.put("keys", ((EditText) findViewById(R.id.et_keys)).getText()
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
				mlist = result.getList("list", new ZhouBian());
				if (mlist == null || mlist.size() == 0) {
					TispToastFactory
							.getToast(TongChengUI.this, result.getMsg());
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
			ResetButtonBg();
			v.setBackgroundResource(R.drawable.left_bg1);
			((Button) v).setTextColor(Color.rgb(240, 28, 97));
			adapter.clear();

			adapter.isDianPu = false;
			condition = "hairstylist";
			page = 1;
			pageCount = 0;
			new GetTongchengListTask().execute();
			break;
		case R.id.btn_geren:
			ResetButtonBg();
			v.setBackgroundResource(R.drawable.center_bg1);
			((Button) v).setTextColor(Color.rgb(240, 28, 97));
			adapter.clear();
			adapter.isDianPu = false;
			condition = "person";
			page = 1;
			pageCount = 0;
			new GetTongchengListTask().execute();
			break;
		case R.id.btn_dianpu:
			adapter.clear();
			adapter.isDianPu = true;
			condition = "store";
			ResetButtonBg();
			v.setBackgroundResource(R.drawable.right_bg1);
			((Button) v).setTextColor(Color.rgb(240, 28, 97));
			page = 1;
			pageCount = 0;
			new GetTongchengListTask().execute();
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
