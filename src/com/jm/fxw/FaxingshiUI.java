package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.jm.util.WidgetUtil;

public class FaxingshiUI extends FinalActivity implements OnClickListener,
		OnScrollListener, OnItemClickListener {
	private ListView ListView;
	private FaXingShiAdapter adapter;
	private List<FaXingShi> mlist = new ArrayList<FaXingShi>();
	private int page = 1;
	private int pageCount = 0;
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
			StartActivityContController.goPage(this, GuangChangUI.class, true);
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
		findViewById(R.id.btn_xijianchui).setOnClickListener(this);
		findViewById(R.id.btn_tangfa).setOnClickListener(this);
		findViewById(R.id.btn_ranfa).setOnClickListener(this);
		findViewById(R.id.btn_huli).setOnClickListener(this);

		findViewById(R.id.btn_location).setOnClickListener(this);

		ListView = (ListView) findViewById(R.id.lv_faxingshi_listview);
		adapter = new FaXingShiAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);
		ListView.setOnScrollListener(this);
		changeCondition("xijianchui", findViewById(R.id.btn_xijianchui));
		new GetHairListTask().execute();
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
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", SessionManager.getInstance().getUserId());
			map.put("city", SessionManager.getInstance().getCity());
			map.put("lng", SessionManager.getInstance().getLng());
			map.put("lat", SessionManager.getInstance().getLat());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_ALL_FAXINGSHI + "&page="
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
				if (!"".equals(result.getString("user_info"))) {
					pageCount = Integer
							.parseInt(result.getString("page_count"));
					mlist = result.getList("price_list", new FaXingShi());
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
		case R.id.btn_xijianchui:
			changeCondition("xijianchui", v);
			break;
		case R.id.btn_tangfa:
			changeCondition("tangfa", v);
			break;
		case R.id.btn_ranfa:
			changeCondition("ranfa", v);
			break;
		case R.id.btn_huli:
			changeCondition("huli", v);

			break;
		case R.id.btn_location:
			startActivity(new Intent(FaxingshiUI.this, CityList.class));
			break;
		}
	}

	private void changeCondition(String pricetype, View v) {
		List<View> blist = new ArrayList<View>();
		blist.add(findViewById(R.id.btn_xijianchui));
		blist.add(findViewById(R.id.btn_tangfa));
		blist.add(findViewById(R.id.btn_ranfa));
		blist.add(findViewById(R.id.btn_huli));
		WidgetUtil.ResetAllButton(blist);
		WidgetUtil.setChangeButton(v);
		adapter.priceType = pricetype;
		adapter.notifyDataSetChanged();
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
		intent.putExtra("uid", user.uid);
		intent.putExtra("type", "2");
		startActivity(intent);

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
