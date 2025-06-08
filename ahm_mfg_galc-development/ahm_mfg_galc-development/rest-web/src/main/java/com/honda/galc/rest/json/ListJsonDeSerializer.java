package com.honda.galc.rest.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.honda.galc.rest.util.RestUtils;

/**
 * @author Subu Kathiresan
 * @date Feb 6, 2017
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ListJsonDeSerializer implements JsonDeserializer<List> {
	
	private enum ListObjectType {
		JsonPrimitive,
		JsonObject;
	}
	
	public List deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		ArrayList list = new ArrayList();
		if (json instanceof JsonArray) {
			Iterator<JsonElement> iter = ((JsonArray) json).iterator();
			while (iter.hasNext()) {
			    addItemToList(list, iter.next());
			}
		}
		return list;
	}

	private void addItemToList(ArrayList list, Object item) {
		ListObjectType type = ListObjectType.valueOf(item.getClass().getSimpleName());
		switch (type) {
		case JsonPrimitive:
			list.add(((JsonPrimitive) item).getAsString());
		    break;
		case JsonObject:
			for (Map.Entry<String, JsonElement> element : ((JsonObject) item).entrySet()) {
				list.add(JsonContentHandler.getGson().fromJson(element.getValue(), RestUtils.getParameterClass(element.getKey())));
			}
		    break;
		default:
		    break;
		}
	}
	
	public static <T> List<T> deserialize(JsonElement json, Class<T> type) throws JsonParseException {
		ArrayList list = new ArrayList();
		if (json instanceof JsonArray) {
			Iterator<JsonElement> iter = ((JsonArray) json).iterator();
			while (iter.hasNext()) {
				list.add(JsonContentHandler.getGson().fromJson((JsonObject) iter.next(), (Class<T>) type));
			}
		}
		return list;
	}
}
