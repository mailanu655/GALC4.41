package com.honda.galc.client.teamlead.checker;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckerType;
import com.honda.galc.checkers.Checkers;
import com.honda.galc.checkers.ReactionType;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.AutoCompleteTextField;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dto.ApplicationCheckerDto;
import com.honda.galc.entity.conf.MCAppChecker;
import com.honda.galc.entity.conf.MCAppCheckerId;
import com.honda.galc.util.KeyValue;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ApplicationViewEditDialog extends FxDialog {
	private AutoCompleteTextField applicationId;
	private LoggedTextField processPointName;
	private LoggedTextField checkSequence;
	private LoggedText errMsgText;
	private LoggedComboBox<KeyValue<String, String>> checkerComboBox;
	private LoggedComboBox<String> reactionTypeComboBox;
	private LoggedComboBox<String> checkPointsComboBox;
	private LoggedButton addBtn;
	private LoggedButton updateBtn;
	private LoggedButton cancelBtn;
	private MCAppChecker appChecker;
	private ApplicationCheckerDto appCheckerDto;
	private CheckerConfigModel model;
	private ApplicationCheckerView view;
	private ToggleButton toggleButton;
	
	public ApplicationViewEditDialog(String type, ApplicationCheckerDto appCheckerDto, CheckerConfigModel model, ApplicationCheckerView view) {
		super("", ClientMainFx.getInstance().getStage());
		this.model = model;
		this.view = view;
		this.appCheckerDto = appCheckerDto;
		initComponents(type);
	}

	private void initComponents(String type) {
		((BorderPane) this.getScene().getRoot()).setCenter(createTitiledPane("Application Checker", createViewEditContainer(type),500, 280));
		initialise(type);
	}
	
	@SuppressWarnings("unchecked")
	private void initialise(String type) {
		handleButtonAction();
		this.processPointName.setDisable(true);
		if(type.equals(CheckerConstants.ADD)) {
			this.updateBtn.setVisible(false);
			this.checkPointsComboBox.getSelectionModel().selectedItemProperty().addListener(checkPointComboBoxChangeListener);
		 } else if(type.equals(CheckerConstants.EDIT)) {
			this.appChecker = ConvertToEntity.convertToEntity(appCheckerDto);
			this.addBtn.setVisible(false);
			setTextFieldsValue();
			this.checkPointsComboBox.getSelectionModel().selectedItemProperty().removeListener(checkPointComboBoxChangeListener);
		}
		loadCheckerComboBox();
		loadReactionTypeComboBox();
		loadCheckPointsComboBox();
	}
	
	ChangeListener<String> checkPointComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			loadMaxSeqNo(new_val);
		} 
	};
	
	@SuppressWarnings("unchecked")
	private void setTextFieldsValue() {
		this.applicationId.setText(getTrimmedOperationName(appChecker.getId().getApplicationId()));
		this.toggleButton.setSelected(!appChecker.getId().getApplicationId().startsWith(Delimiter.UNDERSCORE));
		this.checkPointsComboBox.getSelectionModel().select(appChecker.getId().getCheckPoint());
		this.checkSequence.setText(String.valueOf(appChecker.getId().getCheckSeq()));
		this.reactionTypeComboBox.getSelectionModel().select(appChecker.getReactionType());
		this.processPointName.setText(appCheckerDto.getProcessPointName());
	}
	
	private void handleButtonAction(){
		updateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	updateApplicationChecker();
            }
        });
		addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	addAppChecker();
            }
        });
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	cancelBtnAction(actionEvent);
            }
        });
		applicationId.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				autoCompleteSearch();
			}
		});
		applicationId.textProperty().addListener(new ChangeListener<String>() { 
            @Override 
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) { 
                if (applicationId.getText().contains("-")) { 
                	String appId = applicationId.getText();
                	applicationId.settext(appId.substring(appId.lastIndexOf(" - ") + 3).trim());
                	processPointName.setText(appId.substring(0, appId.lastIndexOf(" - ")).trim());
                }  
            } 
        });
	}
	
	private void updateApplicationChecker() {
		try {
			if(!validateApplicationId()) {
				this.errMsgText.setText("Please Enter Valid Application Id");
				return;
			}
			if(validateCheckPoint()) {
				this.errMsgText.setText("Please Enter Check Point");
				return;
			}
			if(!validateCheckSequence()) {
				this.errMsgText.setText("Please Enter Valid Check Sequence");
				return;
			}
			if(validateChecker()) {
				this.errMsgText.setText("Please Select Checker");
				return;
			}
			if(validateReactionType()) {
				MessageDialog.showInfo(null, "Please Select Reaction Type");
				return;
			}
			
			if(isAppCheckerIdChanged()) {
				MCAppChecker appChecker = model.findAppCheckerById(getAppCheckerId());
				if(appChecker != null) {
					this.errMsgText.setText(CheckerConstants.CHECKER_ALREADY_CONFIGURED_AT_GIVEN_POSITION);
					return;
				}
			}
			model.updateApplicationChecker(appChecker, setApplicationCheckerValue());
			view.doSearch();
			Stage stage = (Stage) this.updateBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private boolean isAppCheckerIdChanged() {
		MCAppCheckerId appCheckerId = getAppCheckerId();
		return (!appCheckerId.getApplicationId().equalsIgnoreCase(appCheckerDto.getApplicationId())
				|| !appCheckerId.getCheckPoint().equalsIgnoreCase(appCheckerDto.getCheckPoint())
				|| appCheckerId.getCheckSeq() != appCheckerDto.getCheckSeq());
	}
	
	@SuppressWarnings("unchecked")
	private void loadCheckerComboBox() {
		this.checkerComboBox.getItems().clear();
		this.checkerComboBox.setPromptText(CheckerConstants.SELECT);
		String checkerName = "";
		List<Checkers> checkers = Checkers.getCheckers(CheckerType.Application);
		Collections.sort(checkers, new Comparator<Checkers>() {
            @Override
            public int compare(Checkers o1, Checkers o2) {
                return o1.name().toString().compareTo(o2.name().toString());
            }
        });
		for (Checkers c : checkers) {
			this.checkerComboBox.getItems().add(getKeyValue(c.getCheckerClass().getName(), c.toString().replace("_", " ")));
			if(appChecker != null && appChecker.getChecker().trim().equals(c.getCheckerClass().getName().trim())){
				checkerName = c.toString().replace("_", " ");
			}
		}
		if(appChecker != null) {
			this.checkerComboBox.getSelectionModel().select(getKeyValue(appChecker.getChecker().trim(), checkerName));
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
		for (CheckPoints checkPoints : checkPointsList) {
			this.checkPointsComboBox.getItems().add(checkPoints.toString());
		}
	}
	
	private void autoCompleteSearch() {
		view.loadApplicationIds();
		applicationId.setSuggestionList(view.getAutoCompleteList());
	}
	
	private void loadMaxSeqNo(String checkPoint) {
		if(StringUtils.isNotBlank(applicationId.getText().toString())) {
			int expectedCheckSeq = model.getMaxCheckSequence(applicationId.getText().toString(), checkPoint);
			this.checkSequence.setText(String.valueOf(expectedCheckSeq));
		}
	}
	
	private void addAppChecker() {
		try {
			if(!validateApplicationId()) {
				this.errMsgText.setText("Please Enter Valid Application Id");
				return;
			}
			if(validateCheckPoint()) {
				this.errMsgText.setText("Please Enter Check Point");
				return;
			}
			if(!validateCheckSequence()) {
				this.errMsgText.setText("Please Enter Valid Check Sequence");
				return;
			}
			if(validateChecker()) {
				this.errMsgText.setText("Please Select Checker");
				return;
			}
			if(validateReactionType()) {
				MessageDialog.showInfo(null, "Please Select Reaction Type");
				return;
			}
			if(model.findAppCheckerById(getAppCheckerId()) != null) {
				this.errMsgText.setText(CheckerConstants.CHECKER_ALREADY_CONFIGURED_AT_GIVEN_POSITION);
				return;
			}
			
			model.addAppChecker(setApplicationCheckerValue());
			view.doSearch();
			Stage stage = (Stage) addBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private MCAppCheckerId getAppCheckerId() {
		MCAppCheckerId id = new MCAppCheckerId();
		id.setApplicationId(getApplicationIdWithCheck());
		id.setCheckPoint(this.checkPointsComboBox.getSelectionModel().getSelectedItem().toString());
		id.setCheckSeq(Integer.valueOf(this.checkSequence.getText()));
		
		return id;
	}
	
	@SuppressWarnings("unchecked")
	private MCAppChecker setApplicationCheckerValue() {
		MCAppChecker appChecker = new MCAppChecker();
		KeyValue<String, String> checker = (KeyValue<String, String>) this.checkerComboBox.getSelectionModel().getSelectedItem();
		String reactionType = reactionTypeComboBox.getSelectionModel().getSelectedItem().toString();
		appChecker.setId(getAppCheckerId());
		appChecker.setChecker(checker.getKey().toString());
		appChecker.setCheckName(checker.getValue().toString());
		ReactionType rt = ReactionType.getReactionType(reactionType); 
		appChecker.setReactionType(rt);
		return appChecker;
	}
	
	private boolean validateApplicationId() {
		if(StringUtils.isBlank(this.applicationId.getText())){
			return false;
		} else {
			if(model.findAllProcessPointById(this.applicationId.getText()) == null)
				return false;
		}
		return true;
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
	
	private void cancelBtnAction(ActionEvent event){
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private VBox createViewEditContainer(String type){
		VBox outerPane = new VBox();
		
		toggleButton = view.createToggleButton();
		HBox toggleContainer = new HBox(toggleButton);
		toggleContainer.setAlignment(Pos.CENTER_RIGHT);
		toggleContainer.setPadding(new Insets(0, 35, 0, 0));
		
		HBox aplicationIdContainer = createHBoxPanel("Application Id ", 35);
		aplicationIdContainer.getChildren().add(createApplicationTextField());
		
		HBox processPointContainer = createHBoxPanel("Process Point ", 40);
		processPointName = createTextField(processPointName, 330);
		processPointContainer.getChildren().add(processPointName);
		
		HBox checkPointComboBoxContainer = createHBoxPanel("Check Point ", 50);
		checkPointsComboBox = createComboBox(checkPointsComboBox, "checkPointsComboBox");
		checkPointComboBoxContainer.getChildren().add(checkPointsComboBox);
		
		HBox checkSeqContainer = createHBoxPanel("Check Sequence ", 22);
		checkSequence = createTextField(checkSequence, 330);
		checkSeqContainer.getChildren().add(checkSequence);
		
		HBox checkerComboBoxContainer = createHBoxPanel("Checker ", 70);
		checkerComboBoxContainer.getChildren().add(createCheckerComboBox());
		
		HBox reactionTypeComboBoxContainer = createHBoxPanel("Reaction Type ", 32);
		reactionTypeComboBox = createComboBox(reactionTypeComboBox, "reactionTypeComboBox");
		reactionTypeComboBoxContainer.getChildren().add(reactionTypeComboBox);
		
		HBox errContainer = new HBox();
		errMsgText = new LoggedText();
		errMsgText.setFont(Font.font ("Verdana", 16));
		errMsgText.setFill(Color.RED);
		errContainer.getChildren().add(errMsgText);
		errContainer.setAlignment(Pos.BASELINE_LEFT);
		
		outerPane.getChildren().addAll(toggleContainer, aplicationIdContainer, processPointContainer, checkPointComboBoxContainer, checkSeqContainer, checkerComboBoxContainer, reactionTypeComboBoxContainer, createButton(type), errContainer);
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
			comboBox.setStyle("-fx-font-size: 11pt; -fx-font-family: arial;");
		}
		return comboBox;
	}
	
	private AutoCompleteTextField createApplicationTextField() {
		if(applicationId == null){
			applicationId = view.createAutoCompleteTextField("applicationId", 175, "");
			applicationId.setMaxWidth(330);
			applicationId.setMinWidth(330);
		}
		return applicationId;
	}
	
	private LoggedTextField createTextField(LoggedTextField textField, float width) {
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
			checkerComboBox.setMinHeight(25.0);
			checkerComboBox.setMaxHeight(25.0);
			checkerComboBox.setMinWidth(330.0);
			checkerComboBox.setMaxWidth(330.0);
			checkerComboBox.setPrefWidth(330);
			checkerComboBox.setStyle("-fx-font-size: 11pt; -fx-font-family: arial;");
		}
		return checkerComboBox;
	}
	
	private HBox createButton(String type) {
		HBox viewBtnHBox = new HBox();
		updateBtn = createBtn("Update");
		cancelBtn = createBtn("Cancel");
		addBtn = createBtn("Add");
		viewBtnHBox.setSpacing(5);
		if(type.equals("Add")) {
			viewBtnHBox.getChildren().addAll(addBtn, cancelBtn);
			viewBtnHBox.setPadding(new Insets(10,0,10,0));
		}else if(type.equals("View")) {
			viewBtnHBox.getChildren().addAll(cancelBtn);
			viewBtnHBox.setPadding(new Insets(10,0,10,0));
		} else {
			viewBtnHBox.getChildren().addAll(updateBtn, cancelBtn);
			viewBtnHBox.setPadding(new Insets(10,0,10,0));
		}
		HBox.setHgrow(viewBtnHBox, Priority.ALWAYS);
		viewBtnHBox.setAlignment(Pos.BOTTOM_CENTER);
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

	public AutoCompleteTextField getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(AutoCompleteTextField applicationId) {
		this.applicationId = applicationId;
	}
	
	private String getApplicationIdWithCheck() {
		String appId = "";
		if(StringUtils.isNotBlank(applicationId.getText()))
				appId = applicationId.getText().toString();
		
		if(!toggleButton.isSelected()) {
			appId = Delimiter.UNDERSCORE + appId;
		}
		return appId;
	}
	
	private String getTrimmedOperationName(String opName) {
		if(opName.startsWith(Delimiter.UNDERSCORE)) {
			return opName.substring(1);
		}
		return opName;
	}
}	
