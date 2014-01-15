package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

public class ZhaoPinInfoUI extends FinalActivity implements OnClickListener {
	private String sid;

	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhaopininfo);
		initView();
	}

	private void initView() {
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		Intent i = getIntent();
		sid = i.getStringExtra("sid");

		new getShaLongInfo().execute();

	}

	@Override
	protected void onResume() {
		MobileProbe.onResume(this, "��Ƹ����");
		super.onResume();
	}

	@Override
	protected void onPause() {

		MobileProbe.onPause(this, "��Ƹ����");
		super.onPause();
	}

	/*
	 * �� ��ȡ������Ϣ
	 */
	class getShaLongInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", sid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_JOBSVIEW, getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get ShaLongInfo");
				return;
			}
			if (result.isSuccessful()) {
				JSONObject jb = result.getJsonString("jobs_info");
				try {

					FinalBitmap.create(ZhaoPinInfoUI.this).display(
							findViewById(R.id.iv_minfouserpic),
							jb.getString("head_photo"));
					uid = jb.getString("uid");
					findViewById(R.id.lin_sixin).setOnClickListener(
							ZhaoPinInfoUI.this);

					((TextView) findViewById(R.id.tv_username)).setText(jb
							.getString("username"));
					((TextView) findViewById(R.id.tv_store_name))
							.setText("��������:" + jb.getString("store_name"));
					((TextView) findViewById(R.id.tv_city)).setText("��������:"
							+ jb.getString("city"));
					((TextView) findViewById(R.id.tv_number)).setText("��Ƹ����:"
							+ jb.getString("number"));
					((TextView) findViewById(R.id.tv_money)).setText("н�����:"
							+ jb.getString("money"));
					if ("1".equals(jb.getString("interviews"))) {

						((TextView) findViewById(R.id.tv_money))
								.setText("н�����:" + "��̸");
					}
					((TextView) findViewById(R.id.tv_phone)).setText("��ϵ�绰:"
							+ jb.getString("telephone"));
					((TextView) findViewById(R.id.tv_job)).setText("��Ƹְλ:"
							+ jb.getString("job"));
					((TextView) findViewById(R.id.tv_address)).setText("���̵�ַ:"
							+ jb.getString("address"));
				} catch (JSONException e) {
					LogUtil.e(e.toString());
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		Map<String, String> map = new HashMap<String, String>();
		switch (v.getId()) {
		case R.id.btn_leftTop:
			finish();
			break;
		case R.id.lin_sixin:
			if (!uid.equals(SessionManager.getInstance().getUserId())) {
				map.put("tid", uid);
				StartActivityContController.goPage(ZhaoPinInfoUI.this,
						ChatUI.class, true, map);
			} else {
				TispToastFactory.getToast(this, "�������Լ���������Ϣ").show();
			}
			break;
		}

	}
}
