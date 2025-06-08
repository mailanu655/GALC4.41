package com.honda.galc.client.teamleader.let;

import java.io.Serializable;
/**
 * 
 * <h3>DisplayAudioFeedEntry Class description</h3>
 * <p>
 * DisplayAudioFeedEntry description
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author Haohua Xie<br>
 *         Feb 27, 2014
 * 
 * 
 */
public class DisplayAudioFeedEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	private String audioDwg;
	private String audioSerial;
	private String audioVersion;
	private String colorCode;
	private String modelId;
	private String modelOptionCode;
	private String modelTypeCode;
	private String productId;
	private String testSeq;
	private String testTime;

	public String getAudioDwg() {
		return audioDwg;
	}

	public String getAudioSerial() {
		return audioSerial;
	}

	public String getAudioVersion() {
		return audioVersion;
	}

	public String getColorCode() {
		return colorCode;
	}

	public String getModelId() {
		return modelId;
	}

	public String getModelOptionCode() {
		return modelOptionCode;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	public String getProductId() {
		return productId;
	}

	public String getTestSeq() {
		return testSeq;
	}

	public String getTestTime() {
		return testTime;
	}

	public void setAudioDwg(String audioDwg) {
		this.audioDwg = audioDwg;
	}

	public void setAudioSerial(String audioSerial) {
		this.audioSerial = audioSerial;
	}

	public void setAudioVersion(String audioVersion) {
		this.audioVersion = audioVersion;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public void setProductId(String vin) {
		this.productId = vin;
	}

	public void setTestSeq(String testSeq) {
		this.testSeq = testSeq;
	}

	public void setTestTime(String testTime) {
		this.testTime = testTime;
	}

}
