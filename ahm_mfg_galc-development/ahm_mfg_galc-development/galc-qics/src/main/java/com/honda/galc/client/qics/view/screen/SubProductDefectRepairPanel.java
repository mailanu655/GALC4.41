package com.honda.galc.client.qics.view.screen;

import java.awt.event.ActionEvent;

import com.honda.galc.client.qics.model.ProductModel;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.component.LabeledComboBox;

public class SubProductDefectRepairPanel extends DefectRepairPanel{
	
	private static final long serialVersionUID = 1L;
	
	private LabeledComboBox subProductComboBox;
	
	public SubProductDefectRepairPanel(QicsFrame frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}

	@Override 
	public QicsViewId getQicsViewId() {
	    return QicsViewId.SUB_PRODUCT_REPAIR;
	 }
	 
	
	@Override
	protected void initialize() {
		setLayout(null);
		setSize(getTabPaneWidth(), getTabPaneHeight());

		subProductComboBox = createProductTypeComboBox();
		defectPane = createDefectPane();
		defectPane.setLocation(0, 40);
		defectPane.setSize(1000, 295);
		if (getClientConfig().isScreenTouch()) {
			defectPane.getTable().setRowHeight((int) (defectPane.getTable().getRowHeight() * getClientConfig().getScreenTouchFactor()));
		}
		inputPane = createInputPane();

		defectStatusPanel = createDefectStatusPanel();
		actionButtonsPanel = createActionButtonsPanel();

		defectTablePopupMenu = createDefectTablePopupMenu();

		add(subProductComboBox);
		add(getDefectPane());
		add(getInputPane());

		add(getDefectStatusPanel());
		add(getActionButtonsPanel());

		setComponentZOrder(getInputPane(), 0);

		mapActions();
		mapEventHandlers();
	}
	
	private LabeledComboBox createProductTypeComboBox() {
		LabeledComboBox productTypeComboBox = new LabeledComboBox("SUB PRODUCT TYPE");
		productTypeComboBox.setModel(getQicsController().getQicsPropertyBean().getSubProductTypes(), -1);
		productTypeComboBox.setLocation(0,0);
		productTypeComboBox.setSize(1000, 40);
		return productTypeComboBox;
	}
	
	@Override
	protected void mapActions() {
		subProductComboBox.getComponent().addActionListener(this);
		super.mapActions();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(subProductComboBox.getComponent()))
			subProductTypeSelected();
		else super.actionPerformed(e);
	}
	
	private void subProductTypeSelected(){
		getProductModel().getExistingDefects().clear();
		getQicsController().getSubProductDefects(getProductType());
		getDefectPane().reloadData(getProductModel().getExistingDefects());
	}
	
	private String getProductType() {
		return (String) subProductComboBox.getComponent().getSelectedItem();
	}
	
	@Override
	public ProductModel getProductModel() {
		return getQicsController().getSubProductModel(getProductType());
	}
	
}
