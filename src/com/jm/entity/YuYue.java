package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class YuYue implements ListItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void parseFromJson(JSONObject json) throws JSONException {

	}

	public String time = "";
	public String time1 = "";
	public String time2 = "";
	public String time3 = "";
	private String price = "";
	private String discount = "";

	public YuYue(String s1, String s2, String s3) {
		this.time1 = s1;
		this.time2 = s2;
		this.time3 = s3;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	@Override
	public <T extends ListItem> T newObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
