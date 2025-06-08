package com.honda.galc.client.common.component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.ei.PlcDevice;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.device.property.PlcDevicePropertyBean;
import com.honda.galc.client.device.property.TorqueDevicePropertyBean;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceDriver;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.util.KeyValue;

public class DeviceTableModel extends PropertyTableModel{
	private static final long serialVersionUID = 1L;
	
	
	public DeviceTableModel(Class<IDevice> property, Object o, JTable table) {
		super(getDevicePropertyList(property, o), table);
	}

	private static List<KeyValue<String, String>> getDevicePropertyList(Class<IDevice> property, Object o) {
		List<KeyValue<String, String>> propertyList = new ArrayList<KeyValue<String,String>>();
		for(Method method : getPropertyMethods(o)){
			if(o instanceof PlcDevice){
				for(IDeviceDriver driver : ((PlcDevice)o).getDriverList())
					addProperty(driver, propertyList, method);

			} else
				addProperty(o, propertyList, method);
		}
		
		return propertyList;
	}

	private static List<Method> getPropertyMethods(Object o) {
		ArrayList<Method> list = new ArrayList<Method>();
		if(o instanceof TorqueSocketDevice){
			Method[] methods = TorqueDevicePropertyBean.class.getMethods();
			for(int i = 0; i < methods.length; i++){
				if(!methods[i].getName().equals("getClassName")){
					list.add(methods[i]);
				}
			}
		}else if(o instanceof PlcDevice){
			Method[] methods = PlcDevicePropertyBean.class.getMethods();
			for(int i = 0; i < methods.length; i++){
				if(!methods[i].getName().equals("getClassName")){
					list.add(methods[i]);
				}
			}
			
		}else if(o instanceof EiDevice){
			Method[] methods = DevicePropertyBean.class.getMethods();
			for(int i = 0; i < methods.length; i++){
				if(!methods[i].getName().equals("getClassName")){
					list.add(methods[i]);
				}
			}
		}
			
		return list;
	}

}
