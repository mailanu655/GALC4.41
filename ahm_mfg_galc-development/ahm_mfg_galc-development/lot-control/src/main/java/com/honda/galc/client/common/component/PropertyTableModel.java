package com.honda.galc.client.common.component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>PropertyTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PropertyTableModel description </p>
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
 * @author Paul Chou
 * May 26, 2010
 *
 */
public class PropertyTableModel extends SortableTableModel<KeyValue<String, String>> {
	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {"KEY", "VALUE"};

	public PropertyTableModel(List<KeyValue<String, String>> items, JTable table) {
		super(items, columns, table);
	}

	public PropertyTableModel(Class<? extends IProperty> property, Object o, JTable table) {
		super(getPropertyList(property, o), columns, table);
	}
	
	public static List<KeyValue<String, String>> getPropertyList(Class<? extends IProperty> property, Object o) {
		List<KeyValue<String, String>> propertyList = new ArrayList<KeyValue<String,String>>();
		for(Method method : getPropertyMethods(property)){
			addProperty(o, propertyList, method);
			
		}
		
		return propertyList;
	}

	@SuppressWarnings("unchecked")
	protected static void addProperty(Object o, List<KeyValue<String, String>> propertyList, Method method) {
		Object[] parameters = {};
		if(method.getReturnType() == Map.class) {
			String key =  getPropertyKey(method.getName());
			Map<String, String> map = (Map)getPropertyValue(o, parameters, method);
			
			if(map == null) return; //OK if not defined; don't add to property list
			
			for(Map.Entry<String, String> me : map.entrySet()){
				KeyValue<String, String> keyValue = new KeyValue<String, String>(key + "{" + me.getKey() + "}", me.getValue());
				if(!propertyList.contains(keyValue)) propertyList.add(keyValue);
			}
			
		} else {
			String key =  getPropertyKey(method.getName());
			Object value = getPropertyValue(o, parameters, method);
			KeyValue<String, String> keyValue = new KeyValue<String, String>(key, value == null ? "" : value.toString());
			if(!propertyList.contains(keyValue))
				propertyList.add(keyValue);
		}
	}

	private static List<Method> getPropertyMethods(Class<? extends IProperty> property) {
		List<Method> methodList = new ArrayList<Method>();
		
		if(property.getAnnotation(PropertyBean.class) != null)
			methodList.addAll(Arrays.asList(property.getMethods()));

		return methodList;
	}

	private static Object getPropertyValue(Object o, Object[] parameters, Method method){ 
			try {
				Method clzMethod = o.getClass().getMethod(method.getName(),method.getParameterTypes());
				return clzMethod.invoke(o, parameters);
			} catch (Exception e) {
				Logger.getLogger().debug("Failed to get property value for:" + method.getName());
			}
			return null;
	}

	private static String getPropertyKey(String name) {
		//Convert method name to Property
		//For example convert getMaxPartLength to MAX_PART_LENGTH
		StringBuilder sb = new StringBuilder();;
		for(int i = 0; i < name.length(); i++){
			if(Character.isUpperCase(name.charAt(i))){
				if(sb.length() > 0)
					sb.append("_").append(name.charAt(i));
				else
					sb.append(name.charAt(i));
				
			} else {
				if(sb.length() > 0)
					sb.append(Character.toUpperCase(name.charAt(i)));
			}
		}
		return sb.toString();
	}
	
	 public void join(PropertyTableModel model){
		 this.getItems().addAll(model.getItems());
	 }

}
