package com.honda.galc.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.honda.galc.common.exception.DataConversionException;

public class DataContainerJSONUtil {
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS z";
	
	/**
	 * Read from the supplied input stream and convert JSON representation into
	 * a collapsed set of key value pairs and store them in the supplied 
	 * data container.
	 * 
	 * @param dc - DataContainer to store new key/values in
	 * @param jsonString - String in json format
	 * @return
	 * @throws GALCException
	 */
	public static DataContainer convertFromJSON(DataContainer dc, String jsonString){
		if (dc == null) {
			dc = new DefaultDataContainer();
		}
		Gson gson;
		try{
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setDateFormat(DATE_FORMAT);
			gsonBuilder.registerTypeAdapter(DataContainer.class, new DataContainerAdaptor());
			gson = gsonBuilder.create();
			dc=gson.fromJson(jsonString, DataContainer.class);
		}
		catch (JsonParseException ex) {
			ex.printStackTrace();
			throw new DataConversionException("Error Converting JSON to DataContainer: "+jsonString,ex);
		}catch (Throwable ex){
			ex.printStackTrace();
			throw new DataConversionException("Error Converting JSON to DataContainer: "+jsonString, ex);
		}
		return dc;	
	}
	
	/**
	 * Convert the DataContainer to a very simple JSON key/value object
	 * format.
	 * 
	 * @param dc        - The data container to process.
	 * @param asStrings - If true, object values will be written as JSON strings
	 *                    and not as numeric or boolean values
	 * @return
	 */	
	public static String convertToJSON(DataContainer dc) {
		return convertToJSON(dc,false);
	}
	/**
	 * Convert the DataContainer to a very simple JSON key/value object
	 * format. 
	 * 
	 * @param dc        - The data container to process.
	 * @param asStrings - If true, object values will be written as JSON strings
	 *                    and not as numeric or boolean values
	 * @return
	 */	
	public static String convertToJSON(DataContainer dc, boolean asString) {
		String jsonString=null;
		Gson gson;
		if (dc != null) {
			try{
				if(!asString){
					gson=new GsonBuilder()
						.setPrettyPrinting()
						.setDateFormat(DATE_FORMAT)
						.create();
					jsonString=gson.toJson(dc);
				}
				else{
					gson=new GsonBuilder()
					.setPrettyPrinting()
					.setDateFormat(DATE_FORMAT)
					.registerTypeAdapter(DataContainer.class, new DataContainerAdaptor())
					.create();
				jsonString=gson.toJson(dc,DataContainer.class);
				}
			}catch(Exception ex){
				ex.printStackTrace();
				throw new DataConversionException("Error Converting DataContainer to JSON: " + dc ,ex);
			}
		}
		return jsonString;
	}
	
	/**
	 * Convert the DataContainer to a very simple JSON key/value object
	 * format which is not very human-readable.
	 * 
	 * @param dc        - The data container to process.
	 * @param asStrings - If true, object values will be written as JSON strings
	 *                    and not as numeric or boolean values
	 * @return
	 */	
	public static String convertToRawJSON(DataContainer dc) {
		return convertToRawJSON(dc,false);
	}
	/**
	 * Convert the DataContainer to a very simple JSON key/value object
	 * format which is not very human-readable. 
	 * 
	 * @param dc        - The data container to process.
	 * @param asStrings - If true, object values will be written as JSON strings
	 *                    and not as numeric or boolean values
	 * @return
	 */	
	public static String convertToRawJSON(DataContainer dc, boolean asString) {
		String jsonString=null;
		Gson gson;
		if (dc != null) {
			try{
				if(!asString){
					gson=new GsonBuilder()
						.setDateFormat(DATE_FORMAT)
						.create();
					jsonString=gson.toJson(dc);
				}
				else{
					gson=new GsonBuilder()
					.setDateFormat(DATE_FORMAT)
					.registerTypeAdapter(DataContainer.class, new DataContainerAdaptor())
					.create();
				jsonString=gson.toJson(dc,DataContainer.class);
				}
			}catch(Exception ex){
				ex.printStackTrace();
				throw new DataConversionException("Error Converting DataContainer to JSON: " + dc ,ex);
			}
		}
		return jsonString;
	}
}
