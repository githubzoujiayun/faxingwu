package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;
import com.nostra13.universalimageloader.core.ImageLoader;

public class YuYueCheck extends FinalActivity implements OnClickListener {

	@ViewInject(id = R.id.btn_leftTop, click = "Click")
	Button btn_leftTop;
	private String tid;
	private String discount;
	private String date;
	private String week;
	private String type;
	private String time;
	private SessionManager sm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyuecheck);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobileProbe.onResume(this, "预约确认页面");
		getUserInfo();
	}

	private void getUserInfo() {
		new getUserInfo().execute();
		new getUserPhone().execute();

	}

	private void init() {
		sm = SessionManager.getInstance();
		Intent i = getIntent();
		tid = i.getStringExtra("tid");
		if (tid == null || tid.equals("")) {
			LogUtil.e("tid = " + tid);
			finish();
		}
		date = i.getStringExtra("date");
		week = i.getStringExtra("week");
		discount = i.getStringExtra("discount");
		type = i.getStringExtra("type");
		time = i.getStringExtra("time");
		((TextView) findViewById(R.id.tv_date)).setText(date.split("-")[0]
				+ "月" + date.split("-")[1] + "日" + "(" + week + ")");
		((TextView) findViewById(R.id.tv_time)).setText(time);
		((TextView) findViewById(R.id.tv_type)).setText(type);
		((TextView) findViewById(R.id.tv_discount)).setText(discount);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_yuyuecheck).setOnClickListener(this);
	}

	/**
	 * 用户信息非空检测
	 */
	private void checkValue() {
		if (((EditText) findViewById(R.id.et_username)).getText() == null
				|| ((EditText) findViewById(R.id.et_username)).getText()
						.toString().trim().equals("")) {
			TispToastFactory.getToast(YuYueCheck.this, "请输入姓名").show();
			return;
		}
		if (((EditText) findViewById(R.id.et_userphone)).getText() == null
				|| ((EditText) findViewById(R.id.et_userphone)).getText()
						.toString().trim().equals("")) {
			TispToastFactory.getToast(YuYueCheck.this, "请输入手机号码").show();
			return;
		}
		new submitYuYue().execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobileProbe.onPause(this, "预约确认页面");
	}

	/*
	 * 提交预约
	 */
	class submitYuYue extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("my_uid", sm.getUserId());
			map.put("to_uid", tid);
			map.put("reserve_time", date.split("-")[0] + "月"
					+ date.split("-")[1] + "日" + week);
			map.put("reserve_hour", time);
			map.put("reserve_type", type);
			map.put("price", discount);
			map.put("my_name", ((EditText) findViewById(R.id.et_username))
					.getText().toString().trim());
			map.put("my_tel", ((EditText) findViewById(R.id.et_userphone))
					.getText().toString().trim());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_YUYUECHECK,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {
				TispToastFactory.getToast(YuYueCheck.this, result.getMsg())
						.show();
				finish();
			} else {
				TispToastFactory.getToast(YuYueCheck.this, result.getMsg())
						.show();
			}
		}
	}

	/*
	 * 读取电话信息
	 */
	class getUserPhone extends AsyncTask<String, Integer, Response> {

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
			return conn.executeAndParse(Constant.URN_GETUSERPHONE,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {
				((EditText) findViewById(R.id.et_userphone)).setText(result
						.getString("mobile"));

			}
		}
	}

	/*
	 * 读取个人信息
	 */
	class getUserInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", tid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_YUYUE, getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {

				try {
					((TextView) findViewById(R.id.tv_username)).setText(result
							.getString("username"));
					ImageLoader.getInstance().displayImage(result.getString("head_photo"), (ImageView) findViewById(R.id.iv_minfouserpic));
					((TextView) findViewById(R.id.tv_dname)).setText("店名:"
							+ result.getString("store_name"));
					((TextView) findViewById(R.id.tv_address)).setText("地址:"
							+ result.getString("store_address"));
				} catch (Exception e) {
					LogUtil.e(e.toString());
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			finish();
			break;
		case R.id.btn_yuyuecheck:
			checkValue();
			break;
		}
	}
}