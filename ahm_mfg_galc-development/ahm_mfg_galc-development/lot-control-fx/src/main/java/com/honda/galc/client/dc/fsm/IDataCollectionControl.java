package com.honda.galc.client.dc.fsm;

import com.honda.galc.client.dc.common.Message;
import com.honda.galc.fsm.IObserver;

/**
 * <h3>IDataCollectionControl</h3>
 * <h4>
 * Common user control event interface to state machine.
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
public interface IDataCollectionControl {
	
	/**
	 * Error event - error message to listeners 
	 */
	void error(Message message);
	
	/**
	 * Broadcasting message to observers
	 * @param string
	 * @param string2
	 */
	void message(Message message);
	
	/**
	 * String of current data collection state 
	 */
	Object getCurrentState();
	
	/**
	 * Get the Product Spec code
	 */
	String getProductSpecCode();
	
	/**
	 * Add Observer
	 * @param o
	 */
	public void addObserver(IObserver o);
	
	/**
	 * Get observer instance by class 
	 * @param <T>
	 * @param clzz
	 * @return
	 */
	public <T extends IObserver> T getObserver(Class<T> clzz);
	
	/**
	 * Do clean up when system close/exit
	 *
	 */
	public void cleanUp();
}
