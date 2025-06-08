package com.honda.galc.client.teamleader.qics.actualproblemrepairmethod;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.teamleader.qics.screen.QicsMaintenanceTabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.qics.DefectActualProblemDao;
import com.honda.galc.dao.qics.DefectRepairMethodDao;
import com.honda.galc.dao.qics.DefectTypeDao;
import com.honda.galc.dao.qics.InspectionPartDao;
import com.honda.galc.dao.qics.SecondaryPartDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.qics.DefectActualProblem;
import com.honda.galc.entity.qics.DefectActualProblemId;
import com.honda.galc.entity.qics.DefectRepairMethod;
import com.honda.galc.entity.qics.DefectRepairMethodId;
import com.honda.galc.entity.qics.DefectType;
import com.honda.galc.entity.qics.InspectionPart;
import com.honda.galc.entity.qics.SecondaryPart;
import com.honda.galc.service.utils.ProductTypeUtil;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Sept 9, 2014
 */
public class ActualProblemRepairMethodPanel extends QicsMaintenanceTabbedPanel {

	private static final long serialVersionUID = 1L;
	private ObjectTablePane<String> modelPane;
	private ObjectTablePane<String> defectTypeNamePane;
	private ObjectTablePane<String> inspectionPartNamePane;
	private ObjectTablePane<String> secondaryPartNamePane;
	private ButtonGroup buttonGroup;
	private JRadioButton radioButtonActualProblem = null;
	private JRadioButton radioButtonRepairMethod = null;
	private JComboBox actProbRepMthdComboBox;
	private JLabel actProbRepMthdLabel;
	private JComboBox repairTimeComboBox;
	private JLabel repairTimeLabel;
	private JTable actProbRepMthDataTable = null;
	private JScrollPane actProbRepMthDataScrollPane=null;
	private JButton submitButton;
	private static final String ADD_TEXT = "ADD";
	private static final String REMOVE_TEXT = "REMOVE";
	private static final String SELECT_ENTER_TEXT = "SELECT OR ENTER TEXT";


	protected void initComponent() {
		setLayout(null);
		modelPane = createModelPane();
		defectTypeNamePane=createDefectTypeNamePane();
		inspectionPartNamePane=createInspectionPartNamePane();
		secondaryPartNamePane=createSecondaryPartNamePane();
		buttonGroup = new ButtonGroup();
		actProbRepMthdLabel = createActProbRepMthdLabel();
		actProbRepMthdComboBox = createActProbRepMthdComboBox();
		repairTimeLabel=createRepairTimeLabel();
		repairTimeComboBox=createRepairTimeComboBox();
		actProbRepMthDataScrollPane=createActProbRepMthDataScrollPane();
		buttonGroup.add(getRadioButtonActualProblem());
		buttonGroup.add(getRadioButtonRepairMethod());	
		submitButton = createSubmitButton();
		add(getRadioButtonActualProblem());
		add(getRadioButtonRepairMethod());
		add(getModelPane());
		add(getDefectTypeNamePane());
		add(getInspectionPartNamePane());
		add(getSecondaryPartNamePane());
		add(getActProbRepMthdLabel());
		add(getActProbRepMthdComboBox());
		add(getRepairTimeLabel());
		add(getRepairTimeComboBox());
		add(getActProbRepMthDataScrollPane());
		add(getSubmitButton());
		mapHandlers();
		getRadioButtonActualProblem().setSelected(true);
		loadData();
	}

	protected JButton createSubmitButton() {
		JButton button = new JButton(ADD_TEXT);
		Component base = getActProbRepMthDataScrollPane();
		button.setSize(150, 50);
		button.setLocation(getLeftMargin()+425, base.getY() + base.getHeight()+30);
		button.setFont(Fonts.DIALOG_PLAIN_18);
		button.setEnabled(true);
		return button;
	}

	private javax.swing.JScrollPane createActProbRepMthDataScrollPane() {
		if (actProbRepMthDataScrollPane == null) {
			try {
				actProbRepMthDataScrollPane = new javax.swing.JScrollPane(getActProbRepMthDataTable());
				actProbRepMthDataScrollPane.setName("lotsOnHoldScrollPane");
				actProbRepMthDataScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				actProbRepMthDataScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				actProbRepMthDataScrollPane.setFont(new java.awt.Font("dialog", 1, 12));
				actProbRepMthDataScrollPane.setViewportView(getActProbRepMthDataTable());				
				Component base = getModelPane();
				int partwidth = 387;
				int rowColWidth = 30;
				actProbRepMthDataScrollPane.setLocation(getLeftMargin(), base.getY() + base.getHeight() + 10);
				int height = 305;
				int modelWidth = 190;
				actProbRepMthDataScrollPane.setSize(rowColWidth + modelWidth + partwidth * 2, height);
			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return actProbRepMthDataScrollPane;
	}

	private JTable getActProbRepMthDataTable() {
		if (actProbRepMthDataTable == null) {
			try {
				actProbRepMthDataTable = new JTable();
				actProbRepMthDataTable.setName("getActProbRepMthDataTable");
				createActProbRepMthDataScrollPane().setColumnHeaderView(actProbRepMthDataTable.getTableHeader());
				actProbRepMthDataTable.getTableHeader().setFont(new Font("dialog", 1, 12));
				actProbRepMthDataTable.setFont(new Font("dialog", 1, 12));
				actProbRepMthDataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				actProbRepMthDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);				
			} catch (Exception ex) {
				handleException(ex);
			}
		}
		return actProbRepMthDataTable;
	}

	@Override
	public void onTabSelected() {
		if(!isInitialized) {
			initComponent();
			isInitialized = true;
		}
	}

	private javax.swing.JRadioButton getRadioButtonActualProblem() {
		if (radioButtonActualProblem == null) {
			try {
				radioButtonActualProblem = new javax.swing.JRadioButton();
				radioButtonActualProblem.setName("radioButtonActualProblem");
				radioButtonActualProblem.setFont(Fonts.DIALOG_BOLD_14);
				radioButtonActualProblem.setText("ACTUAL PROBLEM");
				radioButtonActualProblem.setSize(200, 30);
				radioButtonActualProblem.setSelected(true);
				radioButtonActualProblem.setLocation(getLeftMargin() + 5, getTopMargin() + 10);
			} catch (Exception e) {
				logException(e);
			}
		}
		return radioButtonActualProblem;
	}

	private javax.swing.JRadioButton getRadioButtonRepairMethod() {
		if (radioButtonRepairMethod == null) {
			try {
				radioButtonRepairMethod = new javax.swing.JRadioButton();
				radioButtonRepairMethod.setName("radioButtonRepairMethod");
				radioButtonRepairMethod.setFont(Fonts.DIALOG_BOLD_14);
				radioButtonRepairMethod.setText("REPAIR METHOD");
				Component base = getRadioButtonActualProblem();
				radioButtonRepairMethod.setSize(200, base.getHeight());
				radioButtonRepairMethod.setLocation(base.getX() + base.getWidth(), base.getY());
			} catch (Exception e) {
				logException(e);
			}
		}
		return radioButtonRepairMethod;
	}

	protected JComboBox createActProbRepMthdComboBox() {
		JComboBox comboBox = new JComboBox();
		Component base = getActProbRepMthdLabel();
		comboBox.setSize(200, 25);
		comboBox.setLocation(base.getX() , base.getY()+base.getHeight());
		comboBox.setFont(Fonts.DIALOG_BOLD_12);
		comboBox.setEditable(true);		
		return comboBox;
	}

	protected JLabel createActProbRepMthdLabel() {
		JLabel component = new JLabel("ACTUAL PROBLEM NAME");
		component.setSize(200, 30);
		component.setLocation(getSecondaryPartNamePane().getX() +getSecondaryPartNamePane().getWidth()+35 , getSecondaryPartNamePane().getY() + 10);
		component.setFont(Fonts.DIALOG_BOLD_12);
		return component;
	}

	protected JComboBox createRepairTimeComboBox() {
		JComboBox comboBox = new JComboBox();
		Component base = getRepairTimeLabel();
		comboBox.setSize(200, 25);
		comboBox.setLocation(base.getX(), base.getY()+base.getHeight());
		comboBox.setFont(Fonts.DIALOG_BOLD_12);
		comboBox.setEditable(true);
		comboBox.setVisible(false);
		return comboBox;
	}

	protected JLabel createRepairTimeLabel() {
		JLabel component = new JLabel("REPAIR TIME");
		component.setSize(100, 30);
		Component base = getActProbRepMthdComboBox();
		component.setLocation(base.getX(), base.getY()+base.getHeight()+10);
		component.setFont(Fonts.DIALOG_BOLD_12);
		component.setVisible(false);
		return component;
	}

	protected ObjectTablePane<String> createModelPane() {
		ColumnMappings columnMappings = ColumnMappings.with("MODEL CODE","model"); 			
		ObjectTablePane<String> pane = new ObjectTablePane<String>(columnMappings.get(),true);
		Component base = getRadioButtonActualProblem();
		int height = 215;
		pane.setSize(150, height);
		pane.setLocation(getLeftMargin(), base.getY() + base.getHeight() + 10);
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.setFont(new java.awt.Font("dialog", 0, 14));
		return pane;
	}

	protected ObjectTablePane<String> createDefectTypeNamePane() {		
		ColumnMappings columnMappings = ColumnMappings.with("DEFECT TYPE NAME","defectTypeName"); 		
		ObjectTablePane<String> pane = new ObjectTablePane<String>(columnMappings.get(),true);
		Component base = getModelPane();
		int height = base.getHeight();
		pane.setSize(170, height);
		pane.setLocation(base.getX() + base.getWidth(), base.getY());
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return pane;
	}

	protected ObjectTablePane<String> createInspectionPartNamePane() {		
		ColumnMappings columnMappings = ColumnMappings.with("INSPECTION PART NAME","inspectionPartName"); 		
		ObjectTablePane<String> pane = new ObjectTablePane<String>(columnMappings.get(),true);
		Component base = getDefectTypeNamePane();
		int height = base.getHeight();
		pane.setSize(170, height);
		pane.setLocation(base.getX() + base.getWidth(), base.getY());
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return pane;
	}

	protected ObjectTablePane<String> createSecondaryPartNamePane() {		
		ColumnMappings columnMappings = ColumnMappings.with("SECONDARY PART NAME","secondaryPartName"); 		
		ObjectTablePane<String> pane = new ObjectTablePane<String>(columnMappings.get(),true);
		Component base = getInspectionPartNamePane();
		int height = base.getHeight();
		pane.setSize(170, height);
		pane.setLocation(base.getX() + base.getWidth(), base.getY());
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return pane;
	}

	protected void mapHandlers() {
		getRadioButtonActualProblem().addActionListener(this);
		getRadioButtonRepairMethod().addActionListener(this);	
		getModelPane().getTable().getSelectionModel().addListSelectionListener(this);	
		getDefectTypeNamePane().getTable().getSelectionModel().addListSelectionListener(this);	
		getInspectionPartNamePane().getTable().getSelectionModel().addListSelectionListener(this);	
		getSecondaryPartNamePane().getTable().getSelectionModel().addListSelectionListener(this);
		getActProbRepMthDataTable().getSelectionModel().addListSelectionListener(this);
		getSubmitButton().addActionListener(this);
	}

	public ObjectTablePane<String> getModelPane() {
		return modelPane;
	}

	@Override
	public void deselected(ListSelectionModel listSelectionModel) {
		if(listSelectionModel.equals(getModelPane().getTable().getSelectionModel())) modelDeselected(listSelectionModel) ;
		else  if(listSelectionModel.equals(getActProbRepMthDataTable().getSelectionModel()))
		{
			getSubmitButton().setText(ADD_TEXT);
		}
	}

	private void modelDeselected(ListSelectionModel model) {
		clearActProbRepMthDataTable();	
	}

	@Override
	public void selected(ListSelectionModel listSelectionModel) {
		if(listSelectionModel.equals(getModelPane().getTable().getSelectionModel())
				||listSelectionModel.equals(getDefectTypeNamePane().getTable().getSelectionModel())
				||listSelectionModel.equals(getInspectionPartNamePane().getTable().getSelectionModel())
				||listSelectionModel.equals(getSecondaryPartNamePane().getTable().getSelectionModel()))
		{
			listSelected();
		}else if(listSelectionModel.equals(getActProbRepMthDataTable().getSelectionModel()))
		{
			getSubmitButton().setText(REMOVE_TEXT);
		}
	}

	private void listSelected() {
		clearActProbRepMthDataTable();
		if(getRadioButtonActualProblem().isSelected())
		{
			List<DefectActualProblem> actualProblemList=getDao(DefectActualProblemDao.class).findAllBySelectedValues(getModelPane().getSelectedItem(),getDefectTypeNamePane().getSelectedItem(),getInspectionPartNamePane().getSelectedItem(),getSecondaryPartNamePane().getSelectedItem(),getActProbRepMthdComboBox().getSelectedItem().toString());
			int[] actualProbColWidth = {40,190,190,190,190,190 };
			String[] actualProbHeaders={"#","MODEL CODE","DEFECT TYPE NAME","INSPECTION PART NAME","SECONDARY PART NAME","ACTUAL PROBLEM NAME"};
			createTableHeaders(actualProbHeaders);
			createTableColumns(actualProbColWidth);
			for(int i=0;i<actualProblemList.size();i++)
			{
				Vector<String> data = new Vector<String>();
				data.addElement((new Integer(i)).toString());
				data.addElement(actualProblemList.get(i).getId().getModelCode());
				data.addElement(actualProblemList.get(i).getId().getDefectTypeName());
				data.addElement(actualProblemList.get(i).getId().getInspectionPartName());
				data.addElement(actualProblemList.get(i).getId().getSecondaryPartName());
				data.addElement(actualProblemList.get(i).getId().getActualProblemName());
				((DefaultTableModel) getActProbRepMthDataTable().getModel()).addRow(data);
			}
		}else if (getRadioButtonRepairMethod().isSelected())
		{
			List<DefectRepairMethod> repairMethodsList=getDao(DefectRepairMethodDao.class).findAllBySelectedValues(getModelPane().getSelectedItem(),getDefectTypeNamePane().getSelectedItem(),getInspectionPartNamePane().getSelectedItem(),getSecondaryPartNamePane().getSelectedItem(),getActProbRepMthdComboBox().getSelectedItem().toString());
			int[] repairMethodColWidth = {40,175,175,175,175,175,75 };
			String[] repairMethodHeaders={"#","MODEL CODE","DEFECT TYPE NAME","INSPECTION PART NAME","SECONDARY PART NAME","REPAIR METHOD NAME","REPAIR TIME"};
			createTableHeaders(repairMethodHeaders);
			createTableColumns(repairMethodColWidth);
			for(int i=0;i<repairMethodsList.size();i++)
			{
				Vector<String> data = new Vector<String>();
				data.addElement((new Integer(i)).toString());
				data.addElement(repairMethodsList.get(i).getId().getModelCode());
				data.addElement(repairMethodsList.get(i).getId().getDefectTypeName());
				data.addElement(repairMethodsList.get(i).getId().getInspectionPartName());
				data.addElement(repairMethodsList.get(i).getId().getSecondaryPartName());
				data.addElement(repairMethodsList.get(i).getId().getRepairMethodName());
				data.addElement(new Integer(repairMethodsList.get(i).getRepairTime()).toString());
				((DefaultTableModel) getActProbRepMthDataTable().getModel()).addRow(data);
			}
		}
	}

	private void clearActProbRepMthDataTable() {
		getActProbRepMthDataTable().removeAll();
		DefaultTableModel model=((DefaultTableModel) getActProbRepMthDataTable().getModel());
		model.getDataVector().removeAllElements();
		model.setColumnCount(0);
		getActProbRepMthDataTable().repaint();
	}

	public void createTableHeaders(String[] headers)
	{
		for (int i = 0; i < headers.length; i++)
		{
			((DefaultTableModel) getActProbRepMthDataTable().getModel()).addColumn(headers[i]);
		}
	}

	public void createTableColumns(int[] width)
	{
		for (int i = 0; i < width.length; i++)
		{
			getActProbRepMthDataTable().getColumnModel().getColumn(i).setPreferredWidth(width[i]);	
		}
	}

	public void actionPerformed(ActionEvent e) {
		setErrorMessage(null);
		if(e.getSource().equals(getRadioButtonRepairMethod()))
		{
			getActProbRepMthdLabel().setText("REPAIR METHOD NAME");	
			getRepairTimeLabel().setVisible(true);
			getRepairTimeComboBox().setVisible(true);
			loadData();
		}else if(e.getSource().equals(getRadioButtonActualProblem()))
		{	    	
			getActProbRepMthdLabel().setText("ACTUAL PROBLEM NAME");	    	
			getRepairTimeLabel().setVisible(false);
			getRepairTimeComboBox().setVisible(false);
			loadData();
		}else if(e.getSource().equals(getSubmitButton()))
		{
			setErrorMessage(null);
			if(getRadioButtonActualProblem().isSelected()&& this.getSubmitButton().getText().equals(ADD_TEXT))
			{
				addActualProblem();
			}else if(getRadioButtonRepairMethod().isSelected()&& this.getSubmitButton().getText().equals(ADD_TEXT))
			{
				addRepairMethod();
			}if(getRadioButtonActualProblem().isSelected()&& this.getSubmitButton().getText().equals(REMOVE_TEXT))
			{
				removeActualProblem();
			}if(getRadioButtonRepairMethod().isSelected()&& this.getSubmitButton().getText().equals(REMOVE_TEXT))
			{
				removeRepairMethod();
			}
		}
	}

	private void removeRepairMethod() {
		int selIndex = getActProbRepMthDataTable().getSelectedRow();
		if(selIndex==-1)
		{
			this.setErrorMessage("Select the row from table");
		}else
		{
			DefectRepairMethodId id = new DefectRepairMethodId(getActProbRepMthDataTable().getValueAt(selIndex, 1).toString(), 
					getActProbRepMthDataTable().getValueAt(selIndex, 2).toString(),
					getActProbRepMthDataTable().getValueAt(selIndex, 3).toString(),
					getActProbRepMthDataTable().getValueAt(selIndex, 4).toString(),
					getActProbRepMthDataTable().getValueAt(selIndex, 5).toString());			
			getDao(DefectRepairMethodDao.class).removeByKey(id);
			logUserAction("removed DefectRepairMethod by key: " + id.toString());
			listSelected();
		}
	}

	private void removeActualProblem() {
		int selIndex = getActProbRepMthDataTable().getSelectedRow();
		if(selIndex==-1)
		{
			this.setErrorMessage("Select the row from table");
		}else
		{
			DefectActualProblemId id = new DefectActualProblemId(getActProbRepMthDataTable().getValueAt(selIndex, 1).toString(), 
					getActProbRepMthDataTable().getValueAt(selIndex, 2).toString(),
					getActProbRepMthDataTable().getValueAt(selIndex, 3).toString(),
					getActProbRepMthDataTable().getValueAt(selIndex, 4).toString(),
					getActProbRepMthDataTable().getValueAt(selIndex, 5).toString());	
			getDao(DefectActualProblemDao.class).removeByKey(id);
			logUserAction("removed DefectActualProblem by key: " + id.toString());
			listSelected();
		}
	}

	private void addRepairMethod() {
		if(verifySelections())
		{
			DefectRepairMethodId id=new DefectRepairMethodId(getModelPane().getSelectedItem(),getDefectTypeNamePane().getSelectedItem(),getInspectionPartNamePane().getSelectedItem(),getSecondaryPartNamePane().getSelectedItem(),getActProbRepMthdComboBox().getSelectedItem().toString());	
			DefectRepairMethod repairMethod=new DefectRepairMethod();
			repairMethod.setId(id);
			repairMethod.setRepairTime(Integer.parseInt(getRepairTimeComboBox().getSelectedItem().toString()));
			getDao(DefectRepairMethodDao.class).save(repairMethod);
			logUserAction(SAVED, repairMethod);
			listSelected();
		}
	}

	private void addActualProblem() {
		if(verifySelections())
		{
			DefectActualProblemId id=new DefectActualProblemId(getModelPane().getSelectedItem(),getDefectTypeNamePane().getSelectedItem(),getInspectionPartNamePane().getSelectedItem(),getSecondaryPartNamePane().getSelectedItem(),getActProbRepMthdComboBox().getSelectedItem().toString());	
			DefectActualProblem actualProblem=new DefectActualProblem();
			actualProblem.setId(id);
			getDao(DefectActualProblemDao.class).save(actualProblem);
			logUserAction(SAVED, actualProblem);
			listSelected();
		}
	}

	private boolean verifySelections() {
		if(getModelPane().getTable().getSelectedRow()==-1 
				|| getDefectTypeNamePane().getTable().getSelectedRow()==-1 
				|| getInspectionPartNamePane().getTable().getSelectedRow()==-1
				|| getSecondaryPartNamePane().getTable().getSelectedRow()==-1
				|| ((JTextField) getActProbRepMthdComboBox().getEditor().getEditorComponent()).getText().equals(SELECT_ENTER_TEXT))
		{
			if(getRadioButtonActualProblem().isSelected())
			{
				setErrorMessage("Select Model,DefectTypeName,InspectionPartName,SecondaryPartName,Actual Problem values");
				return false;
			}
			else
			{
				setErrorMessage("Select Model,DefectTypeName,InspectionPartName,SecondaryPartName,RepairMethod,RepairTime values");
				return false;
			}
		}
		if(getRadioButtonRepairMethod().isSelected())
		{
			if(!isInteger(((JTextField) getRepairTimeComboBox().getEditor().getEditorComponent()).getText()))
			{
				setErrorMessage("Repair Time can be a number only");
				return false;
			}
		}
		return true;
	}

	public boolean isInteger( String input ) {
		try {
			Integer.parseInt( input );
			return true;
		}
		catch( Exception e ) {
			return false;
		}
	}

	public void loadData()
	{
		clearData();
		getModelPane().reloadData(findAllModelCodes());
		List<DefectType> defectTypes = getDao(DefectTypeDao.class).findAll();
		List<String> defectTypeStrList=new ArrayList<String>();
		for(DefectType type:defectTypes)
		{
			defectTypeStrList.add(type.getDefectTypeName());
		}
		getDefectTypeNamePane().reloadData(defectTypeStrList);
		List<InspectionPart> inspectionPartList=getDao(InspectionPartDao.class).findAll();
		List<String> inspectionPartStrList=new ArrayList<String>();
		for(InspectionPart inspPart:inspectionPartList)
		{
			inspectionPartStrList.add(inspPart.getInspectionPartName());
		}
		getInspectionPartNamePane().reloadData(inspectionPartStrList);

		List<SecondaryPart> secondaryPartList=getDao(SecondaryPartDao.class).findAll();
		List<String> secondaryPartStrList=new ArrayList<String>();
		for(SecondaryPart secPart:secondaryPartList)
		{
			secondaryPartStrList.add(secPart.getSecondaryPartName());
		}
		getSecondaryPartNamePane().reloadData(secondaryPartStrList);
		getActProbRepMthdComboBox().addItem(SELECT_ENTER_TEXT);
		getActProbRepMthdComboBox().setSelectedItem(SELECT_ENTER_TEXT);
		getRepairTimeComboBox().addItem(SELECT_ENTER_TEXT);
		getRepairTimeComboBox().setSelectedItem(SELECT_ENTER_TEXT);
		if(getRadioButtonActualProblem().isSelected())
		{
			List<DefectActualProblem> defectActualProblemList=getDao(DefectActualProblemDao.class).findAll();
			for(DefectActualProblem problem:defectActualProblemList)
			{
				if(((DefaultComboBoxModel)getActProbRepMthdComboBox().getModel()).getIndexOf(problem.getId().getActualProblemName())==-1)
					getActProbRepMthdComboBox().addItem(problem.getId().getActualProblemName());
			}
		}
		if(getRadioButtonRepairMethod().isSelected())
		{
			List<DefectRepairMethod> defectRepairMethodList=getDao(DefectRepairMethodDao.class).findAll();
			for(DefectRepairMethod repair:defectRepairMethodList)
			{
				if(((DefaultComboBoxModel)getActProbRepMthdComboBox().getModel()).getIndexOf(repair.getId().getRepairMethodName())==-1)
					getActProbRepMthdComboBox().addItem(repair.getId().getRepairMethodName());
				if(((DefaultComboBoxModel)getRepairTimeComboBox().getModel()).getIndexOf(new Integer(repair.getRepairTime()).toString())==-1)
					getRepairTimeComboBox().addItem(new Integer(repair.getRepairTime()).toString());
			}
		}
	}

	private void clearData() {
		getModelPane().removeData();
		getDefectTypeNamePane().removeData();
		getInspectionPartNamePane().removeData();
		getSecondaryPartNamePane().removeData();
		getActProbRepMthdComboBox().removeAllItems();
		getRepairTimeComboBox().removeAllItems();
		clearActProbRepMthDataTable();
		getSubmitButton().setText(ADD_TEXT);	
		setErrorMessage(null);
	}

	protected List<String> findAllModelCodes() {
		Set<String> uniqueModels = new TreeSet<String>();
		List<ProductTypeData> productTypes = getDao(ProductTypeDao.class).findAll();
		if (productTypes != null) {
			for (ProductTypeData type : productTypes) {
				ProductType productType = type.getProductType();
				if (productType == null) {
					continue;
				}
				List<String> list = ProductTypeUtil.getProductSpecDao(productType).findAllModelCodes(productType.name());
				if (list != null) {
					uniqueModels.addAll(list);
				}
			}
		}
		List<String> models = new ArrayList<String>(uniqueModels);
		return models;
	}
	
	public void logException(Exception e) {
		Logger.getLogger(getApplicationId()).error(e,"An error Occurred while processing the Actual Problem Repair Method Panel screen");
		e.printStackTrace();
		setErrorMessage("An error Occurred while processing the Actual Problem Repair Method Panel screen");
	}

	public ObjectTablePane<String> getDefectTypeNamePane() {
		return defectTypeNamePane;
	}

	public void setDefectTypeNamePane(ObjectTablePane<String> defectTypeName) {
		this.defectTypeNamePane = defectTypeName;
	}

	public ObjectTablePane<String> getInspectionPartNamePane() {
		return inspectionPartNamePane;
	}

	public void setInspectionPartNamePane(
			ObjectTablePane<String> inspectionPartNamePane) {
		this.inspectionPartNamePane = inspectionPartNamePane;
	}

	public ObjectTablePane<String> getSecondaryPartNamePane() {
		return secondaryPartNamePane;
	}

	public void setSecondaryPartNamePane(
			ObjectTablePane<String> secondaryPartNamePane) {
		this.secondaryPartNamePane = secondaryPartNamePane;
	}

	public JComboBox getActProbRepMthdComboBox() {
		return actProbRepMthdComboBox;
	}

	public void setActProbRepMthdComboBox(JComboBox actProbRepMthdComboBox) {
		this.actProbRepMthdComboBox = actProbRepMthdComboBox;
	}

	public JLabel getActProbRepMthdLabel() {
		return actProbRepMthdLabel;
	}

	public void setActProbRepMthdLabel(JLabel actProbRepMthdLabel) {
		this.actProbRepMthdLabel = actProbRepMthdLabel;
	}

	public JComboBox getRepairTimeComboBox() {
		return repairTimeComboBox;
	}

	public void setRepairTimeComboBox(JComboBox repairTimeComboBox) {
		this.repairTimeComboBox = repairTimeComboBox;
	}

	public JLabel getRepairTimeLabel() {
		return repairTimeLabel;
	}

	public void setRepairTimeLabel(JLabel repairTimeLabel) {
		this.repairTimeLabel = repairTimeLabel;
	}

	public JScrollPane getActProbRepMthDataScrollPane() {
		return actProbRepMthDataScrollPane;
	}

	public void setActProbRepMthDataScrollPane(
			JScrollPane actProbRepMthDataScrollPane) {
		this.actProbRepMthDataScrollPane = actProbRepMthDataScrollPane;
	}

	public void setActProbRepMthDataTable(JTable actProbRepMthDataTable) {
		this.actProbRepMthDataTable = actProbRepMthDataTable;
	}

	public ActualProblemRepairMethodPanel(QicsMaintenanceFrame mainWindow) {
		super("Actual Problem Repair Method",KeyEvent.VK_A);
		setMainWindow(mainWindow);
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(JButton submitButton) {
		this.submitButton = submitButton;
	}

}