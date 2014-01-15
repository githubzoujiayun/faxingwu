package com.jm.fxw;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mobstat.StatService;
import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.citylist.CityList;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.service.PushService;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

public class MainActivity extends Activity {

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private ImageView imageView;

	private SessionManager sm;
	// //////////////////////////////////
	private SharedPreferences share;
	private SharedPreferences.Editor editor;

	// /////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sm = SessionManager.getInstance();
		setContentView(R.layout.activity_main);

		ClientApp app = (ClientApp) this.getApplication();
		app.setRootpath(Environment.getExternalStorageDirectory().getPath()
				+ File.separator + "fxw" + File.separator);
		imageView = (ImageView) findViewById(R.id.start_logo);
		AlphaAnimation animation = new AlphaAnimation(1, 1);
		imageView.startAnimation(animation);

		animation.setDuration(50);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				share = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
				editor = share.edit();
				mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
				mLocationClient.registerLocationListener(myListener); // 注册监听函数
				mLocationClient.start();
				LocationClientOption option = new LocationClientOption();
				option.setOpenGps(true);
				option.setAddrType("all");// 返回的定位结果包含地址信息
				option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
				option.setScanSpan(0);// 设置发起定位请求的间隔时间为5000ms
				option.disableCache(true);// 禁止启用缓存定位
				option.setPoiNumber(5); // 最多返回POI个数
				option.setPoiDistance(1000); // poi查询距离
				option.setPoiExtraInfo(false); // 是否需要POI的电话和地址等详细信息
				mLocationClient.setLocOption(option);
				if (mLocationClient != null && mLocationClient.isStarted()) {
					mLocationClient.requestLocation();
				} else {
					LogUtil.d("locClient is null or not started");
				}

				StartApp();
			}
		});

	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			if (location.getAddrStr() != null) {
				sm.setCity(location.getCity());
				sm.setLat(location.getLatitude());
				sm.setLng(location.getLongitude());
				editor.putString("city", location.getCity());
				editor.putString("lng", location.getLongitude() + "");
				editor.putString("lat", location.getLatitude() + "");
				editor.commit();

			} else {
				if (sm.getCity().equals("")) {
					TispToastFactory.getToast(MainActivity.this,
							"定位失败:请选择您所在的城市").show();
					startActivity(new Intent(MainActivity.this, CityList.class));
				}
			}
			new sendLocationTask().execute();

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/*
	 * 上传位置信息
	 */

	class sendLocationTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getMsgInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("lng", sm.getLng());
			map.put("lat", sm.getLat());
			map.put("city", share.getString("city", ""));
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_SENDLOCATION,
					getMsgInqVal());

		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				LogUtil.i("send location success, Lng = " + sm.getLng() + "Lat"
						+ sm.getLat());

			}
		}
	}

	private void StartApp() {
		Intent it = new Intent(this, PushService.class);
		startService(it);
		Intent intent = new Intent(MainActivity.this, ZhaofaxingUI.class);
		startActivity(intent);
		finish();
	}

}
