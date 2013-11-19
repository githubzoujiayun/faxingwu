package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Portfolio implements ListItem {

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

	private String id = "";

	@SuppressWarnings("unchecked")
	@Override
	public Portfolio newObject() {
		// TODO Auto-generated method stub
		return new Portfolio();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("work_id")) {
			setId(json.getString("work_id"));
		}
		if (json.has("work_image")) {
			setPic(json.getString("work_image"));
		}
	}
}
