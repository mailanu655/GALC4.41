package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.ProcessProduct;

/**
 * 
 * <h3>ITrakingObserver</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ITrakingObserver description </p>
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
 * Nov 27, 2010
 *
 */

@ObserverInterface
public interface ITrakingObserver {
	
	@ProcessProductState(actions = { Action.COMPLETE })
	void trackProduct(ProcessProduct state);
}
