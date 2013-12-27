package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

public class GuangChangUI extends FinalActivity implements OnClickListener {
	@ViewInject(id = R.id.lin_guangchang, click = "Click")
	LinearLayout lin_guangchang;
	@ViewInject(id = R.id.lin_zhaofaxing, click = "Click")
	LinearLayout lin_zhaofaxing;
	@ViewInject(id = R.id.lin_faxingshi, click = "Click")
	LinearLayout lin_faxingshi;
	@ViewInject(id = R.id.lin_wode, click = "Click")
	LinearLayout lin_wode;
	@ViewInject(id = R.id.lin_dongtai, click = "Click")
	LinearLayout lin_dongtai;

	// @ViewInject(id = R.id.lin_tongcheng, click = "Click")
	// LinearLayout lin_zhoubian;
	// @ViewInject(id = R.id.lin_chakanjiange, click = "Click")
	// LinearLayout lin_chakanjiange;
	// @ViewInject(id = R.id.lin_qingbaozhan, click = "Click")
	// LinearLayout lin_qingbaozhan;
	// @ViewInject(id = R.id.lin_question, click = "Click")
	// LinearLayout lin_question;
	// @ViewInject(id = R.id.lin_redianhuati, click = "Click")
	// LinearLayout lin_redianhuati;
	// @ViewInject(id = R.id.lin_zhaopinxinxi, click = "Click")
	// LinearLayout lin_zhaopinxinxi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.guangchang);
		init();

	}

	@Override
	protected void onPause() {

		super.onPause();
		MobileProbe.onPause(this, "广场页面");
	}

	@Override
	protected void onResume() {

		super.onResume();
		MobileProbe.onResume(this, "广场页面");
		init();
	}

	private void setInco() {

		findViewById(R.id.iv_guangchang).setBackgroundResource(
				R.drawable.guangchang1);
		((TextView) findViewById(R.id.tv_guangchang)).setTextColor(Color
				.parseColor("#f01c61"));
	}

	private void init() {
		setInco();
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

	public void Click(View v) {
		switch (v.getId()) {
		//
		// case R.id.lin_redianhuati:
		// // changeCondition("4", v);
		// StartActivityContController.goPage(this, JiShuHuaTiUI.class, true);
		// break;
		// case R.id.lin_zhaopinxinxi:
		// // changeCondition("4", v);
		// StartActivityContController.goPage(this, JobListUI.class, true);
		// break;
		case R.id.lin_guangchang:
			StartActivityContController.goPage(this, GuangChangUI.class, true);
			break;
		case R.id.lin_zhaofaxing:
			StartActivityContController.goPage(this, ZhaofaxingUI.class, false);
			break;
		case R.id.lin_faxingshi:
			StartActivityContController.goPage(this, FaxingshiUI.class, false);
			break;
		case R.id.lin_wode:
			StartActivityContController.goPage(this,
					StartActivityContController.wode);
			break;
		// case R.id.lin_dongtai:
		// StartActivityContController.goPage(GuangChangUI.this,
		// DongTaiUI.class, false);
		// break;
		// case R.id.lin_tongcheng:
		// StartActivityContController.goPage(GuangChangUI.this,
		// TongChengUI.class, false);
		// break;
		// case R.id.lin_chakanjiange:
		// StartActivityContController.goPage(GuangChangUI.this,
		// PriceListUI.class, false);
		// break;
		//
		// case R.id.lin_qingbaozhan:
		// StartActivityContController.goPage(GuangChangUI.this, 104);
		// break;
		// case R.id.lin_question:
		// StartActivityContController.goPage(GuangChangUI.this,
		// QuestionUI.class, true);
		// break;

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			finish();
			break;
		case R.id.iv_btn_getquesion:
			if (((Button) v).getText().equals("我要提问")) {
				StartActivityContController.goPage(this,
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
				StartActivityContController.goPage(this,
						MyQuestionListUI.class, true);
			}
			if (((Button) v).getText().equals("我的回答")) {

				StartActivityContController.goPage(this, MyAnswerListUI.class,
						true);

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
				Intent intent = new Intent(GuangChangUI.this,
						ChatQuestionUI.class);
				LogUtil.e("pid = " + result.getString("pid"));
				LogUtil.e("uid = " + result.getString("uid"));
				intent.putExtra("pid", result.getString("pid"));
				intent.putExtra("tid", result.getString("uid"));
				startActivity(intent);
			} else {
				TispToastFactory.getToast(GuangChangUI.this, result.getMsg())
						.show();
			}
		}
	}
}
