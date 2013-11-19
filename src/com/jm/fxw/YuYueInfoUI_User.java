package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Reserve;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

public class YuYueInfoUI_User extends FinalActivity implements OnClickListener {
	private Reserve reserve;
	private SessionManager sm;
	private FinalBitmap fb;
	private String rid;
	private String status;

	private boolean isPushIn;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyueinfo_user);
		init();

	}

	private void init() {
		isPushIn = getIntent().getBooleanExtra("isPushIn", false);
		rid = getIntent().getStringExtra("rid");
		fb = FinalBitmap.create(this);
		sm = SessionManager.getInstance();
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.iv_minfouserpic).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(YuYueInfoUI_User.this,
								HisInfoUI.class);
						intent.putExtra("uid", uid);
						intent.putExtra("type", "2");
						startActivity(intent);

					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobileProbe.onResume(this, "用户预约详情页面");
		new getCurrentYuYueInfo().execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobileProbe.onPause(this, "用户预约详情页面");
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
			map.put("order_id", rid);
			map.put("uid", sm.getUserId());
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
				LogUtil.e("can't get ShaLongInfo");
				return;
			}
			if (result.isSuccessful()) {

				reserve = (Reserve) result.getObject("order_info",
						new Reserve());
				fb.display((ImageView) findViewById(R.id.iv_minfouserpic),
						reserve.getHead_photo());

				((TextView) findViewById(R.id.tv_username)).setText(reserve
						.getTo_username());
				((TextView) findViewById(R.id.tv_dname)).setText(reserve
						.getStore_name());
				((TextView) findViewById(R.id.tv_dphone)).setText(reserve
						.getMy_tel());
				((TextView) findViewById(R.id.tv_daddress)).setText(reserve
						.getStore_address());

				((TextView) findViewById(R.id.tv_time)).setText(reserve
						.getReserve_time() + reserve.getReserve_hour());
				((TextView) findViewById(R.id.tv_type)).setText(reserve
						.getReserver_type());
				((TextView) findViewById(R.id.tv_price)).setText(reserve
						.getPrice());
				setBtnByStaues(reserve.getStatus());

				uid = reserve.getTo_uid();
				new getTipsInfo().execute();
			}
		}

	}

	private void setBtnByStaues(String price) {
		findViewById(R.id.lin_btn).setVisibility(View.GONE);
		findViewById(R.id.btn_btn).setOnClickListener(this);
		// 0(自己取消) 都不显示
		// 1(进行中) 只能取消
		// 2(已完成，未评价) 只能评价
		// 3(发型师取消) 都不显示
		// 4(已完成，已评价) 都不显示
		if ("0".equals(price)) {
		}
		if ("1".equals(price)) {
			findViewById(R.id.lin_btn).setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.btn_btn)).setText("取消预约");
		}
		if ("2".equals(price)) {
			findViewById(R.id.lin_btn).setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.btn_btn)).setText("消费评价");
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
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {
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
			map.put("order_id", rid);
			map.put("status", status);
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
				LogUtil.e("change resever failed");
				return;
			}
			if (result.isSuccessful()) {
				TispToastFactory.getToast(YuYueInfoUI_User.this,
						result.getMsg());
				new getCurrentYuYueInfo().execute();
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			if (isPushIn) {
				StartActivityContController.goPage(YuYueInfoUI_User.this,
						StartActivityContController.wode);
			}
			this.finish();
			break;
		case R.id.btn_btn:
			if (((Button) v).getText().equals("取消预约")) {
				status = "0";
				new changeYuYueInfo().execute();
			} else if (((Button) v).getText().equals("消费评价")) {
				Intent i = new Intent();
				i.putExtra("hid", reserve.getTo_uid());
				i.putExtra("oid", reserve.getId());
				i.setClass(YuYueInfoUI_User.this, RatingUI.class);
				startActivity(i);
			}
			break;
		}
	}
}
