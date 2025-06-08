package com.honda.galc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author Subu Kathiresan
 * @date June 6, 2016
 */
public class MbpnSpecCodeUtil {

	public static String getMainNo(String productSpecCode) {
		return StringUtils.substring(productSpecCode,0,5);
	}

	public static String getClassNo(String productSpecCode) {
		return StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,5,8));
	}

	public static String getProtoTypeCode(String productSpecCode) {
		return StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,8,9));
	}

	public static String getTypeNo(String productSpecCode) {
		return StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,9,13));
	}

	public static String getSupplementaryNo(String productSpecCode) {
		return StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,13,15));
	}

	public static String getTargetNo(String productSpecCode) {
		return StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,15,17));
	}
	
	public static String getHesColor(String productSpecCode) {
		return StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,17,27));
	}

	public static String excludeToMainNo(String productSpecCode) {
		return productSpecCode.substring(0, 5);
	}

	public static String excludeToClassNo(String productSpecCode) {
		return productSpecCode.substring(0, 8);
	}

	public static String excludeToProtoTypeCode(String productSpecCode) {
		return productSpecCode.substring(0, 9);
	}

	public static String excludeToTypeNo(String productSpecCode) {
		return productSpecCode.substring(0, 13);
	}

	public static String excludeToSupplementaryNo(String productSpecCode) {
		return productSpecCode.substring(0, 15);
	}

	public static String padClassNo(String classNo) {
		return StringUtils.rightPad(classNo, 3, " ");
	}

	public static String padProtoTypeCode(String protoTypeCode) {
		return StringUtils.rightPad(protoTypeCode, 1, " ");
	}

	public static String padTypeNo(String typeNo) {
		return StringUtils.rightPad(typeNo, 4, " ");
	}

	public static String padSupplementaryNo(String supplementaryNo) {
		return StringUtils.rightPad(supplementaryNo, 2, " ");
	}

	public static String padTargetNo(String targetNo) {
		return StringUtils.rightPad(targetNo, 2, " ");
	}

	public static boolean match(String specCode, String wildCardSpecCode) {
		return excludeToMainNo(specCode).equals(excludeToMainNo(wildCardSpecCode)) && 
				getClassNo(specCode).equals(getClassNo(wildCardSpecCode)) &&
				(getProtoTypeCode(wildCardSpecCode).length() <= 0 || 
				getProtoTypeCode(specCode).equals(getProtoTypeCode(wildCardSpecCode))) && 
				(getTypeNo(wildCardSpecCode).length() <= 0 ||
				getTypeNo(specCode).equals(getTypeNo(wildCardSpecCode))) && 
				(getSupplementaryNo(wildCardSpecCode).length() <= 0 ||
				getSupplementaryNo(specCode).equals(getSupplementaryNo(wildCardSpecCode))) &&
				(getTargetNo(wildCardSpecCode).length() <= 0 || 
				getTargetNo(wildCardSpecCode).equals(getTargetNo(wildCardSpecCode)));
	}

	public static <T> List<T> getMatchedList(String specCode, List<T> specCodeWithWildCardList, Class<T> type) {
		List<T> results = new ArrayList<T>();
		for(T obj : specCodeWithWildCardList) {
			String specCodeMask = (String)ReflectionUtils.invoke(obj, "getProductSpecCode", new Object[]{});
			if(match(specCode,specCodeMask)) results.add(obj);
		}

		Collections.sort(results, new ProductSpecCodeComparator<T>());
		return results;
	}

	public static <T> T getMatched(String specCode, List<T> specCodeWithWildCardList, Class<T> type) {
		List<T> results = getMatchedList(specCode,specCodeWithWildCardList,type);
		return results.isEmpty() ? null : results.get(results.size() -1);
	}

	private static class  ProductSpecCodeComparator<T> implements Comparator<T> {

		public int compare(T object1, T object2) {
			if(object1 == null || object2 == null) return 0;
			String specCode1 = (String) ReflectionUtils.invoke(object1, "getProductSpecCode", new Object[]{});
			String specCode2 = (String) ReflectionUtils.invoke(object2, "getProductSpecCode", new Object[]{});

			int targetNo = getTargetNo(specCode1).compareToIgnoreCase(getTargetNo(specCode2));
			if (targetNo != 0)
				return targetNo;
			int supplementaryNo = getSupplementaryNo(specCode1).compareToIgnoreCase(getSupplementaryNo(specCode2));
			if (supplementaryNo != 0)
				return supplementaryNo;
			int optionCode = getTypeNo(specCode1).compareToIgnoreCase(getTypeNo(specCode2));
			if (optionCode != 0)
				return optionCode;

			return getProtoTypeCode(specCode1).compareToIgnoreCase(getProtoTypeCode(specCode2));
		}
	}
}
