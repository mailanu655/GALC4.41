package com.honda.galc.client.teamleader.hold.qsr.put.process.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.put.process.ProcessPanel;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.util.PropertyComparator;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductNumberListener</code> is ...
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
public class ProductNumberListener extends BaseListener<ProcessPanel> implements
		ActionListener {

	private JTextField productInput;
	private JFormattedTextField timeInput;

	public ProductNumberListener(ProcessPanel parentPanel,
			JTextField productInput, JFormattedTextField timeInput) {
		super(parentPanel);
		this.productInput = productInput;
		this.timeInput = timeInput;
	}

	public void executeActionPerformed(ActionEvent e) {

		ProcessPoint processPoint = (ProcessPoint) getView().getInputPanel()
				.getProcessPointComboBox().getSelectedItem();
		Division division = getView().getDivision();

		if (division == null || processPoint == null) {
			return;
		}
		String inputNumber = getProductInput().getText();
		if (inputNumber == null || inputNumber.trim().length() == 0) {
			return;
		}
		inputNumber = inputNumber.trim();


			ProductType productType = getView().getProductType();

			String productName = productType.getProductName();

		try {
			
			TextFieldState.DEFAULT.setState( (JTextField) e.getSource() );
			
			BaseProduct product = getProductDao(productType).findBySn(inputNumber);			

			if (product == null) {
				String msg = String.format("%s does not exist for number: %s",
						productName, inputNumber);
				JOptionPane.showMessageDialog(getView(), msg);
				return;
			}

			List<? extends ProductHistory> historyList = getProductHistoryDao(
					productType).findAllByProductAndProcessPoint(
					product.getProductId(), processPoint.getProcessPointId());

			if (historyList == null || historyList.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				sb.append(productName).append(" History does not exist for ")
						.append(productName).append(", ProductId:")
						.append(product.getProductId());
				JOptionPane.showMessageDialog(getView(), sb);
				return;
			}

			ProductHistory productHistory = getLastest(historyList);
			Date time = productHistory.getActualTimestamp();
			getTimeInput().setValue(time);
			getProductInput().setText(inputNumber);
		} catch (ServiceInvocationException ex) {
			Logger.getLogger( this.getView().getApplicationId() ).error(Arrays.toString(ex.getStackTrace()));
			TextFieldState.ERROR.setState( (JTextField) e.getSource() );
			getView().getMainWindow().setErrorMessage("Product does not exist in database.");
		}
	}

	protected ProductHistory getLastest(
			List<? extends ProductHistory> historyList) {
		if (historyList == null || historyList.isEmpty()) {
			return null;
		}
		if (historyList.size() == 1) {
			return historyList.get(0);
		}
		Collections.sort(historyList, new PropertyComparator<ProductHistory>(
				ProductHistory.class, "actualTimestamp"));
		return historyList.get(historyList.size() - 1);
	}

	protected JTextField getProductInput() {
		return productInput;
	}

	protected JFormattedTextField getTimeInput() {
		return timeInput;
	}
}
