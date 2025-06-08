package com.honda.galc.service.datacollection;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.dao.product.CrankshaftDao;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.DunnageContentDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DunnageContentId;
import com.honda.galc.property.DunnagePropertyBean;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.TrackingPropertyBean;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.engine.DunnageDeassignmentService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.util.DunnageHistoryUtil;
import com.honda.galc.util.StringUtil;

public class DunnageDeassignmentServiceImpl extends IoServiceBase  implements DunnageDeassignmentService{
	
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
		String productId=(String) device.getInputValue(TagNames.PRODUCT_ID.name());
		String productType= getProductType();
		String dunnageId="";
		String forwardProcessPointId = getTrackingPropertyBean().getTrackingProcessPointIdOnSuccess();
		getLogger().info("received product id " + productId );
		
		boolean aFlag = false;
		try{
			BaseProduct product = checkProductId(productId, productType);
			dunnageId = product.getDunnage();
			
			removeDunnageProduct(product);
			
			if(getDunnagePropertyBean().isCreateDunnageHistory()) DunnageHistoryUtil.updateDunnageHist(product.getProductId(), dunnageId);
			
			//Delete data from DUNNAGE_CONTENT_TBX if INSERT_DUNNAGE_CONTENT is true. Default false
			if(getDunnagePropertyBean().isInsertDunnageContent() && !StringUtil.isNullOrEmpty(dunnageId)){
				updateDunnageContent(productId, dunnageId);
			}
				
			getLogger().info("Deassign product " + productId + " with dunnage " + dunnageId);
			
			trackingService.track( ProductTypeCatalog.getProductType(getProductType()), productId,processPointId);
			if(!StringUtil.isNullOrEmpty(forwardProcessPointId))			
				trackingService.track(ProductTypeCatalog.getProductType(getProductType()), productId, forwardProcessPointId);
	
			aFlag = true;
		} catch (TaskException te) {
			getLogger().error(te, "Exception when collect data for ", this.getClass().getSimpleName());
		} catch (Throwable e){
			getLogger().error(e, "Exception when collect data for ", this.getClass().getSimpleName());
			
		}
		return dataCollectionComplete(aFlag);
	}
	
	public BaseProduct checkProductId(String productId, String productType){
		Map<Object, Object> context = new HashMap<Object,Object>();
		BaseProduct product = ServiceUtil.validateProductId(productType, productId, context, productId);
		checkContext(context);
		getLogger().info("The product " + productId + " is valid.");
		return product;
	}
		
	public void removeDunnageProduct(BaseProduct product) {
		ProductTypeUtil.getProductDao(ProductType.getType(getProductType())).removeDunnage(product.getProductId());
	}
	
	public void updateDunnageContent(String productId, String dunnageNumber) {
		getLogger().info("Remove DunnageContent for dunnage" + dunnageNumber);
		DunnageContentId dunnageContentId = new DunnageContentId(dunnageNumber,productId);
		
		getDunnageContentDao().removeByKey(dunnageContentId);;
		
	}

	private DunnageContentDao getDunnageContentDao() {
		return getDao(DunnageContentDao.class);		
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
	
}
