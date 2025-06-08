package com.honda.galc.service.on;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.TagNames;
import com.honda.galc.data.ProductNumberDef.TokenType;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingId;
import com.honda.galc.notification.service.IProductOnNotification;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.util.CommonUtil;
/**
 * 
 * <h3>MbpnSubProductOnServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductDataCollectorBase description </p>
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
 * <TD>May 16, 2014</TD>
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
 * @since May 16, 2014
 */
public class MbpnSubProductOnServiceImpl extends ProductOnServiceBase implements MbpnSubProductOnService {
	private int sequenceIntv = 0; 
	
	PreProductionLot currentPreProductionLot;

	private String planCode;
	
	@Override
	protected void init(Device device) {
		super.init(device);
		context.putAll(device.getInputMap());
		currentPreProductionLot = null;
	}
	
	protected void processSingleProduct(){
		boolean checkResult;
		try {
			productId = (String)context.get(getFullTag(TagNames.PRODUCT_ID.name()));
			productId = StringUtils.trim(productId);
			
			checkResult = validateProductId();
			if(checkResult){
				SubProduct exist = ServiceFactory.getDao(SubProductDao.class).findByKey(productId);
				
				if(exist != null){//product id is uniq in database; once passed on can't used to another lot
					logAndNotify(MessageType.WARN,"Product:",productId, " already processed.");
					return;
				}
				
				if(getPropertyBean().isCreateShippingSchedule() &&
						getCurrentPreProductionLot().getSendStatus() == PreProductionLotSendStatus.WAITING)
					createShippingDetails(getCurrentPreProductionLot());
				
				saveSubProduct(productId, getCurrentPreProductionLot());
				updatePreProductionLot(getCurrentPreProductionLot());
				
				if(getPropertyBean().isAutoTracking()) track();
				contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
			} else {
				String msg = "Invalid product id:" + productId;
				getLogger().warn(msg);
				logAndNotify(MessageType.ERROR, "Invalid product id:", productId);
				
			}
		} catch (Exception e) {
			getLogger().error(e, " Exception to process Product On.");
			logAndNotify(MessageType.ERROR, "Exception to process Product:", productId);
			checkResult = false;
		
		}
		
		if(!checkResult)
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
	}
	

	private void createShippingDetails(PreProductionLot preproductionLot) throws Exception {
		try {
			//make sure the shipping details is not created yet
			SubProductShippingDao dao = ServiceFactory.getDao(SubProductShippingDao.class);
			SubProductShipping exist = dao.findByKey(new SubProductShippingId(
					preproductionLot.getKdLotNumber(), preproductionLot.getProductionLot()));
			if (exist != null) {
				getLogger().warn("Shipping details for KD:",preproductionLot.getKdLotNumber(), " production lot:",
						preproductionLot.getProcessLocation()," already exist, skip to create shipping details.");
				return;
			}
			createSubProductShippingSchedule(preproductionLot);
		} catch (Exception e) {
			logAndNotify(MessageType.ERROR, "Exception to create Shipping Details:", e.toString());
		}
	}


	private void createSubProductShippingSchedule(PreProductionLot preproductionLot) throws Exception {
		
		SubProductShippingDao dao = getDao(SubProductShippingDao.class);
		dao.createSubProductShipping(preproductionLot, productType, getSequenceIntv(), getSubIds(preproductionLot));
	
	}

	private boolean validateProductId() {
		
		if(!StringUtils.isEmpty(getPropertyBean().getProcessLocation()))
			return legacyValidate();
		else
			return validatesSubProductId();
	}

	private boolean validatesSubProductId() {
		if(!ServiceUtil.validateProductIdNumber(getPropertyBean().getProductType(), productId, context, productName)){
		    getLogger().warn("Invalid product id format:", productId);
			return false;
		}
		
		// validate the it's the right bumper type for the KD Lot
		if(!validatePartMark())
			return false;
		
		//validate the current type is not full for the KD Lot, If this type is full, return error
		validatePartCodeForLot();
		
		return true;
	}

	private void validatePartCodeForLot() {
		// confirmed with Paint Department - as bellow
		//  For scenario when a lot of 30 for example: 30F and 30R
		//  When left is already 30 and right is less then 30 for example 25, and get a left again,
		//  then the system will close the current lot and put the left into the next lot
		
		String partCode = getSubId();
		int stamped = ServiceFactory.getDao(SubProductDao.class).count(getCurrentPreProductionLot().getProductionLot(), partCode);
		if(stamped >= getCurrentPreProductionLot().getLotSize()){
			
			//close the lot
			getPreProductionLotDao().updateSendStatus(getCurrentPreProductionLot().getProductionLot(), PreProductionLotSendStatus.DONE.getId());
			currentPreProductionLot = null;
			
			getLogger().warn("The current Production Lot:", getCurrentPreProductionLot().getProductionLot(), " Sub Id:",
					partCode, " stamped:" + stamped, " close the lot abnormally.");
		}
	}

	private boolean legacyValidate() {
		//1. validate last character, but don't know the length
		//2. validate Injection Machine. 140821M201:17:20F - M2 is existing in Gal475tbx 
		//Confirmed no need to do validation 2.
		validatePartCodeForLot();
		
		return checkSubId(getSubId());
		
		
	}

	private boolean validatePartMark() {
		
		String partCode = getSubId();
		if(!checkSubId(partCode)) return false;
		
		//validate the MBPN is the right one for the production lot
		String molds = getBuildAttributesCache().findAttributeValue(getCurrentPreProductionLot().getProductSpecCode(), "", BuildAttributeTag.PLASTIC_MOLD, getProductType());
		
		if(StringUtils.isEmpty(molds)){
			getLogger().info("No Mold Code is not defined - validation ok.");
			return true;
		}
		
		//if product number has MOULD then check it 
		for(ProductNumberDef def : getProductNumberDefs()){
			if(def.isNumberValid(productId)){
				String mould = def.getToken(TokenType.MOULD.name(), productId);
				if(!StringUtils.isEmpty(mould))
					return molds.contains(mould);
			}
		}
		
		return true;
	}

	private boolean checkSubId(String partCode) {
		String subIds = getBuildAttributesCache().findAttributeValue(getCurrentPreProductionLot().getProductSpecCode(), BuildAttributeTag.SUB_IDS, "");
		List<String> subIdList = CommonUtil.toList(subIds);
		
		return subIdList.contains(partCode);
	}

	private void saveSubProduct(String productId, PreProductionLot preproductionLot) {

		SubProduct subProduct = ProductTypeUtil.createSubProduct(getPropertyBean().getProductType(), productId, getSubId());
		
		if(preproductionLot != null) {
			subProduct.setProductionLot(preproductionLot.getProductionLot());
			subProduct.setKdLotNumber(preproductionLot.getKdLot());
			subProduct.setProductSpecCode(preproductionLot.getProductSpecCode());
		}
		
		ServiceFactory.getDao(SubProductDao.class).save(subProduct);
		product = subProduct;
	}
	

	private String getSubId() {
		List<ProductNumberDef> prodNumbDefs = getProductNumberDefs();
		if(prodNumbDefs != null){
			for(ProductNumberDef def : prodNumbDefs){
				if(def.isNumberValid(productId))
					if(def.hasToken(ProductNumberDef.TokenType.SUB_ID))
						return StringUtils.trim(def.getSubId(productId).replaceAll("0", ""));
					else
						return productId.substring(productId.length() -1);
			}
		}
		
		return productId.substring(productId.length() -1);
	}

	private PreProductionLot updatePreProductionLot(PreProductionLot preProductionLot) throws Exception {
		
		preProductionLot.countOne();
		getPreProductionLotDao().updateStampedCount(preProductionLot.getProductionLot(), preProductionLot.getStampedCount());
		
		if(preProductionLot.getSendStatus() == PreProductionLotSendStatus.INPROGRESS){
			if(isLotCompleted(preProductionLot)) 
				getPreProductionLotDao().updateSendStatus(preProductionLot.getProductionLot(), PreProductionLotSendStatus.DONE.getId());
		} else if(preProductionLot.getSendStatus() == PreProductionLotSendStatus.WAITING){
			getPreProductionLotDao().updateSendStatus(preProductionLot.getProductionLot(), PreProductionLotSendStatus.INPROGRESS.getId());
		} 
		
		invokeNotification(preProductionLot);
		
		return preProductionLot;
	}
	
	protected void invokeNotification(PreProductionLot preProdLot) {
		ServiceFactory.getNotificationService(IProductOnNotification.class, getProcessPointId()).execute(product.getProductId(),preProdLot.getProductionLot(), preProdLot.getPlanCode(), preProdLot.getStampedCount());
	}
	
	private boolean isLotCompleted(PreProductionLot preProductionLot) throws Exception {
		
		String[] subIds = getSubIds(preProductionLot);
		return preProductionLot.getLotSize()*(subIds.length == 0 ? 1 : subIds.length) <= preProductionLot.getStampedCount();
	}

	protected BuildAttributeCache getBuildAttributesCache() {
		if(buildAttributeCache == null){
			buildAttributeCache = new BuildAttributeCache();
			buildAttributeCache.loadAttribute(BuildAttributeTag.SUB_IDS);
			buildAttributeCache.loadAttribute(BuildAttributeTag.PLASTIC_MOLD);
		}
		return buildAttributeCache;
	}


	private PreProductionLot getCurrentPreProductionLot() {
		if(currentPreProductionLot == null)
			currentPreProductionLot = ServiceFactory.getDao(PreProductionLotDao.class).findCurrentPreProductionLotByPlanCode(getPlanCode());
		return currentPreProductionLot;
		
	}

	private String getPlanCode() {
		if(planCode == null)
			planCode = getPropertyBean().getPlanCode();
		
		return planCode;
	}

	public int getSequenceIntv() {
		if(sequenceIntv == 0)
			sequenceIntv =getPropertyBean().getSequenceInterval();
		
		return sequenceIntv;
	}

}
