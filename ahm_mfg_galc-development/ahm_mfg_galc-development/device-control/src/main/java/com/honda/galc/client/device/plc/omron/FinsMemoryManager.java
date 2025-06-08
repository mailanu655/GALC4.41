/**
 * 
 */
package com.honda.galc.client.device.plc.omron;

import java.lang.reflect.Method;

import com.honda.galc.client.device.plc.IMemoryLoc;
import com.honda.galc.client.device.plc.IPlcSocketDevice;
import com.honda.galc.client.device.plc.omron.PlcMemory;
import com.honda.galc.client.device.plc.PlcMemoryOperations;
import com.honda.galc.client.device.plc.RegisterLoc;
import com.honda.galc.client.device.plc.memorymap.AbstractPlcMemoryMap;
import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Jan 9, 2012
 */
public class FinsMemoryManager {

	private static FinsMemoryManager _finsMemoryManager = null;

	private FinsMemoryManager() {}
	
	public static FinsMemoryManager getInstance()
	{
		if (_finsMemoryManager == null)
			_finsMemoryManager = new FinsMemoryManager();
		
		return _finsMemoryManager; 
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean write(AbstractPlcMemoryMap memoryMap, SortedArrayList<PlcMemoryMapItem> fields, IPlcSocketDevice finsPlcDevice) {
		StringBuilder message = new StringBuilder();
		PlcMemory startLoc = null;
		
		for (PlcMemoryMapItem field: fields) {
			if (!field.getOperationType().equals(PlcMemoryOperations.WRITE.getValue())) {
				writeContiguousFields(message, startLoc, finsPlcDevice);
				message = new StringBuilder();
				startLoc = null;
				continue;
			}

			if (field.getBitIndex() != null && field.getBitIndex() > -1) {
				writeContiguousFields(message, startLoc, finsPlcDevice);
				message = new StringBuilder();
				startLoc = null;
				writeBit(field, finsPlcDevice);
				continue;
			}
			
			if (startLoc == null) {
				startLoc = getFinsMemory(field);
			}
			
			Method targetMethod = null;
			try {
				targetMethod = memoryMap.getClass().getDeclaredMethod(StringUtil.getterName(field.getMetricId().trim().split("\\.")[1]));
			}catch (NoSuchMethodException ex) { 
				message.append(field.getValue());	// even if method is not available, include the default value in msg
				continue;
			}
			
			try {
				String fieldValue = targetMethod.invoke(memoryMap).toString();
				boolean padRight = true;
				Character padChar = ' ';
				message.append(StringUtil.pad(new StringBuilder(fieldValue), field.getLength(), padChar, true, padRight));
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}	
		}
		return writeContiguousFields(message, startLoc, finsPlcDevice);
	}
	
	
	/**
	 * write one or more contiguous fields to plc memory
	 * 
	 * @param message
	 * @param startMem
	 */
	public static boolean writeContiguousFields(StringBuilder message, PlcMemory startMem, IPlcSocketDevice finsPlcDevice) {
		if (message.length() > 0 && startMem != null) {
			return finsPlcDevice.writeMemory(startMem, message);
		}
		return true;
	}
	
	/**
	 * read the bit specified 
	 * @param field
	 * @param finsPlcDevice
	 * @return
	 */
	public StringBuilder readBit(PlcMemoryMapItem field, IPlcSocketDevice finsPlcDevice) {
		return finsPlcDevice.readMemory(getFinsMemory(field));	
	}

	/**
	 * write the specified bit
	 * @param field
	 * @param finsPlcDevice
	 * @return
	 */
	public boolean writeBit(PlcMemoryMapItem field, IPlcSocketDevice finsPlcDevice) {
		return finsPlcDevice.writeMemory(getFinsMemory(field), new StringBuilder(field.getValue().trim()));
	}

	private PlcMemory getFinsMemory(PlcMemoryMapItem field) {
		IMemoryLoc memoryLoc = Enum.valueOf(RegisterLoc.class, field.getMemoryBank().trim());
		int register = new Integer(field.getStartAddress().trim());
		
		PlcMemory memory = new PlcMemory(memoryLoc, register);
		if (field.getBitIndex() != null)
			memory.setBitAddress(field.getBitIndex());
		return memory;
	}
}
