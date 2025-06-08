package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;

public class ManualLotControlRepairRecursionView extends TabbedPanel{
	private static final long serialVersionUID = 1L;
	private ManualLotControlRepairRecursionPanel contentPanel;
	private TabbedMainWindow mainWin; 
	private boolean initialized = false;
	
	
	public ManualLotControlRepairRecursionView(TabbedMainWindow mainWin) {
		super("Manual Lot Control Repair Recursion", KeyEvent.VK_M);
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
		add(getViewContentPanel(), BorderLayout.NORTH);
	}


	private ManualLotControlRepairRecursionPanel getViewContentPanel() {
		if(contentPanel == null){
			contentPanel = new ManualLotControlRepairRecursionPanel(mainWin);
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
