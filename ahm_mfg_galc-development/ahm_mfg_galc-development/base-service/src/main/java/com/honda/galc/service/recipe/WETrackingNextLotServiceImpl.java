package com.honda.galc.service.recipe;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.property.WETrackingPropertyBean;
import com.honda.galc.service.WETrackingNextLotService;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Apr 26, 2016
 */
public class WETrackingNextLotServiceImpl extends RecipeDownloadBase implements
		WETrackingNextLotService {

	private enum OrderType {
		PRODUCTION_LOT, KD_LOT
	};
	
	
	/**
	 * The Class InnerData is used as parameter object.
	 */
	private class InnerData {
		private PreProductionLot nextPreProductionLot;
		private List<PreProductionLot> currentPreProductionLots = new ArrayList<PreProductionLot>();
	}


	/**
	 * Retrieve next lot base on current lot number supplied from device's formats .
	 *
	 * @param device the device
	 * @return the device
	 */
	@Override
	public Device execute(Device device) {
		InnerData data = new InnerData();
		try {
			init(device);
			prepareNextLot(device, data);
		} catch (Throwable te) {
			getLogger().error(te, " Exception to get next Lot Number",
					this.getClass().getName());
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(),
					LineSideContainerValue.NOT_COMPLETE);
		}

		populateCommon(data);

		populateReply(device);

		return device;
	}
	

	private String getDataFromDevice(Device device, String tag) {
		DeviceFormat format = device.getDeviceFormat(tag);
		if(format == null) {
			getLogger().error(" Exception to get tag \"" + tag + "\" value from Device " + device.getClientId());
			return "";
		}
		return device.getDeviceFormat(tag).getValue().toString();
	}

	private String getCurrentLotNumber(Device device) {
		return getDataFromDevice(device, DataContainerTag.CURRENT_LOT);
	}

	/**
	 * Main business logic. Prepare next lot.
	 *
	 * @param device the device
	 * @param data the data
	 */
	private void prepareNextLot(Device device, InnerData data) {
		String currentLotNumber = getCurrentLotNumber(device);
		PreProductionLot nextPreProductionLot = null;
		if (isProductionLot()) {
			nextPreProductionLot = retrieveNextPreProductionLotByProductionLot(currentLotNumber, data);
		} else {
			nextPreProductionLot = retrieveNextPreProductionLotByKDLot(currentLotNumber, data);
		}

		if (nextPreProductionLot == null) {
			addError(WETrackingErrorCode.No_Next_Lot, currentLotNumber);
		} else if(nextPreProductionLot.getSendStatus().getId() > PreProductionLotSendStatus.WAITING.getId()) {
			addError(WETrackingErrorCode.Duplicate_Request, currentLotNumber);
		}
	}

	protected void populateCommon(InnerData data) {
		if (null == data.nextPreProductionLot) {
			return;
		}

		if (!StringUtils.isEmpty(getPropertyBean().getBuildAttributes()))
			populateBuildAttributes(data.nextPreProductionLot.getProductSpecCode());

		populateLotInfo(data);
	}

	/**
	 * Populate basic lot info.
	 *
	 * @param data the data
	 */
	private void populateLotInfo(InnerData data) {
		if (data.nextPreProductionLot != null ) {
			contextPut(TagNames.NEXT_LOT_NUMBER.name(), isProductionLot() 
					? data.nextPreProductionLot.getProductionLot() 
					: data.nextPreProductionLot.getKdLotNumber());
			contextPut(TagNames.ORDER_QTY.name(), getOrderQty(data));
			contextPut(TagNames.YMT.name(), data.nextPreProductionLot.getProductSpecCode().substring(0, 7));
			contextPut(TagNames.DEMAND_TYPE.name(), data.nextPreProductionLot.getDemandType());
			contextPut(TagNames.PROCESS_POINT_ID.name(), getProcessPointId());
		}
	}
	
	/**
	 * Gets the order quantity. Order quantity will be sum of all preProductionLot's lot Size.
	 *
	 * @param data the data
	 * @return the order quantity
	 */
	private int getOrderQty(InnerData data) {
		int result = 0;
		List<PreProductionLot> lots = getPropertyBean().isReturnNextKDLotQuantity() ? 
				getPreProductionLotDao().findByKDLotAndPlanCode(data.nextPreProductionLot.getKdLotNumber(), getPlanCode()) : 
				data.currentPreProductionLots;

		for (PreProductionLot preProductionLot : lots) {
			result += preProductionLot.getLotSize();
		}
		return result;
	}

	/**
	 * Populate build attributes which match the spec code.
	 *
	 * @param specCode the spec code
	 */
	private void populateBuildAttributes(String specCode) {
		try {	
		String[] split = getPropertyBean().getBuildAttributes().split(
				Delimiter.COMMA);
		BuildAttributeCache cache = new BuildAttributeCache();

		for (String buildattr : split) {
			BuildAttribute buildAttr = cache.findById(specCode, buildattr);

			if (buildAttr != null)
				contextPut(buildattr, buildAttr.getAttributeValue());
			else
			{
				getLogger().warn("Failed to find build attribute for:",
						buildattr, " product spec code:", specCode);
				if (getPropertyBean().isErrorCodeMissingBuildAttributes())
					addError(WETrackingErrorCode.Missing_Build_Attributes, specCode);
			}
		 }
		} catch (Throwable te) {
			getLogger().error(te, " Exception populating the build attributes", this.getClass().getName());
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
		}

	}

	/**
	 * Retrieve next PreProduction lot by kd lot.
	 *
	 * @param currentKDLotNumber the current kd lot number
	 * @param data the data
	 * @return the PreProduction lot
	 */
	private PreProductionLot retrieveNextPreProductionLotByKDLot(String currentKDLotNumber, InnerData data) {
		PreProductionLot preProductionLot = null;
		PreProductionLot nextPreProductionLot = null;

		List<PreProductionLot> preProdLotByKdLotPlanCodeList = getPreProductionLotDao()
				.findByKDLotAndPlanCode(currentKDLotNumber, getPlanCode());
		if(preProdLotByKdLotPlanCodeList == null || preProdLotByKdLotPlanCodeList.size() == 0) {
			getLogger().warn("Request Lot No. was invalid: " + currentKDLotNumber);
			addError(WETrackingErrorCode.Invalid, currentKDLotNumber);
		}
		
		for (PreProductionLot preProdLotByKdLotPlanCode : preProdLotByKdLotPlanCodeList) {
			boolean filteredByIgnoreRemark = "true".equalsIgnoreCase(getPropertyBean().getIgnoreRemakeFlag())
					|| "MP".equals(preProdLotByKdLotPlanCode.getDemandType());
			if (filteredByIgnoreRemark) {
				preProductionLot = getPreProductionLotDao().findByKey(preProdLotByKdLotPlanCode.getProductionLot());
				nextPreProductionLot = getPreProductionLotDao().findByKey(preProductionLot.getNextProductionLot());
				data.currentPreProductionLots.add(preProductionLot);
				if (!currentKDLotNumber.equals(nextPreProductionLot.getKdLotNumber())) {
					nextPreProductionLot = getPreProductionLotDao().findByKey(preProductionLot.getNextProductionLot());
					data.nextPreProductionLot = nextPreProductionLot;
					break;
				}
			}

		}

		return nextPreProductionLot;
	}

	/**
	 * Retrieve next PreProduction lot by production lot.
	 *
	 * @param currentProductLotNumber the current product lot number
	 * @param data the data
	 * @return the PreProduction lot
	 */
	private PreProductionLot retrieveNextPreProductionLotByProductionLot(String currentProductLotNumber,  InnerData data) {
		String nextLotNumber = "";
		PreProductionLot nextPreProductionLot = null;
		PreProductionLot currentLot = getPreProductionLotDao().findByKey(
				currentProductLotNumber);
		if (currentLot != null) {
			data.currentPreProductionLots.add(currentLot);
			nextLotNumber = currentLot.getNextProductionLot();
			nextPreProductionLot = getPreProductionLotDao().findByKey(
					nextLotNumber);
			data.nextPreProductionLot = nextPreProductionLot;
		} else {
			getLogger().warn("Request Lot No. was invalid: " + currentProductLotNumber);
			addError(WETrackingErrorCode.Invalid, currentProductLotNumber);
		}
		return nextPreProductionLot;
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

	public WETrackingPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(
					WETrackingPropertyBean.class, getProcessPointId());
	}

	protected boolean isProductionLot() {
		return OrderType.PRODUCTION_LOT.name().equals(
				getPropertyBean().getOrderType());
	}


	protected String getPlanCode() {
		return getPropertyBean().getPlanCode();
	}
	
	private Device createDevice(DataContainer data) {
		Device device = getDao(DeviceDao.class).findByKey(data.getClientID());
		device.populate(data);
		return device;
	}
	
	@Override
	public DataContainer execute(DataContainer data) {
		DataContainer dc= execute(createDevice(data)).toReplyDataContainer(true);
		confirmSend(dc);
		if(getPropertyBean().isAlwaysReplyComplete())
			dc.put(TagNames.DATA_COLLECTION_COMPLETE.name(),LineSideContainerValue.COMPLETE);
		return dc;
	}

	@Override
	public void confirmSend(DataContainer dc) {
		Object error = dc.get(TagNames.ERROR_CODE.name());
		if(error==null || error.equals(WETrackingErrorCode.Next_Lot_Normal.getCode())) {
			setProcessPointId((String)dc.get(TagNames.PROCESS_POINT_ID.name()));
			String lotNumber = (String)dc.get(TagNames.NEXT_LOT_NUMBER.name());
			updateSendStatus(lotNumber);
		}
	}


	private void updateSendStatus(String lotNumber) {
		if(isProductionLot()) {
			getPreProductionLotDao().updateSendStatus(lotNumber, PreProductionLotSendStatus.SENT.getId());
		} else {
			List<PreProductionLot> preProdLotByKdLotPlanCodeList = getPreProductionLotDao().findByKDLotAndPlanCode(lotNumber, getPlanCode());
			for (PreProductionLot preProdLotByKdLotPlanCode : preProdLotByKdLotPlanCodeList) {
				getPreProductionLotDao().updateSendStatus(preProdLotByKdLotPlanCode.getProductionLot(), PreProductionLotSendStatus.SENT.getId());
			}
		}
	}

}
