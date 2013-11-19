package com.jm.util;

import android.content.Context;
import android.widget.Toast;

public class TispToastFactory {

	private static Context context = null;

	private static Toast toast = null;

	/**
	 * 
	 * 
	 * 
	 * @param context
	 *            使用时的上下文
	 * 
	 * 
	 * 
	 * @param hint
	 *            在提示框中需要显示的文本
	 * 
	 * 
	 * 
	 * @return 返回一个不会重复显示的toast
	 * 
	 * 
	 * 
	 * */

	public static Toast getToast(Context context, String hint) {
		if (TispToastFactory.context == context) {
			toast.cancel();
			toast.setText(hint);
		} else {
			TispToastFactory.context = context;
			toast = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
		}
		return toast;
	}

}