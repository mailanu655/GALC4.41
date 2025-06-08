package com.honda.galc.client.teamleader.qi.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.client.teamleader.qi.model.RepairMethodMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.teamleader.qi.view.RepairMethodDialog;
import com.honda.galc.client.teamleader.qi.view.RepairMethodMaintenancePanel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiRepairMethod;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;

public class RepairMethodController
		extends AbstractQiController<RepairMethodMaintenanceModel, RepairMethodMaintenancePanel>
		implements EventHandler<ActionEvent> {
	private QiRepairMethod repairMethod;
	private List<QiRepairMethod> qiRepairMethodList;

	public RepairMethodController(RepairMethodMaintenanceModel model, RepairMethodMaintenancePanel view) {
		super(model, view);
		
	}

	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if (actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();

			if (QiConstant.CREATE.equals(menuItem.getText()))
				createRepairMethod(actionEvent);
			else if (QiConstant.UPDATE.equals(menuItem.getText()))
				updateRepairMethod(actionEvent, repairMethod);
			else if (QiConstant.INACTIVATE.equals(menuItem.getText()))
				updateRepairMethodStatus(actionEvent, false);
			else if (QiConstant.REACTIVATE.equals(menuItem.getText()))
				updateRepairMethodStatus(actionEvent, true);
		}
		if (actionEvent.getSource() instanceof LoggedRadioButton) {
			getView().reload(getView().getFilterTextData());
		}
		if (actionEvent.getSource() instanceof UpperCaseFieldBean) {
			getView().reload(getView().getFilterTextData());
		}
	}
	private void createRepairMethod(ActionEvent event){
		RepairMethodDialog dialog = new RepairMethodDialog(QiConstant.CREATE, new QiRepairMethod(), getModel(),getApplicationId());
		dialog.showDialog();
		getView().reload(getView().getFilterTextData());
	};
	
	private void updateRepairMethod(ActionEvent actionEvent, QiRepairMethod qiRepairMethod){
		RepairMethodDialog dialog = new RepairMethodDialog(QiConstant.UPDATE, qiRepairMethod, getModel(),getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		if(getView().getAllRadioBtn().isSelected())
			getView().reload(getView().getFilterTextData());
		else if(getView().getActiveRadioBtn().isSelected())
			getView().reload(getView().getFilterTextData());
		else
			getView().reload(getView().getFilterTextData());
	}
	
	
	
	private void updateRepairMethodStatus(ActionEvent actionEvent,boolean status)
	{		
		try{
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if (dialog.showReasonForChangeDialog(null)) {
				try {
					List<String> qirepairMethodToValidateList = new ArrayList<String>();
					for (QiRepairMethod repairMethod : qiRepairMethodList) {
						String repairMethodName = repairMethod.getRepairMethod();
						qirepairMethodToValidateList.add(repairMethodName);
					}

					if (repairMethod.isActive()) {
						String returnValue=isLocalSiteImpacted(qirepairMethodToValidateList,getView().getStage());
						if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
							return;
						}
						else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
							publishErrorMessage("Inactivation of the repair method affects Local Sites");
							return;
						}
					}


					List<QiRepairMethod> selectedRepairMethodList;
					List<QiRepairMethod> newselectedRepairMethodList = new ArrayList<QiRepairMethod>();

					selectedRepairMethodList = getView().getRepairMethodTablePane().getTable().getSelectionModel()
							.getSelectedItems();

					for (QiRepairMethod repairMethod : selectedRepairMethodList) {
						QiRepairMethod repairMethodCloned = (QiRepairMethod) repairMethod.deepCopy();

						repairMethod.setActive(status);
						repairMethod.setUpdateUser(getUserId());
						repairMethod.setAppUpdateTimestamp(new Date());
						newselectedRepairMethodList.add(repairMethod);

						// call to audit
						AuditLoggerUtil.logAuditInfo(repairMethodCloned, repairMethod, dialog.getReasonForChangeText(),
								getView().getScreenName(),getUserId());
					}
					getModel().updateRepairMethodStatus(newselectedRepairMethodList);								

					getView().reload(getView().getFilterTextData());


				} catch (Exception e) {
					handleException("An error occured in updateRepairMethodStatus method ","Failed to Update status", e);
				}
			}
			else
			{
				return;
			}

		}catch (Exception e) {
			handleException("An error occurred in inactivateDefect method ", "Failed to open inactivate Repair Method popup ", e);
		}

	}	
	
	public void addContextMenuItems()
	{
		clearDisplayMessage();
		List<String> menuItemsList = new ArrayList<String>();
		qiRepairMethodList = getView().getRepairMethodTablePane().getSelectedItems();
		if (qiRepairMethodList.size() == 1) {
			menuItemsList.add(QiConstant.CREATE);
			menuItemsList.add(QiConstant.UPDATE);
			if (getView().getAllRadioBtn().isSelected()) {
				getView().getRepairMethodTablePane().getTable().getSelectionModel()
						.setSelectionMode(SelectionMode.SINGLE);
			} else if (getView().getActiveRadioBtn().isSelected()) {
				menuItemsList.add(QiConstant.INACTIVATE);
				getView().getRepairMethodTablePane().getTable().getSelectionModel()
						.setSelectionMode(SelectionMode.MULTIPLE);
			} else {
				menuItemsList.add(QiConstant.REACTIVATE);
				getView().getRepairMethodTablePane().getTable().getSelectionModel()
						.setSelectionMode(SelectionMode.MULTIPLE);
			}
		} else if (qiRepairMethodList.size() > 1) {
			if (getView().getActiveRadioBtn().isSelected()) {
				menuItemsList.add(QiConstant.INACTIVATE);
			} else {
				menuItemsList.add(QiConstant.REACTIVATE);
			}
		} else {

			menuItemsList.add(QiConstant.CREATE);
		}
		getView().getRepairMethodTablePane()
				.createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
	}

	@Override
	public void initEventHandlers() {
		if (isFullAccess()) {
			addRepairMethodTableListener();
		}
	}
			
	private void addRepairMethodTableListener() {		
		getView().getRepairMethodTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiRepairMethod>() {
			public void changed(
					ObservableValue<? extends QiRepairMethod> arg0,
					QiRepairMethod arg1,
					QiRepairMethod arg2) {
				repairMethod = getView().getRepairMethodTablePane().getSelectedItem();
				qiRepairMethodList = getView().getRepairMethodTablePane().getSelectedItems();
				addContextMenuItems();
			}
		});
		
		getView().getRepairMethodTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}

}
