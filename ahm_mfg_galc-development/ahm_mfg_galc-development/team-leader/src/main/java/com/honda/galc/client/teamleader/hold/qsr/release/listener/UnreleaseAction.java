package com.honda.galc.client.teamleader.hold.qsr.release.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import com.honda.galc.client.teamleader.hold.HoldUtils;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.QsrAction;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>UnreleaseAction</code> is ... .
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
public class UnreleaseAction extends QsrAction<ReleasePanel> implements ActionListener {

	public UnreleaseAction(ReleasePanel panel) {
		super(panel);
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		List<Map<String, Object>> selectedItems = getView().getProductPanel().getSelectedItems();

		if (selectedItems == null || selectedItems.isEmpty()) {
			return;
		}

		List<HoldResult> selectedHoldResults = HoldUtils.extractHoldResults(selectedItems);

		Map<String,HoldResult> releasedHoldResults = new HashMap<String,HoldResult>();
		List<HoldResult> activeHoldResults = new ArrayList<HoldResult>();

		for (HoldResult result : selectedHoldResults) {
			if (result.getReleaseFlag() == 1) {
				releasedHoldResults.put(result.getId().getProductId(), result);
			} else {
				activeHoldResults.add(result);
			}
		}
		
		ArrayList<String> shippedProductIds = this.getShippedProductIds(new ArrayList<String>(releasedHoldResults.keySet()));
		
		StringBuilder message = new StringBuilder();
		
		if (!activeHoldResults.isEmpty() || !shippedProductIds.isEmpty()) {
			message.append("Records for the following products could not be processed:\n");
			for (HoldResult activeHoldResult : activeHoldResults) {
				message.append(activeHoldResult.getId().getProductId() + " - on hold\n");
			}
			
			for (String shippedProductId : shippedProductIds) {
				releasedHoldResults.remove(shippedProductId);
				message.append(shippedProductId + " - shipped\n");
			}
			message.append("\n");
		}
		
		int maxCount = Config.getProperty().getMaxReleaseBatchSize();
		
		if (releasedHoldResults.isEmpty()) {
			message.insert(0, "Nothing to unrelease!\n\n");
		} else if (releasedHoldResults.size() > maxCount) {
			String msg = 	"The number of hold records to be unreleased [" + releasedHoldResults.size() + "]\n " +
							"exceeds maximum allowed [" + maxCount + "]";
			JOptionPane.showMessageDialog(getView(), msg, "Transaction Failed", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (message.length() > 0) {
			showScrollDialog(message.toString());
		}
		
		
		if (!releasedHoldResults.isEmpty()) unrelease(new ArrayList<HoldResult>(releasedHoldResults.values()));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	private ArrayList<String> getShippedProductIds(List<String> productIdList){
		ArrayList<String> shippedProductIds = new ArrayList<String>();
		ProductDao dao = ProductTypeUtil.getProductDao(getView().getProductType());
		List<BaseProduct> productList = dao.findProducts(productIdList, 0, productIdList.size());
		for (BaseProduct product : productList) {
			if (getShipLines().contains(product.getTrackingStatus().trim())) 
				shippedProductIds.add(product.getProductId());
		}
		return shippedProductIds;
	}

	public void unrelease(List<HoldResult> holdResults) {

		int productCount = holdResults.size();
		StringBuilder msg = new StringBuilder();
		msg.append("Are you sure you want unrelease ").append(productCount).append(productCount == 1 ? " released Hold" : " released Holds");
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Unrelease released Holds", JOptionPane.YES_NO_OPTION);

		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}

		Qsr qsr = (Qsr) getView().getInputPanel().getQsrComboBox().getSelectedItem();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		for (HoldResult hr : holdResults) {
			hr.setReleaseAssociateNo(null);
			hr.setReleaseAssociateName(null);
			hr.setReleaseAssociatePager(null);
			hr.setReleaseAssociatePhone(null);
			hr.setReleaseFlag((short) 0);
			hr.setReleaseReason(null);
			hr.setReleaseTimestamp(null);
			hr.setUpdateTimestamp(timestamp);
		}

		try {
			Division division = getView().getDivision();
			ProductType productType = getView().getProductType();
			getQsrDao().updateHoldResults(productType, holdResults, null);

			Qsr qsrResult = getQsrDao().findByKey(qsr.getId());

			msg = new StringBuilder();
			msg.append("Request processed succesfully, QSR Request Code : ").append(qsrResult.getName());
			msg.append("\n" + holdResults.size()).append(holdResults.size() == 1 ? " released Hold is unreleased" : " released Holds are unreleased");
			QsrStatus status = QsrStatus.getByIntValue(qsrResult.getStatus());
			if (status != null) {
				msg.append("\nQSR Status - ").append(status.getLabel());
			}

			JOptionPane.showMessageDialog(getView(), msg);

			getView().setCachedQsr(qsrResult);

			getView().getInputPanel().getDepartmentComboBox().setSelectedIndex(-1);
			getView().getInputPanel().getDepartmentComboBox().setSelectedItem(division);
			getView().getInputPanel().getProductTypeComboBox().setSelectedItem(productType);
			getView().getInputPanel().getQsrComboBox().setSelectedItem(qsrResult);

		} finally {
			getView().getInputPanel().getCommandButton().doClick();
		}
	}
}