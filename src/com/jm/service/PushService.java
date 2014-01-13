package com.jm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Push;
import com.jm.finals.Constant;
import com.jm.fxw.ChatQuestionUI;
import com.jm.fxw.ChatUI;
import com.jm.fxw.ClientApp;
import com.jm.fxw.HairItemInfoUI;
import com.jm.fxw.R;
import com.jm.fxw.UserListUI;
import com.jm.fxw.YuYueInfoUI_Haier;
import com.jm.fxw.YuYueInfoUI_User;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;

public class PushService extends Service {

	NotificationManager mNM = null;

	private Timer timer;

	private int pusd_type;
	public static boolean isPush = true;

	@Override
	public IBinder onBind(Intent arg0) {
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onCreate() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (checkNetworkInfo()) {
					checkRese();

				}
			}
		}, 3 * 1000, 3 * 1000);

	}

	public void checkRese() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				Connection conn = ((ClientApp) getApplication())
						.getConnection();
				Response result = conn.executeAndParse(Constant.URN_MSG_PUSH,
						getMsgInqVal());
				if (result == null) {
					LogUtil.e("can't get tipssettinginfo");
				}
				if (result != null && result.isSuccessful()) {
					if (!result.getString("push_list").equals("")) {
						List<Push> plist = result.getList("push_list",
								new Push());
						if (plist == null || plist.size() == 0) {
							LogUtil.i("暂无可推送的消息");
						} else {
							for (Push push : plist) {
								showNotification(push.getContent());
							}

						}
					}
					LogUtil.i("暂无可推送的消息");

				}
			}
		});
		thread.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.d("Received start id is " + startId + ", intent is " + intent);
		if (intent != null) {

		}

		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}

	/*
	 * 将service设置为五分钟去读取一次是否有未读新闻和需要推送的信息
	 */
	// private void setAlarm() {
	// // 重复设置,周期为10秒
	// am.setRepeating(AlarmManager.RTC_WAKEUP,
	// System.currentTimeMillis() + 300 * 1000, 300 * 1000L, pn);
	// }

	// 检查是否联网
	private boolean checkNetworkInfo() {
		// Network Connectivity
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo ni = conMan.getActiveNetworkInfo();
		if (ni == null) {
			return false;
		}

		return ni.isAvailable();
	}

	private void showNotification(String msg) {

		String type = msg.substring(0, 1);
		LogUtil.e("type = " + type);
		this.pusd_type = Integer.valueOf(type);
		Intent intent = new Intent();
		intent.putExtra("isPushIn", true);
		// 私信1 评论2 预约3 粉丝4
		if ("1".equals(type)) {

			if (isPush) {
				intent = new Intent(this, ChatUI.class);
				intent.putExtra("isPushIn", true);
				intent.putExtra("tid", msg.split(",")[1]);
				msg = "您有一条新私信";
			} else {
				return;
			}
		}
		if ("2".equals(type)) {
			intent = new Intent(this, HairItemInfoUI.class);
			intent.putExtra("isPushIn", true);
			intent.putExtra("hid", msg.split(",")[1]);
			msg = "您有一条新评论";
		}
		if ("3".equals(type)) {
			if (SessionManager.getInstance().getUsertype().equals("1")) {
				intent.setClass(this, YuYueInfoUI_User.class);
				intent.putExtra("rid", msg.split(",")[1]);
			}
			if (SessionManager.getInstance().getUsertype().equals("2")) {
				intent.setClass(this, YuYueInfoUI_Haier.class);
				intent.putExtra("rid", msg.split(",")[1]);
			}
			msg = "您有一条新预约";
		}
		if ("4".equals(type)) {
			intent.setClass(this, UserListUI.class);
			intent.putExtra("type", "fanslist");
			msg = "您有一个新粉丝";
		}
		if ("5".equals(type)) {
			intent.setClass(this, ChatQuestionUI.class);
			intent.putExtra("tid", msg.split(",")[1]);
			intent.putExtra("pid", msg.split(",")[2]);
			msg = "您的问题有了发型师回答";
		}
		if ("6".equals(type)) {
			intent.setClass(this, ChatQuestionUI.class);
			intent.putExtra("tid", msg.split(",")[1]);
			intent.putExtra("pid", msg.split(",")[2]);
			msg = "周边用户有人向您提问";
		}
		NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNM.cancel(R.string.open_new_message);
		Notification notification = new Notification(R.drawable.logo, msg,
				System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		notification.setLatestEventInfo(this,
				getString(R.string.open_new_message), msg, contentIntent);
		notification.defaults = Notification.DEFAULT_SOUND;
		mNM.notify(pusd_type, notification);
	}

	private Map<String, Object> getMsgInqVal() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", SessionManager.getInstance().getUserId());
		return map;
	}

}
