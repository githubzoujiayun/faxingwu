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
	protected void onResume() {
		MobileProbe.onResume(this, "问题中心");
		super.onResume();

		init();
	}

	@Override
	protected void onPause() {

		MobileProbe.onPause(this, "问题中心");
		super.onPause();
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
			((Button) findViewById(R.id.iv_btn_getquesion)).setText("提出问题");
			((Button) findViewById(R.id.iv_btn_myquesion)).setText("我的问题");

		}
		if ((SessionManager.getInstance().getUsertype().equals("2"))) {
			((Button) findViewById(R.id.iv_btn_getquesion)).setText("同城问题");
			((Button) findViewById(R.id.iv_btn_myquesion)).setText("我的回答");
			// 发型师
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
			if (((Button) v).getText().equals("提出问题")) {
				StartActivityContController.goPage(this,
						PublicQuestionUI.class, true);
				// 跳转提问页面
			}
			if (((Button) v).getText().equals("同城问题")) {
				// 跳转发现问题
				new getMsgInfoTask().execute();
			}
			break;
		case R.id.iv_btn_myquesion:
			if (((Button) v).getText().equals("我的问题")) {
				// 我的问题
				StartActivityContController.goPage(this, QuestionListUI.class,
						true);
			}
			if (((Button) v).getText().equals("我的回答")) {

				StartActivityContController.goPage(this, AnswerListUI.class,
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
