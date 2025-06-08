/**
 * 
 */
package com.honda.galc.client.datacollection.processor;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.headless.PlcDataReadyMonitorFactory;
import com.honda.galc.client.device.plc.PlcDataCollectionBean;
import com.honda.galc.client.device.plc.omron.AggregateMemoryWriter;
import com.honda.galc.client.device.plc.omron.PlcDataField;
import com.honda.galc.client.device.plc.omron.PlcMemory;
import com.honda.galc.client.enumtype.PinStampInfoCodes;
import com.honda.galc.client.events.PinStampRequest;
import com.honda.galc.client.headless.PlcDataCollectionController;
import com.honda.galc.data.memorymap.MemoryMapCache;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Aug 30, 2013
 */
public class PinStampPartSNProcessor extends PartSerialNumberProcessor implements Observer {

	public PinStampPartSNProcessor(ClientContext context) {
		super(context);
	}

	public void init() {
		installedPart = new InstalledPart();
		installedPart.setAssociateNo(context.getUserId());
		installedPart.setMeasurements(new ArrayList<Measurement>());
		
		if (getController().getState().getProductId() != null) {
			PlcDataCollectionBean bean = new PlcDataCollectionBean();
			bean.setProductId(getController().getState().getProductId());
			bean.setApplicationId(getApplicationId());
			bean.put("vin", new StringBuilder(getController().getState().getProductId()));
			bean.put("galcDataReady", new StringBuilder("1"), DeviceDataType.INTEGER);
		
			writeTags(bean);
			// Clear PLC memory locations	 
			createDataReadyMonitors(StringUtils.trimToEmpty(getApplicationId()));
		}
	}

	public void update(Observable o, Object event) {
		if (!(event instanceof PinStampRequest)) {
			return;
		}
		
		PinStampRequest req = (PinStampRequest) event;
		// confirm VIN Stamped matches current VIN	 
		if (!req.getVin().equals(getController().getState().getProductId())) {
			if (StringUtils.trimToEmpty(req.getInfoCode()).equals(PinStampInfoCodes.STAMPING_FAILED.getInfoCode())) {
				execute(new PartSerialNumber(getController().getState().getProductId()));	 
			}
		} else {
			// display part not installed Message
			getController().getFsm().error(new Message("The stamped Vin " + req.getVin() + 
					" does not match the current product id: " + getController().getState().getProductId()));
		}
	}
	
	private void createDataReadyMonitors(String applicationId) {
		PlcDataReadyMonitorFactory drFactory = new PlcDataReadyMonitorFactory(getApplicationId());
		drFactory.createMonitors(getPlcDcController());
	}
	
	private void writeTags(PlcDataCollectionBean bean) {
		Enumeration<PlcDataField> e = (Enumeration<PlcDataField>) bean.getPlcDataFields().elements();
		AggregateMemoryWriter aggWriter = new AggregateMemoryWriter(bean);
		while (e.hasMoreElements()) {
			PlcDataField field = e.nextElement();
		    	String key = getMemoryMapItemKey(field);
		    	if (MemoryMapCache.getMap(getApplicationId()).containsKey(key)) {
		    		setPlcMemory(field, key);
					aggWriter.addItem(field, getDeviceId());
		    	} else {
					getLogger().debug("Key: " + key + " not found");
		    	}
		}
		aggWriter.flush(getDeviceId());
	}

	private void setPlcMemory(PlcDataField field, String key) {
		PlcMemoryMapItem item = MemoryMapCache.getMap(getApplicationId()).get(key);
		String dataLoc = (item.getBitIndex() > -1) ? Integer.toString(item.getBitIndex()) : item.getByteOrder().trim();
		field.setPlcMemory(new PlcMemory(item.getMemoryBank().trim() + PlcDataCollectionController.SEPERATOR 
				+ item.getStartAddress().trim()
				+ PlcDataCollectionController.SEPERATOR
				+ dataLoc));
		field.setValue(StringUtil.padRight(field.getValue(), item.getLength(), ' ', true));
	}
	
	private String getMemoryMapItemKey(PlcDataField field) {
		return getApplicationId() + PlcDataCollectionController.SEPERATOR + 
			getDeviceId() + PlcDataCollectionController.SEPERATOR + field.getId().trim();
	}
		
	private String getDeviceId() {
		return getController().getState().getCurrentLotControlRule().getDeviceId();
	}
	
	private PlcDataCollectionController getPlcDcController() {
		return PlcDataCollectionController.getInstance(getApplicationId());
	}
	
	private String getApplicationId() {
		return context.getAppContext().getApplicationId();
	}
}
