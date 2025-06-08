package com.honda.galc.test.service;

import java.awt.Color;
import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBeanAttribute;

public interface TestPropertyBean extends IProperty{
	
	//primitive properties
	@PropertyBeanAttribute(propertyKey ="PROP_STRING")
	public String getStringProperty();
	
	@PropertyBeanAttribute(propertyKey ="PROP_INT")
	public int getIntProperty();
		
	@PropertyBeanAttribute(propertyKey ="PROP_BOOLEAN")
	public boolean getBooleanProperty();
	
	@PropertyBeanAttribute(propertyKey ="PROP_DOUBLE")
	public double getDoubleProperty();
	
	// object properties
	@PropertyBeanAttribute(propertyKey ="PROP_INTEGER")
	public Integer getIntegerProperty();
	
	@PropertyBeanAttribute(propertyKey ="PROP_FLOAT_OBJ")
	public Float getFloatObjectProperty();
	
	@PropertyBeanAttribute(propertyKey ="PROP_DOUBLE_OBJ")
	public Double getDoubleObjectProperty();
	
	@PropertyBeanAttribute(propertyKey ="PROP_COLOR")
	public Color getColorProperty();
	
	// primitive array properties
	@PropertyBeanAttribute(propertyKey ="PROP_STRING_ARRAY")
	public String[] getStringArray();
	
	@PropertyBeanAttribute(propertyKey ="PROP_INT_ARRAY")
	public int[] getIntArray();
	
	@PropertyBeanAttribute(propertyKey ="PROP_FLOAT_ARRAY")
	public float[] getFloatArray();
	
	@PropertyBeanAttribute(propertyKey ="PROP_DOUBLE_ARRAY")
	public double[] getDoubleArray();
	
	// object array properties
	@PropertyBeanAttribute(propertyKey ="PROP_INTEGER_OBJ_ARRAY")
	public Integer[] getIntegerObjectArray(Class<?> clazz);

	@PropertyBeanAttribute(propertyKey ="PROP_FLOAT_OBJ_ARRAY")
	public Float[] getFloatObjectArray(Class<?> clazz);
	
	@PropertyBeanAttribute(propertyKey ="PROP_DOUBLE_OBJ_ARRAY")
	public Double[] getDoubleObjectArray(Class<?> clazz);
	
	// Object Map properties
	@PropertyBeanAttribute(propertyKey ="PROP_STRING_MAP")
	public Map<String,String> getStringMap();
	
	@PropertyBeanAttribute(propertyKey ="PROP_COLOR_MAP")
	public Map<String,Color> getColorMap(Class<?> clazz);
	
	// Object array map properties
	@PropertyBeanAttribute(propertyKey ="PROP_INTEGER_ARRAY_MAP")
	public Map<String, Integer[]> getIntArrayMap(Class<?> clazz);
	
	@PropertyBeanAttribute(propertyKey ="PROP_STRING_ARRAY_MAP")
	public Map<String, String[]> getStringArrayMap(Class<?> clazz);
	
}
