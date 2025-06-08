package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.ResponsibleLoadTriggeredModel;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.teamleader.qi.view.ResponsibleLoadTriggeredPanel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiDepartmentDto;
import com.honda.galc.dto.qi.QiPddaResponsibleLoadTriggerDto;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiPddaResponsibleLoadTrigger;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * 
 * <h3>ResponsibleLoadTriggeredController Class description</h3>
 * <p>
 * ResponsibleLoadTriggeredController is used to load data in TableView for user to confirm PDDA Responsibility.
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
public class ResponsibleLoadTriggeredController extends AbstractQiController<ResponsibleLoadTriggeredModel, ResponsibleLoadTriggeredPanel> implements EventHandler<ActionEvent>{

	boolean isRefreshButton=false;
	public ResponsibleLoadTriggeredController(ResponsibleLoadTriggeredModel model, ResponsibleLoadTriggeredPanel view) {
		super(model, view);
	}
	ChangeListener<String> companyComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> value,  String oldValue, String newValue) { 
			if(isRefreshButton){
				return;
			}
			loadPlantComboBox(newValue);
		}
	};
	ChangeListener<String> plantComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> value,  String oldValue, String newValue) {
			if(isRefreshButton){
				return;
			}
			loadProductComboBox(newValue);
		}
	};
	ChangeListener<String> productComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> value,  String oldValue, String newValue) { 
			if(isRefreshButton){
				return;
			}
			loadDepartmentComboBox(newValue);
		}
	};
	ChangeListener<ComboBoxDisplayDto> departmentComboBoxChangeListener = new ChangeListener<ComboBoxDisplayDto>() {
		public void changed(ObservableValue<? extends ComboBoxDisplayDto> value,  ComboBoxDisplayDto oldValue, ComboBoxDisplayDto newValue) {
			if(isRefreshButton){
				return;
			}
			String id = "";
			if(newValue != null)  {;
			   loadPddaResponsibleTriggerData(newValue.getId());
			}
		}
	};
	ChangeListener<QiPddaResponsibleLoadTriggerDto> pddaResponsibleTablePaneChangeListener = new ChangeListener<QiPddaResponsibleLoadTriggerDto>() {
		public void changed(ObservableValue<? extends QiPddaResponsibleLoadTriggerDto> ov,  QiPddaResponsibleLoadTriggerDto old_val, QiPddaResponsibleLoadTriggerDto new_val) { 
			if(isFullAccess())
			addContextMenuItems();
		}
	};
	/*
	 * this method is used to handle the action events
	 */
	public void handle(ActionEvent event) {
		if (event.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) event.getSource();
			handlerForMenuItem(menuItem,event);		
		}
		else if(event.getSource() instanceof LoggedRadioButton) {
			LoggedRadioButton radioButton = (LoggedRadioButton) event.getSource();
			handlerForLoggedRadioButton(radioButton,event);
		}
	}
	/**
	 * This method is used to handle loggedRadioButton action.
	 */
	private void handlerForLoggedRadioButton(LoggedRadioButton radioButton, ActionEvent event) {
		refreshComboBox(radioButton);
	}
	/**
	 * This method is used to handle MenuItem action.
	 */
	private void handlerForMenuItem(MenuItem menuItem,ActionEvent event) {
		setAdminConfirmedValue(event);
	}
	/*
	 * this method is used to update QiPddaResponsibleLoadTrigger
	 */
	private void setAdminConfirmedValue(ActionEvent event) {
		try {
			clearDisplayMessage();
			QiPddaResponsibleLoadTrigger qiPddaResponsibleLoadTrigger= getModel().findPddaResponsibleTriggerDataById(getView().getPddaResponsibilityTablePane().getTable().getSelectionModel().getSelectedItem().getPddaResponsibilityId());
			QiPddaResponsibleLoadTrigger clonedQiPddaResponsibleLoadTrigger=(QiPddaResponsibleLoadTrigger) qiPddaResponsibleLoadTrigger.deepCopy();
			qiPddaResponsibleLoadTrigger.setAdminConfirmedFix((short)1);
			getModel().updatePddaResponsibleLoadtriggerData(qiPddaResponsibleLoadTrigger);
			AuditLoggerUtil.logAuditInfo(clonedQiPddaResponsibleLoadTrigger, qiPddaResponsibleLoadTrigger, "Mark as confirmed",getView().getScreenName() ,getUserId());
			getView().reload();
		} catch (Exception e) {
			handleException("An error occured while update ", "Failed to update status", e);
		}
}
	/*
	 * this method is used to add context menu to QiPddaResponsibleTablePane
	 */
	@Override
	public void addContextMenuItems() {
		ObjectTablePane<QiPddaResponsibleLoadTriggerDto> tablePane = getView().getPddaResponsibilityTablePane();
		String[] menuItems;
		if(null!=getView().getPddaResponsibilityTablePane().getTable().getContextMenu())
		getView().getPddaResponsibilityTablePane().getTable().getContextMenu().getItems().clear();
		if(null!=getView().getPddaResponsibilityTablePane().getTable().getSelectionModel().getSelectedItem()){
	        if(StringUtils.trimToEmpty(getView().getPddaResponsibilityTablePane().getTable().getSelectionModel().getSelectedItem().getAdminConfirmedFix()).equalsIgnoreCase("No")){
			menuItems = new String[] {QiConstant.MARK_AS_CONFIRMED};
			tablePane.createContextMenu(menuItems, this);
		  }
		}
	}

	@Override
	public void initEventHandlers() {
		addCompanyComboboxListener();
		addPlantComboboxListener();
		addProductComboboxListener();
		addDepartmentComboboxListener();
		addPddaResponsibleTableListener();
		if(isFullAccess())
		addContextMenuItems();
	}
	/*
	 * this method is used to load plant combobox values
	 */
	private void loadPlantComboBox(String company) {
		getView().getPlantComboBox().getControl().setItems(FXCollections.observableArrayList(getModel().findAllPlantsByCompany(company,getView().getFixedRadioBtn().isSelected()?(short)0:getView().getConfirmedRadioBtn().isSelected()?(short)1:(short)2)));
	}
	/*
	 * this method is used to load pddaResponsibility table
	 */
	public void loadPddaResponsibleTriggerData(String department) {
		getView().getPddaResponsibilityTablePane().getTable().setItems(FXCollections.observableArrayList(getModel().findPddaResponsibleTriggerData(null!=getView().getCompanyComboBox().getControl().getSelectionModel().getSelectedItem()?StringUtils.trimToEmpty(getView().getCompanyComboBox().getControl().getSelectionModel().getSelectedItem().toString()):"",
				null!=getView().getPlantComboBox().getControl().getSelectionModel().getSelectedItem()?StringUtils.trimToEmpty(getView().getPlantComboBox().getControl().getSelectionModel().getSelectedItem().toString()):"",
						null!=getView().getProductComboBox().getControl().getSelectionModel().getSelectedItem()?StringUtils.trimToEmpty(getView().getProductComboBox().getControl().getSelectionModel().getSelectedItem().toString()):"",
								department,getView().getFixedRadioBtn().isSelected()?(short)0:getView().getConfirmedRadioBtn().isSelected()?(short)1:(short)2)));
	}
	/*
	 * this method is used to load department combobox values
	 */
	private void loadDepartmentComboBox(String product) {
		String company = getView().getCompanyComboBoxSelectedId();
		String plant = getView().getPlantComboBoxSelectedId();
		List<QiDepartmentDto> allQiDept = getModel().findAllQiDepartmentByCompanyAndPlantAndProduct(
				StringUtils.trimToEmpty(company),
				StringUtils.trimToEmpty(plant),
				product, getView().getFixedRadioBtn().isSelected() ? (short) 0
						: getView().getConfirmedRadioBtn().isSelected() ? (short) 1 : (short) 2);

		List<ComboBoxDisplayDto> dtoList = new ArrayList<ComboBoxDisplayDto>();
		if(allQiDept != null)  {
			for(QiDepartmentDto qiDept : allQiDept)  {
				ComboBoxDisplayDto dto = ComboBoxDisplayDto.getInstance(qiDept);
				dtoList.add(dto);
			}
		}
		getView().getDepartmentComboBox().getControl()
				.setItems(FXCollections.observableArrayList(dtoList));
	}
	/*
	 * this method is used to load product combobox values
	 */
	private void loadProductComboBox(String plant) {
		getView().getProductComboBox().getControl().setItems(FXCollections.observableArrayList(getModel().findAllProductsByCompanyAndPlant(plant,null!=getView().getCompanyComboBox().getControl().getSelectionModel().getSelectedItem()?StringUtils.trimToEmpty(getView().getCompanyComboBox().getControl().getSelectionModel().getSelectedItem().toString()):"",
				getView().getFixedRadioBtn().isSelected()?(short)0:getView().getConfirmedRadioBtn().isSelected()?(short)1:(short)2)));
	}
	/**
	 * This method is event listener for plantComboBox
	 */
	private void addPlantComboboxListener() {
		getView().getPlantComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(plantComboBoxChangeListener);
	}
	/**
	 * This method is event listener for companyComboBox
	 */
	private void addCompanyComboboxListener() {
		getView().getCompanyComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(companyComboBoxChangeListener);
	}
	
	/**
	 * This method is event listener for productComboBox
	 */
	private void addProductComboboxListener() {
		getView().getProductComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(productComboBoxChangeListener);
	}
	/**
	 * This method is event listener for departmentComboBox
	 */
	private void addDepartmentComboboxListener() {
		getView().getDepartmentComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(departmentComboBoxChangeListener);
	}
	/**
	 * This method is event listener for PddaResponsibleTablePane
	 */
	private void addPddaResponsibleTableListener() {
		getView().getPddaResponsibilityTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(pddaResponsibleTablePaneChangeListener);		
	}
	
	private void loadCompanyComboBox(LoggedRadioButton radioButton) {
		if (QiConstant.ALL.equalsIgnoreCase(radioButton.getId())) {
			getView().getCompanyComboBox().getControl().setItems(FXCollections.observableArrayList(getModel().findAllRespCompanyByAdminConfirmedFix((short)2)));
		} else if (QiConstant.FIXED.equalsIgnoreCase(radioButton.getId())) {
			getView().getCompanyComboBox().getControl().setItems(FXCollections.observableArrayList(getModel().findAllRespCompanyByAdminConfirmedFix((short)0)));
		} else if (QiConstant.CONFIRMED.equalsIgnoreCase(radioButton.getId())) {
			getView().getCompanyComboBox().getControl().setItems(FXCollections.observableArrayList(getModel().findAllRespCompanyByAdminConfirmedFix((short)1)));
		}
	}
	
	private void  refreshComboBox(LoggedRadioButton radioButton){
		isRefreshButton = true;
		String plant = null;
		String company = null;
		String productType = null;
		String department=null;
		ComboBoxDisplayDto comboBoxDto = getView().getDepartmentComboBox().getControl().getSelectionModel().getSelectedItem();
		if(getView().getCompanyComboBox().getControl().getSelectionModel().getSelectedItem()!=null)
			company = getView().getCompanyComboBox().getControl().getSelectionModel().getSelectedItem().toString();
		if(getView().getPlantComboBox().getControl().getSelectionModel().getSelectedItem()!=null)
			plant=getView().getPlantComboBox().getControl().getSelectionModel().getSelectedItem();
		if(getView().getProductComboBox().getControl().getSelectionModel().getSelectedItem()!=null)
			productType = getView().getProductComboBox().getControl().getSelectionModel().getSelectedItem().toString();
		if(getView().getDepartmentComboBox().getControl().getSelectionModel().getSelectedItem()!=null)
			department = getView().getDepartmentComboBox().getControl().getSelectionModel().getSelectedItem().toString();
		getView().getCompanyComboBox().getControl().getItems().clear();
		getView().getPlantComboBox().getControl().getItems().clear();
		getView().getProductComboBox().getControl().getItems().clear();
		getView().getDepartmentComboBox().getControl().getItems().clear();
		loadCompanyComboBox(radioButton);
		if(company!=null){
			getView().getCompanyComboBox().getControl().getSelectionModel().select(company);
			loadPlantComboBox(company);
			if(plant!=null){
				getView().getPlantComboBox().getControl().getSelectionModel().select(plant);
				loadProductComboBox(plant);
				if(productType!=null){
					getView().getProductComboBox().getControl().getSelectionModel().select(productType);
					loadDepartmentComboBox(productType);
					if(department!=null){
						getView().getDepartmentComboBox().getControl().getSelectionModel().select(comboBoxDto);
						getView().reload();
					}
				}
			}
		}
		isRefreshButton = false;
	}
	
}
