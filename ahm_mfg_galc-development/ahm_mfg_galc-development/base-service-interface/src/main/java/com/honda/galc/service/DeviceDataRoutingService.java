package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;

/**
 * 
 * 
 * <h3>DeviceDataRoutingService Class description</h3>
 * <p> DeviceDataRoutingService description </p>
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
 * Mar 24, 2014
 *
 *
 */
public interface DeviceDataRoutingService extends IoService{
	
	public DataContainer execute(DefaultDataContainer data);
	
	public void asyncExecute(DefaultDataContainer data);
	
	public void asyncAllExecute(DefaultDataContainer data);
	
}
