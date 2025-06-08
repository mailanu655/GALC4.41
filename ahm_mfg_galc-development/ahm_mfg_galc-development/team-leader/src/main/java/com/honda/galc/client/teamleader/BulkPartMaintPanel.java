package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dao.product.PartsLoadingMaintenanceDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartsLoadingMaintenance;
import com.honda.galc.service.ServiceFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BulkPartMaintPanel extends TabbedPanel implements  ListSelectionListener, TableModelListener, ActionListener{

	private static final long serialVersionUID = 1L;
	private static final String PROPERTY_VALUE = "BULK_PARTS";
	private static final String PROPERTY_KEY = "TRUE";
	private static final String ADD_TO_BIN = "Add New Part To Bin";
	private static final String CHANGE_BIN_NAME = "Change Bin Name";
	private static final String DELETE_BIN = "Delete Bin";
	
	private String DB_ERROR_OCCURED = "A database error has occured in method: ";
	private String UNKNOWN_ERROR_OCCURED = "An unknown error had occured in method: ";


	private boolean isDeleteBin = false;

	private TablePane partNamePanel = new TablePane("Part Info List");
	private TablePane partSpecForPartPanel = new TablePane("Part Spec Info");
	private TablePane assignPartToBinPanel = new TablePane("Bin Selection");
	private TablePane partSpecForSelectedBinPanel = new TablePane("Part Spec For Selected Bin");

	private LabeledComboBox processPointComboBox;
	private LabeledTextField binNameTextField;

	private JButton createBinBtn;

	private List<ProcessPoint> processPoint  = null;
	private List<LotControlRule> lotControlRuleList = null;
	private List<PartSpec> partSpecList = new  ArrayList<PartSpec>();
	private List<PartsLoadingMaintenance> partLoadingList = null;
	private List<PartSpec> partSpecForSelectedBinList = new  ArrayList<PartSpec>();

	private LotControlRulePartTableModel lotControlRuleTableModel;
	private PartSpecTableModel partSpecTableModel;
	private PartsLoadingMaintenanceTableModel partsLoadingMaintTableModel;
	private PartSpecTableModel partSpecForSelectedBinTableModel;

	public BulkPartMaintPanel() {
		super("BULK_PART_MAINTENANCE", KeyEvent.VK_P);
		try{
			processPoint = ServiceFactory.getDao(ProcessPointDao.class).findAllByDbProperty(PROPERTY_VALUE, PROPERTY_KEY);
		}catch(Exception e){
			String msg = DB_ERROR_OCCURED + "BulkPartMaintPanel";
			getLogger().error(e, msg);
		}
		initComponents();
		addActionListeners();
		addListeners();
	}

	public BulkPartMaintPanel(TabbedMainWindow mainWindow) {
		super("BULK_PART_MAINTENANCE", KeyEvent.VK_P,mainWindow);
		try{
			processPoint = ServiceFactory.getDao(ProcessPointDao.class).findAllByDbProperty(PROPERTY_VALUE, PROPERTY_KEY);
		}catch(Exception e){

		}
		initComponents();
		addActionListeners();
		addListeners();
	}

	private void initComponents() {

		setLayout(new BorderLayout());
		JSplitPane topSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
				createLeftPanel(), createRightPanel());
		topSplitPanel.setDividerLocation(700);
		topSplitPanel.setContinuousLayout(true);

		add(topSplitPanel,BorderLayout.CENTER);
	}

	private JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel(new MigLayout("insets 0, gap 0", "[][]",""));

		JSplitPane leftSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
				partNamePanel, partSpecForPartPanel);
		leftSplitPanel.setDividerLocation(400);
		leftSplitPanel.setContinuousLayout(true);
		add(leftSplitPanel,BorderLayout.CENTER);
		leftPanel.add(createProcessPointPanel(),"height : 40, width : max, wrap");
		leftPanel.add(leftSplitPanel,"height : max, width : max, wrap");

		return leftPanel;
	}

	private Component createRightPanel() {
		JPanel rightPanel = new JPanel(new MigLayout("insets 0, gap 0", "[][]",""));

		JSplitPane rightSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
				assignPartToBinPanel, partSpecForSelectedBinPanel);
		rightSplitPanel.setDividerLocation(450);
		rightSplitPanel.setContinuousLayout(true);
		add(rightSplitPanel,BorderLayout.CENTER);
		rightPanel.add(createSelectBinNamePanel(),"height : 40, width : max, wrap");
		rightPanel.add(rightSplitPanel,"height : max, width : max, wrap");	

		return rightPanel;
	}

	@SuppressWarnings("unchecked")
	private Component createProcessPointPanel() {
		JPanel processPointPanel = new JPanel();
		processPointPanel.setLayout(new MigLayout("insets 0","5[][]2",""));

		if (processPointComboBox == null) {

			processPointComboBox = new LabeledComboBox("Process Point");
			processPointComboBox.getComponent().setName("JProcessPointCombobox");
			processPointComboBox.getComponent().setPreferredSize(new Dimension(350,20));
			ComboBoxModel<ProcessPoint> model = new  ComboBoxModel<ProcessPoint>(processPoint,"getDisplayName");
			processPointComboBox.getComponent().setModel(model);
			processPointComboBox.getComponent().setRenderer(model);
			processPointComboBox.getComponent().setSelectedIndex(-1);
			processPointPanel.add(processPointComboBox);
		}
		return processPointPanel;
	}

	private Component createSelectBinNamePanel() {
		JPanel selectBinNamePanel = new JPanel();
		selectBinNamePanel.setLayout(new MigLayout("insets 0","5[][]2",""));

		if(binNameTextField == null) {

			binNameTextField = new LabeledTextField("Bin Name");
			binNameTextField.getComponent().setName("JBinNameCombobox");
			binNameTextField.setUpperCaseField(true);
			binNameTextField.getComponent().setPreferredSize(new Dimension(500,60));
			selectBinNamePanel.add(binNameTextField);
			createBinBtn = new JButton("Create Bin");
			createBinBtn.setEnabled(false);
			selectBinNamePanel.add(createBinBtn);
		}
		return selectBinNamePanel;
	}

	private void createPartSpecTableByPart(List<PartSpec> list) {
		partSpecTableModel = new PartSpecTableModel(partSpecForPartPanel.getTable(), list, false, false);
		partSpecTableModel.addTableModelListener(this);
		partSpecTableModel.pack();
	}

	private void createPartSpecTableForSelectedBin() {
		partSpecForSelectedBinTableModel = new PartSpecTableModel(partSpecForSelectedBinPanel.getTable(), null, false, false);
		partSpecForSelectedBinTableModel.addTableModelListener(this);
		partSpecForSelectedBinTableModel.pack();
	}

	private void addListeners() {
		partNamePanel.addListSelectionListener(this);
		partSpecForPartPanel.addListSelectionListener(this);
		assignPartToBinPanel.addListSelectionListener(this);

		assignPartToBinPanel.getTable().addMouseListener(assignPartToBinMouseListener());
	}

	private MouseListener assignPartToBinMouseListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showAssignPartToBinMouseMenu(e);
			}

			private void showAssignPartToBinMouseMenu(MouseEvent e) {
				JPopupMenu popupMenu = new JPopupMenu();
				popupMenu.add(createMenuItem(ADD_TO_BIN, isPartSpecForPartSelected()));
				popupMenu.add(createMenuItem(CHANGE_BIN_NAME, true));
				popupMenu.add(createMenuItem(DELETE_BIN, true));
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	@Override
	public void onTabSelected() {

	}

	public void addActionListeners() {
		processPointComboBox.getComponent().addActionListener(this);

		createBinBtn.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(processPointComboBox.getComponent())){
			if(!partSpecList.isEmpty()){
				partSpecList.clear();
				partSpecTableModel.refresh(null);
			}
			if(!partSpecForSelectedBinList.isEmpty()) {
				partSpecForSelectedBinList.clear();
				partSpecForSelectedBinTableModel.refresh(null);
			}
			processPointSelected();
		}else if(e.getSource() == createBinBtn) addBinToList();
		else if(e.getSource() instanceof JMenuItem) {
			JMenuItem menuItem = (JMenuItem)e.getSource();
			if(menuItem.getName().startsWith(ADD_TO_BIN)) addToBin();
			if(menuItem.getName().startsWith(CHANGE_BIN_NAME)) updateBinName();
			if(menuItem.getName().startsWith(DELETE_BIN)) deleteBin();
		}
	}

	private void updateBinName() {
		PartsLoadingMaintenance newBin = partsLoadingMaintTableModel.getSelectedItem();
		String binName = MessageDialog.showInputDialog("Input","Input New Bin Name : ",32,true);

		if(StringUtils.isNotEmpty(binName)) {
			if(partsLoadingMaintTableModel.hasBinName(binName)) {
				MessageDialog.showError(this, "Bin name \"" + binName + "\" already exist");
				return;
			}
			newBin.setBinName(binName);
			ServiceFactory.getDao(PartsLoadingMaintenanceDao.class).save(newBin);	
			logUserAction(SAVED, newBin);

			partLoadingList.set(partsLoadingMaintTableModel.getItems().
					indexOf(partsLoadingMaintTableModel.getSelectedItem()), newBin);
			partsLoadingMaintTableModel.refresh(partLoadingList);
		}
	}

	private void addToBin() {
		if(!MessageDialog.confirm(this, "Are you sure that you want add a new part to this bin?")) 
			return; 

		PartsLoadingMaintenance selectedBin = partsLoadingMaintTableModel.getSelectedItem();
		selectedBin.setPartName(partSpecTableModel.getSelectedItem().getId().getPartName());
		selectedBin.setPartSpecId(partSpecTableModel.getSelectedItem().getId().getPartId());
		ServiceFactory.getDao(PartsLoadingMaintenanceDao.class).save(selectedBin);	
		logUserAction(SAVED, selectedBin);

		partLoadingList.set(partsLoadingMaintTableModel.getItems().
				indexOf(partsLoadingMaintTableModel.getSelectedItem()), selectedBin);
		partsLoadingMaintTableModel.refresh(partLoadingList);
	}

	private void deleteBin() {
		PartsLoadingMaintenance binName = partsLoadingMaintTableModel.getSelectedItem();
		if(binName == null) return;

		if(!MessageDialog.confirm(this, "Are you sure that you want to delete this bin?")) 
			return; 

		isDeleteBin = true;
		ServiceFactory.getDao(PartsLoadingMaintenanceDao.class).remove(binName);
		logUserAction(REMOVED, binName);
		partsLoadingMaintTableModel.remove(binName);

		if(!partSpecForSelectedBinList.isEmpty()) {
			partSpecForSelectedBinList.clear();
			partSpecForSelectedBinTableModel.refresh(null);
		}
	}

	private void addBinToList() {
		String binName = binNameTextField.getComponent().getText();
		if(!StringUtils.isEmpty(binName)) {
			if(partsLoadingMaintTableModel.getRowCount() != 0) {
			if(partsLoadingMaintTableModel.hasBinName(binName)) {
				MessageDialog.showError(this, "Bin Name \"" + binName + "\" already exist.");
			
				return;
				}
			}

			PartsLoadingMaintenance partMaint = new PartsLoadingMaintenance();
			ProcessPoint processPoint = (ProcessPoint) processPointComboBox.getComponent().getSelectedItem();
			partMaint.setProcessPointId(processPoint.getProcessPointId());
			partMaint.setPartName(partSpecTableModel.getSelectedItem().getId().getPartName());
			partMaint.setPartSpecId(partSpecTableModel.getSelectedItem().getId().getPartId());
			partMaint.setBinName(binName);

			if(!partsLoadingMaintTableModel.isPartSpecAddedToBin(partMaint)){
				saveBin(partMaint);
			} else { 
				MessageDialog.showError(this, "Part Spec is already added to a bin.");
				return;
			}
		}
	}
	
	private void saveBin(PartsLoadingMaintenance partMaint){
		PartsLoadingMaintenance partMaintFromDB = ServiceFactory.getDao(PartsLoadingMaintenanceDao.class).save(partMaint);
		logUserAction(SAVED, partMaint);
		partLoadingList.add(partMaintFromDB);
		partsLoadingMaintTableModel.refresh(partLoadingList);
		binNameTextField.getComponent().setText("");
	}

	public void tableChanged(TableModelEvent arg0) {
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		if(e.getSource() == (partNamePanel.getTable().getSelectionModel())
				&& e.getFirstIndex() >= 0) {
			createPartSpecTableByPart(getAllSpecsByPartName(
					lotControlRuleTableModel.getSelectedItem().getPartNameString()));
			createBinBtn.setEnabled(false);
		}else if(e.getSource() == assignPartToBinPanel.getTable().getSelectionModel()
				&& e.getFirstIndex() >= 0 &&
				isDeleteBin == false) {

			createPartSpecTableForSelectedBin();
			addSpecToSelectedBinSpecTable(getSpecByPartIdAndPartName(
					partsLoadingMaintTableModel.getSelectedItem().getPartName(),
					partsLoadingMaintTableModel.getSelectedItem().getPartSpecId()));
		}else if(e.getSource() == (partSpecForPartPanel.getTable().getSelectionModel())) {
			createBinBtn.setEnabled(true);
		}
		isDeleteBin = false;
	}

	private void addSpecToSelectedBinSpecTable(PartSpec spec) {
		partSpecForSelectedBinList.clear();
		partSpecForSelectedBinList.add(spec);
		partSpecForSelectedBinTableModel.refresh(partSpecForSelectedBinList);
	}

	private List<PartSpec> getAllSpecsByPartName(String partName) {
		try{
			partSpecList = ServiceFactory.getDao(PartSpecDao.class).findAllByPartName(partName);
		}catch(Exception e){
			String msg = DB_ERROR_OCCURED + "getAllSpecsByPartName";
			getLogger().error(e, msg);
			e.printStackTrace();
		}
		return partSpecList;
	}

	private PartSpec getSpecByPartIdAndPartName(String partName, String partSpec) {
		PartSpec spec = null;
		try{
			spec = ServiceFactory.getDao(PartSpecDao.class).findValueWithPartNameAndPartID(partName, partSpec);
		}catch(Exception e){
			String msg = DB_ERROR_OCCURED + "getSpecByPartIdAndPartName";
			getLogger().error(e, msg);
			e.printStackTrace();
		}	
		return spec;
	}

	private List<LotControlRule> getRulesByProcessPoint(ProcessPoint processPoint) {
		List<LotControlRule> ruleList = new ArrayList<LotControlRule>();
		try{
			ruleList = ServiceFactory.getDao(LotControlRuleDao.class)
					.findAllByProcessPoint(processPoint.getProcessPointId());
		}catch(Exception e) {
			String msg = DB_ERROR_OCCURED + "getRulesByProcessPoint";
			getLogger().error(e, msg);
			e.printStackTrace();
		}
		return ruleList;
	}

	private void processPointSelected() {

		ProcessPoint processPoint = (ProcessPoint) processPointComboBox.getComponent().getSelectedItem();
		lotControlRuleList = getRulesByProcessPoint(processPoint);

		List<LotControlRule> uniquePartRules = (List<LotControlRule>) filterUniquePartNames(lotControlRuleList);
		lotControlRuleTableModel = new LotControlRulePartTableModel(true,partNamePanel.getTable(), uniquePartRules);
		lotControlRuleTableModel.addTableModelListener(this);
		lotControlRuleTableModel.pack();
		populateBinNames(processPoint.getProcessPointId());
	}

	private void populateBinNames(String processPointId) {
		try{
			partLoadingList = ServiceFactory.getDao(PartsLoadingMaintenanceDao.class).findAllByProcessPointId(processPointId);
			partsLoadingMaintTableModel = new PartsLoadingMaintenanceTableModel(assignPartToBinPanel.getTable(), partLoadingList);
			partsLoadingMaintTableModel.addTableModelListener(this);
			partsLoadingMaintTableModel.pack();
		}catch(Exception e) {
			String msg = UNKNOWN_ERROR_OCCURED + "populateBinNames";
			getLogger().error(e, msg);
			e.printStackTrace();
		}
	}

	private boolean isPartSpecForPartSelected() {
		return partSpecForPartPanel.getTable().getSelectedRowCount() == 1;
	}

	private List<LotControlRule> filterUniquePartNames(List<LotControlRule> lotControlRules) {
		List<LotControlRule> uniquePartRules = new ArrayList<LotControlRule>();
		HashSet<String> ruleSet = new HashSet<String>();

		for(LotControlRule rule : lotControlRules)	{
			if(ruleSet.add(rule.getPartNameString())){
				uniquePartRules.add(rule);
			}
		}
		return uniquePartRules;
	}
}