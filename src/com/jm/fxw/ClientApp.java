package com.jm.fxw;

import java.io.File;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Environment;

import com.jm.connection.Connection;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.UnCatchException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ClientApp extends Application {
	private Connection conn = null;
	private String rootpath = "";
	private SessionManager sm;

	public void onCreate() {
		super.onCreate();

		UnCatchException uce = UnCatchException.getInstance();
		uce.init(this);
		sm = SessionManager.getInstance();
		SharedPreferences share = getSharedPreferences(Constant.PREFS_NAME,
				MODE_PRIVATE);
		String userID = share.getString("uid", "");
		String userType = share.getString("usertype", "");
		String city = share.getString("city", "");
		String lat = share.getString("lat", "0");
		String lng = share.getString("lng", "0");
		sm.setUserId(userID);
		sm.setUsertype(userType);
		sm.setCity(city);
		sm.setLat(Double.valueOf(lat));
		sm.setLng(Double.valueOf(lng));
		initCacheDir();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(false).cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(
				defaultOptions).build();
		ImageLoader.getInstance().init(config);
	}

	// private void initSystemInfo() {
	// String versionName = "";
	// try {
	// PackageInfo info = getPackageManager().getPackageInfo(
	// getPackageName(), 0);
	// versionName = info.versionCode + ";" + info.versionName;
	// } catch (Exception e) {
	// LogUtil.e(e.getMessage(), e);
	// }
	//
	// sm.setAppVersion(versionName);
	// }

	private void initCacheDir() {
		SessionManager session = SessionManager.getInstance();
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {// ≈–∂œ «∑Ò”–SDø®
			session.setCacheDir(Environment.getExternalStorageDirectory()
					.getPath() + File.separator + "fxw" + File.separator);
		} else {
			LogUtil.e(getString(R.string.no_sdcard));
			session.setCacheDir(getCacheDir().getPath() + File.separator);
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void onLowMemory() {
		super.onLowMemory();
	}

	public Connection getConnection() {
		if (conn == null) {
			conn = new Connection(Constant.DEFAULT_SERVER, this);
		}
		return conn;
	}

	public String getRootpath() {
		return rootpath;
	}

	public void setRootpath(String rootpath) {
		this.rootpath = rootpath;
	}

}
