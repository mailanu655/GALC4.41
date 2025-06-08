package com.honda.galc.device.dataformat;

/**
 * @author Subu Kathiresan
 * @date Jan 22, 2015
 */
public class DataCollectionIndexData extends InputData {

	private int inputIndex;
	private boolean hasScanPart;
	private boolean exceededMaxAttempts;

	public DataCollectionIndexData() {}
	
	public DataCollectionIndexData(int index) {
		this.inputIndex = index;
	}
	
	public int getInputIndex() {
		return inputIndex;
	}

	public void setInputIndex(int inputIndex) {
		this.inputIndex = inputIndex;
	}

	public boolean hasScanPart() {
		return hasScanPart;
	}

	public void setHasScanPart(boolean hasScanPart) {
		this.hasScanPart = hasScanPart;
	}

	public boolean hasExceededMaxAttempts() {
		return exceededMaxAttempts;
	}

	public void setExceededMaxAttempts(boolean exceededMaxAttempts) {
		this.exceededMaxAttempts = exceededMaxAttempts;
	}
}
