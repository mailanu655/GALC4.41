package com.honda.galc.service.utils;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.HoldParmDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductDigitCheckUtil;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.data.ProductNumberDef.TokenType;
import com.honda.galc.entity.product.HoldParm;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>DiecastUtil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DiecastUtil description </p>
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
 * <TD>Apr 27, 2012</TD>
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
 * @since Apr 27, 2012
 */
public class DiecastUtil extends ServiceUtil{
	private static final String PRE_HEAT = "X";
	
	public static boolean validateProductDcNumber(String productType, String productId, Map<Object, Object> context, String productName) {
		if(StringUtils.isEmpty(productId)){
			context.put(TagNames.EXCEPTION.name(), productType + " product id number is null.");
			return false;
		}
		
		List<ProductNumberDef> productNumberDefs = getDcNumberDefs(productType);
		if(!ProductNumberDef.isNumberValid(productId, productNumberDefs)){
			context.put(getTag(TagNames.VALID_DC_NUMBER_FORMAT.name(), productName), false);
			context.put(TagNames.EXCEPTION.name(), "Invalid " + productType + " DC number:" + productId);
			return false;
		} 
		else
			context.put(getTag(TagNames.VALID_DC_NUMBER_FORMAT.name(), productName), true);
		
		return true;
	}
	
	public static boolean validateProductMcNumber(String productType, String mcNumber, Map<Object, Object> context, String productName) {
		if(StringUtils.isEmpty(mcNumber)){
			context.put(TagNames.EXCEPTION.name(), productType + " MC number is null.");
			return false;
		}
		
		List<ProductNumberDef> mcNumberDefs = getMcNumberDefs(productType);
		if(!ProductNumberDef.isNumberValid(mcNumber, mcNumberDefs)){
			context.put(getTag(TagNames.VALID_MC_NUMBER_FORMAT.name(), productName), false);
			context.put(TagNames.EXCEPTION.name(), "Invalid " + productType + " MC number:" + mcNumber);
			return false;
		} 
		else
			context.put(getTag(TagNames.VALID_MC_NUMBER_FORMAT.name(), productName), true);
		
		return true;
	}
	
	public static boolean validateCheckDigit(String productType, String productId, Map<Object, Object> context, String productName) {
		if(!ProductDigitCheckUtil.isCheckDigitValid(productId)) {
			context.put(getTag(TagNames.VALID_CHECK_DIGIT.name(), productName), false);
			context.put(TagNames.EXCEPTION.name(), "Invalid " + productType + " check digit for Product Id : " + productId);
			return false;
		}
		Logger.getLogger().info("Product Id: " + productId + " check digit is valid. " + "Product Type: " + productType);
		context.put(getTag(TagNames.VALID_CHECK_DIGIT.name(), productName), true);
		return true;
	}
	
	private static List<ProductNumberDef> getMcNumberDefs(String productType) {
		ProductType type = ProductType.valueOf(productType);
		List<ProductNumberDef> list = findNumberDefs(type, NumberType.MC);
		return list;
	}

	public static Boolean isDcNumberMarkedPreheat(String dcNumber){
		return PRE_HEAT.equals(ProductNumberDef.DCH.getToken(TokenType.PART_LEVEL.name(), dcNumber));
	}

	private static List<ProductNumberDef> getDcNumberDefs(String productType) {
		ProductType type = ProductType.valueOf(productType);
		List<ProductNumberDef> list = findNumberDefs(type, NumberType.DC);
		return list;
	}

	public static ProductNumberDef getProductNumberDef(ProductType productType, NumberType numberType, String sn) {
		if (productType == null || numberType == null) {
			return null;
		}
		List<ProductNumberDef> list = findNumberDefs(productType, numberType);
		if (list == null || list.isEmpty()) {
			return null;
		}
		for (ProductNumberDef def : list) {
			if (def.isNumberValid(sn)) {
				return def;
			}
		}
		return null;
	}
	
	
	public static List<ProductNumberDef> findNumberDefs(ProductType productType, NumberType numberType) {
		if (productType == null || numberType == null) {
			return new ArrayList<ProductNumberDef>();
		}
		ProductTypeDao productTypeDao = ServiceFactory.getDao(ProductTypeDao.class);
		ProductTypeData data = productTypeDao.findByKey(productType.name());
		if (data == null) {
			return  new ArrayList<ProductNumberDef>();
		}
		return ProductNumberDef.filter(data.getProductNumberDefs(), productType, numberType);
	}
	
	public static void processDieHold(ProductType productType, String productId) {
		List<HoldParm> activeHolds = getDao(HoldParmDao.class).findActiveHolds(productType.name());
		for(HoldParm hold : activeHolds){
			if(isProcessHold(hold, productId, productType)){
				updateHold(hold, productId, getCurrentDateString(), productType);
			}
		}
	}

	public static void updateHold(HoldParm hold, String productId,String currentDateString, ProductType productType) {
		HoldResult holdResult = new HoldResult(productId, 0);
		holdResult.setProductionDate(java.sql.Date.valueOf(currentDateString));
		holdResult.setHoldAssociateNo(hold.getHoldAssociateId());
		holdResult.setHoldAssociateName(hold.getHoldAssociateName());
		holdResult.setHoldReason(hold.getHoldReason());
		holdResult.setLotHoldStatus(0);
		holdResult.setHoldAssociatePhone("");
		holdResult.setHoldAssociatePager("");
		holdResult.setProductionLot("");
		holdResult.setProductSpecCode("");
		holdResult.setQsrId(hold.getQsrId());
		
		getDao(HoldResultDao.class).save(holdResult);
		ProductTypeUtil.getProductDao(productType).updateHoldStatus(productId, 1);
		
		
	}

	public static boolean isProcessHold(HoldParm hold, String productId,ProductType productType) {

		ProductNumberDef numberDef = getProductNumberDef(productType, NumberType.DC, productId);
		
		if (numberDef == null) {
			return false;
		}
		if (!isValidModelCode(hold, productId, numberDef)) {
			return false;
		}
		if (!isValidMachineNumber(hold, productId, numberDef)) {
			return false;
		}

		if (!isValidDieCoreNumber(hold, productId, numberDef)) {
			return false;
		}
		
		return isValidDate(hold);
	}

	public static boolean isValidModelCode(HoldParm hold, String productId, ProductNumberDef numberDef) {
		if (StringUtils.isBlank(hold.getModelCode())) {
			return true;
		}
		String model = numberDef.getToken(TokenType.MODEL.name(), productId);
		return hold.getModelCode().equals(model);
	}	
	
	public static boolean isValidMachineNumber(HoldParm hold, String productId, ProductNumberDef numberDef) {
		String machine = numberDef.getToken(TokenType.LINE.name(), productId);
		return hold.getMachineNumber().equals(machine);
	}

	public static boolean isValidDieNumber(HoldParm hold, String productId, ProductNumberDef numberDef) {
		if (hold.getDieNumber() == null) {
			return false;
		}
		String die = numberDef.getToken(TokenType.DIE.name(), productId);
		return hold.getDieNumber().contains(die);
	}

	public static boolean isValidCoreNumber(HoldParm hold, String productId, ProductNumberDef numberDef) {
		if (hold.getCoreNumber() == null) {
			return false;
		}
		if (!numberDef.getTokens().containsKey(TokenType.CORE.name())) {
			return false;
		}
		String core = numberDef.getToken(TokenType.CORE.name(), productId);
		return hold.getCoreNumber().contains(core);
	}
	
	public static boolean isValidDieCoreNumber(HoldParm hold, String productId, ProductNumberDef numberDef) {

		if (numberDef.getTokens().containsKey(TokenType.CORE.name())) {
			if (StringUtils.isBlank(hold.getCoreNumber())) {
				return isValidDieNumber(hold, productId, numberDef);
			} else if (StringUtils.isBlank(hold.getDieNumber())) {
				return isValidCoreNumber(hold, productId, numberDef);
			} else {
				return isValidDieNumber(hold, productId, numberDef) && isValidCoreNumber(hold, productId, numberDef);
			}
		} else {
			return isValidDieNumber(hold, productId, numberDef);
		}
	}
	
	
	public static boolean isValidDate(HoldParm hold) {
		try {
			Date currentDate = getCurrentDate();
			return (currentDate.equals(hold.getStartDate()) || currentDate.after(hold.getStartDate())) &&
			       (currentDate.equals(hold.getStopDate()) || currentDate.before(hold.getStopDate()));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
