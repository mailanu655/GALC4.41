package com.honda.galc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.MbpnDef;
import com.honda.galc.data.ProductSpecCodeDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.service.utils.ProductTypeUtil;


public class ProductSpecUtil {
	
	private static final String GET_PRODUCT_SPEC_CODE = "getProductSpecCode";
	public static final String WILDCARD = "*";
	public static final String SQL_SINGLE_WILDCARD = "_";
	public static final String SQL_MULTI_WILDCARD = "%";
	private static final String GET_PART_NUMBER = "getPartNumber";
	
	public static String extractModelYearCode(String productSpecCode) {
		return StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,0,1));
	}
	
	public static String extractModelCode(String productSpecCode) {
		return StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,1,4));
	}
	
	public static String extractModelTypeCode(String productSpecCode) {
		return StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,4,7));
	}
	
	public static String extractModelOptionCode(String productSpecCode) {
		return StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,7,10));
	}
	
	public static String extractExtColorCode(String productSpecCode) {
		return StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,10,20));
	}
	
	public static String extractIntColorCode(String productSpecCode) {
		String intC = StringUtils.trimToEmpty(StringUtils.substring(productSpecCode,20,22));
		return intC;
	}
	
	public static String excludeToModelYearCode(String productSpecCode) {
		return productSpecCode.substring(0, 1);
	}
	
	public static String excludeToModelCode(String productSpecCode) {
		return productSpecCode.substring(0, 4);
	}
	
	public static String excludeToModelTypeCode(String productSpecCode) {
		return productSpecCode.substring(0, 7);
	}
	
	public static String excludeToModelOptionCode(String productSpecCode) {
		return productSpecCode.substring(0, 10);
	}
	
	public static String excludeToExtColorCode(String productSpecCode) {
		return productSpecCode.substring(0, 20);
	}
	
	public static String padModelCode(String modelCode) {
		return StringUtils.rightPad(modelCode, 3, " ");
	}
	
	public static String padModelTypeCode(String modelTypeCode) {
		return StringUtils.rightPad(modelTypeCode, 3, " ");
	}
	
	public static String padModelOptionCode(String modelOptionCode) {
		return StringUtils.rightPad(modelOptionCode, 3, " ");
	}
	
	public static String padExtColorCode(String extColorCode) {
		return StringUtils.rightPad(extColorCode, 10, " ");
	}
	
	public static String padIntColorCode(String intColorCode) {
		return StringUtils.rightPad(intColorCode, 2, " ");
	}
	
	public static String trimColorCode(String productSpecCode) {
		return StringUtils.trim(StringUtils.substring(productSpecCode,0,10));
	}
	
	public static boolean isWildCard(String code) {
		return WILDCARD.equals(StringUtils.trim(code));
	}
	
	private static boolean isMbpn(Object obj){
		try{
			String productType = (String)ReflectionUtils.invoke(obj, "getProductType", new Object[]{});
			if(productType != null && ProductTypeUtil.isMbpnProduct(productType)) 
				return true;
		
		}catch(Exception e){
		}
		return false;
	}
	
	private static boolean matchMbpn(MbpnDef mbpnDef, String specCode, String wildCardSpecCode) {
		if (wildCardSpecCode.trim().length() < mbpnDef.getStartPosition()) {
			return true;
		}
		String token = mbpnDef.getValue(wildCardSpecCode);
		if (StringUtils.isBlank(token) || isWildCard(token)) {
			return true;
		}
		String pattern = mbpnDef.getValue(specCode);	
		return token.equals(pattern);
	}
	
	public static boolean matchMbpn(String specCode, String wildCardSpecCode) {
		MbpnDef[] mbpnDef = {MbpnDef.MAIN_NO, MbpnDef.CLASS_NO, MbpnDef.PROTOTYPE_CODE, MbpnDef.TYPE_NO, MbpnDef.SUPPLEMENTARY_NO, MbpnDef.TARGET_NO, MbpnDef.HES_COLOR};
		for (MbpnDef def : mbpnDef) {
			if (!matchMbpn(def, specCode, wildCardSpecCode)) {
				return false;
			}
		}
		return true;
	}	
	
	public static boolean matchProductSpec(String specCode, String wildCardSpecCode) {
		boolean match = (extractModelYearCode(wildCardSpecCode).length() <=0 ||isWildCard(extractModelYearCode(wildCardSpecCode)) || extractModelYearCode(specCode).equals(extractModelYearCode(wildCardSpecCode))) &&
				(extractModelCode(wildCardSpecCode).length() <=0 || extractModelCode(specCode).equals(extractModelCode(wildCardSpecCode))) &&
				(extractModelTypeCode(wildCardSpecCode).length() <= 0 || 
						extractModelTypeCode(specCode).equals(extractModelTypeCode(wildCardSpecCode))) && 
				(extractModelOptionCode(wildCardSpecCode).length() <= 0 ||
						extractModelOptionCode(specCode).equals(extractModelOptionCode(wildCardSpecCode))) && 
				(extractExtColorCode(wildCardSpecCode).length() <= 0 ||
						extractExtColorCode(specCode).equals(extractExtColorCode(wildCardSpecCode))) &&
				(extractIntColorCode(wildCardSpecCode).length() <= 0 || 
						extractIntColorCode(specCode).equals(extractIntColorCode(wildCardSpecCode)));
		return match;
	}
	
	public static boolean match(String specCode, String wildCardSpecCode, boolean isMbpn) {
		if(!isMbpn){
			return matchProductSpec(specCode, wildCardSpecCode);
		}else{
			return matchMbpn(specCode, wildCardSpecCode); 
		}
	}
	
	public static <T> List<T> getMatchedList(String specCode, List<T> specCodeWithWildCardList, Class<T> type) {
		List<T> results = new ArrayList<T>();
		boolean isMbpn = false;
		if (specCodeWithWildCardList != null) {
			for(T obj : specCodeWithWildCardList) {
				String specCodeMask = (String)ReflectionUtils.invoke(obj, GET_PRODUCT_SPEC_CODE, new Object[]{});
				isMbpn = isMbpn(obj);
				if(match(specCode,specCodeMask,isMbpn)) results.add(obj);
			}
			if(!isMbpn){
				Collections.sort(results, new ProductSpecCodeComparator<T>());
			}
		}
		return results;
	
	}
	
	public static <T> List<T> getMatchedRuleList(String specCode, String productType, List<T> specCodeWithWildCardList, Class<T> type) {
		List<T> results = new ArrayList<T>();
		boolean isMbpn = false;
		if(productType.equals(ProductType.MBPN.name())) isMbpn = true;
		if (specCodeWithWildCardList != null) {
			for(T obj : specCodeWithWildCardList) {
				String specCodeMask = (String)ReflectionUtils.invoke(obj, GET_PRODUCT_SPEC_CODE, new Object[]{});
				if(match(specCode,specCodeMask,isMbpn)) results.add(obj);
			}
			if(!isMbpn){
				Collections.sort(results, new ProductSpecCodeComparator<T>());
			}
		}
		return results;
	
	}
	
	
	public static <T> T getMatched(String specCode, List<T> specCodeWithWildCardList, Class<T> type) {
		List<T> results = getMatchedList(specCode,specCodeWithWildCardList,type);
		return results.isEmpty() ? null : results.get(results.size() -1);
	}
	
	private static abstract class SpecComparator<T> implements Comparator<T> {
		
		protected abstract String getSpecCode(T object);
		
		public int compare(T object1, T object2) {
			if(object1 == null || object2 == null) return 0;
			String specCode1 = getSpecCode(object1);
			String specCode2 = getSpecCode(object2);
			
			int modelYearCode = extractModelYearCode(specCode1).compareToIgnoreCase(extractModelYearCode(specCode2));
			if (modelYearCode != 0)
				return modelYearCode;
			
			int modelCode = extractModelCode(specCode1).compareToIgnoreCase(extractModelCode(specCode2));
			if (modelCode != 0)
				return modelCode;
			int typeCode = extractModelTypeCode(specCode1).compareToIgnoreCase(extractModelTypeCode(specCode2));
			if (typeCode != 0)
				return typeCode;
			int optionCode = extractModelOptionCode(specCode1).compareToIgnoreCase(extractModelOptionCode(specCode2));
			if (optionCode != 0)
				return optionCode;
			int extColorCode = extractExtColorCode(specCode1).compareToIgnoreCase(extractExtColorCode(specCode2));
			if (extColorCode != 0)
				return extColorCode;
			int intColorCode = extractIntColorCode(specCode1).compareToIgnoreCase(extractIntColorCode(specCode2));
			if (intColorCode != 0)
				return intColorCode;
			
			return intColorCode;
		}

	}
	
	private static class PartNumberComparator<T> extends SpecComparator<T> {
		public String getSpecCode(T object) {
			return (String)ReflectionUtils.invoke(object, GET_PART_NUMBER, new Object[]{});
		}
	}
	
	private static class  ProductSpecCodeComparator<T> extends SpecComparator<T> {
		public String getSpecCode(T object) {
			return (String)ReflectionUtils.invoke(object, GET_PRODUCT_SPEC_CODE, new Object[]{});
		}
	}
	
	public static <T> T getMatchedItem(String specCode, List<T> specCodeWithWildCardList, Class<T> type) {
		return getMatchedItem(specCode,specCodeWithWildCardList, type, false);
	}
		
	/*
	 * this is the existing logic in LotControlPartUtil class which is used by lot control process
	 */
	public static <T> T getMatchedItem(String specCode, List<T> specCodeWithWildCardList, Class<T> type, boolean isMbpn) {
		return getMatchedItem(specCode, specCodeWithWildCardList, type, isMbpn, GET_PRODUCT_SPEC_CODE);
	}
	
	public static <T> T getMatchedPartSpec(String specCode, List<T> specCodeWithWildCardList, Class<T> type) {
		return getMatchedItem(specCode, specCodeWithWildCardList, type, true, GET_PART_NUMBER);
	}
	
	
	public static <T> T getMatchedItem(String specCode, List<T> specCodeWithWildCardList, Class<T> type, boolean isMbpn, String method) {
		int[] iHitCount;
		int[] iUnHitCount;
		iHitCount = new int[specCodeWithWildCardList.size()];
		iUnHitCount = new int[specCodeWithWildCardList.size()];
		// check MTOC
		int i = 0;
		int iMaxCount = 0;
		for(T obj : specCodeWithWildCardList) {
			String specCodeMask = (String)ReflectionUtils.invoke(obj, method, new Object[]{});
			
			if(isMbpn){
				hitCountMbpn(specCode, iHitCount,iUnHitCount,i,specCodeMask);
			}else{
				hitCount(specCode, iHitCount, iUnHitCount, i, specCodeMask);
			}
		
			// set Max Count
			if ((iUnHitCount[i] == 0) && (iMaxCount  < iHitCount[i])) {
				iMaxCount = iHitCount[i];
			}
			
			i++;

		}
		List<T> bestMatchList = new ArrayList<T>();
		for (i = 0; i < iUnHitCount.length; i++) {
			if ((iUnHitCount[i] == 0) && (iHitCount[i] == iMaxCount)) {
				bestMatchList.add(specCodeWithWildCardList.get(i));
			}
		}
		if(bestMatchList.size()>0){
			sortList(bestMatchList,method);
			return bestMatchList.get(bestMatchList.size()-1);
		}
		return null;
	}
	
	private static <T> void sortList(List<T> results, String methodStr) {
		if(GET_PRODUCT_SPEC_CODE.equals(methodStr)) {
			Collections.sort(results, new ProductSpecCodeComparator<T>());
		}else {
			Collections.sort(results, new PartNumberComparator<T>());
		}
	}
	
	private static void hitCount(String specCode, int[] iHitCount,
			int[] iUnHitCount, int i, String specCodeMask) {
		hitCount(extractModelYearCode(specCodeMask), extractModelYearCode(specCode), iHitCount, iUnHitCount, i);
		hitCount(extractModelCode(specCodeMask), extractModelCode(specCode), iHitCount, iUnHitCount, i);
		hitCount(extractModelTypeCode(specCodeMask), extractModelTypeCode(specCode), iHitCount, iUnHitCount, i);
		hitCount(extractModelOptionCode(specCodeMask), extractModelOptionCode(specCode), iHitCount, iUnHitCount, i);
		hitCount(extractExtColorCode(specCodeMask), extractExtColorCode(specCode), iHitCount, iUnHitCount, i);
		hitCount(extractIntColorCode(specCodeMask), extractIntColorCode(specCode), iHitCount, iUnHitCount, i);
	}
	
	private static void hitCount(String value, String checkValue, int[] iHitCount, int[] iUnHitCount, int i) {
		if (value.equals("*") || StringUtils.isEmpty(StringUtils.trimToEmpty(value))) {
			
		}
		else if (value.equals(checkValue)) {
			iHitCount[i]++;
		}
		else {
			iUnHitCount[i]++;
		}
	}
		
	private static void hitCountMbpn(String mbpnSpecCode, int[] iHitCount, int[] iUnHitCount, int i, String specCode) {
		hitCountMbpn(MbpnDef.MAIN_NO, MbpnDef.MAIN_NO.getValue(mbpnSpecCode), iHitCount, iUnHitCount,i, specCode);
		hitCountMbpn(MbpnDef.CLASS_NO, MbpnDef.CLASS_NO.getValue(mbpnSpecCode), iHitCount, iUnHitCount,i, specCode);
		hitCountMbpn(MbpnDef.PROTOTYPE_CODE, MbpnDef.PROTOTYPE_CODE.getValue(mbpnSpecCode), iHitCount, iUnHitCount,i, specCode);
		hitCountMbpn(MbpnDef.TYPE_NO, MbpnDef.TYPE_NO.getValue(mbpnSpecCode), iHitCount, iUnHitCount,i, specCode);
		hitCountMbpn(MbpnDef.SUPPLEMENTARY_NO, MbpnDef.SUPPLEMENTARY_NO.getValue(mbpnSpecCode), iHitCount, iUnHitCount,i,specCode);
		hitCountMbpn(MbpnDef.TARGET_NO, MbpnDef.TARGET_NO.getValue(mbpnSpecCode), iHitCount, iUnHitCount,i, specCode);
		hitCountMbpn(MbpnDef.HES_COLOR, MbpnDef.HES_COLOR.getValue(mbpnSpecCode), iHitCount, iUnHitCount,i, specCode);
	}

	private static void hitCountMbpn(MbpnDef mbpnDef, String checkValue, int[] hitCount, int[] unHitCount, int i, String specCode) {
		boolean temp = specCode.trim().length() < mbpnDef.getStartPosition() ? true : false;
		hitCount((temp? "*":mbpnDef.getValue(specCode)), checkValue, hitCount,unHitCount, i);
	
	}
	
	/**
	 * 
	 * @param specCodeMask
	 * @return
	 */
	public static String convertSpecMaskToSqlMask(String specCodeMask) {
		if (StringUtils.isBlank(specCodeMask) || SQL_MULTI_WILDCARD.equals(specCodeMask.trim())) {
			return SQL_MULTI_WILDCARD;
		}
		while (specCodeMask.endsWith(SQL_MULTI_WILDCARD)) {
			specCodeMask = StringUtils.removeEnd(specCodeMask, SQL_MULTI_WILDCARD);
		}
		if (StringUtils.isBlank(specCodeMask)) {
			return SQL_MULTI_WILDCARD;
		}
		StringBuilder sb = new StringBuilder();
		for (ProductSpecCodeDef token : ProductSpecCodeDef.values()) {
			String sqlToken = convertSpecMaskToSqlMask(token, specCodeMask);
			if (sqlToken == null) {
				break;
			}
			sb.append(sqlToken);
		}
		String sqlMask = sb.toString();
		sqlMask = sqlMask.concat(SQL_MULTI_WILDCARD);
		return sqlMask;
	}

	/**
	 * 
	 * @param token
	 * @param specCodeMask
	 * @return
	 */
	public static String convertSpecMaskToSqlMask(ProductSpecCodeDef token, String specCodeMask) {
		if (token == null || specCodeMask == null) {
			return null;
		}
		if (specCodeMask.length() <= token.getStartPosition()) {
			return null;
		}
		String value = token.getValue(specCodeMask);
		String sqlMask = null;
		if (StringUtils.isBlank(value)) {
			sqlMask = StringUtils.rightPad("", token.getLength(), SQL_SINGLE_WILDCARD);
		} else if (value.indexOf(WILDCARD) > -1) {
			sqlMask = StringUtils.replace(value, WILDCARD, SQL_SINGLE_WILDCARD);
			sqlMask = StringUtils.rightPad(sqlMask, token.getLength(), SQL_SINGLE_WILDCARD);
		} else {
			sqlMask = token.format(value);
		}
		return sqlMask;
	}
	
	
	public static boolean matchModelCode(String specCode1, String specCode2){
		return extractModelCode(specCode1).compareToIgnoreCase(extractModelCode(specCode2)) == 0;
	}
	
	public static boolean matchModelYearCode(String specCode1, String specCode2){
		return extractModelYearCode(specCode1).compareToIgnoreCase(extractModelYearCode(specCode2)) == 0;
	}
	
}
