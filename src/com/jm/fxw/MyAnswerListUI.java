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

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Answer;
import com.jm.entity.News;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.AnswerAdapter;
import com.jm.sort.NewsAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;
import com.jm.view.MyListView;

public class MyAnswerListUI extends FinalActivity implements OnClickListener,
		OnItemClickListener {
	private SessionManager sm;
	private AnswerAdapter adapter;
	private List<Answer> mlist = new ArrayList<Answer>();
	private MyListView myListView;
	private String pid = "";// 问题Id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.questionlist);
		sm = SessionManager.getInstance();
		try {

			pid = getIntent().getStringExtra("pid");
		} catch (Exception e) {
			LogUtil.e("没有问题id，显示发型师回答列表");
		}
		if ("".equals(pid)) {
			((TextView) findViewById(R.id.tv_mainhead)).setText("我的回答");
		} else {
			((TextView) findViewById(R.id.tv_mainhead)).setText("回答列表");
		}
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "回答列表页面");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "回答列表页面");
	}

	private void initView() {
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		myListView = (MyListView) findViewById(R.id.msg_list);
		adapter = new AnswerAdapter(this);
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
			map.put("uid", sm.getUserId());
			map.put("pid", pid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_MYANSWER, getMsgInqVal());

		}

		protected void onPostExecute(Response result) {
			myListView.onRefreshComplete();
			if (result == null) {
				LogUtil.e("can not get answer List");
				return;
			}
			if (result.isSuccessful()) {
				if (!"".equals(result.getString("answer_list"))) {
					mlist = result.getList("answer_list", new Answer());
					adapter.setTypeList(mlist);
				} else {
					TispToastFactory.getToast(MyAnswerListUI.this,
							result.getMsg()).show();
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> dapterView, View view, int arg2,
			long arg3) {
		Intent i = new Intent();

		i.putExtra("pid", ((TextView) view.findViewById(R.id.tv_tid)).getText()
				.toString().trim());
		i.putExtra("tid", ((TextView) view.findViewById(R.id.tv_ta_id))
				.getText().toString().trim());
		i.setClass(MyAnswerListUI.this, ChatQuestionUI.class);
		startActivity(i);
	}

}
