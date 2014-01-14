package com.jm.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;
import com.jm.util.JSONUtil;

public class FaXingShi implements ListItem {

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
	public String price_info = "";
	public String price_xijianchui = "";
	public String price_tangfa = "";
	public String price_ranfa = "";
	public String price_huli = "";
	public List<Hair> works_list = new ArrayList<Hair>();

	@SuppressWarnings("unchecked")
	@Override
	public FaXingShi newObject() {
		// TODO Auto-generated method stub
		return new FaXingShi();
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
		if (json.has("works_list") && !"".equals(json.getString("works_list"))) {
			this.works_list = JSONUtil.getList(json, "works_list", new Hair());
		}
		if (json.has("status")) {
			this.status = json.getString("status");
		}
		if (json.has("price_info")) {
			this.price_info = json.getString("price_info");
			this.price_xijianchui = price_info.split("_")[0] + "("
					+ price_info.split("_")[4] + "уш)";
			this.price_tangfa = price_info.split("_")[1] + "("
					+ price_info.split("_")[5] + "уш)";
			this.price_ranfa = price_info.split("_")[2] + "("
					+ price_info.split("_")[6] + "уш)";
			this.price_huli = price_info.split("_")[3] + "("
					+ price_info.split("_")[7] + "уш)";
		}

	}
}
