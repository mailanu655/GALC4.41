/**
 * Service class used to place a kickout or release a kickout on 1 or more products.
 * Kickout information is stored in KICKOUT_TBX and KICKOUT_LOCATION_TBX
 *
 * @author Bradley Brown
 * @version 1.0
 * @since 2.42
 */
package com.honda.galc.service.kickout;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.exception.InputValidationException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.KickoutDao;
import com.honda.galc.dao.product.KickoutLocationDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.KickoutStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Kickout;
import com.honda.galc.entity.product.KickoutLocation;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.KickoutPropertyBean;
import com.honda.galc.service.KickoutService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.StringUtil;

public class KickoutServiceImpl implements KickoutService {
	private static final String INPUT_PASSED_VERIFICATION = "Input passed Verification.";

	private static final String CLASS_NAME = "KickoutServiceImpl";

	private DataContainer retDc = new DefaultDataContainer();

	private HeadLessPropertyBean headlessPropertyBean;
	private KickoutPropertyBean kickoutPropertyBean;

	private static final int SINGLE_BATCH = 1;
	private static final int SMALL_BATCH = 4;
	private static final int MEDIUM_BATCH = 11;
	private static final int LARGE_BATCH = 51;
	private static final int LARGEST_BATCH = 101;
	private Logger logger;

	private List<BaseProduct> productList;
	private List<KickoutDto> kickoutDtoList;
	private String applicationId;
	private String transactionId;
	private boolean fixedDefectFlag;
	private String description;
	private String comment;
	private String releaseComment;
	private String associateNo;
	private String divisionId;
	private String lineId;
	private String processPointId;

	public DataContainer validateProductsForKickout(DefaultDataContainer data) {
		parseVerifyForKickoutDc(data);
		getLogger().info(getClassName() +".validateProductsForKickout recieve Data Container " + data.toString());
		initReturnDc();
		verifyInputForValidateKickout();
		List<KickoutDto> activeKickouts = new ArrayList<KickoutDto>();
		if(!isMultipleKickout(applicationId)) {
			getLogger().info("Checking products for existing active kickouts");
			List<String> productIdList = new ArrayList<String>();
			for(BaseProduct product : productList) {
				productIdList.add(product.getProductId());
			}
			if(StringUtil.isNullOrEmpty(transactionId)) {
				activeKickouts = findKickoutResultsByProductIds(productIdList);
			}else {				
				activeKickouts = findKickoutResultsByTransactionId(transactionId);
			}
			
			if(!activeKickouts.isEmpty()) {
				for(KickoutDto kickout : activeKickouts) {
					BaseProduct product = findProductFromList(kickout.getProductId(), productList);
					productList.remove(product);
					retDc.addErrorMsg("Kickouts already exist for product : " + kickout.getProductId() + " remove existing kickout first. \n");
				}
				getLogger().warn(retDc.getErrorMessages().toString());
			}
		}
		String result;
		if(retDc.getErrorMessages() == null || retDc.getErrorMessages().isEmpty()) {
			result = LineSideContainerValue.COMPLETE;
		} else {
			result = LineSideContainerValue.NOT_COMPLETE;
		}
		createKickoutDto(productList);
		prepareReply(result);
		populateKickoutDto(kickoutDtoList);
		retDc.put(DataContainerTag.KICKOUT_PRODUCTS, kickoutDtoList);
		getLogger().info(getClassName() + ".validateProductsForKickout request complete. Result DC : " + retDc.toString());
		return retDc;
	}

	public DataContainer validateProductsForRelease(DefaultDataContainer data) {
		parseVerifyForKickoutDc(data);
		getLogger().info(getClassName() +".validateProductsForRelease recieve Data Container " + data.toString());
		initReturnDc();
		verifyInputForValidateKickout();
		List<KickoutDto> activeKickouts = new ArrayList<KickoutDto>();
		List<String> productIdList = new ArrayList<String>();
		for(BaseProduct product : productList) {
			productIdList.add(product.getProductId());
		}
		
		if(StringUtil.isNullOrEmpty(transactionId)) {
			activeKickouts = findKickoutResultsByProductIds(productIdList);
		}else {				
			activeKickouts = findKickoutResultsByTransactionId(transactionId);
		}

		populateKickoutDto(activeKickouts);
		retDc.put(DataContainerTag.KICKOUT_PRODUCTS, activeKickouts);
		retDc.put(DataContainerTag.REQUEST_RESULT, LineSideContainerValue.COMPLETE);
		getLogger().info(getClassName() + ".validateProductsForRelease request complete. Result DC : " + retDc.toString());
		return retDc;
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public DataContainer kickoutProducts(DefaultDataContainer data) {
		parseKickoutDc(data);
		getLogger().info(getClass() +".kickoutProducts recieve Data Container " + data.toString());
		initReturnDc();
		verifyInputForKickout();

		getLogger().info("Applying kickout to products.");
		List<KickoutDto> resultList = new ArrayList<KickoutDto>();
		for(KickoutDto kickoutDto : kickoutDtoList) {
			Kickout kickout = new Kickout();
			kickout.setProductId(kickoutDto.getProductId());
			kickout.setComment(comment);
			kickout.setDescription(description);
			kickout.setKickoutUser(associateNo);
			kickout.setApproverName(kickoutDto.getApproverName());
			kickout.setProductType(kickoutDto.getProductType());
			kickout.setKickoutStatus(KickoutStatus.OUTSTANDING.getId());

			Kickout kickoutResult = getKickoutDao().save(kickout);

			KickoutLocation  kickoutLocation = new KickoutLocation();
			kickoutLocation.setKickoutId(kickoutResult.getId());
			kickoutLocation.setDivisionId(divisionId);
			kickoutLocation.setLineId(lineId);
			kickoutLocation.setProcessPointId(processPointId);

			KickoutLocation resultLocation = getKickoutLocationDao().save(kickoutLocation);
			KickoutDto resultKickoutDto = new KickoutDto();
			resultKickoutDto.setKickout(kickoutResult);
			resultKickoutDto.setKickoutLocation(resultLocation);
			resultList.add(resultKickoutDto);
		}
		getLogger().info("Kickout successfully applied to products.");

		prepareReply(LineSideContainerValue.COMPLETE);
		retDc.put(DataContainerTag.KICKOUT_PRODUCTS, resultList);
		getLogger().info(getClassName() + ".kickoutProducts request complete. Result DC : " + retDc.toString());
		return retDc;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public DataContainer kickoutByProductIds(DefaultDataContainer data) {
		List<KickoutDto> kickoutDtoList = new ArrayList<KickoutDto>();

		for (String productId : data.get(DataContainerTag.PRODUCT_ID).toString().split(Delimiter.COMMA)) {
			if(ProductTypeUtil.findProduct(data.get(DataContainerTag.PRODUCT_TYPE).toString(), productId) != null) {
				KickoutDto kickoutDto = new KickoutDto();
				kickoutDto.setProductId(productId);
				kickoutDto.setProductType((String) data.get(DataContainerTag.PRODUCT_TYPE));
				kickoutDtoList.add(kickoutDto);
			}			
		}
		data.put(DataContainerTag.KICKOUT_PRODUCTS, kickoutDtoList);
		return kickoutProducts(data);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public DataContainer releaseKickouts(DefaultDataContainer data) {
		parseRleaseDc(data);
		getLogger().info(getClass() +".releaseKickouts recieve Data Container " + data.toString());
		initReturnDc();
		verifyinputForRelease();

		getLogger().info("Releasing products from kickout.");
		
		for(KickoutDto currentKickoutDto : kickoutDtoList) {
			Kickout kickout = new Kickout();
			kickout.setKickoutId(currentKickoutDto.getKickoutId());
			kickout.setReleaseComment(releaseComment);
			kickout.setReleaseUser(associateNo);
			kickout.setKickoutStatus(KickoutStatus.RELEASED.getId());
			getKickoutDao().releaseKickout(kickout);
		}
		getLogger().info("Products successfully released");

		prepareReply(LineSideContainerValue.COMPLETE);
		getLogger().info(getClassName() + ".releaseKickouts request complete. Result DC : " + retDc.toString());
		return retDc;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public DataContainer releaseByProductIds(DefaultDataContainer data) {
		List<KickoutDto> kickoutDtoList = new ArrayList<KickoutDto>();

		for (String productId : data.get(DataContainerTag.PRODUCT_ID).toString().split(Delimiter.COMMA)) {
			if(ProductTypeUtil.findProduct(data.get(DataContainerTag.PRODUCT_TYPE).toString(), productId) != null) {
				
				List<KickoutDto> tempKickoutDtoList=getKickoutDao().findActiveKickoutInfoByProductId(productId);
				for(KickoutDto tempKickout:tempKickoutDtoList)
					kickoutDtoList.add(tempKickout);
			}			
		}
		data.put(DataContainerTag.KICKOUT_PRODUCTS, kickoutDtoList);
		return releaseKickouts(data);
	}

	private void createKickoutDto(List<BaseProduct> productList) {
		kickoutDtoList = new ArrayList<KickoutDto>();
		for(BaseProduct product : productList) {
			KickoutDto kickoutDto = new KickoutDto();
			kickoutDto.setProductId(product.getProductId());
			kickoutDto.setProductType(getProductTypeName());
			kickoutDto.setProductSpecCode(product.getProductSpecCode());
			kickoutDto.setLastPassingProcessPointId(product.getLastPassingProcessPointId());
			kickoutDto.setLastPassingProcessPointName(getProcessPointName(product.getLastPassingProcessPointId()));
			kickoutDtoList.add(kickoutDto);
		}
	}

	private void populateKickoutDto(List<KickoutDto> kickoutDtoList) {
		for(KickoutDto kickoutDto :kickoutDtoList) {
			kickoutDto.setDivisionName(getDivisionName(kickoutDto.getDivisionId()));
			kickoutDto.setLineName(getLineName(kickoutDto.getLineId()));
			kickoutDto.setProcessPointName(getProcessPointName(kickoutDto.getProcessPointId()));
			KickoutStatus kickoutStatus = KickoutStatus.getType(kickoutDto.getKickoutStatus());
			if(kickoutStatus != null) {
				kickoutDto.setKickoutStatusName(kickoutStatus.getName());
			}
			BaseProduct product = findProductFromList(kickoutDto.getProductId(), productList);
			if(product != null) {
				kickoutDto.setProductSpecCode(product.getProductSpecCode());
				kickoutDto.setLastPassingProcessPointId(product.getLastPassingProcessPointId());
				if(product.getLastPassingProcessPointId() != null) {
					kickoutDto.setLastPassingProcessPointName(getProcessPointName(product.getLastPassingProcessPointId()));
				}
				if(isDcProduct()) {
					kickoutDto.setMcSerialNumber(((DieCast) product).getMcSerialNumber());
					kickoutDto.setDcSerialNumber(((DieCast) product).getDcSerialNumber());
					kickoutDto.setDunnage(((DieCast) product).getDunnage() == null ? "" : ((DieCast) product).getDunnage());
				}
			}
		}
	}

	private boolean isDcProduct() {
		return ProductTypeUtil.isDieCast(ProductType.getType(getProductTypeName()));
	}

	private String getProductTypeName() {
		return getHeadlessPropertyBean().getProductType();
	}

	private BaseProduct findProductFromList(String productId, List<BaseProduct> productList) {
		for(BaseProduct product : productList) {
			if(StringUtils.trim(product.getProductId()).equals(StringUtils.trim(productId))) {
				return product;
			}
		}
		return null;
	}

	private String getLineName(String lineId) {
		if(lineId == null) return "";
		Line line = ServiceFactory.getDao(LineDao.class).findByKey(lineId);
		if(line == null) return "";
		return line.getLineName();
	}

	private String getProcessPointName(String processPointId) {
		if(processPointId == null) return "";
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
		if(processPoint == null) return "";
		return processPoint.getProcessPointName();
	}

	private String getDivisionName(String divisionId) {
		if(divisionId == null) return "";
		Division division = ServiceFactory.getDao(DivisionDao.class).findByKey(divisionId);
		if(division == null) return "";
		return division.getDivisionName();
	}

	public int getBatchSize(int resultSize) {
		int batchSize = 0;
		if (resultSize >= LARGEST_BATCH) {
			batchSize = LARGEST_BATCH;
		} else if (resultSize >= LARGE_BATCH) {
			batchSize = LARGE_BATCH;
		} else if (resultSize >= MEDIUM_BATCH) {
			batchSize = MEDIUM_BATCH;
		} else if (resultSize >= SMALL_BATCH) {
			batchSize = SMALL_BATCH;
		} else if (resultSize >= SINGLE_BATCH) {
			batchSize = SINGLE_BATCH;
		}
		return batchSize;
	}

	public List<String> getProductIdListForQuery(List<String> productIdList, int batchSize, int currentLocation) {
		List<String> returnProductIdList = new ArrayList<String>();
		for(int y = currentLocation, x = batchSize; x > 0; x--, y++) {
			if(productIdList.get(y) != null) {
				returnProductIdList.add(productIdList.get(y));
			}
		}
		return returnProductIdList;
	}

	public boolean isMultipleKickout(String applicationId) {
		if(StringUtil.isNullOrEmpty(this.applicationId)) {
			if(StringUtil.isNullOrEmpty(applicationId)) {
				prepareErrorReply(new InputValidationException("Application Id is null", getClassName()), "Application Id");
			}
			setApplicationId(applicationId);
		}
		boolean isMultipleKickout = getKickoutPropertyBean().isMultipleKickout();
		getLogger().info("isMultipleKickout property : true");
		return isMultipleKickout;
	}

	private void verifyInputForValidateKickout() {
		verifyProductList();
		verifyApplicationId();
		getLogger().info(INPUT_PASSED_VERIFICATION);
	}

	private void verifyInputForKickout() {
		verifyKickoutDtoList();
		verifyApplicationId();
		verifyDescription();
		verifyDivisionId();
		verifyLineId();
		verifyProcessPointId();
		getLogger().info(INPUT_PASSED_VERIFICATION);
	}

	private void verifyinputForRelease() {
		verifyKickoutDtoList();
		verifyApplicationId();
		getLogger().info(INPUT_PASSED_VERIFICATION);
	}

	private List<KickoutDto> findKickoutResultsByProductIds(List<String> productIdList) {
		List<KickoutDto> kickoutResults = new ArrayList<KickoutDto>();
		int batchSize = 0;
		int currentLocation = 0;
		int numProductToCreate = productIdList.size();
		while(numProductToCreate > 0) {
			batchSize = getBatchSize(numProductToCreate);
			if(batchSize > 0) {
				List<String> productIdListForQuery = getProductIdListForQuery(productIdList, batchSize, currentLocation);
				kickoutResults.addAll(getKickoutDao().findProductsWithKickout(productIdListForQuery));
			}
			numProductToCreate -= batchSize;
			currentLocation += batchSize;
		}
		return kickoutResults;
	}
	
	private List<KickoutDto> findKickoutResultsByTransactionId(String trasactionId) {
		List<KickoutDto> kickoutResults = new ArrayList<KickoutDto>();
		if(fixedDefectFlag) {
		kickoutResults.addAll(getKickoutDao().findProductsWithKickoutAndTransactionId(Long.parseLong(transactionId), fixedDefectFlag));
		}else {
			kickoutResults.addAll(getKickoutDao().findProductsWithKickoutAndTransactionId(Long.parseLong(transactionId), fixedDefectFlag));
		}
		return kickoutResults;
	}

	private void verifyProductList() {
		if(productList == null || productList.isEmpty()) {
			prepareErrorReply(new InputValidationException("No Product sent for verification"), null);
		}
	}

	private void verifyKickoutDtoList() {
		if(kickoutDtoList == null || kickoutDtoList.isEmpty()) {
			prepareErrorReply(new InputValidationException("Empty or No Valid Products to kickout/release"), null);
		}
	}

	private void verifyApplicationId() {
		if(StringUtil.isNullOrEmpty(applicationId)) {
			prepareErrorReply(new InputValidationException("Missing application id"), null);
		}
	}

	private void verifyDescription() {
		if(StringUtil.isNullOrEmpty(description)) {
			prepareErrorReply(new InputValidationException("Missing description"), null);
		}
	}

	private void verifyDivisionId() {
		if(StringUtil.isNullOrEmpty(divisionId)) {
			prepareErrorReply(new InputValidationException("Missing kickout department id."), null);
		}
	}

	private void verifyLineId() {
		if(StringUtil.isNullOrEmpty(lineId)) {
			prepareErrorReply(new InputValidationException("Missing kickout line id."), null);
		}
	}

	private void verifyProcessPointId() {
		if(StringUtil.isNullOrEmpty(processPointId)) {
			prepareErrorReply(new InputValidationException("Missing process point id."), null);
		}
	}

	private void prepareReply(String result) {
		getLogger().info("Preparing reply with Request Result : " + result);
		retDc.put(TagNames.REQUEST_RESULT.name(), result);
		retDc.put(DataContainerTag.APPLICATION_ID, applicationId);
	}

	private void prepareErrorReply(TaskException e, String extraMessage) {
		String message = extraMessage == null ? CLASS_NAME : CLASS_NAME + " : " + extraMessage; 
		getLogger().error(e, e.getMessage(), message);
		retDc.put(TagNames.REQUEST_RESULT.name(), LineSideContainerValue.NOT_COMPLETE);
		retDc.put(TagNames.MESSAGE.name(), e.getMessage());
		throw e;
	}

	private void initReturnDc() {
		retDc = new DefaultDataContainer();
		retDc.put(DataContainerTag.APPLICATION_ID, applicationId);
		retDc.put(DataContainerTag.REQUEST_RESULT, LineSideContainerValue.NOT_COMPLETE);
	}

	@SuppressWarnings("unchecked")
	private void parseVerifyForKickoutDc(DefaultDataContainer data) {
		productList = (List<BaseProduct>) data.get(DataContainerTag.PRODUCT);
		applicationId = (String) data.get(DataContainerTag.APPLICATION_ID);
		transactionId = (String) data.get(DataContainerTag.TRANSACTION_ID);
		fixedDefectFlag = (boolean) (data.get(DataContainerTag.FIXED_DEFECT_FLAG)==null?false:data.get(DataContainerTag.FIXED_DEFECT_FLAG));
	}

	@SuppressWarnings("unchecked")
	private void parseRleaseDc(DefaultDataContainer data) {
		kickoutDtoList = (List<KickoutDto>) data.get(DataContainerTag.KICKOUT_PRODUCTS);
		applicationId = (String) data.get(DataContainerTag.APPLICATION_ID);
		description = (String) data.get(DataContainerTag.DESCRIPTION);
		releaseComment = (String) data.get(DataContainerTag.RELEASE_COMMENT);
		associateNo = (String) data.get(DataContainerTag.ASSOCIATE_NO);
	}

	@SuppressWarnings("unchecked")
	private void parseKickoutDc(DefaultDataContainer data) {
		kickoutDtoList = (List<KickoutDto>) data.get(DataContainerTag.KICKOUT_PRODUCTS);
		applicationId = (String) data.get(DataContainerTag.APPLICATION_ID);
		description = (String) data.get(DataContainerTag.DESCRIPTION);
		comment = (String) data.get(DataContainerTag.KICKOUT_COMMENT);
		associateNo = (String) data.get(DataContainerTag.ASSOCIATE_NO);
		divisionId = (String) data.get(DataContainerTag.DIVISION_ID);
		lineId = (String) data.get(DataContainerTag.LINE_ID);
		processPointId = (String) data.get(DataContainerTag.PROCESS_POINT_ID);
	}

	public String getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public KickoutDao getKickoutDao() {
		return ServiceFactory.getDao(KickoutDao.class);
	}

	public KickoutLocationDao getKickoutLocationDao() {
		return ServiceFactory.getDao(KickoutLocationDao.class);
	}

	public HeadLessPropertyBean getHeadlessPropertyBean() {
		if(headlessPropertyBean == null) {
			headlessPropertyBean = PropertyService.getPropertyBean(HeadLessPropertyBean.class, applicationId);
		}
		return headlessPropertyBean;
	}

	public KickoutPropertyBean getKickoutPropertyBean() {
		if(kickoutPropertyBean == null) {
			kickoutPropertyBean = PropertyService.getPropertyBean(KickoutPropertyBean.class, applicationId);
		}
		return kickoutPropertyBean;
	}

	public String getClassName() { 
		return  this.getClass().getCanonicalName();
	}

	public Logger getLogger() {
		if(logger == null) {
			String appId = null;
			if(!StringUtil.isNullOrEmpty(applicationId)) {
				appId = applicationId;

			} else {
				appId = CLASS_NAME;
			}
			logger = Logger.getLogger(appId + (getHeadlessPropertyBean() == null ? "" : getHeadlessPropertyBean().getNewLogSuffix()));	
		}
		return logger;
	}
}
