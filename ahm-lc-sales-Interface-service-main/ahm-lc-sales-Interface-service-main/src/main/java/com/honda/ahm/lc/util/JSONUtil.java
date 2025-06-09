package com.honda.ahm.lc.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.honda.ahm.lc.messages.DataContainer;
import com.honda.ahm.lc.messages.ShippingMessage;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.messages.StatusVehicle;
import com.honda.ahm.lc.messages.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JSONUtil {
	protected static Logger logger = LoggerFactory.getLogger(JSONUtil.class);
	
	protected static Logger getLogger() {
		return logger;
	}

	
	public static DataContainer getDataContainerFromJSON( String jsonString){
		
		DataContainer dc =null;
		
		try{
			dc=new ObjectMapper().readValue(jsonString, DataContainer.class);
			
		}catch (Throwable ex){
			getLogger().error(ex.getMessage());
		}
		return dc;	
	}
	
	public static StatusMessage getStatusMessageFromJSON(String jsonString){
		
		StatusMessage dc = new StatusMessage();
		
		Gson gson;
		try{
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Vehicle.class, (JsonDeserializer<Vehicle>) (jsonElement, type, context) -> context.deserialize(jsonElement, StatusVehicle.class));
			gson = gsonBuilder.create();
			dc=gson.fromJson(jsonString, StatusMessage.class);
			//dc = new ObjectMapper().readValue(jsonString,StatusMessage.class);
		}
		catch (JsonParseException ex) {
			ex.printStackTrace();
			
		}catch (Throwable ex){
			getLogger().error(ex.getMessage());
			
		}
		return dc;	
	}
	
	
	public static String convertShippingMessageToJSON(ShippingMessage dc) {
		String jsonString=null;
		Gson gson;
		if (dc != null) {
			try{
					gson=new GsonBuilder()
					.setPrettyPrinting()
					.create();
				jsonString=gson.toJson(dc,ShippingMessage.class);
				
			}catch(Exception ex){
				getLogger().error(ex.getMessage());
			}
		}
		return jsonString;
	}
	
	public static Gson getGson() {
		return new GsonBuilder()
		.serializeNulls()
		.setPrettyPrinting()
		.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS z")
		
		.create();
	}
}
