package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class Comment implements ListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	private String cpic = "";
	private String type = "";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCpic() {
		return cpic;
	}

	public void setCpic(String cpic) {
		this.cpic = cpic;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCtext() {
		return ctext;
	}

	public void setCtext(String ctext) {
		this.ctext = ctext;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	private String cid = "";
	private String ctext = "";
	private String ctime = "";

	private String uid = "";

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	private String cname = "";

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	@Override
	public Comment newObject() {
		// TODO Auto-generated method stub
		return new Comment();
	}

	@Override
	public void parseFromJson(JSONObject json) throws JSONException {
		if (json.has("head_photo")) {
			setCpic(json.getString("head_photo"));
		}
		if (json.has("id")) {
			setCid(json.getString("id"));
		}
		if (json.has("type")) {
			setType(json.getString("type"));
		}
		if (json.has("content")) {
			setCtext(json.getString("content"));
		}
		if (json.has("add_time")) {
			setCtime(json.getString("add_time"));
		}
		if (json.has("from_name")) {
			setCname(json.getString("from_name"));
		}
		if (json.has("from_uid")) {
			setUid(json.getString("from_uid"));
		}
	}
}
