package com.honda.galc.service.datacollection.work;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.MbpnProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.HeadlessDCInfoCode;
import com.honda.galc.entity.enumtype.HoldStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.MbpnProductType;
import com.honda.galc.entity.product.ProductIdNumberDef;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.ProductDataCollectorBase;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.MbpnUtil;
/**
 * 
 * <h3>PreProcess</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PreProcess description </p>
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
public class PreProcess extends CollectorWork{
	
	BaseProduct product;
	BaseProductSpec productSpec;
	public PreProcess(HeadlessDataCollectionContext context, ProductDataCollectorBase collector) {
		super(context, collector);
	}
	
	@Override
	void doWork()throws Exception {	
		traceProductIdMsg();

		if(context.getDeviceHelper().hasUpdate()){
			context.updateContext(context.getDevice().getDeviceDataFormats());
		}
		

		if (context.containsKey(TagNames.EXCEPTION.name())) context.remove(TagNames.EXCEPTION.name());
		
		associateIdCheck();
		
		prepareInfoCode(HeadlessDCInfoCode.PRODUCT_OK);
		
		checkProductId(context.getProductId());
		
		if(context.getProductType() == ProductType.MBPN_PART)
			checkMbpnProductType();
		
		if(!getProperty().isValidateProductId()) return;
		
		//load lot control rules
		if(context.getProductSpec()!= null && !context.getProductSpec().getProductSpecCode().equals(context.getProductSpecCode())){
			collector.loadLotControlRules();
			context.setProductSpecCode(context.getProductSpec().getProductSpecCode());
		}
		
		if(context.getProductSpecCode() != null){
			context.put(TagNames.PRODUCT_SPEC_CODE.name(), context.getProductSpecCode());
			
			if(getProperty().getBuildAttributes() != null && getProperty().getBuildAttributes().length > 0)
				retrieveBuildAttribute(getProperty().getBuildAttributes());
		}
		//check required part if needed
		if(getProperty().isCheckRequiredPart()){
			context.put(TagNames.REQUIRED_PART.name(), checkRequiredPart());
		}
		
		getLogger().info(context.getProductInfoString());
		
	}
	
	private void checkMbpnProductType() {
	
		Mbpn mbpn = (Mbpn)context.getProductSpec();
		if(!context.getProperty().getProductType().equals(mbpn.getMbpnProductType().getProductType())){
			context.put(TagNames.ERROR_MESSAGE.name(), "Invalid Mbpn product type.");
			throw new TaskException("Invalid Product Id - product type:" + mbpn.getMbpnProductType().getProductType() + "is not matching to the product type:" + context.getProperty().getProductType());
		}
		
		List<MbpnProductType> mbpnProductTypes = ServiceFactory.getDao(MbpnProductTypeDao.class).findAllByProductType(context.getProperty().getProductType());
		if(MbpnUtil.checkProductId(product.getProductId(), mbpnProductTypes))
			return;
		
        context.put(TagNames.ERROR_MESSAGE.name(), "Invalid Mbpn product Product Id.");
        throw new TaskException("Invalid Product Id: " + product.getProductId() + ", not match to Product Number Defs");
		
		
	}
	

	private void associateIdCheck() {  //If associate is not in payload, do not accept data.
		String associateId = context.getAssociateNo().trim();
		if (getProperty().isInlineRepair() && getProperty().isRequireAssociateIdForInlineRepair() && (associateId == null || associateId.equals(""))) {
			context.put(TagNames.ERROR_MESSAGE.name(), "Assoicate ID is required to complete repair.");
			throw new TaskException("Associate ID required to complete repair.");
		}
		
		if (getProperty().isRequireAssociateId() && (associateId == null || associateId.equals(""))) {
			context.put(TagNames.ERROR_MESSAGE.name(), "Assoicate ID is required.");
			throw new TaskException("Associate ID required to complete data collection.");
		}
		
		
	}

	public void retrieveBuildAttribute(String[] buildAttributes) {
		BuildAttributeCache buildAttributeCache = new BuildAttributeCache();
		for(String attr : buildAttributes){
			BuildAttribute attribute = buildAttributeCache.findById((String)context.get(TagNames.PRODUCT_SPEC_CODE.name()), attr, product.getSubId());
			if(attribute != null)
				context.put(attribute.getTag(), attribute.getAttributeValue());
		}
	}

	
	private void checkProductId(String productId) {
		if(context.getProductType() == null)
			getLogger().error("CheckProductId Product type:" + context.getProductType().toString(), this.getClass().getSimpleName());
		
		if(getProperty().isValidateProductId()) {
			try {
				validateProductId(productId);
				context.put(TagNames.VALID_PRODUCT_ID.name(), true);
				
				getLogger().info("The product is valid.");
				
				context.setCurrentProductId(product.getProductId());
			} catch(TaskException te){
				context.put(TagNames.VALID_PRODUCT_ID.name(), false);
				context.put(TagNames.CHECK_RESULT.name(), false);
				if(StringUtils.isBlank(context.get(TagNames.INFO_CODE.name()).toString())) {
                    prepareInfoCode(HeadlessDCInfoCode.INVALID_PRODUCT);
				}

				throw te;
			} catch(Exception e){
				context.put(TagNames.VALID_PRODUCT_ID.name(), false);
				context.put(TagNames.CHECK_RESULT.name(), false);
				prepareInfoCode(HeadlessDCInfoCode.INVALID_PRODUCT);
				throw new TaskException("Exception on validate productId" + productId, e);
			}
		} else {
			try {
				product = ServiceUtil.getProductFromDataBase(context.getProductType().name(), productId);
			} catch (Exception e) {	
			}
			
			if(product != null) context.setProduct(product);
		}	
		
		
	}

	private boolean checkRequiredPart() {
		return collector.checkRequiredPart();
	}


	private void validateProductId(String productId) {
		if(getProperty().isInlineRepair() && getProperty().isPdaRecipeDownload()) 
			product = ServiceUtil.validateProductId(context.getProductType().name(), productId, context, context.getProductName());
		else product = ServiceUtil.validateProductId(getProperty().getProductType(), productId, context, context.getProductName());
		context.setProduct(product);
		if(product == null) {
			prepareInfoCode(HeadlessDCInfoCode.NOT_EXIST);
		}
		checkContext(context);
		
		if(product.getDefectStatus() == DefectStatus.SCRAP && !getProperty().isExceptionOutValidProduct()) {
			prepareInfoCode(HeadlessDCInfoCode.SCRAP);
			throw new TaskException("Product:" + product.getProductId() + " is scraped.");
		} else if(product.getDefectStatus()== DefectStatus.PREHEAT_SCRAP && !getProperty().isExceptionOutValidProduct()) {
			prepareInfoCode(HeadlessDCInfoCode.SCRAP);
			throw new TaskException("Product:" + product.getProductId() + " is preheat scraped.");
		}
		
		if(product.getHoldStatus() == HoldStatus.ON_HOLD.getId()){
			prepareInfoCode(HeadlessDCInfoCode.ONHOLD);
			if(getProperty().isProcessOnHoldProduct())
				getLogger().warn("Product:" + product.getProductId() + " is On hold!"); 
			else
				throw new TaskException("Product:" + product.getProductId() + " is on hold!");
		}
		
		if(getProperty().isCheckOutstandingDefect() && product.isOutstandingStatus()) {
			prepareInfoCode(HeadlessDCInfoCode.OUTSTANDING);
			throw new TaskException("Product:" + product.getProductId() + " has outstanding defects!");
		}
		
		try {
			productSpec = context.getProductTypeUtil().getProductSpecDao().findByProductSpecCode(getProductSpecCodeKey(),getProperty().getProductType());
		} catch (Exception e) {
			productSpec = null;
		}
		
		context.setProductSpec(productSpec);
		
		if(productSpec == null && getProperty().isLotControl())
			throw new TaskException("Product Spec:" + getProductSpecCodeKey() + " does not exist.");
		
		context.put(TagNames.HOME_PRODUCT.name(), context.isHomeProduct(productId));
		
	}

	private String getProductSpecCodeKey() {
		if(context.getProductType() == ProductType.MBPN)
			return ((MbpnProduct)product).getCurrentProductSpecCode();
		else 
			return product.getProductSpecCode();
	}
	

	private void traceProductIdMsg() {
		StringBuilder msg = new StringBuilder(this.getClass().getSimpleName() + ":"); 
		if(!StringUtils.isEmpty(context.getDeviceHelper().getProductId()))
			msg.append(" collect data for product:" + context.getDeviceHelper().getProductId());
		else
			msg.append(" product Id is empty.");
	
		getLogger().info(msg.toString());
	}
}
