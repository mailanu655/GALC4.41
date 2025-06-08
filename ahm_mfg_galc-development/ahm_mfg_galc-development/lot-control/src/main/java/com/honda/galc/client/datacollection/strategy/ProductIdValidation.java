package com.honda.galc.client.datacollection.strategy;

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
public class ProductIdValidation extends PartSerialNumberProcessor{

	public ProductIdValidation(ClientContext context) {
		super(context);
	}

	@Override
	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		List<PartSpec> partList = getController().getState().getCurrentLotControlRulePartList();
		
		if (partnumber == null )
			handleException("Part serial number is null.");
		
		installedPart.setPartSerialNumber(partnumber.getPartSn());
		
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
		if(productId == null){
			handleException("Poduct Id is null.");
		}
		else if(!partSerialNumber.equals(productId)){
			handleException("part sn:" + partSerialNumber + " mismatch with:" + productId);
		}
					
		return true;
		
	}
}
