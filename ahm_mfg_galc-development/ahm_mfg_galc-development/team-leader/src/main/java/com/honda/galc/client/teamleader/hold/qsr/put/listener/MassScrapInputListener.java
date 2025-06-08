package com.honda.galc.client.teamleader.hold.qsr.put.listener;

import java.awt.event.ActionEvent;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.HoldPanel;	
import com.honda.galc.client.teamleader.hold.qsr.put.dialog.MassScrapDialog;
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
public class MassScrapInputListener extends BaseListener<HoldPanel> implements DocumentListener, ListSelectionListener {

	private MassScrapDialog dialog;

	public MassScrapInputListener(MassScrapDialog dialog) {
		super(dialog.getParentPanel());
		this.dialog = dialog;
	}

	protected String getReason() {
		return (String) getDialog().getReasonInput().getSelectedItem();
	}
	
	protected String getProcessPoint() {
		ProcessPoint processPoint = (ProcessPoint)getDialog().getProcessPointPanel().getProcessPointComboBox().getComponent().getSelectedItem();
		
		return processPoint == null? null:processPoint.getProcessPointId();
	}


	public void execute() {

		InspectionPart otherPart = getDialog().getDefectSelectionPanel().getOtherPartPane().getSelectedItem();

		String reason = (String) getDialog().getReasonInput().getSelectedItem();
		String name = getDialog().getAssociateNameInput().getText();
		String phone = getDialog().getPhoneInput().getText();

		String processPoint = getProcessPoint();

		boolean inputValid = true;
		if (isEmpty(processPoint))  inputValid = false;

		if ( getDialog().getProperty().isInputInspectionPart() )  {
			if (otherPart == null)  inputValid = false;
		} else if(isEmpty(reason)) inputValid = false;

		if (getDialog().getProperty().isInputAssociateInfo() ){
			if(isEmpty(name) || isEmpty(phone) ) inputValid = false;
		}

		getDialog().getSubmitButton().setEnabled(inputValid);
	}
	
	@Override
	public void executeActionPerformed(ActionEvent e) {
		execute();
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

	public MassScrapDialog getDialog() {
		return dialog;
	}
}
