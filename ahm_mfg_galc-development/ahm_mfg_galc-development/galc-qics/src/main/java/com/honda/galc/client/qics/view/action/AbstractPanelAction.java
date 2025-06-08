package com.honda.galc.client.qics.view.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMain;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.client.qics.config.QicsClientConfig;
import com.honda.galc.client.qics.controller.QicsController;
import com.honda.galc.client.qics.view.constants.ActionId;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Abstract superclass for implementors of <code>Action</code> interface that
 * are listening to components contained within instances of
 * <code>QicsPanel</code>.
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */

public abstract class AbstractPanelAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private QicsPanel qicsPanel;
	
	private String authorizationGroup;
	private ActionId actionId;

	public AbstractPanelAction(QicsPanel qicsPanel) {
		this.qicsPanel = qicsPanel;
		setActionId();
		setAuthorizationGroup();
	}

	// === action method === //
	protected void beforeExecute(ActionEvent e) {
		getQicsFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		getQicsFrame().clearMessage();
	}

	protected void execute(ActionEvent e) {

	}

	protected void afterExecute(ActionEvent e) {
		getQicsFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public void actionPerformed(ActionEvent e) {
		getQicsFrame().getLogger().info((String)this.getValue(Action.NAME) + " button clicked");
		
		try {
			beforeExecute(e);
			if (!isAuthorized()) {
				return;
			}
			execute(e);
		}catch(Exception ex) {
			ex.printStackTrace();
			handleException (ex);
		} finally {
			afterExecute(e);
		}
	}
	
	protected void setActionId() {
		for (ActionId id : ActionId.values()) {
			if (id.getActionClass() != null && id.getActionClass().equals(this.getClass())) {
				setActionId(id);
				return;
			}
		}
	}
	
	protected void setAuthorizationGroup() {
		ActionId actionId = getActionId();
		if (actionId == null) {
			setAuthorizationGroup(null);
			return;
		}
		Map<String, String> map = getQicsController().getQicsPropertyBean().getAuthorizationGroup();
		if (map == null) {
			setAuthorizationGroup(null);
			return;
		}
		String authorizationGroup = map.get(getActionId().name()); 
		setAuthorizationGroup(authorizationGroup);
	}

	protected boolean isAuthorized() {
		return isAuthorized(getAuthorizationGroup());
	}

	protected boolean isAuthorized(String authorizationGroup) {
		if (StringUtils.isBlank(authorizationGroup)) {
			return true;
		}
		if (LoginDialog.login() != LoginStatus.OK) {
			return false;
		}
		String authenticatedUser = ClientMain.getInstance().getAccessControlManager().getUserName();
		if (!ClientMain.getInstance().getAccessControlManager().isAuthorized(authorizationGroup)) {
			String msg = "User:" + authenticatedUser + " is not authorized to execute " + getActionId() + " action.";
			Logger.getLogger().info(msg);
			JOptionPane.showMessageDialog(getQicsFrame(), "You are not authorized to execute " + getActionId() + " action!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Logger.getLogger().info("Authorized User:" + authenticatedUser + " executed " + getActionId() + " action.");
		return true;
	}

	// === get/set === //
	public void setQicsPanel(QicsPanel qicsPanel) {
		this.qicsPanel = qicsPanel;
	}

	protected QicsPanel getQicsPanel() {
		return qicsPanel;
	}

	protected QicsFrame getQicsFrame() {
		return getQicsPanel().getQicsFrame();
	}

	protected QicsController getQicsController() {
		return getQicsFrame().getQicsController();
	}

	protected QicsClientConfig getClientConfig() {
		return getQicsController().getClientConfig();
	}

	protected void sendDataCollectionCompleteToPlcIfDefined() {
		getQicsFrame().sendDataCollectionCompleteToPlcIfDefined();
	}

	protected void sendDataCollectionNotCompleteToPlcIfDefined() {
		getQicsFrame().sendDataCollectionNotCompleteToPlcIfDefined();
	}

	protected void setMessage(String errorMessageId) {
		getQicsFrame().setMessage(errorMessageId);
	}

	protected void setErrorMessage(String errorMessageId) {
		getQicsFrame().setErrorMessage(errorMessageId);
	}
	
	protected void handleException(Exception ex) {
		getQicsFrame().displayException(ex);
	}

	protected String getAuthorizationGroup() {
		return authorizationGroup;
	}

	protected void setAuthorizationGroup(String authorizationGroup) {
		this.authorizationGroup = authorizationGroup;
	}

	protected void setActionId(ActionId actionId) {
		this.actionId = actionId;
	}
	
	protected ActionId getActionId() {
		return actionId;
	}
}
