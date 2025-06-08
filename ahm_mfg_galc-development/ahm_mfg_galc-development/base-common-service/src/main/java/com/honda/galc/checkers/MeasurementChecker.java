package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.device.dataformat.MeasurementValue;

/**
 * @author Subu Kathiresan
 * @date May 5, 2015
 */
public class MeasurementChecker<T extends MeasurementValue> extends AbstractBaseChecker<T> {

	public MeasurementChecker() {}
	
	public String getName() {
		return this.getClass().getSimpleName();
	}

	public CheckerType getType() {
		return CheckerType.Measurement;
	}

	public int getSequence() {
		return 0;
	}
	
	public List<CheckResult> executeCheck(MeasurementValue measurementValue) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		if (isMeasurementOutsideLimits(measurementValue)) {
			CheckResult checkResult = new CheckResult();
			checkResult.setCheckMessage("Measurement value " + measurementValue.getMeasurementValue() + " not within limits "					
					+ getMeasurement().getMinLimit() 
					+ " - "
					+ getMeasurement().getMaxLimit()
					+ ". Attempt "
					+ measurementValue.getAttemptNumber()
					+ " of "
					+ getMeasurement().getMaxAttempts());
			checkResult.setReactionType(getReactionType());
			checkResults.add(checkResult);
			return checkResults;
		}
		return checkResults;
	}
	
	public boolean isMeasurementOutsideLimits(MeasurementValue meas) {
		return (getMeasurement().getMinLimit() > meas.getMeasurementValue() ||
			getMeasurement().getMaxLimit() < meas.getMeasurementValue());
	}
}
