package com.honda.galc.client.teamleader.hold.qsr.put.vinseq;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;




public class SelectProductsByVinSeqAction extends AbstractAction {

	private BaseListener<VinSeqPanel> action;
	
	
	public SelectProductsByVinSeqAction(VinSeqPanel vinSeqPanel) {
		super();
		putValue(Action.NAME, "Select");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		action = new BaseListener<VinSeqPanel>(vinSeqPanel) {
			@Override
			public void executeActionPerformed(ActionEvent e) {

				String planCode = (String)getView().getInputPanel().getLineElement().getComponent().getSelectedItem();
				String productId = (String) getView().getInputPanel().getProductInput().getText();
				
				List<String> yearsList = filterWildCard(CommonUtil.objectArrayToStringList(getView().getInputPanel().getModelYearListElement().getComponent().getSelectedValues()));
				List<String> codesList = filterWildCard(CommonUtil.objectArrayToStringList(getView().getInputPanel().getModelCodeListElement().getComponent().getSelectedValues()));
				List<String> typesList = filterWildCard(CommonUtil.objectArrayToStringList(getView().getInputPanel().getModelTypeListElement().getComponent().getSelectedValues()));
				List<String> destsList = filterWildCard(CommonUtil.objectArrayToStringList(getView().getInputPanel().getModelDestListElement().getComponent().getSelectedValues()));
				
				
								
				String startAfSeq = (String) getView().getInputPanel().getStartAfSeqInput().getText();
				String endAfSeq = (String) getView().getInputPanel().getEndAfSeqInput().getText();	
				
				String startVinSeq = (String) getView().getInputPanel().getStartVinSeqInput().getText();
				startVinSeq = StringUtils.isEmpty(startVinSeq)?startVinSeq:StringUtils.leftPad(startVinSeq, 6, "0");
				
				String endVinSeq = (String) getView().getInputPanel().getEndVinSeqInput().getText();
				endVinSeq = StringUtils.isEmpty(endVinSeq)?endVinSeq:StringUtils.leftPad(endVinSeq, 6, "0");
				
				String startProdLot = (String) getView().getInputPanel().getStartProductionLotInput().getText();
				String endProdLot = (String) getView().getInputPanel().getEndProductionLotInput().getText();	
				
				Boolean includeShipScrap = getView().getInputPanel().getIncludeShipScrapInput().isSelected();
				
				List<String> shiplineIds = includeShipScrap?new ArrayList<String>():getShipLineIds();
				
				List<BaseProduct> products = new ArrayList<BaseProduct>();
				
				if(!StringUtils.isEmpty(productId)) {
					BaseProduct product = getProductDao(getView().getProductType()).findBySn(productId);
					if(product == null) {
						ProductType type = this.getView().getProductType();
						ProductTypeData productTypeData = ServiceFactory.getDao(ProductTypeDao.class).findByKey(type.toString());
						if(Config.isDisableProductIdCheck(type.toString())){
							if(productTypeData.isNumberValid(productId)){
								product = ProductTypeUtil.createProduct(type.toString(), productId);
								
							} else {
								getView().getMainWindow().setErrorMessage("Invalid Product ID: "+ productId +". Length invalid.");
								return;
							}
						} else {
							getView().getMainWindow().setErrorMessage("Product does not exist in database");
							TextFieldState.ERROR.setState(getView().getInputPanel().getProductInput());
							getView().getInputPanel().getProductInput().selectAll();
							getView().getInputPanel().getProductInput().requestFocus();
							return;
						}
					}else {
						if(getView().getProductType().equals(ProductType.ENGINE)) {
							Engine engine = (Engine)product;
							if(StringUtils.isNotBlank(engine.getVin())){
								Frame frame =  ServiceFactory.getDao(FrameDao.class).findBySn(engine.getVin());
								if(shiplineIds.contains(frame.getTrackingStatus())) {
									getView().getMainWindow().setErrorMessage("Invalid Product ID: "+ productId +".Vin ("+engine.getVin() +") assigned to is already shipped/scrapped.");
									return;
								}
							}
						}
					}
					products.add(product);
				}else {
					if(getView().getProductType().equals(ProductType.FRAME)) {
						int maxSize = Config.getProperty().getMaxResultsetSize();
			
							if (maxSize > 0) {
								long count = ServiceFactory.getDao(FrameDao.class).count(startAfSeq, endAfSeq, startProdLot, endProdLot, startVinSeq, endVinSeq, yearsList, codesList, typesList, destsList, planCode,shiplineIds);
								if (count > maxSize) {
									String msg = "Resultset: %s exceeds max size : %s, please select additional criteria.";
									msg = String.format(msg, count, maxSize);
									this.getMainWindow().setErrorMessage(msg);
									return;
								}
							}
							List<Frame> results = ServiceFactory.getDao(FrameDao.class).findByRanges(startAfSeq, endAfSeq, startProdLot, endProdLot, startVinSeq, endVinSeq, yearsList, codesList, typesList, destsList, planCode,shiplineIds);
							products.addAll(results);
						
					}
				}
				
				selectProducts(products);

				if (getView().getProductPanel().getTable().getRowCount() == 0) {
					String msg = "No Results found for select criteria.";
					this.getMainWindow().setMessage(msg);
					return;
				}
			}
		};
	}

	public void actionPerformed(ActionEvent ae) {
		getAction().actionPerformed(ae);
	}

	protected BaseListener<VinSeqPanel> getAction() {
		return action;
	}

	protected void selectProducts(List<? extends BaseProduct> products) {

		List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();

		List<String> lineIdList = getShipLineIds();
		Map<String, BaseProduct> productIx = new HashMap<String, BaseProduct>();
		for (BaseProduct product : products) {
		
				productIx.put(product.getProductId(), product);
	
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("product", product);
				map.put("ship" ,lineIdList.contains(product.getTrackingStatus()) ? true : false);
				map.put("lastProcessPointName", getProcessPointName(product.getLastPassingProcessPointId()));
			
				tableData.add(map);

			
		}
		getAction().getView().getProductPanel().reloadData(tableData);
	}
	
	
	private Object getProcessPointName(String processPointName) {
		if(StringUtils.isEmpty(processPointName)) return null;
		ProcessPoint processPoint = action.getProcessPointDao().findById(processPointName);
		return  processPoint!= null?processPoint.getProcessPointName():null;
	}
	
	private List<String> filterWildCard(List<String> mtocList){
		return mtocList.contains("*")?new ArrayList<String>():mtocList;
	}
	
	private List<String> getShipLineIds(){
		String shipLineId = Config.getProperty().getShipLineId();
		List<String> lineIdList = Arrays.asList(shipLineId.split(Delimiter.COMMA));
		
		return lineIdList;
	}
}
