package com.honda.galc.client.teamleader.qi.stationconfig.previousdefect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigController;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigPanel;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiEntryDepartmentDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.entity.qi.QiStationPreviousDefectId;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
/**
 * 
 * <h3>PreviousDefectVisibleMaintController Class description</h3>
 * <p>
 * PreviousDefectVisibleMaintController is used to load data in TableViews and perform the action on Update , Reset and Shift buttonsetc.
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
 * @author LnTInfotech<br>
 * 
 */
public class PreviousDefectVisibleMaintController extends EntryStationConfigController {
	public PreviousDefectVisibleMaintController(EntryStationConfigModel model,
			EntryStationConfigPanel view) {
		super(model, view);
	}
	public void loadInitialData() {
		String processPoint= StringUtils.trimToEmpty(getView().getEntryStationPanel().getQicsStationComboBoxSelectedId().toString());
		loadAllEntryDept(processPoint);	
		if(isFullAccess()){
			addAvailableEntryDepartmentTableListners();
			addAssignedEntryDepartmentTableListners();
		}
	}

	/**
	 * This method is used to perform action on 'Update', 'Reset' ,'shift' buttons for EntryDept , WriteupDept and EntryScreen table view
	 */
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			
			switch(StationConfigurationOperations.getType(loggedButton.getId())){

			case RIGHT_SHIFT_ENTRY_DEPARTMENT_DEFECT_STATUS :
				assignEntryDeptAction(actionEvent);
				getView().getEntryDepartmentDefectPanel().getRightShiftEntryDepartment().setDisable(true);
				break;

			case LEFT_SHIFT_ENTRY_DEPARTMENT_DEFECT_STATUS :
				deassignEntryDeptAction(actionEvent);
				getView().getEntryDepartmentDefectPanel().getLeftShiftEntryDepartment().setDisable(true);
				break;

			case UPDATE_ENTRY_DEPARTMENT_DEFECT_BUTTON :
				updateEntryDeptAction(actionEvent);
				break; 

			case RESET_ENTRY_DEPARTMENT_DEFECT_BUTTON :
				resetEntryDepartmentDefect();
				break;
			default:
				break;
			}
		}
	}


	/**
	 * This method is used to shift available EntryDeptDefect to currently assigned EntryDeptDefect
	 * @param actionEvent
	 */
	private void assignEntryDeptAction(ActionEvent actionEvent) {
		PreviousDefectVisibleDialog dialog =new PreviousDefectVisibleDialog("Entry Dpartments",isFullAccess(), getModel(),getView().getEntryDepartmentDefectPanel().getAvailableEntryDepartmentDefectTablePane().getTable().getSelectionModel().getSelectedItems(),getApplicationId());
		dialog.showDialog();
		if (dialog.getAssignedEntryDepartments() != null) {
			getView().getEntryDepartmentDefectPanel().getAvailableEntryDepartmentDefectTablePane().getTable().getItems().removeAll(dialog.getAssignedEntryDepartments());
			getView().getEntryDepartmentDefectPanel().getCurrentlyAssignedEntryDepartmentDefectTablePane().getTable().getItems().addAll(dialog.getAssignedEntryDepartments());
			getView().getEntryDepartmentDefectPanel().getUpdateEntryDepartmenDefectStatustBtn().setDisable(false);
			getView().getEntryDepartmentDefectPanel().getResetEntryDepartmenDefectStatustBtn().setDisable(false);
		} else { //clicked cancel button on dialog
			getView().getEntryDepartmentDefectPanel().getUpdateEntryDepartmenDefectStatustBtn().setDisable(true);
			getView().getEntryDepartmentDefectPanel().getResetEntryDepartmenDefectStatustBtn().setDisable(false);
		}
		getView().getEntryDepartmentDefectPanel().getAvailableEntryDepartmentDefectTablePane().clearSelection();
	}

	/**
	 * This method is used to shift currently assigned EntryDeptDefect to available EntryDeptDefect
	 * @param actionEvent
	 */
	private void deassignEntryDeptAction(ActionEvent actionEvent) {
		List<QiEntryDepartmentDto> currentlyAssignedDepartmentsList = getView().getEntryDepartmentDefectPanel().getCurrentlyAssignedEntryDepartmentDefectTablePane().getSelectedItems();
		if (!currentlyAssignedDepartmentsList.isEmpty()) {
			getView().getEntryDepartmentDefectPanel().getAvailableEntryDepartmentDefectTablePane().getTable().getItems().addAll(currentlyAssignedDepartmentsList);
			getView().getEntryDepartmentDefectPanel().getCurrentlyAssignedEntryDepartmentDefectTablePane().getTable().getItems().removeAll(currentlyAssignedDepartmentsList);
			getView().getEntryDepartmentDefectPanel().getUpdateEntryDepartmenDefectStatustBtn().setDisable(false);
			getView().getEntryDepartmentDefectPanel().getResetEntryDepartmenDefectStatustBtn().setDisable(false);
		}
		getView().getEntryDepartmentDefectPanel().getCurrentlyAssignedEntryDepartmentDefectTablePane().clearSelection();
	}

	private void resetEntryDepartmentDefect() {
		loadAllEntryDept(StringUtils.trimToEmpty(getView().getEntryStationPanel().getQicsStationComboBoxSelectedId().toString()));
		getView().getEntryDepartmentDefectPanel().getUpdateEntryDepartmenDefectStatustBtn().setDisable(true);
		getView().getEntryDepartmentDefectPanel().getResetEntryDepartmenDefectStatustBtn().setDisable(true);
	}

	private void updateEntryDeptAction(ActionEvent actionEvent) {
		List<QiEntryDepartmentDto> currentlyAssignedEntryDeptList = getView().getEntryDepartmentDefectPanel().getCurrentlyAssignedEntryDepartmentDefectTablePane().getTable().getItems();
		List<QiStationPreviousDefect> qiStationEntryDepartmentDbList = getModel().findAllEntryDeptDefectInfoByProcessPoint(getView().getEntryStationPanel().getQicsStationComboBoxSelectedId().toString().trim());
		QiEntryDepartmentDto qiEntryDepartmentDto;
		List<QiEntryDepartmentDto> qiStationEntryDepartmentList=new ArrayList<QiEntryDepartmentDto>();
		for (QiStationPreviousDefect qiStationEntryDepartment : qiStationEntryDepartmentDbList) {
			qiEntryDepartmentDto= new QiEntryDepartmentDto();
			qiEntryDepartmentDto.setDivisionId(qiStationEntryDepartment.getId().getEntryDivisionId());
			qiEntryDepartmentDto.setOriginalDefectStatus(DefectStatus.getType(qiStationEntryDepartment.getOriginalDefectStatus()).getName());
			qiEntryDepartmentDto.setCurrentDefectStatus(DefectStatus.getType(qiStationEntryDepartment.getCurrentDefectStatus()).getName());
			qiStationEntryDepartmentList.add(qiEntryDepartmentDto);
		}
		removeEntryDepartmentDefect(qiStationEntryDepartmentList,currentlyAssignedEntryDeptList);
		addEntryDepartmentDefect(currentlyAssignedEntryDeptList);
		getView().getEntryDepartmentDefectPanel().getUpdateEntryDepartmenDefectStatustBtn().setDisable(true);
		getView().getEntryDepartmentDefectPanel().getResetEntryDepartmenDefectStatustBtn().setDisable(true);
	}

	private void addEntryDepartmentDefect(List<QiEntryDepartmentDto> currentlyAssignedEntryDeptList) {
		QiStationPreviousDefect qiStationEntryDepartment;
		QiStationPreviousDefectId  qiStationEntryDepartmentId;
		List<QiStationPreviousDefect> addedStationEntryDepartmentList = new ArrayList<QiStationPreviousDefect>();
		for (QiEntryDepartmentDto departmentDto : currentlyAssignedEntryDeptList) {
			qiStationEntryDepartment = new QiStationPreviousDefect();
			qiStationEntryDepartmentId = new QiStationPreviousDefectId();
			qiStationEntryDepartmentId.setEntryDivisionId(departmentDto.getDivisionId());
			qiStationEntryDepartmentId.setProcessPointId((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
			qiStationEntryDepartment.setId(qiStationEntryDepartmentId);
			qiStationEntryDepartment.setCreateUser(getUserId());
			qiStationEntryDepartment.setOriginalDefectStatus((short)DefectStatus.getType(StringUtils.trimToEmpty(departmentDto.getOriginalDefectStatus())).getId());
			qiStationEntryDepartment.setCurrentDefectStatus((short)DefectStatus.getType(StringUtils.trimToEmpty(departmentDto.getCurrentDefectStatus())).getId());
			addedStationEntryDepartmentList.add(qiStationEntryDepartment);
		}
		if(!addedStationEntryDepartmentList.isEmpty()) {
			getModel().createStationEntryDepartmentDefects(addedStationEntryDepartmentList);
		}
	}

	private void removeEntryDepartmentDefect(List<QiEntryDepartmentDto> qiStationEntryDepartmentList,
			List<QiEntryDepartmentDto> currentlyAssignedEntryDeptList) {
		QiStationPreviousDefect qiStationEntryDepartment;
		QiStationPreviousDefectId  qiStationEntryDepartmentId;
		List<QiStationPreviousDefect> removedStationEntryDepartmentDefectList = new ArrayList<QiStationPreviousDefect>();
		for (QiEntryDepartmentDto departmentDto : qiStationEntryDepartmentList) {
			if(!currentlyAssignedEntryDeptList.contains(departmentDto)) {
				qiStationEntryDepartment = new QiStationPreviousDefect();
				qiStationEntryDepartmentId = new QiStationPreviousDefectId();
				qiStationEntryDepartmentId.setEntryDivisionId(departmentDto.getDivisionId());
				qiStationEntryDepartmentId.setProcessPointId((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
				qiStationEntryDepartment.setId(qiStationEntryDepartmentId);
				qiStationEntryDepartment.setOriginalDefectStatus((short)DefectStatus.getType(departmentDto.getOriginalDefectStatus()).getId());
				qiStationEntryDepartment.setCurrentDefectStatus((short)DefectStatus.getType(departmentDto.getCurrentDefectStatus()).getId());
				removedStationEntryDepartmentDefectList.add(qiStationEntryDepartment);
			}
		}
		if(!removedStationEntryDepartmentDefectList.isEmpty()){
			getModel().removeStationEntryDepartmentDefects(removedStationEntryDepartmentDefectList);
			for (QiStationPreviousDefect qiStationPreviousDefect : removedStationEntryDepartmentDefectList) {
				AuditLoggerUtil.logAuditInfo(qiStationPreviousDefect, null, QiConstant.UPDATE_REASON_FOR_AUDIT, getView().getScreenName(),getUserId());
			}
		}

	}



	/**
	 * This method is used to populate available and assigned entryDeptDefect tableview
	 * @param processPoint
	 */
	private void loadAllEntryDept(String processPoint) {
		List<QiStationPreviousDefect> qiStationEntryDepartmentList = getModel().findAllEntryDeptDefectInfoByProcessPoint(processPoint);
		List<QiEntryDepartmentDto> assignedEntryDepartmentList = new ArrayList<QiEntryDepartmentDto>(QiCommonUtil.getUniqueArrayList(Lists.transform(qiStationEntryDepartmentList, GET_ASSINGED_DEPT_DEFECT_LIST)));
		List<Division>  divisionList = getModel().findAllDivisionBySiteAndPlant(getView().getEntryStationPanel().getSiteValueLabel().getText(),(String)getView().getEntryStationPanel().getPlantComboBox().getValue());
		List<QiEntryDepartmentDto> entryDepartmentDtoList = new ArrayList<QiEntryDepartmentDto>(QiCommonUtil.getUniqueArrayList(Lists.transform(divisionList, GET_AVAILABLE_DEPT_DEFECT_LIST)));
		assignDivisionDetail(assignedEntryDepartmentList);
		assignDivisionDetail(entryDepartmentDtoList);
		loadAllEntryDept(entryDepartmentDtoList,assignedEntryDepartmentList);
	}
	
	public void assignDivisionDetail(List<QiEntryDepartmentDto> dtoList)  {
		for(QiEntryDepartmentDto dto : dtoList)  {
			Division d = getModel().findDivisionByDivisionId(dto.getDivisionId());
			dto.setDivisionName(d.getDivisionName());
			dto.setDivisionDescription(d.getDivisionDescription());
		}
	}
	
	/** dto
	 * This method is used to populate available and assigned entryDeptDefect tableview
	 * @param entryDepartmentDtoList
	 * @param assignedEntryDepartmentList
	 */
	@SuppressWarnings("unchecked")
	private void loadAllEntryDept(List<QiEntryDepartmentDto> entryDepartmentDtoList,List<QiEntryDepartmentDto> assignedEntryDepartmentList){
		List<QiEntryDepartmentDto> subtractedAllEntryDeptList = ListUtils.subtract(entryDepartmentDtoList,assignedEntryDepartmentList);
		Collections.sort(subtractedAllEntryDeptList,new QiEntryDepartmentDto());
		Collections.sort(assignedEntryDepartmentList,new QiEntryDepartmentDto());
		getView().getEntryDepartmentDefectPanel().getAvailableEntryDepartmentDefectTablePane().setData(subtractedAllEntryDeptList);
		getView().getEntryDepartmentDefectPanel().getCurrentlyAssignedEntryDepartmentDefectTablePane().setData(assignedEntryDepartmentList);
	}
	
	private static final Function<QiStationPreviousDefect, QiEntryDepartmentDto> GET_ASSINGED_DEPT_DEFECT_LIST =
			new Function<QiStationPreviousDefect, QiEntryDepartmentDto>() {
		@Override
		public QiEntryDepartmentDto apply(final QiStationPreviousDefect entity) {
			QiEntryDepartmentDto dto = new QiEntryDepartmentDto();
			dto.setDivisionId(entity.getId().getEntryDivisionId());
			dto.setOriginalDefectStatus(DefectStatus.getType(entity.getOriginalDefectStatus()).getName());
			dto.setCurrentDefectStatus(DefectStatus.getType(entity.getCurrentDefectStatus()).getName());
			return dto;
		}
	};
	private static final Function<Division, QiEntryDepartmentDto> GET_AVAILABLE_DEPT_DEFECT_LIST =
			new Function<Division, QiEntryDepartmentDto>() {
		@Override
		public QiEntryDepartmentDto apply(final Division entity) {
			QiEntryDepartmentDto dto = new QiEntryDepartmentDto();
			dto.setDivisionId(entity.getDivisionId());
			dto.setDivisionName(entity.getDivisionName());
			dto.setDivisionDescription(entity.getDivisionDescription());
			return dto;
		}
	};

	private void addAvailableEntryDepartmentTableListners(){
		getView().getEntryDepartmentDefectPanel().getAvailableEntryDepartmentDefectTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiEntryDepartmentDto>() {
			public void changed(
					ObservableValue<? extends QiEntryDepartmentDto> arg0,QiEntryDepartmentDto arg1,QiEntryDepartmentDto arg2) {
				clearDisplayMessage();
				if (arg2 != null) {
					getView().getEntryDepartmentDefectPanel().getRightShiftEntryDepartment().setDisable(false);
				} else {
					getView().getEntryDepartmentDefectPanel().getRightShiftEntryDepartment().setDisable(true);
				}
			}
		});

	}

	private void addAssignedEntryDepartmentTableListners(){
		getView().getEntryDepartmentDefectPanel().getCurrentlyAssignedEntryDepartmentDefectTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiEntryDepartmentDto>() {
			public void changed(
					ObservableValue<? extends QiEntryDepartmentDto> arg0,QiEntryDepartmentDto arg1,QiEntryDepartmentDto arg2) {
				clearDisplayMessage();
				if (arg2 != null) {
					getView().getEntryDepartmentDefectPanel().getLeftShiftEntryDepartment().setDisable(false);
				} else {
					getView().getEntryDepartmentDefectPanel().getLeftShiftEntryDepartment().setDisable(true);					
				}
			}
		});

	}


}
