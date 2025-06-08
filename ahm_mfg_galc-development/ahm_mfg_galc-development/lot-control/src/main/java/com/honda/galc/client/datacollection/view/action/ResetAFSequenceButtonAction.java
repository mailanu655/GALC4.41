package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.view.ResetAfSequenceDialog;
import com.honda.galc.net.Request;



public class ResetAFSequenceButtonAction extends BaseAFOnAction{

	private static final long serialVersionUID = 1L;

	public ResetAFSequenceButtonAction(ClientContext context, String name) {
		super(context, name);
	}
	
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		resetSequence();
	}

	private void resetSequence() {
		if(!login(context.getFrame())) {
			logInfo();
			return;
		}
			ResetAfSequenceDialog dialog = new ResetAfSequenceDialog(context.getFrame());
			dialog.setLocationRelativeTo(context.getFrame());
			dialog.setVisible(true);
			logInfo();
			runInSeparateThread(new Request("cancel"));
			//send request trigger
			requestTrigger();
	}
	
	
}
