package com.honda.galc.client.dc.validator;

import com.honda.galc.device.dataformat.MeasurementValue;
import com.honda.galc.entity.conf.MCOperationMeasurement;

/**
 * @author Subu Kathiresan
 * @date Jun 23, 2014
 */
public class MeasurementValueValidator extends AbstractMeasurementInputValidator<MeasurementValue> {

	public MeasurementValueValidator(MeasurementValue measurementValue) {
		super(measurementValue);
	}

	public boolean validate(MeasurementValue measurement, MCOperationMeasurement mSpec) {
		return true;
	}

}
