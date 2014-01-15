package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalActivity;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Reserve;
import com.jm.finals.Constant;
import com.jm.fxw.YuYueListUI_Haier.getCurrentYuYueListInfo;
import com.jm.session.SessionManager;
import com.jm.sort.ReserveAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;
import com.jm.util.WidgetUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class YuYueListUI_User extends FinalActivity implements OnClickListener,
		OnItemClickListener {
	private List<Reserve> mlist = new ArrayList<Reserve>();
	private Reserve reserve;
	private ReserveAdapter adapter;
	private ListView ListView;
	private SessionManager sm;
	private String status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyue_user);
		init();
		ListView = (ListView) findViewById(R.id.yuyue_list);
		adapter = new ReserveAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnItemClickListener(this);
		ResetButtonBgAndViews();
		WidgetUtil.setChangeButton(findViewById(R.id.btn_jibenxinxi));
		new getCurrentYuYueInfo().execute();
	}

	private void init() {
		sm = SessionManager.getInstance();
		findViewById(R.id.btn_jibenxinxi).setOnClickListener(this);
		findViewById(R.id.btn_lishiyuyue).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		ResetButtonBgAndViews();
		((Button) findViewById(R.id.btn_jibenxinxi))
				.setTextColor(Constant.color_RoseRed);
		findViewById(R.id.btn_btn).setOnClickListener(this);

	}

	private void ResetButtonBgAndViews() {
		findViewById(R.id.lin_basic_info).setVisibility(View.GONE);
		findViewById(R.id.lin_nobasic_info).setVisibility(View.GONE);
		findViewById(R.id.lin_yuyue_list).setVisibility(View.GONE);
		List<View> blist = new ArrayList<View>();
		blist.add(findViewById(R.id.btn_jibenxinxi));
		blist.add(findViewById(R.id.btn_lishiyuyue));
		WidgetUtil.ResetAllButton(blist);
	}

	@Override
	protected void onResume() {
		MobileProbe.onResume(this, "普通用户查看预约列表");
		super.onResume();

	}

	@Override
	protected void onPause() {

		MobileProbe.onPause(this, "普通用户查看预约列表");
		super.onPause();
	}

	/*
	 * 读取当前预约信息
	 */
	class getCurrentYuYueInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("order_id", "-1");
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_YUYUE_CURRENT,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {

			if (result == null) {
				LogUtil.e("getCurrentYuYueInfo failed");
				return;
			}

			if (result.isSuccessful()) {
				findViewById(R.id.lin_basic_info).setVisibility(View.GONE);
				findViewById(R.id.lin_nobasic_info).setVisibility(View.GONE);
				findViewById(R.id.lin_yuyue_list).setVisibility(View.GONE);
				setYuyue(result);

			} else {
				findViewById(R.id.lin_basic_info).setVisibility(View.GONE);
				findViewById(R.id.lin_nobasic_info).setVisibility(View.VISIBLE);
			}
		}

		private void setYuyue(Response result) {
			if (!"".equals(result.getString("order_info"))) {
				reserve = (Reserve) result.getObject("order_info",
						new Reserve());
				if (reserve.getOrder_type().equals("2")) {
					findViewById(R.id.iv_workpic).setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(
							reserve.getImage_path(),
							(ImageView) findViewById(R.id.iv_workpic));
					((TextView) findViewById(R.id.tv_type)).setText("预约如图发型");
					findViewById(R.id.lin_username).setVisibility(View.GONE);
				} else {
					findViewById(R.id.iv_workpic).setVisibility(View.GONE);
					((TextView) findViewById(R.id.tv_type)).setText("预约类型:"
							+ reserve.getReserver_type());
					((TextView) findViewById(R.id.tv_username)).setText(reserve
							.getTo_username());
				}
				((TextView) findViewById(R.id.tv_dname)).setText(reserve
						.getStore_name());
				((TextView) findViewById(R.id.tv_dphone)).setText(reserve
						.getTelephone());
				((TextView) findViewById(R.id.tv_daddress)).setText(reserve
						.getStore_address());
				((TextView) findViewById(R.id.tv_time)).setText(reserve
						.getReserve_time() + reserve.getReserve_hour());

				((TextView) findViewById(R.id.tv_price)).setText("价格:"
						+ reserve.getPrice());
				setBtnByStaues(reserve.getStatus());
				new getTipsInfo().execute();
				findViewById(R.id.lin_basic_info).setVisibility(View.VISIBLE);
			}
		}

	}

	private void setBtnByStaues(String price) {
		findViewById(R.id.btn_btn).setVisibility(View.GONE);
		// 0(自己取消) 都不显示
		// 1(进行中) 只能取消
		// 2(已完成，未评价) 只能评价
		// 3(发型师取消) 都不显示
		// 4(已完成，已评价) 都不显示
		if ("0".equals(price)) {
		}
		if ("1".equals(price)) {
			findViewById(R.id.btn_btn).setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.btn_btn)).setText("取消预约");
		}
		if ("2".equals(price)) {
		}
		if ("3".equals(price)) {
		}
		if ("4".equals(price)) {
		}

	}

	/*
	 * 读取提示信息
	 */
	class getTipsInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", reserve.getTo_uid());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn
					.executeAndParse(Constant.URN_TIPS_INFO, getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				findViewById(R.id.lin_tip).setVisibility(View.VISIBLE);
				try {
					JSONArray alist = result.getJsonString("notice_info")
							.getJSONArray("info");
					((TextView) findViewById(R.id.tv_tip1))
							.setText((CharSequence) alist.get(0));
					findViewById(R.id.lin_tip1).setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.tv_tip2))
							.setText((CharSequence) alist.get(1));
					findViewById(R.id.lin_tip2).setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.tv_tip3))
							.setText((CharSequence) alist.get(2));
					findViewById(R.id.lin_tip3).setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.tv_tip4))
							.setText((CharSequence) alist.get(3));
					findViewById(R.id.lin_tip4).setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.tv_tip5))
							.setText((CharSequence) alist.get(4));
					findViewById(R.id.lin_tip5).setVisibility(View.VISIBLE);
				} catch (JSONException e) {
					LogUtil.e(e.toString());
				}
			} else {
				findViewById(R.id.lin_tip).setVisibility(View.GONE);

			}
		}
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
			return conn.executeAndParse(Constant.URN_YUYUE_HISTORY,
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
					TispToastFactory.getToast(YuYueListUI_User.this, "暂无预约信息");
				}
			} else {
				TispToastFactory.getToast(YuYueListUI_User.this,
						result.getMsg()).show();
			}
		}
	}

	/*
	 * URN_YUYUCHANGE
	 */
	class changeYuYueInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", reserve.getId());
			map.put("status", status);
			if ("0".equals(status)) {
				reserve = null;
			}
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_YUYUCHANGE,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				TispToastFactory.getToast(YuYueListUI_User.this,
						result.getMsg());
				new getCurrentYuYueInfo().execute();
			} else {
				TispToastFactory.getToast(YuYueListUI_User.this,
						result.getMsg()).show();
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			this.finish();
			break;
		case R.id.btn_jibenxinxi:
			ResetButtonBgAndViews();
			WidgetUtil.setChangeButton(v);
			if (reserve != null) {
				findViewById(R.id.lin_basic_info).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.lin_nobasic_info).setVisibility(View.VISIBLE);
			}
			new getCurrentYuYueInfo().execute();
			break;
		case R.id.btn_lishiyuyue:
			ResetButtonBgAndViews();
			WidgetUtil.setChangeButton(v);
			findViewById(R.id.lin_yuyue_list).setVisibility(View.VISIBLE);
			new getCurrentYuYueListInfo().execute();
			break;

		case R.id.btn_btn:
			if (((Button) v).getText().equals("取消预约")) {
				LogUtil.i("取消预约");
				status = "0";
				new changeYuYueInfo().execute();
			}
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
		Intent intent = new Intent(YuYueListUI_User.this,
				YuYueInfoUI_User.class);
		intent.putExtra("rid", reserve.getId());
		startActivity(intent);
	}

}
