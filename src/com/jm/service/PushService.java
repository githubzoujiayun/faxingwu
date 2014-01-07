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

	private int id = 0;

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
					if (isPush) {
						checkRese();
					}
				}
			}
		}, 300 * 1000, 300 * 1000);

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
							LogUtil.i("���޿����͵���Ϣ");
						} else {
							for (Push push : plist) {
								showNotification(push.getContent());
							}

						}
					}
					LogUtil.i("���޿����͵���Ϣ");

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
	 * ��service����Ϊ�����ȥ��ȡһ���Ƿ���δ�����ź���Ҫ���͵���Ϣ
	 */
	// private void setAlarm() {
	// // �ظ�����,����Ϊ10��
	// am.setRepeating(AlarmManager.RTC_WAKEUP,
	// System.currentTimeMillis() + 300 * 1000, 300 * 1000L, pn);
	// }

	// ����Ƿ�����
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

		String activityid = msg.substring(2);
		String type = msg.substring(0, 1);
		LogUtil.e("type = " + type);
		LogUtil.e("activityid = " + activityid);

		Intent intent = null;

		// ˽��1 ����2 ԤԼ3 ��˿4
		if ("1".equals(type)) {
			intent = new Intent(this, ChatUI.class);
			intent.putExtra("isPushIn", true);
			intent.putExtra("tid", activityid);
			msg = "����һ����˽��";
		}
		if ("2".equals(type)) {
			intent = new Intent(this, HairItemInfoUI.class);
			intent.putExtra("isPushIn", true);
			intent.putExtra("hid", activityid);
			msg = "����һ��������";
		}
		if ("3".equals(type)) {
			if (SessionManager.getInstance().getUsertype().equals("1")) {
				intent = new Intent(this, YuYueInfoUI_User.class);
				intent.putExtra("rid", activityid);
			}
			if (SessionManager.getInstance().getUsertype().equals("2")) {
				intent = new Intent(this, YuYueInfoUI_Haier.class);
				intent.putExtra("rid", activityid);
			}
			intent.putExtra("isPushIn", true);
			msg = "����һ����ԤԼ";
		}
		if ("4".equals(type)) {
			intent = new Intent(this, UserListUI.class);
			intent.putExtra("isPushIn", true);
			intent.putExtra("type", "fanslist");
			msg = "����һ���·�˿";
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
		mNM.notify(id, notification);
		// id++;
	}

	private Map<String, Object> getMsgInqVal() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", SessionManager.getInstance().getUserId());
		return map;
	}

}
