package com.honda.galc.notification.service;

import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;


/**
 * 
 * 
 * <h3>IEngineShippingNotification Class description</h3>
 * <p> IEngineShippingNotification description </p>
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
 * Jul 21, 2015
 *
 *
 */
public interface IEngineShippingNotification extends INotificationService {
	
	public void shippingQuorumUpdated(ShippingQuorum quorum);
	
	public void engineLoaded(int trailerId,ShippingQuorumDetail quorumDetail);

}
