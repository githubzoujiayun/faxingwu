package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

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
		
		super.onResume();
		MobileProbe.onResume(this, "问答中心");
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		MobileProbe.onPause(this, "问答中心");
	}

	@Override
	public void onClick(View v) {
		
	}

	

}
