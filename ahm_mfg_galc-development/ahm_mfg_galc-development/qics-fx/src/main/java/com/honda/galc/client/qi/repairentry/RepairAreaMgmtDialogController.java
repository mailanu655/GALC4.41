package com.honda.galc.client.qi.repairentry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.qi.base.AbstractQiDialogController;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaSpace;
import com.honda.galc.entity.qi.QiRepairAreaSpaceId;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.StringUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * <h3>RepairAreaMgmtDialogController Class description</h3>
 * <p>
 * RepairAreaMgmtDialogController description
 * </p>
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
 *
 * </TABLE>
 * 
 * @author L&T Infotech<br>
 *         Feb 20, 2017
 *
 *
 */
public class RepairAreaMgmtDialogController extends AbstractQiDialogController<RepairEntryModel, RepairAreaMgmtDialog> {

	private TreeItem<QiRepairResultDto> qiRepairResultDto;
	private QiRepairArea initQiRepairArea; 
	List<QiRepairArea> repairAreaList;
	
	private boolean isInTransitAllowed;

	private static final String PLANT_LBL = "Plant : ";
	private static final String ROW_LBL = "Row : ";
	private static final String SPACE_LBL = "Space : ";
	private static final String REPAIR_AREA_LBL = "Repair Area : ";

	private ChangeListener<? super String> plantComboboxListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			clearDisplayMessage();
			populateDivisionCombobox(newValue, null);
		}
	};
	
	private ChangeListener<? super String> divisionComboboxListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			clearDisplayMessage();
			
			String selectedPlant = getDialog().getPlantCombobox().getControl().getSelectionModel().getSelectedItem();

			if (isInside()) {
				populateParkingRepairAreaCombobox(selectedPlant, newValue, null);
			} else {
				getDialog().getParkingRepairAreaCombobox().getControl().valueProperty().removeListener(parkingRepairAreaComboboxListener);
				getDialog().getRowCombobox().getControl().valueProperty().removeListener(rowComboboxListener);
				
				String selectedRepairArea = populateParkingRepairAreaCombobox(selectedPlant, newValue, null);
				if (!StringUtils.isBlank(selectedRepairArea)) {
					populateRepairRowAndSpaceComboBox(selectedRepairArea);
				} else {
					getDialog().getRowCombobox().getControl().getItems().clear();
					getDialog().getSpaceCombobox().getControl().getItems().clear();
				}
				
				getDialog().getParkingRepairAreaCombobox().getControl().valueProperty().addListener(parkingRepairAreaComboboxListener);
				getDialog().getRowCombobox().getControl().valueProperty().addListener(rowComboboxListener);
			}
		}
	};

	private ChangeListener<? super String> parkingRepairAreaComboboxListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			clearDisplayMessage();
			getDialog().getRowCombobox().getControl().valueProperty().removeListener(rowComboboxListener);
			populateRepairRowAndSpaceComboBox(newValue);
			getDialog().getRowCombobox().getControl().valueProperty().addListener(rowComboboxListener);
		}
	};

	private ChangeListener<? super String> rowComboboxListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			clearDisplayMessage();
			populateRepairAreaSpaceCombobox(getDialog().getParkingRepairAreaCombobox().getControl().getValue(), newValue);
		}
	};

	/** This method will be used to populate target departments and target repair area.
	 * 
	 * @param isPlantChanged
	 */
	private void populateTargetDeptAndRepairArea() {
		String defaultTargetDept = qiRepairResultDto.getValue().getResponsibleDept();

		getDialog().getTargetDeptCombobox().getControl().getItems().clear();
		List<String> targetDeptList = getModel().findAllRespDeptBySitePlant(qiRepairResultDto.getValue().getResponsibleSite(), 
			qiRepairResultDto.getValue().getResponsiblePlant());
			
		targetDeptList = StringUtil.trimStringList(targetDeptList);
		getDialog().getTargetDeptCombobox().getControl().getItems().addAll(targetDeptList);
		if (targetDeptList.contains(defaultTargetDept)) {
			getDialog().getTargetDeptCombobox().getControl().getSelectionModel().select(defaultTargetDept);
		} 

		String defaultTargetRepairArea = qiRepairResultDto.getValue().getRepairArea();
		
		getDialog().getTargetRepairAreaCombobox().getControl().getItems().clear();
		
		List<String> targetRepairAreaList = getModel().findAllRepairAreasBySite(getModel().getSiteName()); //both inside and outside
			
		targetRepairAreaList = StringUtil.trimStringList(targetRepairAreaList);
		getDialog().getTargetRepairAreaCombobox().getControl().getItems().addAll(targetRepairAreaList);
		
		for (String targetRepairArea : targetRepairAreaList) {
			int i = targetRepairArea.indexOf("-");
			if (i != -1 && (targetRepairArea.substring(0, i - 1)).equals(defaultTargetRepairArea)) {
				getDialog().getTargetRepairAreaCombobox().getControl().getSelectionModel().select(targetRepairArea);
				break;
			} 
		}
	}

	public RepairAreaMgmtDialogController(RepairEntryModel entryModel, RepairAreaMgmtDialog repairAreaMgmtDialog){
		super();
		this.setModel(entryModel);
		this.setDialog(repairAreaMgmtDialog);
		EventBusUtil.register(this);
		isInTransitAllowed = false;
	}

	@Override 
	public void close() {
		super.close();
		EventBusUtil.unregister(this);
		this.setModel(null);
		this.setDialog(null);
	}
	
	
	public RepairAreaMgmtDialogController(RepairEntryModel entryModel, RepairAreaMgmtDialog repairAreaMgmtDialog, TreeItem<QiRepairResultDto> qiRepairResultDto) {
		this(entryModel, repairAreaMgmtDialog);
		this.qiRepairResultDto = qiRepairResultDto;
	}

	/**
	 * Populate data on dialog on initial load.
	 *
	 */
	public void initData() {
		try {
			long defectResultId = qiRepairResultDto.getValue().getDefectResultId();
			long repairId = qiRepairResultDto.getValue().getRepairId();
			QiDefectResult qiDefectResult = null;
			QiRepairResult qiRepairResult = null;
			String initRepairArea = "";
			String responsibleSite = "";
			String responsiblePlant = "";
			String responsibleDept = "";
			String entryPlant = "";
			
			//get repair_area, responsible site, plant and department by repair_id from QI_REPAIR_RESULT_TBX
			//or get by defectresultid from QI_DEFECT_RESULT_TBX
			//repair_area is same as REPAIR_AREA_NAME from QI_LOCAL_DEFECT_COMBINATION_TBX
			//responsible site, plant and department may be overwritten during defect/repair entry

			if (repairId != 0) {
				qiRepairResult = getModel().findRepairResultById(repairId);
				initRepairArea = qiRepairResult.getRepairArea();
				responsibleSite = qiRepairResult.getResponsibleSite();
				responsiblePlant = qiRepairResult.getResponsiblePlant();
				responsibleDept = qiRepairResult.getResponsibleDept();
				entryPlant = qiRepairResult.getEntryPlantName();
			} else {
				qiDefectResult = getModel().findDefectResultById(defectResultId);
				initRepairArea = qiDefectResult.getRepairArea();
				responsibleSite = qiDefectResult.getResponsibleSite();
				responsiblePlant = qiDefectResult.getResponsiblePlant();
				responsibleDept = qiDefectResult.getResponsibleDept();
				entryPlant = qiDefectResult.getEntryPlantName();
			}
			
			qiRepairResultDto.getValue().setRepairArea(StringUtils.trimToEmpty(initRepairArea));
			qiRepairResultDto.getValue().setEntryPlant(StringUtils.trimToEmpty(entryPlant));
			qiRepairResultDto.getValue().setResponsibleSite(StringUtils.trimToEmpty(responsibleSite));
			qiRepairResultDto.getValue().setResponsiblePlant(StringUtils.trimToEmpty(responsiblePlant));
			qiRepairResultDto.getValue().setResponsibleDept(StringUtils.trimToEmpty(responsibleDept));
			
			//populate initial repair area filter 
			initQiRepairArea = getModel().findRepairAreaByName(initRepairArea);
			if (null != initQiRepairArea && initQiRepairArea.getLocation() == QiConstant.OUTSIDE_LOCATION) {
				getDialog().getOutsideRadioBtn().setSelected(true);
				getDialog().addOutsideFilterComponents();
				populateRepairAreaFilter(false);
			} else { //inside or transit or no init repair area
				getDialog().getInsideRadioBtn().setSelected(true); 
				getDialog().addInsideFilterComponents();
				populateRepairAreaFilter(true);
			}
			
			//populate existing VIN assignment
			String currentAssignedEntryPlant = "";
			String currentAssignedRepairArea = "";
			String currentAssignedRow = "";
			String currentAssignedSpace = "";
			
			//get repair area space by product_id from QI_REPAIR_AREA_SPACE_TBX
			QiRepairAreaSpace currentAssignedQiRepairAreaSpace = 
					getModel().findRepairAreaSpaceByProductId(qiRepairResultDto.getValue().getProductId());

			if (null != currentAssignedQiRepairAreaSpace) {
				//This product is currently in repair area
				QiRepairArea currentAssignedQiRepairArea = getModel().findRepairAreaByName(currentAssignedQiRepairAreaSpace.getId().getRepairAreaName());
				currentAssignedEntryPlant = currentAssignedQiRepairArea.getPlantName();
				currentAssignedRepairArea = currentAssignedQiRepairArea.getRepairAreaName();
				currentAssignedRow = String.valueOf(currentAssignedQiRepairAreaSpace.getId().getRepairArearRow());
				currentAssignedSpace = String.valueOf(currentAssignedQiRepairAreaSpace.getId().getRepairArearSpace());
			}
			
			getDialog().getPlantLabel().setText(PLANT_LBL + currentAssignedEntryPlant);
			getDialog().getParkingRepairAreaLabel().setText(REPAIR_AREA_LBL + currentAssignedRepairArea);
			getDialog().getRowLabel().setText(ROW_LBL + currentAssignedRow);
			getDialog().getSpaceLabel().setText(SPACE_LBL + currentAssignedSpace);
					
			getDialog().getProductTable().getRoot().getChildren().add(qiRepairResultDto);

			QiStationConfiguration qiStationConfiguration = getModel().findPropertyKeyValueByProcessPoint(
					QiEntryStationConfigurationSettings.SEND_TO_IN_TRANSIT.getSettingsName());

			if (qiStationConfiguration != null) {
				if (StringUtils.trimToEmpty(qiStationConfiguration.getPropertyValue()).equalsIgnoreCase(QiConstant.YES)) {
					isInTransitAllowed = true;
				}
			}
			
			disableEnableButtons();
		} catch (Exception e) {
			displayErrorMessage("Initial load failed.", "Initial load failed.");
		}
	}


	/** This methdod will select the initial plant and area
	 * associated with the defect in case not assigned to any space.
	 * 
	 */
	private void populateRepairAreaFilter(boolean isInside) {
		char location = isInside? QiConstant.INSIDE_LOCATION : QiConstant.OUTSIDE_LOCATION;
		String entryPlant = "";
		String division = "";
		String initRepairAreaName = "";
		
		if (initQiRepairArea != null) {
			entryPlant = initQiRepairArea.getPlantName();
			division = initQiRepairArea.getDivName();
			initRepairAreaName = initQiRepairArea.getRepairAreaName();
		}
		
		repairAreaList = getModel().findAllAvailRepairAreaBySiteLocation(getModel().getSiteName(), location);
		
		String selectedPlant = populatePlantCombobox(entryPlant);
		
		if (!StringUtils.isBlank(selectedPlant)) { //init repair area is set up properly
			String selectedDivision = populateDivisionCombobox(selectedPlant, division);
			
			if (!StringUtils.isBlank(selectedDivision)) { 
				String selectedRepairArea = populateParkingRepairAreaCombobox(selectedPlant, selectedDivision, initRepairAreaName);
				if (!isInside) {
					populateTargetDeptAndRepairArea();
					if (!StringUtils.isBlank(selectedRepairArea)) {
						populateRepairRowAndSpaceComboBox(selectedRepairArea);
					}
				}
			}
		}
	}


	/** This method will delete a row specified from the grid pane.
	 * @param grid
	 * @param row
	 */
	private void deleteRow(GridPane grid, final int row) {
		Set<Node> deleteNodes = new HashSet<Node>();
		for (Node child : grid.getChildren()) {
			Integer rowIndex = GridPane.getRowIndex(child);
			int r = rowIndex == null ? 0 : rowIndex;

			if (r > row) {
				GridPane.setRowIndex(child, r-1);
			} else if (r == row) {
				deleteNodes.add(child);
			}
		}
		
		//remove components and their listeners
		grid.getChildren().removeAll(deleteNodes);
	}


	/**
	 * This method will be used to enable and disable buttons to allow and disallow certain operations. 
	 * 
	 */
	private void disableEnableButtons() {

		if (!isValidateRepairArea() && !isValidateRow() && !isValidateSpace() && validateLocationForPrint()) {
			getDialog().getPrintTicketButton().setDisable(false);
		} else {
			getDialog().getPrintTicketButton().setDisable(true);
		}

		String space = StringUtils.trimToEmpty(getDialog().getSpaceLabel().getText().toString().split(":")[1].toString());
		if(!space.isEmpty()) {
			getDialog().getSendVinInTransitButton().setDisable(true);
			getDialog().getReturnFromRepairAreaButton().setDisable(false);

		}
		else {
			if(isInTransitAllowed){
				getDialog().getSendVinInTransitButton().setDisable(false);
			}
			getDialog().getAssignUnitButton().setDisable(false);
			getDialog().getReturnFromRepairAreaButton().setDisable(true);
		}

	}

	/**
	 * This method will return vin from repair area and space. 
	 *
	 */
	private void returnFromRepairArea() {
		clearDisplayMessage();
		try {
			QiRepairAreaSpaceId repairAreaSpaceId = getQiRepairAreaSpaceId();
			if (null != repairAreaSpaceId) {
				createRepairAreaSpaceHistoryData(repairAreaSpaceId);
				if(null == AbstractRepairEntryView.getParentCachedDefectList() || AbstractRepairEntryView.getParentCachedDefectList().isEmpty())
					getModel().clearRepairAreaSpace(repairAreaSpaceId, getUserId());

				EventBusUtil.publish(new StatusMessageEvent("Unit returned from repair area space successfully.", StatusMessageEventType.DIALOG_INFO));

				updateOnScreenContent(false, null, null);
				disableEnableButtons();
			}
		} catch (Exception e) {
			displayErrorMessage("An error occured during unit returning from repair area.", "Failed to return unit from repair area.");
		}
	}
	/**
	 * This method is used to create Repair Area Space History data. 
	 *
	 */
	private void createRepairAreaSpaceHistoryData(QiRepairAreaSpaceId repairAreaSpaceId) {
		QiRepairAreaSpace qiRepairAreaSpace = getModel().findRepairAreaSpaceById(repairAreaSpaceId);
		getModel().saveRepairAreaSpaceHistoryData(getModel().setRepairAreaSpaceHistoryData(qiRepairAreaSpace));
	}

	/** This method will send the vin to in transit repair area.
	 *  The vin will be auto - assigned to the first available repair area space in the in transit repair area 
	 *  available in the current selected plant considering their row and space fill sequences.
	 *
	 */
	private void sendVinInTransit() {
		clearDisplayMessage();
		String plant = null;
		QiRepairArea inTransitRepairArea = null;
		if (!isValidatePlantComboBox()) {
			plant = StringUtils.trimToEmpty(getDialog().getPlantCombobox().getControl().getValue());
		} else {
			displayErrorMessage("Mandatory field Plant not selected.", "Please select plant.");
			return;
		}

		try {
			inTransitRepairArea = getModel().findInTransitRepairAreaBySiteAndPlant(getModel().getSiteName(), plant);
			if (null != inTransitRepairArea) {
				QiRepairAreaRow qiRepairAreaRow = getModel().findFirstAvailableRowByRepairArea(inTransitRepairArea);

				if(null != qiRepairAreaRow){
					QiRepairAreaSpace qiRepairAreaSpace = getModel().findFirstAvailableSpaceByRow(qiRepairAreaRow);

					if(null != qiRepairAreaSpace ){
						if(null == AbstractRepairEntryView.getParentCachedDefectList() || AbstractRepairEntryView.getParentCachedDefectList().isEmpty()){
							getModel().assignRepairAreaSpace(qiRepairResultDto.getValue().getProductId(),
									qiRepairResultDto.getValue().getDefectResultId(), getUserId(), qiRepairAreaSpace.getId());
						}
						EventBusUtil.publish(new StatusMessageEvent("Unit assigned to repair area space successfully.", StatusMessageEventType.DIALOG_INFO));
						updateOnScreenContent(true, StringUtils.trimToEmpty(getDialog().getPlantCombobox().getControl().getValue()), qiRepairAreaSpace.getId());
						disableEnableButtons();
					}
				}

				else{
					displayErrorMessage("No space is available in intransit repair area.", "No space is available in intransit repair area.");
				}

			} else {
				displayErrorMessage("In transit repair area not present at selected plant.", "In transit repair area not present at selected plant.");
			}

		} catch (Exception e) {
			displayErrorMessage("An error occured during transit.", "Failed to perform transit.");
		}

	}


	/**
	 * This method will update the on screen content.
	 *
	 */
	private void updateOnScreenContent(boolean isAssigned, String plant, QiRepairAreaSpaceId spaceId) {

		if (isAssigned) {
			getDialog().getPlantLabel().setText(PLANT_LBL + StringUtils.trimToEmpty(plant));
			getDialog().getParkingRepairAreaLabel().setText(REPAIR_AREA_LBL + spaceId.getRepairAreaName());
			getDialog().getRowLabel().setText(ROW_LBL + spaceId.getRepairArearRow());
			getDialog().getSpaceLabel().setText(SPACE_LBL + spaceId.getRepairArearSpace());
		}

		else {
			getDialog().getPlantLabel().setText(PLANT_LBL);
			getDialog().getParkingRepairAreaLabel().setText(REPAIR_AREA_LBL);
			getDialog().getRowLabel().setText(ROW_LBL);
			getDialog().getSpaceLabel().setText(SPACE_LBL);
		}

	}


	/**
	 * This method will assing unit to vin.
	 *
	 */
	private void assignUnit() {
		clearDisplayMessage();
		returnFromRepairArea();
		if(isInside()){
			assignUnitToInsideRepairArea();
		}
		else{
			assignUnitToOutsideRepairArea();
		}
	}


	/** This method will assign outside repair area space to the vin.
	 * 
	 */
	private void assignUnitToOutsideRepairArea() {
		if(mandatoryFieldCheck()) {

			QiRepairAreaSpaceId qiRepairAreaSpaceId = new QiRepairAreaSpaceId(getDialog().getParkingRepairAreaCombobox().getControl().getValue(),
					(Integer.parseInt(getDialog().getRowCombobox().getControl().getValue())),
					(Integer.parseInt(getDialog().getSpaceCombobox().getControl().getValue())));
			
			QiRepairAreaSpace qiRepairAreaSpace = getModel().findRepairAreaSpaceById(qiRepairAreaSpaceId);
			
			if (qiRepairAreaSpace.getProductId().isEmpty() && null == qiRepairAreaSpace.getDefectResultId()) {
				try {
					
					String targetRespDept = getDialog().getTargetDeptCombobox().getControl().getValue();
					String targetRepairArea = getDialog().getTargetRepairAreaCombobox().getControl().getValue();
					if (targetRespDept == null) {
						targetRespDept = "";
					}
					
					if (targetRepairArea == null) {
						targetRepairArea = "";
					} else {
						int i = targetRepairArea.indexOf("-");
						if (i != -1) {
							targetRepairArea = targetRepairArea.substring(0, i - 1);
						}
					}
					if(null == AbstractRepairEntryView.getParentCachedDefectList() || AbstractRepairEntryView.getParentCachedDefectList().isEmpty()){
						getModel().assignRepairAreaSpaceWithTarget(qiRepairResultDto.getValue().getProductId(),
							qiRepairResultDto.getValue().getDefectResultId(), getUserId(), targetRespDept, targetRepairArea, qiRepairAreaSpaceId);
					}

					EventBusUtil.publish(new StatusMessageEvent("Unit assigned to repair area space successfully.", StatusMessageEventType.DIALOG_INFO));

					getDialog().getParkingRepairAreaLabel().setText(REPAIR_AREA_LBL + (StringUtils.trimToEmpty(getDialog().getParkingRepairAreaCombobox().getControl().getValue())));
					getDialog().getRowLabel().setText(ROW_LBL + (StringUtils.trimToEmpty(getDialog().getRowCombobox().getControl().getValue())));
					getDialog().getSpaceLabel().setText(SPACE_LBL + (StringUtils.trimToEmpty(getDialog().getSpaceCombobox().getControl().getValue())));

					updateOnScreenContent(true, getDialog().getPlantCombobox().getControl().getValue(), 
							new QiRepairAreaSpaceId(getDialog().getParkingRepairAreaCombobox().getControl().getValue(),
									Integer.parseInt(getDialog().getRowCombobox().getControl().getValue()),
									Integer.parseInt(getDialog().getSpaceCombobox().getControl().getValue())));
					
					disableEnableButtons();

					//no print, used to broadcast for print here, but new requirements - print button only

				} catch (Exception e) {
					displayErrorMessage("An error occured during assign repair area.", "Failed to assign repair area.");
				}
			}

			else {
				displayErrorMessage("Repair area space is currently in use.", "Repair area space is currently in use.");
			} 
		}
	}


	/** This method will be used to assign unit to inside repair area.
	 * 
	 *  The vin will be auto - assigned to the first available repair area space in the selected repair area 
	 *  considering their row and space fill sequences.
	 * 
	 */
	private void assignUnitToInsideRepairArea() {
		try {
			if (isValidateRepairAreaComboBox()) {
				displayErrorMessage("Mandatory field Repair Area is empty", "Please Select Repair Area.");
				return;
			}

			QiRepairArea qiRepairArea = getModel().findRepairAreaByName(getDialog().getParkingRepairAreaCombobox().getControl().getValue());

			if(null != qiRepairArea)
			{
				QiRepairAreaRow qiRepairAreaRow = getModel().findFirstAvailableRowByRepairArea(qiRepairArea);

				if(null != qiRepairAreaRow){
					QiRepairAreaSpace qiRepairAreaSpace = getModel().findFirstAvailableSpaceByRow(qiRepairAreaRow);

					if(null != qiRepairAreaSpace ){
						if(null == AbstractRepairEntryView.getParentCachedDefectList() || AbstractRepairEntryView.getParentCachedDefectList().isEmpty()){
							getModel().assignRepairAreaSpace(qiRepairResultDto.getValue().getProductId(),
								qiRepairResultDto.getValue().getDefectResultId(), getUserId(), qiRepairAreaSpace.getId());
						}
						EventBusUtil.publish(new StatusMessageEvent("Unit assigned to repair area space successfully.", StatusMessageEventType.DIALOG_INFO));
						updateOnScreenContent(true, StringUtils.trimToEmpty(getDialog().getPlantCombobox().getControl().getValue()), qiRepairAreaSpace.getId());
						disableEnableButtons();
					}

				}

				else{
					displayErrorMessage("No space is available in selected repair area.", "No space is available in selected repair area.");
				}
			}
		} catch (Exception e) {
			displayErrorMessage("An error occured during assign repair area.", "Failed to assign repair area.");
		}
	}


	/** This method is used to check all the mandatory fields have selected.
	 * 
	 * @return returns true if all mandatory fields for outside repair area space are selected otherwise returns false.
	 */
	private boolean mandatoryFieldCheck() {

		if (isValidateRepairAreaComboBox()) {
			displayErrorMessage("Mandatory field Repair Area is empty", "Please Select Repair Area.");
			return false;
		}

		if (isValidateRowComboBox()) {
			displayErrorMessage("Mandatory field Row is empty", "Please Select Row.");
			return false;
		}

		if (isValidateSpaceComboBox()) {
			displayErrorMessage("Mandatory field Space is empty", "Please Select Space.");
			return false;
		}

		return true;
	}


	@Override
	public void initListeners() {
		initListeners(isInside());
	}
	
	public void initListeners(boolean isInside) {
		getDialog().getPlantCombobox().getControl().valueProperty().addListener(plantComboboxListener);
		getDialog().getDivisionCombobox().getControl().valueProperty().addListener(divisionComboboxListener);
		if (!isInside) {
			getDialog().getParkingRepairAreaCombobox().getControl().valueProperty().addListener(parkingRepairAreaComboboxListener);
			getDialog().getRowCombobox().getControl().valueProperty().addListener(rowComboboxListener);
		}
	}

	
	/** This method will populate plant combobox.
	 * 
	 */
	private String populatePlantCombobox(String entryPlant) {
		List<String> plantList = new ArrayList<String>();
		if (repairAreaList != null && repairAreaList.size() > 0) {
			for (QiRepairArea ra : repairAreaList) {
				if (!plantList.contains(StringUtils.trim(ra.getPlantName()))) {
					plantList.add(ra.getPlantName());
				}
			}
		}
		
		if (plantList.size() > 1) {
			Collections.sort(plantList);
		}
		
		getDialog().getPlantCombobox().getControl().getItems().clear();
		getDialog().getPlantCombobox().getControl().getItems().addAll(StringUtil.trimStringList(plantList));
		
		if (getDialog().getPlantCombobox().getControl().getItems().contains(entryPlant)) {
			getDialog().getPlantCombobox().getControl().getSelectionModel().select(entryPlant);
		} else if (getDialog().getPlantCombobox().getControl().getItems().size() == 1) {
			getDialog().getPlantCombobox().getControl().getSelectionModel().selectFirst();
		} 
		return getDialog().getPlantCombobox().getControl().getSelectionModel().getSelectedItem();
	}
	
	private String populateDivisionCombobox(String selectedPlant, String division) {
		List<String> divisionList = new ArrayList<String>();
		if (repairAreaList != null && repairAreaList.size() > 0) {
			for (QiRepairArea ra : repairAreaList) {
				if (ra.getPlantName().equals(selectedPlant)) {
					if (!divisionList.contains(StringUtils.trim(ra.getDivName()))) {
						divisionList.add(ra.getDivName());
					}
				}
			}
		}
		
		if (divisionList.size() > 1) {
			Collections.sort(divisionList);
		}
		
		getDialog().getDivisionCombobox().getControl().getItems().clear();
		getDialog().getDivisionCombobox().getControl().getItems().addAll(StringUtil.trimStringList(divisionList));
		
		if (getDialog().getDivisionCombobox().getControl().getItems().contains(division)) {
			getDialog().getDivisionCombobox().getControl().getSelectionModel().select(division);
		} else if (getDialog().getDivisionCombobox().getControl().getItems().size() == 1) {
			getDialog().getDivisionCombobox().getControl().getSelectionModel().selectFirst();
		} 
		return getDialog().getDivisionCombobox().getControl().getSelectionModel().getSelectedItem();
	}

	/** This method will convert integer list to string list.
	 * 
	 * @param list
	 * @return string list 
	 */
	private List<String> getStringList(List<Integer> list) {
		List<String> stringList = new ArrayList<String>(/*list.size()*/);
		if (null != list) {
			for (Integer row : list) {
				stringList.add(String.valueOf(row));
			}
			return stringList;
		}
		return null;
	}

	/**  This method will qiRepairResultDto.
	 * @return qiRepairResultDto
	 */
	public TreeItem<QiRepairResultDto> getQiRepairResultDto() {
		return qiRepairResultDto;
	}


	/** This method will return repair area space if vin is currently assinged to any space or returns null.
	 * @return QiRepairAreaSpaceId where vin is currently parked.
	 */
	public QiRepairAreaSpaceId getQiRepairAreaSpaceId() {

		if(!isValidateRepairArea() && !isValidateRow() && !isValidateSpace()){
			return new QiRepairAreaSpaceId(getDialog().getParkingRepairAreaLabel().getText().split(": ")[1].toString(),
					Integer.parseInt(getDialog().getRowLabel().getText().split(": ")[1].toString()),
					Integer.parseInt(getDialog().getSpaceLabel().getText().split(": ")[1].toString()));
		}

		return null;
	}
	
	/**
	 * This method will populate repair area combobox.
	 *
	 */
	private String populateParkingRepairAreaCombobox(String selectedPlant, String selectedDivision, String repairArea) {
		List<String> repairAreaNameList = new ArrayList<String>();
				
		if (repairAreaList != null && repairAreaList.size() > 0) {
			for (QiRepairArea ra : repairAreaList) {
				if (ra.getPlantName().equals(selectedPlant) && ra.getDivName().equals(selectedDivision)) {
					if (!repairAreaNameList.contains(StringUtils.trim(ra.getRepairAreaName()))) {
						repairAreaNameList.add(ra.getRepairAreaName());
					}
				}
			}
		}
		
		if (repairAreaNameList.size() > 1) {
			Collections.sort(repairAreaNameList);
		}
				
		getDialog().getParkingRepairAreaCombobox().getControl().getItems().clear();
		getDialog().getParkingRepairAreaCombobox().getControl().getItems().addAll(StringUtil.trimStringList(repairAreaNameList));

		if (getDialog().getParkingRepairAreaCombobox().getControl().getItems().contains(repairArea)) {
			getDialog().getParkingRepairAreaCombobox().getControl().getSelectionModel().select(repairArea);
		} else if (getDialog().getParkingRepairAreaCombobox().getControl().getItems().size() == 1) {
			getDialog().getParkingRepairAreaCombobox().getControl().getSelectionModel().selectFirst();
		}
		return getDialog().getParkingRepairAreaCombobox().getControl().getSelectionModel().getSelectedItem();
	}


	/**
	 * This method will populate repair area space combobox.
	 *
	 */
	private void populateRepairAreaSpaceCombobox(String selectedRepairArea, String selectedRow) {
		List<String> repairAreaSpaceList = new ArrayList<String>();

		repairAreaSpaceList = getStringList(getModel().findAllAvailableSpaceByRepairAreaRow(selectedRepairArea, new Integer(selectedRow)));

		getDialog().getSpaceCombobox().getControl().getItems().clear();
		getDialog().getSpaceCombobox().getControl().getItems().addAll(repairAreaSpaceList);
		if (getDialog().getSpaceCombobox().getControl().getItems().size() == 1) {
			getDialog().getSpaceCombobox().getControl().getSelectionModel().selectFirst();
		}
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() instanceof LoggedButton) {
			handleButtonEvents(event);
		}

		else if (event.getSource() instanceof LoggedRadioButton) {
			handleRadioButtonEvents(event);
		}
	}

	/** This method will handle events on radio buttons on RepairAreaMgmtDialog.
	 * 
	 * @param event
	 */
	private void handleRadioButtonEvents(ActionEvent event) {
		LoggedRadioButton radioButton = (LoggedRadioButton) event.getSource();

		if (getDialog().getInsideRadioBtn().getText().equalsIgnoreCase(radioButton.getText())) {
			//outside to inside
			deleteRow(getDialog().getRepairAreaFilterGrid(), 2);
			deleteRow(getDialog().getRepairAreaFilterGrid(), 1);
			getDialog().addInsideFilterComponents();
			populateRepairAreaFilter(true);
			initListeners(true);
		
		} else if (getDialog().getOutsideRadioBtn().getText().equalsIgnoreCase(radioButton.getText())) {
			//inside to outside
			deleteRow(getDialog().getRepairAreaFilterGrid(), 1);
			getDialog().addOutsideFilterComponents();
			populateRepairAreaFilter(false);
			initListeners(false);
		}
	}

	/** This method will handle events on buttons on RepairAreaMgmtDialog.
	 * 
	 * @param event
	 */
	private void handleButtonEvents(ActionEvent event) {
		LoggedButton loggedButton = (LoggedButton) event.getSource();

		if (getDialog().getCloseButton().getText().equalsIgnoreCase(loggedButton.getText())) {
			Stage stage = (Stage) loggedButton.getScene().getWindow();
			stage.close();
		}

		else {
			
			//check current tracking status again
			if (getModel().isPreviousLineInvalid()) {
				publishProductPreviousLineInvalidEvent();
				return;
			}
			
			if (getDialog().getAssignUnitButton().getText().equalsIgnoreCase(loggedButton.getText())) {
				assignUnit();
			}
	
			else if (getDialog().getSendVinInTransitButton().getText().equalsIgnoreCase(loggedButton.getText())) {
				sendVinInTransit();
			}
	
			else if (getDialog().getPrintTicketButton().getText().equalsIgnoreCase(loggedButton.getText())) {
				printTicket(false);
			}
	
			else if (getDialog().getReturnFromRepairAreaButton().getText().equalsIgnoreCase(loggedButton.getText())) {
				returnFromRepairArea();
			}
		}
	}


	/** Checks if is validate repair area name.
	 *
	 * @return true, if is validate repair area name
	 */
	private boolean isValidateRepairArea() {
		return getDialog().getParkingRepairAreaLabel().getText().equalsIgnoreCase(REPAIR_AREA_LBL) ? true : false ;
	}

	/** Checks if is validate repair area name.
	 *
	 * @return true, if is validate repair area name
	 */
	private boolean isValidateRepairAreaComboBox() {
		return getDialog().getParkingRepairAreaCombobox().getControl().getValue() != null ? false : true;
	}

	/** Checks if is validate repair area name.
	 *
	 * @return true, if is validate repair area name
	 */
	private boolean isValidatePlantComboBox() {
		return getDialog().getPlantCombobox().getControl().getValue() != null ? false : true;
	}

	/** Checks if is validate row.
	 *
	 * @return true, if is validate row
	 */
	private boolean isValidateRow() {
		return getDialog().getRowLabel().getText().equalsIgnoreCase(ROW_LBL)  ? true : false;
	}

	/** Checks if is validate row ComboBox.
	 *
	 * @return true, if is validate row ComboBox
	 */
	private boolean isValidateRowComboBox() {
		return getDialog().getRowCombobox().getControl().getValue() != null ? false : true;
	}

	/** Checks if is validate space.
	 *
	 * @return true, if is validate space
	 */
	private boolean isValidateSpace() {
		return getDialog().getSpaceLabel().getText().equalsIgnoreCase(SPACE_LBL)  ? true : false;
	}

	/** Checks if is validate space ComboBox.
	 *
	 * @return true, if is validate space ComboBox
	 */
	private boolean isValidateSpaceComboBox(){
		return getDialog().getSpaceCombobox().getControl().getValue() != null ? false : true;
	}

	/**
	 * This method is used to trigger print functionality.
	 */
	private void printTicket(boolean isAssignAndPrint) {
		clearDisplayMessage();
		QiRepairResultDto dto = qiRepairResultDto.getValue();
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, dto.getProductId());
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE,getModel().getProductSpecCode());
		dc.put(DataContainerTag.ROW_VALUE,getDialog().getRowLabel().getText().split(":")[1].toString().trim());
		dc.put(DataContainerTag.SPACE_VALUE,getDialog().getSpaceLabel().getText().split(":")[1].toString().trim());
		dc.put(DataContainerTag.DEFECT_RESULT_ID,String.valueOf(qiRepairResultDto.getValue().getDefectResultId()));
		dc.put(DataContainerTag.USER_ID, getUserId());
		ServiceFactory.getService(BroadcastService.class).broadcast(getModel().getApplicationContext().getProcessPointId(), 1, dc);
		if(!isAssignAndPrint)
			EventBusUtil.publish(new StatusMessageEvent("Print successful.", StatusMessageEventType.DIALOG_INFO));
		else
			EventBusUtil.publish(new StatusMessageEvent("Assign unit and printed ticket successfully.", StatusMessageEventType.DIALOG_INFO));
	}

	/** This method will be used to validate print.
	 * 
	 * @return
	 */
	private boolean validateLocationForPrint() {

		String repairArea = getDialog().getParkingRepairAreaLabel().getText().split(":")[1].toString().trim();
		if (null != repairArea) {
			QiRepairArea qiRepairArea = getModel().findRepairAreaByName(repairArea);
			if (null != qiRepairArea) {
				if (qiRepairArea.getLocation() == QiConstant.OUTSIDE_LOCATION)
					return true;
				else
					return false;
			}
		}
		return false;

	}

	/** This method is used to check whether it is a repair parking Station.
	 */
	private boolean isRepairParkingStation() {
		boolean isRepairParkingStation = false;
		// Check if it is a Repair Parking Station configured from Station Config Screen.
		QiStationConfiguration qiEntryStationConfigManagement = getModel()
				.findPropertyKeyValueByProcessPoint(QiConstant.REPAIR_PARKING_STATION);
		if (qiEntryStationConfigManagement != null
				&& qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.YES))
			isRepairParkingStation = true;
		return isRepairParkingStation;
	}


	/** This method will be used to check whether Inside Repair Area radio button is selected or not.
	 * @return true if Inside Repair Area is selected.
	 */
	private boolean isInside(){
		return getDialog().getInsideRadioBtn().isSelected();
	}

	
	private void populateRepairRowAndSpaceComboBox(String selectedRepairArea) {
		getDialog().getRowCombobox().getControl().getItems().clear();
		getDialog().getSpaceCombobox().getControl().getItems().clear();
		List<String> rowList = getStringList(getModel().findAllAvailableRowByRepairArea(selectedRepairArea));
		if (getModel().findRepairAreaByName(selectedRepairArea).getRowFillSeq() == 'D') {
			Collections.reverse(rowList);
		}
				
		getDialog().getRowCombobox().getControl().getItems().addAll(rowList);
				
		if (getDialog().getRowCombobox().getControl().getItems().size() == 1) {
			getDialog().getRowCombobox().getControl().getSelectionModel().selectFirst();
			String selectedRow = getDialog().getRowCombobox().getControl().getItems().get(0);
			populateRepairAreaSpaceCombobox(selectedRepairArea, selectedRow);
		}
	}
}
