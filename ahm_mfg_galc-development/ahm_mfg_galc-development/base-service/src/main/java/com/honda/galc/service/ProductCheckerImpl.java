package com.honda.galc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.data.InputData;
import com.honda.galc.data.OutputData;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;

/**
 * 
 * <h3>ProductCheckerImpl Interface description</h3>
 * <p> ProductCheckerImpl description </p>
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
 * @author Paul Chou<br>
 * Apr.5, 2024
 *
 */
public class ProductCheckerImpl implements ProductChecker{
	private static final String PMQA = "PMQA";
	private static final String PART_HOLD_CHECK_FAILED = "Part Hold Check Failed - while retriving the hold information. ";
	private static final String NOT_EXIST = " not exist.";
	private static final String CHECK_PASSED = " Passed - ProductId:";
	private static final String CHECK_FAILED = " Failed - ProductId:";
	private static final String CHECK_PRODUCT_ID_FAILED = " Failed - ProductId: ";
	private static final String CHECK_ENGINE_ID_FAILED = " Failed - Engine: ";
	private static final String CHECK_PROCESS_POINT_FAILED = " Failed - ProcessPointId: ";
	private static final String PRODUCT_ID_ERROR_STRING = " Failed - Product Id is empty or null ";
	private static final String PROCESS_POINT_ERROR_STRING = " Failed - ProcessPointId is empty or null ";
	private static final String OVERALL_CHECK_STATUS = "OVERALL_CHECK_STATUS";
	private ProductCheckUtil productCheckUtil;
	private String errorMessage;
	private BaseProduct product;
	private ProcessPoint processPoint;
	private Engine engine;
	private List<BaseProduct> products;


	Logger logger = Logger.getLogger();
	
	@Override
	public Object check(InputData data) {
		if(!validateInputData(data)) {
			logger.error(data.getCheckType(), getErrorMessage());
			return new OutputData(false, getErrorMessage()); 
		}
		
		Object returnObj = doCheck(data);
		
		logResult(returnObj);
		
		return returnObj;
		
	}


	private Object doCheck(InputData data) {
		Map<String, OutputData> resultMap = new HashMap<String, OutputData>();

		if (products == null || products.isEmpty()) {
			ProductCheckUtil productCheckUtil = getProductCheckUtil(data);

			for (String checkType : data.getCheckType().split(Delimiter.COMMA)) {
				if(checkType.contains(PMQA) && isContain(data.getSpecialCheckerNames(), checkType)) {					
					((Engine)product).setProductId(String.format("%-17s", ((Engine)product).getProductId()).replace(' ', '0'));
				}
				addCheckResult(resultMap, productCheckUtil.check(StringUtils.trim(checkType)), checkType);
			}
			addOverallStatus(resultMap);
			return resultMap;
		} else {
			for (BaseProduct productToCheck : products) {
				product = productToCheck;
				ProductCheckUtil productCheckUtil = getProductCheckUtil(data);

				for (String checkType : data.getCheckType().split(Delimiter.COMMA)) {
					addMultiCheckResult(resultMap, productCheckUtil.check(StringUtils.trim(checkType)), checkType);
				}
			}
			addOverallStatus(resultMap);
			return resultMap;
		}

	}

	private ProductCheckUtil getProductCheckUtil(InputData data) {
		if(productCheckUtil == null ) {
			if(data.getPartNames() == null || data.getPartNames().isEmpty())
				return new ProductCheckUtil(product, processPoint);
			else
				return new ProductCheckUtil(product, processPoint, data.getPartNames());
		}
		
		return productCheckUtil;

	}

	private OutputData getReturnOjbect(Object checkResult, String checkType) {
		return new OutputData(isPassed(checkType, checkResult), getOutputMessage(checkType, checkResult));
	}

	private void addOverallStatus(Map<String, OutputData> resultMap) {
		boolean overallStatus = true;
		StringBuilder failedMessage = new StringBuilder();
		for(Map.Entry<String, OutputData> entry : resultMap.entrySet()) {
			overallStatus &= entry.getValue().getCheckResult();
			if(!entry.getValue().getCheckResult()) {
				if(failedMessage.length() > 0) failedMessage.append(Delimiter.SEMI_COLON);
				failedMessage.append(entry.getValue().getMessage());
			}
		}
		
		resultMap.put(OVERALL_CHECK_STATUS, new OutputData(overallStatus, failedMessage.toString()));
		
	}

	private void addCheckResult(Map<String, OutputData> resultMap, Object checkResult, String checkType) {
		resultMap.put(checkType, getReturnOjbect(checkResult, checkType));
	}
	

	private void addMultiCheckResult(Map<String, OutputData> resultMap, Object checkResult, String checkType) {
		resultMap.put(checkType + " : " + product.getProductId(), getReturnOjbect(checkResult, checkType));
	}

	private Object doCheckByType(InputData data, String checkType) {
		ProductCheckUtil util = getProductCheckUtil(data);
		if(SpecialCheker.has(checkType)) {
			return processSpecialCheck(checkType, data, util);
		} else {
			if(checkType.contains(PMQA) && isContain(data.getSpecialCheckerNames(), checkType)) {					
				((Engine)product).setProductId(String.format("%-17s", ((Engine)product).getProductId()).replace(' ', '0'));
			}
		    return util.check(checkType);
		}
	}
	
	private boolean isContain(List<String> pList, String checkType) {
		if(pList == null) return false;
		
		for(String s : pList) {
			if(s.contains(checkType)) return true;
		}
		
		return false;
		
	}


	private Object processSpecialCheck(String checkType, InputData data, ProductCheckUtil util) {
		SpecialCheker checker = SpecialCheker.valueOf(checkType);
		switch(checker) {
			case CHECK_ENGINE_TYPE_FOR_ENGINE_ASSIGNMENT:
				return util.checkEngineTypeForEngineAssignment((Frame)product, engine, data.getUseAltEngineMto());
			case PASSED_PROCESS_POINT_CHECK:
				return util.passedProcessPointCheck(data.getProcessPointIds());
				
			default:
				return util.check(checkType);
		} 
		
	}


	private List<String> processPartOnHoldCheck(InputData data, ProductCheckUtil util) {
		try {
			util.setInstalledPartNames(Arrays.asList(data.getPartSerialNumber()));
			return util.partOnHoldCheck(data.getPartNamesAsString());
		} catch (Exception e) {
			logger.error(e,PART_HOLD_CHECK_FAILED);
			List<String> result =  new ArrayList<String>();
			result.add(PART_HOLD_CHECK_FAILED);
			return result;
		}
	}

	private boolean validateInputData(InputData data) {
		logger.info("Input Data:" + data);
		HeadLessPropertyBean propertyBean = PropertyService.getPropertyBean(HeadLessPropertyBean.class, data.getProcessPointId() != null?data.getProcessPointId():"");
		if (StringUtils.isEmpty(data.getCheckType())) {
			String commaSeparatedChecks = String.join(",", propertyBean.getProductCheckTypes());
			if (StringUtils.isEmpty(commaSeparatedChecks)) {
				this.errorMessage = "Missing check type!";
				return false;
			} else
				data.setCheckType(commaSeparatedChecks);
		}
		
		if(StringUtils.isEmpty(data.getProductType())) {
			this.errorMessage = "Missing product type!";
			return false;
		}
		
		if(StringUtils.isEmpty(data.getProductId())) {
			this.errorMessage = data.getCheckType() + PRODUCT_ID_ERROR_STRING;
			return false;
		} else if (!data.getProductId().contains(Delimiter.COMMA)) {
			this.product = ProductTypeUtil.findProduct(data.getProductType(), data.getProductId());
			if (product == null) {
				this.errorMessage = data.getProductType() + CHECK_PRODUCT_ID_FAILED + data.getProductId() + NOT_EXIST;
				return false;
			}
		} else {
			products = new ArrayList<BaseProduct>();
			for (String productId : data.getProductId().split(Delimiter.COMMA)) {
				this.product = ProductTypeUtil.findProduct(data.getProductType(), productId);
				if (product == null) {
					this.errorMessage = data.getProductType() + CHECK_PRODUCT_ID_FAILED + productId + NOT_EXIST;
					return false;
				}
				products.add(this.product);
			}
		}
		
		if(data.getProcessPointId() != null){
			this.processPoint = getProcessPoint(data.getProcessPointId());
			if(this.processPoint == null) {
				this.errorMessage = data.getCheckType() + CHECK_PROCESS_POINT_FAILED + data.getProcessPointId() + NOT_EXIST;
				return false;
			}
		}
		
		
		
		if((data.getCheckType().contains(SpecialCheker.CHECK_ENGINE_TYPE_FOR_ENGINE_ASSIGNMENT.name()))) {
			if(!validateEngineAssinmentData(data))
				return false;
		}
		
		return true;
	}
	
	private boolean validateEngineAssinmentData(InputData data) {
		if(StringUtils.isEmpty(data.getEngine())) {
			this.errorMessage = "Missing Engine!";
			return false;
		} else {
			this.engine = ServiceFactory.getDao(EngineDao.class).findByKey(data.getEngine());
			if (engine == null) {
				this.errorMessage= data.getEngine() + CHECK_ENGINE_ID_FAILED + data.getEngine() + NOT_EXIST;
				return false;
			} 
		}
		return true;
	}
	
	private ProcessPoint getProcessPoint(String processPointId){
		return  ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
	}
	
	
	private String getOutputMessage(String checkType, Object checkResult) {
		Boolean passed = isPassed(checkType, checkResult);
		StringBuilder sb = new StringBuilder(getActualCheckType(checkType));
		sb.append(passed ? CHECK_PASSED : CHECK_FAILED);
		sb.append(product.getProductId());
		CheckerBooleanFalseAsGood specialType = CheckerBooleanFalseAsGood.getChecker(checkType);
		if(null != specialType)
			sb.append(Delimiter.SPACE).append(checkType).append(Delimiter.COLON);
		if(checkResult != null) {
			sb.append(Delimiter.COMMA).append(ProductCheckUtil.formatTxt(checkResult, ":"));
		}
		
		return sb.toString();
	}
	
	
	private String getActualCheckType(String checkType) {
		CheckerBooleanFalseAsGood specialType = CheckerBooleanFalseAsGood.getChecker(checkType);
		return specialType == null ? checkType : specialType.getType();
	}


	private Boolean isPassed(String checkType, Object object) {
		if(object == null) return true;
		if(isStateCheckStyleChecker(checkType))
			return processStateCheckResults((Map<String, Object>)object);
		else
			return processCheckResults(checkType, object);
	}


	private boolean isStateCheckStyleChecker(String checkType) {
		return CheckerStateCheckStyle.hasChecker(checkType);
	}


	private Boolean processCheckResults(String checkType, Object object) {
		if(ClassUtils.isAssignable(object.getClass(),Boolean.class)){
			if(!CheckerBooleanFalseAsGood.hasChecker(checkType))
				return (Boolean)object;
			else
				return !(Boolean)object;
		} else if(ClassUtils.isAssignable(object.getClass(),List.class)) {
			return ((List)object).isEmpty();
	    } else if(ClassUtils.isAssignable(object.getClass(),Map.class)) {
	    	return ((Map)object).isEmpty();
	    } else if(ClassUtils.isAssignable(object.getClass(),String.class)){
	    	return object == null;
	    }
		
		logger.warn("Invalid result type:", object.getClass().getSimpleName());

		return false;
	    	
		
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String toCamelString (String checkType) {
	    String[] parts = checkType.split("_");
	    StringBuilder camelCaseString = new StringBuilder(parts[0].toLowerCase());
	    for (int i = 1; i < parts.length; i++) {
	        camelCaseString.append(parts[i].substring(0, 1).toUpperCase());
	        camelCaseString.append(parts[i].substring(1).toLowerCase());
	    }
	    return camelCaseString.toString();
	}
	
	public String toCheckName (String checkType) {
	    String[] parts = checkType.split("_");
	    StringBuilder checkName = new StringBuilder();
	    for (int i = 1; i < parts.length; i++) {
	    	if(checkName.length() > 0) checkName.append(Delimiter.SPACE);
	    	checkName.append(parts[i].substring(0, 1).toUpperCase());
	    	checkName.append(parts[i].substring(1).toLowerCase());
	    }
	    return checkName.toString();
	}


	private void logResult(Object returnObj) {
		Gson gson = new Gson();
		logger.info("Checker Result:" + gson.toJson(returnObj));
	}
	
	private Boolean processStateCheckResults(Map<String, Object> checkResults) {
		boolean resultStatus = true;
		for(Entry<String, Object> e:  checkResults.entrySet()){ 
			if(ClassUtils.isAssignable(e.getValue().getClass(),Boolean.class)){
				
				resultStatus &= (Boolean)e.getValue();
			} else if(ClassUtils.isAssignable(e.getValue().getClass(),Map.class))
				resultStatus &= processStateCheckResults((Map<String, Object>)e.getValue()); 
			else 
				logger.warn("Invalid data type: " + e.getValue().getClass());
		}
		
		return resultStatus;
	}

}
