package com.honda.galc.util;

import java.util.List;

import com.honda.galc.entity.product.MbpnProductType;
import com.honda.galc.entity.product.ProductIdNumberDef;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * 
 * <h3>MbpnUtil Class description</h3>
 * <p> MbpnUtil description </p>
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
 * Nov.11, 2021
 *
 *
 */
public class MbpnUtil {
	
	public static boolean checkProductId(String productId,  List<MbpnProductType> mbpnProductTypes) {
		  for(MbpnProductType mbpnType: mbpnProductTypes){
	        	List<ProductIdNumberDef> productIdNumberDefs = mbpnType.getProductIdNumberDefs();
	        	if(productIdNumberDefs == null || productIdNumberDefs.size() == 0)
	        		return true; //no validation required.
	        	
	        	for(ProductIdNumberDef def : productIdNumberDefs) {
	        		 if(productId.length() == def.getLength() &&
	        			   CommonPartUtility.verification(productId, def.getMask(), PropertyService.getPartMaskWildcardFormat()))
	        			   return true;
	        	}
			}
		  
		  return false;
	}

}
