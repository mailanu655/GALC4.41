package com.honda.galc.client.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.util.ReflectionUtils;

/**
 * <h3>Class description</h3>
 * ApplicationMainWindow can be used with panels that do not have
 * direct relationship to specific window. The clients use this 
 * window need a property MAIN_PANEL_CLASS.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>May 22, 2013</TD>
 * <TD>1.0</TD>
 * <TD>GY 20130522</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */


public class ApplicationMainWindow extends MainWindow {
	private static final long serialVersionUID = 8204181488693561117L;
	
	public ApplicationMainWindow(ApplicationContext appContext, Application application) {
		this(appContext, application, false);
	}

	public ApplicationMainWindow(ApplicationContext appContext, Application application, boolean statusPanelFlag) {
		super(appContext, application, statusPanelFlag);
		initializeGui();
	}
	
	private void initializeGui() {
		String mainPanelClassName = this.getApplicationPropertyBean().getMainPanelClass();
		if(!StringUtils.isEmpty(mainPanelClassName)){
			JPanel panel = createPanel(mainPanelClassName);
			if(panel != null) {
				this.setClientPanel(panel);
			}
		}
		setSize(1024, 768);
	}
	
	public void cleanUp() {
		super.cleanUp();
	}

	protected JPanel createPanel(String className) {
		Class<?> panelClass = null;
		JPanel panel = null;
		
		try {
			panelClass = Class.forName(className);
			panel =  (JPanel) ReflectionUtils.createInstance(panelClass, new Class[] {this.getClass()}, new Object[] {this});
		} catch (ClassNotFoundException e) {
			panel = new JPanel();
			panel.add(new JLabel("Unable to create panel: " + className));
		}
		
		return panel;
	}

	public void displayMessage(String msg) {
		if(getStatusMessagePanel() != null) {
			setMessage(msg);
		}
	}

	public void displayErrorMessage(String msg) {
		if(getStatusMessagePanel() != null) {
			setErrorMessage(msg);
		}
	}
}
