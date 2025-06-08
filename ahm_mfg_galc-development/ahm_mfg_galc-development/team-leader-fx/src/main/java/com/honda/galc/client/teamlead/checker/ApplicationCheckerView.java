package com.honda.galc.client.teamlead.checker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import com.honda.galc.checkers.CheckerType;
import com.honda.galc.checkers.Checkers;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.dto.ApplicationCheckerDto;
import com.honda.galc.entity.conf.MCAppChecker;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.util.KeyValue;

public class ApplicationCheckerView extends CheckerView<ApplicationCheckerDto>  {
	private LoggedText processPointNameText;
	
	public ApplicationCheckerView(CheckerConfigModel configModel, MainWindow mainWindow) {
		super(configModel, mainWindow);
	}
	
	@Override
	public Pane createSearchText() {
		VBox vb = new VBox();
		vb.getChildren().addAll(
				createSearchTextField("Application Id: "), 
				createProcessPointBox(), 
				createCheckerComboBox(), 
				createSearchBtn());
		
		loadCheckerComboBox(Checkers.getCheckers(CheckerType.Application), getKeyValue("", "ALL"));
		vb.setPadding(new Insets(10,0,0,0));
		vb.setSpacing(10);
		return vb;
	}
	
	private HBox createProcessPointBox() {
		Label label = new Label("Process Point: ");
		label.setPadding(new Insets(5,17,5,0));
		label.setStyle("-fx-font-weight: bold ;");
		HBox processPointHBox = new HBox();
		this.processPointNameText = new LoggedText();
		processPointHBox.setAlignment(Pos.CENTER_LEFT);
		processPointHBox.getChildren().addAll(label, this.processPointNameText);
		return processPointHBox;
	}
	
	public Pane createDetailsTablePane(){
		ColumnMappingList columnMappingList = ColumnMappingList.with("Application Id", "applicationId")
				.put("Process pt","processPointName")
				.put("Check Point","checkPoint")
				.put("Check Seq", "checkSeq")
				.put("Checker", "checker")
				.put("Reaction Type", "reactionType");
			
		Double[] columnWidths = new Double[] {0.08,0.08,0.13,0.05,0.24,0.17,0.10};
	
		return this.createCheckerDetailTablePane(columnMappingList, columnWidths);
	}
	
	@SuppressWarnings("unchecked")
	public void doSearch() {
		String checker = "";
		checkerDetailTablePane.getTable().getItems().clear();
		if(checkerComboBox != null && checkerComboBox.getSelectionModel().getSelectedItem() != null) {
			KeyValue<String, String> newValue = (KeyValue<String, String>) checkerComboBox.getSelectionModel().getSelectedItem();
			checker = newValue.getKey().toString();
		}
		checkerDetailTablePane.setData(configModel.loadAppliationChecker(searchTextField.getText().toString(), checker));
		addTableListener();
	}
	
	public void duplicateChecker(){
		ApplicationCheckerCopyDialog dialog = new ApplicationCheckerCopyDialog("Duplicate Application Checker", configModel, this);
		dialog.initStyle(StageStyle.UNDECORATED);
		dialog.showDialog();
	}
	
	public void addChecker(){
		ApplicationViewEditDialog dialog = new ApplicationViewEditDialog(CheckerConstants.ADD, null, configModel, this);
		dialog.showDialog();
	}
	
	public void editChecker(ActionEvent actionEvent) {
		ApplicationCheckerDto applicationCheckerDto = this.getSelectedCheckerForEdit();
		if(applicationCheckerDto == null) return;
		
		ApplicationViewEditDialog dialog = new ApplicationViewEditDialog("Edit", applicationCheckerDto, configModel, this);
		dialog.showDialog();
	}
	
	public void deleteChecker(ActionEvent actionEvent) {
		try {
			String msgInfo = "Application Ids - ";
			List<MCAppChecker> appCheckers = new ArrayList<MCAppChecker>();
			List<ApplicationCheckerDto> checkerDtos = this.checkerDetailTablePane.getTable().getSelectionModel().getSelectedItems();
			if(checkerDtos != null && checkerDtos.size() == 0) {
				MessageDialog.showInfo(window.getStage(), "Please Select Checker");
				return;
			}
			for (ApplicationCheckerDto appCheckerDto : checkerDtos) {
				msgInfo  = msgInfo.concat(appCheckerDto.getApplicationId() + " ");
				appCheckers.add(ConvertToEntity.convertToEntity(appCheckerDto));
			}
			if(appCheckers != null && MessageDialog.confirm(window.getStage(), msgInfo + "Do you wish to delete?")) {
		    	configModel.deleteApplicationChecker(appCheckers);
		    	doSearch();
			}
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}
	
    public void searchTextChanged() {
    	if (searchTextField.getText().contains("-")) { 
    		String applicationId = searchTextField.getText();
    		searchTextField.settext(applicationId.substring(applicationId.lastIndexOf(" - ") + 3).trim());
    		this.processPointNameText.setText(applicationId.substring(0, applicationId.lastIndexOf(" - ")).trim());
    	} else {
    		this.processPointNameText.setText(CheckerConstants.EMPTY);
    	}
    }
	
    public void autoCompleteSearch() {
    	loadApplicationIds();
		searchTextField.setSuggestionList(getAutoCompleteList());
	}
    
	public void loadApplicationIds() {
		List<ProcessPoint> processPoints = configModel.findAll();
		getAutoCompleteList().clear();
		Collections.sort(processPoints, new Comparator<ProcessPoint>() {
            @Override
            public int compare(ProcessPoint o1, ProcessPoint o2) {
                return o1.getProcessPointName().compareTo(o2.getProcessPointName());
            }
        });
		for (ProcessPoint pp : processPoints) {
			getAutoCompleteList().add(pp.getProcessPointName() + " - " + pp.getProcessPointId());
		}
	}
	
	public HBox createSearchTextField(String label) {
		HBox hBox = new HBox();
		Label searchLbl = new Label(label);
		searchLbl.setPadding(new Insets(5,17,5,0));
		searchLbl.setStyle("-fx-font-weight: bold ;");
		searchTextField = createAutoCompleteTextField("searchTextField", 162, "");
		hBox.setPadding(new Insets(10,0,0,0));
		hBox.getChildren().addAll(searchLbl, searchTextField);
		return hBox;
	}
}
