package com.jm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jm.connection.ListItem;

public class QuestionChat implements ListItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void parseFromJson(JSONObject json) throws JSONException {

		if (json.has("from_id")) {

			setSender(json.getString("from_id"));
		}
		if (json.has("from_name")) {

			setSenderName(json.getString("from_name"));
		}
		if (json.has("content")) {

			setMessage(json.getString("content"));
		}
		if (json.has("add_time")) {

			setDatetime(json.getString("add_time"));
		}
		if (json.has("id")) {

			setId(json.getString("id"));
		}
		if (json.has("mypic")) {

			setMypic(json.getString("mypic"));
		}
		if (json.has("type")) {

			setType(json.getString("type"));
		}
		if (json.has("pic")) {

			setPic(json.getString("pic"));
		}
	}

	private String senderid = "";
	private String sendername = "";
	private String message = "";
	private String datetime = "";
	private String id = "0";
	private String type = "";

	private String pic = "";

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String mypic;

	public String getMypic() {
		return mypic;
	}

	public void setMypic(String mypic) {
		this.mypic = mypic;
	}

	public String getSenderName() {
		return sendername;
	}

	public void setSenderName(String chatid) {
		this.sendername = chatid;
	}

	public String getSenderpic() {
		return senderpic;
	}

	public void setSenderpic(String senderpic) {
		this.senderpic = senderpic;
	}

	private String senderpic;

	public String getSender() {
		return senderid;
	}

	public void setSender(String sender) {
		this.senderid = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public QuestionChat newObject() {
		// TODO Auto-generated method stub
		return new QuestionChat();
	}
}
