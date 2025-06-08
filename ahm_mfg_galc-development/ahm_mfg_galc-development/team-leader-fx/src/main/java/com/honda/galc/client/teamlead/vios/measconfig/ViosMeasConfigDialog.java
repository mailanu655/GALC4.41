package com.honda.galc.client.teamlead.vios.measconfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang.ObjectUtils;
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
import com.honda.galc.client.teamlead.vios.ViosMeasCheckerExcelUploader;
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
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCViosMasterOperationDao;
import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementCheckerDao;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * <h3>ViosMeasConfigDialog Class description</h3>
 * <p>
 * Dialog for Vios Measurement Maintenance
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
public class ViosMeasConfigDialog extends AbstractViosDialog {

	//private static final String DEFAULT_MFG_MSG = "Note: In case of Default MFG Part, please leave Part No, Part Section Code and Part Item No Blank.";
	private MCViosMasterPlatform platform;
	private MCViosMasterOperationMeasurement selectedOpMeas;

	private LabeledControl<LoggedLabel> plantLabel;
	private LabeledControl<LoggedLabel> deptLabel;
	private LabeledControl<LoggedLabel> modelYearLabel;
	private LabeledControl<LoggedLabel> prodRateLabel;
	private LabeledControl<LoggedLabel> lineNoLabel;
	private LabeledControl<LoggedLabel> vmcLabel;

	private LabeledComboBox<String> unitNoCombobox;
	
	private LabeledTextField measSeqNumTextField;
	private LabeledTextField deviceIdTextField;
	private LabeledTextField deviceMsgTextField;
	private LabeledTextField maxLimitTextField;
	private LabeledTextField minLimitTextField;
	private LabeledTextField maxAttemptsTextField;
	
	private ObjectTablePane<MCViosMasterOperationMeasurementChecker> checkerDetailsTablePane;

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

	private MCViosMasterOperationMeasurementChecker selectedOpMeasChecker;

	private LoggedButton addButton;
	private LoggedButton updateButton;
	private LoggedButton cancelButton;
	private LoggedButton uploadMeasButton;
	

	public ViosMeasConfigDialog(Stage stage, String action, MCViosMasterPlatform platform, MCViosMasterOperationMeasurement selectedOpMeas) {
		super(action + " Measurement Config", stage);
		this.platform = platform;
		this.selectedOpMeas = selectedOpMeas;
		loadPlatformData();
		loadUnitNoComboBox();
		maxAttemptsTextField.setText(String.valueOf(3));
		if(action.equals(ViosConstants.ADD)) {
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

		VBox measDetailsBox = new VBox();
		measDetailsBox.setAlignment(Pos.CENTER);
		measDetailsBox.setSpacing(10);
		
		HBox messageBox = new HBox();
		messageBox.setAlignment(Pos.CENTER_LEFT);
		messageBox.setSpacing(10);
		
		HBox measDetailsLev1Box = new HBox();
		measDetailsLev1Box.setAlignment(Pos.CENTER_LEFT);
		measDetailsLev1Box.setSpacing(10);

		unitNoCombobox = createLabeledComboBox("unitNoCombobox", "Unit No", true, true, false);
		unitNoCombobox.getControl().setEditable(true);
		BorderPane.setMargin(unitNoCombobox.getControl(), new Insets(0, 0, 0, 10));

		measDetailsLev1Box.getChildren().addAll(unitNoCombobox);

		HBox partDetailsLev2Box = new HBox();
		partDetailsLev2Box.setAlignment(Pos.CENTER_LEFT);
		partDetailsLev2Box.setSpacing(10);

		measSeqNumTextField = new LabeledTextField("Meas Seq", true, new Insets(0), true, true, false);
		BorderPane.setMargin(measSeqNumTextField.getControl(), new Insets(0, 0, 0, 10));
		measSeqNumTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(10));
		measSeqNumTextField.getControl().setMinHeight(25);
		measSeqNumTextField.getControl().setPrefWidth(150);
		measSeqNumTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	measSeqNumTextField.getControl().setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
		
		deviceIdTextField = new LabeledTextField("Device ID", true, new Insets(0), true, false, false);
		BorderPane.setMargin(deviceIdTextField.getControl(), new Insets(0, 0, 0, 10));
		deviceIdTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(32));
		deviceIdTextField.getControl().setMinHeight(25);
		deviceIdTextField.getControl().setPrefWidth(300);
		
		deviceMsgTextField = new LabeledTextField("Device Msg", true, new Insets(0), true, false, false);
		BorderPane.setMargin(deviceMsgTextField.getControl(), new Insets(0, 0, 0, 10));
		deviceMsgTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(32));
		deviceMsgTextField.getControl().setMinHeight(25);
		deviceMsgTextField.getControl().setPrefWidth(300);

		partDetailsLev2Box.getChildren().addAll(measSeqNumTextField, deviceIdTextField, deviceMsgTextField);
		
		HBox partDetailsLev3Box = new HBox();
		partDetailsLev3Box.setAlignment(Pos.CENTER_LEFT);
		partDetailsLev3Box.setSpacing(10);
		
		maxLimitTextField = new LabeledTextField("Max Limit", true, new Insets(0), true, true, false);
		BorderPane.setMargin(maxLimitTextField.getControl(), new Insets(0, 0, 0, 10));
		maxLimitTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(15));
		maxLimitTextField.getControl().setMinHeight(25);
		maxLimitTextField.getControl().setPrefWidth(150);
		maxLimitTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	if (!newValue.matches("[-+]?\\d*(\\.\\d*)?")) {
		        	maxLimitTextField.getControl().setText(oldValue);
		        }
		    }
		});
		
		minLimitTextField = new LabeledTextField("Min Limit", true, new Insets(0), true, true, false);
		BorderPane.setMargin(minLimitTextField.getControl(), new Insets(0, 0, 0, 10));
		minLimitTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(15));
		minLimitTextField.getControl().setMinHeight(25);
		minLimitTextField.getControl().setPrefWidth(150);
		minLimitTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	if (!newValue.matches("[-+]?\\d*(\\.\\d*)?")) {
		        	minLimitTextField.getControl().setText(oldValue);
		        }
		    }
		});
		
		maxAttemptsTextField = new LabeledTextField("Max Attempts", true, new Insets(0), true, false, false);
		uploadMeasButton = new LoggedButton("Upload", "uploadMeasButton");
		uploadMeasButton.getStyleClass().add("small-btn");
		
		BorderPane.setMargin(maxAttemptsTextField.getControl(), new Insets(0, 0, 0, 10));
		maxAttemptsTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(10));
		maxAttemptsTextField.getControl().setMinHeight(25);
		maxAttemptsTextField.getControl().setPrefWidth(150);
		maxAttemptsTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	maxAttemptsTextField.getControl().setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
		
		partDetailsLev3Box.getChildren().addAll(minLimitTextField, maxLimitTextField, maxAttemptsTextField, uploadMeasButton);
		measDetailsBox.getChildren().addAll(messageBox, measDetailsLev1Box, partDetailsLev2Box, partDetailsLev3Box);
		TitledPane configPane = createTitiledPane("Measurement Config Details", measDetailsBox);

		checkerDetailsTablePane = createCheckerDetailsTable();
		checkerDetailsTablePane.getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MCViosMasterOperationMeasurementChecker>() {
			@Override
			public void changed(ObservableValue<? extends MCViosMasterOperationMeasurementChecker> observable,
					MCViosMasterOperationMeasurementChecker oldValue, MCViosMasterOperationMeasurementChecker newValue) {
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
		
		uploadCheckerButton = new LoggedButton("Upload", "uploadCheckerButton");
		uploadCheckerButton.getStyleClass().add("small-btn");

		checkerButtonBox.getChildren().addAll(checkerButtonPane, removeCheckerButton, uploadCheckerButton);

		checkerDetailsBox.getChildren().addAll(checkPointCombobox, checkSeqTextField, checkNameTextField, checkerCombobox, reactionTypeCombobox, checkerButtonBox);

		VBox checkerBox = new VBox();
		checkerBox.setAlignment(Pos.CENTER);
		checkerBox.setSpacing(10);
		checkerBox.setPadding(new Insets(10));

		checkerBox.getChildren().addAll(checkerDetailsBox, checkerDetailsTablePane);

		TitledPane checkerTitledPane = createTitiledPane("Measurement Checker Details", checkerBox);

		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(10);
		buttonBox.setPadding(new Insets(10));

		addButton = createBtn(ViosConstants.ADD);
		updateButton = createBtn(ViosConstants.UPDATE);
		cancelButton = createBtn(ViosConstants.CANCEL);
		
		unitNoCombobox.getControl().valueProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
            	if(!ObjectUtils.equals(oldValue,newValue)) {
        			List<MCViosMasterOperationMeasurementChecker> checkerList = ServiceFactory.getDao(MCViosMasterOperationMeasurementCheckerDao.class)
        					.findAllBy(platform.getGeneratedId(), StringUtils.trimToEmpty(unitNoCombobox.getControl().getSelectionModel().getSelectedItem()),-1);
        			checkerDetailsTablePane.setData(checkerList);
            	}
		    }
		});
		
		measSeqNumTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
            	if(!oldValue.equals(newValue)) {
            		int measurementSeqNum = trimNumber(measSeqNumTextField.getText(), Integer.class);
        			List<MCViosMasterOperationMeasurementChecker> checkerList = ServiceFactory.getDao(MCViosMasterOperationMeasurementCheckerDao.class)
        					.findAllBy(platform.getGeneratedId(), StringUtils.trimToEmpty(unitNoCombobox.getControl().getSelectionModel().getSelectedItem()),measurementSeqNum);
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

	private void loadUnitNoComboBox() {
		unitNoCombobox.getControl().setPromptText(ViosConstants.SELECT);
		if(platform != null) {
			ObservableList<String> unitNoList = FXCollections.observableArrayList(ServiceFactory.getDao(MCViosMasterOperationDao.class).findAllUnitNoBy(platform.getGeneratedId()));
			unitNoCombobox.getControl().getItems().clear();
			FilteredList<String> filteredItems = new FilteredList<String>(unitNoList, new Predicate<String>() {
				@Override
				public boolean test(String t) {
					return true;
				}
			});
			unitNoCombobox.getControl().getEditor().textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					final TextField editor = unitNoCombobox.getControl().getEditor();
					final String selected = unitNoCombobox.getControl().getSelectionModel().getSelectedItem();

					if (selected == null || !selected.equals(editor.getText())) {
						filteredItems.setPredicate(new Predicate<String>() {
							@Override
							public boolean test(String item) {
								if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
									return true;
								} else {
									return false;
								}
							}
						});
					}
				}
			});
			unitNoCombobox.getControl().setItems(filteredItems);
		}
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
		List<Checkers> checkers = Checkers.getCheckers(CheckerType.Measurement);
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
		
		uploadCheckerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				uploadCheckerAction();
			}
		});


		removeCheckerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				removeCheckerAction();
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
		
		uploadMeasButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				uploadMeasAction();
			}
		});
		
	}

	private void addCheckerAction() {
		MCViosMasterOperationMeasurementChecker masterOpChecker = getViosMasterOpMeasCheckerObj();
		String statusMsg = ViosMasterValidator.masterOperationMeasurementCheckerValidate(masterOpChecker);
		 if(StringUtils.isNotBlank(statusMsg)) {
			setErrorMessage(statusMsg);
			return ;
		}
		if(checkDuplicateChecker(masterOpChecker)) {
			return;
		}
		checkerDetailsTablePane.getTable().getItems().add(masterOpChecker);
		Collections.sort(checkerDetailsTablePane.getTable().getItems(), new Comparator<MCViosMasterOperationMeasurementChecker>() {
			@Override
			public int compare(MCViosMasterOperationMeasurementChecker o1, MCViosMasterOperationMeasurementChecker o2) {
				return String.valueOf(o1.getId().getCheckSeq()).compareTo(String.valueOf(o2.getId().getCheckSeq()));
			}
		});
		clearCheckerFields();
	}

	private MCViosMasterOperationMeasurementChecker getViosMasterOpMeasCheckerObj() {
		String checkPoint = StringUtils.trimToEmpty(checkPointCombobox.getControl().getSelectionModel().getSelectedItem());
		int checkSeq;
		try {
			checkSeq = Integer.parseInt(StringUtils.trimToEmpty(checkSeqTextField.getText()));
		} catch (NumberFormatException e) {
			checkSeq = 0;
		}
		String checkName = StringUtils.trimToEmpty(checkNameTextField.getText());
		//String checker = Checkers.getChecker(checkerCombobox.getControl().getSelectionModel().getSelectedItem()).getCheckerClass().getName();
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
		String unitNo = StringUtils.trimToEmpty(unitNoCombobox.getControl().getSelectionModel().getSelectedItem());
		
		
		int measurementSeqNum;
		try {
			measurementSeqNum = Integer.parseInt(StringUtils.trimToEmpty(measSeqNumTextField.getText()));
		} catch (NumberFormatException e) {
			measurementSeqNum = 0;
		}
		MCViosMasterOperationMeasurementChecker masterOpMeasChecker = new MCViosMasterOperationMeasurementChecker(viosPlatformId, unitNo, measurementSeqNum, checkPoint, checkSeq, checkName, checker, reactionType);
		masterOpMeasChecker.setUserId(getUserId());
		return masterOpMeasChecker;
	}

	private void saveCheckerAction() {
		checkerDetailsTablePane.getTable().getItems().remove(selectedOpMeasChecker);
		MCViosMasterOperationMeasurementChecker opMeasChecker = getViosMasterOpMeasCheckerObj();
		String statusMsg = ViosMasterValidator.masterOperationMeasurementCheckerValidate(opMeasChecker);
		if(StringUtils.isNotBlank(statusMsg)) {
			setErrorMessage(statusMsg);
			return ;
		}
		
		if(checkDuplicateChecker(opMeasChecker)) {
			return;
		}
		checkerDetailsTablePane.getTable().getItems().add(opMeasChecker);
		Collections.sort(checkerDetailsTablePane.getTable().getItems(), new Comparator<MCViosMasterOperationMeasurementChecker>() {
			@Override
			public int compare(MCViosMasterOperationMeasurementChecker o1, MCViosMasterOperationMeasurementChecker o2) {
				return String.valueOf(o1.getId().getCheckSeq()).compareTo(String.valueOf(o2.getId().getCheckSeq()));
			}
		});
		removeCheckerButton.setDisable(false);
		addCheckerButton.toFront();
		clearCheckerFields();
	}

	private void removeCheckerAction() {
		MCViosMasterOperationMeasurementChecker opMeasChecker = checkerDetailsTablePane.getTable().getSelectionModel().getSelectedItem();
		if(opMeasChecker == null) {
			setErrorMessage("Please select checker to remove");
			return;
		}
		checkerDetailsTablePane.getTable().getItems().remove(opMeasChecker);
	}

	private boolean checkDuplicateChecker(MCViosMasterOperationMeasurementChecker masterOpMeasChecker) {
		List<MCViosMasterOperationMeasurementChecker> checkerList = checkerDetailsTablePane.getTable().getItems();
		for(MCViosMasterOperationMeasurementChecker checker : checkerList) {
			if(checker.getId().getCheckPoint().equals(masterOpMeasChecker.getId().getCheckPoint())
					&& checker.getId().getCheckSeq() == masterOpMeasChecker.getId().getCheckSeq()
					&& checker.getId().getCheckName().equals(masterOpMeasChecker.getId().getCheckName())
					&& checker.getId().getUnitNo().equals(masterOpMeasChecker.getId().getUnitNo())
					&& checker.getId().getViosPlatformId().equals(masterOpMeasChecker.getId().getViosPlatformId())
					&& checker.getId().getMeasurementSeqNum() == masterOpMeasChecker.getId().getMeasurementSeqNum()) {
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

	
	
	private void uploadCheckerAction() {
		IExcelUploader<MCViosMasterOperationMeasurementChecker> excelUploader = new ViosMeasCheckerExcelUploader(this.platform.getViosPlatformId(), this.getUserId());
		ExcelFileUploadDialog dialog = new ExcelFileUploadDialog(getStage(), MCViosMasterOperationMeasurementChecker.class,
				excelUploader);
		dialog.showDialog();
	}
	
	private void uploadMeasAction() {
		IExcelUploader<MCViosMasterOperationMeasurement> excelUploader = new ViosExcelUploader<MCViosMasterOperationMeasurement>(this.platform.getViosPlatformId(), this.getUserId());
		ExcelFileUploadDialog dialog = new ExcelFileUploadDialog(null, MCViosMasterOperationMeasurement.class,
				excelUploader);
		dialog.showDialog();
	}
	private void updateAction() {
		try {
			
				String viosPlatformId = platform.getGeneratedId();
				String unitNo = StringUtils.trimToEmpty(unitNoCombobox.getControl().getSelectionModel().getSelectedItem());
				double minLimit;
				double maxLimit;
				int measurementSeqNum = trimNumber(measSeqNumTextField.getText(), Integer.class);
				
				if(!StringUtils.isEmpty(minLimitTextField.getText())) {
					 minLimit = trimNumber(minLimitTextField.getText(), Double.class);
				} else {
					setErrorMessage("Kindly enter required field(s): Min Limit\n");
					return ;
				}
				
				if(!StringUtils.isEmpty(maxLimitTextField.getText())) {
					maxLimit = trimNumber(maxLimitTextField.getText(), Double.class);
				} else {
					setErrorMessage("Kindly enter required field(s): Max Limit\n");
					return ;
				}
				
				int maxAttempts = trimNumber(maxAttemptsTextField.getText(), Integer.class);
				if(maxAttempts == 0) {
					maxAttempts = 3;
				}
				String deviceId = StringUtils.trimToEmpty(deviceIdTextField.getText());
				String deviceMsg = StringUtils.trimToEmpty(deviceMsgTextField.getText());
				MCViosMasterOperationMeasurement masterOpMeas = new MCViosMasterOperationMeasurement(viosPlatformId, unitNo, 
				PartType.MFG, measurementSeqNum, deviceId, deviceMsg, maxLimit, minLimit, 0, maxAttempts, null, 0, null, null);
				String statusMsg = ViosMasterValidator.masterOperationMeasValidate(masterOpMeas);
				if(StringUtils.isNotBlank(statusMsg)) {
					setErrorMessage(statusMsg);
					return ;
				}
				List<MCViosMasterOperationMeasurementChecker> masterOpMeansCheckerList = new ArrayList<MCViosMasterOperationMeasurementChecker>(checkerDetailsTablePane.getTable().getItems());
	/*			MCOperationPartRevision opPartRev = ServiceFactory.getDao(MCOperationPartRevisionDao.class)
						.findApprovedByPartNoAndType(masterOpMeas.getOperationName(), null,  masterOpMeas.getId().getPartType().name());
				if(opPartRev != null) {
					MCOperationMeasurement opMeas = ServiceFactory.getDao(MCOperationMeasurementDao.class).findByKey(
							new MCOperationMeasurementId(opPartRev.getId().getOperationName(), opPartRev.getId().getPartId(), opPartRev.getId().getPartRevision(), masterOpMeas.getId().getMeasurementSeqNum()));
				 if(opMeas != null) {
							 ServiceFactory.getDao(MCStructureDao.class).findByOpNameAndPart(opPartRev.getId().getOperationName(), opPartRev.getId().getPartId(), opPartRev.getId().getPartRevision());
				 }				
				}*/
				masterOpMeas.setUserId(getUserId());
				ServiceFactory.getService(ViosMaintenanceService.class).updateViosMasterMeas(masterOpMeas);
				List<MCViosMasterOperationMeasurementChecker> masterOpMeansCheckerListWithUserId = new ArrayList<MCViosMasterOperationMeasurementChecker>();
				for( MCViosMasterOperationMeasurementChecker checker : masterOpMeansCheckerList) {
					checker.setUserId(getUserId());
					masterOpMeansCheckerListWithUserId.add(checker);
					
				}	
				//if(!(addButtonClicked && masterOpMeansCheckerListWithUserId.size() == 0))
				ServiceFactory.getService(ViosMaintenanceService.class).deleteAndInsertAllMasterMeansChecker(masterOpMeansCheckerListWithUserId,masterOpMeas);
				
				Stage stage = (Stage) updateButton.getScene().getWindow();
				stage.close();
			
		} catch (Exception e) {
			Logger.getLogger().error(e, new LogRecord("An exception occured while updating MCViosMasterOperationMeasurement"));
			setErrorMessage("Something went wrong while updating Measurement Config");
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
		if(selectedOpMeas != null) {
			String viosPlatformId = platform.getGeneratedId();
			unitNoCombobox.getControl().getSelectionModel().select(selectedOpMeas.getId().getUnitNo());
			measSeqNumTextField.setText(String.valueOf(selectedOpMeas.getId().getMeasurementSeqNum()));
			deviceIdTextField.setText(selectedOpMeas.getDeviceId());
			deviceMsgTextField.setText(selectedOpMeas.getDeviceMsg());
			maxLimitTextField.setText(String.valueOf(selectedOpMeas.getMaxLimit()));
			minLimitTextField.setText(String.valueOf(selectedOpMeas.getMinLimit()));
			maxAttemptsTextField.setText(String.valueOf(selectedOpMeas.getMaxAttempts()));

			List<MCViosMasterOperationMeasurementChecker> checkerList = ServiceFactory.getDao(MCViosMasterOperationMeasurementCheckerDao.class)
					.findAllBy(viosPlatformId, selectedOpMeas.getId().getUnitNo(),selectedOpMeas.getId().getMeasurementSeqNum());
			checkerDetailsTablePane.setData(checkerList);
			disableComponents();
		}
	}
	
	private void disableComponents() {
		unitNoCombobox.setDisable(true);
		measSeqNumTextField.setDisable(true);
	}

	private LabeledControl<LoggedLabel> createLabeledControl(String labelName) {
		LabeledControl<LoggedLabel> label = new LabeledControl<LoggedLabel>(labelName, new LoggedLabel(labelName), true, false, new Insets(0), true, false);
		BorderPane.setMargin(label.getControl(), new Insets(0, 0, 0, 10));
		return label;
	}

	private ObjectTablePane<MCViosMasterOperationMeasurementChecker> createCheckerDetailsTable() {

		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Check Point", "id.checkPoint")
				.put("Check Seq", "id.checkSeq")
				.put("Check Name","id.checkName")
				.put("Checker","checker")
				.put("Reaction Type","reactionTypeAsString");
		Double[] columnWidth = new Double[] {
				0.15, 0.10, 0.15, 0.40, 0.20
		}; 
		ObjectTablePane<MCViosMasterOperationMeasurementChecker> subTable = new ObjectTablePane<MCViosMasterOperationMeasurementChecker>(columnMappingList,columnWidth, true);
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
		selectedOpMeasChecker = checkerDetailsTablePane.getSelectedItem();
		checkPointCombobox.getControl().getSelectionModel().select(selectedOpMeasChecker.getId().getCheckPoint());
		checkSeqTextField.setText(String.valueOf(selectedOpMeasChecker.getId().getCheckSeq()));
		checkNameTextField.setText(selectedOpMeasChecker.getId().getCheckName());
		Checkers checker = Checkers.getCheckerByClassName(selectedOpMeasChecker.getChecker());
		if(checker != null) {
			checkerCombobox.getControl().getSelectionModel().select(checker.toString());
		}
		reactionTypeCombobox.getControl().getSelectionModel().select(selectedOpMeasChecker.getReactionTypeAsString());
		saveCheckerButton.toFront();
		removeCheckerButton.setDisable(true);
	}

	@SuppressWarnings("unchecked")
	private <T extends Number>T trimNumber(String numStr, Class<T> clazz) {
		T var = null;
		try {
			if(clazz.equals(Integer.class)) {
				var = (T) new Integer(Integer.parseInt(numStr));
			} else if(clazz.equals(Double.class)) {
				var = (T) new Double(Double.parseDouble(numStr));
			}
		} catch (NumberFormatException e) {
			if(clazz.equals(Integer.class)) {
				var = (T) new Integer(0);
			} else if(clazz.equals(Double.class)) {
				var = (T) new Double(0.0);
			}
		}
		return var;
	}
	
	
}
