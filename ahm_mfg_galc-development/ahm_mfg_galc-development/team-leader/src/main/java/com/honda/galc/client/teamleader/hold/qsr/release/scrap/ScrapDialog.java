package com.honda.galc.client.teamleader.hold.qsr.release.scrap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;

import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.client.teamleader.hold.qsr.release.defect.DefectDialog;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.HoldResult;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ScrapDialog</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
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
public class ScrapDialog extends DefectDialog {

	private static final long serialVersionUID = 1L;

	public ScrapDialog(ReleasePanel parentPanel, String title, List<HoldResult> qsrHoldResults, List<HoldResult> holdResults, Map<String, Object> selectionCache) {
		super(parentPanel, title, qsrHoldResults, holdResults, selectionCache);
	}

	@Override
	protected void initView() {
		super.initView();
		getAssociateIdInput().setText(getMainWindow().getUserId().trim());
		getAssociateNameInput().setEnabled(true);
		getPhoneInput().setEnabled(true);
		getCommentInput().setEnabled(isQsrCompleted());
		if(!getProcessPointPanel().isReasonsFromProperties() ) {
			getReasonInput().setVisible(true);
			getReasonLabel().setVisible(true);
		}
	}

	@Override
	protected void mapActions() {

		ScrapInputListener inputListener = new ScrapInputListener(this);
		getReasonInput().addActionListener(inputListener);
		getDefectSelectionPanel().getOtherPartPane().getTable().getSelectionModel().addListSelectionListener(inputListener);
		getAssociateNameInput().getDocument().addDocumentListener(inputListener);
		getPhoneInput().getDocument().addDocumentListener(inputListener);
		getCommentInput().getDocument().addDocumentListener(inputListener);
		getProcessPointPanel().getProcessPointComboBox().getComponent().addActionListener(inputListener);

		getSubmitButton().addActionListener(new ScrapAction(this));
		
		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		getProcessPointPanel().getDepartmentComboBox().getComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( getProcessPointPanel().getLineComboBox().getComponent().getSelectedItem() == null) {
					getProcessPointPanel().getProcessPointComboBox().getComponent().setModel(new DefaultComboBoxModel());
				}
				getQSRReason().populateReasonsByDiv(getQCAction());				

			}
		});
		
		getProcessPointPanel().getLineComboBox().getComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getQSRReason().populateReasonsByDivAndLine(getQCAction());				
			}
		});
		
		getProcessPointPanel().getProcessPointComboBox().getComponent().addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent ie) {
			if (ItemEvent.DESELECTED == ie.getStateChange()) {
				getDefectSelectionPanel().setProcessPoint(null);
			} else if (ItemEvent.SELECTED == ie.getStateChange()) {
				ProcessPoint processPoint = (ProcessPoint) getProcessPointPanel().getProcessPointComboBox().getComponent().getSelectedItem();
				getDefectSelectionPanel().setProcessPoint(processPoint);
			}				
		}
	});
	}

	@Override
	protected void initModel() {
		if (getParentPanel().getCachedHoldResultInput() != null) {
			String name = getParentPanel().getCachedHoldResultInput().getReleaseAssociateName();
			String phone = getParentPanel().getCachedHoldResultInput().getReleaseAssociatePhone();
			name = name == null ? "" : name.trim();
			phone = phone == null ? "" : phone.trim();
			getAssociateNameInput().setText(name);
			getPhoneInput().setText(phone);
		}
	}	

	public boolean isQsrCompleted() {

		List<HoldResult> list = getQsrHoldResults();
		int releasedCtr = 0;
		for (HoldResult result : list) {
			if (result.getReleaseFlag() == 1) {
				releasedCtr++;
			}
		}
		return (getHoldResults().size() + releasedCtr) == getQsrHoldResults().size();
	}
}
