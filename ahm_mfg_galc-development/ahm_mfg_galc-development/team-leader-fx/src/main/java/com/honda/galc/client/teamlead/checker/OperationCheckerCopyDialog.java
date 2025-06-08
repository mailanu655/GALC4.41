package com.honda.galc.client.teamlead.checker;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dto.OperationCheckerDto;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.entity.conf.MCOperationRevision;

public class OperationCheckerCopyDialog extends FxDialog{

	private AutoCompleteTextField operationSearchTextField;
	private AutoCompleteTextField copyOperationTextField;
	private LoggedText errMsgText;
	private ObjectTablePane<OperationCheckerDto> operationDetailsTablePane;
	private LoggedButton searchBtn;
	private LoggedButton copyBtn;
	private LoggedButton cancelBtn;
	private CheckerConfigModel model;
	private OperationCheckerView view;
	private CheckBox selectAllCheckBox;
	private int totalRowCount = 0;
	private int selectedRowCount = 0;
	
	public OperationCheckerCopyDialog(String title, CheckerConfigModel model, OperationCheckerView view) {
		super("", ClientMainFx.getInstance().getStage());
		this.model = model;
		this.view = view;
		initComponents(title);
		handleSearchAction();
		setTableData();
	}

	private void initComponents(String type) {
		((BorderPane) this.getScene().getRoot()).setCenter(createTitiledPane("Copy Operation Checker Form", createViewEditContainer(),700, 500));
	}
	
	private VBox createViewEditContainer(){
		VBox outerPane = new VBox();
		outerPane.setSpacing(0);
		outerPane.setPrefWidth(700);
		outerPane.setPrefHeight(500);
		
		HBox operationHBox = new HBox();
		Label searchLbl = new Label("Operation Name ");
		searchLbl.setPadding(new Insets(5,2,5,5));
		searchLbl.setStyle("-fx-font-weight: bold ;");
		operationSearchTextField = view.createAutoCompleteTextField("operationSearchTextField", 175, "");
		operationHBox.getChildren().addAll(searchLbl, operationSearchTextField, createSearchBtn());
		
		operationDetailsTablePane = createOperationDetailsTablePane();
		outerPane.getChildren().addAll(operationHBox, operationDetailsTablePane, createCopyOperationName());
				
		return outerPane;
	}
	
	private void handleSearchAction(){
		operationSearchTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				autoCompleteSearch();
			}
		});	
		copyOperationTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				copyAutoCompleteSearch();
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
            	searchOperationChecker();
            }
        });
	}
	
	public void searchOperationChecker() {
		String opName = "";
		List<OperationCheckerDto> opCheckerList = new ArrayList<OperationCheckerDto>();
		Set<OperationCheckerDto> opDetailSet = new LinkedHashSet<OperationCheckerDto>();
		operationDetailsTablePane.getTable().getItems().clear();
		if(!StringUtils.isBlank(operationSearchTextField.getText()))
			opName = operationSearchTextField.getText().trim();
		for (OperationCheckerDto opCheckerDto : model.loadOperationChecker(opName, StringUtils.EMPTY, StringUtils.EMPTY)) {
			if(opDetailSet.contains(opCheckerDto)) {
				if(opCheckerDto.getStatus().equals("ACTIVE")) {
					opDetailSet.remove(opCheckerDto);
					opDetailSet.add(opCheckerDto);
				}
			} else {
				opDetailSet.add(opCheckerDto);
			}
		}
		opCheckerList.addAll(opDetailSet);
		operationDetailsTablePane.setData(opCheckerList);
		totalRowCount = operationDetailsTablePane.getTable().getItems().size();
		selectAllCheckBox.setSelected(false);
	}
	
	private void setTableData() {
		List<OperationCheckerDto> appCheckerList = view.getCheckerDetailTablePane().getTable().getSelectionModel().getSelectedItems();
		if(appCheckerList != null) {
			operationDetailsTablePane.setData(appCheckerList);
			totalRowCount = operationDetailsTablePane.getTable().getItems().size();
		}
	}
	
	private void autoCompleteSearch() {
		List<String> operations = model.findAllOperationChecker(operationSearchTextField.getText().trim().toString());
		operationSearchTextField.setSuggestionList(operations);
	}
	
	private void copyAutoCompleteSearch() {
		List<String> operations = model.findAllOperation(copyOperationTextField.getText().trim().toString());
		copyOperationTextField.setSuggestionList(operations);
	}
	
	private void copyBtnAction(ActionEvent event){
		LoggedButton cancelBtn = getCopyBtn();
		String opName = "";
		try {
			if(!validateCopyOperationName()) {
				this.errMsgText.setText("Please Enter Copy Operation Name");
				return;
			}
			if(!validateFromAndToOperationName()){
				this.errMsgText.setText("Duplicate Operation cannot be performed on the same Operation Names");
				return;
			}
			opName = copyOperationTextField.getText().trim();
			List<MCOperationRevision> revisions = model.findAllByOperationAndRevisions(opName, null, true);
			if(revisions != null && revisions.size() == 0) {
				this.errMsgText.setText("No active record for operation: " + opName);
				return;
			}
			
			for (OperationCheckerDto opCheckerDto : operationDetailsTablePane.getTable().getItems()) {
    			if(opCheckerDto.isSelected()){
    				if(revisions != null && revisions.size() > 0) {
	            		MCOperationChecker opChecker = ConvertToEntity.convertToEntity(opCheckerDto);
	            		opChecker.getId().setOperationName(opName);
	            		opChecker.getId().setOperationRevision(revisions.get(0).getId().getOperationRevision());
	            		model.saveCopyOperationChecker(opChecker);
            		}
    			}
			}
			view.doSearch();
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private boolean validateCopyOperationName() {
		if(StringUtils.isBlank(this.copyOperationTextField.getText())){
			return false;
		}
		List<String> operations = model.findAllOperation(copyOperationTextField.getText().trim().toString());
		return operations.size() > 0;
	}
	
	private boolean validateFromAndToOperationName() {
		ObservableList<OperationCheckerDto> operationDetails = operationDetailsTablePane.getTable().getItems();
		for(OperationCheckerDto operationCheckerDto:operationDetails){
			if(operationCheckerDto.isSelected() && operationCheckerDto.getOperationName().equalsIgnoreCase(copyOperationTextField.getText()))
				return false;
		}
		return true;
	}
	private void cancelBtnAction(ActionEvent event){
		try {
			LoggedButton cancelBtn = getCancelBtn();
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			for (OperationCheckerDto checkerDto : getOperationDetailsTablePane().getTable().getItems()) {
				checkerDto.setSelected(false);
			}
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private ObjectTablePane<OperationCheckerDto> createOperationDetailsTablePane() { 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Operation Name", "operationName")
		.put("Check Point","checkPoint")
		.put("Check Seq", "checkSeq")
		.put("Checker", "checker")
		.put("Reaction Type", "reactionType");
		
		Double[] columnWidth = new Double[] {
				0.10, 0.12,0.05,0.19,0.16,0.10
			};
		ObjectTablePane<OperationCheckerDto> panel = new ObjectTablePane<OperationCheckerDto>(columnMappingList,columnWidth);
		LoggedTableColumn<OperationCheckerDto,Boolean> defaultCheckBoxCol = new LoggedTableColumn<OperationCheckerDto,Boolean>();
		
		createCheck(defaultCheckBoxCol);
		panel.getTable().getColumns().add(0, defaultCheckBoxCol);
		panel.getTable().getColumns().get(0).setGraphic(getSelectAllCheckBox());
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMaxWidth(40);
		panel.getTable().getColumns().get(0).setMinWidth(40);
		panel.setConstrainedResize(false);
		return panel;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createCheck(LoggedTableColumn rowIndex){
	    rowIndex.setCellFactory( new Callback<LoggedTableColumn<MCOperationChecker, Boolean>, LoggedTableCell<MCOperationChecker,Boolean>>() { 
			public LoggedTableCell<MCOperationChecker, Boolean> call(
					LoggedTableColumn<MCOperationChecker, Boolean> p) {
				return new LoggedTableCell<MCOperationChecker, Boolean>() {
					@Override
					public void updateItem(Boolean item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
							setGraphic(null);
						} else {
							HBox hb = new HBox();
							CheckBox checkBox = createCheckBox();
							final OperationCheckerDto opCheckerDto = getOperationDetailsTablePane().getTable().getItems().get(getIndex());
							SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty();
							booleanProperty.setValue(opCheckerDto.isSelected());
							checkBox.selectedProperty().bindBidirectional(booleanProperty);
							checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
									opCheckerDto.setSelected(newValue);
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
					for (OperationCheckerDto checkerDto : getOperationDetailsTablePane().getTable().getItems()) {
						checkerDto.setSelected(selectAllCheckBox.isSelected());
					}
					resetCheckData();
				}
			});
		}
		return selectAllCheckBox;
	}
	
	private void resetCheckData() {
		List<OperationCheckerDto> opCheckerList = getOperationDetailsTablePane().getTable().getItems();
		if(opCheckerList != null)
			getOperationDetailsTablePane().setData(opCheckerList);
	}
	
	private CheckBox createCheckBox() {
		CheckBox checkBox = new CheckBox();
		checkBox.setStyle("-fx-font-size: 10pt;-fx-padding: 1px;");
		checkBox.getStyleClass().add("radio-btn");
		return checkBox;
	}
	
	private VBox createCopyOperationName() {
		VBox copyAppVBox = new VBox();
		Label copyLbl = new Label("Copy Selected Operation Checkers To ");
		copyLbl.setStyle("-fx-font-weight: bold; fx-font-size: 14pt;");
		HBox copyAppIdContainer= new HBox();
		Label appIdLbl = new Label("Operation Name ");
		appIdLbl.setStyle("-fx-font-weight: bold ;");
		copyOperationTextField = view.createAutoCompleteTextField("copyOperationTextField", 175, "");
		copyAppIdContainer.getChildren().addAll(appIdLbl, copyOperationTextField);
		errMsgText = new LoggedText();
		errMsgText.setFont(Font.font ("Verdana", 16));
		errMsgText.setFill(Color.RED);
		copyAppIdContainer.setPadding(new Insets(15));
		copyAppVBox.setPadding(new Insets(10,0,0,0));
		copyAppVBox.getChildren().addAll(copyLbl, copyAppIdContainer, createCopyCancelBtn(), errMsgText);
		
		return copyAppVBox;
	}
	
	private HBox createSearchBtn() {
		HBox searchBtnHBox = new HBox();
		searchBtn = view.createBtn("Search");
		searchBtnHBox.getChildren().add(searchBtn);
		searchBtnHBox.setPadding(new Insets(0,5,5,5));
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
	
	public LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected) {
		LoggedRadioButton radioButton = new LoggedRadioButton(title);
		radioButton.getStyleClass().add("radio-btn");
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		return radioButton;
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

	public ObjectTablePane<OperationCheckerDto> getOperationDetailsTablePane() {
		return operationDetailsTablePane;
	}

	public void setOperationDetailsTablePane(
			ObjectTablePane<OperationCheckerDto> operationDetailsTablePane) {
		this.operationDetailsTablePane = operationDetailsTablePane;
	}
	
}
