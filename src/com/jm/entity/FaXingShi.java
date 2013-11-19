package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class FaXingShi implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id = "";
	public String username = "";
	public String type = "";
	public String head_photo = "";
	public String address = "";

	public String store_address = "";

	public String getSotre_address() {
		return store_address;
	}

	public void setSotre_address(String sotre_address) {
		this.store_address = sotre_address;
	}

	public String states = "";
	public String distance = "";
	public String signature = "";

	public String works_num = "";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	public FaXingShi newObject() {
		// TODO Auto-generated method stub
		return new FaXingShi();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("uid")) {
			this.id = json.getString("uid");
		}
		if (json.has("name")) {
			setUsername(json.getString("name"));
		}
		if (json.has("type")) {
			this.type = json.getString("type");
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
		if (json.has("distance")) {
			this.distance = json.getString("distance");
		}
		if (json.has("status")) {
			this.states = json.getString("status");
		}
		if (json.has("store_address")) {
			this.store_address = json.getString("store_address");
		}

	}
}
