package com.honda.galc.client.datacollection.processor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.check.PartResultData;

public class VQShipFrameVinCheckerProcessor extends VQShipFrameVinBaseProcessor {

	protected Device device;
	public ClientContext clientContext;

	public VQShipFrameVinCheckerProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
		this.clientContext = lotControlClientContext;
	}

	@Override
	public synchronized boolean execute(ProductId productId) {

		Logger.getLogger().debug("VQShipFrameVinCheckerProcessor : Enter execute method");
		try {
			Logger.getLogger().info(PROCESS_PRODUCT + productId);

			if (super.processFrameVin(productId)) {
				
				return super.execute(productId);
				
			}

		} catch (Exception e) {
			Logger.getLogger().error(e, e.getMessage());
			// make it work the same way as before, but when cause is null use meaningful message
			String errorMsg = (e.getCause() == null) ? e.getMessage() : e.getCause().toString(); 
			getController().getFsm().error(new Message("MSG01", errorMsg));
		}

		Logger.getLogger().debug("VQShipFrameVinCheckerProcessor : Exit execute method");
		return false;
	}

	@Override
	public boolean executeCheck(BaseProduct product, ProcessPoint processPoint) {

		List<String> checkResults = executeProductChecks(product, processPoint,getProductCheckPropertyBean().getProductNotProcessableCheckTypes());
		if (checkResults.size() > 0) {
			showErrorDialog(checkResults);
			handleException(product.getProductId() + " Failed Product Checks");
			
		} else {
			return true;
		}
		return false;
	}
	
	protected void showErrorDialog(List<String> checkResults) {
		final StringBuffer msg = new StringBuffer();
		msg.append(product.getProductId() + " failed the following Product Checks : \n");
		for (int i = 0; i < checkResults.size(); i++) {
			msg.append(checkResults.get(i));
			if (i != checkResults.size() - 1) {
				msg.append("\n");
			}
		}
		Logger.getLogger().info(msg.toString());
		ProductBean productBean = new ProductBean();
		productBean.setProductId(product.getProductId());

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showColoredMessageDialog(msg.toString(), Color.yellow, "Failed Product Checks");
			}
		});
	}

	protected List<String> executeProductChecks(BaseProduct frame, ProcessPoint processPoint,String[] productCheckTypes) {
		Map<String, Object> checkResults = ProductCheckUtil.check(frame, processPoint, productCheckTypes);
		List<String> productCheckTypesList = Arrays.asList(productCheckTypes);
		List<String> failedProductCheckList = new ArrayList<String>();
		for (String checkType : productCheckTypesList) {
			if (checkResults.get(checkType) != null) {
				if (checkResults.get(checkType) instanceof List && ((List) (checkResults.get(checkType))).size() > 0) {
					List<Object> resultList = (List<Object>) checkResults.get(checkType);
					String msg = "";
					for (Object obj : resultList) {
						if (obj instanceof String) {
							String s = (String) obj;
							if (s.trim().length() > 0)
								msg = msg + s + ",";
						}
						if (obj instanceof PartResultData) {
							PartResultData s = (PartResultData) obj;
							msg = msg + "\n" + s.part_Name.trim() + "-" + s.part_Reason + ",";
						}
					}
					failedProductCheckList.add(checkType + " : " + msg);
				} else if (checkResults.get(checkType) instanceof Boolean
						&& checkResults.get(checkType).equals(Boolean.TRUE)) {
					failedProductCheckList.add(checkType);
				} else if (checkResults.get(checkType) instanceof Map
						&& ((Map) (checkResults.get(checkType))).size() > 0) {
					Map<String, Object> resultsMap = (Map<String, Object>) checkResults.get(checkType);
					String msg = "";
					for (String key : resultsMap.keySet()) {
						if (resultsMap.get(key) instanceof Boolean && resultsMap.get(key).equals(Boolean.TRUE)) {
							if (key.trim().length() > 0)
								msg = msg + key + ",";
						}
					}
					failedProductCheckList.add(checkType + " : " + msg);
				}
			}
		}
		return failedProductCheckList;
	}

	protected void showColoredMessageDialog(String message, Color color, String title) {
		JTextArea commentTextArea = new JTextArea(message, 10, 50);
		commentTextArea.setBackground(color);
		commentTextArea.setFont(UiFactory.getInfo().getLabelFont());
		commentTextArea.setLineWrap(true);
		commentTextArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(commentTextArea);
		scrollPane.setBackground(color);

		JOptionPane.showMessageDialog(context.getFrame(), scrollPane, title, JOptionPane.ERROR_MESSAGE);
	}
}
