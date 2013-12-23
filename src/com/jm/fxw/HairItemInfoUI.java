package com.jm.fxw;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Comment;
import com.jm.entity.Like;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.CommentAdapter;
import com.jm.sort.LikeImageAdapter;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;
import com.jm.view.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.weibo.sdk.android.Oauth2AccessToken;

public class HairItemInfoUI extends Activity implements OnClickListener,
		OnItemClickListener, RecognizerDialogListener {

	private String url;
	private CommentAdapter adapter;
	private ListView listView;
	private List<Comment> mlist;
	private String to_uid = "";
	private HorizontalListView likeGallery;
	private List<Like> likelist;
	private LikeImageAdapter likeadapter;
	private String type = "0";
	private SessionManager sm;
	// //////////////////////////////////
	private SharedPreferences share;
	private SharedPreferences.Editor editor;
	private Tencent mTencent;
	public static Oauth2AccessToken accessToken;
	private FrameLayout fl;
	private EditText ed_comment;
	private Handler mHandler;
	private String usertype;
	private String inthid;
	// /////////////////////////////////////////s
	// 识别Dialog
	private RecognizerDialog iatDialog;

	private boolean isPushIn;
	// 初始化参数
	private String mInitParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mInitParams = "appid=" + "522405c5";
		iatDialog = new RecognizerDialog(this, mInitParams);
		iatDialog.setListener(this);
		// 初始化缓存对象.S

		// ///////////////////////////////
		mTencent = Tencent.createInstance(Constant.APP_ID,
				this.getApplicationContext());
		mHandler = new Handler();
		sm = SessionManager.getInstance();
		share = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
		editor = share.edit();
		setContentView(R.layout.hairinfo);
		initView();

	}

	@Override
	protected void onResume() {
		
		super.onResume();
		MobileProbe.onResume(this, "发型详情页面");
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		MobileProbe.onPause(this, "发型详情页面");
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.comment_list);
		View view = LayoutInflater.from(this).inflate(R.layout.hairitemhead,
				null);
		listView.addHeaderView(view);
		adapter = new CommentAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		ed_comment = (EditText) findViewById(R.id.et_comment);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.lin_xihuan).setOnClickListener(this);
		findViewById(R.id.lin_fenxiang).setOnClickListener(this);
		findViewById(R.id.lin_sixin).setOnClickListener(this);
		findViewById(R.id.btn_yuyue).setOnClickListener(this);
		findViewById(R.id.iv_voice).setOnClickListener(this);
		findViewById(R.id.iv_photo).setOnClickListener(this);
		findViewById(R.id.iv_hairinfo_headphoto).setOnClickListener(this);

		// mgadapter = new MerchantGalleryAdapter(this, bitmap);
		// FallingGallery gallery = (FallingGallery)
		// findViewById(R.id.gallery_photos);
		// gallery.setAdapter(mgadapter);
		// gallery.setOnItemSelectedListener(this);
		likeadapter = new LikeImageAdapter(this);
		likeGallery = (HorizontalListView) findViewById(R.id.hairinfo_like);
		likeGallery.setAdapter(likeadapter);
		likeGallery.setOnItemClickListener(this);
		inthid = getIntent().getStringExtra("hid");
		isPushIn = getIntent().getBooleanExtra("isPushIn", false);
		new getHairInfoTask().execute();
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		Map<String, String> map = new HashMap<String, String>();
		switch (v.getId()) {
		case R.id.iv_voice:
			showIatDialog();
			break;

		case R.id.lin_sixin:
			map.put("tid", to_uid);
			StartActivityContController.goPage(HairItemInfoUI.this,
					ChatUI.class, true, map);
			break;
		case R.id.btn_yuyue:
			// 打开预约界面
			if (((Button) v).getText().equals("预约TA")) {
				map.put("tid", to_uid);
				StartActivityContController.goPage(HairItemInfoUI.this,
						YuYueUI.class, true, map);
			} else {
				goUserInfoPage();
			}
			break;
		case R.id.btn_leftTop:
			if (isPushIn) {
				StartActivityContController.goPage(HairItemInfoUI.this,
						StartActivityContController.wode);
			}
			this.finish();
			break;
		case R.id.btn_ok:
			/*
			 * 发布评论
			 */
			if ("".equals(ed_comment.getText().toString().trim())) {
				TispToastFactory.getToast(HairItemInfoUI.this, "请输入评论内容")
						.show();
			}
			if (sm.isLogin()) {
				/*
				 * 取消输入法
				 */
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(HairItemInfoUI.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				new PublicCommentTask().execute();
			} else {
				goLoginPage(intent);
			}
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
			StartActivityContController.goPage(HairItemInfoUI.this,
					HisInfoUI.class, false, map);
		}

	}

	/*
	 * 用户发布评论
	 */
	class PublicCommentTask extends AsyncTask<String, Integer, Response> {
		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("from_uid", sm.getUserId());
			map.put("works_id", inthid);
			map.put("content", ed_comment.getText().toString().trim());
			return map;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_COMMENT, getListInqVal());

		}

		protected void onPostExecute(Response result) {

			if (result == null) {
				LogUtil.e("can not publish comment");
				return;
			} else if (result.isSuccessful()) {

				TispToastFactory.getToast(HairItemInfoUI.this, result.getMsg())
						.show();
				ed_comment.setText("");
				new getCommentListTask().execute();

			} else {

				TispToastFactory.getToast(HairItemInfoUI.this, result.getMsg())
						.show();
			}
		}
	}

	private void showTipInHandler(String s) {
		final String str = s;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				TispToastFactory.getToast(HairItemInfoUI.this, str).show();
			}
		});
	}

	private void addPoint() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				new addPointTask().execute();
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
	private void setHairInfo(JSONObject jb) {
		try {
			this.url = "http://wap.faxingw.cn/web.php?m=Share&a=index&id="
					+ inthid;
			usertype = jb.getString("type");
			to_uid = jb.getString("uid");
			findViewById(R.id.iv_hairinfo_headphoto).setOnClickListener(this);
			ImageLoader.getInstance().displayImage(jb.getString("head_photo"),
					(ImageView) findViewById(R.id.iv_hairinfo_headphoto));
			if (to_uid.equals(sm.getUserId())) {
				findViewById(R.id.lin_sixin).setVisibility(View.GONE);
			} else {

				findViewById(R.id.lin_sixin).setVisibility(View.VISIBLE);
			}
			if (usertype.equals("2") && !to_uid.equals(sm.getUserId())) {
				findViewById(R.id.btn_yuyue).setVisibility(View.VISIBLE);
				if (jb.getString("clear_reserve").equals("0")) {
					((Button) findViewById(R.id.btn_yuyue)).setText("查看发型师");
				}
			} else {
				findViewById(R.id.btn_yuyue).setVisibility(View.GONE);
			}
			JSONArray ja = jb.getJSONArray("works_pic");

			LogUtil.e(ja.get(0).toString());
			// 设置发型图片大小
			
			
			
			FinalBitmap.create(this).display(findViewById(R.id.iv_photo),
					ja.get(0).toString(),
					getWindowManager().getDefaultDisplay().getWidth() / 2,
					getWindowManager().getDefaultDisplay().getHeight() / 2);

			((TextView) findViewById(R.id.tv_utext)).setText(jb
					.getString("content"));
			((TextView) findViewById(R.id.tv_uname)).setText(jb
					.getString("username"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			map.put("uid", sm.getUserId());
			map.put("work_id", inthid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_HAIRINFO_LIST,
					getChangeInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can not get hair info");
				return;
			}
			new getCommentListTask().execute();
			if (result.isSuccessful()) {
				try {
					JSONObject jb = result.getJsonString("works_info");
					setHairInfo(jb);
					if (!"".equals(result.getString("like_list"))) {
						likelist = result.getList("like_list", new Like());
						if (likelist != null) {
							likeadapter.setLikeList(likelist);
							likeadapter.notifyDataSetChanged();
							findViewById(R.id.ll_hairinfo_like_gallery)
									.setVisibility(View.VISIBLE);
						}
						if (likelist == null || likelist.size() == 0) {
							findViewById(R.id.ll_hairinfo_like_gallery)
									.setVisibility(View.GONE);
						}
					} else {
						findViewById(R.id.ll_hairinfo_like_gallery)
								.setVisibility(View.GONE);

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					LogUtil.e(e.toString());
				}

			} else {
				showTipInHandler(result.getMsg());
			}
		}
	}

	/*
	 * 读取评论列表
	 */
	class getCommentListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getChangeInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("works_id", inthid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_COMMENT_LIST,
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

	/*
	 * QQ 分享监听
	 */

	private class BaseApiListener implements IRequestListener {
		private String mScope = "add_share";
		private Boolean mNeedReAuth = false;

		public BaseApiListener(String scope, boolean needReAuth) {
			mScope = scope;
			mNeedReAuth = needReAuth;
		}

		@Override
		public void onComplete(final JSONObject response, Object state) {
			showResult("IRequestListener.onComplete:", response.toString());
			doComplete(response, state);
		}

		protected void doComplete(JSONObject response, Object state) {

			addPoint();
			showTipInHandler("QQ空间分享成功");
			try {
				int ret = response.getInt("ret");
				if (ret == 100030) {
					if (mNeedReAuth) {
						Runnable r = new Runnable() {
							public void run() {
								mTencent.reAuth(HairItemInfoUI.this, mScope,
										new BaseUiListener());
							}
						};
						HairItemInfoUI.this.runOnUiThread(r);
					}
				}
				// azrael 2/1注释掉了, 这里为何要在api返回的时候设置token呢,
				// 如果cgi返回的值没有token, 则会清空原来的token
				// String token = response.getString("access_token");
				// String expire = response.getString("expires_in");
				// String openid = response.getString("openid");
				// mTencent.setAccessToken(token, expire);
				// mTencent.setOpenId(openid);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("toddtest", response.toString());
			}

		}

		@Override
		public void onIOException(final IOException e, Object state) {
			showTipInHandler("IRequestListener.onIOException:");
		}

		@Override
		public void onMalformedURLException(final MalformedURLException e,
				Object state) {
			showTipInHandler("IRequestListener.onMalformedURLException");
		}

		@Override
		public void onJSONException(final JSONException e, Object state) {
			showTipInHandler("IRequestListener.onJSONException:");
		}

		@Override
		public void onConnectTimeoutException(ConnectTimeoutException arg0,
				Object arg1) {
			showTipInHandler("IRequestListener.onConnectTimeoutException:");

		}

		@Override
		public void onSocketTimeoutException(SocketTimeoutException arg0,
				Object arg1) {
			showTipInHandler("IRequestListener.SocketTimeoutException:");
		}

		@Override
		public void onUnknowException(Exception arg0, Object arg1) {
			showTipInHandler("IRequestListener.onUnknowException:");
		}

		@Override
		public void onHttpStatusException(HttpStatusException arg0, Object arg1) {
			showTipInHandler("IRequestListener.HttpStatusException:");
		}

		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException arg0, Object arg1) {
			showTipInHandler("IRequestListener.onNetworkUnavailableException:");
		}
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(JSONObject response) {
			LogUtil.e("onComplete response= " + response);
			LogUtil.e(response.toString());
			doComplete(response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			showResult("onError:", "code:" + e.errorCode + ", msg:"
					+ e.errorMessage + ", detail:" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			showResult("onCancel", "");
		}
	}

	private void showResult(final String base, final String msg) {
		LogUtil.e("base = " + base);

		LogUtil.e("msg = " + msg);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent;
		if (parent.equals(likeGallery)) {
			if (likeadapter.getList() == null || position < 0
					|| position >= likeadapter.getList().size()) {
				LogUtil.e("position = " + position);
				return;
			}
			Like like = likeadapter.getList().get(position);
			if (!like.getId().equals(sm.getUserId())) {
				intent = new Intent(HairItemInfoUI.this, HisInfoUI.class);
				intent.putExtra("uid", like.getId());
				intent.putExtra("type", like.getType());
				startActivity(intent);
			}
		}

		if (parent.equals(listView)) {
			if (adapter.getList() == null || position - 1 < 0
					|| position - 1 >= adapter.getList().size()) {
				LogUtil.e("position = " + (position - 1));
				return;
			}
			Comment comment = adapter.getList().get(position - 1);
			if (!comment.getUid().equals(sm.getUserId())) {
				intent = new Intent(HairItemInfoUI.this, HisInfoUI.class);
				intent.putExtra("uid", comment.getUid());
				intent.putExtra("type", comment.getType());
				startActivity(intent);
			}
		}
	}

	@Override
	public void onEnd(SpeechError arg0) {
		

	}

	@Override
	public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
		StringBuilder builder = new StringBuilder();
		for (RecognizerResult recognizerResult : results) {
			builder.append(recognizerResult.text);
		}
		((EditText) findViewById(R.id.et_comment)).append(builder);
		((EditText) findViewById(R.id.et_comment))
				.setSelection(((EditText) findViewById(R.id.et_comment))
						.length());

	}

	/**
	 * 显示转写对话框.
	 * 
	 * @param
	 */
	public void showIatDialog() {
		// 获取引擎参数
		String engine = "sms";
		// 获取area参数，POI搜索时需要传入.
		String area = null;
		if (TextUtils.isEmpty(area))
			area = "";
		else
			area += ",";
		// 设置转写Dialog的引擎和poi参数.
		iatDialog.setEngine(engine, area, null);
		iatDialog.setSampleRate(RATE.rate16k);
		// 弹出转写Dialog.
		iatDialog.show();
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}

}
