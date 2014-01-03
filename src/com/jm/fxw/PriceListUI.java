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
import android.widget.ListView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.User;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.UserPriceAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;
import com.jm.util.WidgetUtil;

public class PriceListUI extends Activity implements OnClickListener,
		OnScrollListener, OnItemClickListener {
	private ListView ListView;
	private UserPriceAdapter adapter;
	private List<User> mlist;
	private int page = 1;
	private int pageCount = 0;
	private SessionManager sm;
	private boolean isloading = false;
	private boolean showlast = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pricelist);
		init();

		new GetPriceListTask().execute();
	}

	@Override
	protected void onResume() {

		super.onResume();
		MobileProbe.onResume(this, "查看价格页面");
	}

	@Override
	protected void onPause() {

		super.onPause();
		MobileProbe.onPause(this, "查看价格页面");
	}

	private void init() {

		sm = SessionManager.getInstance();
		ListView = (ListView) findViewById(R.id.pricelist);
		adapter = new UserPriceAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);
		ListView.setOnScrollListener(this);
		ChangeType(1, findViewById(R.id.lin_price_xi));
		((TextView) findViewById(R.id.tv_rightTop)).setText(SessionManager
				.getInstance().getCity());
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.lin_price_xi).setOnClickListener(this);
		findViewById(R.id.lin_price_tang).setOnClickListener(this);
		findViewById(R.id.lin_price_ran).setOnClickListener(this);
		findViewById(R.id.lin_price_hu).setOnClickListener(this);

	}

	/*
	 * 读取同城列表
	 */
	class GetPriceListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			if (mlist == null || mlist.size() == 0) {
				adapter.setProgress(true);
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			// map.put("uid", sm.getUserId());
			map.put("city", sm.getCity());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_PRICE_LIST + "&page="
					+ page, getInfoInqVal());

		}

		protected void onPostExecute(Response result) {

			isloading = false;
			adapter.setProgress(false);
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {

				pageCount = Integer.parseInt(result.getString("page_count"));
				mlist = result.getList("price_list", new User());
				if (mlist == null || mlist.size() == 0) {
					TispToastFactory
							.getToast(PriceListUI.this, result.getMsg());
					return;
				}
				showlast = false;
				adapter.appendUserList(mlist);
				adapter.notifyDataSetChanged();

			} else {
				TispToastFactory.getToast(PriceListUI.this, result.getMsg());
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_leftTop:
			// 打开分类
			this.finish();
			break;
		case R.id.lin_price_xi:
			ChangeType(1, v);
			break;
		case R.id.lin_price_tang:
			ChangeType(2, v);
			break;
		case R.id.lin_price_ran:
			ChangeType(3, v);
			break;
		case R.id.lin_price_hu:
			ChangeType(4, v);
			break;

		}
	}

	private void ChangeType(int i, View v) {

		setPrice(i);
		List<View> blist = new ArrayList<View>();
		blist.add(findViewById(R.id.lin_price_xi));
		blist.add(findViewById(R.id.lin_price_tang));
		blist.add(findViewById(R.id.lin_price_ran));
		blist.add(findViewById(R.id.lin_price_hu));
		WidgetUtil.ResetAllButton(blist);
		WidgetUtil.setChangeButton(v);

	}

	private void setPrice(int i) {
		adapter.setPriceList(i);
		adapter.notifyDataSetChanged();
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
			Intent intent = new Intent(PriceListUI.this, YuYueUI.class);
			intent.putExtra("tid", user.getUid());
			startActivity(intent);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (showlast) {
			return;
		}
		if (totalItemCount - (firstVisibleItem + visibleItemCount) <= 6) {

			LogUtil.d("正在显示" + page + "页" + "总页数为" + pageCount + "页");

			if (page < pageCount) {
				if (!isloading) {
					LogUtil.d("即将加载" + ++page + "页");
					isloading = true;
					new GetPriceListTask().execute();
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
