package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class News implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String news_id = "";
	public String news_type = "";
	public String title = "";
	public String content = "";
	public String pic = "";
	public String add_time = "";

	@Override
	public News newObject() {
		// TODO Auto-generated method stub
		return new News();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("news_id")) {
			this.news_id = json.getString("news_id");
		}
		if (json.has("news_type")) {
			this.news_type = json.getString("news_type");
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
