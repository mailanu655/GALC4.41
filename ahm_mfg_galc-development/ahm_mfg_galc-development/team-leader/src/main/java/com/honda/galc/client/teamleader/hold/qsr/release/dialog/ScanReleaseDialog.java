package com.honda.galc.client.teamleader.hold.qsr.release.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.entity.product.HoldResult;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ScanReleaseDialog</code> is ...
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
 * <TD>Jan 15, 2010</TD>
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
public class ScanReleaseDialog extends ReleaseDialog {

	private static final long serialVersionUID = 1L;

	public ScanReleaseDialog(ReleasePanel parentPanel, String title, List<HoldResult> holdResults, List<HoldResult> qsrHoldResults) {
		super(parentPanel, "Release Holds", holdResults, qsrHoldResults);
	}

	@Override
	protected void initModel() {

		initProcessPointSelection();
		getAssociateIdInput().setText(getParentPanel().getMainWindow().getUserId().trim());
		getCommentInput().setEnabled(true);

		if (getParentPanel().getCachedQsr() != null) {
			String approver = getParentPanel().getCachedQsr().getApproverName();
			if (approver != null && approver.trim().length() > 0) {
				getApproverInput().setText(approver.trim());
			}
		}

		if (getParentPanel().getCachedHoldResultInput() != null) {
			String reason = getParentPanel().getCachedHoldResultInput().getReleaseReason();
			String name = getParentPanel().getCachedHoldResultInput().getReleaseAssociateName();
			String phone = getParentPanel().getCachedHoldResultInput().getReleaseAssociatePhone();

			getReasonInput().setSelectedItem(StringUtils.trimToEmpty(reason));
			getAssociateNameInput().setText(StringUtils.trimToEmpty(name));
			getPhoneInput().setText(StringUtils.trimToEmpty(phone));
		}
	}
	
	@Override
	protected void mapActions() {

		ReleaseInputListener inputListener = new ReleaseInputListener(this);

		getApproverInput().getDocument().addDocumentListener(inputListener);
		getAssociateNameInput().getDocument().addDocumentListener(inputListener);
		getPhoneInput().getDocument().addDocumentListener(inputListener);
		getReasonInput().addActionListener(inputListener);

		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		getSubmitButton().addActionListener(new ScanReleaseAction(this));
		getReasonInput().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object reason = getReasonInput().getSelectedItem();
				if (APPROVED_TO_SHIP.equals(reason)) {
					getApproverInput().setEnabled(true);
				} else {
					getApproverInput().setEnabled(false);
				}
			}
		});
		
		getDepartmentComboBoxComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( getDepartmentComboBoxComponent().getSelectedItem() == null) {
					getDepartmentComboBoxComponent().setModel(new DefaultComboBoxModel());
				}
				getQSRReason().populateReasonsByDiv(getQCAction());				
			}
		});
		
		getProcessPointPanel().getLineComboBox().getComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getQSRReason().populateReasonsByDivAndLine(getQCAction());				
			}
		});
	}
}
