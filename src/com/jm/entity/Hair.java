package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.jm.connection.ListItem;

public class Hair implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DatabaseField
	private String pic = "";

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@DatabaseField
	private int id;

	@DatabaseField
	private String type;
	public String whosHair;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Hair newObject() {
		// TODO Auto-generated method stub
		return new Hair();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("work_id")) {
			setId(Integer.parseInt(json.getString("work_id")));
		}
		if (json.has("work_image")) {
			setPic(json.getString("work_image"));
		}
	}
}
