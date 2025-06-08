package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.device.dataformat.Torque;

/**
 * @author Subu Kathiresan
 * @date Dec 5, 2014
 */
public class TorqueMeasurementChecker extends MeasurementChecker<Torque> {

	public TorqueMeasurementChecker() {	}
	
	public TorqueMeasurementChecker(Torque torque) {
		inputData = torque;
	}
	
	public String getName() {
		return this.getClass().getSimpleName();
	}

	public CheckerType getType() {
		return CheckerType.Measurement;
	}

	public int getSequence() {
		return 0;
	}

	public List<CheckResult> executeCheck(Torque torque) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		
		if (isMeasurementOutsideLimits(torque)) {
			CheckResult checkResult = new CheckResult();
			checkResult.setCheckMessage("Torque value " + torque.getMeasurementValue() + " not within limits "					
					+ getMeasurement().getMinLimit() 
					+ " - "
					+ getMeasurement().getMaxLimit()
					+ ". Attempt "
					+ torque.getAttemptNumber()
					+ " of "
					+ getMeasurement().getMaxAttempts());
			checkResult.setReactionType(getReactionType());
			checkResults.add(checkResult);
			return checkResults;
		}

		if (!isTighteningStatusOk(torque)) {
			CheckResult checkResult = new CheckResult();
			checkResult.setCheckMessage("Invalid Tightening status received");
			checkResult.setReactionType(getReactionType());
			checkResults.add(checkResult);
			return checkResults;
		}
		return checkResults;
	}
	
	public boolean isTighteningStatusOk(Torque torque) {
		return (torque.getTorqueStatus() == 1) &&
				(torque.getAngleStatus() == 1)  &&
				(torque.getTighteningStatus() == 1);
	}
}
