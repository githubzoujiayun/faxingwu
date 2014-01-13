package com.jm.fxw;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.data.DatabaseHelper;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;

public class SettingUI extends OrmLiteBaseActivity<DatabaseHelper> implements
		OnClickListener {

	private Handler mHandler;
	private static final String SCOPE = "get_simple_userinfo,add_share";
	private Tencent mTencent;
	// 新浪微博相关
	private Weibo mWeibo;
	public static Oauth2AccessToken accessToken;
	public static final String TAG = "SINA";
	private Button ok;
	private Button cancel;
	private Dialog dialog;
	private TextView progressView;
	/*
	 * SsoHandler 仅当sdk支持sso时有效，
	 */
	SsoHandler mSsoHandler;
	private String qq_keyid = "", sina_keyid = "";
	// //////////////////////////////////
	private SharedPreferences share;
	private SharedPreferences.Editor editor;
	private SessionManager sm;
	private CheckUpdate checkUpdate;

	// /////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		init();
	}

	private void init() {
		mWeibo = Weibo
				.getInstance(Constant.CONSUMER_KEY, Constant.REDIRECT_URL);
		mTencent = Tencent.createInstance(Constant.APP_ID,
				this.getApplicationContext());
		mHandler = new Handler();
		sm = SessionManager.getInstance();
		share = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
		editor = share.edit();
		if (sm.getUsertype().equals("1")) {
			findViewById(R.id.lin_setting).setVisibility(View.GONE);
		}
		findViewById(R.id.lin_setting).setOnClickListener(this);
		findViewById(R.id.lin_qq).setOnClickListener(this);
		findViewById(R.id.lin_sina).setOnClickListener(this);
		findViewById(R.id.btn_update).setOnClickListener(this);
		findViewById(R.id.btn_logout).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.lin_changeinfo).setOnClickListener(this);
		findViewById(R.id.lin_tips).setOnClickListener(this);
		findViewById(R.id.btn_clearCache).setOnClickListener(this);
		findViewById(R.id.lin_help).setOnClickListener(this);
		findViewById(R.id.lin_opinion).setOnClickListener(this);
		((Button) findViewById(R.id.btn_update)).setText("检查更新(当前版本"
				+ getVersionName() + ")");
	}

	private String getVersionName() {
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packInfo.versionName;
	}

	@Override
	protected void onResume() {

		super.onResume();
		getUserSettingInfo();
	}

	private void getUserSettingInfo() {
		new getUserInfo().execute();
	}

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
		}
	}

	/**
	 * 退出登录
	 */
	private void logout() {
		sm.setUserId("");
		sm.setUsertype("");
		editor.putString("uid", "");
		editor.putString("usertype", "");
		editor.putString("access_token", "");
		editor.putString("expires_in", "");
		editor.putString("sina_keyid", "");
		editor.putString("qq_keyid", "");
		editor.commit();
	}

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// keyid, access_token, expires_in, username, meg, type;

			LoginUI.accessToken = new Oauth2AccessToken(
					values.getString("access_token"),
					values.getString("expires_in"));
			if (LoginUI.accessToken.isSessionValid()) {
				sina_keyid = values.getString("uid");
				uploadUserInfo();
			}
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

	private void onClickLogin() {
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					LogUtil.e("=======================================IUiListener.doComplete:"
							+ values.toString());
					try {
						qq_keyid = values.getString("openid");
						uploadUserInfo();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						LogUtil.e("doComplete" + e.toString());
					}
				}
			};
			mTencent.login(this, SCOPE, listener);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(JSONObject response) {
			LogUtil.e("=======================================BaseUiListener.onComplete:"
					+ response.toString());
			doComplete(response);
		}

		protected void doComplete(JSONObject values) {
			LogUtil.e("=======================================BaseUiListener.doComplete:"
					+ values.toString());

		}

		@Override
		public void onError(UiError e) {
			LogUtil.e("=======================================onError:"
					+ "code:" + e.errorCode + ", msg:" + e.errorMessage
					+ ", detail:" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			LogUtil.e("=======================================onCancel");
		}
	}

	private void uploadUserInfo() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				LogUtil.i("开始绑定 qq_keyid= " + qq_keyid + "sina_keyid = "
						+ sina_keyid + "的信息");
				new UpLoadUserInfoTask().execute();
			}
		});
	}

	/*
	 * 上传用户信息
	 */
	class UpLoadUserInfoTask extends AsyncTask<String, Integer, Response> {

		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			if (!"".equals(qq_keyid)) {

				map.put("qq_keyid", qq_keyid);
			}
			if (!"".equals(sina_keyid)) {

				map.put("sina_keyid", sina_keyid);
			}
			map.put("uid", sm.getUserId());
			return map;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_SETID, getListInqVal());

		}

		protected void onPostExecute(Response result) {

			if (result == null) {
				LogUtil.e("can't get UpLoadUserInfoTask");
				return;
			} else {
				getUserSettingInfo();
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
			return conn.executeAndParse(Constant.URN_SETTING + "&uid="
					+ sm.getUserId());

		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {

				JSONObject jb = result.getJsonString("user_info");
				try {
					((TextView) findViewById(R.id.tv_qq)).setText((jb
							.getString("qq_keyid").equals("")) ? "未绑定" : "已绑定");
					((TextView) findViewById(R.id.tv_sina)).setText((jb
							.getString("sina_keyid").equals("")) ? "未绑定"
							: "已绑定");
				} catch (JSONException e) {
					LogUtil.e(e.toString());
				}
			} else {
				TispToastFactory.getToast(SettingUI.this, result.getMsg())
						.show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_setting:
			StartActivityContController.goPage(this, YuYueSheZhi.class, true);
			break;
		case R.id.lin_qq:
			if (((TextView) findViewById(R.id.tv_qq)).getText().equals("未绑定")) {

				onClickLogin();
			}
			break;
		case R.id.lin_sina:
			if (((TextView) findViewById(R.id.tv_sina)).getText().equals("未绑定")) {

				mWeibo.authorize(SettingUI.this, new AuthDialogListener());
			}
			break;
		case R.id.btn_update:
			checkUpdate = new CheckUpdate(SettingUI.this);
			checkUpdate.setNoNewversion(true);
			checkUpdate.check();
			break;
		case R.id.lin_help:

			StartActivityContController.goPage(this, HelpUI.class, true);
			break;
		case R.id.lin_opinion:

			StartActivityContController.goPage(this, OpinionUI.class, true);
			break;
		case R.id.btn_logout:
			ShowDialog(getString(R.string.comfirm_logout));

			break;
		case R.id.btn_leftTop:
			this.finish();
			break;
		case R.id.lin_tips:
			StartActivityContController.goPage(this, TipsSettingUI.class, true);
			break;
		case R.id.lin_changeinfo:
			StartActivityContController.goPage(this, ChangeInfo.class, true);
			break;
		case R.id.btn_clearCache:
			FinalBitmap.create(this).clearCache();
			ImageLoader.getInstance().clearDiscCache();
			ImageLoader.getInstance().clearMemoryCache();
			TispToastFactory.getToast(this, "清除缓存成功").show();
			break;
		}

	}

	private void ShowDialog(String info) {
		View dialogView = LayoutInflater.from(this).inflate(R.layout.confirm,
				null);
		ok = (Button) dialogView.findViewById(R.id.loginoutdialog_button_ok);
		cancel = (Button) dialogView
				.findViewById(R.id.loginoutdialog_button_cancel);
		dialog = new Dialog(this, R.style.MyDialog);
		progressView = (TextView) dialogView.findViewById(R.id.tv_dialog);
		dialog.setContentView(dialogView);
		progressView.setText(info);
		dialog.show();
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				logout();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
}
