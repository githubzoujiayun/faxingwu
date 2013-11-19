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
	 *            ʹ��ʱ��������
	 * 
	 * 
	 * 
	 * @param hint
	 *            ����ʾ������Ҫ��ʾ���ı�
	 * 
	 * 
	 * 
	 * @return ����һ�������ظ���ʾ��toast
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