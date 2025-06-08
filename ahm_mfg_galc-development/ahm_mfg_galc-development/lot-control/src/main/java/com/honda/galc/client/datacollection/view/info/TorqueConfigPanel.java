package com.honda.galc.client.datacollection.view.info;
/**
 * 
 * <h3>TorqueConfigPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> TorqueConfigPanel </p>
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
 * <TD>Meghana G</TD>
 * <TD>Mar 8, 2011</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD>Modified the code to add logic to refresh device details and other panels.</TD>
 * </TR>  
 * <TR>
 * <TD>Meghana G</TD>
 * <TD>Mar 22, 2011</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD>Modified the code to display device status according to the device connection status.</TD>
 * </TR>  
 * </TABLE>
 *   
 * @author Meghana Ghanekar
 * Feb 3, 2011
 *
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.honda.galc.client.common.component.LabeledLabelPanel;
import com.honda.galc.client.common.component.PolygonButton;
import com.honda.galc.client.common.data.PartDetailsTableModel;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.dao.product.DevicePartDetailsDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.product.DevicePartDetails;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.service.ServiceFactory;


public class TorqueConfigPanel  extends JPanel implements ActionListener,IConfigurableDevice {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel statusPanel;
	private JPanel mtocPanel;
	private PolygonButton comStatusButton;
	private LabeledLabelPanel iPAddressPanel;
	private LabeledLabelPanel pSetPanel;
	private LabeledLabelPanel messagePanel;
	private LabeledComboBox yearComboBox;
	private LabeledComboBox modelCodeComboBox;
	private LabeledComboBox typeComboBox;
	private LabeledComboBox optionComboBox;
	private LabeledComboBox intColorComboBox;
	private LabeledComboBox extColorComboBox;
	private JButton selectButton;
	private ClientContext context;
	private TerminalPropertyBean property;
	private TorqueSocketDevice device;
	String instructionCode = null;
	private List<LotControlRule> rules = new ArrayList<LotControlRule>();
	private LotControlRule currentRule = null;
	private PartDetailsPanel partDetailsPanel;

	public TorqueConfigPanel(TorqueSocketDevice device, ClientContext context) {
		super();
		this.device = device;
		this.context = context;
		this.property = context.getProperty();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setName("Lot Control Client Torque Configuration");
		instructionCode = device.getCurrentInstructionCode();
		fetchallRules();
		initialize();
	}

	private void fetchallRules() {
		LotControlRuleDao dao = ServiceFactory.getDao(LotControlRuleDao.class);
		rules = dao.findAllForRule(context.getProcessPointId());	
	}

	private void initialize() {
		createComponents();
		initConnections();	
		loadInitialData();
	}

	private void createComponents() {

		add(getTopPanel());
		add(new JPanel());
		if(rules.size() > 0){
			add(getMTOCPanel());
			add(getPartDetailsPanel());
		}else{
			add(getNORulesDefinedPanel());
		}
	}

	private void initConnections() {
		getSelectButton().addActionListener(this);
	}

	private JPanel getTopPanel(){
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Device Details"));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraint = getConstraint(0,0,GridBagConstraints.RELATIVE,0.5);
		constraint.anchor = GridBagConstraints.NORTH;
		//add status panel to the top
		panel.add(getStatusPanel(),constraint);
		constraint = getConstraint(1, 0, GridBagConstraints.REMAINDER,0.5);
		//add ipAddress panel
		panel.add(getIPAddressPanel(),constraint);
		constraint = getConstraint(0,2, GridBagConstraints.RELATIVE,0.5);
		// add pSetPanel to next line
		panel.add(getPSetPanel(),constraint);   
		// get message panel
		constraint = getConstraint(1,2, GridBagConstraints.REMAINDER,0.5);
		panel.add(getMessagePanel(),constraint); 
		return panel;
	}

	private JPanel getNORulesDefinedPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Select MTOC"));
		panel.add(new JLabel("No Rules Defined for this Lot Control Station"));
		return panel;
	}

	private JPanel getMTOCPanel(){
		mtocPanel = new JPanel();
		mtocPanel.setBorder(new TitledBorder("Select MTOC"));
		mtocPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getConstraint(GridBagConstraints.RELATIVE,0,1,0.5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		mtocPanel.add(getYearComboBox(),gbc);
		gbc.gridx = 1;
		mtocPanel.add(getModelCodeComboBox(),gbc);
		gbc.gridx = 2;
		mtocPanel.add(getTypeComboBox(),gbc);
		gbc.gridx = 3;
		mtocPanel.add(getOptionComboBox(),gbc);
		gbc.gridx = 4;
		mtocPanel.add(getIntColorComboBox(),gbc);
		gbc.gridx = 5;
		mtocPanel.add(getExtColorComboBox(),gbc);
		gbc.gridx = 6;
		mtocPanel.add(new JPanel().add(getSelectButton()),gbc);
		return mtocPanel;
	}

	private GridBagConstraints getConstraint(int gridx, int gridy, int gridwidth, double weightx) {
		GridBagConstraints c = new GridBagConstraints();		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 0, 10);
		c.weightx =weightx;

		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;	
		c.gridheight = 1;
		return c;
	}

	private JPanel getStatusPanel(){
		boolean status = device.isConnected();
		statusPanel = new JPanel();
		statusPanel.setLayout(new BorderLayout());
		statusPanel.setPreferredSize(new Dimension(200, 25));
		comStatusButton = new PolygonButton();
		comStatusButton.setFont(new Font("dialog", 0, 12));
		comStatusButton.setPreferredSize(new Dimension(40,20));
		comStatusButton.setEnabled(false);
		if(status){
			comStatusButton.setText("ON");
		}else{
			comStatusButton.setText("OFF");
		}
		comStatusButton.setComStatus(status);
		comStatusButton.setBorderPainted(false);
		JLabel statusLabel = new JLabel("Current Status");
		statusLabel.setPreferredSize(new Dimension(120, 25));
		statusPanel.add(statusLabel,BorderLayout.WEST);
		statusPanel.add(comStatusButton,BorderLayout.CENTER);
		return statusPanel;
	}

	private LabeledLabelPanel getIPAddressPanel() {
		if(iPAddressPanel == null){
			iPAddressPanel = new LabeledLabelPanel("I.P.Address", device.getHostName());
		}
		return iPAddressPanel;
	}

	public LabeledLabelPanel getPSetPanel() {
		if(pSetPanel == null){
			pSetPanel = new LabeledLabelPanel("Current P Set ", instructionCode == null?"0":instructionCode);
		}
		return pSetPanel;
	}

	private LabeledLabelPanel getMessagePanel() {
		if(messagePanel == null){
			messagePanel = new LabeledLabelPanel("Last Ping Time", new Date(device.getLastPingReply()).toString());
		}
		return messagePanel;
	}

	private LabeledComboBox getYearComboBox() {
		if (yearComboBox == null) {
			yearComboBox = new LabeledComboBox("Year");
			yearComboBox.getComponent().setPreferredSize(new Dimension(150,25));
			yearComboBox.getComponent().addActionListener(this);
		}
		return yearComboBox;
	}

	private LabeledComboBox getModelCodeComboBox() {
		if (modelCodeComboBox == null) {
			modelCodeComboBox = new LabeledComboBox("Model");
			modelCodeComboBox.getComponent().setPreferredSize(new Dimension(150,25));
			modelCodeComboBox.getComponent().addActionListener(this);
		}
		return modelCodeComboBox;
	}

	private LabeledComboBox getTypeComboBox() {
		if (typeComboBox == null) {
			typeComboBox = new LabeledComboBox("Type");
			typeComboBox.getComponent().setPreferredSize(new Dimension(150,25));
		}
		return typeComboBox;
	}

	private LabeledComboBox getOptionComboBox() {
		if (optionComboBox == null) {
			optionComboBox = new LabeledComboBox("Option");
			optionComboBox.getComponent().setPreferredSize(new Dimension(150,25));
		}
		return optionComboBox;
	}

	private LabeledComboBox getIntColorComboBox() {
		if (intColorComboBox == null) {
			intColorComboBox = new LabeledComboBox("IntColor");
			intColorComboBox.getComponent().setPreferredSize(new Dimension(150,25));
		}
		return intColorComboBox;
	}

	private LabeledComboBox getExtColorComboBox() {
		if (extColorComboBox == null) {
			extColorComboBox = new LabeledComboBox("ExtColor");
			extColorComboBox.getComponent().setPreferredSize(new Dimension(150,25));
		}
		return extColorComboBox;
	}

	public JButton getSelectButton() {
		if (selectButton == null) {
			selectButton = new JButton("Select");
			selectButton.setMaximumSize(new Dimension(120,20));
		}
		return selectButton;
	}


	private PartDetailsPanel getPartDetailsPanel() {
		if (partDetailsPanel == null) {
			partDetailsPanel = new PartDetailsPanel(this,context);
		}
		return partDetailsPanel;
	}

	public void loadData() {
		List<String> yearList = new ArrayList<String>();
		List<String> modelCodeList = new ArrayList<String>();	
		List<String> typeCodeList = new ArrayList<String>();
		List<String> optionCodeList = new ArrayList<String>();
		List<String> intColorList = new ArrayList<String>();
		List<String> extColorList = new ArrayList<String>();

		for(LotControlRule rule : rules){
			if(!yearList.contains(rule.getId().getModelYearCode())){
				yearList.add(rule.getId().getModelYearCode());
			}
			if(!modelCodeList.contains(rule.getId().getModelCode())){
				modelCodeList.add(rule.getId().getModelCode());
			}
			if(!typeCodeList.contains(rule.getId().getModelTypeCode())){
				typeCodeList.add(rule.getId().getModelTypeCode());
			}
			if(!optionCodeList.contains(rule.getId().getModelOptionCode())){
				optionCodeList.add(rule.getId().getModelOptionCode());
			}
			if(!intColorList.contains(rule.getId().getIntColorCode())){
				intColorList.add(rule.getId().getIntColorCode());
			}
			if(!extColorList.contains(rule.getId().getExtColorCode())){
				extColorList.add(rule.getId().getExtColorCode());
			}
		}
		getYearComboBox().setModel(new ComboBoxModel<String>(yearList), 0);
		getModelCodeComboBox().setModel(new ComboBoxModel<String>(modelCodeList), 0);
		getTypeComboBox().setModel(new ComboBoxModel<String>(typeCodeList), 0);
		getOptionComboBox().setModel(new ComboBoxModel<String>(optionCodeList), 0);
		getIntColorComboBox().setModel(new ComboBoxModel<String>(intColorList), 0);
		getExtColorComboBox().setModel(new ComboBoxModel<String>(extColorList), 0);
	}

	public void loadInitialData() {
		List<String> yearList = new ArrayList<String>();
		for(LotControlRule rule : rules){
			if(!yearList.contains(rule.getId().getModelYearCode())){
				yearList.add(rule.getId().getModelYearCode());
			}
		}
		getYearComboBox().setModel(new ComboBoxModel<String>(yearList), 0);

	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getSelectButton()) {
			mtocSelect(e);
		}else if(e.getSource().equals(getYearComboBox().getComponent())){
			modelYearChanged();
		}else if(e.getSource().equals(getModelCodeComboBox().getComponent())){
			modelCodeChanged();
		}

	}

	private void modelCodeChanged() {
		String modelYear = getYearComboBox().getComponent().getSelectedItem().toString();
		String modelCode = getModelCodeComboBox().getComponent().getSelectedItem().toString();
		List<String> typeCodeList = new ArrayList<String>();
		List<String> optionCodeList = new ArrayList<String>();
		List<String> intColorList = new ArrayList<String>();
		List<String> extColorList = new ArrayList<String>();
		for(LotControlRule rule : rules){
			if((rule.getId().getModelYearCode().equalsIgnoreCase(modelYear)) &&
					(rule.getId().getModelCode().equalsIgnoreCase(modelCode))){
				if(!typeCodeList.contains(rule.getId().getModelTypeCode())){
					typeCodeList.add(rule.getId().getModelTypeCode());
				}
				if(!optionCodeList.contains(rule.getId().getModelOptionCode())){
					optionCodeList.add(rule.getId().getModelOptionCode());
				}
				if(!intColorList.contains(rule.getId().getIntColorCode())){
					intColorList.add(rule.getId().getIntColorCode());
				}
				if(!extColorList.contains(rule.getId().getExtColorCode())){
					extColorList.add(rule.getId().getExtColorCode());
				}
			}
		}
		getTypeComboBox().setModel(new ComboBoxModel<String>(typeCodeList), 0);
		getOptionComboBox().setModel(new ComboBoxModel<String>(optionCodeList), 0);
		getIntColorComboBox().setModel(new ComboBoxModel<String>(intColorList), 0);
		getExtColorComboBox().setModel(new ComboBoxModel<String>(extColorList), 0);
	}

	private void modelYearChanged() {
		String modelYear = getYearComboBox().getComponent().getSelectedItem().toString();
		List<String> modelCodeList = new ArrayList<String>();	
		for(LotControlRule rule : rules){
			if(rule.getId().getModelYearCode().equalsIgnoreCase(modelYear)){
				if(!modelCodeList.contains(rule.getId().getModelCode())){
					modelCodeList.add(rule.getId().getModelCode());
				}
			}
		}
		getModelCodeComboBox().setModel(new ComboBoxModel<String>(modelCodeList), 0);		
	}

	private void mtocSelect(ActionEvent e) {
	    getPartDetailsPanel().refreshPanel();
		String modelYear = (String) getYearComboBox().getComponent().getSelectedItem();
		String modelCode = (String) getModelCodeComboBox().getComponent().getSelectedItem();
		String modelType = (String) getTypeComboBox().getComponent().getSelectedItem();
		String modelOption = (String) getOptionComboBox().getComponent().getSelectedItem();
		String intColor = (String) getIntColorComboBox().getComponent().getSelectedItem();
		String extColor = (String) getExtColorComboBox().getComponent().getSelectedItem();

		for(LotControlRule rule : rules){
			LotControlRuleId ruleId = rule.getId();
			if((ruleId.getModelYearCode().equalsIgnoreCase(modelYear)) &&
					(ruleId.getModelCode().equalsIgnoreCase(modelCode))&&
					(ruleId.getModelTypeCode().equalsIgnoreCase(modelType))&&
					(ruleId.getModelOptionCode().equalsIgnoreCase(modelOption))&&
					(ruleId.getIntColorCode().equalsIgnoreCase(intColor)) &&
					(ruleId.getExtColorCode().equalsIgnoreCase(extColor))){
				currentRule = rule;
				break;
			}else{
				currentRule = null;
			}
		}
		new PartDetailsTableModel(getPartList(), getPartDetailsPanel().getPartDetailsTablePanel().getTable());
	}

	private List<DevicePartDetails> getPartList() {
		List <DevicePartDetails> partsList = new ArrayList<DevicePartDetails>();
		if(currentRule !=null){
			DevicePartDetailsDao partsDao = ServiceFactory.getDao(DevicePartDetailsDao.class);
			partsList = partsDao.findAllPartDetailsByLotControlRule(currentRule);
		}
		return partsList;
	}

	public TorqueSocketDevice getDevice() {
		return device;
	}

	public void releaseResources(){
		if(partDetailsPanel != null){
			partDetailsPanel.restoreDeviceState();
		}
	}
	public List<ComponentProperty> getChangedProperties() {
		return new ArrayList<ComponentProperty>();
	}

	public void refresh() {
		refreshDeviceDetails();
		if(partDetailsPanel != null){
			partDetailsPanel.activateTestMode();
		}
	}

	private void refreshDeviceDetails() {
		if(device!=null){
			if(comStatusButton!=null){
				boolean status = device.isConnected();
				if(status){
					comStatusButton.setText("ON");
				}else{
					comStatusButton.setText("OFF");
				}
				comStatusButton.setComStatus(status);
			}
			if(iPAddressPanel != null){
				iPAddressPanel.getValueLabel().setText(device.getHostName());
			}
			if(pSetPanel != null){
				String code = device.getCurrentInstructionCode();
				if(code == null){
					code = partDetailsPanel.getInstructionCode();
				}
				if(code == null) {
					code = "0";
				}
				pSetPanel.getValueLabel().setText(code);
			}
			if(messagePanel != null){
				messagePanel.getValueLabel().setText(new Date(device.getLastPingReply()).toString());
			}
		}

	}
}

