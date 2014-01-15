package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.User;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.UserAdapter;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;
import com.jm.util.WidgetUtil;

public class UserListUI extends Activity implements OnClickListener,
		OnItemClickListener {
	private ListView ListView;
	private UserAdapter adapter;
	private List<User> mlist = new ArrayList<User>();
	private SessionManager sm;
	private String type;

	private boolean isPushIn;
	private String usertype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlist);
		init();
	}

	@Override
	protected void onResume() {
		MobileProbe.onResume(this, "关注/粉丝列表");
		super.onResume();

	}

	@Override
	protected void onPause() {

		MobileProbe.onPause(this, "关注/粉丝列表");
		super.onPause();
	}

	private void init() {

		findViewById(R.id.btn_faxingshi).setOnClickListener(this);
		findViewById(R.id.btn_geren).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		sm = SessionManager.getInstance();
		type = getIntent().getStringExtra("type");
		isPushIn = getIntent().getBooleanExtra("isPushIn", false);
		if ("watchlist".equals(type)) {
			((TextView) findViewById(R.id.tv_mainhead)).setText("关注列表");
		} else {
			((TextView) findViewById(R.id.tv_mainhead)).setText("粉丝列表");
		}
		usertype = "2";
		ListView = (ListView) findViewById(R.id.my_userlistview);
		adapter = new UserAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);
		changeUserType("2", findViewById(R.id.btn_faxingshi));

	}

	/*
	 * 读取粉丝或者关注列表
	 */
	class GetUserTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			if (mlist == null || mlist.size() == 0) {
				adapter.setProgress(true);
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("type", usertype);

			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_USER_LIST + type,
					getInfoInqVal());

		}

		protected void onPostExecute(Response result) {

			adapter.setProgress(false);
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {

				mlist = result.getList("user_info", new User());
				if (mlist == null || mlist.size() == 0) {
					TispToastFactory.getToast(UserListUI.this, result.getMsg());
					return;
				}
				adapter.appendUserList(mlist);
				adapter.notifyDataSetChanged();

			} else {
				TispToastFactory.getToast(UserListUI.this, result.getMsg());
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_leftTop:
			if (isPushIn) {
				StartActivityContController.goPage(UserListUI.this,
						StartActivityContController.wode);
			}
			this.finish();
			break;
		case R.id.btn_faxingshi:
			changeUserType("2", v);
			break;
		case R.id.btn_geren:

			changeUserType("1", v);
			break;
		}
	}

	private void changeUserType(String ut, View v) {

		List<View> blist = new ArrayList<View>();
		blist.add(findViewById(R.id.btn_faxingshi));
		blist.add(findViewById(R.id.btn_geren));
		WidgetUtil.ResetAllButton(blist);
		WidgetUtil.setChangeButton(v);
		adapter.clear();
		this.usertype = ut;
		new GetUserTask().execute();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (adapter.getUserList() == null || position < 0
				|| position >= adapter.getUserList().size()) {
			LogUtil.e("position = " + position);
			return;
		}
		User user = adapter.getUserList().get(position);
		if (!user.getUid().equals(sm.getUserId())) {
			Intent intent = new Intent(UserListUI.this, HisInfoUI.class);
			intent.putExtra("uid", user.getUid());
			intent.putExtra("type", user.getType());
			startActivity(intent);
		}
	}
}
