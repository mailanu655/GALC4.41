package com.honda.galc.client.teamleader;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;


import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.MbpnMaintenanceTableModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductIdMaskDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductIdMask;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.StringUtil;

public class MbpnMaintenancePanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;

	private TablePane mbpnPanel;
	private JComboBox mainNoComboBox, classNoComboBox, protoTypeComboBox, typeNoComboBox, supplNoComboBox,
	targetNoComboBox, hesClrComboBox;
	private JTextField maskIdtextField, descriptionTextField;
	private JCheckBox disabledCheckBox;

	private MbpnMaintenanceTableModel mbpnMaintenanceTableModel;
	private List<Mbpn> mbpns = new ArrayList<Mbpn>();
	Mbpn selectedMbpn;
	private MbpnDao mbpnDao;

	public MbpnMaintenancePanel(TabbedMainWindow mainWindow) {
		super("Mbpn - " + mainWindow.getApplicationPropertyBean().getProductType(), KeyEvent.VK_B, mainWindow);
		AnnotationProcessor.process(this);
	}

	@Override
	public void onTabSelected() {
		if (isInitialized)
			return;

		initComponents();
		loadData();

		isInitialized = true;
	}

	protected void initComponents() {

		setLayout(new GridLayout(1, 1));

		JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createMbpnComboBoxSelectionPanel(),
				createButtonPanel());
		splitPane2.setOneTouchExpandable(false);
		splitPane2.setDividerLocation(800);

		JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getMbpnPanel(), splitPane2);
		splitPane1.setOneTouchExpandable(false);
		splitPane1.setDividerLocation(450);

		add(splitPane1);
	}

	private Component getMbpnPanel() {
		mbpnPanel = new TablePane(null);
		mbpnMaintenanceTableModel = new MbpnMaintenanceTableModel(mbpnPanel.getTable(), mbpns);
		mbpnPanel.addListSelectionListener(getListSelectionListener());
		return mbpnPanel;
	}

	private JPanel createMbpnComboBoxSelectionPanel() {
		JPanel panel = new JPanel();
		int rows = MbpnMaintColumns.values().length;
		panel.setLayout(new GridLayout((rows / 2) + 1, 2, 10, 10));

		JPanel mainNoPanel = createPanelWithLabel(MbpnMaintColumns.Main_No.getColumnName(),
				new FlowLayout(FlowLayout.LEFT, 18, 10));
		mainNoComboBox = createComboBox(MbpnMaintColumns.Main_No.name());
		mainNoPanel.add(mainNoComboBox);
		panel.add(mainNoPanel);

		JPanel classNoPanel = createPanelWithLabel(MbpnMaintColumns.Class_No.getColumnName(),
				new FlowLayout(FlowLayout.LEFT, 15, 10));
		classNoComboBox = createComboBox(MbpnMaintColumns.Class_No.name());
		classNoPanel.add(classNoComboBox);
		panel.add(classNoPanel);

		JPanel protoTypePanel = createPanelWithLabel(MbpnMaintColumns.ProtoType_Code.getColumnName(),
				new FlowLayout(FlowLayout.LEFT, 12, 10));
		protoTypeComboBox = createComboBox(MbpnMaintColumns.ProtoType_Code.name());
		protoTypePanel.add(protoTypeComboBox);
		panel.add(protoTypePanel);

		JPanel typeNoPanel = createPanelWithLabel(MbpnMaintColumns.Type_No.getColumnName(),
				new FlowLayout(FlowLayout.LEFT, 18, 10));
		typeNoComboBox = createComboBox(MbpnMaintColumns.Type_No.name());
		typeNoPanel.add(typeNoComboBox);
		panel.add(typeNoPanel);

		JPanel supplNoPanel = createPanelWithLabel(MbpnMaintColumns.Supplementary_No.getColumnName(),
				new FlowLayout(FlowLayout.LEFT, 15, 10));
		supplNoComboBox = createComboBox(MbpnMaintColumns.Supplementary_No.name());
		supplNoPanel.add(supplNoComboBox);
		panel.add(supplNoPanel);

		JPanel targetNoPanel = createPanelWithLabel(MbpnMaintColumns.Target_No.getColumnName(),
				new FlowLayout(FlowLayout.LEFT, 12, 10));
		targetNoComboBox = createComboBox(MbpnMaintColumns.Target_No.name());
		targetNoPanel.add(targetNoComboBox);
		panel.add(targetNoPanel);

		JPanel hesClrPanel = createPanelWithLabel(MbpnMaintColumns.Hes_Color.getColumnName(),
				new FlowLayout(FlowLayout.LEFT, 20, 10));
		hesClrComboBox = createComboBox(MbpnMaintColumns.Hes_Color.name());
		hesClrPanel.add(hesClrComboBox);
		panel.add(hesClrPanel);

		JPanel maskIdPanel = createPanelWithLabel("Mask Id",
				new FlowLayout(FlowLayout.LEFT, 20, 10));
		maskIdtextField = createTextField("Mask_Id");
		maskIdPanel.add(maskIdtextField);
		panel.add(maskIdPanel);
		
		JPanel descriptionPanel = createPanelWithLabel("Description",
				 new FlowLayout(FlowLayout.LEFT, 20, 10));
		descriptionTextField = createTextField("Description");
		descriptionPanel.add(descriptionTextField);
		panel.add(descriptionPanel);

		JPanel disabledPanel = createPanelWithLabel("Disabled",
				new FlowLayout(FlowLayout.LEFT, 20, 10));
		disabledCheckBox = createCheckBox("Disabled");
		disabledPanel.add(disabledCheckBox);
		panel.add(disabledPanel);

		return panel;
	}

	private JCheckBox createCheckBox(String name) {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setName(name);
		checkBox.addActionListener(this);

		return checkBox;
	}

	private JComboBox createComboBox(String name) {
		JComboBox comboBox = new JComboBox();
		comboBox.setName("J" + name + "List");
		comboBox.setEditable(true);
		comboBox.addActionListener(this);

		return comboBox;
	}

	private JPanel createPanelWithLabel(String name, FlowLayout layout) {
		JPanel panel = new JPanel();
		panel.setLayout(layout);
		panel.add(UiFactory.createLabel(name, UiFactory.getDefault().getLabelFont(), SwingConstants.CENTER));

		return panel;
	}

	private JTextField createTextField(String name) {
		JTextField text = new JTextField();
		text.setName(name);
		text.setSize(100,30);
		text.setPreferredSize(new Dimension(100,30));
		text.setEditable(true);
		text.addActionListener(this);

		return text;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 50));

		JButton saveButton = UiFactory.createButton("Save", UiFactory.getDefault().getButtonFont(), true);
		saveButton.addActionListener(this);

		JButton cancelButton = UiFactory.createButton("Cancel", UiFactory.getDefault().getButtonFont(), true);
		cancelButton.addActionListener(this);

		panel.add(saveButton);
		panel.add(cancelButton);

		return panel;
	}

	protected void reset() {
		mbpnPanel.getTable().clearSelection();
		mainNoComboBox.setSelectedItem("");
		classNoComboBox.setSelectedItem("");
		protoTypeComboBox.setSelectedItem("");
		typeNoComboBox.setSelectedItem("");
		supplNoComboBox.setSelectedItem("");
		targetNoComboBox.setSelectedItem("");
		hesClrComboBox.setSelectedItem("");
		maskIdtextField.setText("");
		descriptionTextField.setText("");
		disabledCheckBox.setSelected(false);
	}

	protected void clearAll() {
		mainNoComboBox.setModel(new ComboBoxModel<String>(new ArrayList<String>()));
		classNoComboBox.setModel(new ComboBoxModel<String>(new ArrayList<String>()));
		protoTypeComboBox.setModel(new ComboBoxModel<String>(new ArrayList<String>()));
		typeNoComboBox.setModel(new ComboBoxModel<String>(new ArrayList<String>()));
		supplNoComboBox.setModel(new ComboBoxModel<String>(new ArrayList<String>()));
		targetNoComboBox.setModel(new ComboBoxModel<String>(new ArrayList<String>()));
		hesClrComboBox.setModel(new ComboBoxModel<String>(new ArrayList<String>()));
		maskIdtextField.setText("");
		descriptionTextField.setText("");
		disabledCheckBox.setSelected(false);
	}

	protected void save() {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Mbpn mbpn = createMbpn();
		if(mbpn == null) return;
		Mbpn mbpnFromDatabase = mbpnDao.findByKey(mbpn.getProductSpecCode());
		try {
			if (mbpnFromDatabase == null) {
				if (mbpn.getMainNo().trim().length() > 0 && mbpn.getClassNo().trim().length() > 0
						&& mbpn.getProductSpecCode().matches("[a-zA-Z0-9 ]*")) {
					mbpnDao.save(mbpn);
					logUserAction(SAVED, mbpn);
				} else {
					MessageDialog.showError(mbpn.getProductSpecCode() + " is not valid Mbpn");
				}
			} else {
				if(updateRequired(mbpn, mbpnFromDatabase)){
					final String validation = validateDisabledState(mbpn, mbpnFromDatabase);
					if (StringUtils.isEmpty(validation)) {
						mbpnDao.save(mbpn);
						logUserAction(SAVED, mbpn);
					} else {
						MessageDialog.showError(mbpn.getProductSpecCode() + " cannot be disabled due to existing records:\n" + validation);
					}
				} else {
					Logger.getLogger().info(mbpn.getProductSpecCode() + " Mbpn already exists in database no need to save");
				}
			}
		} catch (Exception e) {
			Logger.getLogger().error(e.getMessage());
		} finally {
			this.setCursor(Cursor.getDefaultCursor());
		}
	}

	private boolean updateRequired(Mbpn mbpn, Mbpn mbpnFromDatabase) {
		return (!mbpn.getMaskId().equalsIgnoreCase(mbpnFromDatabase.getMaskId()))
				|| (!mbpn.getDescription().equals(mbpnFromDatabase.getDescription()))
				|| (mbpn.isDisabled() != mbpnFromDatabase.isDisabled());
	}

	/**
	 * Returns messages with counts of existing records for the given MBPN
	 * which prevent its disabled state from being changed, or returns a
	 * null/empty String signifying that its disabled state may change.
	 */
	private String validateDisabledState(final Mbpn mbpn, final Mbpn mbpnFromDatabase) {
		if (mbpn.isDisabled() == mbpnFromDatabase.isDisabled()) {
			return null; // no validation is required when disabled state did not change
		}
		if (!mbpn.isDisabled()) {
			return null; // no validation is required for re-enabling a disabled MBPN
		}
		final int lotControlRuleCount; {
			List<LotControlRule> lotControlRules = ServiceFactory.getDao(LotControlRuleDao.class).findAllForProductSpecCode(mbpn.getProductSpecCode());
			lotControlRuleCount = lotControlRules == null ? 0 : lotControlRules.size();
		}
		final int buildAttributeCount; {
			List<BuildAttribute> buildAttributes = ServiceFactory.getDao(BuildAttributeDao.class).findAllByProductSpecCode(mbpn.getProductSpecCode());
			buildAttributeCount = buildAttributes == null ? 0 : buildAttributes.size();
		}
		final int productIdMaskCount; {
			List<ProductIdMask> productIdMasks = ServiceFactory.getDao(ProductIdMaskDao.class).findAllByProductSpecCode(mbpn.getProductSpecCode());
			productIdMaskCount = productIdMasks == null ? 0 : productIdMasks.size();
		}
		final int requiredPartCount; {
			List<RequiredPart> requiredParts = ServiceFactory.getDao(RequiredPartDao.class).findAllByProductSpecCode(mbpn.getProductSpecCode());
			requiredPartCount = requiredParts == null ? 0 : requiredParts.size();
		}
		final int preProductionLotCount; {
			List<PreProductionLot> preProductionLots = ServiceFactory.getDao(PreProductionLotDao.class).findAllUpcomingLotsByProductSpecCode(mbpn.getProductSpecCode());
			preProductionLotCount = preProductionLots == null ? 0 : preProductionLots.size();
		}
		StringBuilder countBuilder = new StringBuilder();
		appendCount(countBuilder, LotControlRule.class.getSimpleName(), lotControlRuleCount);
		appendCount(countBuilder, BuildAttribute.class.getSimpleName(), buildAttributeCount);
		appendCount(countBuilder, ProductIdMask.class.getSimpleName(), productIdMaskCount);
		appendCount(countBuilder, RequiredPart.class.getSimpleName(), requiredPartCount);
		appendCount(countBuilder, PreProductionLot.class.getSimpleName(), preProductionLotCount);
		return countBuilder.toString();
	}



	private void appendCount(StringBuilder countBuilder, String entityName, int count) {
		if (count <= 0) {
			return;
		}
		if (countBuilder.length() > 0) {
			countBuilder.append("\n");
		}
		countBuilder.append(entityName);
		countBuilder.append(" (");
		countBuilder.append(count);
		countBuilder.append(")");
	}

	private void loadData() {
		clearAll();
		mbpnDao = ServiceFactory.getDao(MbpnDao.class);
		mbpns = mbpnDao.findAllProductSpecCodesOnly(ProductType.MBPN.name());
		List<String> mainNoList = new ArrayList<String>();

		for (Mbpn mbpn : mbpns) {
			mainNoList = populateList(mainNoList, mbpn.getMainNo(), mbpn.getMainNo(), mbpn.getMainNo());
		}
		mainNoComboBox.setModel(new ComboBoxModel<String>(mainNoList));
		mbpnMaintenanceTableModel.refresh(mbpns);
		reset();
	}

	protected void loadMbpnColumnComboBox(String itemValue, String columnName) {
		MbpnMaintColumns column = MbpnMaintColumns.valueOf(columnName);
		List<String> comboList = new ArrayList<String>();
		JComboBox comboBox = null;
		switch (column) {
		case Main_No:

			for (Mbpn mbpn : mbpns) {
				comboList = populateList(comboList, mbpn.getClassNo(), mbpn.getMainNo(), itemValue);
			}
			comboBox = classNoComboBox;

			break;
		case Class_No:

			for (Mbpn mbpn : mbpns) {
				comboList = populateList(comboList, mbpn.getPrototypeCode(), mbpn.getClassNo(), itemValue);
			}
			comboBox = protoTypeComboBox;

			break;
		case ProtoType_Code:

			for (Mbpn mbpn : mbpns) {
				comboList = populateList(comboList, mbpn.getTypeNo(), mbpn.getPrototypeCode(), itemValue);
			}
			comboBox = typeNoComboBox;

			break;
		case Type_No:

			for (Mbpn mbpn : mbpns) {
				comboList = populateList(comboList, mbpn.getSupplementaryNo(), mbpn.getTypeNo(), itemValue);
			}
			comboBox = supplNoComboBox;

			break;
		case Supplementary_No:

			for (Mbpn mbpn : mbpns) {
				comboList = populateList(comboList, mbpn.getTargetNo(), mbpn.getSupplementaryNo(), itemValue);
			}
			comboBox = targetNoComboBox;

			break;
		case Target_No:

			for (Mbpn mbpn : mbpns) {
				comboList = populateList(comboList, mbpn.getHesColor(), mbpn.getTargetNo(), itemValue);
			}
			comboBox = hesClrComboBox;
			break;
		case Hes_Color:
			break;
		case Disabled:
			break;
		default:
			break;
		}

		if(comboBox != null) comboBox.setModel(new ComboBoxModel<String>(comboList));
		if (comboList.size() == 1 && column.ordinal() < MbpnMaintColumns.values().length) {
			loadMbpnColumnComboBox("", column.next().name());
		}

	}

	private ListSelectionListener getListSelectionListener() {
		return new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					selectedMbpn = mbpnMaintenanceTableModel == null ? null : mbpnMaintenanceTableModel.getSelectedItem();

					if (selectedMbpn != null) {
						mainNoComboBox.setSelectedItem(selectedMbpn.getMainNo());
						classNoComboBox.setSelectedItem(selectedMbpn.getClassNo());
						protoTypeComboBox.setSelectedItem(selectedMbpn.getPrototypeCode());
						typeNoComboBox.setSelectedItem(selectedMbpn.getTypeNo());
						supplNoComboBox.setSelectedItem(selectedMbpn.getSupplementaryNo());
						targetNoComboBox.setSelectedItem(selectedMbpn.getTargetNo());
						hesClrComboBox.setSelectedItem(selectedMbpn.getHesColor());
						maskIdtextField.setText(selectedMbpn.getMaskId());
						descriptionTextField.setText(selectedMbpn.getDescription());
						disabledCheckBox.setSelected(selectedMbpn.isDisabled());
					}
				}

			}
		};
	}

	public void actionPerformed(ActionEvent e) {
		Object eventSource = e.getSource();
		if (eventSource instanceof JComboBox) {
			JComboBox source = ((JComboBox) eventSource);
			String value = (String) source.getSelectedItem();
			String name = source.getName();
			String columnName = name.substring(1, name.length() - 4);

			if (isInitialized && (value.length() > 0) && isvalid(value, columnName)) {
				loadMbpnColumnComboBox(value, columnName);
			} else {
				source.setSelectedItem("");
				source.requestFocus();
			}
		}else if (eventSource instanceof JButton) {
			JButton source = ((JButton) eventSource);
			String name = source.getText();

			if (name.equalsIgnoreCase("Save")) {
				save();
				loadData();
			} else if (name.equalsIgnoreCase("Cancel")) {
				reset();
			}
		}else if(eventSource instanceof JTextField) {
			JTextField source = ((JTextField) eventSource);
			String mask = source.getText();

			if(mask.trim().length() > 2){
				MessageDialog.showError(" Mask Id Cannot be greater than 2 characters in length");
				source.setText("");
				source.requestFocus();
			}
		}

	}

	private List<String> populateList(List<String> list, String value, String mbpnValue, String selectedValue) {
		if (mbpnValue.equalsIgnoreCase(selectedValue)) {
			if (list.isEmpty())
				list.add(" ");
			if (!list.contains(value.trim()) && value.trim().length() > 0)
				list.add(value);
		}
		Collections.sort(list);
		return list;
	}

	protected boolean isvalid(String value, String columnName) {
		boolean flag = true;
		MbpnMaintColumns column = MbpnMaintColumns.valueOf(columnName);
		int columnSize = 0;
		boolean emptyAllowed = true;
		if (column.equals(MbpnMaintColumns.Main_No) || column.equals(MbpnMaintColumns.Class_No)) {
			emptyAllowed = false;
		}
		for(MbpnDef mbpnDef:MbpnDef.values()){
			if(mbpnDef.name().equalsIgnoreCase(column.name())){
				columnSize= mbpnDef.getLength();
			}
		}
		if (!isValidBuildAttribute(value, columnName, columnSize, emptyAllowed)) {
			flag = false;
		}
		return flag;
	}

	boolean isValidBuildAttribute(String value, String columnName, int length, boolean emptyAllowed) {
		if (!emptyAllowed && !(value.trim().length() > 0)) {
			MessageDialog.showError(columnName + " cannot be blank ");
			return false;
		}
		if (value != null && value.length() > length) {
			MessageDialog.showError(columnName + " cannot be greater than " + length + " characters in length ");
			return false;
		}

		if (value != null && value.length() > 0 && !value.matches("[a-zA-Z0-9 ]*")) {
			MessageDialog.showError(columnName + " cannot have special characters ");
			return false;
		}

		return true;
	}

	private Mbpn createMbpn() {
		Mbpn mbpn = new Mbpn();

		String mainNo = (String) mainNoComboBox.getSelectedItem();
		String classNo = (String) classNoComboBox.getSelectedItem();
		String protoTypeCode = (String) protoTypeComboBox.getSelectedItem();
		String typeNo = (String) typeNoComboBox.getSelectedItem();
		String supplementaryNo = (String) supplNoComboBox.getSelectedItem();
		String targetNo = (String) targetNoComboBox.getSelectedItem();
		String hesColor = (String) hesClrComboBox.getSelectedItem();
		String maskId =  maskIdtextField.getText();
		String description = descriptionTextField.getText();
		boolean disabled = disabledCheckBox.isSelected();
		if(maskId.trim().length() > 2){
			MessageDialog.showError(" Mask Id Cannot be greater than 2 characters in length");
			return null;
		}
		if(description.length() > 128){
			MessageDialog.showError(" Description Cannot be greater than 128 characters in length");
			return null;
		}

		mbpn.setMainNo(mainNo != null ? mainNo.toUpperCase() : " ");
		mbpn.setClassNo(classNo != null ? classNo.toUpperCase() : " ");
		mbpn.setPrototypeCode(protoTypeCode != null ? protoTypeCode.toUpperCase() : " ");
		mbpn.setTypeNo(typeNo != null ? typeNo.toUpperCase() : " ");
		mbpn.setSupplementaryNo(supplementaryNo != null ? supplementaryNo.toUpperCase() : " ");
		mbpn.setTargetNo(targetNo != null ? targetNo.toUpperCase() : " ");
		mbpn.setHesColor(hesColor != null ? hesColor.toUpperCase() : " ");
		mbpn.setMaskId(maskId);
		mbpn.setDescription(description);
		mbpn.setDisabled(disabled);


		String mbpnString = StringUtil.padRight(mbpn.getMainNo(), MbpnDef.MAIN_NO.getLength(), ' ', false)
				+ StringUtil.padRight(mbpn.getClassNo(), MbpnDef.CLASS_NO.getLength(), ' ', false)
				+ StringUtil.padRight(mbpn.getPrototypeCode(), MbpnDef.PROTOTYPE_CODE.getLength(), ' ', false)
				+ StringUtil.padRight(mbpn.getTypeNo(), MbpnDef.TYPE_NO.getLength(), ' ', false)
				+ StringUtil.padRight(mbpn.getSupplementaryNo(), MbpnDef.SUPPLEMENTARY_NO.getLength(), ' ', false)
				+ StringUtil.padRight(mbpn.getTargetNo(), MbpnDef.TARGET_NO.getLength(), ' ', false);
		String productSpecCode = mbpnString + ' '+mbpn.getHesColor();

		mbpn.setMbpn(mbpnString);
		mbpn.setProductSpecCode(productSpecCode);

		return mbpn;
	}
	
	public enum MbpnMaintColumns {
		Main_No("Main No"),Class_No("Class No"), ProtoType_Code("ProtoType"),Type_No("Type No"),
		Supplementary_No("Suppl No"), Target_No("Target No"),Hes_Color("Hes Clr"), Disabled("Disabled");

		private String columnName;
		private static MbpnMaintColumns[] vals = values();
		
		
		private MbpnMaintColumns(String columnName){
			this.columnName = columnName;
		}
		
		public MbpnMaintColumns next() {
			return vals[(this.ordinal() + 1)%vals.length];
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		
	}
}
