package com.honda.galc.rest.json;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.honda.galc.data.DataContainer;
import com.honda.galc.rest.common.AbstractContentHandler;
import com.honda.galc.rest.util.RestUtils;

/**
 * @author Subu Kathiresan
 * @date Sep 12, 2013
 */
public class JsonContentHandler extends AbstractContentHandler {

	public JsonContentHandler() {}
	
	/**
	 * parse out parameters from the http request message body
	 * 
	 * @return
	 */
	public ArrayList<Object> getParametersFromRequestBody(HttpServletRequest httpRequest, String reqSignature) {
		ArrayList<Object> parameters = new ArrayList<Object>();
		try {
			JsonElement json = getGson().fromJson(httpRequest.getReader(), JsonElement.class);
			if (json != null) {
				getLogger().info("REST parameters received in JSON payload for "
						+ reqSignature
						+ " from " + getClientAddress() 
						+ ": " + getGson().toJson(json));
				getParamsFromJsonElement(json, parameters);
			} else {
				getLogger().info("No Parameters in JSON payload for "
						+ reqSignature
						+ " from " + getClientAddress());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Error retrieving parameters from Request body");
		}
		return parameters;
	}
	
	public String getResponse(String restRequestSignature, Object obj) {
		String responseString = getGson().toJson(obj);
		getLogger().info("REST response for " 
				+ restRequestSignature
				+ " sent to " + getClientAddress() 
				+ ": " + responseString);
		return responseString;
	}

	public void getParamsFromJsonElement(JsonElement json, ArrayList<Object> params) {
		if (json instanceof JsonObject) {
			getParamsFromJsonObject((JsonObject) json, params);
		} else if (json instanceof JsonArray) {
			Iterator<JsonElement> iter = ((JsonArray) json).iterator();
			while (iter.hasNext()) {
				getParamsFromJsonElement(iter.next(), params);
			}
		}
	}

	public void getParamsFromJsonObject(JsonObject json, ArrayList<Object> params) {
		for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
			Object param = getGson().fromJson(entry.getValue(), RestUtils.getParameterClass(entry.getKey()));
			RestUtils.addParam(param, params);
		}
	}
	
	/**
	 * returns a Gson with all required type adapters registered
	 * @return
	 */
	public static Gson getGson() {
		return new GsonBuilder()
		.serializeNulls()
		.setPrettyPrinting()
		.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS z")
		.registerTypeAdapter(DataContainer.class, new DataContainerJsonDeSerializer())
		.registerTypeAdapter(List.class, new ListJsonDeSerializer())
		.registerTypeAdapter(Timestamp.class, new JsonSerializer<Timestamp>() {
			public JsonElement serialize(Timestamp arg0, Type arg1,	JsonSerializationContext arg2) {
				SimpleDateFormat df = new SimpleDateFormat(" z");
		        String timeZone = df.format(arg0);
		        return new JsonPrimitive(arg0.toString().concat(timeZone));
			}
		}) 
		.create();
	}
}
