package com.honda.galc.device;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.common.NotificationInvoker;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceDataType;

/**
 * 
 * <h3>DeviceFormatGroup</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DeviceFormatGroup description </p>
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
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Nov 5, 2010
 *
 */
public class DeviceFormatGroup {
	private String id;
	private Logger logger;
	private byte[] oldBytes;
	
	private ByteBuffer oldData;
	private List<DeviceFormat> oldDataFormatList;
	private NotificationInvoker invoker;
	
	ByteBuffer data;
	List<DeviceFormat> dataFormatList;
	int position = 0;
	
	public DeviceFormatGroup(String id) {
		super();
		this.id = id;
		dataFormatList = new ArrayList<DeviceFormat>();
	}
	
	public DeviceFormatGroup(String key, Logger logger) {
		this(key);
		this.logger = logger;
	}

	public void add(DeviceFormat deviceFormat){
		dataFormatList.add(deviceFormat);
	};
	
	public void setData(byte[] data){
		backupOldData();
		
		oldBytes = new byte[data.length];
		System.arraycopy(data, 0, oldBytes, 0, data.length);
		
		this.data = ByteBuffer.wrap(data);
		this.data.order(ByteOrder.LITTLE_ENDIAN);

		for(DeviceFormat format : dataFormatList){
			format.setValue(getValue(this.data, format));
			getLogger().info(format.getTag(), ":" + format.getValue());
		}

	}
	
	public synchronized boolean  setDataChangedOnly(byte[] data) {
		
		if(Arrays.equals(data, oldBytes)) {
			return false;
		} else {
			getLogger().info(id + " : Data changed " + DeviceUtil.toHex(data));
			setData(data);
			return true;
		}
	}


	private void backupOldData() {
		oldData = this.data;
		if(oldData != null) oldData.rewind();
		
		oldDataFormatList = new ArrayList<DeviceFormat>();
		for(DeviceFormat format : dataFormatList){
			oldDataFormatList.add(format.clone());
		}
	}
	
	public Map<String, Object> decodeData(byte[] data) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		for(DeviceFormat format : dataFormatList){
			resultMap.put(format.getTag(), getValue(byteBuffer, format));
		}
		
		return resultMap;
		
	}
	
	private Object getValue(ByteBuffer byteBuffer, DeviceFormat format) {
		if(format.getDeviceDataType() == DeviceDataType.BOOLEAN){
			return (byteBuffer.getShort(getBooleanPosition(format)) & (1 << getBooleanOffset(format))) != 0;
		}else if(format.getDeviceDataType() == DeviceDataType.SHORT){
			return byteBuffer.getShort(getOffset(format));
		}else if(format.getDeviceDataType() == DeviceDataType.FLOAT){
			return byteBuffer.getFloat(getOffset(format));
		}else if(format.getDeviceDataType() == DeviceDataType.INTEGER){
			return byteBuffer.getInt(getOffset(format));
		}else if(format.getDeviceDataType() == DeviceDataType.STRING){
			byteBuffer.position(getOffset(format));
			byte[] bytes = new byte[format.getLength()];
			byteBuffer.get(bytes);
			return new String(bytes);
		}
		
		getLogger().error("Error: Unsupport data type:", format.getDeviceDataType().toString());
		return null;
	}
	
	private DevicePoint getDeviceDataValue(ByteBuffer byteBuffer, DeviceFormat format) {
		if(format.getDeviceDataType() == DeviceDataType.BOOLEAN){
			boolean result = (byteBuffer.getShort(getBooleanPosition(format)) & 1 << getBooleanOffset(format)) != 0;
			return new DevicePointBoolean(format.getId().getClientId(), format.getTag(), result);
		}else if(format.getDeviceDataType() == DeviceDataType.SHORT){
			return new DevicePointShort(format.getId().getClientId(), format.getTag(), byteBuffer.getShort(getOffset(format)));
		}else if(format.getDeviceDataType() == DeviceDataType.FLOAT){
			return new DevicePointFloat(format.getId().getClientId(), format.getTag(), byteBuffer.getFloat(getOffset(format)));
		}else if(format.getDeviceDataType() == DeviceDataType.INTEGER){
			return new DevicePointInteger(format.getId().getClientId(), format.getTag(), byteBuffer.getInt(getOffset(format)));
		}else if(format.getDeviceDataType() == DeviceDataType.STRING){
			byteBuffer.position(getOffset(format));
			byte[] bytes = new byte[format.getLength()];
			byteBuffer.get(bytes);
			return new DevicePointString(format.getId().getClientId(), format.getTag(), new String(bytes));
		}
		
		getLogger().error("Error: Unsupport data type:", format.getDeviceDataType().toString());
		return null;
	}

	private int getBooleanOffset(DeviceFormat format) {
		return format.getOffset() < 0 ? 0 : (format.getOffset()%16);
	}

	private int getBooleanPosition(DeviceFormat format) {
		return (format.getOffset()/16)*2;
	}

	private int getOffset(DeviceFormat format) {
		return format.getOffset() < 0 ? 0 : (format.getOffset()*2);
	}
	

	/**
	 * Find the start address of the Device
	 * @param dev
	 * @return
	 */
	public String getStartAddress() {
		return dataFormatList.get(0).getTagValue();
	}
	
	public String getLastRegister() {
		String result = null;
		for(DeviceFormat dev : dataFormatList){
			result = dev.getTagValue();
		}
		return result;
	}

	public int getPoints() {
	    int count = 0;
	    //16 bits for mitshubishi word unit
	    DeviceFormat lastDeviceFormat = dataFormatList.get(dataFormatList.size() -1);
	    if(lastDeviceFormat.getDeviceDataType() == DeviceDataType.BOOLEAN){
	    	count = getOffset(lastDeviceFormat)/16 + ((getOffset(lastDeviceFormat)%16 == 0) ? 0 : 1 );
	    } else if(lastDeviceFormat.getDeviceDataType() == DeviceDataType.STRING){
	    	count = getOffset(lastDeviceFormat) + (lastDeviceFormat.getLength()/2) + 
	    	(lastDeviceFormat.getLength()%2 == 0 ? 0 : 1);
	    } else {
	    	count = getOffset(lastDeviceFormat) + lastDeviceFormat.getLength();
	    }
	    
		return count == 0 ? ++count : count;
	}
	

	public boolean isItemChanged(){
		return !isEqual(dataFormatList.get(position).getValue(), oldDataFormatList.get(position).getValue());
	}
	
	public DevicePoint nextChangedItem(){
		
		try {
			while (position < dataFormatList.size() && !isItemChanged()) {
				position++;
			}
		} catch (Exception e) {
			return null;
		}
		
		return position < dataFormatList.size() ? getDeviceDataValue(data, dataFormatList.get(position++)) : null;
		
	}
	
	public boolean isItemChanged(int index){
		if(oldData == null) return true;
		
		DeviceFormat deviceFormat = dataFormatList.get(index);
		Object oldValue = getValue(oldData, deviceFormat);
		Object newValue = getValue(data, deviceFormat);
		
		return !isEqual(oldValue, newValue);
	}
	
	private boolean isEqual(Object oldValue, Object newValue) {
		if(newValue instanceof String){
			return newValue.equals(oldValue);
		} else 
			return oldValue == newValue;
	}

	public void rewind(){
		position = 0;
	}

	public void notifyChangedItems(DeviceListener listener) {
		rewind();
		
		IDeviceData changedItem = nextChangedItem();
		while(changedItem != null){
			listener.received(id, changedItem);
			changedItem = nextChangedItem();
		}
	}

	public void notifyChangedItems(String address, int port) {
		getInvoker().send(address, port, getChangedItems());
	}

	private DataContainer getChangedItems() {
		DataContainer dc = new DefaultDataContainer();
		DevicePoint changedItem = nextChangedItem();
		while(changedItem != null){
			dc.put(changedItem.getName(), changedItem.getValue().toString());
			changedItem = nextChangedItem();
		}
		return dc;
	}


	public String getId() {
		return id;
	}

	public Map<String, Object> getDataMap() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		for(DeviceFormat format : dataFormatList){
			resultMap.put(format.getTag(), format.getValue());
		}
		
		return resultMap;
	}
	
	private Logger getLogger() {
		return logger;
	}

	public NotificationInvoker getInvoker() {
		if(invoker == null)
			invoker = new NotificationInvoker(logger);
		return invoker;
	}
	

}
