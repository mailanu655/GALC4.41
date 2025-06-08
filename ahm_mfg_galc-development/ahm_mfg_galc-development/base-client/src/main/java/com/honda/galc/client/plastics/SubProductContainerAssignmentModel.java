package com.honda.galc.client.plastics;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.dao.product.SubProductShippingDetailDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.SubId;
import com.honda.galc.entity.enumtype.SubProductShippingDetailStatus;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * 
 * <h3>SubProductContainerAssignmentModel Class description</h3>
 * <p> SubProductContainerAssignmentModel description </p>
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
 * Aug 25, 2014
 *
 *
 */
public class SubProductContainerAssignmentModel extends BaseSubProductModel{
	
	private static final String SHIPPING_PPID_LIST = "SHIPPING_PPID_LIST";
	
	public SubProductContainerAssignmentModel(ApplicationContext applicationContext) {
		super(applicationContext);
	}
	
	/**
	 * Intentionaly get new refreshed list
	 * @return
	 */
	public List<SubProductShipping> getAllShippingLots() {
		
		List<SubProductShipping> shippings = getDao(SubProductShippingDao.class).findAllShipping(getProductType(), getLineNumber(),getSubIds());
		return filterShippedLots(shippings);
		
	}
	
	public List<SubProductShipping> filterShippedLots(List<SubProductShipping> shippings) {
		
		List<SubProductShipping> removeLots = new ArrayList<SubProductShipping>();
		List<SubProductShipping> sameKdLotShippingLots = new ArrayList<SubProductShipping>();
		
		boolean isShipped = true;
		for(int i =0; i<shippings.size();i++) {
			SubProductShipping shipping = shippings.get(i);
			if(!sameKdLotShippingLots.isEmpty() && !shipping.isSameKdLot(sameKdLotShippingLots.get(0))){
				if(isShipped) {
					removeLots.addAll(sameKdLotShippingLots);
				}
				sameKdLotShippingLots.clear();
				isShipped = true;
			}
				
			sameKdLotShippingLots.add(shipping);
			isShipped &= isShipped(shipping);
		}
		
		if(isShipped) {
			removeLots.addAll(sameKdLotShippingLots);
		}
		
		shippings.removeAll(removeLots);
		return shippings;
		
	}
	
	public List<String> getShippingPPID() {
		return PropertyService.getPropertyList(applicationContext.getProcessPointId(),SHIPPING_PPID_LIST);
	}
	
	
	public List<MultiValueObject<List<SubProductShippingDetail>>>  selectShippingContainers(List<SubProductShipping> shippingLots) {
		List<SubProductShippingDetail> details = new ArrayList<SubProductShippingDetail>();
		for(SubProductShipping shippingLot : shippingLots) {
			List<SubProductShippingDetail> lotDetails = 
				getDao(SubProductShippingDetailDao.class).findAllByShippingLot(shippingLot.getKdLotNumber(), shippingLot.getProductionLot());
			for(SubProductShippingDetail item: lotDetails) {
				item.setProductSpecCode(shippingLot.getProductSpecCode());
			}
			details.addAll(lotDetails);
		}
		return createShippingContainers(details);
	}
	
	public List<MultiValueObject<List<SubProductShippingDetail>>>  selectShippingContainers(List<SubProductShipping> shippingLots,String side) {
		List<SubProductShippingDetail> details = new ArrayList<SubProductShippingDetail>();
		for(SubProductShipping shippingLot : shippingLots) {
			details.addAll(getDao(SubProductShippingDetailDao.class).findAllByShippingLot(shippingLot.getKdLotNumber(), shippingLot.getProductionLot(),side));
		}
		return createShippingContainers(details);
	}
	
	public SubProduct findProduct(String productId) {
		return getDao(SubProductDao.class).findByKey(StringUtils.trim(productId));
	}
	
	public SubProductShipping saveShippingProduct(SubProductShippingDetail shipDetail) {
		if(isShipped(shipDetail)) {
			throw new TaskException("Container " + shipDetail.getContainerId() + " is Shipped");
		}
		getDao(SubProductShippingDetailDao.class).save(shipDetail);
		SubProductShipping subProductShipping = getDao(SubProductShippingDao.class).incrementActualQuantity(shipDetail.getKdLot(), shipDetail.getProductionLot());
		invokeTracking(shipDetail.getProductId());
		return subProductShipping;
	}
	
	public boolean isShipped(SubProductShippingDetail shipDetail) {
		SubProductShippingDetail detail = getDao(SubProductShippingDetailDao.class).findByKey(shipDetail.getId());
		return detail != null && detail.getStatus().isShipped();
	}
	
	public boolean isFullyLoaded(List<SubProductShippingDetail> details) {
		for(SubProductShippingDetail detail : details) {
			if(StringUtils.isEmpty(detail.getProductId())) return false;
		}
		return true;
	}
	
	public List<SubProductShippingDetail> removeSubProductFromShipping(String productId) {
		return getDao(SubProductShippingDetailDao.class).removeSubProduct(productId);
	}
	
	public SubProduct saveProduct(SubProductShippingDetail detail, String productId){
		SubProduct subProduct = ProductTypeUtil.createSubProduct(getProductType(), productId, detail.getId().getSubId());
		subProduct.setProductionLot(detail.getProductionLot());
		subProduct.setKdLotNumber(detail.getKdLot());
		subProduct.setProductSpecCode(detail.getProductSpecCode());
		getDao(SubProductDao.class).save(subProduct);
		return subProduct;
	}
	
	public void updateShippingData(List<SubProductShipping> shippingList, SubProductShipping shipping) {
		for(int i =0;i<shippingList.size();i++) {
			SubProductShipping item = shippingList.get(i);
			if(item.getKdLotNumber().equals(shipping.getKdLotNumber()) && 
			   item.getProductionLot().equals(shipping.getProductionLot())){
				shippingList.set(i, shipping);
				return;
			}
		}
	}
	
	public void updateContainerId(List<SubProductShippingDetail> shippingDetails,String containerId){
		for(SubProductShippingDetail detail : shippingDetails) {
			detail.setContainerId(containerId);
		}
		getDao(SubProductShippingDetailDao.class).saveAll(shippingDetails);
	}
	
	public List<MultiValueObject<List<SubProductShippingDetail>>> createShippingContainers(List<SubProductShippingDetail> shippingDetails){
		List<MultiValueObject<List<SubProductShippingDetail>>> shippingContainers = new ArrayList<MultiValueObject<List<SubProductShippingDetail>>>();
		int i = 1;
		MultiValueObject<List<SubProductShippingDetail>> container = null;
		for(SubId subId : getSubIdTypes()){
			for(SubProductShippingDetail shippingDetail : shippingDetails) {
				SubId currentSubId = getSubId(shippingDetail.getId().getSubId());
				if(!subId.equals(currentSubId)) continue;
				if(container == null || container.getKeyObject().size()>= getContainerSize()) {
					container = new MultiValueObject<List<SubProductShippingDetail>>();
					container.add(i);
					container.add(subId);
					container.add(shippingDetail.getContainerId());
					container.setKeyObject(new ArrayList<SubProductShippingDetail>());
					shippingContainers.add(container);
					i++;
				}
				container.getKeyObject().add(shippingDetail);
			}
		}
		return shippingContainers;
	}
	
	public SubProduct validateProductId(SubProductShippingDetail detail,String productId){
		if(detail == null) 
			throw new TaskException("Please select a shipping detail row");
		
		checkProductNumberDef(detail.getId().getSubId(), productId);
		
		SubProduct subProduct = findProduct(productId);
		if(subProduct != null) {
			if(!getProductType().equalsIgnoreCase(subProduct.getProductTypeValue()))
				throw new TaskException("Invalid product type : " + subProduct.getProductTypeValue());
			if(isShipped(subProduct))
				throw new TaskException("Product " + productId + " is shipped");
			if(subProduct.isOutstandingStatus())
				throw new TaskException("Product " + productId + " has outstanding defects");
			else if(subProduct.isScrapStatus())
				throw new TaskException("Product " + productId + " is scrapped");
			detail.setStatus(SubProductShippingDetailStatus.LOADED);
		}else {
			subProduct = saveProduct(detail, productId);
			detail.setStatus(SubProductShippingDetailStatus.NEW_PROD_ID);
		}

		return subProduct;
	}
	
	private boolean isShipped(SubProduct product) {
		for(String ppid : getShippingPPID()) {
			if(product.getLastPassingProcessPointId().equalsIgnoreCase(StringUtils.trim(ppid)))
				return true;
		}
		return false;
	}
	
	private boolean isShipped(SubProductShipping shipping) {
		return shipping.getStatus() == SubProductShipping.SHIPPED || shipping.getStatus() == SubProductShipping.SHORT_SHIPPED;
	}
	
	public boolean isShipped(List<SubProductShippingDetail> details) {
		for(SubProductShippingDetail detail : details) {
			if(detail.getStatus().isShipped()) return true;
		}
		return false;
	}
	
	private void checkProductNumberDef(String subId,String productId){
		if(ProductType.BUMPER.equals(ProductType.getType(getProductType()))) {
			String subIdStr = productId.substring(productId.length() - 1, productId.length());
			if(!subId.equalsIgnoreCase(subIdStr)) 
				throw new TaskException("Invalid product " + productId + " - wrong sub id: " + subIdStr);
		}
	}
	
	public ProductNumberDef getProductNumberDef() {
		List<ProductNumberDef> defs = ProductNumberDef.getProductNumberDef(ProductType.getType(getProductType()));
		return defs.isEmpty() ? null : defs.get(0);
	}
	
	public void checkConfigurations() {
		super.checkConfigurations();
		if(getProductNumberDef() == null)
			throw new TaskException("could not find Product number definitions");
		if(getShippingPPID() == null)
			throw new TaskException("Please configure SHIPPING_PPID_LIST");
	}
	
}


