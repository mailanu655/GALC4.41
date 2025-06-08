package com.honda.galc.client.datacollection.strategy;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.processor.PartSerialNumberProcessor;
import com.honda.galc.client.datacollection.processor.ProductIdProcessor;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.util.PartCheckType;
import com.honda.galc.util.PartCheckUtil;
/**
 * 
 * <h3>ProductIdValidation</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductIdValidation description </p>
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
 * May 14, 2010
 *
 */
public class AntiTheftIdValidation extends PartSerialNumberProcessor{
	private List<String> partnumberList = new ArrayList<String>();
	private List<String> productIdList = new ArrayList<String>();

	public AntiTheftIdValidation(ClientContext context) {
		super(context);
	}

	@Override
	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {

		installedPart.setPartSerialNumber(partnumber.getPartSn());
		List<PartSpec> partList = getController().getState().getCurrentLotControlRulePartList();
		
		if(validate(getController().getCurrentLotControlRule(), getController().getState().getProductId(), installedPart)){
			installedPart.setPartId(partList.get(0).getId().getPartId());
			installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
			installedPart.setValidPartSerialNumber(true);
			}
		
		return true;
	}

	@Override
	protected void handleException(String info) {
		throw new TaskException(info, ProductIdProcessor.PROCESS_PRODUCT);
	}
	
	public boolean validate(LotControlRule lotControlRule, String productId, ProductBuildResult result){
		String partSerialNumber = result.getPartSerialNumber();
		
		String[] partCheckTypes = new String[]{PartCheckType.PART_NUMBER_NULL_CHECK.name(),PartCheckType.PRODUCT_ID_NULL_CHECK.name(),
				PartCheckType.PART_NUMBER_NOT_CONTAINS_PRODUCT_ID_CHECK.name(),PartCheckType.DUPLICATE_PART_FOR_ANTITHEFT_CHECK.name()};
	
		PartCheckUtil.checkWithExceptionalHandling(partSerialNumber, productId, partCheckTypes);
		
		if (null != productIdList && productIdList.size() > 0) { 
			if (!productIdList.contains(productId)){
					productIdList.clear();
					partnumberList.clear();
					productIdList.add(productId);
			} 
		} else {
			productIdList.add(productId);
		}
		
		if (null != partnumberList && partnumberList.size() > 0) { 
			if (partnumberList.contains(partSerialNumber))
				handleException("Duplicate Part");
			else {
				partnumberList.add(partSerialNumber);
			}
		} else {
			partnumberList.add(partSerialNumber);
		}
		
	return true;
	}
}

