package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Msg implements ListItem {

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String uid = "";
	private String user_pic = "";
	private String user_name = "";
	private String content = "";
	private String time = "";
	private int id;

	@Override
	public Msg newObject() {
		// TODO Auto-generated method stub
		return new Msg();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("ta_id")) {
			setUid(json.getString("ta_id"));
		}
		if (json.has("head_photo")) {
			setUser_pic(json.getString("head_photo"));
		}
		if (json.has("ta_name")) {
			setUser_name(json.getString("ta_name"));
		}
		if (json.has("content")) {
			setContent(json.getString("content"));
		}
		if (json.has("add_time")) {
			setTime(json.getString("add_time"));
		}

	}
}
