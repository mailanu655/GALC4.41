package com.honda.galc.client.loader.mbpn;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosDialog;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.teamlead.vios.ViosMasterValidator;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.AutoCompleteTextField;
import com.honda.galc.client.ui.component.LabeledControl;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.dao.conf.MCViosMasterMBPNMatrixDataDao;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixDataId;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;
import com.honda.galc.util.StringUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViosMBPNMatrixDialog extends AbstractViosDialog {

	private MCViosMasterPlatform platform;
	private MCViosMasterMBPNMatrixData selectedMCMbpnMasterData;
	
	private LoggedButton addButton;
	private LoggedButton updateButton;
	private LoggedButton cancelButton;
	
	private LabeledTextField mbpnMTCMaskTextField;
	private LabeledTextField mbpnMTCModelTextField;
	private LabeledTextField mbpnMTCTypesTextField;
	private ViosMBPNMatrixPanel view;
	private String action;
	private LabeledControl<AutoCompleteTextField> processNoTextField;
	private ProgressBar progressBar;
	private boolean isValidationError = true;
	
	
	public ViosMBPNMatrixDialog(Stage stage, String action, MCViosMasterPlatform platform, MCViosMasterMBPNMatrixData mcmbpnMasterData, ViosMBPNMatrixPanel view) {
		super(action + " MBPN Mask ", stage);
		this.platform = platform;
		this.view = view;
		this.action = action;
		this.selectedMCMbpnMasterData = mcmbpnMasterData;
		if(action.equals(ViosConstants.ADD)) {
			loadAddData(platform);
			updateButton.setDisable(true);
		} else if(action.equals(ViosConstants.UPDATE)) {
			loadUpdateData();
			addButton.setDisable(true);
		}
		
	}

	private void loadAddData(MCViosMasterPlatform platform) {
		List<String> processList = ServiceFactory.getDao(ChangeFormUnitDao.class).findAllProcessByPlatform(platform);
		processNoTextField.getControl().getSuggestionList().clear();
		processNoTextField.getControl().getSuggestionList().addAll(processList);
	}

	private void loadUpdateData() {

		if(selectedMCMbpnMasterData != null) {
			processNoTextField.getControl().settext(selectedMCMbpnMasterData.getId().getAsmProcNo());
			mbpnMTCMaskTextField.setText(selectedMCMbpnMasterData.getId().getMbpnMask());
			mbpnMTCModelTextField.setText(selectedMCMbpnMasterData.getId().getMtcModel());
			mbpnMTCTypesTextField.setText(selectedMCMbpnMasterData.getId().getMtcType());
			disableComponents();
		}
		
	}

	private void disableComponents() {
		processNoTextField.setDisable(true);
		mbpnMTCModelTextField.setDisable(true);
	}
	
	@Override
	public Node getMainContainer() {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(10);
		mainBox.setPadding(new Insets(10));
		
		mainBox.setMaxHeight(100);
		mainBox.setMaxWidth(500);
		
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(10);
		buttonBox.setPadding(new Insets(10));

		addButton = createBtn(ViosConstants.ADD);
		updateButton = createBtn(ViosConstants.UPDATE);
		cancelButton = createBtn(ViosConstants.CANCEL);
		buttonBox.getChildren().addAll(addButton, updateButton, cancelButton);
		
		VBox mbpnDetailsBox = new VBox();
		mbpnDetailsBox.setAlignment(Pos.CENTER);
		mbpnDetailsBox.setSpacing(10);
		
		processNoTextField = new LabeledControl<AutoCompleteTextField>("Process Number",
				new AutoCompleteTextField("processNoTextField"), true, true, new Insets(0), true, true);
		processNoTextField.getControl().setMinHeight(25);
		processNoTextField.getControl().setPrefWidth(150);
		BorderPane.setMargin(processNoTextField, new Insets(10));

	
		mbpnMTCMaskTextField = new LabeledTextField("MBPN Mask", true, new Insets(0), true, true, false);
		BorderPane.setMargin(mbpnMTCMaskTextField.getControl(), new Insets(0, 0, 0, 10));
		mbpnMTCMaskTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(30));
		mbpnMTCMaskTextField.getControl().setMinHeight(25);
		mbpnMTCMaskTextField.getControl().setPrefWidth(150);

		
		mbpnMTCModelTextField = new LabeledTextField("MTC Model", true, new Insets(0), true, true, false);
		BorderPane.setMargin(mbpnMTCModelTextField.getControl(), new Insets(0, 0, 0, 10));
		mbpnMTCModelTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(4));
		mbpnMTCModelTextField.getControl().setMinHeight(25);
		mbpnMTCModelTextField.getControl().setPrefWidth(150);
		

		mbpnMTCTypesTextField = new LabeledTextField("MTC Type", true, new Insets(0), true, true, false);
		BorderPane.setMargin(mbpnMTCTypesTextField.getControl(), new Insets(0, 0, 0, 10));
		mbpnMTCTypesTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(10000));
		mbpnMTCTypesTextField.getControl().setMinHeight(75);
		mbpnMTCTypesTextField.getControl().setPrefWidth(300);
		
		mbpnDetailsBox.getChildren().addAll(processNoTextField, mbpnMTCMaskTextField,mbpnMTCModelTextField, mbpnMTCTypesTextField);
		
		TitledPane addUpdatePane = createTitiledPane("MBPN Config Details", mbpnDetailsBox);
		
		progressBar = new ProgressBar();
		progressBar.setPrefWidth(150);
		progressBar.setVisible(false);

		
		mainBox.getChildren().addAll(addUpdatePane,progressBar, buttonBox);
		
		
		mbpnMTCMaskTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
            	if(!oldValue.equals(newValue)) {
            		if(action.equalsIgnoreCase(ViosConstants.ADD)) {
	        			List<MCViosMasterMBPNMatrixData> itemList = ServiceFactory.getDao(MCViosMasterMBPNMatrixDataDao.class)
	        					.findbyASMProcAndMBPNMask(platform.getGeneratedId(), StringUtils.trimToEmpty(mbpnMTCMaskTextField.getText()),StringUtils.trimToEmpty(processNoTextField.getControl().getText()));
	        			String mtcTypes = "";
	        			String mtcModelCode = "";
	        			if(itemList.size() > 0) {
			        		for (MCViosMasterMBPNMatrixData mcmbpnMasterData : itemList) {
			        			if(mtcTypes == "") {
			        				 mtcModelCode = mcmbpnMasterData.getId().getMtcModel();
			        				 mtcTypes = mcmbpnMasterData.getId().getMtcType();
			        			} else {
			        				 mtcTypes +=","+mcmbpnMasterData.getId().getMtcType();
			        			}
			        		}
	        			}
	        			mbpnMTCModelTextField.setText(mtcModelCode);
	        			mbpnMTCTypesTextField.setText(mtcTypes);
            		}
            	}
		    }
		});

		return mainBox;
	}

	@Override
	public void initHandler() {
		buttonActionHandler();
		
	}

	private void buttonActionHandler() {
		clearErrorMessage();
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				progressBar.setVisible(true);
				clearErrorMessage();
				Task<Void> mainTask = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						addAction();
						updateProgress(100, 100);
						return null;
					}
				};

				mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					public void handle(WorkerStateEvent t) {
						progressBar.setVisible(false);
						if(isValidationError) {
							Stage stage = (Stage) addButton.getScene().getWindow();
							stage.close();
							view.reload();
							MessageDialog.showInfo(view.getStage(), "MBPN Data added Succesfully.");
						}
					}
				});
				progressBar.progressProperty().bind(mainTask.progressProperty());
				new Thread(mainTask).start();
			}
		});

		updateButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					progressBar.setVisible(true);
					clearErrorMessage();
					clearErrorMessage();
					Task<Void> mainTask = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							addAction();
							updateProgress(100, 100);
							return null;
						}
					};

					mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
						public void handle(WorkerStateEvent t) {
							progressBar.setVisible(false);
							if(isValidationError) {
								Stage stage = (Stage) updateButton.getScene().getWindow();
								stage.close();
								view.reload();
								MessageDialog.showInfo(view.getStage(), "MBPN Data updated Succesfully.");
							}
						}
					});
					progressBar.progressProperty().bind(mainTask.progressProperty());
					new Thread(mainTask).start();
			}
		});
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) cancelButton.getScene().getWindow();
				stage.close();
				view.reload();
		
			}
		});
		
	}

	protected void addAction() {
		clearErrorMessage();
		MCViosMasterMBPNMatrixData mbpnData = new MCViosMasterMBPNMatrixData();
		MCViosMasterMBPNMatrixDataId id = new MCViosMasterMBPNMatrixDataId();
		id.setAsmProcNo(StringUtils.trimToEmpty(processNoTextField.getControl().getText()));
		id.setMbpnMask(StringUtils.trimToEmpty(mbpnMTCMaskTextField.getText()));
		id.setMtcModel(mbpnMTCModelTextField.getText()==null?"":StringUtils.trimToEmpty(mbpnMTCModelTextField.getText()).toUpperCase());
		id.setMtcType(StringUtils.trimToEmpty(mbpnMTCTypesTextField.getText()));
		mbpnData.setId(id);
		
		String errorMessage = ViosMasterValidator.MBPNMasterDataValidate(mbpnData);
		
		if(!StringUtil.isNullOrEmpty(errorMessage)){
			setErrorMessage(errorMessage);
			isValidationError = false;
			return;
		}

		if(action == ViosConstants.UPDATE) {
			ServiceFactory.getService(ViosMaintenanceService.class).deleteMBPNData(platform.getGeneratedId(),selectedMCMbpnMasterData);
		} else {
			ServiceFactory.getService(ViosMaintenanceService.class).deleteMBPNData(platform.getGeneratedId(),mbpnData);
		}
		
		String[] modelTypeArray = mbpnData.getId().getMtcType().split(",");

		if(mbpnData.getId().getMtcType().contains(",")) {
			for(String modelType : modelTypeArray) {
				MCViosMasterMBPNMatrixData masterData = new MCViosMasterMBPNMatrixData();
				masterData.setUserId(getUserId());
	        	MCViosMasterMBPNMatrixDataId masterDataId =  new MCViosMasterMBPNMatrixDataId(mbpnData.getId().getAsmProcNo(),
	        			mbpnData.getId().getMbpnMask(), StringUtils.trimToEmpty(modelType), mbpnData.getId().getMtcModel());
	        	masterDataId.setViosPlatformId(platform.getGeneratedId());
	        	masterData.setId(masterDataId);
				ServiceFactory.getDao(MCViosMasterMBPNMatrixDataDao.class).insert(masterData);
			}
		} else {
			mbpnData.getId().setViosPlatformId(platform.getGeneratedId());
			mbpnData.setUserId(getUserId());
			ServiceFactory.getDao(MCViosMasterMBPNMatrixDataDao.class).insert(mbpnData);
		}
		ServiceFactory.getService(MCViosMasterMBPNMatrixDataDao.class).updateMBPNMasterRevision(mbpnData, platform.getGeneratedId());
		isValidationError = true;
		
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}

}

