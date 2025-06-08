package com.honda.galc.service.recipe;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.conf.MCOrderStructureDao;
import com.honda.galc.dao.conf.MCOrderStructureForProcessPointDao;
import com.honda.galc.dao.product.BaseOrderStructureDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.MfgCtrlMadeFrom;
import com.honda.galc.entity.conf.BaseMCOrderStructure;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.OrderResponseCode;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

//import com.honda.galc.dao.conf.MCOrderStructureDao;

/**
 * 
 * @author Wade Pei <br>
 * @date Nov 19, 2013
 */
public class RecipeDownloadServiceOrderImpl extends RecipeDownloadServiceImpl implements RecipeDownloadService {
	@Autowired
	private PreProductionLotDao preProductionLotDao;
	@Autowired
	private MCOrderStructureDao orderStructureDao;
	@Autowired
	private MCOrderStructureForProcessPointDao orderStructureForProcessPointDao;
	@Autowired
	private BuildAttributeDao buildAttributeDao;
	
	protected void getNextProduct(Device device) {
		String orderNo = null, planCode = null;
		if (null != device.getDeviceFormat(TagNames.CURR_ORDER_NO.name()).getValue()) {
			orderNo = device.getDeviceFormat(TagNames.CURR_ORDER_NO.name()).getValue().toString();
		}
		if (null != device.getDeviceFormat(TagNames.PLAN_CODE.name()).getValue()) {
			planCode = device.getDeviceFormat(TagNames.PLAN_CODE.name()).getValue().toString();
		}
		
		//RGALCPROD-2363
		boolean isPSDL = false;
		if (null != device.getDeviceFormat(TagNames.IS_PSDL.name()).getValue()) {
			isPSDL = Boolean.parseBoolean(device.getDeviceFormat(TagNames.IS_PSDL.name()).getValue().toString());
		}
		if(isPSDL) {
			getNextOrderForPSDL(orderNo, planCode);
		} else {
			getNextOrder(orderNo, planCode);
		}
		
	}

	private void setOrderStatusToSent(PreProductionLot order) {
		order.setSendStatusId(PreProductionLotSendStatus.SENT.getId());
		getPreProductionLotDao().save(order);
	}

	private void getNextOrder(String orderNo, String planCode) {
		PreProductionLot nextOrder = null, defaultOrder = getPreProductionLotDao().getNextPreProductionLot(planCode, PreProductionLotSendStatus.WAITING.getId());
		if (StringUtils.isBlank(orderNo)) {
			nextOrder = defaultOrder;
		} else {
			nextOrder = getPreProductionLotDao().getNextByLastPreProductionLot(orderNo, planCode);
		}
		OrderResponseCode respCode = null;
		// If the next order is null, it means the orderNo or planCode is wrong, we just return UNKOWN.
		if (null == nextOrder) {
			respCode = OrderResponseCode.UNKOWN;
		} else if (PreProductionLotSendStatus.SENT.getId() == nextOrder.getSendStatusId()) {
			respCode = OrderResponseCode.ALREADY_REQUESTED;
		} else if (nextOrder.getSequence() > defaultOrder.getSequence()) {
			respCode = OrderResponseCode.EXPECTED_SKIPPED;
		} else {
			respCode = OrderResponseCode.OK;
		}
		List<MfgCtrlMadeFrom> madeFroms = null;
		if (null != nextOrder) {
			BaseOrderStructureDao<? extends BaseMCOrderStructure, ?> dao = ServiceFactory.getDao(getStructureCreateMode().getOrderStructureDaoClass());
			madeFroms = dao.getMadeFrom(nextOrder.getProductionLot(), processPointId);
		}
		if (null != madeFroms && !madeFroms.isEmpty()) {
			putToContext(nextOrder, respCode, madeFroms.get(0));
		} else {
			putToContext(nextOrder, respCode, null);
		}
		if (null != nextOrder) {
			setOrderStatusToSent(nextOrder);
		}
	}

	private void putToContext(PreProductionLot order, OrderResponseCode infoCode, MfgCtrlMadeFrom madeFrom) {
		if (null != order) {
			// TODO set the productSpec.
			// productSpec = order.getProductSpecCode();
			context.put(TagNames.ORDER_NO.name(), order.getProductionLot());
			context.put(TagNames.PLAN_CODE.name(), order.getPlanCode());
			context.put(TagNames.PRODUCT_SPEC_CODE.name(), order.getProductSpecCode());
			// TODO set the Priority date.
//			if (null != order.getPriorityDate()) {
//				context.put(TagNames.PRIORITY_DATE.name(),
//						DateFormatUtils.format(order.getPriorityDate(), "yyyy-MM-dd"));
//			}
			context.put(TagNames.PRIORITY_SEQ.name(), order.getSequence());
			context.put(TagNames.PROD_ORDER_QTY.name(), order.getLotSize());
//			context.put(TagNames.LOCATION_LEVEL_ID.name(), order.getLocationLevelId());
			context.put(TagNames.ORDER_STATUS_ID.name(), order.getSendStatusId());
			context.put(TagNames.HOLD_STATUS_ID.name(), order.getHoldStatus());
		}
		if (null != madeFrom) {
			context.put(TagNames.OPERATION_NAME.name(), madeFrom.getOperationName());
			context.put(TagNames.OP_REV.name(), madeFrom.getOperationRevision());
			context.put(TagNames.PART_ID.name(), madeFrom.getPartId());
			context.put(TagNames.PART_REV.name(), madeFrom.getPartRevision());
			context.put(TagNames.STRUCTURE_REV.name(), madeFrom.getStructureRevision());
		}
		context.put(TagNames.INFO_CODE.name(), infoCode.getId());
		context.put(TagNames.INFO_MSG.name(), infoCode.getMessage());
	}
	
	//RGALCPROD-2363: for PSDL
	private void getNextOrderForPSDL(String lotNumber, String planCode) {
		PreProductionLot nextOrder = null, defaultOrder = getPreProductionLotDao().getNextPreProductionLot(planCode, PreProductionLotSendStatus.WAITING.getId());
		if (StringUtils.isBlank(lotNumber)) {
			nextOrder = defaultOrder;
		} else {
			nextOrder = getPreProductionLotDao().getNextByLastLotNumber(lotNumber, planCode);
		}
		OrderResponseCode respCode = null;
		// If the next order is null, it means the orderNo or planCode is wrong, we just return UNKOWN.
		if (null == nextOrder) {
			respCode = OrderResponseCode.UNKOWN;
		} else if (PreProductionLotSendStatus.SENT.getId() == nextOrder.getSendStatusId()) {
			respCode = OrderResponseCode.ALREADY_REQUESTED;
		} else if (nextOrder.getSequence() > defaultOrder.getSequence()) {
			respCode = OrderResponseCode.EXPECTED_SKIPPED;
		} else {
			respCode = OrderResponseCode.OK;
		}
		putToPSDLContext(nextOrder, respCode);
		
		//RGALCPROD-2363: for PSDL, need YMTO from gal259tbx
		if (null != nextOrder) {
			//The product_spec_code is MBPN + HesColor
			List<BuildAttribute> ymtoAttr = buildAttributeDao.findAllMatchId(nextOrder.getMbpn()+nextOrder.getHesColor(), TagNames.YMTO.name());
			if(ymtoAttr!=null && ymtoAttr.size()>0) {
				putYMTO(ymtoAttr.get(0));
			}
		}
		
		if (null != nextOrder) {
			setOrderStatusToSent(nextOrder);
		}
	}

	private void putToPSDLContext(PreProductionLot order, OrderResponseCode respCode) {
		putToContext(order, respCode, null);
		context.put(TagNames.ORDER_NO.name(), order.getLotNumber());
		context.put(TagNames.PRODUCTION_LOT.name(), order.getProductionLot());
	}

	private void putYMTO(BuildAttribute ymto) {
		if(ymto!=null) {
			context.put(TagNames.YMTO.name(), ymto.getAttributeValue());
		} else {
			context.put(TagNames.YMTO.name(), "");
		}
	}

	public static StructureCreateMode getStructureCreateMode() {
		String structureMode = PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString());
		return structureMode == null ? StructureCreateMode.DIVISION_MODE : StructureCreateMode.get(structureMode);
	}
	
	public MCOrderStructureDao getOrderStructureDao() {
		return orderStructureDao;
	}

	public void setOrderStructureDao(MCOrderStructureDao orderStructureDao) {
		this.orderStructureDao = orderStructureDao;
	}

	public MCOrderStructureForProcessPointDao getOrderStructureForProcessPointDao() {
		return orderStructureForProcessPointDao;
	}

	public void setOrderStructureForProcessPointDao(
			MCOrderStructureForProcessPointDao orderStructureForProcessPointDao) {
		this.orderStructureForProcessPointDao = orderStructureForProcessPointDao;
	}
	
	@Override
	protected void prepareSingleProductRecipe(Device device) {
		try{
			if(getHelper().getProperty().isUseNextProductId())
				getNextProduct(device);
			else
				getCurrentProduct(device);
			
			getLotControlRules(product);

			if(hasLotControlRule())
				populateRules(device);
			else 
				getLogger().info("There is no Lot Control Rule defined!");
			
			if(null!=product && !getPropertyBean().getPreviousProcessPointId().equals(product.getLastPassingProcessPointId()))
				addError(getErrorCodeDescription(), product.getProductId());

			populateCommon();
			processTask();
			processAddtionalFeatures(device);

			if(isValidForTracking()) 
				trackProduct();
			
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);

			if(NO_ERROR.equals(context.get(TagNames.ERROR_CODE.name())))
				getLogger().info("Ref#", getRequestProductId(), " - Next Ref# OK.");
			
		}catch(TaskException te){
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().warn(te, te.getMessage(), " ",this.getClass().getSimpleName());
		}catch (Exception e){
			contextPut(TagNames.ERROR_CODE.name(), RecipeErrorCode.System_Err.getCode());
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().warn(e, RecipeErrorCode.System_Err.getDescription() + getRequestProductId(), " ", this.getClass().getSimpleName());
		}
	}
}
