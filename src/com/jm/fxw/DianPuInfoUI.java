package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amapv2.cn.apis.util.AMapUtil;
import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.User;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.UserGalleryAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

public class DianPuInfoUI extends FragmentActivity implements
		OnMapClickListener, OnCameraChangeListener, OnClickListener,
		OnItemClickListener {

	private String did;
	private double lng, lat;

	private AMap aMap;
	private List<User> mlist = new ArrayList<User>();
	private UserGalleryAdapter adapter;
	private GridView ListView;
	private Marker marker;
	private SessionManager sm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dianpuinfo);
		sm = SessionManager.getInstance();
		init();
		ListView = (GridView) findViewById(R.id.faxingshi_hairgridview);
		adapter = new UserGalleryAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);

	}

	@Override
	protected void onPause() {

		super.onPause();
		MobileProbe.onPause(this, "店铺详情页面");
	}

	private void setUpMap() {
		aMap.setOnMapClickListener(this);
	}

	private void init() {
		did = getIntent().getStringExtra("uid");
		findViewById(R.id.btn_jibenxinxi).setOnClickListener(this);
		findViewById(R.id.btn_faxingshi).setOnClickListener(this);
		findViewById(R.id.btn_weizhi).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		ResetButtonBgAndViews();
		((Button) findViewById(R.id.btn_jibenxinxi)).setTextColor(Color.rgb(
				230, 61, 61));
		findViewById(R.id.lin_basic_info).setVisibility(View.VISIBLE);
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				setUpMap();
			}
		}

	}

	private void ResetButtonBgAndViews() {

		findViewById(R.id.lin_basic_info).setVisibility(View.GONE);
		findViewById(R.id.lin_hairer_list).setVisibility(View.GONE);
		findViewById(R.id.lin_location).setVisibility(View.GONE);
		((Button) findViewById(R.id.btn_jibenxinxi)).setTextColor(Color.rgb(0,
				0, 0));
		((Button) findViewById(R.id.btn_faxingshi)).setTextColor(Color.rgb(0,
				0, 0));
		((Button) findViewById(R.id.btn_weizhi)).setTextColor(Color
				.rgb(0, 0, 0));
	}

	@Override
	protected void onResume() {

		super.onResume();
		MobileProbe.onResume(this, "店铺详情页面");
		new getShaLongInfo().execute();
		new getHairListInfo().execute();
	}

	class getShaLongInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", did);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_NEAR_STORE,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get ShaLongInfo");
				return;
			}
			if (result.isSuccessful()) {
				try {

					((TextView) findViewById(R.id.tv_dname)).setText(result
							.getString("store_name"));
					((TextView) findViewById(R.id.tv_dphone)).setText(result
							.getString("telephone"));
					((TextView) findViewById(R.id.tv_daddress)).setText(result
							.getString("store_address"));
					lng = result.getDouble("lng");
					lat = result.getDouble("lat");
				} catch (Exception e) {
					// TODO: handle exception
				}

				CameraPosition MYLOCATION = new CameraPosition.Builder()
						.target(new LatLng(lat, lng)).zoom(14).tilt(0).build();
				if (AMapUtil.checkReady(DianPuInfoUI.this, aMap)) {
					changeCamera(CameraUpdateFactory
							.newCameraPosition(MYLOCATION));
					aMap.clear();
					marker = aMap.addMarker(new MarkerOptions()
							.position(new LatLng(lat, lng)));
				}
			}
		}
	}

	private void changeCamera(CameraUpdate update) {
		aMap.moveCamera(update);
	}

	class getHairListInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", did);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_NEAR_STOREHAIRLIST,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get ShaLongInfo");
				return;
			}
			if (result.isSuccessful()) {
				if (!"".equals(result.getString("hairstylist_info"))) {
					mlist = result.getList("hairstylist_info", new User());
					adapter.setTypeList(mlist);
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			this.finish();
			break;
		case R.id.btn_jibenxinxi:
			ResetButtonBgAndViews();
			((Button) v).setTextColor(Color.rgb(230, 61, 61));
			findViewById(R.id.lin_basic_info).setVisibility(View.VISIBLE);
			break;
		case R.id.btn_faxingshi:
			ResetButtonBgAndViews();
			((Button) v).setTextColor(Color.rgb(230, 61, 61));
			findViewById(R.id.lin_hairer_list).setVisibility(View.VISIBLE);
			break;
		case R.id.btn_weizhi:
			if (lng == 0 || lat == 0) {
				TispToastFactory.getToast(DianPuInfoUI.this,
						"閺嗗倹妫ゆ惔妤呮懙閸︽澘娼冩穱鈩冧紖").show();
				return;
			}
			ResetButtonBgAndViews();
			((Button) v).setTextColor(Color.rgb(230, 61, 61));
			findViewById(R.id.lin_location).setVisibility(View.VISIBLE);
			break;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (adapter.getUserList() == null || position < 0
				|| position >= adapter.getUserList().size()) {
			LogUtil.e("position = " + position);
			return;
		}
		User user = adapter.getUserList().get(position);
		if (!user.getUid().equals(sm.getUserId())) {

			Intent intent = new Intent(DianPuInfoUI.this, HisInfoUI.class);
			intent.putExtra("uid", user.getUid());
			intent.putExtra("type", "2");
			startActivity(intent);
		}
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {

	}

	@Override
	public void onCameraChangeFinish(CameraPosition arg0) {

	}

	@Override
	public void onMapClick(LatLng arg0) {

	}

}
