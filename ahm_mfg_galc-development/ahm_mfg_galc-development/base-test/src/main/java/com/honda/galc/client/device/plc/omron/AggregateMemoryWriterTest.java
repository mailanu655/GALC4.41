/**
 * 
 */
package com.honda.galc.client.device.plc.omron;

import org.junit.Test;

import com.honda.galc.client.device.plc.PlcDataCollectionBean;
import com.honda.galc.entity.enumtype.DeviceTagType;

/**
 * @author Subu Kathiresan
 * @date Dec 11, 2012
 */
public class AggregateMemoryWriterTest {
	
	@Test 
	public void flushTest() {
		AggregateMemoryWriter writer = new AggregateMemoryWriter(new PlcDataCollectionBean());
		PlcDataField dataField1 = new PlcDataField("test1", new StringBuilder("mathu"), DeviceTagType.PLC_WRITE);
		dataField1.setPlcMemory(new PlcMemory("E3.509.H"));
		writer.addItem(dataField1, "deviceId");
		
		PlcDataField dataField2 = new PlcDataField("test2", new StringBuilder("ambaat"), DeviceTagType.PLC_WRITE);
		dataField2.setPlcMemory(new PlcMemory("E3.511.L"));
		writer.addItem(dataField2, "deviceId");
		
		PlcDataField dataField3 = new PlcDataField("test3", new StringBuilder("mathua"), DeviceTagType.PLC_WRITE);
		dataField3.setPlcMemory(new PlcMemory("E3.529.H"));
		writer.addItem(dataField3, "deviceId");
		
		PlcDataField dataField4 = new PlcDataField("test4", new StringBuilder("mbaat"), DeviceTagType.PLC_WRITE);
		dataField4.setPlcMemory(new PlcMemory("E3.532.H"));
		writer.addItem(dataField4, "deviceId");
		
		PlcDataField dataField5 = new PlcDataField("test5", new StringBuilder("mbaat"), DeviceTagType.PLC_WRITE);
		dataField5.setPlcMemory(new PlcMemory("E3.540.H"));
		writer.addItem(dataField5, "deviceId");
		
		PlcDataField dataField6 = new PlcDataField("test6", new StringBuilder("raghu"), DeviceTagType.PLC_WRITE);
		dataField6.setPlcMemory(new PlcMemory("DM.529.H"));
		writer.addItem(dataField6, "deviceId");
		
		PlcDataField dataField7 = new PlcDataField("test7", new StringBuilder("varan"), DeviceTagType.PLC_WRITE);
		dataField7.setPlcMemory(new PlcMemory("DM.532.H"));
		writer.addItem(dataField7, "deviceId");
		
		PlcDataField dataField8 = new PlcDataField("test8", new StringBuilder("raghu"), DeviceTagType.PLC_WRITE);
		dataField8.setPlcMemory(new PlcMemory("DM.540.H"));
		writer.addItem(dataField8, "deviceId");
		
		PlcDataField dataField9 = new PlcDataField("test9", new StringBuilder("varan"), DeviceTagType.PLC_WRITE);
		dataField9.setPlcMemory(new PlcMemory("DM.542.L"));
		writer.addItem(dataField9, "deviceId");
		
		writer.flush("hello");
	}
}
