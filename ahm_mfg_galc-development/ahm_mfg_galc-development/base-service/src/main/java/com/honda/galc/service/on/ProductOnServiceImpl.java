package com.honda.galc.service.on;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.ProductAlreadyProcessedException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.PreProductionLotStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.notification.service.IStampedCountChangedNotification;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;
/**
 * 
 * <h3>ProductOnServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductOnServiceImpl description </p>
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
 * <TD>Jan 24, 2013</TD>
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
 * @since Jan 24, 2013
 */
public class ProductOnServiceImpl extends ProductOnServiceBase implements ProductOnService{
	
	protected void processSingleProduct() {
		try{
			retrieveProduct(getFullTag(TagNames.PRODUCT_ID.name()));

			processTask();

			updatePreproductionLot();

			if(getPropertyBean().isAutoTracking())
				track();
			else
				updateLastPassingProcessPoint(product);

			if (getPropertyBean().isAutoBroadcasting()) {
				DataContainer dc = (DataContainer) context.get(TagNames.BROADCAST_DATA.name());
				ServiceFactory.getService(BroadcastService.class).broadcast(
						this.getProcessPointId(), this.getProduct(), dc);
			}
			
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
			contextPut(TagNames.ERROR_CODE.name(), OnErrorCode.Normal_Reply.getCode());
		}catch(TaskException te){
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().error(te, te.getMessage(), this.getClass().getSimpleName());
			logAndNotify(isNotifyClient(), MessageType.ERROR, te.getMessage());
		}catch (Exception e){
			contextPut(TagNames.ERROR_CODE.name(), OnErrorCode.System_Err.getCode());
			getLogger().error(e, OnErrorCode.System_Err.getDescription() + productId, this.getClass().getSimpleName());
			logAndNotify(isNotifyClient(), MessageType.ERROR, e.getMessage());
		}
	}

	protected void updatePreproductionLot() {
		
		//avoid duplicated count
		//For processed product, throw exception, No need to count and track.
		if(isValidatePreviousProcessPointId() && !getPropertyBean().getPreviousProcessPointId().contains(product.getLastPassingProcessPointId())){
			updateContextError(OnErrorCode.Processed_Ref.getCode(), "last passing process point id: " + product.getLastPassingProcessPointId());
			throw new TaskException("Exception: last passing process point id for " + product.getProductId() +  " is " + product.getLastPassingProcessPointId());
		}
		
		if (isProductAlreadyProcessedCheckEnabled() && hasProductHistory()) {
			updateContextError(OnErrorCode.Processed_Ref.getCode(), " has already been processed at " + getProcessPointId());
			throw new ProductAlreadyProcessedException(product.getProductId() + " has already been processed at " + getProcessPointId());
		}
		
		//get preProductionLot from device
		String preProductionLotString = (String)context.get(TagNames.PRODUCTION_LOT.name());
		
		if (StringUtils.isEmpty(preProductionLotString)) {
			preProductionLot = getPreProductionLot();	
		} else {
			preProductionLot = getPreProductionLotDao().findByKey(preProductionLotString);
		}
		
		if(preProductionLot == null){
			getLogger().warn("Preproduction Lot is empty, so not update pre-production lot.");
			return;
		}
		
		if (preProductionLot.getHoldStatus() == PreProductionLotStatus.HOLD.getId()) {
			String msg = "Product " + productId + " can not be processed because Lot " + preProductionLot.getProductionLot() + " is On Hold.";
			updateContextError(OnErrorCode.Invalid_Ref.getCode(), "product is On Hold.");
			throw new TaskException("Exception: " + msg);
		}
		
		if(preProductionLot.getSendStatus() == PreProductionLotSendStatus.INPROGRESS){
			updateStampCount(preProductionLot);
			if(isLotCompleted(preProductionLot)) completeLot(preProductionLot);
		} else if(preProductionLot.getSendStatus() == PreProductionLotSendStatus.WAITING || preProductionLot.getSendStatus() == PreProductionLotSendStatus.SENT) {
			updateStampCount(preProductionLot);
			if(isLotCompleted(preProductionLot)) {	//For lot size == 1
				completeLot(preProductionLot);
			} else {
				startLot(preProductionLot);
			}
			
			if (isCompleteParentLots())
				CompleteParentLots();
		} else {
			//still update stamp count
			updateStampCount(preProductionLot);
		}
		
		invokeNotification(preProductionLot);
		
	}

	private boolean isCompleteParentLots() {
		return getPropertyBean().isCompleteParentLots();
	}
	
	private void CompleteParentLots() {
		//BA-JG, if there is any open parent lot of this lot, close them
		PreProductionLot previousLot = getPreProductionLotDao().findParent(preProductionLot.getProductionLot());
		while(previousLot!=null && previousLot.getSendStatus() != PreProductionLotSendStatus.DONE){
			completeLot(previousLot);
			getLogger().warn("WARN: close preproduction lot:", previousLot.getProductionLot(), 
					" for skipped to ", preProductionLot.getProductionLot());
			
			previousLot = getPreProductionLotDao().findParent(previousLot.getProductionLot());
		}
	}
	
	protected boolean isValidatePreviousProcessPointId() {
		return getPropertyBean().isValidatePreviousProcessPointId();
	}

	protected boolean isProductAlreadyProcessedCheckEnabled() {
		return getPropertyBean().isProductAlreadyProcessedCheckEnabled();
	}
	
	protected boolean hasProductHistory() {
		return ProductTypeUtil.getProductHistoryDao(getProductType()).hasProductHistory(productId, processPointId);
	}
	
	protected void startLot(PreProductionLot lot) {
		if(getLotSize(lot) > 1)
			getPreProductionLotDao().updateSendStatus(lot.getProductionLot(), PreProductionLotSendStatus.INPROGRESS.getId());
		else if(getLotSize(lot) == 1)
			getPreProductionLotDao().updateSendStatus(lot.getProductionLot(), PreProductionLotSendStatus.DONE.getId());
	}


	protected void completeLot(PreProductionLot lot) {
		getPreProductionLotDao().updateSendStatus(lot.getProductionLot(), PreProductionLotSendStatus.DONE.getId());
		
	}
	
	protected void updateStampCount(PreProductionLot lot){
		if(lot.getSendStatus() == PreProductionLotSendStatus.DONE)
			getLogger().warn("Preprodution Lot:", lot.getProductionLot(), " is already completed.");
		
		if(lot.getStampedCount() == getLotSize(lot)){
			getLogger().warn("Preprodution Lot:", lot.getProductionLot(), " is full. skip counting this product.");
			return;
		}
		
		increaseStampCount(lot);
		getPreProductionLotDao().updateStampedCount(lot.getProductionLot(), lot.getStampedCount());
		ServiceFactory.getNotificationService(IStampedCountChangedNotification.class, processPointId).stampedCountChanged(lot.getProductionLot(), lot.getStampedCount());
		getLogger().info(IStampedCountChangedNotification.class.getSimpleName() + " initiated for process point: " + processPointId + " (production lot: " + lot.getProductionLot() + ", stamped count: " + lot.getStampedCount() + ")");
	}

	protected void increaseStampCount(PreProductionLot lot) {
		lot.countOne();
	}


	protected boolean isLotCompleted(PreProductionLot preProductionLot) {
		return preProductionLot.getStampedCount() >= getLotSize(preProductionLot); 
	}

	private int getLotSize(PreProductionLot preProductionLot) {

		if(getProductType() == ProductType.KNUCKLE) {
			return preProductionLot.getLotSize()*2;
		} else if(ProductTypeUtil.isSubProduct(getProductType())){
			String[] subIds = getSubIds(preProductionLot);
			return preProductionLot.getLotSize()*(subIds.length == 0 ? 1 : subIds.length);

		} else if(ProductTypeUtil.isMbpnProduct(getProductType())){
			return StringUtils.isEmpty(getPropertyBean().getMbpnProductTypes()) ? preProductionLot.getLotSize() :
				preProductionLot.getLotSize() * CommonUtil.toList(getPropertyBean().getMbpnProductTypes()).size();
		} else {
			return preProductionLot.getLotSize();
		}
	}

	@Override
	protected void init(Device device) {
		super.init(device);
		context.putAll(device.getInputMap());
	}

	protected void retrieveProduct(String productIdTag) {
		getLogger().debug("retrieve product on tag:", productIdTag);
		productId = (String)context.get(productIdTag);
		context.put(TagNames.PRODUCT_ID.name(), productId);
		
		if(StringUtils.isEmpty(productId)){
			updateContextError(OnErrorCode.Invalid_Ref.getCode(), "Product Id from PLC is empty.");
			throw new TaskException("Exception: invalid product Id.");
		}
		
		getLogger().info("process product:", productId);
		product = getProductDao().findByKey(productId);
		
		if(product == null){
			updateContextError(OnErrorCode.Invalid_Ref.getCode(), "WARN:" + getProductId() + " product not exist in database:");
			throw new TaskException("Exception: invalid product Id - not exist in database.");
		}
		getHeadlessDataCollectionContext().setProduct(product);
	}

	public BaseProduct getProduct(){
		return product;
	}
	
	protected boolean isNotifyClient(){
		return getPropertyBean().isNotifyClient();
	}
	
}
