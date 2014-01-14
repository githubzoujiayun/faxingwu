package com.jm.util;

import java.util.Map;

import android.content.Context;
import android.content.Intent;

import com.jm.fxw.LoginUI;
import com.jm.fxw.PublicPortfolio;
import com.jm.fxw.PublicPortfolioOfHair;
import com.jm.fxw.QingBaoUI_Haier;
import com.jm.fxw.QingBaoUI_User;
import com.jm.fxw.WodeUI_Hairer;
import com.jm.fxw.WodeUI_User;
import com.jm.fxw.YuYueInfoUI_Haier;
import com.jm.fxw.YuYueInfoUI_User;
import com.jm.fxw.YuYueListUI_Haier;
import com.jm.fxw.YuYueListUI_User;
import com.jm.session.SessionManager;

public class StartActivityContController {

	public static final int wode = 101;
	private static SessionManager sm;

	/**
	 * 该方法会实现判断是否登录并且跳转
	 * 
	 * @param c
	 *            当前activity
	 * @param cls
	 *            需要跳转的activity
	 * @param checklogin
	 *            是否需要验证
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

	/**
	 * 判断登录/跳转
	 * 
	 * @param c
	 *            当前activity
	 * @param cls
	 *            需要跳转的activity
	 * @param checklogin
	 *            是否需要验证登录
	 * @param hm
	 *            Map传值
	 */
	public static void goPage(Context c, Class<?> cls, boolean checklogin,
			Map<String, String> hm) {
		Intent i = new Intent(c, cls);

		for (String key : hm.keySet()) {
			i.putExtra(key, hm.get(key).toString());
			LogUtil.i("Intent: key=" + key + "|value=" + hm.get(key).toString());
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
	 * 该方法根据特殊字符控制不同的页面跳转
	 * 
	 * @param c
	 *            当前activity
	 * @param intent
	 *            跳转代码
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
				LogUtil.i("跳转个人中心");
				return in.setClass(c, WodeUI_User.class);
			} else if ("2".equals(sm.getUsertype())) {
				LogUtil.i("跳转发型师中心");
				return in.setClass(c, WodeUI_Hairer.class);
			}
		}
		if (102 == intent) {
			if ("1".equals(sm.getUsertype())) {
				LogUtil.i("跳转个人预约管理页面");
				return in.setClass(c, YuYueListUI_User.class);
			} else if ("2".equals(sm.getUsertype())) {
				LogUtil.i("跳转发型师管理预约");
				return in.setClass(c, YuYueListUI_Haier.class);
			}
		}
		if (103 == intent) {
			if ("1".equals(sm.getUsertype())) {
				LogUtil.i("跳转个人预约详情页面");
				return in.setClass(c, YuYueInfoUI_User.class);
			} else if ("2".equals(sm.getUsertype())) {
				LogUtil.i("跳转发型师预约详情页面");
				return in.setClass(c, YuYueInfoUI_Haier.class);
			}
		}
		if (104 == intent) {
			if ("1".equals(sm.getUsertype())) {
				LogUtil.i("用户情报");
				return in.setClass(c, QingBaoUI_User.class);
			} else if ("2".equals(sm.getUsertype())) {
				LogUtil.i("发型师情报");
				return in.setClass(c, QingBaoUI_Haier.class);
			}
		}
		if (105 == intent) {
			if ("1".equals(sm.getUsertype())) {
				LogUtil.i("用户上传发型页面");
				return in.setClass(c, PublicPortfolio.class);
			} else if ("2".equals(sm.getUsertype())) {
				LogUtil.i("发型师上传发型页面");
				return in.setClass(c, PublicPortfolioOfHair.class);
			}
		}
		return in;

	}
}
