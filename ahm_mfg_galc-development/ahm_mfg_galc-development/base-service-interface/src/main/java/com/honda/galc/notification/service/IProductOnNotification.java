package com.honda.galc.notification.service;

import com.honda.galc.common.message.MessageType;


/**
 * 
 * <h3>IProductOnEvent Class description</h3>
 * <p> IProductOnEvent description </p>
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
 * Feb 1, 2013
 *
 *
 */
public interface IProductOnNotification extends INotificationService {
	
	public void execute(String productionLot, String processLocation, int stampedCount);

	public void execute(String productId, String productionLot, String processLocation, int stampedCount);
	
	public void execute(String productId, String message, String processLocation, MessageType type);
}
