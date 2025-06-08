package com.honda.galc.client.teamleader.reprint;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.entity.conf.Application;

/**
 * 
 * <h3>ReprintWindow Class description</h3>
 * <p> ReprintWindow description </p>
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
 * Apr 19, 2011
 *
 *
 */
public class ReprintWindow extends MainWindow {

	private static final long serialVersionUID = 1L;
	
	public ReprintWindow(ApplicationContext appContext, Application application) {
		super(appContext, application, true);
		
		setSize(1024,768);
		
		initClientPanel();
	}

	private void initClientPanel() {
		
		this.setClientPanel(new ReprintPanel(this));
		
	}


}
