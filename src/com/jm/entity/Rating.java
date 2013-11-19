package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Rating implements ListItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void parseFromJson(JSONObject json) throws JSONException {

		setId(json.getString("assess_uid"));
		setUid(json.getString("assess_uid"));
		setAssess_uid(json.getString("assess_uid"));
		setService(json.getString("service"));
		setPrice(json.getString("price"));
		setMilieu(json.getString("milieu"));
		setInfo(json.getString("info"));
		setAdd_time(json.getString("add_time"));
		setHead_photo(json.getString("head_photo"));
		setUsername(json.getString("username"));
	}

	private String id;

	private String uid;
	private String assess_uid;
	private String service;
	private String price;
	private String milieu;
	private String info;
	private String add_time;
	private String head_photo;
	private String username;

	public String getHead_photo() {
		return head_photo;
	}

	public void setHead_photo(String head_photo) {
		this.head_photo = head_photo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAssess_uid() {
		return assess_uid;
	}

	public void setAssess_uid(String assess_uid) {
		this.assess_uid = assess_uid;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMilieu() {
		return milieu;
	}

	public void setMilieu(String milieu) {
		this.milieu = milieu;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	@Override
	public Rating newObject() {
		// TODO Auto-generated method stub
		return new Rating();
	}
}
