package com.honda.galc.data;

import java.io.Serializable;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>LotControlRuleInfo</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jackie
 */
public class LotControlRuleInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String partId;
	private String partName;
	private int serialNumberScanFlag;
	private int serialNumberUniqueFlag;
	private int sequenceNumber;
	private int measurementCount;
	private String partSerialNumberMask;
	private int partMaxAttempts;
	private String torqueMin0;
	private String torqueMin1;
	private String torqueMin2;
	private String torqueMin3;
	private String torqueMin4;
	private String torqueMin5;
	private String torqueMin6;
	private String torqueMin7;
	private String torqueMin8;
	private String torqueMin9;
	
	private String torqueMax0;
	private String torqueMax1;
	private String torqueMax2;
	private String torqueMax3;
	private String torqueMax4;
	private String torqueMax5;
	private String torqueMax6;
	private String torqueMax7;
	private String torqueMax8;
	private String torqueMax9;
	
	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public int getSerialNumberScanFlag() {
		return serialNumberScanFlag;
	}

	public void setSerialNumberScanFlag(int serialNumberScanFlag) {
		this.serialNumberScanFlag = serialNumberScanFlag;
	}

	public int getSerialNumberUniqueFlag() {
		return serialNumberUniqueFlag;
	}

	public void setSerialNumberUniqueFlag(int serialNumberUniqueFlag) {
		this.serialNumberUniqueFlag = serialNumberUniqueFlag;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getMeasurementCount() {
		return measurementCount;
	}

	public void setMeasurementCount(int measurementCount) {
		this.measurementCount = measurementCount;
	}

	public String getPartSerialNumberMask() {
		return partSerialNumberMask;
	}

	public void setPartSerialNumberMask(String partSerialNumberMask) {
		this.partSerialNumberMask = partSerialNumberMask;
	}

	public int getPartMaxAttempts() {
		return partMaxAttempts;
	}

	public void setPartMaxAttempts(int partMaxAttempts) {
		this.partMaxAttempts = partMaxAttempts;
	}

	public String getTorqueMin0() {
		return torqueMin0;
	}

	public void setTorqueMin0(String torgueMin0) {
		this.torqueMin0 = torgueMin0;
	}

	public String getTorqueMin1() {
		return torqueMin1;
	}

	public void setTorqueMin1(String torgueMin1) {
		this.torqueMin1 = torgueMin1;
	}

	public String getTorqueMin2() {
		return torqueMin2;
	}

	public void setTorqueMin2(String torgueMin2) {
		this.torqueMin2 = torgueMin2;
	}

	public String getTorqueMin3() {
		return torqueMin3;
	}

	public void setTorqueMin3(String torgueMin3) {
		this.torqueMin3 = torgueMin3;
	}

	public String getTorqueMin4() {
		return torqueMin4;
	}

	public void setTorqueMin4(String torgueMin4) {
		this.torqueMin4 = torgueMin4;
	}

	public String getTorqueMin5() {
		return torqueMin5;
	}

	public void setTorqueMin5(String torgueMin5) {
		this.torqueMin5 = torgueMin5;
	}

	public String getTorqueMin6() {
		return torqueMin6;
	}

	public void setTorqueMin6(String torgueMin6) {
		this.torqueMin6 = torgueMin6;
	}

	public String getTorqueMin7() {
		return torqueMin7;
	}

	public void setTorqueMin7(String torgueMin7) {
		this.torqueMin7 = torgueMin7;
	}

	public String getTorqueMin8() {
		return torqueMin8;
	}

	public void setTorqueMin8(String torgueMin8) {
		this.torqueMin8 = torgueMin8;
	}

	public String getTorqueMin9() {
		return torqueMin9;
	}

	public void setTorqueMin9(String torgueMin9) {
		this.torqueMin9 = torgueMin9;
	}

	public String getTorqueMax0() {
		return torqueMax0;
	}

	public void setTorqueMax0(String torgueMax0) {
		this.torqueMax0 = torgueMax0;
	}

	public String getTorqueMax1() {
		return torqueMax1;
	}

	public void setTorqueMax1(String torgueMax1) {
		this.torqueMax1 = torgueMax1;
	}

	public String getTorqueMax2() {
		return torqueMax2;
	}

	public void setTorqueMax2(String torgueMax2) {
		this.torqueMax2 = torgueMax2;
	}

	public String getTorqueMax3() {
		return torqueMax3;
	}

	public void setTorqueMax3(String torgueMax3) {
		this.torqueMax3 = torgueMax3;
	}

	public String getTorqueMax4() {
		return torqueMax4;
	}

	public void setTorqueMax4(String torgueMax4) {
		this.torqueMax4 = torgueMax4;
	}

	public String getTorqueMax5() {
		return torqueMax5;
	}

	public void setTorqueMax5(String torgueMax5) {
		this.torqueMax5 = torgueMax5;
	}

	public String getTorqueMax6() {
		return torqueMax6;
	}

	public void setTorqueMax6(String torgueMax6) {
		this.torqueMax6 = torgueMax6;
	}

	public String getTorqueMax7() {
		return torqueMax7;
	}

	public void setTorqueMax7(String torgueMax7) {
		this.torqueMax7 = torgueMax7;
	}

	public String getTorqueMax8() {
		return torqueMax8;
	}

	public void setTorqueMax8(String torgueMax8) {
		this.torqueMax8 = torgueMax8;
	}

	public String getTorqueMax9() {
		return torqueMax9;
	}

	public void setTorqueMax9(String torgueMax9) {
		this.torqueMax9 = torgueMax9;
	}

}
