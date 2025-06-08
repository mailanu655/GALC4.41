package com.honda.galc.client.datacollection.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.IPartLotViewObserver;
import com.honda.galc.client.datacollection.state.ProcessTorque;

/**
 * 
 * <h3>ClassicPartLotViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ClassicPartLotViewManager description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Jan 26, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jan 26, 2012
 */

public class ClassicPartLotViewManager extends ClassicViewManager implements IPartLotViewObserver, ActionListener{

	public ClassicPartLotViewManager(ClientContext clientContext) {
		super(clientContext);
		
		//initButtonList();
	}

	public void actionPerformed(ActionEvent e) {
		// This is a skeleton class for Part Lot for future use
		// No Part Lot control implementation yet for Classic 
		
	}

	public void rejectTorque(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}

}
