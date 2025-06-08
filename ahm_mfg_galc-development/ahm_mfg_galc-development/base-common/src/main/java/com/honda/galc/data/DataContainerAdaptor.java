package com.honda.galc.data;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.honda.galc.data.DataContainer;

public class DataContainerAdaptor implements JsonSerializer<DataContainer>, JsonDeserializer<DataContainer>{
	
	public JsonElement serialize(DataContainer dc, Type typeOfDataContainer, JsonSerializationContext context) throws JsonParseException {
		DataContainer aDC=new DefaultDataContainer();
		Iterator iterator=dc.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			Object val=mapEntry.getValue();
			if(val instanceof String || val instanceof java.lang.Number
					|| val instanceof java.lang.Boolean){
				aDC.put(mapEntry.getKey().toString(),mapEntry.getValue().toString());
			}
			else{
				aDC.put(mapEntry.getKey().toString(),mapEntry.getValue());
			}
		}
		
		return context.serialize(aDC,Object.class);
	}
	
	public DataContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		 return handleObject(json.getAsJsonObject(), context);
	  }
	
	private DataContainer handleObject(JsonObject json, JsonDeserializationContext context) {
		 DataContainer dc=new DefaultDataContainer();
		   for(Map.Entry<String, JsonElement> entry : json.entrySet()){
			   dc.put(entry.getKey(), context.deserialize(entry.getValue(), Object.class));
		   }
	    return dc;
	}
	
}
