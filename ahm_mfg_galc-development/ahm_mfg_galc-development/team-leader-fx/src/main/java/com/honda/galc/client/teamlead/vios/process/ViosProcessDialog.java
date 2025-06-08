package com.honda.galc.client.teamlead.vios.process;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosDialog;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.teamlead.vios.ViosMasterValidator;
import com.honda.galc.client.ui.component.AutoCompleteTextField;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledControl;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCViosMasterProcessDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.MCViosMasterProcess;
import com.honda.galc.entity.conf.MCViosMasterProcessId;
import com.honda.galc.service.ServiceFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * <h3>ViosProcessDialog Class description</h3>
 * <p>
 * Dialog for Vios Process Maintenance
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
 * @author Hemant Kumar<br>
 *        Aug 28, 2018
 */
public class ViosProcessDialog extends AbstractViosDialog {

	private MCViosMasterPlatform platform;
	private MCViosMasterProcess selectedProcess;

	private LabeledControl<LoggedLabel> plantLabel;
	private LabeledControl<LoggedLabel> deptLabel;
	private LabeledControl<LoggedLabel> modelYearLabel;
	private LabeledControl<LoggedLabel> prodRateLabel;
	private LabeledControl<LoggedLabel> lineNoLabel;
	private LabeledControl<LoggedLabel> vmcLabel;

	private LabeledComboBox<String> processPointCombobox;
	private LabeledControl<AutoCompleteTextField> processNoTextField;
	private LabeledTextField processSeqNumTextField;

	private LoggedButton addButton;
	private LoggedButton moveButton;
	private LoggedButton cancelButton;

	private int maxSeqNum;

	public ViosProcessDialog(Stage stage, String action, MCViosMasterPlatform platform, MCViosMasterProcess selectedProcess) {
		super(action + " VIOS Process Mapping", stage);
		this.platform = platform;
		this.selectedProcess = selectedProcess;
		loadPlatformData();
		if(action.equals(ViosConstants.ADD)) {
			moveButton.setDisable(true);
			loadProcessData();
			addProcesSeqNumTextFieldListener();
		} else if(action.equals(ViosConstants.MOVE)) {
			loadMoveData();
			addButton.setDisable(true);
			processNoTextField.setDisable(true);
			processSeqNumTextField.setDisable(true);
		}
	}

	@Override
	public Node getMainContainer() {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(10);
		mainBox.setPadding(new Insets(10));

		VBox platformBox = new VBox();
		platformBox.setAlignment(Pos.CENTER);
		platformBox.setSpacing(10);
		platformBox.setPadding(new Insets(10));

		plantLabel = createLabeledControl(ViosConstants.PLANT);
		deptLabel = createLabeledControl(ViosConstants.DEPARTMENT);
		modelYearLabel = createLabeledControl(ViosConstants.MODEL_YEAR);
		prodRateLabel = createLabeledControl(ViosConstants.PRODUCTION_RATE);
		lineNoLabel = createLabeledControl(ViosConstants.LINE_NUMBER);
		vmcLabel = createLabeledControl(ViosConstants.VEHICLE_MODEL_CODE);

		platformBox.getChildren().addAll(plantLabel, deptLabel, modelYearLabel, prodRateLabel, lineNoLabel, vmcLabel);

		TitledPane platformPane = createTitiledPane("Selected Platform", platformBox);

		processPointCombobox = createLabeledComboBox("processPointCombobox", "Process Point", true, true, false);
		processPointCombobox.getControl().setPromptText("Select");

		processNoTextField = new LabeledControl<AutoCompleteTextField>("Process Number", new AutoCompleteTextField("processNoTextField"), 
				true, false, new Insets(0), true, true);
		BorderPane.setMargin(processNoTextField.getControl(), new Insets(0, 0, 0, 40));

		processSeqNumTextField = new LabeledTextField("Process Seq Num", true, new Insets(0), true, true, true);
		processSeqNumTextField.setDisable(true);
		BorderPane.setMargin(processSeqNumTextField.getControl(), new Insets(0, 0, 0, 36));

		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(10);
		buttonBox.setPadding(new Insets(10));

		addButton = createBtn(ViosConstants.ADD);
		moveButton = createBtn(ViosConstants.MOVE);
		cancelButton = createBtn(ViosConstants.CANCEL);

		buttonBox.getChildren().addAll(addButton, moveButton, cancelButton);

		mainBox.getChildren().addAll(platformPane, processPointCombobox, processNoTextField, processSeqNumTextField, buttonBox);
		return mainBox;
	}

	@Override
	public void initHandler() {
		addProcessPointComboboxListener();
		buttonActionHandler();
	}

	@Override
	public void loadData() {
		loadProcessPointCombobox();
	}

	private void buttonActionHandler() {
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					MCViosMasterProcess proc = ServiceFactory.getDao(MCViosMasterProcessDao.class).findByKey(new MCViosMasterProcessId(platform.getGeneratedId(), processNoTextField.getControl().getText()));
					if(proc != null) {
						setErrorMessage("Process is already mapped for the given Platform");
						return;
					}
					@SuppressWarnings("unused")
					int seqenceNo = 0;
					try{
						seqenceNo = Integer.parseInt(processSeqNumTextField.getText());
				}catch(NumberFormatException e){
					seqenceNo = 0;
				}
					MCViosMasterProcess masterProcess = new MCViosMasterProcess(platform.getGeneratedId(), 
							processNoTextField.getControl().getText(), 
							processPointCombobox.getControl().getSelectionModel().getSelectedItem(), 
							seqenceNo);
					masterProcess.setUserId(getUserId());
					String statusMsg = ViosMasterValidator.masterProcessValidate(masterProcess);
					if(StringUtils.isNotBlank(statusMsg)) {
						setErrorMessage(statusMsg);
						return ;
					}
					masterProcess.setUserId(getUserId());
					ServiceFactory.getDao(MCViosMasterProcessDao.class).resequenceAndInsert(masterProcess);
					Stage stage = (Stage) addButton.getScene().getWindow();
					stage.close();
				} catch (Exception e) {
					Logger.getLogger().error(e, new LogRecord("An exception occured while saving MCViosMasterProcess Object"));
					setErrorMessage("Something went wrong while adding Vios Process");
				}
			}
		});

		moveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					
					MCViosMasterProcess newProcess = new MCViosMasterProcess(platform.getGeneratedId(), 
							processNoTextField.getControl().getText(), 
							processPointCombobox.getControl().getSelectionModel().getSelectedItem(), 
							Integer.parseInt(processSeqNumTextField.getText()));
					
					String statusMsg = ViosMasterValidator.masterProcessValidate(newProcess);
					if(StringUtils.isNotBlank(statusMsg)) {
						setErrorMessage(statusMsg);
						return ;
					}
					if(compareProcess(selectedProcess, newProcess)) {
						return;
					}
					newProcess.setUserId(selectedProcess.getUserId());
					ServiceFactory.getDao(MCViosMasterProcessDao.class).resequenceAndMove(selectedProcess.getProcessPointId(), newProcess);
					Stage stage = (Stage) moveButton.getScene().getWindow();
					stage.close();
				} catch (Exception e) {
					Logger.getLogger().error(e, new LogRecord("An exception occured while moving MCViosMasterProcess Object"));
					setErrorMessage("Something went wrong while moving Vios Process");
				}
			}
		});

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) cancelButton.getScene().getWindow();
				stage.close();
			}
		});
	}

	private boolean compareProcess(MCViosMasterProcess oldProcess, MCViosMasterProcess newProcess) {
		if(oldProcess.getId().getViosPlatformId().equals(newProcess.getId().getViosPlatformId())
				&& oldProcess.getId().getAsmProcNo().equals(newProcess.getId().getAsmProcNo())
				&& oldProcess.getProcessPointId().equals(newProcess.getProcessPointId())
				&& oldProcess.getProcessSeqNum() == newProcess.getProcessSeqNum()) {
			setErrorMessage("There is no change to update");
			return true;
		}
		return false;
	}


	private void loadProcessPointCombobox() {
		List<String> processPointList = ServiceFactory.getDao(ProcessPointDao.class).findAllProcessPoint();
		processPointCombobox.getControl().getItems().clear();
		processPointCombobox.getControl().getItems().addAll(processPointList);
	}

	private void addProcessPointComboboxListener() {
		processPointCombobox.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String processPoint = StringUtils.trimToEmpty(newValue);
				if (selectedProcess == null) {
					processSeqNumTextField.setDisable(newValue == null);
					if (processPoint != null) {
						maxSeqNum = ServiceFactory.getDao(MCViosMasterProcessDao.class).getMaxProcesSeqNumBy(platform.getGeneratedId(), processPoint);
						processSeqNumTextField.getControl().setText(String.valueOf(maxSeqNum + 1));
					} 
				} else {
					if(processPoint != null) {
						if(StringUtils.trimToEmpty(newValue).equals(selectedProcess.getProcessPointId())) {
							processSeqNumTextField.getControl().setText(String.valueOf(selectedProcess.getProcessSeqNum()));
						} else {
							maxSeqNum = ServiceFactory.getDao(MCViosMasterProcessDao.class).getMaxProcesSeqNumBy(platform.getGeneratedId(), processPoint);
							processSeqNumTextField.getControl().setText(String.valueOf(maxSeqNum + 1));
						}
					}
				}
			}
		});
	}

	private void addProcesSeqNumTextFieldListener() {
		processSeqNumTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				clearErrorMessage();
				if(StringUtils.isNotEmpty(newValue)) {
					if (newValue.matches("\\d{0,10}")) {
						int seq = Integer.parseInt(newValue);
						if (!(seq > 0 && seq <= (maxSeqNum + 1))) {
							processSeqNumTextField.getControl().setText(oldValue);
						}
					} else {
						processSeqNumTextField.getControl().setText(oldValue);
					} 
				}
			}
		});
	}

	private void loadPlatformData() {
		if(platform != null) {
			plantLabel.getControl().setText(platform.getPlantLocCode());
			deptLabel.getControl().setText(platform.getDeptCode());
			modelYearLabel.getControl().setText(platform.getModelYearDate().toString());
			prodRateLabel.getControl().setText(platform.getProdSchQty().toString());
			lineNoLabel.getControl().setText(platform.getProdAsmLineNo());
			vmcLabel.getControl().setText(platform.getVehicleModelCode());
		}
	}

	private void loadMoveData() {
		if(selectedProcess != null) {
			processPointCombobox.getControl().getSelectionModel().select(selectedProcess.getProcessPointId());
			processNoTextField.getControl().setText(selectedProcess.getId().getAsmProcNo());
			processNoTextField.getControl().getSuggestionPopup().hide();
			processSeqNumTextField.getControl().setText(String.valueOf(selectedProcess.getProcessSeqNum()));
		}
	}

	private void loadProcessData() {
		List<String> processList = ServiceFactory.getDao(ChangeFormUnitDao.class).findAllProcessByPlatform(platform);
		processNoTextField.getControl().getSuggestionList().clear();
		processNoTextField.getControl().getSuggestionList().addAll(processList);
	}

	private LabeledControl<LoggedLabel> createLabeledControl(String labelName) {
		LabeledControl<LoggedLabel> label = new LabeledControl<LoggedLabel>(labelName, new LoggedLabel(labelName), true, false, new Insets(0), true, false);
		return label;
	}
}
