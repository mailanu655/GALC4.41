package com.honda.galc.client.datacollection.observer.knuckles;

import com.honda.galc.client.datacollection.observer.ObserverInterface;
import com.honda.galc.client.datacollection.observer.ProcessProductState;
import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.ProcessProduct;

/**
 * 
 * <h3>ICarliperObserver</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ICarliperObserver description </p>
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
 * Feb 24, 2011
 *
 */

@ObserverInterface
public interface IKnucklesProductObserver {
	@ProcessProductState(actions = {Action.OK})
	public void productIdOk(final ProcessProduct product);
	
}
