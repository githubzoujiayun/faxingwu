package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Answer implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String pid;
	public String ta_id;
	public String ta_name;
	public String content;
	public String head_photo;
	public String add_time;

	@Override
	public Answer newObject() {
		// TODO Auto-generated method stub
		return new Answer();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("pid")) {
			this.pid = json.getString("pid");
		}
		if (json.has("ta_id")) {
			this.ta_id = json.getString("ta_id");
		}
		if (json.has("ta_name")) {
			this.ta_name = json.getString("ta_name");
		}
		if (json.has("content")) {
			this.content = json.getString("content");
		}
		if (json.has("head_photo")) {
			this.head_photo = json.getString("head_photo");
		}
		if (json.has("add_time")) {
			this.add_time = json.getString("add_time");
		}
	}
}
