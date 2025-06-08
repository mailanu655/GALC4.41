package com.honda.galc.service.datacollection;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.dao.product.CrankshaftDao;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.DunnageContentDao;
import com.honda.galc.dao.product.DunnageDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Dunnage;
import com.honda.galc.entity.product.DunnageContent;
import com.honda.galc.entity.product.DunnageContentId;
import com.honda.galc.property.DunnagePropertyBean;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.TrackingPropertyBean;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.engine.DunnageAssignmentService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.util.DunnageHistoryUtil;

public class DunnageAssignmentServiceImpl extends IoServiceBase  implements DunnageAssignmentService{
	
	public HeadLessPropertyBean getHeadLessPropertyBean() {
		return PropertyService.getPropertyBean(HeadLessPropertyBean.class,getProcessPointId());
	}
	
	public DunnagePropertyBean getDunnagePropertyBean() {
		return PropertyService.getPropertyBean(DunnagePropertyBean.class,getProcessPointId());
	}

	@Autowired
	public TrackingService trackingService;
	
	private TrackingPropertyBean getTrackingPropertyBean() {
		return PropertyService.getPropertyBean(TrackingPropertyBean.class,getProcessPointId());
	}

	public DataContainer processData() {
		Device device = getDevice();
		String processPointId = device.getIoProcessPointId(); 
		String dunnageId=(String) device.getInputValue(TagNames.DUNNAGE_ID.name());
		String productId=(String) device.getInputValue(TagNames.PRODUCT_ID.name());
		String productType= getProductType();
		
		String forwardProcessPointId = getTrackingPropertyBean().getTrackingProcessPointIdOnSuccess();
		getLogger().info("received product id " + productId + ", dunnage id " + dunnageId);
		
		boolean aFlag = false;
		try{
			BaseProduct product = checkProductId(productId, productType);
			productId = product.getProductId();
			checkDunnageId(dunnageId);
			
			//Run only if property SAME_MODEL_CODE is true. Default false
			if(getDunnagePropertyBean().isSameModelCode()){
				if(!validateModelCode(product.getModelCode(), dunnageId)){
					String msg = String.format("product: %s model code: %s does not match Dunnage model.", product.getProductId(), product.getModelCode());
					getLogger().info(msg);
					throw new TaskException(msg);
				}
			}
			
			//check the dunnage capacity
			int dunnageCount = (int)selectCountByDunnage(dunnageId);
			if (dunnageCount >= getDunnageCapacity(productType)) {
				String msg = String.format("Dunnage is full, max capacity: %s", getDunnageCapacity(productType));
				getLogger().info(msg);
				throw new TaskException(msg);
}
			
			//Add to dunnage
			updateDunnage(productType, productId, dunnageId);
			
			//create history record with onTimestamp
			if(getDunnagePropertyBean().isCreateDunnageHistory()) DunnageHistoryUtil.createDunnageHist(productId, dunnageId);
			
			//checker whether to use new table (DUNNAGE_CONTENT_TBX) or not. INSERT_DUNNAGE_CONTENT is true. Default false
			if(getDunnagePropertyBean().isInsertDunnageContent()){
				//validateDunnageId(dunnageId);
				String dunnageRow=(String) device.getInputValue(TagNames.DUNNAGE_ROW.name());
				String dunnageColumn=(String) device.getInputValue(TagNames.DUNNAGE_COLUMN.name());
				String dunnageLayer=(String) device.getInputValue(TagNames.DUNNAGE_LAYER.name());
				updateDunnageContent(productType, productId, dunnageId, dunnageRow, dunnageColumn, dunnageLayer);
			}
				
			getLogger().info("update product " + productId + " with dunnage " + dunnageId);
			
			//How do i track the process point currently being executedDunnageHistoryUtil
			trackingService.track( ProductTypeCatalog.getProductType(getProductType()), productId,processPointId);
						
			trackingService.track(ProductTypeCatalog.getProductType(getProductType()), productId, forwardProcessPointId);
	
			aFlag = true;
		} catch (TaskException te) {
			getLogger().error(te, "Exception when collect data for ", this.getClass().getSimpleName());
		} catch (Throwable e){
			getLogger().error(e, "Exception when collect data for ", this.getClass().getSimpleName());
			
		}
		return dataCollectionComplete(aFlag);
	}
	
	public long selectCountByDunnage(String dunnage) {
		return getProductDao().countByDunnage(dunnage);
	}
	
	protected ProductDao<?> getProductDao() {
		return ProductTypeUtil.getProductDao(getProductType());
	}

	private boolean isNewDunnage(String dunnageNumber){
		Dunnage dunnage = getDunnageDao().findByKey(dunnageNumber);
		if(null==dunnage || StringUtils.isEmpty(dunnage.getDunnageId())){
			return true;
		}else return false;
	}
	
	private boolean isFisrtProductInDunnage(String dunnageNumber){
		return getDunnageContentDao().findAllProductsInDunnage(dunnageNumber).size()==0;
	}

	public BaseProduct checkProductId(String productId, String productType){
		Map<Object, Object> context = new HashMap<Object,Object>();
		BaseProduct product = ServiceUtil.validateProductId(productType, productId, context, productId);
		checkContext(context);
		getLogger().info("The product " + productId + " is valid.");
		return product;
	}
		
	public void checkDunnageId(String dunnageId){
			if(StringUtils.isEmpty(dunnageId)) {
				throw new TaskException("Dunnage Id is null");
			}
	}
	
	public int updateDunnage(String productType,String productId, String dunnageNumber) {
		return getDieCastDao(productType).updateDunnage(productId, dunnageNumber);
	}
	
	public void updateDunnageContent(String productType,String productId, String dunnageNumber, String dunnageRow,
			String dunnageColumn, String dunnageLayer) {
		if(isNewDunnage(dunnageNumber)){
			getLogger().info("update Dunnage info " + dunnageNumber);
			Dunnage dunnage = new Dunnage();
			dunnage.setDunnageId(dunnageNumber);
			dunnage.setExpectedQty(getDunnageCapacity(productType));
			dunnage.setProductSpecCode(ServiceUtil.getProductFromDataBase(productType, productId).getProductSpecCode());
			getDunnageDao().update(dunnage);
		}else if(isFisrtProductInDunnage(dunnageNumber)){
			Dunnage dunnage = getDunnageDao().findByKey(dunnageNumber);
			dunnage.setProductSpecCode(getDieCastDao(productType).findBySn(productId).getProductSpecCode());
			getDunnageDao().update(dunnage);
		}
		getLogger().info("update DunnageContent for dunnage " + dunnageNumber);
		DunnageContentId dunnageContentId = new DunnageContentId(dunnageNumber,productId);
		DunnageContent dunnageContent = new DunnageContent();
		dunnageContent.setId(dunnageContentId);
		dunnageContent.setDunnageColumn(dunnageColumn);
		dunnageContent.setDunnageLayer(dunnageLayer);
		dunnageContent.setDunnageRow(dunnageRow);
		
		getDunnageContentDao().update(dunnageContent);
		
	}

	private DunnageContentDao getDunnageContentDao() {
		return getDao(DunnageContentDao.class);		
	}
	
	private DunnageDao getDunnageDao() {
		return getDao(DunnageDao.class);		
	}

	private DiecastDao getDieCastDao(String productType) {
		if(ProductType.BLOCK.toString().equals(productType))
			return getDao(BlockDao.class);
		else if(ProductType.HEAD.toString().equals(productType))
			return getDao(HeadDao.class);
		else if(ProductType.CONROD.toString().equals(productType))
			return getDao(ConrodDao.class);
		else if(ProductType.CRANKSHAFT.toString().equals(productType))
			return getDao(CrankshaftDao.class);
		else return null;
	}
	
	private boolean validateModelCode(String modelCode, String dunnageNumber) {
		List<? extends BaseProduct> findAllByDunnage = ProductTypeUtil.getProductDao(getProductType()).findAllByDunnage(dunnageNumber);
		for (BaseProduct aproduct : findAllByDunnage)
			if (modelCode == null || !modelCode.equals(aproduct.getModelCode())) {
				return false;
			}
		return true;
	}
	
	public int getDunnageCapacity(String productType) {
		if(ProductType.BLOCK.toString().equals(productType)) {
			return getDunnagePropertyBean().getBlockDunnageCartQuantity();
		} else if(ProductType.HEAD.toString().equals(productType)) {
			return getDunnagePropertyBean().getHeadDunnageCartQuantity();
		} else
			return getDunnagePropertyBean().getDunnageCartQuantity();
	}
}
