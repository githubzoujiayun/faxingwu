package com.jm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Looper;

import com.jm.finals.Constant;
import com.jm.session.SessionManager;

public class UnCatchException implements UncaughtExceptionHandler {

	private UncaughtExceptionHandler ueh;
	private SessionManager session;

	private UnCatchException() {

	}

	public static UnCatchException getInstance() {
		return new UnCatchException();
	}

	public void init(Context context) {
		ueh = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		session = SessionManager.getInstance();
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		catchException(arg1);
		if (ueh != null)
			ueh.uncaughtException(arg0, arg1);
	}

	private boolean catchException(Throwable ex) {
		if (ex == null)
			return false;
		final StackTraceElement[] stack = ex.getStackTrace();
		final String message = ex.getMessage();
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				LogUtil.e("������˼��������ǰ��ץ�������ˡ�message = " + message);
				// ����ֻ����һ���ļ����Ժ�ȫ��������appendȻ���ͣ������ͻ����ظ�����Ϣ�����˲��Ƽ�
				String fileName = Constant.CRASH_FILE_NAME;
				File file = new File(session.getCacheDir(), fileName);
				try {
					FileOutputStream fos = new FileOutputStream(file, true);
					fos.write(message.getBytes());
					LogUtil.e("��ʼд�������Ϣ");
					for (int i = 0; i < stack.length; i++) {
						fos.write(stack[i].toString().getBytes());
					}
					LogUtil.e("������Ϣд�����");
					LogUtil.e("session.getCacheDir() = "
							+ session.getCacheDir());
					LogUtil.e("fileName() = " + fileName);
					fos.flush();
					fos.close();
				} catch (Exception e) {
				}
				Looper.loop();
			}

		}.start();
		return true;
	}
}
