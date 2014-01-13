package com.jm.fxw;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jm.citylist.CityList;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.CameraAndGallery;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

/**
 * 手机号码验证页面
 * 
 * @author win
 * 
 */
public class NewUserInfo extends Activity implements OnClickListener {
	private Bitmap mCurrentBitMap1;
	private String UploadUrl1 = "";
	private ProgressDialog dialog;
	public static final int CHECK_MOBILE_OK = 113;
	protected Button btn_leftTop;
	protected ImageButton btn_about, btn_update, btn_help;
	protected TextView tv_mainhead, tv_tophead, tv_uphead;
	private EditText etxt_mobile;
	private CameraAndGallery cag;
	private String type = "1";
	private String uid = "";
	private SharedPreferences share;
	private SharedPreferences.Editor editor;

	// /////////////////////////////////////////
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.newuserinfo);
		init();

	}

	private void init() {
		share = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
		editor = share.edit();
		cag = new CameraAndGallery(NewUserInfo.this);
		etxt_mobile = (EditText) findViewById(R.id.et_mobile);
		uid = getIntent().getStringExtra("uid");
		LogUtil.e("注册uid为" + uid + "的信息");
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.btn_rightTop).setOnClickListener(this);
		findViewById(R.id.iv_minfouserpic).setOnClickListener(this);
		findViewById(R.id.iv_minfouserpic).setOnClickListener(this);

		findViewById(R.id.tv_city).setOnClickListener(this);
		RadioGroup group = (RadioGroup) findViewById(R.id.rg_type);
		// 绑定一个匿名监听器
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {

				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) NewUserInfo.this
						.findViewById(radioButtonId);
				// 更新文本内容，以符合选中项
				if (rb.getText().toString().trim().equals("普通用户")) {
					((TextView) findViewById(R.id.tv_nickname)).setText("昵称:");
					findViewById(R.id.lin_shalonginfo).setVisibility(View.GONE);
				} else {
					((TextView) findViewById(R.id.tv_nickname)).setText("姓名:");
					findViewById(R.id.lin_shalonginfo).setVisibility(
							View.VISIBLE);
				}
			}
		});

	}

	@Override
	protected void onResume() {

		super.onResume();
		((EditText) findViewById(R.id.tv_city)).setText(SessionManager
				.getInstance().getCity());
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

	/**
	 * 单击事件
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_rightTop:
			setUserType();
			if (CheckInfoValue()) {
				new ChangUserInfo().execute();
			}
			break;

		case R.id.iv_minfouserpic:
			cag.doPickPhotoAction();
			break;

		case R.id.tv_city:
			startActivity(new Intent(NewUserInfo.this, CityList.class));
			break;
		// case R.id.iv_lockwise1:
		// if (mCurrentBitMap1 == null) {
		// break;
		// }
		// mCurrentBitMap1 = lockwise(
		// ((ImageView) findViewById(R.id.iv_minfouserpic)),
		// mCurrentBitMap1);
		// break;
		}

	}

	// private Bitmap lockwise(ImageView imageView, Bitmap mCurrentBitMap) {
	// matrix.setRotate(90);
	// mCurrentBitMap = Bitmap.createBitmap(mCurrentBitMap, 0, 0,
	// mCurrentBitMap.getWidth(), mCurrentBitMap.getHeight(), matrix,
	// true);
	// imageView.setImageBitmap(mCurrentBitMap);
	// return mCurrentBitMap;
	//
	// }

	private void setUserType() {

		if (((RadioButton) findViewById(R.id.rb_user)).isChecked()) {
			type = "1";
		} else {
			type = "2";
		}
	}

	class setStoreInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("city", ((EditText) findViewById(R.id.tv_city)).getText()
					.toString().trim());
			map.put("store_name", ((EditText) findViewById(R.id.tv_dname))
					.getText().toString().trim());
			map.put("address", ((EditText) findViewById(R.id.tv_daddress))
					.getText().toString().trim());
			map.put("telephone", ((EditText) findViewById(R.id.tv_dphone))
					.getText().toString().trim());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_CHANGE_STORE,
					getInfoInqVal());

		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {
				StartActivityContController.goPage(NewUserInfo.this,
						StartActivityContController.wode);
				finish();
			} else {
				TispToastFactory.getToast(NewUserInfo.this, result.getMsg())
						.show();
			}
		}
	}

	private boolean checkValue() {
		Pattern p = Pattern
				.compile("(^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\\d{8}$)");
		Matcher m = p.matcher(etxt_mobile.getText().toString().trim());
		if (((EditText) findViewById(R.id.tv_dname)).getText().toString()
				.trim().equals("")) {
			Toast.makeText(NewUserInfo.this, "请输入店铺名称", Toast.LENGTH_SHORT)
					.show();
			return false;

		} else if (((EditText) findViewById(R.id.tv_dphone)).getText()
				.toString().trim().equals("")) {
			Toast.makeText(NewUserInfo.this, "请输入店铺电话", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (((EditText) findViewById(R.id.tv_daddress)).getText()
				.toString().trim().equals("")) {
			Toast.makeText(NewUserInfo.this, "请输入店铺详细地址", Toast.LENGTH_SHORT)
					.show();
			return false;

		} else if (etxt_mobile.getText() == null
				|| etxt_mobile.getText().toString().trim().equals("")) {
			Toast.makeText(NewUserInfo.this, "请填写手机号码", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (!m.matches()) {
			Toast.makeText(NewUserInfo.this, "电话号码错误", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (((EditText) findViewById(R.id.et_qq)).getText().toString()
				.trim().equals("")) {
			Toast.makeText(NewUserInfo.this, "请填写您的QQ号码", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断用户数据
	 */
	private boolean CheckInfoValue() {
		if (mCurrentBitMap1 == null) {

			Toast.makeText(NewUserInfo.this, "请选择您的头像", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (((EditText) findViewById(R.id.et_nickname)).getText()
				.toString().trim().equals("")) {
			Toast.makeText(NewUserInfo.this, "请填写您的昵称", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (type.equals("2")) {
			return checkValue();
		} else {
			return true;
		}
	}

	protected Map<String, Object> getVcodeFormatInqVal(String feild) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", etxt_mobile.getText().toString().trim());
		return map;
	}

	protected Map<String, Object> getChangeInfoInqVal() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("type", type);
		map.put("city", ((EditText) findViewById(R.id.tv_city)).getText()
				.toString().trim());
		map.put("head_photo", UploadUrl1);
		map.put("username", ((EditText) findViewById(R.id.et_nickname))
				.getText().toString().trim());
		map.put("qq", ((EditText) findViewById(R.id.et_qq)).getText()
				.toString().trim());
		map.put("mobile", etxt_mobile.getText().toString().trim());
		return map;
	}

	class ChangUserInfo extends AsyncTask<String, Integer, Response> {

		protected void onPreExecute() {
			dialog = ProgressDialog.show(NewUserInfo.this, "",
					getString(R.string.pubnewinfo), true);
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
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			if (result.isSuccessful()) {

				saveUserInfo(uid, type);
				if ("2".equals(type)) {
					new setStoreInfo().execute();
					return;
				}
				StartActivityContController.goPage(NewUserInfo.this,
						StartActivityContController.wode);
				finish();
			} else {
				TispToastFactory.getToast(NewUserInfo.this, result.getMsg())
						.show();
			}
		}
	}

	private void saveUserInfo(String uid, String type) {
		editor.putString("uid", uid);
		editor.putString("usertype", type);
		SessionManager.getInstance().setUserId(uid);
		SessionManager.getInstance().setUsertype(type);
	}
}
