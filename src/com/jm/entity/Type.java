package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Type implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	public int firstType;
	public int secondType;
	public int picResource;
	public String hairName;

	public Type(int firstType, int secondType, int picResource, String hairName) {
		this.firstType = firstType;
		this.secondType = secondType;
		this.picResource = picResource;
		this.hairName = hairName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Type newObject() {
		return null;
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {

	}
}
