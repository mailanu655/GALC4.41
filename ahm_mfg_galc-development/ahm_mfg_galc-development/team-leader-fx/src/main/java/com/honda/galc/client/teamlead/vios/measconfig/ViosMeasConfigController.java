package com.honda.galc.client.teamlead.vios.measconfig;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosController;

import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementCheckerDao;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

public class ViosMeasConfigController extends AbstractViosController<ViosMeasConfigModel, ViosMeasConfigView> implements EventHandler<ActionEvent> {

	private static final String PLEASE_SELECT_MEAS_CONFIGURATION_TO_DELETE = "Please select Measurement Configuration to delete";
	private static final String DELETE_THE_SELECTED_CONFIGURATION_MSG = "Are you sure you want to delete the selected configuration?";
	private static final String DELETE_THE_CHECKERS_CONFIGURATION_MSG = "Do you want to delete the Checkers for selected configuration?";
	public ViosMeasConfigController(ViosMeasConfigModel model, ViosMeasConfigView view) {
		super(model, view);
	}

	@Override
	public void initEventHandlers() {
		addFilterTextFieldListener();
		addMeasDetailsTablePaneListener();
	}
	
	private void addFilterTextFieldListener() {
		getView().getFilterTextField().getControl().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				getView().reload(StringUtils.trimToEmpty(newValue).toUpperCase());
			}
		});
	}
	
	private void addMeasDetailsTablePaneListener() {
		getView().getMeasDetailsTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MCViosMasterOperationMeasurement>() {
			@Override
			public void changed(ObservableValue<? extends MCViosMasterOperationMeasurement> observable,
					MCViosMasterOperationMeasurement oldValue, MCViosMasterOperationMeasurement newValue) {
				addContextMenuItems();
				getView().getCheckerDetailsTable().getTable().getItems().clear();
				if(newValue != null) {
					List<MCViosMasterOperationMeasurementChecker> checkerList = ServiceFactory.getDao(MCViosMasterOperationMeasurementCheckerDao.class)
							.findAllBy(getView().getPlatform().getGeneratedId(), newValue.getId().getUnitNo(), 
									 newValue.getId().getMeasurementSeqNum());
					getView().getCheckerDetailsTable().setData(checkerList);
				}
			}
		});
		getView().getMeasDetailsTablePane().getTable().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				addContextMenuItems();
			}
		});
	}
	
	private void addContextMenuItems()
	{
		getView().clearErrorMessage();
		if(getView().getMeasDetailsTablePane().getSelectedItem() != null){
			String[] menuItems;
			menuItems = new String[] {
					ViosConstants.UPDATE
			};
			getView().getMeasDetailsTablePane().createContextMenu(menuItems, new EventHandler<ActionEvent>() {
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
		} else if(getView().getMeasDetailsTablePane().getTable().getContextMenu() != null) {
			getView().getMeasDetailsTablePane().getTable().getContextMenu().getItems().clear();
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
		MCViosMasterOperationMeasurement masterOpMeas = getView().getMeasDetailsTablePane().getSelectedItem();
		ViosMeasConfigDialog dialog = new ViosMeasConfigDialog(getView().getStage(), ViosConstants.UPDATE, getView().getPlatform(), masterOpMeas);
		dialog.showDialog();
		getView().reload();
	}
	
	private void addAction() {
		ViosMeasConfigDialog dialog = new ViosMeasConfigDialog(getView().getStage(), ViosConstants.ADD, getView().getPlatform(), null);
		dialog.showDialog();
		getView().reload();
	}
	
	private void deleteAction() {
		MCViosMasterOperationMeasurement masterOpMeas = getView().getMeasDetailsTablePane().getSelectedItem();
		if(masterOpMeas == null) {
			getView().setErrorMessage(PLEASE_SELECT_MEAS_CONFIGURATION_TO_DELETE);
			return;
		}
		if(MessageDialog.confirm(getView().getStage(), DELETE_THE_SELECTED_CONFIGURATION_MSG)) {

			int measurements = ServiceFactory.getService(ViosMaintenanceService.class).getAllUnitMeasurements(masterOpMeas.getId());
			if(measurements > 0 && MessageDialog.confirmNoSelected(getView().getStage(), DELETE_THE_CHECKERS_CONFIGURATION_MSG)) {
				ServiceFactory.getService(ViosMaintenanceService.class).deleteViosMeasConfig(masterOpMeas.getId());
			} else {
				ServiceFactory.getService(ViosMaintenanceService.class).deleteViosMeasConfigWithoutCheckers(masterOpMeas.getId());
			}
			getView().reload();
		} 
	}
	
	
}
