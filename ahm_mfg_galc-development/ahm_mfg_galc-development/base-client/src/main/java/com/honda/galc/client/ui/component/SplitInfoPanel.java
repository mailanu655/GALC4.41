package com.honda.galc.client.ui.component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import com.honda.galc.client.ui.component.LabeledListBox;
/**
 * 
 * <h3>SplitInfoPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SplitInfoPanel description </p>
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
 * Jun 1, 2010
 *
 */
public class SplitInfoPanel extends JSplitPane{
	private static final long serialVersionUID = 1L;
	protected LabeledListBox selectionList;
	protected JPanel detailsPanel;
	
	public SplitInfoPanel() {
		super();
	}

	public void initialize() {
		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		setDividerSize(1);
		setDividerLocation(150); 
		
		setLeftComponent(getSelectionList());
		setRightComponent(getDetailPanel());	
		
	}
	
	public JPanel getDetailPanel() {
		
		 if (detailsPanel == null) {
			 detailsPanel = new JPanel();
			 detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
	     }
	     
		return detailsPanel;
	}

	public void setDetailsPanel(JPanel detailsPanel) {
		this.detailsPanel = detailsPanel;
	}

	public LabeledListBox getSelectionList() {
		if(selectionList == null){
			selectionList = new LabeledListBox("");
			selectionList.getLabel().setAlignmentX(CENTER_ALIGNMENT);
			selectionList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			selectionList.getComponent().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);	
		}
		
		return selectionList;
	}

}
