package com.honda.galc.client.teamlead.vios.partconfig;

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
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledControl;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCViosMasterOperationDao;
import com.honda.galc.dao.conf.MCViosMasterOperationPartCheckerDao;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationPartCheckerId;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.enumtype.PartCheck;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
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
import javafx.scene.control.CheckBox;
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
public class ViosPartConfigDialog extends AbstractViosDialog {

	private static final String DEFAULT_MFG_MSG = "Note: In case of Default MFG Part, please leave Part No as blank.";
	private MCViosMasterPlatform platform;
	private MCViosMasterOperationPart selectedOpPart;

	private LabeledControl<LoggedLabel> plantLabel;
	private LabeledControl<LoggedLabel> deptLabel;
	private LabeledControl<LoggedLabel> modelYearLabel;
	private LabeledControl<LoggedLabel> prodRateLabel;
	private LabeledControl<LoggedLabel> lineNoLabel;
	private LabeledControl<LoggedLabel> vmcLabel;

	private LabeledComboBox<String> unitNoCombobox;
	private LabeledTextField partNoTextField;
	private LabeledTextField partMaskTextField;
	private LoggedButton uploadPartButton;
	private CheckBox partCheckBox;

	private ObjectTablePane<MCViosMasterOperationPartChecker> checkerDetailsTablePane;

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

	private MCViosMasterOperationPartChecker selectedOpPartChecker;

	private LoggedButton addButton;
	private LoggedButton updateButton;
	private LoggedButton cancelButton;
	


	public ViosPartConfigDialog(Stage stage, String action, MCViosMasterPlatform platform, MCViosMasterOperationPart selectedOpPart) {
		super(action + " Part Config", stage);
		this.platform = platform;
		this.selectedOpPart = selectedOpPart;
		loadPlatformData();
		loadUnitNoComboBox();
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

		VBox partDetailsBox = new VBox();
		partDetailsBox.setAlignment(Pos.CENTER);
		partDetailsBox.setSpacing(10);
		
		LoggedLabel messageLabel = UiFactory.createLabel("messsageLabel", DEFAULT_MFG_MSG);
		messageLabel.getStyleClass().add("display-msg-label");
		
		HBox messageBox = new HBox();
		messageBox.setAlignment(Pos.CENTER_LEFT);
		messageBox.setSpacing(10);
		
		messageBox.getChildren().add(messageLabel); 

		HBox partDetailsLev1Box = new HBox();
		partDetailsLev1Box.setAlignment(Pos.CENTER_LEFT);
		partDetailsLev1Box.setSpacing(10);

		unitNoCombobox = createLabeledComboBox("unitNoCombobox", "Unit No", true, true, false);
		unitNoCombobox.getControl().setEditable(true);
		BorderPane.setMargin(unitNoCombobox.getControl(), new Insets(0, 0, 0, 15));

		partNoTextField = new LabeledTextField("Part No", true, new Insets(0), true, false, false);
		BorderPane.setMargin(partNoTextField.getControl(), new Insets(0, 0, 0, 30));
		partNoTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(18));
		partNoTextField.getControl().setMinHeight(25);
		partNoTextField.getControl().setPrefWidth(300);
		
		partCheckBox = new CheckBox("Add Default Part Checker");
		partCheckBox.setStyle("-fx-font-weight: bold");
		partCheckBox.setSelected(true);

		partDetailsLev1Box.getChildren().addAll(messageBox,unitNoCombobox,  partNoTextField, partCheckBox);

		HBox partDetailsLev2Box = new HBox();
		partDetailsLev2Box.setAlignment(Pos.CENTER_LEFT);
		partDetailsLev2Box.setSpacing(10);

		partMaskTextField = new LabeledTextField("Part Mask", true, new Insets(0), true, false, false);
		uploadPartButton = new LoggedButton("Upload", "uploadPartButton");
		uploadPartButton.getStyleClass().add("small-btn");
		
		
		BorderPane.setMargin(partMaskTextField.getControl(), new Insets(0, 0, 0, 5));
		partMaskTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(255));
		partMaskTextField.getControl().setMinHeight(25);
		partMaskTextField.getControl().setPrefWidth(350);

		partDetailsLev2Box.getChildren().addAll(partMaskTextField, uploadPartButton);

		partDetailsBox.getChildren().addAll( partDetailsLev1Box, partDetailsLev2Box);

		TitledPane configPane = createTitiledPane("Part Config Details", partDetailsBox);

		checkerDetailsTablePane = createCheckerDetailsTable();
		checkerDetailsTablePane.getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MCViosMasterOperationPartChecker>() {
			@Override
			public void changed(ObservableValue<? extends MCViosMasterOperationPartChecker> observable,
					MCViosMasterOperationPartChecker oldValue, MCViosMasterOperationPartChecker newValue) {
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

		checkerDetailsBox.getChildren().addAll(checkPointCombobox, checkSeqTextField, checkNameTextField, 
				checkerCombobox, reactionTypeCombobox,  checkerButtonBox);

		VBox checkerBox = new VBox();
		checkerBox.setAlignment(Pos.CENTER);
		checkerBox.setSpacing(10);
		checkerBox.setPadding(new Insets(10));

		checkerBox.getChildren().addAll(checkerDetailsBox, checkerDetailsTablePane);

		TitledPane checkerTitledPane = createTitiledPane("Part Checker Details", checkerBox);

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
            		List<MCViosMasterOperationPartChecker> checkerList = ServiceFactory.getDao(MCViosMasterOperationPartCheckerDao.class)
            				.findAllBy(platform.getGeneratedId(), StringUtils.trimToEmpty(unitNoCombobox.getControl().getSelectionModel().getSelectedItem()), "");
            		checkerDetailsTablePane.setData(checkerList);
            	}
		    }
		});
		
		partNoTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
            	if(!oldValue.equals(newValue)) {
            		List<MCViosMasterOperationPartChecker> checkerList = ServiceFactory.getDao(MCViosMasterOperationPartCheckerDao.class)
            				.findAllBy(platform.getGeneratedId(), StringUtils.trimToEmpty(unitNoCombobox.getControl().getSelectionModel().getSelectedItem()), StringUtils.trimToEmpty(partNoTextField.getText()));
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
		List<Checkers> checkers = Checkers.getCheckers(CheckerType.Part);
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

		removeCheckerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				removeCheckerAction();
			}
		});
		
		uploadCheckerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				uploadCheckerAction();
			}
		});

		addButton.setOnAction(new EventHandler<ActionEvent>() {
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

		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateAction();
			}
		});

		uploadPartButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				uploadPartAction();
			}
		});
	}
	
	private void addCheckerAction() {
		MCViosMasterOperationPartChecker masterOpChecker = getViosMasterOpCheckerObj();
		String statusMsg = ViosMasterValidator.masterPartCheckerValidateForMFG(masterOpChecker);
		if(StringUtils.isNotBlank(statusMsg)) {
			setErrorMessage(statusMsg);
			return ;
		}
		if(checkDuplicateChecker(masterOpChecker)) {
			return;
		} 
		checkerDetailsTablePane.getTable().getItems().add(masterOpChecker);
		Collections.sort(checkerDetailsTablePane.getTable().getItems(), new Comparator<MCViosMasterOperationPartChecker>() {
			@Override
			public int compare(MCViosMasterOperationPartChecker o1, MCViosMasterOperationPartChecker o2) {
				return String.valueOf(o1.getId().getCheckSeq()).compareTo(String.valueOf(o2.getId().getCheckSeq()));
			}
		});
		clearCheckerFields();
	}

	private MCViosMasterOperationPartChecker getViosMasterOpCheckerObj() {
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
		String partNo = StringUtils.trimToEmpty(partNoTextField.getText());
		MCViosMasterOperationPartChecker masterOpPartChecker = new MCViosMasterOperationPartChecker(viosPlatformId, unitNo, 
				partNo, checkPoint, checkSeq, checkName, checker, reactionType);
		masterOpPartChecker.setUserId(getUserId());
		return masterOpPartChecker;
	}

	private void saveCheckerAction() {
		checkerDetailsTablePane.getTable().getItems().remove(selectedOpPartChecker);
		MCViosMasterOperationPartChecker opChecker = getViosMasterOpCheckerObj();

		String statusMsg = ViosMasterValidator.masterPartCheckerValidateForMFG(opChecker);
		if(StringUtils.isNotBlank(statusMsg)) {
			setErrorMessage(statusMsg);
			return ;
		}
		
		if(checkDuplicateChecker(opChecker)) {
			return;
		}
		checkerDetailsTablePane.getTable().getItems().add(opChecker);
		Collections.sort(checkerDetailsTablePane.getTable().getItems(), new Comparator<MCViosMasterOperationPartChecker>() {
			@Override
			public int compare(MCViosMasterOperationPartChecker o1, MCViosMasterOperationPartChecker o2) {
				return String.valueOf(o1.getId().getCheckSeq()).compareTo(String.valueOf(o2.getId().getCheckSeq()));
			}
		});
		removeCheckerButton.setDisable(false);
		addCheckerButton.toFront();
		clearCheckerFields();
	}

	private void removeCheckerAction() {
		MCViosMasterOperationPartChecker opPartChecker = checkerDetailsTablePane.getTable().getSelectionModel().getSelectedItem();
		if(opPartChecker == null) {
			setErrorMessage("Please select checker to remove");
			return;
		}
		checkerDetailsTablePane.getTable().getItems().remove(opPartChecker);
	}

	
	private void uploadCheckerAction() {
		IExcelUploader<MCViosMasterOperationPartChecker> excelUploader = new ViosPartCheckerExcelUploader(this.platform.getViosPlatformId(), this.getUserId());
		ExcelFileUploadDialog dialog = new ExcelFileUploadDialog(getStage(), MCViosMasterOperationPartChecker.class,
				excelUploader);
		dialog.showDialog();
	}
	
	private void uploadPartAction() {
		IExcelUploader<MCViosMasterOperationPart> excelUploader = new ViosExcelUploader<MCViosMasterOperationPart>(this.platform.getViosPlatformId(), this.getUserId());
		ExcelFileUploadDialog dialog = new ExcelFileUploadDialog(getStage(), MCViosMasterOperationPart.class,
				excelUploader);
		dialog.showDialog();
	}
	
	
	private boolean checkDuplicateChecker(MCViosMasterOperationPartChecker masterOpPartChecker) {
		List<MCViosMasterOperationPartChecker> checkerList = checkerDetailsTablePane.getTable().getItems();
		for(MCViosMasterOperationPartChecker checker : checkerList) {
			if(checker.getId().getCheckPoint().equals(masterOpPartChecker.getId().getCheckPoint())
					&& checker.getId().getCheckSeq() == masterOpPartChecker.getId().getCheckSeq()
					&& checker.getId().getUnitNo().equals(masterOpPartChecker.getId().getUnitNo())
					&& checker.getId().getViosPlatformId().equals(masterOpPartChecker.getId().getViosPlatformId())
					&& checker.getId().getPartNo().equals(masterOpPartChecker.getId().getPartNo())) {
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
				String unitNo = StringUtils.trimToEmpty(unitNoCombobox.getControl().getSelectionModel().getSelectedItem());
				String partNo = StringUtils.trimToEmpty(partNoTextField.getText());
				String partMask = StringUtils.trimToEmpty(partMaskTextField.getText());

				MCViosMasterOperationPart masterOpPart = new MCViosMasterOperationPart(viosPlatformId, unitNo, partNo, PartType.MFG, null, 
						null, null, null, null, partMask, null, 0, getPartCheck());
			
				
				 String statusMsg = ViosMasterValidator.masterPartValidate(masterOpPart);
				 if(StringUtils.isNotBlank(statusMsg)) {
					setErrorMessage(statusMsg);
					return ;
				 }
				
				List<MCViosMasterOperationPartChecker> masterOpPartCheckerList = new ArrayList<MCViosMasterOperationPartChecker>(checkerDetailsTablePane.getTable().getItems());
				masterOpPart.setUserId(getUserId());
				String propertyValuePartMask = PropertyService.getPropertyBean(MfgControlMaintenancePropertyBean.class).getPartMask();
				if(StringUtils.isEmpty(masterOpPart.getPartMask())) {
					masterOpPart.setPartMask(propertyValuePartMask);
				}
				ServiceFactory.getService(ViosMaintenanceService.class).uploadViosMasterPart(masterOpPart);
				List<MCViosMasterOperationPartChecker> masterOpPartCheckerListWithUserId = new ArrayList<MCViosMasterOperationPartChecker>();
				for( MCViosMasterOperationPartChecker checker : masterOpPartCheckerList) {
					checker.setUserId(getUserId());
					checker.setId(new MCViosMasterOperationPartCheckerId(viosPlatformId, unitNo, partNo, checker.getId().getCheckPoint(), checker.getId().getCheckSeq()));
					masterOpPartCheckerListWithUserId.add(checker);
				}
					ServiceFactory.getService(ViosMaintenanceService.class).deleteAndInsertAllMasterPartChecker(masterOpPartCheckerListWithUserId, masterOpPart);
			
				Stage stage = (Stage) updateButton.getScene().getWindow();
				stage.close();
			
		} catch (Exception e) {
			Logger.getLogger().error(e, new LogRecord("An exception occured while updating MCViosMasterOperationPart"));
			setErrorMessage("Something went wrong while updating Part Config");
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
		if(selectedOpPart != null) {
			String viosPlatformId = platform.getGeneratedId();
			unitNoCombobox.getControl().getSelectionModel().select(selectedOpPart.getId().getUnitNo());
			partNoTextField.setText(selectedOpPart.getId().getPartNo());
			partMaskTextField.setText(selectedOpPart.getPartMask());
			
			partCheckBox.setSelected(selectedOpPart.getPartCheck().equals(PartCheck.DEFAULT));
			List<MCViosMasterOperationPartChecker> checkerList = ServiceFactory.getDao(MCViosMasterOperationPartCheckerDao.class)
					.findAllBy(viosPlatformId, selectedOpPart.getId().getUnitNo(), selectedOpPart.getId().getPartNo());
			checkerDetailsTablePane.setData(checkerList);
			disableComponents();
		}
	}
	
	private void disableComponents() {
		unitNoCombobox.setDisable(true);
		partNoTextField.setDisable(true);
		//partSecCodeTextField.setDisable(true);
		//partItemNoTextField.setDisable(true);
	}

	private LabeledControl<LoggedLabel> createLabeledControl(String labelName) {
		LabeledControl<LoggedLabel> label = new LabeledControl<LoggedLabel>(labelName, new LoggedLabel(labelName), true, false, new Insets(0), true, false);
		BorderPane.setMargin(label.getControl(), new Insets(0, 0, 0, 10));
		return label;
	}

	private ObjectTablePane<MCViosMasterOperationPartChecker> createCheckerDetailsTable() {

		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Check Point", "id.checkPoint")
				.put("Check Seq", "id.checkSeq")
				.put("Check Name","checkName")
				.put("Checker","checker")
				.put("Reaction Type","reactionTypeAsString");
		Double[] columnWidth = new Double[] {
				0.15, 0.10, 0.15, 0.40, 0.20
		}; 
		ObjectTablePane<MCViosMasterOperationPartChecker> subTable = new ObjectTablePane<MCViosMasterOperationPartChecker>(columnMappingList,columnWidth, true);
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
		selectedOpPartChecker = checkerDetailsTablePane.getSelectedItem();
		checkPointCombobox.getControl().getSelectionModel().select(selectedOpPartChecker.getId().getCheckPoint());
		checkSeqTextField.setText(String.valueOf(selectedOpPartChecker.getId().getCheckSeq()));
		checkNameTextField.setText(selectedOpPartChecker.getCheckName());
		Checkers checker = Checkers.getCheckerByClassName(selectedOpPartChecker.getChecker());
		if(checker != null) {
			checkerCombobox.getControl().getSelectionModel().select(checker.toString());
		}
		reactionTypeCombobox.getControl().getSelectionModel().select(selectedOpPartChecker.getReactionTypeAsString());
		saveCheckerButton.toFront();
		removeCheckerButton.setDisable(true);
	}

	private PartCheck getPartCheck() {
		
		boolean isPartCheckSelected = false;
		isPartCheckSelected = partCheckBox.isSelected();
		
		if(isPartCheckSelected) {
			return PartCheck.DEFAULT;
		}
		return PartCheck.NONE;
	}
	
}
