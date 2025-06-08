package com.honda.galc.client.teamlead.checker;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckerType;
import com.honda.galc.checkers.Checkers;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.dto.OperationCheckerDto;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.util.KeyValue;

public class OperationCheckerView extends CheckerView<OperationCheckerDto> {
	
	public OperationCheckerView(CheckerConfigModel configModel, MainWindow mainWindow) {
		super(configModel, mainWindow);
	}
	
	@Override
	public Pane createSearchText() {
		VBox vb = new VBox();
		vb.setPadding(new Insets(10,0,0,0));
		vb.getChildren().addAll(createSearchTextField("Operation Name "), createCheckerComboBox(), createSearchBtn());
		loadCheckerComboBox(Checkers.getCheckers(CheckerType.Operation), getKeyValue("", "ALL"));
		return vb;
	}
	
	@SuppressWarnings("unchecked")
	public void doSearch() {
		String opName = "";
		String checker = "";
		if(checkerComboBox != null && checkerComboBox.getSelectionModel().getSelectedItem() != null) {
			KeyValue<String, String> newValue = (KeyValue<String, String>) checkerComboBox.getSelectionModel().getSelectedItem();
			checker = newValue.getKey().toString();
		}
		if(!StringUtils.isBlank(searchTextField.getText()))
			opName = searchTextField.getText().toString();
		checkerDetailTablePane.getTable().getItems().clear();
		List<OperationCheckerDto> opCheckerList = new ArrayList<OperationCheckerDto>();
		Set<OperationCheckerDto> opDetailSet = new LinkedHashSet<OperationCheckerDto>();
		for (OperationCheckerDto opCheckerDto : configModel.loadOperationChecker(opName, checker)) {
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
		checkerDetailTablePane.setData(opCheckerList);
		addTableListener();
	}
	
	@Override
	public Pane createDetailsTablePane(){
		ColumnMappingList columnMappingList = ColumnMappingList.with("Operation Name", "operationName")
				.put("Check Point","checkPoint")
				.put("Check Seq", "checkSeq")
				.put("Checker", "checker")
				.put("Reaction Type", "reactionType")
				.put("Status", "status");
				
		Double[] columnWidths = new Double[] {
				0.10, 0.12,0.05,0.19,0.16,0.05,0.10
		};
		return this.createCheckerDetailTablePane(columnMappingList, columnWidths);
	}
	
	@Override
	public void editChecker(ActionEvent actionEvent) {
		OperationCheckerDto operationCheckerDto = this.getSelectedCheckerForEdit();
		if(operationCheckerDto == null) return;
		
		OperationViewEditDialog dialog = new OperationViewEditDialog("Edit", operationCheckerDto, configModel, this);
		dialog.showDialog();
	}
	
	@Override
	public void deleteChecker(ActionEvent actionEvent) {
		try {
			String msgInfo = "Operation Name - ";
			List<MCOperationChecker> operationCheckers = new ArrayList<MCOperationChecker>();
			List<OperationCheckerDto> checkerDtos = checkerDetailTablePane.getTable().getSelectionModel().getSelectedItems();
			if(checkerDtos != null && checkerDtos.size() == 0) {
				MessageDialog.showInfo(window.getStage(), "Please Select Checker");
				return;
			}
			for (OperationCheckerDto opCheckerDto : checkerDtos) {
				msgInfo  = msgInfo.concat(opCheckerDto.getOperationName() + " \n");
			}
			
			if(operationCheckers != null && MessageDialog.confirm(getMainWindow().getStage(), msgInfo +  "Do you wish to delete?")) {
				for (OperationCheckerDto opCheckerDto : checkerDtos) {
					configModel.deleteOpCheckerByOperationNameCheckpointAndSeq(opCheckerDto.getOperationName(), 
							opCheckerDto.getCheckPoint(), opCheckerDto.getCheckSeq());
				}
				doSearch();
			}
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}
	
	@Override
	public void duplicateChecker(){
		OperationCheckerCopyDialog dialog = new OperationCheckerCopyDialog("Duplicate Operation Checker", configModel, this);
		dialog.showDialog();
	}
	
	@Override
	public void addChecker(){
		OperationViewEditDialog dialog = new OperationViewEditDialog("Add", null, configModel, this);
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
}
