package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Push implements ListItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void parseFromJson(JSONObject json) throws JSONException {

		if (json.has("content")) {

			setContent(json.getString("content"));
		}
		if (json.has("status")) {
			setStatus(json.getString("status"));
		}
	}

	private String content = "";

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String status = "";

	@Override
	public Push newObject() {
		// TODO Auto-generated method stub
		return new Push();
	}

}
