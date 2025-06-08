package com.honda.galc.client.teamleader.qi.controller;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.SpaceAssignmentsDialog;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiRepairAreaSapceAssignmentDto;
import com.honda.galc.entity.qi.QiRepairAreaRowId;
import com.honda.galc.entity.qi.QiRepairAreaSpaceId;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;

public class SpaceAssignmentDialogController extends QiDialogController<ParkingLocationMaintenanceModel, SpaceAssignmentsDialog> {
	private QiRepairAreaSapceAssignmentDto qiRepairAreaSapceAssignmentDto;
	QiRepairAreaSpaceId id;
	public SpaceAssignmentDialogController(ParkingLocationMaintenanceModel model,SpaceAssignmentsDialog dialog,QiRepairAreaSapceAssignmentDto qiRepairAreaSapceAssignmentDto) {
		setModel(model);
		setDialog(dialog);
		this.qiRepairAreaSapceAssignmentDto=qiRepairAreaSapceAssignmentDto;
	}
	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof MenuItem) {
			clearDisplayMessage();
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			if(QiConstant.CLEAR_SPACE.equalsIgnoreCase(menuItem.getText())) clearSpace();
		} 	
	}
	@Override
	public void initListeners() {
		addSpaceAssignmentTableListener();
		addRowComboboxListener();
	}
	private void clearSpace(){
		qiRepairAreaSapceAssignmentDto = getDialog().getViewSpaceAssignmentsTablePane().getSelectedItem();
		id=new QiRepairAreaSpaceId(); 
		id.setRepairAreaName(qiRepairAreaSapceAssignmentDto.getRepairAreaName());
		id.setRepairArearRow(qiRepairAreaSapceAssignmentDto.getRepairAreaRow());
		id.setRepairArearSpace(qiRepairAreaSapceAssignmentDto.getRepairAreaSpace());
		getModel().clearRepairAreaSpace(id, getUserId());
		loadSpaceData(getDialog().getRowCombobox().getValue().toString());

	}
	/**
	 * This method is event listener for rowComboBox
	 */


	@SuppressWarnings("unchecked")
	private void addRowComboboxListener() {
		getDialog().getRowCombobox().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				getDialog().getViewSpaceAssignmentsTablePane().getTable().getItems().clear();
				loadSpaceData(newValue);
			}
		});		
	}

	public void addContextMenuItemsForSpaceAssignmentTable() {
		String []menuItems = new String[] { QiConstant.CLEAR_SPACE};
		if(qiRepairAreaSapceAssignmentDto!=null){
			if((!StringUtils.trimToEmpty(qiRepairAreaSapceAssignmentDto.getProductId()).isEmpty() && qiRepairAreaSapceAssignmentDto.getProductId()!=null) ||(!StringUtils.trimToEmpty(qiRepairAreaSapceAssignmentDto.getProductType()).isEmpty() && qiRepairAreaSapceAssignmentDto.getProductType()!=null)){
				getDialog().getViewSpaceAssignmentsTablePane().createContextMenu(menuItems, this);
			}else {
				getDialog().getViewSpaceAssignmentsTablePane().getTable().getContextMenu().getItems().clear();
			}
		}
	}




	private void addSpaceAssignmentTableListener() {
		getDialog().getViewSpaceAssignmentsTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiRepairAreaSapceAssignmentDto>() {
			public void changed(
					ObservableValue<? extends QiRepairAreaSapceAssignmentDto> arg0,
					QiRepairAreaSapceAssignmentDto arg1, QiRepairAreaSapceAssignmentDto arg2) {
				qiRepairAreaSapceAssignmentDto = getDialog().getViewSpaceAssignmentsTablePane().getSelectedItem();
				addContextMenuItemsForSpaceAssignmentTable();
			}

		});


	}

	private void loadSpaceData(String row){
		QiRepairAreaRowId repairAreaRowId =new QiRepairAreaRowId();
		repairAreaRowId.setRepairAreaName(getDialog().getQiRepairAreaRow().getId().getRepairAreaName());
		repairAreaRowId.setRepairAreaRow(Integer.parseInt(row));
		getDialog().getViewSpaceAssignmentsTablePane().getTable().setItems(FXCollections.observableArrayList(getModel().findAllSpaceAssignmentByRepairAreaNameAndRow(repairAreaRowId)));
	}

}
