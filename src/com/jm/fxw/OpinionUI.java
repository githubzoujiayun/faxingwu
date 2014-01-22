package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.baidu.mobstat.StatService;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

public class OpinionUI extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opinion);

		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		StatService.onResume(this);
		super.onResume();

	}

	@Override
	protected void onPause() {

		StatService.onPause(this);
		super.onPause();
	}

	/*
	 * 用户建议
	 */
	class PublicOpinionTask extends AsyncTask<String, Integer, Response> {
		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", SessionManager.getInstance().getUserId());
			map.put("content", ((EditText) findViewById(R.id.edit_suggest))
					.getText().toString().trim());
			return map;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_OPINION, getListInqVal());

		}

		protected void onPostExecute(Response result) {

			if (result == null) {
				LogUtil.e("can not publish comment");
				return;
			} else if (result.isSuccessful()) {

				TispToastFactory.getToast(OpinionUI.this, result.getMsg())
						.show();
				finish();

			} else {
				TispToastFactory.getToast(OpinionUI.this, result.getMsg())
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
		case R.id.btn_submit:
			if ("".equals(((EditText) findViewById(R.id.edit_suggest))
					.getText().toString().trim())) {
				TispToastFactory.getToast(this, "意见或者建议不能为空").show();
				return;
			}
			new PublicOpinionTask().execute();
			break;
		}
	}
}
