package com.jm.fxw;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.CameraAndGallery;
import com.jm.util.LogUtil;
import com.jm.util.PriceUtil;
import com.jm.util.TispToastFactory;
import com.jm.util.WidgetUtil;
import com.weibo.sdk.android.Oauth2AccessToken;

public class PublicPortfolioOfHair extends FinalActivity implements
		OnClickListener {

	private ProgressDialog dialog;
	private CameraAndGallery cag;
	private Matrix matrix = new Matrix();

	private Bitmap bitmap = null;
	private Bitmap mCurrentBitMap1;
	private String UploadUrl1 = "";
	private Bitmap mCurrentBitMap2;
	private String UploadUrl2 = "";
	private Bitmap mCurrentBitMap3;
	private String UploadUrl3 = "";
	private Bitmap mCurrentBitMap4;
	private String UploadUrl4 = "";
	private SessionManager sm;
	private String ImageIndex = "";
	public static Oauth2AccessToken accessToken;

	private int Calendar_Width = 0;
	private int Cell_Width = 0;
	private String[] discount = { "9.5", "9", "8.5", "8", "7.5", "7", "6.5",
			"6", "5.5", "5", "4.5", "4", "3.5", "3", "2.5", "2", "1.5", "1",
			"0.5" };

	ArrayAdapter<String> adapter;
	private Spinner sp;

	// /////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.publichairofhair);
		sm = SessionManager.getInstance();
		initView();
		cag = new CameraAndGallery(this);

	}

	@Override
	protected void onResume() {
		MobileProbe.onResume(this, "发型师上传发型");
		super.onResume();

	}

	@Override
	protected void onPause() {

		MobileProbe.onPause(this, "发型师上传发型");
		super.onPause();
	}

	private void showPicture(Uri uri) {
		if (uri == null) {
			LogUtil.e("When showPicture Uri = null");
			return;
		}
		bitmap = com.jm.util.ImageUtil.compressImageFromFile(uri.getPath());
		if (bitmap == null) {
			bitmap = cag.resizeBitmap(uri);
		}
		if ("1".equals(ImageIndex)) {
			((ImageView) findViewById(R.id.iv_hairpic1)).setImageBitmap(bitmap);
			mCurrentBitMap1 = bitmap;
		}
		if ("2".equals(ImageIndex)) {
			((ImageView) findViewById(R.id.iv_hairpic2)).setImageBitmap(bitmap);
			mCurrentBitMap2 = bitmap;
		}
		if ("3".equals(ImageIndex)) {
			((ImageView) findViewById(R.id.iv_hairpic3)).setImageBitmap(bitmap);
			mCurrentBitMap3 = bitmap;
		}
		if ("4".equals(ImageIndex)) {
			((ImageView) findViewById(R.id.iv_hairpic4)).setImageBitmap(bitmap);
			mCurrentBitMap4 = bitmap;
		}
	}

	private void initView() {

		// 获得屏幕宽和高，并計算出屏幕寬度分七等份的大小
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		Calendar_Width = screenWidth - 15;
		Cell_Width = Calendar_Width / 4;
		findViewById(R.id.btn_PublicTop).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.iv_hairpic1).setOnClickListener(this);
		findViewById(R.id.iv_hairpic2).setOnClickListener(this);
		findViewById(R.id.iv_hairpic3).setOnClickListener(this);
		findViewById(R.id.iv_hairpic4).setOnClickListener(this);
		findViewById(R.id.iv_lockwise1).setOnClickListener(this);
		findViewById(R.id.iv_lockwise2).setOnClickListener(this);
		findViewById(R.id.iv_lockwise3).setOnClickListener(this);
		findViewById(R.id.iv_lockwise4).setOnClickListener(this);
		LayoutParams lp = new LinearLayout.LayoutParams(Cell_Width, Cell_Width);
		((ImageView) findViewById(R.id.iv_hairpic1)).setLayoutParams(lp);
		((ImageView) findViewById(R.id.iv_hairpic2)).setLayoutParams(lp);
		((ImageView) findViewById(R.id.iv_hairpic3)).setLayoutParams(lp);
		((ImageView) findViewById(R.id.iv_hairpic4)).setLayoutParams(lp);

		sp = (Spinner) findViewById(R.id.sp_discount);
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
						((EditText) findViewById(R.id.et_serprice)),
						((Spinner) findViewById(R.id.sp_discount)),
						((TextView) findViewById(R.id.tv_realprice)));

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		((EditText) findViewById(R.id.et_serprice))
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
						PriceUtil.setRealPrice(
								((EditText) findViewById(R.id.et_serprice)),
								((Spinner) findViewById(R.id.sp_discount)),
								((TextView) findViewById(R.id.tv_realprice)));

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_hairpic1:
			ImageIndex = "1";
			cag.doPickPhotoAction();
			break;
		case R.id.iv_hairpic2:
			ImageIndex = "2";
			cag.doPickPhotoAction();
			break;
		case R.id.iv_hairpic3:
			ImageIndex = "3";
			cag.doPickPhotoAction();
			break;
		case R.id.iv_hairpic4:
			ImageIndex = "4";
			cag.doPickPhotoAction();
			break;
		case R.id.btn_leftTop:
			finish();
			break;

		case R.id.iv_lockwise1:
			if (mCurrentBitMap1 == null) {
				break;
			}
			mCurrentBitMap1 = lockwise(
					((ImageView) findViewById(R.id.iv_hairpic1)),
					mCurrentBitMap1);
			break;
		case R.id.iv_lockwise2:
			if (mCurrentBitMap2 == null) {
				break;
			}
			mCurrentBitMap2 = lockwise(
					((ImageView) findViewById(R.id.iv_hairpic2)),
					mCurrentBitMap2);
			break;
		case R.id.iv_lockwise3:
			if (mCurrentBitMap3 == null) {
				break;
			}
			mCurrentBitMap3 = lockwise(
					((ImageView) findViewById(R.id.iv_hairpic3)),
					mCurrentBitMap3);
			break;
		case R.id.iv_lockwise4:
			if (mCurrentBitMap4 == null) {
				break;
			}
			mCurrentBitMap4 = lockwise(
					((ImageView) findViewById(R.id.iv_hairpic4)),
					mCurrentBitMap4);
			break;
		case R.id.btn_PublicTop:
			if (CheckValue()) {
				LogUtil.i("开始上传图片");
				new UpLoadImage().execute();
			}
			break;
		}

	}

	private Bitmap lockwise(ImageView imageView, Bitmap mCurrentBitMap) {

		matrix.setRotate(90);
		mCurrentBitMap = Bitmap.createBitmap(mCurrentBitMap, 0, 0,
				mCurrentBitMap.getWidth(), mCurrentBitMap.getHeight(), matrix,
				true);
		imageView.setImageBitmap(mCurrentBitMap);
		return mCurrentBitMap;

	}

	private boolean CheckValue() {
		if (mCurrentBitMap1 == null && mCurrentBitMap2 == null
				&& mCurrentBitMap3 == null && mCurrentBitMap4 == null) {
			TispToastFactory
					.getToast(PublicPortfolioOfHair.this, "请至少上传一张发型图片").show();
			return false;

		}
		if (WidgetUtil
				.CheckAllEditTextValue((EditText) findViewById(R.id.et_sertime))
				&& WidgetUtil
						.CheckAllEditTextValue((EditText) findViewById(R.id.et_serprice))
				&& WidgetUtil
						.CheckAllEditTextValue((EditText) findViewById(R.id.et_hairinfo))) {
			return true;
		} else {
			TispToastFactory.getToast(this, "请输入完整的发型信息").show();
			return false;
		}

	}

	private String getImageStringList() {
		StringBuffer sb = new StringBuffer();
		if (!"".equals(UploadUrl1)) {
			sb.append("," + UploadUrl1);
		}
		if (!"".equals(UploadUrl2)) {
			sb.append("," + UploadUrl2);
		}
		if (!"".equals(UploadUrl3)) {
			sb.append("," + UploadUrl3);
		}
		if (!"".equals(UploadUrl4)) {
			sb.append("," + UploadUrl4);
		}
		if (!"".equals(sb.toString())) {
			return sb.toString().substring(1);
		} else {
			return "";
		}
	}

	private String getFaceString() {
		if (((RadioButton) findViewById(R.id.cb_yuanlian)).isChecked()) {
			return "1";
		} else if (((RadioButton) findViewById(R.id.cb_fanglian)).isChecked()) {
			return "2";
		} else if (((RadioButton) findViewById(R.id.cb_changlian)).isChecked()) {
			return "3";
		} else if (((RadioButton) findViewById(R.id.cb_guazilian)).isChecked()) {
			return "4";
		} else {
			return "";
		}
	}

	private String getColorString() {
		if (((RadioButton) findViewById(R.id.cb_zhengse)).isChecked()) {
			return "1";
		} else if (((RadioButton) findViewById(R.id.cb_jianbianse)).isChecked()) {
			return "2";
		} else if (((RadioButton) findViewById(R.id.cb_tiaose)).isChecked()) {
			return "3";
		} else if (((RadioButton) findViewById(R.id.cb_duose)).isChecked()) {
			return "4";
		} else {
			return "";
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		LogUtil.d(requestCode + "/" + resultCode + "/" + intent);

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

	class UpLoadImage extends AsyncTask<String, Integer, Response> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PublicPortfolioOfHair.this, "",
					getString(R.string.pubHair), true);

		}

		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("type", sm.getUsertype());
			map.put("content", ((EditText) findViewById(R.id.et_hairinfo))
					.getText().toString().trim());
			map.put("face", getFaceString());
			map.put("hair_color", getColorString());
			map.put("sex", ((RadioButton) findViewById(R.id.rb_female))
					.isChecked() ? "0" : "1");
			map.put("work_image", getImageStringList());

			map.put("price", ((EditText) findViewById(R.id.et_serprice))
					.getText().toString().trim());
			map.put("long_service", ((EditText) findViewById(R.id.et_sertime))
					.getText().toString().trim()
					+ "小时");
			map.put("rebate", ((Spinner) findViewById(R.id.sp_discount))
					.getSelectedItem().toString().trim());
			// map.put("text", access_token);
			return map;
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
					return null;
				} else {
					UploadUrl1 = fileId;
				}
			}
			if (mCurrentBitMap2 != null) {
				cag.storeBitmapFile(mCurrentBitMap2, file);
				LogUtil.d("Upload Second pic" + file.getAbsolutePath());
				String uploadFileName = file.getAbsolutePath();
				fileId = conn.uploadFile(new File(uploadFileName));
				if (fileId == null || fileId.equals("")) {
					LogUtil.e("Second Pic Upload Failed");
					return null;
				} else {
					UploadUrl2 = fileId;
				}
			}
			if (mCurrentBitMap3 != null) {
				cag.storeBitmapFile(mCurrentBitMap3, file);
				LogUtil.d("Upload Third pic" + file.getAbsolutePath());
				String uploadFileName = file.getAbsolutePath();
				fileId = conn.uploadFile(new File(uploadFileName));
				if (fileId == null || fileId.equals("")) {
					LogUtil.e("Third Pic Upload Failed");
					return null;
				} else {
					UploadUrl3 = fileId;
				}
			}
			if (mCurrentBitMap4 != null) {
				cag.storeBitmapFile(mCurrentBitMap4, file);
				LogUtil.d("Upload Forth pic" + file.getAbsolutePath());
				String uploadFileName = file.getAbsolutePath();
				fileId = conn.uploadFile(new File(uploadFileName));
				if (fileId == null || fileId.equals("")) {
					LogUtil.e("Forth Pic Upload Failed");
					return null;
				} else {
					UploadUrl4 = fileId;
				}
			}
			return conn.executeAndParse(Constant.URN_PUBLICPORTFOLIOOFHAIR,
					getListInqVal());

		}

		@Override
		protected void onPostExecute(Response res) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			if (res == null) {
				netDialog();
				return;
			}
			if (res.isSuccessful()) {

				TispToastFactory.getToast(PublicPortfolioOfHair.this,
						res.getMsg()).show();
				finish();
			}

		}
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
						PublicPortfolioOfHair.this.finish();

					}
				});
		builder.setNegativeButton(R.string.tryagain,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new UpLoadImage().execute();
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

}
