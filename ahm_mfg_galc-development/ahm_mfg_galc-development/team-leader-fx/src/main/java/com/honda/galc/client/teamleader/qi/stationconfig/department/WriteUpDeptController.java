package com.honda.galc.client.teamleader.qi.stationconfig.department;


import java.util.List;

import com.honda.galc.client.teamleader.qi.controller.QiDialogController;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiWriteUpDepartmentDto;

import javafx.event.ActionEvent;
/**
 * 
 * <h3>WriteUpDeptController Class description</h3>
 * <p>
 * WriteUpDeptController is used to load data in TableViews and perform the action on Done and Cancel buttons
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

public class WriteUpDeptController extends QiDialogController<EntryStationConfigModel, WriteUpDeptDialog>{
	
	private StationDepartmentPanel entryDeptAndWriteUpDeptView;
	public WriteUpDeptController(){
		
	}
	public WriteUpDeptController(EntryStationConfigModel model, WriteUpDeptDialog dialog) {
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
        if ("Done".equals(loggedButton.getText()))
		    	configureWriteUpDepartments(actionEvent);
        else if (QiConstant.CANCEL.equals(loggedButton.getText()))
        	getDialog().close();
		}
	}

	@Override
	public void initListeners() {
	}
	/**
	 * This method is used to configure write Up Departments values
	 */
	private void configureWriteUpDepartments(ActionEvent actionEvent) {
		List<QiWriteUpDepartmentDto> writeUpDepartments=getDialog().getAvailableWriteUpDepartObjectTablePane().getTable().getItems();
		for (LoggedComboBox<String> comboBox :getDialog().getColorComboBoxes()){
	        writeUpDepartments.get(Integer.parseInt(comboBox.getId().trim())).setColorCode(comboBox.getValue().toString());
	    }
		
		for (LoggedRadioButton button :getDialog().getRadioButtons()){
			if(button.isSelected()){
				getDialog().setFlag(true);
				writeUpDepartments.get(Integer.parseInt(button.getId().trim())).setIsDefault((short)1);
			}
		}
		getDialog().setAssignedWriteUpDepartments(writeUpDepartments);
		getDialog().close();
		}
	public StationDepartmentPanel getEntryDeptAndWriteUpDeptView() {
		return entryDeptAndWriteUpDeptView;
	}
	
}
