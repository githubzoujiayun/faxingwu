package com.jm.session;

import java.io.File;

import com.jm.util.LogUtil;

public class SessionManager {

	private static final int BITMAP_SMALL = 120;
	private static final int BITMAP_LARGE = 480;
	private static final int BITMAP_MID = 240;

	private int smallsize = BITMAP_SMALL;
	private int largesize = BITMAP_LARGE;
	private int midsize = BITMAP_MID;
	private int brodersize = 0;

	private String city = "";

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		if (city.equals(null) || city.equals("")) {
			return;
		} else {
			this.city = city;
		}
	}

	private double lat = 121;
	private double lng = 30;
	private boolean distanceChanged;

	private static SessionManager instance = new SessionManager();

	private String usertype = null;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	/**
	 * 1是用户，2是发型师
	 * 
	 * @return
	 */
	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	private String userId = null;
	private String zipVersion = null;

	public String getUserId() {
		if (userId == null) {
			return "";
		}
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getZipVersion() {
		if (zipVersion == null) {
			return "-1";
		}
		return zipVersion;
	}

	public void setZipVersion(String version) {
		this.zipVersion = version;
	}

	// 参考数据状态 0-未初始化，1-正在初始化，2-已完成初始化
	private byte refDatStatus = 0;

	private String appVersion = null;

	private String cacheDir = "";


	private SessionManager() {
		super();
	}

	public static SessionManager getInstance() {
		return instance;
	}

	public boolean isLogin() {
		if (userId == null || userId.equals("") || usertype == null
				|| usertype.equals("")) {
			return false;
		}
		return true;
	}

	public boolean isUser() {
		if (usertype != null && usertype.equals("2")) {
			return false;
		}
		return true;
	}

	public void refDatInitializing() {
		refDatStatus = 1;
	}

	public void refDatInitialized() {
		refDatStatus = 2;
	}

	public boolean isRefDatInitializing() {
		return refDatStatus == 1;
	}

	public boolean isRefDatInitialized() {
		return refDatStatus == 2;
	}

	public boolean isRefDatNotInitialized() {
		return refDatStatus != 2;
	}

	public void resetRefData() {
		refDatStatus = 0;
	}

	public void setAppVersion(String version) {
		this.appVersion = version;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public int getSmallBitMapSize() {
		return smallsize;
	}

	public boolean isDistanceChanged() {
		return distanceChanged;
	}

	public void setDistanceChanged(boolean distanceChanged) {
		this.distanceChanged = distanceChanged;
	}

	public int getLargeBitMapSize() {
		return largesize;
	}

	public int getMidBitMapSize() {
		return midsize;
	}

	public int getBrodersize() {
		return brodersize;
	}

	public void setCacheDir(String cacheDir) {
		this.cacheDir = cacheDir;

		LogUtil.i("Cache Dir: " + cacheDir);

		initDir(cacheDir);
		// initDir(cacheDir + File.separator + Constant.LOG_TAG);
		initDir(cacheDir + File.separator + "upload");
	}

	private void initDir(String dirStr) {
		File dir = new File(dirStr);
		if (dir.exists()) {
			if (dir.isDirectory()) {
			} else {
				dir.delete();
				if (!dir.mkdirs()) {
					LogUtil.w("Failed to make directory " + dirStr);
				}
			}
		} else {
			if (!dir.mkdirs()) {
				LogUtil.w("Failed to make directory " + dirStr);
			}
		}
	}

	public String getCacheDir() {
		return cacheDir;
	}

	public String getCameraCacheDir() {
		return cacheDir + "camera" + File.separator;
	}

	public String getUploadCacheDir() {
		return cacheDir + "upload" + File.separator;
	}

	public String getRecorderCacheDir() {
		return cacheDir + "recorder" + File.separator;
	}

}
