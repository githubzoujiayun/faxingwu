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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Hair;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.HairAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

/**
 * �ղ��б�ҳ
 * 
 * @author Administrator
 * 
 */
public class CollectListUI extends Activity implements OnClickListener,
		OnScrollListener, OnItemClickListener {
	private GridView ListView;
	private HairAdapter adapter;
	private List<Hair> mlist = new ArrayList<Hair>();
	private int page = 1;
	private int pageCount = 0;
	private SessionManager sm;
	private boolean isloading = false;
	private boolean showlast = false;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.hairlist);
		init();
		new GetHairListTask().execute();
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

	private void init() {
		ListView = (GridView) findViewById(R.id.my_hairgridview);
		adapter = new HairAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);
		ListView.setOnScrollListener(this);
		ListView.setOnScrollListener(this);
		sm = SessionManager.getInstance();
		Intent i = getIntent();
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		uid = i.getStringExtra("uid");
		if (uid != null && !uid.equals("")) {
			if (sm.getUserId().equals(uid)) {
				// �ҵ��ղ�
				((TextView) findViewById(R.id.tv_mainhead)).setText("�ҵ��ղ�");
			} else {
				// �����ղ�
				((TextView) findViewById(R.id.tv_mainhead)).setText("TA���ղ�");
			}
		} else {
			finish();
		}

	}

	/*
	 * ��ȡ�����б�
	 */
	class GetHairListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			if (mlist == null || mlist.size() == 0) {
				adapter.setProgress(true);
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_MYCOLLECTLIST_LIST
					+ "&page=" + page, getInfoInqVal());

		}

		protected void onPostExecute(Response result) {

			isloading = false;
			adapter.setProgress(false);
			if (result == null) {

				LogUtil.e("can't get typelist");
				return;
			}
			if (result.isSuccessful()) {
				mlist = result.getList("works_info", new Hair());
				if (mlist == null || mlist.size() == 0) {
					TispToastFactory.getToast(CollectListUI.this, "��������")
							.show();
					adapter.notifyDataSetChanged();
					return;
				}
				pageCount = Integer.parseInt(result.getString("page_count"));
				showlast = false;
				adapter.appendHairList(mlist);
				adapter.appendHList(result.getList("image_list", new Hair()));
				adapter.notifyDataSetChanged();

			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_leftTop:
			// �򿪷���
			this.finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (adapter.getHairList() == null || position < 0
				|| position >= adapter.getHairList().size()) {
			LogUtil.e("position = " + position);
			return;
		}
		Hair hair = adapter.getHairList().get(position);
		Intent intent = new Intent(CollectListUI.this, HairInfoUI.class);
		intent.putExtra("hlist", adapter.getList());
		intent.putExtra("id", hair.getId());
		startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (showlast) {
			return;
		}
		if (totalItemCount - (firstVisibleItem + visibleItemCount) <= 0) {

			LogUtil.d("������ʾ" + page + "ҳ" + "��ҳ��Ϊ" + pageCount + "ҳ");

			if (page < pageCount) {
				if (!isloading) {
					LogUtil.d("��������" + ++page + "ҳ");
					isloading = true;
					new GetHairListTask().execute();
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
