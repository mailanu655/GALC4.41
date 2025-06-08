package com.honda.galc.client.datacollection.observer;
import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>IAudioObserver</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Paul Chou</TD>
 * <TD>Sep 11, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Paul Chou
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
@ObserverInterface
public interface IAudioObserver {

	@ProcessPartState(actions = { Action.NG })
	@ProcessProductState(actions = { Action.NG })
	public void playRepeatedNGSound(DataCollectionState state);
	
	@ProcessTorqueState(actions = { Action.NG, Action.CANCEL, Action.SKIP_PRODUCT, Action.SKIP_PART })
	@ProcessPartState(actions = { Action.CANCEL, Action.SKIP_PRODUCT, Action.SKIP_PART })
	@ProcessProductState(actions = {Action.SKIP_PRODUCT, Action.SKIP_PART })
	public void playNgSound(Object arg);

	@ProcessPartState(actions = { Action.OK })
	@ProcessTorqueState(actions = { Action.OK })
	public void playOkSound(Object arg);

	@ProcessProductState(actions = { Action.OK })
	public void playOkProductIdSound(Object arg);
	
	@ProcessProductState(actions = { Action.MESSAGE, Action.ERROR})
	@ProcessPartState(actions = { Action.MESSAGE, Action.ERROR })
	@ProcessTorqueState(actions = { Action.MESSAGE})
	public void message(DataCollectionState state);
	
	@ProcessPartState(actions = { Action.NO_ACTION })
	public void playNoActionSound(Object arg);
}
