package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.honda.galc.device.dataformat.MeasurementValue;

public class MeasurementSequenceNumberChecker<T extends MeasurementValue> extends AbstractBaseChecker<T> {
	
	private static ReentrantLock reEntrantLock = new ReentrantLock(true);

	public MeasurementSequenceNumberChecker() {	}
	
	public MeasurementSequenceNumberChecker(T MeasurementValue) {
		inputData = MeasurementValue;
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

	public List<CheckResult> executeCheck(T measurementValue) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		reEntrantLock.lock();
		try{
			if (!isMeasurementSequenceNumberOk(measurementValue)) {
				CheckResult checkResult = new CheckResult();
				checkResult.setCheckMessage("Another valid torque received for sequence number " + inputData.getMeasurementIndex());
				checkResult.setReactionType(getReactionType());
				checkResults.add(checkResult);
			}
			return checkResults;
		}finally{
			reEntrantLock.unlock();
		}
	}
	
	public boolean isMeasurementSequenceNumberOk(T measurementValue) {
		if(measurementValue != null 
				&& measurementValue.getInstalledMeasurementSequenceNumbers() != null) {
			for(int measSeqNo: measurementValue.getInstalledMeasurementSequenceNumbers()) {
				if(measSeqNo == measurementValue.getMeasurementIndex()) {
					//Good Measurement for this sequence number has already been collected
					return false;
				}
			}
		}
		return true;
	}
}
