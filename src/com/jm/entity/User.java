package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class User implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String uid = "";
	private String username = "";
	private String type = "";
	private String head_photo = "";
	private String store_name = "";
	private String store_address = "";

	public String getStore_address() {
		return store_address;
	}

	public void setStore_address(String store_address) {
		this.store_address = store_address;
	}

	private String city = "";
	private String address = "";

	private String works_num = "";

	public String getWorks_num() {
		return works_num;
	}

	public void setWorks_num(String works_num) {
		this.works_num = works_num;
	}

	private String distance = "";

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	private String price_info = "";
	private String price = "";

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice_info() {
		return price_info;
	}

	public void setPrice_info(String price_info) {
		this.price_info = price_info;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsconcerns() {
		return isconcerns;
	}

	public void setIsconcerns(String isconcerns) {
		this.isconcerns = isconcerns;
	}

	private String isconcerns = "";

	@SuppressWarnings("unchecked")
	@Override
	public User newObject() {
		// TODO Auto-generated method stub
		return new User();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("uid")) {
			setUid(json.getString("uid"));

		}
		if (json.has("username")) {
			setUsername(json.getString("username"));

		}
		if (json.has("type")) {
			setType(json.getString("type"));

		}
		if (json.has("head_photo")) {
			setHead_photo(json.getString("head_photo"));

		}
		if (json.has("store_name")) {
			setStore_name(json.getString("store_name"));

		}
		if (json.has("city")) {
			setCity(json.getString("city"));

		}
		if (json.has("address")) {
			setAddress(json.getString("address"));

		}
		if (json.has("isconcerns")) {
			setIsconcerns(json.getString("isconcerns"));
		}
		if (json.has("price_info")) {
			setPrice_info(json.getString("price_info"));
		}
		if (json.has("distance")) {
			setDistance(json.getString("distance"));
		}
		if (json.has("works_num")) {
			setWorks_num(json.getString("works_num"));
		}
		if (json.has("store_address")) {
			setStore_address(json.getString("store_address"));
		}
	}
}
