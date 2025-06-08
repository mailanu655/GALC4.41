package com.honda.galc.client.teamleader;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.data.ProductType;

/**
 * 
 * <h3>ManualLotControlGroupRepairPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlGroupRepairPanel description </p>
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
 * <TD>Nov. 18, 2016</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Nov. 18, 2016
 */
public class ManualLotControlGroupRepairPanel extends ManualLotControlRepairPanel {
	private static final long serialVersionUID = 1L;
	protected LabeledComboBox groupPanel;

	public ManualLotControlGroupRepairPanel(MainWindow mainWin) {
		super(mainWin);
	}

	public ManualLotControlGroupRepairPanel(MainWindow mainWin, ProductType productType) {
		super(mainWin, productType);
		
	}
	
	public JPanel getFilterPanel() {
		if(filterPanel == null){
			filterPanel = new JPanel();
			filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 5));
			filterPanel.add(getGroupIdPanel());
		}
		
		return filterPanel;
		
	}
	
	public LabeledComboBox getGroupIdPanel() {
		if(groupPanel == null){
			groupPanel = new LabeledComboBox("Group Id:");
			Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension dimension = new Dimension(screenDimension.width/3,45);
			groupPanel.setPreferredSize(dimension);
			groupPanel.getComponent().setPreferredSize(new Dimension(screenDimension.width/3,30));
			groupPanel.setMaximumSize(dimension);
			groupPanel.setFont(Fonts.DIALOG_BOLD_14);
			
			groupPanel.getComponent().setEditable(false);
			
		}
		return groupPanel;
	}

	protected ManualLotControlGroupRepairController createController() {
		return new ManualLotControlGroupRepairController(window,this, new ManualLtCtrResultEnterViewManager(window, property));
	}
	
	public TablePane getPartStatusPanel() {
		TablePane tablePanel = super.getPartStatusPanel();
		tablePanel.getTable().setFocusable(false);
		tablePanel.getTable().setRowSelectionAllowed(false);
		tablePanel.getTable().setEnabled(false);
		return tablePanel;
	}
	

	

}
