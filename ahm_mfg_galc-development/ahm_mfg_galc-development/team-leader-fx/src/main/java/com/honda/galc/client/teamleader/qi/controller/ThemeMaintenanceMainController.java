package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;

import com.honda.galc.client.teamleader.qi.model.ThemeMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.teamleader.qi.view.ThemeGroupMaintenanceDialog;
import com.honda.galc.client.teamleader.qi.view.ThemeMaintenanceDialog;
import com.honda.galc.client.teamleader.qi.view.ThemeMaintenanceMainPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiTheme;
import com.honda.galc.entity.qi.QiThemeGroup;
import com.honda.galc.entity.qi.QiThemeGrouping;
import com.honda.galc.util.AuditLoggerUtil;

public class ThemeMaintenanceMainController extends AbstractQiController<ThemeMaintenanceModel, ThemeMaintenanceMainPanel> implements EventHandler<ActionEvent>  {

	private QiTheme selectedThemeCloned;
	private QiThemeGroup selectedThemeGroupCloned;

	public ThemeMaintenanceMainController(ThemeMaintenanceModel model,ThemeMaintenanceMainPanel view) {
		super(model, view);
	}

	@Override
	public void initEventHandlers() {
		themeGroupTablePaneSelectListener(getView().getThemeGroupTablePane());
		themeTablePaneSelectListener(getView().getThemeTablePane());
		if (isFullAccess()) {
			addListener();
		}
	}

	/**
	 * This method is for theme,theme group and theme grouping Table Listeners
	 */
	private void addListener() {

		getView().getThemeGroupTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiThemeGroup>() {
			public void changed(ObservableValue<? extends QiThemeGroup> arg0, QiThemeGroup arg1, QiThemeGroup arg2) {
				if(getView().getThemeGroupTablePane() != null && getView().getThemeGroupTablePane().getSelectedItems().size() < 1)
					addContextMenuItems();
			}
		});
		getView().getThemeGroupTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
		getView().getThemeTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiTheme>() {
			public void changed(ObservableValue<? extends QiTheme> arg0, QiTheme arg1, QiTheme arg2) {
				addContextMenuItemsInTheme();
			}
		});
		getView().getThemeTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
					addContextMenuItemsInTheme();
			}
		});
		getView().getThemeGroupingTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiThemeGrouping>() {
			public void changed(ObservableValue<? extends QiThemeGrouping> arg0, QiThemeGrouping arg1,	QiThemeGrouping arg2) {
				addContextMenuItems();
			}
		});
	}

	/**
	 * This method is added because initEventHandlers() is called before the selection listener is triggered.
	 */
	public void addContextMenuItems() {
		clearDisplayMessage();
		addContextMenuItemsInThemeGroup();
		addContextMenuItemsInThemeGrouping();
	}

	/**
	 * Adds the context menu items in theme group.
	 */
	private void addContextMenuItemsInThemeGroup() {
		List<QiThemeGroup> qiThemeGroupList = getView().getThemeGroupTablePane().getSelectedItems();
		if(qiThemeGroupList!=null && qiThemeGroupList.size() == 1) {
			List<String> themeGroupMenuList = new ArrayList<String>();
			themeGroupMenuList.add(QiConstant.CREATE);
			themeGroupMenuList.add(QiConstant.UPDATE);
			if(getView().getThemeGrpAllRadioBtn().isSelected())	{
				getView().getThemeGroupTablePane().createContextMenu(themeGroupMenuList.toArray(new String[themeGroupMenuList.size()]), this);
				getView().getThemeGroupTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			} else if(getView().getThemeGrpActiveRadioBtn().isSelected()){
				themeGroupMenuList.add(QiConstant.INACTIVATE);
				getView().getThemeGroupTablePane().createContextMenu(themeGroupMenuList.toArray(new String[themeGroupMenuList.size()]), this);
				getView().getThemeGroupTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			} else {
				themeGroupMenuList.add(QiConstant.REACTIVATE);
				getView().getThemeGroupTablePane().createContextMenu(themeGroupMenuList.toArray(new String[themeGroupMenuList.size()]), this);
				getView().getThemeGroupTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
		} else if(qiThemeGroupList != null && qiThemeGroupList.size() > 1){
			if(getView().getThemeGrpActiveRadioBtn().isSelected()){
				String[] menuItems = new String[] { QiConstant.INACTIVATE };
				getView().getThemeGroupTablePane().createContextMenu(menuItems, this);
			}else{
				String[] menuItems = new String[] { QiConstant.REACTIVATE };
				getView().getThemeGroupTablePane().createContextMenu(menuItems, this);
			}
		} else {
			String[] menuItems = new String[] { QiConstant.CREATE };
			getView().getThemeGroupTablePane().createContextMenu(menuItems, this);
		}
	}

	/**
	 * Adds the context menu items in theme.
	 */
	private void addContextMenuItemsInTheme() {
		List<QiTheme> qiThemeList = getView().getThemeTablePane().getSelectedItems();
		List<QiThemeGroup> qiThemeGroupList = getView().getThemeGroupTablePane().getSelectedItems();
		if(qiThemeList != null && qiThemeList.size() == 1) {
			List<String> themeMenuList = new ArrayList<String>();
			themeMenuList.add(QiConstant.CREATE);
			themeMenuList.add(QiConstant.UPDATE);
			boolean isAddToGroup = getView().getThemeGroupingTablePane().getTable().getItems().isEmpty() && (qiThemeGroupList != null && qiThemeGroupList.size() == 1);
			if(isAddToGroup && getView().getThemeGrpActiveRadioBtn().isSelected() && getView().getThemeActiveRadioBtn().isSelected()){ 
				themeMenuList.add(QiConstant.ADD_TO_GROUP);
			}

			if(getView().getThemeAllRadioBtn().isSelected()) {
				getView().getThemeTablePane().createContextMenu(themeMenuList.toArray(new String[themeMenuList.size()]), this);
				getView().getThemeTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			} else if(getView().getThemeActiveRadioBtn().isSelected()){
				themeMenuList.add(QiConstant.INACTIVATE);
				getView().getThemeTablePane().createContextMenu(themeMenuList.toArray(new String[themeMenuList.size()]), this);
				getView().getThemeTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			} else	{
				themeMenuList.add(QiConstant.REACTIVATE);
				getView().getThemeTablePane().createContextMenu(themeMenuList.toArray(new String[themeMenuList.size()]), this);
				getView().getThemeTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
		}else if(qiThemeList != null && qiThemeList.size() > 1){
			if(getView().getThemeActiveRadioBtn().isSelected()){
				String[] menuItems = new String[] {	QiConstant.INACTIVATE };
				getView().getThemeTablePane().createContextMenu(menuItems, this);
			}else{
				String[] menuItems = new String[] {	QiConstant.REACTIVATE };
				getView().getThemeTablePane().createContextMenu(menuItems, this);
			}
		}
		else{
			String[] menuItems = new String[] {	QiConstant.CREATE };
			getView().getThemeTablePane().createContextMenu(menuItems, this);
		}
	}



	/**
	 * Adds the context menu items in theme grouping.
	 */
	private void addContextMenuItemsInThemeGrouping() {
		if(getView().getThemeGroupingTablePane().getTable().getContextMenu() != null)
			getView().getThemeGroupingTablePane().getTable().getContextMenu().getItems().clear();
		if(getView().getThemeGroupingTablePane().getSelectedItems() != null && 
				getView().getThemeGroupingTablePane().getSelectedItems().size() > 0){
			String[] menuItems = new String[] {	QiConstant.DELETE };
			getView().getThemeGroupingTablePane().createContextMenu(menuItems, this);
			getView().getThemeGroupingTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		}
	}

	/**
	 * This method is to map the action event to context menu and radio button
	 */
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();

			if(QiConstant.CREATE.equals(menuItem.getText())){
				if(getView().getThemeTablePane().getTable().isFocused())
					createTheme(actionEvent);
				else if(getView().getThemeGroupTablePane().getTable().isFocused())
					createThemeGroup(actionEvent);
			}
			if(QiConstant.UPDATE.equals(menuItem.getText())){
				if(getView().getThemeTablePane().getTable().isFocused())
					updateTheme(actionEvent);
				else if(getView().getThemeGroupTablePane().getTable().isFocused())
					updateThemeGroup(actionEvent);
			}
			if(QiConstant.REACTIVATE.equals(menuItem.getText())){
				if(getView().getThemeTablePane().getTable().isFocused())
					updateThemeStatus(actionEvent,true);
				else if(getView().getThemeGroupTablePane().getTable().isFocused())
					updateThemeGroupStatus(actionEvent,true);
			}
			if(QiConstant.INACTIVATE.equals(menuItem.getText())){
				if(getView().getThemeTablePane().getTable().isFocused())
					updateThemeStatus(actionEvent,false);
				else if(getView().getThemeGroupTablePane().getTable().isFocused())
					updateThemeGroupStatus(actionEvent,false);
			}
			if(QiConstant.ADD_TO_GROUP.equals(menuItem.getText())) {
				addToGroupAssociation();
			}
			if(QiConstant.DELETE.equals(menuItem.getText())) {
				deleteGroupAssociation();
			}
		} 

		if(actionEvent.getSource() instanceof LoggedRadioButton){
			if(actionEvent.getSource().equals(getView().getThemeGrpAllRadioBtn()) || actionEvent.getSource().equals(getView().getThemeGrpActiveRadioBtn()) ||
					actionEvent.getSource().equals(getView().getThemeGrpInactiveRadioBtn())){
				getView().getThemeGroupTablePane().getTable().getSelectionModel().clearSelection();
				getView().getThemeTablePane().getTable().getSelectionModel().clearSelection();
				getView().getThemeGroupingTablePane().getTable().getItems().clear();
				getView().reloadThemeGroup();
			}
			if(actionEvent.getSource().equals(getView().getThemeAllRadioBtn()) || actionEvent.getSource().equals(getView().getThemeActiveRadioBtn()) ||
					actionEvent.getSource().equals(getView().getThemeInactiveRadioBtn())){
				getView().getThemeTablePane().getTable().getSelectionModel().clearSelection();
				getView().getThemeGroupTablePane().getTable().getSelectionModel().clearSelection();
				getView().getThemeGroupingTablePane().getTable().getItems().clear();
				getView().reloadTheme();
			}	
		}
	}

	/**
	 * Theme group table pane select listener.
	 *
	 * @param themeGroupTblView the theme group tbl view
	 */
	private void themeGroupTablePaneSelectListener(final ObjectTablePane<QiThemeGroup> themeGroupTblView) {
		getView().getThemeGroupTablePane().getTable().getSelectionModel().getSelectedItems()
		.addListener(new ListChangeListener<QiThemeGroup> () {
			public void onChanged(ListChangeListener.Change<? extends QiThemeGroup> arg0) {
				getView().getThemeTablePane().getTable().getSelectionModel();
				getView().getThemeGroupingTablePane().getTable().getItems().clear();
				loadThemeGrouping();
				if(isFullAccess()){
					addContextMenuItemsInThemeGroup();
				}
			}
		});
	}


	/**
	 * Theme table pane select listener.
	 *
	 * @param themeTblView the theme tbl view
	 */
	private void themeTablePaneSelectListener(final ObjectTablePane<QiTheme> themeTblView) {
		getView().getThemeTablePane().getTable().getSelectionModel().getSelectedItems()
		.addListener(new ListChangeListener<QiTheme> () {
			public void onChanged(ListChangeListener.Change<? extends QiTheme> arg0) {
				getView().getThemeGroupingTablePane().getTable().getItems().clear();
				loadThemeGrouping();
				if(isFullAccess()){
					addContextMenuItemsInTheme();
					addContextMenuItemsInThemeGrouping();
				}
			}
		});
	}

	/**
	 * Gets the theme group name list.
	 *
	 * @return the theme group name list
	 */
	private List<String> getThemeGroupNameList() {
		List<String> groupNameList = new ArrayList<String>();
		List<QiThemeGroup> qiThemeGroup = getView().getThemeGroupTablePane().getTable().getSelectionModel().getSelectedItems();
		for (QiThemeGroup q : qiThemeGroup) {
			groupNameList.add(q.getThemeGroupName());
		}
		return groupNameList;
	}

	/**
	 * Gets the theme name list..
	 *
	 * @return the theme name list
	 */
	private List<String> getThemeNameList(){
		List<String> themeNameList = new ArrayList<String>();
		List<QiTheme> qiTheme = getView().getThemeTablePane().getTable().getSelectionModel().getSelectedItems();
		for (QiTheme q : qiTheme) {
			themeNameList.add(q.getThemeName());
		}
		return themeNameList;
	}


	/**
	 * This method is used to update theme status to activate/inactivate
	 * @param event
	 * @param isActive
	 */
	private void updateThemeStatus(ActionEvent event,boolean isActive){
		clearDisplayMessage();
		try{
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			List<QiTheme> updateThemeList = new ArrayList<QiTheme>();
			List<QiPartDefectCombination> pdcList = new ArrayList<QiPartDefectCombination>();

			for (QiTheme qiTheme : getView().getThemeTablePane().getSelectedItems()) {
				pdcList.addAll(getModel().findAllByThemeName(qiTheme.getThemeName()));
			}
			boolean confirm=true;
			if(pdcList.size()>0){
				MessageDialog.showError(getView().getStage(),"Inactivating Theme  is not allowed as it is assigned to " + pdcList.size() + " Part Defect Combination(s) in regional attribute.");
				return;
			}
			else if(getView().getThemeGroupingTablePane().getRowCount()>0){
				confirm=MessageDialog.confirm(getView().getStage(),"Inactivating Theme will delete its association(s) with Theme Group.  Do you wish to continue?");
			}
			if (getView().getThemeActiveRadioBtn().isSelected()  && !confirm){
				return ;
			}
			if(dialog.showReasonForChangeDialog(null)) {
				try{
					for(QiTheme qiTheme : getView().getThemeTablePane().getSelectedItems()){
						selectedThemeCloned = (QiTheme) qiTheme.deepCopy();
						qiTheme.setActive(isActive);
						qiTheme.setUpdateUser(getUserId());
						//call to prepare and insert audit data
						AuditLoggerUtil.logAuditInfo(selectedThemeCloned, qiTheme, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
						updateThemeList.add(qiTheme);
					}
					getModel().updateThemeStatus(updateThemeList);
					getModel().deleteGroupAssociationByThemes(updateThemeList);
					if(pdcList.size() > 0)
						updatePartDefectCombination(pdcList);
					getView().reloadTheme();
				}
				catch (Exception e) {
					handleException("An error occured in activate/inactivate method ", "Failed to activate/inactivate theme", e);
				}
			} else
				return;

		}catch (Exception e) {
			handleException("An error occurred in updateThemeStatus method ", "Failed to updateThemeStatus ", e);
		}
	}

	/**
	 * Load theme grouping.
	 */
	private void loadThemeGrouping() {
		if(getThemeGroupNameList().size() > 0 || getThemeNameList().size() > 0)
			getView().reloadThemeGrouping(getThemeGroupNameList(), getThemeNameList());
	}

	/**
	 * Update part defect combination.
	 *
	 * @param themeList the theme list
	 */
	private void updatePartDefectCombination(List<QiPartDefectCombination> pdcList) {
		List<QiPartDefectCombination> pdcListToUpdate = new ArrayList<QiPartDefectCombination>();

		for (QiPartDefectCombination qiPartDefectCombination : pdcList) {
			qiPartDefectCombination.setThemeName(null);
			pdcListToUpdate.add(qiPartDefectCombination);
		}
		if(pdcListToUpdate.size() > 0){
			getModel().updatePartDefectCombination(pdcListToUpdate);
		}
	}

	/**
	 * This method is used to update theme group status to activate/inactivate
	 * @param event
	 * @param isActive
	 */
	private void updateThemeGroupStatus(ActionEvent event,boolean isActive){
		clearDisplayMessage();
		try{
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			List<QiThemeGroup> updateThemeGroupList = new ArrayList<QiThemeGroup>();
			boolean confirm=true;
			if(getView().getThemeGroupingTablePane().getRowCount()>0){
				confirm=MessageDialog.confirm(getView().getStage(), "Inactivating Theme Group will delete any existing association(s) with Theme. Do you wish to continue?");
			}
			if (getView().getThemeGrpActiveRadioBtn().isSelected() && !confirm){
				return ;
			}
			if (dialog.showReasonForChangeDialog(null)) {
				try {
					for(QiThemeGroup qiThemeGroup : getView().getThemeGroupTablePane().getSelectedItems()){
						selectedThemeGroupCloned=(QiThemeGroup) qiThemeGroup.deepCopy();
						qiThemeGroup.setActive(isActive);
						qiThemeGroup.setUpdateUser(getUserId());
						//call to prepare and insert audit data
						AuditLoggerUtil.logAuditInfo(selectedThemeGroupCloned, qiThemeGroup, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
						updateThemeGroupList.add(qiThemeGroup);
					}
					getModel().updateThemeGroupStatus(updateThemeGroupList);
					getModel().deleteGroupAssociationByThemeGroups(	updateThemeGroupList);
					getView().reloadThemeGroup();
					getView().getThemeGroupingTablePane().getTable().getItems().clear();
				} catch (Exception e) {
					handleException("An error occured in activate/inactivate method ",
							"Failed to activate/inactivate theme group", e);
				}
			}
			else
				return;

		}catch (Exception e) {
			handleException("An error occurred in updateThemeGroupStatus method ", "Failed to updateThemeGroupStatus ", e);
		}
	}

	/**
	 * This method is used to create theme 
	 * @param event
	 */
	private void createTheme(ActionEvent event){
		clearDisplayMessage();
		try{
			ThemeMaintenanceDialog dialog = new ThemeMaintenanceDialog(QiConstant.CREATE, new QiTheme(), getModel(),getApplicationId(),getView().getThemeGroupingTablePane());
			dialog.showDialog();
			getView().reloadTheme();
		}catch(Exception e){
			handleException("An error occured in createTheme method ","Failed to create theme", e);
		}
	}

	/**
	 * This method is used to update theme 
	 * @param event
	 */

	private void updateTheme(ActionEvent event){
		clearDisplayMessage();
		try{
			ThemeMaintenanceDialog dialog=new ThemeMaintenanceDialog(QiConstant.UPDATE, getView().getThemeTablePane().getSelectedItem(), getModel(),getApplicationId(),getView().getThemeGroupingTablePane());
			dialog.setScreenName(getView().getScreenName());
			dialog.showDialog();
			int selectedIndex = getView().getThemeTablePane().getTable().getSelectionModel().getSelectedIndex();
			getView().reloadTheme();
			getView().getThemeTablePane().getTable().getSelectionModel().select(selectedIndex);
			loadThemeGrouping();
		}catch(Exception e){
			handleException("An error occured in updateTheme method ","Failed to update theme", e);
		}
	}

	/**
	 * This method is used to create theme group 
	 * @param event
	 */
	private void createThemeGroup(ActionEvent event){
		clearDisplayMessage();
		try{
			ThemeGroupMaintenanceDialog dialog = new ThemeGroupMaintenanceDialog(QiConstant.CREATE, new QiThemeGroup(), getModel(),getApplicationId(),getView().getThemeGroupingTablePane());
			dialog.showDialog();
			getView().reloadThemeGroup();
		}catch(Exception e){
			handleException("An error occured in createThemeGroup method ","Failed to create theme group", e);
		}

	}
	/**
	 * This method is used to update theme group
	 * @param event
	 */
	private void updateThemeGroup(ActionEvent event){
		clearDisplayMessage();
		try{
			ThemeGroupMaintenanceDialog dialog = new ThemeGroupMaintenanceDialog(QiConstant.UPDATE, getView().getThemeGroupTablePane().getSelectedItem(), getModel(),getApplicationId(),getView().getThemeGroupingTablePane());
			dialog.setScreenName(getView().getScreenName());
			dialog.showDialog(); 
			int selectedIndex = getView().getThemeGroupTablePane().getTable().getSelectionModel().getSelectedIndex();
			getView().reloadThemeGroup();
			getView().getThemeGroupTablePane().getTable().getSelectionModel().select(selectedIndex);
			loadThemeGrouping();
		}catch(Exception e){
			handleException("An error occured in updateThemeGroup method ","Failed to update theme group", e);
		}
	}

	/**
	 * Adds the to group association.
	 */
	private void addToGroupAssociation() {
		clearDisplayMessage();
		try {
			QiThemeGroup qiThemeGroup = getView().getThemeGroupTablePane().getTable().getSelectionModel().getSelectedItem();
			QiTheme qiTheme = getView().getThemeTablePane().getTable().getSelectionModel().getSelectedItem();
			if(qiTheme == null || qiThemeGroup == null){
				displayMessage("Select Theme Group and Theme.");
				return;
			}
			if(!getView().getThemeGroupingTablePane().getTable().getItems().isEmpty()){
				displayMessage("Association already exist.");
				return;
			}
			QiThemeGrouping qiThemeGrouping = setThemeGroupingValue(qiThemeGroup, qiTheme);
			getModel().addToGroupAssociation(qiThemeGrouping);
			List<QiThemeGrouping> themeGroupingsList = new ArrayList<QiThemeGrouping>();
			themeGroupingsList.add(qiThemeGrouping);
			getView().getThemeGroupingTablePane().setData(themeGroupingsList);
			addContextMenuItemsInTheme();
		}
		catch (Exception e) {
			handleException("An error occured in activate/inactivate method ", "Failed to activate/inactivate theme group", e);
		}
	}

	/**
	 * Sets the theme grouping value.
	 *
	 * @param qiThemeGroup the qi theme group
	 * @param qiTheme the qi theme
	 * @return the qi theme grouping
	 */
	private QiThemeGrouping setThemeGroupingValue(QiThemeGroup qiThemeGroup, QiTheme qiTheme) {
		QiThemeGrouping themeGrouping = new QiThemeGrouping();
		themeGrouping.setThemeName(qiTheme.getThemeName());
		themeGrouping.setThemeGroupName(qiThemeGroup.getThemeGroupName());
		themeGrouping.setCreateUser(getUserId());
		return themeGrouping;
	}

	/**
	 * Delete group association.
	 */
	private void deleteGroupAssociation() {
		clearDisplayMessage();
		try {
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if(dialog.showReasonForChangeDialog(null)) {
				List<QiThemeGrouping> qiThemeGrouping = getView().getThemeGroupingTablePane().getTable().getSelectionModel().getSelectedItems();
				if(qiThemeGrouping == null){
					displayErrorMessage("Select Theme Grouping.");
					return;
				}
				getModel().deleteGroupAssociation(new ArrayList<QiThemeGrouping>(qiThemeGrouping));
				for (QiThemeGrouping themegrouping : qiThemeGrouping) {
					//call to prepare and insert audit data
					AuditLoggerUtil.logAuditInfo(themegrouping, null, dialog.getReasonForChangeText(), getView().getScreenName(),getUserId());
				}
				loadThemeGrouping();
			} else {
				return;
			}
		} catch (Exception e) {
			handleException("An error occurred in deleteGroupAssociation method ", "Failed to deleteGroupAssociation ", e);
		}
	}

}
