package com.honda.galc.service.recipe;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.Weld;
import com.honda.galc.property.WETrackingPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.WETrackingProductCompleteService;
import com.honda.galc.service.on.ProductOnServiceImpl;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Apr 26, 2016
 */
public class WETrackingProductCompleteServiceImpl extends ProductOnServiceImpl implements WETrackingProductCompleteService {
	
	private enum OrderType {
		PRODUCTION_LOT, KD_LOT
	};
	
	
	@Override
	protected void init(Device device) {
		super.init(device);
		//set default error info
		context.put(TagNames.ERROR_CODE.name(), WETrackingErrorCode.Product_Complete_Normal.getCode());
		context.put(TagNames.ERROR_MESSAGE.name(), WETrackingErrorCode.Product_Complete_Normal.getDescription());
	}

	@Override
	protected void processSingleProduct() {
		try{
			preProductionLot = retrievePreProductionLot((String)context.get(DataContainerTag.CURRENT_LOT));
			
			if(preProductionLot==null)
			{
				getLogger().error("Missing Pre Production lot:"+(String)context.get(DataContainerTag.CURRENT_LOT));
				addError(WETrackingErrorCode.Missing_PreProduction_lot,(String)context.get(DataContainerTag.CURRENT_LOT));
			}
			checkMachineType();
			
			saveSubProduct();

			updatePreproductionLot();

			track();
			
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
		}catch(TaskException te){
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().error(te, te.getMessage(), this.getClass().getSimpleName());
		}catch (Exception e){
			contextPut(TagNames.ERROR_CODE.name(), RecipeErrorCode.System_Err.getCode());
			getLogger().error(e, RecipeErrorCode.System_Err.getDescription() + productId, this.getClass().getSimpleName());
		}
	}
	
	@Override
	public DataContainer execute(DataContainer dc)
	{
		
		dc=super.execute(dc);		
		if(getPropertyBean().isAlwaysReplyComplete())
			dc.put(TagNames.DATA_COLLECTION_COMPLETE.name(),LineSideContainerValue.COMPLETE);
		return dc;
	}
	
	

	/**
	 * Check machine type match the configuration.
	 *
	 * @param device the device
	 * @param currentPreProductionLot the current pre production lot
	 */
	private void checkMachineType() {
		String machineType = (String)context.get(DataContainerTag.MACHINE_TYPE);
		checkMachineType(preProductionLot.getProductSpecCode(), machineType);
	}

	private void checkMachineType(String specCode, String machineType) {
		BuildAttributeCache cache = new BuildAttributeCache();
		BuildAttribute buildAttr = cache.findById(specCode, DataContainerTag.MACHINE_TYPE);
		boolean mismatch = buildAttr == null || StringUtils.isEmpty(machineType) || !machineType.equals(buildAttr.getAttributeValue());
		if(mismatch) {
			getLogger().warn("Machine type mismatch for product spec code:", specCode);
			addError(WETrackingErrorCode.Machine_Type_Mismatch, machineType);
		}
	}
	
	@Override
	protected boolean isLotCompleted(PreProductionLot preProductionLot) {
		return getLotCount(preProductionLot) >= preProductionLot.getLotSize(); 
	}
	
	private int getLotCount(PreProductionLot preProductionLot){
		return ServiceFactory.getDao(SubProductDao.class).countProductionLotByKdLot(preProductionLot.getProductionLot(), preProductionLot.getKdLotNumber());
	}
	
	/**
	 * Save sub product.
	 *
	 * @param device the device
	 * @param currentPreProductionLot the current pre production lot
	 * @return 
	 */
	private void saveSubProduct() {
		productId = (String)context.get(DataContainerTag.PRODUCT_ID);
		SubProduct subProduct= saveSubProduct(productId, preProductionLot);
		if(subProduct == null) {
			addError(WETrackingErrorCode.Product_Exist, productId);
		}
		product = subProduct;
	}
	
	private SubProduct saveSubProduct(String productId, PreProductionLot lot) {
		SubProductDao subProductDao = ServiceFactory.getDao(SubProductDao.class);
		//If product already exist, do nothing.
		if(subProductDao.findByKey(productId) != null) {
			return null;
		}

		SubProduct subProduct = new Weld();
		subProduct.setProductId(productId);
		subProduct.setProductionLot(lot.getProductionLot());
		subProduct.setKdLotNumber(lot.getKdLotNumber());
		subProduct.setProductSpecCode(lot.getProductSpecCode());
		//TODO set other attributes
		return subProductDao.save(subProduct);
	}
	
	/**
	 * Retrieve PreProductionLot by the lot number.
	 *
	 * @param currentLotNumber the current lot number
	 * @return the pre production lot
	 */
	private PreProductionLot retrievePreProductionLot(String currentLotNumber) {
		if(isProductionLot()) {
			PreProductionLot preProductionLot = getPreProductionLotDao().findByKey(currentLotNumber);
			if(checkLotAvailable(preProductionLot)) {
				return preProductionLot;
			}
		} else {
			List<PreProductionLot> preProdLotByKdLotPlanCodeList = getPreProductionLotDao()
					.findByKDLotAndPlanCode(currentLotNumber, getPlanCode());
			for (PreProductionLot preProdLotByKdLotPlanCode : preProdLotByKdLotPlanCodeList) {
				if(checkLotAvailable(preProdLotByKdLotPlanCode)) {
					return preProdLotByKdLotPlanCode;
				}
			}
		}
		return null;
	}
	
	protected boolean isProductionLot() {
		return OrderType.PRODUCTION_LOT.name().equals(
				getPropertyBean().getOrderType());
	}
	
	private String getPlanCode() {
		return getPropertyBean().getPlanCode();
	}
	
	@Override
	public WETrackingPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(
					WETrackingPropertyBean.class, getProcessPointId());
	}
	
	
	/**
	 * Check lot available. Availed lot should not done yet and stamped count less than lot size;
	 *
	 * @param lot the lot
	 * @return true, if successful
	 */
	private boolean checkLotAvailable(PreProductionLot lot) {
		if (lot != null) {
			boolean lotHaveDone = lot.getSendStatus() == PreProductionLotSendStatus.DONE;
			boolean lotIsCompleted = lot.getStampedCount() >= lot.getLotSize();
			if (!(lotHaveDone || lotIsCompleted)) {
				return true;
			} 
		}
		return false;
	}
	
	protected void addError(WETrackingErrorCode errorCode, String info) {
		WETrackingErrorCode fromCode = WETrackingErrorCode
				.fromCode((String) context.get(TagNames.ERROR_CODE.name()));
		if (fromCode == null
				|| errorCode.getSeverity() > fromCode.getSeverity()) {
			contextPut(TagNames.ERROR_CODE.name(), errorCode.getCode());
			contextPut(TagNames.ERROR_MESSAGE.name(), 
					StringUtils.replace(errorCode.getDescription(), "Ref#", info));
		}

		if (errorCode.isWarning())
			getLogger().warn(errorCode.getDescription(), info);
		else
			throw new TaskException(errorCode.getDescription() + info);
	}
	
}
