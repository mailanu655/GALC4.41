package com.honda.galc.client.teamleader.hold.qsr.put.lot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;

public class SearchLotAction extends BaseListener<HoldLotPanel> implements ActionListener {
	private static final int MIN_SEARCH_VALUE_LENGTH = 5;
	
	public SearchLotAction(HoldLotPanel parentPanel) {
		super(parentPanel);
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {
		String selectedDpt = null;
		String editorInput = null;
		String listInput = null;
		String requestedProdLot = null;
		
		
		try	{
			if (e.getSource() == getView().getInputPanel().getProdLotSearchComboBox().getComponent()) {
				editorInput	= (String) getView().getInputPanel().getProdLotSearchComboBox().getComponent().getEditor().getItem();
				if (StringUtils.isBlank(editorInput)) return;
				if (editorInput.length() < MIN_SEARCH_VALUE_LENGTH) {
					String message = "Please enter at least " + MIN_SEARCH_VALUE_LENGTH + " characters";
					getView().getMainWindow().setErrorMessage(message);
					getView().getLogger().info(message);
					return;
				}
				getView().getInputPanel().loadSearchProductionLots(editorInput);
				int resultCount = getView().getInputPanel().getProdLotSearchComboBox().getComponent().getItemCount();
				if (resultCount == 0) {
					String message = "No production lots found matching \"" + editorInput + "\"";
					getView().getMainWindow().setErrorMessage(message);
					getView().getLogger().info(message);
					getView().getInputPanel().getProdLotSearchComboBox().getComponent().getEditor().setItem(editorInput);
					return;
				} else if (resultCount == 1) {
					getView().getInputPanel().getProdLotSearchComboBox().getComponent().setSelectedIndex(0);
					requestedProdLot = (String)getView().getInputPanel().getProdLotSearchComboBox().getComponent().getSelectedItem();
				} else {
					getView().getInputPanel().getProdLotSearchComboBox().getComponent().getEditor().setItem(editorInput);
					getView().getInputPanel().getProdLotSearchComboBox().getComponent().showPopup();
					return;
				}
			}else if (e.getSource() == getView().getInputPanel().getProdLotComboBox().getComponent()) {
				selectedDpt	= (String) getView().getInputPanel().getInProcDptComboBox().getComponent().getSelectedItem();
				editorInput	= (String) getView().getInputPanel().getProdLotComboBox().getComponent().getEditor().getItem();
				listInput	= (String) getView().getInputPanel().getProdLotComboBox().getComponent().getSelectedItem();
				
				if (e.getActionCommand().equals("comboBoxEdited")) {
					if (StringUtils.isBlank(editorInput)) { 
						getView().getInputPanel().resetPanel();
						return;
					}
					if (!editorInput.trim().equals(listInput)) {
						getView().getInputPanel().resetPanel();
						String message = "Production lot " + editorInput + " could not be found in department " + selectedDpt;
						getView().getMainWindow().setErrorMessage(message);
						getView().getLogger().info(message);
						return;
					}
					requestedProdLot = editorInput.trim();
				} else {
					requestedProdLot = listInput;
				}
			}
			
			if (StringUtils.isBlank(requestedProdLot)) return;
			
			getView().getProductPanel().removeData();
			getView().getLogger().info("Value " + requestedProdLot + " entered in the Production Lot field");

			List<BaseProduct> products = (List<BaseProduct>) findProducts(requestedProdLot);
			if (products == null || products.size() == 0){
				String message = "No products found for production lot " + requestedProdLot;
				getView().getMainWindow().setErrorMessage(message);
				getView().getLogger().info(message);
				getView().getInputPanel().getProdLotComboBox().requestFocus();
				return;
			}
			populateTable(products);
			StringBuilder sb = new StringBuilder("");
			sb.append(
					"\n\t" + products.size() + " " + getView().getProductType().getProductName() + 
					" products found for production lot " + requestedProdLot + ":");
			for (BaseProduct product : products) {
				sb.append(
						"\n\t" + product.getProductId() +
						", Spec Code: " + product.getProductSpecCode() +
						", Last Process: " + product.getLastPassingProcessPointId() +
						", Tracking Status: " + product.getTrackingStatus());
			}
			getView().getLogger().info(sb.toString());
		} catch(ServiceInvocationException ex){
			getView().getLogger().error(Arrays.toString(ex.getStackTrace()));
		}
	}

	@SuppressWarnings("unchecked")
	protected List<BaseProduct> findProducts(String inputNumber) {
		ProductDao<BaseProduct> productDao = (ProductDao<BaseProduct>) getProductDao(getView().getProductType());
		List<BaseProduct> products = productDao.findAllByProductionLot(inputNumber);
		return products;
	}
	
	protected void populateTable(List<BaseProduct> products) {
		List<Map<String, Object>> list = getView().getProductPanel().getItems();
		for (BaseProduct product : products) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("product", product);
			map.put("lastProcessPointName", getProcessPointName(product.getLastPassingProcessPointId()));
			map.put("ship" , getShipLineIds().contains(product.getTrackingStatus()) ? true : false);
			BaseProduct owner = Config.getOwnerProduct(product);
			if (owner != null){
				map.put("owner", owner);
			}
			list.add(map);
		}
		getView().getProductPanel().reloadData(list);
	}
	
	private Object getProcessPointName(String processPointName) {
		if(StringUtils.isEmpty(processPointName)) return null;
		ProcessPoint processPoint = getProcessPointDao().findById(processPointName);
		return  processPoint!= null?processPoint.getProcessPointName():null;
	}
	
	private List<String> getShipLineIds(){
		String shipLineId = Config.getProperty().getShipLineId();
		if (StringUtils.isBlank(shipLineId)) return new ArrayList<String>();
		List<String> lineIdList = Arrays.asList(shipLineId.split(Delimiter.COMMA));
		return lineIdList;
	}
}
