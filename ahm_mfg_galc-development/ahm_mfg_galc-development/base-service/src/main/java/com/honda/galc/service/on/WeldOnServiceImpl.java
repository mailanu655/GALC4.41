package com.honda.galc.service.on;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.FloorStampInfoCodes;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.ProductStampingSendStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.property.RecipePropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.recipe.RecipeProductSequenceHelper;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>WeldOnServiceImpl</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Oct 22, 2013
 */
// TODO
// This class executes similar logic as is executed by :
// FloorStampResultVerificationProcessor.updateStatus() method,
// Those classes should be reviewed/refactored to remove redundancy.
// When refactoring, commonizing with ProductOnService could also be considered
// Though there are some differences with ProductOnService logic
// - ProductOnService does not use/maintain gal216tbx and gal135tbx
// - it does not use table gal216tbx to calculate production lot running count
// - it performs backfilling for production lot send status
// 
public class WeldOnServiceImpl extends ProductOnServiceBase implements WeldOnService {

	private static final String DATE_FORMAT = "MMddyyHHmmss";
	private TrackingService trackingService;
	private BroadcastService broadcastService;
	private ProductStampingSequenceDao productStampingSequenceDao;
	private ExpectedProductDao expectedProductDao;
	protected RecipeProductSequenceHelper helper;
	private RecipePropertyBean recipePropertyBean;
	
	protected void init(Device device) {
		super.init(device);
		context.putAll(device.getInputMap());
	}

	public void processProduct(BaseProduct product, String processPointId) {
		processProduct(product, processPointId, null);
	}
	
	public void processProduct(BaseProduct product, String processPointId, String componentId) {
		setProcessPointId(processPointId);
		if(getPropertyBean().isAutoTracking()) {
			getTrackingService().track(product, processPointId);
		}
		updateStatusAndPublishChanges(product, componentId);
	}
	
	public void processProduct(BaseProduct product, ProductHistory productHistory) {
		processProduct(product, productHistory, null);
	}

	public void processProduct(BaseProduct product, ProductHistory productHistory, String componentId) {
		setProcessPointId(productHistory.getProcessPointId());
		if(getPropertyBean().isAutoTracking()) {
			getTrackingService().track(product.getProductType(), productHistory);
		}
				
		updateStatusAndPublishChanges(product, componentId);
	}

	private void updateStatusAndPublishChanges(BaseProduct product, String componentId) {
		updateProductSendStatus(product);
		updateStampedCount(product);
		updateProductionLotSendStatus(product);
		updateNextExpected(product, processPointId, componentId);

		publishWeldOnChanges(product, processPointId);
	}

	public void publishWeldOnChanges(BaseProduct product, String processPointId) {
		PreProductionLot preProdLot = getPreProductionLotDao().findByKey(product.getProductionLot());
		invokeNotification(preProdLot);
		getBroadcastService().broadcast(processPointId, product.getProductId());
	}

	protected String retrieveProductId() {
		String productIdTag = getFullTag(TagNames.PRODUCT_ID.name());
		getLogger().debug("retrieve productId on tag:", productIdTag);
		String productId = (String) context.get(productIdTag);
		if (StringUtils.isBlank(productId)) {
			throw new TaskException("Exception: invalid productId.");
		}
		return StringUtils.trim(productId);
	}

	protected BaseProduct findProduct(String productId) {
		BaseProduct product = getProductDao().findByKey(productId);
		if (product == null) {
			updateStampingInfoCode(FloorStampInfoCodes.REQUEST_VIN_INVALID, productId);
			getLogger().warn("WARN: product does not exist in database:", productId);
			throw new TaskException("Exception: invalid productId - not exist in database.");
		}
		return product;
	}

	protected void updateNextExpected(BaseProduct product, String processPointId) {
		updateNextExpected(product, processPointId, null);
	}

	protected void updateNextExpected(BaseProduct product, String processPointId, String componentId) {
		ExpectedProduct expectedProduct = new ExpectedProduct();
		String component = null;
		if (!StringUtils.isEmpty(componentId)) {
			component = (componentId + "_VIN_STAMP_DASH");
			List<?> properties = PropertyService.getComponentProperty(component);
			if (properties == null || properties.isEmpty()) component = null;
		}
		String nextProductId = getNextExpectedProductId(component, product.getProductId());
		expectedProduct.setProcessPointId(processPointId);
		expectedProduct.setProductId(nextProductId == null ? "" : nextProductId);
		getExpectedProductDao().save(expectedProduct);
		getLogger().info("Successfully updated next expected productId to : " + expectedProduct.getProductId());
	}

	protected void updateProductionLotSendStatus(BaseProduct product) {
		PreProductionLot preProductionLot = getPreProductionLotDao().findByKey(product.getProductionLot());
		getLogger().debug("Stamped count: " + preProductionLot.getStampedCount());
		getLogger().debug("Production lot size: " + preProductionLot.getLotSize());
		if (preProductionLot.getStampedCount() == preProductionLot.getLotSize()) {
			getPreProductionLotDao().updateSendStatus(product.getProductionLot(), ProductStampingSendStatus.STAMPED.getId());
			getLogger().info("Successfully updated send status to: " + ProductStampingSendStatus.STAMPED.getId() + " for production lot: " + product.getProductionLot());
		} else {
			getLogger().info("Did not update send status for production lot: " + product.getProductionLot() + " because all vins are not stamped");
		}
	}

	protected void updateStampedCount(BaseProduct product) {
		getPreProductionLotDao().updateStampedCount(product.getProductionLot());
		getLogger().info("Successfully updated stamped count for production lot: " + product.getProductionLot());
	}

	protected void updateProductSendStatus(BaseProduct product) {
		ProductStampingSequence prodStampSeq = getProductStampingSequenceDao().findById(product.getProductionLot(), product.getProductId());
		prodStampSeq.setSendStatus(ProductStampingSendStatus.STAMPED.getId());
		getProductStampingSequenceDao().update(prodStampSeq);
		getLogger().info("Successfully updated send status to: " + ProductStampingSendStatus.STAMPED.getId() + " for productId: " + product.getProductId());
	}
	
	protected void processSingleProduct(){
		String productIdTag = getFullTag(TagNames.PRODUCT_ID.name());
		productId = (String) context.get(productIdTag);
		context.put(TagNames.ERROR_CODE.name(), null);
		
		if (StringUtils.isEmpty(productId)) {
			updateStampingInfoCode(FloorStampInfoCodes.RESULT_VIN_RFID_NG, "");
			return;
		}
		
		if(isLayoutBody()) {
			updateLayoutBodayInDatabase();
		} else {
			if(validateResultVin()) {
				ProductHistory productHistory = ProductTypeUtil.createProductHistory(product.getProductId(), getProcessPointId(), getProductType());

				if (productHistory != null) {
 		 	        String stampedTimeString = (String) context.get(DataContainerTag.STAMPED_TIME);
 		 	        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
 		 	       	Timestamp stampedTime;
					try {
						stampedTime = new Timestamp(formatter.parse(stampedTimeString).getTime());
					} catch (Exception e) {
						getLogger().warn("Cannot parse timestamp from device data:", stampedTimeString);
						stampedTime = null;
					}
					productHistory.setActualTimestamp(stampedTime);
				}
				processProduct(product, productHistory);
			}
		}
	};

	private boolean validateResultVin() {
		product = findProduct(productId);
		ProductStampingSequence stampingSeq = getProductStampingSequenceDao().findById(product.getProductionLot(), product.getProductId());
		if(product == null || StringUtils.isEmpty(product.getProductId())) {
			updateStampingInfoCode(FloorStampInfoCodes.REQUEST_VIN_INVALID, productId);
			return false;
		} else {
			ExpectedProduct expectedProduct = getExpectedProductDao().findForProcessPoint(getProcessPointId());
			if(expectedProduct == null || StringUtils.isEmpty(expectedProduct.getProductId())) {
				getLogger().warn("Next expected result VIN:" + ((expectedProduct == null)? "null" : expectedProduct.getProductId()));;
				expectedProduct = new ExpectedProduct(product.getProductId(), getProcessPointId());
			} else if(!expectedProduct.getProductId().equals(product.getProductId())){
				updateStampingInfoCode(FloorStampInfoCodes.RESULT_VIN_SKIPPED, expectedProduct.getProductId());
			}
			
			switch(ProductStampingSendStatus.getType(stampingSeq.getSendStatus())) {
			case STAMPED:
				updateStampingInfoCode(FloorStampInfoCodes.RESULT_VIN_ALREADY_PROCESSED, productId);
				return false;
			case WAITING:
			case SENT:
			case LAYOUT_BODY:
				if(context.get(TagNames.ERROR_CODE.name()) == null)
					updateStampingInfoCode(FloorStampInfoCodes.RESULT_VIN_OK, productId);
				break;
			default: 
				updateStampingInfoCode(FloorStampInfoCodes.RESULT_VIN_INVALID, productId);
				return false;
			}
		    
		}
		return true;
	}

	private void updateLayoutBodayInDatabase() {
		try {
			product = findProduct(productId);
		} catch (Exception e) {
			updateStampingInfoCode(FloorStampInfoCodes.REQUEST_VIN_INVALID, productId);
			return;
		}
		
		if(product == null)
		{
			updateStampingInfoCode(FloorStampInfoCodes.REQUEST_VIN_INVALID, productId);
			return;
		}
		ProductStampingSequence entity = getProductStampingSequenceDao().findById(product.getProductionLot(), product.getProductId());
		entity.setSendStatus(ProductStampingSendStatus.LAYOUT_BODY.getId());
		getProductStampingSequenceDao().update(entity);
		updateStampingInfoCode(FloorStampInfoCodes.RESULT_VIN_OK, productId);
		
	}

	private boolean isLayoutBody() {
		Object object = context.get(getFullTag(TagNames.LAYOUT_BODY.name()));
		if (object == null) return false;
		else {
			return CommonUtil.convertToBoolean(object.toString());
		}
	}

	private String getNextExpectedProductId(String component, String productId) {
		if(getPropertyBean().isWeldOnNextExpected())
			return getPreProductionLotDao().findNextWeldOnProductId(ProductStampingSendStatus.SENT.getId(), component);
		else { 
			ProductStampingSequence nextSeq = getHelper().nextProductForVinStamping(productId);
			return nextSeq == null? "" : nextSeq.getProductId();
		}

	}

	public RecipeProductSequenceHelper getHelper() {
			if(helper == null)
				helper = RecipeProductSequenceHelper.getHelper(getProcessPointId(), getProductType(), getRecipePropertyBean(), getLogger());
			
			return helper;
		}
	private RecipePropertyBean getRecipePropertyBean() {
			if(recipePropertyBean == null)
				recipePropertyBean = PropertyService.getPropertyBean(RecipePropertyBean.class, getProcessPointId());
			return recipePropertyBean;
	}
	// get/set === //
	protected TrackingService getTrackingService() {
		if (trackingService == null) {
			trackingService = ServiceFactory.getService(TrackingService.class);
		}
		return trackingService;
	}

	public BroadcastService getBroadcastService() {
		if (broadcastService == null) {
			broadcastService = ServiceFactory.getService(BroadcastService.class);
		}
		return broadcastService;
	}

	protected ProductStampingSequenceDao getProductStampingSequenceDao() {
		if (productStampingSequenceDao == null) {
			productStampingSequenceDao = ServiceFactory.getService(ProductStampingSequenceDao.class);
		}
		return productStampingSequenceDao;
	}

	protected ExpectedProductDao getExpectedProductDao() {
		if (expectedProductDao == null) {
			expectedProductDao = ServiceFactory.getService(ExpectedProductDao.class);
		}
		return expectedProductDao;
	}
	
	protected void populateReply(Device device) {
		super.populateReply(device);
	}
}
