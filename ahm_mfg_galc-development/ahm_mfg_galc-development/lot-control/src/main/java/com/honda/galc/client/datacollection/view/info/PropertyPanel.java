package com.honda.galc.client.datacollection.view.info;

import java.awt.Dimension;

import javax.swing.JLabel;

import com.honda.galc.client.ui.component.SplitInfoPanel;

/**
 * 
 * <h3>PropertyPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PropertyPanel description </p>
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
 * May 31, 2010
 *
 */
public class PropertyPanel extends SplitInfoPanel{
	private static final long serialVersionUID = 1L;
	private LabeledTablePanel tablePanel;

	public LabeledTablePanel getLabeledTablePanel() {
		if(tablePanel == null){
			tablePanel = new LabeledTablePanel("Properties:");
			tablePanel.setPreferredSize(new Dimension(LotControlSystemInfo.WIN_WIDTH,LotControlSystemInfo.WIN_HIGHT));
		}
		return tablePanel;
	}

	public PropertyPanel() {
		super();
		initialize();
	}

	public void initialize() {
		super.initialize();
		this.setPreferredSize(new Dimension(LotControlSystemInfo.WIN_WIDTH,
				LotControlSystemInfo.WIN_HIGHT - LotControlSystemInfo.BOTTOM_PANEL_HIGHT*3));
		
		getSelectionList().getLabel().setText("Properties:");
		getDetailPanel().add(getLabeledTablePanel());
		
	}

	
	public JLabel getPropertyLabel() {
		return getLabeledTablePanel().getLabel();
	}



}
