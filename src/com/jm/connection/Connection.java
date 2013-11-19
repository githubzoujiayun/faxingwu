package com.jm.connection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.telephony.TelephonyManager;

import com.jm.finals.Constant;
import com.jm.fxw.CheckUpdate;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;

/**
 * 
 * 连接实例，建议尽量减少HttpClient实例的数量
 * 
 */
public class Connection {

	public static final String DEFAULT_ENCOING = "UTF-8";

	private String server = null;

	private Context context;
	private HttpClient client;
	private SessionManager session = SessionManager.getInstance();
	private int timeoutConnection = 10000; // 十秒钟
	private boolean stopDownLoadFile = false;

	public Connection(String server, Context context) {
		super();
		this.server = server;
		this.context = context;

		init();
	}

	public boolean isStopDownLoadFile() {
		return stopDownLoadFile;
	}

	public void setStopDownLoadFile(boolean stopDownLoadFile) {
		this.stopDownLoadFile = stopDownLoadFile;
	}

	private void init() {
		HttpParams params = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.

		// params.setParameter("http.useragent",
		// "Mozilla/4.0 (compatible; MSIE 6.0; Android 1.5+; RIT_LIFE; "
		// + SessionManager.getInstance().getAppVersion() + ";)");

		client = new DefaultHttpClient();

		checkProxy();
	}

	// 类似的wifi是否打开
	public boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	// 网络连接是否好用
	public boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();
		if (network != null) {
			return network.isAvailable();
		}
		return false;
	}

	private void checkProxy() {

		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			// 获取当前正在使用的APN接入点
			Uri uri = Uri.parse("content://telephony/carriers/preferapn");
			Cursor mCursor = context.getContentResolver().query(uri, null,
					null, null, null);
			if (mCursor != null && mCursor.moveToFirst()) {
				// 游标移至第一条记录，当然也只有一条
				String proxyStr = mCursor.getString(mCursor
						.getColumnIndex("proxy"));
				if (proxyStr != null && proxyStr.trim().length() > 0) {
					HttpHost proxy = new HttpHost(proxyStr, 80);
					client.getParams().setParameter(
							ConnRouteParams.DEFAULT_PROXY, proxy);
				}
			}
		}
	}

	private Response getNewResponse() {
		return new Response("{\"code\":\"901\",\"msg\":\"无法连接服务器，请检查网络\"}",
				null);
	};

	/**
	 * 
	 * 
	 * 该方法调用execute方法向服务器发送请求，并将结果解析为JSON对象
	 * 
	 * 注：该方法捕捉所有异常信息，执行成功则返回Response对象
	 * 
	 * @return
	 */
	public synchronized Response executeAndParse(String urn,
			Map<String, Object> request) {

		HttpResponse httpResponse = null;
		try {
			httpResponse = execute(urn, request);
		} catch (IOException e) {
			LogUtil.e(e.getMessage(), e);
		}

		if (httpResponse != null) {
			try {
				Response response = new Response(httpResponse);
				response.parse();
				return response;

			} catch (JSONException e) {
				LogUtil.e(e.getMessage(), e);
			} catch (ParseException e) {
				LogUtil.e(e.getMessage(), e);
			} catch (IOException e) {
				LogUtil.e(e.getMessage(), e);
			}
		}

		return getNewResponse();
	}

	public synchronized Response executeAndParse(String urn,
			Map<String, Object> request, Uri uri) {

		HttpResponse httpResponse = null;
		try {
			httpResponse = execute(urn, request, uri);
		} catch (IOException e) {
			LogUtil.e(e.getMessage(), e);
		}

		if (httpResponse != null) {
			try {
				Response response = new Response(httpResponse);
				response.parse();
				return response;

			} catch (JSONException e) {
				LogUtil.e(e.getMessage(), e);
			} catch (ParseException e) {
				LogUtil.e(e.getMessage(), e);
			} catch (IOException e) {
				LogUtil.e(e.getMessage(), e);
			}
		}

		return getNewResponse();
	}

	/**
	 * 
	 * 该方法调用execute方法向服务器发送请求，并将结果解析为JSON对象
	 * 
	 * 注：该方法捕捉所有异常信息，执行成功则返回Response对象
	 * 
	 * @return
	 */
	public synchronized Response executeAndParse(String urn) {
		HttpResponse httpResponse = null;
		try {
			httpResponse = execute(urn);
		} catch (IOException e) {
			LogUtil.e(e.getMessage(), e);
		}

		if (httpResponse != null) {
			try {
				Response response = new Response(httpResponse);
				response.parse();
				return response;
			} catch (ParseException e) {
				LogUtil.e("", e);
			} catch (IOException e) {
				LogUtil.e("", e);
			} catch (JSONException e) {
				LogUtil.e("", e);
			}
		}

		return getNewResponse();
	}

	// public synchronized String executeAndFetch(String urn, JSONObject
	// request)
	// throws ParseException, IOException {
	// if (!(isWifiEnabled(context) && isNetworkConnected(context))) {
	// return new String("{\"code\":\"901\",\"msg\":\"网络异常\"}");
	// }
	// HttpResponse response = null;
	// try {
	// response = execute(urn);
	// } catch (IOException e) {
	// LogUtil.e(e.getMessage(), e);
	// }
	//
	// if (response == null) {
	// return null;
	// }
	//
	// if (response.getStatusLine() == null) {
	// return null;
	// }
	//
	// int statusCode = response.getStatusLine().getStatusCode();
	//
	// String content = null;
	// if (statusCode == HttpURLConnection.HTTP_OK) {
	// HttpEntity entity = response.getEntity();
	// if (entity != null) {
	// content = EntityUtils.toString(entity, DEFAULT_ENCOING);
	// entity.consumeContent();
	// }
	// }
	//
	// LogUtil.d("RESPONSE(" + statusCode + "): " + content);
	//
	// return content;
	// }

	/**
	 * 按
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private HttpResponse execute(String urn) throws IOException {

		HttpGet req = new HttpGet(urn);

		LogUtil.d("REQUEST:>>>" + urn);

		return execute(req);
	}

	/**
	 * 按照通信协议，所有发往后台的请求都以inqVal为名的值为json字符串的请求中
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	private HttpResponse execute(String urn, Map<String, Object> request)
			throws IOException {
		HttpPost req = new HttpPost(urn);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String key : request.keySet()) {

			if (key == null || request.get(key) == null) {
				LogUtil.e("key = " + key + "空的post值");
				continue;
			}
			nvps.add(new BasicNameValuePair(key, request.get(key).toString()));
			LogUtil.i("REQUEST: key=" + key + "|value="
					+ request.get(key).toString());
		}
		LogUtil.i("REQUEST: >>>" + urn);

		req.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_ENCOING));

		return execute(req);
	}

	/**
	 * 按照通信协议，所有发往后台的请求都以inqVal为名的值为json字符串的请求中
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	private HttpResponse execute(String urn, Map<String, Object> request,
			Uri uri) throws IOException {
		HttpPost req = new HttpPost(urn);
		File file = new File(uri.toString());
		FileEntity entity = new FileEntity(file, "binary/octet-stream");
		req.setEntity(entity);
		entity.setContentEncoding("binary/octet-stream");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String key : request.keySet()) {
			nvps.add(new BasicNameValuePair(key, request.get(key).toString()));
			LogUtil.e("REQUEST: key=" + key + "|value="
					+ request.get(key).toString());
		}
		LogUtil.d("REQUEST: >>>" + urn);

		req.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_ENCOING));

		return execute(req);
	}

	/*
	 * 调用该方法需要同步（synchronized）
	 */
	public HttpResponse execute(HttpUriRequest req) throws IOException {
		try {
			return executeRequest(req);
		} catch (IOException e) {
			// 重置连接
			LogUtil.i("重置连接");
			client.getConnectionManager().shutdown();
			init();
			return executeRequest(req);
		}
	}

	/*
	 * 参考http://code.google.com/p/android/issues/detail?id=5255
	 */
	private HttpResponse executeRequest(HttpUriRequest req) throws IOException {
		try {
			return client.execute(req);
		} catch (RuntimeException e) {

			LogUtil.w(e.getMessage(), e);
			try {
				return client.execute(req);
			} catch (RuntimeException e2) {
				LogUtil.w(e2.getMessage(), e2);
				return null;
			}
		}
	}

	public String executeAndFetch(HttpUriRequest req, String encoding)
			throws IOException {
		synchronized (client) {
			HttpResponse response = null;
			try {
				response = execute(req);
			} catch (IOException e) {
				LogUtil.e(e.getMessage(), e);
			}

			if (response == null) {
				return null;
			}

			if (response.getStatusLine() == null) {
				return null;
			}

			int statusCode = response.getStatusLine().getStatusCode();

			String content = null;
			if (statusCode == HttpURLConnection.HTTP_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					content = EntityUtils.toString(entity, encoding);
					entity.consumeContent();
				}
			}

			LogUtil.d("RESPONSE(" + statusCode + "): " + content);

			return content;
		}
	}

	public String getServer() {
		return server;
	}

	public synchronized boolean getNewVersion(String urlString) {

		String url = urlString;
		HttpGet req = new HttpGet(url);
		LogUtil.d("REQUEST: " + req.getURI());
		int statusCode = Response.DEFAULT_INT_VALUE;
		synchronized (client) {
			try {
				HttpResponse response = client.execute(req);

				if (response.getStatusLine() != null) {
					statusCode = response.getStatusLine().getStatusCode();
				}
				if (statusCode == HttpURLConnection.HTTP_OK) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					String fileName = SessionManager.getInstance()
							.getCacheDir() + Constant.ZIP;
					File newFile = new File(fileName);
					if (newFile.exists()) {
						newFile.delete();
					}
					newFile.mkdirs();
					FileOutputStream fos = new FileOutputStream(fileName);
					int len;
					byte[] buffer = new byte[1024];
					while ((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					entity.consumeContent();
					return true;
				}
			} catch (IOException e) {
				LogUtil.e(e.getMessage(), e);
			}

		}
		return false;
	}

	public synchronized boolean downloadImage(String urlString) {

		HttpPost req = new HttpPost(urlString);
		LogUtil.d("REQUEST: " + req.getURI());
		LogUtil.i("filename: " + urlString);
		int statusCode = Response.DEFAULT_INT_VALUE;
		synchronized (client) {
			try {
				HttpResponse response = client.execute(req);

				if (response.getStatusLine() != null) {
					statusCode = response.getStatusLine().getStatusCode();
				}
				if (statusCode == HttpURLConnection.HTTP_OK) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					String fileName = SessionManager.getInstance()
							.getCacheDir() + urlString;

					File newFile = new File(fileName);
					if (!newFile.exists()) {
						newFile.createNewFile();
					}
					FileOutputStream fos = new FileOutputStream(fileName);
					int len;
					byte[] buffer = new byte[1024];
					while ((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					entity.consumeContent();
					return true;
				}
			} catch (IOException e) {
				LogUtil.e(e.getMessage(), e);
			}

		}
		return false;
	}

	public synchronized int loadNewApp(String localPath, String url) {
		String path = session.getCacheDir();
		if (path == null) {
			return Constant.DOWNLOAD_FAILTURE;
		}
		File file = new File(session.getCacheDir() + localPath
				+ Constant.APP_END);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			LogUtil.e(e.getMessage(), e);
		}
		HttpGet req;
		URI uri;
		try {
			uri = new URI(url);
			req = new HttpGet(uri);
		} catch (URISyntaxException e1) {
			// LogUtil.error(Constant.FILEDB_LOG, "URLEncoder: " + url);
			req = new HttpGet(encodeUrl(url));
		}
		HttpResponse response;
		try {
			response = execute(req);
			if (response == null) {
				return Constant.DOWNLOAD_FAILTURE;
			}

			if (response.getStatusLine() == null) {
				return Constant.DOWNLOAD_FAILTURE;
			}

			int statusCode = response.getStatusLine().getStatusCode();
			LogUtil.d("statusCode" + statusCode);
			if (statusCode == HttpURLConnection.HTTP_PARTIAL
					|| statusCode == HttpURLConnection.HTTP_OK) {
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();

				long curPosition = 0, contentLength = 0;
				Header header = response.getFirstHeader("Content-Length");
				if (header != null) {
					contentLength = Integer.parseInt(header.getValue());
				}
				FileOutputStream accessFile = new FileOutputStream(file);
				// LogUtil.d("contentLength"+contentLength);

				byte buf[] = new byte[8 * 1024];
				int numread = -1;
				while ((numread = is.read(buf)) != -1 && !stopDownLoadFile) {
					accessFile.write(buf, 0, numread);
					curPosition += numread;
					float f = ((float) curPosition) / ((float) contentLength);

					Message msg = CheckUpdate.handler.obtainMessage();
					msg.what = CheckUpdate.MSG_WHAT;
					msg.obj = Float.toString((int) (f * 100));
					msg.sendToTarget();

				}
				is.close();
				accessFile.close();
				if (stopDownLoadFile) {
					return Constant.DOWNLOAD_FAILTURE;
				} else {
					return Constant.DOWNLOAD_SUCCESS;
				}
			}
		} catch (IOException e) {
			LogUtil.e(e.getMessage(), e);
		}
		return Constant.DOWNLOAD_FAILTURE;
	}

	public synchronized int loadFile(String localPath, String url) {
		LogUtil.i("test url:" + url);
		int count = 0;
		String path = SessionManager.getInstance().getCacheDir();
		LogUtil.i("paht : " + path);
		if (path == null) {
			// LogUtil.info(Constant.FILEDB_LOG, "PATH is null");
			return Constant.DOWNLOAD_FAILTURE;
		}
		LogUtil.i("localpath: " + path + localPath);
		File destFile = new File(path + localPath);
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		long curPosition = 0, contentLength = 0;
		// SharedPreferences share =
		// context.getSharedPreferences(Constant.PREFS_NAME,
		// Context.MODE_PRIVATE);
		// contentLength = share.getLong(Constant.CONTENTLENGTH, 0);

		File tempFile = new File(destFile.getPath() + Constant.TEMP);
		if (tempFile.exists()) {
			// tempFile.delete();
			try {
				FileInputStream in = new FileInputStream(tempFile);
				curPosition = in.available();
				in.close();
			} catch (Exception e) {
				LogUtil.e(e.getMessage(), e);
			}
			// curPosition = tempFile.length();
		}
		FileOutputStream accessFile;

		HttpGet req;

		URI uri;
		try {
			uri = new URI(url);
			req = new HttpGet(uri);
		} catch (URISyntaxException e1) {
			// LogUtil.error(Constant.FILEDB_LOG, "URLEncoder: " + url);
			req = new HttpGet(encodeUrl(url));
		}

		req.setHeader("User-Agent", "NetFox");

		LogUtil.d("curPosition" + curPosition);
		while (count < 3) {
			// log.debug("count " + count + " for " + localPath);
			try {
				if (curPosition > 0) {
					String sProperty = "bytes=" + curPosition + "-";
					req.setHeader("Range", sProperty);
				}
				HttpResponse response = execute(req);

				if (response == null) {
					return Constant.DOWNLOAD_FAILTURE;
				}

				if (response.getStatusLine() == null) {
					return Constant.DOWNLOAD_FAILTURE;
				}
				if (contentLength == 0) {
					Header header = response.getFirstHeader("Content-Length");
					if (header != null) {
						contentLength = Integer.parseInt(header.getValue());
						// SharedPreferences.Editor editor = share.edit();
						// editor.putLong(Constant.CONTENTLENGTH,
						// contentLength);
						// editor.commit();
					}
				}
				LogUtil.d("contentLength" + contentLength);
				int statusCode = response.getStatusLine().getStatusCode();
				LogUtil.d("statusCode" + statusCode);
				if (statusCode == HttpURLConnection.HTTP_PARTIAL
						|| statusCode == HttpURLConnection.HTTP_OK) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();

					accessFile = new FileOutputStream(tempFile, true);
					byte buf[] = new byte[8 * 1024];
					int numread = -1;
					while ((numread = is.read(buf)) != -1) {
						accessFile.write(buf, 0, numread);
						curPosition += numread;
						// Message msg = HomeUI.hander.obtainMessage();
						// float f = ((float) curPosition)
						// / ((float) contentLength);
						// LogUtil.d("percent"+f);
						// msg.obj = Float.toString(f);
						// msg.what = Constant.MSG_WHAT;
						// msg.sendToTarget();
					}
					is.close();
					LogUtil.i("tmp: " + destFile.getPath() + Constant.TEMP);
					File file = new File(destFile.getPath() + Constant.TEMP);
					long fileSize = file.length();
					accessFile.close();

					if (fileSize != curPosition) {
						// LogUtil.fatal(Constant.FILEDB_LOG, "file size: " +
						// fileSize + " curPosition: " + curPosition);
						if (tempFile.exists()) {
							tempFile.delete();
						}
						return Constant.DOWNLOAD_FAILTURE;
					}
					// SharedPreferences.Editor edit = share.edit();
					// edit.putLong(Constant.CONTENTLENGTH, 0);
					// edit.commit();
					tempFile.renameTo(destFile);
					if (tempFile.exists()) {
						tempFile.delete();
					}

					return Constant.DOWNLOAD_SUCCESS;
				} else {
					// LogUtil.fatal(Constant.FILEDB_LOG, "httpRequest code: " +
					// statusCode);
					return Constant.DOWNLOAD_FAILTURE;
				}
			} catch (SocketException se) {
				LogUtil.e(se.getMessage(), se);
				count++;
				if (count == 3) {
					LogUtil.d("count" + count);
					// Message msg = HomeUI.hander.obtainMessage();
					// msg.obj = "";
					// msg.what = Constant.MSG_LOAD;
					// msg.sendToTarget();
				}
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e1) {
				}
				reconnect();
			} catch (IOException e) {
				LogUtil.e(e.getMessage(), e);
				count++;
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e1) {
				}
			} finally {

			}
		}
		return Constant.DOWNLOAD_FAILTURE;
	}

	private void reconnect() {
		client.getConnectionManager().shutdown();
		init();
	}

	private String encodeUrl(String url) {
		String[] arrayUrl = url.split("/");
		String encodedUrl = "";
		try {
			for (int j = 0; j < arrayUrl.length; j++) {
				if (arrayUrl[j] != null && j > 2) {
					encodedUrl += "/"
							+ URLEncoder.encode(arrayUrl[j], Constant.ENCODING);
				} else if (j < 3) {
					encodedUrl += arrayUrl[j] + "/";
				} else {
					encodedUrl += "/";
				}
			}
		} catch (UnsupportedEncodingException e) {
			LogUtil.e(e.getMessage(), e);
		}
		return encodedUrl;
	}

	public synchronized String uploadFile(File file) {
		final File fileTemp = file;
		LogUtil.d("fileTemp.getAbsolutePath()" + fileTemp.getAbsolutePath());
		try {
			URL url = new URL(Constant.DEFAULT_SERVER + Constant.URN_ADD_IMG);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url
					.openConnection();
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setRequestMethod("POST");
			OutputStream os = httpUrlConnection.getOutputStream();
			BufferedInputStream fis = new BufferedInputStream(
					new FileInputStream(fileTemp));
			int bufSize = 0;
			byte[] buffer = new byte[1024];
			while ((bufSize = fis.read(buffer)) != -1) {
				os.write(buffer, 0, bufSize);
			}
			fis.close();
			return new JSONObject(new BufferedReader(new InputStreamReader(
					httpUrlConnection.getInputStream())).readLine())
					.getString("image");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// public synchronized String uploadFile(String file) {
	// try {
	// URL url = new URL(Constant.DEFAULT_IMAGE_SERVER
	// + Constant.URN_ADD_IMG);
	// HttpURLConnection httpUrlConnection = (HttpURLConnection) url
	// .openConnection();
	// httpUrlConnection.setDoOutput(true);
	// httpUrlConnection.setDoInput(true);
	// httpUrlConnection.setRequestMethod("POST");
	// OutputStream os = httpUrlConnection.getOutputStream();
	// Thread.sleep(100);
	// BufferedInputStream fis = new BufferedInputStream(
	// new FileInputStream(file));
	//
	// int bufSize = 0;
	// byte[] buffer = new byte[1024];
	// while ((bufSize = fis.read(buffer)) != -1) {
	// os.write(buffer, 0, bufSize);
	// }
	// fis.close();
	//
	// String responMsg = httpUrlConnection.getResponseMessage();
	// LogUtil.e("responMsg =====" + responMsg);
	// } catch (Exception e) {
	// LogUtil.d(e.toString());
	// }
	// return null;
	// }
	// public synchronized String uploadFile(String file) {
	// String fileId = null;
	// try {
	// LogUtil.d("Uploading file " + file);
	// HttpPost httpPost = new HttpPost(Constant.DEFAULT_IMAGE_SERVER
	// + Constant.URN_ADD_IMG);
	//
	// FileBody fBd = new FileBody(new File(file));
	//
	// MultipartEntity reqEntity = new MultipartEntity();
	//
	// reqEntity.addPart("input", fBd);
	//
	// httpPost.setEntity(reqEntity);
	//
	// HttpResponse httpResponse = client.execute(httpPost);
	// Response response = new Response(httpResponse);
	//
	// response.parse();
	//
	// if (response.isHttpOK() && response.isSuccessful()) {
	// LogUtil.e("response=" + response);
	// fileId = response.getString("image");
	// }
	//
	// } catch (Exception e) {
	// LogUtil.e(e.toString());
	// }
	// return fileId;
	// }

	// public synchronized String uploadFile(String file) {
	//
	// // String end = "\r\n";
	// // String twoHyphens = "--";
	// String boundary = "*****";
	//
	// LogUtil.e("开始上传图片");
	// try {
	// URL url = new URL(Constant.DEFAULT_IMAGE_SERVER
	// + Constant.URN_ADD_IMG);
	// HttpURLConnection con = (HttpURLConnection) url.openConnection();
	// /* 允许Input、Output，不使用Cache */
	// // con.setReadTimeout(5 * 1000);
	// con.setDoInput(true);
	// con.setDoOutput(true);
	// con.setUseCaches(false);
	// /* 设定传送的method=POST */
	// con.setRequestMethod("POST");
	// /* setRequestProperty */
	// con.setRequestProperty("Connection", "Keep-Alive");
	// con.setRequestProperty("Charset", "UTF-8");
	// con.setRequestProperty("enctype", "multipart/form-data;boundary="
	// + boundary);
	// /* 设定DataOutputStream */
	// DataOutputStream ds = new DataOutputStream(con.getOutputStream());
	// /*
	// * ds.writeBytes(twoHyphens + boundary + end);
	// * ds.writeBytes("Content-Disposition: form-data; " +
	// * "name=\"file1\";filename=\"" + newName +"\"" + end);
	// * ds.writeBytes(end);
	// */
	// /* 取得文件的FileInputStream */
	// FileInputStream fStream = new FileInputStream(new File(file));
	// /* 设定每次写入1024bytes */
	// int bufferSize = 1024;
	// byte[] buffer = new byte[bufferSize];
	// int length = -1;
	// /* 从文件读取数据到缓冲区 */
	// while ((length = fStream.read(buffer)) != -1) {
	// /* 将数据写入DataOutputStream中 */
	// ds.write(buffer, 0, length);
	// LogUtil.e("写入流数据");
	// }
	// // ds.writeBytes(end);
	// // ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
	// /* close streams */
	// fStream.close();
	// ds.flush();
	//
	// LogUtil.e("上传完成");
	// /* 取得Response内容 */
	// InputStream is = con.getInputStream();
	// int ch;
	// StringBuffer b = new StringBuffer();
	// LogUtil.e("读取响应信息");
	// while ((ch = is.read()) != -1) {
	// b.append((char) ch);
	// }
	// LogUtil.e("响应信息" + b.toString());
	// /* 将Response显示于Dialog */
	// /* 关闭DataOutputStream */
	// ds.close();
	// } catch (Exception e) {
	// LogUtil.e("上传失败" + e.toString());
	// }
	//
	// return null;
	// }
}
