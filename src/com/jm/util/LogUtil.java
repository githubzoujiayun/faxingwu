package com.jm.util;

import android.util.Log;

import com.jm.finals.Constant;


public class LogUtil {

	public static final int NONE = 0;
	public static final int ERROR_ONLY = 1;
	public static final int ERROR_WARN = 2;
	public static final int ERROR_WARN_INFO = 3;
	public static final int ERROR_WARN_INFO_DEBUG = 4;
	private static final int LOGGING_LEVEL = ERROR_WARN_INFO_DEBUG;

	// Errors + warnings + info + debug (default)
	public static void e(String msg) {
		if (LOGGING_LEVEL >= ERROR_ONLY)
			Log.e(Constant.LOG_TAG, msg);
	}

	public static void e(String msg, Throwable e) {
		if (LOGGING_LEVEL >= ERROR_ONLY)
			Log.e(Constant.LOG_TAG, msg, e);
	}

	public static void w(String msg) {
		if (LOGGING_LEVEL >= ERROR_WARN)
			Log.w(Constant.LOG_TAG, msg);
	}
	
	public static void w(String msg, Throwable e) {
		if (LOGGING_LEVEL >= ERROR_WARN)
			Log.w(Constant.LOG_TAG, msg, e);
	}

	public static void i(String msg) {
		if (LOGGING_LEVEL >= ERROR_WARN_INFO)
			Log.i(Constant.LOG_TAG, msg);
	}
	
	public static void i(String msg, Throwable e) {
		if (LOGGING_LEVEL >= ERROR_WARN_INFO)
			Log.i(Constant.LOG_TAG, msg, e);
	}

	public static void d(String msg) {
		if (LOGGING_LEVEL >= ERROR_WARN_INFO_DEBUG)
			Log.d(Constant.LOG_TAG, msg);
	}
	
	public static void d(String msg, Throwable e) {
		if (LOGGING_LEVEL >= ERROR_WARN_INFO_DEBUG)
			Log.d(Constant.LOG_TAG, msg, e);
	}
}
