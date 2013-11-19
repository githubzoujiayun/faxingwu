package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Text;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.News;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class QuestionUI extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question);
		init();

	}

	private void init() {
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.iv_btn_getquesion).setOnClickListener(this);
		findViewById(R.id.iv_btn_myquesion).setOnClickListener(this);
		if (SessionManager.getInstance().getUsertype().equals("1")) {
			// 用户
			((Button) findViewById(R.id.iv_btn_getquesion)).setText("我要提问");
			((Button) findViewById(R.id.iv_btn_myquesion)).setText("我的问题");

			((TextView) findViewById(R.id.tv_quesion))
					.setText("    您可以以图文结合的方式发布问题，将由业内专业知名发型师团队为您解答，并且在第一时间通知您。");
		}
		if ((SessionManager.getInstance().getUsertype().equals("2"))) {
			((Button) findViewById(R.id.iv_btn_getquesion)).setText("发现问题");
			((Button) findViewById(R.id.iv_btn_myquesion)).setText("我的回答");
			// 发型师

			((TextView) findViewById(R.id.tv_quesion))
					.setText("    系统将按照提问者距离、用户活跃程度和问题的回答情况为您自动获取问题，发现您的潜在客户，赶紧试一下吧！");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "问答中心");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "问答中心");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			finish();
			break;
		case R.id.iv_btn_getquesion:
			if (((Button) v).getText().equals("我要提问")) {
				StartActivityContController.goPage(QuestionUI.this,
						PublicQuestionUI.class, true);
				// 跳转提问页面
			}
			if (((Button) v).getText().equals("发现问题")) {
				// 跳转发现问题
				new getMsgInfoTask().execute();
			}
			break;
		case R.id.iv_btn_myquesion:
			if (((Button) v).getText().equals("我的问题")) {
				// 我的问题
				StartActivityContController.goPage(QuestionUI.this,
						MyQuestionListUI.class, true);
			}
			if (((Button) v).getText().equals("我的回答")) {

				StartActivityContController.goPage(QuestionUI.this,
						MyAnswerListUI.class, true);

				// 我的回答
			}
			break;
		}
	}

	/*
	 * 读取消息详情
	 */

	class getMsgInfoTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getMsgInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("city", SessionManager.getInstance().getCity());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_FINDQUESTION,
					getMsgInqVal());

		}

		protected void onPostExecute(Response result) {

			if (result == null) {
				LogUtil.e("can not get answer List");
				return;
			}
			if (result.isSuccessful()) {
				Intent intent = new Intent(QuestionUI.this,
						ChatQuestionUI.class);
				LogUtil.e("pid = " + result.getString("pid"));
				LogUtil.e("uid = " + result.getString("uid"));
				intent.putExtra("pid", result.getString("pid"));
				intent.putExtra("tid", result.getString("uid"));
				startActivity(intent);
			} else {
				TispToastFactory.getToast(QuestionUI.this, result.getMsg())
						.show();
			}
		}
	}

}
