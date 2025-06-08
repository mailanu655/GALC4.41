package com.honda.galc.client.teamleader.hold.qsr.release.scrap;

import java.awt.event.ActionEvent;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.qics.InspectionPart;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ScrapInputListener</code> is ...
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 26, 2010</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ScrapInputListener extends BaseListener<ReleasePanel> implements DocumentListener, ListSelectionListener {
	private static final String DIVISION_SELECTED = "DIVISION_SELECTED";
	private static final String LINE_SELECTED = "LINE_SELECTED";
	private static final String PROCESS_POINT_SELECTED = "PROCESS_POINT_SELECTED";
	private static final String REASON_SELECTED = "REASON_SELECTED";

	private ScrapDialog dialog;

	public ScrapInputListener(ScrapDialog dialog) {
		super(dialog.getParentPanel());
		this.dialog = dialog;
	}

	public void execute() {

		InspectionPart otherPart = getDialog().getDefectSelectionPanel().getOtherPartPane().getSelectedItem();
		String name = getDialog().getAssociateNameInput().getText();
		String phone = getDialog().getPhoneInput().getText();
		String reason = (String) getDialog().getReasonInput().getSelectedItem();

		boolean inputValid = true;
		if (getDialog().getProperty().isInputAssociateInfo() ){
			if(otherPart == null || isEmpty(name) || isEmpty(phone)) inputValid = false;
		}

		if(isEmpty(getProcessPoint())) inputValid = false;

		if (inputValid && getDialog().isQsrCompleted()) {
			String comment = getDialog().getCommentInput().getText();
			inputValid = !isEmpty(comment);
		}
		if (inputValid && !getDialog().getProperty().isInputInspectionPart()) {
			inputValid = inputValid && !isEmpty(reason); 
		}

		getDialog().getSubmitButton().setEnabled(inputValid);
	}
	
	@Override
	public void executeActionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(DIVISION_SELECTED)) {
			if(getDialog().getDepartmentComboBoxComponent().getItemCount() > 0) execute();
		}else if (e.getActionCommand().equals(LINE_SELECTED)) {
			if(getDialog().getLineComboBoxComponent().getItemCount() > 0) execute();
		}else if (e.getActionCommand().equals(PROCESS_POINT_SELECTED)) {
			if(getDialog().getProcessPointComboBoxComponent().getItemCount() > 0) execute();
		}else if (e.getActionCommand().equals(REASON_SELECTED)) execute();
	}

	public void changedUpdate(DocumentEvent e) {
		execute();
	}

	public void insertUpdate(DocumentEvent e) {
		execute();
	}

	public void removeUpdate(DocumentEvent e) {
		execute();
	}

	public void valueChanged(ListSelectionEvent e) {
		execute();
	}
	
	protected String getProcessPoint() {
		ProcessPoint processPoint = (ProcessPoint)getDialog().getProcessPointPanel().getProcessPointComboBox().getComponent().getSelectedItem();
		
		return processPoint == null? null:processPoint.getProcessPointId();
	}

	public ScrapDialog getDialog() {
		return dialog;
	}
}
