package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.ei.PlcDevice;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>LotControlPlcDeviceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlPlcDeviceManager description </p>
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
 * <TD>Feb 1, 2012</TD>
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
 * @since Feb 1, 2012
 */

public class LotControlPlcDeviceManager extends LotControlDeviceManager{
	protected PlcDevice plcDevice;
	
	public LotControlPlcDeviceManager(ClientContext context) {
		super(context);
	}

	@Override
	protected void initialize() {
		super.initialize();

		initEiDevice();
	}
	
	public PlcDevice getPlcDevice(){
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice == null || !(eiDevice instanceof PlcDevice)){
			Logger.getLogger().error("ERROR: ", "No plc device available.");
			return null;
		} 
		
		plcDevice = (PlcDevice) eiDevice;
		
		return plcDevice;
	}
	
}
