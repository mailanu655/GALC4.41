package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.honda.galc.client.teamleader.model.ScheduleTableModel;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.dao.product.ScheduleDao;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * <h3>ScheduleMaintenancePanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ScheduleMaintenancePanel description </p>
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
 * Mar 2, 2011
 *
 */

public abstract class ScheduleMaintenancePanel extends TabbedPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	protected JPanel buttonsPanel = null;
	protected JButton saveButton = null;
	protected JButton queryButton = null;
	protected JButton clearButton = null;
	protected DepartmentSelectionPanel departmentSelectionPanel;
	protected TablePane schedulePanel;
	private JPanel selectionPanel;
	protected JPanel datePanel;
	protected ScheduleTableModel model;
	protected ScheduleDao dao;
	
	protected HashSet<String> plantCodes = new HashSet<String>();
	protected HashSet<String> lineNos = new HashSet<String>();
	protected HashSet<String> processLocations = new HashSet<String>();
	protected JPanel prodTotalsPanel;
	protected LabeledTextField dayProdTime,shiftOneProdTime, shiftTwoProdTime,shiftThreeProdTime;
	protected LabeledTextField dayProdCap,shiftOneProdCap, shiftTwoProdCap,shiftThreeProdCap;
	protected LabeledTextField dayProdCapOn,shiftOneProdCapOn, shiftTwoProdCapOn,shiftThreeProdCapOn;
	
	private boolean editEnabled = true;
	
	protected abstract void doSave();
	protected abstract void doQuery();
	
	public ScheduleMaintenancePanel(String screenName, int keyEvent) {
		super(screenName, keyEvent);
		
		initComponents();
	}

	protected void initComponents() {

		setLayout(new BorderLayout());

		add(getSelectionPanel(),BorderLayout.NORTH);
		add(getSchedulePanel(),BorderLayout.CENTER);
		
		addListeners();

	}
	
	private void addListeners() {
		getQueryButton().addActionListener(this);
		getSaveButton().addActionListener(this);
		getClearButton().addActionListener(this);
		
	}
	// ========= Getters & Setters ============
	protected JPanel getSelectionPanel() {
		if(selectionPanel == null){
			selectionPanel = new JPanel();
			selectionPanel.setLayout(new MigLayout());
			
			selectionPanel.add(getDepartmentSelectionPanel(), "width 190:190:200");
			selectionPanel.add(getDatePanel(),"aligny bottom");
			selectionPanel.add(getButtonsPanel(),"width 265:400:400");
			selectionPanel.add(getProdTotalsPanel(), "push, al right, gapx 10");
		}
		return selectionPanel;
	}
	
	protected JPanel getDatePanel() {
		return new JPanel();
	}

	public TablePane getSchedulePanel() {
		if(schedulePanel == null){
			schedulePanel = new TablePane("Schedule");
			schedulePanel.getTable().setRowHeight(16);
		}

		return schedulePanel;
	}
	
	public JPanel getButtonsPanel() {
		if (this.buttonsPanel == null) {
			this.buttonsPanel = new JPanel(new MigLayout("center"));
			this.buttonsPanel.add(getQueryButton(),"width n:100:100");
			this.buttonsPanel.add(getClearButton(),"width n:100:100");
			this.buttonsPanel.add(getSaveButton(),"width n:100:100");
		}
		return this.buttonsPanel;
	}
	
	public JButton getSaveButton() {
		if (saveButton == null)
			saveButton = this.getButton("Save", false);
		return saveButton;
	}
	
	public JButton getQueryButton() {
		if (queryButton == null)
			queryButton = this.getButton("Query", true);
		return queryButton;
	}
	
	public JButton getClearButton() {
		if (clearButton == null)
			clearButton = this.getButton("Clear", true);
		return clearButton;
	}
	
	private JButton getButton(String title, Boolean enable) {
		JButton button = new JButton(title);
		button.setName(title + "Button");
		button.setFont(Fonts.DIALOG_PLAIN_18);
		button.setEnabled(enable);
		return button;
	}
	
	protected JPanel getProdTotalsPanel() {
		if (this.prodTotalsPanel == null) {
			String cellWidth = "width 55:80:80";
			this.prodTotalsPanel = new JPanel(new MigLayout("insets 0 0 0 0,gap 0,fill"));
			this.prodTotalsPanel.add(new JLabel("Prod. time: "),"alignx right, aligny bottom, gap right 5");
			this.prodTotalsPanel.add(this.dayProdTime = this.getTextField("Day",true),cellWidth);
			this.prodTotalsPanel.add(this.shiftOneProdTime = this.getTextField("Shift 1",true),cellWidth);
			this.prodTotalsPanel.add(this.shiftTwoProdTime = this.getTextField("Shift 2",true),cellWidth);
			this.prodTotalsPanel.add(this.shiftThreeProdTime = this.getTextField("Shift 3",true),cellWidth + ",wrap");
			this.prodTotalsPanel.add(new JLabel("Prod. capacity: "),"alignx right, aligny bottom, gap right 5");
			this.prodTotalsPanel.add(this.dayProdCap = this.getTextField("Day",false),cellWidth);
			this.prodTotalsPanel.add(this.shiftOneProdCap = this.getTextField("Shift 1",false),cellWidth);
			this.prodTotalsPanel.add(this.shiftTwoProdCap = this.getTextField("Shift 2",false),cellWidth);
			this.prodTotalsPanel.add(this.shiftThreeProdCap = this.getTextField("Shift 3",false),cellWidth);
		}
		return this.prodTotalsPanel;
	}
	
	protected void clearProdTotalsPanel() {
		for (Component component : this.getProdTotalsPanel().getComponents())
			if (component instanceof LabeledTextField) ((LabeledTextField)component).getComponent().setText("");
	}
	
	private LabeledTextField getTextField(String title, Boolean showLabel) {
		LabeledTextField field = new LabeledTextField(title,false);
		if (!showLabel) field.remove(field.getLabel());
		field.getComponent().setEditable(false);
		field.getComponent().setHorizontalAlignment(JTextField.CENTER);
		field.getLabel().setHorizontalAlignment(JTextField.CENTER);
		field.getComponent().setFont(Fonts.DIALOG_BOLD_12);
		field.getComponent().setBackground(Color.WHITE);
		((BorderLayout)field.getLayout()).setVgap(0);
		field.setBorder(null);
		return field;
	}
	
	
	public DepartmentSelectionPanel getDepartmentSelectionPanel() {
		if(departmentSelectionPanel == null) {
			departmentSelectionPanel = new DepartmentSelectionPanel();
			departmentSelectionPanel.setBorder(new TitledBorder("Location"));
		}
		return departmentSelectionPanel;
	}
	
	public void dataChanged(boolean changed){
		getSaveButton().setEnabled(this.editEnabled );
		getQueryButton().setEnabled(!changed);
		departmentSelectionPanel.getPlantComboBox().getComponent().setEnabled(!changed);
		departmentSelectionPanel.getLineComboBox().getComponent().setEnabled(!changed);
		departmentSelectionPanel.getDepartmentComboBox().getComponent().setEnabled(!changed);
	}
	
	public void setEditEnabled(boolean editEnabled) {
		this.editEnabled = editEnabled;
		if(schedulePanel != null) {
			schedulePanel.getTable().repaint();
		}
	}
	
	public void setErrorMessage(String errorMessage){
		super.setErrorMessage(errorMessage);
	}

	// ============tab ============
	@Override
	public void onTabSelected() {
		// TODO Auto-generated method stub
		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getQueryButton()){
			doQuery();
			setEditEnabled(true);
		}
		else if (e.getSource() == getClearButton()) {
			doClear();
		}
		else if (e.getSource() == getSaveButton()) {
			doSave();
		}
	}

	private void doClear() {
		if (model.isTableChanged() && !model.isClearPending()) {
			model.setClearPending(true);
			setErrorMessage("Table has unsaved changes. Press <Clear> to force.");
			return;
		}
		model.setClearPending(false);
		clearScreen();
		
	}
	
	public void clearScreen() {
		
		setErrorMessage("");
		model.refresh( new ArrayList<Object[]>());
		this.clearProdTotalsPanel();
		// Reset buttons
		dataChanged(false);
	}
	
	protected void getProcessInfo() {
		List<Object[]> processInfo = dao.getProcessInfo();
		
		for(Object[]info : processInfo){
			plantCodes.add((String)info[0]);
			lineNos.add((String)info[1]);
			processLocations.add((String)info[2]);
		}
		
		departmentSelectionPanel.getPlantComboBox().setModel(new ComboBoxModel<String>(new ArrayList<String>(plantCodes)), 0);
		departmentSelectionPanel.getLineComboBox().setModel(new ComboBoxModel<String>(new ArrayList<String>(lineNos)), 0);
		departmentSelectionPanel.getDepartmentComboBox().setModel(new ComboBoxModel<String>(new ArrayList<String>(processLocations)), 0);
		
		
	}

}
