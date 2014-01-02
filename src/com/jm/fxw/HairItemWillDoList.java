package com.jm.fxw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Comment;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.CommentAdapter;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;
import com.jm.view.HorizontalListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weibo.sdk.android.Oauth2AccessToken;

public class HairItemWillDoList extends Activity implements OnClickListener {

	private CommentAdapter adapter;
	private ListView listView;
	private List<Comment> mlist;
	private String to_uid = "";
	private HorizontalListView likeGallery;
	private SessionManager sm;
	public static Oauth2AccessToken accessToken;
	private Handler mHandler;
	private String usertype;
	private String inthid;
	private boolean isPushIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		sm = SessionManager.getInstance();
		setContentView(R.layout.hairwilldoinfo);
		initView();

		new getHairInfoTask().execute();
	}

	@Override
	protected void onResume() {

		super.onResume();
		MobileProbe.onResume(this, "预约作品详情页面");
	}

	@Override
	protected void onPause() {

		super.onPause();
		MobileProbe.onPause(this, "预约作品详情页面");
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.willdo_list);
		View view = LayoutInflater.from(this).inflate(R.layout.hairitemhead,
				null);
		listView.addHeaderView(view);
		adapter = new CommentAdapter(this);
		listView.setAdapter(adapter);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.lin_sixin).setOnClickListener(this);
		findViewById(R.id.btn_yuyue).setOnClickListener(this);
		findViewById(R.id.iv_photo).setOnClickListener(this);
		findViewById(R.id.iv_hairinfo_headphoto).setOnClickListener(this);
		likeGallery = (HorizontalListView) findViewById(R.id.hairinfo_like);
		likeGallery.setVisibility(View.GONE);
		inthid = getIntent().getStringExtra("hid");
		isPushIn = getIntent().getBooleanExtra("isPushIn", false);
	}

	public void onClick(View v) {
		Map<String, String> map = new HashMap<String, String>();
		switch (v.getId()) {
		case R.id.btn_yuyue:
			// 打开预约界面
			if (((Button) v).getText().equals("预约TA")) {
				map.put("tid", to_uid);
				StartActivityContController.goPage(HairItemWillDoList.this,
						YuYueUI.class, true, map);
			} else {
				goUserInfoPage();
			}
			break;
		case R.id.btn_leftTop:
			if (isPushIn) {
				StartActivityContController.goPage(HairItemWillDoList.this,
						StartActivityContController.wode);
			}
			this.finish();
			break;
		case R.id.iv_hairinfo_headphoto:
			goUserInfoPage();
			break;
		case R.id.iv_photo:
			finish();
			break;

		}

	}

	private void goUserInfoPage() {
		if (!to_uid.equals(sm.getUserId())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("uid", to_uid);
			map.put("type", usertype);
			StartActivityContController.goPage(HairItemWillDoList.this,
					HisInfoUI.class, false, map);
		}

	}

	private void showTipInHandler(String s) {
		final String str = s;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				TispToastFactory.getToast(HairItemWillDoList.this, str).show();
			}
		});
	}

	/*
	 * 用户增加积分评论
	 */
	class addPointTask extends AsyncTask<String, Integer, Response> {
		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("score", "1");
			map.put("status", "1");
			return map;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_ADDPOINT, getListInqVal());

		}

		protected void onPostExecute(Response result) {

			LogUtil.i("addPointTask onPostExecute");
		}
	}

	private void goLoginPage(Intent intent) {
		intent.setClass(this, LoginUI.class);
		startActivity(intent);
	}

	/*
	 * 设置发型图片和发布者图片
	 */
	private void setHairInfo(Response jb) {
		usertype = jb.getString("type");
		to_uid = jb.getString("uid");
		findViewById(R.id.iv_hairinfo_headphoto).setOnClickListener(this);
		ImageLoader.getInstance().displayImage(jb.getString("head_photo"),
				(ImageView) findViewById(R.id.iv_hairinfo_headphoto));

		// 设置发型图片大小
		FinalBitmap.create(this).display(findViewById(R.id.iv_photo),
				jb.getString("work_image"),
				getWindowManager().getDefaultDisplay().getWidth() / 2,
				getWindowManager().getDefaultDisplay().getHeight() / 2);

		StringBuffer sb = new StringBuffer();
		sb.append("服务时长" + jb.getString("long_service"));
		sb.append("服务价格" + jb.getString("price"));
		sb.append("折扣" + jb.getString("rebate"));
		((TextView) findViewById(R.id.tv_utext)).setText(sb.toString());
		((TextView) findViewById(R.id.tv_uname)).setText(jb
				.getString("username"));
	}

	/*
	 * 读取发型详情
	 */
	class getHairInfoTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getChangeInfoInqVal() {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("lng", SessionManager.getInstance().getLng());
			map.put("lat", SessionManager.getInstance().getLat());
			map.put("work_id", inthid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_WILLDOINFO,
					getChangeInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can not get hair info");
				return;
			}
			if (result.isSuccessful()) {
				setHairInfo(result);

			} else {
				showTipInHandler(result.getMsg());
			}
		}
	}

	/*
	 * 读取评论列表
	 */
	class getWillDoListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getChangeInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("work_id", inthid);
			map.put("city", SessionManager.getInstance().getCity());
			map.put("lng", SessionManager.getInstance().getLng());
			map.put("lat", SessionManager.getInstance().getLat());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_HAIRINFOWILLDO_LIST,
					getChangeInfoInqVal());
		}

		protected void onPostExecute(Response result) {

			if (result == null) {
				LogUtil.e("can not get hair info");
				return;
			}
			if (result.isSuccessful()) {
				adapter.clear();
				if (!"".equals(result.getString("comment_list"))) {
					mlist = result.getList("comment_list", new Comment());
					if (mlist != null) {
						adapter.setTypeList(mlist);
						adapter.notifyDataSetChanged();
						((TextView) findViewById(R.id.tv_commentsize))
								.setText("共有" + mlist.size() + "条评论");
					} else {
						((TextView) findViewById(R.id.tv_commentsize))
								.setText("暂无评论");
					}
				} else {
					((TextView) findViewById(R.id.tv_commentsize))
							.setText("暂无评论");
				}
			}
		}
	}

}
