package com.honda.galc.client.teamleader.qi.controller;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;

import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.InspectionPartDialog;
import com.honda.galc.client.teamleader.qi.view.InspectionPartMaintPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.qi.QiBomQicsPartMapping;
import com.honda.galc.entity.qi.QiImageSection;
import com.honda.galc.entity.qi.QiInspectionPart;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiPartLocationCombination;
import com.honda.galc.util.AuditLoggerUtil;
/**
 * 
 * <h3>InspectionPartController Class description</h3>
 * <p>
 * InspectionPartController is used to load data in TableView and perform to the action on the RadioButton (All, Activate, Inactivate )etc.
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
public class InspectionPartController extends AbstractQiController<ItemMaintenanceModel, InspectionPartMaintPanel> implements EventHandler<ActionEvent> {
	private QiInspectionPart selectedPart;
	private List<QiInspectionPart> selectedPartList;
	private final static String PART_NAME_UPDATE_MESSAGE_WITH_ASSOCIATION = "The Part(s) being updated affects ";
	private final static String UPDATE_MESSAGE="Do you still want to continue?";
	
	public InspectionPartController(ItemMaintenanceModel model,InspectionPartMaintPanel view) {
		super(model, view);
	}
	
	@Override
	public void initEventHandlers() {
		if (isFullAccess()) {
			addTableListener();
		}
	}
	/**
	 * This method is added because initEventHandlers() is called before the selection listener is triggered.
	 */
	public void addContextMenuItems()
	{
		clearDisplayMessage();
		ObjectTablePane<QiInspectionPart> partTablePane = getView().getPartScreenTablePane();
		int partsCounted=partTablePane.getTable().getSelectionModel().getSelectedItems().size();
		String[] menuItems;
		if(selectedPart!=null){
			if(getView().getAllRadioBtn().isSelected()){
				if(partsCounted > 1) {
					menuItems = new String[] { QiConstant.CREATE};
				}else{
					menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE };
				}
				getView().getPartScreenTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}else if(getView().getActiveRadioBtn().isSelected())
			{
				if(partsCounted > 1) {
					menuItems = new String[] {QiConstant.INACTIVATE };
				}else{
					menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.INACTIVATE };
				}
				getView().getPartScreenTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
			else
			{
				if(partsCounted > 1) {
					menuItems = new String[] { QiConstant.REACTIVATE };
				}else{
					menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.REACTIVATE };
				}
				getView().getPartScreenTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
		}else{
			 menuItems = new String[] { QiConstant.CREATE };
		}
		partTablePane.createContextMenu(menuItems, this);
	}

	/**
	 * This method is used to create, edit ,inactivate,reactivate Part
	 */
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedRadioButton){
			loadStatusData();
		}
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			if(QiConstant.CREATE.equals(menuItem.getText()))
				createPart(actionEvent);
			else if(QiConstant.UPDATE.equals(menuItem.getText()))
				updatePart(actionEvent);
			else if(QiConstant.INACTIVATE.equals(menuItem.getText()))
				inactivatePart(actionEvent);
			else if(QiConstant.REACTIVATE.equals(menuItem.getText())) 
				reactivatePart(actionEvent);
		}
		if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			clearDisplayMessage();
			loadStatusData();
		}
	}
	
	private void addTableListener() {
		getView().getPartScreenTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiInspectionPart>() {
			public void changed(ObservableValue<? extends QiInspectionPart> arg0,
					QiInspectionPart arg1, QiInspectionPart arg2) {
				selectedPart=getView().getPartScreenTablePane().getSelectedItem();
				selectedPartList = getView().getPartScreenTablePane().getSelectedItems();
				addContextMenuItems();
			}
		});
		getView().getPartScreenTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}
	/**
	 * This method is used to create the Part
	 */
	private void createPart(ActionEvent event){
		InspectionPartDialog dialog = new InspectionPartDialog(QiConstant.CREATE, new QiInspectionPart(), getModel(),getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		if(!dialog.isCancel())  {
			loadStatusData();
		}
	}
	/**
	 * This method is used to edit the Part info. clicking on 'Update' context menu
	 */
	private void updatePart(ActionEvent event){
		
		InspectionPartDialog dialog = new InspectionPartDialog(QiConstant.UPDATE, selectedPart, getModel(),getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		if(!dialog.isCancel())  {
			loadStatusData(true, true);
		}
	}
	
	/**
	 * This method is used to Reactivate the Part
	 */
	private void reactivatePart(ActionEvent event){
		updatePartStatus(true);
	}
	/**
	 * This method is used to Inactivate the Part
	 */
	private void inactivatePart(ActionEvent event){
		updatePartStatus(false);
	}
	
	/**
	 *  This method is used to update the status of Part
	 * @param status
	 */
	private void updatePartStatus(boolean isActive){
		ReasonForChangeDialog dialog=null;
		try {
			if(isActive){
				updatePartStatus(dialog,isActive);
			}else{
				List<Integer> partLocationIdList= new ArrayList<Integer>();
				List<Integer> bomQicsPartList= new ArrayList<Integer>();
				for (QiInspectionPart selectedPart : selectedPartList) {
					List<QiPartLocationCombination> qiPartLocationCombinationsList =getModel().checkPartInPartLocCombination(selectedPart.getInspectionPartName());
					for (QiPartLocationCombination qiPartLocationCombination : qiPartLocationCombinationsList) {
						partLocationIdList.add(qiPartLocationCombination.getPartLocationId());
					}
					List<QiBomQicsPartMapping> bomQicsPartMappinglist =getModel().findAllByPartName(selectedPart.getInspectionPartName());
					int bomQicsPartSize=bomQicsPartMappinglist.size();
					if(bomQicsPartSize>0){
						bomQicsPartList.add(bomQicsPartSize);	
					}
				}
				
				if(partLocationIdList.size()>0 || bomQicsPartList.size()>0){
					showCountedPartsWithAssociatedScreens(dialog,partLocationIdList,isActive,bomQicsPartList);
				}else{
					updatePartStatus(dialog,isActive);
				}
			}
		} catch (Exception e) {
			handleException("Failed to inactivate/Reactivate part  ", "An error occurred at inactivatePart/reactivatePart method ",e);
		}
	}
	/**
	 * This method is used to load data into ObjectTablePane based on status
	 */
	private void loadStatusData(){
		loadStatusData(false, false);
	}
	/**
	 * This method is used to load data into ObjectTablePane based on status
	 */
	private void loadStatusData(boolean isPreserveListPosition, boolean isPreserveSelect){
		if(getView().getAllRadioBtn().isSelected())
			getView().reload(getView().getFilterTextData(), isPreserveListPosition, isPreserveSelect);
		else if(getView().getActiveRadioBtn().isSelected())
			getView().reload(getView().getFilterTextData(),QiActiveStatus.ACTIVE.getName(), isPreserveListPosition, isPreserveSelect);
		else
			getView().reload(getView().getFilterTextData(),QiActiveStatus.INACTIVE.getName(), isPreserveListPosition, isPreserveSelect);
	}
	/**
	 *  This method is used to update the status of Part without association of Part in Part location combination
	 * @param status
	 */
	private void updatePartStatus(ReasonForChangeDialog dialog,boolean status){
		QiInspectionPart selectedPartCloned;
		dialog = new ReasonForChangeDialog(getApplicationId());
		if(dialog.showReasonForChangeDialog(null))
		{
			try{
				for (QiInspectionPart selectedPart : selectedPartList) {
					getModel().updatePartStatus(selectedPart.getInspectionPartName(),status? (short) 1:(short) 0);
					selectedPartCloned=(QiInspectionPart) selectedPart.deepCopy();
					selectedPart.setActive(status);
					//call to prepare and insert audit data
					AuditLoggerUtil.logAuditInfo(selectedPartCloned, selectedPart, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
				}
				loadStatusData();
			}catch(Exception e){
				handleException("An error occured in updatePartStatus method " , "Failed to updatePartStatus Part Status ", e);
			}
		}
	}

	/**
	 * This method is used to show the number of counts of Associated screens (PLC,PDC and Image Section) and update the Part to the Associated screens as well
	 * @param dialog
	 * @param partLocationIdList
	 * @param isActive
	 */
	private void showCountedPartsWithAssociatedScreens(ReasonForChangeDialog dialog,List<Integer> partLocationIdList,boolean isActive,List<Integer> bomQicsPartList){
		int countedPartInPartLocComb = partLocationIdList.size();
		int countedPartInPartDefectComb=0 ;
		int countedPartInImageSection=0;
		int countedPartInRegionalAttribute=0;
		int countedPartInBomQics = bomQicsPartList.size();
		List<QiImageSection> partLocIdInImageSectionList = getModel().findPartLocationIdsInImageSection(partLocationIdList);
		List<QiPartDefectCombination> partLocIdInPartDefCombList = getModel().findPartLocationIdsInPartDefectCombination(partLocationIdList);
		List<QiPartDefectCombination> partLocationIdInPartDefComb=getModel().findAllPLCIdsByPartLocId(partLocationIdList);
		if(partLocIdInPartDefCombList!=null && partLocIdInPartDefCombList.size()>0){
			countedPartInPartDefectComb=partLocIdInPartDefCombList.size();
			countedPartInRegionalAttribute=partLocationIdInPartDefComb.size();
		}
		List<Integer> regionalDefectCombIdList = new ArrayList<Integer>();
		for(QiPartDefectCombination qiPartDefectCombination : partLocIdInPartDefCombList){
			regionalDefectCombIdList.add(qiPartDefectCombination.getRegionalDefectCombinationId());
		}
		
		StringBuilder message=new StringBuilder(PART_NAME_UPDATE_MESSAGE_WITH_ASSOCIATION);
		if(countedPartInPartLocComb>0){
			message.append(countedPartInPartLocComb + " Part Loc Comb, ");
		}
		if((partLocIdInImageSectionList!=null && partLocIdInImageSectionList.size()>0)){
			countedPartInImageSection=partLocIdInImageSectionList.size();
			if(countedPartInPartDefectComb>0)
				message.append(countedPartInPartDefectComb + " Part Defect Comb");
			if(countedPartInRegionalAttribute>0)
				message.append(","+countedPartInRegionalAttribute + " Regional Attribute ");
			if(countedPartInImageSection>0)
				message.append(","+countedPartInImageSection +" Image Section");
			
			MessageDialog.showError(getView().getStage(),message.toString());
		}else{
			if(countedPartInPartDefectComb>0)
				message.append(countedPartInPartDefectComb + " Part Defect Comb");
			if(countedPartInRegionalAttribute>0)
				message.append(","+countedPartInRegionalAttribute + " Regional Attribute ");
			if(countedPartInBomQics>0)
				message.append(","+countedPartInBomQics + " BOM Qics Part Association "); 
			message.append("."+UPDATE_MESSAGE);
			updatePartInAllAssociatedScreens(partLocIdInPartDefCombList,message.toString(),isActive,partLocationIdList);
		}
	}
	/**
	 * This method is used to update the Part to the Associated screens 
	 * @param partLocIdInPartDefCombList
	 * @param message
	 * @param isActive
	 */
	private void updatePartInAllAssociatedScreens(List<QiPartDefectCombination> partLocIdInPartDefCombList,String message,boolean isActive,List<Integer> partLocationIdList){
		QiInspectionPart selectedPartCloned;
		ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
		if(dialog.showReasonForChangeDialog(message.toString()))
		{
			String returnValue=isLocalSiteImpacted(partLocationIdList,getView().getStage());
			if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
				return;
			}
			else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
				publishErrorMessage("Inactivation of  part(s) affects Local Sites.");
				return;
			}
			try{
				for (QiInspectionPart selectedPart : selectedPartList) {
					getModel().inactivatePart(selectedPart.getInspectionPartName());
					getModel().updatePartStatus(selectedPart.getInspectionPartName(), isActive? (short) 1:(short) 0);
					selectedPartCloned=(QiInspectionPart) selectedPart.deepCopy();
					selectedPart.setActive(isActive);
					//call to prepare and insert audit data
					AuditLoggerUtil.logAuditInfo(selectedPartCloned, selectedPart, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
				}
				for (QiPartDefectCombination partDefectCombination: partLocIdInPartDefCombList) {
					getModel().inactivatePartinPartDefectCombination(partDefectCombination.getPartLocationId());
				}
				loadStatusData();
			}catch(Exception e){
				handleException("An error occured in updatePartStatus method " , "Failed to updatePartStatus Part Status ", e);
			}
		}
	}
	
}
