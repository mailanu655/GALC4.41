package com.honda.galc.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * @author Wade Pei <br>
 *         Jul 15, 2013
 * @author Keifer Xing
 */
public class PartCheckUtil {

	private ProcessPoint processPoint;
	private String partNumber;
	private String productId;
	private String partName;

	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public static Map<String, Object> check(String partNumber, String partName, String productId,
			String[] partCheckTypes) {
		return check(partNumber, partName, productId, null, partCheckTypes);
	}
	public static Map<String, Object> check(String productId,String partNumber,String[] partCheckTypes) {
		return check(partNumber, null, productId, null, partCheckTypes);
	}
	
	public static Map<String, Object> check(String partNumber, String partName, String productId,
			ProcessPoint processPoint, String[] partCheckTypes) {
		Map<String, Object> checkResults = new HashMap<String, Object>(0);
		if (null == partCheckTypes || partCheckTypes.length < 1) {
			return checkResults;
		}
		PartCheckUtil partCheckUtil = new PartCheckUtil();
		Object result = null;
		for (String partCheckType : partCheckTypes) {
			if (!StringUtils.isEmpty(partCheckType)) {
				result = partCheckUtil.initializeAndCheck(partNumber, partName, productId, processPoint, partCheckType.trim());
				if (isValid(result)) {
					// As long as we got one valid result, we put it in the Map and return soon.
					checkResults.put(partCheckType, result);
					return checkResults;
				}
			}
		}
		return checkResults;
	}

	public static  void checkWithExceptionalHandling(String partNumber, String partName, String productId,
			String[] partCheckTypes){
		if(null!=partCheckTypes&&partCheckTypes.length>0){
			Map<String, Object> result = PartCheckUtil.check(partNumber, partName, productId, partCheckTypes);
			for(String key:result.keySet()){
				Object value = result.get(key);
				if(value instanceof Boolean && (Boolean)value){
					throw new TaskException(PartCheckType.valueOf(key).getName(), "Part SN:");
				}
			}
		}
	}
	public static  void checkWithExceptionalHandling(String partNumber, String productId,
			String[] partCheckTypes){
		if(null!=partCheckTypes&&partCheckTypes.length>0){
			Map<String, Object> result = PartCheckUtil.check(productId, partNumber,partCheckTypes);;
			for(String key:result.keySet()){
				Object value = result.get(key);
				if(value instanceof Boolean && (Boolean)value){
					throw new TaskException(PartCheckType.valueOf(key).getName(), "Part SN:");
				}
			}
		}
	}
	
	public Object initializeAndCheck(String partNumber, String partName, String productId, ProcessPoint processPoint,
			String partCheckType) {
		setPartNumber(partNumber);
		setPartName(partName);
		setProductId(productId);
		setProcessPoint(processPoint);
		return executeCheck(partCheckType);
	}

	public boolean partNumberNotContainsProductIdCheck() {
		if (StringUtils.isBlank(partNumber) || StringUtils.isBlank(productId) || partNumber.indexOf(productId) < 1) {
			return true;
		}
		return false;
	}

	public boolean duplicatePartInstalledCheck() {
		List<InstalledPart> installedParts = ServiceFactory.getDao(InstalledPartDao.class)
				.findAllByPartNameAndSerialNumber(partName, partNumber);
		for (InstalledPart part : installedParts) {
			if (!part.getId().getProductId().equals(productId)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean duplicatePartForAntitheftCheck() {
		List<InstalledPart> installedPartList = ServiceFactory.getDao(InstalledPartDao.class).findAllByProductIdAndPartSerialNo(productId,partNumber);
			//context.getDbManager().findDuplicatePartsByProductId(productId,partNumber);
		if (null!=installedPartList && installedPartList.size()>0) 
			return true;
		return false;
	}
	
	public boolean partNumberNullCheck() {
		if(partNumber == null)
			return true;
		return false;
	}
	public boolean productIdNullCheck() {
		if(productId == null)
			return true;
		return false;
	}

	@SuppressWarnings({ "rawtypes" })
	private static boolean isValid(Object object) {
		if (object == null)
			return false;
		if (object instanceof List)
			return !((List) object).isEmpty();
		if (object instanceof Map)
			return !((Map) object).isEmpty();
		if (object instanceof Boolean)
			return (Boolean) object;
		return true;
	}

	private Object executeCheck(String partCheckType) {
		String methodName = getMethodName(partCheckType);
		Method method = getMethod(methodName);
		Object returnValue = null;
		try {
			returnValue = method.invoke(this, new Object[] {});
		} catch (Exception e) {
			throw new SystemException("Failed to invoke method " + methodName, e);
		}

		return returnValue;
	}

	private Method getMethod(String methodName) {
		Method method = null;
		try {
			method = ClassUtils.getPublicMethod(PartCheckUtil.class, methodName, null);
		} catch (Exception e) {
			throw new SystemException("Failed to get method " + methodName, e);
		}
		return method;
	}

	private String getMethodName(String partCheckType) {
		String[] items = partCheckType.split("_");
		boolean isFirst = true;
		String methodName = "";
		for (String item : items) {
			String name = item.toLowerCase();
			if (isFirst) {
				isFirst = false;
			} else {
				name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
			}
			methodName += name;
		}
		return methodName;
	}
	
	public static boolean antiTheftLabelCheck(LotControlRule lotControlRule, String productId, ProductBuildResult result) { 
		String partSerialNumber = result.getPartSerialNumber(); 

		String[] partCheckTypes = new String[]{PartCheckType.PART_NUMBER_NULL_CHECK.name(),PartCheckType.PRODUCT_ID_NULL_CHECK.name(), 
				PartCheckType.PART_NUMBER_NOT_CONTAINS_PRODUCT_ID_CHECK.name(),PartCheckType.DUPLICATE_PART_FOR_ANTITHEFT_CHECK.name()}; 

		checkWithExceptionalHandling(partSerialNumber, productId, partCheckTypes); 
		return true; 
	} 

}
