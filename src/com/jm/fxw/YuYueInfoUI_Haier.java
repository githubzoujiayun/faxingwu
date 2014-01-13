package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Reserve;
import com.jm.finals.Constant;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;
import com.nostra13.universalimageloader.core.ImageLoader;

public class YuYueInfoUI_Haier extends FinalActivity implements OnClickListener {
	private Reserve reserve;
	private String rid;
	private String status;

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
		findViewById(R.id.btn_leftTop).setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		new getCurrentYuYueInfo().execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
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
				setYuyue(result);
				setBtnByStaues(reserve.getStatus());
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
				findViewById(R.id.lin_basic_info).setVisibility(View.VISIBLE);
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
			findViewById(R.id.btn_btn).setOnClickListener(this);
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
		case R.id.btn_btn:
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
