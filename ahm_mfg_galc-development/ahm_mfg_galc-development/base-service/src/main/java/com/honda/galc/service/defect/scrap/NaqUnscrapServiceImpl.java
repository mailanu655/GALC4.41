package com.honda.galc.service.defect.scrap;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qics.ReuseProductResult;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.defect.NaqUnscrapService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.MultiLineHelper;
import com.honda.galc.util.ProductCheckUtil;

public class NaqUnscrapServiceImpl extends UnscrapServiceImpl implements NaqUnscrapService{
	private List<QiDefectResult> qiDefectResultList;
	ProductCheckUtil productCheckUtil;

	private static final String CLASS_NAME = "NaqUnscrapService";

	@Override
	protected void performUnscrap(BaseProduct product) {
		ReuseProductResult reuseProductResult = createReuseProductResult(product.getProductId());

		updateQiDefectResult(product);
		updateQiRepairResult(product);
		
		saveReuseProductResult(reuseProductResult);
		deleteExceptionalOutForProduct(product.getProductId());
		updateDefectAndLastTrackingStatus(product);
		getLogger().info("Product : " + product.getProductId() + " was sucessfully unscrapped.");
	}

	@Override
	protected boolean isValidProducts() {
		boolean areAllProductsValid = true;
		for(String productId : getProductIdList()) {
			BaseProduct product = findProduct(productId);
			if(product == null) {
				getErrorList().add("Product not found : " + productId);
				areAllProductsValid = false;
				getInvalidProductList().add(product);
			} else if(!isProductScrapped(product)) {
				getErrorList().add("Product : " + productId + " is not scrapped.");
				getInvalidProductList().add(product);
				areAllProductsValid = false;
			} else if(!isProducScrappable(product)) {
				getErrorList().add("Product : " + productId + " is not scrappable.");
				getInvalidProductList().add(product);
				areAllProductsValid = false;
			} else if(isLineIdCheckEnabled()) {
				getProductCheckUtil().setProduct(product);
				MultiLineHelper multiLineHelper = MultiLineHelper.getInstance(getProcessPointId());
				getProductCheckUtil().setProcessPoint(multiLineHelper.getProcessPointToUse(product));
				if(getProductCheckUtil().invalidPreviousLineCheck()) {
					getInvalidProductList().add(product);
					prepareNgReply("Invalid Tracking Status");
					areAllProductsValid = false;
				}
			} else {
				getProductList().add(product);
			}
		}
		if(!areAllProductsValid) {
			prepareNgReply(getErrorList().toString());
		}
		return areAllProductsValid;
	}

	public QiDefectResult prepareQiDefectResultForUnscrap(QiDefectResult qiDefectResult) {
		qiDefectResult.setCurrentDefectStatus((short) DefectStatus.NOT_FIXED.getId());
		qiDefectResult.setComment(unscrapReason);
		qiDefectResult.setUpdateUser(associateId);
		return qiDefectResult;
	}

	private void updateQiDefectResult(BaseProduct productToCheck) {
		// convert Scrapped status(8) and NotFixedScrapped status(10) to NotFixed status(6)
		List<QiDefectResult> resultList = findScrappedRelatedDefectsForProductId(productToCheck.getProductId());
		if (resultList != null && !resultList.isEmpty()) {
			List<QiDefectResult> defectResultList = new ArrayList<QiDefectResult>();
			for (QiDefectResult qiDefectResult : resultList) {
				if(qiDefectResult.getCurrentDefectStatus() == (short) DefectStatus.NON_REPAIRABLE.getId())
					qiDefectResult.setComment(unscrapReason);
				qiDefectResult.setCurrentDefectStatus((short) DefectStatus.NOT_FIXED.getId());				
				qiDefectResult.setUpdateUser(associateId);
				defectResultList.add(qiDefectResult);
			}
			ServiceFactory.getDao(QiDefectResultDao.class).updateAll(defectResultList);
		}
	}
	
	public void updateQiRepairResult(BaseProduct productToCheck) {
		// convert Scrapped status(8) and NotFixedScrapped status(10) to NotFixed status(6)
		List<QiRepairResult> resultList = findScrappedRelatedRepairsForProductId(productToCheck.getProductId());
		if(resultList!=null && !resultList.isEmpty()) {
			List<QiRepairResult> qiRepairtResultList = new ArrayList<QiRepairResult>(); 			
			for(QiRepairResult qiRepairResult : resultList) {
				if(qiRepairResult.getCurrentDefectStatus() == (short) DefectStatus.NON_REPAIRABLE.getId())	
					qiRepairResult.setComment(unscrapReason);
				qiRepairResult.setCurrentDefectStatus((short) DefectStatus.NOT_FIXED.getId());
				qiRepairResult.setUpdateUser(associateId);
				qiRepairtResultList.add(qiRepairResult);
			}

			ServiceFactory.getDao(QiRepairResultDao.class).updateAll(qiRepairtResultList);
		}
	}

	@Override
	public boolean isProductScrapped(BaseProduct productToCheck) {
		List<QiDefectResult> resultList = getQiDefectResultList(productToCheck);
		if(qiDefectResultList == null) {
			qiDefectResultList = new ArrayList<QiDefectResult>();
		} 
		qiDefectResultList.addAll(resultList);
		return (qiDefectResultList != null && !qiDefectResultList.isEmpty());
	}

	private boolean isLineIdCheckEnabled() {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPointId).isLineIdCheckEnabled();
	}

	public List<QiDefectResult> getQiDefectResultList(BaseProduct product) {
		return findScrappedDefectsForProductId(product.getProductId());
	}

	private List<QiDefectResult> findScrappedDefectsForProductId(String productId) {
		return ServiceFactory.getDao(QiDefectResultDao.class).findAllByProductIdAndCurrentDefectStatus(productId, (short) DefectStatus.NON_REPAIRABLE.getId());
	}
	
	private List<QiDefectResult> findScrappedRelatedDefectsForProductId(String productId) {
		List<Short> statusIds = new ArrayList<Short>();
		statusIds.add((short)DefectStatus.NON_REPAIRABLE.getId());
		statusIds.add((short)DefectStatus.NOT_FIXED_SCRAPPED.getId());				
		return ServiceFactory.getDao(QiDefectResultDao.class).findAllByProductId(productId, statusIds);
	}
	
	private List<QiRepairResult> findScrappedRelatedRepairsForProductId(String productId) {
		List<Short> statusIds = new ArrayList<Short>();
		statusIds.add((short)DefectStatus.NON_REPAIRABLE.getId());
		statusIds.add((short)DefectStatus.NOT_FIXED_SCRAPPED.getId());				
		return ServiceFactory.getDao(QiRepairResultDao.class).findAllByProductId(productId, statusIds);
	}
	
	public ProductCheckUtil getProductCheckUtil() {
		return productCheckUtil == null ? productCheckUtil = new ProductCheckUtil() : productCheckUtil;
	}

	@Override
	public String getClassName() {
		return CLASS_NAME ;
	}
}