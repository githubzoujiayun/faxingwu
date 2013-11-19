package com.jm.fxw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.CancelableCallback;
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
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

/**
 * AMapV2閸︽澘娴樻稉顓犵暆閸楁洑绮欑紒宄卬MapClickListener, OnMapLongClickListener,
 * OnCameraChangeListener娑撳顬岄惄鎴濇儔閸ｃ劎鏁ら敓锟�
 */

public class MapActivity extends FragmentActivity implements
		OnMapClickListener, OnCameraChangeListener, OnClickListener {
	private AMap aMap;
	private Marker marker;
	private SessionManager sm;
	private double lng = 0, lat = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events_demo);
		init();
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		sm = SessionManager.getInstance();
		CameraPosition MYLOCATION = new CameraPosition.Builder()
				.target(new LatLng(sm.getLat(), sm.getLng())).zoom(13).tilt(0)
				.build();
		if (AMapUtil.checkReady(this, aMap)) {
			changeCamera(CameraUpdateFactory.newCameraPosition(MYLOCATION));
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "选择坐标页面");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "选择坐标页面");
	}

	/**
	 * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
	 */
	private void changeCamera(CameraUpdate update, CancelableCallback callback) {
		aMap.moveCamera(update);
	}

	private void changeCamera(CameraUpdate update) {
		changeCamera(update, null);
	}

	/**
	 * 閸掓繂顬婇崠鏈匨ap鐎电钖�
	 */
	private void init() {
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		aMap.setOnMapClickListener(this);// 鐎电map濞ｈ濮為崡鏇炲毊閸︽澘娴樻禍瀣╂閻╂垵鎯夐敓锟� //
											// aMap.setOnMapLongClickListener(this);//
											// 鐎电map濞ｈ濮為梹鎸庡瘻閸︽澘娴樻禍瀣╂閻╂垵鎯夐敓锟�
											// //
											// aMap.setOnCameraChangeListener(this);//
											// 鐎电map濞ｈ濮炵粔璇插З閸︽澘娴樻禍瀣╂閻╂垵鎯夐敓锟�}
	}

	/**
	 * 鐎靛湱鍋ｉ崙璇叉勾閸ュ彞绨ㄦ禒璺烘惙閿燂拷
	 */
	@Override
	public void onMapClick(LatLng point) {
		aMap.clear();
		lng = point.longitude;
		lat = point.latitude;
		marker = aMap.addMarker(new MarkerOptions().position(point));
	}

	/**
	 * 鐎靛湱些閸斻劌婀撮崶鍙ョ皑娴犺泛鎼烽敓锟�
	 */
	@Override
	public void onCameraChange(final CameraPosition position) {
		LogUtil.e("onCameraChange:" + position.toString());
	}

	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
		LogUtil.e("onCameraChangeFinish:" + cameraPosition.toString());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			if (lat == 0 || lng == 0) {
				TispToastFactory.getToast(MapActivity.this, "请在您店铺位置上点击")
						.show();
			} else {
				Intent i = new Intent(MapActivity.this, RenZheng.class);
				i.putExtra("lng", lng);
				i.putExtra("lat", lat);
				setResult(RESULT_OK, i);
				finish();
			}
			break;

		default:
			break;
		}

	}
}
