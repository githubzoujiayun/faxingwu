package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Question;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.QuesionAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;
import com.jm.view.MyListView;

public class QuestionListUI extends FinalActivity implements OnClickListener,
		OnItemClickListener {
	private QuesionAdapter adapter;
	private List<Question> mlist = new ArrayList<Question>();
	private MyListView myListView;

	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.questionlist);

		uid = getIntent().getStringExtra("uid");
		if (uid == null || "".equals(uid)) {
			uid = SessionManager.getInstance().getUserId();
			((TextView) findViewById(R.id.tv_mainhead)).setText("我的提问");
		} else {
			((TextView) findViewById(R.id.tv_mainhead)).setText("TA的提问");
		}
		initView();
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

	private void initView() {
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		myListView = (MyListView) findViewById(R.id.msg_list);
		adapter = new QuesionAdapter(this);
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(this);
		myListView
				.setOnRefreshListener(new com.jm.view.MyListView.OnRefreshListener() {

					public void onRefresh() {
						new getMsgInfoTask().execute();
					}
				});
		new getMsgInfoTask().execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			finish();
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
			map.put("uid", uid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn
					.executeAndParse(Constant.URN_MYQUESTION, getMsgInqVal());

		}

		protected void onPostExecute(Response result) {
			myListView.onRefreshComplete();
			if (result == null) {
				LogUtil.e("can not get msg List");
				return;
			}
			if (result.isSuccessful()) {
				if (!"".equals(result.getString("problem_list"))) {
					mlist = result.getList("problem_list", new Question());
					adapter.setTypeList(mlist);
				} else {
					TispToastFactory.getToast(QuestionListUI.this,
							result.getMsg()).show();
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> dapterView, View view, int arg2,
			long arg3) {
		if (((TextView) findViewById(R.id.tv_mainhead)).getText().equals(
				"TA的提问")) {
			Intent i = new Intent();

			i.putExtra("pid", ((TextView) view.findViewById(R.id.tv_tid))
					.getText().toString().trim());
			i.putExtra("tid", uid);
			i.setClass(QuestionListUI.this, ChatQuestionUI.class);
			startActivity(i);
		}

		else {

			Intent i = new Intent();
			i.putExtra("pid", ((TextView) view.findViewById(R.id.tv_tid))
					.getText().toString().trim());
			i.setClass(QuestionListUI.this, AnswerListUI.class);

			startActivity(i);
		}
	}
}
