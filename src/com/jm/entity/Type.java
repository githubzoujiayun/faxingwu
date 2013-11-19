package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Type implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	private String name = "";
	private String id = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Type newObject() {
		return new Type();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("id")) {
			setId(json.getString("id"));
		}
		if (json.has("name")) {
//			try {
//				setName(new String(json.getString("name").getBytes("UTF-8"),
//						"GBK"));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			setName(json.getString("name"));
		}
	}
}
