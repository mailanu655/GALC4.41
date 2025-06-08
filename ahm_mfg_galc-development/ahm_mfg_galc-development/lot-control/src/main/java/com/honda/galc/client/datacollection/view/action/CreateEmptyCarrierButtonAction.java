package com.honda.galc.client.datacollection.view.action;


import java.awt.event.ActionEvent;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.ProductId;

public class CreateEmptyCarrierButtonAction extends BaseAFOnAction{

	private static String EMPTY_CARRIER = "EMPTY_CARRIER";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CreateEmptyCarrierButtonAction(ClientContext context, String name) {
		super(context, name);
		// TODO Auto-generated constructor stub
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		createEmpty();
	}

	private void createEmpty() {
		boolean confirm = MessageDialog.confirm(context.getFrame(), " Are you Sure you want to create empty carrier?");
		if(confirm){
			Logger.getLogger().info(" Creating Empty Carrier");
			ProductId productId = new ProductId(context.getProperty().getEmptyCarrierName());
			runInSeparateThread(productId);
		}
	}
}
