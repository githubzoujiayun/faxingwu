package com.jm.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class JSONUtil {
	@SuppressWarnings("unchecked")
	public static <T extends ListItem> List<T> getList(JSONObject jsonData,
			String key, T t) {
		if (jsonData == null) {
			return null;
		}
		if (jsonData.equals("")) {
			return null;
		}
		if (!jsonData.has(key)) {
			return null;
		}

		try {
			JSONArray jsArr = jsonData.getJSONArray(key);
			if (jsArr == null || jsArr.length() == 0) {
				return null;
			}

			List<T> res = new ArrayList<T>();

			T nt = t;
			for (int i = 0; i < jsArr.length(); i++) {
				if (nt == null) {
					nt = (T) t.newObject();
				}
				nt.parseFromJson(jsArr.getJSONObject(i));

				res.add(nt);

				nt = null;
			}
			return res;
		} catch (JSONException e) {
			LogUtil.e(e.getMessage(), e);
		}

		return null;
	}

}
