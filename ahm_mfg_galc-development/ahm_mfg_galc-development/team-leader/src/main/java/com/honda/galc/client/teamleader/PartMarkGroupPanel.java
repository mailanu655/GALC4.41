package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.FilteredLabeledComboBox;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.conf.BuildAttributeDefinitionDao;
import com.honda.galc.dao.conf.BuildSheetPartGroupDao;
import com.honda.galc.dao.conf.PrintFormDao;
import com.honda.galc.dao.product.ModelGroupingDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BuildSheetPartGroup;
import com.honda.galc.entity.conf.BuildSheetPartGroupId;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;

public class PartMarkGroupPanel extends TabbedPanel implements ListSelectionListener,ActionListener {

	private static final long serialVersionUID = 1L;

	private LabeledComboBox formComboBox;
	private LabeledComboBox modelGroupComboBox;
	private List<String> form;
	private List<String> modelGroupList = new ArrayList<String>();
	
	private DataContainer dc;

	private ObjectTablePane<BuildSheetPartGroup> printFormTablePane;
	private List<BuildSheetPartGroup> matrix;
	
	protected JButton saveButton = null;
	protected JButton deleteButton = null;
	protected JButton copyButton = null;
	
	private LabeledComboBox modelGroupComboBoxSub = new LabeledComboBox("Model Group");
	private FilteredLabeledComboBox attributeComboBoxSub = new FilteredLabeledComboBox("Build Attribute");
	private LabeledComboBox rowComboBox = new LabeledComboBox("ROW");
	private LabeledComboBox columnComboBox = new LabeledComboBox("COLUMN");
	private List<String> rowList = new ArrayList<String>();
	private List<String> columnList = new ArrayList<String>();
	
	private List<String> modelGroupListSub = new ArrayList<String>();
	private List<String> attributeListSub = new ArrayList<String>();
	
	private PartMarkGroupDialog partMarkGroupDialog;
	
	public PartMarkGroupPanel(TabbedMainWindow mainWindow) {
		super("PartMarkGroupPanel - " + mainWindow.getApplicationPropertyBean().getProductType(), KeyEvent.VK_G, mainWindow);
		AnnotationProcessor.process(this);
	}	

	@Override
	public void onTabSelected() {
		if (isInitialized)
			return;
		loadData();
		initComponents();
		addListeners();
		isInitialized = true;
	}
	
	protected void initComponents() {

		setLayout(new GridLayout(1, 2));

		JSplitPane splitPane4 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createMbpnComboBoxSelectionPanel(), createButtonPanel());
		splitPane4.setOneTouchExpandable(false);
		splitPane4.setDividerLocation(400);

		JSplitPane splitPane3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, printFormTablePane=createPrintFormTablePane(), splitPane4);
		splitPane3.setOneTouchExpandable(false);
		splitPane3.setDividerLocation(850);

		JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getmodelGroupComboBox(), splitPane3);
		splitPane1.setOneTouchExpandable(false);
		splitPane1.setDividerLocation(50);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getFormComboBox(), splitPane1);
		splitPane.setOneTouchExpandable(false);
		splitPane.setDividerLocation(50);
		
		add(splitPane);
	}
	
	private LabeledComboBox getFormComboBox() {
		if (formComboBox == null) {
			formComboBox = new LabeledComboBox("Forms");
			formComboBox.getComponent().setName("FormCombobox");
			ComboBoxModel<String> model = new ComboBoxModel<String>(form);
			formComboBox.getComponent().setModel(model);
			formComboBox.getComponent().setSelectedIndex(-1);
			formComboBox.getComponent().setRenderer(model);
			formComboBox.setLabelPreferredWidth(150);
		}
		return formComboBox;
	}
	
	private LabeledComboBox getmodelGroupComboBox() {
		if (modelGroupComboBox == null) {
			modelGroupComboBox = new LabeledComboBox("Model Group");
			modelGroupComboBox.getComponent().setName("FormCombobox1");
			ComboBoxModel<String> model = new ComboBoxModel<String>(modelGroupList);
			modelGroupComboBox.getComponent().setModel(model);
			modelGroupComboBox.getComponent().setSelectedIndex(-1);
			modelGroupComboBox.getComponent().setRenderer(model);
			modelGroupComboBox.setLabelPreferredWidth(150);
		}
		return modelGroupComboBox;
	}
	
	private void loadData() {
		try {
			dc = new DefaultDataContainer();
			form = ServiceFactory.getDao(PrintFormDao.class)
					.findDistinctForms();
			dc.put(DataContainerTag.TERMINAL, ApplicationContext.getInstance()
					.getHostName());
			
			String hostName = getMainWindow().getApplicationContext().getTerminal().getHostName();
			String system = PropertyService.getPropertyBean(FrameLinePropertyBean.class, hostName).getBuildSheetModelGroupingSystem();
			List<String> list = ServiceFactory.getDao(ModelGroupingDao.class).findRecentModelGroupsBySystem(system);
			modelGroupList.addAll(list);
			modelGroupListSub = modelGroupList;
			attributeListSub = ServiceFactory.getDao(BuildAttributeDefinitionDao.class).findAllAttributes();
			
		} catch (Exception e) {
			getLogger().info("Load Data" + e);
		}
	}
	
	private ObjectTablePane<BuildSheetPartGroup> createPrintFormTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Model Group", "modelGroup")
			.put("Attribute", "attribute")
			.put("Row","row")
			.put("Column","column");
		
		ObjectTablePane<BuildSheetPartGroup> tablePane = new ObjectTablePane<BuildSheetPartGroup>(clumnMappings.get(),true,true);
		tablePane.setBorder(new TitledBorder("Part Matrix Group List"));
		configureTablePane(tablePane);
		return tablePane;
	}
	
	private void configureTablePane(ObjectTablePane<?> tablePane) {
		tablePane.getTable().setRowHeight(22);
		tablePane.getTable().setFont(Fonts.FONT_BOLD("sansserif",14));
		tablePane.setAlignment(JLabel.CENTER);
		tablePane.getTable().setSelectionBackground(Color.green);
		tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));
		saveButton = UiFactory.createButton("Save", UiFactory.getDefault().getButtonFont(), true);
		deleteButton = UiFactory.createButton("Delete", UiFactory.getDefault().getButtonFont(), true);
		copyButton = UiFactory.createButton("Copy", UiFactory.getDefault().getButtonFont(), true);
		panel.add(saveButton);
		panel.add(deleteButton);
		panel.add(copyButton);
		return panel;
	}
	
	private JPanel createMbpnComboBoxSelectionPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(7, 1, 10, 10));
		int labelWidth = 150;
		modelGroupComboBoxSub.setModel(new ComboBoxModel<String>(modelGroupListSub), -1);
		modelGroupComboBoxSub.setLabelPreferredWidth(labelWidth);
		attributeComboBoxSub.setModel(new ComboBoxModel<String>(attributeListSub), -1);
		attributeComboBoxSub.getComponent().getSelectedIndex();
		attributeComboBoxSub.setLabelPreferredWidth(labelWidth);
		attributeComboBoxSub.getComponent().setEditable(true);
		rowComboBox.setLabelPreferredWidth(labelWidth);
		columnComboBox.setLabelPreferredWidth(labelWidth);
		
		panel.add(modelGroupComboBoxSub,"wrap");
		panel.add(attributeComboBoxSub,"wrap");
		panel.add(rowComboBox,"wrap");
		panel.add(columnComboBox,"wrap");
		
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(formComboBox.getComponent())){
			this.modelGroupComboBox.getComponent().setSelectedIndex(-1);
			rowList.clear();
			columnList.clear();
			loadForm();
		} else if(e.getSource().equals(modelGroupComboBox.getComponent())){ 
			loadForm();
		} else if(e.getSource().equals(saveButton)){
			saveChange();
		} else if(e.getSource().equals(deleteButton)){
			deletePrintForm();
		} else if(e.getSource().equals(copyButton)){
			copyAllPrintForm();
		}
		
	}
	
	private void copyAllPrintForm() {
		
		if (matrix == null || matrix.isEmpty()) {
			JOptionPane.showMessageDialog(PartMarkGroupPanel.this, "Please display at least one row.");
			return;
		}
		getPartMarkGroupDialog().showDialog(matrix, true);
		
	}
	
	private void loadForm() {
		clearSubValues();
		if(formComboBox.getComponent().getSelectedItem() != null || modelGroupComboBox.getComponent().getSelectedItem() != null) {
			String formId = (String)formComboBox.getComponent().getSelectedItem();
			String modelGroup = (String)modelGroupComboBox.getComponent().getSelectedItem();
			this.matrix = getDao(BuildSheetPartGroupDao.class).findAllByFormId(formId, modelGroup);					
			this.printFormTablePane.reloadData(this.matrix);
			
			String formIDselected = (String)formComboBox.getComponent().getSelectedItem();
			String maxRow = PropertyService.getProperty(formIDselected, "NUMBER_OF_ROWS", "6");
			String maxColumn = PropertyService.getProperty(formIDselected, "NUMBER_OF_COLUMNS", "6");
			
			Vector<String> rowIds = new Vector<String>();
			for (int i = 1; i <= Integer.parseInt(maxRow); i++) {
				rowIds.add(String.valueOf(i));
			} 
			
			Vector<String> columnIds = new Vector<String>();
			for (int i = 1; i <= Integer.parseInt(maxColumn); i++) {
				columnIds.add(String.valueOf(i));
			} 
			if(rowList.isEmpty() || columnList.isEmpty()){
				rowList.addAll(rowIds);
				columnList.addAll(columnIds);
			} 
			rowComboBox.setModel(new ComboBoxModel<String>(rowList), 0);
			columnComboBox.setModel(new ComboBoxModel<String>(columnList), 0);
		}
	}
	
	private void deletePrintForm() {
		BuildSheetPartGroup form = printFormTablePane.getSelectedItem();
		if(form == null) return;
		if(MessageDialog.confirm(this, "Are you sure you want to delete the selected Build Sheet Attribute : "+form.getId().getAttribute())) {
			getDao(BuildSheetPartGroupDao.class).remove(form);
			logUserAction(REMOVED, form);
			AuditLoggerUtil.logAuditInfo(form,null, "delete", getScreenName(), getUserName().toUpperCase(), "GALC", "GALC_Maintenance");
			setMessage("Build Sheet Matrix " + form + "is deleted");
			refreshData();
		}	
	}
	
	private void saveChange() {
		
		String modelGroup = (String) modelGroupComboBoxSub.getComponent().getSelectedItem();
		String attribute = (String) attributeComboBoxSub.getComponent().getSelectedItem();
		String rowNum =  (String) rowComboBox.getComponent().getSelectedItem();
		String colNum =  (String) columnComboBox.getComponent().getSelectedItem();
		String formName = StringUtils.trim((String)formComboBox.getComponent().getSelectedItem());
		if(StringUtils.isEmpty(formName)) {
			MessageDialog.showError("Form ID is Empty");
			return;
		}
		if(StringUtils.isEmpty(modelGroup)) {
			MessageDialog.showError("Model Group is Empty");
			return;
		}
		if(StringUtils.isEmpty(attribute)) {
			MessageDialog.showError("Attribute is Empty");
			return;
		}
		if(StringUtils.isEmpty(rowNum)) {
			MessageDialog.showError("Row is Empty");
			return;
		}
		if(StringUtils.isEmpty(colNum)) {
			MessageDialog.showError("Column is Empty");
			return;
		}
		
		BuildSheetPartGroup form = new BuildSheetPartGroup();
		BuildSheetPartGroupId id = new BuildSheetPartGroupId();
		id.setAttribute(StringUtils.trim(attribute));
		id.setFormId(StringUtils.trim(formName));
		id.setModelGroup(StringUtils.trim(modelGroup));
		form.setRow(Integer.parseInt(rowNum));
		form.setColumn(Integer.parseInt(colNum));
		form.setId(id);
		if(MessageDialog.confirm(this, "Are you sure you want to save ?")) {
			BuildSheetPartGroup oldForm = getDao(BuildSheetPartGroupDao.class).findByKey(id);
			getDao(BuildSheetPartGroupDao.class).save(form);
			logUserAction(SAVED, form);
			if(oldForm!=null) {
				AuditLoggerUtil.logAuditInfo(oldForm,form, "update", getScreenName(), getUserName().toUpperCase(), "GALC", "GALC_Maintenance");
				setMessage("Form Id " + formName + " , Attribute " + attribute + " is updated ");
			}else {
				AuditLoggerUtil.logAuditInfo(null,form, "save", getScreenName(), getUserName().toUpperCase(), "GALC", "GALC_Maintenance");
				setMessage("Form Id " + formName + " , Attribute " + attribute + " is created !");
			}
			refreshData();
		}
	}
	
	private void refreshData() {
		if(formComboBox.getComponent().getSelectedItem() != null) {
			String formIDselected = (String)formComboBox.getComponent().getSelectedItem(); 
			String modelGroup = (String)modelGroupComboBox.getComponent().getSelectedItem();
			this.matrix = getDao(BuildSheetPartGroupDao.class).findAllByFormId(formIDselected, modelGroup);
			this.printFormTablePane.reloadData(this.matrix);
			int selectionIndex = printFormTablePane.getTable().getSelectedRow();
			printFormTablePane.clearSelection();
			printFormTablePane.getTable().getSelectionModel().setSelectionInterval(selectionIndex, selectionIndex);
			clearSubValues();
		}
	}
	
	private void clearSubValues(){
		this.modelGroupComboBoxSub.getComponent().setSelectedIndex(-1);
		this.attributeComboBoxSub.getComponent().setSelectedIndex(-1);
		this.rowComboBox.getComponent().setSelectedIndex(-1);
		this.columnComboBox.getComponent().setSelectedIndex(-1);
		printFormTablePane.clearSelection();
	}

	public void valueChanged(ListSelectionEvent e) {
		BuildSheetPartGroup form = printFormTablePane.getSelectedItem();
		if(form == null) return;

		this.modelGroupComboBoxSub.setModel(this.modelGroupListSub, modelGroupListSub.indexOf(form.getModelGroup().trim()));
		this.attributeComboBoxSub.setModel(new ComboBoxModel<String>(attributeListSub), attributeListSub.indexOf(form.getAttribute().trim()));
		this.rowComboBox.setModel(this.rowList, rowList.indexOf(String.valueOf(form.getRow())));
		this.columnComboBox.setModel(this.columnList, columnList.indexOf(String.valueOf(form.getColumn())));
	}
	
	private void addListeners() {
		getFormComboBox().getComponent().addActionListener(this);
		getmodelGroupComboBox().getComponent().addActionListener(this);
		modelGroupComboBoxSub.getComponent().addActionListener(this);
		attributeComboBoxSub.getComponent().addActionListener(this);
		saveButton.addActionListener(this);
		deleteButton.addActionListener(this);
		copyButton.addActionListener(this);
		printFormTablePane.getTable().getSelectionModel().addListSelectionListener(this);
		
	}
	
	private PartMarkGroupDialog getPartMarkGroupDialog() {
		String hostName = getMainWindow().getApplicationContext().getTerminal().getHostName();
		String system = PropertyService.getPropertyBean(FrameLinePropertyBean.class, hostName).getBuildSheetModelGroupingSystem();
		if (this.partMarkGroupDialog == null) {
			this.partMarkGroupDialog = new PartMarkGroupDialog(getMainWindow(), system);
		}
		return this.partMarkGroupDialog;
	}
	
}
