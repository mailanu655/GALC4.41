package com.honda.galc.client.teamleader.qi.stationconfig.previousdefect;


import java.util.List;

import com.honda.galc.client.teamleader.qi.controller.QiDialogController;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiEntryDepartmentDto;

import javafx.event.ActionEvent;
/**
 * 
 * <h3>PreviousDefectVisibleController Class description</h3>
 * <p>
 * PreviousDefectVisibleController is used to load data in TableViews and perform the action on Done and Cancel buttons
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

public class PreviousDefectVisibleController extends QiDialogController<EntryStationConfigModel, PreviousDefectVisibleDialog>{

	public PreviousDefectVisibleController(){

	}
	public PreviousDefectVisibleController(EntryStationConfigModel model, PreviousDefectVisibleDialog dialog) {
		super();
		setModel(model);
		setDialog(dialog);
	}
	/**
	 * This method is used to handle action events .
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();	
			clearDisplayMessage();
			if (QiConstant.DONE.equals(loggedButton.getText())) {
				configureEntryDepartments(actionEvent);
			} else if (QiConstant.CANCEL.equals(loggedButton.getText())) {
				getDialog().close();
			}
		}
	}

	@Override
	public void initListeners() {
	}
	/**
	 * This method is used to configure entry Departments values
	 */
	private void configureEntryDepartments(ActionEvent actionEvent) {
		List<QiEntryDepartmentDto> entryDepartments=getDialog().getAvailableEntryDepartObjectTablePane().getTable().getItems();
		for (LoggedComboBox<String> comboBox :getDialog().getOriginalDefectComboBoxes()){
			entryDepartments.get(Integer.parseInt(comboBox.getId().trim())).setOriginalDefectStatus(comboBox.getValue().toString());
		}
		for (LoggedComboBox<String> comboBox :getDialog().getCurrentDefectComboBoxes()){
			entryDepartments.get(Integer.parseInt(comboBox.getId().trim())).setCurrentDefectStatus(comboBox.getValue().toString());
		}
		getDialog().setAssignedEntryDepartments(entryDepartments);
		getDialog().close();
	}

}
