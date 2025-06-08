package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>ProductCarrierRepairView</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductCarrierRepairView description </p>
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
 * @author K Maharjan
 * Nov 10, 2014
 *
 */
public class ProductCarrierRepairView extends TabbedPanel {
	private static final long serialVersionUID = 1L;
	private ProductCarrierRepairPanel contentPanel;
	private TabbedMainWindow mainWin; 
	private boolean initialized = false;
	
	
	public ProductCarrierRepairView(TabbedMainWindow mainWin) {
		super("Engine Rack Marriage", KeyEvent.VK_E);
		this.mainWin = mainWin;
		
		initialize();
	}

	private void initialize() {		
		try {
			initComponents();
			initialized = true;
			
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start EngineRackMarriageView");
		}
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		add(getViewContentPanel(), BorderLayout.NORTH);
	}

	private ProductCarrierRepairPanel getViewContentPanel() {
		if(contentPanel == null){
			contentPanel = new ProductCarrierRepairPanel(mainWin);
		}
		return contentPanel;
	}

	@Override
	public void onTabSelected() {
		Logger.getLogger().info("Enigne Rack Marriage is selected");
		if(initialized) return;
		
		initialize();
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub		
	}
	
}

