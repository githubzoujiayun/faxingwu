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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Hair;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.ImageUtil;
import com.jm.util.LogUtil;
import com.jm.util.PriceUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;
import com.jm.util.WidgetUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;

public class HairInfoUI extends Activity implements OnClickListener,
		OnPageChangeListener {

	private PictureTaskCallback callback;
	private String url;
	private String to_uid = "";
	private ArrayList<Hair> alist;
	private String type = "0";
	private SessionManager sm;
	// //////////////////////////////////
	private SharedPreferences share;
	private SharedPreferences.Editor editor;
	private Tencent mTencent;
	private Weibo mWeibo;
	public static Oauth2AccessToken accessToken;
	private String qq_keyid = "", sina_keyid = "", access_token, expires_in;
	private Handler mHandler;
	private String usertype;
	private Button ok;
	private Button cancel;
	private Dialog dialog;
	private int inthid;
	private int galleryindex;
	private static final String SCOPE = "get_simple_userinfo,add_share";
	// /////////////////////////////////////////s
	private ViewPagerAdapter viewPagerAdapter;
	private boolean isPushIn;
	/**
	 * 用于管理图片的滑动
	 */
	private ViewPager viewPager;

	/**
	 * 显示当前图片的页数
	 */
	private TextView pageText;
	private String[] discount = { "9.5", "9", "8.5", "8", "7.5", "7", "6.5",
			"6", "5.5", "5", "4.5", "4", "3.5", "3", "2.5", "2", "1.5", "1",
			"0.5" };

	ArrayAdapter<String> adapter;
	private Spinner sp;
	public List<String> imageUrls = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		callback = new PictureTaskCallback();
		viewPagerAdapter = new ViewPagerAdapter(this);

		initweibo();
		mTencent = Tencent.createInstance(Constant.APP_ID,
				this.getApplicationContext());
		mHandler = new Handler();
		sm = SessionManager.getInstance();
		share = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
		editor = share.edit();
		setContentView(R.layout.hairhead);
		initView();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	// 初始化微博相关
	private void initweibo() {
		mWeibo = Weibo
				.getInstance(Constant.CONSUMER_KEY, Constant.REDIRECT_URL);
	}

	private void initView() {
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.lin_xihuan).setOnClickListener(this);
		findViewById(R.id.lin_fenxiang).setOnClickListener(this);
		findViewById(R.id.lin_sixin).setOnClickListener(this);
		findViewById(R.id.lin_comment).setOnClickListener(this);

		findViewById(R.id.btn_yuyue).setOnClickListener(this);

		Intent intent = getIntent();
		inthid = intent.getIntExtra("id", 0);
		alist = (ArrayList<Hair>) intent.getSerializableExtra("hlist");
		if (alist == null || alist.size() == 0) {
			finish();
		}
		imageUrls.clear();
		for (Hair h : alist) {
			imageUrls.add(h.getPic());
		}

		new getHairInfoTask().execute();

		pageText = (TextView) findViewById(R.id.tv_mainhead);
		viewPager = (ViewPager) findViewById(R.id.photoview);

		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(this);
		viewPager.setEnabled(false);
		for (int index = 0; index < alist.size(); index++) {
			if (alist.get(index).getId() == inthid) {
				viewPager.setCurrentItem(index);
				pageText.setText((index + 1) + "/" + imageUrls.size());
				break;
			}
		}
		new getHairInfoTask().execute();
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		Map<String, String> map = new HashMap<String, String>();
		switch (v.getId()) {
		case R.id.lin_fenxiang:

			ShowDialog();
			break;
		case R.id.lin_sixin:
			map.put("tid", to_uid);
			StartActivityContController.goPage(HairInfoUI.this, ChatUI.class,
					true, map);
			break;
		case R.id.btn_yuyue:
			if (!((Button) v).getText().equals("报价")) {
				map.put("tid", to_uid);
				map.put("hid", alist.get(galleryindex).getId() + "");
				StartActivityContController.goPage(HairInfoUI.this,
						HairItemWillDoList.class, true, map);
			} else {
				ShowDialogforAddPrice();
			}
			break;
		case R.id.btn_leftTop:
			if (isPushIn) {
				StartActivityContController.goPage(HairInfoUI.this,
						StartActivityContController.wode);
			}
			this.finish();
			break;
		case R.id.lin_comment:
			map.put("hid", alist.get(galleryindex).getId() + "");
			StartActivityContController.goPage(HairInfoUI.this,
					HairItemInfoUI.class, true, map);
			break;

		case R.id.lin_xihuan:
			/*
			 * 点击喜欢
			 */if (sm.isLogin()) {
				new LikeTask().execute();
			} else {
				goLoginPage(intent);
			}
			break;
		case R.id.iv_hairinfo_headphoto:
			goUserInfoPage();
			break;
		}

	}

	private View dialogView;

	private void ShowDialogforAddPrice() {

		dialogView = LayoutInflater.from(this).inflate(R.layout.confirmprice,
				null);

		sp = (Spinner) dialogView.findViewById(R.id.sp_discount);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, discount);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		sp.setSelection(0);
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				PriceUtil.setRealPrice(
						((EditText) dialogView.findViewById(R.id.et_serprice)),
						((Spinner) dialogView.findViewById(R.id.sp_discount)),
						((TextView) dialogView.findViewById(R.id.tv_realprice)));

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		((EditText) dialogView.findViewById(R.id.et_serprice))
				.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						PriceUtil.setRealPrice(((EditText) dialogView
								.findViewById(R.id.et_serprice)),
								((Spinner) dialogView
										.findViewById(R.id.sp_discount)),
								((TextView) dialogView
										.findViewById(R.id.tv_realprice)));

					}
				});

		ok = (Button) dialogView.findViewById(R.id.loginoutdialog_button_ok);
		cancel = (Button) dialogView
				.findViewById(R.id.loginoutdialog_button_cancel);
		dialog = new Dialog(this, R.style.MyDialog);
		dialog.setContentView(dialogView);
		dialog.show();
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addWillDo();

			}

		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	private void goUserInfoPage() {
		if (!to_uid.equals(sm.getUserId())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("uid", to_uid);
			map.put("type", usertype);
			StartActivityContController.goPage(HairInfoUI.this,
					HisInfoUI.class, false, map);
		}
	}

	private void addWillDo() {

		if (WidgetUtil.CheckAllEditTextValue((EditText) dialogView
				.findViewById(R.id.et_sertime))
				&& WidgetUtil.CheckAllEditTextValue((EditText) dialogView
						.findViewById(R.id.et_serprice))) {
			new PublicWillDoTask().execute();

		} else {
			TispToastFactory.getToast(this, "请输入完整的价格信息").show();
		}
	}

	/*
	 * 发型师添加我会做
	 */
	class PublicWillDoTask extends AsyncTask<String, Integer, Response> {
		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("work_id", alist.get(galleryindex).getId());
			map.put("long_service",
					((EditText) dialogView.findViewById(R.id.et_sertime))
							.getText().toString().trim()
							+ "小时");
			map.put("price",
					((EditText) dialogView.findViewById(R.id.et_serprice))
							.getText().toString().trim());
			map.put("rebate",
					((Spinner) dialogView.findViewById(R.id.sp_discount))
							.getSelectedItem().toString().trim());
			return map;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn
					.executeAndParse(Constant.URN_ADDWILLDO, getListInqVal());

		}

		protected void onPostExecute(Response result) {

			if (result == null) {
				LogUtil.e("error");
				return;
			} else {
				dialog.dismiss();
				TispToastFactory.getToast(HairInfoUI.this, result.getMsg())
						.show();
				new getHairInfoTask().execute();
			}
		}
	}

	private void ShowDialog() {
		dialogView = LayoutInflater.from(HairInfoUI.this).inflate(
				R.layout.choiceshare, null);
		ok = (Button) dialogView.findViewById(R.id.dialog_button_qq);
		cancel = (Button) dialogView.findViewById(R.id.dialog_button_sina);
		dialog = new Dialog(HairInfoUI.this, R.style.MyDialog);
		dialog.setContentView(dialogView);
		dialog.show();
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				shareqq();
				dialog.dismiss();
			}

		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sharesina();
				dialog.dismiss();
			}
		});
	}

	private void shareqq() {
		qq_keyid = share.getString("qq_keyid", "");
		if (!qq_keyid.equals("")) {
			LogUtil.i("开始QQ分享");
			mTencent = Tencent.createInstance(Constant.APP_ID,
					this.getApplicationContext());
			String openid = share.getString("qq_keyid", null);
			String access_token = share.getString("qq_access_token", null);
			String expires_in = share.getString("qq_expires_in", null);
			if (openid != null && access_token != null && expires_in != null) {
				mTencent.setOpenId(openid);
				mTencent.setAccessToken(access_token, expires_in + "");
				qqShare();
			}

		} else {
			LogUtil.e("QQ未登录");
			onClickLogin();

		}
	}

	private void onClickLogin() {
		IUiListener listener = new BaseUiListener() {
			@Override
			protected void doComplete(JSONObject values) {
				try {
					editor.putString("openid", values.getString("openid"));
					qq_keyid = values.getString("openid");
					editor.putString("expires_in", System.currentTimeMillis()
							+ Long.parseLong(values.getString("expires_in"))
							* 1000 + "");
					expires_in = System.currentTimeMillis()
							+ Long.parseLong(values.getString("expires_in"))
							* 1000 + "";
					editor.putString("access_token",
							values.getString("access_token"));
					access_token = values.getString("access_token");
					editor.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LogUtil.e("doComplete" + e.toString());
				}
			}

			@Override
			public void onCancel() {

				super.onCancel();
				LogUtil.e("=======================================onCancel");
			}

			@Override
			public void onError(UiError e) {

				super.onError(e);
				LogUtil.e("=======================================onError"
						+ e.toString());
			}
		};

		mTencent.login(HairInfoUI.this, SCOPE, listener);
	}

	class PictureTaskCallback extends com.jm.connection.AbstractPictureCallback {

		@Override
		public void loaded(com.jm.connection.PictureObject pObj) {

			if (isInterrupted()) {
				return;
			}
			LogUtil.e("开始weibo分享");
			mWeibo = Weibo.getInstance(Constant.CONSUMER_KEY,
					Constant.REDIRECT_URL);
			HairInfoUI.accessToken = new Oauth2AccessToken(share.getString(
					"sina_access_token", ""), share.getString(
					"sina_expires_in", ""));
			StatusesAPI sa = new StatusesAPI(accessToken);
			if (HairInfoUI.accessToken.isSessionValid()) {
				sa.upload(((TextView) findViewById(R.id.tv_utext)).getText()
						.equals("") ? "发型屋发型分享 " + url
						: ((TextView) findViewById(R.id.tv_utext)).getText()
								.toString().trim()
								+ url, ImageUtil.pictureStringExists(alist.get(
						galleryindex).getPic()), "0.0", "0.0",
						new RequestListener() {
							@Override
							public void onIOException(IOException arg0) {

								showTipInHandler("onIOException新浪微博分享失败"
										+ arg0.toString());
								LogUtil.e(arg0.toString());
							}

							@Override
							public void onError(WeiboException arg0) {

								showTipInHandler("onError新浪微博分享失败"
										+ arg0.toString());
								LogUtil.e(arg0.toString());
							}

							@Override
							public void onComplete(String arg0) {
								showTipInHandler("新浪微博分享成功");
							}
						});

			}
		}
	}

	private void sharesina() {
		sina_keyid = share.getString("sina_keyid", "");
		if (!sina_keyid.equals("")) {
			LogUtil.e("下载图片,下载完成之后开始分享");
			callback.addLarge(alist.get(galleryindex).getPic());
			callback.checkPictureTask(HairInfoUI.this);

		} else {
			LogUtil.e("SINA未登录");
			mWeibo.authorize(HairInfoUI.this, new AuthDialogListener());
		}

	}

	private void showTipInHandler(String s) {
		final String str = s;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				TispToastFactory.getToast(HairInfoUI.this, str).show();
			}
		});
	}

	private void qqShare() {
		if (mTencent.isSessionValid() && mTencent.getOpenId() != null) {

			LogUtil.e("mTencent.getOpenId()=" + mTencent.getOpenId());
			Bundle parmas = new Bundle();
			parmas.putString("title", "最新发型分享，点击跳转");// 必须。feeds的标题，最长36个中文字，超出部分会被截断。
			parmas.putString("url", url);// 必须。分享所在网页资源的链接，点击后跳转至第三方网页，
			// 请以http://开头。
			parmas.putString(
					"comment",
					((TextView) findViewById(R.id.tv_utext)).getText().equals(
							"") ? "发型屋发型分享 " + url
							: ((TextView) findViewById(R.id.tv_utext))
									.getText().toString().trim()
									+ url);// 用户评论内容，也叫发表分享时的分享理由。禁止使用系统生产的语句进行代替。最长40个中文字，超出部分会被截断。
			parmas.putString("summary",
					(((TextView) findViewById(R.id.tv_utext)).getText()
							.toString().trim()));// 所分享的网页资源的摘要内容，或者是网页的概要描述。
													// 最长80个中文字，超出部分会被截断。
			parmas.putString("images", alist.get(galleryindex).getPic());// 所分享的网页资源的代表性图片链接"，请以http://开头，长度限制255字符。多张图片以竖线（|）分隔，目前只有第一张图片有效，图片规格100*100为佳。
			parmas.putString("type", "4");// 分享内容的类型。
			mTencent.requestAsync(Constants.GRAPH_ADD_SHARE, parmas,
					Constants.HTTP_POST,
					new BaseApiListener("add_share", true), null);
		}
	}

	private void goLoginPage(Intent intent) {
		intent.setClass(this, LoginUI.class);
		startActivity(intent);
	}

	/*
	 * 读取添加喜欢详情
	 */
	class LikeTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
			findViewById(R.id.lin_xihuan).setOnClickListener(null);
		}

		protected Map<String, Object> getLikeInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("work_id", alist.get(galleryindex).getId());
			map.put("to_uid", to_uid);
			map.put("status", type);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_LIKE, getLikeInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			/*
			 * 添加喜欢的单击事件
			 */
			findViewById(R.id.lin_xihuan).setOnClickListener(HairInfoUI.this);
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				if ("0".equals(type)) {
					type = "1";
				} else {
					type = "0";
				}
				SetLikeIcon(type);
				new getHairInfoTask().execute();

			} else {
				TispToastFactory.getToast(HairInfoUI.this, result.getMsg())
						.show();
			}

		}

	}

	private void SetLikeIcon(String t) {
		if ("0".equals(t)) {
			this.type = "1";
			((ImageView) findViewById(R.id.iv_islike))
					.setImageResource(R.drawable.like);
		} else {
			this.type = "0";
			((ImageView) findViewById(R.id.iv_islike))
					.setImageResource(R.drawable.like1);
		}
	}

	/*
	 * 设置发型图片和发布者图片
	 */
	private void setHairInfo(JSONObject jb) {
		try {
			this.url = "http://wap.faxingw.cn/web.php?m=Share&a=index&id="
					+ alist.get(galleryindex).getId();
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
			if (SessionManager.getInstance().getUsertype().equals("2")
					&& usertype.equals("2")
					&& jb.getString("isWillDo").equals("0")) {
				((Button) findViewById(R.id.btn_yuyue)).setText("报价");
				findViewById(R.id.btn_yuyue).setVisibility(View.VISIBLE);
			} else if (SessionManager.getInstance().getUsertype().equals("1")
					&& usertype.equals("2")) {
				((Button) findViewById(R.id.btn_yuyue)).setText(" 预 约 ");
				findViewById(R.id.btn_yuyue).setVisibility(View.VISIBLE);
			} else if (SessionManager.getInstance().getUsertype().equals("2")
					&& usertype.equals("2")
					&& jb.getString("isWillDo").equals("1")) {
				((Button) findViewById(R.id.btn_yuyue)).setText("查看报价");
				findViewById(R.id.btn_yuyue).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.btn_yuyue).setVisibility(View.GONE);
			}
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
			map.put("work_id", alist.get(galleryindex).getId());
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
			if (result.isSuccessful()) {
				try {
					JSONObject jb = result.getJsonString("works_info");
					SetLikeIcon(jb.getString("islike"));
					setHairInfo(jb);

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
			showTipInHandler("QQ空间分享成功");
			try {
				int ret = response.getInt("ret");
				if (ret == 100030) {
					if (mNeedReAuth) {
						Runnable r = new Runnable() {
							public void run() {
								mTencent.reAuth(HairInfoUI.this, mScope,
										new BaseUiListener());
							}
						};
						HairInfoUI.this.runOnUiThread(r);
					}
				}
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

	private void uploadUserInfo() {
		if (!qq_keyid.equals("")) {
			LogUtil.i("本地保存QQ分享信息");
			editor.putString("qq_keyid", qq_keyid);
			editor.putString("qq_access_token", access_token);
			editor.putString("qq_expires_in", expires_in);
			editor.commit();
			shareqq();
		}
		if (!sina_keyid.equals("")) {
			LogUtil.i("本地保存SINA分享信息");
			editor.putString("sina_keyid", sina_keyid);
			editor.putString("sina_access_token", access_token);
			editor.putString("sina_expires_in", expires_in);
			editor.commit();
			sharesina();
		}

	}

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {

			HairInfoUI.accessToken = new Oauth2AccessToken(
					values.getString("access_token"),
					values.getString("expires_in"));
			if (HairInfoUI.accessToken.isSessionValid()) {
				access_token = values.getString("access_token");
				expires_in = values.getString("expires_in");
				sina_keyid = values.getString("uid");
				UsersAPI um = new UsersAPI(accessToken);
				um.show(Long.parseLong(sina_keyid),
						new com.weibo.sdk.android.net.RequestListener() {

							@Override
							public void onIOException(IOException arg0) {

								showTipInHandler("登录失败_" + arg0.toString());

							}

							@Override
							public void onError(WeiboException arg0) {
								showTipInHandler("登录失败_" + arg0.toString());

							}

							@Override
							public void onComplete(String result) {
								uploadUserInfo();
							}
						});
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			showTipInHandler("分享失败_" + e.toString());
		}

		@Override
		public void onCancel() {
			showTipInHandler("分享失败_" + "onCancel");
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onPageScrollStateChanged(int scrollState) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int currentPage) {

		pageText.setText((currentPage + 1) + "/" + imageUrls.size());
		if (alist.get(galleryindex).getId() != alist.get(currentPage).getId()) {
			galleryindex = currentPage;
			new getHairInfoTask().execute();
		}
	}

	/**
	 * ViewPager的适配器
	 * 
	 * @author guolin
	 */
	class ViewPagerAdapter extends PagerAdapter {
		private Context context;

		public ViewPagerAdapter(Context context) {
			this.context = context;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			View view = LayoutInflater.from(HairInfoUI.this).inflate(
					R.layout.zoom_image_layout, null);
			FinalBitmap.create(context).display(
					view.findViewById(R.id.zoom_image_view),
					imageUrls.get(position));
			container.addView(view);

			return view;
		}

		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}

	}

}
