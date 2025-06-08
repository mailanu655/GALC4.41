package com.honda.galc.client.teamleader.datamigration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.AbstractQiController;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.DataMigrationDto;
import com.honda.galc.dto.qi.QiDefectCombinationResultDto;
import com.honda.galc.entity.qi.QiMappingCombination;
import com.honda.galc.entity.qi.QiMappingCombinationId;
import com.honda.galc.entity.qi.QiOldCombination;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * <h3>DataMigrationAssociationControllerClass description</h3> <h4>Description</h4>
 * <p> <code>DataMigrationAssociationController </code> is the Controller class for to populate data and create or delete defect combination association 
 * between NAQ Defect description and Old GALC defect combination
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
 * <TD></TD>
 * <TD>(none)</TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */

public class DataMigrationAssociationController extends AbstractQiController<DataMigrationAssociationMaintenanceModel, DataMigrationAssociationMaintenancePanel> implements EventHandler<ActionEvent>
{
	public DataMigrationAssociationController(DataMigrationAssociationMaintenanceModel model,
			DataMigrationAssociationMaintenancePanel view) {
		super(model, view);
	}

	private List<DataMigrationDto> selectedAssociatedDefectCombinationResult;
	private DataMigrationDto selectedQiOldCombination;

	@Override
	public void initEventHandlers() {
		addNaqDefectCombTablePaneListener();
		addLegacyDefectCombTablePaneListener();
		addAssociatedDefectCombinationListingTableListener();
	}

	/**
	 * This method is for NAQ Defect Description Table Listeners
	 */
	private void addNaqDefectCombTablePaneListener() {
		getView().getNaqDefectCombinationTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DataMigrationDto>() {
			public void changed(
					ObservableValue<? extends DataMigrationDto> arg0,
					DataMigrationDto arg1,
					DataMigrationDto arg2) {
				selectedAssociatedDefectCombinationResult = getView().getNaqDefectCombinationTablePane().getSelectedItems();
					fetchDefectCombinationResultList();
				if (isFullAccess()) 
					addContextMenuItems();
				
			}
		});
	}

	/**
	 * This method is for Legacy Defect Description Table Listeners
	 */
	private void addLegacyDefectCombTablePaneListener(){
		getView().getLegacyDefectCombTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DataMigrationDto>() {
			public void changed(
					ObservableValue<? extends DataMigrationDto> arg0,
					DataMigrationDto arg1,
					DataMigrationDto arg2) {
				selectedQiOldCombination = getView().getLegacyDefectCombTablePane().getSelectedItem();
				if (isFullAccess()) 
					addContextMenuItems();
			}
		});
	}

	/**
	 * This method is for Associated Defect Combination Table Listeners
	 */
	private void addAssociatedDefectCombinationListingTableListener() {
		getView().getAssociatedPartListingTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiMappingCombination>() {
			public void changed(
					ObservableValue<? extends QiMappingCombination> arg0,
					QiMappingCombination arg1,
					QiMappingCombination arg2) {
				getView().getLegacyDefectCombTablePane().getTable().getSelectionModel().clearSelection();
				if (isFullAccess())
				addContextMenuItems();
			}
		});
	}


	@Override
	public void addContextMenuItems() {
		clearDisplayMessage();
		if(selectedQiOldCombination!=null && null!=selectedAssociatedDefectCombinationResult && selectedAssociatedDefectCombinationResult.size()>=1 ){
			String[] menuItems = new String[] {
					QiConstant.CREATE
			};
			
			getView().getNaqDefectCombinationTablePane().createContextMenu(menuItems, this);
			getView().getLegacyDefectCombTablePane().createContextMenu(menuItems, this);
		
		} else if(getView().getAssociatedPartListingTablePane().getSelectedItem()!=null){
			String[] menuItems = new String[] {
					QiConstant.DELETE
			};	
			getView().getAssociatedPartListingTablePane().createContextMenu(menuItems, this);
		}else{
			if(null!=getView().getAssociatedPartListingTablePane().getTable().getContextMenu())
				getView().getAssociatedPartListingTablePane().getTable().getContextMenu().getItems().clear();	
			if (null == getView().getNaqDefectCombinationTablePane().getSelectedItem() ||
					null == getView().getLegacyDefectCombTablePane().getSelectedItem()) {
				clearContextMenu();
			}
		}
	}
	
	/**
	 * This method is used to clear the context menu 
	 */
	private void clearContextMenu(){
		if (null != getView().getNaqDefectCombinationTablePane().getTable().getContextMenu())
			getView().getNaqDefectCombinationTablePane().getTable().getContextMenu().getItems().clear();
		if (null != getView().getLegacyDefectCombTablePane().getTable().getContextMenu())
			getView().getLegacyDefectCombTablePane().getTable().getContextMenu().getItems().clear();
	}

	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			if(QiConstant.CREATE.equals(menuItem.getText())) createDefectCombinationAssociation(actionEvent);
			if(QiConstant.DELETE.equals(menuItem.getText())) deleteDefectCombinationAssociation(actionEvent);
		}
		else if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			clearDisplayMessage();
			if(getView().getLegacyPartFilterTextField().isFocused())
				getView().reload(StringUtils.trim(getView().getLegacyPartFilterTextField().getText()),"NGLC Filter");
			else if (getView().getNaqPartFilterTextField().isFocused())
				getView().reload(StringUtils.trim(getView().getNaqPartFilterTextField().getText()),"NAQ Filter");
			else if (getView().getAssociatedFilterTextField().isFocused())
				getView().reload(StringUtils.trim(getView().getAssociatedFilterTextField().getText()),"Associated Filter");
		}
	}

	/**
	 * This method fetches all associated defect combination List on select NAQ defect description
	 * @param newDtoList
	 */
	private void fetchDefectCombinationResultList() {
		clearDisplayMessage();
		List<QiMappingCombination> associatedDefectCombinationResultList;
		getView().getAssociatedPartListingTablePane().getTable().getItems().clear();
		final DataMigrationDto selectedNaqDefectDescription=getView().getNaqDefectCombinationTablePane().getTable().getSelectionModel().getSelectedItem();
		try{
			String filterText="";
			if(getView().getAssociatedFilterTextField().isFocused()){
				filterText=StringUtils.trim(getView().getAssociatedFilterTextField().getText());
			}
			if (selectedNaqDefectDescription!=null && selectedNaqDefectDescription.getRegionalDefectCombinationId()>0) 
				associatedDefectCombinationResultList= getModel().findAllAssociatedDefectCombinationResultByFilter(selectedNaqDefectDescription.getRegionalDefectCombinationId(),filterText);
			else {
				associatedDefectCombinationResultList= getModel().findAllAssociatedDefectCombinationResultByFilter(0,filterText);
			}
			getView().getAssociatedPartListingTablePane().setData(FXCollections.observableArrayList(associatedDefectCombinationResultList));
		}catch(Exception e){
			handleException("An error occured in fetching defect combination association ", "Failed to fetch defect combination association ", e);
		}
	}


	/**
	 * This method is called when create menu is clicked to create new defect combination association
	 * @param event
	 */
	public void createDefectCombinationAssociation(ActionEvent event){
		clearDisplayMessage();
		final List<QiMappingCombination> newMappingCombinationresult = new ArrayList<QiMappingCombination>();
		//find selected NAQ DEFECT Combination
		final DataMigrationDto selectedNaqDefectCombinationResult = getView().getNaqDefectCombinationTablePane().getSelectedItem();
		//find list of selected NGLC DEFECT Combination
		final List<DataMigrationDto> qiOldCombinationList = getView().getLegacyDefectCombTablePane().getTable().getSelectionModel().getSelectedItems();
		try{
			if(qiOldCombinationList.isEmpty() || selectedNaqDefectCombinationResult==null){
				displayErrorMessage("Please select atleast a NAQ defect description/NGLC defect combination");
			}else{
				mergeDefectCombination(newMappingCombinationresult, selectedNaqDefectCombinationResult, qiOldCombinationList);
				getModel().createAllDefectCombinationsAssociation(newMappingCombinationresult);
				getView().reload(StringUtils.trim(getView().getLegacyPartFilterTextField().getText()),"NGLC Filter");
				getView().reload(StringUtils.trim(getView().getAssociatedFilterTextField().getText()),"Associated Filter");
				if(null!=getView().getNaqDefectCombinationTablePane().getTable().getContextMenu()){
					getView().getNaqDefectCombinationTablePane().getTable().getContextMenu().getItems().clear();
				}
				if(null!=getView().getLegacyDefectCombTablePane().getTable().getContextMenu()){
					getView().getLegacyDefectCombTablePane().getTable().getContextMenu().getItems().clear();
				}
			}
		}catch(Exception e){
			handleException("An error occured in creating defect combination association ", "Failed to create defect combination association ", e);
		}
		getView().getNaqDefectCombinationTablePane().getTable().getSelectionModel().clearSelection();
		getView().getLegacyDefectCombTablePane().getTable().getSelectionModel().clearSelection();
		getView().getAssociatedPartListingTablePane().getTable().getSelectionModel().clearSelection();
	}
	
	/**
	 * this method is used to associating between NAQ Defect description and NGLC Defect Combination
	 */
	private void mergeDefectCombination(final List<QiMappingCombination> newMappingCombinationresult,final DataMigrationDto selectedNaqDefectCombinationResult,
			final List<DataMigrationDto> qiOldCombinationList) {
		QiMappingCombination qiMappingCombination;
		for (DataMigrationDto oldCombCode : qiOldCombinationList) {
			qiMappingCombination=new QiMappingCombination();
			QiMappingCombinationId qiMappingCombinationId=new QiMappingCombinationId(selectedNaqDefectCombinationResult.getRegionalDefectCombinationId(),oldCombCode.getPartDefectDesc());
			qiMappingCombination.setId(qiMappingCombinationId);
			QiDefectCombinationResultDto defectCombDto=getModel().findByRegionalDefectCombinationId(selectedNaqDefectCombinationResult.getRegionalDefectCombinationId());
			qiMappingCombination.setInspectionPartName(defectCombDto.getInspectionPartName());
			qiMappingCombination.setInspectionPartLocationName(defectCombDto.getInspectionPartLocationName());
			qiMappingCombination.setInspectionPartLocation2Name(defectCombDto.getInspectionPartLocation2Name());
			qiMappingCombination.setInspectionPart2Name(defectCombDto.getInspectionPart2Name());
			qiMappingCombination.setInspectionPart2LocationName(defectCombDto.getInspectionPart2LocationName());
			qiMappingCombination.setInspectionPart2Location2Name(defectCombDto.getInspectionPart2Location2Name());
			qiMappingCombination.setInspectionPart3Name(defectCombDto.getInspectionPart3Name());	
			qiMappingCombination.setDefectTypeName(defectCombDto.getDefectTypeName());
			qiMappingCombination.setDefectTypeName2(defectCombDto.getDefectTypeName2());
			QiOldCombination oldCombination=getModel().findByCombination(oldCombCode.getPartDefectDesc());
			qiMappingCombination.setOldInspectionPartName(oldCombination.getInspectionPartName());
			qiMappingCombination.setOldInspectionPartLocationName(oldCombination.getInspectionPartLocationName());  
			qiMappingCombination.setOldInspectionPartLocation2Name(oldCombination.getInspectionPartLocation2Name());
			qiMappingCombination.setOldInspectionPart2Name(oldCombination.getInspectionPart2Name());
			qiMappingCombination.setOldInspectionPart2LocationName(oldCombination.getInspectionPart2LocationName());
			qiMappingCombination.setOldInspectionPart2Location2Name(oldCombination.getInspectionPart2Location2Name());
			qiMappingCombination.setOldInspectionPart3Name(oldCombination.getInspectionPart3Name());		
			qiMappingCombination.setOldDefectTypeName(oldCombination.getDefectTypeName());
			qiMappingCombination.setOldDefectTypeName2(oldCombination.getDefectTypeName2());
			qiMappingCombination.setProductKind(defectCombDto.getProductKind());
			qiMappingCombination.setOldCombinationCode(oldCombination.getCombinationCode());
			newMappingCombinationresult.add(qiMappingCombination);
		}
	}

	/**
	 * on Click of delete context menu this method is called
	 * @param event
	 */
	private void deleteDefectCombinationAssociation(ActionEvent event){
		clearDisplayMessage();
		// get selected associated result for delete
		List<QiMappingCombination> existingAssociatedDefectCombinationList = new ArrayList<QiMappingCombination>(new HashSet<QiMappingCombination>(getView().getAssociatedPartListingTablePane().getTable().getSelectionModel().getSelectedItems()));
		if (existingAssociatedDefectCombinationList==null || existingAssociatedDefectCombinationList.isEmpty()){
			displayErrorMessage("Please select atleast single associated defect combination result");
		}
		else {	
			try{
				getModel().deleteAllDefectCombinationsAssociation(existingAssociatedDefectCombinationList);
				//get all updated associated result 
				final List<QiMappingCombination>  newAssociatedDefectCombinationResult=getModel().findAllAssociatedDefectCombinationResultByFilter(0,"");
				//get NGLC Defect Combination List 
				final List<DataMigrationDto>  availableLegacyDefectCombinationList=getModel().findAllOldCombinationByFilter("");
				getView().getAssociatedPartListingTablePane().setData(FXCollections.observableArrayList(newAssociatedDefectCombinationResult));
				getView().getLegacyDefectCombTablePane().setData(FXCollections.observableArrayList(availableLegacyDefectCombinationList));
			}catch(Exception e){
				handleException("An error occured in deleting defect combination association ", "Failed to delete defect combination association ", e);
			}
			getView().getNaqDefectCombinationTablePane().getTable().getSelectionModel().clearSelection();
			getView().getLegacyDefectCombTablePane().getTable().getSelectionModel().clearSelection();
			getView().getAssociatedPartListingTablePane().getTable().getSelectionModel().clearSelection();
		}
	}
	
	/**
	 * This method used to filter on defect combination  
	 * @param filterText
	 * @param filterName
	 */
	public void filterAllDefectCombinations(String filterText, String filterName){
		if (filterName.equalsIgnoreCase("NAQ Filter")) {
			getView().getNaqDefectCombinationTablePane().setData(FXCollections.observableArrayList(getModel().findAllNaqDefectCombinationByFilter(filterText)));
		} else if (filterName.equalsIgnoreCase("NGLC Filter")) {
			final List<DataMigrationDto> filteredLegacyDefectCombinations = getModel().findAllOldCombinationByFilter(filterText);
		    getView().getLegacyDefectCombTablePane().setData(FXCollections.observableArrayList(filteredLegacyDefectCombinations));
			
		} else if (filterName.equalsIgnoreCase("Associated Filter")) {
			fetchDefectCombinationResultList();
		}
	}
	
}
