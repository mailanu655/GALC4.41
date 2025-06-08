package com.honda.galc.client.dc.validator;

import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.enumtype.MeasurementAngleStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.openprotocol.model.LastTighteningResult;

/**
 * @author Subu Kathiresan
 * @date Jun 23, 2014
 */
public class LastTighteningResultValidator extends AbstractMeasurementInputValidator<LastTighteningResult> {

	public LastTighteningResultValidator(LastTighteningResult lastTighteningResult) {
		super(lastTighteningResult);
	}

	public boolean validate(LastTighteningResult lastTighteningResult, MCOperationMeasurement mSpec) {
		Logger.getLogger().debug("LastTighteningResultValidator :: Enter execute");
		Measurement measurement = null;
		try {
			Logger.getLogger().info("Processing LastTighteningResult:" + CommonUtil.toString(inputData));
			measurement = confirmTorqueValue(inputData, mSpec);
			measurement.setMeasurementStatus(MeasurementStatus.OK);
			
			Logger.getLogger().debug("LastTighteningResultValidator :: Exit execute ok ");
			return true;
		} catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + " " + e.toString());
		}
		Logger.getLogger().debug("LastTighteningResultValidator :: Exit execute ng ");
		return false;
	}
	
	/**
	 * Verify the received torque value 
	 * @param data
	 * @throws TaskException
	 */
	protected Measurement confirmTorqueValue(LastTighteningResult lastTighteningResult, MCOperationMeasurement mSpec) throws TaskException {
		Measurement measurement = getMeasurement(lastTighteningResult);
		checkTorqueValue(measurement, mSpec);
		return measurement;
	}

	/**
	 * Check torque value is within the defined range.
	 * The preservation of the actual results is done.
	 * @param torque
	 * @return
	 */
	public void checkTorqueValue(Measurement torque, MCOperationMeasurement mSpec) {
		
		String msgString = null;
		torque.setMeasurementValue(torque.getMeasurementValue());

		//check Status
		if(torque.getMeasurementAngleStatus() != MeasurementAngleStatus.OK) {
			msgString = ("FinalAngleStatus:" + torque.getMeasurementAngleStatus());
		} else if (torque.getMeasurementValueStatus() != MeasurementStatus.OK) {
			msgString = ("PeakTorqueStatus:" + torque.getMeasurementValueStatus());
		} else if ((torque.getMeasurementValue()  < mSpec.getMinLimit()) || 
				(mSpec.getMaxLimit() < torque.getMeasurementValue())) 
		{
			msgString = ("PeakTorqueValue:" + torque.getMeasurementValue() + 
					" is not within range (" + mSpec.getMinLimit() + "," + mSpec.getMaxLimit() + ")");
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
}
