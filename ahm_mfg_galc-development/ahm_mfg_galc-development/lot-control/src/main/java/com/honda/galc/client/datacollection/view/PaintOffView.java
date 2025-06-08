package com.honda.galc.client.datacollection.view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.ViewProperty;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.data.ProductType;


public class PaintOffView extends DataCollectionPanelBase{

	private static final long serialVersionUID = 1L;
	
	protected JLabel operationModeLabel;
	protected JLabel prodIdLabel;
	protected JLabel labelAfOnSeqNumValue = null;
	protected JLabel labelAfOnSeqNum = null;
	protected JLabel labelExpPidOrProdSpec = null;
	protected UpperCaseFieldBean textFieldExpPIDOrProdSpec = null;
	protected JLabel labelLotNumber;
	protected UpperCaseFieldBean textFieldLotNumber = null;
	
	protected UpperCaseFieldBean textFieldProdId;
	
	protected JButton resetSequenceButton,switchModeButton, relinkButton, passBodyButton;
	protected ViewProperty viewProperty; 
	protected TablePane upcomingSequenceTable, previouslyScannedTable;
	protected AssemblyOnTableModel previouslyScannedTableModel;

	
	
	public PaintOffView(ViewProperty property) {
		super();
		viewProperty = property;
		initialize();
	}

	private void initialize() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initComponents();
	}

	protected void initComponents() {
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JPanel productPanel = new JPanel();
		productPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
		
		if(viewProperty.isProductIdButton())
			productPanel.add(getJButtonProductId(), getJButtonProductId().getName());
		else
			productPanel.add(getLabelProdId(), getLabelProdId().getName());
		
		productPanel.add(getTextFieldProdId(), getTextFieldProdId().getName());
	
		JPanel productSpecPanel = new JPanel();
		productSpecPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 7, 10));
		productSpecPanel.add(getLabelExpPIDOrProdSpec(), getLabelExpPIDOrProdSpec().getName());
		productSpecPanel.add(getTextFieldExpPidOrProdSpec(), getTextFieldExpPidOrProdSpec().getName());
		
		JPanel lotPanel = new JPanel();
		lotPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 7, 10));
		lotPanel.add(getLabelLotNumber(), getLabelLotNumber().getName());
		lotPanel.add(getTextFieldLotNumber(), getTextFieldLotNumber().getName());
	
		Box prodBox = Box.createVerticalBox();
		prodBox.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 5));
		prodBox.add(productPanel);prodBox.add(productSpecPanel);prodBox.add(lotPanel);
		
		Box seqBox = Box.createVerticalBox();
		seqBox.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 5));
		seqBox.add(getLabelAfOnSeqNum(), getLabelAfOnSeqNum().getName());
		seqBox.add(getLabelAfOnSeqNumValue(),getLabelAfOnSeqNumValue().getName());
			
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
		topPanel.add(prodBox);topPanel.add(seqBox);
	
		
		Box tableModelBox = Box.createVerticalBox();
		tableModelBox.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 5));
		JPanel tableLabelPanel = new JPanel();
		tableLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 7, 10));
		tableLabelPanel.add(UiFactory.createLabel("Previously Scanned", new Font("dialog", java.awt.Font.BOLD, 20)));
		tableModelBox.add(tableLabelPanel);
		tableModelBox.add(getPreviouslyScannedTable());
	
			
		
		JPanel opModePanel = new JPanel();
		opModePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 5));
		opModePanel.add(getOperationModeLabel());
				
				
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 5));
		buttonPanel.add(getResetSequenceButton());
		buttonPanel.add(getSwitchModeButton());
		buttonPanel.add(getRelinkButton());
		buttonPanel.add(getPassBodyButton());
			
		mainPanel.add(topPanel);	mainPanel.add(tableModelBox); mainPanel.add(opModePanel);mainPanel.add(buttonPanel);
		add(mainPanel);
		
	}

	public JButton getSwitchModeButton() {
		if (switchModeButton == null) {
			switchModeButton = new javax.swing.JButton("Switch Mode");
			switchModeButton.setName("JSwitchMode");
			switchModeButton.setSize(50,40);
			switchModeButton.setFont(new Font("dialog", java.awt.Font.PLAIN, 16));
			switchModeButton.setEnabled(true);
		}
		return switchModeButton;
	}

	public JButton getResetSequenceButton() {
		if (resetSequenceButton == null) {
			resetSequenceButton = new javax.swing.JButton("Reset Sequence");
			resetSequenceButton.setName("JResetSequence");
			resetSequenceButton.setSize(50, 40);
			resetSequenceButton.setFont(new Font("dialog", java.awt.Font.PLAIN, 16));
			resetSequenceButton.setEnabled(true);
		}
		return resetSequenceButton;
	}
	
	public JButton getRelinkButton() {
		if (relinkButton == null) {
			relinkButton = new javax.swing.JButton("Relink");
			relinkButton.setName("JRelink");
			relinkButton.setSize(50, 40);
			relinkButton.setFont(new Font("dialog", java.awt.Font.PLAIN, 16));
			relinkButton.setEnabled(true);
		}
		return relinkButton;
	}
	
	public JButton getPassBodyButton() {
		if (passBodyButton == null) {
			passBodyButton = new javax.swing.JButton("Pass Body");
			passBodyButton.setName("JRelink");
			passBodyButton.setSize(50, 40);
			passBodyButton.setFont(new Font("dialog", java.awt.Font.PLAIN, 16));
			passBodyButton.setEnabled(true);
		}
		return passBodyButton;
	}
	
	public UpperCaseFieldBean getTextFieldExpPidOrProdSpec() {
		if (textFieldExpPIDOrProdSpec == null) {
			textFieldExpPIDOrProdSpec = new UpperCaseFieldBean();
			textFieldExpPIDOrProdSpec.setName("TextFieldExpPIDOrProdSpec");
			textFieldExpPIDOrProdSpec.setFont(new java.awt.Font("dialog", 0, 36));
			textFieldExpPIDOrProdSpec.setBounds(378, 25 + 46, 400, 45);
			textFieldExpPIDOrProdSpec.setMaximumLength(20);
			textFieldExpPIDOrProdSpec.setColumns(20);
		}
		return textFieldExpPIDOrProdSpec;
	}
	
	public UpperCaseFieldBean getTextFieldLotNumber() {
		if (textFieldLotNumber == null) {
			textFieldLotNumber = new UpperCaseFieldBean();
			textFieldLotNumber.setName("TextFieldLotNumber");
			textFieldLotNumber.setFont(new java.awt.Font("dialog", 0, 36));
			textFieldLotNumber.setBounds(378, 96, 400, 40);
			textFieldLotNumber.setMaximumLength(20);
			textFieldLotNumber.setColumns(20);
		}
		return textFieldLotNumber;
	}
	
	public javax.swing.JLabel getLabelAfOnSeqNumValue() {
		if (labelAfOnSeqNumValue == null) {
			labelAfOnSeqNumValue = new javax.swing.JLabel();
			labelAfOnSeqNumValue.setName("TextFieldAfOnSeqNo");
			labelAfOnSeqNumValue.setFont(new java.awt.Font("dialog", 0, 65));
			labelAfOnSeqNumValue.setText("-----");
			Rectangle refbounds = gettextFieldAfOnSeqNoRefrenceBounds();
			int fieldWith = viewProperty.isShowProductSubid() ? refbounds.width *7/10 : refbounds.width;
			labelAfOnSeqNumValue.setBounds(refbounds.x+460, refbounds.y-10 , fieldWith, refbounds.height+15);
			labelAfOnSeqNumValue.setVisible(false);
		}
		return labelAfOnSeqNumValue;
	}
	
	public JButton getJButtonProductId()
	{
		if (ivjJButtonProductId == null)
		{
			try
			{
				ivjJButtonProductId = new javax.swing.JButton();
				ivjJButtonProductId.setName("JButtonProductId");
				ivjJButtonProductId.setText(viewProperty.getProductIdLabel());
				ivjJButtonProductId.setFont(new java.awt.Font("dialog", 0, 18));
				ivjJButtonProductId.setIconTextGap(40);

				ivjJButtonProductId.setBounds(viewProperty.getProductIdButtonX(), viewProperty.getProductIdButtonY(),
						viewProperty.getProductIdButtonWidth(), viewProperty.getProductIdButtonHeight());


				ivjJButtonProductId.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
				ivjJButtonProductId.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc){
				handleException(ivjExc);
			}
		}
		return ivjJButtonProductId;
	}
	public JLabel getLabelProdId() {
		if (prodIdLabel == null) {
			prodIdLabel = new JLabel();
			prodIdLabel.setText("JLabel");
			prodIdLabel.setName("JLabelEngineSN");
			prodIdLabel.setFont(new java.awt.Font("dialog", 0, 18));
			prodIdLabel.setIconTextGap(40);
			prodIdLabel.setBounds(184, 25, 185, 47);
			prodIdLabel.setText(viewProperty.getProductIdLabel());
			prodIdLabel.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			prodIdLabel.setForeground(java.awt.Color.black);
		}
		return prodIdLabel;
	}

	public UpperCaseFieldBean getTextFieldProdId()
	{
		if (textFieldProdId == null) {
			textFieldProdId = new UpperCaseFieldBean();
			textFieldProdId.setName("JTextFieldProductID");
			textFieldProdId.setFont(new java.awt.Font("dialog", 0, 36));
			textFieldProdId.setText("");
			textFieldProdId.setFixedLength(true);
			
			//2016-01-13 - BAK - Increase max length by 1 if Remove "I" enabled and product type is Frame			
			int maxLength = viewProperty.getMaxProductSnLength();
			if (getClientContext().isRemoveIEnabled() && getClientContext().getProperty().getProductType().equalsIgnoreCase(ProductType.FRAME.toString())) {
				maxLength = viewProperty.getMaxProductSnLength() + 1;
			}						
			
			textFieldProdId.setMaximumLength(maxLength);
			textFieldProdId.setColumns(maxLength);
			textFieldProdId.setBounds(378, 25, 450, 45);
			textFieldProdId.setSelectionColor(new Color(204, 204, 255));
			textFieldProdId.setColor(Color.blue);
			textFieldProdId.setBackground(Color.blue);
		}
		return textFieldProdId;
	}
	
	public JLabel getLabelExpPIDOrProdSpec() {
		if (labelExpPidOrProdSpec == null) {
			labelExpPidOrProdSpec = new javax.swing.JLabel();
			labelExpPidOrProdSpec.setName("LabelExpProdSpec");
			labelExpPidOrProdSpec.setFont(new java.awt.Font("dialog", 0, 18));
			labelExpPidOrProdSpec.setText("");
			labelExpPidOrProdSpec.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			Rectangle refbounds = getLabelExpPidOrProdSpecRefrenceBounds();
			labelExpPidOrProdSpec.setBounds(refbounds.x, refbounds.y + 46, refbounds.width, refbounds.height);
			labelExpPidOrProdSpec.setForeground(java.awt.Color.black);
		}
		return labelExpPidOrProdSpec;
	}
	
	public JLabel getLabelLotNumber() {
		if (labelLotNumber == null) {
			labelLotNumber = new javax.swing.JLabel();
			labelLotNumber.setName("LabelExpProdSpec");
			labelLotNumber.setFont(new java.awt.Font("dialog", 0, 18));
			labelLotNumber.setText("");
			labelLotNumber.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			Rectangle refbounds = getLabelLotNumberRefrenceBounds();
			labelLotNumber.setBounds(refbounds.x, refbounds.y + 46, refbounds.width, refbounds.height);
			labelLotNumber.setForeground(java.awt.Color.black);
		}
		return labelLotNumber;
	}

	private Rectangle getLabelLotNumberRefrenceBounds() {
		
		return getLabelProdId().getBounds();
	}

	public JLabel getLabelAfOnSeqNum() {
		if (labelAfOnSeqNum == null) {
			labelAfOnSeqNum = new javax.swing.JLabel();
			labelAfOnSeqNum.setName("LabelAfOnSeq");
			labelAfOnSeqNum.setFont(new java.awt.Font("dialog", Font.BOLD, 25));		
			labelAfOnSeqNum.setText("");
			labelAfOnSeqNum.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			Rectangle refbounds = getLabelAfOnSeqNoBounds();
			labelAfOnSeqNum.setBounds(refbounds.x+440, refbounds.y-10, refbounds.width-250, refbounds.height-5);
			labelAfOnSeqNum.setForeground(java.awt.Color.black);
		
		}
		return labelAfOnSeqNum;
	}

	protected ClientContext getClientContext() {
		return DataCollectionController.getInstance().getClientContext();
	}
	
	public TablePane getPreviouslyScannedTable() {
		previouslyScannedTable= new TablePane("");
		previouslyScannedTable.getTable().setRowHeight(50);
		previouslyScannedTable.getTable().setFont(new java.awt.Font("dialog", Font.PLAIN, 20));
		previouslyScannedTable.getTable().getTableHeader().setPreferredSize(new Dimension(250,50));
		previouslyScannedTable.getTable().getTableHeader().setFont(new java.awt.Font("dialog", Font.BOLD, 20));
		previouslyScannedTableModel = new AssemblyOnTableModel(previouslyScannedTable.getTable(),new ArrayList<AssemblyOnModel>(), new String[] {"Sequence", "VIN", "MTOCI","KD Lot","Comments"},false,true);
		
		return previouslyScannedTable;
	}
	
	protected Rectangle gettextFieldAfOnSeqNoRefrenceBounds(){
		return getTextFieldExpPidOrProdSpec().getBounds();
	}
	
		
	protected Rectangle getLabelAfOnSeqNoBounds(){
		return getTextFieldProdId().getBounds();
	}
	protected Rectangle getLabelExpPidOrProdSpecRefrenceBounds() {
		return getLabelProdId().getBounds();
	}
	
	public JLabel getOperationModeLabel() {
		if (operationModeLabel == null) {
			operationModeLabel = new javax.swing.JLabel("Auto Mode");
			operationModeLabel.setName("LabelOperationMode");
			operationModeLabel.setSize(40, 45);
			operationModeLabel.setFont(new java.awt.Font("dialog", Font.BOLD, 40));		
			operationModeLabel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			operationModeLabel.setForeground(java.awt.Color.BLUE);

		}
		return operationModeLabel;
	}
		
	
	public AssemblyOnTableModel getPreviouslyScannedTableModel() {
		return previouslyScannedTableModel;
	}
	
	public String getProdSpecLabel() {
		return viewProperty.getProdSpecLabel();
	}

	public String getPAOffSeqNumLabel() {
		return viewProperty.getAfOnSeqNumLabel();
	}

	@Override
	protected int getTorquePositionY(int row, int height, int gap) {
		return 0;
	}
	public void setProductInputFocused() {
	
	}

	public String getLotNumberLabel() {
		return viewProperty.getLotNumberLabel();
	}
	
	
}
