package com.honda.galc.client.teamleader.frame;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.product.controller.listener.InputNumberChangeListener;
import com.honda.galc.client.product.validator.RegExpValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxState;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.property.EngineMissionAssignPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>EngineMissionAssignmentPanel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Nov 26, 2013
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date May 04, 2016
 */

/*
 * Functionality moved to com.honda.galc.client.frame.engine.assignment.EngineMissionAssignmentPanel
 */
@Deprecated
public class EngineMissionAssignmentPanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;
	public static final String ASSIGNMENT_ACTION_COMMAND = "ASSIGN";
	public static final String DEASSIGNMENT_ACTION_COMMAND = "DEASSIGN";
	public static final String ASSIGN_LABEL = "Assign";
	public static final String DEASSIGN_LABEL = "Deassign";
	public static final String ASSIGN_ENGINE_LABEL = "Assign EIN";
	public static final String ASSIGN_MISSION_LABEL = "Assign MSN";
	public static final String DEASSIGN_ENGINE_LABEL = "Deassign EIN";
	public static final String DEASSIGN_MISSION_LABEL = "Deassign MSN";

	private EngineMissionAssignmentController controller;

	private JTextField productIdTextField;
	private JButton resetButton;

	private JTextField productionLotTextField;
	private JTextField prodSpecCodeTextField;

	private JTextField enginePlantTextField;
	private JComboBox engineRequiredTypeComboBox;
	
	private JTextField engineTypeTextField;
	private JTextField einTextField;

	private JButton engineAssignButton;
	private JButton einResetButton;

	private JTextField missionRequiredTypeTextField;
	private JTextField missionTypeTextField;
	private JTextField missionSnTextField;

	private JButton missionAssignButton;
	private JButton missionSnResetButton;

	private JTextField plantTextField;
	private JTextField divisionTextField;
	private JTextField lineTextField;
	private JTextField processPointTextField;
	private JTextField timestampTextField;
	private EngineMissionAssignPropertyBean propertyBean=null;

	public EngineMissionAssignmentPanel(TabbedMainWindow mainWindow) {
		super("Engine/Mission Assignment", KeyEvent.VK_A, mainWindow);
		init();
		initView();
		mapActions();
		mapValidators();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getProductIdTextField().requestFocusInWindow();
			}
		});
	}

	// === initialization ===//
	protected void init() {
		this.controller = new EngineMissionAssignmentController(this);
		propertyBean=PropertyService.getPropertyBean(EngineMissionAssignPropertyBean.class, getApplicationId());	
	}

	protected void initView() {
		String productIdLabel = getMainWindow().getApplicationContext().getProductTypeData().getProductIdLabel();
		int inputLength = getMainWindow().getProductType().getProductIdLength();

		UiFactory factory = UiFactory.getInfoSmall();
		this.productIdTextField = UiFactory.getIdle().createTextField(inputLength, TextFieldState.EDIT);
		this.productIdTextField.setName("ProductInputField");
		this.resetButton = factory.createButton("Next Product");
		setName(getClass().getSimpleName());
		StringBuilder sb = new StringBuilder("10[]10[]10[]10");
		setLayout(new MigLayout("insets 10 5 0 5", sb.toString(), ""));
		add(UiFactory.getIdle().createLabel(productIdLabel));
		add(getProductIdTextField(), "width max");
		add(getResetButton(), "aligny center, width 160!, height 50!,wrap 5");
		add(createFunctionPanel(), String.format("span %s, width max, height max", getComponentCount()));
	}

	protected JPanel createFunctionPanel() {
		UiFactory factory = UiFactory.getInfoSmall();

		this.productionLotTextField = UiFactory.getInfo().createTextField(TextFieldState.DISABLED);
		this.prodSpecCodeTextField = UiFactory.getInfo().createTextField(TextFieldState.DISABLED);

		this.enginePlantTextField = UiFactory.getInfo().createTextField(TextFieldState.DISABLED);
		this.engineRequiredTypeComboBox = UiFactory.getInfo().createRequiredTypeComboBox(ComboBoxState.DISABLED);
		
		this.engineTypeTextField = UiFactory.getInfo().createTextField(TextFieldState.DISABLED);
		this.einTextField = UiFactory.getInfo().createTextField(17, TextFieldState.EDIT);
		this.einTextField.setName("EinInputField");
		
		this.engineAssignButton = factory.createButton(ASSIGN_ENGINE_LABEL);
		this.engineAssignButton.setActionCommand(ASSIGNMENT_ACTION_COMMAND);
		this.einResetButton = factory.createButton("Reset EIN");

		this.missionRequiredTypeTextField = UiFactory.getInfo().createTextField(TextFieldState.DISABLED);
		this.missionTypeTextField = UiFactory.getInfo().createTextField(22,TextFieldState.DISABLED);
		this.missionSnTextField = UiFactory.getInfo().createTextField(20, TextFieldState.DISABLED);

		this.missionAssignButton = factory.createButton(ASSIGN_MISSION_LABEL);
		this.missionAssignButton.setActionCommand(ASSIGNMENT_ACTION_COMMAND);
		this.missionSnResetButton = factory.createButton("Reset MSN");

		this.plantTextField = UiFactory.getInputSmall().createTextField(TextFieldState.DISABLED);
		this.divisionTextField = UiFactory.getInputSmall().createTextField(TextFieldState.DISABLED);
		this.lineTextField = UiFactory.getInputSmall().createTextField(TextFieldState.DISABLED);
		this.processPointTextField = UiFactory.getInputSmall().createTextField(TextFieldState.DISABLED);
		this.timestampTextField = UiFactory.getInputSmall().createTextField(TextFieldState.DISABLED);

		JPanel panel = new JPanel(new MigLayout("", "", ""));
		panel.setBorder(BorderFactory.createEtchedBorder());

		JPanel framePanel = new JPanel(new MigLayout("", "20[]10[max, fill]20[]10[max, fill]", "[center]"));
		framePanel.add(factory.createLabel("Prod Lot"), "alignx right");
		framePanel.add(getProductionLotTextField());
		framePanel.add(factory.createLabel("Type"), "alignx right");
		framePanel.add(getProdSpecCodeTextField());
		panel.add(framePanel, "wrap 20");

		String secLayout = "20[]10[max,fill]20[]10[250!, fill]10[150!,fill]";
		JPanel engPanel = new JPanel(new MigLayout("insets n n n n", secLayout));
		UiFactory.addSeparator(engPanel, "Engine");
		engPanel.add(factory.createLabel("Plant"), "alignx right");
		engPanel.add(getEnginePlantTextField());
		engPanel.add(factory.createLabel("Required Type"));
		engPanel.add(getEngineRequiredTypeComboBox());
		engPanel.add(getEngineAssignButton(), "aligny center, height 40!, wrap 10");

		engPanel.add(factory.createLabel("EIN"), "alignx right");
		engPanel.add(getEinTextField());
		engPanel.add(factory.createLabel("Actual Type"), "alignx right");
		engPanel.add(getEngineTypeTextField());
		engPanel.add(getEinResetButton(), "aligny center, height 40!");

		panel.add(engPanel, "height max, wrap");

		JPanel misPanel = new JPanel(new MigLayout("insets n n n n", secLayout));
		UiFactory.addSeparator(misPanel, "Mission");
		misPanel.add(factory.createLabel("Required Type"), "cell 2 1");
		misPanel.add(getMissionRequiredTypeTextField());
		misPanel.add(getMissionAssignButton(), "aligny center, height 40!, wrap 10");

		misPanel.add(factory.createLabel("Mission SN"));
		misPanel.add(getMissionSnTextField());
		misPanel.add(factory.createLabel("Actual Type"), "alignx right");
		misPanel.add(getMissionTypeTextField());
		misPanel.add(getMissionSnResetButton(), "aligny center, height 40!");
		panel.add(misPanel, "height max, wrap");

		factory = UiFactory.getInputSmall();
		JPanel procesPanel = new JPanel(new MigLayout("", "[]10[max, fill]20[]10[max, fill]20[]10[max, fill]"));
		UiFactory.addSeparator(procesPanel, "Current Process");
		procesPanel.add(factory.createLabel("Plant"));
		procesPanel.add(getPlantTextField());
		procesPanel.add(factory.createLabel("Division"));
		procesPanel.add(getDivisionTextField());
		procesPanel.add(factory.createLabel("Time"));
		procesPanel.add(getTimestampTextField(), "wrap 10");

		procesPanel.add(factory.createLabel("Line"));
		procesPanel.add(getLineTextField());
		procesPanel.add(factory.createLabel("Process"));
		procesPanel.add(getProcessPointTextField(), "span 3");

		panel.add(procesPanel, "height max, wrap");
		return panel;
	}

	protected void mapActions() {
		getProductIdTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getProductIdTextField()));
		getProductIdTextField().addActionListener(getController());
		getResetButton().addActionListener(getController());

		getEinTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getEinTextField()));
		getEinTextField().addActionListener(getController());

		getEngineAssignButton().addActionListener(getController());
		getEinResetButton().addActionListener(getController());

		getMissionAssignButton().addActionListener(getController());
		getMissionSnResetButton().addActionListener(getController());

		getMissionSnTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getMissionSnTextField()));
		getMissionSnTextField().addActionListener(getController());

		getMissionTypeTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getMissionTypeTextField()));
		getMissionTypeTextField().addActionListener(getController());

		getEinTextField().addFocusListener(getController());
		getEngineAssignButton().addFocusListener(getController());
		getEinResetButton().addFocusListener(getController());

		getMissionSnTextField().addFocusListener(getController());
		getMissionTypeTextField().addFocusListener(getController());
		getMissionAssignButton().addFocusListener(getController());
		getMissionSnResetButton().addFocusListener(getController());

		Action action = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				getController().actionPerformed(ae);
			}
		};
		mapEnterKey(getResetButton(), action);
		mapEnterKey(getEngineAssignButton(), action);
		mapEnterKey(getEinResetButton(), action);
		mapEnterKey(getMissionAssignButton(), action);
		mapEnterKey(getMissionSnResetButton(), action);
	}

	protected void mapEnterKey(JButton button, Action action) {
		button.getInputMap().put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "action");
		button.getActionMap().put("action", action);
	}

	public void mapValidators() {
		RequiredValidator requiredValidator = new RequiredValidator();
		RegExpValidator productIdValidator = new RegExpValidator(PropertyService.getPropertyBean(SystemPropertyBean.class).getProductIdRegexValidator());
		
		UiUtils.mapValidator(getProductIdTextField(), ChainCommand.create("VIN", requiredValidator, productIdValidator));
		UiUtils.mapValidator(getEinTextField(), ChainCommand.create("EIN", requiredValidator, productIdValidator));
		UiUtils.mapValidator(getMissionSnTextField(), ChainCommand.create("Mission SN", requiredValidator, productIdValidator));
		if(getPropertyBean().isMissionTypeRequired())
			UiUtils.mapValidator(getMissionTypeTextField(), ChainCommand.create("Mission Type", requiredValidator));
	}

	// === get/set === //
	protected JTextField getProductIdTextField() {
		return productIdTextField;
	}

	protected JButton getResetButton() {
		return resetButton;
	}

	protected JTextField getEnginePlantTextField() {
		return enginePlantTextField;
	}
	
	protected JComboBox getEngineRequiredTypeComboBox() {
		return this.engineRequiredTypeComboBox;
	}

	protected JTextField getEngineTypeTextField() {
		return engineTypeTextField;
	}

	protected JTextField getEinTextField() {
		return einTextField;
	}

	protected JButton getEngineAssignButton() {
		return engineAssignButton;
	}

	protected JTextField getMissionRequiredTypeTextField() {
		return missionRequiredTypeTextField;
	}

	protected JTextField getMissionTypeTextField() {
		return missionTypeTextField;
	}

	protected JTextField getMissionSnTextField() {
		return missionSnTextField;
	}

	protected JButton getMissionAssignButton() {
		return missionAssignButton;
	}

	protected JTextField getPlantTextField() {
		return plantTextField;
	}

	protected JTextField getLineTextField() {
		return lineTextField;
	}

	protected JTextField getProcessPointTextField() {
		return processPointTextField;
	}

	protected JTextField getTimestampTextField() {
		return timestampTextField;
	}

	protected JTextField getDivisionTextField() {
		return divisionTextField;
	}

	protected EngineMissionAssignmentController getController() {
		return controller;
	}

	protected JTextField getProductionLotTextField() {
		return productionLotTextField;
	}

	protected JTextField getProdSpecCodeTextField() {
		return prodSpecCodeTextField;
	}

	protected List<JTextField> getLocationTextFields() {
		List<JTextField> list = new ArrayList<JTextField>();
		list.add(getPlantTextField());
		list.add(getDivisionTextField());
		list.add(getLineTextField());
		list.add(getProcessPointTextField());
		list.add(getTimestampTextField());
		return list;
	}

	@Override
	public void onTabSelected() {

	}

	public void actionPerformed(ActionEvent arg0) {

	}

	protected JButton getEinResetButton() {
		return einResetButton;
	}

	protected JButton getMissionSnResetButton() {
		return missionSnResetButton;
	}
	
	public EngineMissionAssignPropertyBean getPropertyBean() {
		return propertyBean;
	}

	public void setPropertyBean(EngineMissionAssignPropertyBean propertyBean) {
		this.propertyBean = propertyBean;
	}
}

