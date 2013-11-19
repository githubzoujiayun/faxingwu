package com.jm.connection;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.jm.finals.Constant;
import com.jm.fxw.ClientApp;
import com.jm.fxw.R;

public class RequestTask extends AsyncTask<String, Integer, Response> {

	private Connection conn;
	private Context context;
	private JSONObject inqVal;
	private ProgressDialog dialog;
	private ResponseResultCallBack callback;
	private String progressStr;
	private boolean showProgress = true;
	private boolean cancelTask = true;

	private boolean isReceive = true;

	public RequestTask(Context context) {
		this.context = context;
		isReceive = true;
	}

	public boolean isShowProgress() {
		return showProgress;
	}

	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}

	public void setProgressStr(String progress) {
		this.progressStr = progress;
	}

	public void setInqVal(JSONObject json) {
		this.inqVal = json;
	}

	public boolean isCancelTask() {
		return cancelTask;
	}

	public void setCancelTask(boolean cancelTask) {
		this.cancelTask = cancelTask;
	}

	public void setResponseResultCallBack(ResponseResultCallBack cb) {
		this.callback = cb;
	}

	@Override
	protected void onPreExecute() {
		conn = ((ClientApp) context.getApplicationContext()).getConnection();
		if (!conn.isWifiEnabled(context) && !conn.isNetworkConnected(context)) {
			this.cancel(false);
			callback.responseResultCallback(null);
			Toast.makeText(context, R.string.net_unwork, Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (showProgress) {
			dialog = ProgressDialog.show(context, "", progressStr, true);
			dialog.setCancelable(true);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					if (cancelTask) {
						cancel(false);
					}
				}
			});
			dialog.show();
		}
	}

	@Override
	protected Response doInBackground(String... params) {
		Response res = conn.executeAndParse(Constant.DEFAULT_SERVER);
		return res;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();

		isReceive = false;
		callback.responseCancelBack(true);
	}

	@Override
	protected void onPostExecute(Response result) {
		if (!isReceive) {
			return;
		}
		if (showProgress && dialog.isShowing()) {
			dialog.dismiss();
		}
		if (result != null) {
			if (result.isHttpOK()) {
				callback.responseResultCallback(result);
			} else {
				callback.responseResultCallback(null);
				Toast.makeText(context, R.string.server_error,
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(context, R.string.network_error, Toast.LENGTH_LONG)
					.show();
			callback.responseResultCallback(result);
		}
	}

	public interface ResponseResultCallBack {
		void responseResultCallback(Response response);

		void responseCancelBack(boolean cancel);
	}

}
