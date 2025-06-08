package com.honda.galc.client.device.plc.omron;
import com.honda.galc.client.device.plc.omron.PlcMemory;
import com.honda.galc.client.device.plc.omron.FinsSocketPlcDevice;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Dec 5, 2011
 */
public class DeviceUtil {
	
	public static long avgWriteTime =  0;
	public static long avgReadTime =  0;
	
	public static boolean setMemoryLoc(FinsSocketPlcDevice finsDevice, PlcMemory PlcMemory, String val) {
		return setMemoryLoc(finsDevice, PlcMemory, val, false);
	}
	
	public static boolean setMemoryLoc(FinsSocketPlcDevice finsDevice, PlcMemory PlcMemory, String val, boolean log) {
		boolean writeSuccess = false;
		
		try {
			long startTime = System.currentTimeMillis();
			writeSuccess = finsDevice.writeMemory(PlcMemory, new StringBuilder(val));
			long endTime = System.currentTimeMillis();
			System.out.println(StringUtil.now("HH:mm:ss:SSS") +  " Time taken to write: " + (endTime - startTime));
			avgWriteTime = (avgWriteTime + endTime - startTime)/2;
			System.out.println(StringUtil.now("HH:mm:ss:SSS") + " Average write time " + avgWriteTime);
			
			if (writeSuccess && log)
				System.out.println(StringUtil.now("HH:mm:ss:SSS") + " Memory location " + PlcMemory.getMemoryLoc().name() + "." + PlcMemory.getRegister() + "   set to '" + val + "'");
		} catch(Exception ex) {
			return false;
		}
		
		return writeSuccess;
	}
	
	public static StringBuilder readMemoryLoc(FinsSocketPlcDevice finsDevice, PlcMemory PlcMemory) {
		return readMemoryLoc(finsDevice, PlcMemory, false);
	}
		
	public static StringBuilder readMemoryLoc(FinsSocketPlcDevice finsDevice, PlcMemory PlcMemory, boolean log) {
		long startTime = System.currentTimeMillis();
		StringBuilder readVal = finsDevice.readMemory(PlcMemory);
		long endTime = System.currentTimeMillis();
		System.out.println(StringUtil.now("HH:mm:ss:SSS") +  " Time taken to read: " + (endTime - startTime));
		avgReadTime = (avgReadTime + endTime - startTime)/2;
		System.out.println(StringUtil.now("HH:mm:ss:SSS") + " Average read time " + avgReadTime);
		
		if (log)
			System.out.println(StringUtil.now("HH:mm:ss:SSS") + " Memory location " + PlcMemory.getMemoryLoc().name() + "." + PlcMemory.getRegister() + " read val '" + readVal + "'");
		return readVal;
	}
}
