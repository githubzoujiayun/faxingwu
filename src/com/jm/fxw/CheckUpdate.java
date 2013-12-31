package com.jm.fxw;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;

public class CheckUpdate {

	private Context context;
	private String _urlStr;
	private Button ok;
	private Button cancel;
	private GetAppTask getAppTask;
	private Dialog dialog;
	private Connection conn;
	private String appString = "fxw";
	private TextView progressView;
	public static final int MSG_WHAT = 2;

	private boolean shownonewversion = false;
	public static MyHandler handler;

	public CheckUpdate(Context context) {
		this.context = context;
		handler = new MyHandler();
	}

	public void check() {
		LogUtil.i("==============================开始检查更新");
		new getLikeTask().execute();
	}

	public void setNoNewversion(boolean isshow) {
		shownonewversion = isshow;
	}

	/*
	 * 读取喜欢列表
	 */
	class getLikeTask extends AsyncTask<String, Integer, Response> {
		/*
		 * 获取当前程序的版本号
		 */
		private String getVersionName() {
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = null;
			try {
				packInfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return packInfo.versionName;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) ((Activity) context)
					.getApplication()).getConnection();
			return conn
					.executeAndParse("http://wap.faxingw.cn/index.php?m=index&a=check_update&edition="
							+ getVersionName());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get updateinfo");
				return;
			}
			if (result.isSuccessful()) {
				if (!result.getString("url").equals("")) {
					_urlStr = result.getString("url");
					String tip = "";
					try {
						tip = result.getString("msg");
					} catch (Exception e) {
						tip = context.getString(R.string.has_new_version);
					} finally {
						ShowDialog(tip);
					}
				}
			} else {
				if (shownonewversion) {
					Toast.makeText(context, "当前已经是最新版本了", Toast.LENGTH_SHORT)
							.show();
				}
			}

		}
	}

	private void ShowDialog(String info) {
		View dialogView = LayoutInflater.from(context).inflate(
				R.layout.confirm, null);
		ok = (Button) dialogView.findViewById(R.id.loginoutdialog_button_ok);
		cancel = (Button) dialogView
				.findViewById(R.id.loginoutdialog_button_cancel);
		progressView = (TextView) dialogView.findViewById(R.id.tv_dialog);
		dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(dialogView);
		progressView.setText(info);
		dialog.show();
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getAppTask = new GetAppTask();
				getAppTask.execute();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ok.setEnabled(true);
				dialog.dismiss();
			}
		});

		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (conn != null) {
					conn.setStopDownLoadFile(true);
				}
			}
		});
	}

	class GetAppTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected void onPreExecute() {
			ok.setEnabled(false);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			conn = new Connection(_urlStr, context);
			conn.setStopDownLoadFile(false);
			int result = conn.loadNewApp(appString, _urlStr);
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			LogUtil.d("result" + result);
			ok.setEnabled(true);
			if (result == Constant.DOWNLOAD_SUCCESS) {
				installApp();
			} else {
				progressView.setText(R.string.download_faild);
			}
		}
	}

	private void installApp() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		String path = SessionManager.getInstance().getCacheDir() + appString
				+ Constant.APP_END;
		LogUtil.e("path = " + path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_WHAT) {
				progressView.setText(context
						.getString(R.string.download_progress)
						+ msg.obj.toString() + "%");
			}
			super.handleMessage(msg);
		}
	}

}
