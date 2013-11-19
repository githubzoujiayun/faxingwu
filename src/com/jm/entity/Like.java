package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Like implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7150128823898023546L;
	/**
	 * 
	 */
	private String pic = "";

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String type = "";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String id = "";

	@SuppressWarnings("unchecked")
	@Override
	public Like newObject() {
		// TODO Auto-generated method stub
		return new Like();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("uid")) {
			setId(json.getString("uid"));
		}
		if (json.has("type")) {
			setType(json.getString("type"));
		}
		if (json.has("head_photo")) {
			setPic(json.getString("head_photo"));
		}
	}
}
