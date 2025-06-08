package com.honda.galc.client.ui;

import java.awt.Container;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JPanel;

import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>MainPanel Class description</h3>
 * <p> MainPanel description </p>
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
 * @author Jeffray Huang<br>
 * Nov 21, 2010
 *
 *
 */
public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	
	protected MainWindow getMainWindow() {
		Container container = getTopLevelAncestor();
		if(container != null && container instanceof MainWindow)
			return (MainWindow) container;
		else return null;
	}
	
	protected void handleException (Exception e) {
		if(e == null) this.clearErrorMessage();
		else {
			getLogger().error(e, "unexpected exception occurs: " + e.getMessage() + " stack trace:" + getStackTrace(e));
			this.setErrorMessage(e.getMessage());
			
		}
	}
	
	protected void clearErrorMessage() {
		if(getMainWindow() != null)getMainWindow().clearMessage();
	}
	
	protected String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}
	
	protected void setErrorMessage(String errorMessage){
		getLogger().warn(errorMessage);
		if(getMainWindow() != null) getMainWindow().setErrorMessage(errorMessage);
	}
	
	protected void setWarningMessage(String warnMessage){
		getLogger().warn(warnMessage);
		if(getMainWindow() != null) getMainWindow().setWarningMessage(warnMessage);
	}

	protected void setMessage(String message){
		if(getMainWindow() != null) getMainWindow().setMessage(message);
	}
	
	protected void setStatusMessage(String message) {
		if(getMainWindow() != null) getMainWindow().setStatusMessage(message);
	}
	
	protected Logger getLogger(){
		if(getMainWindow() != null){
			String applicationId = getMainWindow().getApplication().getApplicationId();
			return Logger.getLogger(applicationId);
		}else return Logger.getLogger();
	}


}
