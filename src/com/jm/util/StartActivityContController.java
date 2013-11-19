package com.jm.util;

import java.util.Map;

import android.content.Context;
import android.content.Intent;

import com.jm.fxw.LoginUI;
import com.jm.fxw.QingBaoUI_Haier;
import com.jm.fxw.QingBaoUI_User;
import com.jm.fxw.WodeUI_Hairer;
import com.jm.fxw.WodeUI_User;
import com.jm.fxw.YuYueInfoUI_Haier;
import com.jm.fxw.YuYueInfoUI_User;
import com.jm.fxw.YuYueUI_Haier;
import com.jm.fxw.YuYueUI_User;
import com.jm.session.SessionManager;

public class StartActivityContController {

	public static final int wode = 101;
	private static SessionManager sm;

	/**
	 * �÷�����ʵ���ж��Ƿ��¼������ת
	 * 
	 * @param c
	 *            ��ǰactivity
	 * @param cls
	 *            ��Ҫ��ת��activity
	 * @param checklogin
	 *            �Ƿ���Ҫ��֤
	 */
	public static void goPage(Context c, Class<?> cls, boolean checklogin) {
		Intent i = new Intent(c, cls);
		if (checklogin) {
			sm = SessionManager.getInstance();
			if (sm.isLogin()) {
				c.startActivity(i);
			} else {
				i.setClass(c, LoginUI.class);
				c.startActivity(i);
			}
		} else {

			c.startActivity(i);
		}
	}

	public static void goPage(Context c, Class<?> cls, boolean checklogin,
			Map<String, String> hm) {
		Intent i = new Intent(c, cls);

		for (String key : hm.keySet()) {
			i.putExtra(key, hm.get(key).toString());
			LogUtil.e("Intent: key=" + key + "|value=" + hm.get(key).toString());
		}
		if (checklogin) {
			sm = SessionManager.getInstance();
			if (sm.isLogin()) {
				c.startActivity(i);
			} else {
				i.setClass(c, LoginUI.class);
				c.startActivity(i);
			}
		} else {
			c.startActivity(i);
		}
	}

	/**
	 * �÷������������ַ����Ʋ�ͬ��ҳ����ת
	 * 
	 * @param c
	 *            ��ǰactivity
	 * @param intent
	 *            ��ת����
	 */
	public static void goPage(Context c, int intent) {
		Intent i = getIntent(c, intent);
		sm = SessionManager.getInstance();
		if (sm.isLogin()) {
			c.startActivity(i);
		} else {
			i.setClass(c, LoginUI.class);
			c.startActivity(i);
		}
	}

	private static Intent getIntent(Context c, int intent) {
		sm = SessionManager.getInstance();
		Intent in = new Intent();
		if (101 == intent) {
			if ("1".equals(sm.getUsertype())) {
				LogUtil.e("��ת��������");
				return in.setClass(c, WodeUI_User.class);
			} else if ("2".equals(sm.getUsertype())) {
				LogUtil.e("��ת����ʦ����");
				return in.setClass(c, WodeUI_Hairer.class);
			}
		}
		if (102 == intent) {
			if ("1".equals(sm.getUsertype())) {
				LogUtil.e("��ת����ԤԼҳ��");
				return in.setClass(c, YuYueUI_User.class);
			} else if ("2".equals(sm.getUsertype())) {
				LogUtil.e("��ת����ʦԤԼ");
				return in.setClass(c, YuYueUI_Haier.class);
			}
		}
		if (103 == intent) {
			if ("1".equals(sm.getUsertype())) {
				LogUtil.e("��ת����ԤԼҳ��");
				return in.setClass(c, YuYueInfoUI_User.class);
			} else if ("2".equals(sm.getUsertype())) {
				LogUtil.e("��ת����ʦԤԼ");
				return in.setClass(c, YuYueInfoUI_Haier.class);
			}
		}
		if (104 == intent) {
			if ("1".equals(sm.getUsertype())) {
				LogUtil.e("��ת����ԤԼҳ��");
				return in.setClass(c, QingBaoUI_User.class);
			} else if ("2".equals(sm.getUsertype())) {
				LogUtil.e("��ת����ʦԤԼ");
				return in.setClass(c, QingBaoUI_Haier.class);
			}
		}
		return in;

	}
}
