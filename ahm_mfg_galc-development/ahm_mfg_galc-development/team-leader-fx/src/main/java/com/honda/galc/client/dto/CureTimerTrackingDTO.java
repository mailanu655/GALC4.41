package com.honda.galc.client.dto;

import com.honda.galc.entity.product.ProductSpec;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CureTimerTrackingDTO {
	/**
	 * An hour, in milliseconds.
	 */
	public static final int HOUR = 3600000;
	/**
	 * A minute, in milliseconds.
	 */
	public static final int MINUTE = 60000;
	/**
	 * A second, in milliseconds.
	 */
	public static final int SECOND = 1000;

	private final ProductSpec productSpec;
	private final long qsdStartTime;
	private final long requiredCureTime;
	private long fipgStartTime;
	private long fipgEndTime;
	private String dateFormat;

	public CureTimerTrackingDTO(final long qsdStartTime, final long requiredCureTime, final long fipgStartTime, final String engineNumber, final ProductSpec productSpec, final String lastFipgProcess, final String dateFormat) {
		this.qsdStartTime = qsdStartTime;
		this.requiredCureTime = requiredCureTime;
		this.fipgStartTime = fipgStartTime;
		this.fipgEndTime = fipgStartTime + requiredCureTime;
		this.productSpec = productSpec;
		this.dateFormat = dateFormat;

		setEngineNumber(engineNumber);
		setLastFipgProcess(lastFipgProcess);
		setCheckInTime(formatAsCheckInTime(qsdStartTime));
		refresh(new java.util.Date().getTime());
	}

	/**
	 * Returns the product spec code for this engine.
	 */
	public ProductSpec getProductSpec() { return productSpec; }
	/**
	 * Returns the QSD start time for this engine.
	 */
	public long getQsdStartTime() { return qsdStartTime; }
	/**
	 * Returns the FIPG start time for this engine.<br>
	 * A negative FIPG start time indicates an invalid cure time status.
	 */
	public long getFipgStartTime() { return fipgStartTime; }
	/**
	 * Sets the FIPG start time for this engine.<br>
	 * Also calculates the FIPG end time based on the FIPG start time and required cure time.<br>
	 * A negative FIPG start time indicates an invalid cure time status.
	 */
	public void setFipgStartTime(final long startTime) {
		this.fipgStartTime = startTime;
		this.fipgEndTime = startTime + this.requiredCureTime;
	}
	/**
	 * Returns the FIPG end time for this engine.
	 */
	public long getFipgEndTime() { return fipgEndTime; }

	private StringProperty checkInTime;
	public void setCheckInTime(String value) { checkInTimeProperty().set(value); }
	public String getCheckInTime() { return checkInTimeProperty().get(); }
	public StringProperty checkInTimeProperty() {
		if (checkInTime == null) checkInTime = new SimpleStringProperty(this, "checkInTime");
		return checkInTime;
	}

	private StringProperty timeInQsd;
	public void setTimeInQsd(String value) { timeInQsdProperty().set(value); }
	public String getTimeInQsd() { return timeInQsdProperty().get(); }
	public StringProperty timeInQsdProperty() {
		if (timeInQsd == null) timeInQsd = new SimpleStringProperty(this, "timeInQsd");
		return timeInQsd;
	}

	private StringProperty engineNumber;
	public void setEngineNumber(String value) { engineNumberProperty().set(value); }
	public String getEngineNumber() { return engineNumberProperty().get(); }
	public StringProperty engineNumberProperty() {
		if (engineNumber == null) engineNumber = new SimpleStringProperty(this, "engineNumber");
		return engineNumber;
	}

	private StringProperty cureTimeRemaining;
	public void setCureTimeRemaining(String value) { cureTimeRemainingProperty().set(value); }
	public String getCureTimeRemaining() { return cureTimeRemainingProperty().get(); }
	public StringProperty cureTimeRemainingProperty() {
		if (cureTimeRemaining == null) cureTimeRemaining = new SimpleStringProperty(this, "cureTimeRemaining");
		return cureTimeRemaining;
	}

	private StringProperty lastFipgProcess;
	public void setLastFipgProcess(String value) { lastFipgProcessProperty().set(value.split("_")[0]); }
	public String getLastFipgProcess() { return lastFipgProcessProperty().get(); }
	public StringProperty lastFipgProcessProperty() {
		if (lastFipgProcess == null) lastFipgProcess = new SimpleStringProperty(this, "lastFipgProcess");
		return lastFipgProcess;
	}

	/**
	 * Refreshes the Elapsed time in QSD and remaining FIPG time fields for the given refresh time.
	 */
	public void refresh(final long refreshTime) {
		final long timeInQsd = refreshTime - this.qsdStartTime;
		final long cureTimeRemaining = this.fipgEndTime - refreshTime;
		setTimeInQsd(formatAsTimeInQsd(timeInQsd));
		setCureTimeRemaining(formatAsCureTimeRemaining(cureTimeRemaining));
	}

	/**
	 * Returns true iff this engine is ready at the given time.
	 */
	public boolean isFipgReady(final long time) {
		return (!isFipgNg() && (this.fipgEndTime - time < 1));
	}

	/**
	 * Returns true iff this engine is NG.
	 */
	public boolean isFipgNg() {
		return !(this.fipgStartTime < 0);
	}

	/**
	 * Refreshes the Elapsed time in QSD and remaining FIPG time fields for the given refresh time.
	 */
	public void refreshTimeInQsd(final long refreshTime) {
		final long timeInQsd = refreshTime - this.qsdStartTime;
		setTimeInQsd(formatAsTimeInQsd(timeInQsd));
	}

	public  String formatAsCheckInTime(final long time) {
		
		return new java.text.SimpleDateFormat(this.dateFormat).format(new java.util.Date(time));
	}

	public static String formatAsTimeInQsd(final long time) {
		StringBuilder timeInQsd = new StringBuilder();
		timeInQsd.append(time/HOUR + "h");
		timeInQsd.append(" ");
		timeInQsd.append((time%HOUR)/MINUTE + "m");
		return timeInQsd.toString();
	}

	private String formatAsCureTimeRemaining(final long time) {
		if (this.fipgStartTime < 0) {
			return "NG";
		}
		if (time < 1) {
			return "READY";
		}
		StringBuilder cureTimeRemaining = new StringBuilder();
		long cureTimeHoursRemaining = time/HOUR;
		long cureTimeMinutesRemaining = ((time%HOUR)/MINUTE + (time%MINUTE > 0 ? 1 : 0));
		if (cureTimeMinutesRemaining == 60) {
			cureTimeHoursRemaining++;
			cureTimeMinutesRemaining = 0;
		}

		cureTimeRemaining.append(cureTimeHoursRemaining + "h");
		cureTimeRemaining.append(" ");
		cureTimeRemaining.append(cureTimeMinutesRemaining + "m");
		return cureTimeRemaining.toString();
	}

	public boolean hasCommonEngineNumber(CureTimerTrackingDTO cureTimerTrackingDTO) {
		if (cureTimerTrackingDTO == null) return false;
		if (this.getEngineNumber() == null) return false;
		return this.getEngineNumber().equals(cureTimerTrackingDTO.getEngineNumber());
	}
}
