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
import android.widget.ListView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Reserve;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.ReserveAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

public class YuYueUI_Haier extends FinalActivity implements OnClickListener,
		OnItemClickListener {
	private List<Reserve> mlist = new ArrayList<Reserve>();
	private ReserveAdapter adapter;
	private ListView ListView;
	private SessionManager sm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyue_haier);
		init();
		ListView = (ListView) findViewById(R.id.yuyue_list);
		adapter = new ReserveAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobileProbe.onPause(this, "发型师预约管理页面");
	}

	private void init() {
		sm = SessionManager.getInstance();
		findViewById(R.id.btn_leftTop).setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobileProbe.onResume(this, "发型师预约管理页面");
		new getCurrentYuYueListInfo().execute();
	}

	/*
	 * 读取发型师列表信息
	 */
	class getCurrentYuYueListInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_YUYUE_HAIER_LIST,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				if (!"".equals(result.getString("order_list"))) {
					mlist = result.getList("order_list", new Reserve());
					adapter.setTypeList(mlist);
					adapter.notifyDataSetChanged();
				} else {
					TispToastFactory.getToast(YuYueUI_Haier.this, "暂无预约信息");
				}
			} else {
				TispToastFactory.getToast(YuYueUI_Haier.this, result.getMsg())
						.show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			this.finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (adapter.getReserveList() == null || position < 0
				|| position >= adapter.getReserveList().size()) {
			LogUtil.e("position = " + position);
			return;
		}
		Reserve reserve = adapter.getReserveList().get(position);
		Intent intent = new Intent(YuYueUI_Haier.this, YuYueInfoUI_Haier.class);
		intent.putExtra("rid", reserve.getId());
		startActivity(intent);
	}

}
