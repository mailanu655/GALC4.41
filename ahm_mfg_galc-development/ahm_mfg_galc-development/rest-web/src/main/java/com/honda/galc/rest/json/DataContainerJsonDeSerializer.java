package com.honda.galc.rest.json;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;

/**
 * @author Subu Kathiresan
 * @date Sep 9, 2013
 */
public class DataContainerJsonDeSerializer implements JsonDeserializer<DataContainer> {
	
	public DataContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		DataContainer dc = handleObject(json.getAsJsonObject(), context);
		return dc;
	}

	private DataContainer handleObject(JsonObject json, JsonDeserializationContext context) {
		DataContainer dc = new DefaultDataContainer();
		for (Map.Entry<String, JsonElement> entry : json.entrySet())
			dc.put(entry.getKey(), context.deserialize(entry.getValue(), Object.class));
		return dc;
	}
}
