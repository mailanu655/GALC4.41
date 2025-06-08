package com.honda.galc.client.teamleader.qi.stationconfig.department;

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
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiEntryDepartmentDto;
import com.honda.galc.dto.qi.QiWriteUpDepartmentDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.entity.qi.QiStationEntryDepartmentId;
import com.honda.galc.entity.qi.QiStationWriteUpDepartment;
import com.honda.galc.entity.qi.QiStationWriteUpDepartmentId;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
/**
 * 
 * <h3>StationDepartmentController Class description</h3>
 * <p>
 * StationDepartmentController is used to load data in TableViews and perform the action on Update , Reset and Shift buttonsetc.
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
public class StationDepartmentController extends EntryStationConfigController {

	private final String setDefault = "Set default";
	private boolean flag;
	public StationDepartmentController(EntryStationConfigModel model,
			EntryStationConfigPanel view) {
		super(model, view);
	}

	/**
	 * This method is used to initialize listeners
	 */
	public void initListeners() {
		if(isFullAccess()){
			addTableListener();
		}
	}

	/**
	 * This method is used to apply listener on TableView
	 */
	private void addTableListener() {
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiEntryDepartmentDto>() {
			public void changed(ObservableValue<? extends QiEntryDepartmentDto> arg0,
					QiEntryDepartmentDto arg1, QiEntryDepartmentDto arg2) {
				flag=true;
				addContextMenuItems();
			}
		});
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				flag=true;
				addContextMenuItems();
			}
		});
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiWriteUpDepartmentDto>() {
			public void changed(ObservableValue<? extends QiWriteUpDepartmentDto> arg0,
					QiWriteUpDepartmentDto arg1, QiWriteUpDepartmentDto arg2) {
				flag=false;
				addContextMenuItems();
			}
		});
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				flag=false;
				addContextMenuItems();
			}
		});
	}

	/**
	 * This method is added because initEventHandlers() is called before the selection listener is triggered.
	 */
	@Override
	public void addContextMenuItems() {
		ObjectTablePane<QiEntryDepartmentDto> entryDepartmentTablePane = getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane();
		ObjectTablePane<QiWriteUpDepartmentDto> writeUpDepartmentTablePane = getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane();
		String[] menuItems;
		if(getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().isFocused()){
			if(null!=entryDepartmentTablePane.getSelectedItem()){
				if(entryDepartmentTablePane.getSelectedItem().getIsDefault()!=(short)1){
					menuItems = new String[] {setDefault};
					getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().createContextMenu(menuItems, this);
				}else{
					menuItems = new String[] {};
					if(null!=entryDepartmentTablePane.getTable().getContextMenu())
						entryDepartmentTablePane.getTable().getContextMenu().getItems().clear();
					entryDepartmentTablePane.createContextMenu(menuItems, this);
				}
			}
		}else if(getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().isFocused()){
			if(null!=writeUpDepartmentTablePane.getSelectedItem()){
				if(writeUpDepartmentTablePane.getSelectedItem().getIsDefault()!=(short)1){
					menuItems = new String[] {setDefault};
					getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().createContextMenu(menuItems, this);
				}else{
					menuItems = new String[] {};
					if(null!=writeUpDepartmentTablePane.getTable().getContextMenu())
						writeUpDepartmentTablePane.getTable().getContextMenu().getItems().clear();
					writeUpDepartmentTablePane.createContextMenu(menuItems, this);
				}
			}
		}
	}

	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			switch(StationConfigurationOperations.getType(loggedButton.getId())){
			case RIGHT_SHIFT_DEPARTMENT :
				assignEntryDeptAction(actionEvent);
				break;

			case LEFT_SHIFT_DEPARTMENT :
				deassignEntryDeptAction(actionEvent);
				break;

			case UPDATE_ENTRY_DEPARTMENT :
				updateEntryDeptAction(actionEvent);
				break; 

			case RESET_ENTRY_DEPARTMENT :
				resetEntryDepartment();
				break; 

			case RIGHT_SHIFT_WRITEUP_DEPARTMENT :
				assignWriteUpDeptAction(actionEvent);
				break;

			case LEFT_SHIFT_WRITEUP_DEPARTMENT :
				deassignWriteUpDeptAction(actionEvent);
				break;

			case UPDATE_WRITEUP_DEPARTMENT :
				updateWriteUpDeptAction(actionEvent);
				break; 

			case RESET_WRITEUP_DEPARTMENT :
				resetWriteUpDepartment();
				break; 
			}
		}
		actionPerformedByComponent(actionEvent);
	}
	/**
	 * This method is used to perform action on MenuItem , RadioButton and LoggedTextField components
	 * @param actionEvent
	 */
	private void actionPerformedByComponent(ActionEvent actionEvent){
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem)actionEvent.getSource();
			if(flag && setDefault.equals(menuItem.getText()))
				setDepartmentDefaultValue(actionEvent);
			else
				setWriteUpDepartmentDefaultValue(actionEvent);
		}
	}
	/**
	 * This method is used to set 'tick image' on default column
	 * @param event
	 */
	private void setDepartmentDefaultValue(ActionEvent event){
		for(QiEntryDepartmentDto dto : getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getItems()){
			if(dto.equals( getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getSelectionModel().getSelectedItem()))
				dto.setIsDefault((short)1);
			else
				dto.setIsDefault((short)0);
		}
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getColumns().remove(0);
		LoggedTableColumn<QiEntryDepartmentDto,Boolean> defaultImageCol = new LoggedTableColumn<QiEntryDepartmentDto,Boolean>();
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getColumns().add(0, defaultImageCol);
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getColumns().get(0).setText("Default");
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getColumns().get(0).setResizable(true);
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getColumns().get(0).setMaxWidth(60);
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getColumns().get(0).setMinWidth(60);
		getView().getEntryDeptAndWriteUpDeptPanel().createDefaultEntryDepartment(defaultImageCol);
		getView().getEntryDeptAndWriteUpDeptPanel().getUpdateEntryDepartmentBtn().setDisable(false);
	}
	/**
	 * This method is used to set 'tick image' on default column
	 * @param event
	 */
	private void setWriteUpDepartmentDefaultValue(ActionEvent actionEvent) {
		for(QiWriteUpDepartmentDto dto : getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getItems()){
			if(dto.equals( getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getSelectionModel().getSelectedItem()))
				dto.setIsDefault((short)1);
			else
				dto.setIsDefault((short)0);
		}
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getColumns().remove(0);
		LoggedTableColumn<QiWriteUpDepartmentDto,Boolean> defaultImageCol = new LoggedTableColumn<QiWriteUpDepartmentDto,Boolean>();
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getColumns().add(0, defaultImageCol);
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getColumns().get(0).setText("Default");
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getColumns().get(0).setResizable(true);
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getColumns().get(0).setMaxWidth(60);
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getColumns().get(0).setMinWidth(60);
		getView().getEntryDeptAndWriteUpDeptPanel().createDefaultWriteupDepartment(defaultImageCol);
		getView().getEntryDeptAndWriteUpDeptPanel().getUpdateWriteUpDepartmentBtn().setDisable(false);
	}
	/**
	 * This method is used to shift available EntryDept to currently assigned EntryDept
	 * @param actionEvent
	 */
	private void assignEntryDeptAction(ActionEvent actionEvent){
		List<QiEntryDepartmentDto> availableEntryDepartmentsList = getView().getEntryDeptAndWriteUpDeptPanel().getAvailableEntryDepartObjectTablePane().getSelectedItems();
		if(!availableEntryDepartmentsList.isEmpty()){
			getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getItems().addAll(availableEntryDepartmentsList);
			getView().getEntryDeptAndWriteUpDeptPanel().getAvailableEntryDepartObjectTablePane().getTable().getItems().removeAll(availableEntryDepartmentsList);
			getView().getEntryDeptAndWriteUpDeptPanel().getUpdateEntryDepartmentBtn().setDisable(false);
			getView().getEntryDeptAndWriteUpDeptPanel().getResetEntryDepartmentBtn().setDisable(false);
		}
	}

	/**
	 * This method is used to shift currently assigned EntryDept to available EntryDept
	 * @param actionEvent
	 */
	private void deassignEntryDeptAction(ActionEvent actionEvent){
		List<QiEntryDepartmentDto> currentlyAssignedDepartmentsList = getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getSelectedItems();
		for (QiEntryDepartmentDto qiEntryDepartmentDto : currentlyAssignedDepartmentsList) {
			qiEntryDepartmentDto.setIsDefault((short)0);
		}
		if(!currentlyAssignedDepartmentsList.isEmpty()){
			getView().getEntryDeptAndWriteUpDeptPanel().getAvailableEntryDepartObjectTablePane().getTable().getItems().addAll(currentlyAssignedDepartmentsList);
			getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getItems().removeAll(currentlyAssignedDepartmentsList);
			getView().getEntryDeptAndWriteUpDeptPanel().getUpdateEntryDepartmentBtn().setDisable(false);
			getView().getEntryDeptAndWriteUpDeptPanel().getResetEntryDepartmentBtn().setDisable(false);
		}
	}
	/**
	 * This method is used to update Entry dept while clicking update button
	 * @param actionEvent
	 */
	private void updateEntryDeptAction(ActionEvent actionEvent){
		if(null!=getView().getEntryStationDefaultStatusPanel().getDepartmentComboBox().getItems())
			getView().getEntryStationDefaultStatusPanel().clearDepartmentComboBox();
		List<QiEntryDepartmentDto> currentlyAssignedEntryDeptList = getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getItems();
		for (QiEntryDepartmentDto division : currentlyAssignedEntryDeptList) {
			getView().getEntryStationDefaultStatusPanel().getDepartmentComboBox().getItems().add(division);
		}
		String qicsStation =(String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId();
		List<QiStationEntryDepartment> qiStationEntryDepartmentDbList = getModel().findAllEntryDeptInfoByProcessPoint(qicsStation);
		QiEntryDepartmentDto qiEntryDepartmentDto;
		List<QiEntryDepartmentDto> qiStationEntryDepartmentList=new ArrayList<QiEntryDepartmentDto>();
		for (QiStationEntryDepartment qiStationEntryDepartment : qiStationEntryDepartmentDbList) {
			qiEntryDepartmentDto= new QiEntryDepartmentDto();
			qiEntryDepartmentDto.setDivisionId(qiStationEntryDepartment.getId().getDivisionId());
			qiEntryDepartmentDto.setIsDefault(qiStationEntryDepartment.getIsDefault());
			qiStationEntryDepartmentList.add(qiEntryDepartmentDto);
		}
		removeEntryDepartment(qiStationEntryDepartmentList,currentlyAssignedEntryDeptList);
		addEntryDepartment(qiStationEntryDepartmentList,currentlyAssignedEntryDeptList);
		getView().getEntryDeptAndWriteUpDeptPanel().getUpdateEntryDepartmentBtn().setDisable(true);
		getView().getEntryDeptAndWriteUpDeptPanel().getResetEntryDepartmentBtn().setDisable(true);
	}

	/**
	 * This method is used to add EntryDept when not available in currently assigned EntryDept
	 * @param qiStationEntryDepartmentList
	 * @param currentlyAssignedEntryDeptList
	 */
	private void addEntryDepartment(List<QiEntryDepartmentDto> qiStationEntryDepartmentList,List<QiEntryDepartmentDto> currentlyAssignedEntryDeptList){
		QiStationEntryDepartment qiStationEntryDepartment,qiStationPreviousDefaultEntryDepartment=null;
		QiStationEntryDepartmentId  qiStationEntryDepartmentId;
		List<QiStationEntryDepartment> addedStationEntryDepartmentList = new ArrayList<QiStationEntryDepartment>();
		String qicsStation = StringUtils.trimToEmpty((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
		boolean oneEntryDept=false;
		for (QiEntryDepartmentDto departmentDto : currentlyAssignedEntryDeptList) {
			if(!qiStationEntryDepartmentList.contains(departmentDto)){
				if(departmentDto.getIsDefault()==(short)1){
					qiStationPreviousDefaultEntryDepartment	= getModel().findDefaultEntryDepartment(qicsStation);
				}
			}else if(departmentDto.getIsDefault()==(short)1 ){
				QiStationEntryDepartment entryDepartment = getModel().findDefaultEntryDepartment(qicsStation);
				if(entryDepartment!=null && entryDepartment.getId().getDivisionId() != null && !departmentDto.getDivisionId().equalsIgnoreCase(entryDepartment.getId().getDivisionId()))
				qiStationPreviousDefaultEntryDepartment = (QiStationEntryDepartment)entryDepartment.deepCopy();
			}
			qiStationEntryDepartment = new QiStationEntryDepartment();
			qiStationEntryDepartmentId = new QiStationEntryDepartmentId();
			qiStationEntryDepartmentId.setDivisionId(departmentDto.getDivisionId());
			qiStationEntryDepartmentId.setProcessPointId((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
			qiStationEntryDepartment.setId(qiStationEntryDepartmentId);
			qiStationEntryDepartment.setIsDefault(departmentDto.getIsDefault());
			addedStationEntryDepartmentList.add(qiStationEntryDepartment);
		}
		if(null!= qiStationPreviousDefaultEntryDepartment){
			qiStationPreviousDefaultEntryDepartment.setIsDefault((short)0);
			addedStationEntryDepartmentList.add(qiStationPreviousDefaultEntryDepartment);
		}
		if(addedStationEntryDepartmentList.size()==1){
			addedStationEntryDepartmentList.get(0).setIsDefault((short)1);
			oneEntryDept=true;
		}
		if(!addedStationEntryDepartmentList.isEmpty()){
			getModel().createStationEntryDepartments(addedStationEntryDepartmentList);
		}
		if(oneEntryDept){
			resetEntryDepartment();	
		}

	}

	/**
	 * This method is used to remove EntryDept 
	 * @param qiStationEntryDepartmentList
	 * @param currentlyAssignedEntryDeptList
	 */
	private void removeEntryDepartment(List<QiEntryDepartmentDto> qiStationEntryDepartmentList,List<QiEntryDepartmentDto> currentlyAssignedEntryDeptList){
		List<QiStationEntryDepartment> removedStationEntryDepartmentList = new ArrayList<QiStationEntryDepartment>();
		QiStationEntryDepartment qiStationEntryDepartment;
		QiStationEntryDepartmentId  qiStationEntryDepartmentId;
		for (QiEntryDepartmentDto departmentDto: qiStationEntryDepartmentList) {
			if(!currentlyAssignedEntryDeptList.contains(departmentDto)){
				qiStationEntryDepartment = new QiStationEntryDepartment();
				qiStationEntryDepartmentId = new QiStationEntryDepartmentId();
				qiStationEntryDepartmentId.setDivisionId(departmentDto.getDivisionId());
				qiStationEntryDepartmentId.setProcessPointId((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
				qiStationEntryDepartment.setId(qiStationEntryDepartmentId);
				qiStationEntryDepartment.setIsDefault(departmentDto.getIsDefault());
				removedStationEntryDepartmentList.add(qiStationEntryDepartment);
			}
		}
		if(!removedStationEntryDepartmentList.isEmpty()){
			getModel().removedStationEntryDepartments(removedStationEntryDepartmentList);
			for (QiStationEntryDepartment qiStationEntryDepartment2 : removedStationEntryDepartmentList) {
				AuditLoggerUtil.logAuditInfo(qiStationEntryDepartment2, null, QiConstant.UPDATE_REASON_FOR_AUDIT, getView().getScreenName(),getUserId());
			}
		}
	}
	/**
	 * This method is used to reset EntryDept tableviews
	 */
	private void resetEntryDepartment(){
		if(null!=getView().getEntryStationDefaultStatusPanel().getDepartmentComboBox().getItems())
			getView().getEntryStationDefaultStatusPanel().clearDepartmentComboBox();
		populateAvailableAndAssignedEntryDept((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
		getView().getEntryDeptAndWriteUpDeptPanel().getResetEntryDepartmentBtn().setDisable(true);
		getView().getEntryDeptAndWriteUpDeptPanel().getUpdateEntryDepartmentBtn().setDisable(true);
	}

	/**
	 * This method is used to shift available WriteUp dept to currently assigned WriteUp dept
	 * @param actionEvent
	 */
	private void assignWriteUpDeptAction(ActionEvent actionEvent){
		WriteUpDeptDialog dialog =new WriteUpDeptDialog("WriteUp Departments",isFullAccess(), getModel(),getView().getEntryDeptAndWriteUpDeptPanel().getAvailableWriteUpDepartObjectTablePane().getTable().getSelectionModel().getSelectedItems(),getApplicationId());
		dialog.showDialog();
		if(dialog.isFlag()){
			for(QiWriteUpDepartmentDto dto : getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getItems())
				dto.setIsDefault((short)0);
		}
		getView().getEntryDeptAndWriteUpDeptPanel().getAvailableWriteUpDepartObjectTablePane().getTable().getItems().removeAll(dialog.getAssignedWriteUpDepartments());
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getItems().addAll(dialog.getAssignedWriteUpDepartments());
		getView().getEntryDeptAndWriteUpDeptPanel().getUpdateWriteUpDepartmentBtn().setDisable(false);
		getView().getEntryDeptAndWriteUpDeptPanel().getResetWriteUpDepartmentBtn().setDisable(false);
	}
	/**
	 *  This method is used to shift currently assigned WriteUp dept to available WriteUp dept 
	 * @param actionEvent
	 */
	private void deassignWriteUpDeptAction(ActionEvent actionEvent){
		List<QiWriteUpDepartmentDto> currentlyAssignedWriteUpDepartmentsList = getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getSelectedItems();
		for (QiWriteUpDepartmentDto qiWriteUpDepartmentDto : currentlyAssignedWriteUpDepartmentsList) {
			qiWriteUpDepartmentDto.setIsDefault((short)0);
		}
		if(!currentlyAssignedWriteUpDepartmentsList.isEmpty()){
			getView().getEntryDeptAndWriteUpDeptPanel().getAvailableWriteUpDepartObjectTablePane().getTable().getItems().addAll(currentlyAssignedWriteUpDepartmentsList);
			getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getItems().removeAll(currentlyAssignedWriteUpDepartmentsList);
			getView().getEntryDeptAndWriteUpDeptPanel().getUpdateWriteUpDepartmentBtn().setDisable(false);
			getView().getEntryDeptAndWriteUpDeptPanel().getResetWriteUpDepartmentBtn().setDisable(false);
		}
	}
	/**
	 * This method is used to update WriteUp dept while clicking update button
	 * @param actionEvent
	 */
	private void updateWriteUpDeptAction(ActionEvent actionEvent){
		String qicsStation =(String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId();
		List<QiWriteUpDepartmentDto> currentlyAssignedWriteUpDeptList = getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getItems();
		List<QiStationWriteUpDepartment> qiStationWriteUpDeptDbList = getModel().findAllWriteUpDepartmentByQicsStation(qicsStation,getView().getEntryStationPanel().getSiteValueLabel().getText(),(String)getView().getEntryStationPanel().getPlantComboBox().getValue());
		List<QiStationWriteUpDepartment> removedStationWriteUpDeptList = new ArrayList<QiStationWriteUpDepartment>();
		List<QiWriteUpDepartmentDto> qiStationWriteUpDeptList=new ArrayList<QiWriteUpDepartmentDto>();
		for (QiStationWriteUpDepartment qiStationWriteUpDepartment2 : qiStationWriteUpDeptDbList) {
			QiWriteUpDepartmentDto qiWriteUpDepartmentDto = new QiWriteUpDepartmentDto(); 
			qiWriteUpDepartmentDto.setDivisionId(qiStationWriteUpDepartment2.getId().getDivisionId());
			qiWriteUpDepartmentDto.setIsDefault(qiStationWriteUpDepartment2.getIsDefault());
			qiWriteUpDepartmentDto.setColorCode(qiStationWriteUpDepartment2.getId().getColorCode());
			qiStationWriteUpDeptList.add(qiWriteUpDepartmentDto);
			if(!currentlyAssignedWriteUpDeptList.contains(qiWriteUpDepartmentDto)){
				removedStationWriteUpDeptList.add(createStationWriteUpDepartment(qiWriteUpDepartmentDto));
			} else {
				if(isColorCodeChanged(currentlyAssignedWriteUpDeptList, qiWriteUpDepartmentDto)) {
					removedStationWriteUpDeptList.add(createStationWriteUpDepartment(qiWriteUpDepartmentDto));
				}
			}
		}
		removeWriteUpDept(removedStationWriteUpDeptList);
		createWriteUpDept(qiStationWriteUpDeptList,currentlyAssignedWriteUpDeptList);
		getView().getEntryDeptAndWriteUpDeptPanel().getUpdateWriteUpDepartmentBtn().setDisable(true);
		getView().getEntryDeptAndWriteUpDeptPanel().getResetWriteUpDepartmentBtn().setDisable(true);
	}
	
	private QiStationWriteUpDepartment createStationWriteUpDepartment(QiWriteUpDepartmentDto qiWriteUpDepartmentDto) {
		QiStationWriteUpDepartment qiStationWriteUpDepartment = new QiStationWriteUpDepartment();
		QiStationWriteUpDepartmentId qiStationWriteUpDeptId = new QiStationWriteUpDepartmentId();
		qiStationWriteUpDeptId.setDivisionId(qiWriteUpDepartmentDto.getDivisionId());
		qiStationWriteUpDeptId.setProcessPointId((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
		qiStationWriteUpDeptId.setColorCode(qiWriteUpDepartmentDto.getColorCode());
		qiStationWriteUpDeptId.setSite(getView().getEntryStationPanel().getSiteValueLabel().getText());
		qiStationWriteUpDeptId.setPlant((String)getView().getEntryStationPanel().getPlantComboBox().getValue());
		qiStationWriteUpDepartment.setId(qiStationWriteUpDeptId);
		qiStationWriteUpDepartment.setIsDefault(qiWriteUpDepartmentDto.getIsDefault());
		return qiStationWriteUpDepartment;
	}
	
	private boolean isColorCodeChanged(List<QiWriteUpDepartmentDto> currentlyAssignedWriteUpDeptList, QiWriteUpDepartmentDto qiWriteUpDepartmentDto) {
		for(QiWriteUpDepartmentDto dto : currentlyAssignedWriteUpDeptList) {
			if(!dto.getColorCode().equalsIgnoreCase(qiWriteUpDepartmentDto.getColorCode())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method is used to remove assigned Writeup dept
	 */
	private void removeWriteUpDept(List<QiStationWriteUpDepartment> removedStationWriteUpDeptList){
		if(!removedStationWriteUpDeptList.isEmpty()){
			getModel().removedStationWriteUpDept(removedStationWriteUpDeptList);
			for (QiStationWriteUpDepartment qiStationWriteUpDepartment2 : removedStationWriteUpDeptList) {
				AuditLoggerUtil.logAuditInfo(qiStationWriteUpDepartment2, null, QiConstant.UPDATE_REASON_FOR_AUDIT, getView().getScreenName(),getUserId());
			}
		}
	}
	/**
	 * This method is used to create Writeup Dept when not available in currentlyAssignedWriteUpDept
	 * @param qiStationWriteUpDeptList
	 * @param currentlyAssignedWriteUpDeptList
	 */
	private void  createWriteUpDept(List<QiWriteUpDepartmentDto> qiStationWriteUpDeptList,List<QiWriteUpDepartmentDto> currentlyAssignedWriteUpDeptList){
		QiStationWriteUpDepartment prevDefaultWriteUpDept=null; 
		List<QiStationWriteUpDepartment> addedStationWriteUpDeptList = new ArrayList<QiStationWriteUpDepartment>();
		QiStationWriteUpDepartment qiStationWriteUpDepartment;
		QiStationWriteUpDepartmentId  qiStationWriteUpDeptId;
		boolean oneWriteDept=false;
		String qicsStation = StringUtils.trimToEmpty((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
		for (QiWriteUpDepartmentDto qiWriteUpDepartmentDto: currentlyAssignedWriteUpDeptList) {
			if(!qiStationWriteUpDeptList.contains(qiWriteUpDepartmentDto)){
				if(qiWriteUpDepartmentDto.getIsDefault()==(short)1){
					prevDefaultWriteUpDept = getModel().findDefaultWriteUpDepartmentByProcessPointId(qicsStation);
				}
			}else if(qiWriteUpDepartmentDto.getIsDefault()==(short)1 ){
				QiStationWriteUpDepartment stationWriteUpDepertment = getModel().findDefaultWriteUpDepartmentByProcessPointId(qicsStation);
				if(stationWriteUpDepertment != null && stationWriteUpDepertment.getId() != null 
						&& !qiWriteUpDepartmentDto.getDivisionId().equalsIgnoreCase(stationWriteUpDepertment.getId().getDivisionId())) {
					prevDefaultWriteUpDept = getModel().findDefaultWriteUpDepartmentByProcessPointId(StringUtils.trimToEmpty((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId()));
					prevDefaultWriteUpDept = (QiStationWriteUpDepartment) stationWriteUpDepertment.deepCopy();
				}
			}
			qiStationWriteUpDepartment = new QiStationWriteUpDepartment();
			qiStationWriteUpDeptId = new QiStationWriteUpDepartmentId();
			qiStationWriteUpDeptId.setDivisionId(qiWriteUpDepartmentDto.getDivisionId());
			qiStationWriteUpDeptId.setProcessPointId((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
			qiStationWriteUpDeptId.setColorCode(qiWriteUpDepartmentDto.getColorCode()!=null?qiWriteUpDepartmentDto.getColorCode():StringUtils.EMPTY);
			qiStationWriteUpDeptId.setSite(getView().getEntryStationPanel().getSiteValueLabel().getText());
			qiStationWriteUpDeptId.setPlant((String)getView().getEntryStationPanel().getPlantComboBox().getValue());
			qiStationWriteUpDepartment.setId(qiStationWriteUpDeptId);
			qiStationWriteUpDepartment.setIsDefault(qiWriteUpDepartmentDto.getIsDefault());
			addedStationWriteUpDeptList.add(qiStationWriteUpDepartment);
		}
		if(null!= prevDefaultWriteUpDept){
			prevDefaultWriteUpDept.setIsDefault((short)0);
			addedStationWriteUpDeptList.add(prevDefaultWriteUpDept);
		}
		if(addedStationWriteUpDeptList.size()==1){
			addedStationWriteUpDeptList.get(0).setIsDefault((short)1);
			oneWriteDept=true;
		}
		if(!addedStationWriteUpDeptList.isEmpty()){
			getModel().createStationWriteUpDept(addedStationWriteUpDeptList);
		}
		if(oneWriteDept){
			resetWriteUpDepartment();
		}

	}
	/**
	 * This method is used to reset Writeup Dept tableviews
	 */
	private void resetWriteUpDepartment(){
		populateAvailableAndAssignedWriteUpDept((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
		getView().getEntryDeptAndWriteUpDeptPanel().getUpdateWriteUpDepartmentBtn().setDisable(true);
		getView().getEntryDeptAndWriteUpDeptPanel().getResetWriteUpDepartmentBtn().setDisable(true);
	}

	public void loadInitialData() {
		String processPoint= StringUtils.trimToEmpty(getView().getEntryStationPanel().getQicsStationComboBoxSelectedId().toString());
		populateAvailableAndAssignedEntryDept(processPoint);
		populateAvailableAndAssignedWriteUpDept(processPoint);	
	}
	private static final Function<QiStationEntryDepartment, QiEntryDepartmentDto> GET_ASSINGED_DEPT_LIST =
			new Function<QiStationEntryDepartment, QiEntryDepartmentDto>() {
		@Override
		public QiEntryDepartmentDto apply(final QiStationEntryDepartment entity) {
			QiEntryDepartmentDto dto = new QiEntryDepartmentDto();
			dto.setDivisionId(entity.getId().getDivisionId());
			dto.setIsDefault(entity.getIsDefault());
			return dto;
		}
	};

	private static final Function<Division, QiEntryDepartmentDto> GET_AVAILABLE_DEPT_LIST =
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
	/**
	 * This method is used to populate available and assigned entryDept tableview
	 * @param processPoint
	 */
	private void populateAvailableAndAssignedEntryDept(String processPoint){
		List<QiStationEntryDepartment> qiStationEntryDepartmentList = getModel().findAllEntryDeptInfoByProcessPoint(processPoint);
		List<QiEntryDepartmentDto> assignedEntryDepartmentList = new ArrayList<QiEntryDepartmentDto>(QiCommonUtil.getUniqueArrayList(Lists.transform(qiStationEntryDepartmentList, GET_ASSINGED_DEPT_LIST)));
		for(QiEntryDepartmentDto dto : assignedEntryDepartmentList)  {
			Division d = getModel().findDivisionByDivisionId(dto.getDivisionId());
			dto.setDivisionName(d.getDivisionName());
			dto.setDivisionDescription(d.getDivisionDescription());
		}
		List<Division>  divisionList = getModel().findAllDivisionBySiteAndPlant(getView().getEntryStationPanel().getSiteValueLabel().getText(),(String)getView().getEntryStationPanel().getPlantComboBox().getValue());
		List<QiEntryDepartmentDto> entryDepartmentDtoList = new ArrayList<QiEntryDepartmentDto>(QiCommonUtil.getUniqueArrayList(Lists.transform(divisionList, GET_AVAILABLE_DEPT_LIST)));
		populateAvailableAndAssignedEntryDept(entryDepartmentDtoList,assignedEntryDepartmentList);
	}
	/**
	 * This method is used to populate available and assigned entryDept tableview
	 * @param entryDepartmentDtoList
	 * @param assignedEntryDepartmentList
	 */
	private void populateAvailableAndAssignedEntryDept(List<QiEntryDepartmentDto> entryDepartmentDtoList,List<QiEntryDepartmentDto> assignedEntryDepartmentList){
		List<QiEntryDepartmentDto> subtractedAllEntryDeptList = ListUtils.subtract(entryDepartmentDtoList,assignedEntryDepartmentList);
		Collections.sort(subtractedAllEntryDeptList,new QiEntryDepartmentDto());
		getView().getEntryDeptAndWriteUpDeptPanel().getAvailableEntryDepartObjectTablePane().setData(subtractedAllEntryDeptList);
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().setData(assignedEntryDepartmentList);
		for (QiEntryDepartmentDto qiEntryDepartmentDto2 : assignedEntryDepartmentList) {
			getView().getEntryStationDefaultStatusPanel().getDepartmentComboBox().getItems().add(qiEntryDepartmentDto2);
		}
	}
	private static final Function<QiStationWriteUpDepartment, QiWriteUpDepartmentDto> GET_ASSINGED_WRITE_DEPT_LIST =
			new Function<QiStationWriteUpDepartment, QiWriteUpDepartmentDto>() {
		@Override
		public QiWriteUpDepartmentDto apply(final QiStationWriteUpDepartment entity) {
			QiWriteUpDepartmentDto dto = new QiWriteUpDepartmentDto();
			dto.setDivisionId(entity.getId().getDivisionId());
			dto.setIsDefault(entity.getIsDefault());
			dto.setColorCode(entity.getId().getColorCode());
			return dto;
		}
	};
	/**
	 * This method is used to populate available and assigned WriteupDept tableview
	 * @param processPoint
	 */
	private void populateAvailableAndAssignedWriteUpDept(String processPoint){
		String site = getView().getEntryStationPanel().getSiteValueLabel().getText();
		List<QiStationWriteUpDepartment> qiWriteUpDepartments=getModel().findAllWriteUpDepartmentByQicsStation(processPoint,getView().getEntryStationPanel().getSiteValueLabel().getText(),(String)getView().getEntryStationPanel().getPlantComboBox().getValue());
		List<QiWriteUpDepartmentDto> assignedWriteUpDepartmentList = new ArrayList<QiWriteUpDepartmentDto>(QiCommonUtil.getUniqueArrayList(Lists.transform(qiWriteUpDepartments, GET_ASSINGED_WRITE_DEPT_LIST)));
		for(QiWriteUpDepartmentDto dto : assignedWriteUpDepartmentList)  {
			QiDepartment qiDept = getModel().findFirstQiDepartmentBySiteAndDepartment(site, dto.getDivisionId());
			dto.setDivisionName(qiDept.getDepartmentName());
			dto.setDivisionDescription(dto.getDivisionDescription());
		}
		populateAvailableAndAssignedWriteUpDept(assignedWriteUpDepartmentList);
	}

	private static final Function<String, QiWriteUpDepartmentDto> GET_AVAILABLE_WRITEUP_DEPT_LIST =
			new Function<String, QiWriteUpDepartmentDto>() {
		@Override
		public QiWriteUpDepartmentDto apply(final String entity) {
			QiWriteUpDepartmentDto dto = new QiWriteUpDepartmentDto();
			dto.setDivisionId(StringUtils.trimToEmpty(entity));
			return dto;
		}
	};
	/**
	 * This method is used to populate available and assigned WriteupDept tableview
	 * @param assignedWriteUpDepartmentList
	 */
	private void populateAvailableAndAssignedWriteUpDept(List<QiWriteUpDepartmentDto> assignedWriteUpDepartmentList){
		String site = getView().getEntryStationPanel().getSiteValueLabel().getText();
		List<String>  divisionList = getModel().findAllDeptBySite(getView().getEntryStationPanel().getSiteValueLabel().getText());
		List<QiWriteUpDepartmentDto> writeUpDeptDBList = new ArrayList<QiWriteUpDepartmentDto>(Lists.transform(divisionList, GET_AVAILABLE_WRITEUP_DEPT_LIST));
		List<QiWriteUpDepartmentDto> subtractedAllWriteUpDeptList = ListUtils.subtract(writeUpDeptDBList,assignedWriteUpDepartmentList);
		for(QiWriteUpDepartmentDto dto : subtractedAllWriteUpDeptList)  {
			QiDepartment qiDept = getModel().findFirstQiDepartmentBySiteAndDepartment(site, dto.getDivisionId());
			dto.setDivisionName(qiDept.getDepartmentName());
			dto.setDivisionDescription(dto.getDivisionDescription());
		}
		getView().getEntryDeptAndWriteUpDeptPanel().getAvailableWriteUpDepartObjectTablePane().setData(subtractedAllWriteUpDeptList);
		getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().setData(assignedWriteUpDepartmentList);
	}

}
