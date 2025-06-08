package com.honda.galc.client.device.plc;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.device.exceptions.NetworkCommunicationException;
import com.honda.galc.client.device.exceptions.ResponseTimeoutException;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.dataformat.PlcBoolean;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Oct 19, 2011
 */
public class PlcDataReadyEventMonitor implements IPlcEventGenerator, Runnable {
	private int _interval = 1;				// default is 1 milliseconds // TODO Make configurable
	private String _name = "";
	private IPlcMemory _memLoc = null;
	private String _applicationId;
	private String _eventClass = null;
	private IPlcSocketDevice _plcSocketDevice = null;
	private DeviceDataType _dataType = DeviceDataType.PLC_BOOLEAN;
	private boolean _active = false;
	private boolean _logTraffic = true;
	
	private volatile boolean _prevVal = false;
	private volatile List<DeviceListener> _listeners = new ArrayList<DeviceListener>();
	
	/**
	 * 
	 * @param plcDevice
	 * @param memLoc
	 * @param eventClass
	 */
	public PlcDataReadyEventMonitor(IPlcSocketDevice plcSocketDevice, IPlcMemory memLoc, String eventClass) {
		_plcSocketDevice = plcSocketDevice;
		_memLoc = memLoc;
		_eventClass = eventClass;
		_active = true;
	}
	
	/**
	 * 
	 * @param plcDevice
	 * @param memLoc
	 * @param eventClass
	 * @param monitorInterval
	 */
	public PlcDataReadyEventMonitor(IPlcSocketDevice plcDevice, IPlcMemory memLoc, String eventClass, int monitorInterval) {
		this(plcDevice, memLoc, eventClass);
		_interval = monitorInterval;
	}
	
	/**
	 * Monitors the data ready memory location
	 */
	public void run() {
		boolean currVal = false;
		StringBuilder memReadVal = new StringBuilder();
		int bitAddress = getMemLoc().getBitAddress();

		while (isActive()) {
			try {
				Thread.sleep(getInterval());
				if (!getPlcSocketDevice().isInitialized())
					continue;				// plc is not initialized yet, wait till next cycle

				try {
					getLogger().debug("Attempting to read DataReady at " + getMemLoc().toString());
					memReadVal = getPlcSocketDevice().readMemory(getMemLoc(), islogTraffic());
					if (bitAddress > -1) {
						int bitVal = Integer.parseInt(StringUtil.stringToBitArray(memReadVal));
						memReadVal = (bitVal > 0) ? new StringBuilder("1") : new StringBuilder("0");
					}
					
					if (memReadVal == null)
						continue;			// nothing to process, wait for next cycle

					currVal = getCurrentValue(memReadVal);
					getLogger().debug(getName() + " " + getMemLoc().toString() + " value: " + currVal);
				} catch (NetworkCommunicationException ncx) {
					getLogger().warn("Network communication exception. Continuing with next read: " + getName());
					continue;
				} catch (ResponseTimeoutException rtx) {
					getLogger().warn("Response timed out. Continuing with next read: " + getName());
					continue;
				} catch(NumberFormatException ex) {
					ex.printStackTrace();
					getLogger().warn("NumberFormatException. Continuing with next read: " + getName());
					continue;
				} catch(Exception ex) {
					ex.printStackTrace();
					getLogger().warn("Exception occured with dataready read: " + getName());
					continue;
				} 
				
				if ((!getPreviousValue()) && currVal) {
					notifyListeners(createDataReadyEvent(this));	// event occurred, notify listeners
				}
			} catch (Exception ex) {
				getLogger().error("Data ready polling failed..continuing with next iteration");
				ex.printStackTrace();
			} finally {
				setPreviousValue(currVal);
			}
		}
		getLogger().warn("Exiting DataReady " + getMemLoc().toString());
	}

	private boolean getCurrentValue(StringBuilder memReadVal) {
		boolean currVal = false;
		final DeviceDataType dataType = getDataType();
		switch(dataType) {
			case SHORT:
				currVal = Integer.parseInt(StringUtil.stringToBitArray(memReadVal), 2) == 1;
				break;
			case INTEGER:
				currVal = Integer.parseInt(StringUtil.stringToBitArray(memReadVal), 2) == 1;
				break;
			case STRING:
				currVal = new PlcBoolean(memReadVal.toString()).getValue();
				break;
			case PLC_BOOLEAN:
				currVal = new PlcBoolean(memReadVal.toString()).getValue();
				break;
			default:
				break;
		}
		return currVal;
	}

	public void notifyListeners(final IPlcDeviceData dataReadyEvent) {
		synchronized (getListeners()) {
			for (final DeviceListener listener : getListeners()) {
				Runnable notifyWorker = new Runnable() {
					public void run() {
						try {
							listener.received(getName(), dataReadyEvent);
							getLogger().info("Successfully notified Data Ready event to listener");
						} catch (Exception ex) {
							ex.printStackTrace();
							getLogger().error(ex, "Could not notify Data Ready event to listener");
						}
					}
				};

				Thread worker = new Thread(notifyWorker);
				worker.start();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private IPlcDeviceData createDataReadyEvent(final PlcDataReadyEventMonitor monitor)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Class<IPlcDeviceData> plcEventClass = null;
		try {
			plcEventClass = (Class<IPlcDeviceData>) Class.forName(getEventClass());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		IPlcDeviceData dataReadyEvent = (IPlcDeviceData) plcEventClass.getConstructor(String.class, String.class).newInstance(getApplicationId(), getPlcSocketDevice().getId());
		dataReadyEvent.setEventGenerator(monitor);
		dataReadyEvent.collectDeviceData(getPlcSocketDevice().getId());
		getLogger().info("DataReadyMonitor generated dataready Event for " + getMemLoc().toString());
		return dataReadyEvent;
	}
	
	public boolean registerListener(DeviceListener listener) {
		synchronized (getListeners()) {
			// add if already not registered
			if (!getListeners().contains(listener))
				getListeners().add(listener);
		}
		return true;
	}
	
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getApplicationId() {
		return _applicationId;
	}

	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;
	}
	
	public boolean isActive() {
		return _active;
	}
	
	public void activate() {
		_active = true;
	}
	
	public void deActivate() {
		_active = false;
	}
	
	public void setInterval(Integer interval) {
		_interval = interval;
	}

	public Integer getInterval() {
		return _interval;
	}

	public boolean getPreviousValue() {
		return _prevVal;
	}
	
	public void setPreviousValue(boolean prevVal) {
		_prevVal = prevVal;
	}
	
	public void setMemLoc(IPlcMemory memLoc) {
		_memLoc = memLoc;
	}
	
	public IPlcMemory getMemLoc() {
		return _memLoc;
	}

	public void setEventClass(String eventClass) {
		_eventClass = eventClass;
	}
	
	public String getEventClass() {
		return _eventClass;
	}

	public void setPlcSocketDevice(IPlcSocketDevice plcSocketDevice) {
		_plcSocketDevice = plcSocketDevice;
	}

	public IPlcSocketDevice getPlcSocketDevice() {
		return _plcSocketDevice;
	}
	
	public void setLogTraffic(boolean logTraffic) {
		_logTraffic = logTraffic;
	}

	public boolean islogTraffic() {
		return _logTraffic;
	}
	
	public void setDataType(DeviceDataType dataType) {
		_dataType = dataType;
	}

	public DeviceDataType getDataType() {
		return _dataType;
	}
	
	public List<DeviceListener> getListeners() {		
		return _listeners;
	}
}
