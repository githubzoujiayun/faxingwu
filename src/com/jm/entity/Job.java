package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Job implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id = "";
	public String uid = "";

	public String type = "";
	public String title = "";
	public String job = "";
	public String head_photo = "";
	public String money = "";
	public String area = "";
	public String city = "";
	public String add_time = "";
	public String interviews = "";
	
	@Override
	public Job newObject() {
		// TODO Auto-generated method stub
		return new Job();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("id")) {
			this.id = json.getString("id");
		}
		if (json.has("uid")) {
			this.uid = json.getString("uid");
		}
		if (json.has("type")) {
			this.type = json.getString("type");
		}
		if (json.has("head_photo")) {
			this.head_photo = json.getString("head_photo");
		}
		if (json.has("title")) {
			this.title = json.getString("title");
		}
		if (json.has("job")) {
			this.job = json.getString("job");
		}
		if (json.has("area")) {
			this.area = json.getString("area");
		}
		if (json.has("money")) {
			this.money = json.getString("money");
		}
		if (json.has("city")) {
			this.city = json.getString("city");
		}
		if (json.has("add_time")) {
			this.add_time = json.getString("add_time");
		}
		if (json.has("interviews")) {
			this.interviews = json.getString("interviews");
		}
		
	}
}
