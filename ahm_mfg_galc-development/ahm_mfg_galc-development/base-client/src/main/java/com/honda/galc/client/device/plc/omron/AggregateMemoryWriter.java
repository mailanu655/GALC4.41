/**
 * 
 */
package com.honda.galc.client.device.plc.omron;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.plc.ByteOrder;
import com.honda.galc.client.device.plc.IPlcSocketDevice;
import com.honda.galc.client.device.plc.PlcDataCollectionBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Dec 11, 2012
 */
public class AggregateMemoryWriter {

	protected SortedArrayList<KeyValue<Double, StringBuilder>> _itemsToWrite = new SortedArrayList<KeyValue<Double, StringBuilder>>("getKey", true);
	protected ConcurrentHashMap<Double, PlcMemory> _locationsToWrite = new ConcurrentHashMap<Double, PlcMemory>();
	protected Logger _logger = null;
	protected String _loggerId = "";
	
	public AggregateMemoryWriter(String loggerId) {
		_loggerId = loggerId;
	}
	
	public AggregateMemoryWriter(PlcDataCollectionBean bean) {
		_loggerId = bean.getTerminalId() + "_" + bean.getApplicationId();
	}
	
	@SuppressWarnings("unchecked")
	public boolean addItem(PlcDataField dataField, String deviceId) {
		try {
			PlcMemory memory = dataField.getPlcMemory();
			Double dataLength = new Double(dataField.getValue().length());
			if (memory.getBitAddress() > -1) {	
				// write bit values immediately
				getLogger().info("Writing to Plc " + dataField.getPlcMemory().toString() + StringUtil.replaceNonPrintableCharacters(dataField.getValue()));
				getPlcDevice(deviceId).writeMemory(dataField.getPlcMemory(), dataField.getValue());
			} else {
				Double key = getStartLocation(memory, dataLength);
				getItems().add(new KeyValue(key, dataField.getValue()));
				getLocations().put(key, dataField.getPlcMemory());
			}
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * convert starting location to a double
	 * 
	 * @param memory
	 * @param dataLength
	 * @return
	 */
	private Double getStartLocation(PlcMemory memory, Double dataLength) {
		Double key = memory.getBankCode() * 10000 
				+ memory.getRegister()
				+ getByteOrderVal(memory.getByteOrder());
		return key;
	}
	
	private Double getByteOrderVal(ByteOrder byteOrder) {
		if (byteOrder.equals(ByteOrder.low)) {
			return 0.5;
		} else {
			return 0.0;
		}
	}
	
	/**
	 * flushes all the items to the plc memory, using only
	 * the minimum number of fins messages required to write all data
	 * 
	 * @param deviceId
	 * @return
	 */
	public boolean flush(String deviceId) {
		KeyValue<Double, StringBuilder> currElem = null;
		KeyValue<Double, StringBuilder> nextElem = null;

		try {
			Stack<KeyValue<Double, StringBuilder>> stack = createStack();
			if (stack.isEmpty()){
				return true;
			}
			currElem = stack.pop();
			while (!stack.isEmpty()) {
				nextElem = stack.pop();
				if ((currElem.getKey() + currElem.getValue().length()/2.0) == nextElem.getKey()){
					currElem.setValue(currElem.getValue().append(nextElem.getValue()));
					getLogger().debug("Adding value for: " + nextElem.getKey() + " : " + nextElem.getValue());
				} else{
					writeCurrentElement(deviceId, currElem);
					currElem = nextElem;
				}
				nextElem = null;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (currElem != null) {
				writeCurrentElement(deviceId, currElem);
			}
		}
		return true;
	}

	private Stack<KeyValue<Double, StringBuilder>> createStack() {
		Stack<KeyValue<Double, StringBuilder>> stack = new Stack<KeyValue<Double, StringBuilder>>();
		stack.addAll(getItems());
		return stack;
	}

	private void writeCurrentElement(String deviceId, KeyValue<Double, StringBuilder> currElem) {
		StringBuilder val = currElem.getValue();
		if (getLocations().get(currElem.getKey()).getByteOrder() == ByteOrder.low){
			val = new StringBuilder(" ").append(val);
		}
		getLogger().info("Writing to Plc " + getLocations().get(currElem.getKey()).toString() + StringUtil.replaceNonPrintableCharacters(val));
		getPlcDevice(deviceId).writeMemory(getLocations().get(currElem.getKey()), val);
	}
	
	private IPlcSocketDevice getPlcDevice(String deviceId) {
		IPlcSocketDevice device = (IPlcSocketDevice) DeviceManager.getInstance().getDevice(deviceId);
		if (!device.isInitialized()) {
			device.activate();
		}
		return device;
	}
	
  	protected Logger getLogger() {
		if (_logger == null) {
			_logger = Logger.getLogger(_loggerId);
			_logger.getLogContext().setApplicationInfoNeeded(true);
			_logger.getLogContext().setMultipleLine(false);
			_logger.getLogContext().setCenterLog(false);
		}
		return _logger;
	}
  	
	private SortedArrayList<KeyValue<Double, StringBuilder>> getItems() {
		return _itemsToWrite;
	}
	
	private ConcurrentHashMap<Double, PlcMemory> getLocations() {
		return _locationsToWrite;
	}
}
