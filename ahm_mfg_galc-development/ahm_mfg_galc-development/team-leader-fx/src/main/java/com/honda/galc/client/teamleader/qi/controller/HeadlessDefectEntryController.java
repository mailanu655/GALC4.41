package com.honda.galc.client.teamleader.qi.controller;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.HeadlessDefectEntryModel;
import com.honda.galc.client.teamleader.qi.view.HeadlessDefectEntryDialog;
import com.honda.galc.client.teamleader.qi.view.HeadlessDefectEntryPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.qi.QiExternalSystemInfoDao;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiExternalSystemInfo;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.AuditLoggerUtil;
/**
 * 
 * <h3>HeadlessDefectEntryController Class description</h3>
 * <p>
 * HeadlessDefectEntryController is used to load data in TableViews and perform the action on Create , Update and Delete etc.
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
public class HeadlessDefectEntryController extends AbstractQiController<HeadlessDefectEntryModel,HeadlessDefectEntryPanel> implements EventHandler<ActionEvent>{
	
	private boolean isRefreshButton=false;
	
	public HeadlessDefectEntryController(HeadlessDefectEntryModel model,HeadlessDefectEntryPanel view) {
		super(model, view);
		
	}
	
	@Override
	public void initEventHandlers() {
			addComboBoxListener();
			addMappingTableRowListener();
			//Add context menu
			if(isFullAccess()){
				addExternalDefectTableListener();
			}
	}

	
	/**
	 * This method adds table listener.
	 */
	private void addExternalDefectTableListener() {
		clearDisplayMessage();
		getView().getExternalSystemDefectTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DefectMapDto>() {
			public void changed(
					ObservableValue<? extends DefectMapDto> arg0,DefectMapDto arg1, DefectMapDto arg2) {
				addContextMenuItems();
			}
		});
	}
	
	/**
	 * This method adds table listener.
	 */
	private void addMappingTableRowListener() {

		clearDisplayMessage();
		getView().getExternalSystemDefectTablePane().getTable().setRowFactory(new Callback<TableView<DefectMapDto>, TableRow<DefectMapDto>>() {
			@Override
			public TableRow<DefectMapDto> call(TableView<DefectMapDto> tableView) {
				final TableRow<DefectMapDto> row = new TableRow<DefectMapDto>() {
					@Override
					protected void updateItem(DefectMapDto mapping, boolean empty) {
						if(mapping != null &&
								(mapping.getLocalDefectCombinationId() <= 0
								|| StringUtils.isEmpty(mapping.getFullPartDesc()) || mapping.getFullPartDesc().contains("NOT FOUND LDC"))) {
								setStyle("-fx-text-background-color: red ;");
						}
						else {
							setStyle("-fx-text-background-color: black ;");
					}
						super.updateItem(mapping, empty);
					}
				};
				return row;
			}
		});		
	}
	
	/**
	 * This method adds All combo box listeners.
	 */
	private void addComboBoxListener() {
		addPlantComboBoxListener();
		addProductTypeComboBoxListener();
		addExternalSystemComboBoxListener();
		addEntryModelComboBoxListener();
		addEntryScreenComboBoxListener();
	}
	

	/**
	 * This method adds the plant combo box listener.
	 */
	private void addPlantComboBoxListener() {
		getView().getPlantComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				if(isRefreshButton){
					return;
				}
				removeContextMenuItems();
				loadProductTypeComboBox(new_val);
			} 
		});
	}

	/**
	 * This method loads product type combo box.
	 * @param plant 
	 */
	private void loadProductTypeComboBox(String plant) {
		List<String> productTypeList = getModel().findAllProductTypes();
		getView().getProductTypeComboBox().getItems().clear();
		getView().getExternalSystemComboBox().getItems().clear();
		getView().getEntryModelComboBox().getItems().clear();
		getView().getEntryScreenComboBox().getItems().clear();
		getView().getProductTypeComboBox().setPromptText("Select");
		if(productTypeList!=null )
			getView().getProductTypeComboBox().getItems().addAll(productTypeList);
	}

	/**
	 * This method adds the product type combo box listener.
	 */
	private void addProductTypeComboBoxListener() {
		getView().getProductTypeComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
					if(isRefreshButton){
						return;
					}
					removeContextMenuItems();
					loadExternalSystemComboBox(new_val);
				}
			});
	}
	

	/**
	 * This method load entry model combo box.
	 * @param productType 
	 */
	private void loadExternalSystemComboBox(String productType) {
		getView().getExternalSystemComboBox().getItems().clear();
		getView().getEntryModelComboBox().getItems().clear();
		getView().getEntryScreenComboBox().getItems().clear();
		getView().getExternalSystemComboBox().setPromptText("Select");
		List<QiExternalSystemInfo> extSysList = ServiceFactory.getDao(QiExternalSystemInfoDao.class).findAll();
		if(extSysList != null && !extSysList.isEmpty())  {
			for(QiExternalSystemInfo extSys:extSysList){
				getView().getExternalSystemComboBox().getItems().add(extSys.getExternalSystemName());
			}
		}
	}
	
	/**
	 * This method adds the External System combo box listener.
	 */
	private void addExternalSystemComboBoxListener() {
		getView().getExternalSystemComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				if(isRefreshButton){
					return;
				}
				removeContextMenuItems();
				loadEntryModelComboBox(new_val);
			} 
		});
	}

	
	/**
	 *This method Loads entry model combo box.
	 * @param productType 
	 */
	
	private void loadEntryModelComboBox(String externalSystem) {
		String plant = getView().getPlantComboBox().getSelectionModel().getSelectedItem() == null ? StringUtils.EMPTY : getView().getPlantComboBox().getSelectionModel().getSelectedItem().toString();
		String productType = getView().getProductTypeComboBox().getSelectionModel().getSelectedItem() == null ? StringUtils.EMPTY : getView().getProductTypeComboBox().getSelectionModel().getSelectedItem().toString();
		getView().getEntryModelComboBox().getItems().clear();
		getView().getEntryScreenComboBox().getItems().clear();
		getView().getEntryModelComboBox().setPromptText("Select");
		List<String> entryModelList = getModel().findAllByPlantAndProductType(plant, productType);
		if(entryModelList!=null)
			getView().getEntryModelComboBox().getItems().addAll(entryModelList);
	}

	/**
	 * This method adds the entry model combo box listener.
	 */
	private void addEntryModelComboBoxListener() {
		getView().getEntryModelComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				if(isRefreshButton){
					return;
				}
				removeContextMenuItems();
				loadEntryScreenComboBox(new_val);
			} 
		});
	}

	/**
	 *This Method Loads entry screen combo box.
	 * @param entryModel 
	 */
	private void loadEntryScreenComboBox(String entryModel) {
		String plant = getView().getPlantComboBox().getSelectionModel().getSelectedItem() == null ? StringUtils.EMPTY: getView().getPlantComboBox().getSelectionModel().getSelectedItem().toString();
		String productType = getView().getProductTypeComboBox().getSelectionModel().getSelectedItem() == null ? StringUtils.EMPTY : getView().getProductTypeComboBox().getSelectionModel().getSelectedItem().toString();
		getView().getEntryScreenComboBox().getItems().clear();
		getView().getEntryScreenComboBox().setPromptText("Select");
		List<String> entryScreenList = getModel().findAllByPlantProductTypeAndEntryModel(plant, productType, entryModel);
		if(entryScreenList!=null)
			getView().getEntryScreenComboBox().getItems().addAll(entryScreenList);
	}

	/**
	 * This method adds the entry screen combo box listener.
	 */
	private void addEntryScreenComboBoxListener() {
		getView().getEntryScreenComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				if(isRefreshButton){
					return;
				}
				getView().reload();
			} 
		});
	}
		
	
	/**
	 * This method adds the context menu
	 */
	@Override
	public void addContextMenuItems() {
		clearDisplayMessage();
		String[] menuItems ;
		String entryScreen = getView().getEntryScreenComboBox().getSelectionModel().getSelectedItem() != null ? getView().getEntryScreenComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		String entryModel = getView().getEntryModelComboBox().getSelectionModel().getSelectedItem() != null ? getView().getEntryModelComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		List<DefectMapDto> localAttributeMaintList = getView().getExternalSystemDefectTablePane().getTable().getSelectionModel().getSelectedItems();
		if(getView().getExternalSystemDefectTablePane().getTable().getContextMenu() != null)
			getView().getExternalSystemDefectTablePane().getTable().getContextMenu().getItems().clear();
		if(localAttributeMaintList != null && localAttributeMaintList.size() > 0 ) {
			if(getModel().isVersionCreated(entryModel))
				menuItems = new String[] {QiConstant.CREATE,};
			 else
				menuItems = new String[] {QiConstant.CREATE, QiConstant.UPDATE, QiConstant.DELETE};
			getView().getExternalSystemDefectTablePane().createContextMenu(menuItems, this);
		}else if(!entryScreen.equalsIgnoreCase(StringUtils.EMPTY)){
			menuItems = new String[] {QiConstant.CREATE};
			getView().getExternalSystemDefectTablePane().createContextMenu(menuItems, this);
		}
	}
	
	/**
	 * This method removes the context menu
	 */
	public void removeContextMenuItems() {
		clearDisplayMessage();
		if(getView().getExternalSystemDefectTablePane().getTable().getContextMenu()!=null)
			getView().getExternalSystemDefectTablePane().getTable().getContextMenu().getItems().clear();
		
	}
	/**
	 * This method is to map the action event to context menu and radio button.
	 * @param actionEvent the action event
	 */
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			if(QiConstant.CREATE.equals(menuItem.getText())){
				createButtonAction(actionEvent);
			} else if(QiConstant.UPDATE.equals(menuItem.getText())) {
				updateButtonAction(actionEvent);
			}else if(QiConstant.DELETE.equals(menuItem.getText())) {
				deleteButtonAction(actionEvent);
			}
		}
		if (actionEvent.getSource().equals(getView().getRefreshBtn())){
			refreshBtnAction();
			EventBusUtil.publish(new StatusMessageEvent("Refreshed successfully", StatusMessageEventType.INFO));
		}
	}

	/**
	 * This method consists action for Create button 
	 * @param event 
	 */
	private void createButtonAction(ActionEvent event){
		clearDisplayMessage();
		try{
			DefectMapDto defectMapDto = getSelectedData();
			HeadlessDefectEntryDialog createDialog = new HeadlessDefectEntryDialog(QiConstant.CREATE, defectMapDto, getModel(),getApplicationId());
			createDialog.setScreenName(getView().getScreenName());
			createDialog.showDialog();
			if(!createDialog.isCancel())  {
				getView().reload();
			}
		}catch(Exception e){
			handleException("An error occured in create method ","Failed To Create", e);
		}
	}

	private DefectMapDto getSelectedData() {
		DefectMapDto defectMapDto=new DefectMapDto();
		defectMapDto.setEntryScreen(getView().getEntryScreenComboBox().getSelectionModel().getSelectedItem() == null?StringUtils.EMPTY:getView().getEntryScreenComboBox().getSelectionModel().getSelectedItem().toString());
		defectMapDto.setEntryModel(getView().getEntryModelComboBox().getSelectionModel().getSelectedItem() == null?StringUtils.EMPTY:getView().getEntryModelComboBox().getSelectionModel().getSelectedItem().toString());
		defectMapDto.setExternalSystemName(getView().getExternalSystemComboBox().getSelectionModel().getSelectedItem() == null?StringUtils.EMPTY:getView().getExternalSystemComboBox().getSelectionModel().getSelectedItem().toString());
		return defectMapDto;
	}

	/**
	 *  This method consists action for update button 
	 * @param event
	 */
	 
	private void updateButtonAction(ActionEvent event){
		clearDisplayMessage();
		try{
			DefectMapDto defectMapDto=getSelectedData();
			DefectMapDto selectedDefect = getView().getExternalSystemDefectTablePane().getTable().getSelectionModel().getSelectedItem();
			defectMapDto.setExternalDefectCode(selectedDefect.getExternalDefectCode());
			defectMapDto.setExternalPartCode(selectedDefect.getExternalPartCode());
			defectMapDto.setTextEntryMenu(selectedDefect.getTextEntryMenu());
			defectMapDto.setFullPartDesc(selectedDefect.getFullPartDesc());
			defectMapDto.setLocalDefectCombinationId(selectedDefect.getLocalDefectCombinationId());
			defectMapDto.setIsQicsRepairReqd(selectedDefect.getIsQicsRepairReqd());
			defectMapDto.setIsExtSysRepairReqd(selectedDefect.getIsExtSysRepairReqd());
			defectMapDto.setUpdateTimestamp(selectedDefect.getUpdateTimestamp());
			HeadlessDefectEntryDialog updateDialog = new HeadlessDefectEntryDialog(QiConstant.UPDATE, defectMapDto, getModel(),getApplicationId());
			updateDialog.setScreenName(getView().getScreenName());
			updateDialog.showDialog();
			if(!updateDialog.isCancel())  {
				getView().reload();
			}
		}catch(Exception e){
			handleException("An error occured in update method ","Failed To Update", e);
		}
	}
	
	/**
	 *  This method consists action for delete button 
	 * @param event 
	 */
	private void deleteButtonAction(ActionEvent event){
		clearDisplayMessage();
		try{
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			DefectMapDto defectMapDto = getView().getExternalSystemDefectTablePane().getSelectedItem();
			String externalSystemName = getView().getExternalSystemComboBox().getSelectionModel().getSelectedItem() == null?StringUtils.EMPTY:getView().getExternalSystemComboBox().getSelectionModel().getSelectedItem().toString();
			String entryModel = getView().getEntryModelComboBox().getSelectionModel().getSelectedItem();
			if(StringUtils.isBlank(entryModel))  {
				entryModel = StringUtils.EMPTY;
			}
			if (dialog.showReasonForChangeDialog(null)) {
				if(defectMapDto!= null){
					QiExternalSystemDefectMap qiExternalSystemDefectMapOld = getModel().findByPartAndDefectCodeExternalSystemAndEntryModel(defectMapDto.getExternalPartCode(),
							defectMapDto.getExternalDefectCode(), externalSystemName, entryModel);
					//creating primary key for audit log
					
					QiLocalDefectCombination qiLocalDefectCombinationOld = null;
					if(qiExternalSystemDefectMapOld.getLocalDefectCombinationId() > 0)  {
						qiLocalDefectCombinationOld = getModel().findbyLocalCombinationId(qiExternalSystemDefectMapOld.getLocalDefectCombinationId());
					}
					int rdcId = 0;
					if(qiLocalDefectCombinationOld != null)  {
						rdcId = qiLocalDefectCombinationOld.getRegionalDefectCombinationId();				
					}
					
					String auditPrimaryKeyValue = qiExternalSystemDefectMapOld.getExternalSystemName()+" "+qiExternalSystemDefectMapOld.getExternalPartCode()+" "+
							qiExternalSystemDefectMapOld.getExternalDefectCode()+" "+ (rdcId > 0 ? getModel().getAuditPrimaryKeyValue(rdcId) : "NotFound-" + System.currentTimeMillis());
					getModel().deleteExternalData(qiExternalSystemDefectMapOld);
					AuditLoggerUtil.logAuditInfo(qiExternalSystemDefectMapOld, null, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),auditPrimaryKeyValue);
				}
			}
			getView().reload();
		}catch(Exception e){
			handleException("An error occured in delete method ","Failed To Delete", e);
		}
	}
	
	/**
	 * This method is used to refresh the data.
	 */
	public void refreshBtnAction() {
		isRefreshButton = true;
		String plant = null;
		String externalSystemName = null;
		String productType = null;
		String entryModel = null;
		String entryScreen = null;
		if(getView().getPlantComboBox().getSelectionModel().getSelectedItem()!=null)
			plant = getView().getPlantComboBox().getSelectionModel().getSelectedItem().toString();
		if(getView().getExternalSystemComboBox().getSelectionModel().getSelectedItem()!=null)
			externalSystemName=getView().getExternalSystemComboBox().getSelectionModel().getSelectedItem().toString();
		if(getView().getProductTypeComboBox().getSelectionModel().getSelectedItem()!=null)
			productType = getView().getProductTypeComboBox().getSelectionModel().getSelectedItem().toString();
		if(getView().getEntryModelComboBox().getSelectionModel().getSelectedItem()!=null)
			entryModel = getView().getEntryModelComboBox().getSelectionModel().getSelectedItem().toString();
		if(getView().getEntryScreenComboBox().getSelectionModel().getSelectedItem()!=null)
			entryScreen = getView().getEntryScreenComboBox().getSelectionModel().getSelectedItem().toString();
		getView().getPlantComboBox().getItems().clear();
		getView().getProductTypeComboBox().getItems().clear();
		getView().getExternalSystemComboBox().getItems().clear();
		getView().getEntryModelComboBox().getItems().clear();
		getView().getEntryScreenComboBox().getItems().clear();
		//refresh plant drop down
		getView().loadPlantComboBox(getView().getSiteLabel());
		if(plant!= null){
			getView().getPlantComboBox().getSelectionModel().select(plant);
			//refresh product type drop down
			List<String> productTypeList = getModel().findAllProductTypes();
			getView().getProductTypeComboBox().getItems().addAll(productTypeList);
			if(productType!= null){
				getView().getProductTypeComboBox().getSelectionModel().select(productType);
				//refresh external system
				List<QiExternalSystemInfo> extSysList = ServiceFactory.getDao(QiExternalSystemInfoDao.class).findAll();
				if(extSysList != null && !extSysList.isEmpty())  {
					for(QiExternalSystemInfo extSys:extSysList){
						getView().getExternalSystemComboBox().getItems().add(extSys.getExternalSystemName());
					}
				}
				if(externalSystemName!= null ){
					getView().getExternalSystemComboBox().getSelectionModel().select(externalSystemName);
					//refresh entry model
					getView().getEntryModelComboBox().getItems().addAll(getModel().findAllByPlantAndProductType(plant, productType));
					if(entryModel!= null){
						getView().getEntryModelComboBox().getSelectionModel().select(entryModel);
						//refresh entry Screen
						getView().getEntryScreenComboBox().getItems().addAll(getModel().findAllByPlantProductTypeAndEntryModel(plant, productType, entryModel));
						if(entryScreen!= null){
							getView().getEntryScreenComboBox().getSelectionModel().select(entryScreen);
							getView().reload();
						}
					}
				}
			}
		}
		isRefreshButton = false;
	}

}
