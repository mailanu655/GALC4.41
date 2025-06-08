package com.honda.galc.client.plastics;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.dao.product.SubProductShippingDetailDao;
import com.honda.galc.data.SubId;
import com.honda.galc.entity.enumtype.SubProductShippingDetailStatus;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.entity.product.SubProductShippingId;
import com.honda.galc.util.KeyValue;

/**
 * 
 * 
 * <h3>SubProductShippingModel Class description</h3>
 * <p> SubProductShippingModel description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Aug 26, 2014
 *
 *
 */
public class SubProductShippingModel extends BaseSubProductModel{

	
	public SubProductShippingModel(ApplicationContext applicationContext) {
		super(applicationContext);
	}
	
	public List<MultiValueObject<List<SubProductShippingDetail>>> createShippingContainers(List<SubProductShippingDetail> shippingDetails){
		List<MultiValueObject<List<SubProductShippingDetail>>> shippingContainers = new ArrayList<MultiValueObject<List<SubProductShippingDetail>>>();
		MultiValueObject<List<SubProductShippingDetail>> container = null;
		for(SubId subId : getSubIdTypes()){
			for(SubProductShippingDetail shippingDetail : shippingDetails) {
				SubId currentSubId = getSubId(shippingDetail.getId().getSubId());
				if(!currentSubId.equals(subId)) continue;
				if(container == null || container.getKeyObject().size()>= getContainerSize()) {
					container = new MultiValueObject<List<SubProductShippingDetail>>();
					container.add(shippingDetail.getContainerId());
					container.add(subId);
					container.setKeyObject(new ArrayList<SubProductShippingDetail>());
					shippingContainers.add(container);
				}
				container.getKeyObject().add(shippingDetail);
			}
		}
		return shippingContainers;
	}
	
	public void shipProducts(List<SubProductShippingDetail> details) {
		
		for(SubProductShippingDetail detail :details) {
    		detail.setStatusId(detail.getStatusId() < 8 ? detail.getStatusId() + 8 : detail.getStatusId());
    	}
    	getDao(SubProductShippingDetailDao.class).saveAll(details);
    	
    	for(SubProductShippingDetail detail :details) {
    		if(!StringUtils.isEmpty(detail.getProductId()))
    			invokeTracking(detail.getProductId());
    	}
    	
    	updateShippingLots(details);
	}
	
	public boolean isContainerLoaded(List<SubProductShippingDetail> details) {
		for(SubProductShippingDetail detail : details) {
			if(!detail.getStatus().equals(SubProductShippingDetailStatus.WAITING)) return true;
		}
		return false;
	}
	
	private void updateShippingLots(List<SubProductShippingDetail> details) {
		Set<KeyValue<String,String>> lots = new LinkedHashSet<KeyValue<String,String>>();
		for(SubProductShippingDetail detail :details) {
			lots.add(new KeyValue<String,String>(detail.getKdLot(),detail.getProductionLot()));
		}	
		
		for(KeyValue<String,String> lot : lots) {
			int count = getDao(SubProductShippingDetailDao.class).countNotShippedProducts(lot.getKey(), lot.getValue());
			if(count != 0) {
				SubProductShipping shippingLot = getDao(SubProductShippingDao.class).findByKey(new SubProductShippingId(lot.getKey(),lot.getValue()));
				if(shippingLot != null) {
					shippingLot.setActQuantity(shippingLot.getSchQuantity() - count);
					if(shippingLot.getActQuantity() < shippingLot.getSchQuantity()) shippingLot.setStatus(SubProductShipping.SHORT_SHIPPED);
					else shippingLot.setStatus(SubProductShipping.SHIPPED);
					getDao(SubProductShippingDao.class).update(shippingLot);
				}
			}
		}
	}
	
	
}
