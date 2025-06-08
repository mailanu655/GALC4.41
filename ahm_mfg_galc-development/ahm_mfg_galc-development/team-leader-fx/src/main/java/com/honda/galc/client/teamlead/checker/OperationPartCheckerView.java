package com.honda.galc.client.teamlead.checker;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckerType;
import com.honda.galc.checkers.Checkers;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.PartCheckerDto;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.util.KeyValue;

public class OperationPartCheckerView extends CheckerView<PartCheckerDto> {
	private UpperCaseFieldBean basePartNoTextField;
	
	public OperationPartCheckerView(CheckerConfigModel configModel, MainWindow mainWindow) {
		super(configModel, mainWindow);
	}
	
	@Override
	public Pane createSearchText() {
		VBox vb = new VBox();
		vb.setPadding(new Insets(10,0,0,0));
			
		vb.getChildren().addAll(this.createSearchTextField("Operation Name "), createBasePartNumber(), createCheckerComboBox(), createSearchBtn());
		loadCheckerComboBox(Checkers.getCheckers(CheckerType.Part), getKeyValue("", "ALL"));
		return vb;
	}
	
	private HBox createBasePartNumber() {
		HBox basePartNoHBox = new HBox();
		Label basePartNoLbl = new Label("Base Part No ");
		basePartNoLbl.setPadding(new Insets(5,20,5,1));
		basePartNoLbl.setStyle("-fx-font-weight: bold ;");
		basePartNoTextField = UiFactory.createUpperCaseFieldBean("basePartNoTextField", 13, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		basePartNoHBox.setPadding(new Insets(10,0,0,0));
		basePartNoHBox.getChildren().addAll(basePartNoLbl, basePartNoTextField);
		return basePartNoHBox; 
	}
	
	@SuppressWarnings("unchecked")
	public void doSearch() {
		String opName = "";
		String checker = "";
		String basePartNo = "";
		if(checkerComboBox != null && checkerComboBox.getSelectionModel().getSelectedItem() != null) {
			KeyValue<String, String> newValue = (KeyValue<String, String>) checkerComboBox.getSelectionModel().getSelectedItem();
			checker = newValue.getKey().toString();
		}
		if(!StringUtils.isBlank(searchTextField.getText()))
			opName = searchTextField.getText().toString();
		if(!StringUtils.isBlank(this.basePartNoTextField.getText()))
			basePartNo = this.basePartNoTextField.getText().toString();
		checkerDetailTablePane.getTable().getItems().clear();
		Set<PartCheckerDto> partDetailSet = new LinkedHashSet<PartCheckerDto>();
		List<PartCheckerDto> opCheckerList = new ArrayList<PartCheckerDto>();
		for (PartCheckerDto partCheckerDto : configModel.loadOperationPartChecker(opName, basePartNo, checker)) {
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
		checkerDetailTablePane.setData(opCheckerList);
		addTableListener();
	}
	
	public void autoCompleteSearch() {
		if(searchTextField != null) {
			List<String> operations = configModel.findAllOperation(searchTextField.getText().trim().toString());
			searchTextField.setSuggestionList(operations);
		}
	}
	
	@Override
	public Pane createDetailsTablePane(){
		ColumnMappingList columnMappingList = ColumnMappingList.with("Operation Name", "operationName")
			.put("Part No","partNo")
			.put("Part Item No","partItemNo")
			.put("Part Sec Code","partSectionCode")
			.put("Check Point", "checkPoint")
			.put("Seq", "checkSeq")
			.put("Checker", "checker")
			.put("Reaction Type", "reactionType")
			.put("Status", "status");
			
		Double[] columnWidths = new Double[] {
				0.14,0.08,0.09,0.08,0.09,0.03,0.12,0.10,0.05
			};
		return this.createCheckerDetailTablePane(columnMappingList, columnWidths);
	}
	
	@Override
	public void editChecker(ActionEvent actionEvent) {
		PartCheckerDto partCheckerDto = this.getSelectedCheckerForEdit();
		if(partCheckerDto == null) return;
		
		OperationPartViewEditDialog dialog = new OperationPartViewEditDialog("Edit", partCheckerDto, configModel, this);
		dialog.showDialog();
	}
	
	@Override
	public void deleteChecker(ActionEvent actionEvent) {
		try {
			String msgInfo = "Operation Name - ";
			List<MCPartChecker> partCheckers = new ArrayList<MCPartChecker>();
			List<PartCheckerDto> checkerDtos = checkerDetailTablePane.getTable().getSelectionModel().getSelectedItems();
			if(checkerDtos != null && checkerDtos.size() == 0) {
				MessageDialog.showInfo(window.getStage(), "Please Select Checker");
				return;
			}
			for (PartCheckerDto partCheckerDto : checkerDtos) {
				msgInfo  = msgInfo.concat(partCheckerDto.getOperationName() + " \n");
				partCheckers.addAll(configModel.findAllPartCheckerBy(partCheckerDto));
			}
			
			if(partCheckers != null && MessageDialog.confirm(getMainWindow().getStage(), msgInfo +  "Do you wish to delete?")) {
				configModel.deletePartChecker(partCheckers);
				doSearch();
			}
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}
	
	@Override
	public void duplicateChecker(){
		OperationPartCheckerCopyDialog dialog = new OperationPartCheckerCopyDialog("Duplicate Part Checker", configModel, this);
		dialog.showDialog();
	}
	
	@Override
	public void addChecker(){
		OperationPartViewEditDialog dialog = new OperationPartViewEditDialog("Add", null, configModel, this);
		dialog.showDialog();
	}

	@Override
	public void searchTextChanged() {
		
	}
}
