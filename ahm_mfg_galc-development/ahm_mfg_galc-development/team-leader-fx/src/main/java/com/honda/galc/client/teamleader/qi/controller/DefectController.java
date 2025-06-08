package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
import com.honda.galc.client.teamleader.qi.view.DefectDialog;
import com.honda.galc.client.teamleader.qi.view.DefectMaintPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiDefect;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;

/**
 * 
 * <h3>DefectMaintController Class description</h3>
 * <p> DefectMaintController description </p>
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
 * @author L&T Infotech<br>
 * Aug 1, 2016
 *
 *
 */

public class DefectController extends AbstractQiController<ItemMaintenanceModel, DefectMaintPanel> implements EventHandler<ActionEvent> {

	private QiDefect qiDefectCloned;
	public DefectController(ItemMaintenanceModel model,DefectMaintPanel view) {
		super(model, view);
	}

	@Override
	public void initEventHandlers() {
		if (isFullAccess()) {
			addDefectTableListener();
		}
	}
	/**
	 * This method is for Defect Table Listeners
	 */
	private void addDefectTableListener() {
		getView().getDefectTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiDefect>() {
			public void changed(
					ObservableValue<? extends QiDefect> arg0,
					QiDefect arg1,
					QiDefect arg2) {
				addContextMenuItems();
			}
		});
		getView().getDefectTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}
	/**
	 * This method is added because initEventHandlers() is called before the selection listener is triggered.
	 */
	public void addContextMenuItems()
	{
		List<QiDefect> qiDefectList = getView().getDefectTablePane().getSelectedItems();
		clearDisplayMessage();
		if(qiDefectList.size()==1){
			if(getView().getAllRadioBtn().isSelected())
			{
				String[] menuItems = new String[] {
						QiConstant.CREATE, QiConstant.UPDATE
				};
				getView().getDefectTablePane().createContextMenu(menuItems, this);
				getView().getDefectTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}
			else if(getView().getActiveRadioBtn().isSelected()){
				String[] menuItems = new String[] {
						QiConstant.CREATE, QiConstant.UPDATE,QiConstant.INACTIVATE
				};
				getView().getDefectTablePane().createContextMenu(menuItems, this);
				getView().getDefectTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
			else
			{
				String[] menuItems = new String[] {
						QiConstant.CREATE, QiConstant.UPDATE,QiConstant.REACTIVATE
				};
				getView().getDefectTablePane().createContextMenu(menuItems, this);
				getView().getDefectTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
		}else if(qiDefectList.size()>1){
			if(getView().getActiveRadioBtn().isSelected()){
				String[] menuItems = new String[] {
						QiConstant.INACTIVATE
				};
				getView().getDefectTablePane().createContextMenu(menuItems, this);
			}else{
				String[] menuItems = new String[] {
						QiConstant.REACTIVATE
				};
				getView().getDefectTablePane().createContextMenu(menuItems, this);
			}
		}
		else{
			String[] menuItems = new String[] {
					QiConstant.CREATE
			};
			getView().getDefectTablePane().createContextMenu(menuItems, this);
		}
	}

	/**
	 * This method is to map the action event to context menu
	 */
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			QiDefect qiDefect = getView().getDefectTablePane().getSelectedItem();
			if(QiConstant.CREATE.equals(menuItem.getText())) createDefect(actionEvent);
			else if(QiConstant.UPDATE.equals(menuItem.getText())) updateDefect(actionEvent,qiDefect);
			else if(QiConstant.REACTIVATE.equals(menuItem.getText())) updateDefectStatus(actionEvent,true,qiDefect);
			else if(QiConstant.INACTIVATE.equals(menuItem.getText())) updateDefectStatus(actionEvent,false,qiDefect);
		} 
		if(actionEvent.getSource() instanceof LoggedRadioButton){
			getView().reload(getView().getFilterTextData());
		}
		if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			getView().reload(getView().getFilterTextData());
		}
	}

	/**
	 * This method is to open Popup dialog for Create Defect
	 */
	private void createDefect(ActionEvent actionEvent){
		clearDisplayMessage();
		try{
			DefectDialog dialog = new DefectDialog(QiConstant.CREATE, new QiDefect(), getModel(),getApplicationId());
			dialog.setScreenName(getView().getScreenName());
			dialog.showDialog();
			if(!dialog.isCancel())  {
				getView().reload(getView().getFilterTextData(), true, true);
			}
		}catch (Exception e) {
			handleException("An error occurred in createDefect method ", "Failed to open Create Defect popup ", e);
		}
	}


	/**
	 * This method is to open Popup dialog to inactivate/reactivate Defect
	 */
	private void updateDefectStatus(ActionEvent actionEvent, boolean isActive, QiDefect defect){
		clearDisplayMessage();
		try{
			List<QiDefect> qiDefectList = getView().getDefectTablePane().getSelectedItems();
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			List<QiPartDefectCombination> partDefectCombinationList = new ArrayList<QiPartDefectCombination>();
			List<QiPartDefectCombination> qiPartDefectCombinationsList = new ArrayList<QiPartDefectCombination>();
			List<Integer> partDefectIdList = new ArrayList<Integer>();
			List<String> entryScreenList = new ArrayList<String>();
			if(dialog.showReasonForChangeDialog(null))
			{
				try{
					if(defect.isActive()){
						for(QiDefect qiDefect : qiDefectList){
							qiPartDefectCombinationsList =getModel().findDefectInPartDefectCombination(qiDefect.getDefectTypeName());
							partDefectCombinationList.addAll((Collection<? extends QiPartDefectCombination>) qiPartDefectCombinationsList);
						}
					}

					List<QiPartDefectCombination> uniqueList = new ArrayList<QiPartDefectCombination>(new HashSet<QiPartDefectCombination>(partDefectCombinationList));

					/** To modify Part Defect Combination  */
					if(uniqueList.size()>0){
						for(QiPartDefectCombination qiPartDefectCombination : uniqueList){
							partDefectIdList.add(qiPartDefectCombination.getRegionalDefectCombinationId());
							for(QiEntryScreenDefectCombination qiEntryScreenDefectCombination : getModel().findDefectInEntryScreenDefectCombination(qiPartDefectCombination.getRegionalDefectCombinationId())){
								if(!entryScreenList.contains(qiEntryScreenDefectCombination.getId().getEntryScreen())){
									entryScreenList.add(qiEntryScreenDefectCombination.getId().getEntryScreen());
								}
							}
						}
						List<QiPartDefectCombination> regionalAttributeList= getModel().findAllRegionalAttributesByPartDefectId(partDefectIdList);
						boolean isDefectUpdated = false;
						isDefectUpdated = MessageDialog.confirm(getView().getStage(),QiCommonUtil.getMessage("defect(s)",qiPartDefectCombinationsList.size(),regionalAttributeList.size(),0).toString());
						if(!isDefectUpdated)
							return;
						else{
							String returnValue=isLocalSiteImpacted(partDefectIdList,getView().getStage());
							if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
								return;
							}
							else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
								publishErrorMessage("Inactivation of the defect affects Local Sites.");
								return;
							}
							updatePartDefectCombStatus(qiDefectList);
						}
					}
					for(QiDefect qiDefect : qiDefectList){
						getModel().updateDefectStatus(qiDefect.getDefectTypeName(),isActive? (short) 1:(short) 0);
						//added for audit logging
						this.qiDefectCloned  =(QiDefect) qiDefect.deepCopy();
						if(isActive){
							qiDefect.setActive( true);
							qiDefect.setActiveValue((short) 1);
						}else{
							qiDefect.setActive( false);
							qiDefect.setActiveValue((short) 0);
						}
					
						//call to prepare and insert audit data
						AuditLoggerUtil.logAuditInfo(qiDefectCloned, qiDefect, dialog.getReasonForChangeTextArea().getText(),getView().getScreenName() ,getUserId());
					}
					getView().reload(getView().getFilterTextData());
				}
				catch (Exception e) {
					handleException("An error occured in updateDefectStatus method " , "Failed to updateDefectStatus Defect Status ", e);
				}
			}
			else
				return;

		}catch (Exception e) {
			handleException("An error occurred in inactivateDefect method ", "Failed to open inactivate Defect popup ", e);
		}
	}
	/**
	 * This method update Part Defect Combination related to defect 
	 */
	private void updatePartDefectCombStatus(List<QiDefect> qiDefectList) {
		List<QiPartDefectCombination> qiPartDefectCombinationsList = new ArrayList<QiPartDefectCombination>();
		for(QiDefect qiDefect : qiDefectList){
			qiPartDefectCombinationsList =getModel().findDefectInPartDefectCombination(qiDefect.getDefectTypeName());
			for (QiPartDefectCombination qiPartDefectCombination : qiPartDefectCombinationsList) {
				qiPartDefectCombination.setActive(false);
				qiPartDefectCombination.setUpdateUser(getUserId());
				getModel().updatePartDefectCombination(qiPartDefectCombination);
			}
		}
	}

	/**
	 * This method is to open Popup dialog for update Defect
	 */
	private void updateDefect(ActionEvent actionEvent, QiDefect qiDefect){
		clearDisplayMessage();
		try{
			DefectDialog dialog = new DefectDialog(QiConstant.UPDATE, qiDefect, getModel(),getApplicationId());
			dialog.setScreenName(getView().getScreenName());
			dialog.showDialog();
			if(!dialog.isCancel())  {
				getView().reload(getView().getFilterTextData(), true, true);
			}
		}catch (Exception e) {
			handleException("An error occurred in updateDefect method ", "Failed to open updateDefect Defect popup ", e);
		}
	}
	
}
