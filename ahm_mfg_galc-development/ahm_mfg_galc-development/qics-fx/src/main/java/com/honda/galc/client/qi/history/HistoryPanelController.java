package com.honda.galc.client.qi.history;


import static com.honda.galc.client.product.action.ProductActionId.CANCEL;
import static com.honda.galc.client.product.action.ProductActionId.DIRECTPASS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.qi.base.AbstractQiProcessController;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.dto.ProductHistoryDisplayDto;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.util.StringUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class HistoryPanelController extends AbstractQiProcessController<HistoryModel, HistoryPanel> implements EventHandler<ActionEvent> {
	
	public HistoryPanelController(HistoryModel model, HistoryPanel view) {
		super(model, view);
	}
	
	/**
	 * This method is used to create Product Panel Buttons
	 */
	public ProductActionId[] getProductActionIds(){
		if(isCancelBtnDisable())  {
			return new ProductActionId[]{DIRECTPASS};
		} else 
			return new ProductActionId[]{CANCEL,DIRECTPASS};
	}
	
	/**
	 * this method is used to get product history list
	 * @param list
	 * @return
	 */
	public List<ProductHistoryDisplayDto> getProductHistoryList(List<ProductHistory> list) {
		QiProgressBar qiProgressBar = null;
		try{
		     qiProgressBar = QiProgressBar.getInstance("Loading Product History View.","Loading Product History View.",
		    		 getModel().getProductId(),getView().getStage(),true);
			qiProgressBar.showMe();
		
			if(list == null){
				list = new ArrayList<ProductHistory>(getModel().selectProductHistory());
			}
			List<ProcessPoint> processPointList = getModel().findAllProcessPoints();
			Map<String, ProcessPoint> processPoints = new HashMap<String, ProcessPoint>();
			if (processPointList != null) {
				for (ProcessPoint item : processPointList) {
					processPoints.put(item.getProcessPointId(), item);
				}
			}
			
			List<ProductCarrier> carrierList = getModel().findAllByProductId(getProductModel().getProductId());
			List<String> process=new ArrayList<String>();  
			Map<String, ProductCarrier> carrierItems = new HashMap<String, ProductCarrier>();
			if (carrierList != null && !carrierList.isEmpty()) {
				for (ProductCarrier item : carrierList) {
					carrierItems.put(item.getProcessPointId(),item);
					process.add(item.getProcessPointId());
				}
			}
			
			getModel().setProcessPoints(processPoints);
			for(ProductHistory productHistory : list) {
				productHistory.setProcessPointName(getModel().getProcessPointName(productHistory.getProcessPointId()));
				productHistory.setCarrierId("");
				if(process != null && !process.isEmpty()){
					if(process.contains(productHistory.getProcessPointId())) {
						productHistory.setCarrierId(carrierItems.get(productHistory.getProcessPointId()).getId().getCarrierId());
						carrierItems.remove(productHistory.getProcessPointId());
					}
					else
						productHistory.setCarrierId("");
				}
			}
			
			if(carrierItems !=null && !carrierItems.isEmpty()) {
				Iterator<Entry<String, ProductCarrier>> iterator = carrierItems.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, ProductCarrier> history = iterator.next();
					ProductResult temp = new ProductResult(history.getValue().getId().getProductId(),history.getValue().getProcessPointId(),history.getValue().getId().getOnTimestamp());
					temp.setCarrierId(history.getValue().getId().getCarrierId());
					temp.setProcessPointName(getModel().getProcessPointName(history.getValue().getProcessPointId()));
					list.add(temp);
				}
			}
			list.sort((e1, e2) -> e1.getActualTimestamp().compareTo(e2.getActualTimestamp()));
		}
		finally {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
		return getProductHistoryDisplayDto(list);
	}
	
	public List<ProductHistoryDisplayDto> getProductHistoryDisplayDto(List<ProductHistory> productHistories) {
		List<ProductHistoryDisplayDto> productHistoryDisplayDtos = new ArrayList<>();
		productHistories.stream().forEach(p -> productHistoryDisplayDtos.add(createProductHistoryDisplayDto(p)));
		return productHistoryDisplayDtos;
	}

	private ProductHistoryDisplayDto createProductHistoryDisplayDto(ProductHistory productHistory) {
		ProductHistoryDisplayDto productHistoryDisplayDto = new ProductHistoryDisplayDto();
		productHistoryDisplayDto.setProductId(productHistory.getProductId());
		productHistoryDisplayDto.setProcessPointName(productHistory.getProcessPointName());
		productHistoryDisplayDto.setActualTimestamp(productHistory.getActualTimestamp());
		String processPointId = StringUtil.isNullOrEmpty(productHistory.getDeviceId()) ? productHistory.getProcessPointId() : productHistory.getProcessPointId() + " - " + productHistory.getDeviceId();
		productHistoryDisplayDto.setProcessPointId(processPointId);
		productHistoryDisplayDto.setCarrierId(productHistory.getCarrierId());
		return productHistoryDisplayDto;
	}

	@Override
	public void handle(ActionEvent arg0) {

	}

	@Override
	public void initializeListeners() {

	}

	@Override
	public void initEventHandlers() {
		getView().reload();
	}

}
