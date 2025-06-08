package com.honda.galc.service;

import com.honda.galc.data.DataContainer;

/**
 * 
 * <h3>MQMessagingService Class description</h3>
 * <p>
 * MQMessagingService description
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
 * @author Deshane Joseph<br>
 *         Apr 12, 2013
 * 
 * 
 */

public interface MQMessagingService extends IService {
	
	public void send(DataContainer dc);

}
