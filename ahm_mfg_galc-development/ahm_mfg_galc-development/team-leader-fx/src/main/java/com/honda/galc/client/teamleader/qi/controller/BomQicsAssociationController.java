package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;

import com.honda.galc.client.teamleader.qi.model.BomQicsPartAssociationMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.BomQicsAssociationMaintPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.teamleader.qi.view.QiBomNewItemDialog;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiBomPartDto;
import com.honda.galc.entity.qi.QiBomQicsPartMapping;
import com.honda.galc.entity.qi.QiInspectionPart;
import com.honda.galc.util.AuditLoggerUtil;


/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BomQicsAssociationController </code> is the Controller class for Bom Qics Part Association .
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

public class BomQicsAssociationController extends AbstractQiController<BomQicsPartAssociationMaintenanceModel, BomQicsAssociationMaintPanel> implements EventHandler<ActionEvent>
{
	boolean isRefresh=false;
	
	public BomQicsAssociationController(BomQicsPartAssociationMaintenanceModel model,
			BomQicsAssociationMaintPanel view) {
		super(model, view);
	}

	private List<QiBomPartDto> selectedBomList;
	private QiInspectionPart selectedQics;

	@Override
	public void initEventHandlers() {
		
			addBomPartNameTableListener();
			addQicsPartNameTableListener();
			addAssociatedPartListingTableListener();
			
	}

	/**
	 * This method is for Bom Part Table Listeners
	 */
	private void addBomPartNameTableListener() {
		getView().getBomPartNameTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiBomPartDto>() {
			public void changed(
					ObservableValue<? extends QiBomPartDto> arg0,
					QiBomPartDto arg1,
					QiBomPartDto arg2) {
				if(!isRefresh){
					fetchAssociatedBomPartList();
					selectedBomList = getView().getBomPartNameTablePane().getSelectedItems();
					if (isFullAccess()) 
						addContextMenuItems();
				}
				
			}
		});
	}

	/**
	 * This method is for QICS Part Table Listeners
	 */
	private void addQicsPartNameTableListener(){
		getView().getQicsPartNameTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiInspectionPart>() {
			public void changed(
					ObservableValue<? extends QiInspectionPart> arg0,
					QiInspectionPart arg1,
					QiInspectionPart arg2) {
				if(!isRefresh){
					fetchAssociatedBomPartList();
					selectedQics = getView().getQicsPartNameTablePane().getSelectedItem();
					if (isFullAccess()) 
						addContextMenuItems();
				}
			}
		});
	}

	/**
	 * This method is for Bom Qics mapping Table Listeners
	 */
	private void addAssociatedPartListingTableListener() {
		getView().getAssociatedPartListingTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiBomPartDto>() {
			public void changed(
					ObservableValue<? extends QiBomPartDto> arg0,
					QiBomPartDto arg1,
					QiBomPartDto arg2) {
				if(!isRefresh){
					getView().getAssociatedPartListingTablePane().getSelectedItems();
					getView().getAssociatedPartListingTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					if (isFullAccess())
					addContextMenuItems();
				}
			}
		});
	}


	@Override
	public void addContextMenuItems() {
		clearDisplayMessage();
		if(getView().getNotAssociatedRadioButton().isSelected() || getView().getAssociatedRadioButton().isSelected() || getView().getAllRadioBtn().isSelected()) {
			String[] menuItems = new String[] {
					QiConstant.NEW
			};
			getView().getBomPartNameTablePane().createContextMenu(menuItems, this);
		};
		
		if(getView().getNotAssociatedRadioButton().isSelected() && selectedQics!=null && null!=selectedBomList && selectedBomList.size()>=1 ){
			String[] menuItems = new String[] {
					QiConstant.CREATE
			};
			getView().getBomPartNameTablePane().createContextMenu(menuItems, this);
			getView().getQicsPartNameTablePane().createContextMenu(menuItems, this);
		}
		if(getView().getAssociatedPartListingTablePane().getSelectedItem()!=null){
			String[] menuItems = new String[] {
					QiConstant.DELETE
			};	
			getView().getAssociatedPartListingTablePane().createContextMenu(menuItems, this);
		}else{
			if(null!=getView().getAssociatedPartListingTablePane().getTable().getContextMenu())
				getView().getAssociatedPartListingTablePane().getTable().getContextMenu().getItems().clear();	
		}
	}

	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedRadioButton){
			LoggedRadioButton radioButton = (LoggedRadioButton) actionEvent.getSource();
			getView().reload(getView().getFilterTextData(),"BOM Filter");
			if(radioButton.getText().equalsIgnoreCase("Not Associated"))
			{
				getView().getBomPartNameTablePane().getTable().getSelectionModel().clearSelection();
				getView().getQicsPartNameTablePane().getTable().getSelectionModel().clearSelection();
			}
			else if(radioButton.getText().equalsIgnoreCase("Associated")|| radioButton.getText().equalsIgnoreCase("All")){
				if(null!=getView().getBomPartNameTablePane().getTable().getContextMenu()){
					getView().getBomPartNameTablePane().getTable().getContextMenu().getItems().clear();
				}
				if(null!=getView().getQicsPartNameTablePane().getTable().getContextMenu()){
					getView().getQicsPartNameTablePane().getTable().getContextMenu().getItems().clear();
				}
				getView().getBomPartNameTablePane().getTable().getSelectionModel().clearSelection();
				getView().getQicsPartNameTablePane().getTable().getSelectionModel().clearSelection();
			}
		}

		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			if(QiConstant.NEW.equals(menuItem.getText())) newBomPartItem(actionEvent);
			if(QiConstant.CREATE.equals(menuItem.getText())) createBomPartAssociation(actionEvent);
			if(QiConstant.DELETE.equals(menuItem.getText())) deleteBomPartAssociation(actionEvent);
		}
		if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			if(getView().getQicsPartFilterTextField().isFocused())
				getView().reload(StringUtils.trim(getView().getQicsPartFilterTextField().getText()),"QICS Filter");
			else
				getView().reload(StringUtils.trim(getView().getBomPartFilterTextField().getText()),"BOM Filter");
			
			fetchAssociatedBomPartList();
		}
	}

	/**
	 * This method fetches all associated bom Part List on select Bom Part
	 * @param newDtoList
	 */
	private void fetchAssociatedBomPartList() {
		clearDisplayMessage();
		final List<QiBomPartDto> selectedBomPartsList = new ArrayList<QiBomPartDto>();

		selectedBomPartsList.addAll(getView().getBomPartNameTablePane().getTable().getSelectionModel().getSelectedItems());

		try{
			List<QiBomPartDto> associatedBomPartList = getModel().getNewBomPartMappingList(selectedBomPartsList,getView().getQicsPartNameTablePane().getTable().getSelectionModel().getSelectedItem());
			getView().getAssociatedPartListingTablePane().setData(associatedBomPartList);
			selectedBomPartsList.clear();
		}catch(Exception e){
			handleException("An error occured in fetching associatedBOMPartList ", "Failed to fetch associatedBOMPartList ", e);
		}

	}


	/**
	 * This method is called when create menu is clicked to create association
	 * @param event
	 */
	public void createBomPartAssociation(ActionEvent event){
		QiInspectionPart qiInspectionPart;
		QiBomQicsPartMapping qiBomQicsPartMapping;
		List<QiBomQicsPartMapping> qiBomQicsMappingList = new ArrayList<QiBomQicsPartMapping>();
		ObservableList<QiBomPartDto> bomPartObservableList = getView().getBomPartNameTablePane().getSelectedItems();
		List<QiBomPartDto> qiBomPartList = new ArrayList<QiBomPartDto>();
		for (QiBomPartDto qiBomPartDto: bomPartObservableList){
			qiBomPartDto.setDieCastPartNo(null);
			qiBomPartDto.setDieCastPartName(null);
			qiBomPartList.add(qiBomPartDto);
		}

		List<QiBomPartDto> associatedBomPartList = new ArrayList<QiBomPartDto>();
		clearDisplayMessage();
		try{
			qiInspectionPart = getView().getQicsPartNameTablePane().getTable().getSelectionModel().getSelectedItem();
			List<QiBomPartDto> uniqueBomPartList = new ArrayList<QiBomPartDto>(new HashSet<QiBomPartDto>(qiBomPartList));
			if(uniqueBomPartList.isEmpty() || qiInspectionPart==null){
				displayErrorMessage("Please select atleast a BOM Part and a QICS Part");
			}else{
				for (QiBomPartDto qiBomPart : uniqueBomPartList) {
					qiBomQicsPartMapping = new QiBomQicsPartMapping();
					qiBomQicsPartMapping.setMainPartNo(qiBomPart.getMainPartNo());
					qiBomQicsPartMapping.setInspectionPartName(qiInspectionPart.getInspectionPartName());
					qiBomQicsPartMapping.setCreateUser(getUserId());
					qiBomQicsMappingList.add(qiBomQicsPartMapping);
				}
				getModel().createBomPartAssociation(qiBomQicsMappingList);
				associatedBomPartList = getModel().getNewBomPartMappingList(uniqueBomPartList,getView().getQicsPartNameTablePane().getTable().getSelectionModel().getSelectedItem());
				getView().getAssociatedRadioButton().setSelected(true);
				getView().getQicsPartNameTablePane().clearSelection(); 
				getView().reload(getView().getFilterTextData(),"BOM Filter");
				getView().getAssociatedPartListingTablePane().setData(associatedBomPartList);
				if(null!=getView().getBomPartNameTablePane().getTable().getContextMenu()){
					getView().getBomPartNameTablePane().getTable().getContextMenu().getItems().clear();
				}
				if(null!=getView().getQicsPartNameTablePane().getTable().getContextMenu()){
					getView().getQicsPartNameTablePane().getTable().getContextMenu().getItems().clear();
				}

			}
		}catch(Exception e){
			handleException("An error occured in creating BOM Part association ", "Failed to create BOM Part association ", e);
		}
	}

	/**
	 * on Click of delete context menu this method is called
	 * @param event
	 */
	private void deleteBomPartAssociation(ActionEvent event){
		clearDisplayMessage();
		List<QiBomQicsPartMapping> qiBomQicsMappingList = new ArrayList<QiBomQicsPartMapping>();
		QiBomQicsPartMapping qiBomQicsPartMapping;
		ReasonForChangeDialog dialog=new ReasonForChangeDialog(getApplicationId());
		if(dialog.showReasonForChangeDialog(null)){
			try{

				ObservableList<QiBomPartDto> bomPartObservableList = getView().getAssociatedPartListingTablePane().getTable().getSelectionModel().getSelectedItems();
				List<QiBomPartDto> associatedBomPartList = new ArrayList<QiBomPartDto>(new HashSet<QiBomPartDto>(bomPartObservableList));
				for (QiBomPartDto qiBomPartDtoObj : associatedBomPartList) {
					qiBomQicsPartMapping = new QiBomQicsPartMapping();
					qiBomQicsPartMapping.setMainPartNo(qiBomPartDtoObj.getMainPartNo());
					qiBomQicsPartMapping.setInspectionPartName(qiBomPartDtoObj.getInspectionPart());
					qiBomQicsMappingList.add(qiBomQicsPartMapping);
					//call to prepare and insert audit data
					AuditLoggerUtil.logAuditInfo(qiBomQicsPartMapping, null, dialog.getReasonForChangeText(), getView().getScreenName(),getUserId());
				}
				getModel().deleteBomPartAssociation(qiBomQicsMappingList);
				associatedBomPartList = getModel().getNewBomPartMappingList(associatedBomPartList,getView().getQicsPartNameTablePane().getTable().getSelectionModel().getSelectedItem());
				getView().reload(getView().getFilterTextData(),"BOM Filter");
				getView().getBomPartNameTablePane().getTable().getSelectionModel().clearSelection();
				getView().getQicsPartNameTablePane().getTable().getSelectionModel().clearSelection();
				getView().getAssociatedPartListingTablePane().setData(associatedBomPartList);
			}catch(Exception e){
				handleException("An error occured in deleting BOM Part association ", "Failed to delete BOM Part association ", e);
			}
		}else
		{
			return;
		}

	}
	
	private void newBomPartItem(ActionEvent event){
		clearDisplayMessage();
		QiBomNewItemDialog dialog=new QiBomNewItemDialog();
		if(dialog.showBomNewItemDialog(null)){
			try{
				getView().reload(getView().getFilterTextData(),"BOM Filter");
				getView().getBomPartNameTablePane().getTable().getSelectionModel().clearSelection();
				getView().getQicsPartNameTablePane().getTable().getSelectionModel().clearSelection();
			}catch(Exception e){
				handleException("An error occured in adding new BOM item ", "Failed to add new BOM Part", e);
			}
		}else
		{
			return;
		}
	}

}
