package com.honda.galc.service.defect.scrap;

import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.InputValidationException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.ScrappedProductDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.ExceptionalOutId;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.UnscrapPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.defect.ScrapService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.StringUtil;

public class ScrapServiceImpl implements ScrapService {
	private static final String CLASS_NAME = "ScrapService";

	ProductCheckUtil productCheckUtil;

	private List<String> productIdList;
	private List<String> errorList;
	protected List<BaseProduct> productList;
	protected List<BaseProduct> invalidProductList;
	
	protected String productType;
	protected String associateId;
	protected String scrapReason;
	protected String applicationId;
	protected String processPointId;
	protected String location;

	private List<ScrappedProductDto> scrappedProductDtoList;
	private ScrappedProductDto scrappedProductDto;
	protected DataContainer returnDc = new DefaultDataContainer();
	protected Logger logger;

	protected HeadLessPropertyBean propertyBean;

	private Date productionDate;

	private String comment;

	@Override
	public DataContainer scrapProduct(DefaultDataContainer data) {
		try {
			returnDc = new DefaultDataContainer();
			parseDataContainer(data);
			setLogger();
			getLogger().info(getClassName() + " begin Scrap ", data.toString());
			initReturnDc();
			verifyInputData();
			getLogger().info("Performing Scrap on " + productIdList.size() + " products.");

			if(isValidProducts()) {
				for(String product : getProductIdList()) {
					performScrap(product);
				}
				prepareScrapCompleteReply(); 
			} else {
				getLogger().warn("Products : " + getInvalidProductList().toString() + " could not be Scrapped");
				throw new TaskException(returnDc.getString(TagNames.MESSAGE.name()));
			}
		} catch(TaskException e) {
			return returnDc;
		} catch(Exception e) {
			getLogger().error(e, e.getMessage(), getClassName());
			returnDc.put(TagNames.REQUEST_RESULT.name(), LineSideContainerValue.NOT_COMPLETE);
			returnDc.put(TagNames.MESSAGE.name(), "Unknown error occured while processing Scrap");
		} finally {
			getLogger().info("Return Data Container to client" ,returnDc.toString());
		}
		return returnDc;
	}

	@SuppressWarnings("unchecked")
	protected void parseDataContainer(DataContainer data) {
		applicationId = (String) data.get(TagNames.APPLICATION_ID.name());
		processPointId = (String) data.get(TagNames.PROCESS_POINT_ID.name());
		productIdList = (List<String>) data.get(TagNames.PRODUCT_ID.name());
		productType = (String) data.get(TagNames.PRODUCT_TYPE.name());
		scrapReason = (String) data.get(TagNames.REASON.name());
		associateId = (String) data.get(TagNames.ASSOCIATE_ID.name());
		location = (String) data.get(TagNames.PROCESS_LOCATION.name());
		productionDate = (Date) data.get(TagNames.CURRENT_DATE.name());
		comment = (String) data.get(TagNames.COMMENT.name());
				
	}

	protected void initReturnDc() {
		returnDc = new DefaultDataContainer();
		returnDc.put(TagNames.ASSOCIATE_ID.name(), associateId);
		returnDc.put(TagNames.PROCESS_POINT_ID.name(), processPointId);
		returnDc.put(TagNames.APPLICATION_ID.name(), applicationId);
	}

	protected void performScrap(String productId) {
		//add defect status
		updateDefectStatus(productId, DefectStatus.SCRAP);
		//create exceptionalOut
		addExceptionalOutForProduct(createExceptionalOutObject(productId));		
		//start tracking
		startTracking(productId);
		
		updateQiDefectResult(productId);
		updateQiRepairResult(productId);
		
		
		getLogger().info("Product : " + productId + " was sucessfully Scrapped.");
	}

	private void updateQiDefectResult(String productToCheck) {
		List<QiDefectResult> resultList = getQiDefectResultList(productToCheck);
		if(resultList!=null && !resultList.isEmpty()) {
			List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>(); 
			for(QiDefectResult qiDefectResult : resultList) {
				qiDefectResult.setCurrentDefectStatus((short) DefectStatus.NOT_FIXED_SCRAPPED.getId());
				qiDefectResult.setUpdateUser(associateId);
				qiDefectResultList.add(qiDefectResult);
			}
			ServiceFactory.getDao(QiDefectResultDao.class).updateAll(qiDefectResultList);
		}
		
	}	
	
	public void updateQiRepairResult(String productToCheck) {		
		List<QiRepairResult> resultList = getQiRepairResultList(productToCheck);
		if(resultList!=null && !resultList.isEmpty()) {
			List<QiRepairResult> qiRepairtResultList = new ArrayList<QiRepairResult>(); 
			for(QiRepairResult qiRepairResult : resultList) {
				qiRepairResult.setCurrentDefectStatus((short) DefectStatus.NOT_FIXED_SCRAPPED.getId());
				qiRepairResult.setUpdateUser(associateId);
				qiRepairtResultList.add(qiRepairResult);
			}
			ServiceFactory.getDao(QiRepairResultDao.class).updateAll(qiRepairtResultList);
		}
	}

	public List<QiDefectResult> getQiDefectResultList(String product) {
		return findNotFixedDefectsForProductId(product);
	}

	private List<QiDefectResult> findNotFixedDefectsForProductId(String productId) {
		return ServiceFactory.getDao(QiDefectResultDao.class).findAllByProductIdAndCurrentDefectStatus(productId, (short) DefectStatus.NOT_FIXED.getId());
	}
	
	public List<QiRepairResult> getQiRepairResultList(String product) {
		return findNotFixedRepairResultsForProductId(product);
	}

	private List<QiRepairResult> findNotFixedRepairResultsForProductId(String productId) {
		return ServiceFactory.getDao(QiRepairResultDao.class).findAllByProductIdAndCurrentDefectStatus(productId, (short) DefectStatus.NOT_FIXED.getId());
	}

	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	private Date getProductionDate() {
		return productionDate;
	}

	protected boolean isValidProducts() {
		boolean areAllProductsValid = true;
		for(String productId : getProductIdList()) {
			BaseProduct product = findProduct(productId);
			if(product == null) {
				getErrorList().add("Product not found : " + productId);
				areAllProductsValid = false;
				getInvalidProductList().add(product);
			} else if(!isProductScrappable(product)) {
				getErrorList().add("Product : " + productId + " is not scrappable.");
				getInvalidProductList().add(product);
				areAllProductsValid = false;
			} else {
				getProductList().add(product);
			}
		}
		if(!areAllProductsValid) {
			prepareNgReply(getErrorList().toString());
		}
		return areAllProductsValid;
	}
	
	protected void updateDefectStatus(String productId, DefectStatus defectStatus) {
		ProductDao<?> productDao = getProductDao();
		productDao.updateDefectStatus(productId, defectStatus);
		productDao.removeDunnage(productId);
		getLogger().info("Updated DefectStatus and remove Dunnage: " + productId);
	}

	protected void addExceptionalOutForProduct(ExceptionalOut exceptionOut) {
		ServiceFactory.getDao(ExceptionalOutDao.class).createExceptionalOut(exceptionOut);
		getLogger().info("Added all ExceptionalOut by product ID: " + exceptionOut);
	}

	protected ExceptionalOut createExceptionalOutObject(String productId) {
		ExceptionalOutId id = new ExceptionalOutId();
		id.setProductId(productId);

		ExceptionalOut exceptionalOut = new ExceptionalOut();
		exceptionalOut.setId(id);
		exceptionalOut.setProcessPointId(getProcessPointId());
		exceptionalOut.setAssociateNo(getAssociateId());
		exceptionalOut.setLocation(getLocation());
		exceptionalOut.setExceptionalOutReasonString(getScrapReason());
		exceptionalOut.setProductionDate(getProductionDate());
		if(getComment() != null) {
			exceptionalOut.setExceptionalOutComment(getComment());
		}
		return exceptionalOut;
	}
	
	public void startTracking(String inputNumber) {
		String trackingPP = getScrapProcessPointId();
		if (StringUtils.isEmpty(trackingPP))
			return;

		getLogger().info("Start Scrap tracking");
		ProductHistory productHistory = ProductTypeUtil.createProductHistory(inputNumber, trackingPP,getProductType());

		if (productHistory != null) {
			productHistory.setAssociateNo(getAssociateId());
			productHistory.setApproverNo("");
			productHistory.setProcessPointId(trackingPP);
			productHistory.setProductId(inputNumber);
		}

		getService(TrackingService.class).track(ProductType.getType(getProductType()), productHistory);
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

	protected boolean isProductScrappable(BaseProduct product) {
		return product.isProductScrappable();
	}

	protected void verifyInputData() {
		verifyProductIdList();
		verifyProductType();
		verifyAssociateId();
		verifyScrapReason();
		verifyProcessPointId();
		verifyApplicationId();
	}

	protected void verifyProductIdList() {
		if(productIdList == null || productIdList.isEmpty()) {
			prepareErrorReply(new InputValidationException("Missing product id list to Scrap"), null);
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

	protected void verifyScrapReason() {
		boolean isScrapReasonRequired = PropertyService.getPropertyBean(UnscrapPropertyBean.class).isUnscrapReasonRequired();
		if(isScrapReasonRequired && StringUtil.isNullOrEmpty(scrapReason)) {
			prepareErrorReply(new InputValidationException("Scrap reason is required"), null);
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

	protected void prepareScrapCompleteReply() {
		returnDc.put(TagNames.REQUEST_RESULT.name(), LineSideContainerValue.COMPLETE);
		returnDc.put(TagNames.MESSAGE.name(), "Scrap Service completed successfully.");
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

	public String getScrapReason() {
		return this.scrapReason;
	}

	public void setScrapReason(String scrapReason) {
		this.scrapReason = scrapReason;
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
		return ScrapServiceImpl.CLASS_NAME;
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
	
	private String getScrapProcessPointId() {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPointId).getScrapProcessPoint();
	}
	
	public ScrappedProductDto getScrappedProductDto() {
		return scrappedProductDto;
	}

	public void setScrappedProductDto(ScrappedProductDto scrappedProductDto) {
		this.scrappedProductDto = scrappedProductDto;
	}
}
