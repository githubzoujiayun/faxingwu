package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.citylist.CityList;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.FaXingShi;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.FaXingShiAdapter;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

public class FaxingshiUI extends FinalActivity implements OnClickListener,
		OnScrollListener, OnItemClickListener {

	public LocationClient mLocationClient = null;

	// //////////////////////////////////
	private SharedPreferences share;
	private SharedPreferences.Editor editor;
	private ListView ListView;
	private FaXingShiAdapter adapter;
	private List<FaXingShi> mlist = new ArrayList<FaXingShi>();
	private int page = 1;
	private int pageCount = 0;
	private String condition = Constant.URN_ALL_FAXINGSHI;
	private SessionManager sm;
	private boolean isloading = false;
	private boolean showlast = false;
	@ViewInject(id = R.id.lin_guangchang, click = "Click")
	LinearLayout lin_guangchang;
	@ViewInject(id = R.id.lin_zhaofaxing, click = "Click")
	LinearLayout lin_zhaofaxing;
	@ViewInject(id = R.id.lin_faxingshi, click = "Click")
	LinearLayout lin_faxingshi;
	@ViewInject(id = R.id.lin_wode, click = "Click")
	LinearLayout lin_wode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faxingshi);
		init();
		sm = SessionManager.getInstance();
		share = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
		editor = share.edit();

		ListView = (ListView) findViewById(R.id.lv_faxingshi_listview);
		adapter = new FaXingShiAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);
		ListView.setOnScrollListener(this);
		new GetHairListTask().execute();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		((Button) findViewById(R.id.btn_location)).setText(SessionManager
				.getInstance().getCity());
		MobileProbe.onResume(this, "发型师页面");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "发型师页面");
	}

	public void Click(View v) {
		switch (v.getId()) {
		case R.id.lin_guangchang:
			StartActivityContController.goPage(this, GuangChangUI.class, false);
			break;
		case R.id.lin_zhaofaxing:
			StartActivityContController.goPage(this, ZhaofaxingUI.class, false);
			break;
		case R.id.lin_faxingshi:
			StartActivityContController.goPage(this, FaxingshiUI.class, false);
			break;
		case R.id.lin_wode:
			StartActivityContController.goPage(this,
					StartActivityContController.wode);
			break;
		}
	}

	private void setInco() {
		// TODO Auto-generated method stub
		findViewById(R.id.iv_faxingshi).setBackgroundResource(
				R.drawable.faxingshi1);
		((TextView) findViewById(R.id.tv_faxingshi)).setTextColor(Color
				.parseColor("#f01c61"));
	}

	private void init() {
		setInco();
		findViewById(R.id.btn_quanbu).setOnClickListener(this);
		findViewById(R.id.btn_tongcheng).setOnClickListener(this);
		findViewById(R.id.btn_tuijian).setOnClickListener(this);
		findViewById(R.id.btn_guanzhu).setOnClickListener(this);

		findViewById(R.id.btn_location).setOnClickListener(this);
		ResetButtonBg();
		((Button) findViewById(R.id.btn_quanbu))
				.setBackgroundResource(R.drawable.left_bg1);

		((Button) findViewById(R.id.btn_quanbu)).setTextColor(Color.rgb(240,
				28, 97));
	}

	/*
	 * 读取发型师列表
	 */
	class GetHairListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (adapter.getUserList() == null
					|| adapter.getUserList().size() == 0) {
				adapter.setProgress(true);
				LogUtil.e("设置显示读取进度条");
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("city", sm.getCity());
			map.put("lng", sm.getLng());
			map.put("lat", sm.getLat());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			if (condition.equals(Constant.URN_ALL_FAXINGSHI)) {

				return conn.executeAndParse(condition + "&page=" + page,
						getInfoInqVal());
			} else if (condition.equals(Constant.URN_TONGCHENG_FAXINGSHI)) {

				return conn.executeAndParse(condition + "&page=" + page,
						getInfoInqVal());
			} else if (condition.equals(Constant.URN_TUIJIAN_FAXINGSHI)) {

				return conn.executeAndParse(condition + "&page=" + page,
						getInfoInqVal());
			} else {
				// 这里是关注的发型师列表
				return conn.executeAndParse(condition + "&page=" + page,
						getInfoInqVal());
			}

		}

		protected void onPostExecute(Response result) {

			isloading = false;
			adapter.setProgress(false);
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				showlast = false;
				if (!"".equals(result.getString("user_info"))) {
					pageCount = Integer
							.parseInt(result.getString("page_count"));
					mlist = result.getList("user_info", new FaXingShi());
					if (mlist == null || mlist.size() == 0) {
						TispToastFactory.getToast(FaxingshiUI.this, "暂无数据")
								.show();
					}
					adapter.appendUserList(mlist);
				} else {
					TispToastFactory.getToast(FaxingshiUI.this, "暂无数据").show();
				}
				adapter.notifyDataSetChanged();
			} else {
				TispToastFactory.getToast(FaxingshiUI.this, result.getMsg())
						.show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_quanbu:
			changeCondition(Constant.URN_ALL_FAXINGSHI, v);
			break;
		case R.id.btn_tongcheng:
			changeCondition(Constant.URN_TONGCHENG_FAXINGSHI, v);
			break;
		case R.id.btn_tuijian:
			changeCondition(Constant.URN_TUIJIAN_FAXINGSHI, v);
			break;
		case R.id.btn_guanzhu:
			if (sm.isLogin()) {
				changeCondition(Constant.URN_GUANZHU_FAXINGSHI, v);
			} else {
				StartActivityContController.goPage(this, LoginUI.class, true);
			}
			break;
		case R.id.btn_location:
			startActivity(new Intent(FaxingshiUI.this, CityList.class));
			break;
		}
	}

	private void changeCondition(String condition, View v) {
		ResetButtonBg();
		v.setBackgroundResource(R.drawable.left_bg1);
		((Button) v).setTextColor(Color.rgb(240, 28, 97));
		adapter.clear();
		this.condition = condition;
		page = 1;
		pageCount = 0;
		new GetHairListTask().execute();
	}

	private void ResetButtonBg() {

		((Button) findViewById(R.id.btn_quanbu)).setTextColor(Color
				.rgb(0, 0, 0));
		((Button) findViewById(R.id.btn_tongcheng)).setTextColor(Color.rgb(0,
				0, 0));
		((Button) findViewById(R.id.btn_tuijian)).setTextColor(Color.rgb(0, 0,
				0));
		((Button) findViewById(R.id.btn_guanzhu)).setTextColor(Color.rgb(0, 0,
				0));

		findViewById(R.id.btn_quanbu).setBackgroundResource(R.drawable.left_bg);
		findViewById(R.id.btn_tongcheng).setBackgroundResource(
				R.drawable.center_bg);
		findViewById(R.id.btn_tuijian).setBackgroundResource(
				R.drawable.center_bg);
		findViewById(R.id.btn_guanzhu).setBackgroundResource(
				R.drawable.right_bg);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (adapter.getUserList() == null || position < 0
				|| position >= adapter.getUserList().size()) {
			LogUtil.e("position = " + position);
			return;
		}
		FaXingShi user = adapter.getUserList().get(position);
		Intent intent = new Intent(FaxingshiUI.this, HisInfoUI.class);
		if (!user.id.equals(sm.getUserId())) {
			intent.putExtra("uid", user.id);
			intent.putExtra("type", "2");
			startActivity(intent);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub{
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
		// TODO Auto-generated method stub

	}

}
