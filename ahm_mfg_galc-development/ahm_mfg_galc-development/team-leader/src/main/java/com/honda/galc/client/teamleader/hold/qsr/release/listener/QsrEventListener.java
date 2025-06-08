package com.honda.galc.client.teamleader.hold.qsr.release.listener;

import java.util.List;
import java.util.Map;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.teamleader.hold.qsr.event.QsrCreatedEvent;
import com.honda.galc.client.teamleader.hold.qsr.event.QsrUpdatedEvent;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.product.Qsr;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QsrEventListener</code> is ... .
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
public class QsrEventListener {

	private ReleasePanel parentPanel;

	public QsrEventListener(ReleasePanel parentPanel) {
		this.parentPanel = parentPanel;
		AnnotationProcessor.process(this);
	}

	@EventSubscriber()
	public void created(QsrCreatedEvent event) {
		Qsr qsr = event.getQsr();
		if (qsr == null) {
			return;
		}
		Division division = getParentPanel().getDivision();
		if (division == null || division.getDivisionId() == null) {
			return;
		}
		if (!division.getDivisionId().equals(qsr.getProcessLocation())) {
			return;
		}

		ProductType productType = (ProductType) getParentPanel().getInputPanel().getProductTypeComboBox().getSelectedItem();
		if (productType == null) {
			return;
		}
		if (!productType.name().equals(qsr.getProductType())) {
			return;
		}

		Qsr selectedQsr = (Qsr) getParentPanel().getInputPanel().getQsrComboBox().getSelectedItem();
		List<Map<String, Object>> data = getParentPanel().getProductPanel().getItems();

		getParentPanel().getInputPanel().getDepartmentComboBox().setSelectedIndex(-1);
		getParentPanel().getInputPanel().getDepartmentComboBox().setSelectedItem(division);
		getParentPanel().getInputPanel().getProductTypeComboBox().setSelectedItem(productType);

		if (selectedQsr == null) {
			return;
		}
		getParentPanel().getInputPanel().getQsrComboBox().setSelectedItem(selectedQsr);

		if (data == null || data.isEmpty()) {
			return;
		}
		if (selectedQsr.equals(getParentPanel().getInputPanel().getQsrComboBox().getSelectedItem())) {
			getParentPanel().getProductPanel().reloadData(data);
		}
	}

	@EventSubscriber
	public void updated(QsrUpdatedEvent event) {
		Qsr qsr = event.getQsr();
		if (qsr == null) {
			return;
		}
		Qsr selectedQsr = (Qsr) getParentPanel().getInputPanel().getQsrComboBox().getSelectedItem();
		if (selectedQsr == null || !selectedQsr.equals(qsr)) {
			return;
		}
		getParentPanel().getProductPanel().clearSelection();
		getParentPanel().getInputPanel().getCommandButton().doClick();
	}

	protected ReleasePanel getParentPanel() {
		return parentPanel;
	}
}
