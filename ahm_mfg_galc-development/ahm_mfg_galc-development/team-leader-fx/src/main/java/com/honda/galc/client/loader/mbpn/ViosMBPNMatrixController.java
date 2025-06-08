package com.honda.galc.client.loader.mbpn;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosController;
import com.honda.galc.client.teamlead.vios.ExcelFileUploadDialog;
import com.honda.galc.client.teamlead.vios.IExcelUploader;
import com.honda.galc.client.teamlead.vios.VIOSMBPNMasterUploader;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ViosMBPNMatrixController extends AbstractViosController<ViosMBPNMatrixModel, ViosMBPNMatrixPanel>
implements   EventHandler<ActionEvent>{


	private static final String PLEASE_SELECT_MBPN_CONFIGURATION_TO_DELETE = "Please select MBPN Configuration for processing";
	private static final String DELETE_THE_SELECTED_CONFIGURATION_MSG = "Are you sure you want to delete the selected configuration?";
	

	public ViosMBPNMatrixController(ViosMBPNMatrixModel model, ViosMBPNMatrixPanel view) {
		super(model, view);
	}


	@Override
	public void initEventHandlers() {
		addFilterTextFieldListener();
	}
	
	
	private void addFilterTextFieldListener() {
		getView().getFilterTextField().getControl().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				getView().reload(StringUtils.trimToEmpty(newValue).toUpperCase());
			}
		});
	}

	
	public void handle(ActionEvent actionEvent) {
		getView().clearErrorMessage();
		if(actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if(ViosConstants.UPLOAD.equals(loggedButton.getText())) {
				
				IExcelUploader<MCViosMasterMBPNMatrixData> excelUploader = new VIOSMBPNMasterUploader(getView().getPlatform().getViosPlatformId(), getView().getPlatform().getUserId());
				ExcelFileUploadDialog dialog = new ExcelFileUploadDialog(getView().getStage(), MCViosMasterMBPNMatrixData.class,
						excelUploader);
				dialog.showDialog();
				getView().reload();
				
			} else if(ViosConstants.ADD.equals(loggedButton.getText())) {
				
				ViosMBPNMatrixDialog mfgDataLoad = new ViosMBPNMatrixDialog(getView().getStage(), ViosConstants.ADD, getView().getPlatform(), null, getView());
				mfgDataLoad.showDialog();
				
			} else if(ViosConstants.UPDATE.equals(loggedButton.getText())) {
				
				MCViosMasterMBPNMatrixData mcmbpnMasterData =  getView().getmbpnMasterDataList().getSelectedItem();
				if(mcmbpnMasterData == null) { 
					getView().setErrorMessage(PLEASE_SELECT_MBPN_CONFIGURATION_TO_DELETE);
					return;
				}
				ViosMBPNMatrixDialog mfgDataLoad = new ViosMBPNMatrixDialog(getView().getStage(), ViosConstants.UPDATE, getView().getPlatform(), mcmbpnMasterData, getView());
				mfgDataLoad.showDialog();
				
			} else if(ViosConstants.DELETE.equals(loggedButton.getText())) {
				
				MCViosMasterMBPNMatrixData mcmbpnMasterData =  getView().getmbpnMasterDataList().getSelectedItem();
				deleteAction(mcmbpnMasterData);
			}
		} 
	}
	
	private void deleteAction(MCViosMasterMBPNMatrixData mcmbpnMasterData) {
		if(mcmbpnMasterData == null) {
			getView().setErrorMessage(PLEASE_SELECT_MBPN_CONFIGURATION_TO_DELETE);
			return;
		}
		if(MessageDialog.confirm(getView().getStage(), DELETE_THE_SELECTED_CONFIGURATION_MSG)) {
			getView().getProgressBar().setVisible(true);
			Task<Void> mainTask = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					getView().getUploadButton().setDisable(true);
					getView().getAddButton().setDisable(true);
					getView().getUpdateButton().setDisable(true);
					getView().getDeleteButton().setDisable(true);
					ServiceFactory.getService(ViosMaintenanceService.class).deleteMBPNData(getView().getPlatform().getViosPlatformId(),mcmbpnMasterData);
					updateProgress(100, 100);
					return null;
				}
			};
	
			mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				public void handle(WorkerStateEvent t) {
					getView().getProgressBar().setVisible(false);
					getView().reload();
					MessageDialog.showInfo(getView().getStage(), "Selected MBPN Data deleted Succesfully.");
					getView().getUploadButton().setDisable(false);
					getView().getAddButton().setDisable(false);
					getView().getUpdateButton().setDisable(false);
					getView().getDeleteButton().setDisable(false);
				}
			});
			getView().getProgressBar().progressProperty().bind(mainTask.progressProperty());
			new Thread(mainTask).start();
		}
	}


}
