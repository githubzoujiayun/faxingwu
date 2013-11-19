package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class ZhouBian implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id = "";
	public String username = "";
	public String type = "";
	public String head_photo = "";
	public String address = "";

	public String signature = "";

	public String works_num = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHead_photo() {
		return head_photo;
	}

	public void setHead_photo(String head_photo) {
		this.head_photo = head_photo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String isconcerns = "";

	@SuppressWarnings("unchecked")
	@Override
	public ZhouBian newObject() {
		// TODO Auto-generated method stub
		return new ZhouBian();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("id")) {
			setId(json.getString("id"));

		}
		if (json.has("name")) {
			setUsername(json.getString("name"));

		}

		if (json.has("type")) {
			setType(json.getString("type"));
		}
		if (json.has("head_photo")) {
			setHead_photo(json.getString("head_photo"));

		}
		if (json.has("address")) {
			setAddress(json.getString("address"));
		}

		if (json.has("signature")) {
			this.signature = json.getString("signature");
		}
		if (json.has("works_num")) {
			this.works_num = json.getString("works_num");
		}
		if (json.has("signature")) {
			this.signature = json.getString("signature");
		}
		if (json.has("isconcerns")) {
			this.isconcerns = json.getString("isconcerns");
		}
	}
}
