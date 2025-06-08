package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>ManualLotControlRepairView</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlRepairView description </p>
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
 * Aug 18, 2010
 *
 */
public class ManualLotControlRepairView extends TabbedPanel {
	private static final long serialVersionUID = 1L;
	private ManualLotControlRepairPanel contentPanel;
	private TabbedMainWindow mainWin; 
	private boolean initialized = false;
	
	
	public ManualLotControlRepairView(TabbedMainWindow mainWin) {
		super("Manual Lot Control Repair", KeyEvent.VK_M);
		this.mainWin = mainWin;
		
		initialize();
	}

	private void initialize() {
		
		try {
			initComponents();
			initialized = true;
			
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start ManualLotControlRepairView");
		}
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		JScrollPane pane = new JScrollPane(getViewContentPanel());
		add(pane, BorderLayout.NORTH);
	}


	private ManualLotControlRepairPanel getViewContentPanel() {
		if(contentPanel == null){
			contentPanel = new ManualLotControlRepairPanel(mainWin);
		}
		
		return contentPanel;
	}

	@Override
	public void onTabSelected() {
		Logger.getLogger().info("Manual Lot Control Repair Panel is selected");
		if(initialized) return;
		
		initialize();
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
