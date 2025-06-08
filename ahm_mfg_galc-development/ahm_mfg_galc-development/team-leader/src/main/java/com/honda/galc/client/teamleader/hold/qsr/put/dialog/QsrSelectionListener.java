package com.honda.galc.client.teamleader.hold.qsr.put.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QsrSelectionListener</code> is ...
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
 * <TD>Jan 27, 2010</TD>
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
public class QsrSelectionListener extends BaseListener<HoldProductPanel> implements ActionListener {

	private static final long serialVersionUID = 1L;

	private HoldDialog dialog;

	public QsrSelectionListener(HoldDialog dialog) {
		super(dialog.getParentPanel());
		this.dialog = dialog;

	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		Qsr qsr = (Qsr) getDialog().getInputPanel().getQsrInput().getSelectedItem();

		if (qsr == null) {
			getDialog().getFilteredProducts().clear();
			getDialog().getFilteredProducts().addAll(getDialog().getProducts());
			return;
		}

		String qsrDesc = qsr.getDescription();
		String qsrResponsibleDepartment = qsr.getResponsibleDepartment();

		String spindleNumber = "";
		String reason = "";

		int i = qsrDesc.indexOf("-");
		if (i < 0) {
			spindleNumber = "";
			reason = qsrDesc;
			
		} else if (i < 5) { // may have spindle number
			// Test to see if the first 1 to 5 characters are delimeted by a -
			// if so there may be a spindle number
			try {
				// if so, they are a spindle number
				// Test to see if the first 1 to 5 characters delimited by a -
				// are a number
				spindleNumber = qsrDesc.substring(0, i);
				Integer.parseInt(spindleNumber);
				reason = qsrDesc.substring(i + 1);
			} catch (NumberFormatException nfe) {
				spindleNumber = "";
				reason = qsrDesc;
			}
		} else {
			spindleNumber = "";
			reason = qsrDesc;
		}
		getDialog().getInputPanel().getSpindleInput().setText(spindleNumber);
		JTextComponent editor = (JTextComponent) getDialog().getInputPanel().getReasonInput().getEditor().getEditorComponent();
		editor.setText(reason);
		String trimmedReason = editor.getText();
		getDialog().getInputPanel().getReasonInput().setSelectedItem(trimmedReason);
		//Set data in UI department component
		if(!StringUtil.isNullOrEmpty(qsrResponsibleDepartment)) {
			Division division = ServiceFactory.getDao(DivisionDao.class).findByDivisionId(qsrResponsibleDepartment);
			getDialog().getInputPanel().getRespDptComboBox().getComponent().setSelectedItem(division);
		} else {
			getDialog().getInputPanel().getRespDptComboBox().getComponent().setSelectedIndex(-1);
		}
		
		filterProducts(qsr);

		int productCount = getDialog().getProducts().size();
		int filteredProductCount = getDialog().getFilteredProducts().size();
		int delta = productCount - filteredProductCount;

		if (filteredProductCount == 0) {
			JOptionPane.showMessageDialog(getDialog(), "All Products are already On Hold for selected QSR ", "No Product to Hold", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (filteredProductCount < productCount) {
			JOptionPane.showMessageDialog(getDialog(), delta + " products are already On Hold for selected QSR, will be skipped ", "Hold Products", JOptionPane.WARNING_MESSAGE);
		}
	}

	protected void filterProducts(Qsr qsr) {
		List<HoldResult> holdResults = getHoldResultDao().findAllByQsrId(qsr.getId());
		List<String> holdProductIds = new ArrayList<String>();
		for (HoldResult hr : holdResults) {
			holdProductIds.add(hr.getId().getProductId());
		}

		getDialog().getFilteredProducts().clear();
		getDialog().getFilteredProducts().addAll(getDialog().getProducts());

		Iterator<BaseProduct> it = getDialog().getFilteredProducts().iterator();

		while (it.hasNext()) {
			BaseProduct bp = it.next();
			if (holdProductIds.contains(bp.getId())) {
				it.remove();
			}
		}
	}

	public HoldDialog getDialog() {
		return dialog;
	}
}
