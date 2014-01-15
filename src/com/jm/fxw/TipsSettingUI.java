package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

public class TipsSettingUI extends FinalActivity implements OnClickListener {

	private SessionManager sm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tipssetting);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_rightTop).setOnClickListener(this);

		sm = SessionManager.getInstance();
		new getTipsSettingInfo().execute();
	}

	@Override
	protected void onResume() {
		MobileProbe.onResume(this, "设置推送");
		super.onResume();

	}

	@Override
	protected void onPause() {

		MobileProbe.onPause(this, "设置推送");
		super.onPause();
	}

	/*
	 * 读取个人推送信息
	 */
	class getTipsSettingInfo extends AsyncTask<String, Integer, Response> {

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
			return conn.executeAndParse(Constant.URN_TIPSSETTING,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get tipssettinginfo");
				return;
			}
			if (result.isSuccessful()) {
				((ToggleButton) findViewById(R.id.tbtn_comment))
						.setChecked(result.getString("iscomment").equals("1") ? true
								: false);
				((ToggleButton) findViewById(R.id.tbtn_message))
						.setChecked(result.getString("ismessage").equals("1") ? true
								: false);
				((ToggleButton) findViewById(R.id.tbtn_fans)).setChecked(result
						.getString("isfans").equals("1") ? true : false);
				((ToggleButton) findViewById(R.id.tbtn_reserve))
						.setChecked(result.getString("isreserve").equals("1") ? true
								: false);
			} else {
				TispToastFactory.getToast(TipsSettingUI.this, result.getMsg())
						.show();
			}
		}
	}

	/*
	 * 修改个人推送信息
	 */
	class getTipsSettingChange extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("iscomment",
					((ToggleButton) findViewById(R.id.tbtn_comment))
							.isChecked() ? "1" : "0");
			map.put("ismessage",
					((ToggleButton) findViewById(R.id.tbtn_message))
							.isChecked() ? "1" : "0");
			map.put("isfans", ((ToggleButton) findViewById(R.id.tbtn_fans))
					.isChecked() ? "1" : "0");
			map.put("isreserve",
					((ToggleButton) findViewById(R.id.tbtn_reserve))
							.isChecked() ? "1" : "0");
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_TIPSSETTINGCHANGE,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get tipssettinginfo");
				return;
			}
			if (result.isSuccessful()) {
				TispToastFactory.getToast(TipsSettingUI.this, result.getMsg())
						.show();
				finish();
			} else {

				TispToastFactory.getToast(TipsSettingUI.this, result.getMsg())
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
			new getTipsSettingChange().execute();
			break;
		default:
			break;
		}

	}
}
