package com.honda.galc.device.dataformat;

import java.util.List;

/**
 * @author Subu Kathiresan
 * @date Sep 24, 2014
 */
public class MeasurementInputData extends InputData {

	private int measurementIndex;
	private int attemptNumber = -1;
	private List<Integer> installedMeasurementSequenceNumbers;

	public MeasurementInputData() {}
	
	public MeasurementInputData(int index) {
		this.measurementIndex = index;
	}
	
	public int getMeasurementIndex() {
		return measurementIndex;
	}

	public void setMeasurementIndex(int measurementIndex) {
		this.measurementIndex = measurementIndex;
	}

	public int getAttemptNumber() {
		return attemptNumber;
	}

	public void setAttemptNumber(int attemptNumber) {
		this.attemptNumber = attemptNumber;
	}

	public List<Integer> getInstalledMeasurementSequenceNumbers() {
		return installedMeasurementSequenceNumbers;
	}

	public void setInstalledMeasurementSequenceNumbers(List<Integer> installedMeasurementSequenceNumbers) {
		this.installedMeasurementSequenceNumbers = installedMeasurementSequenceNumbers;
	}

}
