package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Reserve implements ListItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void parseFromJson(JSONObject json) throws JSONException {

		if (json.has("id"))
			setId(json.getString("id"));
		if (json.has("my_uid"))
			setMy_uid(json.getString("my_uid"));
		if (json.has("to_uid"))
			setTo_uid(json.getString("to_uid"));
		if (json.has("to_username"))
			setTo_username(json.getString("to_username"));
		if (json.has("store_name"))
			setStore_name(json.getString("store_name"));
		if (json.has("store_address"))
			setStore_address(json.getString("store_address"));
		if (json.has("order_number"))
			setOrder_number(json.getString("order_number"));
		if (json.has("reserve_time"))
			setReserve_time(json.getString("reserve_time"));
		if (json.has("reserve_hour"))
			setReserve_hour(json.getString("reserve_hour"));
		if (json.has("reserve_type"))
			setReserver_type(json.getString("reserve_type"));
		if (json.has("reserve_price"))
			setPrice(json.getString("reserve_price"));
		if (json.has("rebate"))
			setRebate(json.getString("rebate"));
		if (json.has("my_name"))
			setMy_name(json.getString("my_name"));
		if (json.has("my_tel"))
			setMy_tel(json.getString("my_tel"));
		if (json.has("status"))
			setStatus(json.getString("status"));
		if (json.has("head_photo"))
			setHead_photo(json.getString("head_photo"));

	}

	private String id;
	private String my_uid;
	private String to_uid;
	private String to_username;
	private String store_name;
	private String store_address;
	private String order_number;
	private String reserve_time;
	private String reserve_hour;
	private String reserver_type;
	private String head_photo;

	public String getHead_photo() {
		return head_photo;
	}

	public void setHead_photo(String head_photo) {
		this.head_photo = head_photo;
	}

	private String price;
	private String rebate;
	private String my_name;
	private String my_tel;
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMy_uid() {
		return my_uid;
	}

	public void setMy_uid(String my_uid) {
		this.my_uid = my_uid;
	}

	public String getTo_uid() {
		return to_uid;
	}

	public void setTo_uid(String to_uid) {
		this.to_uid = to_uid;
	}

	public String getTo_username() {
		return to_username;
	}

	public void setTo_username(String to_username) {
		this.to_username = to_username;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getStore_address() {
		return store_address;
	}

	public void setStore_address(String store_address) {
		this.store_address = store_address;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getReserve_time() {
		return reserve_time;
	}

	public void setReserve_time(String reserve_time) {
		this.reserve_time = reserve_time;
	}

	public String getReserve_hour() {
		return reserve_hour;
	}

	public void setReserve_hour(String reserve_hour) {
		this.reserve_hour = reserve_hour;
	}

	public String getReserver_type() {
		return reserver_type;
	}

	public void setReserver_type(String reserver_type) {
		this.reserver_type = reserver_type;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRebate() {
		return rebate;
	}

	public void setRebate(String rebate) {
		this.rebate = rebate;
	}

	public String getMy_name() {
		return my_name;
	}

	public void setMy_name(String my_name) {
		this.my_name = my_name;
	}

	public String getMy_tel() {
		return my_tel;
	}

	public void setMy_tel(String my_tel) {
		this.my_tel = my_tel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Reserve newObject() {
		// TODO Auto-generated method stub
		return new Reserve();
	}
}
