package com.honda.galc.client.teamleader.hold.qsr.put.die;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.DateBean;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>InputPanel</code> is ... .
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
 */
public class InputPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final String SET_HOLD_LABEL = "Set Hold";
	public static final String RELEASE_HOLD_LABEL = "Release Hold";

	public static final String ACTIVE_HOLDS_LABEL = "Active Holds";
	public static final String INACTIVE_HOLDS_LABEL = "Inactive Holds";
	public static final int HOLD_REASON_MAX_LENGTH = 80;

	private JComboBox departmentComboBox;
	private JComboBox productTypeComboBox;
	private JComboBox modelCodeComboBox;
	private ButtonGroup machineGroup;
	private ArrayList<JRadioButton> machineSelectionList;
	private ArrayList<JCheckBox> dieSelectionList;
	private ArrayList<JCheckBox> coreSelectionList;

	private DateBean startDateBean;
	private DateBean stopDateBean;
	private JComboBox holdReasonComboBox;
	private JTextField associateIdTextField;
	private JTextField associateNameTextField;

	private JButton holdButton;
	private JButton selectHoldsButton;

	
	/**
	 * As we need still to support java 1.5 on client side, this dummy button is used
	 * to deselect machine jradio buttons in a group. When GALC drops support for
	 * 1.5 on client side this button and refering code should be removed and
	 * ButtonGroup.clearSelection() method should be used instead of
	 * getMachineNoSelection().setSelected(true);
	 */
	private JRadioButton machineNoSelection;
	
	public InputPanel(String[] dieIds, String[] coreIds) {
		this.machineSelectionList = new ArrayList<JRadioButton>();
		this.dieSelectionList = new ArrayList<JCheckBox>();
		this.coreSelectionList = new ArrayList<JCheckBox>();
		initView(dieIds, coreIds);
		initData();
	}

	protected void initView(String[] dieIds, String[] coreIds) {
		setLayout(new MigLayout("insets 0 0 0 0, gap 0", "[max,fill]", ""));
		this.departmentComboBox = createDepartmentComboBox();
		this.productTypeComboBox = createProductTypeComboBox();
		this.modelCodeComboBox = createModelCodeComboBox();
		JPanel departmentSelectionPanel = createDepartmentPanel();
		JPanel productTypeSelectionPanel = createProductTypePanel();
		JPanel productSpecSelectionPanel = createProductSpecPanel();
		JPanel machineSelectionPanel = createMachineSelectionPanel();
		JPanel dieSelectionPanel = createDieSelectionPanel(dieIds);
		JPanel coreSelectionPanel = createCoreSelectionPanel(coreIds);

		this.holdButton = createButton(SET_HOLD_LABEL);
		getHoldButton().setActionCommand(DiePanel.SET_HOLD_COMMAND);
		getHoldButton().setMnemonic(KeyEvent.VK_O);
		this.selectHoldsButton = createButton(ACTIVE_HOLDS_LABEL);
		getSelectHoldsButton().setMnemonic(KeyEvent.VK_V);
		getSelectHoldsButton().setActionCommand(DiePanel.SELECT_ACTIVE_HOLDS_COMMAND);

		JPanel firstRowPanel = new JPanel();
		firstRowPanel.setLayout(new MigLayout("insets 0 0 0 5, gap 0", "[max,fill][max,fill][max,fill][max,fill][150!,fill]", "[center]"));
		firstRowPanel.add(departmentSelectionPanel);
		firstRowPanel.add(productTypeSelectionPanel);
		firstRowPanel.add(productSpecSelectionPanel);
		firstRowPanel.add(machineSelectionPanel);
		firstRowPanel.add(getHoldButton(), "height 40, gap 2 2 7, align right");

		JPanel secondRowPanel = new JPanel();
		secondRowPanel.setLayout(new MigLayout("insets 0 0 0 5, gap 0 ", "[max,fill][max,fill][150!,fill]", "[center]"));
		secondRowPanel.add(dieSelectionPanel);
		secondRowPanel.add(coreSelectionPanel);
		secondRowPanel.add(getSelectHoldsButton(), "height 40, gap 2 2 7, align right");

		add(firstRowPanel, "wrap");
		add(secondRowPanel, "wrap");
		add(createHoldParamPanel(), "height 100");
	}

	protected void initData() {

	}

	// === factory === //
	protected JPanel createDepartmentPanel() {
		JPanel panel = new JPanel();
		panel.setName("departmentPanel");
		panel.setBorder(DiePanel.createTitledBorder("Department"));
		panel.setLayout(new GridLayout(1, 1));
		panel.add(getDepartmentComboBox(), null);
		return panel;
	}

	protected JPanel createProductTypePanel() {
		JPanel panel = new JPanel();
		panel.setName("productTypePanel");
		panel.setBorder(DiePanel.createTitledBorder("Product Type"));
		panel.setLayout(new GridLayout(1, 1));
		panel.add(getProductTypeComboBox(), null);
		return panel;
	}

	protected JPanel createProductSpecPanel() {
		JPanel panel = new JPanel();
		panel.setName("productSpecPanel");
		panel.setBorder(DiePanel.createTitledBorder("Model"));
		panel.setLayout(new GridLayout(1, 1));
		panel.add(getModelCodeComboBox(), null);
		return panel;
	}

	protected JPanel createMachineSelectionPanel() {
		JPanel panel = new JPanel();
		panel.setName("machineSelectionPanel");
		panel.setEnabled(true);
		int count = Config.getMachineCount();
		panel.setBorder(DiePanel.createTitledBorder("Machine"));
		panel.setLayout(new MigLayout("insets 0 0 0 0, gap 0 ", "[fill]"));
		this.machineGroup = new ButtonGroup();
		for (int i = 0; i < count; i++) {
			JRadioButton item = new JRadioButton();
			item.setName("machineButton" + (i + 1));
			item.setFont(Fonts.DIALOG_BOLD_12);
			getMachineGroup().add(item);
			panel.add(item);
			getMachineSelectionList().add(item);
		}
		this.machineNoSelection = new JRadioButton();
		getMachineGroup().add(getMachineNoSelection());
		return panel;
	}

	protected JPanel createDieSelectionPanel(String[] dieIds) {
		return createCheckBoxSelectionPanel("dieSelectionPanel", "Die", dieIds, "dieSelection", getDieSelectionList(), true);
	}

	protected JPanel createCoreSelectionPanel(String[] coreIds) {
		return createCheckBoxSelectionPanel("coreSelectionPanel", "Sand Core", coreIds, "coreSlection", getCoreSelectionList(), false);
	}

	protected JPanel createCheckBoxSelectionPanel(String panelName, String title, String[] ids, String namePrefix, List<JCheckBox> list, boolean enabled) {
		JPanel panel = new JPanel();
		panel.setName(panelName);
		if (ids == null) {
			return panel;
		}
		for (String str : ids) {
			if (StringUtils.isNotBlank(str)) {
				String id = str.trim();
				JCheckBox item = new JCheckBox();
				item.setName(namePrefix + id);
				item.setText(id);
				item.setFont(Fonts.DIALOG_BOLD_12);
				item.setEnabled(enabled);
				panel.add(item);
				list.add(item);
			}
		}
		panel.setLayout(new MigLayout("insets 0 0 0 0, gap 0 ", "[fill]"));
		if (list != null && !list.isEmpty()) {
			panel.setBorder(DiePanel.createTitledBorder(title));
		}
		return panel;
	}

	protected JPanel createHoldParamPanel() {
		JPanel panel = new JPanel();
		panel.setName("holdParameterPanel");
		panel.setLayout(new MigLayout("insets 0, gap 0", "10[][]10[][]10[grow,fill]1[grow,fill]1[grow,fill]", ""));
		panel.setBorder(DiePanel.createTitledBorder("Hold Parameter"));

		this.startDateBean = createDateBean("startDate");
		this.stopDateBean = createDateBean("startDate");
		this.holdReasonComboBox = createHoldReasonComboBox();
		this.associateIdTextField = createAssociateIdTextField();
		this.associateNameTextField = createAssociateNameTextField();

		panel.add(new JLabel("Start:"), "cell 0 1");
		panel.add(getStartDateBean(), "spany 2, cell 1 0, height 55, width 185");
		panel.add(new JLabel("Stop:"), "cell 2 1");
		panel.add(getStopDateBean(), "spany 2, cell 3 0, height 55, width 185");
		panel.add(new JLabel("Hold Reason"));
		panel.add(new JLabel("Associate ID"));
		panel.add(new JLabel("Associate Name"), "wrap");
		panel.add(getHoldReasonComboBox(), "height 30");
		panel.add(getAssociateIdTextField(), "height 30");
		panel.add(getAssociateNameTextField(), "height 30");

		return panel;
	}

	// === ui input === //
	public DateBean createDateBean(String name) {
		DateBean item = new DateBean();
		item.setName(name);
		for (Component c : item.getComponents()) {
			if (c instanceof LengthFieldBean) {
				c.setFont(Fonts.DIALOG_BOLD_12);
			}
		}
		return item;
	}

	protected JComboBox createDepartmentComboBox() {
		JComboBox item = new JComboBox();
		item.setName("deparmtent");
		item.setRenderer(new PropertyComboBoxRenderer<Division>(Division.class, "divisionName"));
		List<Division> list = Config.getDiecastDivisions();
		item.setModel(new DefaultComboBoxModel(new Vector<Division>(list)));
		return item;
	}

	protected JComboBox createProductTypeComboBox() {
		JComboBox item = new JComboBox();
		item.setName("productType");
		item.setRenderer(new PropertyComboBoxRenderer<ProductType>(ProductType.class, "productName"));
		return item;
	}

	protected JComboBox createModelCodeComboBox() {
		JComboBox item = new JComboBox();
		item.setName("modelCode");
		return item;
	}

	protected JComboBox createHoldReasonComboBox() {
		JComboBox item = new JComboBox();
		item.setName("holdReason");
		item.setEditable(true);
		JTextField editor = ((JTextField) item.getEditor().getEditorComponent());
		editor.setDocument(new LimitedLengthPlainDocument(HOLD_REASON_MAX_LENGTH));
		return item;
	}

	protected JTextField createAssociateIdTextField() {
		JTextField item = new JTextField();
		item.setName("associateId");
		item.setDocument(new LimitedLengthPlainDocument(8));
		item.setFont(Fonts.DIALOG_BOLD_12);
		return item;
	}

	protected JTextField createAssociateNameTextField() {
		JTextField item = new JTextField();
		item.setName("associateName");
		item.setDocument(new LimitedLengthPlainDocument(25));
		item.setFont(Fonts.DIALOG_BOLD_12);
		item.setEditable(false);
		return item;
	}

	public JButton createButton(String text) {
		JButton item = new JButton(text);
		item.setFont(Fonts.DIALOG_BOLD_16);
		return item;
	}

	// === utility === //
	public String getSelectedMachine() {
		if (getSelectedMachines().size() > 0) {
			return getSelectedMachines().get(0);
		}
		return null;
	}

	public List<String> getSelectedMachines() {
		List<String> selectedList = new ArrayList<String>();
		for (JRadioButton item : getMachineSelectionList()) {
			if (item.isEnabled() && item.isSelected()) {
				selectedList.add(item.getText());
			}
		}
		return selectedList;
	}

	public List<String> getSelectedDies() {
		List<String> selectedList = new ArrayList<String>();
		for (JCheckBox item : getDieSelectionList()) {
			if (item.isSelected()) {
				selectedList.add(item.getText());
			}
		}
		return selectedList;
	}

	public List<String> getSelectedCores() {
		List<String> selectedList = new ArrayList<String>();
		for (JCheckBox item : getCoreSelectionList()) {
			if (item.isSelected()) {
				selectedList.add(item.getText());
			}
		}
		return selectedList;
	}

	// === get/set === //
	public JComboBox getDepartmentComboBox() {
		return departmentComboBox;
	}

	public ArrayList<JRadioButton> getMachineSelectionList() {
		return machineSelectionList;
	}

	public ArrayList<JCheckBox> getDieSelectionList() {
		return dieSelectionList;
	}

	public ArrayList<JCheckBox> getCoreSelectionList() {
		return coreSelectionList;
	}

	public DateBean getStartDateBean() {
		return startDateBean;
	}

	public DateBean getStopDateBean() {
		return stopDateBean;
	}

	public JComboBox getHoldReasonComboBox() {
		return holdReasonComboBox;
	}

	public JTextField getAssociateIdTextField() {
		return associateIdTextField;
	}

	public JTextField getAssociateNameTextField() {
		return associateNameTextField;
	}

	public JButton getSelectHoldsButton() {
		return selectHoldsButton;
	}

	public JComboBox getProductTypeComboBox() {
		return productTypeComboBox;
	}

	public JComboBox getModelCodeComboBox() {
		return modelCodeComboBox;
	}

	public JButton getHoldButton() {
		return holdButton;
	}

	public ButtonGroup getMachineGroup() {
		return machineGroup;
	}

	public JRadioButton getMachineNoSelection() {
		return machineNoSelection;
	}
}
