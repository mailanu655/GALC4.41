package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.common.LotControlAccessControlManager;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.view.ViewManagerBase;
import com.honda.galc.common.logging.Logger;
/**
 * <h3>BaseDataCollectionAction</h3>
 * <h4>
 * Base Action class.
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

public abstract class BaseDataCollectionAction extends AbstractAction {
	private static final long serialVersionUID = 200159338327837290L;
	protected ClientContext context;
	protected String authorizedUser;
	public BaseDataCollectionAction(ClientContext context, String name)
	{
		this.context = context;
		putValue(Action.NAME, name);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(LotControlAudioManager.isExist())
			LotControlAudioManager.getInstance().stopRepeatedSound();
	}
	
	public void traceLog(String messageString) {
		Logger.getLogger().debug(messageString);
		
	}

	protected void handleException(Throwable t) {
		Logger.getLogger().error(t, this.getClass().getSimpleName() + "::handleException()");
		
	}

	protected String getCurrentState() {
		return DataCollectionController.getInstance().getState().getClass().getSimpleName();
	}
	
	protected boolean login() {
		if(LotControlAccessControlManager.getInstance().login()){
			authorizedUser = LotControlAccessControlManager.getInstance().getAuthorizedUser();
			return true;
		} else {
			authorizedUser = null;
			return false;
		}
	}

	protected String getUserId() {
		return authorizedUser == null ? context.getUserId() : authorizedUser;
		
	}
	
	protected void logInfo(){
		logInfo(this.getClass().getSimpleName().replace("Button", "").replace("Action",""));
	}
	
	protected void logInfo(String action) {
		Logger.getLogger().info(action + " by user:" + getUserId() + " at state:" + getCurrentState());
		
	}
	
	protected TerminalPropertyBean getProperty(){
		return context.getProperty();
	}
	
	protected boolean runUniqueScanAction(String scan) {
		ViewManagerBase vmb = context.getCurrentViewManager();
		if (vmb == null) {
			context.createViewManager();
			vmb = context.getCurrentViewManager();
		}
		return vmb.handleUniqueScanCode(vmb.getUniqueScanType(scan));
	}
	
	protected void runInSeparateThread(final Object request){
		Thread t = new Thread(){
			public void run() {
				DataCollectionController.getInstance(context.getAppContext().getApplicationId()).received(request);
			}
		};
		
		t.start();
	}
}
