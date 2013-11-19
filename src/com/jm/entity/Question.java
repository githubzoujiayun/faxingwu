package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Question implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id = "";
	public String type = "";
	public String title = "";
	public String content = "";
	public String pic = "";
	public String add_time = "";

	@Override
	public Question newObject() {
		// TODO Auto-generated method stub
		return new Question();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("id")) {
			this.id = json.getString("id");
		}
		if (json.has("type")) {
			this.type = json.getString("type");
		}
		if (json.has("title")) {
			this.title = json.getString("title");
		}
		if (json.has("content")) {
			this.content = json.getString("content");
		}
		if (json.has("pic")) {
			this.pic = json.getString("pic");
		}
		if (json.has("add_time")) {
			this.add_time = json.getString("add_time");
		}
	}
}
