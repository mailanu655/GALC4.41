package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.teamleader.qi.model.ExternalSystemMaintModel;
import com.honda.galc.client.teamleader.qi.view.ExternalSystemMaintDialog;
import com.honda.galc.client.teamleader.qi.view.ExternalSystemMaintPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiExternalSystemInfo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * 
 * <h3>ExternalSystemMaintController Class description</h3>
 * <p>
 * ExternalSystemMaintController description
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
 * @author Justin Jiang<br>
 *         January 14, 2021
 *
 */

public class ExternalSystemMaintController extends AbstractQiController<ExternalSystemMaintModel, ExternalSystemMaintPanel> implements EventHandler<ActionEvent>{
	
	private QiExternalSystemInfo selectedExternalSystem;
	
	public ExternalSystemMaintController(ExternalSystemMaintModel model, ExternalSystemMaintPanel view) {
		super(model, view);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if (actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			String menuItemText = menuItem.getText();
			if (QiConstant.CREATE.equals(menuItemText)) 
				createExternalSystem(actionEvent);
			else if (QiConstant.UPDATE.equals(menuItemText))
				updateExternalSystem(actionEvent);
			else if (QiConstant.DELETE.equals(menuItemText))
				deleteExternalSystem(actionEvent);
		}
	}

	@Override
	public void initEventHandlers() {
		addExternalSystemTableListener();
		updateContextMenuItems();
	}

	private void addExternalSystemTableListener() {	
		getView().getExternalSystemTablePane().getTable().getSelectionModel().selectedItemProperty().addListener
			(new ChangeListener<QiExternalSystemInfo>() {
			public void changed(
					ObservableValue<? extends QiExternalSystemInfo> arg0,
					QiExternalSystemInfo arg1,
					QiExternalSystemInfo arg2) {
				clearDisplayMessage();
				updateContextMenuItems();
			}
		});
	}

	@Override
	public void addContextMenuItems() {
	}
	
	public void updateContextMenuItems() {
		List<String> menuItemsList = new ArrayList<String>();
		menuItemsList.add(QiConstant.CREATE);
		
		selectedExternalSystem = getView().getExternalSystemTablePane().getSelectedItem();
		if (selectedExternalSystem != null) {
			menuItemsList.add(QiConstant.UPDATE);
			menuItemsList.add(QiConstant.DELETE);
		}
		getView().getExternalSystemTablePane().createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
	}

	private void createExternalSystem(ActionEvent event){
		ExternalSystemMaintDialog dialog = new ExternalSystemMaintDialog(getApplicationId());
		if (!dialog.showExternalSystemMaintDialog("Create External System", false)) {
			//clicked Cancel button
			return;
		}

		String name = dialog.getName();
		String desc = dialog.getDesc();
		if (!getModel().isExisted(name)) {
			selectedExternalSystem = getModel().createExternalSystemInfo(name, desc, getUserId());
			int index = refreshTableData(selectedExternalSystem);
			getView().getExternalSystemTablePane().getTable().getSelectionModel().select(index);
		} else {
			MessageDialog.showError("New External System can't be the same as an existing one!");
		}
	}

	private void updateExternalSystem(ActionEvent event){
		ExternalSystemMaintDialog dialog = new ExternalSystemMaintDialog(getApplicationId());
		if (!dialog.showExternalSystemMaintDialog("Update External System", true)) {
			return;
		}
		String name = dialog.getName();
		String desc = dialog.getDesc();
		boolean canUpdate = false;
		if (selectedExternalSystem.getExternalSystemName().equals(name)) {
			//update desc only
			canUpdate = true;
		} else if (getModel().isExternalSystemNameUsed(selectedExternalSystem.getExternalSystemName())) {
			MessageDialog.showError("The External System Name is being used and you can only update Description");
		} else if (getModel().isExisted(name)) {
			MessageDialog.showError("Updated External System can't be the same as an existing one!");
		} else {
			canUpdate = true;
		}
			
		if (canUpdate) {
			getModel().updateExternalSystemInfo(selectedExternalSystem.getExternalSystemId(), name, desc, getUserId());
			int index = refreshTableData(selectedExternalSystem);
			getView().getExternalSystemTablePane().getTable().getSelectionModel().select(index);
		}
	}
	
	private void deleteExternalSystem(ActionEvent event){
		boolean delete = MessageDialog.confirm(getView().getStage(), 
				"Are you sure you want to delete the External System?");

		if (!delete) {
			return;
		} else {
			if (!getModel().isExternalSystemIdUsed(selectedExternalSystem.getExternalSystemId()) &&
					!getModel().isExternalSystemNameUsed(selectedExternalSystem.getExternalSystemName())) {
				getModel().deleteExternalSystemInfo(selectedExternalSystem);
				selectedExternalSystem = null;
				refreshTableData(null);
			} else {
				MessageDialog.showError("The External System is being used and can't be deleted!");
			}
		}
	}

	private int refreshTableData(QiExternalSystemInfo selectedExternalSystem){
		List<QiExternalSystemInfo> list= getModel().findAllExternalSystem();
		int index = 0;
		if (selectedExternalSystem != null) {
			for (int i = 0; i < list.size(); i++) {
				if (((QiExternalSystemInfo)list.get(i)).getId() == selectedExternalSystem.getId()) {
					index = i;
					break;
				}
			}
		}
		getView().getExternalSystemTablePane().setData(list);
		return index;
	}
}
