package com.honda.galc.service.broadcast;


import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.net.DataContainerSocketSender;


/**
 * 
 * <h3>EquipmentBroadcast Class description</h3>
 * <p> EquipmentBroadcast description </p>
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
 * @author Jeffray Huang<br>
 * Aug 20, 2013
 *
 * 
 */
public class EquipmentBroadcast extends AbstractDeviceBroadcast{

	public EquipmentBroadcast(BroadcastDestination destination, String processPointId,DataContainer dc) {
		super(destination, processPointId,dc);
	}
	
	@Override
	public DataContainer syncSend(Device device, DataContainer dc) {
		DataContainer returnDC = null;
		try{
			DataContainerSocketSender socketSender
				= new DataContainerSocketSender(device.getEifIpAddress(),device.getEifPort());
			returnDC = socketSender.syncSend(dc);
			logger.info("sent data : " + dc.toString());
		}catch(Exception e) {
			if (returnDC == null) {
				returnDC = new DefaultDataContainer();
			}
			DataContainerUtil.error(logger, returnDC, e, "Failed to sync send data " + dc.toString() + " to device " + device);
		}
		return returnDC;
	}
	
	@Override
	public void send(Device device, DataContainer dc) {
		try{
			DataContainerSocketSender socketSender = 
				new DataContainerSocketSender(device.getEifIpAddress(),device.getEifPort());
			socketSender.send(dc);
			logger.info("sent data : " + dc.toString());
		}catch(Exception e) {
			DataContainerUtil.error(logger, dc, e, "Failed to send data " + dc.toString() + " to device " + device);
		}
	}
}
