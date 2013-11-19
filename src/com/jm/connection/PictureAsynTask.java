package com.jm.connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.os.AsyncTask;

import com.jm.fxw.ClientApp;
import com.jm.util.ImageUtil;
import com.jm.util.LogUtil;

public class PictureAsynTask extends AsyncTask<Integer, PictureObject, Integer> {

	private Context cntx;
	private AbstractPictureCallback callback;
	private int count = 0, downloaded = 0, failed = 0;

	public PictureAsynTask(Context cntx, AbstractPictureCallback callback) {
		this.cntx = cntx;
		this.callback = callback;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		while (!callback.isInterrupted() && !callback.isEmpty()) {
			process();
		}

		return count;
	}

	private void process() {
		Connection conn = ((ClientApp) cntx.getApplicationContext())
				.getConnection();

		List<PictureObject> pList = new ArrayList<PictureObject>();
		pList.addAll(callback.getPictureList());

		LogUtil.d("Start to process for " + pList.size() + " pictures");

		for (int i = 0; i < pList.size(); i++) {
			if (callback.isInterrupted()) {
				LogUtil.i("PictureAsynTask Interrupted");
				break;
			}

			PictureObject pObj = pList.get(i);

			if (pObj == null) {
				continue;
			}

			if (pObj.getId() == null || "".equals(pObj.getId())
					|| "null".equals(pObj.getId())) {
				pObj.setStatus(PictureObject.STATUS_NOT_CHECK);
			} else if (ImageUtil.pictureExists(pObj.getId())) {
				pObj.setStatus(PictureObject.STATUS_ALREADY_EXISTS);
			} else {
				boolean flag = getBitmap(conn, pObj.getId());
				if (flag) {
					pObj.setStatus(PictureObject.STATUS_SUCCESSFUL);
					downloaded++;
				} else {
					pObj.setStatus(PictureObject.STATUS_FAIL);
					failed++;
				}
			}

			count++;

			publishProgress(pObj);

			callback.remove(pObj);
		}
		pList.clear();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Integer result) {
		LogUtil.d(result + " pictures processed, " + downloaded
				+ " downloaded and " + failed + " failed.");
		callback.interrupt();
	}

	@Override
	protected void onProgressUpdate(PictureObject... values) {
		for (int i = 0; i < values.length; i++) {
			callback.callback(values[i]);
		}
	}

	public boolean getBitmap(Connection conn, String fileId) {
		if (conn == null) {
			LogUtil.w("No connnection available for file " + fileId);
			return false;
		}

		if (fileId == null || fileId.trim().length() == 0) {
			return false;
		}

		// String imageUrl = Constant.DEFAULT_IMAGE_SERVER+fileId;

		try {
			// JSONObject json = new JSONObject();
			// json.put("brand", Build.BRAND);
			// json.put("model", Build.MODEL);
			// imageUrl += "?inqVal=" + URLEncoder.encode(json.toString());
			// LogUtil.d("fileid"+fileId);
			LogUtil.e("fileId = (" + fileId.toString() + ")");
			HttpGet req = new HttpGet(fileId.trim());

			LogUtil.d("REQUEST: " + req.getURI());

			int statusCode = Response.DEFAULT_INT_VALUE;
			synchronized (conn) {
				HttpResponse response = conn.execute(req);

				if (response.getStatusLine() != null) {
					statusCode = response.getStatusLine().getStatusCode();
				}

				if (statusCode == HttpURLConnection.HTTP_OK) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						ImageUtil.saveCachedBitmap(fileId, entity.getContent());

						entity.consumeContent();
						return true;
					}
				}
			}
			LogUtil.d("RESPONSE(" + statusCode + ")");
		} catch (IOException e) {
			LogUtil.e("", e);
			// } catch (JSONException e) {
			// LogUtil.e("", e);
		} catch (Exception e) {
			LogUtil.e("", e);
		}

		return false;
	}
}
