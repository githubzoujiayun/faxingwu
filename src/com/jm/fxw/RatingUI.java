package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.TispToastFactory;

public class RatingUI extends Activity implements OnClickListener {

	private String hid, oid;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ratringinfo);
		hid = getIntent().getStringExtra("hid");
		oid = getIntent().getStringExtra("oid");
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_no).setOnClickListener(this);
	}

	@Override
	protected void onResume() {

		super.onResume();
		MobileProbe.onResume(this, "发表评价页面");
	}

	@Override
	protected void onPause() {

		super.onPause();
		MobileProbe.onPause(this, "发表评价页面");
	}

	private class CommentTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {

		}

		private Map<String, Object> getMyReserveInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", SessionManager.getInstance().getUserId());
			map.put("assess_uid", hid);
			map.put("order_id", oid);

			if (((RadioButton) findViewById(R.id.cb_haoping)).isChecked()) {

				map.put("score", "1");
			}
			if (((RadioButton) findViewById(R.id.cb_haoping)).isChecked()) {

				map.put("score", "2");
			}
			if (((RadioButton) findViewById(R.id.cb_haoping)).isChecked()) {

				map.put("score", "3");
			}
			map.put("info", ((EditText) findViewById(R.id.comment)).getText()
					.toString().trim());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_COMMENT_YUYUE,
					getMyReserveInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				TispToastFactory.getToast(RatingUI.this, result.getMsg());
				finish();
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_ok:
			new CommentTask().execute();
			break;
		case R.id.btn_no:
			finish();
			break;
		default:
			break;
		}

	}
}
