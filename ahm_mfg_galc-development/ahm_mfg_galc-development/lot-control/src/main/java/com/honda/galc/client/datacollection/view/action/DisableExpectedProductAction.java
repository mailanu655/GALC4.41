package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.view.ViewManager;
import com.honda.galc.client.datacollection.view.ViewManagerBase;

/**
 * 
 * <h3>DisableExpectedProductAction</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DisableExpectedProductAction description </p>
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
 * Jun 22, 2010
 *
 */
public class DisableExpectedProductAction extends BaseDataCollectionAction {
	private static final long serialVersionUID = 1L;
	private ViewManagerBase viewManager;
	public DisableExpectedProductAction(ClientContext context, String name, ViewManagerBase viewManager) {
		super(context, name);
		this.viewManager = viewManager;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		
		if(getProperty().isNeedAuthorizedUserToDisableExpected() && !login()) return;
		
		logInfo();
		boolean disabledExpectedProductCheck = context.isDisabledExpectedProductCheck();
		context.setDisabledExpectedProductCheck(!disabledExpectedProductCheck);
		viewManager.enableExpectedProduct(!context.isDisabledExpectedProductCheck());
		
		((JButton)e.getSource()).setText(getExpectedProductControlButtonName());
		logInfo("Expected product check is " + ((context.isDisabledExpectedProductCheck()) ? "disabed" : "enabled"));
	}

	private String getExpectedProductControlButtonName() {
		
		return context.isDisabledExpectedProductCheck() ? 
				"Enable " + ViewManager.TEXT_EXPECTED_PRODUCT_ID : "Disable " + ViewManager.TEXT_EXPECTED_PRODUCT_ID;
	}

}
