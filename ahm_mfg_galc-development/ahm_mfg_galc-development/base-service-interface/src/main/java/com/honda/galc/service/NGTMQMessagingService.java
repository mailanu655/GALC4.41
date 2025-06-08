package com.honda.galc.service;

import com.honda.galc.data.DataContainer;

/**
 * 
 * <h3>NGTMQMessagingService Class description</h3>
 * <p>
 * NGTMQMessagingService description
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
 * @author Haohua Xie<br>
 *         Feb 25, 2014
 * 
 * 
 */

public interface NGTMQMessagingService extends IService {
	
	public void send(DataContainer dc);

}
