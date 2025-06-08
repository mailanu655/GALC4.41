package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JLabel;

import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.datacollection.BasePartResult;

public class OfflineRepairDataCollectionPanel extends DataCollectionPanel {
	private static final long serialVersionUID = 1L;

	protected ArrayList<JLabel> torqueResultLabelList = new ArrayList<JLabel>();
	protected OfflineRepairTableModel repairProcessPointTableModel;

	public OfflineRepairDataCollectionPanel(DefaultViewProperty property, int winWidth,
			int winHeight) {
		super(property, winWidth, winHeight);
	}

	@Override
	protected void initComponents() {
		super.initComponents();
		createRepairPartList();
		createRemoveResultButton();
	}

	
	private void createRepairPartList() {
		Rectangle bounds = getPartSerialNumber(0).getBounds();
		repairPartsTable= new TablePane("Repair Parts");
		repairPartsTable.setBounds(viewProperty.getRepairPartsTableStartPositionX(), bounds.y+viewProperty.getRepairPartsTableStartPositionY(), viewProperty.getRepairPartsTableWidth(), viewProperty.getRepairPartsTableHeight());
		repairPartsTable.getTable().setRowHeight(viewProperty.getRepairPartsTableRowHeight());
		repairProcessPointTableModel = new OfflineRepairTableModel(repairPartsTable.getTable(), new ArrayList<BasePartResult>());
	}
	
	public javax.swing.JButton createRemoveResultButton() {
		if (removeResultButton == null) {
			removeResultButton = new javax.swing.JButton("Remove Result");
			removeResultButton.setBounds(viewProperty.getRemoveResultButtonStartPositionX(), viewProperty.getRemoveResultButtonStartPositionY(),
					viewProperty.getRemoveResultButtonWidth(), viewProperty.getRemoveResultButtonHeight());
			removeResultButton.setName("JRemoveResult");
			removeResultButton.setFont(new Font("dialog", java.awt.Font.PLAIN, 16));
			removeResultButton.setEnabled(true);
			removeResultButton.setVisible(false);
		}
		return removeResultButton;
	}

	public void setProductSpecBackGroudColor(String colorName) {
		getTextFieldExpPidOrProdSpec().setBackground(Color.white);
	}

	
	public OfflineRepairTableModel getRepairProcessPointTableModel() {
		return repairProcessPointTableModel;
	}
	
	@Override
	protected int getTorquePositionY(int row, int height, int gap) {
		int y = viewProperty.getTorqueStartPositionY();
		return y + row * (height + gap);

	}
}
