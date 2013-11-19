package com.jm.connection;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public interface ListItem extends Serializable {
	
	<T extends ListItem> T newObject();
	
	void parseFromJson(JSONObject json) throws JSONException;
}
