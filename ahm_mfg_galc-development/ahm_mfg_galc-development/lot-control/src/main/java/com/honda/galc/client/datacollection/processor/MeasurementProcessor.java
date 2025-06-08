
package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.MeasurementValue;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.MeasurementSpec;

/**
 *
 * @author Zack <br>
 *         Jul 23, 2013
 */
public class MeasurementProcessor extends TorqueProcessor {

	protected static final String PART_SN_MESSAGE_ID = "PART_SN"; //Part Serial Number validation failed.
	
	public MeasurementProcessor(ClientContext context) {
		super(context);
	}

	
	public synchronized boolean execute(MeasurementValue data) {
		Logger.getLogger().debug("MeasurementProcessor :: Enter execute");
		try {
			
			DataCollectionState currentState = (DataCollectionState) getController().getState();
			if (currentState instanceof ProcessPart) {
				getController().getFsm().error(new Message("Unexpected Torque received, waiting for Part Serial number scan"));
				return false;
			} else if (currentState instanceof ProcessProduct) {
				getController().getFsm().error(new Message("Unexpected Torque received, waiting for Product scan"));
				return false;
			}
			
			Logger.getLogger().info("Processing MeasurementValue:" + data.getMeasurementValue());
			confirmManualMeasurementValue(data);
			
			measurement.setMeasurementStatus(MeasurementStatus.OK);
			getController().getFsm().torqueOk(measurement);
			Logger.getLogger().debug("MeasurementProcessor :: Exit execute ok ");
			return true;
		}  catch (TaskException te) {
			Logger.getLogger().warn(this.getClass().getSimpleName() + " " + te.getMessage());
			measurement.setMeasurementStatus(MeasurementStatus.NG);
			getController().getFsm().torqueNg(measurement, PART_SN_MESSAGE_ID, te.getMessage());
		} catch (SystemException se){
			Logger.getLogger().error(se, this.getClass().getSimpleName() + " " + se.getMessage());
			getController().getFsm().error(new Message(PART_SN_MESSAGE_ID, se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + " " + e.toString());
			getController().getFsm().error(new Message("MSG01", e.getMessage()));
		} catch (Throwable t){
			Logger.getLogger().error(t, this.getClass().getSimpleName() + " "+ t.toString());
			getController().getFsm().error(new Message("MSG01", t.getMessage()));
		}
		Logger.getLogger().debug("MeasurementProcessor :: Exit execute ng ");
		return false;
	}
	
	/**
	 * Verify the received torque value 
	 * @param data
	 * @throws TaskException
	 */
	public void confirmManualMeasurementValue(MeasurementValue data) throws TaskException
	{
		measurement = getManualMeasurement(data);
		checkManualMeasurementValue(data);
	}
	
	/**
	 * Get the current torque value 
	 * @param torqueValue
	 * @return
	 * @throws TaskException
	 */
	private Measurement getManualMeasurement(MeasurementValue measurementValue) throws TaskException {
		
		try {
			measurement = new Measurement();
			measurement.setMeasurementAngle(0.0);
			measurement.setMeasurementAngleStatusId(1);
			measurement.setMeasurementValue(measurementValue.getMeasurementValue());
			measurement.setMeasurementValueStatusId(1);
			measurement.setMeasurementStatus(MeasurementStatus.OK);
			MeasurementId id = new MeasurementId();
			id.setProductId(getController().getState().getProductId() == null ? null : getController().getState().getProductId().trim());
			measurement.setId(id);
			measurement.setPartSerialNumber("");
			
		} catch (Exception e) {
			handleException("Failed to get Torque - exception:" + e.getMessage());
		}
		return measurement;
	}
	
	/**
	 * Check torque value is within the defined range.
	 * The preservation of the actual results is done.
	 * @param torque
	 * @return
	 */
	public void checkManualMeasurementValue(MeasurementValue measurement)
	{
		String msgString = null;
		MeasurementSpec measurementSpec = getMeasurementSpec(); 
		
		//check Status
		double value = measurement.getMeasurementValue();
		if ((value < measurementSpec.getMinimumLimit()) || 
				(measurementSpec.getMaximumLimit() < value)) 
		{
			msgString = ("PeakMeasurementValue:" + value + 
			  " is not within range (" + measurementSpec.getMinimumLimit() 
			  + "," + measurementSpec.getMaximumLimit() + ")");
		} 
		
		if(msgString != null) handleException(msgString);
		
	}
}
