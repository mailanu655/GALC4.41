package com.honda.galc.client.teamleader.hold.qsr.reason;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.reason.listener.ReasonController;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.utils.ComboBoxUtils;
import com.honda.galc.dto.HoldReasonMappingDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.product.HoldReason;

import net.miginfocom.swing.MigLayout;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ReasonPanel</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Prasanna P</TD>
 * <TD>Dec 1 2016</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author hat0926
 */

public class ReasonPanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;

	private ObjectTablePane<HoldReason> holdReasonPane;
	private ObjectTablePane<HoldReasonMappingDto> holdReasonMappingPane;

	protected static final String DELETE_TEXT = "DELETE";
	protected static final String NEW_TEXT = "NEW";
	protected static final String ADD_TEXT = "ADD";
	public static final String SELECT_PLANT = "Select Plant";
	public static final String SELECT_LINE = "Select Line";
	public static final String SELECT_DIVISION = "Select Division";
	public static final String SELECT_QCACTION = "Select QC Action";
	protected static final String REASON_DATA = "Hold Reasons";
	protected static final String REASON_MAPPING = "Hold Reason Mappings";

	private JButton newButton;
	private JButton addButton;
	private JButton removeButton;

	private JComboBox plantComboBox;
	private JComboBox divisionComboBox;
	private JComboBox lineComboBox;
	private JComboBox qcActionComboBox;

	private ReasonController controller;
	
	public ReasonPanel(QsrMaintenanceFrame mainWindow) {
		super("Hold Reason", KeyEvent.VK_D, mainWindow);
		setMainWindow(mainWindow);
		this.controller = new ReasonController(this);
	}

	protected void initComponents() {
		// === create ui fragments === //
		plantComboBox = createComboBox("PlantComboBox");
		divisionComboBox = createComboBox("DivisionComboBox");
		lineComboBox = createComboBox("LineComboBox");
		qcActionComboBox = createComboBox("QCActionComboBox");

		holdReasonPane = createHoldReasonPane();
		holdReasonMappingPane = createHoldReasonMappingPane();
		
		newButton = createNewButton();
		addButton = createAddButton();
		removeButton = createSubmitButton();
		

		setName("ReasonPanel");
		setLayout(new MigLayout("insets 0 0 0 2, gap 0", "[max,fill]", ""));

		// initialize
		initView();
		loadData();
		mapActions();
		clearErrorMessage();
	}

	protected int getLeftMargin() {
		return 5;
	}

	protected int getTopMargin() {
		return 40;
	}

	@Override
	public void onTabSelected() {

		if (!isInitialized) {
			initComponents();
			isInitialized = true;
		} else
			loadData();
	}

	// ============= factory methods for ui components
	protected ObjectTablePane<HoldReason> createHoldReasonPane() {
		ColumnMappings mapping = ColumnMappings.with("#", "row").put("ID", "reasonId")
				.put("Division", "divisionId").put("Hold Reason", "holdReason");

		ObjectTablePane<HoldReason> pane = new ObjectTablePane<HoldReason>(
				mapping.get(), true, true);
		pane.getTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return pane;
	}

	protected ObjectTablePane<HoldReasonMappingDto> createHoldReasonMappingPane() {

		ColumnMappings mapping = ColumnMappings.with("#","row").put("ID", "reasonMappingId")
				.put("Hold Reason", "reasonId").put("Division", "divisionId")
				.put("LineId", "lineId").put("Action", "qcActionId")
				.put("Associate ID", "associateId").put("Hold Reason", "holdReason");

		ObjectTablePane<HoldReasonMappingDto> pane = new ObjectTablePane<HoldReasonMappingDto>(
				mapping.get(), true, true);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.getTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return pane;
	}

	private JComboBox createComboBox(String name) {
		JComboBox comboBox = new JComboBox();
		comboBox.setName(name);
		comboBox.setMaximumSize(new Dimension(130, 100));
		comboBox.setFont(Fonts.DIALOG_PLAIN_14);
		return comboBox;
	}

	protected JButton createSubmitButton() {
		JButton button = new JButton();
		button.setFont(Fonts.DIALOG_PLAIN_18);
		button.setEnabled(false);
		button.setMargin(new Insets(0, 0, 0, 0));
		return button;
	}

	protected JButton createNewButton() {
		JButton button = new JButton();
		button.setText(NEW_TEXT);
		button.setFont(Fonts.DIALOG_PLAIN_18);
		button.setEnabled(true);
		button.setMargin(new Insets(0, 0, 0, 0));
		return button;
	}

	protected JButton createAddButton() {
		JButton button = new JButton();
		button.setText(ADD_TEXT);
		button.setFont(Fonts.DIALOG_PLAIN_16);
		button.setEnabled(false);
		button.setMargin(new Insets(1, 1, 1, 1));
		return button;
	}

	protected JLabel createLabel(String text) {
		JLabel label = new JLabel(text, JLabel.CENTER);
		label.setFont(Fonts.DIALOG_BOLD_14);
		label.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		label.setMaximumSize(new Dimension(130, 100));
		return label;
	}

	// === get/set === //
	public ObjectTablePane<HoldReason> getHoldReasonPane() {
		return holdReasonPane;
	}

	public ObjectTablePane<HoldReasonMappingDto> getHoldReasonMappingPane() {
		return holdReasonMappingPane;
	}

	protected void mapActions() {

		getHoldReasonPane().getTable().getSelectionModel()
		.addListSelectionListener(getController());
		getHoldReasonMappingPane().getTable().getSelectionModel()
		.addListSelectionListener(getController());

		plantComboBox.addActionListener(getController());
		divisionComboBox.addActionListener(getController());
		lineComboBox.addActionListener(getController());
		qcActionComboBox.addActionListener(getController());

		getRemoveButton().setText(DELETE_TEXT);

		getRemoveButton().addActionListener(getController());
		getAddButton().addActionListener(getController());
		getNewButton().addActionListener(getController());

		getRemoveButton().setEnabled(false);
		getAddButton().setEnabled(false);
		getNewButton().setEnabled(false);

		Action action = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				getController().actionPerformed(ae);
			}
		};

	}

	public JButton getRemoveButton() {
		return removeButton;
	}

	public JButton getNewButton() {
		return newButton;
	}

	public JButton getAddButton() {
		return addButton;
	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		ComboBoxUtils.loadComboBox(plantComboBox, getController().getPlants());
		ComboBoxUtils.loadComboBox(divisionComboBox, getController()
				.getDivisions());
		ComboBoxUtils.loadComboBox(lineComboBox, getController().getLines());
		ComboBoxUtils.loadComboBox(qcActionComboBox, getController()
				.getActions());

		if (getController().getPlants() != null
				&& getController().getPlants().size() > 0) {
			getPlantComboBox().setSelectedIndex(0);
			getPlantComboBox().setRenderer(
					new PropertyComboBoxRenderer<Plant>(Plant.class,
							"plantName"));
		}
		divisionComboBox.setSelectedIndex(0);
		lineComboBox.setSelectedIndex(0);
		qcActionComboBox.setSelectedIndex(0);

	}

	public static TitledBorder createTitledBorder(String title) {
		return new TitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED), title);
	}

	public JPanel createReasonDataPanel() {
		JPanel panel = new JPanel();
		panel.setName("reasonDataPanel");
		panel.setBorder(createTitledBorder(REASON_DATA));
		panel.setLayout(new GridLayout(1, 1));
		panel.add(getHoldReasonPane());
		return panel;
	}

	public JPanel createReasonMappingDataPanel() {
		JPanel panel = new JPanel();
		panel.setName("reasonMappingDataPanel");
		panel.setBorder(createTitledBorder(REASON_MAPPING));
		panel.setLayout(new GridLayout(1, 1));
		panel.add(getHoldReasonMappingPane());
		return panel;
	}

	protected JPanel createPlantPanel() {
		JPanel panel = new JPanel();
		panel.setName("plantPanel");
		panel.setBorder(createTitledBorder("Plant"));
		panel.setLayout(new GridLayout(1, 1));
		panel.add(getPlantComboBox(), null);
		return panel;
	}

	protected JPanel createDivisonPanel() {
		JPanel panel = new JPanel();
		panel.setName("divisonPanel");
		panel.setBorder(createTitledBorder("Divison"));
		panel.setLayout(new GridLayout(1, 1));
		panel.add(getDivisonComboBox(), null);
		return panel;
	}

	protected JPanel createLinePanel() {
		JPanel panel = new JPanel();
		panel.setName("linePanel");
		panel.setBorder(createTitledBorder("Line"));
		panel.setLayout(new GridLayout(1, 1));
		panel.add(getLineComboBox(), null);
		return panel;
	}

	protected JPanel createActionPanel() {
		JPanel panel = new JPanel();
		panel.setName("reasonPanel");
		panel.setBorder(createTitledBorder("QC Action"));
		panel.setLayout(new GridLayout(1, 1));
		panel.add(getQCActionComboBox(), null);
		return panel;
	}

	public JComboBox getPlantComboBox() {
		return plantComboBox;
	}

	@SuppressWarnings("rawtypes")
	public JComboBox getDivisonComboBox() {
		return divisionComboBox;
	}

	@SuppressWarnings("rawtypes")
	public JComboBox getLineComboBox() {
		return lineComboBox;
	}

	public JComboBox getQCActionComboBox() {
		return qcActionComboBox;
	}

	@SuppressWarnings("unchecked")
	protected void initView() {
		setLayout(new MigLayout("insets 0 0 0 0, gap 0", "[max,fill]", ""));

		JPanel plantSelectionPanel = createPlantPanel();
		JPanel divisonSelectionPanel = createDivisonPanel();
		JPanel lineIdSelectionPanel = createLinePanel();
		JPanel actionSelectionPanel = createActionPanel();

		plantComboBox.setRenderer(new PropertyComboBoxRenderer<Plant>(
				Plant.class, "plantName"));
		divisionComboBox.setRenderer(new PropertyComboBoxRenderer<Division>(
				Division.class, "divisionName"));
		lineComboBox.setRenderer(new PropertyComboBoxRenderer<Line>(Line.class,
				"lineName"));

		JPanel firstRowPanel = new JPanel();
		firstRowPanel.setLayout(new MigLayout("insets 0 0 0 5, gap 0",
				"[max,fill][max,fill][max,fill][max,fill][150!,fill]",
				"[center]"));
		firstRowPanel.add(plantSelectionPanel);
		firstRowPanel.add(divisonSelectionPanel);

		JPanel thirdRowPanel = new JPanel();
		thirdRowPanel.setLayout(new MigLayout("insets 0 0 0 5, gap 0 ",
				"[max,fill][max,fill][150!,fill]", "[center]"));

		thirdRowPanel.add(lineIdSelectionPanel);
		thirdRowPanel.add(actionSelectionPanel);
		thirdRowPanel.add(getAddButton(), "height 40, gap 2 2 7, align right");
		thirdRowPanel.add(getNewButton(), "height 40, gap 2 2 7, align right");
		thirdRowPanel.add(getRemoveButton(),
				"height 40, gap 2 2 7, align right");

		add(firstRowPanel, "wrap");
		add(createReasonDataPanel(), "height 200!,wrap 5");
		add(thirdRowPanel, "wrap");
		add(createReasonMappingDataPanel(), "height 200!,wrap 5");
	}

	protected ReasonController getController() {
		return controller;
	}

	public void actionPerformed(ActionEvent e) {
	}

}
