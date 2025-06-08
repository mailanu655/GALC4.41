package com.honda.galc.service.defect.scrap;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.exception.InputValidationException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.dao.qics.ReuseProductResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.ScrappedProductDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.ReuseProductResult;
import com.honda.galc.entity.qics.ReuseProductResultId;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.UnscrapPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.notification.service.IProductScrapNotification;
import com.honda.galc.service.defect.UnscrapService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.StringUtil;

public class UnscrapServiceImpl implements UnscrapService {
	private static final String CLASS_NAME = "UnscrapService";

	private List<String> productIdList;
	private List<String> errorList;
	protected List<BaseProduct> productList;
	protected List<BaseProduct> invalidProductList;
	
	protected String productType;
	protected String associateId;
	protected String unscrapReason;
	protected String applicationId;
	protected String processPointId;

	protected DefectResult defectResult;
	private List<ScrappedProductDto> scrappedProductDtoList;
	protected DataContainer returnDc = new DefaultDataContainer();
	protected Logger logger;

	protected HeadLessPropertyBean propertyBean;

	@Override
	public DataContainer validateProductsForUnscrap(DefaultDataContainer data) {
		try {
			parseVerifyDataContainer(data);
			setLogger();
			initReturnDc();
			verifyInputDataForUnscrap();
			isValidProducts();
			if(getProductList().size() >= 1) { 
				getLogger().info("Products " + getProductList().toString() + " are valid for scrap");
				populateScrappedProductDtoList();
				if(getErrorList().isEmpty()) {
					prepareProductVerifyCompleteReply();
				}
			}
		} catch(TaskException e) {
			return returnDc;
		} catch(Exception e) {
			getLogger().error(e, e.getMessage(), getClassName());
			returnDc.put(TagNames.REQUEST_RESULT.name(), LineSideContainerValue.NOT_COMPLETE);
			returnDc.put(TagNames.MESSAGE.name(), "Unknown error occured while verifying products for unscrap");
		}
		return returnDc;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public DataContainer unscrapProduct(DefaultDataContainer data) {
		try {
			returnDc = new DefaultDataContainer();
			parseDataContainer(data);
			setLogger();
			getLogger().info(getClassName() + " begin unscrap ", data.toString());
			initReturnDc();
			verifyInputData();
			getLogger().info("Performing Unscrap on " + productIdList.size() + " products.");

			if(isValidProducts()) {
				for(BaseProduct product : productList) {
					performUnscrap(product);
				}
				prepareUnscrapCompleteReply();
			} else {
				getLogger().warn("Products : " + getInvalidProductList().toString() + " could not be unscrapped");
				throw new TaskException(returnDc.getString(TagNames.MESSAGE.name()));
			}
		} catch(TaskException e) {
			return returnDc;
		} catch(Exception e) {
			getLogger().error(e, e.getMessage(), getClassName());
			returnDc.put(TagNames.REQUEST_RESULT.name(), LineSideContainerValue.NOT_COMPLETE);
			returnDc.put(TagNames.MESSAGE.name(), "Unknown error occured while processing unscrap");
		} finally {
			getLogger().info("Return Data Container to client" ,returnDc.toString());
		}
		return returnDc;
	}

	@SuppressWarnings("unchecked")
	protected void parseVerifyDataContainer(DataContainer data) {
		applicationId = (String) data.get(TagNames.APPLICATION_ID.name());
		processPointId = (String) data.get(TagNames.PROCESS_POINT_ID.name());
		productIdList = (List<String>) data.get(TagNames.PRODUCT_ID.name());
		productType = (String) data.get(TagNames.PRODUCT_TYPE.name());
		unscrapReason = (String) data.get(TagNames.REASON.name());
		associateId = (String) data.get(TagNames.ASSOCIATE_ID.name());
	}

	@SuppressWarnings("unchecked")
	protected void parseDataContainer(DataContainer data) {
		applicationId = (String) data.get(TagNames.APPLICATION_ID.name());
		processPointId = (String) data.get(TagNames.PROCESS_POINT_ID.name());
		productIdList = (List<String>) data.get(TagNames.PRODUCT_ID.name());
		productType = (String) data.get(TagNames.PRODUCT_TYPE.name());
		unscrapReason = (String) data.get(TagNames.REASON.name());
		associateId = (String) data.get(TagNames.ASSOCIATE_ID.name());
	}

	protected void initReturnDc() {
		returnDc = new DefaultDataContainer();
		returnDc.put(TagNames.ASSOCIATE_ID.name(), associateId);
		returnDc.put(TagNames.PROCESS_POINT_ID.name(), processPointId);
		returnDc.put(TagNames.APPLICATION_ID.name(), applicationId);
	}

	protected void performUnscrap(BaseProduct product) {
		ReuseProductResult reuseProductResult = createReuseProductResult(product.getProductId());
		deleteExceptionalOutForProduct(product.getProductId());
		saveReuseProductResult(reuseProductResult);
		updateDefectAndLastTrackingStatus(product);
		getLogger().info("Product : " + product.getProductId() + " was sucessfully unscrapped.");
	}
	
	private DefaultDataContainer createRequestDc(String productId) {
		DefaultDataContainer requestDc = new DefaultDataContainer();
		requestDc.put(TagNames.PRODUCT_ID.name(), new ArrayList<String>(Arrays.asList(productId)));
		requestDc.put(TagNames.REASON.name(), unscrapReason);
		requestDc.put(TagNames.APPLICATION_ID.name(),processPointId );
		requestDc.put(TagNames.PROCESS_POINT_ID.name(), processPointId);
		requestDc.put(TagNames.PRODUCT_TYPE.name(), productType);
		requestDc.put(TagNames.ASSOCIATE_ID.name(), associateId);
		return requestDc;
	}
	
	public void updateDefectAndLastTrackingStatus(BaseProduct product) {
		DefectStatus defectStatus = calculateDefectStatus(product.getProductId());
		String lastTrackingStatus = getLastTrackingStatus(product);
		updateDefectStatus(product.getProductId(), defectStatus);
		updateLastTrackingStatus(product.getProductId(),lastTrackingStatus);
	}

	public void updateLastTrackingStatus(String productId, String lastTrackingStatus) {
		ProductDao<?> productDoa = getProductDao();
		productDoa.updateNextTracking(productId, lastTrackingStatus);
		getLogger().info("Updated last Tracking Status: "+productId+"->"+lastTrackingStatus);
	}

	public String getLastTrackingStatus(BaseProduct product) {
		List<String> exceptionalLines =  Arrays.asList(PropertyService.getPropertyBean(UnscrapPropertyBean.class).getScrapLines());
		
		StringBuilder scrapLines = new StringBuilder();
		scrapLines.append("'");
		for (int i = 0; i < exceptionalLines.size(); i++) {
			scrapLines.append("','").append(exceptionalLines.get(i));
		}			
		scrapLines.append("'");
		
		return ServiceFactory.getDao(ProcessPointDao.class).findLastTrackingStatus(product.getProductId(), scrapLines.toString(), product.getProductType().getProductName());
	}

	protected ReuseProductResult createReuseProductResult(String productId) {
		long time = System.currentTimeMillis();
		ReuseProductResult reuseProductResult = new ReuseProductResult();
		ReuseProductResultId id = new ReuseProductResultId();
		id.setProductId(productId);
		id.setActualTimestamp(new Timestamp(time));
		reuseProductResult.setId(id);
		reuseProductResult.setReuseVinReason(unscrapReason);
		reuseProductResult.setAssociateNo(associateId);
		reuseProductResult.setProductionDate(new Date(time));

		return reuseProductResult;
	}

	protected ReuseProductResult saveReuseProductResult(ReuseProductResult reuseProductResult) {
		reuseProductResult = ServiceFactory.getDao(ReuseProductResultDao.class).save(reuseProductResult);
		getLogger().info("Saved RequseProductResult : " + reuseProductResult.getId().getProductId());
		return reuseProductResult;
	}

	protected void updateDefectStatus(String productId, DefectStatus defectStatus) {
		ProductDao<?> productDao = getProductDao();
		productDao.updateDefectStatus(productId, defectStatus);
		getLogger().info("Updated DefectStatus : " + productId);
	}

	protected void deleteExceptionalOutForProduct(String productId) {
		ServiceFactory.getDao(ExceptionalOutDao.class).deleteAllByProductId(productId);
		getLogger().info("Deleted all ExceptionalOut by product ID: " + productId);
	}

	protected DefectStatus calculateDefectStatus(String productId) {
		List<DefectResult> defects = ServiceFactory.getDao(DefectResultDao.class).findAllByProductId(productId);
		if (defects == null || defects.isEmpty()) {
			return DefectStatus.REPAIRED;
		}
		for (DefectResult dr : defects) {
			if (dr.getDefectStatusValue() != DefectStatus.REPAIRED.getId()) {
				return DefectStatus.OUTSTANDING;
			}
		}

		return DefectStatus.REPAIRED;
	}

	protected boolean isValidProducts() {
		boolean areAllProductsValid = true;
		for(String productId : getProductIdList()) {
			BaseProduct product = findProduct(productId);
			if(product == null) {
				getErrorList().add("Product not found : " + productId +"\n");
				areAllProductsValid = false;
				getInvalidProductList().add(product);
			} else if(!isProductScrapped(product)) {
				getErrorList().add("Product : " + productId + " is not scrapped.\n");
				getInvalidProductList().add(product);
				areAllProductsValid = false;
			} else if(!isProducScrappable(product)) {
				getErrorList().add("Product : " + productId + " is not scrappable.\n");
				getInvalidProductList().add(product);
				areAllProductsValid = false;
			} else {
				getProductList().add(product);
			}
		}
		if(!areAllProductsValid) {
			prepareNgReply(getErrorList().toString());
			returnDc.put(TagNames.INVALID_PRODUCT_LIST.name(), getInvalidProductList());
		}
		return areAllProductsValid;
	}

	public List<String> getErrorList() {
		if(errorList == null) {
			errorList = new ArrayList<String>();
		}
		return errorList;
	}
	
	public List<BaseProduct> getProductList() {
		if(productList == null) {
			productList = new ArrayList<BaseProduct>();
		}
		return productList;
	}

	@Override
	public boolean isProductScrapped(BaseProduct productToCheck) {
		List<ExceptionalOut> exceptionalOut = getExceptionalOut(productToCheck.getProductId());
		return (exceptionalOut != null && !exceptionalOut.isEmpty()); 
	}

	private List<ExceptionalOut> getExceptionalOut(String productId) {
		return ServiceFactory.getDao(ExceptionalOutDao.class).findAllByProductId(productId);
	}

	protected boolean isProducScrappable(BaseProduct product) {
		return product.isProductScrappable();
	}
	
	protected void verifyInputDataForUnscrap() {
		verifyProductIdList();
		verifyProductType();
		verifyProcessPointId();
		verifyApplicationId();
	}

	protected void verifyInputData() {
		verifyProductIdList();
		verifyProductType();
		verifyAssociateId();
		verifyUnscrapReason();
		verifyProcessPointId();
		verifyApplicationId();
	}

	protected void verifyProductIdList() {
		if(productIdList == null || productIdList.isEmpty()) {
			prepareErrorReply(new InputValidationException("Missing product id list to unscrap"), null);
		}
	}

	protected void verifyProductType() {
		if(StringUtil.isNullOrEmpty(productType)) {
			prepareErrorReply(new InputValidationException("Missing product type"), null);
		}
	}

	protected void verifyAssociateId() {
		if(StringUtil.isNullOrEmpty(associateId)) {
			prepareErrorReply(new InputValidationException("Associate Id is required"), null);
		} else {
			associateId = associateId.toUpperCase();
		}
	}

	protected void verifyUnscrapReason() {
		boolean isUnscrapReasonRequired = PropertyService.getPropertyBean(UnscrapPropertyBean.class).isUnscrapReasonRequired();
		if(isUnscrapReasonRequired && StringUtil.isNullOrEmpty(unscrapReason)) {
			prepareErrorReply(new InputValidationException("Unscrap reason is required"), null);
		}
	}

	protected void verifyProcessPointId() {	
		if(StringUtil.isNullOrEmpty(processPointId)) {
			prepareErrorReply(new InputValidationException("Missing process point id"), null);
		}
	}

	protected void verifyApplicationId() {
		if(StringUtil.isNullOrEmpty(applicationId) && !(StringUtil.isNullOrEmpty(processPointId))) {
			applicationId = processPointId;
		}else if (StringUtil.isNullOrEmpty(applicationId)){
			prepareErrorReply(new InputValidationException("Missing application id"), null);
		}
	}
	
	protected void populateScrappedProductDtoList() {
		for(BaseProduct product : productList) {
			getScrappedProductDtoList().add(populateScrappedProductDto(product));
		}
	}

	protected ScrappedProductDto populateScrappedProductDto(BaseProduct product) {
		ScrappedProductDto scrappedProductDto = new ScrappedProductDto();

		scrappedProductDto.setProduct(product);
		scrappedProductDto.setNaqScrap(isNaqScrap(product));
		List<ExceptionalOut> exceptionalOutList = getExceptionalOut(product.getProductId());
		if(!exceptionalOutList.isEmpty()) {
		scrappedProductDto.setExceptionalOut(exceptionalOutList.get(0));
		}
		return scrappedProductDto;
	}

	public boolean isNaqScrap(BaseProduct product) {
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(product);
		return productCheckUtil.productNonRepairableCheck();
	}

	protected BaseProduct findProduct(String productId) {
		BaseProduct baseProduct;
		try {
			if(getProductDao() instanceof DiecastDao) {
				DiecastDao<?> diecastDao = (DiecastDao<?>) getProductDao();
				baseProduct = diecastDao.findByMCDCNumber(productId);
			} else {
				baseProduct = getProductDao().findByKey(productId);
			}
		} catch (NullPointerException e) {
			baseProduct = null;
		}
		return baseProduct;
	}

	protected void prepareProductVerifyCompleteReply() {
		returnDc.put(TagNames.PRODUCT_INFO_LIST.name(), getScrappedProductDtoList());
		returnDc.put(TagNames.PRODUCT_LIST.name(), getProductList());
		returnDc.put(TagNames.REQUEST_RESULT.name(), LineSideContainerValue.COMPLETE);
		if(productList.size() == 1) {
		returnDc.put(TagNames.MESSAGE.name(), "Product " + productList.get(0) + " is valid for scrap");
		} else {
			returnDc.put(TagNames.MESSAGE.name(), "Products are valid for scrap");
		}
	}

	protected void prepareUnscrapCompleteReply() {
		returnDc.put(TagNames.REQUEST_RESULT.name(), LineSideContainerValue.COMPLETE);
		returnDc.put(TagNames.MESSAGE.name(), "Unscrap Service completed successfully.");
		returnDc.put(TagNames.PRODUCT_ID.name(), productIdList);
	}

	protected void prepareNgReply(String message) {
		getLogger().warn(message);
		returnDc.put(TagNames.PRODUCT_INFO_LIST.name(), getScrappedProductDtoList());
		returnDc.put(TagNames.PRODUCT_LIST.name(), getProductList());
		returnDc.put(TagNames.REQUEST_RESULT.name(), LineSideContainerValue.NOT_COMPLETE);
		returnDc.put(TagNames.MESSAGE.name(), message);
	}

	protected void prepareErrorReply(TaskException e, String extraMessage) {
		String message = extraMessage == null ? getClassName() : getClassName() + " : " + extraMessage; 
		getLogger().error(e, e.getMessage(), message);
		returnDc.put(TagNames.REQUEST_RESULT.name(), LineSideContainerValue.NOT_COMPLETE);
		returnDc.put(TagNames.MESSAGE.name(), e.getMessage());
		throw e;
	}

	private ProductDao<?> getProductDao() {
		return ProductTypeUtil.getProductDao(productType);
	}
	
	public void setLogger() {
		String appId = null;
		if(!StringUtil.isNullOrEmpty(applicationId)) {
			appId = applicationId;
		} else if (!StringUtil.isNullOrEmpty(processPointId)) {
			appId = processPointId;
		} else {
			appId = getClassName();
		}
		logger = Logger.getLogger(appId + (propertyBean == null ? "" :propertyBean.getNewLogSuffix()));	
	}

	public Logger getLogger() {
		return this.logger;
	}

	public String getUnscrapReason() {
		return this.unscrapReason;
	}

	public void setUnscrapReason(String unscrapReason) {
		this.unscrapReason = unscrapReason;
	}

	public String getAssociateId() {
		return this.associateId;
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getApplicationId() {
		return this.applicationId;
	}

	public String getProcessPointId() {
		return this.processPointId;
	}
	
	public DataContainer getReturnDataContainer() {
		return this.returnDc;
	}
	
	public List<String> getProductIdList() {
		return this.productIdList;
	}
	
	public List<BaseProduct> getInvalidProductList() {
		if(invalidProductList == null) {
			invalidProductList = new ArrayList<BaseProduct>();
		}
		return invalidProductList;
	}
	
	public String getClassName() {
		return UnscrapServiceImpl.CLASS_NAME;
	}

	public List<ScrappedProductDto> getScrappedProductDtoList() {
		if(scrappedProductDtoList == null) {
			scrappedProductDtoList = new ArrayList<ScrappedProductDto>();
		}
		return scrappedProductDtoList;
	}

	public void setScrappedProductDtoList(List<ScrappedProductDto> scrappedProductDtoList) {
		this.scrappedProductDtoList = scrappedProductDtoList;
	}
}
