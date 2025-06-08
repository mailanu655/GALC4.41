package com.honda.galc.client.teamlead.vios.partconfig;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosController;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.dao.conf.MCViosMasterOperationPartCheckerDao;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

public class ViosPartConfigController extends AbstractViosController<ViosPartConfigModel, ViosPartConfigView> implements EventHandler<ActionEvent> {

	private static final String PLEASE_SELECT_PART_CONFIGURATION_TO_DELETE = "Please select Part Configuration to delete";
	private static final String DELETE_THE_SELECTED_CONFIGURATION_MSG = "Are you sure you want to delete the selected configuration?";
	private static final String DELETE_CHEKCER_SELECTED_CONFIGURATION_MSG= "Do you want to delete the Checkers for selected configuration?";
	public ViosPartConfigController(ViosPartConfigModel model, ViosPartConfigView view) {
		super(model, view);
	}

	@Override
	public void initEventHandlers() {
		addFilterTextFieldListener();
		addPartDetailsTablePaneListener();
	}
	
	private void addFilterTextFieldListener() {
		getView().getFilterTextField().getControl().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				getView().reload(StringUtils.trimToEmpty(newValue).toUpperCase());
			}
		});
	}
	
	private void addPartDetailsTablePaneListener() {
		getView().getPartDetailsTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MCViosMasterOperationPart>() {
			@Override
			public void changed(ObservableValue<? extends MCViosMasterOperationPart> observable,
					MCViosMasterOperationPart oldValue, MCViosMasterOperationPart newValue) {
				addContextMenuItems();
				getView().getCheckerDetailsTable().getTable().getItems().clear();
				if(newValue != null) {
					List<MCViosMasterOperationPartChecker> checkerList = ServiceFactory.getDao(MCViosMasterOperationPartCheckerDao.class).findAllBy(getView().getPlatform().getGeneratedId(), 
							newValue.getId().getUnitNo(), newValue.getId().getPartNo());
					getView().getCheckerDetailsTable().setData(checkerList);
				}
			}
		});
		getView().getPartDetailsTablePane().getTable().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				addContextMenuItems();
			}
		});
	}
	
	private void addContextMenuItems()
	{
		getView().clearErrorMessage();
		if(getView().getPartDetailsTablePane().getSelectedItem() != null){
			String[] menuItems;
			menuItems = new String[] {
					ViosConstants.UPDATE
			};
			getView().getPartDetailsTablePane().createContextMenu(menuItems, new EventHandler<ActionEvent>() {
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
		} else if(getView().getPartDetailsTablePane().getTable().getContextMenu() != null) {
			getView().getPartDetailsTablePane().getTable().getContextMenu().getItems().clear();
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
		MCViosMasterOperationPart masterOpPart = getView().getPartDetailsTablePane().getSelectedItem();
		ViosPartConfigDialog dialog = new ViosPartConfigDialog(getView().getStage(), ViosConstants.UPDATE, getView().getPlatform(), masterOpPart);
		dialog.showDialog();
		getView().reload();
	}
	
	private void addAction() {
		ViosPartConfigDialog dialog = new ViosPartConfigDialog(getView().getStage(), ViosConstants.ADD, getView().getPlatform(), null);
		dialog.showDialog();
		getView().reload();
	}
	
	private void deleteAction() {
		MCViosMasterOperationPart masterOpPart = getView().getPartDetailsTablePane().getSelectedItem();
		if(masterOpPart == null) {
			getView().setErrorMessage(PLEASE_SELECT_PART_CONFIGURATION_TO_DELETE);
			return;
		}
		
		if(MessageDialog.confirm(getView().getStage(), DELETE_THE_SELECTED_CONFIGURATION_MSG)) {
			int masterOpParts = ServiceFactory.getService(ViosMaintenanceService.class).getAllPartCheckers(masterOpPart.getId());
			if(masterOpParts  > 0 && MessageDialog.confirmNoSelected(getView().getStage(), DELETE_CHEKCER_SELECTED_CONFIGURATION_MSG)) {
				ServiceFactory.getService(ViosMaintenanceService.class).deleteViosPartConfig(masterOpPart.getId());
			} else {
				ServiceFactory.getService(ViosMaintenanceService.class).deleteViosPartConfigWithoutCheckers(masterOpPart.getId());
			}
			getView().reload();
		}
	}
	
	
}
