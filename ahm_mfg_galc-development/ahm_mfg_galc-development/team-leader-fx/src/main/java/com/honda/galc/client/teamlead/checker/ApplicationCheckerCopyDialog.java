package com.honda.galc.client.teamlead.checker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.AutoCompleteTextField;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dto.ApplicationCheckerDto;
import com.honda.galc.entity.conf.MCAppChecker;

public class ApplicationCheckerCopyDialog extends FxDialog{

	private AutoCompleteTextField applicationSearchTextField;
	private AutoCompleteTextField copyApplicationTextField;
	private LoggedText processPointNameCopyText;
	private LoggedText processPointNameText;
	private LoggedText errMsgText;
	private ObjectTablePane<ApplicationCheckerDto> applicationDetailsTablePane;
	private LoggedButton searchBtn;
	private LoggedButton copyBtn;
	private LoggedButton cancelBtn;
	private CheckerConfigModel model;
	private ApplicationCheckerView view;
	private CheckBox selectAllCheckBox;
	private int totalRowCount = 0;
	private int selectedRowCount = 0;
	
	public ApplicationCheckerCopyDialog(String title, CheckerConfigModel model, ApplicationCheckerView view) {
		super("", ClientMainFx.getInstance().getStage());
		this.model = model;
		this.view = view;
		initComponents(title);
		handleSearchAction();
		setTableData();
	}

	private void initComponents(String type) {
		((BorderPane) this.getScene().getRoot()).setCenter(createTitiledPane("Copy Application Checker Form", createViewEditContainer(),850, 500));
	}
	
	private VBox createViewEditContainer(){
		VBox outerPane = new VBox();
		outerPane.setPrefWidth(850);
		outerPane.setPrefHeight(500);
		
		HBox applicationHBox = new HBox();
		Label searchLbl = new Label("Application Id ");
		searchLbl.setPadding(new Insets(5,2,5,5));
		searchLbl.setStyle("-fx-font-weight: bold ;");
		applicationSearchTextField = view.createAutoCompleteTextField("applicationSearchTextField", 175, "");
		applicationHBox.getChildren().addAll(searchLbl, applicationSearchTextField, createSearchBtn());
		
		HBox procesPointHBox = new HBox();
		Label procesPointLbl = new Label("Process Point ");
		procesPointLbl.setPadding(new Insets(5,2,5,5));
		procesPointLbl.setStyle("-fx-font-weight: bold ;");
		processPointNameText = new LoggedText();
		procesPointHBox.setPadding(new Insets(0,0,10,0));
		procesPointHBox.getChildren().addAll(procesPointLbl, processPointNameText);
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(applicationHBox, procesPointHBox);
		
		applicationDetailsTablePane = createApplicationDetailsTablePane();
		outerPane.getChildren().addAll(vBox, applicationDetailsTablePane, createCopyApplicationId());
				
		return outerPane;
	}
	
	private void handleSearchAction(){
		applicationSearchTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				autoCompleteSearch();
			}
		});	
		copyApplicationTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				copyAutoCompleteSearch();
			}
		});	
		applicationSearchTextField.textProperty().addListener(new ChangeListener<String>() { 
            @Override 
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) { 
                if (applicationSearchTextField.getText().contains("-")) { 
                	String appId = applicationSearchTextField.getText();
                	applicationSearchTextField.settext(appId.substring(appId.lastIndexOf(" - ") + 3).trim());
                	processPointNameText.setText(appId.substring(0, appId.lastIndexOf(" - ")).trim());
                	
                }  
            } 
        });
		copyApplicationTextField.textProperty().addListener(new ChangeListener<String>() { 
            @Override 
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) { 
                if (copyApplicationTextField.getText().contains("-")) { 
                	String appId = copyApplicationTextField.getText();
                	copyApplicationTextField.settext(appId.substring(appId.lastIndexOf(" - ") + 3).trim());
                	processPointNameCopyText.setText(appId.substring(0, appId.lastIndexOf(" - ")).trim());
                }  
            } 
        });
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	cancelBtnAction(actionEvent);
            }
        });
		copyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	copyBtnAction(actionEvent);
            }
        });
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	searchApplicationChecker();
            }
        });
	}
	
	public void searchApplicationChecker() {
		String applicationId = "";
		if(StringUtils.isNotBlank(applicationSearchTextField.getText()))
			applicationId = applicationSearchTextField.getText().toString();
		applicationDetailsTablePane.getTable().getItems().clear();
		if(selectAllCheckBox.isSelected())
			selectAllCheckBox.setSelected(false);
		applicationDetailsTablePane.setData(model.loadAppliationChecker(applicationId, StringUtils.EMPTY, StringUtils.EMPTY));
		totalRowCount = applicationDetailsTablePane.getTable().getItems().size();
		selectAllCheckBox.setSelected(false);
	}
	
	private void setTableData() {
		List<ApplicationCheckerDto> appCheckerList = view.getCheckerDetailTablePane().getTable().getSelectionModel().getSelectedItems();
		if(appCheckerList != null) {
			applicationDetailsTablePane.setData(appCheckerList);
			totalRowCount = applicationDetailsTablePane.getTable().getItems().size();
		}
	}
	
	private void autoCompleteSearch() {
		Set<String> appIds = new HashSet<String>();
		for (MCAppChecker appChecker : model.loadAllAppliationChecker()) {
			appIds.add(appChecker.getId().getApplicationId().trim());
		}
		applicationSearchTextField.setSuggestionList(new ArrayList<String>(appIds));
	}
	
	private void copyAutoCompleteSearch() {
		view.loadApplicationIds();
		copyApplicationTextField.setSuggestionList(view.getAutoCompleteList());
	}
	
	private void copyBtnAction(ActionEvent event){
		LoggedButton cancelBtn = getCopyBtn();
		try {
			if(!validateCopyApplicationId()) {
				this.errMsgText.setText("Please Enter Valid Application Id");
				return;
			}
			if(validateFromAndToApplicationId()){
				this.errMsgText.setText("Duplicate Operation cannot be performed on the same Application Id");
				return;
			}
			for (ApplicationCheckerDto appCheckerDto : applicationDetailsTablePane.getTable().getItems()) {
    			if(appCheckerDto.isSelected()){
        			MCAppChecker appChecker = ConvertToEntity.convertToEntity(appCheckerDto);
        			appChecker.getId().setApplicationId(copyApplicationTextField.getText().toString());
            		appChecker.getId().setCheckSeq(model.getMaxCheckSequence(copyApplicationTextField.getText().toString(), appCheckerDto.getCheckPoint()));
            		model.saveCopyApplicationChecker(appChecker);
    			}
			}
    		view.doSearch();
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private boolean validateCopyApplicationId() {
		if(StringUtils.isBlank(this.copyApplicationTextField.getText()) || 
				model.findAllProcessPointById(this.copyApplicationTextField.getText()) == null){
			return false;
		} 
		return true;
	}
	
	private boolean validateFromAndToApplicationId() {
		ObservableList<ApplicationCheckerDto> applicationDetails = applicationDetailsTablePane.getTable().getItems();
		for(ApplicationCheckerDto applicationCheckerDto:applicationDetails){
			if(applicationCheckerDto.isSelected() && applicationCheckerDto.getApplicationId().equalsIgnoreCase(copyApplicationTextField.getText())) {
				return true;
			}	
		}
		return false;
	}
	
	
	
	private void cancelBtnAction(ActionEvent event){
		try {
			LoggedButton cancelBtn = getCancelBtn();
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			for (ApplicationCheckerDto checkerDto : getApplicationDetailsTablePane().getTable().getItems()) {
				checkerDto.setSelected(false);
			}
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private ObjectTablePane<ApplicationCheckerDto> createApplicationDetailsTablePane() { 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Application Id", "applicationId")
			.put("Process Pt","processPointName")
			.put("Check Point","checkPoint")
			.put("Check Seq", "checkSeq")
			.put("Checker", "checker")
			.put("Reaction Type", "reactionType");
		
		Double[] columnWidth = new Double[] {
				0.10,0.08,0.12,0.05,0.19,0.16,0.10
			};
		ObjectTablePane<ApplicationCheckerDto> panel = new ObjectTablePane<ApplicationCheckerDto>(columnMappingList,columnWidth);
		LoggedTableColumn<ApplicationCheckerDto,Boolean> defaultCheckBoxCol = new LoggedTableColumn<ApplicationCheckerDto,Boolean>();
		createCheck(defaultCheckBoxCol);
		panel.getTable().getColumns().add(0, defaultCheckBoxCol);
		panel.getTable().getColumns().get(0).setGraphic(getSelectAllCheckBox());
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMaxWidth(37);
		panel.getTable().getColumns().get(0).setMinWidth(37);
		panel.setConstrainedResize(false);
		return panel;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createCheck(LoggedTableColumn rowIndex){
	    rowIndex.setCellFactory( new Callback<LoggedTableColumn<ApplicationCheckerDto, Boolean>, LoggedTableCell<ApplicationCheckerDto,Boolean>>() { 
			public LoggedTableCell<ApplicationCheckerDto, Boolean> call(
					LoggedTableColumn<ApplicationCheckerDto, Boolean> p) {
				return new LoggedTableCell<ApplicationCheckerDto, Boolean>() {
					@Override
					public void updateItem(Boolean item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
							setGraphic(null);
						} else {
							HBox hb = new HBox();
							CheckBox checkBox = new CheckBox();
							final ApplicationCheckerDto appCheckerDto = getApplicationDetailsTablePane().getTable().getItems().get(getIndex());
					    	SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty();
							booleanProperty.setValue(appCheckerDto.isSelected());
							checkBox.selectedProperty().bindBidirectional(booleanProperty);
							checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
							    @Override
							    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
							    	appCheckerDto.setSelected(newValue);
							    	if(newValue){
										selectedRowCount++;
									} else {
										selectedRowCount--;
									}
							    	selectAllCheckBox.setSelected(totalRowCount == selectedRowCount);
								}
							});
							hb.getChildren().add(checkBox);
							setGraphic(hb);
						}
					}
				};
			}
		});
	  
	}
	
	public CheckBox getSelectAllCheckBox() {
		if (selectAllCheckBox == null) {
			selectAllCheckBox = createCheckBox();
			selectAllCheckBox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if(selectAllCheckBox.isSelected())
						selectedRowCount = totalRowCount;
					else
						selectedRowCount = 0;
					for (ApplicationCheckerDto checkerDto : getApplicationDetailsTablePane().getTable().getItems()) {
						checkerDto.setSelected(selectAllCheckBox.isSelected());
					}
					resetCheckData();
				}
			});
		}
		return selectAllCheckBox;
	}
	
	private void resetCheckData() {
		List<ApplicationCheckerDto> appCheckerList = getApplicationDetailsTablePane().getTable().getItems();
		if(appCheckerList != null)
			applicationDetailsTablePane.setData(appCheckerList);
	}
		
	private CheckBox createCheckBox() {
		CheckBox checkBox = new CheckBox();
		checkBox.setStyle("-fx-font-size: 10pt;-fx-padding: 1px;");
		checkBox.getStyleClass().add("radio-btn");
		return checkBox;
	}
	
	private VBox createCopyApplicationId() {
		VBox copyAppVBox = new VBox();
		Label copyLbl = new Label("Copy Selected Application Checkers To ");
		copyLbl.setStyle("-fx-font-weight: bold; fx-font-size: 14pt;");
		HBox copyAppIdContainer= new HBox();
		Label appIdLbl = new Label("Application Id ");
		appIdLbl.setStyle("-fx-font-weight: bold ;");
		copyApplicationTextField = view.createAutoCompleteTextField("copyApplicationTextField", 175, "");
		copyAppIdContainer.getChildren().addAll(appIdLbl, copyApplicationTextField);
		copyAppIdContainer.setPadding(new Insets(15));
		copyAppVBox.setPadding(new Insets(10,0,0,0));
		
		HBox procesPointCopyHBox = new HBox();
		Label procesPoinCopytLbl = new Label("Process Point ");
		procesPoinCopytLbl.setStyle("-fx-font-weight: bold ;");
		processPointNameCopyText = new LoggedText();
		procesPointCopyHBox.setPadding(new Insets(0,0,10,12));
		procesPointCopyHBox.getChildren().addAll(procesPoinCopytLbl, processPointNameCopyText);
		
		errMsgText = new LoggedText();
		errMsgText.setFont(Font.font ("Verdana", 16));
		errMsgText.setFill(Color.RED);
		copyAppVBox.getChildren().addAll(copyLbl, copyAppIdContainer, procesPointCopyHBox, createCopyCancelBtn(), errMsgText);
		return copyAppVBox;
	}
	
	private HBox createSearchBtn() {
		HBox searchBtnHBox = new HBox();
		searchBtn = view.createBtn("Search");
		searchBtnHBox.getChildren().add(searchBtn);
		searchBtnHBox.setPadding(new Insets(5));
		return searchBtnHBox;
	}
	
	private HBox createCopyCancelBtn() {
		HBox copyBtnHBox = new HBox();
		copyBtn = view.createBtn("Copy");
		cancelBtn = view.createBtn("Cancel");
		copyBtnHBox.getChildren().addAll(copyBtn, cancelBtn);
		copyBtnHBox.setPrefSize(250,20);
		copyBtnHBox.setPadding(new Insets(5,0,0,10));
		copyBtnHBox.setSpacing(15);
		HBox.setHgrow(copyBtnHBox, Priority.ALWAYS);
		copyBtnHBox.setAlignment(Pos.BOTTOM_LEFT);
		return copyBtnHBox;
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
	
	public ObjectTablePane<ApplicationCheckerDto> getApplicationDetailsTablePane() {
		return applicationDetailsTablePane;
	}

	public void setApplicationDetailsTablePane(
			ObjectTablePane<ApplicationCheckerDto> applicationDetailsTablePane) {
		this.applicationDetailsTablePane = applicationDetailsTablePane;
	}

	public LoggedButton getSearchBtn() {
		return searchBtn;
	}

	public void setSearchBtn(LoggedButton searchBtn) {
		this.searchBtn = searchBtn;
	}

	public LoggedButton getCopyBtn() {
		return copyBtn;
	}

	public void setCopyBtn(LoggedButton copyBtn) {
		this.copyBtn = copyBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}
	
}
