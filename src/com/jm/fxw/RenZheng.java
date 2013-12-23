package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

public class RenZheng extends FinalActivity {
	@ViewInject(id = R.id.btn_getLocation, click = "Click")
	Button btn_getLocation;

	@ViewInject(id = R.id.btn_leftTop, click = "Click")
	Button btn_leftTop;
	@ViewInject(id = R.id.btn_renzheng, click = "Click")
	Button btn_renzheng;
	private SessionManager sm;

//	private double lng = 0, lat = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.renzheng);
		sm = SessionManager.getInstance();
		new getShaLongInfo().execute();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		MobileProbe.onResume(this, "店铺认证页面");
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		MobileProbe.onPause(this, "店铺认证页面");
	}

	/*
	 * 读取店铺信息
	 */
	class getShaLongInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_STORE_INFO,
					getInfoInqVal());

		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {
				JSONObject jb = result.getJsonString("store_info");
				try {
					((EditText) findViewById(R.id.tv_dname)).setText(jb
							.getString("store_name"));
					((EditText) findViewById(R.id.tv_dphone)).setText(jb
							.getString("telephone"));
					((EditText) findViewById(R.id.tv_daddress)).setText(jb
							.getString("store_address"));
//					lng = jb.getDouble("lng");
//					lat = jb.getDouble("lat");
				} catch (JSONException e) {

				}

			}
		}
	}

	/*
	 * 设置店铺信息
	 */
	class setStoreInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("city", sm.getCity());
			map.put("store_name", ((EditText) findViewById(R.id.tv_dname))
					.getText().toString().trim());
			map.put("address", ((EditText) findViewById(R.id.tv_daddress))
					.getText().toString().trim());
			map.put("telephone", ((EditText) findViewById(R.id.tv_dphone))
					.getText().toString().trim());
//			map.put("lng", lng);
//			map.put("lat", lat);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_CHANGE_STORE,
					getInfoInqVal());

		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {
				TispToastFactory.getToast(RenZheng.this, result.getMsg())
						.show();
				finish();

			} else {
				TispToastFactory.getToast(RenZheng.this, result.getMsg())
						.show();
			}
		}
	}

	public void Click(View v) {
		switch (v.getId()) {
//		case R.id.btn_getLocation:
//
//			Intent i = new Intent(RenZheng.this, MapActivity.class);
//			startActivityForResult(i, 101);
//			break;
		case R.id.btn_leftTop:
			finish();
			break;
		case R.id.btn_renzheng:
			if (checkValue()) {
				new setStoreInfo().execute();
			}
			break;
		default:
			break;
		}
	}

	private boolean checkValue() {
		if (((EditText) findViewById(R.id.tv_dname)).getText().toString()
				.trim().equals("")) {
			TispToastFactory.getToast(RenZheng.this, "请输入店铺名称").show();
			return false;

		}
		if (((EditText) findViewById(R.id.tv_dphone)).getText().toString()
				.trim().equals("")) {
			TispToastFactory.getToast(RenZheng.this, "请输入店铺电话").show();
			return false;
		}
		if (((EditText) findViewById(R.id.tv_daddress)).getText().toString()
				.trim().equals("")) {
			TispToastFactory.getToast(RenZheng.this, "请输入店铺详细地址").show();
			return false;
		}
//		if (lng == 0 || lat == 0) {
//			TispToastFactory.getToast(RenZheng.this, "请标记店铺具体位置").show();
//			return false;
//		}
		return true;
	}
}
