package com.honda.galc.client.device;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.ei.PlcDevice;
import com.honda.galc.client.device.plc.IPlcSocketDevice;
import com.honda.galc.device.DeviceDataConverter;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceDriver;
import com.honda.galc.device.IDeviceUser;
import com.honda.galc.device.exception.DeviceInUseException;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.property.IProperty;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;

/**
 * This class manages all the devices that client communicates with.  
 * 
 * All devices DeviceManager manages will implement the IDevice interface.  
 * 
 * DeviceManager can also register itself as a device listener, if actions need
 * to be taken based on an event that happened in the device. This is also useful 
 * to keep track of the correct status of a device at all times. 
 *  
 * @author Subu Kathiresan
 * Jan 30, 2009
 * 
 **/
public class DeviceManager {
	private static DeviceManager _deviceManager = null;
	
	private Hashtable<String, IDevice> _devices = new Hashtable<String, IDevice>();
	private Hashtable<String, KeyValue<Integer,String>> _exclusiveLocks = new Hashtable<String, KeyValue<Integer,String>>();
	private Random _random = new Random();
	private String _clientId = null;
	private String _applicationId = null;
	
	/**
	 * static method that returns the singleton instance
	 * 
	 * @return
	 */
	public static DeviceManager getInstance() {
		if (_deviceManager == null)
			_deviceManager = new DeviceManager();
		
		return _deviceManager; 
	}
	
	/**
	 * private default constructor used to 
	 * create the singleton object
	 */
	private DeviceManager(){}
	
	/**
	 * initializes the DeviceManager with the specified clientId and applicationId
	 * 
	 * @param device
	 * @param clientId
	 * @param applicationId
	 */
	public void initialize(IDevice device, String clientId, String applicationId) {
		Hashtable<String, IDevice> devices = new Hashtable<String, IDevice>();
		devices.put(device.getId(), device);
		
		_deviceManager.setDevices(devices);
		_deviceManager.setClientId(clientId);
		_deviceManager.setApplicationId(applicationId);

		activate();
	}

	/**
	 * initializes the DeviceManager with the specified clientId and applicationId
	 * 
	 * @param devices
	 * @param clientId
	 * @param applicationId
	 */
	public void initialize(Hashtable<String, IDevice> devices, String clientId, String applicationId) {
		_devices = devices;
		_deviceManager.setDevices(devices);
		_deviceManager.setClientId(clientId);
		_deviceManager.setApplicationId(applicationId);
		
		activate();
	}
	
	/**
	 * activates the device manager
	 */
	private void activate() {
		try {
			for(IDevice device: getDevices().values()) {
				getDevices().put(device.getId(), device);
			}

			// initialize all devices			
			initDevices();
		} catch(Exception ex) {
			getLogger().error("Failed to activate DeviceManager due to " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * initializes all devices
	 * 
	 * @return
	 */
	public boolean initDevices() {
		boolean allDevicesInitialized = true;
		
		for(IDevice device: getDevices().values()) {
			try {
				if (device.isEnabled())
					device.activate();	
			} catch(Exception ex) {			
				allDevicesInitialized = false;
				ex.printStackTrace();
				getLogger().error(ex,"Unable to initialize device " + device.getId());
			}
		}

		if (allDevicesInitialized) {
			getLogger().info("All enabled devices initialized");
			return true;
		} else {	
			getLogger().error("Unable to initialize atleast one enabled device");
			return false;
		}
	}
	
	/**
	 * Returns the device that matches the
	 * provided device Id.  Returns null 
	 * if device Id is not valid
	 * 
	 * @param deviceId
	 * @return
	 */
	public IDevice getDevice(String deviceId) {
		if (getDevices().containsKey(deviceId))
			return getDevices().get(deviceId);
		else 
			return null;
	}
	
	/**
	 * Returns a device, if available, with
	 * the specified id, or null, if not found
	 * 
	 * If the device is exclusively accessed by
	 * another application, throws a DeviceInUseException
	 * 
	 * @param deviceId
	 * @param app
	 * @return
	 * @throws DeviceInUseException
	 */
	public IDevice getDevice(String deviceId, IDeviceUser app) throws DeviceInUseException {
		if (getDevices().containsKey(deviceId)) {
			// check if exclusively locked
			if (getExclusiveLocks().containsKey(deviceId)) {
				KeyValue<Integer,String> holder = getExclusiveLocks().get(deviceId);
				
				// check if access key is valid
				if(app.getDeviceAccessKey(deviceId).intValue() != -1)
					// check if locked by this application
					if (holder.getKey() == app.getDeviceAccessKey(deviceId).intValue())
						return getDevices().get(deviceId);
					 
					throw new DeviceInUseException(deviceId, holder.getValue());
			} else {
				return getDevices().get(deviceId);
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the accessKey after registering the
	 * listener as the exclusive controller of the device
	 * Throws DeviceInUseException if another application
	 * is already using the device
	 * 
	 * @param deviceId
	 * @param app
	 * @return
	 */
	public synchronized Integer requestExclusiveAccess(String deviceId, IDeviceUser app) throws DeviceInUseException {
		if (getExclusiveLocks().containsKey(deviceId)) {	
			// already accessed exclusively
			KeyValue<Integer,String> holder = getExclusiveLocks().get(deviceId);
		
			// check if locked by this application
			if (holder.getKey() == app.getDeviceAccessKey(deviceId).intValue())
				return holder.getKey();
			else 
				throw new DeviceInUseException(deviceId, holder.getValue());
		} else {
			// not exclusively accessed, so, create a key
			Integer accessKey = getRandomGen().nextInt();
			
			// update locks and return the key
			getExclusiveLocks().put(deviceId, new KeyValue<Integer,String>(accessKey, app.getApplicationName()));
			return accessKey;
		}
	}
	
	/**
	 * Releases the device from the application that
	 * was exclusively using it.
	 * 
	 * @param deviceId
	 * @param app
	 * @return
	 */
	public synchronized boolean releaseExclusiveAccess(String deviceId, IDeviceUser app) {
		try {
			if (getExclusiveLocks().containsKey(deviceId)) {	
				// already accessed exclusively
				KeyValue<Integer,String> holder = getExclusiveLocks().get(deviceId);
				
				// remove only if the app is currently holding the lock
				if (holder.getKey() == app.getDeviceAccessKey(deviceId).intValue())
					getExclusiveLocks().remove(deviceId);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Unable to release exclusive lock on device " + deviceId);
			return false;
		}
		return true;
	}
	
	/**
	 * Returns a map of all devices that 
	 * are currently active
	 * 
	 * @return
	 */
	public TreeMap<String, IDevice> getActiveDevices() {
		TreeMap<String, IDevice> treeMap = new TreeMap<String, IDevice>();
		
		try {
			for(IDevice device: getDevices().values()) {
				if (device.isActive())
					treeMap.put(device.getId(), device);
			}
		} catch(Exception ex) {			
			ex.printStackTrace();
			getLogger().error(ex,"Unable to retrieve active devices");
		}
		
		return treeMap;
	}

	/**
	 * Closes communication with all devices and
	 * gracefully disengages them
	 */
	public boolean closeDevices() {
		for (IDevice device: getDevices().values()) {				
			try {
				if (device.isEnabled())
					device.deActivate();
			} catch(Exception ex) {
				ex.printStackTrace();
				getLogger().error(ex,"Unable to close device");
			}
		}

		return true;
	}
	
	
	/**
	 * @param devices the devices to set
	 */
	public void setDevices(Hashtable<String, IDevice> devices) {
		_devices = devices;
	}

	/**
	 * @return the devices
	 */
	public Hashtable<String, IDevice> getDevices() {
		return _devices;
	}
	
	public Set<String> getDeviceNames() {
		return _devices.keySet();
	}
	
	/**
	 * 
	 * @return
	 */
	public Hashtable<String, KeyValue<Integer,String>> getExclusiveLocks() {
		return _exclusiveLocks;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClientId() {
		return _clientId;
	}
	
	/**
	 * 
	 * @param clientId
	 */
	public void setClientId(String clientId) {
		_clientId = clientId;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getApplicationId() {
		return _applicationId;
	}
	
	/**
	 * 
	 * @param applicationId
	 */
	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;
	}
	
	/**
	 * 
	 * @return
	 */
	private Random getRandomGen() {
		return _random;
	}
	
	public void loadDevices(String terminalName) {
		List<ComponentProperty> plcProperties= PropertyService.getProperties(terminalName, "plcdevice.*deviceId\\d*");
		if(plcProperties.size() > 0) {
			createPlcDevices(terminalName, plcProperties);
		} else {
			createEiDevice(terminalName);
		}
		
		List<ComponentProperty> properties= PropertyService.getProperties(terminalName, "device.*deviceId\\d*");
		for(ComponentProperty property : properties) {
			DevicePropertyBean devicePropertyBean = getDevicePropertyBean(terminalName, property);
			if(!devicePropertyBean.isEnabled() || 
					StringUtils.isEmpty(devicePropertyBean.getDeviceId())) {
				getLogger().warn("Device is not enabled or DeviceId is empty");
				continue;
			}
			IDevice device = createDevice(terminalName, devicePropertyBean);
			if(device!= null)
			_devices.put(devicePropertyBean.getDeviceId(), device);
		}
	}
	
	private void createPlcDevices(String terminalName, List<ComponentProperty> plcProperties) {
		
		if(hasEiDevice()) return;
		
		List<IDeviceDriver> driverList = new ArrayList<IDeviceDriver>();
		PlcDevice device = new PlcDevice(terminalName);
		
		for(ComponentProperty property : plcProperties) {
			DevicePropertyBean devicePropertyBean = getDevicePropertyBean(terminalName, property);
			if(StringUtils.isEmpty(devicePropertyBean.getDeviceId()))continue;
			IDeviceDriver driver = (IDeviceDriver)createDevice(terminalName, devicePropertyBean);
			driverList.add((IDeviceDriver)driver);
		}
		
		device.setDriverList(driverList);
		device.setDeviceDataList(DeviceDataConverter.getInstance().getDeviceDataList());
		_devices.put(EiDevice.NAME,device);
		
	}


	public IDevice createDevice(String terminalName, DevicePropertyBean devicePropertyBean) {
		IDevice device = null;
		if(StringUtils.isBlank(devicePropertyBean.getClassName())) {
			getLogger().warn("ClassName property missed in configurations");
		} else {
			device = this.instantiateDevice(devicePropertyBean.getClassName()); 
			device.setClientId(terminalName);
			device.setDeviceProperty(devicePropertyBean);
			if (device instanceof IPlcSocketDevice){
				IPlcSocketDevice plcDevice = (IPlcSocketDevice) device;
				if (!plcDevice.isInitialized())
					plcDevice.activate();
			}
		}
		return device;
	}

	public DevicePropertyBean getDevicePropertyBean(String terminalName, ComponentProperty property) {
		String deviceString = property.getId().getPropertyKey();
		String deviceType = getDeviceType(deviceString);
		String suffix = getDeviceSuffix(deviceString);
		Class<IProperty> clazz = getPropertyBeanInterface(deviceType);
		DevicePropertyBean devicePropertyBean = (DevicePropertyBean)PropertyService.getPropertyBean(clazz, terminalName,suffix);
		return devicePropertyBean;
	}
	
	public void createEiDevice(String terminalName) {
		if(!EiDevice.hasEiDevice()) return;
		EiDevice device = new EiDevice(terminalName);
		_devices.put(EiDevice.NAME,device);
	}
	
	public boolean hasEiDevice() {
		return _devices.containsKey(EiDevice.NAME);
	}
	
	public EiDevice getEiDevice(IDeviceUser app) throws DeviceInUseException {
		
		return (EiDevice)getDevice(EiDevice.NAME, app);
		
	}
	
	public EiDevice getEiDevice() {
		return (EiDevice) getDevice(EiDevice.NAME);
	}
	
	private IDevice instantiateDevice(String className) {
		
		Class<?> deviceClass;
		IDevice device = null;
		try {
			deviceClass = Class.forName(className.trim());
			Constructor<?> deviceClassConstructor = deviceClass.getConstructor(new Class[]{});
			
			device = (IDevice)deviceClassConstructor.newInstance(new Object[]{});
			getLogger().info("device " ,deviceClass.getSimpleName(), " is created");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return device;
	}
	
	private  String getDeviceType(String deviceString) {
		return deviceString.split("\\.")[1];
	}
	
	private String getDeviceSuffix(String deviceString) {
		int index = deviceString.lastIndexOf("deviceId");
		if(index < 0 ) return "";
		return deviceString.substring(index + "deviceId".length());
	}
	
	@SuppressWarnings("unchecked")
	private  Class<IProperty> getPropertyBeanInterface(String deviceType) {
		String interfaceName = "com.honda.galc.client.device.property." + deviceType.substring(0,1).toUpperCase() + deviceType.substring(1) + "DevicePropertyBean";
		try {
			return (Class<IProperty>) Class.forName(interfaceName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}