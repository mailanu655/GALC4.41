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
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.PartType;
import com.honda.galc.dto.PartCheckerDto;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCPartChecker;

public class OperationPartCheckerCopyDialog extends FxDialog{

	private AutoCompleteTextField operationSearchTextField;
	private AutoCompleteTextField copyOperationTextField;
	private UpperCaseFieldBean basePartNoTextField;
	private LoggedText errMsgText;
	private ObjectTablePane<PartCheckerDto> partDetailsTablePane;
	private LoggedButton searchBtn;
	private LoggedButton copyBtn;
	private LoggedButton cancelBtn;
	private CheckerConfigModel model;
	private OperationPartCheckerView view;
	private CheckBox selectAllCheckBox;
	private int totalRowCount = 0;
	private int selectedRowCount = 0;
	
	public OperationPartCheckerCopyDialog(String title, CheckerConfigModel model, OperationPartCheckerView view) {
		super("", ClientMainFx.getInstance().getStage());
		this.model = model;
		this.view = view;
		initComponents(title);
		handleSearchAction();
		setTableData();
	}

	private void initComponents(String type) {
		((BorderPane) this.getScene().getRoot()).setCenter(createTitiledPane("Copy Operation Part Checker Form", createViewEditContainer(),700, 500));
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
		searchBtn.setStyle("-fx-pref-height: 5px; -fx-pref-width: 90px; -fx-font-size : 11pt;");
		
		partDetailsTablePane = createOperationDetailsTablePane();
		outerPane.getChildren().addAll(operationHBox, partDetailsTablePane, createCopyOperationName());
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
            	searchPartChecker();
            }
        });
	}
	
	public void searchPartChecker() {
		String opName = "", partNo = "";
		if(!StringUtils.isBlank(operationSearchTextField.getText()))
			opName = operationSearchTextField.getText().trim();
		if(!StringUtils.isBlank(basePartNoTextField.getText()))
			partNo = basePartNoTextField.getText().trim();
		partDetailsTablePane.getTable().getItems().clear();
		Set<PartCheckerDto> partDetailSet = new LinkedHashSet<PartCheckerDto>();
		List<PartCheckerDto> opCheckerList = new ArrayList<PartCheckerDto>();
		for (PartCheckerDto partCheckerDto : model.loadOperationPartChecker(opName, partNo, StringUtils.EMPTY, StringUtils.EMPTY)) {
			if(partDetailSet.contains(partCheckerDto)) {
				if(partCheckerDto.getStatus().equals("ACTIVE")) {
					partDetailSet.remove(partCheckerDto);
					partDetailSet.add(partCheckerDto);
				}
			} else {
				partDetailSet.add(partCheckerDto);
			}
		}
		opCheckerList.addAll(partDetailSet);
		partDetailsTablePane.setData(opCheckerList);
		totalRowCount = partDetailsTablePane.getTable().getItems().size();
		selectAllCheckBox.setSelected(false);
	}
	
	private void setTableData() {
		List<PartCheckerDto> partCheckerList = view.getCheckerDetailTablePane().getTable().getSelectionModel().getSelectedItems();
		if(partCheckerList != null) {
			partDetailsTablePane.setData(partCheckerList);
			totalRowCount = partDetailsTablePane.getTable().getItems().size();
		}
	}
	
	private void autoCompleteSearch() {
		List<String> operations = model.loadAllPartCheckerOpName(operationSearchTextField.getText().trim().toString());
		operationSearchTextField.setSuggestionList(operations);
	}
	
	private void copyAutoCompleteSearch() {
		List<String> operations = model.findAllPartOperation(copyOperationTextField.getText().trim().toString());
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
			if(validateFromAndToOperationPartNames()){
				this.errMsgText.setText("Duplicate Operation cannot be performed on the same Operation Names");
				return;
			}
			opName = copyOperationTextField.getText().trim();
			List<MCOperationRevision> revisions = model.findAllByOperationAndRevisions(opName, null, true);
			if(revisions != null && revisions.size() == 0) {
				this.errMsgText.setText("No active record for operation: " + opName);
				return;
			}
			
			for (PartCheckerDto partCheckerDto : getPartDetailsTablePane().getTable().getItems()) {
    			if(partCheckerDto.isSelected()){
    				MCOperationPartRevision partRevision =  model.findAllByPartNoSecCodeItemNoAndType(partCheckerDto.getOperationName(), partCheckerDto.getPartNo(), partCheckerDto.getPartItemNo(), 
            				partCheckerDto.getPartSectionCode(), PartType.get(partCheckerDto.getPartType()));
            		if(revisions != null && partRevision!= null && revisions.size() > 0) {
	            		MCPartChecker partChecker = ConvertToEntity.convertToEntity(partCheckerDto);
	    				partChecker.getId().setOperationName(opName);
	    				partChecker.getId().setOperationRevision(revisions.get(0).getId().getOperationRevision());
	    				partChecker.getId().setPartId(partRevision.getId().getPartId());
	            		model.copyPartChecker(partChecker);
            		}
    			}
			}
			
			view.doSearch();
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			for (PartCheckerDto checkerDto : getPartDetailsTablePane().getTable().getItems()) {
				checkerDto.setSelected(false);
			}
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private boolean validateCopyOperationName() {
		if(StringUtils.isBlank(this.copyOperationTextField.getText())){
			return false;
		}
		List<String> operations = model.findAllPartOperation(copyOperationTextField.getText().trim().toString());
		return operations.size() > 0;
	}
	
	private boolean validateFromAndToOperationPartNames() {
		ObservableList<PartCheckerDto> partDetails = partDetailsTablePane.getTable().getItems();
		for(PartCheckerDto partCheckerDto:partDetails){
			if(partCheckerDto.isSelected() && partCheckerDto.getOperationName().equalsIgnoreCase(copyOperationTextField.getText()))
				return true;
		}
	return false;
	}
	private void cancelBtnAction(ActionEvent event){
		try {
			LoggedButton cancelBtn = getCancelBtn();
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			for (PartCheckerDto checkerDto : getPartDetailsTablePane().getTable().getItems()) {
				checkerDto.setSelected(false);
			}
			stage.close();
		} catch (Exception e) {
			view.getLogger().error(e.getMessage());
		}
	}
	
	private ObjectTablePane<PartCheckerDto> createOperationDetailsTablePane() { 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Operation Name", "operationName")
				.put("Part No","partNo")
				.put("Part Item No","partItemNo")
				.put("Part Sec Code","partSectionCode")
				.put("Check Point", "checkPoint")
				.put("Seq", "checkSeq")
				.put("Checker", "checker")
				.put("Reaction Type", "reactionType")
				.put("Status", "status");
				
		Double[] columnWidth = new Double[] {
				0.14,0.08,0.09,0.08,0.09,0.03,0.12,0.10,0.05
			};
		ObjectTablePane<PartCheckerDto> panel = new ObjectTablePane<PartCheckerDto>(columnMappingList,columnWidth);
		LoggedTableColumn<PartCheckerDto,Boolean> defaultCheckBoxCol = new LoggedTableColumn<PartCheckerDto,Boolean>();
		
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
	    rowIndex.setCellFactory( new Callback<LoggedTableColumn<PartCheckerDto, Boolean>, LoggedTableCell<PartCheckerDto,Boolean>>() { 
			public LoggedTableCell<PartCheckerDto, Boolean> call(
					LoggedTableColumn<PartCheckerDto, Boolean> p) {
				return new LoggedTableCell<PartCheckerDto, Boolean>() {
					@Override
					public void updateItem(Boolean item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
							setGraphic(null);
						} else {
							HBox hb = new HBox();
							CheckBox checkBox = createCheckBox();
							final PartCheckerDto partCheckerDto = getPartDetailsTablePane().getTable().getItems().get(getIndex());
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
					for (PartCheckerDto checkerDto : getPartDetailsTablePane().getTable().getItems()) {
						checkerDto.setSelected(selectAllCheckBox.isSelected());
					}
					resetCheckData();
				}
			});
		}
		return selectAllCheckBox;
	}
	
	private void resetCheckData() {
		List<PartCheckerDto> partCheckerList = getPartDetailsTablePane().getTable().getItems();
		if(partCheckerList != null)
			getPartDetailsTablePane().setData(partCheckerList);
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
		searchBtnHBox.setPadding(new Insets(5));
		return searchBtnHBox;
	}
	
	private HBox createCopyCancelBtn() {
		HBox copyBtnHBox = new HBox();
		copyBtn = view.createBtn("Copy");
		cancelBtn = view.createBtn("Cancel");
		copyBtnHBox.getChildren().addAll(copyBtn, cancelBtn);
		copyBtnHBox.setPrefSize(250,20);
		copyBtnHBox.setPadding(new Insets(10,0,0,20));
		copyBtnHBox.setSpacing(15);
		copyBtnHBox.setAlignment(Pos.BASELINE_LEFT);
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

	public ObjectTablePane<PartCheckerDto> getPartDetailsTablePane() {
		return partDetailsTablePane;
	}

	public void setPartDetailsTablePane(
			ObjectTablePane<PartCheckerDto> partDetailsTablePane) {
		this.partDetailsTablePane = partDetailsTablePane;
	}
	
}
