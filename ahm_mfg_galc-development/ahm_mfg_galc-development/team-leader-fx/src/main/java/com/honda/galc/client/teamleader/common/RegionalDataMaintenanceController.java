package com.honda.galc.client.teamleader.common;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.RegionalCode;
import com.honda.galc.entity.enumtype.RegionalCodeName;
import com.honda.galc.util.AuditLoggerUtil;

public class RegionalDataMaintenanceController extends AbstractController<RegionalDataMaintenanceModel, RegionalDataMaintenancePanel>
	implements EventHandler<ActionEvent> {

	public RegionalDataMaintenanceController(RegionalDataMaintenanceModel model, RegionalDataMaintenancePanel view) {
		super(model, view);
		getModel().setApplicationContext(getView().getMainWindow().getApplicationContext());
	}
	
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			handleMenuItem(menuItem, actionEvent);		
		}
	}
	
	private void handleMenuItem(MenuItem menuItem, ActionEvent event) {
		Logger.getLogger().check("Menu Item: "+ menuItem.getText().trim() + " clicked");
		clearMessages();

		if (RegionalDataConstant.DELETE_CODE.equals(menuItem.getText())) {
			String selectedRegionalCodeName = getView().getRegionalCodeTablePane().getSelectedItem().getId().getRegionalCodeName();
			if (RegionalCodeName.getId(selectedRegionalCodeName) != 0) { //enum item, can't be deleted or updated
				displayErrorMessage("This Regional Code cannot be deleted as it is defined in RegionalCodeName enum.");
				return;
			}
			
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if (dialog.showReasonForChangeDialog("Are you sure to delete the Regional Code below?" +
					"\n\nRegional Code: "+ selectedRegionalCodeName)) {
				try {
					List<RegionalCode> regionalCodeList = getModel().findRegionalValueList(selectedRegionalCodeName);
					
					getModel().deleteRegionalCode(getView().getRegionalCodeTablePane().getSelectedItem().getId().getRegionalCodeName());
					for (int i = 0; i < regionalCodeList.size(); i++) {
						AuditLoggerUtil.logAuditInfo(regionalCodeList.get(i), null, dialog.getReasonForChangeTextArea().getText(), 
								getView().getScreenName(), getModel().getApplicationContext().getUserId().toUpperCase());
					}
					getModel().setSelectedRegionalCode(null); //clear the selected
				} catch (Exception e) {
					displayErrorMessage("An error occured in deleting regional code.");
				}
			}
		} else if (RegionalDataConstant.DELETE_VALUE.equals(menuItem.getText())) {
			RegionalCode selectedRegionalValue = getView().getRegionalValueTablePane().getSelectedItem();
			String selectedRegionalCodeName = selectedRegionalValue.getId().getRegionalCodeName();
			String selectedRegionalValueString = selectedRegionalValue.getId().getRegionalValue();
			
			if (RegionalCodeName.CATEGORY_CODE.getName().equals(selectedRegionalCodeName)) { 
				//check usage in REGIONA_PROCESS_POINT_GROUP_TBX 
				if (getModel().findCountByCategoryCode(new Short(selectedRegionalValueString).shortValue()) > 0) {
					displayErrorMessage("This Category Code cannot be deleted as it is used in Regional Process Point Group.");
					return;
				}
			} else if (RegionalCodeName.PROCESS_AREA_GROUP_NAME.getName().equals(selectedRegionalCodeName)) { 
				//check usage in REGIONA_PROCESS_POINT_GROUP_TBX 
				if (getModel().findCountByprocessPointGroupName(selectedRegionalValueString) > 0) {
					displayErrorMessage("This Process Area Group Name cannot be deleted as it is used in Regional Process Point Group.");
					return;
				}
			}
			
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			String selectedRegionalValueName = selectedRegionalValue.getRegionalValueName();
			String selectedRegionalValueAbbr = selectedRegionalValue.getRegionalValueAbbr();
			String selectedRegionalValueDesc = selectedRegionalValue.getRegionalValueDesc();
			
			if (dialog.showReasonForChangeDialog("Are you sure to delete the Regional Value below?" + 
			"\n\nRegional Code: " + selectedRegionalCodeName +
			"\nRegional Value: " + selectedRegionalValueString +
			"\nRegional Value Name: " + selectedRegionalValueName +
			"\nRegional Value Abbreviation: " + selectedRegionalValueAbbr +
			"\nRegional Value Description: " + selectedRegionalValueDesc)) {
				
				try {
					getModel().deleteRegionalValue(selectedRegionalValue);
					AuditLoggerUtil.logAuditInfo(selectedRegionalValue, null, dialog.getReasonForChangeTextArea().getText(), 
							getView().getScreenName(), getModel().getApplicationContext().getUserId().toUpperCase());
					
					List<RegionalCode> regionalCodeList = getModel().findRegionalValueList(selectedRegionalCodeName);
					if (regionalCodeList == null || regionalCodeList.size() == 0) {
						getModel().setSelectedRegionalCode(null); //clear the selected code
					} else {
						getModel().setSelectedRegionalCode(regionalCodeList.get(0)); //set the selected to the first one on the list
					}
					getModel().setSelectedRegionalValue(null); //clear the selected value
				} catch (Exception e) {
					displayErrorMessage("An error occured in deleting regional value.");
				}
			}
		} else {
			if (RegionalDataConstant.UPDATE_CODE.equals(menuItem.getText())) {
				getModel().setSelectedRegionalCode(getView().getRegionalCodeTablePane().getSelectedItem());
			} else if (RegionalDataConstant.UPDATE_VALUE.equals(menuItem.getText())) {
				getModel().setSelectedRegionalValue(getView().getRegionalValueTablePane().getSelectedItem());
			}
			
			RegionalDataDialog dialog = new RegionalDataDialog(menuItem.getText(), getModel(), getApplicationId());
			dialog.showDialog();
		}
		
		getView().reload();
		
		List<RegionalCode> selectedRegionalCodeList = new ArrayList<RegionalCode>();
		RegionalCode selectedRegionalCode = getModel().getSelectedRegionalCode();
		if (selectedRegionalCode != null) {
			selectedRegionalCodeList.add(selectedRegionalCode);
			getView().getRegionalCodeTablePane().selectList(selectedRegionalCodeList);
		}
		
		List<RegionalCode> selectedRegionalValueList = new ArrayList<RegionalCode>();
		RegionalCode selectedRegionalValue = getModel().getSelectedRegionalValue();
		if (selectedRegionalValue != null) {
			selectedRegionalValueList.add(selectedRegionalValue);
			getView().getRegionalValueTablePane().selectList(selectedRegionalValueList);
		}
	}

	@Override
	public void initEventHandlers() {
		if (isFullAccess()) {
			addContextMenuItemsRegionalCode();
		}
		getView().getRegionalCodeTablePane().getTable().getSelectionModel().selectedItemProperty()
			.addListener(regionalCodeTablePaneChangeListener);
		getView().getRegionalValueTablePane().getTable().getSelectionModel().selectedItemProperty()
			.addListener(regionalValueTablePaneChangeListener);
	}

	ChangeListener<RegionalCode> regionalCodeTablePaneChangeListener = new ChangeListener<RegionalCode>() {
		public void changed(ObservableValue<? extends RegionalCode> arg0, RegionalCode arg1,
				RegionalCode arg2) {
			if (isFullAccess()) {
				addContextMenuItemsRegionalCode();
				addContextMenuItemsRegionalValue();
			}
			if (null != getView().getRegionalCodeTablePane().getSelectedItem()) {
				RegionalCode selectedRegionalCode = getView().getRegionalCodeTablePane().getSelectedItem();
				getModel().setSelectedRegionalCode(selectedRegionalCode);
				List<RegionalCode> regionalValueList = getModel().findRegionalValueList(selectedRegionalCode.getId().getRegionalCodeName());
				getView().getRegionalValueTablePane().setData(regionalValueList);
			}
		}
	};
	
	ChangeListener<RegionalCode> regionalValueTablePaneChangeListener = new ChangeListener<RegionalCode>() {
		public void changed(ObservableValue<? extends RegionalCode> arg0, RegionalCode arg1,
				RegionalCode arg2) {
			if (isFullAccess()) {
				addContextMenuItemsRegionalValue();
			}
			if (null != getView().getRegionalValueTablePane().getSelectedItem()) {
				RegionalCode selectedRegionalCode = getView().getRegionalCodeTablePane().getSelectedItem();
				RegionalCode selectedRegionalValue = getView().getRegionalValueTablePane().getSelectedItem();
				getModel().setSelectedRegionalCode(selectedRegionalCode);
				getModel().setSelectedRegionalValue(selectedRegionalValue);
			}
		}
	};
	
	public void addContextMenuItemsRegionalCode() {
		ObjectTablePane<RegionalCode> tablePane = getView().getRegionalCodeTablePane();
		String[] menuItems;

		if (null != tablePane.getSelectedItem()) {
			String selectedRegionalCodeName = tablePane.getSelectedItem().getId().getRegionalCodeName();
			boolean isDefinedInEnum = false;
			for(RegionalCodeName type : RegionalCodeName.values()) {
				if(type.getName().equalsIgnoreCase(selectedRegionalCodeName)) {
					isDefinedInEnum = true;
					break;
				}
			}
		
			if (isDefinedInEnum) { //if defined in RegionalCodeName enum, it can't be deleted or updated
				menuItems = new String[] {RegionalDataConstant.CREATE_CODE};
			} else {
				menuItems = new String[] {RegionalDataConstant.CREATE_CODE, 
					RegionalDataConstant.UPDATE_CODE, RegionalDataConstant.DELETE_CODE};	
			}
		} else {
			menuItems = new String[] {RegionalDataConstant.CREATE_CODE};
		}
		
		tablePane.createContextMenu(menuItems, this);
	}
	
	public void addContextMenuItemsRegionalValue() {
		ObjectTablePane<RegionalCode> tablePane = getView().getRegionalValueTablePane();
		String[] menuItems;

		if (null != tablePane.getSelectedItem()) {
			menuItems = new String[] {RegionalDataConstant.CREATE_VALUE, 
					RegionalDataConstant.UPDATE_VALUE, RegionalDataConstant.DELETE_VALUE};			
		} else if (null != getView().getRegionalCodeTablePane().getSelectedItem()) {
			menuItems = new String[] {RegionalDataConstant.CREATE_VALUE};
		} else {
			return;
		}
		
		tablePane.createContextMenu(menuItems, this);
	}
}
