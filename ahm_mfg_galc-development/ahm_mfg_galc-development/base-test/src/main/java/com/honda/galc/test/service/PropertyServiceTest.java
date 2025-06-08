package com.honda.galc.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.LaserEtcherSerialDevice;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.device.plc.omron.FinsSocketPlcDevice;
import com.honda.galc.client.device.property.TorqueDevicePropertyBean;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.device.printer.PrinterSocketDevice;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.property.IProperty;
import com.honda.galc.property.LoggerConfigPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.test.dao.AbstractBaseTest;

/**
 * @author Subu Kathiresan
 * @date May 23, 2014
 */
public class PropertyServiceTest extends AbstractBaseTest {
	
	public static final int STATUS_OK = 1;
	protected static int waitTimeInSecs = 20;
	
	public static List<String> componentIds = null;
	public static List<ComponentProperty> properties = null;
	public static List<ComponentProperty> plcProperties = null;
	DeviceManager deviceMgr = DeviceManager.getInstance();

	public PropertyServiceTest() {
		try {
			waitTimeInSecs = Integer.parseInt(System.getProperty("waitTimeInSecs"));
		} catch(Exception ex){}
	}
	
	@Before
	public void loadConfig() {
		
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
		HttpServiceProvider.setUrl("http://localhost:9080/BaseWeb/HttpServiceHandler");
	}
	
	@Test
	public void componentIdCount() {
		componentIds = getComponentPropertyDao().findAllComponentIds();
		int componentIdCount = 0;
		for(String componentId: componentIds) {
			assertEquals(componentIdCount, PropertyService.getComponentIds().size());
			properties = PropertyService.getComponentProperty(componentId);
			assertNotNull(properties);
			componentIdCount++;
		}
		
		// retrieve properties for all componentIds again
		for(String componentId: componentIds) {
			properties = PropertyService.getComponentProperty(componentId);
		}
		assertEquals(componentIdCount, PropertyService.getComponentIds().size());
	}
	
	@Test
	public void cacheItemCount() {
		componentIds = getComponentPropertyDao().findAllComponentIds();
		for(String componentId: componentIds) {
			properties = PropertyService.getComponentProperty(componentId);
			for (ComponentProperty property: properties) {
				// access all properties
				PropertyService.getProperty(componentId, property.getPropertyKey());
				System.out.println("COMPONENT_ID  : " + componentId);
				System.out.println("Property Name : " + property.getPropertyKey());
				System.out.println(PropertyService.getInstance().getSize());
			}
		}

		int initialCacheSize = PropertyService.getInstance().getSize();
		
		for(String componentId: componentIds) {
			PropertyService.getComponentProperty(componentId);
			properties = getComponentPropertyDao().findAllByComponentId(componentId);
			for (ComponentProperty property: properties) {
				// assert property exists
				assertTrue(PropertyService.hasProperty(componentId, property.getPropertyKey()));
				PropertyService.getProperty(componentId, property.getPropertyKey());
				System.out.println("COMPONENT_ID  : " + componentId);
				System.out.println("Property Name : " + property.getPropertyKey());
			}
		}

		int finalCacheSize = PropertyService.getInstance().getSize();
		System.out.println("Initial Cache size: " + initialCacheSize);
		System.out.println("Final Cache size  : " + finalCacheSize);
		
		assertEquals(initialCacheSize, finalCacheSize);
	}
	
	@Test
	public void beanListCount() {
		
		// retrieve a TorqueDevicePropertyBean multiple times
		for (int i = 0; i < 100; i++) {
			TorqueDevicePropertyBean torqueDevicePropertyBean = PropertyService.getPropertyBean(TorqueDevicePropertyBean.class, "SRS_RS_FR_AIRBAG");
			assertNotNull(torqueDevicePropertyBean);
			assertNotNull(torqueDevicePropertyBean.getDeviceId());
			assertEquals("torque1", torqueDevicePropertyBean.getDeviceId());
			
			List<IProperty> properties = PropertyService.getBeanList("SRS_RS_FR_AIRBAG");
			assertEquals(1, properties.size());
		}
		
		// retrieve a LoggerConfigPropertyBean multiple times
		for (int i = 0; i < 100; i++) {
			LoggerConfigPropertyBean loggerConfigProperty = PropertyService.getPropertyBean(LoggerConfigPropertyBean.class, "LOGGER");
			assertNotNull(loggerConfigProperty);
			assertEquals("", loggerConfigProperty.getClientPreferredSuffix());
			assertEquals(false, loggerConfigProperty.isUsePPID());
			
			List<IProperty> properties = PropertyService.getBeanList("LOGGER");
			assertEquals(1, properties.size());
		}
	}
	
	@Test
	public void loadSingleTorqueDevice() {
		
		// single torque device
		deviceMgr.loadDevices("SRS_RS_FR_AIRBAG");
		assertEquals(1, deviceMgr.getDevices().size());
		assertNotNull((TorqueSocketDevice)deviceMgr.getDevice("torque1"));
	}

	@Test
	public void loadMultiplePlcDevices() {
		
		//multiple plc devices
		deviceMgr.loadDevices("EI_PA");
		assertNotNull((FinsSocketPlcDevice)deviceMgr.getDevice("PA_A"));
		assertNotNull((FinsSocketPlcDevice)deviceMgr.getDevice("PA_B"));
	}
	
	@Test
	public void loadMultiplePrinterDevices() {
		
		// multiple printer devices
		deviceMgr.loadDevices("GALC_MAINTENANCE");
		
		assertNotNull((LaserEtcherSerialDevice)deviceMgr.getDevice("etcher1"));
		assertNotNull((PrinterSocketDevice)deviceMgr.getDevice("ipuprinter"));
		assertNotNull((PrinterSocketDevice)deviceMgr.getDevice("ipbmpl"));
		assertNotNull((PrinterSocketDevice)deviceMgr.getDevice("pmccc1"));
		assertNotNull((PrinterSocketDevice)deviceMgr.getDevice("imaprinter"));	
	}
	
	@Test
	public void loadDevicePropertyBeans() {
		for(String componentId: componentIds) {
			plcProperties = PropertyService.getProperties(componentId, "plcdevice.*deviceId\\d*");
			for(ComponentProperty property : plcProperties) {
				DevicePropertyBean devicePropertyBean = getDevicePropertyBean(componentId, property);
				assertNotNull(devicePropertyBean.getDeviceId());
				assertNotNull(devicePropertyBean);
			}
			
			properties = PropertyService.getProperties(componentId, "device.*deviceId\\d*");
			for(ComponentProperty property : properties) {
				DevicePropertyBean devicePropertyBean = getDevicePropertyBean(componentId, property);
				assertNotNull(devicePropertyBean.getDeviceId());
				assertNotNull(devicePropertyBean);
			}
		}
	}
	
	public DevicePropertyBean getDevicePropertyBean(String terminalName, ComponentProperty property) {
		String deviceString = property.getId().getPropertyKey();
		String deviceType = getDeviceType(deviceString);
		String suffix = getDeviceSuffix(deviceString);
		Class<? extends DevicePropertyBean> clazz = (Class<? extends DevicePropertyBean>) getPropertyBeanInterface(deviceType);
		DevicePropertyBean devicePropertyBean = (DevicePropertyBean)PropertyService.getPropertyBean(clazz, terminalName,suffix);
		return devicePropertyBean;
	}
	
	private  String getDeviceType(String deviceString) {
		return deviceString.split("\\.")[1];
	}
	
	private String getDeviceSuffix(String deviceString) {
		int index = deviceString.lastIndexOf("deviceId");
		if(index < 0 ) return "";
		return deviceString.substring(index + "deviceId".length());
	}
	
	private  Class<?> getPropertyBeanInterface(String deviceType) {
		String interfaceName = "com.honda.galc.client.device.property." + deviceType.substring(0,1).toUpperCase() + deviceType.substring(1) + "DevicePropertyBean";
		try {
			return Class.forName(interfaceName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
