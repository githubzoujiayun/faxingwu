package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
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

public class YuYueInfoUI_Haier extends FinalActivity implements OnClickListener {
	private Reserve reserve;
	private FinalBitmap fb;
	private String rid;
	private String status;
	private String uid;

	private boolean isPushIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyueinfo_haier);
		init();

	}

	private void init() {
		isPushIn = getIntent().getBooleanExtra("isPushIn", false);
		rid = getIntent().getStringExtra("rid");
		fb = FinalBitmap.create(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);

		findViewById(R.id.iv_minfouserpic).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(YuYueInfoUI_Haier.this,
								HisInfoUI.class);
						intent.putExtra("uid", uid);
						intent.putExtra("type", "1");
						startActivity(intent);

					}
				});

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobileProbe.onResume(this, "发型师预约详情页面");
		new getCurrentYuYueInfo().execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobileProbe.onPause(this, "发型师预约详情页面页面");
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
				TispToastFactory.getToast(YuYueInfoUI_Haier.this,
						result.getMsg());
				new getCurrentYuYueInfo().execute();
			}
		}

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
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_YUYUE_INFO,
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
						.getMy_name());
				((TextView) findViewById(R.id.tv_phone)).setText(reserve
						.getMy_tel());
				((TextView) findViewById(R.id.tv_time)).setText(reserve
						.getReserve_time() + reserve.getReserve_hour());
				((TextView) findViewById(R.id.tv_type)).setText(reserve
						.getReserver_type());
				((TextView) findViewById(R.id.tv_price)).setText(reserve
						.getPrice());

				((TextView) findViewById(R.id.tv_dname)).setText(reserve
						.getStore_name());
				((TextView) findViewById(R.id.tv_daddress)).setText(reserve
						.getStore_address());
				setBtnByStaues(reserve.getStatus());
				uid = reserve.getMy_uid();

			}
		}
	}

	private void setBtnByStaues(String price) {
		findViewById(R.id.lin_btns).setVisibility(View.GONE);
		// 0(自己取消)
		// 1(进行中) 显示两个按钮
		// 2(已完成，未评价)
		// 3(发型师取消)
		// 4(已完成，已评价)
		if ("0".equals(price)) {
		}
		if ("1".equals(price)) {
			findViewById(R.id.lin_btns).setVisibility(View.VISIBLE);
			findViewById(R.id.btn_ok).setOnClickListener(this);
			findViewById(R.id.btn_no).setOnClickListener(this);
			findViewById(R.id.lin_btn).setOnClickListener(this);

		}
		if ("2".equals(price)) {
		}
		if ("3".equals(price)) {
		}
		if ("4".equals(price)) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			if (isPushIn) {
				StartActivityContController.goPage(YuYueInfoUI_Haier.this,
						StartActivityContController.wode);
			}
			this.finish();
			break;

		case R.id.lin_btn:
			if (((Button) v).getText().equals("取消预约")) {

				status = "0";
				new changeYuYueInfo().execute();
			}
			if (((Button) v).getText().equals("消费评价")) {
			}
			break;
		case R.id.btn_ok:
			status = "2";
			new changeYuYueInfo().execute();
			break;
		case R.id.btn_no:
			status = "3";
			new changeYuYueInfo().execute();
			break;
		}
	}

}
