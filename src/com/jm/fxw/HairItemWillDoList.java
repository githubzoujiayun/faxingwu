package com.jm.fxw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.WillDo;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.WillDoAdapter;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;
import com.jm.view.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HairItemWillDoList extends Activity implements OnClickListener {

	private WillDoAdapter adapter;
	private ListView listView;
	private List<WillDo> mlist;
	private String to_uid = "";
	private HorizontalListView likeGallery;
	private SessionManager sm;
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
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.willdo_list);
		View view = LayoutInflater.from(this).inflate(R.layout.hairitemhead,
				null);
		listView.addHeaderView(view);
		adapter = new WillDoAdapter(this);
		listView.setAdapter(adapter);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.iv_photo).setOnClickListener(this);
		findViewById(R.id.iv_hairinfo_headphoto).setOnClickListener(this);
		findViewById(R.id.btn_yuyue).setVisibility(View.VISIBLE);
		findViewById(R.id.btn_yuyue).setOnClickListener(this);
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
			map.put("tid", to_uid);
			map.put("hid", inthid);
			StartActivityContController.goPage(HairItemWillDoList.this,
					YuYueUI.class, true, map);
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
		sb.append("时长" + jb.getString("long_service"));
		sb.append("价格" + jb.getString("price"));

		StringBuffer sb2 = new StringBuffer();
		sb2.append("实际价格" + jb.getString("reserve_price"));
		sb2.append("(" + jb.getString("rebate") + "折)");
		((TextView) findViewById(R.id.tv_utext)).setText(sb.toString());

		findViewById(R.id.tv_utext2).setVisibility(View.VISIBLE);
		findViewById(R.id.lin_address).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.tv_utext2)).setText(sb2.toString());
		((TextView) findViewById(R.id.tv_uname)).setText(jb
				.getString("username"));
		((TextView) findViewById(R.id.tv_address)).setText(jb
				.getString("store_address"));
		((TextView) findViewById(R.id.tv_distance)).setText(jb
				.getString("distance"));
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
			new getWillDoListTask().execute();
			if (result.isSuccessful()) {
				setHairInfo(result);

			} else {
				showTipInHandler(result.getMsg());
			}
		}
	}

	/*
	 * 读取WillDo列表
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
				if (!"".equals(result.getString("willdo_list"))) {
					mlist = result.getList("willdo_list", new WillDo());
					if (mlist != null) {
						adapter.setTypeList(mlist);
						adapter.notifyDataSetChanged();
						((TextView) findViewById(R.id.tv_commentsize))
								.setText("周边共有" + mlist.size() + "个发型师报价此发型");
					} else {
						((TextView) findViewById(R.id.tv_commentsize))
								.setText("周边暂无发型师报价此发型");
					}
				} else {
					((TextView) findViewById(R.id.tv_commentsize))
							.setText("周边暂无发型师报价此发型");
				}
			}
		}
	}

}
