package com.honda.galc.device.mitsubishi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.device.property.PlcDevicePropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceFormatGroup;
import com.honda.galc.device.DevicePlcData;
import com.honda.galc.device.DriverDevice;
import com.honda.galc.device.IDeviceDriver;
import com.honda.galc.device.PlcDataBlock;
/**
 * 
 * <h3>DeviceScaner</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> A separate thread to scan plc data at configured scan rate.
 * Same register type are expected in the data ready device for example,
 * M101, M105, M104 OK
 * M101, D105, D106 NG
 * 
 * </p>
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
 * Oct 14, 2010
 *
 */
public class DeviceScaner implements Runnable{
	private IDeviceDriver driver;
	private PlcDevicePropertyBean property;
	private List<DriverDevice> dataReadyDeviceList;
	private Map<String, byte[]> dataReadyDeviceMap;
	private boolean isRunning = true;
	
	
	public DeviceScaner(QnAPollingDriver driver, 
			List<DriverDevice> dataReadyDeviceList, PlcDevicePropertyBean property) {
		super();
		this.driver = driver;
		this.dataReadyDeviceList = dataReadyDeviceList;
		this.property = property;
		
		initialize();
		
	}

	private void initialize() {
		dataReadyDeviceMap = new HashMap<String, byte[]>();
		
		for(DriverDevice dev : dataReadyDeviceList)
			dataReadyDeviceMap.put(dev.getDevice().getClientId(), null);
		
		getLogger().info("initialize scanner for ", driver.getId(), " data ready device:", dataReadyDeviceMap.keySet().toString());
		
	}

	void scan(){
		try {
			if (dataReadyDeviceList.size() > 0) {
				for (DriverDevice dev : dataReadyDeviceList) {
					scanDevice(dev);
				}

			} else {
				String pingPoint = property.getPingPoint();
				driver.read(pingPoint, QnASubCommand.bitUnit, 1, false);//act like heart beat
			}
		} catch (Exception e) {
			getLogger().error(e, "Exception on scan plc device.");
		}
		
	}

	private void scanDevice(DriverDevice dev) throws Exception {
		List<PlcDataBlock> dataList = new ArrayList<PlcDataBlock>();
		if(dev.isDataReadyDevice()){
			
			for(DeviceFormatGroup group : dev.getDeviceFormatGroupMap().values()){
				dataList.add(new PlcDataBlock(group.getId(), QnAParser.getReadPacketData(read(group))));
			}
		}
		
		if(dataList.size() > 0){
			driver.getQueue().add(new DevicePlcData(dev.getDevice().getClientId(), dataList, true));
		}
	}

	private byte[] read(DeviceFormatGroup group) throws Exception {
		return driver.read(group, false);
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void run() {
		getLogger().info("starting scaner for ", driver.getId());

		try{
			while(isRunning){

				scan();

				Thread.sleep(property.getScanRate());
			}
			
		} catch(Throwable t){
			getLogger().emergency(t, "Error: DeviceScaner exception: ", driver.getId());
		}
	}

	private Logger getLogger() {
		return driver.getLogger();
	}


	public long getScanRate() {
		return property.getScanRate();
	}

}
