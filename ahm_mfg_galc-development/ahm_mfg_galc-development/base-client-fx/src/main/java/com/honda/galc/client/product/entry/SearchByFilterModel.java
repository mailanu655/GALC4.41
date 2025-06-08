package com.honda.galc.client.product.entry;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.mvc.IModel;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

public class SearchByFilterModel implements IModel {

	private static SearchByFilterModel _instance = null;
	private ProductTypeData productTypeData;
	@Override
	public void reset() {
	}

	private SearchByFilterModel(ProductTypeData pType)  {
		setProductTypeData(pType);
	}
	
	/**
	 * @return the productTypeData
	 */
	public ProductTypeData getProductTypeData() {
		return productTypeData;
	}

	/**
	 * @param productTypeData the productTypeData to set
	 */
	public void setProductTypeData(ProductTypeData productTypeData) {
		this.productTypeData = productTypeData;
	}

	public static SearchByFilterModel getInstance(ProductTypeData pType)  {
		if(_instance == null)  {
			_instance = new SearchByFilterModel(pType);
		}
		return _instance;
	}
	
	public List<? extends BaseProduct> findByProductionLot(String productionLot, int count) {
		if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(productionLot)) 
			return ProductTypeUtil.getProductDao(productTypeData.getProductType()).findAllByProductionLot(productionLot, count); 
		else 
			return new ArrayList<Product>();
	}

	public List<? extends BaseProduct> findByTrackingStatus(String trackingStatus, int count) {
		if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(trackingStatus)) 
			return ProductTypeUtil.getProductDao(productTypeData.getProductType()).findByTrackingStatus(trackingStatus, count); 
		else 
			return new ArrayList<Product>();
	}
	
	public List<? extends BaseProduct> findBySeqRange(String start, String end, int count) {
		if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) 
			return ((FrameDao)ProductTypeUtil.getProductDao(ProductType.FRAME)).findAllByAfOnSequenceNumber(Integer.parseInt(start),Integer.parseInt(end),count); 
		else 
			return new ArrayList<Product>();
	}

	public List<? extends BaseProduct> findByPlantSeqRange(String plant, String start, String end, int count) {
		if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) 
			return ((FrameDao)ProductTypeUtil.getProductDao(ProductType.FRAME)).findAllByPlantAfOnSequenceNumber(plant,Integer.parseInt(start),Integer.parseInt(end), count); 
		else 
			return new ArrayList<Product>();
	}

	public List<? extends BaseProduct> findByProductIdRange(String start, String end, int count) {
		if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) 
			return ((FrameDao)ProductTypeUtil.getProductDao(ProductType.FRAME)).findAllByProductIdRange(start, end, count); 
		else 
			return new ArrayList<Product>();
	}

	public List<? extends BaseProduct> findByPlantProductIdRange(String plant, String start, String end, int count) {
		if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) 
			return ((FrameDao)ProductTypeUtil.getProductDao(ProductType.FRAME)).findAllByPlantProductIdRange(plant, start, end, count); 
		else 
			return new ArrayList<Product>();
	}

	public List<String> findProductIdsBySeqRange(String plant, String start, String end, int count) {
		List<String> productIds = null;
		List<? extends BaseProduct> productList = null;
		if(StringUtils.isBlank(plant))  {
			productList = findBySeqRange(start, end, count);
		}
		else  {
			productList = findByPlantSeqRange(plant,start, end, count);
			
		}
		productIds = getListofProductIds(productList);
		return productIds;
	}

	public List<String> findProductIdsByProductionLot(String productionLot, int count) {
		List<String> productIds = null;
		List<? extends BaseProduct> productList = findByProductionLot(productionLot, count);
		productIds = getListofProductIds(productList);
		return productIds;
	}

	public List<String> findProductIdsByTrackingStatus(String trackingStatus, int count) {
		List<String> productIds = null;
		List<? extends BaseProduct> productList = findByTrackingStatus(trackingStatus, count);
		productIds = getListofProductIds(productList);
		return productIds;
	}

	public List<String> findProductIdsByProductIdRange(String plant, String start, String end, int count) {
		List<String> productIds = null;
		List<? extends BaseProduct> productList = null;
		if(StringUtils.isBlank(plant))  {
			productList = findByProductIdRange(start, end, count);
		}
		else  {
			productList = findByPlantProductIdRange(plant, start, end, count);
		}
		productIds = getListofProductIds(productList);
		return productIds;
	}

	private List<String> getListofProductIds(List<? extends BaseProduct> productList)  {
		List<String> productIds = new ArrayList<String>();
		for(BaseProduct bp : productList)  {
			productIds.add(bp.getProductId());
		}
		return productIds;
		
	}
	
	public List<Line> findAllTrackingStatus() {
		List<Line> trackingStatusLineLst = new ArrayList<Line>();
		if(!StringUtils.isEmpty(productTypeData.getProductType().name())){
			List<String> trackingStatusLst= ProductTypeUtil.getProductDao(productTypeData.getProductType()).findAllTrackingStatus();
			for (String trackingStatus:trackingStatusLst) {
				if (!StringUtils.isBlank(trackingStatus)) {
					Line line = ServiceFactory.getDao(LineDao.class).findByKey(trackingStatus);
					if (line != null) trackingStatusLineLst.add(line);
				}
			}
		}
		return trackingStatusLineLst;
	}

	public List<Line> findAllTrackingStatusByPlant(ProcessPoint selected) {
		List<Line> trackingLines = new ArrayList<Line>();
		ProductType productType = productTypeData.getProductType();
		String selectedPlant = selected.getPlantName();
		if(!StringUtils.isEmpty(productType.name())){
			List<String> allTrackingStatus= ProductTypeUtil.getProductDao(productType).findAllTrackingStatusByPlant(selectedPlant);
			for (String trackingStatus:allTrackingStatus) {
				if (!StringUtils.isBlank(trackingStatus)) {
					Line line = ServiceFactory.getDao(LineDao.class).findByKey(trackingStatus);
					if (line != null) trackingLines.add(line);
				}
			}
		}
		return trackingLines;
	}

}
