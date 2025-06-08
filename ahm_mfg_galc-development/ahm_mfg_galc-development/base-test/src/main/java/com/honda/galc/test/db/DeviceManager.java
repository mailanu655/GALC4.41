package com.honda.galc.test.db;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.OpcConfigEntryDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.simulator.data.PropertiesTagValueSource;
import com.honda.galc.device.simulator.utils.DevSimulatorUtil;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.OpcConfigEntry;
import com.honda.galc.test.common.LoggerFactory;
import com.honda.galc.test.device.DataContainerFixture;
import com.honda.galc.test.device.DeviceSimulator;

import static com.honda.galc.service.ServiceFactory.getDao;

public class DeviceManager {
    
    public static DeviceManager deviceManager;
    private Logger log = LoggerFactory.createLogger(getClass().getSimpleName());
    ;
    private List<Device> devices= new ArrayList<Device>();
    private Device device = null;
    
    public static DeviceManager getInstance() {
        
        if(deviceManager == null) deviceManager = new DeviceManager();
        return deviceManager;
    }
    
    
    public DeviceManager(){

    	ApplicationContextProvider.loadFromClassPathXml("Dao.xml");
        for(Division division :ProcessManager.getInstance().getAllDivisions()) 
        	devices.addAll(getDao(DeviceDao.class).findAllByDivisionId(division.getDivisionId()));
        
    }
    
    /**
     * Load device data format data of the current process point into data container
     * this is used when only the process only uses one device data format
     * @return
     */
    
    public DataContainer loadDeviceData() {
        
        return loadDeviceData(getDevice().getClientId());

    }
    
    /**
     * load device data format data into data container
     *  /Have to set up device data format data under  /PROPERTY_FILES/DeviceDataFormat/ folder with
     * device id as the property file name
     * @param deviceId - device Id
     * @return
     */
    
    public DataContainer loadDeviceData(String deviceId) {
        
        PropertiesTagValueSource rs = new PropertiesTagValueSource();
        DefaultDataContainer plcData = new DefaultDataContainer();
        rs.loadEntries("/DeviceDataFormat/" + deviceId); 
        plcData.putAll(rs.toDataContainer());
        return plcData;
        
    }
    
    /**
     * Send Device data to the device using default device Id
     * Data is using default data
     * @return DataContainerFixture
     */
    
    public DataContainerFixture send() {
        
        return send(loadDeviceData());
        
    }
    
    /**
     * send device data to the device using default device Id
     * @param data - device data
     * @return DataContainerFixture
     */
    
    public DataContainerFixture send(DataContainer data) {
        
        return send(getDevice().getClientId().trim(), data);
        
    }
    
    /**
     * send device data to the device
     * @param deviceId - device Id
     * @param data - device data
     * @return DataContainerFixture
     */
    
    public DataContainerFixture send(String deviceId,DataContainer data) {
        
        return send(getDevice(deviceId), data);
        
    }
    
    private DataContainerFixture send(Device device,DataContainer data) {
        
        this.device = device;
        OpcConfigEntry opccfg = getOpcConfiguration(device.getClientId());
        DeviceSimulator.startOpcEiListener(device.getEifPort());
        DataContainerFixture rdcf = DeviceSimulator.send(opccfg.getOpcInstanceName(), device.getClientId(), data);
        return rdcf;
    }
    
    /**
     * Wait for reply
     * @param timeout - timeout value
     * @return DataContainerFixture
     */
    
	public DataContainerFixture waitReply(int timeout) {
        return DeviceSimulator.waitForData(device.getEifPort(), timeout);
        
	}

	public DataContainerFixture waitReply(int port, int timeout) {

		return DeviceSimulator.waitForData(port, timeout);

	}

    /*
     * getDevice from the current process point id
     * assume there is only one device id for the process point id
     */
    
    public Device getDevice() {
        
        return getDeviceFromProcessPoint(ProcessManager.getInstance().getProcessPointId().trim());
    }
    
    public Device getDeviceFromProcessPoint(String ppId) {
        
        for(Device device: this.getDevicesFromProcessPoint(ppId)) {
            if(device.getReplyClientId() != null && !device.getReplyClientId().equals("")){
            	this.device = device;
            	return device;
            }
        }
        
        return null;
        
    }
    
    public List<Device> getDevicesFromProcessPoint(String ppId) {
        
        List<Device> devs = new ArrayList<Device>();
        for(Device device:devices) {
            
            if(device.getIoProcessPointId() != null && 
                            ppId.equalsIgnoreCase(device.getIoProcessPointId().trim()))
            devs.add(device);                
        }
        return devs;
    }
    
    public Device getDevice(String deviceId) {
        
        for(Device device: devices) {
            if(device.getClientId().trim().equalsIgnoreCase(deviceId))
            {
            	this.device = device;
            	return device;
            }
        }
        return null;
    }
    
    public void updateHostIps() {
        
    	if(ProcessManager.getInstance().getProcessPointId() == null) return;
        
        
        for(Device device :getDevicesFromProcessPoint(ProcessManager.getInstance().getProcessPointId().trim())){
            
            updateDeviceHostIp(device);
            
        }

 
    }
    
    private void updateDeviceHostIp(Device device) {
        
        device.setEifIpAddress(DevSimulatorUtil.getLocalHostIP());
        
        OpcConfigEntry opccfg = getOpcConfiguration(device.getClientId());
        if(opccfg == null) return;
        if(opccfg.getServerClientTypeId() == 1) {
        	// http Client
        	
        	opccfg.setServerUrl(SystemPropertyManager.getProperty("Dispatcher"));
        }else if(opccfg.getServerClientTypeId() == 2) {
        	// Router client
        	if(ProcessManager.getInstance().getTerminal() != null){
        		
        		opccfg.setServerUrl(ProcessManager.getInstance().getTerminal().getIpAddress().toString() + ":" + 
        				            ProcessManager.getInstance().getTerminal().getRouterPort());
        	}
        }
        
        getDao(DeviceDao.class).save(device);

    }
    
    private OpcConfigEntry getOpcConfiguration(String devId) {
    	OpcConfigEntry opcCfg = null;
    	List<OpcConfigEntry> opcentries = getDao(OpcConfigEntryDao.class).findAllByDeviceId(devId);

		if(opcentries.size() > 0)
			opcCfg = opcentries.get(0);
		else
			log.warning("Error: Can not find OPC Instance name or device Id from OPC configuration!");
		
		if(opcentries.size() > 1)
			log.warning("Warn: Found more than one opc configuration enries!");
		
		return opcCfg;
	}


	public void startOpcEiListener() {
		DeviceSimulator.startOpcEiListener(device.getEifPort());
		
	}
	
	public void startOpcEiListener(int port) {
		
        DeviceSimulator.startOpcEiListener(port);
		
	}

	public void cleanUp() {
		DeviceSimulator.cleanUp();
		
	}


	public void cleanUpDeviceQueue() {
	    DeviceSimulator.cleanUpDeviceQueue(device.getEifPort());
	    
	}


	public void cleanUpDeviceQueue(Integer port) {
	    DeviceSimulator.cleanUpDeviceQueue(port);
	    
	}
    
}
