package com.honda.galc.client.teamlead.vios.opconfig;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosController;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.dao.conf.MCViosMasterOperationCheckerDao;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

public class ViosUnitNoConfigController extends AbstractViosController<ViosUnitNoConfigModel, ViosUnitNoConfigView> implements EventHandler<ActionEvent> {

	private static final String PLEASE_SELECT_UNIT_CONFIGURATION_TO_DELETE = "Please select Unit Configuration to delete";
	private static final String DELETE_THE_SELECTED_CONFIGURATION_MSG = "Are you sure you want to delete the selected configuration?";
	private static final String  DELETE_CHECKERS_SELECTED_CONFIGURATION_MSG = "Do you want to delete the checkers for selected configuration?";

	public ViosUnitNoConfigController(ViosUnitNoConfigModel model, ViosUnitNoConfigView view) {
		super(model, view);
	}

	@Override
	public void initEventHandlers() {
		addFilterTextFieldListener();
		addUnitNoTablePaneListener();
	}
	
	private void addFilterTextFieldListener() {
		getView().getFilterTextField().getControl().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				getView().reload(StringUtils.trimToEmpty(newValue).toUpperCase());
			}
		});
	}
	
	private void addUnitNoTablePaneListener() {
		getView().getUnitNumberTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MCViosMasterOperation>() {
			@Override
			public void changed(ObservableValue<? extends MCViosMasterOperation> observable,
					MCViosMasterOperation oldValue, MCViosMasterOperation newValue) {
				addContextMenuItems();
				getView().getCheckerDetailsTable().getTable().getItems().clear();
				if(newValue != null) {
					List<MCViosMasterOperationChecker> list = ServiceFactory.getDao(MCViosMasterOperationCheckerDao.class).findAllBy(getView().getPlatform().getGeneratedId(), newValue.getId().getUnitNo());
					getView().getCheckerDetailsTable().setData(list);
				}
			}
		});
		getView().getUnitNumberTablePane().getTable().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				addContextMenuItems();
			}
		});
	}
	
	private void addContextMenuItems()
	{
		getView().clearErrorMessage();
		if(getView().getUnitNumberTablePane().getSelectedItem() != null){
			String[] menuItems;
			menuItems = new String[] {
					ViosConstants.UPDATE
			};
			getView().getUnitNumberTablePane().createContextMenu(menuItems, new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					if(event.getSource() instanceof MenuItem) {
						MenuItem menuItem = (MenuItem) event.getSource();
						if(menuItem.getText().equals(ViosConstants.UPDATE)) {
							updateAction();
						} 
					}
				}
			});
		} else if(getView().getUnitNumberTablePane().getTable().getContextMenu() != null) {
			getView().getUnitNumberTablePane().getTable().getContextMenu().getItems().clear();
		}
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() instanceof LoggedButton){
			LoggedButton loggedButton = (LoggedButton) event.getSource();	
			if (ViosConstants.ADD.equals(loggedButton.getText())) addAction();
			else if (ViosConstants.DELETE.equals(loggedButton.getText())) deleteAction();
			
		}
	}
	
	private void updateAction() {
		MCViosMasterOperation masterOp = getView().getUnitNumberTablePane().getSelectedItem();
		ViosUnitNoConfigDialog dialog = new ViosUnitNoConfigDialog(getView().getStage(), ViosConstants.UPDATE, getView().getPlatform(), masterOp);
		dialog.showDialog();
		getView().reload();
	}
	
	private void addAction() {
		ViosUnitNoConfigDialog dialog = new ViosUnitNoConfigDialog(getView().getStage(), ViosConstants.ADD, getView().getPlatform(), null);
		dialog.showDialog();
		getView().reload();
	}
	
	private void deleteAction() {
		MCViosMasterOperation masterOp = getView().getUnitNumberTablePane().getSelectedItem();
		if(masterOp == null) {
			getView().setErrorMessage(PLEASE_SELECT_UNIT_CONFIGURATION_TO_DELETE);
			return;
		}
		if(MessageDialog.confirm(getView().getStage(), DELETE_THE_SELECTED_CONFIGURATION_MSG)) {
			int unitCheckers = ServiceFactory.getService(ViosMaintenanceService.class).getAllUnitCheckers(masterOp.getId());
			if(unitCheckers > 0 && MessageDialog.confirmNoSelected(getView().getStage(), DELETE_CHECKERS_SELECTED_CONFIGURATION_MSG)) {
				ServiceFactory.getService(ViosMaintenanceService.class).deleteViosUnitNoConfig(masterOp.getId());
			} else {
				ServiceFactory.getService(ViosMaintenanceService.class).deleteViosUnitNoConfigWithoutCheckers(masterOp.getId());
			}
			getView().reload();
		}
	}
	
	

}
