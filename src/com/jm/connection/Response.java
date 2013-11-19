package com.jm.connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jm.finals.Constant;
import com.jm.util.JSONUtil;
import com.jm.util.LogUtil;

public class Response {
	public static final int DEFAULT_INT_VALUE = Integer.MIN_VALUE;

	private HttpResponse response;

	private int statusCode = DEFAULT_INT_VALUE;
	private String responseContent = null;

	private JSONObject jsonData = null;

	protected Response(HttpResponse response) throws ParseException,
			IOException {
		super();

		this.response = response;

		if (response.getStatusLine() != null) {
			statusCode = response.getStatusLine().getStatusCode();
		}

		if (statusCode == HttpURLConnection.HTTP_OK) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseContent = EntityUtils.toString(entity,
						Connection.DEFAULT_ENCOING);
				entity.consumeContent();
			}
		}
		LogUtil.d("RESPONSE(" + statusCode + "): " + responseContent);
	}

	protected Response(String fakeResponseContent) {
		statusCode = HttpURLConnection.HTTP_OK;
		responseContent = fakeResponseContent;

		LogUtil.d("FAKERESPONSE(" + statusCode + "): " + responseContent);
	}

	protected Response(String fakeResponseContent, String code) {
		statusCode = HttpURLConnection.HTTP_BAD_REQUEST;
		responseContent = fakeResponseContent;
		JSONObject obj = null;
		try {

			obj = new JSONObject(responseContent);
		} catch (Exception e) {
			LogUtil.e("Response出现问题，本地初始化数据无法转换为json");
		}
		jsonData = obj;

		LogUtil.d("FAKERESPONSE(" + statusCode + "): " + responseContent);
	}

	public static Response buildFakeResponse(String fakeResponseContent) {
		if (fakeResponseContent == null) {
			return null;
		}

		return new Response(fakeResponseContent);
	}

	public JSONObject parse() throws JSONException {
		if (responseContent == null) {
			return null;
		}
		JSONObject obj = null;
		try {

			obj = new JSONObject(responseContent);
		} catch (Exception e) {
			LogUtil.e("Response出现问题，服务器返回数据无法转换为json");
		}
		jsonData = obj;

		return obj;
	}

	public HttpResponse getHttpResponse() {
		return response;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public boolean isHttpOK() {
		return statusCode == HttpURLConnection.HTTP_OK;
	}

	public boolean isSuccessful() {
		return Constant.CODE_SUCCESSFUL.equals(getCode());
	}

	public String getResponseContent() {
		return responseContent;
	}

	public int getInt(String key) {
		if (jsonData == null) {
			return DEFAULT_INT_VALUE;
		}

		if (jsonData.has(key)) {
			try {
				return jsonData.getInt(key);
			} catch (JSONException e) {
				LogUtil.e(e.getMessage(), e);
			}
		}

		return DEFAULT_INT_VALUE;
	}

	public String getString(String key) {
		if (jsonData == null) {
			return null;
		}
		if (jsonData.has(key)) {
			try {
				return jsonData.getString(key);
			} catch (JSONException e) {
				LogUtil.e(e.getMessage(), e);
			}
		}

		return null;
	}

	public Double getDouble(String key) {
		if (jsonData == null) {
			return null;
		}
		if (jsonData.has(key)) {
			try {
				return jsonData.getDouble(key);
			} catch (JSONException e) {
				LogUtil.e(e.getMessage(), e);
			}
		}

		return null;
	}

	public JSONObject getJsonString(String key) {
		if (jsonData == null) {
			return null;
		}

		if (jsonData.has(key)) {
			try {
				return jsonData.getJSONObject(key);
			} catch (JSONException e) {
				LogUtil.e(e.getMessage(), e);
			}
		}

		return null;
	}

	public <T extends ListItem> List<T> getList(String key, T t) {
		return JSONUtil.getList(jsonData, key, t);
	}

	public ListItem getObject(String key, ListItem t) {
		if (jsonData == null) {
			return null;
		}

		if (jsonData.has(key)) {
			try {
				t.parseFromJson(jsonData.getJSONObject(key));

				return t;
			} catch (JSONException e) {
				LogUtil.e(e.getMessage(), e);
			}
		}

		return null;
	}

	public String getCode() {
		return getString("code");
	}

	public String getMsg() {
		return getString("msg");
	}
}
