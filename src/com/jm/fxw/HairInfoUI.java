package com.jm.fxw;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import ru.truba.touchgallery.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.iflytek.ui.RecognizerDialog;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Hair;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.ImageUtil;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;
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

public class HairInfoUI extends Activity implements OnClickListener {

	private PictureTaskCallback callback;
	private String url;
	private String to_uid = "";
	private ArrayList<Hair> alist = new ArrayList<Hair>();
	private String type = "0";
	private SessionManager sm;
	private FinalBitmap fbPic;
	// //////////////////////////////////
	private SharedPreferences share;
	private SharedPreferences.Editor editor;
	private Tencent mTencent;
	private Weibo mWeibo;
	public static Oauth2AccessToken accessToken;
	private String qq_keyid = "", sina_keyid = "", access_token, expires_in;
	private EditText ed_comment;
	private Handler mHandler;
	private String usertype;
	private int inthid;
	private int galleryindex;
	private static final String SCOPE = "get_simple_userinfo,add_share";
	// /////////////////////////////////////////s
	private SharedPreferences mSharedPreferences;
	// ʶ��Dialog
	private RecognizerDialog iatDialog;

	private boolean isPushIn;
	// ��ʼ������
	private GalleryViewPager mViewPager;

	private String[] urls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		callback = new PictureTaskCallback();
		// ��ʼ���������.S
		mSharedPreferences = getSharedPreferences(getPackageName(),
				MODE_PRIVATE);

		// ///////////////////////////////
		fbPic = FinalBitmap.create(this);
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
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "����ͼ��ҳ��");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "����ͼ��ҳ��");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	// ��ʼ��΢�����
	private void initweibo() {
		mWeibo = Weibo
				.getInstance(Constant.CONSUMER_KEY, Constant.REDIRECT_URL);
	}

	private void initView() {
		ed_comment = (EditText) findViewById(R.id.et_comment);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.lin_xihuan).setOnClickListener(this);
		findViewById(R.id.lin_fenxiang).setOnClickListener(this);
		findViewById(R.id.lin_sixin).setOnClickListener(this);
		findViewById(R.id.lin_comment).setOnClickListener(this);

		findViewById(R.id.btn_yuyue).setOnClickListener(this);
		// mgadapter = new MerchantGalleryAdapter(this, bitmap);
		// FallingGallery gallery = (FallingGallery)
		// findViewById(R.id.gallery_photos);
		// gallery.setAdapter(mgadapter);
		// gallery.setOnItemSelectedListener(this);
		Intent intent = getIntent();
		inthid = intent.getIntExtra("id", 0);

		alist = (ArrayList<Hair>) intent.getSerializableExtra("hlist");
		LogUtil.e("alist.size() = " + alist.size());

		// mgadapter.setList(alist);
		// mgadapter.notifyDataSetChanged();
		LogUtil.e("��ʼ����������");

		// ////////////////////////////
		int i = 0;
		urls = new String[alist.size()];
		for (Hair h : alist) {
			urls[i++] = h.getPic();
		}
		i = 0;
		for (String s : urls) {
			LogUtil.e("i = " + i++ + s);
		}
		List<String> items = new ArrayList<String>();
		Collections.addAll(items, urls);

		UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
		pagerAdapter.setOnItemChangeListener(new OnItemChangeListener() {
			@Override
			public void onItemChange(int currentPosition) {
				((TextView) findViewById(R.id.tv_mainhead)).setText("����չʾ("
						+ (currentPosition + 1) + "/" + (alist.size()) + ")");

				if (alist.get(galleryindex).getId() != alist.get(
						currentPosition).getId()) {
					LogUtil.e("galleryindex = " + galleryindex);
					galleryindex = currentPosition;
					new getHairInfoTask().execute();
				}
			}
		});

		mViewPager = (GalleryViewPager) findViewById(R.id.gallery_photos);
		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setAdapter(pagerAdapter);
		for (int index = 0; index < alist.size(); index++) {
			if (alist.get(index).getId() == inthid) {
				mViewPager.setCurrentItem(index);
				((TextView) findViewById(R.id.tv_mainhead)).setText("��������("
						+ (index + 1) + "/" + (alist.size()) + ")");
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
			// ��ԤԼ����
			if (((Button) v).getText().equals("ԤԼTA")) {
				map.put("tid", to_uid);
				StartActivityContController.goPage(HairInfoUI.this,
						YuYueUI.class, true, map);
			} else {
				goUserInfoPage();
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
			LogUtil.e("hid = " + alist.get(galleryindex).getId() + "");
			StartActivityContController.goPage(HairInfoUI.this,
					HairItemInfoUI.class, true, map);
			break;

		case R.id.btn_ok:
			/*
			 * ��������
			 */
			if ("".equals(ed_comment.getText().toString().trim())) {
				TispToastFactory.getToast(HairInfoUI.this, "��������������").show();
			}
			if (sm.isLogin()) {
				/*
				 * ȡ�����뷨
				 */
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(HairInfoUI.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				new PublicCommentTask().execute();
			} else {
				goLoginPage(intent);
			}
			break;
		case R.id.lin_xihuan:
			/*
			 * ���ϲ��
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

	private void goUserInfoPage() {
		if (!to_uid.equals(sm.getUserId())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("uid", to_uid);
			map.put("type", usertype);
			StartActivityContController.goPage(HairInfoUI.this,
					HisInfoUI.class, false, map);
		}

	}

	/*
	 * �û���������
	 */
	class PublicCommentTask extends AsyncTask<String, Integer, Response> {
		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("from_uid", sm.getUserId());
			map.put("works_id", alist.get(galleryindex).getId());
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

				TispToastFactory.getToast(HairInfoUI.this, result.getMsg())
						.show();
				ed_comment.setText("");

			} else {

				TispToastFactory.getToast(HairInfoUI.this, result.getMsg())
						.show();
			}
		}
	}

	private Button ok;
	private Button cancel;
	private Dialog dialog;

	private void ShowDialog() {
		View dialogView = LayoutInflater.from(HairInfoUI.this).inflate(
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
		// TODO Auto-generated method stub
		qq_keyid = share.getString("qq_keyid", "");
		if (!qq_keyid.equals("")) {
			LogUtil.i("��ʼQQ����");
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
			LogUtil.e("QQδ��¼");
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

					mTencent.requestAsync(Constants.GRAPH_SIMPLE_USER_INFO,
							null, Constants.HTTP_GET, new LoginApiListener(
									"get_simple_userinfo", false), null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LogUtil.e("doComplete" + e.toString());
				}
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				super.onCancel();
				LogUtil.e("=======================================onCancel");
			}

			@Override
			public void onError(UiError e) {
				// TODO Auto-generated method stub
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
			LogUtil.e("��ʼweibo����");
			mWeibo = Weibo.getInstance(Constant.CONSUMER_KEY,
					Constant.REDIRECT_URL);
			HairInfoUI.accessToken = new Oauth2AccessToken(share.getString(
					"sina_access_token", ""), share.getString(
					"sina_expires_in", ""));
			StatusesAPI sa = new StatusesAPI(accessToken);
			if (HairInfoUI.accessToken.isSessionValid()) {
				sa.upload(((TextView) findViewById(R.id.tv_utext)).getText()
						.equals("") ? "�����ݷ��ͷ��� " + url
						: ((TextView) findViewById(R.id.tv_utext)).getText()
								.toString().trim()
								+ url, ImageUtil.pictureStringExists(alist.get(
						galleryindex).getPic()), "0.0", "0.0",
						new RequestListener() {
							@Override
							public void onIOException(IOException arg0) {
								// TODO Auto-generated method stub
								showTipInHandler("onIOException����΢������ʧ��"
										+ arg0.toString());
								LogUtil.e(arg0.toString());
							}

							@Override
							public void onError(WeiboException arg0) {
								// TODO Auto-generated method stub
								showTipInHandler("onError����΢������ʧ��"
										+ arg0.toString());
								LogUtil.e(arg0.toString());
							}

							@Override
							public void onComplete(String arg0) {
								showTipInHandler("����΢������ɹ�");
								addPoint();
							}
						});

			}
		}
	}

	private void sharesina() {
		sina_keyid = share.getString("sina_keyid", "");
		if (!sina_keyid.equals("")) {
			LogUtil.e("����ͼƬ,�������֮��ʼ����");
			callback.addLarge(alist.get(galleryindex).getPic());
			callback.checkPictureTask(HairInfoUI.this);

		} else {
			LogUtil.e("SINAδ��¼");
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

	private void addPoint() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				new addPointTask().execute();
			}
		});
	}

	/*
	 * �û����ӻ�������
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

	private void qqShare() {
		if (mTencent.isSessionValid() && mTencent.getOpenId() != null) {

			LogUtil.e("mTencent.getOpenId()=" + mTencent.getOpenId());
			Bundle parmas = new Bundle();
			parmas.putString("title", "���·��ͷ��������ת");// ���롣feeds�ı��⣬�36�������֣��������ֻᱻ�ضϡ�
			parmas.putString("url", url);// ���롣����������ҳ��Դ�����ӣ��������ת����������ҳ��
			// ����http://��ͷ��
			parmas.putString(
					"comment",
					((TextView) findViewById(R.id.tv_utext)).getText().equals(
							"") ? "�����ݷ��ͷ��� " + url
							: ((TextView) findViewById(R.id.tv_utext))
									.getText().toString().trim()
									+ url);// �û��������ݣ�Ҳ�з������ʱ�ķ������ɡ���ֹʹ��ϵͳ�����������д��档�40�������֣��������ֻᱻ�ضϡ�
			parmas.putString("summary",
					(((TextView) findViewById(R.id.tv_utext)).getText()
							.toString().trim()));// ���������ҳ��Դ��ժҪ���ݣ���������ҳ�ĸ�Ҫ������
													// �80�������֣��������ֻᱻ�ضϡ�
			parmas.putString("images", alist.get(galleryindex).getPic());// ���������ҳ��Դ�Ĵ�����ͼƬ����"������http://��ͷ����������255�ַ�������ͼƬ�����ߣ�|���ָ���Ŀǰֻ�е�һ��ͼƬ��Ч��ͼƬ���100*100Ϊ�ѡ�
			parmas.putString("type", "4");// �������ݵ����͡�
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
	 * ��ȡ���ϲ������
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
			 * ���ϲ���ĵ����¼�
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
	 * ���÷���ͼƬ�ͷ�����ͼƬ
	 */
	private void setHairInfo(JSONObject jb) {
		try {
			this.url = "http://wap.faxingw.cn/web.php?m=Share&a=index&id="
					+ alist.get(galleryindex).getId();
			usertype = jb.getString("type");
			to_uid = jb.getString("uid");
			findViewById(R.id.iv_hairinfo_headphoto).setOnClickListener(this);
			fbPic.display((ImageView) findViewById(R.id.iv_hairinfo_headphoto),
					jb.getString("head_photo"));
			if (to_uid.equals(sm.getUserId())) {
				findViewById(R.id.lin_sixin).setVisibility(View.GONE);
			} else {

				findViewById(R.id.lin_sixin).setVisibility(View.VISIBLE);
			}
			if (usertype.equals("2") && !to_uid.equals(sm.getUserId())) {
				findViewById(R.id.btn_yuyue).setVisibility(View.VISIBLE);
				if (jb.getString("clear_reserve").equals("0")) {
					((Button) findViewById(R.id.btn_yuyue)).setText("�鿴����ʦ");
				}
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
	 * ��ȡ��������
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
	 * QQ �������
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
			showTipInHandler("QQ�ռ����ɹ�");
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
				// azrael 2/1ע�͵���, ����Ϊ��Ҫ��api���ص�ʱ������token��,
				// ���cgi���ص�ֵû��token, ������ԭ����token
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

	private void uploadUserInfo() {
		if (!qq_keyid.equals("")) {
			LogUtil.i("���ر���QQ������Ϣ");
			editor.putString("qq_keyid", qq_keyid);
			editor.putString("qq_access_token", access_token);
			editor.putString("qq_expires_in", expires_in);
			editor.commit();
			shareqq();
		}
		if (!sina_keyid.equals("")) {
			LogUtil.i("���ر���SINA������Ϣ");
			editor.putString("sina_keyid", sina_keyid);
			editor.putString("sina_access_token", access_token);
			editor.putString("sina_expires_in", expires_in);
			editor.commit();
			sharesina();
		}

	}

	// ������ʸ�����Ϣ�ص�����
	private class LoginApiListener implements IRequestListener {

		private String mScope = "all";
		private Boolean mNeedReAuth = false;

		public LoginApiListener(String scope, boolean needReAuth) {
			mScope = scope;
			mNeedReAuth = needReAuth;
		}

		@Override
		public void onComplete(final JSONObject response, Object state) {
			doComplete(response, state);
		}

		protected void doComplete(JSONObject response, Object state) {
			uploadUserInfo();
		}

		@Override
		public void onConnectTimeoutException(ConnectTimeoutException arg0,
				Object arg1) {

			showTipInHandler("����ʧ��_" + "onConnectTimeoutException");

		}

		@Override
		public void onHttpStatusException(HttpStatusException arg0, Object arg1) {
			// TODO Auto-generated method stub

			showTipInHandler("����ʧ��_" + "onHttpStatusException");

		}

		@Override
		public void onIOException(IOException arg0, Object arg1) {
			// TODO Auto-generated method stub

			showTipInHandler("����ʧ��_" + "onIOException");
		}

		@Override
		public void onJSONException(JSONException arg0, Object arg1) {
			// TODO Auto-generated method stub

			showTipInHandler("����ʧ��_" + "onJSONException");

		}

		@Override
		public void onMalformedURLException(MalformedURLException arg0,
				Object arg1) {

			showTipInHandler("����ʧ��_" + "onMalformedURLException");

		}

		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException arg0, Object arg1) {

			showTipInHandler("����ʧ��_" + "onNetworkUnavailableException");

		}

		@Override
		public void onSocketTimeoutException(SocketTimeoutException arg0,
				Object arg1) {

			showTipInHandler("����ʧ��_" + "onSocketTimeoutException");

		}

		@Override
		public void onUnknowException(Exception arg0, Object arg1) {

			showTipInHandler("����ʧ��_" + "onUnknowException");

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

								showTipInHandler("��¼ʧ��_" + arg0.toString());

							}

							@Override
							public void onError(WeiboException arg0) {
								showTipInHandler("��¼ʧ��_" + arg0.toString());

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
			showTipInHandler("����ʧ��_" + e.toString());
		}

		@Override
		public void onCancel() {
			showTipInHandler("����ʧ��_" + "onCancel");
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}
}
