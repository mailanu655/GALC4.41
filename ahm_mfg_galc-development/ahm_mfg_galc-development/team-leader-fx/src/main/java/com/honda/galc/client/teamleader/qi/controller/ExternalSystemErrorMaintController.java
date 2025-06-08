package com.honda.galc.client.teamleader.qi.controller;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;

import org.apache.commons.lang.StringUtils;


import com.honda.galc.client.teamleader.qi.model.ExternalSystemErrorMaintModel;
import com.honda.galc.client.teamleader.qi.view.ExternalSystemErrorMaintPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.dao.qi.QiExternalSystemDataDao;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.qi.QiExternalSystemData;
import com.honda.galc.qi.constant.QiConstant;
import com.honda.galc.util.AuditLoggerUtil;


/**
 * 
 * <h3>ExternalSystemErrorMaintController Class description</h3>
 * <p>
 * ExternalSystemErrorMaintController is used to load headless error data and perform reprocess and delete action.
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
public class ExternalSystemErrorMaintController extends AbstractQiController<ExternalSystemErrorMaintModel,ExternalSystemErrorMaintPanel> implements EventHandler<ActionEvent> {

	private List<QiExternalSystemData> qiExternalSystemDataList;
	private boolean isRefreshButton=false;

	public ExternalSystemErrorMaintController(ExternalSystemErrorMaintModel model,ExternalSystemErrorMaintPanel view) {
		super(model, view);
	}

	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			if("Reprocess".equals(menuItem.getText())){
				reprocessButtonAction(actionEvent);
			}else if(QiConstant.DELETE.equals(menuItem.getText())) {
				deleteButtonAction(actionEvent);
			}
		}
		else if (actionEvent.getSource().equals(getView().getRefreshBtn())){
				refreshBtnAction();
				EventBusUtil.publish(new StatusMessageEvent("Refreshed successfully", StatusMessageEventType.INFO));
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
		String siteName=getView().getSiteLabel().getText();
		if(getView().getPlantComboBox().getSelectionModel().getSelectedItem()!=null)
			plant = getView().getPlantComboBox().getSelectionModel().getSelectedItem().toString();
		if(getView().getExternalSystemComboBox().getSelectionModel().getSelectedItem()!=null)
			externalSystemName=getView().getExternalSystemComboBox().getSelectionModel().getSelectedItem().toString();
		if(getView().getProductTypeComboBox().getSelectionModel().getSelectedItem()!=null)
			productType = getView().getProductTypeComboBox().getSelectionModel().getSelectedItem().toString();
		getView().getPlantComboBox().getItems().clear();
		getView().getProductTypeComboBox().getItems().clear();
		getView().getExternalSystemComboBox().getItems().clear();
		getView().loadPlantComboBox(siteName);
		if(plant!= null){
			if(getModel().findAllBySite(siteName).contains(plant))
				getView().getPlantComboBox().getSelectionModel().select(plant);
			loadProductTypeComboBox(plant);
			if(productType!= null){
				if(getModel().findAllProductTypes().contains(productType))
					getView().getProductTypeComboBox().getSelectionModel().select(productType);
				loadExternalSystemComboBox(productType);
				if(externalSystemName!= null ){
					if(getModel().findAllExternalSystemList(productType).contains(externalSystemName))
						getView().getExternalSystemComboBox().getSelectionModel().select(externalSystemName);
					getView().reload();
				}
			}
		}
		
		isRefreshButton = false;
	}

	@Override
	public void addContextMenuItems() {
		String[] menuItems ;
		qiExternalSystemDataList=getView().getExternalSystemErrorTablePane().getSelectedItems();
		if(qiExternalSystemDataList.size() >= 1){
			menuItems= new String[] {"Reprocess", QiConstant.DELETE};
			getView().getExternalSystemErrorTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			getView().getExternalSystemErrorTablePane().createContextMenu(menuItems, this);
		}
	}

	@Override
	public void initEventHandlers() {
		addComboBoxListener();
		if (isFullAccess()) {
			addExternalSystemErrorTablePaneListener();
		}
	}

	/**
	 * This method adds All combo box listeners.
	 */
	private void addComboBoxListener() {
		addPlantComboBoxListener();
		addProductTypeComboBoxListener();
		addExternalSystemComboBoxListener();
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
				loadExternalSystemComboBox(new_val);
			}
		});
	}

	/**
	 * This method load external system combo box.
	 * @param productType 
	 */
	private void loadExternalSystemComboBox(String productType) {
		List<String> externalSystemList = getModel().findAllExternalSystemList(productType);
		getView().getExternalSystemComboBox().getItems().clear();
		getView().getExternalSystemComboBox().setPromptText("Select");
		if(externalSystemList!=null )
			getView().getExternalSystemComboBox().getItems().addAll(externalSystemList);
	}

	/**
	 * This method adds the external system combo box listener.
	 */
	private void addExternalSystemComboBoxListener() {
		getView().getExternalSystemComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				if(isRefreshButton){
					return;
				}
				getView().reload();
			} 
		});
	}

	/**
	 * This method is used for External System Error table listener
	 */
	private void addExternalSystemErrorTablePaneListener(){
		getView().getExternalSystemErrorTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiExternalSystemData>() {
			public void changed(
					ObservableValue<? extends QiExternalSystemData> arg0,
					QiExternalSystemData arg1,
					QiExternalSystemData arg2) {
				addContextMenuItems();
			}
		});

		getView().getExternalSystemErrorTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}


	/**
	 * This method is used to reprocess the record(s)
	 * @param event
	 */
	private void reprocessButtonAction(ActionEvent event){
		
		int successCount=0;
		int failedCount=0;
		
		qiExternalSystemDataList = getView().getExternalSystemErrorTablePane().getSelectedItems();
		if(qiExternalSystemDataList.size()==1){
			DefectMapDto data = setExternalSystemMapDto(qiExternalSystemDataList.get(0));
			//new entry in error should not be created when same data is reprocessed
			data.setReprocess(true);
			int response=getModel().reprocessData(data);
			if(response==QiConstant.SC_CREATED){
				getDao(QiExternalSystemDataDao.class).removeByKey(qiExternalSystemDataList.get(0).getId());
				EventBusUtil.publish(new StatusMessageEvent("Reprocessed Successfully.", StatusMessageEventType.INFO));
			}
			else if(response==QiConstant.SC_NOT_ACCEPTABLE){
				displayErrorMessage("Reprocessing failed as mandatory field(s) are missing.");
			}
			else if(response==QiConstant.SC_LENGTH_REQUIRED){
				displayErrorMessage("Reprocessing failed due to data type or length mismatch.");
			}
			else if(response==QiConstant.SC_NOT_FOUND){
				displayErrorMessage("Reprocessing failed as mapping doesn't exists for selected part defect code.");
			}
			else if(response==QiConstant.SC_PARTIAL_CONTENT){
				displayErrorMessage("Reprocessing failed due to partial content.");
			}
			else{
				displayErrorMessage("Reprocessing Failed");
				return;
			}
		}
		else{
			for(QiExternalSystemData externalSystemData:qiExternalSystemDataList){
				DefectMapDto data=setExternalSystemMapDto(externalSystemData);
				//new entry in error should not be created when same data is reprocessed
				data.setReprocess(true);
				int response=getModel().reprocessData(data);
				if(response==201){
					getModel().deleteExternalSystemData(externalSystemData.getId());
					successCount++;
				}
				else{
					failedCount++;
				}
			}
			MessageDialog.showError(getView().getStage(),"Reprocessing completed for "+successCount +" records and failed for "+failedCount + " records .");
		}
		getView().reload();
	}


	/**
	 * This method is collect selected data in dto for reprocessing.
	*/
	private DefectMapDto setExternalSystemMapDto(QiExternalSystemData externalSystemData) {
		DefectMapDto dto=new DefectMapDto();
		dto.setProductId(StringUtils.trimToEmpty(externalSystemData.getId().getProductId()));
		dto.setProcessPointId(StringUtils.trimToEmpty(externalSystemData.getId().getProcessPointId()));
		dto.setEntryDepartment(StringUtils.trimToEmpty(externalSystemData.getEntryDept().toUpperCase()));
		dto.setProductType(StringUtils.trimToEmpty(externalSystemData.getProductType().toUpperCase()));
		dto.setWriteupDepartment(StringUtils.trimToEmpty(externalSystemData.getWriteUpDept().toUpperCase()));
		dto.setImageName(StringUtils.trimToEmpty(externalSystemData.getImageName().toUpperCase()));
		dto.setxAxis(String.valueOf(externalSystemData.getPointX()));
		dto.setyAxis(String.valueOf(externalSystemData.getPointY()));
		dto.setCurrentDefectStatus(String.valueOf(externalSystemData.getCurrentDefectStatus()));
		dto.setOriginalDefectStatus(String.valueOf(externalSystemData.getOriginalDefectStatus()));
		dto.setAssociateId(StringUtils.trimToEmpty(externalSystemData.getAssociateId()).toUpperCase());
		dto.setEntrySite(externalSystemData.getEntrySite());
		dto.setExternalDefectCode(StringUtils.trimToEmpty(externalSystemData.getId().getExternalDefectCode().toUpperCase()));
		dto.setExternalPartCode(StringUtils.trimToEmpty(externalSystemData.getId().getExternalPartCode().toUpperCase()));
		String extSysName = StringUtils.trimToEmpty(externalSystemData.getId().getExternalSystemName());
		dto.setExternalSystemName(extSysName);
		if(extSysName.equalsIgnoreCase(QiExternalSystem.LOT_CONTROL.name()))  {
			dto.setLotControl(true);
		}
		return dto;
	}

	/**
	 * This method is used to delete the record(s)
	 * @param event
	 */
	private void deleteButtonAction(ActionEvent event){
		clearDisplayMessage();
		try{
			qiExternalSystemDataList=getView().getExternalSystemErrorTablePane().getSelectedItems();
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if(dialog.showReasonForChangeDialog(null)){
				for(QiExternalSystemData qiExternalSystemData: qiExternalSystemDataList){
					//call to prepare and insert audit data
					AuditLoggerUtil.logAuditInfo(qiExternalSystemData, null, dialog.getReasonForChangeText(), getView().getScreenName(),getUserId());
					getModel().deleteExternalSystemData(qiExternalSystemData.getId());

				}
				EventBusUtil.publish(new StatusMessageEvent("Deleted Successfully.", StatusMessageEventType.INFO));
				getView().reload();

			}else
				return;

		}
		catch(Exception e){
			handleException("An error occured in delete method ","Failed To Delete", e);
		}

	}
}
