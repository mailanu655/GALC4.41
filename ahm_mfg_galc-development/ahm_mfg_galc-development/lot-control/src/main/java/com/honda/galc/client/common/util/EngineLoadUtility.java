/**
 * 
 */
package com.honda.galc.client.common.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.datacollection.property.EngineLoadPropertyBean;
import com.honda.galc.client.device.plc.omron.FinsSocketPlcDevice;
import com.honda.galc.client.device.plc.omron.FinsMemoryManager;
import com.honda.galc.client.engine.EngineLoad;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.PlcMemoryMapItemDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.AfbData;
import com.honda.galc.entity.conf.AfbDataId;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.common.ProductHoldService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.EngineMissionAssignmentUtil;
import com.honda.galc.util.ProductHoldUtil;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Jan 19, 2012
 */

public class EngineLoadUtility extends EngineMissionAssignmentUtil {
	
	public static final String DATA_READY = "dataReady";
	
	/**
	 * updates Engine Load PLC with the validated Vin and AFB data
	 * @param plcDevice
	 * @param vin
	 * @return
	 */
	public boolean updateEngineLoadPlc(FinsSocketPlcDevice plcDevice, String vin) {
		try {
			EngineLoad engineLoad = new EngineLoad();
			List<PlcMemoryMapItem> memoryMapItems = ServiceFactory.getDao(PlcMemoryMapItemDao.class).findAllByPrefix(ClientMain.getInstance().getApplicationContext().getTerminalId() + ".");
			
			// add current vin and next 3 vins to list for processing
			List<String> vins = new ArrayList<String>();
			vins.addAll(getInProcessProductDao().getVinSequence(vin, 4));
			
			int index = 1;
			for(String currVin: vins) {
				updateAfbData(engineLoad, index++, currVin);
			}
			return FinsMemoryManager.getInstance().write(engineLoad, updateMemoryMapAttributes(memoryMapItems), plcDevice);
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error("Could not update Engine load PLC with AFB data: " + ex.getMessage());
		}
		return false;
	}
	
	/**
	 * updates Afb data for a given vin
	 *
	 * @param engineLoad
	 * @param index
	 * @param vin
	 * @return
	 */
	private boolean updateAfbData(EngineLoad engineLoad, int index, String vin) {
		FrameSpec frameSpec = getFrameSpec(vin);

		engineLoad.setValue("setMto" + index, frameSpec.getModelCode() 
				+ frameSpec.getModelTypeCode() 
				+ StringUtil.padRight(frameSpec.getModelOptionCode(), 3, ' ', true) 
				+ frameSpec.getModelYearCode());
		engineLoad.setValue("setSeq" + index, StringUtil.padLeft(new Integer(getFrameDao().findByKey(vin).getAfOnSequenceNumber()).toString(), 4, '0', true));
		engineLoad.setValue("setVin" + index, vin);
		engineLoad.setValue("setAfb" + index, getAfbDataText(vin));
		engineLoad.setValue("setChecksum" + index, "");

		return true;
	}
	
	/**
	 * returns the AFB data text for the current product
	 * @param vin
	 * @return
	 */
	protected StringBuilder getAfbDataText(String vin) {
		StringBuilder afbDataText = new StringBuilder("");
		FrameSpec frameSpec = getFrameSpec(vin);
		try {
			AfbDataId afbId = new AfbDataId();
			afbId.setModel(frameSpec.getModelYearCode() + frameSpec.getModelCode());
			afbId.setType(frameSpec.getModelTypeCode());
			afbId.setOption(frameSpec.getModelOptionCode());
			AfbData afbData = getAfbDataDao().findByKey(afbId);
			return StringUtil.bitArrayToChars(afbData.getAfbDataText());
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error("Could not retrieve afb data for FrameSpec: " + 
					(ex.getMessage() == null ? "" : ex.getMessage() + " ")
					+ (frameSpec == null ? "" : frameSpec.getModelYearCode())
					+ frameSpec.getModelCode()
					+ frameSpec.getModelTypeCode()
					+ frameSpec.getModelOptionCode());
		}
		return afbDataText;
	}
	
	/**
	 * takes an unsorted list of memory map items and returns a sorted list based 
	 * on the sequence number specified for map items in DeviceFormat
	 * 
	 * @param mItems
	 * @return
	 */
	private SortedArrayList<PlcMemoryMapItem> updateMemoryMapAttributes(List<PlcMemoryMapItem> mItems) {
		List<DeviceFormat> deviceFormats = getDeviceFormatDao().findAllByDeviceId(ClientMain.getInstance().getApplicationContext().getTerminalId());	
		Hashtable<String, DeviceFormat> deviceFormatMap = new Hashtable<String, DeviceFormat>();

		for(DeviceFormat deviceFormat: deviceFormats) {
			String key = deviceFormat.getId().getClientId().trim() + "." + deviceFormat.getId().getTag().trim();
			if (!deviceFormatMap.containsKey(key))
				deviceFormatMap.put(key, deviceFormat);
		}

		for(PlcMemoryMapItem item: mItems) {
			String metricId = item.getMetricId().trim();
			if (deviceFormatMap.containsKey(item.getMetricId().trim())) {
				DeviceFormat deviceFormat = deviceFormatMap.get(item.getMetricId().trim());
				item.setSequence(deviceFormat.getSequenceNumber());
				item.setValue(deviceFormat.getTagValue() == null ? "" : deviceFormat.getTagValue().trim());
				item.setOperationType(deviceFormat.getTagType());
				item.setDataReady(metricId.contains(DATA_READY) ? 1 : 0);
			}
		}

		SortedArrayList<PlcMemoryMapItem> memoryMapItems = new SortedArrayList<PlcMemoryMapItem>("getSequence");
		memoryMapItems.addAll(mItems);
		return memoryMapItems;
	}

	/**
	 * checks if the engine tracking status is valid
	 * 
	 * @return
	 */
	public boolean checkValidPreviousEngineLine(Engine engine, String processPointId) {
		String validPreviousLines = "";
		EngineLoadPropertyBean engineLoadPropertyBean;

		try {
			engineLoadPropertyBean = PropertyService.getPropertyBean(EngineLoadPropertyBean.class, processPointId);
		} catch(Exception ex) {
			// can't perform the validation if the property is not accessible, so skip validation
			Logger.getLogger().warn("Could not retrieve EngineLoadPropertyBean: skipping checkValidPreviousEngineLine() " + (ex.getMessage() == null ? "" : ex.getMessage()));					
			return true;
		}

		// return true if no previous lines are specified
		if (engineLoadPropertyBean.getValidEngineLoadPreviousLines() == null || 
				engineLoadPropertyBean.getValidEngineLoadPreviousLines().trim().equals(""))
			return true;

		// if previous lines are specified in the property, perform the check
		validPreviousLines = engineLoadPropertyBean.getValidEngineLoadPreviousLines();
		boolean isValidPreviousLine = false;

		for(String validLine: validPreviousLines.split(Delimiter.COMMA)) {
			if (engine.getTrackingStatus().equals(validLine)) {
				isValidPreviousLine = true;
				break;
			}
		}
		return isValidPreviousLine;
	}
	
	public static void holdFrame(Frame frame, ProcessPoint processPoint, String reason) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT.name(), frame);
		dc.put(TagNames.PRODUCT_ID.name(), frame.getProductId());
		dc.put(TagNames.PRODUCTION_LOT.name(), frame.getProductionLot());
		dc.put(TagNames.PRODUCT_TYPE.name(), frame.getProductType());
		dc.put(TagNames.PROCESS_POINT.name(), processPoint);
		dc.put(TagNames.PROCESS_POINT_ID.name(), processPoint.getId());
		dc.put(TagNames.PROCESS_LOCATION.name(), processPoint.getDivisionId());
		dc.put(TagNames.ASSOCIATE_ID.name(), processPoint.getId());
		dc.put(TagNames.HOLD_REASON.name(), reason);
		dc.put(TagNames.HOLD_SOURCE.name(), 0);
		dc.put(TagNames.HOLD_RESULT_TYPE.name(), HoldResultType.HOLD_AT_SHIPPING);
		dc.put(TagNames.HOLD_ACCESS_TYPE.name(), ProductHoldUtil.getDefaultAccessTypeByHoldType(processPoint.getId(), HoldResultType.HOLD_AT_SHIPPING));
		dc.put(TagNames.QSR_HOLD.name(), true);		
		ServiceFactory.getService(ProductHoldService.class).execute(dc);
	}
}
