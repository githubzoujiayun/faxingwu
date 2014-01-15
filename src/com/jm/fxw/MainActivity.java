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
				mLocationClient = new LocationClient(getApplicationContext()); // ����LocationClient��
				mLocationClient.registerLocationListener(myListener); // ע���������
				mLocationClient.start();
				LocationClientOption option = new LocationClientOption();
				option.setOpenGps(true);
				option.setAddrType("all");// ���صĶ�λ���������ַ��Ϣ
				option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
				option.setScanSpan(0);// ���÷���λ����ļ��ʱ��Ϊ5000ms
				option.disableCache(true);// ��ֹ���û��涨λ
				option.setPoiNumber(5); // ��෵��POI����
				option.setPoiDistance(1000); // poi��ѯ����
				option.setPoiExtraInfo(false); // �Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ
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
							"��λʧ��:��ѡ�������ڵĳ���").show();
					startActivity(new Intent(MainActivity.this, CityList.class));
				}
			}
			new sendLocationTask().execute();

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/*
	 * �ϴ�λ����Ϣ
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
