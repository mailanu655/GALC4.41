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
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.PartType;
import com.honda.galc.dto.MeasurementCheckerDto;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;

public class MeasurementCheckerCopyDialog extends FxDialog{

	private AutoCompleteTextField operationSearchTextField;
	private AutoCompleteTextField copyOperationTextField;
	private UpperCaseFieldBean basePartNoTextField;
	private LoggedText errMsgText;
	private ObjectTablePane<MeasurementCheckerDto> measurementDetailsTablePane;
	private LoggedButton searchBtn;
	private LoggedButton copyBtn;
	private LoggedButton cancelBtn;
	private CheckerConfigModel model;
	private MeasurementCheckerView view;
	private CheckBox selectAllCheckBox;
	private int totalRowCount = 0;
	private int selectedRowCount = 0;
	
	public MeasurementCheckerCopyDialog(String title, CheckerConfigModel model, MeasurementCheckerView view) {
		super("", ClientMainFx.getInstance().getStage());
		this.model = model;
		this.view = view;
		initComponents(title);
		handleSearchAction();
		setTableData();
	}

	private void initComponents(String type) {
		((BorderPane) this.getScene().getRoot()).setCenter(createTitiledPane("Copy Measurement Checker Form", createViewEditContainer(),700, 500));
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
		Label partNoLbl = new Label("Part No ");
		partNoLbl.setPadding(new Insets(5,2,15,5));
		partNoLbl.setStyle("-fx-font-weight: bold ;");
		basePartNoTextField = UiFactory.createUpperCaseFieldBean("copyBasePartNoTextField", 14, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		
		operationHBox.getChildren().addAll(searchLbl, operationSearchTextField, partNoLbl, basePartNoTextField, createSearchBtn());
		measurementDetailsTablePane = createOperationDetailsTablePane();
		outerPane.getChildren().addAll(operationHBox, measurementDetailsTablePane, createCopyOperationName());
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
            	searchMeasurementChecker();
            }
        });
	}
	
	public void searchMeasurementChecker() {
		String opName = "", partNo = "";
		if(!StringUtils.isBlank(operationSearchTextField.getText()))
			opName = operationSearchTextField.getText().trim();
		if(!StringUtils.isBlank(basePartNoTextField.getText()))
			partNo = basePartNoTextField.getText().trim();
		measurementDetailsTablePane.getTable().getItems().clear();
		Set<MeasurementCheckerDto> measDetailSet = new LinkedHashSet<MeasurementCheckerDto>();
		List<MeasurementCheckerDto> measCheckerList = new ArrayList<MeasurementCheckerDto>();
		for (MeasurementCheckerDto measCheckerDto : model.loadMeasurementChecker(opName, partNo, StringUtils.EMPTY, StringUtils.EMPTY)) {
			if(measDetailSet.contains(measCheckerDto)) {
				if(measCheckerDto.getStatus().equals("ACTIVE")) {
					measDetailSet.remove(measCheckerDto);
					measDetailSet.add(measCheckerDto);
				}
			} else {
				measDetailSet.add(measCheckerDto);
			}
		}
		measCheckerList.addAll(measDetailSet);
		measurementDetailsTablePane.setData(measCheckerList);
		totalRowCount = getMeasurementDetailsTablePane().getTable().getItems().size();
		selectAllCheckBox.setSelected(false);
	}
	
	private void setTableData() {
		List<MeasurementCheckerDto> measCheckerList = view.getCheckerDetailTablePane().getTable().getSelectionModel().getSelectedItems();
		if(measCheckerList != null) {
			measurementDetailsTablePane.setData(measCheckerList);
			totalRowCount = getMeasurementDetailsTablePane().getTable().getItems().size();
		}
	}
	
	private void autoCompleteSearch() {
		List<String> operations = model.loadAllMeasurementCheckerOpName(operationSearchTextField.getText().trim().toString());
		operationSearchTextField.setSuggestionList(operations);
	}
	
	private void copyAutoCompleteSearch() {
		List<String> operations = model.findAllMeasurementOperation(copyOperationTextField.getText().trim().toString());
		copyOperationTextField.setSuggestionList(operations);
	}
	
	private void copyBtnAction(ActionEvent event){
		Stage stage = (Stage) getCopyBtn().getScene().getWindow();
		
		String opName = "";
		try {
			if(!validateCopyOperationName()) {
				this.errMsgText.setText("Please Enter Valid Copy Operation Name");
				return;
			}
			if(validateFromAndToOperationPartNames()){
				this.errMsgText.setText("Duplicate Operation cannot be performed on the same Operation Names");
				return;
			}
			if(StringUtils.isNotBlank(copyOperationTextField.getText()))
				opName = copyOperationTextField.getText().trim();
			List<MCOperationRevision> revisions = model.findAllByOperationAndRevisions(opName, null, true);
			if(revisions != null && revisions.size() == 0) {
				this.errMsgText.setText("No active record for operation: " + opName);
				return;
			}
    		
			for (MeasurementCheckerDto measCheckerDto : getMeasurementDetailsTablePane().getTable().getItems()) {
    			if(measCheckerDto.isSelected()){
    				MCOperationPartRevision partRevision =  model.findAllByPartNoSecCodeItemNoAndType(measCheckerDto.getOperationName(), measCheckerDto.getPartNo(), measCheckerDto.getPartItemNo(), 
            				measCheckerDto.getPartSectionCode(),PartType.get(measCheckerDto.getPartType()));
            		if(revisions != null && partRevision != null && revisions.size() > 0) {
            			MCMeasurementChecker measChecker = ConvertToEntity.convertToEntity(measCheckerDto);
            			measChecker.getId().setOperationName(opName);
            			measChecker.getId().setOperationRevision(revisions.get(0).getId().getOperationRevision());
            			measChecker.getId().setPartId(partRevision.getId().getPartId());
	            		model.copyMeasurementChecker(measChecker);
            		}
    			}
			}
			
			view.doSearch();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private boolean validateCopyOperationName() {
		if(StringUtils.isBlank(this.copyOperationTextField.getText())){
			return false;
		}
		List<String> operations = model.findAllMeasurementOperation(copyOperationTextField.getText().trim().toString());
		return operations.size() > 0;
	}
	
	private boolean validateFromAndToOperationPartNames() {
		ObservableList<MeasurementCheckerDto> measurementDetails = measurementDetailsTablePane.getTable().getItems();
		for(MeasurementCheckerDto measurementCheckerDto:measurementDetails){
			if(measurementCheckerDto.isSelected() && measurementCheckerDto.getOperationName().equalsIgnoreCase(copyOperationTextField.getText()))
				return true;
		}
		return false;
	}
	
	private void cancelBtnAction(ActionEvent event){
		try {
			LoggedButton cancelBtn = getCancelBtn();
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private ObjectTablePane<MeasurementCheckerDto> createOperationDetailsTablePane() { 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Operation Name", "operationName")
			.put("Part No","partNo")
			.put("Check Point", "checkPoint")
			.put("Seq", "checkSeq")
			.put("Checker", "checker")
			.put("Reaction Type", "reactionType")
			.put("Status", "status");
				
		Double[] columnWidth = new Double[] {
				0.10, 0.06,0.12,0.05,0.12,0.10,0.12
			};
		ObjectTablePane<MeasurementCheckerDto> panel = new ObjectTablePane<MeasurementCheckerDto>(columnMappingList,columnWidth);
		LoggedTableColumn<MeasurementCheckerDto,Boolean> defaultCheckBoxCol = new LoggedTableColumn<MeasurementCheckerDto,Boolean>();
		
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
							final MeasurementCheckerDto partCheckerDto = getMeasurementDetailsTablePane().getTable().getItems().get(getIndex());
							SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty();
							booleanProperty.setValue(partCheckerDto.isSelected());
							checkBox.selectedProperty().bindBidirectional(booleanProperty);
							checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
									partCheckerDto.setSelected(newValue);
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
					for (MeasurementCheckerDto checkerDto : getMeasurementDetailsTablePane().getTable().getItems()) {
						checkerDto.setSelected(selectAllCheckBox.isSelected());
					}
					resetCheckData();
				}
			});
		}
		return selectAllCheckBox;
	}
	
	private void resetCheckData() {
		List<MeasurementCheckerDto> measCheckerList = getMeasurementDetailsTablePane().getTable().getItems();
		if(measCheckerList != null)
			getMeasurementDetailsTablePane().setData(measCheckerList);
	}
	
	private CheckBox createCheckBox() {
		CheckBox checkBox = new CheckBox();
		checkBox.setStyle("-fx-font-size: 10pt;-fx-padding: 1px;");
		checkBox.getStyleClass().add("radio-btn");
		return checkBox;
	}
	
	private VBox createCopyOperationName() {
		VBox copyAppVBox = new VBox();
		Label copyLbl = new Label("Copy Selected Measurement Checkers To ");
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

	public ObjectTablePane<MeasurementCheckerDto> getMeasurementDetailsTablePane() {
		return measurementDetailsTablePane;
	}

	public void setMeasurementDetailsTablePane(
			ObjectTablePane<MeasurementCheckerDto> measurementDetailsTablePane) {
		this.measurementDetailsTablePane = measurementDetailsTablePane;
	}
	
}
