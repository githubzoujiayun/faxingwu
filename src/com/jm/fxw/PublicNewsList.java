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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.CameraAndGallery;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

public class PublicNewsList extends FinalActivity implements OnClickListener {

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
	private String type = "";

	// /////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.publicnewslist);
		sm = SessionManager.getInstance();
		initView();
		cag = new CameraAndGallery(this);
		type = getIntent().getStringExtra("pubtype");
		if (type.equals("1")) {
			((TextView) findViewById(R.id.tv_mainhead)).setText("发布名店展示");
		} else if (type.equals("2")) {
			((TextView) findViewById(R.id.tv_mainhead)).setText("发布名人堂");
		} else if (type.equals("3")) {
			((TextView) findViewById(R.id.tv_mainhead)).setText("发布行业新闻");
		} else {
			finish();
		}

	}

	@Override
	protected void onResume() {
		MobileProbe.onResume(this, "发布情报");
		super.onResume();

	}

	@Override
	protected void onPause() {

		MobileProbe.onPause(this, "发布情报");
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
		findViewById(R.id.btn_addhuati1).setOnClickListener(this);
		findViewById(R.id.btn_addhuati2).setOnClickListener(this);
		findViewById(R.id.btn_addhuati3).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_addhuati1:
			findViewById(R.id.btn_addhuati1).setVisibility(View.GONE);
			findViewById(R.id.lin_huati2).setVisibility(View.VISIBLE);
			findViewById(R.id.btn_addhuati2).setVisibility(View.VISIBLE);
			break;
		case R.id.btn_addhuati2:
			findViewById(R.id.btn_addhuati2).setVisibility(View.GONE);
			findViewById(R.id.lin_huati3).setVisibility(View.VISIBLE);
			findViewById(R.id.btn_addhuati3).setVisibility(View.VISIBLE);
			break;
		case R.id.btn_addhuati3:
			findViewById(R.id.btn_addhuati3).setVisibility(View.GONE);
			findViewById(R.id.lin_huati4).setVisibility(View.VISIBLE);
			break;
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
			TispToastFactory.getToast(PublicNewsList.this, "请至少上传一张图片").show();
			return false;

		}
		if ("".equals(((EditText) findViewById(R.id.et_hairinfo1)).getText()
				.toString().trim())
				&& "".equals(((EditText) findViewById(R.id.et_hairinfo2))
						.getText().toString().trim())
				&& "".equals(((EditText) findViewById(R.id.et_hairinfo3))
						.getText().toString().trim())
				&& "".equals(((EditText) findViewById(R.id.et_hairinfo4))
						.getText().toString().trim())) {
			TispToastFactory.getToast(PublicNewsList.this, "请至少输入一段文字").show();
			return false;
		}
		if ("".equals(((EditText) findViewById(R.id.et_title)).getText()
				.toString().trim())) {
			TispToastFactory.getToast(PublicNewsList.this, "请输入标题").show();
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
			dialog = ProgressDialog.show(PublicNewsList.this, "",
					getString(R.string.pubHair), true);

		}

		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("news_type", type);
			map.put("image1", UploadUrl1);
			map.put("title", ((EditText) findViewById(R.id.et_title)).getText()
					.toString().trim());

			map.put("content1", ((EditText) findViewById(R.id.et_hairinfo1))
					.getText().toString().trim());
			map.put("image2", UploadUrl2);
			map.put("content2", ((EditText) findViewById(R.id.et_hairinfo2))
					.getText().toString().trim());
			map.put("image3", UploadUrl3);
			map.put("content3", ((EditText) findViewById(R.id.et_hairinfo3))
					.getText().toString().trim());
			map.put("image4", UploadUrl4);
			map.put("content4", ((EditText) findViewById(R.id.et_hairinfo4))
					.getText().toString().trim());
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
			return conn.executeAndParse(Constant.URN_ADD_NEWS, getListInqVal());

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

				TispToastFactory.getToast(PublicNewsList.this, res.getMsg())
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
						PublicNewsList.this.finish();

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
