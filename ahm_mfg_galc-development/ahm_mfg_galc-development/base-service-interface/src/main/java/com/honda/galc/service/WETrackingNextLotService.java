package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
/**
 * 
 * <h3>WETrackingNextLotService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> WETrackingNextLotService description </p>
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
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author zqwang
 * @since December 03, 2014
 */
public interface WETrackingNextLotService extends IoService{
	
	public void confirmSend(DataContainer data);
}