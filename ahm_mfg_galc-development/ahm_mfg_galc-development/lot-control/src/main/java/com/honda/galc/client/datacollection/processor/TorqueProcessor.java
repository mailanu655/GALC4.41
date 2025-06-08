package com.honda.galc.client.datacollection.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.lotcontrol.ITorqueDevice;
import com.honda.galc.client.device.lotcontrol.ITorqueDeviceListener;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.PlcDataString;
import com.honda.galc.entity.MeasurementList;
import com.honda.galc.entity.enumtype.MeasurementAngleStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.openprotocol.model.LastTighteningResult;

/**
 * <h3>TorqueProcessor</h3>
 * <h4>
 * Torque collection processor - used to verify torque value from torque device
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
public class TorqueProcessor extends ProcessorBase 
implements ITorqueProcessor{
	Map<String, Long> tighteningIds = new HashMap<String, Long>();
	Measurement measurement;
	public static final String TORQUE_MESSAGE_ID = "TORQUE";//Torque data validation failed.

	public TorqueProcessor(ClientContext context) {
		super(context);
		
		init();
	}

	public void init() {
		//clean up data for previous torque
		measurement = null;
	}

	public void registerDeviceListener(DeviceListener listener) {
		if(context.getProperty().isTorqueDataFromDeviceDriver())
			registerTorqueDevices((ITorqueDeviceListener)listener);
		else
			registerListener(EiDevice.NAME, listener, getProcessData());
	}

	private void registerTorqueDevices(ITorqueDeviceListener listener) {
		for(IDevice device: DeviceManager.getInstance().getDevices().values()){
			if(device instanceof ITorqueDevice){
				((ITorqueDevice)device).registerListener(listener);
				((ITorqueDevice)device).requestControl(listener);
			} 
		}
	}

	public List<IDeviceData> getProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new LastTighteningResult());
		list.add(new PlcDataString());
		return list;
	}

	public synchronized boolean execute(LastTighteningResult data) {
		Logger.getLogger().debug("TorqueProcessor :: Enter execute");
		try {
			Logger.getLogger().info("Processing LastTighteningResult:" + CommonUtil.toString(data));
			confirmTorqueValue(data);
			
			measurement.setMeasurementStatus(MeasurementStatus.OK);
			getController().getFsm().torqueOk(measurement);
			Logger.getLogger().debug("TorqueProcessor :: Exit execute ok ");
			return true;
		}  catch (TaskException te) {
			Logger.getLogger().warn(this.getClass().getSimpleName() + " " + te.getMessage());
			measurement.setMeasurementStatus(MeasurementStatus.NG);
			getController().getFsm().torqueNg(measurement, TORQUE_MESSAGE_ID, te.getMessage());
		} catch (SystemException se){
			Logger.getLogger().error(se, this.getClass().getSimpleName() + " " + se.getMessage());
			getController().getFsm().error(new Message(TORQUE_MESSAGE_ID, se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + " " + e.toString());
			getController().getFsm().error(new Message("MSG01", e.getMessage()));
		} catch (Throwable t){
			Logger.getLogger().error(t, this.getClass().getSimpleName() + " "+ t.toString());
			getController().getFsm().error(new Message("MSG01", t.getMessage()));
		}
		Logger.getLogger().debug("TorqueProcessor :: Exit execute ng ");
		return false;
	}
	
	public synchronized boolean execute(MeasurementList torqList) {
		Logger.getLogger().debug("TorqueProcessor :: Enter execute torque list");
		try {
			
			if(torqList == null || torqList.getMeasurements() == null || torqList.getMeasurements().size()==0) {
				Logger.getLogger().warn("Processing Measurement List: invalid measurement list");
				throw new Exception("Invalid measurement list.");
			}
			Logger.getLogger().info("Processing Measurement List:" + getLogString(torqList)); 
			boolean overallStatus = validateMeasurementList(torqList);
			
			if(overallStatus) {
				for(Measurement torq : torqList.getMeasurements()) {
					measurement = torq;
					getController().getFsm().torqueOk(measurement);
				}
				
			} else {
				getController().getState().getCurrentInstallPart().getMeasurements().clear();
				for(int i =0; i < (torqList.getMeasurements().size() -1); i++) {
					
					getController().getState().getCurrentInstallPart().getMeasurements().add(torqList.getMeasurements().get(i));
				}
				measurement = torqList.getMeasurements().get(torqList.getMeasurements().size() -1);
				getController().getFsm().torqueNg(measurement,TORQUE_MESSAGE_ID,"PLC forces NG");
				
			}
			
			Logger.getLogger().debug("TorqueProcessor :: Exit execute ok ");
			return overallStatus;
		}  catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + " " + e.toString());
			getController().getFsm().error(new Message("MSG01", e.getMessage()));
		} catch (Throwable t){
			Logger.getLogger().error(t, this.getClass().getSimpleName() + " "+ t.toString());
			getController().getFsm().error(new Message("MSG01", t.getMessage()));
		}
		Logger.getLogger().debug("TorqueProcessor :: Exit execute ng ");
		return false;
	}
	
	private String getLogString(MeasurementList torqList) {
		StringBuilder sb = new StringBuilder();
		for(Measurement m : torqList.getMeasurements())
			sb.append("[").append(m.toString()).append("]");
		return sb.toString();
	}

	private boolean validateMeasurementList(MeasurementList torqs) {
		boolean overallStatus = true;
		for(Measurement torq: torqs.getMeasurements()) {
			try {
				checkTorqueData(torq);
				checkPlcForceValue(torq);
				torq.setMeasurementStatus(MeasurementStatus.OK);
			} catch (Exception e) {
				overallStatus = false;
				torq.setMeasurementStatus(MeasurementStatus.NG);
			}
			
		} 
		return overallStatus;
		
	}

	public String getUnexpectedResultMessage(IDeviceData deviceData){
		return "Unexpected " + deviceData.getClass().getSimpleName() + " received, waiting for torque";
	}

	/**
	 * Verify the received torque value 
	 * @param data
	 * @throws TaskException
	 */
	public void confirmTorqueValue(LastTighteningResult lastTighteningResult) throws TaskException	{
		measurement = getMeasurement(lastTighteningResult);
		if(context.getProperty().isCheckTighteningId()) {
			validateTighteningId(lastTighteningResult);
		}		
		checkTorqueData(measurement);
		checkTorqueValue(measurement);
	}

	protected String getDeviceIdFromRule() {
		String devid = getController().getCurrentLotControlRule().getDeviceId();
		if(devid == null || devid.trim().length() == 0)
			return null;
		else
			return devid;
	}

	protected void validateTighteningId(LastTighteningResult lastTighteningResult) {
		String deviceIdFromRule = getDeviceIdFromRule();
		if(deviceIdFromRule != null && !lastTighteningResult.getDeviceId().equals(deviceIdFromRule)) {
			String userMsg = "Received Torque from unexpected device : " + lastTighteningResult.getDeviceId();
			handleException(userMsg); 
		}
		if(!isValidTighteningId(lastTighteningResult)) {
			String userMsg = "Tightening Id: " + lastTighteningResult.getTighteningId() + " was processed already";
			handleException(userMsg);
		}
	}

	private void checkTorqueData(Measurement torque) {
		// validate product Id
		String currentProductId = getController().getState().getProductId();
		if (context.getProperty().isCheckTorqueProductId() && !torque.getId().getProductId().equals(currentProductId)) {
			String userMsg = "Torque of " + torque.getId().getProductId() + " are not for current product: "
					+ currentProductId;
			handleException(userMsg);
		}
	}

	private boolean isValidTighteningId(LastTighteningResult lastTighteningResult) {
		if (isNewTighteningIdValid(lastTighteningResult)) {
			tighteningIds.put(lastTighteningResult.getDeviceId(), Long.parseLong(lastTighteningResult.getTighteningId()));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check if the current tightening Id is valid by comparing to old id
	 * @param lastTighteningResult
	 * @return
	 */
	private boolean isNewTighteningIdValid(LastTighteningResult lastTighteningResult) {
		if(tighteningIds.get(lastTighteningResult.getDeviceId()) == null) return true;
		
		// Tightening ID must greater than previous value
		return Long.parseLong(lastTighteningResult.getTighteningId()) > tighteningIds.get(lastTighteningResult.getDeviceId());
	}


	/**
	 * Get the current torque value from data container
	 * @param lastTighteningResult
	 * @return
	 * @throws TaskException
	 */
	private Measurement getMeasurement(LastTighteningResult lastTighteningResult) throws TaskException {

		try {
			measurement = new Measurement();
			measurement.setLastTighteningStatusId(lastTighteningResult.getTighteningStatus());
			measurement.setMeasurementAngle(lastTighteningResult.getAngle());
			measurement.setMeasurementAngleStatusId(lastTighteningResult.getAngleStatus());
			measurement.setMeasurementValue(lastTighteningResult.getTorque());
			measurement.setMeasurementValueStatusId(lastTighteningResult.getTorqueStatus());
			measurement.setMeasurementStatus(MeasurementStatus.OK);
			MeasurementId id = new MeasurementId();
			id.setProductId(lastTighteningResult.getProductId() == null ? null : lastTighteningResult.getProductId().trim());
			measurement.setId(id);
			measurement.setPartSerialNumber("");
			
		} catch (Exception e) {
			handleException("Failed to get Torque - exception:" + e.getMessage());
		}
		return measurement;
	}


	protected void handleException(String info) {
		throw new TaskException(info, this.getClass().getSimpleName());
		
	}

	/**
	 * Check torque value is within the defined range.
	 * The preservation of the actual results is done.
	 * @param torque
	 * @return
	 */
	public void checkTorqueValue(Measurement torque)
	{
		String msgString = null;
		MeasurementSpec measurementSpec = getMeasurementSpec(); 
		torque.setMeasurementValue(torque.getMeasurementValue());

		//check Status
		if(torque.getMeasurementAngleStatus() != MeasurementAngleStatus.OK) {
			msgString = ("FinalAngleStatus:" + torque.getMeasurementAngleStatus());
		} else if (torque.getMeasurementValueStatus() != MeasurementStatus.OK) {
			msgString = ("PeakTorqueStatus:" + torque.getMeasurementValueStatus());
		} else if ((torque.getMeasurementValue()  < measurementSpec.getMinimumLimit()) || 
				(measurementSpec.getMaximumLimit() < torque.getMeasurementValue())) 
		{
			msgString = ("PeakTorqueValue:" + torque.getMeasurementValue() + 
					" is not within range (" + measurementSpec.getMinimumLimit() + "," + measurementSpec.getMaximumLimit() + ")");
		} 

		if (torque.getLastTighteningStatus() != MeasurementStatus.OK) {
			//keep the msg string if it already set
			if (msgString == null) {
				msgString = ("OverallTighteningStatus:" + torque.getLastTighteningStatus());
			}		
		}

		if(msgString != null) handleException(msgString);

	}
	
	public void checkPlcForceValue(Measurement torque)
	{
		String msgString = null;
		MeasurementSpec measurementSpec = getMeasurementSpec(); 
		torque.setMeasurementValue(torque.getMeasurementValue());

		//check Status
		if (torque.getMeasurementStatus() != null && torque.getMeasurementStatus() != MeasurementStatus.OK) {
			msgString = ("measurement status:" + torque.getMeasurementValueStatus());
		} else if ((torque.getMeasurementValue()  < measurementSpec.getMinimumLimit()) || 
				(measurementSpec.getMaximumLimit() < torque.getMeasurementValue())) 
		{
			msgString = ("measurement value:" + torque.getMeasurementValue() + 
					" is not within range (" + measurementSpec.getMinimumLimit() + "," + measurementSpec.getMaximumLimit() + ")");
		} 

		if(msgString != null) handleException(msgString);

	}

	/**
	 * Get the maximum and minimum value of the current torque from lot control rule
	 */
	protected MeasurementSpec getMeasurementSpec() {
		List<PartSpec> partInfo = getController().getState().getCurrentLotControlRulePartList();
		int partIndex = getController().getState().getCurrentPartIndex();
		partIndex = getController().getState().getCurrentInstallPart().getPartIndex();
		
		if(partInfo.get(partIndex).getMeasurementCount() <= getController().getState().getCurrentTorqueIndex()){
			Logger.getLogger().error("Measurement count:" + partInfo.get(partIndex).getMeasurementCount() +
					"is less than current torque index:" + getController().getState().getCurrentTorqueIndex());
		}
		return partInfo.get(partIndex).getMeasurementSpecs().get(getController().getState().getCurrentTorqueIndex());
	}

	public boolean execute(InputData data) {
		return (data instanceof LastTighteningResult) ?  execute((LastTighteningResult)data) : true;
	}
}
