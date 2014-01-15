package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

/**
 * Android实现日历控件
 * 
 * @Description: Android实现日历控件
 * 
 * @File: MainActivity.java
 * 
 * @Package com.calendar.demo
 * 
 * @Author Hanyonglu
 * 
 * @Date 2012-6-21 上午11:42:32
 * 
 * @Version V1.0
 */
public class YuYueSheZhi extends FinalActivity implements OnClickListener {

	private SessionManager sm;
	private String[] discount = { "9.5", "9", "8.5", "8", "7.5", "7", "6.5",
			"6", "5.5", "5", "4.5", "4", "3.5", "3", "2.5", "2", "1.5", "1",
			"0.5" };

	ArrayAdapter<String> adapter;
	private Spinner sp;
	private String[] price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyueshezhi);
		init();
		add_list();
	}

	@Override
	protected void onResume() {
		MobileProbe.onResume(this, "设置预约信息");
		super.onResume();

		new getTipsInfo().execute();
		new getPriceInfo().execute();
	}

	@Override
	protected void onPause() {

		MobileProbe.onPause(this, "设置预约信息");
		super.onPause();
	}

	private void add_list() {
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, discount);

		sp = (Spinner) findViewById(R.id.sp_discount1);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		sp.setSelection(0);

		sp = (Spinner) findViewById(R.id.sp_discount2);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		sp.setSelection(0);

		sp = (Spinner) findViewById(R.id.sp_discount3);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		sp.setSelection(0);

		sp = (Spinner) findViewById(R.id.sp_discount4);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		sp.setSelection(0);
	}

	private void save() {
		new setTipsInfo().execute();
	}

	private void init() {
		sm = SessionManager.getInstance();
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_rightTop).setOnClickListener(this);

	}

	/*
	 * 读取提示信息
	 */
	class getTipsInfo extends AsyncTask<String, Integer, Response> {

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
			return conn
					.executeAndParse(Constant.URN_TIPS_INFO, getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {
				try {
					JSONArray alist = result.getJsonString("notice_info")
							.getJSONArray("info");
					((EditText) findViewById(R.id.tv_tip1))
							.setText((CharSequence) alist.get(0));

					((EditText) findViewById(R.id.tv_tip2))
							.setText((CharSequence) alist.get(1));
					((EditText) findViewById(R.id.tv_tip3))
							.setText((CharSequence) alist.get(2));
					((EditText) findViewById(R.id.tv_tip4))
							.setText((CharSequence) alist.get(3));
					((EditText) findViewById(R.id.tv_tip5))
							.setText((CharSequence) alist.get(4));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * 读取价格信息
	 */
	class getPriceInfo extends AsyncTask<String, Integer, Response> {

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
			return conn.executeAndParse(Constant.URN_PRICE_INFO,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {
				try {
					price = result.getString("price_info").split("_");

					((EditText) findViewById(R.id.et_price1)).setText(price[0]);
					((EditText) findViewById(R.id.et_price2)).setText(price[1]);
					((EditText) findViewById(R.id.et_price3)).setText(price[2]);
					((EditText) findViewById(R.id.et_price4)).setText(price[3]);

					((Spinner) findViewById(R.id.sp_discount1))
							.setSelection(adapter.getPosition(price[4]));
					((Spinner) findViewById(R.id.sp_discount2))
							.setSelection(adapter.getPosition(price[5]));
					((Spinner) findViewById(R.id.sp_discount3))
							.setSelection(adapter.getPosition(price[6]));
					((Spinner) findViewById(R.id.sp_discount4))
							.setSelection(adapter.getPosition(price[7]));
				} catch (Exception e) {
					LogUtil.e("getPriceInfo = " + e.toString());
				}

			}
		}
	}

	/*
	 * 修改提示信息
	 */
	class setTipsInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());

			map.put("tip1", (((EditText) findViewById(R.id.tv_tip1)).getText()
					.toString().trim()));
			map.put("tip2", (((EditText) findViewById(R.id.tv_tip2)).getText()
					.toString().trim()));
			map.put("tip3", (((EditText) findViewById(R.id.tv_tip3)).getText()
					.toString().trim()));
			map.put("tip4", (((EditText) findViewById(R.id.tv_tip4)).getText()
					.toString().trim()));
			map.put("tip5", (((EditText) findViewById(R.id.tv_tip5)).getText()
					.toString().trim()));
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_SETTIPS_INFO,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {

				new setPriceInfo().execute();
			} else {
				TispToastFactory.getToast(YuYueSheZhi.this, result.getMsg())
						.show();
			}
		}
	}

	/*
	 * 修改价格信息
	 */
	class setPriceInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("price_info", getPirce());
			return map;
		}

		private String getPirce() {
			StringBuffer sb = new StringBuffer();
			sb.append(((EditText) findViewById(R.id.et_price1)).getText()
					.toString().trim()
					+ "_");
			sb.append(((EditText) findViewById(R.id.et_price2)).getText()
					.toString().trim()
					+ "_");
			sb.append(((EditText) findViewById(R.id.et_price3)).getText()
					.toString().trim()
					+ "_");
			sb.append(((EditText) findViewById(R.id.et_price4)).getText()
					.toString().trim()
					+ "_");
			sb.append(((Spinner) findViewById(R.id.sp_discount1))
					.getSelectedItem().toString().trim()
					+ "_");
			sb.append(((Spinner) findViewById(R.id.sp_discount2))
					.getSelectedItem().toString().trim()
					+ "_");
			sb.append(((Spinner) findViewById(R.id.sp_discount3))
					.getSelectedItem().toString().trim()
					+ "_");
			sb.append(((Spinner) findViewById(R.id.sp_discount4))
					.getSelectedItem().toString().trim());
			return sb.toString();
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_SETPRICE_INFO,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				TispToastFactory.getToast(YuYueSheZhi.this, "修改成功").show();
				finish();
			} else {
				TispToastFactory.getToast(YuYueSheZhi.this, result.getMsg())
						.show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			finish();
			break;
		case R.id.btn_rightTop:
			save();
			break;
		}
	}
}