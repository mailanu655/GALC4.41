package com.honda.galc.service.on;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.ProductIdMaskDao;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.BaseMCProductStructure;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductIdMask;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.property.ProductOnHlPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.vios.ProductStructureService;
import com.honda.galc.util.CommonPartUtility;

public class MbpnProductOnServiceImpl extends ProductOnServiceImpl implements MbpnProductOnService {
	protected String productionLot;
	protected String productSpecCode;
	protected String lotNumber;
	
	private MbpnProductDao _mbpnProductDao;
	private ProductStampingSequenceDao _productStampingSequenceDao;
	private BroadcastService _broadcastService;
	
	protected void processSingleProduct() {
		try {

			lotNumber = getContextString(getFullTag(TagNames.LOT_NUMBER.name()));
			
			if (lotNumber != null) {
				String planCode = getPropertyBean().getPlanCode();
				
				preProductionLot = getPreProductionLotDao().getByLotNumberAndPlanCode(lotNumber, planCode);
				
				if (preProductionLot == null) throw new TaskException(String.format("Could not find PreProductionLot by Lot Number (%s) & Plan Code (%s)", lotNumber, planCode));

				productionLot = preProductionLot.getProductionLot();
				productSpecCode = preProductionLot.getMbpn();
			} else {
				productionLot = getContextString(getFullTag(TagNames.PRODUCTION_LOT.name()));
				if (getPropertyBean().isCheckPreproductionLot() && checkProduct()) {
					contextPut(TagNames.ERROR_CODE.name(), OnErrorCode.Invalid_Ref.getCode());
					logAndNotify(MessageType.ERROR, "Product:", productionLot, " not exist in PreProductionLot.");
					return;
				}
			}
			
			extractDeviceData();

			createProduct(productId, productionLot, productSpecCode);

			processTask();

			if (getPropertyBean().isAutoTracking())
				track();

			getBroadcastService().broadcast(getProcessPointId(), productId);

			logAndNotify(MessageType.INFO, "Product:", productId, " processed.");

			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(),LineSideContainerValue.COMPLETE);
			
		} catch (TaskException te) {
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(),LineSideContainerValue.NOT_COMPLETE);
			getLogger().error(te, te.getMessage(),this.getClass().getSimpleName());
		} catch (Exception e) {
			contextPut(TagNames.ERROR_CODE.name(),OnErrorCode.System_Err.getCode());
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(),LineSideContainerValue.NOT_COMPLETE);
			getLogger().error(e,OnErrorCode.System_Err.getDescription() + productId, this.getClass().getSimpleName());
			
		}
	}

	@Override
	protected void init(Device device) {
		super.init(device);

		productType = null;
		productName = null;
		preProductionLot = null;
		productId = null;
		product = null;
		productionLot = null;
		productSpecCode = null;
		lotNumber = null;
	}

	private void extractDeviceData() {
		productId = getContextString(getFullTag(TagNames.PRODUCT_ID.name()));
		if(StringUtils.isEmpty(productId)){
			contextPut(TagNames.ERROR_CODE.name(), OnErrorCode.Invalid_Ref.getCode());
			throw new TaskException("Exception: invalid product Id.");
		}
		
		if (productSpecCode == null) productSpecCode = getContextString(getFullTag(TagNames.PRODUCT_SPEC_CODE.name()));
		if (productionLot == null) productionLot = getContextString(getFullTag(TagNames.PRODUCTION_LOT.name()));

		//throws an exception if duplicate check fails
		checkForDuplicate();		
		
		//try to get the spec from the database "PRODUCT_SPEC_CODE_MASK_TBX"
		//Only works if property CHECK_PRODUCT_SPEC : true.
		boolean checkProductSpec = PropertyService.getPropertyBean(ProductOnHlPropertyBean.class,getProcessPointId()).isCheckProductSpec();
		if((null==productSpecCode || productSpecCode.equals("")) && checkProductSpec){
			productSpecCode = getProductSpecCode(productId);			
		}
		if(productSpecCode == null || productSpecCode.equals("")){
			contextPut(DataContainerTag.ERROR_CODE,OnErrorCode.No_Spec_Code.getCode());
			throw new TaskException("No Spec Code Defined for ", productId);
		}

		getLogger().info("Data from Device - ProductId:", productId, " productionLot:", productionLot, " productSpecCode:", productSpecCode);
	}

	private void checkForDuplicate() {
		MbpnProduct mbpnProduct = ServiceFactory.getDao(MbpnProductDao.class).findByKey(productId);
		if(mbpnProduct == null) return;
		
		if(getPropertyBean().isProductSpecCodeUpdateAllowed()){
			// do not count as duplicate if spec code does not match, spec code will be updated
			if (mbpnProduct.getProductSpecCode().equalsIgnoreCase(productSpecCode)) {
				contextPut(TagNames.ERROR_CODE.name(), OnErrorCode.Duplicate_Ref.getCode());
				throw new TaskException("Exception: Duplicate product Id and spec.");
			}			
		} else {
			contextPut(TagNames.ERROR_CODE.name(), OnErrorCode.Duplicate_Ref.getCode());
			throw new TaskException("Exception: Duplicate product Id.");
		}
	}

	private String getProductSpecCode(String productId) {
		List<ProductIdMask> productIdMasks = new ArrayList<ProductIdMask>();
		productIdMasks= ServiceFactory.getDao(ProductIdMaskDao.class).findAllByProcessPointId(getProcessPointId());
		String maskFormat = PropertyService.getPartMaskWildcardFormat();
		for(ProductIdMask productIdMask:productIdMasks){
			String productMask = productIdMask.getId().getProductIdMask().trim();
			
			if(CommonPartUtility.verification(productId, productMask, maskFormat))
				return productIdMask.getProductSpecCode();
		}
		return null;
	}

	private boolean checkProduct() {
		try {
			PreProductionLot preProductionLot = new PreProductionLot();
			preProductionLot = getPreProductionLotDao().findByKey(productionLot);
			if (preProductionLot == null) {
				contextPut(TagNames.ERROR_CODE.name(), OnErrorCode.Invalid_Ref.getCode());
				return true;
			}

		} catch (Exception ex) {
			contextPut(TagNames.ERROR_CODE.name(), OnErrorCode.System_Err.getCode());
			getLogger().error(ex, " Exception to process Product On.");
			logAndNotify(MessageType.ERROR, "Exception to process Product:", productId);
			return true;
		}
		return false;
	}

	private void createProduct(String productId, String productionLot,	String productSpecCode) {
		try {
			MbpnProduct mbpnProduct = new MbpnProduct();
			mbpnProduct.setProductId(productId);
			getLogger().info("New MbpnProduct " + productId + " created");

			mbpnProduct.setCurrentOrderNo(productionLot);
			mbpnProduct.setCurrentProductSpecCode(productSpecCode);
			mbpnProduct.setLastPassingProcessPointId(getProcessPointId());
			getMbpnProductDao().save(mbpnProduct);
			
			product = mbpnProduct;
			contextPut(getFullTag(TagNames.PRODUCT.name()), product);						

			//Only update stamp sequence and pre-production lot if preproduction lot number is sent from plc
			if(!StringUtils.isEmpty(productionLot)){
				createProductStampingSequence(productId, productionLot);
				updatePreproductionLot();				
			}
			
			//bak - 201508319 - Add product to MC_PRODUCT_STRUCTURE_TBX
			if (getPropertyBean().isCreateProductStructure()) {
				BaseMCProductStructure prodStru = ServiceFactory.getService(ProductStructureService.class)
						.findOrCreateProductStructure(product, ServiceFactory.getDao(ProcessPointDao.class).findByKey(getProcessPointId()),
								PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString()));
				if(prodStru == null){
					getLogger().error("Could not create Product Structure for " + product.getProductId());				
				}
			}
			
			getLogger().info("MBPN record saved" + productionLot);
		} catch (Exception e) {
			contextPut(TagNames.ERROR_CODE.name(),	OnErrorCode.System_Err.getCode());
			logAndNotify(MessageType.ERROR, "Exception to create MbpnProduct:",	productId);
			getLogger().error(e, "Unable to create MbpnProduct " + productId);

		}
	}

	private void createProductStampingSequence(String productId,
			String productionLot) {
		try {
			int stampingSequenceNumber = getProductStampingSequenceDao().findStampCount(productionLot);
			ProductStampingSequence productStampSequence = new ProductStampingSequence();
			productStampSequence.setProductId(productId);
			productStampSequence.setProductionLot(productionLot);
			productStampSequence.setStampingSequenceNumber(stampingSequenceNumber);
			productStampSequence.setSendStatus(2);
			getProductStampingSequenceDao().save(productStampSequence);
			getLogger().info("ProductStampingSequence record saved" + productId);
		} catch (Exception e) {
			contextPut(TagNames.ERROR_CODE.name(),OnErrorCode.System_Err.getCode());
			logAndNotify(MessageType.ERROR,"Exception to create ProductStampingSequence:", productId);
			getLogger().error(e,"Unable to create ProductStampingSequence " + productId);

		}
	}
	
	private ProductStampingSequenceDao getProductStampingSequenceDao() {
		if (_productStampingSequenceDao == null) {
			_productStampingSequenceDao = ServiceFactory.getDao(ProductStampingSequenceDao.class);
		}
		return _productStampingSequenceDao;
	}

	private MbpnProductDao getMbpnProductDao() {
		if (_mbpnProductDao == null) {
			_mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		}
		return _mbpnProductDao;
	}

	private BroadcastService getBroadcastService() {
		if (_broadcastService == null) {
			_broadcastService = ServiceFactory.getService(BroadcastService.class);
		}
		return _broadcastService;
	}
}
