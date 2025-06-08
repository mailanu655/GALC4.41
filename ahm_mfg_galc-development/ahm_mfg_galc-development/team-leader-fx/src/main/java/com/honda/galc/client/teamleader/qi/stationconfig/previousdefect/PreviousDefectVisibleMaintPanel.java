package com.honda.galc.client.teamleader.qi.stationconfig.previousdefect;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigPanel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiEntryDepartmentDto;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.stage.Screen;

/**
 * 
 * <h3>PreviousDefectVisibleMaintPanel Class description</h3>
 * <p>
 * PreviousDefectVisibleMaintPanel is used to load data in TableViews and perform the action on Update , Reset and Shift buttonsetc.
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 * 
 */
public class PreviousDefectVisibleMaintPanel {

	private PreviousDefectVisibleMaintController controller;
	private ObjectTablePane<QiEntryDepartmentDto> currentlyAssignedEntryDepartmentDefectTablePane;
	private ObjectTablePane<QiEntryDepartmentDto> availableEntryDepartmentDefectTablePane;
	private LoggedButton rightShiftEntryDepartment;
	private LoggedButton leftShiftEntryDepartment;
	private LoggedButton updateEntryDepartmenDefectStatustBtn;
	private LoggedButton resetEntryDepartmenDefectStatustBtn;

	public PreviousDefectVisibleMaintPanel(EntryStationConfigModel model,EntryStationConfigPanel view) {
		controller=new PreviousDefectVisibleMaintController(model, view);
	}
	/**
	 * This method used to load data on panel
	 */
	public void reload() {
		controller.loadInitialData();
	}
	public MigPane getEntryDepartmentDefectPanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[left]10[center]10[left]", "[]10[]10[]");
		LoggedLabel availableLabel= UiFactory.createLabel("available", "Available");
		availableLabel.getStyleClass().add("display-label-14");
		LoggedLabel currentlyAssignedLabel= UiFactory.createLabel("currentlyAssigned", "Currently Assigned");
		currentlyAssignedLabel.getStyleClass().add("display-label-14");
		availableEntryDepartmentDefectTablePane=createEntryDepartmentDefectTablePane("Available");
		availableEntryDepartmentDefectTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		currentlyAssignedEntryDepartmentDefectTablePane=createEntryDepartmentDefectTablePane("Assigned");
		currentlyAssignedEntryDepartmentDefectTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		rightShiftEntryDepartment= createBtn(">",getController());
		rightShiftEntryDepartment.setId(StationConfigurationOperations.RIGHT_SHIFT_ENTRY_DEPARTMENT_DEFECT_STATUS.getName());
		rightShiftEntryDepartment.setDisable(true);
		leftShiftEntryDepartment= createBtn("<",getController());
		leftShiftEntryDepartment.setId(StationConfigurationOperations.LEFT_SHIFT_ENTRY_DEPARTMENT_DEFECT_STATUS.getName());
		leftShiftEntryDepartment.setDisable(true);
		updateEntryDepartmenDefectStatustBtn = createBtn("Update", getController());
		updateEntryDepartmenDefectStatustBtn.setId(StationConfigurationOperations.UPDATE_ENTRY_DEPARTMENT_DEFECT_BUTTON.getName());
		updateEntryDepartmenDefectStatustBtn.setDisable(true);
		resetEntryDepartmenDefectStatustBtn = createBtn("Reset",getController());
		resetEntryDepartmenDefectStatustBtn.setId(StationConfigurationOperations.RESET_ENTRY_DEPARTMENT_DEFECT_BUTTON.getName());
		resetEntryDepartmenDefectStatustBtn.setDisable(true);
		pane.add(availableLabel,"cell 0 0");
		pane.add(currentlyAssignedLabel,"cell 2 0");
		pane.add(availableEntryDepartmentDefectTablePane,"cell 0 1");
		pane.add(rightShiftEntryDepartment,"cell 1 1,center,split 2,flowy");
		pane.add(leftShiftEntryDepartment);
		pane.add(currentlyAssignedEntryDepartmentDefectTablePane,"cell 2 1");
		pane.add(updateEntryDepartmenDefectStatustBtn,"cell 0 2,right");
		pane.add(resetEntryDepartmenDefectStatustBtn,"cell 2 2");
		return pane;
	}
	/**
	 * This method is used to create available EntryDept and currently assigned Entry Dept table view 
	 * @param department
	 * @return
	 */
	private ObjectTablePane<QiEntryDepartmentDto> createEntryDepartmentDefectTablePane(String department){ 
		ColumnMappingList columnMappingList=null;
		Double[] columnWidth=null;
		ObjectTablePane<QiEntryDepartmentDto> panel;
		columnMappingList = ColumnMappingList.with("Entry Dept", "divisionId").put("Dept Name", "divisionName");
		columnWidth = new Double[] {0.2,0.2};
		if(!department.equalsIgnoreCase("Available")){
			columnMappingList.put("Origianl Defect Status","originalDefectStatus").put("Current Defect Status", "currentDefectStatus");
			columnWidth = new Double[] {0.1,0.1,0.1,0.1};
		}
		panel = new ObjectTablePane<QiEntryDepartmentDto>(columnMappingList,columnWidth);
		panel.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth()*0.4, Screen.getPrimary().getVisualBounds().getHeight()*0.3);
		return panel;	
	}

	/**
	 * This method is used to create Button.
	 * @param text
	 * @param handler
	 * @return
	 */
	public static LoggedButton createBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("main-screen-btn");
		btn.setStyle("-fx-font-size: 14px;");
		return btn;
	}
	public PreviousDefectVisibleMaintController getController() {
		return controller;
	}
	public void setController(PreviousDefectVisibleMaintController controller) {
		this.controller = controller;
	}
	public ObjectTablePane<QiEntryDepartmentDto> getCurrentlyAssignedEntryDepartmentDefectTablePane() {
		return currentlyAssignedEntryDepartmentDefectTablePane;
	}
	public void setCurrentlyAssignedEntryDepartmentDefectTablePane(
			ObjectTablePane<QiEntryDepartmentDto> currentlyAssignedEntryDepartmentDefectTablePane) {
		this.currentlyAssignedEntryDepartmentDefectTablePane = currentlyAssignedEntryDepartmentDefectTablePane;
	}
	public ObjectTablePane<QiEntryDepartmentDto> getAvailableEntryDepartmentDefectTablePane() {
		return availableEntryDepartmentDefectTablePane;
	}
	public void setAvailableEntryDepartmentDefectTablePane(
			ObjectTablePane<QiEntryDepartmentDto> availableEntryDepartmentDefectTablePane) {
		this.availableEntryDepartmentDefectTablePane = availableEntryDepartmentDefectTablePane;
	}
	public LoggedButton getUpdateEntryDepartmenDefectStatustBtn() {
		return updateEntryDepartmenDefectStatustBtn;
	}
	public void setUpdateEntryDepartmenDefectStatustBtn(LoggedButton updateEntryDepartmenDefectStatustBtn) {
		this.updateEntryDepartmenDefectStatustBtn = updateEntryDepartmenDefectStatustBtn;
	}
	public LoggedButton getResetEntryDepartmenDefectStatustBtn() {
		return resetEntryDepartmenDefectStatustBtn;
	}
	public void setResetEntryDepartmenDefectStatustBtn(LoggedButton resetEntryDepartmenDefectStatustBtn) {
		this.resetEntryDepartmenDefectStatustBtn = resetEntryDepartmenDefectStatustBtn;
	}

	public LoggedButton getRightShiftEntryDepartment() {
		return rightShiftEntryDepartment;
	}
	public LoggedButton getLeftShiftEntryDepartment() {
		return leftShiftEntryDepartment;
	}


}
