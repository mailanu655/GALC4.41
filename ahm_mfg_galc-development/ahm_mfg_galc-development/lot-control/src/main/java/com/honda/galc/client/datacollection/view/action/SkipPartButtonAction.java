package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.net.Request;
/**
 * <h3>SkipPartButtonAction</h3>
 * <h4>
 * Defines action for Skip Part button.
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
 * <TR>
 * <TD>Meghana G</TD>
 * <TD>Mar 22, 2011</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD> Modified the code to read the actionCommand configured as a property in DB. Depending on configured action Command. 
 * Skip will either skip the entire part or will just skip the current input. </TD>
 * </TR>  
 * <TR>
 * <TD>Meghana G</TD>
 * <TD>Mar 22, 2011</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD> Modified the code to read the actionCommand configured as a property specific to ProcessPoint. </TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
public class SkipPartButtonAction extends BaseDataCollectionAction {
	public SkipPartButtonAction(ClientContext context, String name) {
		super(context, name);
	}

	private static final long serialVersionUID = 9221242645490018462L;

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		
		if(getProperty().isNeedAuthorizedUserToSkipPart() && !login()) return;
		
		logInfo();
		skipCurrentPart();
		
	}
		
	public void skipCurrentPart() {
		try {
			runInSeparateThread(new Request(getProperty().getSkipPartActionCommand()));
		} catch (java.lang.Throwable t) {
			handleException(t);
		}
	
	}
}
		

