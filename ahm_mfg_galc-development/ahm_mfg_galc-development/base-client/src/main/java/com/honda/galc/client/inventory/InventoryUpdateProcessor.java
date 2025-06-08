/**
 * 
 */
package com.honda.galc.client.inventory;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.plc.IMemoryLoc;
import com.honda.galc.client.device.plc.IPlcSocketDevice;
import com.honda.galc.client.device.plc.RegisterLoc;
import com.honda.galc.client.device.plc.omron.PlcMemory;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.conf.PlcMemoryMapItemDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.service.FactoryNewsUpdateService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.QueueProcessor;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Feb 9, 2012
 */
public class InventoryUpdateProcessor extends QueueProcessor<String> {

	public static final String PAINT_A_DEVICE_ID = "PA_A";
	public static final String PAINT_B_DEVICE_ID = "PA_B";
	public static final Character nullChar = (char) 0x0;
	public static final long DEFAULT_UPDATE_INTERVAL = 10000;
	public static final String UPDATE_INTERVAL_PROP_KEY = "UPDATE_INTERVAL";
	public static final String INVENTORY_UPDATE = "InventoryUpdate";

	private Application _application = null;
	
	private DeviceFormatDao _deviceFormatDao;
	private PlcMemoryMapItemDao _plcMemoryMapItemDao;
	private FactoryNewsUpdateService _factoryNewsUpdateService;
	private Timer _memoryUpdateTimer = null;
	
	public volatile boolean _active = false;
	
	public InventoryUpdateProcessor(ApplicationContext appContext, Application application) {
		super();
		_application = application;
		_active = true;
		startInventoryUpdateDaemon();
	}
	
	private void startInventoryUpdateDaemon() {
		TimerTask timerTask = new TimerTask() {
			public void run() {
				try {
					enqueue("update inventory data");
				} catch (Exception ex) {
					ex.printStackTrace();
					getLogger().error("Unable to get inventory data");
				}
				
				if (!isActive())
					getMemoryUpdateTimer().cancel();
			}
		};

		// start in a new thread in 100 millisecs and execute every getInterval() secs
		getMemoryUpdateTimer().scheduleAtFixedRate(timerTask, 100, getInterval());
	}
	
	@Override
	public void processItem(String str) {
		try {
			List<PlcMemoryMapItem> items = ServiceFactory.getDao(PlcMemoryMapItemDao.class).findAllByPrefix(getApplication().getApplicationId() + ".");
			SortedArrayList<PlcMemoryMapItem> sortedItems = new SortedArrayList<PlcMemoryMapItem>("getSequence");
		
			StringBuilder strToWrite = createPlcWriteString(items);
			sortedItems.addAll(items);
		
			getPaintAFinsPlcDevice().writeMemory(getStartLoc(sortedItems), strToWrite);
			getPaintBFinsPlcDevice().writeMemory(getStartLoc(sortedItems), strToWrite);
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error("Exception occured in InventoryUpdateProcessor.processItem(). Unable to process InventoryUpdate: " + ex.getMessage());
		}
	}
	
	/**
	 * returns the start memory location for the plc write operation
	 * 
	 * @param mItems
	 * @return
	 */
	private PlcMemory getStartLoc(SortedArrayList<PlcMemoryMapItem> mItems) {
		PlcMemoryMapItem field = mItems.get(0);
		IMemoryLoc memoryLoc = Enum.valueOf(RegisterLoc.class, field.getMemoryBank().trim());
		int register = new Integer(field.getStartAddress().trim());
		
		PlcMemory memory = new PlcMemory(memoryLoc, register);
		if (field.getBitIndex() != null)
			memory.setBitAddress(field.getBitIndex());
		return memory;
	}
	
	/**
	 * gets all the configured DeviceFormat entries and constructs the message to 
	 * write to plc
	 * 
	 * @param mItems
	 * @return
	 */
	private StringBuilder createPlcWriteString(List<PlcMemoryMapItem> mItems) {
		List<DeviceFormat> deviceFormats = getDeviceFormatDao().findAllByDeviceId(getApplication().getApplicationId());	
		SortedArrayList<DeviceFormat> sortedDeviceFormats = new SortedArrayList<DeviceFormat>("getSequenceNumber");
		sortedDeviceFormats.addAll(deviceFormats);
		StringBuilder currentInventoryLogMsg = new StringBuilder("Current Inventory:  ");
		
		Hashtable<String, PlcMemoryMapItem> plcMemoryMap = createPlcMemoryMap(mItems);
		StringBuilder strBld = new StringBuilder();
		for(DeviceFormat deviceFormat: deviceFormats) {
			String deviceFormatKey = deviceFormat.getId().getClientId().trim() + "." + deviceFormat.getId().getTag().trim();
			if (plcMemoryMap.containsKey(deviceFormatKey)) {
				PlcMemoryMapItem plcMemoryMapItem = plcMemoryMap.get(deviceFormatKey);
				plcMemoryMapItem.setSequence(deviceFormat.getSequenceNumber());
				StringBuilder value = new StringBuilder();
				if (deviceFormat.getTagValue().startsWith(INVENTORY_UPDATE)) {
					value = getValueByReflection(deviceFormat, plcMemoryMapItem, currentInventoryLogMsg);
				}
				strBld.append(value);
			}
		}
		getLogger().info(currentInventoryLogMsg.toString());
		return strBld;
	}

	/**
	 * retrieves the value from the InventoryUpdate object using reflection
	 * based on the method name and parameter specified in the DeviceFormat table
	 * 		Example: InventoryUpdate.getCurrentInventory("LINE11")
	 * 		Example: InventoryUpdate.getTotalInventory("DIV2")
	 * 
	 * @param deviceFormat
	 * @param plcMemoryMapItem
	 * @return
	 */
	private StringBuilder getValueByReflection(DeviceFormat deviceFormat, PlcMemoryMapItem plcMemoryMapItem, StringBuilder logMessage) {
		Integer value = -1;
		StringBuilder strBldRetVal = new StringBuilder();
		String tagValue = deviceFormat.getTagValue().replace(INVENTORY_UPDATE + ".", "");
		String[] tokens =  tagValue.split("\\(");
		String methodName = tokens[0];
		String parameter = tokens[1].replace(")", "").equals("") ? "" : tokens[1].replace(")", "").replace("\"", "");
		try {
			if (parameter.equals("")) {
				value  = (Integer) getFactoryNewsUpdateService().getClass().getDeclaredMethod(methodName).invoke(getFactoryNewsUpdateService());
				logMessage.append(INVENTORY_UPDATE + "." + methodName + "() = " + value + "; ");
			}else { 
				value = (Integer) getFactoryNewsUpdateService().getClass().getDeclaredMethod(methodName, String.class).invoke(getFactoryNewsUpdateService(), parameter);
				logMessage.append(INVENTORY_UPDATE + "." + methodName + "(" + parameter + ") = " + value + "; ");
			}
			strBldRetVal = StringUtil.padLeft(StringUtil.intToChars(value), plcMemoryMapItem.getLength(), nullChar);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return strBldRetVal;
	}
	
	private Hashtable<String, PlcMemoryMapItem> createPlcMemoryMap(List<PlcMemoryMapItem> plcMemoryMapItems) {
		Hashtable<String, PlcMemoryMapItem> plcMemoryMap = new Hashtable<String, PlcMemoryMapItem>();
		for(PlcMemoryMapItem plcMemoryMapItem: plcMemoryMapItems) {
			String key = plcMemoryMapItem.getMetricId().trim();
			if (!plcMemoryMap.containsKey(key))
				plcMemoryMap.put(key, plcMemoryMapItem);
		}
		return plcMemoryMap;
	}

	public boolean isActive() {
		return _active;
	}
	
	public Application getApplication() {
		return _application;
	}
	
	public Timer getMemoryUpdateTimer() {
		if (_memoryUpdateTimer == null)
			_memoryUpdateTimer = new Timer();

		return _memoryUpdateTimer;
	}
	
	public DeviceFormatDao getDeviceFormatDao() {
		if(_deviceFormatDao == null)
			_deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		return _deviceFormatDao;
	}
	
	public PlcMemoryMapItemDao getPlcMemoryMapItemDao() {
		if(_plcMemoryMapItemDao == null)
			_plcMemoryMapItemDao = ServiceFactory.getDao(PlcMemoryMapItemDao.class);
		return _plcMemoryMapItemDao;
	}
	
	public FactoryNewsUpdateService getFactoryNewsUpdateService() {
		if(_factoryNewsUpdateService == null)
			_factoryNewsUpdateService = ServiceFactory.getService(FactoryNewsUpdateService.class);
		return _factoryNewsUpdateService;
	}
	
	public IPlcSocketDevice getPaintAFinsPlcDevice() {
		IPlcSocketDevice device = (IPlcSocketDevice) DeviceManager.getInstance().getDevice(PAINT_A_DEVICE_ID);
		if (!device.isInitialized()) {
			device.activate();
		}
		return device;
	}

	public IPlcSocketDevice getPaintBFinsPlcDevice() {
		IPlcSocketDevice device = (IPlcSocketDevice) DeviceManager.getInstance().getDevice(PAINT_B_DEVICE_ID);
		if (!device.isInitialized()) {
			device.activate();
		}
		return device;
	}
	
	private long getInterval() {
		long interval = DEFAULT_UPDATE_INTERVAL;
		try {
			interval = Long.parseLong(PropertyService.getProperty(getApplication().getApplicationId(), UPDATE_INTERVAL_PROP_KEY));
		} catch(Exception ex) {}
		
		return interval;
	}
}
