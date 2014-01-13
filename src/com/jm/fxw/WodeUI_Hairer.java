package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WodeUI_Hairer extends Activity implements OnClickListener {

	private SessionManager sm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.wode_hairer);
		initView();
	}


	private void initView() {
		setInco();
		sm = SessionManager.getInstance();

		findViewById(R.id.lin_fans_num).setOnClickListener(this);
		findViewById(R.id.lin_attention_num).setOnClickListener(this);

		findViewById(R.id.lin_pingjia).setOnClickListener(this);
		findViewById(R.id.lin_collect).setOnClickListener(this);
		findViewById(R.id.lin_guangchang).setOnClickListener(this);
		findViewById(R.id.lin_zhaofaxing).setOnClickListener(this);
		findViewById(R.id.lin_faxingshi).setOnClickListener(this);
		findViewById(R.id.lin_wode).setOnClickListener(this);
		findViewById(R.id.lin_message).setOnClickListener(this);
		findViewById(R.id.btn_rightTop).setOnClickListener(this);
		findViewById(R.id.lin_renzheng).setOnClickListener(this);
		findViewById(R.id.lin_yuyue).setOnClickListener(this);
		findViewById(R.id.lin_setting).setOnClickListener(this);
		findViewById(R.id.lin_dongtai).setOnClickListener(this);
		findViewById(R.id.lin_zuopin).setOnClickListener(this);
		findViewById(R.id.lin_woxingwoxiu).setOnClickListener(this);

		findViewById(R.id.lin_gongjuxiang).setOnClickListener(this);
		findViewById(R.id.lin_faxingbaojia).setOnClickListener(this);
	}

	@Override
	protected void onResume() {

		super.onResume();
		if (sm.getUserId() == null || sm.getUserId().equals("")) {
			finish();
		}
		getUserInfo();
	}

	private void setInco() {

		findViewById(R.id.iv_wode).setBackgroundResource(R.drawable.wode1);
		((TextView) findViewById(R.id.tv_wode)).setTextColor(Color
				.parseColor("#f01c61"));
	}

	private void getUserInfo() {
		new getUserInfo().execute();
	}

	/*
	 * 读取个人信息
	 */
	class getUserInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_MYINFO + "&type="
					+ sm.getUsertype() + "&uid=" + sm.getUserId());

		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {

				JSONObject jb = result.getJsonString("user_info");
				try {
					((TextView) findViewById(R.id.tv_username)).setText(jb
							.getString("username"));
					ImageLoader.getInstance().displayImage(
							jb.getString("head_photo"),
							(ImageView) findViewById(R.id.iv_minfouserpic));
					((TextView) findViewById(R.id.tv_fans_num)).setText(jb
							.getString("fans_num"));
					((TextView) findViewById(R.id.tv_attention_num)).setText(jb
							.getString("attention_num"));
					((TextView) findViewById(R.id.tv_collect_num)).setText(jb
							.getString("collect_num"));

					((TextView) findViewById(R.id.tv_score)).setText("城市:"
							+ jb.getString("city"));

				} catch (JSONException e) {
					LogUtil.e(e.toString());
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		Map<String, String> map;
		switch (v.getId()) {

		case R.id.lin_zuopin:
			map = new HashMap<String, String>();
			map.put("uid", sm.getUserId());
			StartActivityContController.goPage(this, WorkListUI.class, false,
					map);
			break;

		case R.id.lin_faxingbaojia:
			map = new HashMap<String, String>();
			map.put("uid", sm.getUserId());
			StartActivityContController.goPage(this, WorkDoListUI.class, true,
					map);
			break;
		case R.id.lin_dongtai:
			StartActivityContController.goPage(this, TongChengUI.class, false);
			break;
		case R.id.lin_gongjuxiang:
			StartActivityContController.goPage(this, 104);
			break;
		case R.id.lin_woxingwoxiu:
			StartActivityContController.goPage(this, DongTaiUI.class, false);
			break;
		case R.id.lin_guangchang:
			StartActivityContController.goPage(this, GuangChangUI.class, true);
			break;
		case R.id.lin_zhaofaxing:
			StartActivityContController.goPage(this, ZhaofaxingUI.class, false);
			break;
		case R.id.lin_faxingshi:
			StartActivityContController.goPage(this, FaxingshiUI.class, false);
			break;
		case R.id.lin_wode:
			StartActivityContController.goPage(this,
					StartActivityContController.wode);
			break;
		case R.id.lin_message:
			StartActivityContController.goPage(this, MsgUI.class, true);
			break;

		case R.id.btn_rightTop:
			StartActivityContController.goPage(this, 105);
			break;

		case R.id.lin_renzheng:
			StartActivityContController.goPage(this, RenZheng.class, true);
			break;
		case R.id.lin_yuyue:
			StartActivityContController.goPage(WodeUI_Hairer.this, 102);
			break;
		case R.id.lin_setting:
			StartActivityContController.goPage(this, SettingUI.class, true);
			break;
		case R.id.lin_pingjia:
			map = new HashMap<String, String>();
			map.put("uid", sm.getUserId());
			StartActivityContController.goPage(this, RatingListUI.class, true,
					map);
			break;

		case R.id.lin_collect:

			map = new HashMap<String, String>();
			map.put("uid", sm.getUserId());
			StartActivityContController.goPage(this, CollectListUI.class, true,
					map);
			break;

		case R.id.lin_attention_num:

			if ("0".equals(((TextView) findViewById(R.id.tv_attention_num))
					.getText())) {
				return;
			}
			map = new HashMap<String, String>();
			map.put("type", "watchlist");
			StartActivityContController.goPage(this, UserListUI.class, true,
					map);
			break;
		case R.id.lin_fans_num:
			if ("0".equals(((TextView) findViewById(R.id.tv_fans_num))
					.getText())) {
				return;
			}
			map = new HashMap<String, String>();
			map.put("type", "fanlist");
			StartActivityContController.goPage(this, UserListUI.class, true,
					map);
			break;
		}
	}

}
