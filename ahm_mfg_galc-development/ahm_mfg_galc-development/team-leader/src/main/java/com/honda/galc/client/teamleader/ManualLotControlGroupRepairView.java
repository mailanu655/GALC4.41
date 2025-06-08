package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;
/**
 * 
 * <h3>ManualLotControlGroupRepairView</h3>
 * <h3> Class description</h3>
 * <h4> This is for GaugeFlex to repair a group of predefined parts </h4>
 * <p> ManualLotControlGroupRepairView description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Nov 29, 2016</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Nov 29, 2016
 */
public class ManualLotControlGroupRepairView extends TabbedPanel{
	private static final long serialVersionUID = 1L;
	private ManualLotControlGroupRepairPanel contentPanel;
	private TabbedMainWindow mainWin; 
	private boolean initialized = false;

	public ManualLotControlGroupRepairView(TabbedMainWindow mainWin) {
		super("Gauge Flex Group Repair", KeyEvent.VK_G);
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


	private ManualLotControlGroupRepairPanel getViewContentPanel() {
		if(contentPanel == null){
			contentPanel = new ManualLotControlGroupRepairPanel(mainWin);
		}

		return contentPanel;
	}

	@Override
	public void onTabSelected() {
		Logger.getLogger().info("Manual Lot Group Control Repair Panel is selected");
		if(initialized) return;

		initialize();

	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
