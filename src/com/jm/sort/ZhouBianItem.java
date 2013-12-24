package com.jm.sort;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.ZhouBian;
import com.jm.finals.Constant;
import com.jm.fxw.ClientApp;
import com.jm.fxw.HisInfoUI;
import com.jm.fxw.LoginUI;
import com.jm.fxw.R;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;

public class ZhouBianItem extends LinearLayout implements OnClickListener {

	private Context context;

	private ZhouBian zhoubian;

	public boolean isDianPu = false;
	private String isconcerns;

	public ZhouBianItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

	}

	public void setZhouBian(ZhouBian zhoubian) {
		this.zhoubian = zhoubian;
	}

	public void initView() {
		findViewById(R.id.iv_pic).setOnClickListener(this);
		findViewById(R.id.btn_isconcerns).setOnClickListener(this);
		findViewById(R.id.lin_all).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (zhoubian.getId().equals(SessionManager.getInstance().getUserId())) {
			return;
		}
		if (!SessionManager.getInstance().isLogin()) {
			StartActivityContController.goPage(context, LoginUI.class, true);
			return;
		}
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_isconcerns:
			if (((Button) v).getText().equals("已关注")) {
				isconcerns = "0";
			}
			if (((Button) v).getText().equals(" + 关注")) {
				isconcerns = "1";
			}
			new setConcernsInfo().execute();
			break;
		case R.id.iv_pic:
		case R.id.lin_all:
			intent = new Intent(context, HisInfoUI.class);
			intent.putExtra("uid", zhoubian.getId());
			intent.putExtra("type", zhoubian.getType());
			context.startActivity(intent);

			break;
		}
	}

	/*
	 * 设置关注
	 */
	class setConcernsInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", SessionManager.getInstance().getUserId());
			map.put("touid", zhoubian.getId());
			map.put("type", SessionManager.getInstance().getUsertype());
			map.put("totype", zhoubian.getType());
			map.put("status", isconcerns);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) context.getApplicationContext())
					.getConnection();
			return conn.executeAndParse(Constant.URN_GUANZHU, getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't concerns");
				return;
			}
			if (result.isSuccessful()) {
				zhoubian.isconcerns = isconcerns.equals("0") ? "1" : "0";
				((Button) findViewById(R.id.btn_isconcerns)).setText(isconcerns
						.equals("0") ? " + 关注" : "已关注");
			}
		}
	}
}
