package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
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

import com.baidu.mobstat.StatService;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Hair;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.SmallImageAdapter;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.view.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HisInfoUI extends FinalActivity implements OnItemClickListener,
		OnClickListener {
	private SmallImageAdapter yuanchuangadapter;
	private HorizontalListView yuanchuangGallery;
	private SmallImageAdapter huizuoadapter;
	private HorizontalListView huizuoGallery;
	LinearLayout lin_message;
	private String uid;
	private String type;
	private String isconcerns;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.hisinfo);
		initView();
	}

	@Override
	protected void onResume() {
		StatService.onResume(this);
		super.onResume();
		new getUserInfo().execute();

	}

	@Override
	protected void onPause() {

		StatService.onPause(this);
		super.onPause();
	}

	private void initView() {
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_rightTop).setOnClickListener(this);
		findViewById(R.id.btn_yuyue).setOnClickListener(this);
		findViewById(R.id.btn_sixin).setOnClickListener(this);
		findViewById(R.id.lin_pingjia).setOnClickListener(this);
		findViewById(R.id.lin_collect).setOnClickListener(this);
		findViewById(R.id.lin_question).setOnClickListener(this);

		Intent i = getIntent();
		uid = i.getStringExtra("uid");
		if (uid == null || "".equals(uid)
				|| SessionManager.getInstance().getUserId().equals(uid)) {
			finish();
		}
		type = i.getStringExtra("type");
		initGallery();

	}

	private void setViewByUserType(String type, String stutes) {
		if ("1".equals(type)) {
			findViewById(R.id.lin_pingjia).setVisibility(View.GONE);
			findViewById(R.id.lin_shalongInfo).setVisibility(View.GONE);
			findViewById(R.id.fl_huizuo).setVisibility(View.GONE);
			findViewById(R.id.btn_yuyue).setVisibility(View.GONE);
			if ("2".equals(SessionManager.getInstance().getUsertype())) {
				findViewById(R.id.lin_question).setVisibility(View.VISIBLE);
			}
		} else {

			findViewById(R.id.lin_pingjia).setVisibility(View.VISIBLE);
			findViewById(R.id.lin_shalongInfo).setVisibility(View.VISIBLE);
			findViewById(R.id.fl_huizuo).setVisibility(View.VISIBLE);

			if ("1".equals(stutes)) {
				findViewById(R.id.btn_yuyue).setVisibility(View.VISIBLE);
			}
		}
	}

	private void initGallery() {
		yuanchuangadapter = new SmallImageAdapter(this);
		yuanchuangGallery = (HorizontalListView) findViewById(R.id.yuanchuang_gallery);
		yuanchuangGallery.setAdapter(yuanchuangadapter);
		yuanchuangGallery.setOnItemClickListener(this);

		huizuoadapter = new SmallImageAdapter(this);
		huizuoGallery = (HorizontalListView) findViewById(R.id.huizuo_gallery);
		huizuoGallery.setAdapter(huizuoadapter);
		huizuoGallery.setOnItemClickListener(this);
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
			map.put("touid", uid);
			map.put("type", SessionManager.getInstance().getUsertype());
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
	 * 读取个人信息
	 */
	class getUserInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getUserInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("type", type);
			map.put("to_uid", SessionManager.getInstance().getUserId());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_VISITINFO,
					getUserInfoInqVal());

		}

		protected void onPostExecute(Response result) {
			findViewById(R.id.lin_loadhisinfofinish)
					.setVisibility(View.VISIBLE);
			findViewById(R.id.lin_loadhisinfo).setVisibility(View.GONE);
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {

				((TextView) findViewById(R.id.tv_username)).setText(result
						.getString("username"));

				ImageLoader.getInstance().displayImage(
						result.getString("head_photo"),
						(ImageView) findViewById(R.id.iv_minfouserpic));
				((TextView) findViewById(R.id.tv_signature)).setText(result
						.getString("signature"));
				((TextView) findViewById(R.id.tv_score)).setText("地址:"
						+ result.getString("city"));
				((TextView) findViewById(R.id.tv_rate_num)).setText("查看评价:"
						+ result.getString("assess_num"));

				yuanchuangadapter.setImageList(result.getList("worksInfo",
						new Hair()));
				yuanchuangadapter.notifyDataSetChanged();
				huizuoadapter.setImageList(result.getList("willdoInfo",
						new Hair()));
				huizuoadapter.notifyDataSetChanged();

				((Button) findViewById(R.id.btn_rightTop)).setText(result
						.getString("isconcerns").equals("0") ? "添加关注" : "取消关注");

				((TextView) findViewById(R.id.tv_dname)).setText(result
						.getString("store_name"));
				((TextView) findViewById(R.id.tv_dphone)).setText(result
						.getString("telephone"));
				((TextView) findViewById(R.id.tv_daddress)).setText(result
						.getString("store_address"));

				setViewByUserType(type, result.getString("status"));

			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		if (arg0.equals(huizuoGallery)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("uid", uid);
			StartActivityContController.goPage(HisInfoUI.this,
					WorkDoListUI.class, false, map);
		} else {

			Map<String, String> map = new HashMap<String, String>();
			map.put("uid", uid);
			StartActivityContController.goPage(HisInfoUI.this,
					WorkListUI.class, false, map);
		}
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
			if (SessionManager.getInstance().isLogin()) {
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

		case R.id.btn_yuyue:
			map.put("tid", uid);
			StartActivityContController.goPage(HisInfoUI.this, YuYueUI.class,
					true, map);
			break;
		case R.id.btn_sixin:
			map.put("tid", uid);
			StartActivityContController.goPage(HisInfoUI.this, ChatUI.class,
					true, map);
			break;
		case R.id.lin_pingjia:
			map.put("uid", uid);
			StartActivityContController.goPage(HisInfoUI.this,
					RatingListUI.class, true, map);
			break;
		case R.id.lin_collect:
			map.put("uid", uid);
			StartActivityContController.goPage(HisInfoUI.this,
					CollectListUI.class, true, map);
			break;
		case R.id.lin_question:
			map.put("uid", uid);
			StartActivityContController.goPage(HisInfoUI.this,
					QuestionListUI.class, true, map);
			break;

		default:
			break;
		}

	}
}
