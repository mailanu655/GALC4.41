package com.honda.galc.rest.json;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.util.Primitive;

/**
 * @author Subu Kathiresan
 * @date Nov 06, 2018
 */
public class JsonContentHandlerTest {

	private JsonContentHandler jsonContentHandler = null;
	private Gson gson = null;

	@Before
	public void methodSetup() {
		gson = JsonContentHandler.getGson();
		jsonContentHandler = new JsonContentHandler();
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 06, 2018
	 * 
	 * Test deserialization of DefaultDataContainer
	 * single parameter - DefaultDataContainer
	 */
	@Test
	public void getParamsFromJsonElement_defaultDataContainer() {
		
		DataContainer dc = new DefaultDataContainer();
		dc.put("Ride Height Left Front.MEASUREMENT_VALUE", "1");
		dc.put("Ride Height Right Front.PART_SERIAL_NUMBER", "1");
		
		ArrayList<Object> params = new ArrayList<Object>();
		
		String jsonString = "{\"com.honda.galc.data.DefaultDataContainer\": {" + 
							"      \"Ride Height Left Front.MEASUREMENT_VALUE\":\"1\"," + 
							"      \"Ride Height Right Front.PART_SERIAL_NUMBER\":\"1\"}}";
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		jsonContentHandler.getParamsFromJsonElement(jsonElement, params);
		
		assertEquals(dc, params.get(0));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 06, 2018
	 * 
	 * Test deserialization of DataContainer
	 * single parameter - DataContainer
	 */
	@Test
	public void getParamsFromJsonElement_dataContainer() {
		
		DataContainer dc = new DefaultDataContainer();
		dc.put("Ride Height Left Front.MEASUREMENT_VALUE", "1");
		dc.put("Ride Height Right Front.PART_SERIAL_NUMBER", "1");
		
		ArrayList<Object> params = new ArrayList<Object>();
		
		String jsonString = "{\"com.honda.galc.data.DataContainer\": {" + 
							"      \"Ride Height Left Front.MEASUREMENT_VALUE\":\"1\"," + 
							"      \"Ride Height Right Front.PART_SERIAL_NUMBER\":\"1\"}}";
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		jsonContentHandler.getParamsFromJsonElement(jsonElement, params);
		
		assertEquals(dc, params.get(0));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 06, 2018
	 * 
	 * Test deserialization of DataContainer
	 * multiple parameters - String, DataContainer
	 */
	@Test
	public void getParamsFromJsonElement_stringDataContainer() {
		
		DataContainer dc = new DefaultDataContainer();
		dc.put("Ride Height Left Front.MEASUREMENT_VALUE", "1");
		dc.put("Ride Height Right Front.PART_SERIAL_NUMBER", "1");
		
		ArrayList<Object> params = new ArrayList<Object>();
		
		String jsonString = "[{\"java.lang.String\": \"hello\"}," + 
							" {\"com.honda.galc.data.DataContainer\": {" + 
							"      \"Ride Height Left Front.MEASUREMENT_VALUE\":\"1\"," + 
							"      \"Ride Height Right Front.PART_SERIAL_NUMBER\":\"1\"}}]";
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		jsonContentHandler.getParamsFromJsonElement(jsonElement, params);
		
		assertEquals("hello", params.get(0));
		assertEquals(dc, params.get(1));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 06, 2018
	 * 
	 * Test deserialization of DataContainer
	 * multiple parameters - String, DataContainer & int
	 */
	@Test
	public void getParamsFromJsonElement_stringDataContainerInt() {
		
		DataContainer dc = new DefaultDataContainer();
		dc.put("Ride Height Left Front.MEASUREMENT_VALUE", "1");
		dc.put("Ride Height Right Front.PART_SERIAL_NUMBER", "1");
		
		Primitive primitiveInt = new Primitive(9);
		ArrayList<Object> params = new ArrayList<Object>();
		
		String jsonString = "[{\"java.lang.String\": \"hello there\"}," +
							" {\"com.honda.galc.data.DataContainer\": {" + 
							"		\"Ride Height Left Front.MEASUREMENT_VALUE\":\"1\"," + 
							"		\"Ride Height Right Front.PART_SERIAL_NUMBER\":\"1\"}}," +
							" {\"com.honda.galc.rest.util.PrimitiveInt\":{ \"val\":9 }}]";
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		jsonContentHandler.getParamsFromJsonElement(jsonElement, params);
		
		assertEquals("hello there", params.get(0));
		assertEquals(dc, params.get(1));
		assertEquals(primitiveInt.getValue(), ((Primitive)params.get(2)).getValue());
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 06, 2018
	 * 
	 * Test deserialization of DataContainer
	 * multiple parameters - String, DataContainer & boolean
	 */
	@Test
	public void getParamsFromJsonElement_stringDataContainerBoolean() {
		
		DataContainer dc = new DefaultDataContainer();
		dc.put("Ride Height Left Front.MEASUREMENT_VALUE", "1");
		dc.put("Ride Height Right Front.PART_SERIAL_NUMBER", "1");
		
		Primitive primitiveBoolean = new Primitive(true);
		ArrayList<Object> params = new ArrayList<Object>();
		
		String jsonString = "[{\"java.lang.String\": \"hello there\"}," +
							" {\"com.honda.galc.data.DataContainer\": {" + 
							"		\"Ride Height Left Front.MEASUREMENT_VALUE\":\"1\"," + 
							"		\"Ride Height Right Front.PART_SERIAL_NUMBER\":\"1\"}}," +
							" {\"com.honda.galc.rest.util.PrimitiveBoolean\":{ \"val\":true }}]";
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		jsonContentHandler.getParamsFromJsonElement(jsonElement, params);
		
		assertEquals("hello there", params.get(0));
		assertEquals(dc, params.get(1));
		assertEquals(primitiveBoolean.getValue(), ((Primitive)params.get(2)).getValue());
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 06, 2018
	 * 
	 * Tests deserialization of List
	 * multiple params - String, List, int
	 */
	@Test
	public void getParamsFromJsonElement_stringListInt() {
		List<String> list = new ArrayList<String>();
		list.add("SENSOR ASSY");
		list.add("PILLAR B LS");
		
		Primitive primitiveInt = new Primitive(10);
		ArrayList<Object> params = new ArrayList<Object>();
		
		String jsonString = "[{\"java.lang.String\": \"hello again\"}," + 
							" {\"java.util.List\":[" + 
							"	{ \"java.lang.String\" : \"SENSOR ASSY\" }," + 
							"	{ \"java.lang.String\" : \"PILLAR B LS\" }]}," + 
							" {\"com.honda.galc.rest.util.PrimitiveInt\":{ \"val\":10 }}]";
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		jsonContentHandler.getParamsFromJsonElement(jsonElement, params);
		
		assertEquals("hello again", params.get(0));
		assertEquals(list, params.get(1));
		assertEquals(primitiveInt.getValue(), ((Primitive)params.get(2)).getValue());
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 06, 2018
	 * 
	 * Tests deserialization of Timestamp
	 * invalid date format - slashes instead of hyphen
	 */
	@Test(expected=JsonSyntaxException.class)
	public void getParamsFromJsonElement_invalidTimestamp() {

		ArrayList<Object> params = new ArrayList<Object>();
		String jsonString = "[{\"java.sql.Timestamp\": \"2018/11/06 10:42:52.620 EST\"}]";
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		jsonContentHandler.getParamsFromJsonElement(jsonElement, params);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 06, 2018
	 * 
	 * Tests deserialization of Timestamp
	 * invalid date format - no time zone
	 */
	@Test(expected=JsonSyntaxException.class)
	public void getParamsFromJsonElement_invalidTimestampNoTimeZone() {

		ArrayList<Object> params = new ArrayList<Object>();
		String jsonString = "[{\"java.sql.Timestamp\": \"2018/11/06 10:42:52.620\"}]";
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		jsonContentHandler.getParamsFromJsonElement(jsonElement, params);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 06, 2018
	 * 
	 * Tests deserialization of Timestamp
	 * multiple params - Timestamp, String
	 */
	@Test
	public void getParamsFromJsonElement_timestampString() {
		
		Timestamp timestamp = new Timestamp(1541518972620L);
		ArrayList<Object> params = new ArrayList<Object>();
		
		String jsonString = "[{\"java.sql.Timestamp\": \"2018-11-06 10:42:52.620 EST\"}," + 
							" {\"java.lang.String\": \"hello there again\"}]";
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		jsonContentHandler.getParamsFromJsonElement(jsonElement, params);
		
		assertEquals(timestamp, params.get(0));
		assertEquals("hello there again", params.get(1));
	}
}
	