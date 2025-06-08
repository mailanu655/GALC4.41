package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.ParkingLocationMaintPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.teamleader.qi.view.RepairAreaDialog;
import com.honda.galc.client.teamleader.qi.view.RepairAreaRowDataDialog;
import com.honda.galc.client.teamleader.qi.view.RepairAreaSpaceDialog;
import com.honda.galc.client.teamleader.qi.view.RepeatRowDialog;
import com.honda.galc.client.teamleader.qi.view.RepeatSpaceDialog;
import com.honda.galc.client.teamleader.qi.view.SpaceAssignmentsDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaSpace;
import com.honda.galc.util.AuditLoggerUtil;
/**
 * 
 * <h3>ParkingLocationMaintController Class description</h3>
 * <p>
 * ParkingLocationMaintController is used to to add all the main functionality.
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
 * </Table>
 * @author LnTInfotech<br>
 * 
 */

public class ParkingLocationMaintController extends AbstractQiController<ParkingLocationMaintenanceModel, ParkingLocationMaintPanel> implements EventHandler<ActionEvent>{

	private List<QiRepairAreaRow> repairAreaRowList;
	private List<QiRepairAreaSpace> repairAreaSpaceList;
	
	private QiRepairArea selectedRepairArea;
	private QiRepairAreaRow selectedRepairAreaRow;
	private List<QiRepairAreaSpace> selectedRepairAreaSpaceList;
	
	public ParkingLocationMaintController(ParkingLocationMaintenanceModel model, ParkingLocationMaintPanel view) {
		super(model, view);
	}

	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if (actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			String menuItemText = menuItem.getText();
			if (QiConstant.CREATE.equals(menuItemText))
				createRepairArea(actionEvent);
			else if (QiConstant.UPDATE.equals(menuItemText))
				updateRepairArea(actionEvent);
			else if (QiConstant.DELETE.equals(menuItemText))
				deleteRepairArea(actionEvent);
			else if (QiConstant.CREATE_ROW.equals(menuItemText))
				createRepairAreaRow(actionEvent);
			else if (QiConstant.UPDATE_ROW.equals(menuItemText))
				updateRepairAreaRow(actionEvent);
			else if (QiConstant.DELETE_ROW.equals(menuItemText))
				deleteRepairAreaRow(actionEvent);
			else if (QiConstant.REPEAT_ROW.equals(menuItemText))
				repeatRowDialog(actionEvent);
			else if (QiConstant.SPACE_ASSIGNMENT.equals(menuItemText))
				createSpaceAssignment(actionEvent);
			else if (QiConstant.CREATE_SPACE.equals(menuItemText))
				createRepairSpace(actionEvent);
			else if (QiConstant.DELETE_SPACE.equals(menuItemText))
				deleteRepairAreaSpace(actionEvent);
			else if (QiConstant.REPEAT_SPACE.equals(menuItemText))
				repeatSpaceDialog(actionEvent);
			else if (QiConstant.INACTIVATE.equals(menuItemText))
				updateRepairAreaSpaceStatus(actionEvent, false);
			else if (QiConstant.REACTIVATE.equals(menuItemText))
				updateRepairAreaSpaceStatus(actionEvent, true);
				
			if (QiConstant.CREATE_ROW.equals(menuItemText) || QiConstant.UPDATE_ROW.equals(menuItemText) || 
					QiConstant.REPEAT_ROW.equals(menuItemText) || QiConstant.DELETE_ROW.equals(menuItemText)) {
				if (selectedRepairArea != null) {
					fetchAssociatedRepairAreaRow(selectedRepairArea.getRepairAreaName());
				}
			} else if (QiConstant.CREATE_SPACE.equals(menuItemText) || 
					QiConstant.DELETE_SPACE.equals(menuItemText) || QiConstant.REPEAT_SPACE.equals(menuItemText) || 
					QiConstant.INACTIVATE.equals(menuItemText) || QiConstant.REACTIVATE.equals(menuItemText)) {
				if (selectedRepairAreaRow != null) {
					fetchAssociatedRepairAreaSpace(selectedRepairArea.getRepairAreaName(), selectedRepairAreaRow.getId().getRepairAreaRow());
				} 
			}
		}
	}

	@Override
	public void initEventHandlers() {
		addPlantComboboxListener();
		addRepairAreaTableListener();
		addRowDataTableListener();
		addSpaceDataTableListener();
		
		if(getView().getPlantCombobox().getItems().size() == 1)	{
			getView().getPlantCombobox().getSelectionModel().select(0);
		}
		
		if(getView().getRepairAreaTablePane().getTable().getItems().size() == 1)	{
			getView().getRepairAreaTablePane().getTable().getSelectionModel().select(0);
		}
		
		if(getView().getRepairRowDataTablePane().getTable().getItems().size() == 1)	{
			getView().getRepairRowDataTablePane().getTable().getSelectionModel().select(0);
		}
		
		if(getView().getSpaceDataTablePane().getTable().getItems().size() == 1)	{
			getView().getSpaceDataTablePane().getTable().getSelectionModel().select(0);
		}
	}

	@SuppressWarnings("unchecked")
	private void addPlantComboboxListener() {
		getView().getPlantCombobox().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, 
					String oldValue, String newValue) {
				
				clearDisplayMessage();

				getView().getRepairAreaTablePane().getTable().getItems().clear();
				getView().getRepairRowDataTablePane().getTable().getItems().clear();
				getView().getSpaceDataTablePane().getTable().getItems().clear();
				
				if (getView().getRepairAreaTablePane().getTable().getContextMenu() != null) {
					getView().getRepairAreaTablePane().getTable().getContextMenu().getItems().clear();
				}
				
				if (getView().getRepairRowDataTablePane().getTable().getContextMenu() != null) {
					getView().getRepairRowDataTablePane().getTable().getContextMenu().getItems().clear();
				}
				
				if (getView().getSpaceDataTablePane().getTable().getContextMenu() != null) {
					getView().getSpaceDataTablePane().getTable().getContextMenu().getItems().clear();
				}
				
				getView().reload();

				if (isFullAccess()) {
					addContextMenuItems();
				}
			}
		});		
	}

	private void addRepairAreaTableListener() {	
		getView().getRepairAreaTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiRepairArea>() {
			public void changed(
					ObservableValue<? extends QiRepairArea> arg0,
					QiRepairArea arg1,
					QiRepairArea arg2) {
				
				clearDisplayMessage();
				
				if (getView().getRepairAreaTablePane().getTable().getContextMenu() != null) {
					getView().getRepairAreaTablePane().getTable().getContextMenu().getItems().clear();
				}
					
				if (getView().getRepairRowDataTablePane().getTable().getContextMenu() != null) {
					getView().getRepairRowDataTablePane().getTable().getContextMenu().getItems().clear();
				}
					
				if (getView().getSpaceDataTablePane().getTable().getContextMenu() != null) {
					getView().getSpaceDataTablePane().getTable().getContextMenu().getItems().clear();
				}

				getView().getRepairRowDataTablePane().getTable().getItems().clear();
				getView().getSpaceDataTablePane().getTable().getItems().clear();
				
				selectedRepairArea = getView().getRepairAreaTablePane().getSelectedItem();
				
				if (selectedRepairArea != null) {
					fetchAssociatedRepairAreaRow(selectedRepairArea.getRepairAreaName());
				}
				
				if (isFullAccess()) {
					addContextMenuItems();
					if (selectedRepairArea != null) {
						addContextMenuItemsForRowDataTable();
					}
				}
			}
		});
	}

	private void addRowDataTableListener() {
		getView().getRepairRowDataTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiRepairAreaRow>() {
			public void changed(
					ObservableValue<? extends QiRepairAreaRow> arg0,
					QiRepairAreaRow arg1, QiRepairAreaRow arg2) {
				
				clearDisplayMessage();
				
				if (getView().getRepairRowDataTablePane().getTable().getContextMenu() != null) {
					getView().getRepairRowDataTablePane().getTable().getContextMenu().getItems().clear();
				}
				
				if (getView().getSpaceDataTablePane().getTable().getContextMenu() != null) {
					getView().getSpaceDataTablePane().getTable().getContextMenu().getItems().clear();
				}
				
				getView().getSpaceDataTablePane().getTable().getItems().clear();
				
				selectedRepairAreaRow = getView().getRepairRowDataTablePane().getSelectedItem();
				
				if (selectedRepairAreaRow != null) {
					fetchAssociatedRepairAreaSpace(selectedRepairAreaRow.getId().getRepairAreaName(),selectedRepairAreaRow.getId().getRepairAreaRow());
				}
				
				if (isFullAccess()) {
					if (selectedRepairArea != null) {
						addContextMenuItemsForRowDataTable();
						if (selectedRepairAreaRow != null) {
							addContextMenuItemsForRepairAreaSpaceTable();
						}
					}
				}
			}
		});
	}



	private void addSpaceDataTableListener() {
		getView().getSpaceDataTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiRepairAreaSpace>() {
			public void changed(
					ObservableValue<? extends QiRepairAreaSpace> arg0,
					QiRepairAreaSpace arg1, QiRepairAreaSpace arg2) {
				
				clearDisplayMessage();
				
				getView().getSpaceDataTablePane().getTable().getContextMenu().getItems().clear();

				if(isFullAccess() && selectedRepairAreaRow != null) {
					addContextMenuItemsForRepairAreaSpaceTable();
				}						
			}
		});
	}

	private void addContextMenuItemsForRowDataTable() {
		List<String> menuItemsList = new ArrayList<String>();
		selectedRepairAreaRow = getView().getRepairRowDataTablePane().getSelectedItem();
		repairAreaRowList = getView().getRepairRowDataTablePane().getTable().getItems();
		menuItemsList.add(QiConstant.CREATE_ROW);
		menuItemsList.add(QiConstant.REPEAT_ROW);
		
		if(selectedRepairAreaRow !=null && selectedRepairArea != null){
			menuItemsList.add(QiConstant.UPDATE_ROW);
			menuItemsList.add(QiConstant.DELETE_ROW);
			menuItemsList.add(QiConstant.SPACE_ASSIGNMENT);
		}
		getView().getRepairRowDataTablePane().createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
	}

	private void addContextMenuItemsForRepairAreaSpaceTable() {
		List<String> menuItemsList = new ArrayList<String>();
		selectedRepairAreaSpaceList = getView().getSpaceDataTablePane().getSelectedItems();
		repairAreaSpaceList = getView().getSpaceDataTablePane().getTable().getItems();
		menuItemsList.add(QiConstant.CREATE_SPACE);
		menuItemsList.add(QiConstant.REPEAT_SPACE);	
		
		if (selectedRepairArea != null && selectedRepairAreaRow != null && 
				selectedRepairAreaSpaceList.size() > 0) {

			menuItemsList.add(QiConstant.DELETE_SPACE);	

			boolean isAllActive = true;
			boolean isAllInactive = true;
			for (QiRepairAreaSpace selectedRepairAreaSpace : selectedRepairAreaSpaceList) {
				if (selectedRepairAreaSpace.isActive()) {
					isAllInactive = false;
				} else {
					isAllActive = false;
				}
			}
			if (isAllActive) {
				menuItemsList.add(QiConstant.INACTIVATE);
			} else if (isAllInactive) {
				menuItemsList.add(QiConstant.REACTIVATE);
			}
		}
		getView().getSpaceDataTablePane().createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
	}

	private void createRepairArea(ActionEvent event){
		RepairAreaDialog dialog = new RepairAreaDialog(QiConstant.CREATE, new QiRepairArea(), getModel(),getApplicationId(),getView().getPlantCombobox().getSelectionModel().getSelectedItem().toString());
		dialog.showDialog();
		getView().reload();
	}

	private void updateRepairArea(ActionEvent event){
		RepairAreaDialog dialog = new RepairAreaDialog(QiConstant.UPDATE, selectedRepairArea, getModel(),getApplicationId(),getView().getPlantCombobox().getSelectionModel().getSelectedItem().toString());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		getView().reload();
	}
	
	private void deleteRepairArea(ActionEvent event){
		
		List<QiRepairAreaRow> list = getModel().findAllByRepairAreaName(selectedRepairArea.getRepairAreaName());
		
		if (!list.isEmpty()) {
			MessageDialog.showError(getView().getStage(),"Cannot delete Repair Area as it is assigned to "+list.size()+" Repair Area Rows");
		} else {
			boolean deletedRepairArea  = MessageDialog.confirm(getView().getStage(), "Do you want to delete Repair Area?");
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if (!deletedRepairArea) {
				return;
			} else {
				if (dialog.showReasonForChangeDialog(null)) {
					getModel().deleteRepairArea(selectedRepairArea);
					AuditLoggerUtil.logAuditInfo(selectedRepairArea, null, dialog.getReasonForChangeText(), getView().getScreenName(), getUserId());
				}
			}
		}
		
		getView().reload();
	}

	private void createRepairAreaRow(ActionEvent event){
		RepairAreaRowDataDialog dialog = new RepairAreaRowDataDialog(QiConstant.CREATE_ROW, new QiRepairAreaRow(), getModel(),getApplicationId(),getView().getPlantCombobox().getSelectionModel().getSelectedItem().toString(),selectedRepairArea);
		dialog.showDialog();
	}

	private void updateRepairAreaRow(ActionEvent event){
		RepairAreaRowDataDialog dialog = new RepairAreaRowDataDialog(QiConstant.UPDATE_ROW, selectedRepairAreaRow, getModel(),getApplicationId(),getView().getPlantCombobox().getSelectionModel().getSelectedItem().toString(),selectedRepairArea);
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();	
	}
	private void deleteRepairAreaRow(ActionEvent event){
		List<QiRepairAreaSpace> repairAreaSpaceList = getModel().findAllByRepairAreaNameAndRow(selectedRepairAreaRow.getId().getRepairAreaName(),selectedRepairAreaRow.getId().getRepairAreaRow());
		if(!repairAreaSpaceList.isEmpty()){
			MessageDialog.showError(getView().getStage(),"Cannot delete Repair Area Row as it is assigned to "+repairAreaSpaceList.size()+" Repair Area Spaces");
		}
		else{
			boolean deletedRepairAreaRow  = MessageDialog.confirm(getView().getStage(), "Do you want to delete Repair Area Row # ?");
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if(!deletedRepairAreaRow){
				return;
			}
			else{
				if(dialog.showReasonForChangeDialog(null))
				{
					getModel().deleteRepairAreaRow(selectedRepairAreaRow);
					AuditLoggerUtil.logAuditInfo(selectedRepairAreaRow, null, dialog.getReasonForChangeText(),getView().getScreenName() ,getUserId());
				}
			}
		}
	}

	private void createSpaceAssignment(ActionEvent event){
		SpaceAssignmentsDialog dialog = new SpaceAssignmentsDialog(QiConstant.SPACE_ASSIGNMENT, getApplicationId(), getModel(),selectedRepairAreaRow,getApplicationId(),getView().getPlantCombobox().getSelectionModel().getSelectedItem().toString(),selectedRepairArea);
		dialog.showDialog();
	}
	private void createRepairSpace(ActionEvent event){
		RepairAreaSpaceDialog dialog = new RepairAreaSpaceDialog(QiConstant.CREATE_SPACE, new QiRepairAreaSpace(), getModel(),getApplicationId(),getView().getPlantCombobox().getSelectionModel().getSelectedItem().toString(),selectedRepairArea,selectedRepairAreaRow);
		dialog.showDialog();
	}

	private void deleteRepairAreaSpace(ActionEvent event) {
		String message = null;
		for (QiRepairAreaSpace selectedRepairAreaSpace : selectedRepairAreaSpaceList) {
			if (!StringUtils.isBlank(selectedRepairAreaSpace.getProductId())) {
				message = " " + selectedRepairAreaSpace.getId().getRepairArearSpace();
			}
		}
		if (message != null) {
			MessageDialog.showError(getView().getStage(),"Can not delete Repair Area Space:" + message + " as product id assigned to.");
		} else {
			boolean deletedRepairAreaSpace  = MessageDialog.confirm(getView().getStage(), "Do you want to delete Repair Area Space #?");
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());

			if(!deletedRepairAreaSpace) {
				return;
			} else {
				if (dialog.showReasonForChangeDialog(null))	{
					for (QiRepairAreaSpace selectedRepairAreaSpace : selectedRepairAreaSpaceList) {
						getModel().deleteRepairAreaSpace(selectedRepairAreaSpace);
						AuditLoggerUtil.logAuditInfo(selectedRepairAreaSpace, null, dialog.getReasonForChangeText(),getView().getScreenName() ,getUserId());
					}
				}
			}
		}
	}

	private void fetchAssociatedRepairAreaRow(String repairAreaName){
		List<QiRepairAreaRow> list= getModel().findAllByRepairAreaName(repairAreaName);
		getView().getRepairRowDataTablePane().setData(list);
	}

	private void fetchAssociatedRepairAreaSpace(String repairAreaName,int repairAreaRow){
		List<QiRepairAreaSpace> list= getModel().findAllByRepairAreaNameAndRow(repairAreaName,repairAreaRow);
		getView().getSpaceDataTablePane().setData(list);
	}

	private void repeatSpaceDialog(ActionEvent event){
		RepeatSpaceDialog dialog = new RepeatSpaceDialog(QiConstant.REPEAT_SPACE, getApplicationId(), getModel(),repairAreaSpaceList);
		dialog.showDialog();
	}

	private void repeatRowDialog(ActionEvent event){
		RepeatRowDialog dialog = new RepeatRowDialog(QiConstant.REPEAT_ROW, getApplicationId(), getModel(),repairAreaRowList);
		dialog.showDialog();
	}

	private void updateRepairAreaSpaceStatus(ActionEvent actionEvent,boolean status) {	
		String message = null;
		for (QiRepairAreaSpace selectedRepairAreaSpace : selectedRepairAreaSpaceList) {
			if (!StringUtils.isBlank(selectedRepairAreaSpace.getProductId()) && selectedRepairAreaSpace.isActive()) {
				message = " " + selectedRepairAreaSpace.getId().getRepairArearSpace();
			}
		}
		if (message != null) {
			MessageDialog.showError(getView().getStage(),"Can not inactivate Repair Area Space:" + message + " as product id assigned to.");
		} else {
		
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
	
			if (dialog.showReasonForChangeDialog(null)) {		
				for (QiRepairAreaSpace selectedRepairAreaSpace : selectedRepairAreaSpaceList) {
					QiRepairAreaSpace selectedRepairAreaSpaceCloned = (QiRepairAreaSpace) selectedRepairAreaSpace.deepCopy();
					selectedRepairAreaSpace.setActive(status);
					selectedRepairAreaSpace.setUpdateUser(getUserId());
					// call to audit
					AuditLoggerUtil.logAuditInfo(selectedRepairAreaSpaceCloned, selectedRepairAreaSpace, dialog.getReasonForChangeText(),
							getView().getScreenName(),getUserId());			
					getModel().updateRepairAreaSpaceStatus(selectedRepairAreaSpace);
				}
			}
		}
	}

	@Override
	public void addContextMenuItems() {
		List<String> menuItemsList = new ArrayList<String>();	
		selectedRepairArea = getView().getRepairAreaTablePane().getSelectedItem();
		menuItemsList.add(QiConstant.CREATE);
		if (selectedRepairArea != null) {
			menuItemsList.add(QiConstant.UPDATE);
			menuItemsList.add(QiConstant.DELETE);
		}
		getView().getRepairAreaTablePane().createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
	}
}
