package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.BaseSubProductDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.dao.product.CrankshaftDao;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.DunnageContentDao;
import com.honda.galc.dao.product.DunnageDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.MissionCaseDao;
import com.honda.galc.dao.product.MovablePulleyDriveDao;
import com.honda.galc.dao.product.MovablePulleyDrivenDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.dao.product.PulleyShaftDriveDao;
import com.honda.galc.dao.product.PulleyShaftDrivenDao;
import com.honda.galc.dao.product.TorqueConverterCaseDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Dunnage;
import com.honda.galc.entity.product.DunnageContent;
import com.honda.galc.entity.product.DunnageContentId;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.property.DunnagePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.PropertyComparator;

/**
 * 
 * <h3>DunnageMaintModel Class description</h3>
 * <p> DunnageMaintModel description </p>
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
 * @author L&T Infotech<br>
 * April 20, 2017
 *
 *
 */

public class DunnageMaintModel extends  QiModel {

	private static final DateFormat OFF_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings("unchecked")
	public List<BaseProduct> findAllByDunnage(String dunnageNumber) {
		return (List<BaseProduct>) ProductTypeUtil.getProductDao(getProductType()).findAllByDunnage(dunnageNumber);
	}

	public BaseProduct findProduct(String pin) {
		return ProductTypeUtil.getProductDao(getProductType()).findBySn(pin);
	}

	public int getDunnageCapacity() {
		String type = getProductType();
		if (ProductType.BLOCK.toString().equalsIgnoreCase(type)) {
			return getProperty().getBlockDunnageCartQuantity();
		} else if (ProductType.HEAD.toString().equalsIgnoreCase(type)) {
			return getProperty().getHeadDunnageCartQuantity();
		} else
			return getProperty().getDunnageCartQuantity();
	}

	public DunnagePropertyBean getProperty() {
		return PropertyService.getPropertyBean(DunnagePropertyBean.class, getApplicationContext().getApplicationId());
	}
	public boolean isShippingSupported() {
		return !StringUtils.isBlank(getProperty().getShippingProcessPointId());
	}

	public boolean isOffConfigured() {
		return getProperty().getOffProcessPointIds() != null && getProperty().getOffProcessPointIds().length > 0;
	}
	public boolean isInsertDunnageContent() {
		return getProperty().isInsertDunnageContent();
	}

	public boolean isNewDunnage(String dunnageNumber){
		Dunnage dunnage = getDunnageDao().findByKey(dunnageNumber);
		return (null==dunnage || StringUtils.isEmpty(dunnage.getDunnageId()));
	}
	public void shipProducts(List<BaseProduct> products) {
		String processPointId = getProperty().getShippingProcessPointId();
		if (products == null || products.isEmpty()) {
			return;
		}
		TrackingService service = ServiceFactory.getService(TrackingService.class);
		for (BaseProduct product : products) {
			service.track(product, processPointId);
		}
	}

	public void updateDunnageInfo(String dunnageNumber, String expectedQty, String productSpec)
	{
		Dunnage dunnage = new Dunnage();
		dunnage.setDunnageId(dunnageNumber);
		dunnage.setExpectedQty(Integer.valueOf(expectedQty));
		dunnage.setProductSpecCode(productSpec);
		getDunnageDao().update(dunnage);

	}

	public boolean isFirstProductInDunnage(String dunnageNumber){
		return getDunnageContentDao().findAllProductsInDunnage(dunnageNumber).size()==0;
	}

	private DunnageContentDao getDunnageContentDao() {
		return getDao(DunnageContentDao.class);		
	}

	private DunnageDao getDunnageDao() {
		return getDao(DunnageDao.class);		
	}

	public int addToDunnage(BaseProduct product, String dunnageNumber) {
		return ProductTypeUtil.getProductDao(getProductType()).updateDunnage(product.getProductId(), dunnageNumber, getDunnageCapacity());

	}

	public void removeFromDunnage(BaseProduct product) {
		if (getProperty().isInsertDunnageContent()) {
			DunnageContentId dcid = new DunnageContentId();
			dcid.setDunnageId(product.getDunnage());
			dcid.setProductId(product.getProductId());
			ServiceFactory.getDao(DunnageContentDao.class).removeByKey(dcid);
		}
		ProductTypeUtil.getProductDao(getProductType()).removeDunnage(product.getProductId());

	}

	@SuppressWarnings("rawtypes")
	private DiecastDao getDieCastDao(String productType) {
		if(ProductType.BLOCK.toString().equals(productType))
			return getDao(BlockDao.class);
		else if(ProductType.HEAD.toString().equals(productType))
			return getDao(HeadDao.class);
		else if(ProductType.CONROD.toString().equals(productType))
			return getDao(ConrodDao.class);
		else if(ProductType.CRANKSHAFT.toString().equals(productType))
			return getDao(CrankshaftDao.class);
		else if(ProductType.TCCASE.toString().equals(productType))
			return getDao(TorqueConverterCaseDao.class);
		else if(ProductType.MCASE.toString().equals(productType))
			return getDao(MissionCaseDao.class);
		else return null;
	}
	
	@SuppressWarnings("rawtypes")
	private BaseSubProductDao getSubProductDao(String productType) {
		if(ProductType.MPDR.toString().equals(productType))
			return getDao(MovablePulleyDriveDao.class);
		else if(ProductType.MPDN.toString().equals(productType))
			return getDao(MovablePulleyDrivenDao.class);
		else if(ProductType.PSDR.toString().equals(productType))
			return getDao(PulleyShaftDriveDao.class);
		else if(ProductType.PSDN.toString().equals(productType))
			return getDao(PulleyShaftDrivenDao.class);
		else return null;
	}
	

	public DunnageContent findDunnageContentByDunnageId(String dunnageNumber){
		return getDunnageContentDao().findById(dunnageNumber);
	}

	@SuppressWarnings("unchecked")
	public List<BaseProduct> selectDunnageProducts(String dunnageNumber) {
		return (List<BaseProduct>) ProductTypeUtil.getProductDao(getProductType()).findAllByDunnage(dunnageNumber);
	}

	public List<Map<String, Object>> selectDunnageProductsData(String dunnageNumber) {

		List<BaseProduct> products = selectDunnageProducts(dunnageNumber);

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (products == null || products.isEmpty()) {
			return data;
		}

		if (products != null && !products.isEmpty()) {
			Collections.sort(products, new PropertyComparator<BaseProduct>(BaseProduct.class, "updateTimestamp"));
			Collections.reverse(products);
		}

		return mapDunnageMaintData(products, getApplicationContext().getProductTypeData().getProductType(), getProperty());
	}

	public static List<Map<String, Object>> mapDunnageMaintData(List<BaseProduct> products, ProductType productType, DunnagePropertyBean property) {
		if (ProductTypeUtil.isInstanceOf(productType, DieCast.class)) {
			return mapDieCastData(products, productType, property);
		} else if (ProductTypeUtil.isInstanceOf(productType, MbpnProduct.class)) {
			return mapMbpnData(products, productType, property);
		} else {
			return mapProductData(products, productType, property);
		}
	}
		public List<BaseProduct> filterShippable(List<Map<String, Object>> list) {
			List<BaseProduct> shippableProducts = new ArrayList<BaseProduct>();
			if (list == null) {
				return shippableProducts;
			}
			for (Map<String, Object> map : list) {
				Object shippable = map.get("shippable");
				if (Boolean.TRUE.equals(shippable)) {
					shippableProducts.add((BaseProduct) map.get("product"));
				}
			}
			return shippableProducts;
		}
	
	/**
	 * 
	 * @param products
	 * @param productType
	 * @param property
	 * @return
	 */
	private static List<Map<String, Object>> mapDieCastData(List<BaseProduct> products, ProductType productType, DunnagePropertyBean property) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (BaseProduct product : products) {
			DieCast dieCastProduct = (DieCast) product;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("product", product);
			map.put("dcSerialNumber", dieCastProduct.getDcSerialNumber());
			map.put("mcSerialNumber", dieCastProduct.getMcSerialNumber());
			map.put("model", dieCastProduct.getModelCode());

			boolean repaired = product.getDefectStatus() == null || DefectStatus.REPAIRED.equals(product.getDefectStatus());
			boolean onHold = product.getHoldStatus() == 0 ? false : true;
			boolean offed = false;

			map.put("machineId", product.getProductNumberDef().getLine(dieCastProduct.getDcSerialNumber()));
			map.put("dieId", product.getProductNumberDef().getDie(dieCastProduct.getDcSerialNumber()));
			map.put("defect", repaired ? "OK" : "NG");
			map.put("hold", onHold ? "NG" : "OK");
			addLineOffandShippingStatus(productType, property, product, map, repaired, onHold, offed);
			
			if (property.isInsertDunnageContent()) {
				DunnageContentId dcid = new DunnageContentId();
				dcid.setDunnageId(product.getDunnage());
				dcid.setProductId(product.getProductId());
				DunnageContent dcd = ServiceFactory.getDao(DunnageContentDao.class).findByKey(dcid);
				if(dcd!=null)
					map.put("matrix", dcd.getDunnageRow()+","+dcd.getDunnageColumn()+","+dcd.getDunnageLayer());
			}
			data.add(map);
		}
		return data;
	}
	
	private static List<Map<String, Object>> mapMbpnData(List<BaseProduct> products, ProductType productType, DunnagePropertyBean property) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (BaseProduct product : products) {
			MbpnProduct mbpnProduct = (MbpnProduct) product;
			Map<String, Object> map = new HashMap<String, Object>();

			boolean repaired = product.getDefectStatus() == null || DefectStatus.REPAIRED.equals(product.getDefectStatus());
			boolean onHold = product.getHoldStatus() == 0 ? false : true;
			boolean offed = false;

			map.put("product", product);
			map.put("productId", mbpnProduct.getProductId());
			map.put("currentProductSpecCode", mbpnProduct.getProductSpecCode());
			map.put("currentOrderNo", mbpnProduct.getCurrentOrderNo());
			map.put("containerId", mbpnProduct.getContainerId());
			map.put("trackingSeq", mbpnProduct.getTrackingSeq());
			map.put("defect", repaired ? "OK" : "NG");
			map.put("onHold", onHold ? "NG" : "OK");
			addLineOffandShippingStatus(productType, property, product, map, repaired, onHold, offed);
			if (property.isInsertDunnageContent()) {
				
				//getting the dunnage matrix from DUNNAGE_CONTENT_TBX
				DunnageContentId dcid = new DunnageContentId();
				dcid.setDunnageId(product.getDunnage());
				dcid.setProductId(product.getProductId());
				DunnageContent dcd = ServiceFactory.getDao(DunnageContentDao.class).findByKey(dcid);
				if(dcd!=null)
					map.put("matrix", dcd.getDunnageRow()+","+dcd.getDunnageColumn()+","+dcd.getDunnageLayer());
			}
			data.add(map);
		}
		return data;
	}
	
	private static List<Map<String, Object>> mapProductData(List<BaseProduct> products, ProductType productType, DunnagePropertyBean property) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (BaseProduct product : products) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("product", product);
			map.put("productId", product.getProductId());
			map.put("productSpecCode", product.getProductSpecCode());

			boolean repaired = product.getDefectStatus() == null || DefectStatus.REPAIRED.equals(product.getDefectStatus());
			boolean onHold = product.getHoldStatus() == 0 ? false : true;
			boolean offed = false;

			map.put("defect", repaired ? "OK" : "NG");
			map.put("hold", onHold ? "NG" : "OK");

			addLineOffandShippingStatus(productType, property, product, map, repaired, onHold, offed);

			data.add(map);
		}
		return data;
	}
	
	@SuppressWarnings("unchecked")
	private static void addLineOffandShippingStatus(ProductType productType, DunnagePropertyBean property, BaseProduct product, Map<String, Object> map, boolean repaired, boolean onHold, boolean offed) {
		ProductHistoryDao<? extends ProductHistory, ?> productHistoryDao = ProductTypeUtil.getProductHistoryDao(productType);
		List<ProductHistory> histories;
		java.util.Date date;
		if (property.getOffProcessPointIds() != null && property.getOffProcessPointIds().length > 0) {
			offed = false;
			String[] offProcessPointIds = property.getOffProcessPointIds();
			for (String processPointId : offProcessPointIds) {
				histories = (List<ProductHistory>) productHistoryDao.findAllByProductAndProcessPoint(product.getProductId(), processPointId);
				if (histories.size() > 0) {
					offed = true;
					date = histories.get(0).getActualTimestamp();
					for (int i = 1; i < histories.size(); i++) {
						if (histories.get(i).getActualTimestamp().after(date)) {
							date = histories.get(i).getActualTimestamp();
						}
					}
					map.put("offDate", OFF_DATE_FORMAT.format(date));
					break;
				}
			}
			map.put("offedLabel", offed ? "OK" : "NG");
		}
		if (!StringUtils.isBlank(property.getShippingProcessPointId())) {
			String shippingProcessPointId = property.getShippingProcessPointId();
			boolean shipped = productHistoryDao.hasProductHistory(product.getProductId(), shippingProcessPointId);
			map.put("shippedLabel", shipped ? "Yes" : "No");
			
		}
		boolean shippable = repaired && !onHold && offed;
		map.put("shippable", shippable);
	}
	
	public Dunnage findDunnageById(String dunnageNumber) {
		return getDao(DunnageDao.class).findByKey(dunnageNumber);
	}
	
	public void updateDieCaseDunnage(String productType, String productId, String dunnageNumber) {
		getDieCastDao(productType).updateDunnage(productId, dunnageNumber);
	}
	
	public void updateSubProductDunnage(String productType, String productId, String dunnageNumber, int dunnageCapacity) {
		getSubProductDao(productType).updateDunnage(productId, dunnageNumber,dunnageCapacity);
	}
	
	public DieCast findDieCastByProductId(String productType, String productId) {
		return (DieCast) getDieCastDao(productType).findBySn(productId);
	}
	
	public SubProduct findSubProductByProductId(String productType, String productId) {
		return (SubProduct) getSubProductDao(productType).findBySn(productId);
	}
	
	public void updateDunnage(Dunnage dunnage) {
		getDunnageDao().update(dunnage);
	}
	
	public void updateDunnageContent(DunnageContent dunnageContent) {
		getDunnageContentDao().update(dunnageContent);
	}
}
