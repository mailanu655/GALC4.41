package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.util.AuditLoggerUtil;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.teamleader.qi.model.QicsStationRepairMethodMaintenanceModel;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.teamleader.qi.view.QicsStationRepairMethodMaintPanel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.qi.QiRepairMethod;
import com.honda.galc.entity.qi.QiStationRepairMethod;
import com.honda.galc.entity.qi.QiStationRepairMethodId;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QicsStationRepairMethodMaintController</code> is the controller class for Qics Station Repair Method Maintenance Panel.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class QicsStationRepairMethodMaintController extends AbstractQiController<QicsStationRepairMethodMaintenanceModel, QicsStationRepairMethodMaintPanel> implements EventHandler<ActionEvent> {

	boolean isRefresh=false;
	
	public QicsStationRepairMethodMaintController(QicsStationRepairMethodMaintenanceModel model,QicsStationRepairMethodMaintPanel view) {
		super(model, view);
	}


	ChangeListener<String> plantComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,
				String oldValue, String newValue) {
			if(!isRefresh){
			Logger.getLogger().check(newValue+" plant code selected");
			clearDisplayMessage();
			getView().clearDivisionComboBox();
			disableAvailableAndSelectedRepairMethodTablePane();
			loadDivisionComboBox(newValue);
			}
		}
	};
	
	ChangeListener<ComboBoxDisplayDto> divisionComboBoxChangeListener = new ChangeListener<ComboBoxDisplayDto>() {
		public void changed(ObservableValue<? extends ComboBoxDisplayDto> ov,  ComboBoxDisplayDto oldValue, ComboBoxDisplayDto newValue) { 
			if(!isRefresh){
				Logger.getLogger().check(newValue+" division selected");
				clearDisplayMessage();
				getView().getMethodFilterTextField().clear();
				getView().getQicsStationListView().getItems().clear();
				getView().getAvailableRepairMethodTablePane().getTable().getItems().clear();
				getView().getSelectedRepairMethodTablePane().getTable().getItems().clear();
				if(getView().getShowRepairMehodForDivisionChkBox().isSelected()){
					getView().getShowRepairMehodForDivisionChkBox().setIndeterminate(false);
					getView().getShowRepairMehodForDivisionChkBox().setSelected(false);
				}
				getView().reload();
				getView().getLeftShiftBtn().setDisable(true);
				getView().getRightShiftBtn().setDisable(true);
				getView().getSaveBtn().setDisable(true);
				disableAvailableAndSelectedRepairMethodTablePane();
				if(newValue != null)  {
				   loadQicsStationListView(newValue.getId());
				}
			}
		} 
	};
	
	ChangeListener<String> qicsStationListViewChangeListener = new ChangeListener<String>() {

		public void changed(ObservableValue<? extends String> arg0, String arg1,
				String arg2) {
			if(!isRefresh){
			clearDisplayMessage();
			getView().getMethodFilterTextField().clear();
			enableOrDisableLeftRightShiftAndSaveBtn(true);
			if (null == getView().getQicsStationListView().getSelectionModel().getSelectedItem()) {
				disableAvailableAndSelectedRepairMethodTablePane();
			}
			if (null != getView().getQicsStationListView().getSelectionModel().getSelectedItem()) {
				getView().getAvailableRepairMethodTablePane().setDisable(false);
				getView().getSelectedRepairMethodTablePane().setDisable(false);
				getView().getMethodFilterTextField().setDisable(false);
				getView().getShowRepairMehodForDivisionChkBox().setDisable(false);
				Logger.getLogger().check(getView().getQicsStationListView().getSelectionModel().getSelectedItem().toString()+" station selected");
			}
			if(getView().getShowRepairMehodForDivisionChkBox().isSelected()){
				getView().getShowRepairMehodForDivisionChkBox().setIndeterminate(false);
				getView().getShowRepairMehodForDivisionChkBox().setSelected(false);
			}
			getView().getSelectedRepairMethodTablePane().getTable().getItems().clear();
			getView().getAvailableRepairMethodTablePane().getTable().getItems().clear();
			setAvailableAndSelectedRepairMethodTablePaneData();
			}

		}

	};

	/**
	 * This is an implemented method of EventHandler interface. Called whenever an ActionEvent is triggered.
	 * Selecting Left shift,right shift,save Btn and show repair mehod for division ChkBox is an ActionEvent. So respective method is called based on which action event is triggered.
	 */
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			if (actionEvent.getSource().equals(getView().getRightShiftBtn()))shiftRepairMethodsAvailableToSelectedTablePaneBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getView().getLeftShiftBtn()))shiftRepairMethodsSelectedToAvailableTablePaneBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getView().getSaveBtn()))saveBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getView().getRefreshBtn())){
				List<QiRepairMethod> selectedRepairMethodList =new ArrayList<QiRepairMethod>();
				selectedRepairMethodList.addAll(getView().getAvailableRepairMethodTablePane().getTable().getSelectionModel().getSelectedItems());
				refreshBtnAction(selectedRepairMethodList);
				EventBusUtil.publish(new StatusMessageEvent("Refreshed successfully", StatusMessageEventType.INFO));
			}
		}
		else if (actionEvent.getSource() instanceof CheckBox){
			CheckBox cb=(CheckBox)actionEvent.getSource();
			if ("showRepairMethodForDivisionChkBox".equalsIgnoreCase(cb.getId()))showRepairMethodForDivisionChkBoxListener();
		}
		else  if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			if(getView().getMethodFilterTextField().isFocused())
				getView().reload(StringUtils.trim(getView().getMethodFilterTextField().getText()));
		}

	}

	/**
	 * This method is to refresh the data
	 */

	public void refreshBtnAction(List<QiRepairMethod> selectedRepairMethodList) {
		isRefresh=true;
		
		String div=null;
		String filter =getView().getMethodFilterTextField().getText();
	    List<QiRepairMethod> selectedAssignedRepairMethodList = new ArrayList<QiRepairMethod>();
		Boolean showRepairMethodForDivisionChkBoxStatus = getView().getShowRepairMehodForDivisionChkBox().isSelected();
		String selectedEntryStation = getView().getQicsStationListView().getSelectionModel().getSelectedItem();
		selectedAssignedRepairMethodList.addAll(getView().getSelectedRepairMethodTablePane().getTable().getSelectionModel().getSelectedItems());

		if(null != getView().getDivisionComboBoxSelectedItem()){
			div = getView().getDivisionComboBoxSelectedId();
		}
		refreshPlant();                   
		refreshDivision(div,selectedEntryStation,showRepairMethodForDivisionChkBoxStatus,filter,selectedRepairMethodList,selectedAssignedRepairMethodList);
		
		isRefresh=false;
		
	}



	/**
	 * @param div
	 * @param filter
	 * @param showRepairMethodForDivisionChkBoxStatus
	 * @param selectedEntryStation
	 */
	@SuppressWarnings("unchecked")
	private void refreshDivision(String div,String selectedEntryStation,Boolean showRepairMethodForDivisionChkBoxStatus, String filter, List<QiRepairMethod> selectedRepairMethodList, List<QiRepairMethod> selectedAssignedRepairMethodList) {
		String plant;
		if (null != div) {
			plant = getView().getPlantComboBox().getSelectionModel().getSelectedItem().toString();
			if (plant != null) {
				getView().clearDivisionComboBox();
				List<Division> divList = getModel().findById(getView().getSiteValueLabel().getText().trim(), plant);
				for (Division divObj : divList) {
					ComboBoxDisplayDto dto = ComboBoxDisplayDto.getInstance(divObj);
					getView().getDivisionComboBox().getItems().add(dto);
					if((divObj.getDivisionId()).equals(div))  {
						getView().getDivisionComboBox().setValue(dto);
					}
				}
			}    
			if(selectedEntryStation == null)  {
				getView().reload();
			}else{
				getView().getQicsStationListView().getSelectionModel().select(selectedEntryStation);
				getView().getShowRepairMehodForDivisionChkBox().setSelected(showRepairMethodForDivisionChkBoxStatus);
				showRepairMethodForDivisionChkBoxListener();
				List<QiRepairMethod> allRepairMethodByQicsStationList = getModel().findAllRepairMethodByQicsStation(getQicsStationIdFromDescription());
				getView().getMethodFilterTextField().setText(filter);
				getView().getSelectedRepairMethodTablePane().getTable().getItems().clear();
				getView().getSelectedRepairMethodTablePane().getTable().getItems().addAll(allRepairMethodByQicsStationList);
				getView().reload(filter);
				for(QiRepairMethod selectedAvailableQiRepairMethod : selectedRepairMethodList)
				getView().getAvailableRepairMethodTablePane().getTable().getSelectionModel().select(selectedAvailableQiRepairMethod);
				for(QiRepairMethod selectedAssignedQiRepairMethod : selectedAssignedRepairMethodList)
				getView().getSelectedRepairMethodTablePane().getTable().getSelectionModel().select(selectedAssignedQiRepairMethod);
			}
		}
	}


	/**
	 * This method is to refresh the plant
	 */
	@SuppressWarnings("unchecked")
	private void refreshPlant() {
		String plant;
		List<Plant> plantList = getModel().findAllBySite(getView().getSiteValueLabel().getText().trim());
		if (null != getView().getPlantComboBox().getSelectionModel().getSelectedItem()) {
			plant = getView().getPlantComboBox().getSelectionModel().getSelectedItem().toString();
			getView().getPlantComboBox().getItems().clear();
			for (Plant plantObj : plantList) {
				getView().getPlantComboBox().getItems().add(plantObj.getPlantName());
				if((plantObj.getPlantName()).equals(plant))
					getView().getPlantComboBox().setValue(plant);
			}
		}
		else{
			getView().getPlantComboBox().getItems().clear();
			for (Plant plantObj : plantList) {
				getView().getPlantComboBox().getItems().add(plantObj.getPlantName());
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
		addPlantComboBoxListener();
		addDivisionComboBoxListener();
		addQicsStationListViewListener();
		addAvailableAndSelectedRepairMethodTableListener();
		disableAvailableAndSelectedRepairMethodTablePane();
	}

	/**
	 * This method is used to disable Available and selected Repair method table pane
	 * @param 
	 */
	private void disableAvailableAndSelectedRepairMethodTablePane() {
		if (null == getView().getQicsStationListView().getSelectionModel().getSelectedItem() ){
			getView().getSelectedRepairMethodTablePane().setDisable(true);
			getView().getAvailableRepairMethodTablePane().setDisable(true);
			getView().getMethodFilterTextField().setDisable(true);
			getView().getShowRepairMehodForDivisionChkBox().setDisable(true);
		}
	}
	
	/**
	 * This method is used shift repair methods Available to Selected Repair Method Table Pane
	 * @param event
	 */
	private void shiftRepairMethodsAvailableToSelectedTablePaneBtnAction(ActionEvent actionEvent) {
		List<QiRepairMethod> availableRepairMethodlist = getView().getAvailableRepairMethodTablePane().getSelectedItems();
		if(!availableRepairMethodlist.isEmpty()){
			List<QiRepairMethod> addRemoveAvailableRepairMethodList = new ArrayList<QiRepairMethod>();
			for (QiRepairMethod qiRepairMethod : availableRepairMethodlist) {
				if(qiRepairMethod !=null){
					addRemoveAvailableRepairMethodList.add(qiRepairMethod);
				}
			}
			getView().getSelectedRepairMethodTablePane().getTable().getItems().addAll(addRemoveAvailableRepairMethodList);
			getView().getAvailableRepairMethodTablePane().getTable().getItems().removeAll(addRemoveAvailableRepairMethodList);
			if(availableRepairMethodlist.size() > 1)
				getView().getAvailableRepairMethodTablePane().getTable().getSelectionModel().clearSelection();
		}


		getView().getSaveBtn().setDisable(false);
		if (!isFullAccess()) {
			getView().getSaveBtn().setDisable(true);}

	}

	/**
	 * This method is used shift repair methods Selected to Available Repair Method Table Pane
	 * @param event
	 */
	private void shiftRepairMethodsSelectedToAvailableTablePaneBtnAction(ActionEvent actionEvent) {
		List<QiRepairMethod> selectedRepairMethodList = getView().getSelectedRepairMethodTablePane().getSelectedItems();
		if(!selectedRepairMethodList.isEmpty()){
			List<QiRepairMethod> addRemoveSelectedRepairMethodList = new ArrayList<QiRepairMethod>();
			for (QiRepairMethod qiRepairMethod : selectedRepairMethodList) {
				if(qiRepairMethod !=null){
					addRemoveSelectedRepairMethodList.add(qiRepairMethod);
				}
			}
			getView().getAvailableRepairMethodTablePane().getTable().getItems().addAll(addRemoveSelectedRepairMethodList);
			getView().getSelectedRepairMethodTablePane().getTable().getItems().removeAll(addRemoveSelectedRepairMethodList);
			if(selectedRepairMethodList.size() > 1)
				getView().getSelectedRepairMethodTablePane().getTable().getSelectionModel().clearSelection();
		}

		getView().getSaveBtn().setDisable(false);
		if (!isFullAccess()) {
			getView().getSaveBtn().setDisable(true);}

	}

	
	/**
	 * This method is used to save repair method into repair method process point table
	 * @param repair method list
	 */
	private void createQiStationRepairMethods(List<QiRepairMethod> selectedRepairMethodlist){
		QiStationRepairMethod qiStationRepairMethod =new QiStationRepairMethod();  
		QiStationRepairMethodId qiStationRepairMethodId =new QiStationRepairMethodId();
		
		if(selectedRepairMethodlist.isEmpty())
			EventBusUtil.publish(new StatusMessageEvent("Deassignment repair method to Qics station successfully", StatusMessageEventType.INFO));
		else{
			for (QiRepairMethod qiRepairMethod : selectedRepairMethodlist) {
				qiStationRepairMethodId.setProcessPointId(getQicsStationIdFromDescription());
				qiStationRepairMethodId.setRepairMethod(qiRepairMethod.getRepairMethod());
				qiStationRepairMethod.setId(qiStationRepairMethodId);
				qiStationRepairMethod.setCreateUser(getModel().getUserId());
				getModel().createQiStationRepairMethod(qiStationRepairMethod);
			}
			EventBusUtil.publish(new StatusMessageEvent("Assigned repair method to Qics station successfully", StatusMessageEventType.INFO));
	}
	}
	/**
	 * This method is used to save repair method into repair method process point table
	 * @param event
	 */

	private void saveBtnAction(ActionEvent actionEvent) {
		getView().getSaveBtn().setDisable(true);
		setDeassignButton();
		setAssignButton();
		List<QiRepairMethod> selectedRepairMethodlist = getView().getSelectedRepairMethodTablePane().getTable().getItems();
		List<QiRepairMethod> allRepairMethodByQicsStationList = getModel().findAllRepairMethodByQicsStation(getQicsStationIdFromDescription());
		if (allRepairMethodByQicsStationList.isEmpty()) {
			try {
				createQiStationRepairMethods(selectedRepairMethodlist);
			} catch (Exception e) {
				handleException("An error occurred while deassignment repair method to QICS station", "Failed to deassign repair method to QICS station",e);
			}
		} else if (!allRepairMethodByQicsStationList.isEmpty()) {
			List<QiStationRepairMethod> removedQiStationRepairMethodList =new ArrayList<QiStationRepairMethod>();
			List<QiRepairMethod> addRepairMethodQicsStationList = new ArrayList<QiRepairMethod>();
			for (QiRepairMethod qiRepairMethodObj : allRepairMethodByQicsStationList) {
				if (!selectedRepairMethodlist.contains(qiRepairMethodObj)) {
					QiStationRepairMethod removedQiStationRepairMethodObj = new QiStationRepairMethod();
					QiStationRepairMethodId qiStationRepairMethodId =new QiStationRepairMethodId();
					qiStationRepairMethodId.setProcessPointId(getQicsStationIdFromDescription());
					qiStationRepairMethodId.setRepairMethod(qiRepairMethodObj.getRepairMethod());
					removedQiStationRepairMethodObj.setId(qiStationRepairMethodId);
					removedQiStationRepairMethodList.add(removedQiStationRepairMethodObj);
					// Call to audit log
					AuditLoggerUtil.logAuditInfo(removedQiStationRepairMethodObj, null, QiConstant.SAVE_REASON_FOR_QICS_STN_REPAIR_METHOD_AUDIT, getView().getScreenName(), getUserId());
				}
			}

			try {
				if(removedQiStationRepairMethodList.isEmpty())
					EventBusUtil.publish(new StatusMessageEvent("Assigned repair method to Qics station successfully", StatusMessageEventType.INFO));
				else{
				getModel().deleteQiStationRepairMethods(removedQiStationRepairMethodList);
				EventBusUtil.publish(new StatusMessageEvent("Deassignment repair method to Qics station successfully", StatusMessageEventType.INFO));
				}
			} catch (Exception e) {
				handleException("An error occurred while deassignment repair method to QICS station", "Failed to deassign repair method to QICS station",e);
			}

			for (QiRepairMethod qiRepairMethodObj : selectedRepairMethodlist) {
				if (!allRepairMethodByQicsStationList.contains(qiRepairMethodObj)) {
					addRepairMethodQicsStationList.add(qiRepairMethodObj);
				}
			}

			if (!addRepairMethodQicsStationList.isEmpty()) {
				try {
					createQiStationRepairMethods(addRepairMethodQicsStationList);
				} catch (Exception e) {
					handleException("An error occurred while assigning Repair method to QICS station","Failed to assign Repair method to QICS station",e);
				}
			}
		}
		getView().getAvailableRepairMethodTablePane().getTable().getSelectionModel().clearSelection();
		getView().getSelectedRepairMethodTablePane().getTable().getSelectionModel().clearSelection();

	}

	/**
	 * This method is event listener for qicsStationListView
	 */
	private void addQicsStationListViewListener() {
		getView().getQicsStationListView().getSelectionModel().selectedItemProperty().addListener(qicsStationListViewChangeListener);


	}

	/**
	 * This method is event listener for Available and selected repair method table
	 */
	private void addAvailableAndSelectedRepairMethodTableListener() {
		getView().getAvailableRepairMethodTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiRepairMethod>() {
					public void changed(ObservableValue<? extends QiRepairMethod> observableValue, QiRepairMethod oldValue,QiRepairMethod newValue) {
						if(isFullAccess()){
							if(null != newValue) {
								Logger.getLogger().check(newValue.getRepairMethod()+" : Available Repair Method is selected");
								clearDisplayMessage();
							}
							setAssignButton();
					   }
                    }

				});

		getView().getSelectedRepairMethodTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiRepairMethod>() {
					public void changed(ObservableValue<? extends QiRepairMethod> observableValue, QiRepairMethod oldValue,QiRepairMethod newValue) {
						if(isFullAccess()){
							if(null != newValue) {
								 Logger.getLogger().check(newValue.getRepairMethod()+" : Selected Repair Method is selected");
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
		if (null != getView().getAvailableRepairMethodTablePane().getSelectedItem())
			getView().getRightShiftBtn().setDisable(false);
		else
			getView().getRightShiftBtn().setDisable(true);
	}
	/**
	 * This method is used to enable/disable deassign button.
	 */
	private void setDeassignButton() {
		if (null != getView().getSelectedRepairMethodTablePane().getSelectedItem() ){
			getView().getLeftShiftBtn().setDisable(false);
		}
		else
			getView().getLeftShiftBtn().setDisable(true);
      }
	
	/**
	 * This method is used to Enable or Disable Left,Right shift and Save button base on flag
	 * @param flag
	 * @return
	 */
	private void enableOrDisableLeftRightShiftAndSaveBtn(Boolean flag) {
		getView().getLeftShiftBtn().setDisable(flag);
		getView().getRightShiftBtn().setDisable(flag);
		getView().getSaveBtn().setDisable(flag);

	}
	/**
	 * This method is used to set Available and Selected repair method table pane data  
	 */
	@SuppressWarnings("unchecked")
	private void setAvailableAndSelectedRepairMethodTablePaneData(){
		List<QiRepairMethod> allRepairMethodList = getModel().findAllActiveRepairMethod();
		List<QiRepairMethod> allRepairMethodByQicsStationList = getModel().findAllRepairMethodByQicsStation(getQicsStationIdFromDescription());

		if(allRepairMethodByQicsStationList.isEmpty()){
			getView().getAvailableRepairMethodTablePane().getTable().getItems().addAll(allRepairMethodList);
		}
		else if(!allRepairMethodByQicsStationList.isEmpty()){
			getView().getSelectedRepairMethodTablePane().getTable().getItems().addAll(allRepairMethodByQicsStationList);
			List<QiRepairMethod> subtractedAllRepairMethodList = ListUtils.subtract(allRepairMethodList,allRepairMethodByQicsStationList);
			getView().getAvailableRepairMethodTablePane().getTable().getItems().addAll(subtractedAllRepairMethodList);
		}

	}
	/**
	 * This method is event listener for showRepairMethodForDivisionChkBox
	 */
	@SuppressWarnings("unchecked")
	private void showRepairMethodForDivisionChkBoxListener(){
		clearDisplayMessage();
		getView().getMethodFilterTextField().clear();
		if(getView().getShowRepairMehodForDivisionChkBox().isSelected()){
			String divisionName = (String) getView().getDivisionComboBoxSelectedId();
			List<QiRepairMethod> assignedRepairMethodList = getModel().findAllAssignedRepairMethodByDivision(divisionName);
			getView().getAvailableRepairMethodTablePane().getTable().getItems().clear();
			getView().getAvailableRepairMethodTablePane().getTable().getItems().addAll(assignedRepairMethodList);

			List<QiRepairMethod> selectedRepairMethodList = getView().getSelectedRepairMethodTablePane().getTable().getItems();
			if(selectedRepairMethodList.size() != 0){
				List<QiRepairMethod> subtractedAllRepairMethodList = ListUtils.subtract(assignedRepairMethodList,selectedRepairMethodList);
				getView().getAvailableRepairMethodTablePane().getTable().getItems().clear();
				getView().getAvailableRepairMethodTablePane().getTable().getItems().addAll(subtractedAllRepairMethodList);
			}
		}
		else if(!getView().getShowRepairMehodForDivisionChkBox().isSelected()){
			getView().getSelectedRepairMethodTablePane().getTable().getItems().clear();
			getView().getAvailableRepairMethodTablePane().getTable().getItems().clear();
			setAvailableAndSelectedRepairMethodTablePaneData();
		}
	}

	/**
	 * This method is event listener for plantComboBox
	 */
	@SuppressWarnings("unchecked")
	private void addPlantComboBoxListener() {
		getView().getPlantComboBox().getSelectionModel().selectedItemProperty().addListener(plantComboBoxChangeListener);
	}

	/**
	 * This method is event listener for divisionComboBox
	 */
	@SuppressWarnings("unchecked")
	private void addDivisionComboBoxListener(){
		getView().getDivisionComboBox().getSelectionModel().selectedItemProperty().addListener(divisionComboBoxChangeListener);
	}

	/**
	 * This method is used to load Division ComboBox based on Plant
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void loadDivisionComboBox(String plantName) {
		String siteName = getView().getSiteValueLabel().getText();
		for (Division divisionObj : getModel().findById(siteName, plantName)) {
			ComboBoxDisplayDto dto = ComboBoxDisplayDto.getInstance(divisionObj);
			getView().getDivisionComboBox().getItems().add(dto);
		}
	}	

	/**
	 * This method is used to load Qics station list view based on division 
	 * @param divisionId
	 */
	private void loadQicsStationListView(String divisionId){
		for (ProcessPoint processPointObj : getModel().findQicsStationByApplicationComponentDivision(divisionId)) {
			String qicsStationDesc = processPointObj.getProcessPointId() +"  ("+processPointObj.getProcessPointDescription() +")";
			getView().getQicsStationListView().getItems().add(qicsStationDesc);
		}

	}

	/**
	 * This method is used to split QicsStation from string QicsStationDescription 
	 * return QicsStation
	 */
	private String getQicsStationIdFromDescription(){
		String selectedQicsStationWithDesc ="";
		if(getView().getQicsStationListView().getSelectionModel().getSelectedItem()!=null){
			selectedQicsStationWithDesc =getView().getQicsStationListView().getSelectionModel().getSelectedItem();
		}
		String[] selectedQicsStation = selectedQicsStationWithDesc.split(" ", 2);
		return  selectedQicsStation[0];
	}

}
