package com.honda.galc.service.on;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.MbpnProductType;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.notification.service.IProductOnNotification;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.work.ProductStateCheck;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>ExistingMbpnProductOnServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ExistingMbpnProductOnServiceImpl description </p>
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
 * <TD>May 10, 2017</TD>
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
 * @since May 10, 2017
 */
public class ExistingMbpnProductOnServiceImpl extends ProductOnServiceImpl implements ExistingMbpnProductOnService{
	String productSpecCode;
	
	protected void retrieveProduct(String productIdTag){
		getHeadlessDataCollectionContext().setProductName(productName);
		super.retrieveProduct(productIdTag);
		productSpecCode = product.getProductSpecCode();
	}

	protected void updatePreproductionLot() {
		//check make sure that the MBPN product is valid for the production lot
		if(!validate()){
			logAndNotify(MessageType.ERROR, (String)context.get(TagNames.ERROR_MESSAGE.name()));
			throw new TaskException("Exception: " + getProductId() + " " + (String)context.get(TagNames.ERROR_MESSAGE.name()));
		}
		super.updatePreproductionLot();
		
		if(product != null){
			product.setProductionLot(preProductionLot.getProductionLot());
			ServiceFactory.getDao(MbpnProductDao.class).save((MbpnProduct)product);
		}
	}
	
	private boolean validate() {
		PreProductionLot currentLot = getPreProductionLot();
		if(!validateMbpn(currentLot)){ 
			Logger.getLogger().info("Failed validate Mbpn.");
			return false;
		}

		if(!StringUtils.isEmpty(getPropertyBean().getMbpnProductTypes())){
			MbpnProductType mbpnPartType = ProductTypeCatalog.valueOfMbpnProductTypeByMbpn(productSpecCode);
			if(!CommonUtil.toList(getPropertyBean().getMbpnProductTypes()).contains(mbpnPartType)){
				Logger.getLogger().info("Invalid Mbpn product type");
				return false;
			}
		}
		
		try {
			return validateExistProduct(currentLot);
		} catch (Exception e) {
			updateContextError(OnErrorCode.System_Err.getCode(), "Unexpected system error.");
			return false;
		}
		
	}
	
	private boolean validateMbpn(PreProductionLot currentLot) {
		
		if(StringUtils.isEmpty(currentLot.getMbpn())){
			String mbpnAttrName = getPropertyBean().getMbpnAttributeName();
			String  mbpnAttribute = getBuildAttributesCache().findAttributeValue(currentLot.getProductSpecCode(), mbpnAttrName, "", getPropertyBean().getScheduleProductType());

			if(!StringUtils.isEmpty(mbpnAttribute) && !CommonUtil.toList(mbpnAttribute).contains(productSpecCode))
			{
				updateContextError(OnErrorCode.Invalid_Product.getCode(), " MBPN:" + productSpecCode + " failed validation for current lot.");
				getHeadlessDataCollectionContext().put(TagNames.CHECK_RESULT.name(), false);
				return false;
			}
		} else {
			if(!currentLot.getMbpn().equals(productSpecCode)){
				updateContextError(OnErrorCode.Invalid_Product);
				return false;
			}
		}
		
		return true;
	}
	

	private boolean validateExistProduct(PreProductionLot currentLot)  throws Exception{
		
		ProductStateCheck stateCheck = new ProductStateCheck(getHeadlessDataCollectionContext(), null);
		stateCheck.doWork();
		if(getHeadlessDataCollectionContext().getBoolean(TagNames.CHECK_RESULT.name(), true) == false){
			updateContextError(OnErrorCode.Invalid_Ref);
			return false;
		}
		
		return true;
	}
	
	
	protected void invokeNotification(PreProductionLot preProdLot) {
		String planCode = (StringUtils.isEmpty(getPropertyBean().getPlanCode())) ? preProdLot.getProcessLocation() : preProdLot.getPlanCode();
		ServiceFactory.getNotificationService(IProductOnNotification.class, getProcessPointId()).execute(getNotificationProductId(),preProdLot.getProductionLot(), planCode, preProdLot.getStampedCount());
	}
	
	private String getNotificationProductId() {
		return (product != null) ? product.getProductId() : "";
	}
	protected PreProductionLot getPreProductionLot() {
		if(preProductionLot != null) return preProductionLot;
		String lotNumber = getContextString(getFullTag(TagNames.LOT_NUMBER.name()));
		String productionLot = getContextString(getFullTag(TagNames.PRODUCTION_LOT.name()));
		if (lotNumber != null) {
			preProductionLot = getPreProductionLotDao().getByLotNumberAndPlanCode(lotNumber, getPropertyBean().getPlanCode());
			if (preProductionLot == null) throw new TaskException(String.format("Could not find PreProductionLot by Lot Number (%s) & Plan Code (%s)", lotNumber, getPropertyBean().getPlanCode()));
		} else if (productionLot != null){
			preProductionLot = getPreProductionLotDao().findByKey(productionLot);
			if (preProductionLot == null) throw new TaskException(String.format("Could not find PreProductionLot by production lot (%s)", getFullTag(TagNames.PRODUCTION_LOT.name())));
		} else{
			if(!StringUtils.isEmpty(getPropertyBean().getPlanCode()))
				preProductionLot = getPreProductionLotDao().findCurrentPreProductionLotByPlanCode(getPropertyBean().getPlanCode());
			else
				preProductionLot = getPreProductionLotDao().findCurrentPreProductionLot(getPropertyBean().getProcessLocation());
		}
		
		return preProductionLot;
	}
	
	
}
