package com.jm.fxw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Hair;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.SmallImageAdapter;
import com.jm.util.JSONUtil;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.view.HorizontalListView;

public class WodeUI_Hairer extends FinalActivity implements OnItemClickListener {

	private SmallImageAdapter likeadapter;
	private List<Hair> portfoliolist;
	private HorizontalListView likeGallery;
	private SessionManager sm;
	private String store_id = "";
	@ViewInject(id = R.id.lin_guangchang, click = "Click")
	LinearLayout lin_guangchang;
	@ViewInject(id = R.id.lin_zhaofaxing, click = "Click")
	LinearLayout lin_zhaofaxing;
	@ViewInject(id = R.id.lin_faxingshi, click = "Click")
	LinearLayout lin_faxingshi;
	@ViewInject(id = R.id.lin_collect, click = "Click")
	LinearLayout lin_collect;
	@ViewInject(id = R.id.lin_wode, click = "Click")
	LinearLayout lin_wode;
	@ViewInject(id = R.id.btn_leftTop, click = "Click")
	Button btn_leftTop;
	@ViewInject(id = R.id.btn_rightTop, click = "Click")
	Button btn_rightTop;
	@ViewInject(id = R.id.lin_renzheng, click = "Click")
	LinearLayout lin_renzheng;
	@ViewInject(id = R.id.lin_shalong, click = "Click")
	LinearLayout lin_shalong;

	@ViewInject(id = R.id.iv_minfouserpic, click = "Click")
	ImageView iv_minfouserpic;
	@ViewInject(id = R.id.lin_message, click = "Click")
	LinearLayout lin_message;
	@ViewInject(id = R.id.lin_attention_num, click = "Click")
	LinearLayout lin_attention_num;
	@ViewInject(id = R.id.lin_pingjia, click = "Click")
	LinearLayout lin_pingjia;
	@ViewInject(id = R.id.lin_yuyue, click = "Click")
	LinearLayout lin_yuyue;

	@ViewInject(id = R.id.lin_fans_num, click = "Click")
	LinearLayout lin_fans_num;
	private FinalBitmap fb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wode_hairer);
		initView();
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		MobileProbe.onPause(this, "发型师个人中心");
	}

	public void Click(View v) {
		Map<String, String> map;
		switch (v.getId()) {
		case R.id.lin_guangchang:
			StartActivityContController.goPage(this, GuangChangUI.class, false);
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
		case R.id.btn_leftTop:
			StartActivityContController.goPage(this, SettingUI.class, true);
			break;
		case R.id.btn_rightTop:
			StartActivityContController.goPage(this, PublicPortfolio.class,
					true);
			break;
		case R.id.iv_minfouserpic:
			StartActivityContController.goPage(this, ChangeInfo.class, true);
			break;
		case R.id.lin_renzheng:
			StartActivityContController.goPage(this, RenZheng.class, true);
			break;
		case R.id.lin_yuyue:
			StartActivityContController.goPage(WodeUI_Hairer.this, 102);
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
		case R.id.lin_shalong:
			if (!"".equals(store_id)) {
				map = new HashMap<String, String>();
				map.put("uid", store_id);
				StartActivityContController.goPage(this, DianPuInfoUI.class,
						true, map);
			}
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

	private void initView() {
		setInco();
		fb = FinalBitmap.create(this);
		sm = SessionManager.getInstance();
		likeadapter = new SmallImageAdapter(this);
		initGallery();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		MobileProbe.onResume(this, "发型师个人中心");
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
		new sendLocationTask().execute();
		new getUserInfo().execute();
	}

	private void initGallery() {

		likeGallery = (HorizontalListView) findViewById(R.id.like_gallery);
		likeGallery.setAdapter(likeadapter);
		likeGallery.setOnItemClickListener(this);
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
					fb.display((ImageView) findViewById(R.id.iv_minfouserpic),
							jb.getString("head_photo"));
					((TextView) findViewById(R.id.tv_fans_num)).setText(jb
							.getString("fans_num"));
					((TextView) findViewById(R.id.tv_attention_num)).setText(jb
							.getString("attention_num"));
					((TextView) findViewById(R.id.tv_collect_num)).setText(jb
							.getString("collect_num"));
					((TextView) findViewById(R.id.tv_reserve_num)).setText(jb
							.getString("reserve_num"));

					((TextView) findViewById(R.id.tv_mssage)).setText(jb
							.getString("newpm"));

					((TextView) findViewById(R.id.tv_signature)).setText(jb
							.getString("signature"));
					((TextView) findViewById(R.id.tv_score)).setText("城市:"
							+ jb.getString("city"));
					((TextView) findViewById(R.id.tv_zuopinji))
							.setText("作品集(共 " + jb.getString("works_num")
									+ " 张)");
					store_id = jb.getString("store_id");
					if (!"".equals(jb.getString("portfolio"))) {
						portfoliolist = JSONUtil.getList(jb, "portfolio",
								new Hair());

					}
					if (portfoliolist != null && portfoliolist.size() != 0) {
						likeadapter.setImageList(portfoliolist);
						likeadapter.notifyDataSetChanged();
					} else {
						LogUtil.d("portfoliolist为空");
					}
				} catch (JSONException e) {
					LogUtil.e(e.toString());
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", sm.getUserId());
		StartActivityContController.goPage(WodeUI_Hairer.this,
				WorkListUI.class, false, map);

	}

	/*
	 * 上传位置信息
	 */

	class sendLocationTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getMsgInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("lng", sm.getLng());
			map.put("lat", sm.getLat());

			map.put("city", sm.getCity());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_SENDLOCATION,
					getMsgInqVal());

		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				LogUtil.i("send location success, Lng = " + sm.getLng() + "Lat"
						+ sm.getLat());

			}
		}
	}
}
