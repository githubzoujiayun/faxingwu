package com.jm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.jm.fxw.R;
import com.jm.session.SessionManager;

public class CameraAndGallery {

	/* ������ʶ�������๦�ܵ�activity */
	public static final int CAMERA_WITH_DATA = 3021;

	private Button ok;
	private Button cancel;
	private Dialog dialog;
	/* ������ʶ����gallery��activity */
	public static final int PHOTO_PICKED_WITH_DATA = 3023;

	/* ������ʶ����gallery��activity */
	public static final int PHOTO_ROLL = 3043;

	/* ������ʶ����gallery��activity */
	public static final int PHOTO_DEL = 3053;
	/* ��Galleryѡ������û�����ͼƬ�ü� */
	public static final int PHOTO_PICKED_WITH_DATA_CROP = 3033;

	// /* ���պ����û�����ͼƬ�ü� */
	// public static final int CAMERA_WITH_DATA_CROP = 3031;
	//
	// /* ��Galleryѡ������û�����ͼƬ�ü� */
	// public static final int PHOTO_PICKED_WITH_DATA_CROP = 3033;

	private static final int DEFALUT_IMAGE_SIZE = 480;

	private Activity activity;

	/* ���յ���Ƭ�洢λ�� */
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
			photoTmpDir.mkdirs();// ������Ƭ�Ĵ洢Ŀ¼
		}

		if (!uploadTmpDir.exists()) {
			uploadTmpDir.mkdirs();// ������Ƭ�Ĵ洢Ŀ¼
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
			m_fileOutPutStream = new FileOutputStream(file);// д����ļ�·��
		} catch (FileNotFoundException e) {
			LogUtil.e(e.getMessage(), e);
			return false;
		}

		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//
		// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
		// int options = 100;
		// while (baos.toByteArray().length / 1024 > 100 && options < 20) { //
		// ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
		// baos.reset();// ����baos�����baos
		// bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//
		// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
		// options -= 10;// ÿ�ζ�����10
		// LogUtil.e("options = " + options);
		// LogUtil.e("baos.toByteArray().length / 1024 = "
		// + baos.toByteArray().length / 1024);
		// }
		// LogUtil.e("ͼƬѹ����� = " + bitmap.getRowBytes());
		bitmap.compress(CompressFormat.JPEG, 100, m_fileOutPutStream);
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
		if (status.equals(Environment.MEDIA_MOUNTED)) {// �ж��Ƿ���SD��
			doPickPhotoFromGallery();// �������ȥ��ȡ
		} else {
			showToast(R.string.no_sdcard);
		}
	}

	public void takePicture() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {// �ж��Ƿ���SD��
			doTakePhoto();// �û�����˴��������ȡ
		} else {
			showToast(R.string.no_sdcard);
		}
	}

	private void showToast(int resId) {
		Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
	}

	/**
	 * �ɵ����Ի������û�ѡ����������Ƭ����ѡ����Ƭ
	 */
	public void doPickPhotoAction() {

		View dialogView = LayoutInflater.from(activity).inflate(
				R.layout.confirmchoice, null);
		ok = (Button) dialogView.findViewById(R.id.loginoutdialog_button_ok);
		cancel = (Button) dialogView
				.findViewById(R.id.loginoutdialog_button_cancel);
		dialog = new Dialog(activity, R.style.MyDialog);
		dialog.setContentView(dialogView);
		dialog.show();
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				doTakePhoto();
				dialog.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doPickPhotoFromGallery();
				dialog.dismiss();
			}
		});

		// Wrap our context to inflate list items using correct theme
		// final Context dialogContext = new ContextThemeWrapper(activity,
		// android.R.style.Theme_Light);
		// String[] choices;
		// choices = new String[2];
		// choices[0] = activity.getString(R.string.take_photo); // ����
		// choices[1] = activity.getString(R.string.pick_photo); // �������ѡ��
		// final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
		// android.R.layout.simple_list_item_1, choices);
		//
		// final AlertDialog.Builder builder = new AlertDialog.Builder(
		// dialogContext);
		// builder.setTitle(R.string.photo_dlg_title);
		// builder.setSingleChoiceItems(adapter, -1,
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// switch (which) {
		// case 0:// �����������
		// // doTakeAndCropPhoto();
		//
		// doTakePhoto();
		//
		// break;
		// case 1:// �������ȥ��ȡ
		// // doPickPhotoFromGalleryDoCrop();
		//
		// doPickPhotoFromGallery();
		//
		// break;
		// }
		// }
		// });
		// builder.setNegativeButton(R.string.go_back,
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		//
		// });
		// builder.create().show();
	}

	// ����
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
	 * ����ͼƬ��ʱ����Ҫ
	 */
	public void doCropPhoto(Uri uri) {
		try {
			// ����galleryȥ���������Ƭ
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

	// ����ͼƬ��������
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

	// �õ�ǰʱ���ȡ�õ�ͼƬ����
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

	// ����Gallery����
	public void doPickPhotoFromGallery() {
		try {
			Intent intent = getPhotoPickIntent();
			activity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			showToast(R.string.photo_picker_not_found);
		}
	}

	// ��װ����Gallery��intent
	private Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		return intent;
	}
}
