package com.honda.galc.client.teamleader.hold.qsr.put.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.HoldUtils;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.client.teamleader.hold.qsr.put.dialog.HoldDialog;
import com.honda.galc.client.teamleader.hold.qsr.put.dialog.MassScrapDialog;
import com.honda.galc.client.teamleader.hold.qsr.release.scrap.ScrapDialog;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.Qsr;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PopupHoldDialogAction</code> is ...
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
public class PopupMassScrapDialogAction extends BaseListener<HoldProductPanel> implements ActionListener {

	public PopupMassScrapDialogAction(HoldProductPanel panel) {
		super(panel);
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		List<Map<String, Object>> list = getView().getProductPanel().getSelectedItems();
		int maxCount = Config.getProperty().getMaxReleaseBatchSize();

		StringBuilder message = new StringBuilder();
		if (list.size() > maxCount) {
			message.append("The total number of Products to scrap exceeds maximum allowed ").append(maxCount);
			JOptionPane.showMessageDialog(getView(), message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		List<BaseProduct> products = extractProducts(list);
		MassScrapDialog dialog = new MassScrapDialog(getView(), "Scrap Products", products);

		String modelCode = ((BaseProduct) products.get(0)).getModelCode();
		dialog.getDefectSelectionPanel().setModelCode(modelCode);
		dialog.setLocationRelativeTo(getView().getMainWindow());
		dialog.setVisible(true);

	}
	
	protected List<BaseProduct> extractProducts(List<Map<String, Object>> list) {
		List<BaseProduct> products = new ArrayList<BaseProduct>();
		if (list == null || list.isEmpty()) {
			return products;
		}
		for (Map<String, Object> o : list) {
			BaseProduct p = (BaseProduct) o.get("product");
			products.add(p);
		}
		return products;
	}

	protected List<Qsr> selectQsrs() {
		
		Division division = getView().getDivision();
		ProductType productType = getView().getProductType();
		if (division == null || productType == null) {
			return new ArrayList<Qsr>();
		}
		List<Qsr> list = getQsrDao().findAll(division.getDivisionId(), productType.name(), QsrStatus.ACTIVE.getIntValue());
		if (list == null) {
			return new ArrayList<Qsr>();
		}
		Comparator<Qsr> c = new Comparator<Qsr>() {
			public int compare(Qsr o1, Qsr o2) {
				return -o1.getId().compareTo(o2.getId());
			}
		};
		Collections.sort(list, c);
		return list;
	}

	
}
