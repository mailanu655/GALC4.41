/**
 * 
 */
package com.honda.galc.client.device.plc;

import java.util.ArrayList;
import java.util.Date;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.PlcBoolean;
import com.honda.galc.util.KeyValue;


/**
 * @author Subu Kathiresan
 * Oct 18, 2011
 */
public interface IPlcDeviceData extends IDeviceData {
	
	public Date getEventTimeStamp();
	
	public IPlcEventGenerator getEventGenerator();
	
	public void setEventGenerator(IPlcEventGenerator eventGenerator);

	public String getPlcDeviceId();

	public void setPlcDeviceId(String plcId);
	
	public String getApplicationId();

	public void setApplicationId(String applicationId);
	
	public void collectDeviceData(String deviceId);
	
	public ArrayList<KeyValue<String, Double>> getMeasurements();
	
	public Double getMeasurement(String measurementName);

	public void setMeasurement(String measurementName, Double measurementVal);
	
	public ArrayList<KeyValue<String, PlcBoolean>> getJudgements();
	
	public PlcBoolean getJudgement(String judgementName);
	
	public void setJudgement(String judgementName, PlcBoolean judgement);
	
	public ArrayList<KeyValue<String, StringBuilder>> getAttributes();
	
	public void setAttribute(String measurementName, StringBuilder attribVal);
	
	public StringBuilder getAttribute(String attributeName);
	
	
}
