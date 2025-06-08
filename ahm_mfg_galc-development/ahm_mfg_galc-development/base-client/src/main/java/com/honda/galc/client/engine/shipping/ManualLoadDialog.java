package com.honda.galc.client.engine.shipping;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.enumtype.ShippingQuorumStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;

/**
 * 
 * 
 * <h3>ManualLoadDialog Class description</h3>
 * <p> ManualLoadDialog description </p>
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
 * @author Jeffray Huang<br>
 * Sep 29, 2014
 *
 *
 */
public class ManualLoadDialog extends JDialog implements ActionListener, ListSelectionListener{

	private static final long serialVersionUID = 1L;
	
	private LabeledUpperCaseTextField einTextField;
	private ObjectTablePane<ShippingQuorum> quorumTablePane;
	private ObjectTablePane<ShippingQuorumDetail> quorumDetailTablePane;
	private ObjectTablePane<ShippingQuorumDetail> engineTablePane;
	
	private JButton assignButton;
	private JButton resetButton;
	private JButton doneButton;
	private JButton moveButton;
	
	private JLabel messageLabel;
	
	private EngineShippingModel model;
	private EngineShippingController controller;
	
	private boolean isOk = false;
	
	public ManualLoadDialog(EngineShippingController controller) {
		super(controller.getView().getMainWindow(),true);
		
		this.controller = controller;
		this.model = controller.getModel();
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	//	setResizable(false);
		setTitle("Manual Load Engine");
		setSize(700, 400);
		setLocationRelativeTo(controller.getView().getMainWindow());
		
		initComponents();
		mapActions();
		loadData();
	}
	
	public boolean isOk() {
		return isOk;
	}
	
	private void initComponents() {
		einTextField = new LabeledUpperCaseTextField("Enter EIN:");
		einTextField.setFont(Fonts.DIALOG_BOLD_26);
		messageLabel = new JLabel();
		messageLabel.setFont(Fonts.DIALOG_BOLD_16);
		
		JPanel panel = new JPanel(new MigLayout("insets 20 10 10 10", "[grow,fill]"));
		panel.add(einTextField,"gapleft 100,gapright 100, span,wrap");
		panel.add(quorumTablePane = createQuorumTablePane(),"width 120:120:120");
		panel.add(quorumDetailTablePane = createQuorumDetailTablePane(),"span 3");
		panel.add(engineTablePane = createEngineTablePane(),"width 120:120:120,wrap");
		panel.add(createButtonPanel(),"span,wrap");
		panel.add(messageLabel,"gapleft 30,height 20:20:20,span");
		
		resetButtonClicked();
		
		setContentPane(panel);
	}

	private void mapActions() {
		einTextField.getComponent().addActionListener(this);
		assignButton.addActionListener(this);
		resetButton.addActionListener(this);
		doneButton.addActionListener(this);
		moveButton.addActionListener(this);
		quorumTablePane.addListSelectionListener(this);
	}

	private void loadData() {
		int selectionIndex = quorumTablePane.getTable().getSelectedRow();
		if(selectionIndex < 0) selectionIndex = 0;
		else quorumTablePane.clearSelection();
		quorumTablePane.reloadData(model.findAllManualLoadQuorums());
		quorumTablePane.getTable().getSelectionModel().setSelectionInterval(selectionIndex,selectionIndex);
	}
	
	private ObjectTablePane<ShippingQuorum> createQuorumTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Trailer #", "trailerNumber").put("Row", "trailerRow");
		
		ObjectTablePane<ShippingQuorum> tablePane = new ObjectTablePane<ShippingQuorum>(clumnMappings.get(),false);
		tablePane.setBorder(new TitledBorder("Quorum List"));
		configureTablePane(tablePane);
		return tablePane;
	}
	
	private ObjectTablePane<ShippingQuorumDetail> createQuorumDetailTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Seq", "quorumSeq")
			.put("KD Lot Number", "kdLot").put("YMTO","ymto").put("Engine Number","engineNumber");
		
		ObjectTablePane<ShippingQuorumDetail> tablePane = new ObjectTablePane<ShippingQuorumDetail>(clumnMappings.get(),false);
		
		tablePane.setBorder(new TitledBorder("Quorum Detail List"));
		configureTablePane(tablePane);
		return tablePane;
	}
	
	private ObjectTablePane<ShippingQuorumDetail> createEngineTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Engine Number","engineNumber");
		
		ObjectTablePane<ShippingQuorumDetail> tablePane = new ObjectTablePane<ShippingQuorumDetail>(clumnMappings.get(),false);
		
		tablePane.setBorder(new TitledBorder("Scanned Engine List"));
		configureTablePane(tablePane);
		return tablePane;
	}
	
	
	private void configureTablePane(ObjectTablePane<?> tablePane) {
		tablePane.getTable().setRowHeight(26);
		tablePane.getTable().setFont(Fonts.FONT_BOLD("sansserif",15));
		tablePane.setAlignment(JLabel.CENTER);
		tablePane.getTable().setSelectionBackground(Color.green);
		tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	private JPanel createButtonPanel(){
		assignButton = createButton("Assign");
		resetButton = createButton("Reset");
		doneButton = createButton("Done");
		moveButton = createButton("Move to Delayed");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(100));
		panel.add(assignButton);
		panel.add(Box.createHorizontalGlue());
		panel.add(resetButton);
		panel.add(Box.createHorizontalGlue());
		panel.add(moveButton);
		panel.add(Box.createHorizontalGlue());
		panel.add(doneButton);
		panel.add(Box.createHorizontalStrut(100));
		return panel;
	}
	
	private JButton createButton(String label) {
		JButton button = new JButton(label);
		button.setName(label);
		button.setMargin(new Insets(5,20,5,20));
		button.setFont(Fonts.DIALOG_BOLD_16);
		return button;
	}
	
	private void setErrorMessage(String errorMessage) {
		messageLabel.setForeground(Color.red);
		messageLabel.setText(errorMessage);
	}
	
	private void setMessage(String errorMessage) {
		messageLabel.setForeground(Color.black);
		messageLabel.setText(errorMessage);
	}
	
	private void clearMessage() {
		messageLabel.setText("");
	}
	
	
	public void actionPerformed(ActionEvent e) {
		clearMessage();
		try{
			if(e.getSource().equals(einTextField.getComponent())) einReceived();
			else if(e.getSource().equals(resetButton)) resetButtonClicked();
			else if(e.getSource().equals(assignButton)) assignButtonClicked();
			else if(e.getSource().equals(moveButton)) moveButtonClicked();
			else if(e.getSource().equals(doneButton)) doneButtonClicked();
		}catch(Exception ex) {
			setErrorMessage("Exception Occured: " + ex.getMessage());
		}
	}
	
	

	private void resetButtonClicked() {
		engineTablePane.reloadData(null);
		einTextField.getComponent().setText("");
		einTextField.getComponent().requestFocus();
		assignButton.setEnabled(false);
	}

	private void assignButtonClicked() {
		ShippingQuorum quorum = quorumTablePane.getSelectedItem();
		if(quorum == null) return;
		List<String> eins = new ArrayList<String>();
		for(int i = 0; i<quorum.getShippingQuorumDetails().size();i++) {
			eins.add(engineTablePane.getItems().get(i).getEngineNumber());
		}
		
		model.saveManualLoadEngines(quorum,eins);
		loadData();
		resetButtonClicked();
		isOk = true;
	}

	private void doneButtonClicked() {
		this.setVisible(false);
	}
	
	private void moveButtonClicked() {
		ShippingQuorum quorum = quorumTablePane.getSelectedItem();
		
		//TL override
		if(!controller.login()) return;
		
		model.removeEnginesFromQuorum(quorum);
		model.updateQuorumStatus(quorum, ShippingQuorumStatus.DELAYED);
		model.reloadActiveQuorums();
		controller.getView().reload();
		loadData();
		resetButtonClicked();
	}

	private void einReceived() {
		String ein = einTextField.getComponent().getText();
		String message = checkEngine(ein);
		einTextField.getComponent().selectAll();
		einTextField.getComponent().requestFocus();
		if(!StringUtils.isEmpty(message)) {
			setErrorMessage(message);
			return;
		}
		
		setMessage("Engine " + ein + " is scanned successfully");
		ShippingQuorumDetail quorumDetail = new ShippingQuorumDetail();
		quorumDetail.setEngineNumber(ein);
		engineTablePane.getItems().add(quorumDetail);
		engineTablePane.reloadData(engineTablePane.getItems());
		if(engineTablePane.getTable().getRowCount() >= quorumDetailTablePane.getTable().getRowCount())
			assignButton.setEnabled(true);
	}
	
	private String checkEngine(String ein) {
		
		if(!ProductNumberDef.EIN.isNumberValid(ein)) return  "Invalid Engine Number received";
		if(engineTablePane.getTable().getRowCount() >= quorumDetailTablePane.getTable().getRowCount())
			return "You already scanned full size of engines for this quorum!";
		
		Engine engine = model.findEngine(ein);
		if(engine == null) return "Engine does not exist!";
		
		if(isEngineScanned(ein)) return "Engine number has been scanned";
		
		if(!engine.isDirectPassStatus() && !engine.isRepairedStatus())
			return "Engine has defects, cannot be loaded";
		
		if(engine.getEngineFiringFlag() > 0) {
			boolean isTestFired = model.checkTestFireResults(engine);
			if(!isTestFired) return "Engine needs to be test fired";
		}
		
		String productSpecCode = quorumDetailTablePane.getItems().get(engineTablePane.getTable().getRowCount()).getYmto();
		if(!engine.getProductSpecCode().equalsIgnoreCase(productSpecCode))
				return "Product spec code " + engine.getProductSpecCode() + " does not match required : " + productSpecCode;
		
		if(engine.getAutoHoldStatus() == 1) return "Engine " + ein + " is on hold!";
		
		if(engine.getLastPassingProcessPointId().equals(model.getProcessPointId())) 
			return "Engine " + ein + " is shipped!";
		
		List<String> missingParts = model.checkRequiredParts(engine);
		if(!missingParts.isEmpty()) return "Engine has missing required parts : " + missingParts.get(0);	
		
		return null;
		
	}
	
	private boolean isEngineScanned(String ein) {
		for(ShippingQuorumDetail engine : engineTablePane.getItems()){
			if(ein.equalsIgnoreCase(engine.getEngineNumber())) return true;
		}
		return false;
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource().equals(quorumTablePane.getTable().getSelectionModel()))
			quorumSelected();
	}

	private void quorumSelected() {
		ShippingQuorum quorum = quorumTablePane.getSelectedItem();
		if(quorum != null){
			quorumDetailTablePane.reloadData(quorum.getShippingQuorumDetails());
			resetButtonClicked();
		}
	}

}
