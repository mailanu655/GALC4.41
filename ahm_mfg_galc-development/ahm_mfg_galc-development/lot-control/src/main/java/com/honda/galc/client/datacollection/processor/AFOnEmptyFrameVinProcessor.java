package com.honda.galc.client.datacollection.processor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.device.dataformat.ProductId;

import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.entity.product.ProductCarrierId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;


public class AFOnEmptyFrameVinProcessor extends AFOnFrameVinProcessor {

	public AFOnEmptyFrameVinProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}
	
	@Override
	public synchronized boolean execute(ProductId productId) {
		Logger.getLogger().debug("AFOnEmptyFrameVinProcessor : Enter execute method");

		try {	
				if(isEmptyCarrier(productId.getProductId())) {
					
					Logger.getLogger().info("Processing Empty Carrier");
					
					product.setProductId(getEmptyCarrier());
					product.setProductSpec(getEmptyCarrier());
					getController().getFsm().initLotControlRules(getEmptyCarrier(), getLotControlRules());
					
					if(!setSeqNum()) return false;
					getController().getFsm().productIdOk(product);
					
				}else {
					return super.execute(productId);
				}
						
			}catch (Exception  e){
				logException(e, productId.getProductId());
				e.printStackTrace();
				return false;
			}
		Logger.getLogger().debug("AFOnEmptyFrameVinProcessor : Exit execute method");
		return true;
	}
	
	
	protected boolean isEmptyCarrier(String productId) {
		if(productId.equalsIgnoreCase(getEmptyCarrier())) return true;
		return false;
	}

	@Override
	public boolean setSeqNum() {
		try {
			String seq = getSequenceString(getNextSequence());
			super.setSeqNum();
			Logger.getLogger().info("Updating ProductCarrier Table for - "+product.getProductId() +" with sequence - "+seq);
			updateCarrierTable(seq);
		} catch (Exception e) {
			logErrorMessage("An error occured while assigning AFON Sequence to ProductId");
			logException(e,product.getProductId());
			return false;
		}
		return true;
	}
	
	private void updateCarrierTable(String seq) {
		ProductCarrier productCarrier = new ProductCarrier();
		
		ProductCarrierId carrierId = new ProductCarrierId();
		carrierId.setCarrierId(seq);
		if(isEmptyCarrier(product.getProductId())) {
			String emptyProductId = StringUtils.substring(product.getProductId(), 0, 5) + 
					PropertyService.getProperty(getProcessPoint().getId(), "LINE_NUMBER", "1") + 
					DateFormatUtils.format(new Date(), "yyMMdd") + seq;
			carrierId.setProductId(emptyProductId);
			product.setProductId(emptyProductId);
		} else {
			carrierId.setProductId(product.getProductId());
		}
		carrierId.setOnTimestamp(new Timestamp(System.currentTimeMillis()));
		
		productCarrier.setId(carrierId);
		productCarrier.setProcessPointId(getProcessPoint().getId());
				
		ServiceFactory.getDao(ProductCarrierDao.class).save(productCarrier);
	}

	
	private List<LotControlRule> getLotControlRules(){
		return ServiceFactory.getDao(LotControlRuleDao.class).findAllForRule(context.getProcessPointId());
	}
	
	private String getEmptyCarrier() {
		return getController().getProperty().getEmptyCarrierName();
	}
}
