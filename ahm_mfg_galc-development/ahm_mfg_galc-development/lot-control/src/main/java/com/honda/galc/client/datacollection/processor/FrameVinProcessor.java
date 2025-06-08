package com.honda.galc.client.datacollection.processor;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.FrameSpec;

/**
 * 
 * <h3>VinProcessor</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> VinProcessor description </p>
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
 * Mar 19, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class FrameVinProcessor extends ProductIdProcessor{
	public enum VinScanType{CERT_LABEL,FIC,ALL};
	
	public FrameVinProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}
	
	public FrameSpec findProductSpec(String productSpecCode) {
		
		FrameSpec frameSpec = context.getFrameSpec(productSpecCode);
		
		if(frameSpec != null) return frameSpec;
		
		throw new TaskException("Invalid product spec code:" + productSpecCode );
	}
	
	@Override
	public String getProductSpecCode(String productId) {
		return getProductSpecCode(productId, context.getFrameSpecs());
	}
	
	protected void confirmProductId(ProductId productId)
			throws SystemException, TaskException, IOException
	{
		validateVinScanType(productId);
		super.confirmProductId(productId);
	}

	protected void validateVinScanType(ProductId productId) {
		String scanTypeStr = context.getFrameLinePropertyBean().getVinScanType();
		if(StringUtils.isEmpty(scanTypeStr)) scanTypeStr=VinScanType.ALL.name();
		String inputProductId = StringUtils.trimToEmpty(productId.getProductId());
		 
		switch(VinScanType.valueOf(scanTypeStr)) {
		case CERT_LABEL:
			if(!validateCertLabel(inputProductId))
				throw new TaskException("Invalid Cert Label received!", MESSAGE_ID);
			break;
		case FIC:
			if(inputProductId.length() != property.getMaxProductSnLength())
				throw new TaskException("Invalid FIC received!", MESSAGE_ID);
			break;
		default:
		}
	}
	
	protected boolean validateCertLabel(String productId) {
		for(String token: property.getLeadingVinCharsToRemove().split(Delimiter.COMMA)) {
			if(!productId.startsWith(token)) return false;
			else if(productId.replaceFirst(token, "").length() != property.getMaxProductSnLength())
				return false;
		}
		
		return true;
				
	}

}
