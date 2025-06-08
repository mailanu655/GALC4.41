package com.honda.galc.client.teamleader.hold.qsr.put.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.HoldAccessType;
import com.honda.galc.entity.product.Qsr;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HoldInputListener</code> is ...
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
 * <TD>Jan 7, 2010</TD>
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
public class HoldInputListener extends BaseListener<HoldProductPanel> implements ActionListener, DocumentListener {

	private HoldDialog dialog;
	protected final String NONE = "NONE";
	public HoldInputListener(HoldDialog dialog) {
		super(dialog.getParentPanel());
		this.dialog = dialog;
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {
		process();
	}

	@Override
	public void executeDocumentListener(DocumentEvent e) {
		process();
	}

	protected String getReason() {
		return (String) getDialog().getInputPanel().getReasonInput().getSelectedItem();
	}

	protected String getKickoutProcessPoint() {
		ProcessPoint processPoint = (ProcessPoint)getDialog().getInputPanel().getProcessPointComboBoxComponent().getSelectedItem();
		
		return processPoint == null? null:processPoint.getProcessPointId();
	}

	protected void process() {
		String reason = getReason();
		String name = getDialog().getInputPanel().getAssociateNameInput().getText();
		String phone = getDialog().getInputPanel().getPhoneInput().getText();
		HoldAccessType holdAccessType = (HoldAccessType) getDialog().getInputPanel().getHoldAccessTypeInput().getSelectedItem();
		boolean addToExisting = getDialog().getInputPanel().getAddToQsrInput().isSelected();
		String kickoutProcessPoint = getDialog().getInputPanel().getKickoutInput().isSelected()?getKickoutProcessPoint():NONE;
		Qsr qsr = (Qsr) getDialog().getInputPanel().getQsrInput().getSelectedItem();
		
		boolean submitEnabled = false;
		if ((!getDialog().getProperty().isInputAssociateInfo()&&!isEmpty(reason)&&!isEmpty(kickoutProcessPoint) ) ||
			(!isEmpty(reason) && !isEmpty(name) && !isEmpty(phone)&& !isEmpty(kickoutProcessPoint))) {
			if(holdAccessType != null)submitEnabled = true;
		}

		if (addToExisting && qsr == null ) {
			submitEnabled = false;
		}
		if (getDialog().getFilteredProducts().size() == 0) {
			submitEnabled = false;
		}
		
		if(addToExisting && (qsr != null && exceedsMaxAllowed(qsr))) {
			submitEnabled = false;
		}
			
		getDialog().getInputPanel().getSubmitButton().setEnabled(submitEnabled);
	}

	private boolean exceedsMaxAllowed(Qsr qsr) {
	
			int maxSize = getDialog().getProperty().getMaxQsrSize();
			long count = getHoldResultDao().countByQsr(qsr.getId());
			int productCount = getDialog().getFilteredProducts().size();
			
			if (maxSize > 0) {
				if ((count+productCount)  > maxSize) {
					String msg = "Qsr has "+count +" holds assigned. Adding Resultset: %s exceeds Qsr max hold size : %s, please create new QSR.";
					msg = String.format(msg, productCount, maxSize);
					this.getMainWindow().setErrorMessage(msg);
					return true;
				}
			}
			return false;
		
	}

	public HoldDialog getDialog() {
		return dialog;
	}
}