/**
 * 
 */
package com.honda.galc.client.events;

/**
 * @author Subu Kathiresan
 * Oct 18, 2011
 */
public class FloorStampResultVerificationRequest extends AbstractPlcDataReadyEvent {

	private String _month = "";
	private String _day = "";
	private String _year = "";
	private String _hour = "";
	private String _minute = "";
	private String _seconds = "";
	private String _bodyIndicator = "";
	private String _tagResult = "";
	private String _readerResult = "";
	private String _stampedVin = "";
	private String _tagNumber = "";
	private String _tagWriteCount = "";
	private String _bodyCount = "";
	private String _controlFlag = "";
	private String _frameCode = "";
	private String _rfidVin = "";
	private String _prodLotKd = "";
	private String _prodLotKdQty = "";
	private String _model = "";
	private String _type = "";
	private String _option = "";
	private String _intColor = "";
	private String _extColor = "";
	private String _repairLevel = "";
	private String _rfidSpaceFill = "";
	
	public FloorStampResultVerificationRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}

	public String getMonth() {
		return _month;
	}

	public void setMonth(String month) {
		_month = month;
	}

	public String getDay() {
		return _day;
	}

	public void setDay(String day) {
		_day = day;
	}

	public String getYear() {
		return _year;
	}

	public void setYear(String year) {
		_year = year;
	}

	public String getHour() {
		return _hour;
	}

	public void setHour(String hour) {
		_hour = hour;
	}
	
	public String getMinute() {
		return _minute;
	}

	public void setMinute(String minute) {
		_minute = minute;
	}

	public String getSeconds() {
		return _seconds;
	}

	public void setSeconds(String seconds) {
		_seconds = seconds;
	}

	public String getBodyIndicator() {
		return _bodyIndicator;
	}

	public void setBodyIndicator(String bodyIndicator) {
		_bodyIndicator = bodyIndicator;
	}

	public String getTagResult() {
		return _tagResult;
	}

	public void setTagResult(String tagResult) {
		_tagResult = tagResult;
	}

	public String getReaderResult() {
		return _readerResult;
	}

	public void setReaderResult(String readerResult) {
		_readerResult = readerResult;
	}

	public String getTagNumber() {
		return _tagNumber;
	}

	public void setTagNumber(String tagNumber) {
		_tagNumber = tagNumber;
	}

	public String getTagWriteCount() {
		return _tagWriteCount;
	}

	public void setTagWriteCount(String tagWriteCount) {
		_tagWriteCount = tagWriteCount;
	}

	public String getBodyCount() {
		return _bodyCount;
	}

	public void setBodyCount(String bodyCount) {
		_bodyCount = bodyCount;
	}

	public String getControlFlag() {
		return _controlFlag;
	}

	public void setControlFlag(String controlFlag) {
		_controlFlag = controlFlag;
	}

	public String getFrameCode() {
		return _frameCode;
	}

	public void setFrameCode(String frameCode) {
		_frameCode = frameCode;
	}

	public String getRfidVin() {
		return _rfidVin;
	}

	public void setRfidVin(String rfidVin) {
		_rfidVin = rfidVin;
	}

	public String getProdLotKd() {
		return _prodLotKd;
	}

	public void setProdLotKd(String prodLotKd) {
		_prodLotKd = prodLotKd;
	}

	public String getProdLotKdQty() {
		return _prodLotKdQty;
	}

	public void setProdLotKdQty(String prodLotKdQty) {
		_prodLotKdQty = prodLotKdQty;
	}

	public String getModel() {
		return _model;
	}

	public void setModel(String model) {
		_model = model;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}

	public String getOption() {
		return _option;
	}

	public void setOption(String option) {
		_option = option;
	}

	public String getIntColor() {
		return _intColor;
	}

	public void setIntColor(String intColor) {
		_intColor = intColor;
	}

	public String getExtColor() {
		return _extColor;
	}

	public void setExtColor(String extColor) {
		_extColor = extColor;
	}

	public String getRepairLevel() {
		return _repairLevel;
	}

	public void setRepairLevel(String repairLevel) {
		_repairLevel = repairLevel;
	}

	public String getRfidSpaceFill() {
		return _rfidSpaceFill;
	}

	public void setRfidSpaceFill(String rfidSpaceFill) {
		_rfidSpaceFill = rfidSpaceFill;
	}
	
	public void setStampedVin(String stampedVin) {
		_stampedVin = stampedVin;
	}

	public String getStampedVin() {
		return _stampedVin;
	}
}
