package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class WillDo implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String w_id;
	public String uid;
	public String username;
	public String head_photo;
	public String long_service;
	public String price;
	public String rebate;
	public String distance;
	public String store_address;
	public String status;
	public String reserve_price;
	public String work_id;

	@Override
	public WillDo newObject() {
		// TODO Auto-generated method stub
		return new WillDo();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("w_id")) {
			this.w_id = json.getString("w_id");
		}

		if (json.has("uid")) {
			this.uid = json.getString("uid");
		}
		if (json.has("username")) {
			this.username = json.getString("username");
		}
		if (json.has("head_photo")) {
			this.head_photo = json.getString("head_photo");
		}
		if (json.has("long_service")) {
			this.long_service = json.getString("long_service");
		}
		if (json.has("price")) {
			this.price = json.getString("price");
		}
		if (json.has("rebate")) {
			this.rebate = json.getString("rebate");
		}
		if (json.has("distance")) {
			this.distance = json.getString("distance");
		}
		if (json.has("store_address")) {
			this.store_address = json.getString("store_address");
		}
		if (json.has("status")) {
			this.status = json.getString("status");
		}
		if (json.has("reserve_price")) {
			this.reserve_price = json.getString("reserve_price");
		}
		if (json.has("work_id")) {
			this.work_id = json.getString("work_id");
		}

	}
}
