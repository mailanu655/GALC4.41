package com.honda.galc.service.datacollection.work;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ClassUtils;

import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.ProductDataCollectorBase;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.util.ProductCheckUtil;
/**
 * 
 * <h3>ProductStateCheck</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductStateCheck description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Mar 12, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 12, 2014
 */
public class ProductStateCheck extends CollectorWork{

	private static final String DELIMITER = "^";
	private final int maxReasons;

	public ProductStateCheck(HeadlessDataCollectionContext context, ProductDataCollectorBase collector) {
		super(context, collector);
		int maxReasons;
		try { maxReasons = PropertyService.getPropertyInt(context.getProcessPointId(), "MAX_REASONS", 4); } catch (Exception e) { maxReasons = 4; }
		this.maxReasons = maxReasons;
	}

	public void doWork() throws Exception {

		if(context.getProperty().getProductStateChecks().length > 0) 
			doProductStateCheck();
		
		if(context.getProperty().getProductCheckTypes().length > 0)
			doProductCheck();

	}

	private void doProductStateCheck() {
		Map<String, Object> checkResults = checkProduct(context.getProperty().getProductStateChecks());
		context.put(TagNames.CHECK_RESULT.name(), processStateCheckResults(checkResults));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void doProductCheck() {
		
		String[] checkTypes = context.getProperty().getProductCheckTypes();
		Map<String, Object> checkReturn = ProductCheckUtil.check(getProductToCheck(), context.getProcessPoint(), checkTypes, context);
		
		Boolean overallResult = true;
		StringBuilder checkResultMessageBuilder = new StringBuilder();
		for(String checkType: checkTypes){
			Boolean typeCheckResult = !checkReturn.keySet().contains(checkType);
			overallResult &= typeCheckResult;
			
			context.put(checkType, typeCheckResult);
			if(!typeCheckResult) {
				getLogger().warn(checkType, checkReturn.get(checkType).toString());

				ChkRslt chkRslt = analyzeCheckResultEntry(checkType, checkReturn.get(checkType));
				if (chkRslt.getResult()) getLogger().warn("Analysis of check result for " + chkRslt.getKey() + " was OK.");
				else {
					if (checkResultMessageBuilder.length() > 0) checkResultMessageBuilder.append(DELIMITER);
					checkResultMessageBuilder.append(tagToName(chkRslt.getKey()));
					checkResultMessageBuilder.append(" failed:");
					checkResultMessageBuilder.append(chkRslt.getReason());
					context.put(chkRslt.getKey().concat("_").concat(TagNames.MESSAGE.name()), chkRslt.getReason());
				}
				
			}
			
			if(checkReturn.get(checkType) instanceof Map){
				Map mapResult = (Map)checkReturn.get(checkType);
				context.putAll(mapResult);
				
			}
		}
		
		context.put(TagNames.PRODUCT_TYPE_CHECK_RESULT.name(), overallResult);
		context.merge(TagNames.CHECK_RESULT.name(), overallResult);
		context.put(TagNames.CHECK_RESULT_MESSAGE.name(), checkResultMessageBuilder.toString());
		
	}
	
	public Map<String,Object> checkProduct(String[] productCheckTypes) {

		return ProductCheckUtil.check(getProductToCheck(), context.getProcessPoint(), productCheckTypes, getDevice(), context);

	}

	private Device getDevice() {
		return (collector != null) ? collector.getDeviceHelper().getDevice() : null;
	}
	
	
	@SuppressWarnings("unchecked")
	private Boolean processStateCheckResults(Map<String, Object> checkResults) {
		boolean resultStatus = true;
		for(Entry<String, Object> e:  checkResults.entrySet()){ 
			if(ClassUtils.isAssignable(e.getValue().getClass(),Boolean.class)){
				context.put(e.getKey(), e.getValue());
				resultStatus &= (Boolean)e.getValue();
			} else if(ClassUtils.isAssignable(e.getValue().getClass(),Map.class))
				resultStatus &= processStateCheckResults((Map<String, Object>)e.getValue()); 
			else 
				getLogger().warn("Invalid data type: " + e.getValue().getClass());
		}
		
		context.put(TagNames.PRODUCT_STATE_CHECK_RESULT.name(), resultStatus);
		
		return resultStatus;
	}

	@SuppressWarnings("unchecked")
	private ChkRslt analyzeCheckResultEntry(String key, Object value) {
		if (ClassUtils.isAssignable(value.getClass(),Boolean.class)) {
			return analyzeCheckResultEntryBoolean(key, (Boolean) value);
		}
		else if (ClassUtils.isAssignable(value.getClass(),List.class)) {
			return analyzeCheckResultEntryList(key, (List<Object>) value);
		}
		else if (ClassUtils.isAssignable(value.getClass(),Map.class)) {
			return analyzeCheckResultEntryMap(key, (Map<String, Object>) value);
		}
		return analyzeCheckResultEntryError(key, value);
	}

	private ChkRslt analyzeCheckResultEntryBoolean(String key, Boolean value) {
		ChkRslt chkRslt = new ChkRslt(key, !value, ""); // ProductCheckUtil returns true when NG and false when OK
		return chkRslt;
	}

	private ChkRslt analyzeCheckResultEntryError(String key, Object value) {
		String reason = "Invalid check result data type: " + value.getClass();
		getLogger().warn(reason);
		ChkRslt chkRslt = new ChkRslt(key, false, reason);
		return chkRslt;
	}

	private ChkRslt analyzeCheckResultEntryList(String key, List<Object> value) {
		String reason;
		{
			StringBuilder reasonBuilder = new StringBuilder();
			int count = 0;
			for (Object obj : value) {
				if (maxReasons > -1 && count >= maxReasons) break;
				if (count > 0) reasonBuilder.append(",");
				reasonBuilder.append(cleanWhitespace(obj));
				count++;
			}
			reason = reasonBuilder.toString();
		}
		ChkRslt chkRslt = new ChkRslt(key, (reason.length() == 0), reason);
		return chkRslt;
	}

	private ChkRslt analyzeCheckResultEntryMap(String key, Map<String, Object> value) {
		String reason;
		{
			StringBuilder reasonBuilder = new StringBuilder();
			int count = 0;
			for (Entry<String, Object> e : value.entrySet()) {
				if (maxReasons > -1 && count >= maxReasons) break;
				Boolean pass = false; // this assumes that a String reason was provided for why it failed if reason is not a boolean
				if (ClassUtils.isAssignable(e.getValue().getClass(),Boolean.class)) pass = (Boolean) e.getValue();
				if (!pass) {
					if (count > 0) reasonBuilder.append(",");
					reasonBuilder.append(cleanWhitespace(e.getKey()));
					reasonBuilder.append(" ");
					reasonBuilder.append(cleanWhitespace(e.getValue()));
					count++;
				}
			}
			reason = cleanWhitespace(reasonBuilder.toString());
		}
		ChkRslt chkRslt = new ChkRslt(key, (reason.length() == 0), reason);
		return chkRslt;
	}

	private String tagToName(String tag) {
		StringBuilder nameBuilder = new StringBuilder();
		char[] tagChars = tag.toCharArray();
		boolean capitalize = true;
		for (char tagChar : tagChars) {
			if (tagChar == '_' || Character.isWhitespace(tagChar)) {
				nameBuilder.append(' ');
				capitalize = true;
			}
			else if (Character.isLetter(tagChar)) {
				if (capitalize) {
					if (Character.isUpperCase(tagChar)) nameBuilder.append(tagChar);
					else if (Character.isLowerCase(tagChar)) nameBuilder.append(Character.toUpperCase(tagChar));
					capitalize = false;
				}
				else {
					if (Character.isUpperCase(tagChar)) nameBuilder.append(Character.toLowerCase(tagChar));
					else if (Character.isLowerCase(tagChar)) nameBuilder.append(tagChar);
				}
			}
			else nameBuilder.append(tagChar);
		}
		return nameBuilder.toString();
	}

	private String cleanWhitespace(Object o) {
		if (o == null) return null;
		return cleanWhitespace(o.toString());
	}

	private String cleanWhitespace(String s) {
		if (s == null || s.length() == 0) return s;
		StringBuilder sb = new StringBuilder();
		char[] cs = s.toCharArray();
		boolean ws = false;
		for (char c : cs) {
			if (Character.isWhitespace(c)) {
				ws = true;
				continue;
			}
			if (ws) {
				if (sb.length() > 0) sb.append(' ');
				ws = false;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	public BaseProduct getProductToCheck() {
		if(context.getProduct() != null) return  context.getProduct();
			
		BaseProduct existProduct = null;
		try {
			existProduct = ServiceUtil.getProductFromDataBase(context.getProductType().name(), context.getProductId());
			if(existProduct != null) context.setProduct(existProduct);
		} catch (Exception e) {
			getLogger().warn(e, " exception to find product.");
		}
		
		return context.getProductToCheck();
		
	}

	private class ChkRslt {
		private final String key;
		private final boolean result;
		private final String reason;
		public ChkRslt(String key, boolean result, String reason) { this.key = key; this.result = result; this.reason = reason; }
		public String getKey() { return this.key; }
		public boolean getResult() { return this.result; }
		public String getReason() { return this.reason; }
	}


}
