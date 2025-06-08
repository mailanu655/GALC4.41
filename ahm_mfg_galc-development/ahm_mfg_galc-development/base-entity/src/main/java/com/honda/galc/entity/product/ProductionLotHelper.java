package com.honda.galc.entity.product;

import com.honda.galc.common.exception.TaskException;

/**
 * 
 * <h3>ProductionLotHelper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductionLotHelper description </p>
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
 * @author Paul Chou
 * Nov 11, 2010
 *
 */
public class ProductionLotHelper {

	public static boolean isInLot(String productId, String startProductId, int productNoPrefixLength, int lotSize) {
		if (!isProductIdPrefixMatch(productId, startProductId, productNoPrefixLength))
			return false;

		int snDigits = productId.length() - productNoPrefixLength;
		int diff = getProductIndex(productId, snDigits) - getProductIndex(startProductId, snDigits);
		return diff >= 0 && diff < lotSize;

	}

	private static boolean isProductIdPrefixMatch(String productIdA, String productIdB, int productNoPrefixLength) {
		if (productIdA == null || productIdB == null) return false;
		if (productIdA.length() < productNoPrefixLength || productIdB.length() < productNoPrefixLength) return false;
		String prefixA = productIdA.substring(0, productNoPrefixLength);
		String prefixB = productIdB.substring(0, productNoPrefixLength);
		for (int i = 0; i < prefixA.length(); i++) {
			char charA = prefixA.charAt(i);
			if (charA == '*') continue;
			char charB = prefixB.charAt(i);
			if (charB == '*') continue;
			if (charA != charB) return false;
		}
		return true;
	}

	private static int getProductIndex(String productId, int snDigits) {
		try {
			return Integer.parseInt(productId.substring(productId.length() - snDigits));
		} catch (Exception e) {
			throw new TaskException("Error: Failed to get product index (last " + snDigits + " digits) for product id " + productId, e);
		}
	}



	public static boolean isInLot(ProductionLot lot, SubProduct subProduct, int snDigits) {
		return isInLot(lot, subProduct.getSubId(), subProduct.getId());
	}


	public static boolean isInLot(ProductionLot lot, String subId, String productId) {
		int startProductSn = getStartProductSn(lot.getStartProductId(), subId);
		int diff = SubProduct.getProductSerialNumber(productId) - startProductSn;
		
		return diff >= 0  && diff < lot.getLotSize();
	}



	public static int getStartProductSn(String startProductId, String subId) {
		String[] startProducts = startProductId.split("\\*");
		for(String start: startProducts){
			if(start.startsWith(subId))
				return Integer.parseInt(start.substring(1));
		}
		
		throw new TaskException("Error: Failed to find start product Sn for sub product id:" + subId);
	}
}
