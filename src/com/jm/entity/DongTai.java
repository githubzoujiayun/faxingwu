package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.jm.connection.ListItem;

public class DongTai implements ListItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void parseFromJson(JSONObject json) throws JSONException {

		setAdd_time(json.getString("add_time"));
		setContent(json.getString("content"));
		setType(json.getString("type"));
		setUid(json.getString("uid"));
		setWork_id(json.getString("work_id"));
		setWork_image(json.getString("work_image"));
		setImage_count(json.getString("image_count"));
		setCollect_num(json.getString("collect_num"));
		setComment_num(json.getString("comment_num"));
	}

	@DatabaseField
	private String work_id = "";

	public String getWork_id() {
		return work_id;
	}

	public void setWork_id(String work_id) {
		this.work_id = work_id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWork_image() {
		return work_image;
	}

	public void setWork_image(String work_image) {
		this.work_image = work_image;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	@DatabaseField
	private String uid = "";
	@DatabaseField
	private String image_count = "";
	@DatabaseField
	private String collect_num = "";
	@DatabaseField
	private String comment_num = "";

	public String getImage_count() {
		return image_count;
	}

	public void setImage_count(String image_count) {
		this.image_count = image_count;
	}

	public String getCollect_num() {
		return collect_num;
	}

	public void setCollect_num(String collect_num) {
		this.collect_num = collect_num;
	}

	public String getComment_num() {
		return comment_num;
	}

	public void setComment_num(String comment_num) {
		this.comment_num = comment_num;
	}

	@DatabaseField
	private String type = "";
	@DatabaseField
	private String content = "";
	@DatabaseField
	private String work_image = "";
	@DatabaseField
	private String add_time = "";

	@Override
	public DongTai newObject() {
		// TODO Auto-generated method stub
		return new DongTai();
	}
}
