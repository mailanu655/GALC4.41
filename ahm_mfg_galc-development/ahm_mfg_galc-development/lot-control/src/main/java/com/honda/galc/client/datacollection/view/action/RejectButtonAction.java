package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.net.Request;
/**
 * 
 * <h3>RejectButtonAction</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RejectButtonAction description </p>
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
 * Jun 17, 2010
 *
 */
public class RejectButtonAction extends BaseDataCollectionAction {
	private static final long serialVersionUID = 8394239778741676739L;
	
	public RejectButtonAction(ClientContext context, String name) {
		super(context, name);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		
		if(getProperty().isNeedAuthorizedUserToReject() && !login()) return;
		
		logInfo();
		runInSeparateThread(new Request("reject"));
	}
}
