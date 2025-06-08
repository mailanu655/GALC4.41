package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.PartDefectCombinationController.ListType;
import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.PartLocationCombinationDialog;
import com.honda.galc.client.teamleader.qi.view.PartLocationCombinationMaintPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiImageSection;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiPartLocationCombination;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartLocationCombinationController</code> is the controller class for Part Location Combination Panel.
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
public class PartLocationCombinationController extends AbstractQiController<ItemMaintenanceModel, PartLocationCombinationMaintPanel> implements EventHandler<ActionEvent> {

	private QiPartLocationCombination selectedComb;

	public QiPartLocationCombination getSelectedComb() {
		return selectedComb;
	}

	public void setSelectedComb(QiPartLocationCombination selectedComb) {
		this.selectedComb = selectedComb;
	}

	public PartLocationCombinationController(ItemMaintenanceModel model,PartLocationCombinationMaintPanel view) {
		super(model, view);
	}

	@Override
	public void initEventHandlers() {
		/**
		 *  This method is used to add listener on main panel table.
		 */
		if (isFullAccess()) {
			addPartLocationCombinationTableListener();
		}
		addRadioButtonListener();
	}

	private void addRadioButtonListener() {
		final ObjectTablePane<QiPartLocationCombination> tablePane = getView().getPartLocationCombinationTablePane();

		getView().getAllRadioBtn().selectedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				tablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}
		});
		getView().getActiveRadioBtn().selectedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				tablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
		});
		getView().getInactiveRadioBtn().selectedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				tablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}
		});
	}

	private void addPartLocationCombinationTableListener() {
		getView().getPartLocationCombinationTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiPartLocationCombination>() {
			public void changed(
					ObservableValue<? extends QiPartLocationCombination> arg0,
					QiPartLocationCombination arg1,
					QiPartLocationCombination arg2) {
				selectedComb = getView().getPartLocationCombinationTablePane().getSelectedItem();
				addContextMenuItems();
			}
		});
		getView().getPartLocationCombinationTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}
	/**
	 * This method is called to add context menus to main panel.
	 */
	@Override
	public void addContextMenuItems()
	{
		clearDisplayMessage();
		ObjectTablePane<QiPartLocationCombination> tablePane = getView().getPartLocationCombinationTablePane();
		if(selectedComb!=null){
			if(selectedComb.isActive() && !getView().getAllRadioBtn().isSelected())
			{
				String[] menuItems;
				if(tablePane.getTable().getSelectionModel().getSelectedItems().size() > 1) {
					menuItems = new String[] {
							QiConstant.INACTIVATE
						};
				}
				else {
					menuItems = new String[] {
							QiConstant.CREATE, QiConstant.UPDATE, QiConstant.INACTIVATE
						};
				}
				tablePane.createContextMenu(menuItems, this);
			}
			else
			{
				String[] menuItems = new String[] {
						QiConstant.CREATE, QiConstant.UPDATE
					};
				tablePane.createContextMenu(menuItems, this);
			}
		} else {
			String[] menuItems = new String[] {
					QiConstant.CREATE
				};
			tablePane.createContextMenu(menuItems, this);
		}
	}
	/**
	 * This is an implemented method of EventHandler interface. Called whenever an ActionEvent is triggered.
	 * Selecting context menu is an ActionEvent. So respective method is called based on which action event is triggered.
	 */
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();

			if(QiConstant.CREATE.equals(menuItem.getText())) createPartLocationCombination(actionEvent);
			else if(QiConstant.UPDATE.equals(menuItem.getText())) updatePartLocationCombination(actionEvent);
			else if(QiConstant.INACTIVATE.equals(menuItem.getText())) inactivatePartLocationCombination(actionEvent);
		}
		else if (actionEvent.getSource() instanceof LoggedRadioButton || actionEvent.getSource() instanceof UpperCaseFieldBean) {
			// clear table
			getView().getPartLocationCombinationTablePane().setData(new ArrayList<QiPartLocationCombination>());

			String filter = getView().getFullPartNameFilterTextField().getText();
			if (StringUtils.isBlank(filter) || StringUtils.trim(filter).length() < 3) {
				displayErrorMessage("Please enter minimal 3 char to filter");
				return;
			}
			clearDisplayMessage();
			getView().reload(filter);
		}
	}
	/**
	 * This method is used create a Part Location Combination
	 * @param event
	 */
	public void createPartLocationCombination(ActionEvent event){
		PartLocationCombinationDialog dialog = new PartLocationCombinationDialog(QiConstant.CREATE, new QiPartLocationCombination(), getModel(), getView().getScreenName(),getApplicationId(), this);
		dialog.setListType(getListType());
		dialog.showDialog();
		if(!dialog.isCancel())  {
			dialog.setCancel(false);
			getView().reload(getView().getFilterTextData());
		}
	};
	
	/**
	 * This method is used to update a Part Location Combination
	 * @param event
	 */
	public void updatePartLocationCombination(ActionEvent event){
		PartLocationCombinationDialog dialog = new PartLocationCombinationDialog(QiConstant.UPDATE, selectedComb, getModel(), getView().getScreenName(),getApplicationId(), this);
		dialog.setListType(getListType());
		dialog.showDialog();
		if(!dialog.isCancel())  {
			getView().reload(getView().getFilterTextData(), true, true);
		}
	};
	/**
	 * This method is used to inactivate a Part Location Combination.
	 * @param event
	 */
	public void inactivatePartLocationCombination(ActionEvent event){
		
		ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
		if(dialog.showReasonForChangeDialog(null))
		{
			clearDisplayMessage();
			try {
				List<QiPartLocationCombination> partLocCombList = getView().getPartLocationCombinationTablePane().getSelectedItems();
				List<Integer> partLocationIdList = new ArrayList<Integer>();
				if(getView().getActiveRadioBtn().isSelected()){
					for(QiPartLocationCombination comb: partLocCombList){
						partLocationIdList.add(comb.getPartLocationId());
					}
				}
				List<QiImageSection> imageSectionList = getModel().findPartLocationIdsInImageSection(partLocationIdList);
				List<QiPartDefectCombination> partDefectCombList = getModel().findPartLocationIdsInPartDefectCombination(partLocationIdList);
				List<QiPartDefectCombination> regionalAttributeList= getModel().findAllRegionalAttributesByPartLocationId(partLocationIdList);
				

				if(!imageSectionList.isEmpty()){
					StringBuilder errorMesg=new StringBuilder("The Part Location Combination(s) being updated affects " );
					if( imageSectionList.size()>0)
						errorMesg.append(imageSectionList.size()+" Image Section(s)" );
					MessageDialog.showError(getView().getStage(),errorMesg.append( ". Hence, inactivation of this PLC is not allowed.").toString());
					return;
				}
				/** To modify Part Defect Combination  */
				if(!partDefectCombList.isEmpty()  || !regionalAttributeList.isEmpty()){
					boolean isPLCombinationUpdated = MessageDialog.confirm(getView().getStage(), QiCommonUtil.getMessage("Part Location Combination(s)",partDefectCombList.size(),regionalAttributeList.size(),0).toString());
					if(isPLCombinationUpdated){
						String returnValue=isLocalSiteImpacted(partLocationIdList,getView().getStage());
						if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
							return;
						}
						else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
							publishErrorMessage("Inactivation of PLC(s) affects Local Sites");
							return;
						}
						updatePartDefectCombStatus();
					}
					else
						return;
				}
				for(QiPartLocationCombination comb: partLocCombList)
				{
					getModel().updatePartLocCombStatus(comb.getPartLocationId(), (short)0);
					
					/**
					 * call for audit log
					 */
					QiPartLocationCombination partLocationCombinationCloned = (QiPartLocationCombination) comb.deepCopy();
					comb.setActive(false);
					AuditLoggerUtil.logAuditInfo(partLocationCombinationCloned, comb, dialog.getReasonForChangeText(), getView().getScreenName(),getUserId());
				}
				if(partLocCombList != null && partLocCombList.size() > 0)  {
					removeSelectedFromList();
				}
			}catch (Exception e) {
				handleException("An error occured in updatePartLocCombStatus method ","Failed to update status", e);
			}
		}
		else
			return;
	};

	public void removeSelectedFromList()  {
		getView().getPartLocationCombinationTablePane().removeSelected();
		getView().getPartLocationCombinationTablePane().clearSelection();
		
	}
	
	/**
	 * This method update Part Defect Combination related to defect 
	 */
	private void updatePartDefectCombStatus() {
		List<QiPartLocationCombination> partLocCombList = getView().getPartLocationCombinationTablePane().getSelectedItems();
		List<QiPartDefectCombination> newPartDefectCombList = new ArrayList<QiPartDefectCombination>();
		for(QiPartLocationCombination qiPartLocationCombination : partLocCombList){
			List<QiPartDefectCombination> qiPartDefectCombinationsList = getModel().findPartLocCombInPartDefectCombination(qiPartLocationCombination.getPartLocationId());
			for (QiPartDefectCombination qiPartDefectCombination : qiPartDefectCombinationsList) {
				qiPartDefectCombination.setActive(false);
				qiPartDefectCombination.setUpdateUser(getUserId());
				newPartDefectCombList.add(qiPartDefectCombination);
			}
		}
		getModel().updatePartDefectCombination(newPartDefectCombList);
	}
	public ListType getListType() {
		if(getView().getAllRadioBtn().isSelected()) {
			return ListType.BOTH;
		}
		else if(getView().getActiveRadioBtn().isSelected())
				return ListType.ACTIVE;
		else  {
			return ListType.INACTIVE;
		}
	}
}
