package com.honda.galc.client.teamlead.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dto.OperationCheckerDto;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.entity.conf.MCOperationCheckerId;
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
import javafx.stage.Stage;

public class OperationViewEditDialog extends FxDialog {

	private AutoCompleteTextField operationName;
	private LoggedTextField checkSequence;
	private LoggedComboBox<KeyValue<String, String>> checkerComboBox;
	private LoggedComboBox<String> reactionTypeComboBox;
	private LoggedComboBox<String> checkPointsComboBox;
	private LoggedText errMsgText;
	private LoggedRadioButton activeOpRev;
	private LoggedRadioButton allOpRev;
	private LoggedRadioButton customOpRev;
	private LoggedTextArea customOpRevText;
	private ToggleGroup radioToggleGroup;
	private LoggedButton updateBtn;
	private LoggedButton addBtn;
	private LoggedButton cancelBtn;
	private OperationCheckerDto opCheckerDto;
	private CheckerConfigModel model;
	private OperationCheckerView view;
	private ListView<Integer> customOpRevListView; 
	private String viewType;
	private ToggleButton toggleButton;
	
	public OperationViewEditDialog(String type, OperationCheckerDto opCheckerDto, CheckerConfigModel model, OperationCheckerView view) {
		super("", ClientMainFx.getInstance().getStage());
		this.opCheckerDto = opCheckerDto;
		this.model = model;
		this.view = view;
		this.getScene().getStylesheets().add(CheckerConstants.CSS_PATH);
		viewType = type;
		initComponents();
	}	

	private void initComponents() {
		((BorderPane) this.getScene().getRoot()).setCenter(createTitiledPane("Operation Checker", createViewEditContainer(),500, 300));
		initialize();
	}
	
	private void initialize() {
		handleButtonAction();
		loadCheckerComboBox();
		loadReactionTypeComboBox();
		loadCheckPointsComboBox();
		if(viewType.equals("Add")) {
			this.updateBtn.setVisible(false);
			customOpRevListView.setVisible(false);
			customOpRevText.setVisible(false);
			handleRadioAction();
		}else if(viewType.equals("Edit")) {
			this.allOpRev.setDisable(true);
			setTextFieldsValue();
			this.addBtn.setVisible(false);
			this.operationName.setEditable(false);
			handleRadioAction();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setTextFieldsValue() {
		this.operationName.setText(getTrimmedOperationName(opCheckerDto.getOperationName()));
		this.toggleButton.setSelected(!opCheckerDto.getOperationName().startsWith(Delimiter.UNDERSCORE));
		this.checkPointsComboBox.getSelectionModel().select(opCheckerDto.getCheckPoint());
		this.checkSequence.setText(String.valueOf(opCheckerDto.getCheckSeq()));
		this.reactionTypeComboBox.getSelectionModel().select(opCheckerDto.getReactionType());
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
	
	private void handleButtonAction(){
		updateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	updateOperationChecker();
            }
        });
		addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	addOperationChecker();
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
		        	customOpRevText.setVisible(false);
		        	allOpRev.setSelected(false);
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
		        	if(viewType.equals(CheckerConstants.EDIT)) {
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
	}
	
	private void viewCustomOperationRev(){
		Set<Integer> opRevSet = new TreeSet<Integer>();
		customOpRevListView.getItems().clear();
		if(StringUtils.isNotBlank(this.operationName.getText().toString())) {
			if(viewType.equals(CheckerConstants.EDIT)) {
				for (MCOperationChecker mcOpChecker : loadOperationRevision(new ArrayList<Integer>())) {
					opRevSet.add(mcOpChecker.getId().getOperationRevision());
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
	
	private List<MCOperationChecker> loadOperationRevision(List<Integer> opRevList) {
		return model.findAllOpCheckerOperationRevision(this.opCheckerDto.getOperationName(), this.opCheckerDto.getCheckPoint(), this.opCheckerDto.getCheckSeq(), opRevList);
	}
	
	private void selectOpRev() {
		int count = 0;
		ObservableList<Integer> opRevList = customOpRevListView.getItems();
		for (MCOperationChecker opChecker : loadOperationRevision(new ArrayList<Integer>())) {
			count = 0;
			for (Integer o : opRevList) {
				count++;
				if(opChecker.getId().getOperationRevision() == o) {
					customOpRevListView.getSelectionModel().select(count - 1);
					customOpRevListView.scrollTo(count - 1);
				}
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
		for(MCOperationChecker opChecker : loadOperationRevision(new ArrayList<Integer>())) {
			opRevList.add(opChecker.getId().getOperationRevision());
		}
		return opRevList;
	}
	
	private void updateOperationChecker() {
		try {
			LoggedButton actionBtn = this.updateBtn;
			if(!validateOperationName()) {
				this.errMsgText.setText("Please Enter Valid Operation Name");
				return;
			}
			if(!validateCheckSequence()) {
				this.errMsgText.setText("Please Enter Check Sequence");
				return;
			}
			if(validateCustomOpRevList()) {
				this.errMsgText.setText("Please Select Operation Revision");
				return;
			}
			if(isOpCheckerIdChanged()) {
				if(model.findOperationCheckerById(getOperationCheckerId()) != null) {
					this.errMsgText.setText(CheckerConstants.CHECKER_ALREADY_CONFIGURED_AT_GIVEN_POSITION);
					return;
				}
			}
			List<MCOperationChecker> updateOpCheckersList = new ArrayList<MCOperationChecker>();
			List<MCOperationRevision> revisions = model.findAllByOperationAndRevisions(this.operationName.getText().toString(), new ArrayList<Integer>(getOperationRevisionList()), activeOpRev.isSelected());
			for (MCOperationRevision revision : revisions) {
				MCOperationChecker mcOperationChecker = setOperationCheckerValue();
				mcOperationChecker.getId().setOperationRevision(revision.getId().getOperationRevision());
				updateOpCheckersList.add(mcOperationChecker);
			}
			model.updateOperationChecker(updateOpCheckersList, loadOperationRevision(getOperationRevisionList()));
			
			view.doSearch();
			Stage stage = (Stage) actionBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private boolean isOpCheckerIdChanged() {
		MCOperationCheckerId opCheckerId = getOperationCheckerId();
		return (!opCheckerId.getOperationName().equalsIgnoreCase(opCheckerDto.getOperationName())
				|| !opCheckerId.getCheckPoint().equalsIgnoreCase(opCheckerDto.getCheckPoint())
				|| opCheckerId.getOperationRevision() != opCheckerDto.getOperationRevision()
				|| opCheckerId.getCheckSeq() != opCheckerDto.getCheckSeq());
	}
	
	private void addOperationChecker() {
		try {
			LoggedButton actionBtn = this.addBtn;
			
			if(!validateOperationName()) {
				this.errMsgText.setText("Please Enter Operation Name");
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
			if(validateCustomOpRevList()) {
				this.errMsgText.setText("Please Select Operation Revision");
				return;
			}
			
			if(model.findOperationCheckerById(getOperationCheckerId()) != null) {
				this.errMsgText.setText(CheckerConstants.CHECKER_ALREADY_CONFIGURED_AT_GIVEN_POSITION);
				return;
			}
			
			List<MCOperationChecker> addOpCheckers = new ArrayList<MCOperationChecker>();
			MCOperationChecker mcOperationChecker = setOperationCheckerValue();
			for (Integer opRev : getOperationRevisionList()) {
				mcOperationChecker.getId().setOperationRevision(opRev);
				addOpCheckers.add(mcOperationChecker);
				model.addOperationChecker(mcOperationChecker);
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
	private MCOperationChecker setOperationCheckerValue() {
		MCOperationChecker opChecker = new MCOperationChecker();
		KeyValue<String, String> checker = (KeyValue<String, String>) checkerComboBox.getSelectionModel().getSelectedItem();
		String reactionType = reactionTypeComboBox.getSelectionModel().getSelectedItem().toString();
		opChecker.setId(getOperationCheckerId());
		opChecker.setChecker(checker.getKey().toString());
		opChecker.setCheckName(checker.getValue().toString());
		ReactionType rt = ReactionType.getReactionType(reactionType); 
		opChecker.setReactionType(rt);
		return opChecker;
	}
	
	private MCOperationCheckerId getOperationCheckerId() {
		MCOperationCheckerId id = new MCOperationCheckerId();
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
	
	private boolean validateCheckPoint() {
		return this.checkPointsComboBox.getSelectionModel().getSelectedItem() != null ? false : true;
	}
	
	private boolean validateChecker() {
		return this.checkerComboBox.getSelectionModel().getSelectedItem() != null ? false : true;
	}
	
	private boolean validateReactionType() {
		return this.reactionTypeComboBox.getSelectionModel().getSelectedItem() != null ? false : true;
	}
	
	private boolean validateCustomOpRevList() {
		if(this.customOpRev.isSelected() && this.customOpRevListView.getSelectionModel().getSelectedItems().size() < 1) {
			return true;
		} 
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private void loadCheckerComboBox() {
		this.checkerComboBox.getItems().clear();
		String checkerName = "";
		this.checkerComboBox.setPromptText(CheckerConstants.SELECT);
		List<Checkers> checkers = Checkers.getCheckers(CheckerType.Operation);
		Collections.sort(checkers, new Comparator<Checkers>() {
            @Override
            public int compare(Checkers o1, Checkers o2) {
                return o1.name().toString().compareTo(o2.name().toString());
            }
        });
		for (Checkers c : checkers) {
			this.checkerComboBox.getItems().add(getKeyValue(c.getCheckerClass().getName(), c.toString().replace("_", " ")));
			if(opCheckerDto != null && opCheckerDto.getChecker().trim().equals(c.getCheckerClass().getName().trim())){
				checkerName = c.toString().replace("_", " ");
			}
		}
		if(opCheckerDto != null) {
			this.checkerComboBox.getSelectionModel().select(getKeyValue(opCheckerDto.getChecker().trim(), checkerName));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadReactionTypeComboBox() {
		this.reactionTypeComboBox.getItems().clear();
		this.reactionTypeComboBox.setPromptText(CheckerConstants.SELECT);
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
	private void loadCheckPointsComboBox() {
		this.checkPointsComboBox.getItems().clear();
		this.checkPointsComboBox.setPromptText(CheckerConstants.SELECT);
		List<CheckPoints> checkPointsList = Arrays.asList(CheckPoints.values());
		Collections.sort(checkPointsList, new Comparator<CheckPoints>() {
            @Override
            public int compare(CheckPoints o1, CheckPoints o2) {
                return o1.name().toString().compareTo(o2.name().toString());
            }
        });
		this.checkPointsComboBox.getItems().addAll(checkPointsList);
	}
	
	private void autoCompleteSearch() {
		List<String> operations = model.findAllOperation(operationName.getText().toString().trim());
		operationName.setSuggestionList(operations);
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
		toggleContainer.setPadding(new Insets(0, 25, 0, 0));
		
		HBox opNameContainer = createHBoxPanel("Operation Name ", 25);
		opNameContainer.getChildren().addAll(createOperationTextField());

		HBox checkPointsComboBoxContainer = createHBoxPanel("Checker Point ", 40);
		checkPointsComboBox = createComboBox(checkPointsComboBox, "checkPointsComboBox");
		checkPointsComboBoxContainer.getChildren().add(checkPointsComboBox);
	
		HBox checkSeqContainer = createHBoxPanel("Check Sequence ", 27);
		checkSequence = createTextField(checkSequence, 330);
		checkSeqContainer.getChildren().addAll(checkSequence);
		
		HBox checkerComboBoxContainer = createHBoxPanel("Checker ", 75);
		checkerComboBoxContainer.getChildren().add(createCheckerComboBox());
		
		HBox reactionTypeComboBoxContainer = createHBoxPanel("Reaction Type ", 37);
		reactionTypeComboBox = createComboBox(reactionTypeComboBox, "reactionTypeComboBox");
		reactionTypeComboBoxContainer.getChildren().add(reactionTypeComboBox);
		
		HBox errContainer = new HBox();
		errMsgText = new LoggedText();
		errMsgText.setFont(Font.font ("Verdana", 16));
		errMsgText.setFill(Color.RED);
		errContainer.getChildren().add(errMsgText);
		errContainer.setAlignment(Pos.BASELINE_LEFT);
	
		outerPane.getChildren().addAll(toggleContainer, opNameContainer, checkPointsComboBoxContainer, checkSeqContainer, checkerComboBoxContainer, reactionTypeComboBoxContainer, createApplyFor(), createButton(), errContainer);
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
	
	private LoggedTextField createTextField(LoggedTextField textField, float width) {
		if(textField == null){
			textField = new LoggedTextField();
			textField.setMaxWidth(width);
			textField.setMinWidth(width);
		}
		return textField;
	}
	
	private LoggedComboBox<String> createComboBox(LoggedComboBox<String> comboBox, String id) {
		if(comboBox == null){
			comboBox = new LoggedComboBox<String>(id);
			comboBox.setMaxWidth(330);
			comboBox.setMinWidth(330);
			comboBox.setMinHeight(25.0);
			comboBox.setMaxHeight(25.0);
			comboBox.setPrefWidth(330);
			comboBox.setStyle("-fx-font-size: 11pt; -fx-font-family: arial;");
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
		customOpRevHBox.setSpacing(5);
		applyForVBox.getChildren().addAll(allOpRev, activeOpRev, customOpRevHBox);
		applyForVBox.setSpacing(5);
		applyForHBox.setSpacing(55);
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
	
	public AutoCompleteTextField createOperationTextField() {
		if(operationName == null){
			operationName = view.createAutoCompleteTextField("operationName", 175, "");
			operationName.setMaxWidth(330);
			operationName.setMinWidth(330);
		}
		return operationName;
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
	
	private LoggedComboBox<KeyValue<String, String>> createCheckerComboBox() {
		if(checkerComboBox == null){
			checkerComboBox = new LoggedComboBox<KeyValue<String, String>>("checkerComboBox");
			checkerComboBox.setMinHeight(25.0);
			checkerComboBox.setMaxHeight(25.0);
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
			viewBtnHBox.setPadding(new Insets(0,20,10,0));
		}else {
			viewBtnHBox.getChildren().addAll(updateBtn, cancelBtn);
			viewBtnHBox.setPadding(new Insets(0,20,10,0));
		}
		
		viewBtnHBox.setSpacing(5);
		HBox.setHgrow(viewBtnHBox, Priority.ALWAYS);
		viewBtnHBox.setAlignment(Pos.BOTTOM_CENTER);
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
