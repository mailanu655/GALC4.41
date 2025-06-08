package com.honda.galc.client.teamleader.qi.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.model.EntryModelMaintenanceModel;
import com.honda.galc.client.teamleader.qi.model.ExportMtcModelModel;
import com.honda.galc.client.teamleader.qi.model.ImportMtcModelModel;
import com.honda.galc.client.teamleader.qi.view.EntryModelDialog;
import com.honda.galc.client.teamleader.qi.view.ExportMtcModelDialog;
import com.honda.galc.client.teamleader.qi.view.ImportMtcModelDialog;
import com.honda.galc.client.teamleader.qi.view.MtcToEntryModelPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiEntryModelGrouping;
import com.honda.galc.entity.qi.QiEntryModelGroupingId;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiEntryScreenDept;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.ObjectComparator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>MtcToEntryModelController</code> is the controller class for Mtc to
 * Entry Model Panel.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class MtcToEntryModelController extends AbstractQiController<EntryModelMaintenanceModel, MtcToEntryModelPanel>
implements EventHandler<ActionEvent> {
	boolean isRefresh=false;
	List<String> previousMtcModels, currentlyAssignedMtcModels;

	public MtcToEntryModelController(EntryModelMaintenanceModel model, MtcToEntryModelPanel view) {
		super(model, view);

	}
	
	ChangeListener<String> productTypeChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if(!isRefresh){
			try {
				Logger.getLogger().check("ProductType:"+newValue.trim()+" is selected");
				clearDisplayMessage();
				getView().getSaveBtn().setDisable(true);
				getView().getDeassignBtn().setDisable(true);
				getView().getAssignBtn().setDisable(true);
				getView().getMtcModelNameFilterTextField().clear();
				clearAssginedTableData();
				if(null!=newValue)
					getView().reloadEntrymodel(StringUtils.trimToEmpty(newValue));
				getView().reload();
				disableMTCmodelTables();
				if(isFullAccess()){
					addContextMenuItems();
				}
			} catch (Exception e) {
				handleException("An error occurred while searching data ", "Failed to search Avl MTC Model data ",
						e);
			}
			}
		}
	};
	
	ChangeListener<QiEntryModel> entryModelTablePaneChangeListener = new ChangeListener<QiEntryModel>() {
		public void changed(ObservableValue<? extends QiEntryModel> arg0, QiEntryModel arg1,
				QiEntryModel arg2) {
			if(!isRefresh){
			clearDisplayMessage();
			getView().getSaveBtn().setDisable(true);
			if(isFullAccess()){
				setAssignButton();
				setDeassignButton();
				addContextMenuItems();
			}
			if (null != getView().getQiEntryModelTablePane().getSelectedItem()) {
				getView().getQiAvailableMtcModelgroupingTablePane().setDisable(false);
				getView().getAssignModelGroupingTablePane().setDisable(false);
				getView().getMtcModelNameFilterTextField().setDisable(false);
				QiEntryModel qiEntryModel = getView().getQiEntryModelTablePane().getSelectedItem();
				Logger.getLogger().check("Entry Model:"+qiEntryModel.getId().getEntryModel()+" selected");
				List<QiEntryModelGrouping> qiEntryModelGrouping = getModel().getAssginedMtcModel(StringUtils.trimToEmpty(qiEntryModel.getId().getEntryModel()), qiEntryModel.getId().getIsUsed());
				getView().getAssignModelGroupingTablePane().setData(setMtcModelYearDescriptionToDto(getModel().getAssginedMtcModel(StringUtils.trimToEmpty(qiEntryModel.getId().getEntryModel()), qiEntryModel.getId().getIsUsed())));
				previousMtcModels = new ArrayList<String>();
				for (QiEntryModelGrouping qiEntryModelGroup : qiEntryModelGrouping) {
					previousMtcModels.add(qiEntryModelGroup.getId().getMtcModel());
				}
			}
			getView().reload();	
		}
		}
	};

	/**
	 * This method is used to add listener on main Entry Model and Available Mtc
	 * Model table.
	 */
	@Override
	public void initEventHandlers() {
		clearDisplayMessage();
		if(isFullAccess()){
		 addContextMenuItems();
		}
		addProductTypeListener();
		addEntryModelTableListener();
		addAvailableMtcModelTableListener();
	}

	/**
	 * This method is the Listener for Product Type ComboBox.
	 */
	private void addProductTypeListener() {
		getView().getProductType().valueProperty().addListener(productTypeChangeListener);
	}

	/**
	 * This method is the Listener for filter.
	 */
	private void addAvailableMtcModelTableListener() {
		getView().getQiAvailableMtcModelgroupingTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiMtcToEntryModelDto>() {
			public void changed(ObservableValue<? extends QiMtcToEntryModelDto> observableValue, QiMtcToEntryModelDto oldValue,QiMtcToEntryModelDto newValue) {
				if(newValue!=null)
				{
				 Logger.getLogger().check("Available MTC Model:"+newValue.getModelCode()+" selected");
				}
				enableAssignDeassignBtn(true);
			}
		});
		
		getView().getAssignModelGroupingTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiMtcToEntryModelDto>() {
			public void changed(ObservableValue<? extends QiMtcToEntryModelDto> arg0, QiMtcToEntryModelDto arg1,QiMtcToEntryModelDto arg2) {
				if(arg2!=null)
				{
				 Logger.getLogger().check("Assigned MTC Model:"+arg2.getModelCode()+" selected");
				}
				enableAssignDeassignBtn(false);
			}
		});
	}
	
	private void enableAssignDeassignBtn(boolean isAssign) {
		if(!isRefresh){
			clearDisplayMessage();
			if(isFullAccess()){
				if(isAssign){
					setAssignButton();
				}
				else
				{
					setDeassignButton();
				}
		    }
		}
	}
	
	/**
	 * This method is used to reload assigned Mtc based on entry model selection.
	 */
	private void addEntryModelTableListener() {
		getView().getQiEntryModelTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(entryModelTablePaneChangeListener);

		getView().getQiEntryModelTablePane().getTable().setRowFactory(new Callback<TableView<QiEntryModel>, TableRow<QiEntryModel>>() {
			public TableRow<QiEntryModel> call (TableView<QiEntryModel> tableView) {
				final TableRow<QiEntryModel> row= new TableRow<QiEntryModel>();
				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						final int index= row.getIndex();
						if(index >= 0 && index < getView().getQiEntryModelTablePane().getTable().getItems().size() && getView().getQiEntryModelTablePane().getTable().getSelectionModel().isSelected(index) && event.isControlDown()) {
							getView().getQiEntryModelTablePane().getTable().getSelectionModel().clearSelection(index);
							clearDisplayMessage();
							clearAssginedTableData();
							disableMTCmodelTables();
							getView().reload();
							event.consume();
						}
					}

				});
				return row;
			}
		});

	}
	/**
	 * This method is used to set QiEntryModelGrouping to QiMtcToEntryModelDto.
	 */
	private List<QiMtcToEntryModelDto> setMtcModelToDto(List<QiEntryModelGrouping> assginedMtcModel) {
		List<QiMtcToEntryModelDto> assignedMtcModelList = new ArrayList<QiMtcToEntryModelDto>();
		if (assginedMtcModel != null) {
			for (QiEntryModelGrouping qiEntryModelGrouping : assginedMtcModel) {
				QiMtcToEntryModelDto qiEntrytoMtcDto = new QiMtcToEntryModelDto();
				qiEntrytoMtcDto.setMtcModel(qiEntryModelGrouping.getId().getMtcModel());
				assignedMtcModelList.add(qiEntrytoMtcDto);
			}
		}
		return assignedMtcModelList;
	}
	/**
	 * This method is used to clear assigned mtc table.
	 */
	private void clearAssginedTableData() {
		if (null != getView().getAssignModelGroupingTablePane().getTable().getItems())
			getView().getAssignModelGroupingTablePane().getTable().getItems().clear();
	}

	/**
	 * This method is used to enable/disable assign button.
	 */
	private void setAssignButton() {
		if (null != getView().getQiAvailableMtcModelgroupingTablePane().getSelectedItem() && null != getView().getQiEntryModelTablePane().getSelectedItem()) {
			QiEntryModel entryModel = getView().getQiEntryModelTablePane().getSelectedItem();
			if(entryModel.getId().getIsUsed() == (short) 1 && getModel().isVersionCreated(entryModel.getId().getEntryModel()))
				getView().getAssignBtn().setDisable(true);
			else
				getView().getAssignBtn().setDisable(false);
		}
		else
			getView().getAssignBtn().setDisable(true);
	}
	/**
	 * This method is used to enable/disable deassign button.
	 */
	private void setDeassignButton() {
		if (null != getView().getAssignModelGroupingTablePane().getSelectedItem() && null != getView().getQiEntryModelTablePane().getSelectedItem()){
			QiEntryModel entryModel = getView().getQiEntryModelTablePane().getSelectedItem();
			if(entryModel.getId().getIsUsed() == (short) 1 && getModel().isVersionCreated(entryModel.getId().getEntryModel()))
				getView().getDeassignBtn().setDisable(true);
			else
				getView().getDeassignBtn().setDisable(false);
		}
		else
			getView().getDeassignBtn().setDisable(true);
	}

	public void handle(ActionEvent event) {
		if (event.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) event.getSource();
			handlerForMenuItem(menuItem,event);		
		}
		if (event.getSource() instanceof LoggedButton) {
			LoggedButton button = (LoggedButton) event.getSource();
			handlerForLoggedButton(button,event);	
		}
		if (event.getSource() instanceof LoggedRadioButton) {
			LoggedRadioButton radioButton = (LoggedRadioButton) event.getSource();
			handlerForLoggedRadioButton(radioButton,event);
		}
		if(event.getSource() instanceof UpperCaseFieldBean){
			handleForUpperCaseFieldBean();
		}
	}
	private void handleForUpperCaseFieldBean() {
		try {
			clearDisplayMessage();
			getView().reload();
		} 
		catch (Exception e) {
			handleException("An error occurred while searching data ", "Failed to search Mtc Model data ", e);
		}

	}

	/**
	 * This method is used to handle loggedRadioButton action.
	 */
	private void handlerForLoggedRadioButton(LoggedRadioButton radioButton, ActionEvent event) {
		if (QiConstant.ALL.equalsIgnoreCase(radioButton.getText())) {
			allRadioButtonAction(event);
		} else if (QiConstant.ACTIVE.equalsIgnoreCase(radioButton.getText())) {
			activeRadioButtonAction(event);
		} else if (QiConstant.INACTIVE.equalsIgnoreCase(radioButton.getText())) {
			inactiveRadioButtonAction(event);
		}
	}
	/**
	 * This method is used to handle loggedButton action.
	 */
	private void handlerForLoggedButton(LoggedButton button, ActionEvent event) {

		if (QiConstant.DEASSIGN.equals(button.getId())) {
			deassignButtonAction(event);
			getView().getSaveBtn().setDisable(false);
			if (!isFullAccess()) {
				getView().getSaveBtn().setDisable(true);
			}
		} else if (QiConstant.ASSIGN.equals(button.getId())) {
			assignButtonAction(event);
			getView().getSaveBtn().setDisable(false);
			if (!isFullAccess()) {
				getView().getSaveBtn().setDisable(true);
			}
		} else if (QiConstant.SAVE.equalsIgnoreCase(button.getText()))
			saveButtonAction(event);
		else if (QiConstant.REFRESH.equalsIgnoreCase(button.getText()))
			{
			refreshBtnAction();
			EventBusUtil.publish(new StatusMessageEvent("Refreshed successfully", StatusMessageEventType.INFO));
			}
	}

	/**
	 * This method is used to refresh the data.
	 */
	public void refreshBtnAction() {
		
		isRefresh=true;
		
		String productType=getView().getProductType().getValue();
		String filter = getView().getMtcModelNameFilterTextField().getText();
		int selectedEntryModel =getView().getQiEntryModelTablePane().getTable().getSelectionModel().getSelectedIndex();
		List<Integer> selectedAvailablMtcModelList = new ArrayList<Integer>();
		List<QiMtcToEntryModelDto> selectedAssignedMtcModelList = new ArrayList<QiMtcToEntryModelDto>();
		selectedAvailablMtcModelList.addAll(getView().getQiAvailableMtcModelgroupingTablePane().getTable().getSelectionModel().getSelectedIndices());
		selectedAssignedMtcModelList.addAll(getView().getAssignModelGroupingTablePane().getTable().getSelectionModel().getSelectedItems());
		List<String> productTypeList = getModel().getProductTypes();
		getView().getProductType().getItems().clear();
		if (productTypeList.size() > 0) 
			getView().getProductType().getItems().addAll(productTypeList);
		if(null!= productType) {
			if(productTypeList.contains(productType)) {
				getView().getProductType().setValue(productType);
				getView().reloadEntrymodel(productType);
			}
			getView().getQiEntryModelTablePane().getTable().getSelectionModel().select(selectedEntryModel);
			getView().getMtcModelNameFilterTextField().settext(filter);
			QiEntryModel entryModel  =getView().getQiEntryModelTablePane().getSelectedItem();
			if(null!= entryModel){
			getView().getAssignModelGroupingTablePane().setData(setMtcModelYearDescriptionToDto(getModel().getAssginedMtcModel(StringUtils.trimToEmpty(entryModel.getId().getEntryModel()), entryModel.getId().getIsUsed())));
			getView().reload();
			
			for (int selectedAvailablMtcModel : selectedAvailablMtcModelList) 
				getView().getQiAvailableMtcModelgroupingTablePane().getTable().getSelectionModel().select(selectedAvailablMtcModel);
				
			for (QiMtcToEntryModelDto selectedAssignedMtcModel : selectedAssignedMtcModelList) 
				getView().getAssignModelGroupingTablePane().getTable().getSelectionModel().select(selectedAssignedMtcModel);
			}
			
		}
		isRefresh=false;
	}
	/**
	 * This method is used to handle MenuItem action.
	 */
	private void handlerForMenuItem(MenuItem menuItem,ActionEvent event) {
		Logger.getLogger().check("Menu Item:"+menuItem.getText().trim()+" clicked");
		if (QiConstant.CREATE.equals(menuItem.getText()))
			createEntryModel(event);
		else if (QiConstant.UPDATE.equals(menuItem.getText()))
			updateEntryModel(event);
		else if (QiConstant.INACTIVATE.equals(menuItem.getText()))
			inactivateEntryModel(event);
		else if (QiConstant.REACTIVATE.equals(menuItem.getText()))
			reactivateEntryModel(event);
		else if (QiConstant.DELETE.equals(menuItem.getText()))
			deleteEntryModel(event);
		else if (QiConstant.CREATE_VERSION.equals(menuItem.getText()))
			createEntryModelVersion(event);
		else if (QiConstant.APPLY_VERSION.equals(menuItem.getText()))
			applyEntryModelVersion(event);
		else if (QiConstant.EXPORT.equals(menuItem.getText()))	
			exportEntryModel(event);
		else if (QiConstant.IMPORT.equals(menuItem.getText()))	
			importEntryModel(event);
	}

	/**
	 * This method is used to inactivate entry model.
	 */
	private void inactivateEntryModel(ActionEvent event) {
		updateEntryModelStatus(false);
		getView().reloadEntrymodel(StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()));
		addContextMenuItems();

	}
	/**
	 * This method is used to reactivate entry model.
	 */
	private void reactivateEntryModel(ActionEvent event) {
		updateEntryModelStatus(true);
		getView().reloadEntrymodel(StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()));
		addContextMenuItems();

	}
	/**
	 * This method is used to update entry model status.
	 */
	private void updateEntryModelStatus(boolean isActive) {
		ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
		if (dialog.showReasonForChangeDialog(null)) {
			try {
				clearDisplayMessage();
				QiEntryModel entryModel=getView().getQiEntryModelTablePane().getSelectedItem();
				QiEntryModel qiEntryModelCloned=(QiEntryModel) entryModel.deepCopy();
				getView().getQiEntryModelTablePane().getSelectedItem().setActive((short) (isActive ? 1 : 0));
				getView().getQiEntryModelTablePane().getSelectedItem().setUpdateUser(getUserId());
				getModel().updateEntryModel(getView().getQiEntryModelTablePane().getSelectedItem());
				//call to prepare and insert audit data
				AuditLoggerUtil.logAuditInfo(qiEntryModelCloned, entryModel, dialog.getReasonForChangeText(),getView().getScreenName() ,getUserId());
			} catch (Exception e) {
				handleException("An error occured in updateEntryModelStatus method ", "Failed to update status", e);
			}
		}
		clearAssginedTableData();
		disableMTCmodelTables();
	}
	/**
	 * This method is used to update entry model.
	 */
	private void updateEntryModel(ActionEvent event) {		
		EntryModelDialog dialog =new EntryModelDialog(QiConstant.UPDATE, getView().getQiEntryModelTablePane().getSelectedItem(), getModel(),getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		getView().reloadEntrymodel(StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()));
		getView().reload();
		clearAssginedTableData();
		disableMTCmodelTables(); 
	}

	/**
	 * This method is used to create Entry Model.
	 */
	private void createEntryModel(ActionEvent event) {
		QiEntryModel entryModel = new QiEntryModel();
		entryModel.setProductType(StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()));
		EntryModelDialog dialog = new EntryModelDialog(QiConstant.CREATE, entryModel, getModel(),getApplicationId());
		dialog.showDialog();
		getView().reloadEntrymodel(StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()));
		getView().reload();
		clearAssginedTableData();
		disableMTCmodelTables();
	}
	
	/**
	 * This method is used to export an Mtc Model to a local XML file.
	 */
	private void exportEntryModel(ActionEvent event) {
		ExportMtcModelModel myModel = new ExportMtcModelModel();
		if(getView().getQiEntryModelTablePane().getSelectedItem() == null)  return;
		
		myModel.setEntryModel(getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel());
		
		myModel.setApplicationContext(getModel().getApplicationContext());
		ExportMtcModelDialog dialog = new ExportMtcModelDialog(QiConstant.EXPORT, new QiEntryScreenDto(), myModel,getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		getView().reloadEntrymodel(StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()));
		getView().reload();
		clearAssginedTableData();
		disableMTCmodelTables(); 
	}	
	
	/**
	 * This method is used to import a previously exported mtc model XML file. 
	 */
	private void importEntryModel(ActionEvent event) {
		
		ImportMtcModelModel myModel = new ImportMtcModelModel();
		myModel.setApplicationContext(getModel().getApplicationContext());
		myModel.setProductType(getView().getProductType().getValue());
		ImportMtcModelDialog dialog = new ImportMtcModelDialog(QiConstant.IMPORT, new QiEntryScreenDto(), myModel,getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		getView().reloadEntrymodel(StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()));
		getView().reload();
		clearAssginedTableData();
		disableMTCmodelTables(); 
	}
	
	/**
	 * This method is used to find inactive Mtc Models.
	 */
	private void inactiveRadioButtonAction(ActionEvent event) {
		getView().getQiEntryModelTablePane().setData(getModel().getEntryModelByStatus(QiConstant.INACTIVE,StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem())));
		clearAssginedTableData();
		disableMTCmodelTables();
	}
	/**
	 * This method is used to find active Mtc Models.
	 */
	private void activeRadioButtonAction(ActionEvent event) {
		getView().getQiEntryModelTablePane().setData(getModel().getEntryModelByStatus(QiConstant.ACTIVE,StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem())));
		clearAssginedTableData();
		disableMTCmodelTables();
	}
	/**
	 * This method is used to find all Mtc Models.
	 */
	private void allRadioButtonAction(ActionEvent event) {
		getView().reloadEntrymodel(StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()));
		clearAssginedTableData();
		disableMTCmodelTables();
	}
	/**
	 * This method is used to assign and deaasign Mtc models.
	 */
	private void saveButtonAction(ActionEvent event) {
		getView().getSaveBtn().setDisable(true);
		currentlyAssignedMtcModels= new ArrayList<String>();
		for (QiMtcToEntryModelDto dto : getView().getAssignModelGroupingTablePane().getTable().getItems()) {
			currentlyAssignedMtcModels.add(dto.getMtcModel());
		}
		if(!currentlyAssignedMtcModels.equals(previousMtcModels)){
		setDeassignButton();
		setAssignButton();
		List<QiEntryModelGrouping> entryModelGroupingList = findPreviousQiEntryModelGroupingList(findPreviousMtcList(findExistingAvailableMtcModelList()));
		List<QiEntryModelGrouping> newEntryModelGroupingList = findNewEntryModelGroupingOfMtcModels(findExistingAssignedMtcModelList());
		getModel().removeEntryModelGrouping(entryModelGroupingList);

		// call audit logger
		for (QiEntryModelGrouping qiEntryModelGroup : entryModelGroupingList) {

			if (qiEntryModelGroup != null) {

				QiEntryModelGrouping entryModelGrouping= new QiEntryModelGrouping();
				QiEntryModelGroupingId entryModelGroupingId= new QiEntryModelGroupingId();
				entryModelGroupingId.setEntryModel(qiEntryModelGroup.getId().getEntryModel().trim());
				entryModelGroupingId.setMtcModel(qiEntryModelGroup.getId().getMtcModel().trim());
				entryModelGrouping.setId(entryModelGroupingId);
				entryModelGrouping.setCreateUser(qiEntryModelGroup.getCreateUser());
				entryModelGrouping.setUpdateUser(qiEntryModelGroup.getUpdateUser());
				AuditLoggerUtil.logAuditInfo(entryModelGrouping, null, QiConstant.SAVE_REASON_FOR_MTC_TO_ENTRY_MODEL_AUDIT, getView().getScreenName(),getUserId());

			}
		}
		getModel().updateEntryModelGrouping(newEntryModelGroupingList);
		getView().reload();
		entryModelGroupingList.removeAll(Collections.singleton(null));
		if(entryModelGroupingList.isEmpty())
			EventBusUtil.publish(new StatusMessageEvent("Assigned MTC Model(s) to Entry Model successfully", StatusMessageEventType.INFO));
		else if(newEntryModelGroupingList.isEmpty())
			EventBusUtil.publish(new StatusMessageEvent("Deassigned MTC Model(s) from Entry Model successfully", StatusMessageEventType.INFO));
		else
			EventBusUtil.publish(new StatusMessageEvent("Assigned/Deassigned MTC Model(s) to/from Entry Model successfully", StatusMessageEventType.INFO));
		}else
			EventBusUtil.publish(new StatusMessageEvent("No Changes Detected", StatusMessageEventType.INFO));
		getView().getAssignModelGroupingTablePane().getTable().getSelectionModel().clearSelection();
	}
	/**
	 * This method is used to find previous combination of Entry Model & Mtc Models.
	 */
	private List<QiEntryModelGrouping> findPreviousQiEntryModelGroupingList(List<String> previousMtcList) {
		List<QiEntryModelGrouping> previousQiEntryModelGroupingList = new ArrayList<QiEntryModelGrouping>();
		short modelVersion = getView().getQiEntryModelTablePane().getTable().getSelectionModel().getSelectedItem().getId().getIsUsed();
		for (String mtcModel : previousMtcList) {
			QiEntryModelGrouping qiEntryModelGrouping = getModel().findEntryModelGroupingByMtcModel(mtcModel, modelVersion);
			previousQiEntryModelGroupingList.add(qiEntryModelGrouping);
		}
		return previousQiEntryModelGroupingList;
	}
	/**
	 * This method is used to find new combination Mtc Models.
	 */
	private List<QiEntryModelGrouping> findNewEntryModelGroupingOfMtcModels(
			List<QiEntryModelGrouping> existingAssignedMtcModelList) {
		List<QiEntryModelGrouping> newCombinationOfMtcModels= new ArrayList<QiEntryModelGrouping>();
		if (null != getView().getAssignModelGroupingTablePane().getTable().getItems()) {
			for (QiMtcToEntryModelDto dto : getView().getAssignModelGroupingTablePane().getTable().getItems()) {
				if (null != dto) {
					QiEntryModelGrouping qiEntryModelGroup = new QiEntryModelGrouping();
					QiEntryModelGroupingId qiEntryModelGroupingId=new QiEntryModelGroupingId();
					qiEntryModelGroupingId.setEntryModel(StringUtils.trimToEmpty(getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel()));
					qiEntryModelGroupingId.setMtcModel(dto.getMtcModel());
					qiEntryModelGroupingId.setIsUsed(getView().getQiEntryModelTablePane().getTable().getSelectionModel().getSelectedItem().getId().getIsUsed());
					qiEntryModelGroup.setId(qiEntryModelGroupingId);
					qiEntryModelGroup.setCreateUser(getUserId());
					if (null != existingAssignedMtcModelList) {
						if (!existingAssignedMtcModelList.contains(qiEntryModelGroup))
							newCombinationOfMtcModels.add(qiEntryModelGroup);
					} else {
						newCombinationOfMtcModels.add(qiEntryModelGroup);
					}
				}
			}
		}
		return newCombinationOfMtcModels;
	}
	/**
	 * This method is used to find previous Mtc Models.
	 */
	private List<String> findPreviousMtcList(List<QiMtcToEntryModelDto> existingAvailableMtcModelList) {
		List<String> previousMtcList=new ArrayList<String>();
		if (null != existingAvailableMtcModelList) {
			for (QiMtcToEntryModelDto dto : getView().getQiAvailableMtcModelgroupingTablePane().getTable().getItems()) {
				if (null != dto && !existingAvailableMtcModelList.contains(dto)) {
					previousMtcList.add(StringUtils.trimToEmpty(dto.getMtcModel()));
				}
			}
		}
		else {
			for (QiMtcToEntryModelDto dto : getView().getQiAvailableMtcModelgroupingTablePane().getTable().getItems()) {
				if (null != dto) {
					previousMtcList.add(StringUtils.trimToEmpty(dto.getMtcModel()));
				}
			}
		}
		return previousMtcList;
	}
	/**
	 * This method is used to find existing available Mtc Models.
	 */
	private List<QiMtcToEntryModelDto> findExistingAvailableMtcModelList() {
		List<QiMtcToEntryModelDto> existingAvailableMtcModelList = new ArrayList<QiMtcToEntryModelDto>();
		if (null != getAvailableMtcModelData("",StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()))) {
			existingAvailableMtcModelList = getAvailableMtcModelData("",StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()));
		} else {
			existingAvailableMtcModelList = null;
		}
		return existingAvailableMtcModelList;
	}
	/**
	 * This method is used to find existing assigned Mtc Models.
	 */
	private List<QiEntryModelGrouping> findExistingAssignedMtcModelList() {
		List<QiEntryModelGrouping> existingAssignedMtcModelList = new ArrayList<QiEntryModelGrouping>();
		if (null != getModel().getAssginedMtcModel(StringUtils.trimToEmpty(getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel()),getView().getQiEntryModelTablePane().getSelectedItem().getId().getIsUsed())) {
			QiEntryModelGrouping previousElement;
			QiEntryModelGroupingId qiEntryModelGroupingId;
			for (QiMtcToEntryModelDto element : setMtcModelToDto(getModel().getAssginedMtcModel(StringUtils.trimToEmpty(getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel()),getView().getQiEntryModelTablePane().getSelectedItem().getId().getIsUsed()))) {
				previousElement = new QiEntryModelGrouping();
				qiEntryModelGroupingId=new QiEntryModelGroupingId();
				qiEntryModelGroupingId.setEntryModel(StringUtils.trimToEmpty(getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel()));
				qiEntryModelGroupingId.setMtcModel(element.getMtcModel());
				previousElement.setId(qiEntryModelGroupingId);
				existingAssignedMtcModelList.add(previousElement);
			}
		} else {
			existingAssignedMtcModelList = null;
		}
		return existingAssignedMtcModelList;
	}
	
	/**
	 * This method is used to populate available Mtc Models.
	 */
	public List<QiMtcToEntryModelDto> getAvailableMtcModelData(String filter, String productType) {
		List<QiMtcToEntryModelDto> availableMtcModelList = getAvailableMtcModelList(filter, productType);
		List<QiMtcToEntryModelDto> existingList = getExistingMtcModel(productType);
		
		if(isDieCastProduct(productType)) {
			removeAssignedForDieCast(availableMtcModelList, existingList);
		} else  {
			removeAssigned(availableMtcModelList, existingList);
		}
		Collections.sort(availableMtcModelList, new ObjectComparator<QiMtcToEntryModelDto>("availablelMtcModels"));
		return availableMtcModelList;
	}

	private void removeAssigned(List<QiMtcToEntryModelDto> availableMtcModelList,
			List<QiMtcToEntryModelDto> existingList) {
		if (!availableMtcModelList.isEmpty()) {
			availableMtcModelList.removeAll(existingList);
			for(QiMtcToEntryModelDto assignedMtcModelList : getView().getAssignModelGroupingTablePane().getTable().getItems()){
				QiMtcToEntryModelDto assignedMTCModel = new QiMtcToEntryModelDto();
				assignedMTCModel.setModelYearCode(assignedMtcModelList.getMtcModel().substring(0, 1));
				assignedMTCModel.setModelCode(assignedMtcModelList.getMtcModel().substring(1));
				availableMtcModelList.remove(assignedMTCModel);
			}
		}
	}

	private void removeAssignedForDieCast(List<QiMtcToEntryModelDto> availableMtcModelList,
			List<QiMtcToEntryModelDto> existingList) {
		if (!availableMtcModelList.isEmpty()) {
			availableMtcModelList.removeAll(existingList);
			for(QiMtcToEntryModelDto assignedMtcModelList : getView().getAssignModelGroupingTablePane().getTable().getItems()){
				QiMtcToEntryModelDto assignedMTCModel = new QiMtcToEntryModelDto();
				assignedMTCModel.setModelCode(assignedMtcModelList.getMtcModel());
				availableMtcModelList.remove(assignedMTCModel);
			}
		}
	}

	private List<QiMtcToEntryModelDto> getExistingMtcModel(String productType) {
		return isDieCastProduct(productType) ? findExistingMtcModelForDieCast(getModel().findAllEntryModelGroupingByProductType(productType)) :
			findExistingMtcModel(getModel().findAllEntryModelGroupingByProductType(productType));
	}

	private List<QiMtcToEntryModelDto> getAvailableMtcModelList(String filter, String productType) {
		List<QiMtcToEntryModelDto> availableMtcModelList = new ArrayList<QiMtcToEntryModelDto>();

		if(isDieCastProduct(productType)) {
			availableMtcModelList = findAvailableMtcModelListDieCast(filter, productType);
		}
		else if (isMbpnProduct(getSelectedProductType())) {
			availableMtcModelList = findAvailableMtcModelListMbpn(filter, productType); 
		} 
		else {
			availableMtcModelList = findAvailableMtcModelList(filter, productType); 
		}
		return availableMtcModelList;
	}
	
	private List<QiMtcToEntryModelDto> findAvailableMtcModelListDieCast(String filter, String productType) {
		List<QiMtcToEntryModelDto> mtcModelList = getModel().findAvailableMtcModelDataForDieCast(filter, productType);
		return findAvailableMtcByDieCastProduct(mtcModelList, productType);
	}
	
	private List<QiMtcToEntryModelDto> findAvailableMtcModelListMbpn(String filter, String productType) {
		List<QiMtcToEntryModelDto> mtcModelList = getModel().findAvailableMtcModelData(filter, productType);
		return findAvailableMtcByMbpn(mtcModelList);
	}
	
	private List<QiMtcToEntryModelDto> findAvailableMtcModelList(String filter, String productType) {
		List<QiMtcToEntryModelDto> mtcModelList = getModel().findAvailableMtcModelData(filter, productType);
		return findAvailableMtcByProduct(mtcModelList, productType);
	}

	private boolean isDieCastProduct(String productType) {
		return ProductTypeUtil.isDieCast(ProductTypeCatalog.getProductType(productType.trim()));
	}
	
	private boolean isMbpnProduct(String productType) {
		return ProductTypeUtil.isMbpnProduct(productType) ;
	}
	
	private String getSelectedProductType() {
		return StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem());
	}

	/**
	 * This method is used to populate existing Mtc Models.
	 */
	private List<QiMtcToEntryModelDto> findExistingMtcModel(List<QiEntryModelGrouping> mtcModelList) {
		List<QiMtcToEntryModelDto> assgndMtcList = new ArrayList<QiMtcToEntryModelDto>();
		if (mtcModelList != null) {
			QiMtcToEntryModelDto qiEntrytoMtcDto;
			for (QiEntryModelGrouping object : mtcModelList) {
				qiEntrytoMtcDto = new QiMtcToEntryModelDto();
				String mtcModel = object.getId().getMtcModel();
				qiEntrytoMtcDto.setModelYearCode(StringUtils.trimToEmpty(mtcModel).substring(0, 1));
				qiEntrytoMtcDto.setModelCode(StringUtils.trimToEmpty(mtcModel).substring(1, StringUtils.trimToEmpty(mtcModel).length()));
				assgndMtcList.add(qiEntrytoMtcDto);
			}
		}
		return assgndMtcList;
	}
	
	/**
	 * This method is used to populate existing Mtc Models for DieCast Products.
	 */
	private List<QiMtcToEntryModelDto> findExistingMtcModelForDieCast(List<QiEntryModelGrouping> mtcModelList) {
		List<QiMtcToEntryModelDto> assgndMtcList = new ArrayList<QiMtcToEntryModelDto>();
		if (mtcModelList != null) {
			QiMtcToEntryModelDto qiEntrytoMtcDto;
			for (QiEntryModelGrouping object : mtcModelList) {
				qiEntrytoMtcDto = new QiMtcToEntryModelDto();
				String mtcModel = object.getId().getMtcModel();
				qiEntrytoMtcDto.setModelCode(StringUtils.trimToEmpty(mtcModel));
				assgndMtcList.add(qiEntrytoMtcDto);
			}
		}
		return assgndMtcList;
	}
	
	/**
	 * This method is used to populate Mtc Models excluding null and empty values.
	 */
	private List<QiMtcToEntryModelDto> findAvailableMtcByProduct(List<QiMtcToEntryModelDto> availableMtcList, String productType) {
		List<QiMtcToEntryModelDto> availableMtcModelList=new ArrayList<QiMtcToEntryModelDto>();
		for(QiMtcToEntryModelDto dto :availableMtcList){
			if (null !=dto.getModelYearCode() && null !=dto.getModelCode()) {
				if (!StringUtils.isEmpty(StringUtils.trimToEmpty(dto.getModelYearCode()))&& !StringUtils.isEmpty(StringUtils.trimToEmpty(dto.getModelCode()))) {
					dto.setProductType(productType);
					availableMtcModelList.add(dto);
				}
			}
		}
		return availableMtcModelList;
	}
	
	/**
	 * This method is used to populate Mtc Models for Diecast product excluding null and empty values.
	 */
	private List<QiMtcToEntryModelDto> findAvailableMtcByDieCastProduct(List<QiMtcToEntryModelDto> availableMtcList, String productType) {
		List<QiMtcToEntryModelDto> availableMtcModelList=new ArrayList<QiMtcToEntryModelDto>();
		for(QiMtcToEntryModelDto dto :availableMtcList){
			if(null !=dto.getModelDescription()) {
				if (!StringUtils.isEmpty(StringUtils.trimToEmpty(dto.getModelDescription()))) {
					dto.setModelDescription(dto.getModelDescription());
				}
			}
			if (null !=dto.getModelCode()) {
				if (!StringUtils.isEmpty(StringUtils.trimToEmpty(dto.getModelCode()))) {
					dto.setProductType(productType);
					availableMtcModelList.add(dto);
				}
			}
		}
		return availableMtcModelList;
	}

	/**
	 * This method is used to populate Mtc Models excluding null and empty values for Mbpn.
	 */
	private List<QiMtcToEntryModelDto> findAvailableMtcByMbpn(List<QiMtcToEntryModelDto> availableMtcListByMbpn) {
		List<QiMtcToEntryModelDto> availableMtcModelList=new ArrayList<QiMtcToEntryModelDto>();
		for(QiMtcToEntryModelDto dto :availableMtcListByMbpn){
			dto.setModelYearDescription("");
			dto.setProductType(ProductType.MBPN.name());
			if (null != dto.getClassNo() && !StringUtils.isEmpty(StringUtils.trimToEmpty(dto.getClassNo()))) {	
				dto.setModelYearCode(StringUtils.trimToEmpty(dto.getClassNo()).substring(0, 1));
				if (!(StringUtils.trimToEmpty(dto.getClassNo()).length() > 1))
					dto.setModelCode(null);
				else
					dto.setModelCode(StringUtils.trimToEmpty(dto.getClassNo()).substring(1,StringUtils.trimToEmpty(dto.getClassNo()).length()));
				availableMtcModelList.add(dto);
			}
		}
		return availableMtcModelList;
	}

	/**
	 * This method is used to assign Mtc Models to Entry Model.
	 */
	private void assignButtonAction(ActionEvent event) {
		List<QiMtcToEntryModelDto> availableMtcMdellist = new ArrayList<QiMtcToEntryModelDto>();
		for (QiMtcToEntryModelDto dto : getView().getQiAvailableMtcModelgroupingTablePane().getTable().getSelectionModel().getSelectedItems()) {
			if (null != dto) {
				String modelCode = dto.getModelCode();
				String modelYearCode = dto.getModelYearCode();
				dto.setMtcModelForPane(modelYearCode, modelCode);
				availableMtcMdellist.add(dto);
			}
		}
		getView().getAssignModelGroupingTablePane().getTable().getItems().addAll(availableMtcMdellist);
		availableMtcMdellist = null;
		if (null != getView().getQiAvailableMtcModelgroupingTablePane().getTable().getSelectionModel().getSelectedItems()) {
			getView().getQiAvailableMtcModelgroupingTablePane().getTable().getItems().removeAll(getView().getQiAvailableMtcModelgroupingTablePane().getTable().getSelectionModel().getSelectedItems());
		}
	}
	/**
	 * This method is used to deassign Mtc Models from Entry Model.
	 */
	private void deassignButtonAction(ActionEvent event) {
		List<QiMtcToEntryModelDto> assignedMtclist = new ArrayList<QiMtcToEntryModelDto>();
		for (QiMtcToEntryModelDto dto : getView().getAssignModelGroupingTablePane().getTable().getSelectionModel().getSelectedItems()) {
			if (null != dto) {
				String mtcModel = StringUtils.trimToEmpty(dto.getMtcModel());
				String modelYearCode, modelCode = null;
				modelYearCode = mtcModel.substring(0, 1);
				if (mtcModel.length() > 1){
					modelCode = mtcModel.substring(1, mtcModel.length());
				}
				dto.setModelYearCode(modelYearCode);
				dto.setModelCode(modelCode);
				assignedMtclist.add(dto);
			}
		}
		getView().getQiAvailableMtcModelgroupingTablePane().getTable().getItems().addAll(assignedMtclist);
		assignedMtclist = null;
		if (null != getView().getAssignModelGroupingTablePane().getTable().getSelectionModel().getSelectedItems()) {
			getView().getAssignModelGroupingTablePane().getTable().getItems().removeAll(getView().getAssignModelGroupingTablePane().getTable().getSelectionModel().getSelectedItems());
		}
	}
	/**
	 * This method is called to add context menus to Entry Model table.
	 */
	@Override
	public void addContextMenuItems() {
		ObjectTablePane<QiEntryModel> tablePane = getView().getQiEntryModelTablePane();
		String[] menuItems;

		if (null != getView().getProductType().getValue()
				&& !StringUtils.EMPTY.equals(getView().getProductType().getValue())) {
			if (null != getView().getQiEntryModelTablePane().getSelectedItem()) {
				QiEntryModel qiEntryModel = getView().getQiEntryModelTablePane().getSelectedItem();
				boolean isVersionCreated = getModel().isVersionCreated(qiEntryModel.getId().getEntryModel());
				
				if (getView().getAllRadioBtn().isSelected()) {

					if (qiEntryModel.getId().getIsUsed() == (short) 1 && qiEntryModel.isActive()) {
						if (isVersionCreated)  {
							menuItems = new String[] { QiConstant.CREATE, QiConstant.IMPORT, QiConstant.EXPORT};
						}
						else  {
							menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.DELETE, QiConstant.CREATE_VERSION, QiConstant.IMPORT, QiConstant.EXPORT };
						}
					} else if (qiEntryModel.getId().getIsUsed() == (short) 0 && qiEntryModel.isActive()) {
						if (isVersionCreated)  {
							menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.DELETE, QiConstant.IMPORT, QiConstant.APPLY_VERSION };
						}
						else  {
							menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.DELETE, QiConstant.IMPORT};
						}
					} else {
						menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.DELETE, QiConstant.IMPORT};
					}

				} else {
					
					if (getView().getQiEntryModelTablePane().getSelectedItem().isActive()) {
						
						if (qiEntryModel.getId().getIsUsed() == (short) 1) {
							if (isVersionCreated)  {
								menuItems = new String[] { QiConstant.CREATE,QiConstant.EXPORT};
							}
							else  {
								menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.INACTIVATE, QiConstant.DELETE, QiConstant.CREATE_VERSION , QiConstant.EXPORT};
							}
						} else if(qiEntryModel.getId().getIsUsed() == (short) 0) {
							if (isVersionCreated)  {
								menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.DELETE, QiConstant.APPLY_VERSION};
							}
							else  {
								menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.INACTIVATE, QiConstant.DELETE};
							}
						} else {
							menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.INACTIVATE, QiConstant.DELETE, QiConstant.EXPORT};
						}
						
					} else  {
						menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.REACTIVATE, QiConstant.DELETE, QiConstant.IMPORT};
					}
				}
			} else  {
				menuItems = new String[] { QiConstant.CREATE, QiConstant.IMPORT};
			}
			tablePane.createContextMenu(menuItems, this);
		}
	}
	
	/**
	 * This method is used to set assigned MTC model and description to QiMtcToEntryModelDto.
	 */
	private List<QiMtcToEntryModelDto> setMtcModelYearDescriptionToDto(List<QiEntryModelGrouping> assginedMtcModel) {
		List<QiMtcToEntryModelDto> assignedMtcModelList = new ArrayList<QiMtcToEntryModelDto>();
		if (assginedMtcModel != null) {
			for (QiEntryModelGrouping qiEntryModelGrouping : assginedMtcModel) {
				QiMtcToEntryModelDto qiEntrytoMtcDto = new QiMtcToEntryModelDto();
				qiEntrytoMtcDto.setMtcModel(qiEntryModelGrouping.getId().getMtcModel());
				if(isDieCastProduct(getView().getProductType().getSelectionModel().getSelectedItem())) {
					qiEntrytoMtcDto.setModelYearDescription("");	

				} else if (!(ProductTypeUtil.isMbpnProduct(StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem())))) {
					qiEntrytoMtcDto.setModelYearDescription(getModel().findAvailableMtcModelData(qiEntryModelGrouping.getId().getMtcModel(),StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem())).get(0).getModelYearDescription());
				} else {
					qiEntrytoMtcDto.setModelYearDescription("");	
				}
				assignedMtcModelList.add(qiEntrytoMtcDto);
			}
		}
		return assignedMtcModelList;
	}
	/**
	 * This method is used to disable the content of MTC Model data when no Entry Model is selected
	 */
	private void disableMTCmodelTables() {
		getView().getQiAvailableMtcModelgroupingTablePane().setDisable(true);
		getView().getAssignModelGroupingTablePane().setDisable(true);
		getView().getMtcModelNameFilterTextField().setDisable(true);
	}

	private void deleteEntryModel(ActionEvent event) {
		Integer activeEntryScreens=0;
		List<QiEntryModelGrouping> entryModelGroupingList = new ArrayList<QiEntryModelGrouping>();
		List<QiEntryScreen> deletedEntryScreenList = new ArrayList<QiEntryScreen>();
		for(QiEntryScreen qiEntryScreen : getModel().findAllByEntryModel(getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel(),getView().getQiEntryModelTablePane().getSelectedItem().getId().getIsUsed())){
			if(qiEntryScreen.isActive()){
				activeEntryScreens++;
			}else{
				deletedEntryScreenList.add(qiEntryScreen);
			}
		}
		entryModelGroupingList = getModel().getAssginedMtcModel(getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel(),getView().getQiEntryModelTablePane().getSelectedItem().getId().getIsUsed());
		if(getView().getQiEntryModelTablePane().getSelectedItem().getId().getIsUsed()==(short) 0) { 
			try{	
				if(entryModelGroupingList.size()!=0){
					displayErrorMessage("Cannot delete as " + getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel() + " is associated with " + entryModelGroupingList.size() + " MTC model(s)");
					return;
				}else if(getView().getQiEntryModelTablePane().getSelectedItem().isActive()){
					displayErrorMessage("Cannot delete as " + getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel() + " is still in active state");
					return;	
				}else {
					ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
					if (dialog.showReasonForChangeDialog(null)) {
						getModel().deleteVersionDataFromDependentTables(getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel(), (short)0);
						getModel().removeEntryModel(getView().getQiEntryModelTablePane().getSelectedItem());
						AuditLoggerUtil.logAuditInfo(getView().getQiEntryModelTablePane().getSelectedItem(), null, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
						getView().reloadEntrymodel(StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()));
						disableMTCmodelTables();
					}
				}
			}catch (Exception e) {
				handleException("An error occured while delete ", "Failed to delete Entry Model", e);
			}
		}
		else {
		if(entryModelGroupingList.size()!=0 || activeEntryScreens!=0){
			try{	
				if(entryModelGroupingList.size()==0){
					displayErrorMessage("Cannot delete as " + getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel() + " is associated with " + activeEntryScreens + " active Entry Screen(s)");
					return;
				} else if(activeEntryScreens==0){
					displayErrorMessage("Cannot delete as " + getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel() + " is associated with " + entryModelGroupingList.size() + " MTC model(s)");
					return;	
				} else{
					displayErrorMessage("Cannot delete as " + getView().getQiEntryModelTablePane().getSelectedItem().getId().getEntryModel() + " is associated with "+entryModelGroupingList.size()+" MTC model(s) and " + activeEntryScreens + " active Entry Screen(s)");
					return;	
				}
			}catch (Exception e) {
				handleException("An error occured while delete ", "Failed to delete Entry Model", e);
			}
		 }else {
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if (dialog.showReasonForChangeDialog(null)) {
				try {
					if(!deletedEntryScreenList.isEmpty()){
					  getModel().deleteInactiveEntryScreens(deletedEntryScreenList);
					}	
					getModel().removeEntryModel(getView().getQiEntryModelTablePane().getSelectedItem());
					QiEntryModel qiEntryModel=getView().getQiEntryModelTablePane().getSelectedItem();
					AuditLoggerUtil.logAuditInfo(qiEntryModel, null, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
					getView().reloadEntrymodel(StringUtils.trimToEmpty(getView().getProductType().getSelectionModel().getSelectedItem()));
					disableMTCmodelTables();
				}catch (Exception e) {
					handleException("An error occured while delete ", "Failed to delete Entry Model", e);
				}
			}
		}
	  }
	}
	
	private void applyEntryModelVersion(ActionEvent event) {
		try {
			QiEntryModel qiEntryModel = getView().getQiEntryModelTablePane().getTable().getSelectionModel().getSelectedItem();
			String entryModel  = qiEntryModel.getId().getEntryModel();
			List <QiLocalDefectCombination> localDefectCombinations = getModel().findAllByEntryModelAndVersion(entryModel, (short)1);
			Map<Integer, Integer> localDefectVersionMap = new HashMap<Integer, Integer>();
			
			//Update External System - update LocalDefectCombinationID to newly activated LocalDefectCombinationID
			for (QiLocalDefectCombination qiLocalDefectCombination : localDefectCombinations) {
				List<QiExternalSystemDefectMap> externalSystemDefectMaps = getModel()
						.findAllByLocalCombinationId(qiLocalDefectCombination.getLocalDefectCombinationId());
				if (externalSystemDefectMaps.size() > 0) {
					List<QiLocalDefectCombination> localDefectVersionRecords = getModel().findAllByLocalDefectCombination(qiLocalDefectCombination);
					if (localDefectVersionRecords.size() > 0) {
						localDefectVersionMap.put(qiLocalDefectCombination.getLocalDefectCombinationId(),
								localDefectVersionRecords.get(0).getLocalDefectCombinationId());
					}
				}
			}
			
			if(localDefectVersionMap.size() > 0) {
				getModel().updateExternalSystem(localDefectVersionMap);
			}
			// Delete all records with Entry Model name and version is 1
			getModel().deleteVersionDataFromDependentTables(entryModel, (short)1);
			getModel().removeEntryModel(qiEntryModel);
			
			//update version 0 to 1 for Entry Model and oldVersion is 0
			getModel().updateEntryModelVersion(entryModel, (short)0, (short)1, true);
		}catch (Exception e) {
			handleException("An error occured in Apply Version ", "Failed to Apply Version", e);
		}
		refreshBtnAction();
		activate();
		EventBusUtil.publish(new StatusMessageEvent("Entry Model Version Apply successfully", StatusMessageEventType.INFO));
	}

	private void createEntryModelVersion(ActionEvent event) {
		QiEntryModel qiEntryModel = getView().getQiEntryModelTablePane().getTable().getSelectionModel().getSelectedItem();
		if(!getModel().isMtcAssignedToModel(qiEntryModel.getId().getEntryModel(), (short)1)) {
			displayErrorMessage("Please assigned MTC Models");
			return;
		}
		
		qiEntryModel.getId().setIsUsed((short)0);
		
		try {
			getModel().createEntryModel(qiEntryModel);
			createVersionDataForDependentTables(qiEntryModel.getId().getEntryModel());
			
		}catch (Exception e) {
			handleException("An error occured in Create Version ", "Failed to Create Version", e);
		}
		refreshBtnAction();
		activate();
		EventBusUtil.publish(new StatusMessageEvent("Entry Model Version Created successfully", StatusMessageEventType.INFO));
	}

	private void createVersionDataForDependentTables(String entryModel) {
		
		List<QiEntryModelGrouping> oldEntryModelGroupingVersionData = getModel().getAssginedMtcModel(entryModel,(short)1);
		
		List<QiEntryModelGrouping> newEntryModelGroupingVersionData = new ArrayList<QiEntryModelGrouping>();
		for (QiEntryModelGrouping qiEntryModelGrouping : oldEntryModelGroupingVersionData) {
			newEntryModelGroupingVersionData.add((QiEntryModelGrouping)qiEntryModelGrouping.deepCopy());
		}
		
		//set isUsed 0 for new version record
		for (QiEntryModelGrouping newEntryModelGrouping : newEntryModelGroupingVersionData) {
			newEntryModelGrouping.getId().setIsUsed((short) 0);
		}
		
		List<QiEntryScreen> oldEntryScreenOldVersionData = getModel().findAllByEntryModel(entryModel,(short)1);
		
		List<QiEntryScreen> newEntryScreenVersion = new ArrayList<QiEntryScreen>();
		for (QiEntryScreen qiEntryScreen : oldEntryScreenOldVersionData) {
			newEntryScreenVersion.add((QiEntryScreen)qiEntryScreen.deepCopy());
		}
		
		//set isUsed 0 for new version record
		for (QiEntryScreen qiEntryScreen : newEntryScreenVersion) {
			qiEntryScreen.getId().setIsUsed((short) 0);
		}
		
		List<QiEntryScreenDept> oldEntryScreenDeptVersionData = getModel().findAllDeptByEntryModel(entryModel, (short)1);
		
		List<QiEntryScreenDept> newEntryScreenDeptVersionData = new ArrayList<QiEntryScreenDept>();
		for (QiEntryScreenDept oldQiEntryScreenDept : oldEntryScreenDeptVersionData) {
			newEntryScreenDeptVersionData.add((QiEntryScreenDept)oldQiEntryScreenDept.deepCopy());
		}
		
		//set isUsed 0 for new version record
		for (QiEntryScreenDept entryScreenDept : newEntryScreenDeptVersionData) {
			entryScreenDept.getId().setIsUsed((short) 0);
		}
		
		List<QiTextEntryMenu> oldTextEntryMenuVersionData = getModel().findAllTextMenuByEntryModel(entryModel, (short)1);
		
		List<QiTextEntryMenu> newTextEntryMenuVersionData = new ArrayList<QiTextEntryMenu>();
		for (QiTextEntryMenu oldTextEntryMenu : oldTextEntryMenuVersionData) {
			newTextEntryMenuVersionData.add((QiTextEntryMenu)oldTextEntryMenu.deepCopy());
		}
		
		//set isUsed 0 for new version record
		for (QiTextEntryMenu textEntryMenu : newTextEntryMenuVersionData) {
			textEntryMenu.getId().setIsUsed((short) 0);
		}
		
		List<QiEntryScreenDefectCombination> oldEntryScreenDefectCombinationVersionData = getModel().findAllDefectCombinationByEntryModel(entryModel, (short)1);
		
		List<QiEntryScreenDefectCombination> newEntryScreenDefectCombination = new ArrayList<QiEntryScreenDefectCombination>();
		for (QiEntryScreenDefectCombination qiEntryScreenDefectCombination : oldEntryScreenDefectCombinationVersionData) {
			newEntryScreenDefectCombination.add((QiEntryScreenDefectCombination)qiEntryScreenDefectCombination.deepCopy());
		}
		
		//set isUsed 0 for new version record
		for (QiEntryScreenDefectCombination qiEntryScreenDefectCombination : newEntryScreenDefectCombination) {
			qiEntryScreenDefectCombination.getId().setIsUsed((short) 0);
		}
		
		List<QiLocalDefectCombination> oldLocalDefectCombinationVersionData = getModel().findAllAssociatedLocalDefectCombinationByEntryModelAndIsUsed(entryModel, (short)1);

		getModel().insertVersionDataInDependentTables(newEntryModelGroupingVersionData, newEntryScreenVersion, newEntryScreenDeptVersionData,
				newTextEntryMenuVersionData, newEntryScreenDefectCombination);
		createLocalDefectCombinationDao(oldLocalDefectCombinationVersionData);
	}
	
	private void createLocalDefectCombinationDao(List<QiLocalDefectCombination> qiLocalDefectCombinationVersionData) {
		
		for (QiLocalDefectCombination localDefect : qiLocalDefectCombinationVersionData) {
			QiLocalDefectCombination qiLocalDefectCombination = new QiLocalDefectCombination();
			qiLocalDefectCombination.setRegionalDefectCombinationId((Integer)localDefect.getRegionalDefectCombinationId());
			qiLocalDefectCombination.setEntrySiteName(localDefect.getEntrySiteName());
			qiLocalDefectCombination.setEntryPlantName(localDefect.getEntryPlantName());
			qiLocalDefectCombination.setResponsibleLevelId(localDefect.getResponsibleLevelId());
			qiLocalDefectCombination.setPddaResponsibilityId(localDefect.getPddaResponsibilityId());
			qiLocalDefectCombination.setEntryModel(localDefect.getEntryModel());
			qiLocalDefectCombination.setEntryScreen(localDefect.getEntryScreen());
			qiLocalDefectCombination.setIsUsed((short)0);
			qiLocalDefectCombination.setTextEntryMenu(localDefect.getTextEntryMenu());
			qiLocalDefectCombination.setRepairMethod(localDefect.getRepairMethod());
			qiLocalDefectCombination.setRepairMethodTime(localDefect.getRepairMethodTime());
			qiLocalDefectCombination.setEstimatedTimeToFix(localDefect.getEstimatedTimeToFix());
			qiLocalDefectCombination.setLocalTheme(localDefect.getLocalTheme());
			qiLocalDefectCombination.setEngineFiringFlag(localDefect.getEngineFiringFlag());
			qiLocalDefectCombination.setRepairAreaName(localDefect.getRepairAreaName());
			qiLocalDefectCombination.setDefectCategoryName(localDefect.getDefectCategoryName());
			qiLocalDefectCombination.setReportable(localDefect.getReportable());
			qiLocalDefectCombination.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
			qiLocalDefectCombination.setCreateUser(getUserId());
			qiLocalDefectCombination.setUpdateTimestamp(null);
			qiLocalDefectCombination.setUpdateUser(null);
			getModel().createLocalDefectCombinationDao(qiLocalDefectCombination);	
		}
	}
}
