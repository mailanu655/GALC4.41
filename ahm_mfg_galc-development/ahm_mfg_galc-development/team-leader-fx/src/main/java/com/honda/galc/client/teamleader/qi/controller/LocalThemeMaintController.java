package com.honda.galc.client.teamleader.qi.controller;
import java.util.List;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.model.LocalThemeModel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.teamleader.qi.view.LocalThemeDialog;
import com.honda.galc.client.teamleader.qi.view.LocalThemeMaintPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiLocalTheme;
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
 * 
 * <h3>LocalThemeMaintController Class description</h3>
 * <p>
 * LocalThemeMaintController is used to load data in TableView and perform to the action on the RadioButton (All, Activate, Inactivate )etc.
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
public class LocalThemeMaintController extends AbstractQiController<LocalThemeModel, LocalThemeMaintPanel> implements EventHandler<ActionEvent> {

	public LocalThemeMaintController(LocalThemeModel model,LocalThemeMaintPanel view) {
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
		ObjectTablePane<QiLocalTheme> localThemeTablePane = getView().getLocalThemeTablePane();
		int recordsSelected=localThemeTablePane.getTable().getSelectionModel().getSelectedItems().size();
		String[] menuItems;
		if(null != getView().getLocalThemeTablePane().getSelectedItem()){
			if(getView().getAllRadioBtn().isSelected()){
				if(recordsSelected > 1) {
					menuItems = new String[] { QiConstant.CREATE};
				}else{
					menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE };
				}
				getView().getLocalThemeTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}else if(getView().getActiveRadioBtn().isSelected())
			{
				if(recordsSelected > 1) {
					menuItems = new String[] {QiConstant.INACTIVATE };
				}else{
					menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.INACTIVATE };
				}
				getView().getLocalThemeTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
			else
			{
				if(recordsSelected > 1) {
					menuItems = new String[] { QiConstant.REACTIVATE };
				}else{
					menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.REACTIVATE };
				}
				getView().getLocalThemeTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
		}else{
			menuItems = new String[] { QiConstant.CREATE };
		}
		localThemeTablePane.createContextMenu(menuItems, this);
	}


	/**
	 * This method is used to create, edit , inactivate, reactivate Part
	 */
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedRadioButton) {
			clearDisplayMessage();
			loadStatusData();
		} else if (actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			if (QiConstant.CREATE.equals(menuItem.getText()))
				createLocalTheme(actionEvent);
			else if (QiConstant.UPDATE.equals(menuItem.getText()))
				updateLocalTheme(actionEvent);
			else if (QiConstant.INACTIVATE.equals(menuItem.getText()))
				inactivateLocalTheme(actionEvent);
			else if (QiConstant.REACTIVATE.equals(menuItem.getText()))
				reactivateLocalTheme(actionEvent);
		}

		else if (actionEvent.getSource() instanceof UpperCaseFieldBean) {
			clearDisplayMessage();
			loadStatusData();
		}

	}

	private void addTableListener() {
		getView().getLocalThemeTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiLocalTheme>() {
			public void changed(ObservableValue<? extends QiLocalTheme> arg0,
					QiLocalTheme arg1, QiLocalTheme arg2) {
				addContextMenuItems();
			}
		});
		getView().getLocalThemeTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}


	/**
	 * This method is used to create the Part
	 */
	private void createLocalTheme(ActionEvent event){
		LocalThemeDialog dialog = new LocalThemeDialog(QiConstant.CREATE, new QiLocalTheme(), getModel(),getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		loadStatusData();
	}


	/**
	 * This method is used to edit the Part info. clicking on 'Update' context menu
	 */
	private void updateLocalTheme(ActionEvent event) {
		LocalThemeDialog dialog = new LocalThemeDialog(QiConstant.UPDATE,
				getView().getLocalThemeTablePane().getSelectedItem(), getModel(), getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		loadStatusData();
	}


	/**
	 * This method is used to Reactivate the Part
	 */
	private void reactivateLocalTheme(ActionEvent event){
		updateLocalThemeStatus(true);
	}


	/**
	 * This method is used to Inactivate the Part
	 */
	private void inactivateLocalTheme(ActionEvent event){
		updateLocalThemeStatus(false);
	}


	/**
	 *  This method is used to update the status of Part
	 * @param status
	 */
	private void updateLocalThemeStatus(boolean isActive){
		boolean isUpdated = false;
		if(isActive == false){
			if(isLocalThemeInUse(getView().getLocalThemeTablePane().getSelectedItems())){
				MessageDialog.showError(ClientMainFx.getInstance().getStage(getApplicationId()), 
						"The selected Local Theme/s is/are in use. Hence, inactivate not allowed.");
				return;
			}

		}
		try {
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if(dialog.showReasonForChangeDialog(null)){
				for (QiLocalTheme localtheme : getView().getLocalThemeTablePane().getSelectedItems()) {
					localtheme  = getModel().findLocalThemeByName(localtheme.getLocalTheme());

					QiLocalTheme qiLocalThemeClonned = (QiLocalTheme) localtheme.deepCopy();

					localtheme.setActive(isActive);
					localtheme.setUpdateUser(getUserId());

					getModel().updateLocalTheme(localtheme);

					//call to prepare and insert audit data
					AuditLoggerUtil.logAuditInfo(qiLocalThemeClonned, localtheme, dialog.getReasonForChangeTextArea().getText(),
							getView().getScreenName(),getUserId());

					isUpdated = true;

				}

				if(isUpdated)
					getView().getLocalThemeTablePane().getTable().getItems().removeAll(getView().getLocalThemeTablePane().getSelectedItems());
			}

		} catch (Exception e) {
			handleException("Failed to inactivate/Reactivate part  ", "An error occurred at inactivatePart/reactivatePart method ",e);
		}
	}
	/**
	 * This method is used to load data into ObjectTablePane based on status
	 */
	private void loadStatusData() {
		if (getView().getAllRadioBtn().isSelected())
			getView().reload(getView().getFilterTextData());
		else
			getView().reload(getView().getFilterTextData(), getView().getActiveRadioBtn().isSelected() ? (short) 1 : (short) 0);

	}


	/** This method will be used to check whether Local Theme is in use by any local attribute or not. 
	 * 
	 * @param qiLocalThemeList
	 * @return true if Local Theme is in use
	 */
	private boolean isLocalThemeInUse(List<QiLocalTheme> qiLocalThemeList) {

		for(QiLocalTheme localTheme : qiLocalThemeList){
			if(getModel().isLocalThemeInUseByLocalDefect(localTheme.getLocalTheme())){
				return true;
			}
		}

		return false;

	}
}
