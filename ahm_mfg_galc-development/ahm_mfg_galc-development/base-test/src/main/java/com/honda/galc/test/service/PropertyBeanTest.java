package com.honda.galc.test.service;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.property.PropertyService;

import static com.honda.galc.service.ServiceFactory.getDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
/**
 * 
 * 
 * <h3>PropertyBeanTest Class description</h3>
 * <p> PropertyBeanTest description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Oct 10, 2014
 *
 *
 */
public class PropertyBeanTest {
	
	private static final String TEST_COMPONENT_ID ="PROPRERTY_BEAN_TEST";
	
	@Before
	public void loadConfig() {
		
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
		HttpServiceProvider.setUrl("http://192.168.7.209/BaseWeb/HttpServiceHandler");
		
		loadTestProperties();
	}
	
	@Test
	public void testPrimitiveProperty() {
		TestPropertyBean bean = PropertyService.getPropertyBean(TestPropertyBean.class, TEST_COMPONENT_ID);
		assertEquals(bean.getStringProperty(), "Test String");
		assertEquals(bean.getIntProperty(), 10);
		assertEquals(bean.getBooleanProperty(), false);
		assertEquals(bean.getDoubleProperty(), 5.58,0.001);
		
	}
	
	@Test
	public void testObjectProperty() {
		TestPropertyBean bean = PropertyService.getPropertyBean(TestPropertyBean.class, TEST_COMPONENT_ID);
		assertEquals(bean.getIntegerProperty(), new Integer(888));
		assertEquals(bean.getFloatObjectProperty(), new Float(11.11));
		assertEquals(bean.getDoubleObjectProperty(), new Double(15.56));
		assertEquals(bean.getColorProperty(), new Color(100,120,235));
		
	}
	
	@Test
	public void testArrayProperty() {
		TestPropertyBean bean = PropertyService.getPropertyBean(TestPropertyBean.class, TEST_COMPONENT_ID);
		assertArrayEquals(bean.getStringArray(), new String[] {"String 1", "String 2"});
		assertArrayEquals(bean.getIntArray(), new int[] {10,12,13});
		assertArrayEquals(bean.getDoubleArray(), new double[] {5.58,6.35},0.001);
		
	}
	
	@Test
	public void testObjectArrayProperty() {
		TestPropertyBean bean = PropertyService.getPropertyBean(TestPropertyBean.class, TEST_COMPONENT_ID);
		assertArrayEquals(bean.getIntegerObjectArray(Integer.class), new Integer[] {11,53});
		assertArrayEquals(bean.getFloatObjectArray(Float.class), new Float[]{8.24f,6.35f});
		assertArrayEquals(bean.getDoubleObjectArray(Double.class), new Double[] {3.56,9.99});
	}
	
	@Test
	public void testMapProperty() {
		TestPropertyBean bean = PropertyService.getPropertyBean(TestPropertyBean.class, TEST_COMPONENT_ID);
		
		assertEquals(bean.getStringMap(), getExpectedStringMap());
		assertEquals(bean.getColorMap(Color.class), getExpectedColorMap());
		
		Map<String,Integer[]> intArrayMap = bean.getIntArrayMap(Integer[].class);
		assertArrayEquals(intArrayMap.get("R1J"),new Integer[]{5,4,7,8,9,6});
		assertArrayEquals(intArrayMap.get("R2J"),new Integer[]{7,8,6,5,9,11});
		
		Map<String,String[]> stringArrayMap = bean.getStringArrayMap(String[].class);
		assertArrayEquals(stringArrayMap.get("R1J"),new String[]{"cat","dog","fish"});
		assertArrayEquals(stringArrayMap.get("R2J"),new String[]{"apple","grape","banana"});
		
	}
	
	private void loadTestProperties() {
		getDao(ComponentPropertyDao.class).removeAllByComponentId(TEST_COMPONENT_ID);
		getDao(ComponentPropertyDao.class).saveAll(createTestProperties());
	}
	
	private List<ComponentProperty> createTestProperties(){
		List<ComponentProperty> properties = new ArrayList<ComponentProperty>();
		
		//primitive properties
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_STRING","Test String"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_INT","10"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_BOOLEAN","FALSE"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_FLOAT","3.51"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_DOUBLE","5.58"));
		
		// object properties
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_INTEGER","888"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_FLOAT_OBJ","11.11"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_DOUBLE_OBJ","15.56"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_COLOR","100,120,235"));
		
		// primitive array properties
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_STRING_ARRAY","String 1, String 2"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_INT_ARRAY","10,12,13"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_DOUBLE_ARRAY","5.58,6.35"));
		
		// object array properties
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_INTEGER_OBJ_ARRAY","11,53"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_FLOAT_OBJ_ARRAY","8.24,6.35"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_DOUBLE_OBJ_ARRAY","3.56,9.99"));
		
		
		// Object Map properties
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_STRING_MAP{Key 1}","String 1"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_STRING_MAP{Key 2}","String 2"));
		
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_COLOR_MAP{Color 1}","100,100,99"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_COLOR_MAP{Color 2}","200,101,85"));
		
		// Object array map properties
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_INTEGER_ARRAY_MAP{R1J}","5,4,7,8,9,6"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_INTEGER_ARRAY_MAP{R2J}","7,8,6,5,9,11"));
		
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_STRING_ARRAY_MAP{R1J}","cat,dog,fish"));
		properties.add(new ComponentProperty(TEST_COMPONENT_ID,"PROP_STRING_ARRAY_MAP{R2J}","apple,grape,banana"));
		
		
		return properties;
	}
	
	private Map<String,String> getExpectedStringMap(){
		Map<String,String> stringMap = new HashMap<String,String>() ;
		stringMap.put("Key 1", "String 1");
		stringMap.put("Key 2", "String 2");
		return stringMap;
	}
	
	private Map<String,Color> getExpectedColorMap(){
		Map<String,Color> colorMap = new HashMap<String,Color>() ;
		colorMap.put("Color 1", new Color(100,100,99));
		colorMap.put("Color 2", new Color(200,101,85));
		return colorMap;
	}
	

}
