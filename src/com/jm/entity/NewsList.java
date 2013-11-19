package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class NewsList implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7150128823898023546L;
	/**
	 * 
	 */

	private String content = "";

	private String image = "";

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@SuppressWarnings("unchecked")
	@Override
	public NewsList newObject() {
		// TODO Auto-generated method stub
		return new NewsList();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("content")) {
			setContent(json.getString("content"));
		}
		if (json.has("image")) {
			setImage(json.getString("image"));
		}
	}
}
