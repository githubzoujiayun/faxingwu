package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class LoginUI extends Activity implements OnClickListener {
	// //////////////////////////////////
	private SharedPreferences share;
	private SharedPreferences.Editor editor;
	// /////////////////////////////////////////
	private Handler mHandler;
	private static final String SCOPE = "add_share";
	private static Tencent mTencent;
	// 新浪微博相关
	private Weibo mWeibo;
	public static Oauth2AccessToken accessToken;
	private String qq_keyid = "", sina_keyid = "", access_token, expires_in,
			username, meg;

	private SessionManager sm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		final Context ctxContext = this.getApplicationContext();
		share = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
		editor = share.edit();
		sm = SessionManager.getInstance();
		mTencent = Tencent.createInstance(Constant.APP_ID, ctxContext);
		mHandler = new Handler();
		setContentView(R.layout.login);
		findViewById(R.id.iv_qqlogin).setOnClickListener(this);
		initweibo();
		init();
	}

	@Override
	protected void onResume() {

		super.onResume();
		MobileProbe.onResume(this, "登录页面");
		if (sm.isLogin()) {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {

		super.onPause();
		MobileProbe.onPause(this, "登录页面");
	}

	// 初始化微博相关
	private void initweibo() {
		mWeibo = Weibo
				.getInstance(Constant.CONSUMER_KEY, Constant.REDIRECT_URL);

		findViewById(R.id.auth).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWeibo.authorize(LoginUI.this, new AuthDialogListener());
			}
		});

	}

	// 初始化返回按钮
	private void init() {
		Button button;
		button = (Button) findViewById(R.id.btn_leftTop);
		button.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			finish();
			break;
		case R.id.iv_qqlogin:
			mTencent.login(LoginUI.this, SCOPE, new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					try {
						LogUtil.e("(JSONObject values = " + values.toString());
						mTencent.setAccessToken(
								values.getString("access_token"),
								values.getString("expires_in"));
						mTencent.setOpenId(values.getString("openid"));
						editor.putString("openid", values.getString("openid"));
						qq_keyid = values.getString("openid");
						editor.putString(
								"expires_in",
								System.currentTimeMillis()
										+ Long.parseLong(values
												.getString("expires_in"))
										* 1000 + "");
						expires_in = System.currentTimeMillis()
								+ Long.parseLong(values.getString("expires_in"))
								* 1000 + "";
						editor.putString("access_token",
								values.getString("access_token"));
						access_token = values.getString("access_token");
						editor.commit();
						uploadUserInfo();
					} catch (Exception e) {
						LogUtil.e("doComplete" + e.toString());
					}
				}
			});
			break;

		}
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(JSONObject response) {
			Log.e("onComplete", "onComplete");
			doComplete(response);
		}

		protected void doComplete(JSONObject values) {

			LogUtil.e("doComplete");
		}

		@Override
		public void onCancel() {
			LogUtil.e("onCancel");
		}

		@Override
		public void onError(UiError arg0) {
			LogUtil.e("onError" + arg0.toString());

		}
	}

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// keyid, access_token, expires_in, username, meg, type;

			LoginUI.accessToken = new Oauth2AccessToken(
					values.getString("access_token"),
					values.getString("expires_in"));
			access_token = values.getString("access_token");
			expires_in = values.getString("expires_in");
			sina_keyid = values.getString("uid");
			uploadUserInfo();

		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	private void uploadUserInfo() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "正在拉取您的个人信息",
						Toast.LENGTH_SHORT).show();
				new UpLoadUserInfoTask().execute();
			}
		});
	}

	/*
	 * time out 后 充值请求
	 */

	public void netDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.getinfofail);
		builder.setMessage(R.string.netunused);
		builder.setPositiveButton(R.string.giveup,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						LoginUI.this.finish();

					}
				});
		builder.setNegativeButton(R.string.tryagain,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new UpLoadUserInfoTask().execute();
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/*
	 * 上传用户信息
	 */
	class UpLoadUserInfoTask extends AsyncTask<String, Integer, Response> {

		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sina_keyid", sina_keyid);
			map.put("qq_keyid", qq_keyid);
			return map;
		}

		@Override
		protected void onPreExecute() {
			LogUtil.e("开始上传qqid = " + qq_keyid + "的信息");
			LogUtil.e("开始上传sinaid = " + sina_keyid + "的信息");

		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_GETID, getListInqVal());

		}

		protected void onPostExecute(Response result) {

			if (result == null) {
				LogUtil.e("can't get UpLoadUserInfoTask");
				netDialog();
				return;
			} else {
				String uid = result.getString("uid");
				String type = result.getString("type");
				LogUtil.i("登录成功");
				if ("1".equals(type)) {
					saveUserInfo(uid, type);
					finish();
					// tartActivityContController.goPage(LoginUI.this,
					// StartActivityContController.wode);
				} else if ("2".equals(type)) {
					saveUserInfo(uid, type);
					finish();
					// StartActivityContController.goPage(LoginUI.this,
					// StartActivityContController.wode);
				} else {
					LogUtil.i("新用户");
					saveOpenId();
					// 完善个人资料
					Intent intent = new Intent(LoginUI.this, NewUserInfo.class);
					intent.putExtra("uid", uid);
					startActivity(intent);
				}

			}
		}

		private void saveUserInfo(String uid, String type) {
			editor.putString("uid", uid);
			editor.putString("usertype", type);
			saveOpenId();
			sm.setUserId(uid);
			sm.setUsertype(type);
		}

		private void saveOpenId() {
			editor.putString("sina_keyid", sina_keyid);
			editor.putString("qq_keyid", qq_keyid);
			if (!qq_keyid.equals("")) {
				LogUtil.i("本地保存QQ分享信息");
				editor.putString("qq_access_token", access_token);
				editor.putString("qq_expires_in", expires_in);
			}
			if (!sina_keyid.equals("")) {
				LogUtil.i("本地保存SINA分享信息");
				editor.putString("sina_access_token", access_token);
				editor.putString("sina_expires_in", expires_in);
			}
			editor.commit();
		}

	}
}
