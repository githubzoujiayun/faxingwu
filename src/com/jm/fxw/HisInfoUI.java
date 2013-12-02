package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.jm.util.TispToastFactory;
import com.jm.view.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HisInfoUI extends FinalActivity implements OnItemClickListener,
		OnClickListener {
	private SmallImageAdapter likeadapter;
	private List<Hair> portfoliolist = new ArrayList<Hair>();
	private HorizontalListView likeGallery;
	private SessionManager sm;
	private List<Hair> mlist = new ArrayList<Hair>();
	LinearLayout lin_message;
	private String uid;
	private String type;
	private String isconcerns;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hisinfo);
		initView();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "查看他人详情页面");
	}

	private void initView() {
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_rightTop).setOnClickListener(this);
		findViewById(R.id.btn_hisbtn).setOnClickListener(this);
		findViewById(R.id.lin_sixin).setOnClickListener(this);
		findViewById(R.id.lin_pingjia).setOnClickListener(this);
		findViewById(R.id.img_hair1).setOnClickListener(this);
		findViewById(R.id.img_hair2).setOnClickListener(this);
		findViewById(R.id.img_hair3).setOnClickListener(this);
		Intent i = getIntent();
		uid = i.getStringExtra("uid");
		type = i.getStringExtra("type");
		sm = SessionManager.getInstance();
		likeadapter = new SmallImageAdapter(this);
		initGallery();

	}

	private void setViewByUserType(String t, String s) {
		findViewById(R.id.lin_btns).setVisibility(View.VISIBLE);
		if ("1".equals(t)) {
			findViewById(R.id.lin_pingjia).setVisibility(View.INVISIBLE);
			findViewById(R.id.lin_sixin).setVisibility(View.INVISIBLE);
			findViewById(R.id.lin_shalongInfo).setVisibility(View.GONE);
			((Button) findViewById(R.id.btn_hisbtn)).setText("私信");
		} else {
			findViewById(R.id.lin_btns).setVisibility(View.VISIBLE);
			findViewById(R.id.lin_collect).setVisibility(View.GONE);
			findViewById(R.id.lin_collectlist).setVisibility(View.GONE);
			if ("1".equals(s)) {
				((Button) findViewById(R.id.btn_hisbtn)).setText("预约发型师");
				findViewById(R.id.btn_hisbtn).setEnabled(true);
			} else {
				findViewById(R.id.btn_hisbtn).setEnabled(false);
				((Button) findViewById(R.id.btn_hisbtn))
						.setBackgroundResource(R.drawable.btn_sixin_ablefalse);
				((Button) findViewById(R.id.btn_hisbtn)).setText("暂未开通预约");
			}
		}
		findViewById(R.id.btn_hisbtn).setVisibility(View.VISIBLE);
	}

	/*
	 * 读取收购藏列表
	 */
	class GetCollectListTask extends AsyncTask<String, Integer, Response> {

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_MYCOLLECTLIST_LIST
					+ "&page=" + 1, getInfoInqVal());

		}

		protected void onPostExecute(Response result) {

			if (result == null) {

				LogUtil.e("can't get collectlist");
				return;
			} else {
				if (!"".equals(result.getString("works_info"))) {
					mlist = result.getList("works_info", new Hair());
				}
				if (mlist == null || mlist.size() == 0) {
					TispToastFactory.getToast(HisInfoUI.this, "暂无数据");
					findViewById(R.id.lin_hair1).setVisibility(View.GONE);
					findViewById(R.id.lin_hair2).setVisibility(View.GONE);
					findViewById(R.id.lin_hair3).setVisibility(View.GONE);
				}
				try {
					if (mlist.get(0).getPic() == null
							|| "".equals(mlist.get(0).getPic())) {
						findViewById(R.id.lin_hair1).setVisibility(View.GONE);
					} else {
						findViewById(R.id.lin_hair1)
								.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(
								mlist.get(0).getPic(),
								(ImageView) findViewById(R.id.img_hair1));
					}
					if (mlist.get(1).getPic() == null
							|| "".equals(mlist.get(1).getPic())) {
						findViewById(R.id.lin_hair2).setVisibility(View.GONE);
					} else {
						findViewById(R.id.lin_hair2)
								.setVisibility(View.VISIBLE);

						ImageLoader.getInstance().displayImage(
								mlist.get(1).getPic(),
								(ImageView) findViewById(R.id.img_hair2));
					}
					if (mlist.get(2).getPic() == null
							|| "".equals(mlist.get(2).getPic())) {
						findViewById(R.id.lin_hair3).setVisibility(View.GONE);

					} else {
						findViewById(R.id.lin_hair3)
								.setVisibility(View.VISIBLE);

						ImageLoader.getInstance().displayImage(
								mlist.get(2).getPic(),
								(ImageView) findViewById(R.id.img_hair3));
					}

				} catch (Exception e) {
					LogUtil.e(e.toString());
				}
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "查看他们详情页面");
		getUserInfo();
	}

	private void getUserInfo() {
		new getUserInfo().execute();
		new GetCollectListTask().execute();
		new getShaLongInfo().execute();
	}

	private void initGallery() {

		likeGallery = (HorizontalListView) findViewById(R.id.his_gallery);
		likeGallery.setAdapter(likeadapter);
		likeGallery.setOnItemClickListener(this);
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
			map.put("uid", sm.getUserId());
			map.put("touid", uid);
			map.put("type", sm.getUsertype());
			map.put("totype", type);
			map.put("status", isconcerns);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_GUANZHU, getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't concerns");
				return;
			}
			if (result.isSuccessful()) {

				((Button) findViewById(R.id.btn_rightTop)).setText(isconcerns
						.equals("0") ? "添加关注" : "取消关注");
			}
		}
	}

	/*
	 * 添 读取店铺信息
	 */
	class getShaLongInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_STORE_INFO,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get ShaLongInfo");
				return;
			}
			if (result.isSuccessful()) {
				JSONObject jb = result.getJsonString("store_info");
				try {
					((TextView) findViewById(R.id.tv_dname)).setText(jb
							.getString("store_name"));
					((TextView) findViewById(R.id.tv_dphone)).setText(jb
							.getString("telephone"));
					((TextView) findViewById(R.id.tv_daddress)).setText(jb
							.getString("store_address"));
				} catch (JSONException e) {
					LogUtil.e(e.toString());
				}
			}
		}
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
			return conn.executeAndParse(Constant.URN_MYINFO + "&type=" + type
					+ "&uid=" + uid + "&to_uid=" + sm.getUserId());

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
					((TextView) findViewById(R.id.tv_signature)).setText(jb
							.getString("signature"));
					((TextView) findViewById(R.id.tv_score)).setText("地址:"
							+ jb.getString("city"));
					((TextView) findViewById(R.id.tv_rate_num)).setText(jb
							.getString("assess_num"));
					((TextView) findViewById(R.id.tv_zuopinji))
							.setText("作品集(共 " + jb.getString("works_num")
									+ " 张)");

					setViewByUserType(type, jb.getString("clear_reserve"));
					if (!"".equals(jb.getString("portfolio"))) {
						portfoliolist = JSONUtil.getList(jb, "portfolio",
								new Hair());

					}
					if (portfoliolist != null && portfoliolist.size() != 0) {
						likeadapter.setImageList(portfoliolist);
						likeadapter.notifyDataSetChanged();
					} else {
						LogUtil.i("portfoliolist为空");
					}

					((Button) findViewById(R.id.btn_rightTop)).setText(jb
							.getString("isconcerns").equals("0") ? "添加关注"
							: "取消关注");
				} catch (Exception e) {
					LogUtil.e(e.toString());
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid);
		StartActivityContController.goPage(HisInfoUI.this, WorkListUI.class,
				false, map);
	}

	@Override
	public void onClick(View v) {
		Map<String, String> map = new HashMap<String, String>();
		switch (v.getId()) {
		case R.id.btn_leftTop:

			finish();
			break;
		case R.id.btn_rightTop:
			// 加关注
			if (sm.isLogin()) {
				if (((Button) v).getText().equals("取消关注")) {
					isconcerns = "0";
				}
				if (((Button) v).getText().equals("添加关注")) {
					isconcerns = "1";
				}
				new setConcernsInfo().execute();
			} else {
				Intent i = new Intent();
				i.setClass(HisInfoUI.this, LoginUI.class);
				startActivity(i);
			}

			break;

		case R.id.img_hair1:
		case R.id.img_hair2:
		case R.id.img_hair3:
			map.put("uid", uid);
			StartActivityContController.goPage(HisInfoUI.this,
					CollectListUI.class, false, map);
			break;
		case R.id.btn_hisbtn:
			if (((Button) v).getText().equals("预约发型师")) {
				// 打开预约界面
				map.put("tid", uid);
				StartActivityContController.goPage(HisInfoUI.this,
						YuYueUI.class, true, map);
			} else if (((Button) v).getText().equals("私信")) {

				map.put("tid", uid);
				StartActivityContController.goPage(HisInfoUI.this,
						ChatUI.class, true, map);
			}
			break;
		case R.id.lin_sixin:
			map.put("tid", uid);
			StartActivityContController.goPage(HisInfoUI.this, ChatUI.class,
					true, map);
			break;
		case R.id.lin_pingjia:
			map.put("uid", uid);
			StartActivityContController.goPage(HisInfoUI.this,
					RatingListUI.class, true, map);
			break;
		default:
			break;
		}

	}
}
