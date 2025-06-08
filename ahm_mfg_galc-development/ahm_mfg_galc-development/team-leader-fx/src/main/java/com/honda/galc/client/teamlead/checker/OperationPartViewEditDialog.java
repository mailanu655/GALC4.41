package com.honda.galc.client.teamlead.checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckerType;
import com.honda.galc.checkers.Checkers;
import com.honda.galc.checkers.ReactionType;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.MessageDialog;
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
import com.honda.galc.dto.PartCheckerDto;
import com.honda.galc.dto.PartDetailsDto;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.entity.conf.MCPartCheckerId;
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

public class OperationPartViewEditDialog extends FxDialog {

	private AutoCompleteTextField operationName;
	private LoggedTextField checkSequence;
	private AutoCompleteTextField searchPartNumber;
	private LoggedComboBox<String> partTypeComboBox;
	private LoggedComboBox<KeyValue<String, String>> checkerComboBox;
	private LoggedComboBox<String> reactionTypeComboBox;
	private LoggedComboBox<String> checkPointsComboBox;
	private LoggedTextArea customOpRevText;
	private LoggedText errMsgText;
	private LoggedRadioButton activeOpRev;
	private LoggedRadioButton allOpRev;
	private LoggedRadioButton customOpRev;
	private ToggleGroup radioToggleGroup;
	private LoggedButton searchPartBtn;
	private LoggedButton updateBtn;
	private LoggedButton addBtn;
	private LoggedButton cancelBtn;
	private PartCheckerDto opPartCheckerDto;
	private CheckerConfigModel model;
	private OperationPartCheckerView view;
	private ListView<Integer> customOpRevListView; 
	private ObjectTablePane<PartDetailsDto> partDetailsTablePane;
	private String viewType;
	private ToggleButton toggleButton;
	
	public OperationPartViewEditDialog(String type, PartCheckerDto opPartCheckerDto, CheckerConfigModel model, OperationPartCheckerView view) {
		super("", ClientMainFx.getInstance().getStage());
		this.opPartCheckerDto = opPartCheckerDto;
		this.model = model;
		this.view = view;
		this.getScene().getStylesheets().add(CheckerConstants.CSS_PATH);
		viewType = type;
		initComponents();
	}	

	private void initComponents() {
		((BorderPane) this.getScene().getRoot()).setCenter(createTitiledPane("Operation Part Checker", createViewEditContainer(),600, 500));
		initialise();
	}
	
	private void initialise() {
		handleButtonAction();
		loadComboBox();
		if(viewType.equals(CheckerConstants.ADD)) {
			this.updateBtn.setVisible(false);
			customOpRevListView.setVisible(false);
			customOpRevText.setVisible(false);
			loadPartTypeComboBox();
			handleRadioAction();
		}
		else if(viewType.equals(CheckerConstants.EDIT)) {
			this.allOpRev.setDisable(true);
			this.addBtn.setVisible(false);
			loadPartTypeComboBox();
			setTextFieldsValue();
			searchPartDetails();
			selectPartRow();
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
		this.operationName.setText(getTrimmedOperationName(opPartCheckerDto.getOperationName()));
		this.toggleButton.setSelected(!opPartCheckerDto.getOperationName().startsWith(Delimiter.UNDERSCORE));
		
		this.checkPointsComboBox.getSelectionModel().select(opPartCheckerDto.getCheckPoint());
		this.checkSequence.setText(String.valueOf(opPartCheckerDto.getCheckSeq()));
		this.reactionTypeComboBox.getSelectionModel().select(opPartCheckerDto.getReactionType());
		this.customOpRevText.setVisible(true);
		this.activeOpRev.setSelected(false);
		this.allOpRev.setSelected(false);
		this.customOpRev.setSelected(true);
		setActiveRadioLabel();
		viewCustomOperationRev();
		selectOpRev();
		if(customOpRevListView != null && customOpRevListView.getSelectionModel() != null)
			customOpRevText.setText(customOpRevListView.getSelectionModel().getSelectedItems().toString());
	}
	
	private void selectPartRow() {
		List<PartDetailsDto> detailsDtos = partDetailsTablePane.getTable().getItems();
		for (PartDetailsDto partDetailsDto : detailsDtos) {
			if(partDetailsDto.getPartNo().trim().equals(opPartCheckerDto.getPartNo().trim())) {
				partDetailsTablePane.getTable().getSelectionModel().select(partDetailsDto);
				partDetailsTablePane.getTable().scrollTo(partDetailsDto);
				break;
			}
		}
	}
	
	private void handleButtonAction(){
		updateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	updatePartChecker();
            }
        });
		addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	addPartChecker();
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
				autoCompleteOperationSearch();
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
		        	customOpRevText.setVisible(false);
		        	customOpRev.setSelected(false);
		        	setActiveRadioLabel();
		        } 
		    }
		});
		
		allOpRev.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
		    	if (isNowSelected) { 
		    		customOpRevListView.setVisible(false);
		    		customOpRevText.setVisible(false);
		        	activeOpRev.setSelected(false);
		        	customOpRev.setSelected(false);
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
		        			customOpRevText.setText(customOpRevListView.getSelectionModel().getSelectedItems().toString());
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
	
	private void viewCustomOperationRev(){
		Set<Integer> opRevSet = new TreeSet<Integer>();
		customOpRevListView.getItems().clear();
		if(StringUtils.isNotBlank(this.operationName.getText().toString())) {
			if(viewType.equals(CheckerConstants.EDIT)) {
				for (MCPartChecker mcPartChecker : loadPartChecker(new ArrayList<Integer>())) {
					opRevSet.add(mcPartChecker.getId().getOperationRevision());
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
			List<Integer> opRevList = new ArrayList<Integer>(getOpRevListFromPartChecker());
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
	
	private List<MCPartChecker> loadPartChecker(List<Integer> opRevList) {
		return model.loadAllPartCheckerOpRev(this.opPartCheckerDto.getOperationName(), this.opPartCheckerDto.getPartId(), this.opPartCheckerDto.getCheckPoint(), this.opPartCheckerDto.getCheckSeq(), opRevList);
	}
	
	private List<Integer> getOpRevListFromPartChecker() {
		List<Integer> opRevList = new ArrayList<Integer>();
		for(MCPartChecker partChecker : loadPartChecker(new ArrayList<Integer>())) {
			opRevList.add(partChecker.getId().getOperationRevision());
		}
		return opRevList;
	}
	
	private void selectOpRev() {
		int count = 0;
		ObservableList<Integer> opRevList = customOpRevListView.getItems();
		for (MCPartChecker mcPartChecker : loadPartChecker(new ArrayList<Integer>())) {
			count = 0;
			for (Integer o : opRevList) {
				count++;
				if(mcPartChecker.getId().getOperationRevision() == o) {
					customOpRevListView.getSelectionModel().select(count - 1);
					customOpRevListView.scrollTo(count - 1);
				}
			}
		}
	}
	
	private void updatePartChecker() {
		try {
			LoggedButton actionBtn = this.updateBtn;
			String opRevisions = "";
			if(!validateOperationName()) {
				this.errMsgText.setText("Please Enter Operation Name");
				return;
			}
			if(!validateCheckSequence()) {
				this.errMsgText.setText("Please Enter Check Sequence");
				return;
			}
			List<MCPartChecker> updatePartCheckersList = new ArrayList<MCPartChecker>();
			for (Integer opRev : getOperationRevisionList()) {
				opRevisions = opRevisions + opRev + ", ";
			}
			if(StringUtils.isEmpty(opRevisions)) {
				this.errMsgText.setText("No Operation Revision Found.");
				return;
			}
			List<PartDetailsDto> partDtos = partDetailsTablePane.getTable().getSelectionModel().getSelectedItems();
			for (PartDetailsDto p : partDtos) {
				List<PartDetailsDto> partDetailsDtos = model.loadOperationRevisionAndPartIdBy(this.operationName.getText().toString(), p.getPartNo(), p.getPartSectionCode(), 
						p.getPartItemNo(), p.getPartType(), opRevisions.replaceAll(", $", ""), activeOpRev.isSelected());
				for (PartDetailsDto partDetailsDto : partDetailsDtos) {
					MCPartChecker mcPartChecker = setOperationPartCheckerValue();
					mcPartChecker.getId().setOperationRevision(partDetailsDto.getOperationRevision());
					mcPartChecker.getId().setPartId(partDetailsDto.getPartId());
					updatePartCheckersList.add(mcPartChecker);
				}
			}
			if(updatePartCheckersList.size() > 0) {
				model.updatePartChecker(updatePartCheckersList, loadPartChecker(getOperationRevisionList()));
			}
			view.doSearch();
			Stage stage = (Stage) actionBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private void addPartChecker() {
		try {
			LoggedButton actionBtn = this.addBtn;
			String opRevisions = "";
			if(!validateOperationName()) {
				this.errMsgText.setText("Please Enter Valid Operation Name");
				return;
			}
			if(validatePartDetail()) {
				this.errMsgText.setText("Please Select Part");
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
				opRevisions = opRevisions + opRev + ", ";
			}
			if(StringUtils.isEmpty(opRevisions)) {
				this.errMsgText.setText("No Operation Revision Found.");
				return;
			}
			List<PartDetailsDto> partDtos = partDetailsTablePane.getTable().getSelectionModel().getSelectedItems();
			for (PartDetailsDto p : partDtos) {
				List<PartDetailsDto> partDetailsDtos = model.loadOperationRevisionAndPartIdBy(this.operationName.getText().toString(), p.getPartNo(), p.getPartSectionCode(), 
						p.getPartItemNo(), p.getPartType(), opRevisions.replaceAll(", $", ""), activeOpRev.isSelected());
				for (PartDetailsDto partDetailsDto : partDetailsDtos) {
					MCPartChecker mcPartChecker = setOperationPartCheckerValue();
					mcPartChecker.getId().setOperationRevision(partDetailsDto.getOperationRevision());
					mcPartChecker.getId().setPartId(partDetailsDto.getPartId());
					try {
						if(model.findPartCheckerById(mcPartChecker.getId()) == null) {
							model.addPartChecker(mcPartChecker);
						} else {
							view.getLogger().info("Ignored as the given Part Checker already exists in the system: " + mcPartChecker);
						}
					} catch (Exception e) {
						view.getLogger().error(new LogRecord("An exception occured while creating Part Checker"));
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
	private MCPartChecker setOperationPartCheckerValue() {
		MCPartChecker partChecker = new MCPartChecker();
		KeyValue<String, String> checker = (KeyValue<String, String>) checkerComboBox.getSelectionModel().getSelectedItem();
		String reactionType = reactionTypeComboBox.getSelectionModel().getSelectedItem().toString();
		partChecker.setId(getPartCheckerId());
		partChecker.setChecker(checker.getKey().toString());
		partChecker.setCheckName(checker.getValue().toString());
		ReactionType rt = ReactionType.getReactionType(reactionType); 
		partChecker.setReactionType(rt);
		return partChecker;
	}
	
	private MCPartCheckerId getPartCheckerId() {
		MCPartCheckerId id = new MCPartCheckerId();
		id.setOperationName(getOperationNameWithCheck());
		id.setCheckPoint(this.checkPointsComboBox.getSelectionModel().getSelectedItem().toString());
		id.setCheckSeq(Integer.valueOf(this.checkSequence.getText()));
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
	private boolean  validatePartDetail() {
		return partDetailsTablePane == null && partDetailsTablePane.getTable().getSelectionModel().getSelectedItems().size() < 1;
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
		this.checkerComboBox.setPromptText("Select");
		String checkerName = "";
		List<Checkers> checkers = Checkers.getCheckers(CheckerType.Part);
		for (Checkers c : checkers) {
			this.checkerComboBox.getItems().add(getKeyValue(c.getCheckerClass().getName(), c.toString().replace("_", " ")));
			if(opPartCheckerDto != null && opPartCheckerDto.getChecker().trim().equals(c.getCheckerClass().getName().trim())){
				checkerName = c.toString().replace("_", " ");
			}
		}
		if(opPartCheckerDto != null) {
			this.checkerComboBox.getSelectionModel().select(getKeyValue(opPartCheckerDto.getChecker().trim(), checkerName));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadReactionTypeComboBox() {
		this.reactionTypeComboBox.getItems().clear();
		this.reactionTypeComboBox.setPromptText("Select");
		for (ReactionType rt : ReactionType.values()) {
			reactionTypeComboBox.getItems().add(rt.name().toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadPartTypeComboBox() {
		partTypeComboBox.getItems().clear();
		partTypeComboBox.setPromptText("Select");
		partTypeComboBox.getItems().add("ALL");
		for (PartType rt : PartType.values()) {
			partTypeComboBox.getItems().add(rt.name().toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadCheckPointsComboBox() {
		this.checkPointsComboBox.getItems().clear();
		this.checkPointsComboBox.setPromptText("Select");
		for (CheckPoints rt : CheckPoints.values()) {
			checkPointsComboBox.getItems().add(rt.name().toString());
		}
	}
	
	private void searchPartDetails() {
		String partType = "";
		if(!validateOperationName()) {
			MessageDialog.showInfo(null, "Enter Operation Name");
			this.errMsgText.setText("Operation name is not valid");
			return;
		}
		partDetailsTablePane.getTable().getItems().clear();
		String basePartNo = searchPartNumber.getText();
		if(partTypeComboBox.getSelectionModel().getSelectedItem() != null) {
			partType = partTypeComboBox.getSelectionModel().getSelectedItem().toString();
		}
		
		List<PartDetailsDto> partDetailsList = model.loadPartByOperationPartAndPartType(operationName.getText(), basePartNo, partType);
		partDetailsTablePane.setData(partDetailsList);
	}
	
	private void autoCompleteOperationSearch() {
		if(operationName != null) {
			List<String> operations = model.findAllOperation(operationName.getText().toString().trim());
			operationName.setSuggestionList(operations);
		}
	}
	
	private void autoCompletePartNoSearch() {
		if(searchPartNumber != null) {
			List<String> partList = model.findAllParts(searchPartNumber.getText().toString().trim(), operationName.getText().toString().trim());
			searchPartNumber.setSuggestionList(partList);
		}
	}
	
	private void cancelBtnAction(ActionEvent event){
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private VBox createViewEditContainer(){
		VBox outerPane = new VBox();
		
		toggleButton = view.createToggleButton();
		HBox toggleContainer = new HBox(toggleButton);
		toggleContainer.setAlignment(Pos.CENTER_RIGHT);
		toggleContainer.setPadding(new Insets(0, 30, 0, 0));
		
		HBox opNameContainer = createHBoxPanel("Operation Name ", 30);
		operationName = createAutoCompleteTextField(operationName, 330);
		opNameContainer.getChildren().add(operationName);
		
		HBox checkPointComboBoxContainer = createHBoxPanel("Checker Point ", 40);
		checkPointsComboBox = createComboBox(checkPointsComboBox, "checkPointsComboBox");
		checkPointComboBoxContainer.getChildren().add(checkPointsComboBox);
		
		HBox checkSeqContainer = createHBoxPanel("Check Sequence ", 27);
		checkSequence = createTextField(checkSequence, 330);
		checkSeqContainer.getChildren().add(checkSequence);
		
		HBox checkerComboBoxContainer = createHBoxPanel("Checker ", 75);
		checkerComboBoxContainer.getChildren().addAll(createCheckerComboBox());
		
		HBox reactionTypeComboBoxContainer = createHBoxPanel("Reaction Type ", 40);
		reactionTypeComboBox = createComboBox(reactionTypeComboBox, "reactionTypeComboBox");
		reactionTypeComboBoxContainer.getChildren().add(reactionTypeComboBox);
		
		HBox errContainer = new HBox();
		errMsgText = new LoggedText();
		errMsgText.setFont(Font.font ("Verdana", 16));
		errMsgText.setFill(Color.RED);
		errContainer.getChildren().add(errMsgText);
		errContainer.setAlignment(Pos.BASELINE_LEFT);
		
		outerPane.getChildren().addAll(toggleContainer, opNameContainer, createPartSearch(), checkPointComboBoxContainer, checkSeqContainer, checkerComboBoxContainer, reactionTypeComboBoxContainer, createApplyFor(), createButton(), errContainer);
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
			comboBox.setMinHeight(25);
			comboBox.setMaxHeight(25);
			comboBox.setMinWidth(330.0);
			comboBox.setMaxWidth(330.0);
			comboBox.setPrefWidth(330);
			comboBox.setStyle("-fx-font-size: 10pt; -fx-font-family: arial;");
		}
		return comboBox;
	}
	
	private LoggedComboBox<String> createPartComboBox(LoggedComboBox<String> comboBox, String id) {
		if(comboBox == null){
			comboBox = new LoggedComboBox<String>(id);
			comboBox.setMinHeight(25);
			comboBox.setMaxHeight(25);
			comboBox.setPrefWidth(90);
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
		searchPartBtn.setStyle("-fx-font-size: 12px;");
		partTypeComboBox = createPartComboBox(partTypeComboBox, "partTypeComboBox");
	
		partSerachHBox.getChildren().addAll(searchPartLbl, searchPartNumber, partTypeLbl, partTypeComboBox, searchPartBtn);
		partSerachHBox.setPadding(new Insets(5));
		partSerachHBox.setSpacing(5);
		partSerachHBox.setAlignment(Pos.CENTER);
		VBox borderVbox = new VBox();
		HBox selectPartHbox = new HBox();
		Label selectPartLbl = new Label("Select Part ");
		selectPartLbl.setStyle("-fx-font-weight: bold ;");
		selectPartHbox.setSpacing(60);
		selectPartHbox.setPadding(new Insets(2));
		partDetailsTablePane = createPartDetailsTablePane();
		borderVbox.getChildren().addAll(partSerachHBox, partDetailsTablePane);
		borderVbox.setMinHeight(150);
		borderVbox.setMaxHeight(140);
		borderVbox.setMaxWidth(400);
		borderVbox.setMinWidth(400);
		
		selectPartHbox.getChildren().addAll(selectPartLbl, borderVbox);
		partSearchVbox.getChildren().addAll(selectPartHbox);
		borderVbox.setStyle(String.format("-fx-border-color: black;-fx-border-insets: 2;-fx-border-width: 1;-fx-border-style: dashed;-fx-font-size: %dpx;", (int)(0.008 * getScreenWidth())));
		return partSearchVbox;
	}
	
	public ObjectTablePane<PartDetailsDto> createPartDetailsTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Part No", "partNo")
		.put("Item No","partItemNo")
		.put("Sec Code", "partSectionCode")
		.put("Type", "partType");
		
		Double[] columnWidth = new Double[] {
				0.08, 0.06,0.07,0.07
			};
		ObjectTablePane<PartDetailsDto> panel = new ObjectTablePane<PartDetailsDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		return panel;
	}
	
	public AutoCompleteTextField createAutoCompleteTextField(AutoCompleteTextField textField, int width) {
		if(textField == null){
			textField = view.createAutoCompleteTextField("operationName", width, "");
			textField.setMaxWidth(width);
			textField.setMinWidth(width);
		}
		return textField;
	}
	
	public LoggedTextField createTextField(LoggedTextField textField, float width) {
		if(textField == null){
			textField = new LoggedTextField();
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
	
	public LoggedComboBox<KeyValue<String, String>> createCheckerComboBox() {
		if(checkerComboBox == null){
			checkerComboBox = new LoggedComboBox<KeyValue<String, String>>("checkerComboBox");
			checkerComboBox.setMinHeight(30.0);
			checkerComboBox.setMaxHeight(30.0);
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

	public AutoCompleteTextField getOperationName() {
		return operationName;
	}

	public void setOperationName(AutoCompleteTextField operationName) {
		this.operationName = operationName;
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
