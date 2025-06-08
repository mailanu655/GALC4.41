package com.honda.galc.client.teamlead.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckerType;
import com.honda.galc.checkers.Checkers;
import com.honda.galc.checkers.ReactionType;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.AutoCompleteTextField;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.PartType;
import com.honda.galc.dto.MeasurementCheckerDto;
import com.honda.galc.dto.PartDetailsDto;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCMeasurementCheckerId;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.util.KeyValue;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MeasurementViewEditDialog extends FxDialog {
	private AutoCompleteTextField operationName;
	private LoggedTextField checkSequence;
	private LoggedText errMsgText;
	private AutoCompleteTextField searchPartNumber;
	private LoggedComboBox<String> partTypeComboBox;
	private LoggedComboBox<KeyValue<String, String>> checkerComboBox;
	private LoggedComboBox<String> reactionTypeComboBox;
	private LoggedComboBox<String> checkPointsComboBox;
	private LoggedRadioButton activeOpRev;
	private LoggedRadioButton allOpRev;
	private LoggedRadioButton customOpRev;
	private LoggedTextArea customOpRevText;
	private ToggleGroup radioToggleGroup;
	private LoggedButton searchPartBtn;
	private LoggedButton updateBtn;
	private LoggedButton addBtn;
	private LoggedButton cancelBtn;
	private MeasurementCheckerDto measurementCheckerDto;
	private CheckerConfigModel model;
	private MeasurementCheckerView view;
	private ListView<Integer> customOpRevListView; 
	private ObjectTablePane<PartDetailsDto> partDetailsTablePane;
	private String viewType;
	private ToggleButton toggleButton;
	
	public MeasurementViewEditDialog(String type, MeasurementCheckerDto measurementCheckerDto, CheckerConfigModel model, MeasurementCheckerView view) {
		super("", ClientMainFx.getInstance().getStage());
		this.measurementCheckerDto = measurementCheckerDto;
		this.model = model;
		this.view = view;
		this.getScene().getStylesheets().add(CheckerConstants.CSS_PATH);
		viewType = type;
		initComponents();
	}	

	private void initComponents() {
		((BorderPane) this.getScene().getRoot()).setCenter(createTitiledPane("Unit Part Measurement Checker", createViewEditContainer(),650, 500));
		initialise();
	}
	
	private void initialise() {
		handleButtonAction();
		loadComboBox();
		if(viewType.equals(CheckerConstants.ADD)) {
			customOpRevListView.setVisible(false);
			customOpRevText.setVisible(false);
			loadPartTypeComboBox();
			handleRadioAction();
		} else if(viewType.equals(CheckerConstants.EDIT)) {
			this.allOpRev.setDisable(true);
			loadPartTypeComboBox();
			setTextFieldsValue();
			searchPartDetails();
			selectPartDetail();
			handleRadioAction();
		}
	}
	
	private void loadComboBox(){
		loadCheckerComboBox();
		loadReactionTypeComboBox();
		loadCheckPointsComboBox();
	}
	
	@SuppressWarnings("unchecked")
	private void setTextFieldsValue() {
		this.operationName.setText(getTrimmedOperationName(measurementCheckerDto.getOperationName()));
		this.toggleButton.setSelected(!measurementCheckerDto.getOperationName().startsWith(Delimiter.UNDERSCORE));
		this.checkPointsComboBox.getSelectionModel().select(measurementCheckerDto.getCheckPoint());
		this.checkSequence.setText(String.valueOf(measurementCheckerDto.getCheckSeq()));
		this.reactionTypeComboBox.getSelectionModel().select(measurementCheckerDto.getReactionType());
		this.activeOpRev.setSelected(false);
		this.allOpRev.setSelected(false);
		this.customOpRev.setSelected(true);
		this.customOpRevText.setVisible(true);
		setActiveRadioLabel();
		viewCustomOperationRev();
		selectOpRev();
		if(customOpRevListView != null && customOpRevListView.getSelectionModel() != null)
			customOpRevText.setText(customOpRevListView.getSelectionModel().getSelectedItems().toString().replace("[", " ( ").replace("]", " ) "));
	}
	
	private void selectOpRev() {
		int count = 0;
		ObservableList<Integer> opRevList = customOpRevListView.getItems();
		for (MCMeasurementChecker meas : loadAllMeasCheckerBy(new ArrayList<Integer>())) {
			count = 0;
			for (Integer opRev : opRevList) {
				count++;
				if(meas.getId().getOperationRevision() == opRev) {
					customOpRevListView.getSelectionModel().select(count - 1);
					customOpRevListView.scrollTo(count - 1);
				}
			}
		}
	}
	
	private List<MCMeasurementChecker> loadAllMeasCheckerBy(List<Integer> opRevList) {
		return model.loadAllMeasCheckerBy(measurementCheckerDto, opRevList);
	}
	
	private void selectPartDetail() {
		List<PartDetailsDto> detailsDtos = partDetailsTablePane.getTable().getItems();
		for (PartDetailsDto partDetailsDto : detailsDtos) {
			if(partDetailsDto.getPartNo().trim().equals(measurementCheckerDto.getPartNo().trim())) {
				partDetailsTablePane.getTable().getSelectionModel().select(partDetailsDto);
				break;
			}
		}
	}
	
	private void handleButtonAction(){
		updateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	updateMeasurementChecker();
            }
        });
		addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	addMeasurementChecker();
            }
        });
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	cancelBtnAction(actionEvent);
            }
        });
		operationName.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				autoCompleteSearch();
			}
		});
	}
	
	private void handleRadioAction() {
		activeOpRev.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
		        if (isNowSelected) { 
		        	customOpRevListView.setVisible(false);
		        	allOpRev.setSelected(false);
		        	customOpRev.setSelected(false);
		        	customOpRevText.setVisible(false);
		        	setActiveRadioLabel();
		        } 
		    }
		});
		
		allOpRev.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
		    	if (isNowSelected) { 
		    		customOpRevListView.setVisible(false);
		        	activeOpRev.setSelected(false);
		        	customOpRev.setSelected(false);
		        	customOpRevText.setVisible(false);
		        } 
		    }
		});
		customOpRev.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
		        if (isNowSelected) { 
		        	customOpRevListView.setVisible(true);
		        	allOpRev.setSelected(false);
		        	activeOpRev.setSelected(false);
		        	viewCustomOperationRev();
		        	if(viewType.equals("Edit")) {
		        		selectOpRev();
		        		customOpRevText.setVisible(true);
		        		if(customOpRevListView != null && customOpRevListView.getSelectionModel() != null)
		        			customOpRevText.setText(customOpRevListView.getSelectionModel().getSelectedItems().toString().replace("[", " ( ").replace("]", " ) "));
		        	}
		        } else {
		        	customOpRevListView.setVisible(false);
		        	customOpRevText.setVisible(false);
		        }
		    }
		});
		searchPartBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				searchPartDetails();
			}
		});
		searchPartNumber.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				autoCompletePartNoSearch();
			}
		});
	}
	
	private void autoCompletePartNoSearch() {
		if(searchPartNumber != null && StringUtils.isNotBlank(operationName.getText().toString().trim())) {
			List<String> partList = model.findAllParts(searchPartNumber.getText().toString().trim(), operationName.getText().toString().trim());
			searchPartNumber.setSuggestionList(partList);
		}
	}
	
	private void viewCustomOperationRev(){
		Set<Integer> opRevSet = new TreeSet<Integer>();
		customOpRevListView.getItems().clear();
		if(StringUtils.isNotBlank(this.operationName.getText().toString())) {
			if(viewType.equals(CheckerConstants.EDIT)) {
				for (MCMeasurementChecker mcMeasChecker : loadAllMeasCheckerBy(new ArrayList<Integer>())) {
					opRevSet.add(mcMeasChecker.getId().getOperationRevision());
				}
				customOpRevListView.getItems().addAll(opRevSet);
			} else if(viewType.equals(CheckerConstants.ADD)){
				for (MCOperationRevision opRev : model.loadAllOperationRevisions(this.operationName.getText().toString())) {
					opRevSet.add(opRev.getId().getOperationRevision());
				}
				customOpRevListView.getItems().addAll(opRevSet);
			}
		}
	}
	
	private void setActiveRadioLabel() {
		if(this.operationName.getText().length() > 15){
			List<Integer> opRevList = new ArrayList<Integer>(getOpRevListFromOpChecker());
			boolean isContained = false;
			String opRevision = "";
			for (Integer opRev : model.loadActiveOperationRevision(this.operationName.getText())) {
				opRevision += opRev;
				if(!isContained) {
					isContained = opRevList.contains(opRev);
				}
			}
			activeOpRev.setText("Active Op Rev (" + opRevision + ")");
			/**
			 * Check if active operation revision is contained in the list of operation revisions
			 * If not, then disable active operation revision radio button 
			 */
			activeOpRev.setDisable(viewType.equals(CheckerConstants.EDIT) && !isContained);
		}
	}
	
	private List<Integer> getOpRevListFromOpChecker() {
		List<Integer> opRevList = new ArrayList<Integer>();
		for(MCMeasurementChecker measChecker : loadAllMeasCheckerBy(new ArrayList<Integer>())) {
			opRevList.add(measChecker.getId().getOperationRevision());
		}
		return opRevList;
	}
	
	private void updateMeasurementChecker() {
		try {
			LoggedButton actionBtn = this.updateBtn;
			List<MCMeasurementChecker> updateMeasCheckersList = new ArrayList<MCMeasurementChecker>();
			String opRevision = "";
			if(!validateOperationName()) {
				this.errMsgText.setText("Please Enter Valid Operation Name");
				return;
			}
			if(!validateCheckSequence()) {
				this.errMsgText.setText("Please Enter Check Sequence");
				return;
			}
			if(validateCheckPoint()) {
				this.errMsgText.setText("Please Enter Check Point");
				return;
			}
			if(validateChecker()) {
				this.errMsgText.setText("Please Select Checker");
				return;
			}
			if(validateReactionType()) {
				this.errMsgText.setText("Please Select Reaction Type");
				return;
			}
			
			for (Integer opRev : getOperationRevisionList()) {
				opRevision = opRevision + opRev + ", ";
			}
			if(StringUtils.isEmpty(opRevision)) {
				this.errMsgText.setText("No Operation Revision Found.");
				return;
			}
			
			List<PartDetailsDto> partDtos = partDetailsTablePane.getTable().getSelectionModel().getSelectedItems();
			for (PartDetailsDto p : partDtos) {
				List<PartDetailsDto> partDetailsDtos = model.loadOperationRevisionAndPartIdBy(this.operationName.getText().toString(), p.getPartNo(), p.getPartSectionCode(), 
						p.getPartItemNo(), p.getPartType(), opRevision.replaceAll(", $", ""), activeOpRev.isSelected());
				
				for (PartDetailsDto partDetailsDto : partDetailsDtos) {
					MCMeasurementChecker mcMeasChecker = setMeasurementCheckerValue(p);
					mcMeasChecker.getId().setOperationRevision(partDetailsDto.getOperationRevision());
					mcMeasChecker.getId().setPartId(partDetailsDto.getPartId());
					mcMeasChecker.getId().setMeasurementSeqNumber(p.getMeasurementSeqNum());
					updateMeasCheckersList.add(mcMeasChecker);
				}
			}
			model.updateMeasurementChecker(updateMeasCheckersList, loadAllMeasCheckerBy(getOperationRevisionList()));
			view.doSearch();
			Stage stage = (Stage) actionBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private void addMeasurementChecker() {
		try {
			LoggedButton actionBtn = this.addBtn;
			if(!validateOperationName()) {
				this.errMsgText.setText("Please Enter Valid Operation Name");
				return;
			}
			if(!validateCheckSequence()) {
				this.errMsgText.setText("Please Enter Check Sequence");
				return;
			}
			if(validateCheckPoint()) {
				this.errMsgText.setText("Please Enter Check Point");
				return;
			}
			if(validateChecker()) {
				this.errMsgText.setText("Please Select Checker");
				return;
			}
			if(validateReactionType()) {
				this.errMsgText.setText("Please Select Reaction Type");
				return;
			}
			
			String opRevision = "";
			for (Integer opRev : getOperationRevisionList()) {
				opRevision = opRevision + opRev + ", ";
			}
			if(StringUtils.isEmpty(opRevision)) {
				this.errMsgText.setText("No Operation Revision Found.");
				return;
			}
			List<PartDetailsDto> partDtos = partDetailsTablePane.getTable().getSelectionModel().getSelectedItems();
			for (PartDetailsDto p : partDtos) {
				MCMeasurementChecker mcMeasChecker = setMeasurementCheckerValue(p);
				List<PartDetailsDto> partDetailsDtos = model.loadOperationRevisionAndPartIdBy(this.operationName.getText().toString(), p.getPartNo(), p.getPartSectionCode(), 
						p.getPartItemNo(), p.getPartType(), opRevision.replaceAll(", $", ""), activeOpRev.isSelected());
				
				for (PartDetailsDto partDetailsDto : partDetailsDtos) {
					mcMeasChecker.getId().setOperationRevision(partDetailsDto.getOperationRevision());
					mcMeasChecker.getId().setPartId(partDetailsDto.getPartId());
					mcMeasChecker.getId().setMeasurementSeqNumber(p.getMeasurementSeqNum());
					try {
						if(model.findMeasurementCheckerById(mcMeasChecker.getId()) == null) {
							model.addMeasurementChecker(mcMeasChecker);
						} else {
							view.getLogger().info("Ignored as the given Measurement Checker already exists in the system: " + mcMeasChecker);
						}
					} catch (Exception e) {
						view.getLogger().error(new LogRecord("An exception occured while creating Measurement Checker"));
					}
				}
			}
			view.doSearch();
			Stage stage = (Stage) actionBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private List<Integer> getOperationRevisionList() {
		List<Integer> revList = new ArrayList<Integer>();
		if(activeOpRev != null && activeOpRev.isSelected()) {
			revList.addAll(model.loadActiveOperationRevision(this.operationName.getText()));
		} else if(allOpRev != null && allOpRev.isSelected()) {
			for (MCOperationRevision opRev : model.loadAllOperationRevisions(this.operationName.getText())) {
				revList.add(opRev.getId().getOperationRevision());
			}
		} else if (customOpRev != null && customOpRev.isSelected() && customOpRevListView.getSelectionModel().getSelectedItems().size() > 0) {
			revList.addAll(customOpRevListView.getSelectionModel().getSelectedItems());
		}
		return revList;
	}
	
	@SuppressWarnings("unchecked")
	private MCMeasurementChecker setMeasurementCheckerValue(PartDetailsDto partDto) {
		MCMeasurementChecker measChecker = new MCMeasurementChecker();
		KeyValue<String, String> checker = (KeyValue<String, String>) checkerComboBox.getSelectionModel().getSelectedItem();
		String reactionType = reactionTypeComboBox.getSelectionModel().getSelectedItem().toString();
		measChecker.setId(getMCMeasurementCheckerId(partDto));
		measChecker.setChecker(checker.getKey().toString());
		ReactionType rt = ReactionType.getReactionType(reactionType); 
		measChecker.setReactionType(rt);
		return measChecker;
	}
	
	@SuppressWarnings("unchecked")
	private MCMeasurementCheckerId getMCMeasurementCheckerId(PartDetailsDto partDto) {
		MCMeasurementCheckerId id = new MCMeasurementCheckerId();
		KeyValue<String, String> checker = (KeyValue<String, String>) checkerComboBox.getSelectionModel().getSelectedItem();
		id.setOperationName(getOperationNameWithCheck());
		id.setCheckPoint(this.checkPointsComboBox.getSelectionModel().getSelectedItem().toString());
		id.setCheckSeq(Integer.valueOf(this.checkSequence.getText()));
		id.setCheckName(checker.getValue().toString().replace(" ", "_"));
		id.setMeasurementSeqNumber(partDto.getMeasurementSeqNum());
		return id;
	}
	
	private boolean validateOperationName() {
		if(StringUtils.isBlank(this.operationName.getText())){
			return false;
		}
		List<MCOperationRevision> operations = model.findAllByOperationName(this.operationName.getText().trim());
		return operations.size() > 0;
	}
	
	private boolean validateCheckSequence() {
		String regex = "[0-9]+";
		if(StringUtils.isBlank(this.checkSequence.getText())){
			return false;
		}
		if (!this.checkSequence.getText().matches(regex)) {
			return false;
		}
		return true;
	}
	
	private boolean validateCheckPoint() {
		return this.checkPointsComboBox.getSelectionModel().getSelectedItem() != null ? false : true;
	}
	
	private boolean validateChecker() {
		return this.checkerComboBox.getSelectionModel().getSelectedItem() != null ? false : true;
	}
	
	private boolean validateReactionType() {
		return this.reactionTypeComboBox.getSelectionModel().getSelectedItem() != null ? false : true;
	}
	
	@SuppressWarnings("unchecked")
	private void loadCheckerComboBox() {
		this.checkerComboBox.getItems().clear();
		String checkerName = "";
		this.checkerComboBox.setPromptText("Select");
		
		List<Checkers> checkers = Checkers.getCheckers(CheckerType.Measurement);
		Collections.sort(checkers, new Comparator<Checkers>() {
            @Override
            public int compare(Checkers o1, Checkers o2) {
                return o1.name().toString().compareTo(o2.name().toString());
            }
        });
		for(Checkers c : checkers) {
			this.checkerComboBox.getItems().add(getKeyValue(c.getCheckerClass().getName(), c.toString().replace("_", " ")));
			if(measurementCheckerDto != null && measurementCheckerDto.getChecker().trim().equals(c.getCheckerClass().getName().trim())){
				checkerName = c.toString().replace("_", " ");
			}
		}
		if(measurementCheckerDto != null) {
			this.checkerComboBox.getSelectionModel().select(getKeyValue(measurementCheckerDto.getChecker().trim(), checkerName));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadReactionTypeComboBox() {
		this.reactionTypeComboBox.getItems().clear();
		this.reactionTypeComboBox.setPromptText("Select");
		List<ReactionType> reactionTypeList = Arrays.asList(ReactionType.values());
		Collections.sort(reactionTypeList, new Comparator<ReactionType>() {
            @Override
            public int compare(ReactionType o1, ReactionType o2) {
                return o1.name().toString().compareTo(o2.name().toString());
            }
        });
		this.reactionTypeComboBox.getItems().addAll(reactionTypeList);
	}
	
	@SuppressWarnings("unchecked")
	private void loadPartTypeComboBox() {
		partTypeComboBox.getItems().clear();
		partTypeComboBox.setPromptText("Select");
		partTypeComboBox.getItems().add("ALL");
		for (PartType rt : PartType.values()) {
			partTypeComboBox.getItems().add(rt.name().toString());
		}
		partTypeComboBox.getSelectionModel().select("MFG");
	}
	
	@SuppressWarnings("unchecked")
	private void loadCheckPointsComboBox() {
		this.checkPointsComboBox.getItems().clear();
		this.checkPointsComboBox.setPromptText("Select");
		List<CheckPoints> checkPointsList = Arrays.asList(CheckPoints.values());
		Collections.sort(checkPointsList, new Comparator<CheckPoints>() {
            @Override
            public int compare(CheckPoints o1, CheckPoints o2) {
                return o1.name().toString().compareTo(o2.name().toString());
            }
        });
		this.checkPointsComboBox.getItems().addAll(checkPointsList);
	}
	
	private void searchPartDetails() {
		String partType = "";
		Set<PartDetailsDto> partDetailSet = new LinkedHashSet<PartDetailsDto>();
		List<PartDetailsDto> partDetailsList = new ArrayList<PartDetailsDto>();
		if(!validateOperationName()) {
			this.errMsgText.setText("Operation name is not valid");
			return;
		}
		partDetailsTablePane.getTable().getItems().clear();
		String basePartNo = searchPartNumber.getText();
		if(partTypeComboBox.getSelectionModel().getSelectedItem() != null && 
				!partTypeComboBox.getSelectionModel().getSelectedItem().toString().equals("NONE")) {
			partType = partTypeComboBox.getSelectionModel().getSelectedItem().toString();
		}
		for (PartDetailsDto partDetailsDto : model.loadPartByOpPartTypeAndMeasurement(operationName.getText(), basePartNo, partType)) {
			if(partDetailSet.contains(partDetailsDto)) {
				if(partDetailsDto.getStatus().equals("ACTIVE")) {
					partDetailSet.remove(partDetailsDto);
					partDetailSet.add(partDetailsDto);
				}
			} else {
				partDetailSet.add(partDetailsDto);
			}
		}
		partDetailsList.addAll(partDetailSet);
		partDetailsTablePane.setData(partDetailsList);
	}
	
	private void autoCompleteSearch() {
		List<String> operations = model.findAllOperation(operationName.getText().toString().trim());
		operationName.setSuggestionList(operations);
	}
	
	private void cancelBtnAction(ActionEvent event){
		LoggedButton cancelBtn = this.cancelBtn;
		Stage stage = (Stage) cancelBtn.getScene().getWindow();
		stage.close();
	}
	
	private VBox createViewEditContainer(){
		VBox outerPane = new VBox();
		
		toggleButton = view.createToggleButton();
		HBox toggleContainer = new HBox(toggleButton);
		toggleContainer.setAlignment(Pos.CENTER_RIGHT);
		toggleContainer.setPadding(new Insets(0, 25, 0, 0));
		
		HBox opNameContainer = createHBoxPanel("Operation Name ", 25);
		operationName = createAutoCompleteTextField(operationName, 330);
		opNameContainer.getChildren().add(operationName);

		HBox checkPointComboBoxContainer = createHBoxPanel("Checker Point ", 40);
		checkPointsComboBox = createComboBox(checkPointsComboBox, "checkPointsComboBox");
		checkPointComboBoxContainer.getChildren().add(checkPointsComboBox);

		HBox checkSequenceContainer = createHBoxPanel("Check Sequence ", 27);
		checkSequence = createTextField(checkSequence, 330);
		checkSequenceContainer.getChildren().add(checkSequence);
		
		HBox checkerComboBoxContainer = createHBoxPanel("Checker ", 75);
		checkerComboBoxContainer.getChildren().add(createCheckerComboBox());
		
		HBox reactionTypeComboBoxContainer = createHBoxPanel("Reaction Type ", 40);
		reactionTypeComboBox = createComboBox(reactionTypeComboBox, "reactionTypeComboBox");
		reactionTypeComboBoxContainer.getChildren().add(reactionTypeComboBox);

		HBox errContainer = new HBox();
		errMsgText = new LoggedText();
		errMsgText.setFont(Font.font ("Verdana", 16));
		errMsgText.setFill(Color.RED);
		errContainer.getChildren().add(errMsgText);
		errContainer.setAlignment(Pos.BASELINE_LEFT);
		
		outerPane.getChildren().addAll(toggleContainer, opNameContainer, createPartSearch(), checkPointComboBoxContainer, checkSequenceContainer, checkerComboBoxContainer, reactionTypeComboBoxContainer, createApplyFor(), createButton(), errContainer);
		return outerPane;
	}
	
	private HBox createHBoxPanel(String label, int space) {
		HBox hbox = new HBox();
		Label lbl = new Label(label);
		lbl.setStyle("-fx-font-weight: bold ;");
		hbox.setSpacing(space);
		hbox.setPadding(new Insets(5));
		hbox.getChildren().add(lbl);
		return hbox;
	}
	
	private LoggedComboBox<String> createComboBox(LoggedComboBox<String> comboBox, String id) {
		if(comboBox == null){
			comboBox = new LoggedComboBox<String>(id);
			comboBox.setMaxWidth(330);
			comboBox.setMinWidth(330);
			comboBox.setMinHeight(25.0);
			comboBox.setMaxHeight(25.0);
			comboBox.setPrefWidth(330);
			comboBox.setStyle("-fx-font-size: 10pt; -fx-font-family: arial;");
		}
		return comboBox;
	}
	
	private HBox createApplyFor() {
		HBox applyForHBox = new HBox(); 
		radioToggleGroup = new ToggleGroup();
		Label applyForLbl = new Label("Apply For ");
		applyForLbl.setStyle("-fx-font-weight: bold ;");
		VBox applyForVBox = new VBox();
		allOpRev = createRadioButton("All Op Rev", radioToggleGroup, true);
		activeOpRev = createRadioButton("Active Op Rev", radioToggleGroup, false);
		HBox customOpRevHBox = new HBox(); 
		customOpRev = createRadioButton("Custom Op Rev", radioToggleGroup, false);
		customOpRevText = createCustomOpRevTextField();
		customOpRevHBox.getChildren().addAll(customOpRev, createCheckedComboBox(), customOpRevText);
		applyForVBox.getChildren().addAll(allOpRev, activeOpRev, customOpRevHBox);
		applyForVBox.setSpacing(5);
		applyForHBox.setSpacing(60);
		applyForHBox.setPadding(new Insets(5));
		applyForHBox.getChildren().addAll(applyForLbl, applyForVBox);
		return applyForHBox;
	}
	
	public LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected) {
		LoggedRadioButton radioButton = new LoggedRadioButton(title);
		radioButton.getStyleClass().add("radio-btn");
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		return radioButton;
	}
	
	private VBox createPartSearch() {
		VBox partSearchVbox = new VBox();
		HBox partSerachHBox = new HBox();
		Label searchPartLbl = new Label("Part No ");
		searchPartLbl.setStyle("-fx-font-weight: bold ;");
		searchPartNumber = createAutoCompleteTextField(searchPartNumber, 100);
		
		Label partTypeLbl = new Label("Part Type ");
		partTypeLbl.setPadding(new Insets(0,0,0,10));
		partTypeLbl.setStyle("-fx-font-weight: bold ;");
		searchPartBtn = createBtn("Search");
		
		partTypeComboBox = createComboBox(partTypeComboBox, "partTypeComboBox");
		partTypeComboBox.setMaxWidth(80);
		partTypeComboBox.setMinWidth(80);
		
		partSerachHBox.getChildren().addAll(searchPartLbl, searchPartNumber, partTypeLbl, partTypeComboBox, searchPartBtn);
		partSerachHBox.setPadding(new Insets(5));
		partSerachHBox.setSpacing(5);
		partSerachHBox.setAlignment(Pos.CENTER);
		VBox borderVbox = new VBox();
		HBox selectPartHbox = new HBox();
		Label selectPartLbl = new Label("Select Part ");
		selectPartLbl.setStyle("-fx-font-weight: bold ;");
		selectPartHbox.setSpacing(60);
		selectPartHbox.setPadding(new Insets(5));
		partDetailsTablePane = createPartDetailsTablePane();
		
		borderVbox.getChildren().addAll(partSerachHBox, partDetailsTablePane);
		borderVbox.setMinHeight(140);
		borderVbox.setMaxHeight(140);
		borderVbox.setMinWidth(480);
		borderVbox.setMaxWidth(480);
		selectPartHbox.getChildren().addAll(selectPartLbl, borderVbox);
		partSearchVbox.getChildren().addAll(selectPartHbox);
		borderVbox.setStyle(String.format("-fx-border-color: black;-fx-border-insets: 2;-fx-border-width: 1;-fx-border-style: dashed;-fx-font-size: %dpx;", (int)(0.008 * getScreenWidth())));
		
		return partSearchVbox;
	}
	
	public ObjectTablePane<PartDetailsDto> createPartDetailsTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Part No", "partNo")
		.put("Item No","partItemNo")
		.put("Sec Code", "partSectionCode")
		.put("Type", "partType")
		.put("Meas Seq", "measurementSeqNum")
		.put("Min", "minLimit")
		.put("Max", "maxLimit");
		
		Double[] columnWidth = new Double[] {
				0.06, 0.06,0.07,0.05,0.03,0.03,0.03,0.05
			};
		ObjectTablePane<PartDetailsDto> panel = new ObjectTablePane<PartDetailsDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		return panel;
	}
	
	public AutoCompleteTextField createAutoCompleteTextField(AutoCompleteTextField textField, int width) {
		if(textField == null){
			textField = view.createAutoCompleteTextField("", width, "");
			textField.setMaxWidth(width);
			textField.setMinWidth(width);
		}
		return textField;
	}
	
	private LoggedTextArea createCustomOpRevTextField() {
		if(customOpRevText == null) {
			customOpRevText = new LoggedTextArea();
			customOpRevText.setMinWidth(150);
			customOpRevText.setMaxWidth(150);
			customOpRevText.setEditable(false);
		}
		return customOpRevText;
	}
	
	public LoggedTextField createTextField(LoggedTextField textField, float width) {
		if(textField == null){
			textField = new LoggedTextField();
			textField.setMaxWidth(width);
			textField.setMinWidth(width);
		}
		return textField;
	}
	
	public LoggedComboBox<KeyValue<String, String>> createCheckerComboBox() {
		if(checkerComboBox == null){
			checkerComboBox = new LoggedComboBox<KeyValue<String, String>>("checkerComboBox");
			checkerComboBox.setMinHeight(25);
			checkerComboBox.setMaxHeight(25);
			checkerComboBox.setMinWidth(330.0);
			checkerComboBox.setMaxWidth(330.0);
			checkerComboBox.setPrefWidth(330);
			checkerComboBox.setStyle("-fx-font-size: 10pt; -fx-font-family: arial;");
		}
		return checkerComboBox;
	}
	
	private HBox createButton() {
		HBox viewBtnHBox = new HBox();
		updateBtn = createBtn("Update");
		addBtn = createBtn("Add");
		cancelBtn = createBtn("Cancel");
		if(viewType.equals("Add")) {
			viewBtnHBox.getChildren().addAll(addBtn, cancelBtn);
			viewBtnHBox.setPadding(new Insets(10,0,10,0));
		}else {
			viewBtnHBox.getChildren().addAll(updateBtn, cancelBtn);
			viewBtnHBox.setPadding(new Insets(10,0,10,0));
		}
		viewBtnHBox.setSpacing(5);
		HBox.setHgrow(viewBtnHBox, Priority.ALWAYS);
		viewBtnHBox.setAlignment(Pos.BASELINE_CENTER);
		return viewBtnHBox;
	}
	
	public LoggedButton createBtn(String text) {
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setStyle("-fx-pref-height: 5px; -fx-pref-width: 100px; -fx-font-size : 11pt;");
		btn.getStyleClass().add("table-button");
		return btn;
	}
	
	private TitledPane createTitiledPane(String title, Node content, double width, double height) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setFont(Font.font("", FontWeight.BOLD, 14));
		titledPane.setContent(content);
		titledPane.setPrefSize(width,height);
		titledPane.setCollapsible(false);
		return titledPane;
	}
	
	private ListView<Integer> createCheckedComboBox() {
		customOpRevListView = new ListView<Integer>();
		customOpRevListView.setMaxHeight(70);
		customOpRevListView.setMinHeight(70);
		customOpRevListView.setPrefWidth(60);
		customOpRevListView.setPrefHeight(70);
		customOpRevListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		return customOpRevListView;
	}
	
	private KeyValue<String, String> getKeyValue(String key, final String value) {
		KeyValue<String, String> kv = new KeyValue<String, String>(key, value) {
			private static final long serialVersionUID = 1L;
			@Override
			public String toString() {
				return value;
			}
		};
		return kv;
	}
	
	private double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}
	
	private String getOperationNameWithCheck() {
		String opName = this.operationName.getText();
		if(!toggleButton.isSelected()) {
			opName = Delimiter.UNDERSCORE + opName;
		}
		return opName;
	}
	
	private String getTrimmedOperationName(String opName) {
		if(opName.startsWith(Delimiter.UNDERSCORE)) {
			return opName.substring(1);
		}
		return opName;
	}
}
