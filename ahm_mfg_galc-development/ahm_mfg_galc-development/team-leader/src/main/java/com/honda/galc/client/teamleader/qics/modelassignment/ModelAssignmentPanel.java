package com.honda.galc.client.teamleader.qics.modelassignment;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.teamleader.qics.screen.QicsMaintenanceTabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.utils.ComboBoxUtils;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.entity.qics.InspectionModel;
import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.util.PropertyComparator;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ModelAssignmentPanel</code> is ...
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
 * <TD>Sep 3, 2008</TD>
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
public class ModelAssignmentPanel extends QicsMaintenanceTabbedPanel {

	private static final long serialVersionUID = 1L;

	private static final String ADD_TEXT = "Add";
	private static final String REMOVE_TEXT = "Remove";
	
	private JLabel departmentsLabel;
	private JComboBox departmentComboBox;
	private ObjectTablePane<ProcessPoint> processPointPane;

	private ObjectTablePane<String> modelPane;
	private ObjectTablePane<PartGroup> partGroupPane;
	private ObjectTablePane<DefectGroup> defectGroupPane;
	private ObjectTablePane<InspectionModel> inspectionModelPane;

	private JButton submitButton;
	private Action createAction;
	private Action deleteAction;

	private Map<String, String> inpspectionModelcolumnPropertyMapping;

	public ModelAssignmentPanel(QicsMaintenanceFrame mainWindow) {
		super("Model Assignment",KeyEvent.VK_M);
		setMainWindow(mainWindow);
	}

	protected void initComponent() {
		setLayout(null);
		inpspectionModelcolumnPropertyMapping = new LinkedHashMap<String, String>();
		// === create ui fragments === //
		departmentsLabel = createDepartmentsLabel();
		departmentComboBox = createDepartments();
		processPointPane = createProcessPoints();

		modelPane = createModelPane();
		partGroupPane = createPartGroupPane();
		defectGroupPane = createDefectGroupPane();

		inspectionModelPane = createInspectionModels();

		submitButton = createSubmitButton();

		// === add fragments == //
		add(getDepartmentsLabel());
		add(getDepartmentComboBox());
		add(getProcessPointPane());

		add(getModelPane());
		add(getPartGroupPane());
		add(getDefectGroupPane());

		add(getInspectionModelPane());

		add(getSubmitButton());

		mapActions();
		mapHandlers();

		// == init data === //
		List<Division> departments = getClientModel().getDepartments();
		Collections.sort(departments, new PropertyComparator<Division>(Division.class, "divisionName"));
		ComboBoxUtils.loadComboBox(getDepartmentComboBox(), departments);

	}

	@Override
	public void onTabSelected() {
		if(!isInitialized) {
			initComponent();
			isInitialized = true;
		}
		if (getClientModel().isPartGroupsUpdated(getPartGroupPane().getDataSelectedTime())) {
			getPartGroupPane().reloadData(getClientModel().getPartGroups(), getClientModel().getPartGroupsUpdateTime(), "getPartGroupName");
		}
		if (getClientModel().isDefectGroupsUpdated(getDefectGroupPane().getDataSelectedTime())) {
			getDefectGroupPane().reloadData(getClientModel().getDefectGroups(), getClientModel().getDefectGroupsUpdateTime(), "getDefectGroupName");
		}
	}

	// ============= factory methods for ui components
	protected JLabel createDepartmentsLabel() {
		JLabel component = new JLabel("Departments");
		component.setSize(125, 30);
		component.setLocation(getLeftMargin() + 5, getTopMargin() + 10);
		component.setFont(Fonts.DIALOG_BOLD_16);
		return component;
	}

	protected JComboBox createDepartments() {

		JComboBox comboBox = new JComboBox();
		Component base = getDepartmentsLabel();
		comboBox.setSize(460, base.getHeight());
		comboBox.setLocation(base.getX() + base.getWidth(), base.getY());
		ListCellRenderer tableCellRenderer = new PropertyComboBoxRenderer<Division>(Division.class, "divisionName");
		comboBox.setRenderer(tableCellRenderer);
		comboBox.setFont(Fonts.DIALOG_PLAIN_20);
		return comboBox;
	}

	protected ObjectTablePane<ProcessPoint> createProcessPoints() {
		
		ColumnMappings columnMappings = 
			ColumnMappings.with("#", "row").put("Process Point ID", "processPointId").put("Process Point Name", "processPointName");
		
		ObjectTablePane<ProcessPoint> pane = new ObjectTablePane<ProcessPoint>(columnMappings.get(),true);
		int height = 215;

		Component base = getDepartmentComboBox();
		pane.setSize(300, height);
		pane.setLocation(getLeftMargin(), base.getY() + base.getHeight() + 10);
//		pane.setColumnWidths(new int[] { 30, 100, pane.getWidth() - 30 - 100 - 3 });
		pane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return pane;
	}

	protected ObjectTablePane<String> createModelPane() {
		
		ColumnMappings columnMappings = ColumnMappings.with("MODEL_CODE","model"); 
			
		ObjectTablePane<String> pane = new ObjectTablePane<String>(columnMappings.get(),true);
		Component base = getProcessPointPane();
		int height = base.getHeight();
		pane.setSize(170, height);
		pane.setLocation(base.getX() + base.getWidth(), base.getY());
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return pane;
	}

	protected ObjectTablePane<PartGroup> createPartGroupPane() {
		
		ColumnMappings columnMappings = ColumnMappings.with("Part Group", "partGroupName");
		
		ObjectTablePane<PartGroup> pane = new ObjectTablePane<PartGroup>(columnMappings.get(),true);
		Component base = getModelPane();
		int height = base.getHeight();
		pane.setSize(270, height);
		pane.setLocation(base.getX() + base.getWidth(), base.getY());
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return pane;
	}

	protected ObjectTablePane<DefectGroup> createDefectGroupPane() {
		
		ColumnMappings columnMappings = ColumnMappings.with("Defect Group", "defectGroupName");
		
		ObjectTablePane<DefectGroup> pane = new ObjectTablePane<DefectGroup>(columnMappings.get(),true);
		Component base = getPartGroupPane();
		pane.setSize(base.getWidth() - 5, base.getHeight());
		pane.setLocation(base.getX() + base.getWidth(), base.getY());
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return pane;
	}

	protected ObjectTablePane<InspectionModel> createInspectionModels() {
	
		ColumnMappings columnMappings = 
			ColumnMappings.with("#", "row").put("Model", "modelCode")
			              .put("Part Group", "partGroupName").put("Defect Group", "defectGroupName");
		
		
		ObjectTablePane<InspectionModel> pane = new ObjectTablePane<InspectionModel>(columnMappings.get(),true,true);
		Component base = getProcessPointPane();
		int partwidth = 387;
		int rowColWidth = 30;
		pane.setLocation(getLeftMargin(), base.getY() + base.getHeight() + 10);
		int height = 305;
		int modelWidth = 200;
		pane.setSize(rowColWidth + modelWidth + partwidth * 2, height);
//		pane.setColumnWidths(new int[] { rowColWidth, modelWidth, partwidth, partwidth - 3 });
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		pane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return pane;
	}

	protected JButton createSubmitButton() {
		JButton button = new JButton(ADD_TEXT);
		Component base = getDefectGroupPane();
		button.setSize(150, 50);
		button.setLocation(base.getX() + base.getWidth() - button.getWidth(), base.getY() - button.getHeight() - 5);
		button.setFont(Fonts.DIALOG_PLAIN_18);
		button.setEnabled(false);
		return button;
	}

	protected void mapActions() {
		getSubmitButton().addActionListener(this);
	}

	protected void mapHandlers() {
		getDepartmentComboBox().addActionListener(this);
		getProcessPointPane().getTable().getSelectionModel().addListSelectionListener(this);
		getModelPane().getTable().getSelectionModel().addListSelectionListener(this);
		getPartGroupPane().getTable().getSelectionModel().addListSelectionListener(this);
		getDefectGroupPane().getTable().getSelectionModel().addListSelectionListener(this);
		getInspectionModelPane().getTable().getSelectionModel().addListSelectionListener(this);
	}

	public ObjectTablePane<InspectionModel> getInspectionModelPane() {
		return inspectionModelPane;
	}

	public JComboBox getDepartmentComboBox() {
		return departmentComboBox;
	}

	public ObjectTablePane<ProcessPoint> getProcessPointPane() {
		return processPointPane;
	}

	public ObjectTablePane<DefectGroup> getDefectGroupPane() {
		return defectGroupPane;
	}

	public ObjectTablePane<String> getModelPane() {
		return modelPane;
	}

	public ObjectTablePane<PartGroup> getPartGroupPane() {
		return partGroupPane;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	protected JLabel getDepartmentsLabel() {
		return departmentsLabel;
	}

	public Action getCreateAction() {
		return createAction;
	}

	public void setCreateAction(Action createAction) {
		this.createAction = createAction;
	}

	public Action getDeleteAction() {
		return deleteAction;
	}

	public void setDeleteAction(Action deleteAction) {
		this.deleteAction = deleteAction;
	}

	protected Map<String, String> getInpspectionModelcolumnPropertyMapping() {
		return inpspectionModelcolumnPropertyMapping;
	}

	@Override
	public void deselected(ListSelectionModel model) {
		if(model.equals(getProcessPointPane().getTable().getSelectionModel())) processPointDeselected();
		if(model.equals(getModelPane().getTable().getSelectionModel())) modelDeselected();
		if(model.equals(getPartGroupPane().getTable().getSelectionModel())) partGroupDeselected();
		if(model.equals(getDefectGroupPane().getTable().getSelectionModel())) defectGroupDeselected();
		if(model.equals(getInspectionModelPane().getTable().getSelectionModel())) inspectionModelDeselected();
	}

	@Override
	public void selected(ListSelectionModel model) {
		if(model.equals(getProcessPointPane().getTable().getSelectionModel())) processPointSelected();
		if(model.equals(getModelPane().getTable().getSelectionModel())) modelSelected();
		if(model.equals(getPartGroupPane().getTable().getSelectionModel())) partGroupSelected();
		if(model.equals(getDefectGroupPane().getTable().getSelectionModel())) defectGroupSelected();
		if(model.equals(getInspectionModelPane().getTable().getSelectionModel())) inspectionModelSelected();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getDepartmentComboBox()) departmentSelected();
		else if(e.getSource() == getSubmitButton()) {
			if(getSubmitButton().getText().equals(ADD_TEXT)) creatInspectionModels();
			else if(getSubmitButton().getText().equals(REMOVE_TEXT)) deleteInspectionModels();
		}
	}
	
	
	private void departmentSelected() {
		getProcessPointPane().removeData();
		Division division = (Division) getDepartmentComboBox().getSelectedItem();
		if (division == null) return;
		List<ProcessPoint> processPoints = getController().selectProcessPoints(division);
		
		Collections.sort(processPoints, new PropertyComparator<ProcessPoint>(ProcessPoint.class, "processPointName"));
		getProcessPointPane().reloadData(processPoints);

		if (getModelPane().getTable().getRowCount() == 0) {
			getModelPane().reloadData(getClientModel().getModels());
		}
		if (getPartGroupPane().getTable().getRowCount() == 0) {
			getPartGroupPane().reloadData(getClientModel().getPartGroups());
		}

		if (getDefectGroupPane().getTable().getRowCount() == 0) {
			getDefectGroupPane().reloadData(getClientModel().getDefectGroups());
		}
	}

	private void creatInspectionModels() {
		ProcessPoint processPoint = getProcessPointPane().getSelectedItem();
		if (processPoint == null) return;
		List<String> models = getModelPane().getSelectedItems();
		if (models == null || models.isEmpty()) return;
		List<PartGroup> partGroups = getPartGroupPane().getSelectedItems();
		if (partGroups == null || partGroups.isEmpty()) return;
		List<DefectGroup> defectGroups = getDefectGroupPane().getSelectedItems();
		if (defectGroups == null || defectGroups.isEmpty()) return;
		List<InspectionModel> inspectionModels = new ArrayList<InspectionModel>();
		List<InspectionModel> existingInspectionModels = getInspectionModelPane().getItems();

		for (String modelCode : models) {
			for (PartGroup partGroup : partGroups) {
				for (DefectGroup defectGroup : defectGroups) {
					InspectionModel inspectionModel = new InspectionModel();
					inspectionModel.setApplicationId(processPoint.getProcessPointId());
					inspectionModel.getId().setModelCode(modelCode);
					inspectionModel.getId().setPartGroupName(partGroup.getPartGroupName());
					inspectionModel.getId().setDefectGroupName(defectGroup.getDefectGroupName());
					if (!existingInspectionModels.contains(inspectionModel)) {
						inspectionModels.add(inspectionModel);
					}
				}
			}
		}

		if (!inspectionModels.isEmpty()) {
			getController().createInspectionModels(inspectionModels);
			
		}

		getDefectGroupPane().clearSelection();
		processPointSelected();
	}

	private void deleteInspectionModels() {
		List<InspectionModel> selectedValues = getInspectionModelPane().getSelectedItems();

		if (selectedValues == null || selectedValues.isEmpty()) return;
		getController().deleteInspectionModels(selectedValues);

		getInspectionModelPane().clearSelection();
		processPointSelected();
	}
	
	private void processPointSelected() {
		getInspectionModelPane().removeData();

		ProcessPoint processPoint = getProcessPointPane().getSelectedItem();
		if (processPoint == null)return;
		String productType = getApplictionProductTypeName(processPoint.getProcessPointId());
		List<String> models = getController().selectModels(productType);
		getModelPane().reloadData(models);
		
		List<InspectionModel> inspectionModels = getController().selectInspectionModels(processPoint.getProcessPointId());
		getInspectionModelPane().reloadData(inspectionModels);
	}
	
	private void processPointDeselected() {
		getInspectionModelPane().removeData();
		getModelPane().clearSelection();
	}
	
	private void modelSelected() {
		List<String> selectedModelCodes = getModelPane().getSelectedItems();
		
		getPartGroupPane().reloadData(getClientModel().getPartGroups(selectedModelCodes));
		getDefectGroupPane().reloadData(getClientModel().getDefectGroups(selectedModelCodes));
	}
	
	private void modelDeselected() {
		getPartGroupPane().clearSelection();
		
		List<String> selectedModelCodes = getModelPane().getSelectedItems();
		getPartGroupPane().reloadData(getClientModel().getPartGroups(selectedModelCodes));
		getDefectGroupPane().reloadData(getClientModel().getDefectGroups(selectedModelCodes));
	}
	
	private void partGroupDeselected() {
		getDefectGroupPane().clearSelection();
	}
	
	private void partGroupSelected() {
		List<String> selectedValues = getModelPane().getSelectedItems();
		if (selectedValues == null || selectedValues.isEmpty()) 
			getPartGroupPane().clearSelection();
	}
	
	private void defectGroupSelected(){
		List<PartGroup> selectedValues = getPartGroupPane().getSelectedItems();
		if (selectedValues == null ||   selectedValues.isEmpty()) {
			getDefectGroupPane().clearSelection();
			return;
		}
		
		getInspectionModelPane().clearSelection();
		getSubmitButton().setText(ADD_TEXT);
		getSubmitButton().setEnabled(true);
	}
	
	private void defectGroupDeselected(){
		getSubmitButton().setEnabled(false);
	}
	
	private void inspectionModelSelected() {
		getDefectGroupPane().clearSelection();
		getSubmitButton().setText(REMOVE_TEXT);
		getSubmitButton().setEnabled(true);
	}
	
	private void inspectionModelDeselected() {
		getSubmitButton().setEnabled(false);
	}
}
