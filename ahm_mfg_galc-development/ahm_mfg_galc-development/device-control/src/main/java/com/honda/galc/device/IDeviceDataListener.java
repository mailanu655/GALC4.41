package com.honda.galc.device;

import com.honda.galc.data.DataContainer;

/**
 * 
 * <h3>IDeviceDataListener</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IDeviceDataListener description </p>
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
 * Nov 5, 2010
 *
 */
public interface IDeviceDataListener extends DeviceListener{
	boolean isNotifyChangedOnly();
	boolean isNotifyChangedItemOnly();
	DataContainer received(String clientId, DataContainer dc);
}
