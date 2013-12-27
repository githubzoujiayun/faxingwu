package com.jm.fxw;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.CameraAndGallery;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 手机号码验证页面
 * 
 * @author win
 * 
 */
public class ChangeInfo extends Activity implements OnClickListener {
	private Bitmap mCurrentBitMap1;
	private String UploadUrl1 = "";
	private Handler handler;

	public static final int CHECK_MOBILE_OK = 113;
	protected Button btn_leftTop;
	protected ImageButton btn_about, btn_update, btn_help;
	protected TextView tv_mainhead, tv_tophead, tv_uphead;
	private Button btn_getvcode;
	private EditText etxt_vcode, etxt_mobile;
	private CameraAndGallery cag;
	private String vcode = "";
	private String sex = "";
	private int handlerTime = 60;
	private SessionManager sm;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.changeinfo);
		init();
		getUserInfo();

	}

	private void init() {
		cag = new CameraAndGallery(this);
		sm = SessionManager.getInstance();
		etxt_mobile = (EditText) findViewById(R.id.etxt_mobile);
		etxt_vcode = (EditText) findViewById(R.id.etxt_vcode);
		btn_getvcode = (Button) findViewById(R.id.btn_getvcode);
		btn_getvcode.setOnClickListener(this);
		findViewById(R.id.btn_checkmobile).setOnClickListener(this);
		etxt_vcode = (EditText) findViewById(R.id.etxt_vcode);
		handler = new Handler();
		findViewById(R.id.btn_checkagain).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_changinfo).setOnClickListener(this);
		findViewById(R.id.iv_minfouserpic).setOnClickListener(this);
	}

	private void getUserInfo() {
		new getUserInfo().execute();

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobileProbe.onResume(this, "修改资料页面");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobileProbe.onPause(this, "修改资料页面");
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
			return conn.executeAndParse(Constant.URN_GETCHANGINFO + "&uid="
					+ sm.getUserId());

		}

		protected void onPostExecute(Response result) {
			if (result.isSuccessful()) {

				JSONObject jb = result.getJsonString("user_info");
				try {
					((TextView) findViewById(R.id.et_username)).setText(jb
							.getString("username"));

					ImageLoader.getInstance().displayImage(
							jb.getString("head_photo"),
							(ImageView) findViewById(R.id.iv_minfouserpic));

					if ("0".equals(jb.getString("sex"))) {
						((RadioButton) findViewById(R.id.rb_female))
								.setChecked(true);
						((RadioButton) findViewById(R.id.rb_male))
								.setChecked(false);
						sex = "0";

					}

					if ("1".equals(jb.getString("sex"))) {
						((RadioButton) findViewById(R.id.rb_female))
								.setChecked(false);
						((RadioButton) findViewById(R.id.rb_male))
								.setChecked(true);
						sex = "1";
					}
					((EditText) findViewById(R.id.et_city)).setText(jb
							.getString("city"));
					((EditText) findViewById(R.id.et_qq)).setText(jb
							.getString("qq"));
					((TextView) findViewById(R.id.tv_mobile)).setText(jb
							.getString("mobile"));
					((EditText) findViewById(R.id.et_signature)).setText(jb
							.getString("signature"));
				} catch (JSONException e) {
					LogUtil.e(e.toString());
				}
			} else {
				TispToastFactory.getToast(ChangeInfo.this, result.getMsg())
						.show();
			}
		}
	}

	/**
	 * 单击事件
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			this.finish();
			break;
		case R.id.btn_getvcode:
			doGetvcode();
			break;
		case R.id.btn_checkmobile:
			// 验证手机
			checkVcode();
			break;
		case R.id.btn_checkagain:
			findViewById(R.id.lin_checkmobile).setVisibility(View.VISIBLE);
			break;
		case R.id.btn_changinfo:
			setUserSex();
			new ChangUserInfo().execute();
			break;

		case R.id.iv_minfouserpic:
			cag.doPickPhotoAction();
			break;
		}

	}

	private void showPicture(Uri uri) {
		if (uri == null) {
			LogUtil.e("When showPicture Uri = null");
			return;
		}
		mCurrentBitMap1 = com.jm.util.ImageUtil.compressImageFromFile(uri
				.getPath());
		if (mCurrentBitMap1 == null) {
			LogUtil.e("mCurrentBitMap1 = cag.resizeBitmap(uri) ,从本地选择");
			mCurrentBitMap1 = cag.resizeBitmap(uri);
		} else {
			LogUtil.e(" mCurrentBitMap1 = com.jm.util.ImageUtil.compressImageFromFile(uri.getPath())，从相机拍摄");
		}
		((ImageView) findViewById(R.id.iv_minfouserpic))
				.setImageBitmap(mCurrentBitMap1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		LogUtil.i("requestCode = " + requestCode);
		LogUtil.i("intent = " + intent);
		LogUtil.i("resultCode = " + resultCode);
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case CameraAndGallery.PHOTO_PICKED_WITH_DATA:
			showPicture(intent.getData());
			break;
		case CameraAndGallery.CAMERA_WITH_DATA:
			showPicture(cag.getCameraPictureUri());
			break;
		}
	}

	private void setUserSex() {
		if (((RadioButton) findViewById(R.id.rb_female)).isChecked()) {
			sex = "0";
		} else {
			sex = "1";
		}
	}

	/**
	 * 判断用户数据
	 */
	private void doGetvcode() {
		if (etxt_mobile.getText() == null
				|| etxt_mobile.getText().toString().trim().equals("")) {
			TispToastFactory.getToast(this, "请填写手机号码").show();
			return;
		}
		Pattern p = Pattern
				.compile("(^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\\d{8}$)");
		Matcher m = p.matcher(etxt_mobile.getText().toString().trim());
		if (!m.matches()) {
			TispToastFactory.getToast(this, "电话号码错误").show();
			return;
		}
		new GetvcodeTask().execute();

	}

	protected Map<String, Object> getVcodeFormatInqVal(String feild) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", etxt_mobile.getText().toString().trim());
		return map;
	}

	protected Map<String, Object> getChangeInfoInqVal() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", sm.getUserId());
		map.put("sex", sex);
		map.put("city", ((EditText) findViewById(R.id.et_city)).getText()
				.toString().trim());
		map.put("head_photo", UploadUrl1);
		map.put("qq", ((EditText) findViewById(R.id.et_qq)).getText()
				.toString().trim());
		map.put("username", ((EditText) findViewById(R.id.et_username))
				.getText().toString().trim());
		map.put("signature", ((EditText) findViewById(R.id.et_signature))
				.getText().toString().trim());
		return map;
	}

	class ChangUserInfo extends AsyncTask<String, Integer, Response> {

		protected void onPreExecute() {

		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			File file = cag.getNewUploadFile();
			String fileId = null;
			if (mCurrentBitMap1 != null) {
				cag.storeBitmapFile(mCurrentBitMap1, file);
				LogUtil.d("Upload First pic" + file.getAbsolutePath());
				String uploadFileName = file.getAbsolutePath();
				fileId = conn.uploadFile(new File(uploadFileName));
				if (fileId == null || fileId.equals("")) {
					LogUtil.e("First Pic Upload Failed");
				} else {
					LogUtil.e("上传图片路径= " + fileId);
					UploadUrl1 = fileId;
				}
			}
			Response res = conn.executeAndParse(Constant.URN_CHANGEINFO,
					getChangeInfoInqVal());

			return res;
		}

		@Override
		protected void onPostExecute(Response result) {

			if (result.isSuccessful()) {
				TispToastFactory.getToast(ChangeInfo.this, result.getMsg())
						.show();
				finish();
			} else {
				TispToastFactory.getToast(ChangeInfo.this, result.getMsg())
						.show();
			}
		}
	}

	class UpLoadImage extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			File file = cag.getNewUploadFile();
			String fileId = null;
			if (mCurrentBitMap1 != null) {
				cag.storeBitmapFile(mCurrentBitMap1, file);
				LogUtil.d("Upload First pic" + file.getAbsolutePath());
				String uploadFileName = file.getAbsolutePath();
				fileId = conn.uploadFile(new File(uploadFileName));
				if (fileId == null || fileId.equals("")) {
					LogUtil.e("First Pic Upload Failed");
					return "";
				} else {
					LogUtil.e("上传图片路径= " + fileId);
					UploadUrl1 = fileId;
					return UploadUrl1;
				}
			}
			return "";
		}

		@Override
		protected void onPostExecute(String res) {
			if (!res.equals("")) {

			}
		}
	}

	/*
	 * 获取验证码
	 */
	class GetvcodeTask extends AsyncTask<String, Integer, Response> {

		protected void onPreExecute() {

			TispToastFactory.getToast(ChangeInfo.this, "验证码正在发送中").show();
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			Response res = conn.executeAndParse(Constant.URN_GETVCODE,
					getVcodeFormatInqVal(""));

			return res;
		}

		@Override
		protected void onPostExecute(Response result) {
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				TispToastFactory.getToast(ChangeInfo.this, "验证码已发送,请输入验证码")
						.show();
				vcode = result.getString("verify").toString().trim();
				handlerTime = 60;
				handler.post(timeback);
				btn_getvcode.setEnabled(false);
			}

		}
	}

	/**
	 * 用户信息非空检测
	 */
	private void checkVcode() {
		if (etxt_vcode.getText() == null
				|| etxt_vcode.getText().toString().trim().equals("")) {
			TispToastFactory.getToast(ChangeInfo.this, "请输入验证码").show();
			return;
		}
		if (!etxt_vcode.getText().toString().trim().equals(vcode)) {
			TispToastFactory.getToast(ChangeInfo.this, "验证码输入错误").show();
			return;
		}
		new CheckMobilesuccessTask().execute();

	}

	/**
	 * 手机验证成功告知服务器
	 * 
	 * @author win
	 * 
	 */
	class CheckMobilesuccessTask extends AsyncTask<String, Integer, Response> {

		protected Map<String, Object> getVcodeSuccessFormatInqVal(String feild) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("mobile", etxt_mobile.getText().toString().trim());
			map.put("uid", sm.getUserId());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			Response res = conn.executeAndParse(Constant.URN_VCOCESUCCESS,
					getVcodeSuccessFormatInqVal(""));
			return res;
		}

		@Override
		protected void onPostExecute(Response result) {
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				TispToastFactory.getToast(ChangeInfo.this, result.getMsg())
						.show();
				finish();
			} else {
				TispToastFactory.getToast(ChangeInfo.this, result.getMsg())
						.show();
			}
		}
	}

	Runnable timeback = new Runnable() {

		@Override
		public void run() {
			if (handlerTime >= 0) {
				btn_getvcode.setText(handlerTime + "S");
				handler.postDelayed(this, 1000);
				handlerTime--;
			} else {
				btn_getvcode.setText("获取验证码");
				btn_getvcode.setEnabled(true);
			}
		}
	};
}
