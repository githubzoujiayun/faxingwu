package com.jm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;

import com.jm.session.SessionManager;

public class ImageUtil {

	public static File getCachedBitmapFile(String fileId) {
		if (fileId == null || fileId.length() == 0) {
			return null;
		}

		File file = new File(SessionManager.getInstance().getUploadCacheDir()
				+ fileId.substring(fileId.lastIndexOf("cn/")).replace(
						File.separator, "_"));
		if (file.exists() && file.isFile()) {

			return file;
		}

		return null;
	}

	public static String getCachedBitmapPath(String fileId) {
		if (fileId == null || fileId.length() == 0) {
			return null;
		}

		File file = new File(SessionManager.getInstance().getUploadCacheDir()
				+ fileId.substring(fileId.lastIndexOf("cn/")).replace(
						File.separator, "_"));
		if (file.exists() && file.isFile()) {

			return file.getPath();
		}

		return null;
	}

	public static Bitmap getCachedBitmap(String fileId) {
		try {

			return getCachedBitmap(SessionManager.getInstance()
					.getUploadCacheDir(), fileId);
		} catch (Exception e) {
			return null;
		}
	}

	private static Bitmap getCachedBitmap(String cacheDir, String fileId) {
		if (fileId == null || fileId.length() == 0) {
			return null;
		}

		File file = new File(cacheDir
				+ fileId.substring(fileId.lastIndexOf("cn/")).replace(
						File.separator, "_"));
		if (file.exists() && file.isFile()) {
			LogUtil.d("getCachedBitmap:" + file.getPath());

			// Update the time stamp
			file.setLastModified(new Date().getTime());
			return BitmapFactory.decodeFile(file.getPath());
		}
		return null;
	}

	public static String pictureStringExists(String fileId) {
		if (fileId == null || fileId.length() == 0) {
			return "";
		}
		String pictureId = "";
		try {
			pictureId = fileId.substring(fileId.lastIndexOf("cn/")).replace(
					File.separator, "_");
		} catch (Exception e) {
			LogUtil.e(e.toString());
			return "";
		}
		File file = new File(SessionManager.getInstance().getUploadCacheDir()
				+ pictureId);
		if (file.exists() && file.isFile()) {
			return file.getPath();
		}

		return "";
	}

	public static boolean pictureExists(String fileId) {
		if (fileId == null || fileId.length() == 0) {
			return false;
		}
		String pictureId = "";
		try {
			pictureId = fileId.substring(fileId.lastIndexOf("cn/")).replace(
					File.separator, "_");
		} catch (Exception e) {
			LogUtil.e(e.toString());
			return false;
		}
		File file = new File(SessionManager.getInstance().getUploadCacheDir()
				+ pictureId);
		if (file.exists() && file.isFile()) {
			return true;
		}

		return false;
	}

	public static String getFilePath(String fileId) {
		return fileId;
	}

	public static void saveCachedBitmap(String fileId, InputStream is)
			throws IOException {

		String fileName = SessionManager.getInstance().getUploadCacheDir()
				+ fileId.substring(fileId.lastIndexOf("cn/")).replace(
						File.separator, "_");
		FileOutputStream fos = new FileOutputStream(fileName);
		int len;
		byte[] buffer = new byte[1024];
		while ((len = is.read(buffer)) != -1) {
			fos.write(buffer, 0, len);
		}
		fos.close();
		File file = new File(fileName);
		file.renameTo(new File(fileName));
	}

	/**
	 * ѹ��ͼƬ(����OOM��)
	 */
	public static Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// ֻ����,��������
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		if (bitmap == null) {
			LogUtil.e("inJustDecodeBounds = true;bitmap = null");
		}
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// ���ò�����
		newOpts.inPreferredConfig = Config.ARGB_8888;// ��ģʽ��Ĭ�ϵ�,�ɲ���
		newOpts.inPurgeable = true;// ͬʱ���òŻ���Ч
		newOpts.inInputShareable = true;// ����ϵͳ�ڴ治��ʱ��ͼƬ�Զ�������
		newOpts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		File file = new File(srcPath);

		bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		if (bitmap == null) {
			LogUtil.e("inJustDecodeBounds = false;bitmap = null");
		}
		// return compressBmpFromBmp(bitmap);//ԭ���ķ������������������ͼ���ж���ѹ��
		// ��ʵ����Ч��,��Ҿ��ܳ���
		return bitmap;
	}
}
