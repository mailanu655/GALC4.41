package com.honda.galc.client.device.plc.omron;

import com.honda.galc.client.device.plc.omron.PlcMemory;
import com.honda.galc.device.dataformat.BitArray;

public class FloorStampTest {
	
	private static final String TEST_PLC_IP = "10.204.21.156";
	private static final int TEST_PLC_PORT = 9600;
	
	//private static final String PROD_PLC_IP ="10.204.21.156";
	//private static final int PROD_PLC_PORT = 9600;
	
	private static PlcMemory finsMemory;
	
	public static void main(String[] args) {
		
		FinsSocketPlcDevice plcDevice;
		plcDevice = createPlc(args);
	
		try {
			if (args[1].equals("READ")) {
				finsMemory = new PlcMemory(args[2], Integer.parseInt(args[3]));
				StringBuilder readValue = DeviceUtil.readMemoryLoc(plcDevice, finsMemory);
				System.out.println("Read " + readValue.toString() + " from " + finsMemory.toString());
				String strRead = readValue.toString();
				byte[] bytes = strRead.getBytes();
				StringBuilder binary = new StringBuilder();
				for (byte b : bytes)
				{
					int val = b;
					for (int i = 0; i < 8; i++)
				     {
				        binary.append((val & 128) == 0 ? 0 : 1);
				        val <<= 1;
				     }
				     binary.append(' ');
				  }
				  System.out.println("Read to binary: " + binary);
			}
			else if (args[1].equals("WRITE")){
				finsMemory = new PlcMemory(args[2]);
				boolean writeSuccessful = DeviceUtil.setMemoryLoc(plcDevice, finsMemory, args[3]);
				System.out.println("Writing " + args[3] + " to " + finsMemory.toString());
				System.out.println("Memory write was " + (writeSuccessful? "Successful" : "NOT Successful"));
			}
			else if (args[1].equals("WRITE_BINARY")){
				StringBuilder writeStr = new BitArray(args[3]).getValue();
				finsMemory = new PlcMemory(args[2]);
				boolean writeSuccessful = plcDevice.writeMemory(finsMemory, writeStr);
				System.out.println("Writing " + writeStr + " to " + finsMemory.toString());
				System.out.println("Memory write was " + (writeSuccessful? "Successful" : "NOT Successful"));
			}} catch (Exception ex) {
		} finally {
			plcDevice.deActivate();
		}
		System.exit(0);
	}

	private static FinsSocketPlcDevice createPlc(String[] args) {
		FinsSocketPlcDevice plcDevice;
		//if (args[0].equals("PROD"))
			//plcDevice = new FinsSocketPlcDevice(PROD_PLC_IP, PROD_PLC_PORT);
		//else
			plcDevice = new FinsSocketPlcDevice(TEST_PLC_IP, TEST_PLC_PORT);
		
		plcDevice.activate();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return plcDevice;
	}
}
