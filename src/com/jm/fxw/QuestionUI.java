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
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "�ʴ�����");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "�ʴ�����");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			finish();
			break;
		case R.id.iv_btn_getquesion:
			if (((Button) v).getText().equals("��Ҫ����")) {
				StartActivityContController.goPage(QuestionUI.this,
						PublicQuestionUI.class, true);
				// ��ת����ҳ��
			}
			if (((Button) v).getText().equals("��������")) {
				// ��ת��������
				new getMsgInfoTask().execute();
			}
			break;
		case R.id.iv_btn_myquesion:
			if (((Button) v).getText().equals("�ҵ�����")) {
				// �ҵ�����
				StartActivityContController.goPage(QuestionUI.this,
						MyQuestionListUI.class, true);
			}
			if (((Button) v).getText().equals("�ҵĻش�")) {

				StartActivityContController.goPage(QuestionUI.this,
						MyAnswerListUI.class, true);

				// �ҵĻش�
			}
			break;
		}
	}

	/*
	 * ��ȡ��Ϣ����
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
