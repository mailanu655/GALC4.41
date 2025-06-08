package com.honda.galc.client.teamlead.vios.opconfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckerType;
import com.honda.galc.checkers.Checkers;
import com.honda.galc.checkers.ReactionType;
import com.honda.galc.client.teamlead.vios.AbstractViosDialog;
import com.honda.galc.client.teamlead.vios.ExcelFileUploadDialog;
import com.honda.galc.client.teamlead.vios.IExcelUploader;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.teamlead.vios.ViosExcelUploader;
import com.honda.galc.client.teamlead.vios.ViosMasterValidator;
import com.honda.galc.client.teamlead.vios.ViosOperationCheckerExcelUploader;
import com.honda.galc.client.ui.component.AutoCompleteTextField;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledControl;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCViosMasterOperationCheckerDao;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
public class ViosUnitNoConfigDialog extends AbstractViosDialog {

	private MCViosMasterPlatform platform;
	private MCViosMasterOperation selectedOp;

	private LabeledControl<LoggedLabel> plantLabel;
	private LabeledControl<LoggedLabel> deptLabel;
	private LabeledControl<LoggedLabel> modelYearLabel;
	private LabeledControl<LoggedLabel> prodRateLabel;
	private LabeledControl<LoggedLabel> lineNoLabel;
	private LabeledControl<LoggedLabel> vmcLabel;

	private LabeledControl<AutoCompleteTextField> unitNoTextField;
	private LabeledTextField commonNameTextField;
	
	private LabeledTextField viewTextField;
	private LabeledTextField processorTextField;
	private LoggedButton uploadButton;
	private CheckBox opCheckBox;
	
	private ObjectTablePane<MCViosMasterOperationChecker> checkerDetailsTablePane;
	
	private LabeledComboBox<String> checkPointCombobox;
	private LabeledTextField checkSeqTextField;
	private LabeledTextField checkNameTextField;
	private LabeledComboBox<String> checkerCombobox;
	private LabeledComboBox<String> reactionTypeCombobox;
	
	private LoggedButton addCheckerButton;
	private LoggedButton removeCheckerButton;
	private LoggedButton saveCheckerButton;
	private LoggedButton uploadCheckerButton;
	
	private StackPane checkerButtonPane;
	
	private MCViosMasterOperationChecker selectedOpChecker;

	private LoggedButton addButton;
	private LoggedButton updateButton;
	private LoggedButton cancelButton;


	public ViosUnitNoConfigDialog(Stage stage, String action, MCViosMasterPlatform platform, MCViosMasterOperation selectedOp) {
		super(action + " Unit Number Config", stage);
		this.platform = platform;
		this.selectedOp = selectedOp;
		loadPlatformData();
		if(action.equals(ViosConstants.ADD)) {
			loadUnitNoSuggestions();
			updateButton.setDisable(true);
		} else if(action.equals(ViosConstants.UPDATE)) {
			loadUpdateData();
			addButton.setDisable(true);
		}
	}

	@Override
	public Node getMainContainer() {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(10);
		mainBox.setPadding(new Insets(10));

		HBox platformBox = new HBox();
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
		
		unitNoTextField = new LabeledControl<AutoCompleteTextField>("Unit Number", new AutoCompleteTextField("unitNoTextField"), 
				true, false, new Insets(0), true, true);
		BorderPane.setMargin(unitNoTextField.getControl(), new Insets(0, 0, 0, 5));
		unitNoTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(6));
		unitNoTextField.getControl().setMinHeight(25);
		unitNoTextField.getControl().setPrefWidth(25);
		

		
		commonNameTextField = new LabeledTextField("Common Name", true, new Insets(0), true, false, false);
		BorderPane.setMargin(commonNameTextField.getControl(), new Insets(0, 0, 0, 5));
		commonNameTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(255));
		commonNameTextField.getControl().setMinHeight(25);
		commonNameTextField.getControl().setPrefWidth(225);
		
		viewTextField = new LabeledTextField("View", true, new Insets(0), true, false, false);
		BorderPane.setMargin(viewTextField.getControl(), new Insets(0, 0, 0, 5));
		viewTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(255));
		viewTextField.getControl().setMinHeight(25);
		viewTextField.getControl().setPrefWidth(400);
		
		processorTextField = new LabeledTextField("Processor", true, new Insets(0), true, false, false);
		uploadButton = new LoggedButton("Upload", "uploadOperationButton");
		uploadButton.getStyleClass().add("small-btn");
		
		BorderPane.setMargin(processorTextField.getControl(), new Insets(0, 0, 0, 5));
		processorTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(255));
		processorTextField.getControl().setMinHeight(25);
		processorTextField.getControl().setPrefWidth(400);
		
		opCheckBox = new CheckBox("Op Check");
		opCheckBox.setStyle("-fx-font-weight: bold");
		opCheckBox.setSelected(true);
		
		HBox unitBox1 = new HBox();
		unitBox1.setAlignment(Pos.CENTER_LEFT);
		unitBox1.setSpacing(10);
		unitBox1.getChildren().addAll(unitNoTextField, commonNameTextField, opCheckBox, viewTextField, processorTextField );
		
		HBox unitBox2 = new HBox();
		unitBox2.setAlignment(Pos.CENTER_LEFT);
		unitBox2.setSpacing(10);
		
		unitBox2.getChildren().addAll(viewTextField, processorTextField, uploadButton);
		
		VBox unitDetailsBox = new VBox();
		unitDetailsBox.setAlignment(Pos.CENTER_LEFT);
		unitDetailsBox.setSpacing(10);
		unitDetailsBox.setPadding(new Insets(10));
		
		unitDetailsBox.getChildren().addAll(unitBox1, unitBox2);
		
		TitledPane configPane = createTitiledPane("Unit Config Details", unitDetailsBox);
		
		checkerDetailsTablePane = createCheckerDetailsTable();
		checkerDetailsTablePane.getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MCViosMasterOperationChecker>() {
			@Override
			public void changed(ObservableValue<? extends MCViosMasterOperationChecker> observable,
					MCViosMasterOperationChecker oldValue, MCViosMasterOperationChecker newValue) {
				addContextMenuItems();
			}
		});
		checkerDetailsTablePane.getTable().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				addContextMenuItems();
			}
		});
		HBox checkerDetailsBox = new HBox();
		checkerDetailsBox.setAlignment(Pos.CENTER);
		checkerDetailsBox.setSpacing(10);
		
		checkPointCombobox = createLabeledComboBox("checkPointCombobox", "Check Point", false, true, false);
		checkerCombobox = createLabeledComboBox("checkerCombobox", "Checker", false, true, false);
		reactionTypeCombobox = createLabeledComboBox("reactionTypeCombobox", "Reaction Type", false, true, false);
		
		checkSeqTextField = new LabeledTextField("Check Seq", false, new Insets(0), true, true);
		checkSeqTextField.getControl().setPrefWidth(100);
		checkSeqTextField.getControl().setMinHeight(25);
		checkSeqTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(10));
		checkSeqTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	checkSeqTextField.getControl().setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
		BorderPane.setMargin(checkSeqTextField.getControl(), new Insets(0, 0, 0, 10));
		
		checkNameTextField = new LabeledTextField("Check Name", false, new Insets(0), true, true);
		checkNameTextField.getControl().setPrefWidth(200);
		checkNameTextField.getControl().setMinHeight(25);
		checkNameTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(35));
		BorderPane.setMargin(checkNameTextField.getControl(), new Insets(0, 0, 0, 10));
		
		VBox checkerButtonBox = new VBox();
		checkerButtonBox.setAlignment(Pos.CENTER);
		checkerButtonBox.setSpacing(5);
		
		addCheckerButton = new LoggedButton("Add", "addCheckerButton");
		addCheckerButton.getStyleClass().add("small-btn");
		
		saveCheckerButton = new LoggedButton("Save", "saveCheckerButton");
		saveCheckerButton.getStyleClass().add("small-btn");
		
		checkerButtonPane = new StackPane(addCheckerButton, saveCheckerButton);
		addCheckerButton.toFront();
		
		removeCheckerButton = new LoggedButton("Remove", "removeCheckerButton");
		removeCheckerButton.getStyleClass().add("small-btn");
		
		uploadCheckerButton  = new LoggedButton("Upload", "uploadeCheckerButton");
		uploadCheckerButton.getStyleClass().add("small-btn");
		
		
		
		checkerButtonBox.getChildren().addAll(checkerButtonPane, removeCheckerButton, uploadCheckerButton);
		
		checkerDetailsBox.getChildren().addAll(checkPointCombobox, checkSeqTextField, checkNameTextField, checkerCombobox, reactionTypeCombobox , checkerButtonBox);
		
		VBox checkerBox = new VBox();
		checkerBox.setAlignment(Pos.CENTER);
		checkerBox.setSpacing(10);
		checkerBox.setPadding(new Insets(10));

		checkerBox.getChildren().addAll(checkerDetailsBox, checkerDetailsTablePane);
		
		TitledPane checkerTitledPane = createTitiledPane("Checker Details", checkerBox);
		
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(10);
		buttonBox.setPadding(new Insets(10));

		addButton = createBtn(ViosConstants.ADD);
		updateButton = createBtn(ViosConstants.UPDATE);
		cancelButton = createBtn(ViosConstants.CANCEL);

		
		unitNoTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
            	if(!oldValue.equals(newValue)) {
            		List<MCViosMasterOperationChecker> checkerList = ServiceFactory.getDao(MCViosMasterOperationCheckerDao.class).findAllBy(platform.getGeneratedId(), unitNoTextField.getControl().getText());
            		checkerDetailsTablePane.setData(checkerList);
            	}
		    }
		});
		buttonBox.getChildren().addAll(addButton, updateButton, cancelButton);

		mainBox.getChildren().addAll(platformPane, configPane, checkerTitledPane, buttonBox);
		return mainBox;
	}

	@Override
	public void initHandler() {
		buttonActionHandler();
	}

	@Override
	public void loadData() {
		loadCheckPointComboBox();
		loadCheckerComboBox();
		loadReactionTypeComboBox();
	}
	
	private void loadCheckPointComboBox() {
		checkPointCombobox.getControl().getItems().clear();
		checkPointCombobox.getControl().setPromptText(ViosConstants.SELECT);
		List<CheckPoints> checkPointsList = Arrays.asList(CheckPoints.values());
		List<String> checkPointStringList = new ArrayList<String>();
		for(CheckPoints cp : checkPointsList) {
			checkPointStringList.add(cp.toString());
		}
		Collections.sort(checkPointStringList);
		checkPointCombobox.getControl().getItems().addAll(checkPointStringList);
	}
	
	private void loadCheckerComboBox() {
		checkerCombobox.getControl().getItems().clear();
		checkerCombobox.getControl().setPromptText(ViosConstants.SELECT);
		List<Checkers> checkers = Checkers.getCheckers(CheckerType.Operation);
		List<String> checkerList = new ArrayList<String>();
		for(Checkers checker : checkers) {
			checkerList.add(checker.toString());
		}
		Collections.sort(checkerList);
		checkerCombobox.getControl().getItems().addAll(checkerList);
	}
	
	private void loadReactionTypeComboBox() {
		reactionTypeCombobox.getControl().getItems().clear();
		reactionTypeCombobox.getControl().setPromptText(ViosConstants.SELECT);
		List<ReactionType> reactionTypeList = Arrays.asList(ReactionType.values());
		List<String> reactionTypeStringList = new ArrayList<String>();
		for(ReactionType rt : reactionTypeList) {
			reactionTypeStringList.add(rt.toString());
		}
		reactionTypeCombobox.getControl().getItems().addAll(reactionTypeStringList);
	}
	
	private void loadUnitNoSuggestions() {
		List<String> unitNoList = ServiceFactory.getDao(ChangeFormUnitDao.class).findAllUnitNoByPlatform(platform);
		unitNoTextField.getControl().setSuggestionList(unitNoList);
	}

	private void buttonActionHandler() {
		addCheckerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addCheckerAction();
			}
		});
		
		saveCheckerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				saveCheckerAction();
			}
		});
		
		removeCheckerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				removeCheckerAction();
			}
		});
		
		uploadCheckerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateCheckerAction();
			}
		});
		
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateAction();
			}
		});
		
		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateAction();
			}
		});

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) cancelButton.getScene().getWindow();
				stage.close();
			}
		});
		
		uploadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				uploadOperationAction();
			}
		});
	}
	
	private void addCheckerAction() {
		
		MCViosMasterOperationChecker masterOpChecker = getViosMasterOpCheckerObj();
		String statusMsg = ViosMasterValidator.updateMasterOperationCheckerValidate(masterOpChecker);
		if(StringUtils.isNotBlank(statusMsg)) {
			setErrorMessage(statusMsg);
			return ;
		}
		if(checkDuplicateChecker(masterOpChecker)) {
			return;
		}
		checkerDetailsTablePane.getTable().getItems().add(masterOpChecker);
		Collections.sort(checkerDetailsTablePane.getTable().getItems(), new Comparator<MCViosMasterOperationChecker>() {
			@Override
			public int compare(MCViosMasterOperationChecker o1, MCViosMasterOperationChecker o2) {
				return String.valueOf(o1.getId().getCheckSeq()).compareTo(String.valueOf(o2.getId().getCheckSeq()));
			}
		});
		clearCheckerFields();
	}

	private MCViosMasterOperationChecker getViosMasterOpCheckerObj() {
		String checkPoint = StringUtils.trimToEmpty(checkPointCombobox.getControl().getSelectionModel().getSelectedItem());
		int checkSeq;
		try {
			checkSeq = Integer.parseInt(StringUtils.trimToEmpty(checkSeqTextField.getText()));
		} catch (NumberFormatException e) {
			checkSeq = 0;
		}
		String checkName = StringUtils.trimToEmpty(checkNameTextField.getText());
		//String checker = StringUtils.trimToEmpty(checkerCombobox.getControl().getSelectionModel().getSelectedItem());
		String checkerStr = StringUtils.trimToEmpty(checkerCombobox.getControl().getSelectionModel().getSelectedItem());
		String checker = null;
		if(checkerStr != null && !checkerStr.equals("") ) {
			checker = Checkers.getChecker(checkerStr).getCheckerClass().getName();
		} 
		String reactionTypeStr = StringUtils.trimToEmpty(reactionTypeCombobox.getControl().getSelectionModel().getSelectedItem());
		ReactionType reactionType = null;
		if(reactionTypeStr != null && !reactionTypeStr.equals("") ) {
		 reactionType = ReactionType.getReactionType(reactionTypeStr);
		} 
		String viosPlatformId = platform.getGeneratedId();
		String unitNo = StringUtils.trimToEmpty(unitNoTextField.getControl().getText());
		MCViosMasterOperationChecker masterOpChecker = new MCViosMasterOperationChecker(viosPlatformId, unitNo, checkPoint, checkSeq, checkName, checker, reactionType);
		masterOpChecker.setUserId(getUserId());
		return masterOpChecker;
	}
	
	private void saveCheckerAction() {
		checkerDetailsTablePane.getTable().getItems().remove(selectedOpChecker);
		MCViosMasterOperationChecker opChecker = getViosMasterOpCheckerObj();
		String statusMsg = ViosMasterValidator.updateMasterOperationCheckerValidate(opChecker);
		if(StringUtils.isNotBlank(statusMsg)) {
			setErrorMessage(statusMsg);
			return ;
		}
		if(checkDuplicateChecker(opChecker)) {
			return;
		}
		checkerDetailsTablePane.getTable().getItems().add(opChecker);
		Collections.sort(checkerDetailsTablePane.getTable().getItems(), new Comparator<MCViosMasterOperationChecker>() {
			@Override
			public int compare(MCViosMasterOperationChecker o1, MCViosMasterOperationChecker o2) {
				return String.valueOf(o1.getId().getCheckSeq()).compareTo(String.valueOf(o2.getId().getCheckSeq()));
			}
		});
		removeCheckerButton.setDisable(false);
		addCheckerButton.toFront();
		clearCheckerFields();
	}
	
	private void removeCheckerAction() {
		MCViosMasterOperationChecker opChecker = checkerDetailsTablePane.getTable().getSelectionModel().getSelectedItem();
		if(opChecker == null) {
			setErrorMessage("Please select checker to remove");
			return;
		}
		checkerDetailsTablePane.getTable().getItems().remove(opChecker);
	}
	
	private void updateCheckerAction() {
		IExcelUploader<MCViosMasterOperationChecker> excelUploader = new ViosOperationCheckerExcelUploader(this.platform.getViosPlatformId(), this.getUserId());
		ExcelFileUploadDialog dialog = new ExcelFileUploadDialog(getStage(), MCViosMasterOperationChecker.class,
				excelUploader);
		dialog.showDialog();
	}
	
	private void uploadOperationAction() {
		IExcelUploader<MCViosMasterOperation> excelUploader = new ViosExcelUploader<MCViosMasterOperation>(this.platform.getViosPlatformId(), this.getUserId());
		ExcelFileUploadDialog dialog = new ExcelFileUploadDialog(getStage(), MCViosMasterOperation.class,
				excelUploader);
		dialog.showDialog();
	}
	
	private boolean checkDuplicateChecker(MCViosMasterOperationChecker masterOpChecker) {
		List<MCViosMasterOperationChecker> checkerList = checkerDetailsTablePane.getTable().getItems();
		for(MCViosMasterOperationChecker checker : checkerList) {
			if(checker.getId().getCheckPoint().equals(masterOpChecker.getId().getCheckPoint())
					&& checker.getId().getCheckSeq() == masterOpChecker.getId().getCheckSeq()
					&& checker.getId().getUnitNo().equals(masterOpChecker.getId().getUnitNo())
					&& checker.getId().getViosPlatformId().equals(masterOpChecker.getId().getViosPlatformId())) {
						setErrorMessage("Duplicate Checker");
						return true;
					}
		}
		return false;
	}
	
	private void clearCheckerFields() {
		checkPointCombobox.getControl().getSelectionModel().clearSelection();
		checkSeqTextField.clear();
		checkNameTextField.clear();
		checkerCombobox.getControl().getSelectionModel().clearSelection();
		reactionTypeCombobox.getControl().getSelectionModel().clearSelection();
	}
	

	
	private void updateAction() {
		try {
			
				String viosPlatformId = platform.getGeneratedId();
				String unitNo = unitNoTextField.getControl().getText();
				String view = viewTextField.getText();
				String processor = processorTextField.getText();
				String commonName = commonNameTextField.getText();
				boolean isOpCheckSelected = false;
				isOpCheckSelected = opCheckBox.isSelected();
				
				MCViosMasterOperation operation = new MCViosMasterOperation(viosPlatformId, unitNo, view, processor, commonName, isOpCheckSelected);
				String statusMsg = ViosMasterValidator.masterOperationValidate(operation);
				if(StringUtils.isNotBlank(statusMsg)) {
					setErrorMessage(statusMsg);
					return;
				}
				List<MCViosMasterOperationChecker> opCheckerList = new ArrayList<MCViosMasterOperationChecker>(checkerDetailsTablePane.getTable().getItems());
				operation.setUserId(getUserId());
				ServiceFactory.getService(ViosMaintenanceService.class).uploadViosMasterOperation(operation);
				List<MCViosMasterOperationChecker> opCheckerListWithUserId = new ArrayList<MCViosMasterOperationChecker>();
				
				for(MCViosMasterOperationChecker checker : opCheckerList){
					checker.setUserId(getUserId());
					opCheckerListWithUserId.add(checker);
					
				}
				
				ServiceFactory.getService(ViosMaintenanceService.class).deleteAndInsertAllMasterOperationChecker(opCheckerListWithUserId,operation);
				
				Stage stage = (Stage) updateButton.getScene().getWindow();
				stage.close();
			
		} catch (Exception e) {
			Logger.getLogger().error(e, new LogRecord("An exception occured while updating MCViosMasterOperation"));
			setErrorMessage("Something went wrong while updating Unit Number Config");
		}
	
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

	private void loadUpdateData() {
		if(selectedOp != null) {
			unitNoTextField.getControl().setText(selectedOp.getId().getUnitNo());
			unitNoTextField.setDisable(true);
			commonNameTextField.setText(selectedOp.getCommonName());
			viewTextField.setText(selectedOp.getView());
			processorTextField.setText(selectedOp.getProcessor());
			opCheckBox.setSelected(selectedOp.isOpCheckSelected());
			List<MCViosMasterOperationChecker> checkerList = ServiceFactory.getDao(MCViosMasterOperationCheckerDao.class).findAllBy(platform.getGeneratedId(), selectedOp.getId().getUnitNo());
			checkerDetailsTablePane.setData(checkerList);
		}
	}

	private LabeledControl<LoggedLabel> createLabeledControl(String labelName) {
		LabeledControl<LoggedLabel> label = new LabeledControl<LoggedLabel>(labelName, new LoggedLabel(labelName), true, false, new Insets(0), true, false);
		BorderPane.setMargin(label.getControl(), new Insets(0, 0, 0, 10));
		return label;
	}
	
	private ObjectTablePane<MCViosMasterOperationChecker> createCheckerDetailsTable() {

		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Check Point", "id.checkPoint")
				.put("Check Seq", "id.checkSeq")
				.put("Check Name","checkName")
				.put("Checker","checker")
				.put("Reaction Type","reactionTypeAsString");
		Double[] columnWidth = new Double[] {
				0.15, 0.10, 0.15, 0.40, 0.20
		}; 
		ObjectTablePane<MCViosMasterOperationChecker> subTable = new ObjectTablePane<MCViosMasterOperationChecker>(columnMappingList,columnWidth, true);
		subTable.setConstrainedResize(false);
		subTable.setPrefSize(400, 200);
		return subTable;
	}
	
	private void addContextMenuItems()
	{
		clearErrorMessage();
		if(checkerDetailsTablePane.getSelectedItem() != null){
			String[] menuItems;
			menuItems = new String[] {
					ViosConstants.EDIT
			};
			checkerDetailsTablePane.createContextMenu(menuItems, new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if(event.getSource() instanceof MenuItem) {
						MenuItem menuItem = (MenuItem) event.getSource();
						if(menuItem.getText().equals(ViosConstants.EDIT)) {
							editCheckerAction();
						} 
					}
				}
			});
		} else if(checkerDetailsTablePane.getTable().getContextMenu() != null) {
			checkerDetailsTablePane.getTable().getContextMenu().getItems().clear();
		}
	}
	
	private void editCheckerAction() {
		selectedOpChecker = checkerDetailsTablePane.getSelectedItem();
		checkPointCombobox.getControl().getSelectionModel().select(selectedOpChecker.getId().getCheckPoint());
		checkSeqTextField.setText(String.valueOf(selectedOpChecker.getId().getCheckSeq()));
		checkNameTextField.setText(selectedOpChecker.getCheckName());
		Checkers checker = Checkers.getCheckerByClassName(selectedOpChecker.getChecker());
		if(checker != null) {
			checkerCombobox.getControl().getSelectionModel().select(checker.toString());
		}
		reactionTypeCombobox.getControl().getSelectionModel().select(selectedOpChecker.getReactionTypeAsString());
		saveCheckerButton.toFront();
		removeCheckerButton.setDisable(true);
	}
	
}
