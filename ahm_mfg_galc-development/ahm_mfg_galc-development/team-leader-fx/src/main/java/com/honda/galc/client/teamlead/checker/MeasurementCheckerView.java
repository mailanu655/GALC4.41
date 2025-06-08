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
import com.honda.galc.dto.MeasurementCheckerDto;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.util.KeyValue;

public class MeasurementCheckerView extends CheckerView<MeasurementCheckerDto> {
	private UpperCaseFieldBean basePartNoTextField;
	
	public MeasurementCheckerView(CheckerConfigModel configModel, MainWindow mainWindow) {
		super(configModel, mainWindow);
	}
	
	@Override
	public Pane createSearchText() {
		HBox operationSearchBox = this.createSearchTextField("Operation Name ");
		
		Pane pane = new Pane();
		VBox vb = new VBox();
		vb.setPadding(new Insets(10,0,0,0));
			
		vb.getChildren().addAll(operationSearchBox, createBasePartNumber(), createCheckerComboBox(), createSearchBtn());
		pane.getChildren().add(vb);
		loadCheckerComboBox(Checkers.getCheckers(CheckerType.Measurement), getKeyValue("", "ALL"));
		return pane;
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
		Set<MeasurementCheckerDto> measDetailSet = new LinkedHashSet<MeasurementCheckerDto>();
		List<MeasurementCheckerDto> measCheckerList = new ArrayList<MeasurementCheckerDto>();
		for (MeasurementCheckerDto measCheckerDto : configModel.loadMeasurementChecker(opName, basePartNo, checker)) {
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
		checkerDetailTablePane.setData(measCheckerList);
		addTableListener();
	}
	
	@Override
	public Pane createDetailsTablePane(){
		ColumnMappingList columnMappingList = ColumnMappingList.with("Operation Name", "operationName")
			.put("Part No","partNo")
			.put("Meas Seq", "measurementSeqNum")
			.put("Min", "minLimit")
			.put("Max", "maxLimit")
			.put("Check Point", "checkPoint")
			.put("Seq", "checkSeq")
			.put("Checker", "checker")
			.put("Reaction Type", "reactionType")
			.put("Item No", "partItemNo")
			.put("Sec Code", "partSectionCode")
			.put("Status", "status");
			
		Double[] columnWidths = new Double[] {
				0.10, 0.06,0.04,0.03,0.03,0.09,0.03,0.12,0.08,0.06,0.06,0.05
			};
		return this.createCheckerDetailTablePane(columnMappingList, columnWidths);
	}
	
	@Override
	public void editChecker(ActionEvent actionEvent) {
		MeasurementCheckerDto measCheckerDto = this.getSelectedCheckerForEdit();
		if(measCheckerDto == null) return;
		
		MeasurementViewEditDialog dialog = new MeasurementViewEditDialog("Edit", measCheckerDto, configModel, this);
		dialog.showDialog();
	}
	
	@Override
	public void deleteChecker(ActionEvent actionEvent) {
		try {
			String msgInfo = "Operation Name - ";
			List<MCMeasurementChecker> measCheckers = new ArrayList<MCMeasurementChecker>();
			List<MeasurementCheckerDto> checkerDtos = checkerDetailTablePane.getTable().getSelectionModel().getSelectedItems();
			if(checkerDtos != null && checkerDtos.size() == 0) {
				MessageDialog.showInfo(window.getStage(), "Please Select Checker");
				return;
			}
			for (MeasurementCheckerDto measCheckerDto : checkerDtos) {
				msgInfo  = msgInfo.concat(measCheckerDto.getOperationName() + " \n");
				measCheckers.addAll(configModel.findAllMeasurementCheckerBy(measCheckerDto));
			}
			
			if(measCheckers != null && MessageDialog.confirm(getMainWindow().getStage(), msgInfo +  "Do you wish to delete?")) {
				configModel.deleteMeasurementChecker(measCheckers);
				doSearch();
			}
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}
	
	@Override
	public void duplicateChecker(){
		MeasurementCheckerCopyDialog dialog = new MeasurementCheckerCopyDialog("Duplicate Measurement Checker", configModel, this);
		dialog.showDialog();
	}
	
	@Override
	public void addChecker(){
		MeasurementViewEditDialog dialog = new MeasurementViewEditDialog("Add", null, configModel, this);
		dialog.showDialog();
	}

	@Override
	public void searchTextChanged() {
		
	}
	
	public void autoCompleteSearch() {
		if(searchTextField != null) {
			List<String> operations = configModel.findAllOperation(searchTextField.getText().trim().toString());
			searchTextField.setSuggestionList(operations);
		}
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
}
