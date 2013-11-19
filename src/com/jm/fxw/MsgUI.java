package com.jm.fxw;

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
import com.jm.entity.Msg;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.MsgAdapter;
import com.jm.util.LogUtil;
import com.jm.view.MyListView;

public class MsgUI extends FinalActivity implements OnClickListener,
		OnItemClickListener {
	private SessionManager sm;
	private MsgAdapter adapter;
	private List<Msg> mlist;
	private MyListView myListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg);
		sm = SessionManager.getInstance();

		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "私信列表页面");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "私信列表页面");
	}

	private void initView() {
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		myListView = (MyListView) findViewById(R.id.msg_list);
		adapter = new MsgAdapter(this);
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
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_MSG_LIST, getMsgInqVal());

		}

		protected void onPostExecute(Response result) {
			myListView.onRefreshComplete();
			if (result == null) {
				LogUtil.e("can not get msg List");
				return;
			}
			if (result.isSuccessful()) {
				if (!"".equals(result.getString("message_list"))) {
					mlist = result.getList("message_list", new Msg());
					adapter.setTypeList(mlist);
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> dapterView, View view, int arg2,
			long arg3) {
		LogUtil.e(((TextView) view.findViewById(R.id.tv_tid)).getText()
				.toString().trim());
		Intent i = new Intent();
		i.putExtra("tid", ((TextView) view.findViewById(R.id.tv_tid)).getText()
				.toString().trim());
		i.setClass(MsgUI.this, ChatUI.class);
		startActivity(i);
	}

}
