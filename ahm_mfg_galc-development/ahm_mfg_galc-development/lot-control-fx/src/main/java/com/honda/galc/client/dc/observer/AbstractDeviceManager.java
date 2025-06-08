package com.honda.galc.client.dc.observer;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.ei.PlcDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.Acknowledgment;
import com.honda.galc.device.dataformat.DataCollectionComplete;

/**
 * 
 * <h3>DeviceManagerBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DeviceManagerBase description </p>
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
 * <TD>Apr 27, 2011</TD>
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
 * @since Apr 27, 2011
 * @see
 * @version 0.2
 * @author Jeffray Huang
 * @since Feb 27, 2014
 * */
public abstract class AbstractDeviceManager extends AbstractManager{
	public AbstractDeviceManager(DataCollectionController dcController) {
		super(dcController);
	}

	public void sendDataCollectionComplete() {
		if(getEiDevice().containOutputDeviceData(DataCollectionComplete.class))
			sendEiDeviceData(DataCollectionComplete.OK());
	}
	
	public void sendDataCollectionInComplete() {
		if(getEiDevice().containOutputDeviceData(DataCollectionComplete.class))
			sendEiDeviceData(DataCollectionComplete.NG());
	}
	
	protected void sendEiDeviceData(IDeviceData deviceData) {
		try {
			getLogger().info("start sendDeviceData: " + deviceData.getClass().getSimpleName());
			
			Acknowledgment acknowlegement = (Acknowledgment)getEiDevice().syncSend(deviceData);
			if(!acknowlegement.isSuccess()){
				getLogger().error(acknowlegement.getTransmitException());
			}
	
			getLogger().info("sendDeviceData " + deviceData + " succeeded.");
		} catch (Throwable e) {
			handleException(deviceData, e);
		}
	}

	public EiDevice getEiDevice() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		return eiDevice;
	}
	
	public PlcDevice getPlcDevice() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice == null || !(eiDevice instanceof PlcDevice)){
			getLogger().error("ERROR: ", "No plc device available.");
			return null;
		}
		
		return (PlcDevice)eiDevice;
	}
	
	protected abstract void handleException(IDeviceData deviceData, Throwable e);

}
