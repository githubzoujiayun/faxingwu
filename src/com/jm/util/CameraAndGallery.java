package com.jm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.jm.entity.News;
import com.jm.fxw.R;
import com.jm.session.SessionManager;

public class CameraAndGallery {

	/* 用来标识请求照相功能的activity */
	public static final int CAMERA_WITH_DATA = 3021;

	/* 用来标识请求gallery的activity */
	public static final int PHOTO_PICKED_WITH_DATA = 3023;

	/* 用来标识请求gallery的activity */
	public static final int PHOTO_ROLL = 3043;

	/* 用来标识请求gallery的activity */
	public static final int PHOTO_DEL = 3053;
	/* 从Gallery选择后由用户进行图片裁剪 */
	public static final int PHOTO_PICKED_WITH_DATA_CROP = 3033;

	// /* 拍照后由用户进行图片裁剪 */
	// public static final int CAMERA_WITH_DATA_CROP = 3031;
	//
	// /* 从Gallery选择后由用户进行图片裁剪 */
	// public static final int PHOTO_PICKED_WITH_DATA_CROP = 3033;

	private static final int DEFALUT_IMAGE_SIZE = 480;

	private Activity activity;

	/* 拍照的照片存储位置 */
	private File photoTmpDir = new File(SessionManager.getInstance()
			.getCameraCacheDir());

	private Matrix matrix = new Matrix();
	/* Upload image file dir */
	private File uploadTmpDir = new File(SessionManager.getInstance()
			.getUploadCacheDir());

	private Uri cameraPictureUri;

	public CameraAndGallery(Activity activity) {
		super();
		this.activity = activity;
		if (!photoTmpDir.exists()) {
			photoTmpDir.mkdirs();// 创建照片的存储目录
		}

		if (!uploadTmpDir.exists()) {
			uploadTmpDir.mkdirs();// 创建照片的存储目录
		}
	}

	public Uri getLocalBitmapUri(Bitmap bitmap) {
		File file = new File(photoTmpDir, getPhotoFileName());
		boolean result = storeBitmapFile(bitmap, file);
		// bitmap.recycle();
		if (result) {
			return Uri.fromFile(file);
		} else {
			return null;
		}
	}

	public Bitmap getBitmapFromUri(Uri uri) {
		if (uri == null) {
			return null;
		}

		Bitmap bitmap = null;
		try {
			bitmap = MediaStore.Images.Media.getBitmap(
					activity.getContentResolver(), uri);
		} catch (IOException e) {
			LogUtil.e(e.getMessage());
		}

		return bitmap;
	}

	public Bitmap resizeBitmap(Uri uri) {
		return getBitmapFromUri(uri);
	}

	public boolean storeBitmapFile(Bitmap bitmap, File file) {
		FileOutputStream m_fileOutPutStream = null;
		try {
			m_fileOutPutStream = new FileOutputStream(file);// 写入的文件路径
		} catch (FileNotFoundException e) {
			LogUtil.e(e.getMessage(), e);
			return false;
		}

		bitmap.compress(CompressFormat.JPEG, 80, m_fileOutPutStream);
		try {
			m_fileOutPutStream.flush();
			m_fileOutPutStream.close();
		} catch (IOException e) {
			LogUtil.e(e.getMessage(), e);
			return false;
		}
		return true;
	}

	public void pickPicture() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
			doPickPhotoFromGallery();// 从相册中去获取
		} else {
			showToast(R.string.no_sdcard);
		}
	}

	public void takePicture() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
			doTakePhoto();// 用户点击了从照相机获取
		} else {
			showToast(R.string.no_sdcard);
		}
	}

	private void showToast(int resId) {
		Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 可弹出对话框让用户选择是拍摄照片还是选择照片
	 */
	public void doPickPhotoAction() {
		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(activity,
				android.R.style.Theme_Light);
		String[] choices;
		choices = new String[2];
		choices[0] = activity.getString(R.string.take_photo); // 拍照
		choices[1] = activity.getString(R.string.pick_photo); // 从相册中选择
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle(R.string.photo_dlg_title);
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:// 启动相机拍照
								// doTakeAndCropPhoto();

							doTakePhoto();

							break;
						case 1:// 从相册中去获取
								// doPickPhotoFromGalleryDoCrop();

							doPickPhotoFromGallery();

							break;
						}
					}
				});
		builder.setNegativeButton(R.string.go_back,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});
		builder.create().show();
	}

	// 拍照
	private void doTakePhoto() {
		try {
			// Launch camera to take photo
			Intent intent = getTakePickIntent();
			activity.startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			showToast(R.string.photo_picker_not_found);
		}
	}

	private Intent getTakePickIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		File file = new File(photoTmpDir, getPhotoFileName());

		LogUtil.e("____________________________________file = "
				+ file.getAbsolutePath().toString());
		cameraPictureUri = Uri.fromFile(file);
		LogUtil.e("__________________________________cameraPictureUri = "
				+ cameraPictureUri.getAuthority().toString());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUri);
		return intent;
	}

	/*
	 * 剪辑图片暂时不需要
	 */
	public void doCropPhoto(Uri uri) {
		try {
			// 启动gallery去剪辑这个照片
			Intent intent = getCropImageIntent(uri);

			if (intent == null) {
				LogUtil.w("Can not initial intent for " + uri);
				return;
			}

			activity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA_CROP);
		} catch (Exception e) {
			showToast(R.string.photo_picker_not_found);
		}
	}

	// 调用图片剪辑程序
	private Intent getCropImageIntent(Uri photoUri) {
		LogUtil.d("crop for : " + photoUri);

		if (photoUri == null) {
			return null;
		}

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 254);
		intent.putExtra("outputY", 254);
		intent.putExtra("return-data", true);
		return intent;
	}

	public Uri getCameraPictureUri() {
		return cameraPictureUri;
	}

	// 用当前时间给取得的图片命名
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMddHHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	public File getNewUploadFile() {
		return new File(uploadTmpDir, getUploadFileName());
	}

	private String getUploadFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'UP'_yyyyMMddHHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	// 请求Gallery程序
	public void doPickPhotoFromGallery() {
		try {
			Intent intent = getPhotoPickIntent();
			activity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			showToast(R.string.photo_picker_not_found);
		}
	}

	// 封装请求Gallery的intent
	private Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		return intent;
	}
}
