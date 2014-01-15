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
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.CameraAndGallery;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;
import com.weibo.sdk.android.Oauth2AccessToken;

public class PublicQuestionUI extends FinalActivity implements OnClickListener {

	private ProgressDialog dialog;
	private CameraAndGallery cag;
	private Matrix matrix = new Matrix();
	private Bitmap mCurrentBitMap1;
	private String UploadUrl1 = "";
	private SessionManager sm;
	private EditText et_hairinfo;
	public static Oauth2AccessToken accessToken;

	private int Calendar_Width = 0;
	private int Cell_Width = 0;

	// /////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.publicquestion);
		sm = SessionManager.getInstance();
		initView();
		cag = new CameraAndGallery(this);

	}


	@Override
	protected void onResume() {
		MobileProbe.onResume(this, "发布问题");
		super.onResume();

	}

	@Override
	protected void onPause() {

		MobileProbe.onPause(this, "发布问题");
		super.onPause();
	}
	private void showPicture(Uri uri) {
		if (uri == null) {
			LogUtil.e("When showPicture Uri = null");
			return;
		}
		mCurrentBitMap1 = com.jm.util.ImageUtil.compressImageFromFile(uri
				.getPath());
		if (mCurrentBitMap1 == null) {
			mCurrentBitMap1 = cag.resizeBitmap(uri);
		}
		((ImageView) findViewById(R.id.iv_hairpic1))
				.setImageBitmap(mCurrentBitMap1);

	}

	private void initView() {

		// 获得屏幕宽和高，并計算出屏幕寬度分七等份的大小
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		Calendar_Width = screenWidth - 15;
		Cell_Width = Calendar_Width / 5 * 4;
		LayoutParams lp = new LinearLayout.LayoutParams(Cell_Width, Cell_Width);
		((ImageView) findViewById(R.id.iv_hairpic1)).setLayoutParams(lp);
		findViewById(R.id.btn_PublicTop).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		et_hairinfo = (EditText) findViewById(R.id.et_hairinfo);
		findViewById(R.id.iv_hairpic1).setOnClickListener(this);
		findViewById(R.id.iv_lockwise1).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_hairpic1:

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
		if (mCurrentBitMap1 == null) {
			TispToastFactory.getToast(PublicQuestionUI.this, "请上传问题配图").show();
			return false;

		}

		return true;
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
			dialog = ProgressDialog.show(PublicQuestionUI.this, "",
					getString(R.string.pubHair), true);

		}

		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("lng", sm.getLng());
			map.put("lat", sm.getLat());
			map.put("city", sm.getCity());
			map.put("content", et_hairinfo.getText().toString().trim());
			map.put("image", UploadUrl1);
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
			return conn.executeAndParse(Constant.URN_PUBLICQUESTION,
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
				TispToastFactory.getToast(PublicQuestionUI.this, res.getMsg())
						.show();
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
						PublicQuestionUI.this.finish();

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
