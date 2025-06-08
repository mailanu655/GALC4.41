package com.honda.galc.client.teamleader.common;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.AbstractQiController;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.RegionalProcessPointGroupDto;
import com.honda.galc.entity.conf.RegionalCode;
import com.honda.galc.entity.conf.RegionalCodeId;
import com.honda.galc.entity.conf.RegionalProcessPointGroup;
import com.honda.galc.entity.conf.RegionalProcessPointGroupId;
import com.honda.galc.entity.enumtype.RegionalCodeName;
import com.honda.galc.util.AuditLoggerUtil;

public class RegionalProcessPointGroupMaintenanceController extends AbstractQiController<RegionalProcessPointGroupMaintenanceModel, RegionalProcessPointGroupMaintenancePanel> implements EventHandler<ActionEvent> {

	public RegionalProcessPointGroupMaintenanceController(RegionalProcessPointGroupMaintenanceModel model,RegionalProcessPointGroupMaintenancePanel view) {
		super(model, view);
	}

	ChangeListener<String> categoryCodeComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,
				String oldValue, String newValue) {
			Logger.getLogger().check(newValue + " category code selected");
			clearDisplayMessage();
			if (getView().getSiteComboBox().getSelectionModel().getSelectedItem() != null) {
				getView().reload();
			}
		}
	};
	
	ChangeListener<String> siteComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
			Logger.getLogger().check(newValue + " site selected");
			clearDisplayMessage();
			getView().reload();
			
			getView().getLeftShiftBtn().setDisable(true);
			getView().getRightShiftBtn().setDisable(true);
			getView().getSaveBtn().setDisable(true);
		}
	};
	
	/**
	 * This is an implemented method of EventHandler interface. Called whenever an ActionEvent is triggered.
	 * Selecting Left shift,right shift,save Btn is an ActionEvent. So respective method is called based on which action event is triggered.
	 */
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			if (actionEvent.getSource().equals(getView().getRightShiftBtn())) {
				shiftAvailableToAssignedBtnAction(actionEvent);
			} else if (actionEvent.getSource().equals(getView().getLeftShiftBtn())) {
				shiftAssignedToAvailableBtnAction(actionEvent);
			} else if (actionEvent.getSource().equals(getView().getSaveBtn())) {
				saveBtnAction(actionEvent);
			}
		} else if (actionEvent.getSource() instanceof UpperCaseFieldBean) {
			if (getView().getFilterTextField().isFocused()) {
				getView().reload(StringUtils.trim(getView().getFilterTextField().getText()));
			}
		}
	}

	@Override
	public void addContextMenuItems() {
	}

	/**
	 *  This method is used to add listener on main panel table.
	 */
	@Override
	public void initEventHandlers() {
		addCategoryCodeComboBoxListener();
		addSiteComboBoxListener();
		addAvailableAndAssignedProcessPointGroupListener();
	}

	
	/**
	 * This method is used when shift Available Process Point Group to Assigned
	 * @param event
	 */
	private void shiftAvailableToAssignedBtnAction(ActionEvent actionEvent) {
		List<RegionalCode> selectedAvailableProcessPointGrouplist = getView().getAvailableProcessPointGroupObjectTablePane().getSelectedItems();
		if (!selectedAvailableProcessPointGrouplist.isEmpty()) {
			List<RegionalProcessPointGroupDto> toBeAssignedProcessPointGroupDtoList = new ArrayList<RegionalProcessPointGroupDto>();
			for (RegionalCode regionalCode : selectedAvailableProcessPointGrouplist) {
				if (regionalCode != null) {
					RegionalProcessPointGroupDto regionalProcessPointGroupDto = new RegionalProcessPointGroupDto();
					regionalProcessPointGroupDto.setSite((String)getView().getSiteComboBox().getSelectionModel().getSelectedItem());
					String categoryCodeString = (String)getView().getCategoryCodeComboBox().getSelectionModel().getSelectedItem();
					regionalProcessPointGroupDto.setCategoryCode(Short.parseShort(categoryCodeString.substring(0, categoryCodeString.indexOf(" - "))));
					regionalProcessPointGroupDto.setRegionalValue(regionalCode.getId().getRegionalValue());
					regionalProcessPointGroupDto.setRegionalValueName(regionalCode.getRegionalValueName());
					regionalProcessPointGroupDto.setRegionalValueAbbr(regionalCode.getRegionalValueAbbr());
					regionalProcessPointGroupDto.setRegionalValueDesc(regionalCode.getRegionalValueDesc());
					toBeAssignedProcessPointGroupDtoList.add(regionalProcessPointGroupDto);
				}
			}
			getView().getAssignedProcessPointGroupObjectTablePane().getTable().getItems().addAll(toBeAssignedProcessPointGroupDtoList);
			getView().getAvailableProcessPointGroupObjectTablePane().getTable().getItems().removeAll(selectedAvailableProcessPointGrouplist);
			getView().getAvailableProcessPointGroupObjectTablePane().getTable().getSelectionModel().clearSelection();
			
			if (isFullAccess()) {
				getView().getSaveBtn().setDisable(false);
			} else {
				getView().getSaveBtn().setDisable(true);
			}
		}
	}

	/**
	 * This method is used when shift Assigned Process Point Group to Available
	 * @param event
	 */
	private void shiftAssignedToAvailableBtnAction(ActionEvent actionEvent) {
		List<RegionalProcessPointGroupDto> selectedAssignedProcessPointGrouplist = getView().getAssignedProcessPointGroupObjectTablePane().getSelectedItems();
		if (!selectedAssignedProcessPointGrouplist.isEmpty()) {
			List<RegionalCode> toBeDeassignedProcessPointGroupList = new ArrayList<RegionalCode>();
			for (RegionalProcessPointGroupDto regionalProcessPointGroupDto : selectedAssignedProcessPointGrouplist) {
				if (regionalProcessPointGroupDto != null) {
					RegionalCodeId regionalCodeId = new RegionalCodeId();
					regionalCodeId.setRegionalCodeName(RegionalCodeName.PROCESS_AREA_GROUP_NAME.getName());
					regionalCodeId.setRegionalValue(regionalProcessPointGroupDto.getRegionalValue());
					RegionalCode regionalCode = new RegionalCode(regionalCodeId);
					regionalCode.setRegionalValueName(regionalProcessPointGroupDto.getRegionalValueName());
					regionalCode.setRegionalValueAbbr(regionalProcessPointGroupDto.getRegionalValueAbbr());
					regionalCode.setRegionalValueDesc(regionalProcessPointGroupDto.getRegionalValueDesc());					
					toBeDeassignedProcessPointGroupList.add(regionalCode);
				}
			}
			getView().getAvailableProcessPointGroupObjectTablePane().getTable().getItems().addAll(toBeDeassignedProcessPointGroupList);
			getView().getAssignedProcessPointGroupObjectTablePane().getTable().getItems().removeAll(selectedAssignedProcessPointGrouplist);
			getView().getAssignedProcessPointGroupObjectTablePane().getTable().getSelectionModel().clearSelection();
			
			if (isFullAccess()) {
				getView().getSaveBtn().setDisable(false);
			} else {
				getView().getSaveBtn().setDisable(true);
			}
		}
	}
	
	/**
	 * This method is used to create Regional Process Point Group
	 * @param repair method list
	 */
	private void createRegionalProcessPointGroups(List<RegionalProcessPointGroupDto> regionalProcessPointGroupDtoList) {
		List<RegionalProcessPointGroup> regionalProcessPointGroupList = new ArrayList<RegionalProcessPointGroup>();
		if (!regionalProcessPointGroupDtoList.isEmpty()) {
			for (RegionalProcessPointGroupDto regionalProcessPointGroupDto : regionalProcessPointGroupDtoList) {
				RegionalProcessPointGroupId regionalProcessPointGroupId = new RegionalProcessPointGroupId(
						regionalProcessPointGroupDto.getCategoryCode(), regionalProcessPointGroupDto.getSite(), regionalProcessPointGroupDto.getRegionalValue());
				RegionalProcessPointGroup regionalProcessPointGroup = new RegionalProcessPointGroup(regionalProcessPointGroupId); 
				regionalProcessPointGroup.setCreateUser(getModel().getApplicationContext().getUserId().toUpperCase());
				regionalProcessPointGroupList.add(regionalProcessPointGroup);
			}
			
			getModel().createRegionalProcessPointGroups(regionalProcessPointGroupList);
			EventBusUtil.publish(new StatusMessageEvent("Assigned Regional Process Point Groups successfully", StatusMessageEventType.INFO));
		}
	}
	
	/**
	 * This method is used to save Regional Process Point Group
	 * @param event
	 */

	private void saveBtnAction(ActionEvent actionEvent) {
		getView().getSaveBtn().setDisable(true);
		setDeassignButton();
		setAssignButton();
		List<RegionalProcessPointGroupDto> currentlyAssignedProcessPointGroupDtoList = getView().getAssignedProcessPointGroupObjectTablePane().getTable().getItems();
		List<RegionalProcessPointGroupDto> previousAssignedProcessPointGroupDtoList = getModel().findAssignedProcessPointGroup(getView().getCategoryCode(), getView().getSite());
		List<RegionalProcessPointGroupDto> addedProcessPointGroupDtoList = new ArrayList<RegionalProcessPointGroupDto>();
		List<RegionalProcessPointGroup> removedProcessPointGroupList = new ArrayList<RegionalProcessPointGroup>();
		
		if (previousAssignedProcessPointGroupDtoList.isEmpty()) {
			if (!currentlyAssignedProcessPointGroupDtoList.isEmpty()) { //add only
				addedProcessPointGroupDtoList = currentlyAssignedProcessPointGroupDtoList;
			} else {
				return;
			}
		} else {
			for (RegionalProcessPointGroupDto regionalProcessPointGroupDto : previousAssignedProcessPointGroupDtoList) {
				if (!currentlyAssignedProcessPointGroupDtoList.contains(regionalProcessPointGroupDto)) {
					RegionalProcessPointGroupId regionalProcessPointGroupId = new RegionalProcessPointGroupId(
							regionalProcessPointGroupDto.getCategoryCode(), regionalProcessPointGroupDto.getSite(), regionalProcessPointGroupDto.getRegionalValue());
					RegionalProcessPointGroup removedProcessPointGroup = new RegionalProcessPointGroup(regionalProcessPointGroupId);
					removedProcessPointGroupList.add(removedProcessPointGroup);
				}
			}
		
			if (!currentlyAssignedProcessPointGroupDtoList.isEmpty()) { //may need to add some
				for (RegionalProcessPointGroupDto regionalProcessPointGroupDto : currentlyAssignedProcessPointGroupDtoList) {
					if (!previousAssignedProcessPointGroupDtoList.contains(regionalProcessPointGroupDto)) {
						addedProcessPointGroupDtoList.add(regionalProcessPointGroupDto);
					}
				}
			}
		}
	
		if (!addedProcessPointGroupDtoList.isEmpty()) {
			try {
				createRegionalProcessPointGroups(addedProcessPointGroupDtoList);
			} catch (Exception e) {
				handleException("An error occurred while create Regional Process Point Group", "Failed to create Regional Process Point Group", e);
			}
		}

		if (!removedProcessPointGroupList.isEmpty()) {
			//check local impact when delete
			String returnValue = isLocalSiteImpacted(removedProcessPointGroupList, getView().getStage());
			if (returnValue.equals(com.honda.galc.client.utils.QiConstant.NO_LOCAL_SITES_CONFIGURED)) {
				getView().reload();
				return;
			} else if (returnValue.equals(com.honda.galc.client.utils.QiConstant.LOCAL_SITES_IMPACTED)) {
				getView().reload();
				publishErrorMessage("Some Process Point Groups are used at Local Sites and cannot be deassigned.");
				return;
			}
				
			try {
				getModel().deleteRegionalProcessPointGroups(removedProcessPointGroupList);
			
				for (RegionalProcessPointGroup removedProcessPointGroup : removedProcessPointGroupList) {
					// Call to audit log
					AuditLoggerUtil.logAuditInfo(removedProcessPointGroup, null, 
						com.honda.galc.client.utils.QiConstant.SAVE_REASON_FOR_REGIONAL_PROCESS_POINT_GROUP_AUDIT, getView().getScreenName(), getUserId());
				}
			} catch (Exception e) {
				handleException("An error occurred while delete Regional Process Point Group", "Failed to delete Regional Process Point Group", e);
			}
		}

		getView().getAvailableProcessPointGroupObjectTablePane().getTable().getSelectionModel().clearSelection();
		getView().getAssignedProcessPointGroupObjectTablePane().getTable().getSelectionModel().clearSelection();
		EventBusUtil.publish(new StatusMessageEvent("Regional Process Point Group assigned successfully", StatusMessageEventType.INFO));
	}

	/**
	 * This method is event listener for Available and selected repair method table
	 */
	private void addAvailableAndAssignedProcessPointGroupListener() {
		getView().getAvailableProcessPointGroupObjectTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RegionalCode>() {
			public void changed(ObservableValue<? extends RegionalCode> observableValue, RegionalCode oldValue, RegionalCode newValue) {
				if (isFullAccess()) {
					if (null != newValue) {
						Logger.getLogger().check(newValue.getRegionalValueName() + " : Available Regional Process Point Group is selected");
						clearDisplayMessage();
					}
					setAssignButton();
				}
			}
		});

		getView().getAssignedProcessPointGroupObjectTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RegionalProcessPointGroupDto>() {
			public void changed(ObservableValue<? extends RegionalProcessPointGroupDto> observableValue, RegionalProcessPointGroupDto oldValue, RegionalProcessPointGroupDto newValue) {
				if (isFullAccess()) {
					if (null != newValue) {
						Logger.getLogger().check(newValue.getRegionalValueName() + " : Assigned Regional Process Point Group is selected");
						clearDisplayMessage();
					}
					setDeassignButton();
				}
			}
		});
	}
	
	/**
	 * This method is used to enable/disable assign button.
	 */
	private void setAssignButton() {
		if (null != getView().getAvailableProcessPointGroupObjectTablePane().getSelectedItem())
			getView().getRightShiftBtn().setDisable(false);
		else
			getView().getRightShiftBtn().setDisable(true);
	}
	
	/**
	 * This method is used to enable/disable deassign button.
	 */
	private void setDeassignButton() {
		if (null != getView().getAssignedProcessPointGroupObjectTablePane().getSelectedItem() ){
			getView().getLeftShiftBtn().setDisable(false);
		}
		else
			getView().getLeftShiftBtn().setDisable(true);
      }
	
	/**
	 * This method is event listener for categoryCodeComboBox
	 */
	@SuppressWarnings("unchecked")
	private void addCategoryCodeComboBoxListener() {
		getView().getCategoryCodeComboBox().getSelectionModel().selectedItemProperty().addListener(categoryCodeComboBoxChangeListener);
	}

	/**
	 * This method is event listener for siteComboBox
	 */
	@SuppressWarnings("unchecked")
	private void addSiteComboBoxListener(){
		getView().getSiteComboBox().getSelectionModel().selectedItemProperty().addListener(siteComboBoxChangeListener);
	}
}
