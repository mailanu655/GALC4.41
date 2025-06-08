package com.honda.galc.client.datacollection.observer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.property.ExpectedProductPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.tracking.MbpnProductHelper;

public class StampedSequenceExpectedProductManager extends ProductSequenceManager
{

	public StampedSequenceExpectedProductManager(ClientContext context) {
		super(context);
	}

	@Override
	public String getNextExpectedProductId(String productId) {
		
		ProductStampingSequence productSeq = getNextStampingSequence(productId);
		
		return productSeq!= null? productSeq.getProductId():null;
	}
	
	public ProductStampingSequence getNextStampingSequence(String productId) {
		ExpectedProductPropertyBean bean = PropertyService.getPropertyBean(ExpectedProductPropertyBean.class, context.getProcessPointId());
		Boolean isNextProdByStampSeq = bean.isNextProdByStampSeq();
		ProductStampingSequence productSeq = null;
		if(isNextProdByStampSeq) {
			productSeq = getNextProductInStampingSequence(productId);
		} else {
			productSeq = getNextProductInSequence();
		}
		
		return productSeq;
	}
	
	public PreProductionLot getCurrentProductionLot(){
		PreProductionLot curProductionLot = null;
		String planCode = MbpnProductHelper.getPlanCode(context.getProcessPointId());
		curProductionLot = getPreProductionLotDao().findCurrentPreProductionLotByPlanCode(planCode);
		return curProductionLot;
	}
	
	public void updateProductSequence(ProcessProduct state) {
		if(StringUtils.isEmpty(StringUtils.trim(state.getProductId()))) return; 
		ExpectedProductPropertyBean bean = PropertyService.getPropertyBean(ExpectedProductPropertyBean.class, context.getProcessPointId());
		Boolean isUpdateSendStatus = bean.isUpdateSendStatus();
		if(isUpdateSendStatus) {
			ProductStampingSequence productSeq = null;
			productSeq = getProductInStampingSequence(state.getProductId());
			if(productSeq == null) return;
			productSeq.setSendStatus(PlanStatus.LOADED.getId());
			getProductStampingSequenceDao().save(productSeq);
			getPreProductionLotDao().updateStampedCount(productSeq.getProductionLot());
			
			PreProductionLot preProductionLot = getPreProductionLotDao().findByKey(productSeq.getProductionLot());
			if(preProductionLot!= null && 
				(preProductionLot.getSendStatus() == PreProductionLotSendStatus.INPROGRESS || preProductionLot.getSendStatus() == PreProductionLotSendStatus.WAITING)){
				if(isLotCompleted(preProductionLot)) completeLot(preProductionLot);
			}
		}
	}
	
	private ProductStampingSequence getProductInStampingSequence(String productId) {
		PreProductionLot curProductionLot = getCurrentProductionLot(productId);
		if(curProductionLot == null) return null; 
		ProductStampingSequence productStampingSequence = getProductStampingSequenceDao().findById(curProductionLot.getProductionLot(), productId);
		return productStampingSequence;
	}
	
	private ProductStampingSequence getNextProductInStampingSequence(String productId) {
		PreProductionLot curProductionLot = getCurrentProductionLot(productId);
		if(curProductionLot == null) return null; 
		ProductStampingSequence productStampingSequence = getProductStampingSequenceDao().findById(curProductionLot.getProductionLot(), productId);
		//If last product of the production lot
		if(curProductionLot.getLotSize()==productStampingSequence.getStampingSequenceNumber()){
			return getProductStampingSequenceDao().findNextProduct(curProductionLot.getNextProductionLot(),0);
		}
		return getProductStampingSequenceDao().findNextProduct(curProductionLot.getProductionLot(), productStampingSequence.getStampingSequenceNumber());
	}

	private ProductStampingSequence getNextProductInSequence(){
		PreProductionLot curProductionLot = getCurrentProductionLot();
		if(curProductionLot == null) return null; 
		if(curProductionLot.getSendStatus() != PreProductionLotSendStatus.INPROGRESS){
			getPreProductionLotDao().updateSendStatus(curProductionLot.getProductionLot(),PreProductionLotSendStatus.INPROGRESS.getId() );
		}
		return getProductStampingSequenceDao().findNextProduct(curProductionLot.getProductionLot(), PlanStatus.SCHEDULED);
	}
	
	public PreProductionLot getCurrentProductionLot(String productId){
		PreProductionLot curProductionLot = null;
		BaseProduct aproduct = context.getDbManager().confirmProductOnServer(productId);
		curProductionLot = getPreProductionLotDao().findByKey(aproduct.getProductionLot());
		return curProductionLot;
	}

	protected boolean isLotCompleted(PreProductionLot preProductionLot) {
		return preProductionLot.getStampedCount() >= preProductionLot.getLotSize(); 
	}
	
	protected void completeLot(PreProductionLot lot) {
		getPreProductionLotDao().updateSendStatus(lot.getProductionLot(), PreProductionLotSendStatus.DONE.getId());
	}
	
	private ProductStampingSequenceDao getProductStampingSequenceDao(){
		return ServiceFactory.getDao(ProductStampingSequenceDao.class);
	}
	
	private PreProductionLotDao getPreProductionLotDao(){
		return ServiceFactory.getDao(PreProductionLotDao.class);
	}
}
