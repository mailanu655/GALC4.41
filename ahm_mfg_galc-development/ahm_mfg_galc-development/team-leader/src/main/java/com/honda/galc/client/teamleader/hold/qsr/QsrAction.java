package com.honda.galc.client.teamleader.hold.qsr;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

public abstract class QsrAction<T extends JPanel> extends BaseListener<T>{
	protected Boolean allHoldsReleased = true;
	static final String PRODUCT_TAG = "product";
	static final String OWNER_TAG = "owner";
	static final String SHIP_TAG = "ship";
	static final String HOLD_RESULT_TAG = "holdResult";
	static final String LAST_PROCESS_POINT_NAME_TAG = "lastProcessPointName";
	static final String LAST_PROCESS_TIME_STAMP = "updateTimestamp";
	static final String HOLD_PROCESS_POINT_NAME_TAG = "holdProcessPointName";
	static final String DEVICE_ID_TAG = "deviceId";
	
	public QsrAction(T view) {
		super(view);
	}

	protected List<Map<String, Object>> prepareHoldRecords(BaseProduct product){
		List<BaseProduct> productList = new ArrayList<BaseProduct>();
		productList.add(product);
		return this.prepareHoldRecords(productList);	
	}
	
	protected List<Map<String, Object>> prepareHoldRecords(List<? extends BaseProduct> productList){
		List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
		for (BaseProduct product : productList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(PRODUCT_TAG, product);
			map.put(SHIP_TAG,this.getShipLines().contains(product.getTrackingStatus()) ? true : false);
			if (getConfig().getOwnerProduct(product) != null){
				map.put(OWNER_TAG, Config.getOwnerProduct(product));
			}
			map.put(LAST_PROCESS_POINT_NAME_TAG, product.getLastPassingProcessPointId() == null ? "none" : getProcessPointName(product.getLastPassingProcessPointId()));
			tableData.add(map);
			map.put(LAST_PROCESS_TIME_STAMP, product.getUpdateTimestamp() == null ? "none" : product.getUpdateTimestamp());
		}
		return tableData;
	}
	
	protected List<Map<String, Object>> prepareReleaseRecords(BaseProduct product, List<? extends HoldResult> holdResultList){
		List<BaseProduct> productList = new ArrayList<BaseProduct>();
		productList.add(product);
		return prepareReleaseRecords(productList, holdResultList);
	}
	
	protected List<Map<String, Object>> prepareReleaseRecords(List<? extends BaseProduct> productList, List<? extends HoldResult> holdResultList){
		List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
		
		Map<Object, BaseProduct> productIx = new HashMap<Object, BaseProduct>();
		for (BaseProduct product : productList) {
			productIx.put(product.getId().toString().trim(), product);
		}
		
		for (HoldResult holdResult : holdResultList) {
			BaseProduct product = productIx.get(holdResult.getId().getProductId().trim());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(HOLD_RESULT_TAG, holdResult);
			map.put(PRODUCT_TAG, product);
			map.put(SHIP_TAG,this.getShipLines().contains(product.getTrackingStatus()) ? true : false);
			if (getConfig().getOwnerProduct(product) != null)
				map.put(OWNER_TAG, getConfig().getOwnerProduct(product));
			map.put(LAST_PROCESS_POINT_NAME_TAG, product.getLastPassingProcessPointId() == null ? "" : getProcessPointName(product.getLastPassingProcessPointId()));
			if(holdResult.getHoldProcessPoint() != null) {
			map.put(HOLD_PROCESS_POINT_NAME_TAG, holdResult.getHoldProcessPoint() == null ? "" : getProcessPointName(holdResult.getHoldProcessPoint()));
			String deviceId = getDeviceId(product.getProductId(), holdResult.getHoldProcessPoint(), product.getProductType());
			map.put(DEVICE_ID_TAG,deviceId == null ? "" : deviceId);
			}
			tableData.add(map);
		}
		return tableData;
	}
	
	protected Config getConfig() {
		String appId = getApplicationId();
		if(!StringUtils.isEmpty(appId))return Config.getInstance(appId);
		else return Config.getInstance();
	}
	
	protected String getApplicationId() {
		if( getView() instanceof ApplicationMainPanel) {
			return ((ApplicationMainPanel) getView()).getApplicationId();
		}
		else return null;
	}
	
	private String getDeviceId(String productId, String processPointId, ProductType productType) {
		ProductHistory history = ProductTypeUtil.getProductHistoryDao(productType).findMostRecentByProductAndProcessPointId(productId, processPointId);
		return history == null ? null : history.getDeviceId();
	}

	protected Object getProcessPointName(String processPointId) {
		if(StringUtils.isEmpty(processPointId)) return null;
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
		return  processPoint!= null?processPoint.getProcessPointName():null;
	}
	
	protected List<String> getShipLines(){
		String shipLines = getConfig().getPropertyBean().getShipLineId();
		return Arrays.asList(shipLines.split(Delimiter.COMMA));
	}
	
	protected void showScrollDialog(String message) {
		JOptionPane.showMessageDialog(null, this.getScrollPane(message), "WARNING", JOptionPane.WARNING_MESSAGE);
	}
	
	protected int showConfirmDialog(String message) {
		return (JOptionPane.showConfirmDialog(null, this.getScrollPane(message), "WARNING", JOptionPane.OK_CANCEL_OPTION));
	}
	
	protected JScrollPane getScrollPane(String message) {
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText(message.toString());
		textArea.setCaretPosition(0);
		JScrollPane scrollPane = new JScrollPane(textArea);	
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(350, 200));
		return scrollPane;
	}
	
	public List<List<HoldResult>> partitionHoldResults(List<HoldResult> list, int partitionSize){
		List<List<HoldResult>> partitions = new ArrayList<List<HoldResult>>();
	    Iterator<HoldResult> i = list.iterator();
	    while (i.hasNext()) {
	        List<HoldResult> partition = new ArrayList<HoldResult>();
	        for (int j=0; i.hasNext() && j < partitionSize; j++) {
	        	partition.add(i.next());
	        }
	        partitions.add(partition);
	    }
		return partitions;
	}
}
