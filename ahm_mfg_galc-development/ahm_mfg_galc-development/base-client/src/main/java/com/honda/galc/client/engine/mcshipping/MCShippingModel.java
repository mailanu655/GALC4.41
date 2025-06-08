package com.honda.galc.client.engine.mcshipping;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.dao.product.CrankshaftDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.ProductShippingDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Conrod;
import com.honda.galc.entity.product.Crankshaft;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.ProductShipping;
import com.honda.galc.entity.product.ProductShippingId;
import com.honda.galc.property.ProductShippingPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.ProductSpecUtil;

/**
 * 
 * 
 * <h3>MCShippingModel Class description</h3>
 * <p> MCShippingModel description </p>
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
 * Sep 10, 2014
 *
 *
 */
public class MCShippingModel extends AbstractModel{
	
	private List<ProductShipping> activeDunnages = new ArrayList<ProductShipping>();
	private List<ProductShipping> allActiveDunnages = new ArrayList<ProductShipping>();
	private Boolean isBroadcast;
	
	public List<ProductShipping> findAllActiveDunnages() {
		return getDao(ProductShippingDao.class).findAllActiveDunnages();
	}
	
	public void reloadActiveDunnages() {
		allActiveDunnages = findAllActiveDunnages();
		if(activeDunnages.size() > 0) activeDunnages.clear();
		for(ProductShipping ps : allActiveDunnages)
			if(ps.getTrackingStatus().equals(getShippingDestination()))
				activeDunnages.add(ps);
		
	}
	
	public String[] getActiveTrailers() {
		Set<String> trailers = new HashSet<String>();
		for(ProductShipping productShipping : getActiveDunnages()) {
			trailers.add(productShipping.getId().getTrailerNumber());
		}
		return trailers.toArray(new String[trailers.size()]);
	}
	
	public List<ProductShipping> getActiveDunnages() {
		if(activeDunnages.isEmpty())  reloadActiveDunnages();
		return activeDunnages;
	}
	
	public List<ProductShipping> selectDunnages(ProductType productType, String trailerNumber) {
		List<ProductShipping> productShippings = new ArrayList<ProductShipping>();
		if(!StringUtils.isEmpty(trailerNumber) && productType!=null) {
			for(ProductShipping productShipping : getActiveDunnages()) {
				if(trailerNumber.equalsIgnoreCase(productShipping.getId().getTrailerNumber()) && 
						productType.toString().equalsIgnoreCase(productShipping.getProductTypeString()))
					productShippings.add(productShipping);
			}
		}
		return productShippings;
	}
	
	public List<ProductShipping> selectDunnages(String trailerNumber) {
		List<ProductShipping> productShippings = new ArrayList<ProductShipping>();
		for(ProductShipping mcShipping : getActiveDunnages()) {
			if(trailerNumber.equalsIgnoreCase(mcShipping.getId().getTrailerNumber()))
				productShippings.add(mcShipping);
		}
		return productShippings;
	}	 
	
	public int getTotalCount(ProductType productType, String trailerNumber) {
		int totalCount = 0;
		for(ProductShipping item : selectDunnages(productType, trailerNumber)) {
			totalCount += item.getCount();
		}
		return totalCount;
	}
	
	public ProductShipping selectLoadedDunnage(String dunnageNumber) {
		for(ProductShipping dunnage : activeDunnages) {
			if(dunnageNumber.equalsIgnoreCase(dunnage.getDunnage())) return dunnage;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<BaseProduct> findAllByDunnage(String dunnageNumber, String productType) {
		return (List<BaseProduct>) ProductTypeUtil.getProductDao(productType).findAllByDunnage(dunnageNumber);
	}
	
	public String getProductType() {
		return getApplicationContext().getApplicationPropertyBean().getProductType();
	}
	
	public List<Head> findHeads(String dunnageNumber) {
		return getDao(HeadDao.class).findAllByDunnage(dunnageNumber);
	}
	
	public List<Block> findBlocks(String dunnageNumber) {
		return getDao(BlockDao.class).findAllByDunnage(dunnageNumber);
	}
	
	public List<Conrod> findConrods(String dunnageNumber) {
		return getDao(ConrodDao.class).findAllByDunnage(dunnageNumber);
	}
	
	public List<Crankshaft> findCrankshafts(String dunnageNumber) {
		return getDao(CrankshaftDao.class).findAllByDunnage(dunnageNumber);
	}
	
	public List<ProductShipping> findAllShipments(String trailerNumber, String dunnage) {
		return getDao(ProductShippingDao.class).findAllShippments(trailerNumber, dunnage);
	}
	
	public ProductShippingPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(ProductShippingPropertyBean.class, getProcessPointId());
	}
	
	public void completeTrailer(String trailerNumber) {
		ProductShippingDao shippingDao = getDao(ProductShippingDao.class);
		TrackingService trackingService = getService(TrackingService.class);
		
		List<ProductShipping> shipItems = shippingDao.findAllShippments(trailerNumber);
		
		for(ProductShipping shipItem : shipItems) {
			ProductTypeUtil util = ProductTypeUtil.getTypeUtil(shipItem.getProductTypeString());
			BaseProduct product = util.findProduct(shipItem.getId().getProductId());
			
			if(product == null) continue;
			
			trackingService.track(product, getProcessPointId());
			
			if(isBroadcastConfigured())
				doBroadcast(shipItem);

			if(getPropertyBean().isClearDunnage())
				util.getProductDao().removeDunnage(shipItem.getId().getProductId());
		}
		
		shippingDao.shipTrailer(trailerNumber, getProcessPoint().getLineId(),getProcessPointId());
	}

	private void doBroadcast(ProductShipping shipItem) {
		try{
			BroadcastService broadcastService = ServiceFactory.getService(BroadcastService.class);
			DataContainer dc = new DefaultDataContainer();
			dc.put(DataContainerTag.PRODUCT_ID, shipItem.getId().getProductId());
			dc.put(DataContainerTag.PRODUCT_TYPE, shipItem.getProductTypeString());
			broadcastService.broadcast(getProcessPointId(), dc);
			Logger.getLogger().info("invoke broadcast service:" + dc.toString());
		}catch(Exception e){
			Logger.getLogger().warn(e, "Failed to invoke broadcast service");
		}
	}
	
	protected boolean isBroadcastConfigured() {
		if(isBroadcast == null) {
			List<BroadcastDestination> destinations = 
					ServiceFactory.getDao(BroadcastDestinationDao.class)
					.findAllByProcessPointId(getProcessPointId());
			isBroadcast = !destinations.isEmpty();
		}

		return isBroadcast;
	}
	
	public void removeDunnage(String trailerNumber, String dunnage) {
		getDao(ProductShippingDao.class).removeDunnage(trailerNumber, dunnage);
	}
	
	public void checkActiveTrailerCount() {
		if(isTrailerCountFull())
			throw new TaskException("You are only allowed " + getPropertyBean().getActiveTrailerCount() + " active trailers");
	}
	
	public boolean isTrailerCountFull() {
		return getActiveTrailers().length >= getPropertyBean().getActiveTrailerCount();
	}
	
	public void checkConsumedDunnage(String trailerNumber) {
		List<String> dunnages = getDao(ProductShippingDao.class).findAllConsumedDunnages(trailerNumber);
		if(!dunnages.isEmpty())
			throw new TaskException("Cannot complete trailer. Consumed Dunnages: " + StringUtils.join(dunnages, Delimiter.COMMA));
	}
	
	public List<String> loadProductTypesFromProperties() {
		return Arrays.asList(getPropertyBean().getProductTypes());
	}
	
	public Map<String, String> findAllProdTypesInDunnage(String dunnage, List<String> prodTypesFromProperties) {
		Map<String, String> productTypes = new HashMap<>();
		for (String productType : prodTypesFromProperties) {
			if (findAllByDunnage(dunnage, productType).size() > 0) {
				productTypes.put("current", productType);
			} else {
				productTypes.put("other", productType);
			}
		}
		return productTypes;
	}
	
	public List<BaseProduct> getAllProductsInDunnage(String dunnage, List<String> prodTypesFromProperties) {
		List<BaseProduct> products = new ArrayList<>();
		for (String productType : prodTypesFromProperties) {
			products.addAll(findAllByDunnage(dunnage, productType));
		}
		return products;
	}
	
	public void validateDunnageInfo(String dunnage, String trailerNumber, List<ProductShipping> dunnagesOnTrailer, List<BaseProduct> products) {
		dunnage = dunnage.trim().toUpperCase();
		
		ProductShipping loadedDunnage = selectLoadedDunnage(dunnage);
		if (loadedDunnage != null) {
			throw new TaskException(
					"Dunnage " + dunnage + " has been loaded into trailer " + loadedDunnage.getId().getTrailerNumber());
		}
		
		int totalDunnageOnTrailer = dunnagesOnTrailer.size();
		int totalDunnageLimit = getPropertyBean().getTotalDunnageLimit();
		if (totalDunnageOnTrailer >= totalDunnageLimit) {
			throw new TaskException("Trailer " + trailerNumber + " has reached its total dunnage limit");
		}
		
		if (products.isEmpty()) {
			throw new TaskException("Dunnage " + dunnage + " does not exist");
		}
	}

	public void validateAndCheckProduct(BaseProduct product, int totalCurrentProductOnTrailer, int totalOtherProductOnTrailer, String dunnage) {
	    if (product instanceof Block) {
	        validateDunnage(ProductType.BLOCK, totalOtherProductOnTrailer, totalCurrentProductOnTrailer,
	            getPropertyBean().getBlockDunnageSize(), getPropertyBean().getBlockDunnageLimit(),dunnage);
	    } else if (product instanceof Head) {
	        validateDunnage(ProductType.HEAD, totalOtherProductOnTrailer, totalCurrentProductOnTrailer,
	            getPropertyBean().getHeadDunnageSize(), getPropertyBean().getHeadDunnageLimit(),dunnage);
	    } else if (product instanceof Conrod) {
	        validateDunnage(ProductType.CONROD, totalOtherProductOnTrailer, totalCurrentProductOnTrailer,
	            getPropertyBean().getConrodDunnageSize(), getPropertyBean().getConrodDunnageLimit(),dunnage);
	    } else if (product instanceof Crankshaft) {
	        validateDunnage(ProductType.CRANKSHAFT, totalOtherProductOnTrailer, totalCurrentProductOnTrailer,
	            getPropertyBean().getCrankshaftDunnageSize(), getPropertyBean().getCrankshaftDunnageLimit(),dunnage);
	    }
	    
	    if (!this.validateModel(product)) {
	        throw new TaskException(product.getClass().getSimpleName() + " model " + product.getModelCode() + " is not allowed.");
	    }
	    
	    Map<String, Object> checkResults = ProductCheckUtil.check(product, getProcessPoint(),
	        getProductCheckTypes(product.getProductType()), getProductInstalledParts(product.getProductType()));
	    for (Map.Entry<String, Object> entry : checkResults.entrySet()) {
	        throw new TaskException(product.getClass().getSimpleName() + " Check failed : "
	            + product.getProductId() + " - " + entry.getKey() + " : " + entry.getValue().toString());
	    }
	}
	
	private void validateDunnage(ProductType productType, int otherProductDunnageOnTrailer,
	    int currentProductDunnageOnTrailer, int dunnageSize, int dunnageLimit, String dunnage) {
	    if (!getPropertyBean().isAllowMixedProducts() && otherProductDunnageOnTrailer > 0) {
	        throw new TaskException(productType.toString() + " Dunnage is not allowed to be mixed with other product dunnages");
	    } else if (findAllByDunnage(dunnage,productType.toString()).size() != dunnageSize) {
	        throw new TaskException("Dunnage " + dunnage + " is loaded with incorrect size "+findAllByDunnage(dunnage,productType.toString()).size()+" for " + productType.toString(), "Dunnage Size");
	    } else if (currentProductDunnageOnTrailer >= dunnageLimit) {
	        throw new TaskException("Could not load dunnage " + dunnage +" due to " + productType.toString() + " Dunnage Limit");
	    }
	}
	
	public int countDunnagesByType(ProductType productType, String trailerNumber) {
	    return selectDunnages(productType, trailerNumber).size();
	}

	private String[] getProductCheckTypes(ProductType productType) {
	    switch (productType) {
	        case BLOCK:
	            return getPropertyBean().getBlockCheckTypes();
	        case HEAD:
	            return getPropertyBean().getHeadCheckTypes();
	        case CONROD:
	            return getPropertyBean().getConrodCheckTypes();
	        case CRANKSHAFT:
	            return getPropertyBean().getCrankshaftCheckTypes();
	        default:
	            return null;
	    }
	}

	private List<String> getProductInstalledParts(ProductType productType) {
	    switch (productType) {
	        case BLOCK:
	            return Arrays.asList(getPropertyBean().getBlockInstalledParts());
	        case HEAD:
	            return Arrays.asList(getPropertyBean().getHeadInstalledParts());
	        case CONROD:
	            return Arrays.asList(getPropertyBean().getConrodInstalledParts());
	        case CRANKSHAFT:
	            return Arrays.asList(getPropertyBean().getCrankshaftInstalledParts());
	        default:
	            return Collections.emptyList();
	    }
	}

	
	public void saveProducts(List<BaseProduct> diecasts,String trailerNumber,Date date) {
		List<ProductShipping> productShippings = new ArrayList<ProductShipping>();
		
		for(BaseProduct diecast : diecasts) {
			ProductShipping productShipping = new ProductShipping();
			ProductShippingId id = new ProductShippingId();
			id.setProductId(diecast.getProductId());
			id.setTrailerNumber(trailerNumber);
			id.setProcessPointId(getProcessPointId());
			productShipping.setId(id);
			productShipping.setProductTypeString(diecast.getProductType().toString());
			productShipping.setDunnage(diecast.getDunnage());
			productShipping.setShipDate(date);
			productShipping.setTrailerStatus(0);
			productShipping.setTrackingStatus(getShippingDestination());
			productShippings.add(productShipping);
		}
		getDao(ProductShippingDao.class).saveAll(productShippings);
	}
	
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	private String getShippingDestination() {
		return getProcessPoint().getLineId();
	}
		
	private Boolean validateModel(BaseProduct product){
		ProductType prodType = product.getProductType();
		StringBuffer prodSpec = new StringBuffer(ProductSpecUtil.extractModelYearCode(product.getProductSpecCode()));
		prodSpec.append(ProductSpecUtil.extractModelCode(product.getProductSpecCode()));
		List<BuildAttribute> buildAttributes = getDao(BuildAttributeDao.class).findAllMatchBuildAttributes(getPropertyBean().getBuildAttrbuteKey());
		for (BuildAttribute att : buildAttributes){
			if (	att.getProductType().equalsIgnoreCase(prodType.toString()) && 
					att.getProductSpecCode().replaceAll("~", "").equalsIgnoreCase(prodSpec.toString())){
				return false;
			}
		}
		return true;
	}

	public void checkTrailer(String trailerNumber) throws Exception {
		for(ProductShipping ps : allActiveDunnages)
			if(ps.getId().getTrailerNumber().equals(trailerNumber) && !ps.getTrackingStatus().equals(getShippingDestination()))
				throw new Exception("Trailer:" + trailerNumber + " is loaded for " + ps.getTrackingStatus());
		
	}

	public void checkDunnage(String dunnage) throws Exception {
		for(ProductShipping ps : allActiveDunnages)
			if(dunnage.equals(ps.getDunnage()))
				throw new Exception("Dunnage:" + dunnage + " is loaded in " + ps.getId().getTrailerNumber());
		
	}
}
