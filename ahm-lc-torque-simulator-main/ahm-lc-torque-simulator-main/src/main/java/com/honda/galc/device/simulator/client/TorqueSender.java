package com.honda.galc.device.simulator.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.honda.galc.data.DataContainer;
import com.honda.galc.device.simulator.torque.OPMessageField;
import com.honda.galc.device.simulator.torque.Torque;
import com.honda.galc.device.simulator.torque.VirtualTorqueDevice;

/**
 * 
 * <h3>TorqueSender</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> TorqueSender description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>May 25, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since May 25, 2012
 */
public class TorqueSender {
	static Map<String, String> tagMap = new HashMap<String, String>();
	public static Map<String,Integer> tighteningIdMap = new HashMap<String,Integer>();
	static {
		tagMap.put("PEAK_TORQUE", "TORQUE");
		tagMap.put("PEAK_TORQUE_STATUS", "TORQUE_STATUS");
		tagMap.put("FINAL_ANGLE", "ANGLE");
		tagMap.put("FINAL_ANGLE_STATUS", "ANGLE_STATUS");
		tagMap.put("PRODUCT_ID", "VIN_NUMBER");
	}
	
	public void send(VirtualTorqueDevice virtualTorqueDevice, DataContainer dc){
		virtualTorqueDevice.sendTorque(createDefaultTorque(dc, virtualTorqueDevice.getDeviceId()));
	}
	
	private Torque createDefaultTorque(DataContainer dc, String deviceId) {
		Torque torque = new Torque();

		if(!tighteningIdMap.containsKey(deviceId)) {
			tighteningIdMap.put(deviceId, 0);		
		}		
		torque.setId(tighteningIdMap.get(deviceId));
		torque.setFields(prepareTorqueFields(dc, deviceId));
		
		return torque;
	}

	private ArrayList<OPMessageField> prepareTorqueFields(DataContainer dc, String deviceId) {
		ArrayList<OPMessageField> fields = new ArrayList<OPMessageField>();
		for(Object key : dc.keySet()){
			String fieldName = tagMap.containsKey(key.toString().trim()) ? tagMap.get(key.toString().trim()) : key.toString().trim();
			String value = dc.get(key).toString();
			
			if(fieldName.equals("ANGLE")) value = String.format("%1$05d",  Math.round(100*Float.parseFloat(value)));
			else if(fieldName.equals("TORQUE")) value = String.format("%1$06d", Math.round(100*Float.parseFloat(value)));
			fields.add(createOPMessageField(fieldName,value));
		}
		fields.add(createOPMessageField("TIGHTENING_ID",String.format("%1$010d", getNextTighteningId(deviceId))));
		return  fields;
	}

	private Integer getNextTighteningId(String deviceId) {
		tighteningIdMap.put(deviceId, tighteningIdMap.get(deviceId) + 1);
		System.out.println("Next tightening id for device " + deviceId + " :: " + tighteningIdMap.get(deviceId));
		return tighteningIdMap.get(deviceId);
	}
	
	private OPMessageField createOPMessageField(String name, String value) {
		OPMessageField  torqueField = new OPMessageField();
		torqueField.setName(name);
		torqueField.setValue(value);
		return torqueField;
	}
}
