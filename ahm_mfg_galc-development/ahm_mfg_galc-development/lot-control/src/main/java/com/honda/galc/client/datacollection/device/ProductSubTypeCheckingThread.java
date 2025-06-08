package com.honda.galc.client.datacollection.device;

import com.honda.galc.client.datacollection.observer.ProductSubIdDeviceManager;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.device.ei.PlcDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.SubIdSide;

/**
 * 
 * <h3>ProductSubTypeCheckingThread</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductSubTypeCheckingThread description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jan 24, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jan 24, 2012
 */

public class ProductSubTypeCheckingThread extends Thread{

	private boolean stop = false;
	private ProcessPart part;
	private PlcDevice plcDevice;
	private ProductSubIdDeviceManager deviceManager;
	
	public ProductSubTypeCheckingThread(ProcessPart part, PlcDevice plcDevice,
			ProductSubIdDeviceManager deviceManager) {
		super();
		this.part = part;
		this.plcDevice = plcDevice;
		this.deviceManager = deviceManager;
		this.setDaemon(true);
		
	}

	@Override
	public void run() {
		
		verifyProductSubId(part, plcDevice);
	}
	
	private void verifyProductSubId(ProcessPart part, PlcDevice plcDevice) {
		SubIdSide read = null;
		
		try {
			read = (SubIdSide)plcDevice.read(deviceManager.getProperty().getProductSubIdDeviceId());
		} catch (Throwable e) {
			Logger.getLogger().error(e, "exception on check SubId side.");
		}	
		
		while(!stop && !deviceManager.processSideRead(part, read)){
			try {
				Thread.sleep(deviceManager.getProperty().getProductSubIdPollingInterval());
				read = (SubIdSide)plcDevice.read(deviceManager.getProperty().getProductSubIdDeviceId());
			} catch (Throwable e) {
				Logger.getLogger().error(e, "exception on check SubId side.");
			}
		}
		
	}
	
	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	


}
