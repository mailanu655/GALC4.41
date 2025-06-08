package com.honda.galc.client.datacollection.processor;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.LotControlVerificationPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * SR27623 Process product detailed information when a VIN is entered 
 * <h4>Usage and Example</h4>
 *
 * <h4>Special Notes</h4>
 * Based on FrameVinProcessor, and add EngineSN of VIN
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>YX</TD>
 * <TD>2013.07.02</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>YX</TD>
 * <TD>2013.12.12</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Modify logic to support multiple part check</TD>
 * </TR>
 * </TABLE>
 * @see
 * @ver 0.1
 * @author YX
 */

public class FrameVinDetailProcessor extends FrameVinProcessor {
	
	public FrameVinDetailProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}
	
	//Add Engine Serial No as one Installed Part of the product
	@Override
	protected boolean confirmProdIdOnServer() throws Exception{
		Logger.getLogger().debug("ProductIdProcessor:Enter invoke on server.");

		product.getPartList().clear();
		Frame aproduct = getProductFromServer();
		product.setProductSpec(aproduct.getProductSpecCode());
		product.setProductionLot(aproduct.getProductionLot());
		product.setSubId(aproduct.getSubId());
		product.setKdLotNumber(aproduct.getKdLotNumber());
					
		//must done before productIdOk
		loadLotControlRule();

		validateProduct(aproduct);
		
		doRequiredPartCheck();

		//update Lot Control Rule mask to the expected values
		LotControlVerificationPropertyBean verificationPropertyBean = PropertyService.getPropertyBean(LotControlVerificationPropertyBean.class);
		String einName = verificationPropertyBean.getPartEin();
		String missionName = verificationPropertyBean.getPartMission();
		List<LotControlRule> rules = state.getLotControlRules();
		if(rules!=null && rules.size()>0) {
			for(LotControlRule rule: rules){
				String mask="Unknown";
				if(einName!=null && einName.trim().equals(rule.getPartName().getPartName())){
					if(aproduct.getEngineSerialNo()==null || StringUtils.isEmpty(aproduct.getEngineSerialNo().trim())) {
						handleException("ProductIdProcessor:No Expected Engine Serial No for VIN [" + product.getProductId() + "].");
					} else {
						mask = aproduct.getEngineSerialNo();
					}
					for(PartSpec spec: rule.getParts()) {
						spec.setPartSerialNumberMask(mask);
					}
				} else if(missionName!=null && missionName.trim().equals(rule.getPartName().getPartName())){
					if(aproduct.getMissionSerialNo()==null || StringUtils.isEmpty(aproduct.getMissionSerialNo().trim())){
						handleException("ProductIdProcessor:No Expected Mission Serial No for VIN [" + product.getProductId() + "].");
					} else {
						mask = aproduct.getMissionSerialNo();
					}
					for(PartSpec spec: rule.getParts()) {
						spec.setPartSerialNumberMask(mask);
					}
				}
			}
		}
		
		Logger.getLogger().debug("ProductIdProcessor:Return invoke on server.");
		return true;
	}
	
	@Override
	protected Frame getProductFromServer() {
		Product aproduct = (Product) context.getDbManager().confirmProductOnServer(product.getProductId());
		if(aproduct == null) {
			handleException("Product: " + product.getProductId() + " not exist.");
		} if(!(aproduct instanceof Frame)) {
			handleException("Product: " + product.getProductId() + " not a Frame.");
		}
		
		return (Frame)aproduct;
	}
	
	//Show Error message when processing Product in an off-line mode (Since No VIN_EIN information are cached locally)
	@Override
	protected void confirmProductIdOnLocalCache() {
		//show error message when user is off line
		Logger.getLogger().info("Off Line Mode, Cannot retrieve VIN data!");
		handleException("Off Line Mode, Cannot retrieve VIN data!");
	}

}
