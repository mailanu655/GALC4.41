package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.net.Request;
/**
 * <h3>CancelButtonAction</h3>
 * <h4>
 * Defines action for Cancel button.
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
public class CancelButtonAction extends BaseDataCollectionAction {
	private static final long serialVersionUID = 4048471714964621331L;
	
	public CancelButtonAction(ClientContext context, String name) {
		super(context, name);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		if(getProperty().isNeedAuthorizedUserToCancel() && !login()) return;
		doCancel();
	}
	
	public void doCancel() {
		logInfo();
		runInSeparateThread(new Request("cancel"));
	}
}
