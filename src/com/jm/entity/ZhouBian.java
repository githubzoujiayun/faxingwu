package com.jm.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;
import com.jm.util.JSONUtil;
import com.jm.util.LogUtil;

public class ZhouBian implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String uid = "";
	public String username = "";
	public String type = "";
	public String head_photo = "";
	public String city = "";
	public String signature = "";
	public String problem_num = "";
	public String distance = "";
	public String assess_num = "";
	public String works_num = "";
	public String store_name = "";
	public String status = "";
	public String store_address = "";
	public List<Hair> works_list = new ArrayList<Hair>();

	@SuppressWarnings("unchecked")
	@Override
	public ZhouBian newObject() {
		// TODO Auto-generated method stub
		return new ZhouBian();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {

		if (json.has("uid")) {
			this.uid = json.getString("uid");
		}

		if (json.has("username")) {
			this.username = json.getString("username");
		}
		if (json.has("type")) {
			this.type = json.getString("type");
		}
		if (json.has("head_photo")) {
			this.head_photo = json.getString("head_photo");
		}
		if (json.has("city")) {
			this.city = json.getString("city");
		}
		if (json.has("signature")) {
			this.signature = json.getString("signature");
		}
		if (json.has("problem_num")) {
			this.problem_num = json.getString("problem_num");
		}
		if (json.has("distance")) {
			this.distance = json.getString("distance");
		}
		if (json.has("assess_num")) {
			this.assess_num = json.getString("assess_num");
		}
		if (json.has("works_num")) {
			this.works_num = json.getString("works_num");
		}
		if (json.has("store_name")) {
			this.store_name = json.getString("store_name");
		}
		if (json.has("store_address")) {
			this.store_address = json.getString("store_address");
		}
		if (json.has("works_list")) {
			this.works_list = JSONUtil.getList(json, "works_list", new Hair());
		}
		if (json.has("status")) {
			this.status = json.getString("status");
		}

	}
}
