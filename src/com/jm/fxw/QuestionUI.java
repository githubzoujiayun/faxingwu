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
			// �û�
			((Button) findViewById(R.id.iv_btn_getquesion)).setText("��Ҫ����");
			((Button) findViewById(R.id.iv_btn_myquesion)).setText("�ҵ�����");

			((TextView) findViewById(R.id.tv_quesion))
					.setText("    ��������ͼ�Ľ�ϵķ�ʽ�������⣬����ҵ��רҵ֪������ʦ�Ŷ�Ϊ����𣬲����ڵ�һʱ��֪ͨ����");
		}
		if ((SessionManager.getInstance().getUsertype().equals("2"))) {
			((Button) findViewById(R.id.iv_btn_getquesion)).setText("��������");
			((Button) findViewById(R.id.iv_btn_myquesion)).setText("�ҵĻش�");
			// ����ʦ

			((TextView) findViewById(R.id.tv_quesion))
					.setText("    ϵͳ�����������߾��롢�û���Ծ�̶Ⱥ�����Ļش����Ϊ���Զ���ȡ���⣬��������Ǳ�ڿͻ����Ͻ���һ�°ɣ�");
		}
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		MobileProbe.onResume(this, "�ʴ�����");
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		MobileProbe.onPause(this, "�ʴ�����");
	}

	@Override
	public void onClick(View v) {
		
	}

	

}
