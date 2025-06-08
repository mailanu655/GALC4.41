package com.honda.galc.client.datacollection.processor;

import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;



/**
 * <h3>IDataCollectionTaskProcessor</h3>
 * <h4>
 * Interface for all task processors
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
public interface IDataCollectionTaskProcessor <T extends IDeviceData>{
	public void init();
	public boolean execute(T data);
	public IDeviceData processReceived(IDeviceData deviceData) ;
	public void registerDeviceListener(DeviceListener listener);

}