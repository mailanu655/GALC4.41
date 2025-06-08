package com.honda.galc.client.dc.processor;

import com.honda.galc.client.dc.common.Message;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.MeasurementAngleStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.openprotocol.model.LastTighteningResult;


/**
 * 
 * <h3>TorqueProcessor Class description</h3>
 * <p> TorqueProcessor description </p>
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
 * @author Jeffray Huang<br>
 * Feb 24, 2014
 *
 *
 */
public class MeasurementProcessor extends AbstractDataCollectionProcessor<InputData>implements IMeasurementProcessor{
	
	private MCOperationMeasurement measurementSpec;
	
	public static final String TORQUE_MESSAGE_ID = "TORQUE";//Torque data validation failed.

	public MeasurementProcessor(DataCollectionController controller, MCOperationRevision operation, MCOperationMeasurement measurement) {
		super(controller, operation);
		this.measurementSpec = measurement;
	}

	public MCOperationMeasurement getMeasurementSpec() {
		return measurementSpec;
	}

	public void setMeasurementSpec(MCOperationMeasurement measurement) {
		this.measurementSpec = measurement;
	}

	public boolean execute(InputData data) {
		return (data instanceof LastTighteningResult) ?  processTorque((LastTighteningResult)data) : true;
	}
	
	protected boolean processTorque(LastTighteningResult data) {
		getLogger().debug("TorqueProcessor :: Enter execute");
		Measurement measurement = null;
		try {
			getLogger().info("Processing LastTighteningResult:" + CommonUtil.toString(data));
			measurement = confirmTorqueValue(data);
			
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
	
	/**
	 * Verify the received torque value 
	 * @param data
	 * @throws TaskException
	 */
	protected Measurement confirmTorqueValue(LastTighteningResult lastTighteningResult) throws TaskException
	{
		validateTighteningId(lastTighteningResult);
		Measurement measurement = getMeasurement(lastTighteningResult);
		checkTorqueData(measurement);
		checkTorqueValue(measurement);
		return measurement;
	}
	
	private void validateTighteningId(LastTighteningResult lastTighteningResult) {
		
		
	}
	
	private void checkTorqueData(Measurement torque) {
		
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
		torque.setMeasurementValue(torque.getMeasurementValue());

		//check Status
		if(torque.getMeasurementAngleStatus() != MeasurementAngleStatus.OK) {
			msgString = ("FinalAngleStatus:" + torque.getMeasurementAngleStatus());
		} else if (torque.getMeasurementValueStatus() != MeasurementStatus.OK) {
			msgString = ("PeakTorqueStatus:" + torque.getMeasurementValueStatus());
		} else if ((torque.getMeasurementValue()  < measurementSpec.getMinLimit()) || 
				(measurementSpec.getMaxLimit() < torque.getMeasurementValue())) 
		{
			msgString = ("PeakTorqueValue:" + torque.getMeasurementValue() + 
					" is not within range (" + measurementSpec.getMinLimit() + "," + measurementSpec.getMaxLimit() + ")");
		} 

		if (torque.getLastTighteningStatus() != MeasurementStatus.OK) {
			//keep the msg string if it already set
			if (msgString == null) {
				msgString = ("OverallTighteningStatus:" + torque.getMeasurementStatus());
			}		
		}

		if(msgString != null) handleException(msgString);

	}
	
	/**
	 * Get the current torque value from data container
	 * @param lastTighteningResult
	 * @return
	 * @throws TaskException
	 */
	private Measurement getMeasurement(LastTighteningResult lastTighteningResult) throws TaskException {

		Measurement measurement = new Measurement();
		try {
			
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


	public void init() {
		// TODO Auto-generated method stub
		
	}

	public IDeviceData processReceived(InputData deviceData) {
		// TODO Auto-generated method stub
		return null;
	}

	public void registerDeviceListener(DeviceListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void setController(DataCollectionController controller) {
		// TODO Auto-generated method stub
		
	}

}
