package com.jm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;

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
	 * 压缩图片(不会OOM的)
	 */
	public static Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 960f;//
		float ww = 640f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0) {
			be = 1;
		}
		newOpts.inSampleSize = be;// 设置采样率
		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
		newOpts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		// 其实是无效的,大家尽管尝试
		return bitmap;
	}

	public static void getFitBitMap(int ScreenWidth, int bmw, int bmh,
			ImageView iv) {
		LogUtil.e("ScreenWidth = " + ScreenWidth);
		LogUtil.e("原图bm.w = " + bmw);
		LogUtil.e("原图bm.h = " + bmh);
		if (bmw > ScreenWidth / 2) {
			LogUtil.e("原图需要缩放");
			double be = (double) bmw / (double) (ScreenWidth / 2);
			LogUtil.e("缩放值 =" + be);
			LogUtil.e("新图" + bmw / be);
			LogUtil.e("新图" + bmh / be);
			int h = (int) (bmh / be);
			int w = (int) (bmw / be);
			ViewGroup.LayoutParams para;
			para = iv.getLayoutParams();
			para.height = h;
			para.width = w;
			iv.setLayoutParams(para);
		} else {
			LogUtil.e("原图不需要缩放");
		}
	}
}
