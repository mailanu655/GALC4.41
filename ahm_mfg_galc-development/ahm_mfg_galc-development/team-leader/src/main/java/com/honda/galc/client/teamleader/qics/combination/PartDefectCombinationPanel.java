package com.honda.galc.client.teamleader.qics.combination;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.teamleader.qics.screen.QicsMaintenanceTabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.utils.ComboBoxUtils;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Zone;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.entity.qics.DefectTypeDescription;
import com.honda.galc.entity.qics.InspectionPartDescription;
import com.honda.galc.entity.qics.Iqs;
import com.honda.galc.entity.qics.IqsId;
import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.entity.qics.Regression;
import com.honda.galc.util.PropertyComparator;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>PartDefectCombinationPanel</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Sep 2, 2008</TD>
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

/**
 * 
 * @author Gangadhararao Gadde
 * @date july 13, 2016
 */
public class PartDefectCombinationPanel extends QicsMaintenanceTabbedPanel {

	private static final long serialVersionUID = 1L;

	private ObjectTablePane<PartGroup> partGroupPane;
	private ObjectTablePane<InspectionPartDescription> inspectionPartDescriptionPane;

	private ObjectTablePane<DefectGroup> defectGroupPane;
	private ObjectTablePane<DefectTypeDescription> defectTypeDescriptionPane;

	private ObjectTablePane<DefectDescription> defectDescriptionPane;

	private JPanel defectPropertiesPanel;
	
	private static final String ADD_TEXT = "ADD";
	private static final String DELETE_TEXT = "DELETE";
	private static final String QUERY_TEXT = "QUERY";
	private static final String QUERY_DEFECT_TEXT = "QUERY BY DEFECT";
	private static final String SELECT_DEPARTMENT = "Select Department";
	private static final String SELECT_LINE = "Select Line";
	private static final String SELECT_ZONE = "Select Zone";
	private static final String SELECT_REGRESSION = "Select Regression";
	private static final String SELECT_IQS_CAT = "Select Cat";
	private static final String SELECT_IQS_ITEM = "Select Item";
	
	private JButton submitButton;
	private JButton queryByDefectButton;
	private JButton queryButton;

	private JComboBox departmentComboBox ;
	private JComboBox lineComboBox ;
	private JComboBox zoneComboBox ;
	private JComboBox iqsCategoryComboBox ;
	//private JTextField iqsItemTextField = new JTextField() ;
	private JComboBox iqsItemComboBox ;
	private JComboBox regressionCodeComboBox ;
	private JCheckBox firingFlagCheckBox = new JCheckBox();

	public PartDefectCombinationPanel(QicsMaintenanceFrame mainWindow) {
		super("Part Defect Combination",KeyEvent.VK_C);
		setMainWindow(mainWindow);
	}

	protected void initComponents() {
		setLayout(null);
		// === create ui fragments === //
		departmentComboBox = createComboBox("DepartmentComboBox");
		lineComboBox = createComboBox("LineComboBox");
		zoneComboBox = createComboBox("ZoneComboBox");
		iqsCategoryComboBox = createComboBox("IqsCategoryComboBox");
		iqsItemComboBox = createComboBox("IqsItemComboBox");
		regressionCodeComboBox = createComboBox("RegressionCodeComboBox");
		partGroupPane = createPartGroupPane();
		inspectionPartDescriptionPane = createInspectionPartDescriptionPane();
		defectGroupPane = createDefectGroupPane();
		defectTypeDescriptionPane = createDefectTypeDescriptionPane();
		defectPropertiesPanel = createDefectPropertiesPanel();
		submitButton = createSubmitButton();
		queryByDefectButton = createQueryDefectButton();
		queryButton = createQueryButton();
		defectDescriptionPane = createDefectDescriptionPane();

		// === add fragments == //
		add(getPartGroupPane());
		add(getInspectionPartDescriptionPane());

		add(getDefectGroupPane());
		add(getDefectTypeDescriptionPane());
		add(getDefectPropertiesPanel());
		add(getDefectDescriptionPane());

		add(getSubmitButton());
		add(getQueryButton());
		add(getQueryDefectButton());

		// == init data === //
		getPartGroupPane().reloadData(getClientModel().getPartGroups(), getClientModel().getPartGroupsUpdateTime(), "getPartGroupName");
		
		getDefectGroupPane().reloadData(getClientModel().getDefectGroups(), getClientModel().getDefectGroupsUpdateTime(), "getDefectGroupName");

		loadData();
		
		mapActions();
		mapHandlers();

	}
	
	protected int getLeftMargin() {
		return 5;
	}

	protected int getTopMargin() {
		return 40;
	}


	@Override
	public void onTabSelected() {

		if(!isInitialized) {
			initComponents();
			isInitialized = true;
		}else
			loadData();
		
		if (getClientModel().isPartGroupsUpdated(getPartGroupPane().getDataSelectedTime()))
			getPartGroupPane().reloadData(getClientModel().getPartGroups(), getClientModel().getPartGroupsUpdateTime(), "getPartGroupName");

		List<InspectionPartDescription> selectedValues = getInspectionPartDescriptionPane().getSelectedItems();
		if (selectedValues == null || selectedValues.isEmpty()) return;

		if (getClientModel().isDefectGroupsUpdated(getDefectGroupPane().getDataSelectedTime())) 
			getDefectGroupPane().reloadData(getClientModel().getDefectGroups(), getClientModel().getDefectGroupsUpdateTime(), "getDefectGroupName");
	}

	// ============= factory methods for ui components
	protected ObjectTablePane<PartGroup> createPartGroupPane() {
		int	height = 175;
		
		ColumnMappings columnMappings = ColumnMappings.with("Part Group", "partGroupName");
		ObjectTablePane<PartGroup> pane = new ObjectTablePane<PartGroup>(columnMappings.get(),true);
		pane.setSize(180, height);
		pane.setLocation(getLeftMargin(), getTopMargin());
		return pane;
	}

	protected ObjectTablePane<InspectionPartDescription> createInspectionPartDescriptionPane() {
		
		ColumnMappings columnMappings = 
			ColumnMappings.with("Part", "inspectionPartName").put("Location", "inspectionPartLocationName");
		
		ObjectTablePane<InspectionPartDescription> pane = new ObjectTablePane<InspectionPartDescription>(columnMappings.get(),true);
		pane.setSize(300, getPartGroupPane().getHeight());
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		Component base = getPartGroupPane();
		pane.setLocation(base.getX() + base.getWidth(), getTopMargin());
		return pane;
	}

	protected ObjectTablePane<DefectGroup> createDefectGroupPane() {

		ColumnMappings columnMappings = ColumnMappings.with("Defect Group", "defectGroupName");

		ObjectTablePane<DefectGroup> pane = new ObjectTablePane<DefectGroup>(columnMappings.get(),true);
		pane.setSize(getPartGroupPane().getWidth(), getPartGroupPane().getHeight());
		Component base = getInspectionPartDescriptionPane();
		pane.setLocation(base.getX() + base.getWidth() + 2, getTopMargin());
		return pane;
	}

	protected ObjectTablePane<DefectTypeDescription> createDefectTypeDescriptionPane() {
		int width = 345;
		
		ColumnMappings columnMappings = 
			ColumnMappings.with("#", "row").put("Defect", "defectTypeName").put("2nd Part", "secondaryPartName");

		ObjectTablePane<DefectTypeDescription> pane = new ObjectTablePane<DefectTypeDescription>(columnMappings.get(),true);
		pane.setSize(width, getPartGroupPane().getHeight());
//		pane.setColumnWidths(new int[] { rowColWidth, (width - rowColWidth) / 2, (width - rowColWidth) / 2 });
		Component base = getDefectGroupPane();
		pane.setLocation(base.getX() + base.getWidth(), getTopMargin());
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return pane;
	}

	protected ObjectTablePane<DefectDescription> createDefectDescriptionPane() {

		int width = getDefectTypeDescriptionPane().getX() + getDefectTypeDescriptionPane().getWidth() - getPartGroupPane().getX();
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Part Group", "partGroupName");
		mapping.put("Part", "inspectionPartName");
		mapping.put("Part Location", "id.inspectionPartLocationName");
		mapping.put("Defect", "defectTypeName");
		mapping.put("2nd Part", "secondaryPartName");
		mapping.put("Dept", "responsibleDept");
		mapping.put("Line", "responsibleLine");
		mapping.put("Zone", "responsibleZone");
		mapping.put("Category", "iqsCategoryName");
		mapping.put("Item","iqsItemName");
		mapping.put("Regression", "regressionCode");
		mapping.put("Firing Flag", "engineFiringFlag");

		ObjectTablePane<DefectDescription> pane = new ObjectTablePane<DefectDescription>(mapping.get(),true,true);

		pane.setSize(width, 320);
		Component base = getDefectPropertiesPanel();
		pane.setLocation(getLeftMargin(), base.getY() + base.getHeight() + 10);

		pane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

//		pane.setHeaders(columnPropertyMapping.keySet().toArray(new String[] {}));

//		pane.setColumnWidths(new int[] { rowColWidth, getPartGroupPane().getWidth(), getInspectionPartDescriptionPane().getWidth() / 2, getInspectionPartDescriptionPane().getWidth() / 2,
//				getDefectTypeDescriptionPane().getWidth() / 2, getDefectTypeDescriptionPane().getWidth() / 2 });

//		TableCellRenderer tableCellRenderer = new PropertyTableCellRenderer<DefectDescription>(DefectDescription.class, columnPropertyMapping);
//		pane.setCellRenderer(tableCellRenderer);
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return pane;
	}

	protected JPanel createDefectPropertiesPanel() {
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
				departmentComboBox.setEnabled(enabled);
				lineComboBox.setEnabled(enabled);
				zoneComboBox.setEnabled(enabled);
				iqsCategoryComboBox.setEnabled(enabled);
				//iqsItemTextField.setEnabled(enabled);
				iqsItemComboBox.setEnabled(enabled);
				regressionCodeComboBox.setEnabled(enabled);
				firingFlagCheckBox.setEnabled(enabled);
			}
		};
		Component base = getPartGroupPane();
		panel.setSize(920, 60);

		panel.setLocation(base.getX(), base.getY() + base.getHeight() + 35);
		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		departmentComboBox.setRenderer(new PropertyComboBoxRenderer<Division>(Division.class, "divisionName"));
		lineComboBox.setRenderer(new PropertyComboBoxRenderer<Line>(Line.class, "lineName"));
		zoneComboBox.setRenderer(new PropertyComboBoxRenderer<Zone>(Zone.class, "zoneName"));
		iqsCategoryComboBox.setRenderer(new PropertyComboBoxRenderer<Iqs>(Iqs.class, "iqsCategoryName"));
		//iqsItemTextField.setEditable(false);
		//iqsItemTextField.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		//iqsItemTextField.setFont(Fonts.DIALOG_BOLD_12);
		iqsItemComboBox.setRenderer(new PropertyComboBoxRenderer<Iqs>(Iqs.class, "iqsItemName"));
		regressionCodeComboBox.setRenderer(new PropertyComboBoxRenderer<Regression>(Regression.class, "regressionCode"));
		firingFlagCheckBox.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		firingFlagCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
		
		panel.setLayout(new MigLayout("insets 0,gap 0", "[max,fill][max,fill][max,fill][max,fill][max,fill][max,fill][max,fill]1"));
		panel.add(createLabel("Department"));
		panel.add(createLabel("Line"));
		panel.add(createLabel("Zone"));
		panel.add(createLabel("IQS Cat"));
		panel.add(createLabel("IQS Item"));
		panel.add(createLabel("Regres Code"));
		panel.add(createLabel("Firing Flag"), "wrap");

		panel.add(departmentComboBox);
		panel.add(lineComboBox);
		panel.add(zoneComboBox);
		panel.add(iqsCategoryComboBox);
		//panel.add(iqsItemTextField);
		panel.add(iqsItemComboBox);
		panel.add(regressionCodeComboBox);
		panel.add(firingFlagCheckBox);

		panel.setEnabled(true);
		return panel;
	}

	private JComboBox createComboBox(String name) {
		JComboBox comboBox=new JComboBox();
		comboBox.setName(name);
		comboBox.setMaximumSize(new Dimension(130,100));
		comboBox.setFont(Fonts.DIALOG_PLAIN_14);
		return comboBox;
	}

	protected JButton createSubmitButton() {
		JButton button = new JButton();
		Component base = getDefectPropertiesPanel();
		button.setSize(getDefectTypeDescriptionPane().getX() + getDefectTypeDescriptionPane().getWidth() - base.getX() - base.getWidth() - 10, base.getHeight()/2);

		button.setLocation(base.getX() + base.getWidth() + 5, base.getY() + base.getHeight()/2);
		button.setFont(Fonts.DIALOG_PLAIN_18);
		button.setEnabled(false);
		button.setMargin(new Insets(0,0,0,0));
		return button;
	}
	
	protected JButton createQueryButton() {
		JButton button = new JButton();
		Component base = getDefectPropertiesPanel();
		button.setSize(getDefectTypeDescriptionPane().getX() + getDefectTypeDescriptionPane().getWidth() - base.getX() - base.getWidth() - 10, base.getHeight()/2);
		button.setText(QUERY_TEXT);
		button.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		button.setFont(Fonts.DIALOG_PLAIN_18);
		button.setEnabled(false);
		button.setMargin(new Insets(0,0,0,0));
		return button;
	}
			
	protected JButton createQueryDefectButton() {
		JButton button = new JButton();
		Component base = getSubmitButton();
		button.setSize(getDefectTypeDescriptionPane().getWidth() - 150, base.getHeight());
		button.setText(QUERY_DEFECT_TEXT);
		button.setLocation(getDefectTypeDescriptionPane().getX() + getDefectTypeDescriptionPane().getWidth()/4, getDefectTypeDescriptionPane().getY() + getDefectTypeDescriptionPane().getHeight());
		button.setFont(Fonts.DIALOG_PLAIN_16);
		button.setEnabled(false);
		button.setMargin(new Insets(1,1,1,1));
		return button;
	}

	protected JLabel createLabel(String text) {
		JLabel label = new JLabel(text, JLabel.CENTER);
		label.setFont(Fonts.DIALOG_BOLD_14);
		label.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		label.setMaximumSize(new Dimension(130,100));
		return label;
	}

	// === get/set === //
	public ObjectTablePane<PartGroup> getPartGroupPane() {
		return partGroupPane;
	}

	public ObjectTablePane<InspectionPartDescription> getInspectionPartDescriptionPane() {
		return inspectionPartDescriptionPane;
	}

	public ObjectTablePane<DefectGroup> getDefectGroupPane() {
		return defectGroupPane;
	}

	public ObjectTablePane<DefectTypeDescription> getDefectTypeDescriptionPane() {
		return defectTypeDescriptionPane;
	}

	public ObjectTablePane<DefectDescription> getDefectDescriptionPane() {
		return defectDescriptionPane;
	}

	protected void mapActions() {
		getSubmitButton().setText(ADD_TEXT);
		getSubmitButton().setEnabled(false);
		getSubmitButton().addActionListener(this);
		getQueryButton().setEnabled(false);
		getQueryButton().addActionListener(this);
		getQueryDefectButton().setEnabled(false);
		getQueryDefectButton().addActionListener(this);
	}

	protected void mapHandlers() {
		getPartGroupPane().getTable().getSelectionModel().addListSelectionListener(this);
		getDefectGroupPane().getTable().getSelectionModel().addListSelectionListener(this);
		getInspectionPartDescriptionPane().getTable().getSelectionModel().addListSelectionListener(this);
		getDefectTypeDescriptionPane().getTable().getSelectionModel().addListSelectionListener(this);
		getDefectDescriptionPane().getTable().getSelectionModel().addListSelectionListener(this);

		departmentComboBox.addActionListener(this);
		iqsCategoryComboBox.addActionListener(this);
		regressionCodeComboBox.addActionListener(this);
		iqsItemComboBox.addActionListener(this);
		zoneComboBox.addActionListener(this);

	}

	public JButton getSubmitButton() {
		return submitButton;
	}
	
	public JButton getQueryButton() {
		return queryButton;
	}
	
	public JButton getQueryDefectButton() {
		return queryByDefectButton;
	}
	
	public JPanel getDefectPropertiesPanel() {
		return defectPropertiesPanel;
	}

	@Override
	public void selected(ListSelectionModel model) {
		if(model.equals(getPartGroupPane().getTable().getSelectionModel())) partGroupSelected();
		else if(model.equals(getDefectGroupPane().getTable().getSelectionModel())) defectGroupSelected();
		else if(model.equals(getInspectionPartDescriptionPane().getTable().getSelectionModel())) partDescriptionSelected();
		else if(model.equals(getDefectTypeDescriptionPane().getTable().getSelectionModel())) defectTypeDescriptionSelected();
		else if(model.equals(getDefectDescriptionPane().getTable().getSelectionModel())) defectDescriptionSelected();
	}

	private void defectDescriptionDeselected() {
		getSubmitButton().setEnabled(false);
	}

	private void defectTypeDescriptionDeselected() {
		/*getSubmitButton().setEnabled(false);
		getDefectPropertiesPanel().setEnabled(false);*/
		enableAddQueryButtons();
		getQueryDefectButton().setEnabled(false);
	}

	private void partDescriptionDeselected() {
		getDefectDescriptionPane().removeData();
		/*getDefectGroupPane().removeData();
		// Clear Defect Type Description when user deselects Part Description
		getDefectTypeDescriptionPane().removeData();*/
		enableAddQueryButtons();
	}

	@Override
	public void deselected(ListSelectionModel model) {
		if(model.equals(getPartGroupPane().getTable().getSelectionModel())) partGroupDeselected();
		else if(model.equals(getDefectGroupPane().getTable().getSelectionModel())) defectGroupDeselected();
		else if(model.equals(getInspectionPartDescriptionPane().getTable().getSelectionModel())) partDescriptionDeselected();
		else if(model.equals(getDefectTypeDescriptionPane().getTable().getSelectionModel())) defectTypeDescriptionDeselected();
		else if(model.equals(getDefectDescriptionPane().getTable().getSelectionModel())) defectDescriptionDeselected();
	}



	private void defectDescriptionSelected() {
		getDefectTypeDescriptionPane().clearSelection();
		getSubmitButton().setText(DELETE_TEXT);
		getSubmitButton().setEnabled(true);
	}

	private void partGroupDeselected() {
		getInspectionPartDescriptionPane().removeData();
		enableAddQueryButtons();
	}
	
	private void defectGroupDeselected() {
		// Clear Defect Type Description when user deselects Defect Group
		getDefectTypeDescriptionPane().removeData();
		enableAddQueryButtons();
		List<InspectionPartDescription> selectedItems = getInspectionPartDescriptionPane().getSelectedItems();
		if(selectedItems.isEmpty()) return;
		
		List<DefectDescription> defectList = getController().selectDefectDescriptions(selectedItems);
		getDefectDescriptionPane().reloadData(defectList);
	}


	private void partGroupSelected() {
		PartGroup partGroup = getPartGroupPane().getSelectedItem();
		if (partGroup == null) return;
		
		getInspectionPartDescriptionPane().removeData();
		InspectionPartDescription criteria = new InspectionPartDescription();
		criteria.setPartGroupName(partGroup.getPartGroupName());
		List<InspectionPartDescription> list = getController().selectInspectionPartDescriptions(criteria);
		if (list == null) return;
		Collections.sort(list, new PropertyComparator<InspectionPartDescription>(InspectionPartDescription.class, "inspectionPartName", "inspectionPartLocationName"));
		getInspectionPartDescriptionPane().reloadData(list);
		enableAddQueryButtons();
	}
	
	private void defectGroupSelected() {
		DefectGroup defectGroup = getDefectGroupPane().getSelectedItem();
		if (defectGroup == null)return;

		DefectTypeDescription criteria = new DefectTypeDescription();
		criteria.setDefectGroupName(defectGroup.getDefectGroupName());
		List<DefectTypeDescription> list = getController().selectDefectTypeDescriptions(criteria);
		
		Collections.sort(list, new PropertyComparator<DefectTypeDescription>(DefectTypeDescription.class, "defectTypeName", "secondaryPartName"));
		getDefectTypeDescriptionPane().reloadData(list);

		List<InspectionPartDescription> selectedValues = getInspectionPartDescriptionPane().getSelectedItems();
		if (selectedValues.isEmpty()) return;

		List<DefectDescription> defectList = getController().selectDefectDescriptions(selectedValues, defectGroup);
		getDefectDescriptionPane().reloadData(defectList);
		enableAddQueryButtons();

	}
	
	private void partDescriptionSelected() {
		List<InspectionPartDescription> selectedValues = getInspectionPartDescriptionPane().getSelectedItems();
		if (selectedValues.isEmpty()) return;

		DefectGroup defectGroup = getDefectGroupPane().getSelectedItem();
		List<DefectDescription> list = null;
		if (defectGroup == null) 
			list = getController().selectDefectDescriptions(selectedValues);
		else
			list = getController().selectDefectDescriptions(selectedValues, defectGroup);
		
		if (getDefectGroupPane().getTable().getModel().getRowCount() == 0) {
			getDefectGroupPane().reloadData(getClientModel().getDefectGroups(), getClientModel().getDefectGroupsUpdateTime(), "getDefectGroupName");
		}
		getDefectDescriptionPane().reloadData(list);
		enableAddQueryButtons();
	}
	
	private void defectTypeDescriptionSelected() {
		getDefectDescriptionPane().clearSelection();
		getSubmitButton().setText(ADD_TEXT);
		//getDefectPropertiesPanel().setEnabled(true);
		getQueryDefectButton().setEnabled(true);
		enableAddQueryButtons();
	}

	private void createDefectDescriptions() {
		List<InspectionPartDescription> parts = getInspectionPartDescriptionPane().getSelectedItems();
		if (parts == null || parts.isEmpty()) return;

		List<DefectTypeDescription> defectTypes = getDefectTypeDescriptionPane().getSelectedItems();
		if (defectTypes == null || defectTypes.isEmpty())return;

		List<DefectDescription> defectDescriptions = new ArrayList<DefectDescription>();
		List<DefectDescription> existingDefectDescriptions = getDefectDescriptionPane().getItems();

		Division department = (Division) departmentComboBox.getSelectedItem();
		Line line = (Line) lineComboBox.getSelectedItem();
		Zone zone = (Zone) zoneComboBox.getSelectedItem();
		Iqs iqs = (Iqs) iqsCategoryComboBox.getSelectedItem();
		//String iqsItemName = iqsItemTextField.getText().trim();
		Iqs iqsItem = (Iqs) iqsItemComboBox.getSelectedItem();
		Regression regression = (Regression) regressionCodeComboBox.getSelectedItem();
		boolean engineFiringFlag = firingFlagCheckBox.isSelected();

		String departmentName = department == null || department.getDivisionId() == null ? null : department.getDivisionName().trim();
		String lineName = line == null || line.getLineId() == null ? null : line.getLineName().trim();
		String zoneName = zone == null || zone.getZoneId() == null ? null : zone.getZoneName().trim();
		String iqsCategoryName = iqs == null || iqs.getIqsCategoryName() == null || iqs.getIqsCategoryName().equals(SELECT_IQS_CAT) ? null : iqs.getIqsCategoryName().trim();
		String iqsItemName = iqsItem == null || iqsItem.getIqsItemName() == null || iqsItem.getIqsItemName().equals(SELECT_IQS_ITEM)? null : iqsItem.getIqsItemName().trim();
		String regressionCode = regression == null || regression.getRegressionCode() == null || regression.getRegressionCode().equals(SELECT_REGRESSION) ? null : regression.getRegressionCode().trim();

		// TODO validate properties here if requested
		
		for (InspectionPartDescription part : parts) {
			for (DefectTypeDescription defectType : defectTypes) {
				DefectDescription defect = constructDefectDescription(part, defectType, departmentName, lineName, zoneName, iqsCategoryName, iqsItemName, regressionCode, engineFiringFlag);
				if (!existingDefectDescriptions.contains(defect)) {
					defectDescriptions.add(defect);
				}
			}
		}

		if (!defectDescriptions.isEmpty()) 
			getController().createDefectDescriptions(defectDescriptions);
		
		getDefectTypeDescriptionPane().clearSelection();
		partDescriptionSelected();
	}
	
	private void deleteDefectDescriptions() {
		List<DefectDescription> selectedValues = getDefectDescriptionPane().getSelectedItems();
		if (selectedValues == null || selectedValues.isEmpty())return;

		getController().deleteDefectDescriptions(selectedValues);
		getDefectDescriptionPane().clearSelection();
		List<DefectDescription> retainedValues = getDefectDescriptionPane().getItems();
		retainedValues.removeAll(selectedValues);
		getDefectDescriptionPane().reloadData(retainedValues);
		
		enableAddQueryButtons();
		
	}
	
	protected DefectDescription constructDefectDescription(InspectionPartDescription part, DefectTypeDescription defectType, String departmentName, String lineName, String zoneName, String iqsCategoryName,
			String iqsItemName, String regressionCode, boolean engineFiringFlag) {
		DefectDescription defect = new DefectDescription();
		defect.getId().setDefectTypeName(defectType.getDefectTypeName());
		defect.getId().setInspectionPartLocationName(part.getInspectionPartLocationName());
		defect.getId().setInspectionPartName(part.getInspectionPartName());
		defect.getId().setPartGroupName(part.getPartGroupName());
		defect.getId().setSecondaryPartName(defectType.getSecondaryPartName());
		defect.setTwoPartDefectFlag(false);
		defect.getId().setTwoPartPairLocation("");
		defect.getId().setTwoPartPairPart("");

		defect.setResponsibleDept(departmentName);
		
		defect.setResponsibleLine(lineName);
		defect.setResponsibleZone(zoneName);
		defect.setIqsCategoryName(iqsCategoryName);
		defect.setIqsItemName(iqsItemName);
		defect.setRegressionCode(regressionCode);
		defect.setEngineFiringFlag(engineFiringFlag);

		// defect.setInventoryRepairTime(inventoryRepairTime) ?
		return defect;
	}
	
	private void departmentSelected() {
		Division division = (Division)departmentComboBox.getSelectedItem();
		if(division == null) return;
		List<Line> lines = null;
		List<Zone> zones = null;
		if (division.getDivisionName().trim().equals(SELECT_DEPARTMENT)) {
			lines = getController().selectLines();
			zones = getController().selectZones();
		} else {
			lines = getController().selectLines(division);
			zones = getController().selectZones(division.getDivisionId());
		}
		Collections.sort(lines, new PropertyComparator<Line>(Line.class, "lineName"));
		Collections.sort(zones, new PropertyComparator<Zone>(Zone.class, "zoneName"));
		lines.add(0, createDummyLine());
		ComboBoxUtils.loadComboBox(lineComboBox, lines);
		zones.add(0, createDummyZone());
		ComboBoxUtils.loadComboBox(zoneComboBox, zones);
		enableAddQueryButtons();
	}
	
	private void iqsSelected() {
		Iqs iqs =  (Iqs) iqsCategoryComboBox.getSelectedItem();
		if(iqs == null) return;
		List<Iqs> iqsItem = null;
		if (iqs.getIqsCategoryName().trim().equals(SELECT_IQS_CAT)) {
			iqsItem = getController().selectDistinctIqsItems();
		}
		else
		{
			iqsItem = getController().findAllByCategoryName(iqs.getIqsCategoryName());
		}
		iqsItem.add(0, createDummyIQS());
		ComboBoxUtils.loadComboBox(iqsItemComboBox, iqsItem);
		//String iqsItem = iqs == null ? "" : iqs.getId().getIqsItemName();
		//iqsItemTextField.setText(iqsItem);
		enableAddQueryButtons();
	}
	
	/* Enable Add button after mandatory fields - Department, Zone, IQS Cat,
	 *  IQS Item and Regression Code are selected
	 */
	private void enableAddQueryButtons(){
		String divisionName = "";
		String zoneName = "";
		String iqsName = "";
		String iqsItemName = "";
		String regressionName = "";


		if (departmentComboBox.getSelectedItem() != null) {
			Division division = (Division) departmentComboBox.getSelectedItem();
			divisionName = division.getDivisionName().trim();
		}
		if (zoneComboBox.getSelectedItem() != null) {
			Zone zone = (Zone) zoneComboBox.getSelectedItem();
			zoneName = zone.getZoneName().trim();
		}
		if (iqsCategoryComboBox.getSelectedItem() != null) {
			Iqs iqs = (Iqs) iqsCategoryComboBox.getSelectedItem();
			iqsName = iqs.getIqsCategoryName().trim();
			//iqsItem = iqs == null ? "" : iqs.getId().getIqsItemName();
		}
		if(iqsItemComboBox.getSelectedItem() != null){
			Iqs iqsItem = (Iqs) iqsItemComboBox.getSelectedItem();
			iqsItemName = iqsItem.getIqsItemName().trim();
		}
		if (regressionCodeComboBox.getSelectedItem() != null) {
			Regression regression = (Regression) regressionCodeComboBox
					.getSelectedItem();
			regressionName = regression.getRegressionCode().trim();
		}
		List<DefectTypeDescription> defectTypes = getDefectTypeDescriptionPane().getSelectedItems();
		List<DefectGroup> defectGroups = getDefectGroupPane().getSelectedItems();
		List<InspectionPartDescription> partTypes = getInspectionPartDescriptionPane().getSelectedItems();
		List<PartGroup> partGroups = getPartGroupPane().getSelectedItems();

		if (divisionName.equals("") || divisionName.equals(SELECT_DEPARTMENT)
				|| zoneName.equals("") || zoneName.equals(SELECT_ZONE)
				|| iqsName.equals("") || iqsName.equals(SELECT_IQS_CAT) 
				|| iqsItemName.equals("") || iqsItemName.equals(SELECT_IQS_ITEM)
				|| regressionName.equals("") || regressionName.equals(SELECT_REGRESSION)
				|| defectTypes == null || defectTypes.isEmpty()
				|| defectGroups == null || defectGroups.isEmpty()
				|| partTypes == null || partTypes.isEmpty()
				|| partGroups == null || partGroups.isEmpty()) {
			getSubmitButton().setEnabled(false);
		} else {
			getSubmitButton().setEnabled(true);
		}
		
		if ((divisionName.equals("") || divisionName.equals(SELECT_DEPARTMENT)) && (zoneName.equals("") || zoneName.equals(SELECT_ZONE))
				&& (iqsName.equals("") || iqsName.equals(SELECT_IQS_CAT)) 
				&& (iqsItemName.equals("") || iqsItemName.equals(SELECT_IQS_ITEM))
				&& (regressionName.equals("") || regressionName.equals(SELECT_REGRESSION))) {
			getQueryButton().setEnabled(false);
		} else {
			getQueryButton().setEnabled(true);
		}

	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(departmentComboBox)) departmentSelected();
		else if(e.getSource().equals(iqsCategoryComboBox)) iqsSelected();
		else if(e.getSource().equals(regressionCodeComboBox)) regressionCodeSelected();
		else if(e.getSource().equals(iqsItemComboBox)) iqsItemSelected();
		if(e.getSource().equals(zoneComboBox)) zoneSelected();
		else if(e.getSource().equals(getQueryDefectButton())) fetchByDefect();
		else if(e.getSource().equals(getQueryButton())) fetchByDefectProperties();
		else if(e.getSource().equals(getSubmitButton())) {
			if(getSubmitButton().getText().equals(ADD_TEXT)) 
				createDefectDescriptions();
			else if(getSubmitButton().getText().equals(DELETE_TEXT))
				deleteDefectDescriptions();
		}
	}

	private void iqsItemSelected() {
		enableAddQueryButtons();
	}

	protected void fetchByDefect() {
		List<DefectTypeDescription> defectTypes = getDefectTypeDescriptionPane().getSelectedItems();
		if (defectTypes == null || defectTypes.isEmpty())return;
		getPartGroupPane().clearSelection();
		List<DefectDescription> list = getController().selectDefectDescriptionsByDefectTypeDescriptions(defectTypes);
		getDefectDescriptionPane().reloadData(list);
	}
	
	private void regressionCodeSelected() {
		enableAddQueryButtons();
	}
	
	private void zoneSelected() {
		enableAddQueryButtons();
	}
	
	protected void fetchByDefectProperties() {
		getPartGroupPane().clearSelection();
		getDefectGroupPane().clearSelection();
		Division department = (Division) departmentComboBox.getSelectedItem();
		Zone zone = (Zone) zoneComboBox.getSelectedItem();
		Iqs iqs = (Iqs) iqsCategoryComboBox.getSelectedItem();
		Iqs iqsItem = (Iqs) iqsItemComboBox.getSelectedItem();
		Regression regression = (Regression) regressionCodeComboBox.getSelectedItem();
		
		String departmentName = department == null || department.getDivisionId() == null ? null : department.getDivisionName().trim();
		String zoneName = zone == null || zone.getZoneId() == null ? null : zone.getZoneName().trim();
		String iqsCategoryName = iqs == null || iqs.getIqsCategoryName() == null || iqs.getIqsCategoryName().equals(SELECT_IQS_CAT) ? null : iqs.getIqsCategoryName().trim();
		String iqsItemName = iqsItem == null || iqsItem.getIqsItemName() == null || iqsItem.getIqsItemName().equals(SELECT_IQS_ITEM) ? null : iqsItem.getIqsItemName().trim();
		String regressionCode = regression == null || regression.getRegressionCode() == null || regression.getRegressionCode().equals(SELECT_REGRESSION) ? null : regression.getRegressionCode().trim();

		List<DefectDescription> list = getController().selectDefectDescriptionsByDefectProperties(
				departmentName, zoneName, iqsCategoryName, iqsItemName, regressionCode);
		getDefectDescriptionPane().reloadData(list);
	}
	
	private void loadDefectDescriptionPane()
	{
		List<InspectionPartDescription> selectedValues = getInspectionPartDescriptionPane().getSelectedItems();
		if (selectedValues.isEmpty()) return;
		DefectGroup defectGroup = getDefectGroupPane().getSelectedItem();
		List<DefectDescription> list = null;
		if (defectGroup == null) 
			list = getController().selectDefectDescriptions(selectedValues);
		else
			list = getController().selectDefectDescriptions(selectedValues, defectGroup);
		
		if (getDefectGroupPane().getTable().getModel().getRowCount() == 0) {
			getDefectGroupPane().reloadData(getClientModel().getDefectGroups(), getClientModel().getDefectGroupsUpdateTime(), "getDefectGroupName");
		}
		getDefectDescriptionPane().reloadData(list);
	}
	
	protected Line createDummyLine() {
		Line dummy = new Line();
		dummy.setLineName(SELECT_LINE);
		return dummy;
	}

	protected Zone createDummyZone() {
		Zone dummy = new Zone();
		dummy.setZoneName(SELECT_ZONE);
		return dummy;
	}
	
	protected Division createDummyDivision() {
		Division dummy = new Division();
		dummy.setDivisionName(SELECT_DEPARTMENT);
		return dummy;
	}
	
	protected Regression createDummyRegression() {
		Regression dummy = new Regression();
		dummy.setRegressionCode(SELECT_REGRESSION);
		return dummy;
	}
	
	protected Iqs createDummyIQS() {
		Iqs dummy = new Iqs();
		IqsId dummyId = new IqsId(SELECT_IQS_CAT, SELECT_IQS_ITEM);
		dummy.setid(dummyId);
		return dummy;
	}
	
	private void loadData() {
		List<Division> departments = new ArrayList<Division>();
		departments.addAll(getClientModel().getDepartments());
		Collections.sort(departments, new PropertyComparator<Division>(Division.class, "divisionName"));
		departments.add(0, createDummyDivision());
		
		List<Line> lines = getController().selectLines();
		Collections.sort(lines, new PropertyComparator<Line>(Line.class, "lineName"));
		lines.add(0, createDummyLine());
				
		List<Zone> zones = getController().selectZones();
		Collections.sort(zones, new PropertyComparator<Zone>(Zone.class, "zoneName"));
		zones.add(0, createDummyZone());
		
		List<Iqs> iqs = getController().selectDistinctIqs();
		iqs.add(0, createDummyIQS());
		
		List<Iqs> iqsItems = getController().selectDistinctIqsItems();
		iqsItems.add(0, createDummyIQS());
		
		List<Regression> regression = getController().selectRegression();
		regression.add(0, createDummyRegression());
		
		ComboBoxUtils.loadComboBox(departmentComboBox, departments);
		ComboBoxUtils.loadComboBox(lineComboBox, lines);
		ComboBoxUtils.loadComboBox(zoneComboBox, zones);
		ComboBoxUtils.loadComboBox(iqsCategoryComboBox, iqs);
		ComboBoxUtils.loadComboBox(iqsItemComboBox, iqsItems);
		ComboBoxUtils.loadComboBox(regressionCodeComboBox, regression);

		if (departments != null && departments.size() > 0) {
			departmentComboBox.setSelectedIndex(0);
		}
		if (lines != null && lines.size() > 0) {
			lineComboBox.setSelectedIndex(0);
		}
		if (zones != null && zones.size() > 0) {
			zoneComboBox.setSelectedIndex(0);
		}
		if (iqs != null && iqs.size() > 0) {
			iqsCategoryComboBox.setSelectedIndex(0);
		}
		if (iqsItems != null && iqsItems.size() > 0) {
			iqsItemComboBox.setSelectedIndex(0);
		}
		if (regression != null && regression.size() > 0) {
			regressionCodeComboBox.setSelectedIndex(0);
		}
		
	}
	
}
