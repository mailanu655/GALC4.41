package com.honda.galc.client.qics.view.screen;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.qics.util.QicsUtils;
import com.honda.galc.client.qics.util.StringTrim;
import com.honda.galc.client.qics.view.constants.ActionId;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Zone;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectResultId;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Defect text input screen for QICS.
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
public class DefectTextInputPanel extends DefectInputPanel implements ListSelectionListener {

	private static final long serialVersionUID = 1L;

	private ObjectTablePane<String> partGroupPane;
	private ObjectTablePane<String> partPane;
	private ObjectTablePane<String> locationPane;
	private ObjectTablePane<String> defectPane;
	private ObjectTablePane<DefectDescription> otherPartPane;
	private ObjectTablePane<Division> writeUpDeptPane = null;
	private ObjectTablePane<Division> respDeptPane = null;
	private ObjectTablePane<Line> respLinePane = null;
	private ObjectTablePane<Zone> respZonePane = null;


	public DefectTextInputPanel(QicsFrame frame) {
		super(frame);
		initialize();
	}

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.TEXT_INPUT;
	}

	@Override
	protected void initialize() {

		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()){
			initComponentsWithRespDept();
			initComponentLocationsWithRespDept();
		}else {
			initComponents();
			initComponentLocations();
		}
		super.initialize();
	}
	
	protected void initComponents() {
		partGroupPane = createPartGroupPane();
		partPane = createPartPane();
		locationPane = createLocationPane();
		defectPane = createDefectPane();
		otherPartPane = createOtherPartPane();

		add(getPartGroupPane());
		add(getPartPane());
		add(getLocationPane());
		add(getDefectPane());
		add(getOtherPartPane());
	}
	
	protected void initComponentsWithRespDept() {
		initComponents();

		writeUpDeptPane = createWriteUpDeptPane();
		respDeptPane = createRespDeptPane();
		respLinePane = createRespLinePane();
		respZonePane = createRespZonePane();

		add(getWriteUpDeptPane());		
		add(getRespDeptPane());
		add(getRespLinePane());
		add(getRespZonePane());
	}


	// === controlling api === //
	@Override
	public void startPanel() {
		
		getAcceptButton().setVisible(true);

		if(getProductModel() == null) return;
		getPartGroupPane().reloadData(getProductModel().getPartGroupNames());
		getPartGroupPane().clearSelection();
		getPartPane().clearSelection();
		getLocationPane().clearSelection();
		getDefectPane().clearSelection();
		getOtherPartPane().clearSelection();
		if (getPartGroupPane().getTable().getRowCount() == 1) {
			getPartGroupPane().getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()){
			getRespDeptPane().clearSelection();
			getRespLinePane().clearSelection();
			getRespZonePane().clearSelection();
			getRespDeptPane().removeData();
			getRespLinePane().removeData();
			getRespZonePane().removeData();
		}
		setButtonsState();
		setDefaultDefectStatusSelected();
		setFocus(getDefaultElement());

		if (!getQicsFrame().isIdle() && (getProductModel().getInspectionModels() == null || getProductModel().getInspectionModels().isEmpty())) {
			setErrorMessage("Missing Defect/Part Group (Model Assignment Maintenance)");
		}
	}

	@Override
	public void setButtonsState() {
		super.setButtonsState();
		boolean otherPartSelected = getOtherPartPane().getTable().getSelectedRowCount() > 0;
		getAcceptButton().setEnabled(otherPartSelected);
	}

	public boolean isDefectSelected() {
		return getDefectPane().getTable().getSelectedRowCount() > 0;
	}

	// === get/set === //
	public ObjectTablePane<String> getPartGroupPane() {
		return partGroupPane;
	}

	public ObjectTablePane<String> getPartPane() {
		return partPane;
	}

	public ObjectTablePane<String> getLocationPane() {
		return locationPane;
	}

	public ObjectTablePane<String> getDefectPane() {
		return defectPane;
	}

	public ObjectTablePane<DefectDescription> getOtherPartPane() {
		return otherPartPane;
	}

	protected int getSpacing() {
		return 0;
	}

	// ============= factory methods for ui components
	public ObjectTablePane<String> createPartGroupPane() {
		return createTablePane("Part Group","Part Group",true);
	}

	public ObjectTablePane<String> createPartPane() {
		return createTablePane("Part","Part",true);
	}

	public ObjectTablePane<String> createLocationPane() {
		return createTablePane("Location","Location",true);
	}

	public ObjectTablePane<String> createDefectPane() {
		return createTablePane("Defect","Defect",true);
	}

	public ObjectTablePane<DefectDescription> createOtherPartPane() {
       return createTablePane("Other Part", ColumnMappings.with("Other Part", "secondaryPartName"), true);
	}

	// === panel buttons ===//
	@Override
	protected Collection<ActionId> getActionButtonCodes() {
		return Arrays.asList(new ActionId[] {ActionId.ACCEPT_NEW_DEFECT });
	}
	
	private void initComponentLocations() {
		partGroupPane.setSize(250, 150);
		partGroupPane.setLocation(0, 0);
		
		partPane.setSize(getPartGroupPane().getWidth(), 350);
		partPane.setLocation(getPartGroupPane().getX(), getPartGroupPane().getY() + getPartGroupPane().getHeight() + getSpacing());

		locationPane.setSize(getPartGroupPane().getWidth(), 500);
		locationPane.setLocation(getPartGroupPane().getX() + getPartGroupPane().getWidth() + getSpacing(), getPartGroupPane().getY());

		defectPane.setSize(265, 500);
		defectPane.setLocation(getLocationPane().getX() + getLocationPane().getWidth() + getSpacing(), getLocationPane().getY());

		otherPartPane.setSize(230, 350);
		otherPartPane.setLocation(getDefectPane().getX() + getDefectPane().getWidth() + getSpacing(), getDefectPane().getY());
	}	
	
	private void initComponentLocationsWithRespDept() {
		partGroupPane.setSize(250, 500);
		partGroupPane.setLocation(0, 0);
		
		partPane.setSize(getPartGroupPane().getWidth(), 500);
		partPane.setLocation(getPartGroupPane().getX() + getPartGroupPane().getWidth() + getSpacing(), getPartGroupPane().getY());
		
		locationPane.setSize(250, 160);
		locationPane.setLocation(getPartPane().getX() + getPartPane().getWidth() + getSpacing(), getPartPane().getY());
		
		defectPane.setSize(250, 160);
		defectPane.setLocation(getLocationPane().getX(), getLocationPane().getY() + getLocationPane().getHeight() + getSpacing());
       
		otherPartPane.setSize(250, 180);
		otherPartPane.setLocation(getDefectPane().getX(), getDefectPane().getY() + getDefectPane().getHeight() + getSpacing());
		
		writeUpDeptPane.setSize(230, 90);
		writeUpDeptPane.setLocation(getLocationPane().getX() + getLocationPane().getWidth() + getSpacing()+14, getLocationPane().getY());

		respDeptPane.setSize(230, 90);
		respDeptPane.setLocation(getWriteUpDeptPane().getX(), getWriteUpDeptPane().getY() + getWriteUpDeptPane().getHeight() + getSpacing());

		respLinePane.setSize(230, 90);
		respLinePane.setLocation(getRespDeptPane().getX(), getRespDeptPane().getY() + getRespDeptPane().getHeight() + getSpacing());

		respZonePane.setSize(230, 90);
		respZonePane.setLocation(getRespLinePane().getX(), getRespLinePane().getY() + getRespLinePane().getHeight() + getSpacing());
	}


	// ================= event handlers mappings ========================//
	@Override
	protected void mapEventHandlers() {
		getPartGroupPane().getTable().getSelectionModel().addListSelectionListener(this);
		getPartPane().getTable().getSelectionModel().addListSelectionListener(this);
		getLocationPane().getTable().getSelectionModel().addListSelectionListener(this);
		getDefectPane().getTable().getSelectionModel().addListSelectionListener(this);
		getOtherPartPane().getTable().getSelectionModel().addListSelectionListener(this);
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()){
			getWriteUpDeptPane().getTable().getSelectionModel().addListSelectionListener(this);
			getRespDeptPane().getTable().getSelectionModel().addListSelectionListener(this);
			getRespLinePane().getTable().getSelectionModel().addListSelectionListener(this);
			getRespZonePane().getTable().getSelectionModel().addListSelectionListener(this);
		}	
	}
	
	protected void deselected(ListSelectionModel model) {
		if(model.equals(getPartGroupPane().getTable().getSelectionModel())) partGroupDeselected();
		else if(model.equals(getPartPane().getTable().getSelectionModel())) partDeselected();
		else if(model.equals(getLocationPane().getTable().getSelectionModel())) locationDeselected();
		else if(model.equals(getDefectPane().getTable().getSelectionModel())) defectDeselected();
		else if(model.equals(getOtherPartPane().getTable().getSelectionModel())) otherPartDeselected();
		
	}
	
	protected void selected(ListSelectionModel model) {
		if(model.equals(getPartGroupPane().getTable().getSelectionModel())) partGroupSelected();
		else if(model.equals(getPartPane().getTable().getSelectionModel())) partSelected();
		else if(model.equals(getLocationPane().getTable().getSelectionModel())) locationSelected();
		else if(model.equals(getDefectPane().getTable().getSelectionModel())) defectSelected();
		else if(model.equals(getOtherPartPane().getTable().getSelectionModel())) otherPartSelected();
		else if(model.equals(getRespDeptPane().getTable().getSelectionModel())) respDeptSelected();

	}
	
	
	private void respDeptSelected() {
		String respDeptName = null;
		respDeptName = getRespDeptPane().getSelectedString();
		getRespLinePane().removeData();
		getRespZonePane().removeData();
		List<Zone> zones = new ArrayList<Zone>();
		List<Line> lines = new ArrayList<Line>();
		List<Division> list = getQicsController().getClientModel().getDepartments();
		String id = QicsUtils.getDepartmentId(list, respDeptName);		
		lines = getQicsController().selectLines(id);
		zones = getQicsController().selectZones(id);
		
		getRespLinePane().reloadData(lines);
		if (getRespLinePane().getTable().getRowCount() >= 1) {
			getRespLinePane().getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
		
		getRespZonePane().reloadData(zones);
		if (getRespZonePane().getTable().getRowCount() >= 1) {
			getRespZonePane().getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}
	

	protected void partGroupDeselected() {
		getPartPane().removeData();
	}
	
	protected void partGroupSelected() {
		String partGroup = getPartGroupPane().getSelectedItem();

		getQicsController().selectPartLocations(partGroup);

		getPartPane().removeData();
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()) {
			getWriteUpDeptPane().removeData();
			getRespDeptPane().removeData();
			getRespLinePane().removeData();
			getRespZonePane().removeData();
		}
		if (!getQicsFrame().displayDelayedMessage()){
			getPartPane().reloadData(new ArrayList<String>(getQicsController().getProductModel().getPartNames()));
			if (getPartPane().getTable().getRowCount() == 1) {
				getPartPane().getTable().getSelectionModel().setSelectionInterval(0, 0);
			}
		}
		
		getLogger().info("Part Group " + partGroup + " is selected");

	}
	
	protected void partDeselected() {
		getLocationPane().removeData();
		
	}
	
	protected void partSelected() {
		String partName = getPartPane().getSelectedItem();
		getLocationPane().removeData();
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()) {
			getWriteUpDeptPane().removeData();
			getRespDeptPane().removeData();
			getRespLinePane().removeData();
			getRespZonePane().removeData();
		}

		List<String> locations = getQicsController().getProductModel().getLocationNames(partName);
		if (locations == null) {
			locations = new ArrayList<String>();
		}
		getLocationPane().reloadData(locations);
		if (getLocationPane().getTable().getRowCount() == 1) {
			getLocationPane().getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
		getLogger().info("Part " + partName + " is selected");
	}
	
	protected void locationDeselected() {
		getDefectPane().removeData();
	}
	
	protected void locationSelected() {
		String partGroupName = getPartGroupPane().getSelectedItem();
		String partName = getPartPane().getSelectedItem();
		String locationName = getLocationPane().getSelectedItem();

		getDefectPane().removeData();
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()) {
			getWriteUpDeptPane().removeData();
			getRespDeptPane().removeData();
			getRespLinePane().removeData();
			getRespZonePane().removeData();
		}

		getQicsController().selectDefectDescriptions(partGroupName, partName, locationName);

		if (!getQicsFrame().displayDelayedMessage()){
			getDefectPane().reloadData(new ArrayList<String>(getQicsController().getProductModel().getDefectNames()));
			if (getDefectPane().getTable().getRowCount() == 1) {
				getDefectPane().getTable().getSelectionModel().setSelectionInterval(0, 0);
			}
		}
		
		getLogger().info("Location " + locationName + " is selected");

	}
	
	protected void defectDeselected() {
		getOtherPartPane().removeData();
	}
	
	protected void defectSelected() {
		getOtherPartPane().getTable().removeAll();
		String defectName = getDefectPane().getSelectedItem();
		getOtherPartPane().removeData();

		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()) {
			getWriteUpDeptPane().removeData();
			getRespDeptPane().removeData();
			getRespLinePane().removeData();
			getRespZonePane().removeData();
		}

		getOtherPartPane().reloadData(getQicsController().getProductModel().getDefectDescriptions(defectName));
		if (getOtherPartPane().getTable().getRowCount() == 1) {
			getOtherPartPane().getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
		
		getLogger().info("Defect " + defectName + " is selected");

	}
	
	protected void otherPartDeselected() {
		setButtonsState();
	}
	
	protected void otherPartSelected() {
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()) {
			getWriteUpDeptPane().removeData();
			getRespDeptPane().removeData();
			getRespLinePane().removeData();
			getRespZonePane().removeData();
		}
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()) {
			DefectDescription defectDescription = getOtherPartPane().getSelectedItem();
			StringTrim stringTrim = new StringTrim();
			List<Division> writeUpDepts = getClientModel().getDepartments();
			getWriteUpDeptPane().reloadData(writeUpDepts);
			getRespDeptPane().reloadData(writeUpDepts);
	
			if (getWriteUpDeptPane().getTable().getRowCount() > 0){
				if(!StringUtils.isEmpty(getQicsPropertyBean().getDefaultWriteUpDept().trim()))
				{
					getWriteUpDeptPane().setSelectedObjectDataRow(getQicsPropertyBean().getDefaultWriteUpDept().trim());
				}else
				{
					getWriteUpDeptPane().setSelectedObjectDataRow(getClientModel().getProcessPoint().getDivisionName());
				}

				int row=getWriteUpDeptPane().getTable().getSelectedRow();
				getWriteUpDeptPane().getTable().scrollRectToVisible(getWriteUpDeptPane().getTable().getCellRect(row, 0, true));	
	
			}
			if(defectDescription!=null){
				if ((getRespDeptPane().getTable().getRowCount() > 0)&&(defectDescription.getResponsibleDept()!=null)){
					getRespDeptPane().setSelectedObjectDataRow(stringTrim.trimSpace(defectDescription.getResponsibleDept()));
					int row=getRespDeptPane().getTable().getSelectedRow();
					getRespDeptPane().getTable().scrollRectToVisible(getRespDeptPane().getTable().getCellRect(row, 0, true));	
				}
				if ((getRespLinePane().getTable().getRowCount() > 0)&&(defectDescription.getResponsibleLine()!=null) ){
					getRespLinePane().setSelectedObjectDataRow(stringTrim.trimSpace(defectDescription.getResponsibleLine()));
					int row=getRespLinePane().getTable().getSelectedRow();
					getRespLinePane().getTable().scrollRectToVisible(getRespLinePane().getTable().getCellRect(row, 0, true));
				}
				if ((getRespZonePane().getTable().getRowCount() > 0)&&(defectDescription.getResponsibleZone()!=null)){
					getRespZonePane().setSelectedObjectDataRow(stringTrim.trimSpace(defectDescription.getResponsibleZone()));
					int row=getRespZonePane().getTable().getSelectedRow();
					getRespZonePane().getTable().scrollRectToVisible(getRespZonePane().getTable().getCellRect(row, 0, true));
				}
			}
		}	

		setButtonsState();

		getLogger().info("Other part is selected");

	}

	public ObjectTablePane<Division> getWriteUpDeptPane() {
		return writeUpDeptPane;
	}

	public ObjectTablePane<Division> getRespDeptPane() {
		return respDeptPane;
	}

	public ObjectTablePane<Line> getRespLinePane() {
		return respLinePane;
	}

	public ObjectTablePane<Zone> getRespZonePane() {
		return respZonePane;
	}

	public ObjectTablePane<Division> createWriteUpDeptPane() {
		ColumnMappings columnMappings = ColumnMappings.with("Write Up Dept", "divisionName");
		ObjectTablePane<Division> panel = new ObjectTablePane<Division>(columnMappings.get());
		return panel;
	}

	public ObjectTablePane<Division> createRespDeptPane() {
		ColumnMappings columnMappings = ColumnMappings.with("Responsible Dept", "divisionName");
		ObjectTablePane<Division> panel = new ObjectTablePane<Division>(columnMappings.get());
		return panel;
	}

	public ObjectTablePane<Line> createRespLinePane() {
		ColumnMappings columnMappings = ColumnMappings.with("Responsible Line", "lineName");
		ObjectTablePane<Line> panel = new ObjectTablePane<Line>(columnMappings.get());
		return panel;
	}

	public ObjectTablePane<Zone> createRespZonePane() {
		ColumnMappings columnMappings = ColumnMappings.with("Responsible Zone", "zoneName");
		ObjectTablePane<Zone> panel = new ObjectTablePane<Zone>(columnMappings.get());
		return panel;
	}
	
	public DefectResult getRepairResultData() {

		DefectResult defectResult = new DefectResult();
		
		DefectResultId id = new DefectResultId();

		DefectDescription defectDescription = getOtherPartPane().getSelectedItem();

		
		id.setProductId(getQicsController().getProductModel().getProduct().getProductId());
		id.setApplicationId(getQicsController().getProcessPointId());
		id.setDefectTypeName(getDefectPane().getSelectedItems().get(0));
		id.setInspectionPartName(getPartPane().getSelectedItems().get(0));
		id.setInspectionPartLocationName(getLocationPane().getSelectedItems().get(0));
		id.setSecondaryPartName(defectDescription.getSecondaryPartName());
		id.setTwoPartPairPart(defectDescription.getId().getTwoPartPairPart());
		id.setTwoPartPairLocation(defectDescription.getId().getTwoPartPairLocation());
		
		defectResult.setId(id);
		defectResult.setNewDefect(true);
		defectResult.setAssociateNo(getQicsFrame().getUserId());

		defectResult.setEntryStation(getClientModel().getProcessPoint().getProcessPointName());
		// Use Entry Department Name instead of ID
		defectResult.setEntryDept(getClientModel().getProcessPoint().getDivisionName());
		
		defectResult.setDefectStatus((short)(getDefectStatusPanel().getSelectedStatus()));
		defectResult.setPartGroupName(defectDescription.getPartGroupName());

		if (defectResult.isOutstandingStatus()) 
			defectResult.setOutstandingFlag((short)1);

		if (getQicsController().getClientModel().getCurrentSchedule() != null) {
			defectResult.setShift(getQicsController().getClientModel().getCurrentShiftCode());
			defectResult.setDate(getQicsController().getClientModel().getCurrentSchedule().getId().getProductionDate());
		}


		defectResult.setIqsCategoryName(defectDescription.getIqsCategoryName());
		defectResult.setIqsItemName(defectDescription.getIqsItemName());
//		defectResult.lock_Mode = defectDescription.getLockMode() == 0 ? false : true;
		defectResult.setRegressionCode(defectDescription.getRegressionCode());
		
		defectResult.setEngineFiring(defectDescription.getEngineFiringFlag());
		
		if(getQicsPropertyBean().isRespDeptLineZoneEnabled()) {
			defectResult.setWriteUpDepartment(getWriteUpDeptPane().getSelectedString());
			defectResult.setResponsibleDept(getRespDeptPane().getSelectedString());
			defectResult.setResponsibleLine(getRespLinePane().getSelectedString());
			defectResult.setResponsibleZone(getRespZonePane().getSelectedString());
		}else {
			// Use Write Up Department Name instead of ID
			defectResult.setWriteUpDepartment(getClientModel().getProcessPoint().getDivisionName());
			defectResult.setResponsibleDept(defectDescription.getResponsibleDept());
			defectResult.setResponsibleLine(defectDescription.getResponsibleLine());
			defectResult.setResponsibleZone(defectDescription.getResponsibleZone());
		}
		defectResult.setTwoPartDefectFlag(defectDescription.getTwoPartDefectFlag());
		defectResult.setActualTimestamp(new Timestamp(new Date().getTime()));
		return defectResult;
	}

	
}
