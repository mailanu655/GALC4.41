package com.honda.galc.rest.test;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.honda.galc.entity.product.MapFeatureLocation;
import com.honda.galc.rest.util.PrimitiveDouble;
import com.honda.galc.rest.util.PrimitiveInt;
import com.honda.galc.util.Primitive;
import com.honda.galc.util.ReflectionDummy;
import com.honda.galc.util.ReflectionUtils;

/**
 * @author Subu Kathiresan
 * @date May 3, 2013
 */
public class JsonConverterTest {

	static Gson gson = null;
	static GsonBuilder gsonBuilder = null;
	static JsonParser parser = null;
	
	public JsonConverterTest() {}
	
	@BeforeClass
	public static void beforeClass() {
		gson = new Gson();
		gsonBuilder = new GsonBuilder();
	    parser = new JsonParser();
	}
	
	@Test
	public void convertJsonToObjectTest() {
		String jsonString = "{\"id\":{\"featureId\": \"19XFB5F58CE000005\",\"featureType\": \"1\",\"featureLayer\": \"1\"},\"latitude\": 39.36704,\"longitude\": -85.54423,\"createTimestamp\": \"Apr 5, 2013 7:44:11 PM\",\"updateTimestamp\": \"Apr 5, 2013 8:11:05 PM\"}";
		MapFeatureLocation featureLocation = gson.fromJson(jsonString, MapFeatureLocation.class);
		assertEquals(featureLocation.getId().getFeatureId(), "19XFB5F58CE000005");
	}
	
	@Test
	public void convertObjectToJsonTest() {
	    Collection<Serializable> collection = new ArrayList<Serializable>();
	    collection.add("hello");
	    collection.add(5);
	    collection.add(new MapFeatureLocation("19XFB5F58CE000005", "1", "1"));
	    String json = gson.toJson(collection);
	    JsonArray array = parser.parse(json).getAsJsonArray();
	    
	    String message = gson.fromJson(array.get(0), String.class);
	    int number = gson.fromJson(array.get(1), int.class);
	    MapFeatureLocation location = gson.fromJson(array.get(2), MapFeatureLocation.class);

	    assertEquals(message, "hello");
	  	assertEquals(number, 5);
	  	assertEquals(location.getId().getFeatureId(), "19XFB5F58CE000005");
	}
	
	@Test
	public void invokeMethodWithPrimitivesTest1() {
		String testJson = "{\"java.lang.String\":\"hello\", \"com.honda.galc.rest.util.PrimitiveInt\":{\"val\":1}}";
		JsonObject jsonObj = parser.parse(testJson).getAsJsonObject();
	    ArrayList<Object> parameters = new ArrayList<Object>();
	    
    	for(Map.Entry<String, JsonElement> entry: jsonObj.entrySet()) {
    		Object param = gson.fromJson(entry.getValue(), getParameterClass(entry.getKey()));
	    	if (param instanceof PrimitiveInt)
	    		parameters.add(new Primitive(((PrimitiveInt) param).getVal()));
	    	else
	    		parameters.add(param);
    	}
    	
	    ReflectionDummy dummy = new ReflectionDummy();
		ReflectionUtils.invoke(dummy, "testMethod", parameters.toArray());
		assertEquals(dummy.strVal, "invoked method with parameters String, int");
	    assertEquals(dummy.intVal, 100);
	}
	
	@Test
	public void invokeMethodWithPrimitivesTest2() {
		String testJson = "{\"java.lang.String\":\"hello\", \"com.honda.galc.rest.util.PrimitiveDouble\":{\"val\":1.0}}";
		JsonObject jsonObj = parser.parse(testJson).getAsJsonObject();
	    ArrayList<Object> parameters = new ArrayList<Object>();

    	for(Map.Entry<String, JsonElement> entry: jsonObj.entrySet()) {
    		Object param = gson.fromJson(entry.getValue(), getParameterClass(entry.getKey()));
	    	if (param instanceof PrimitiveDouble)
	    		parameters.add(new Primitive(((PrimitiveDouble) param).getVal()));
	    	else
	    		parameters.add(param);
    	}
	    
	    ReflectionDummy dummy = new ReflectionDummy();
		ReflectionUtils.invoke(dummy, "testMethod", parameters.toArray());
		assertEquals(dummy.strVal, "invoked method with parameters String, double");
	}
	
	@Test
	public void invokeMethodWithObjectsTest() {
		String testJson = "{\"java.lang.String\":\"hello\", \"java.lang.Integer\":1}";
		JsonObject jsonObj = parser.parse(testJson).getAsJsonObject();
	    ArrayList<Object> parameters = new ArrayList<Object>();

    	for(Map.Entry<String, JsonElement> entry: jsonObj.entrySet()) {
    		Object param = gson.fromJson(entry.getValue(), getParameterClass(entry.getKey()));
    		parameters.add(param);
    	}
	    
	    ReflectionDummy dummy = new ReflectionDummy();
	    ReflectionUtils.invoke(dummy, "testMethod", parameters.toArray());
	    assertEquals(dummy.strVal, "invoked method with parameters String, Integer");
	    assertEquals(dummy.intVal, 110);
	}
	
	@Test
	public void invokeMethodWithObjectsAndPrimitivesTest() {
		String testJson = "{\"com.honda.galc.entity.product.MbpnProduct\":{\"productId\": \"19XFB5F58CE000005\",\"currentOrderNo\": \"A1234B\",\"currentProductSpecCode\": \"XSIMBA5\"}, \"com.honda.galc.rest.util.PrimitiveInt\":{\"val\":1}}";
		JsonObject jsonObj = parser.parse(testJson).getAsJsonObject();
	    ArrayList<Object> parameters = new ArrayList<Object>();
	    
    	for(Map.Entry<String, JsonElement> entry: jsonObj.entrySet()) {
    		Object param = gson.fromJson(entry.getValue(), getParameterClass(entry.getKey()));
	    	if (param instanceof PrimitiveInt)
	    		parameters.add(new Primitive(((PrimitiveInt) param).getVal()));
	    	else
	    		parameters.add(param);
    	}
    	
	    ReflectionDummy dummy = new ReflectionDummy();
		ReflectionUtils.invoke(dummy, "testMethod", parameters.toArray());
		assertEquals(dummy.strVal, "invoked method with parameters MbpnProduct, int");
	}
	
	@Test
	public void invokeMethodWithListParameters() {
		String testJson = "	[ 																" + 
		                  "		{ 															" + 
		                  "			\"java.lang.String\":\"19XFA16539E000269\"      		" +
		                  "		},															" +					
		                  "		{  															" +	
		                  " 		\"java.util.List\":										" +
		                  "								[  									" +	
		                  "									\"SENSOR ASSY,PILLAR B LS\"    	" +
		                  "								]									" +
		                  "		}															" +
		                  "	]																";
		JsonArray jsonArray = parser.parse(testJson).getAsJsonArray();
	    ArrayList<Object> parameters = new ArrayList<Object>();
	    
    	for(JsonElement element: jsonArray) {
    	 	for(Map.Entry<String, JsonElement> entry: ((JsonObject)element).entrySet()) {
        		Object param = gson.fromJson(entry.getValue(), getParameterClass(entry.getKey()));
   	    		parameters.add(param);
        	}
    	}
    	
	    ReflectionDummy dummy = new ReflectionDummy();
		ReflectionUtils.invoke(dummy, "testMethod", parameters.toArray());
		assertEquals(dummy.strVal, "invoked method with parameters String, List<String>");
	}
	
	public Class<?> getParameterClass(String clazz) {
		Class<?> parameterClass = null;
		try {
			parameterClass = Class.forName(clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return parameterClass;
	}
}
