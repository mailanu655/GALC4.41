package com.honda.galc.device.mitsubishi;

/**
 * 
 * <h3>QnADevice</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QnADevice description </p>
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
 * @author Paul Chou
 * Nov 5, 2010
 *
 */
public enum QnADevice {
	SM("SM",(byte)0x91, true),
	SD("SD",(byte)0xA9, false),
	X("X*",(byte)0x9C, true),
	Y("Y*",(byte)0x9D, true),
	M("M*",(byte)0x90, true),
	L("L*",(byte)0x92, true),
	F("F*",(byte)0x93, true),
	V("V*",(byte)0x94, true),
	B("B*",(byte)0xA0, true),
	D("D*",(byte)0xA8, false),
	W("W*",(byte)0xB4, false),
	TS("TS",(byte)0xC1, true),
	TC("TC",(byte)0xC0, true),
	TN("TN",(byte)0xC2, false),
	SS("SS",(byte)0xC7, true),
	SC("SC",(byte)0xC6, true),
	SN("SN",(byte)0xC8, false),
	CS("CS",(byte)0xC4, true),
	CC("CC",(byte)0xC3, true),
	CN("CN",(byte)0xC5, false),
	SB("SB",(byte)0xA1, true),
	SW("SW",(byte)0xB5, false),
	S("S*",(byte)0x98, true),
	DX("DX",(byte)0xA2, true),
	DY("DY",(byte)0xA3, true),
	Z("Z*",(byte)0xCC, false),
	R("R*",(byte)0xAF, false),
	ZR("ZR",(byte)0xB0, false),
	DSH("D*",(byte)0xA8, false),;
	
	private String asciiCode;
	private byte binaryCode;
	private boolean bitDevice;
	private QnADevice(String asciiCode, byte binaryCode, boolean bitDevice) {
		this.asciiCode = asciiCode;
		this.binaryCode = binaryCode;
		this.bitDevice = bitDevice;
	}
	
	// Getters & Setters
	public String getAsciiCode() {
		return asciiCode;
	}
	public void setAsciiCode(String asciiCode) {
		this.asciiCode = asciiCode;
	}
	public byte getBinaryCode() {
		return binaryCode;
	}
	public void setBinaryCode(byte binaryCode) {
		this.binaryCode = binaryCode;
	}

	public boolean isBitDevice() {
		return bitDevice;
	}

	public void setBitDevice(boolean bitDevice) {
		this.bitDevice = bitDevice;
	}
	
	public static String getDeviceAsciiCodeByCode(byte binaryCode){
		for(QnADevice d : QnADevice.values()){
			if(d.getBinaryCode() == binaryCode)
				return d.getAsciiCode();
		}
		
		//return null if can't find device; TODO log
		return null; 
	}
	
	public static QnADevice getQnADeviceByCode(byte binaryCode){
		for(QnADevice d : QnADevice.values()){
			if(d.getBinaryCode() == binaryCode)
				return d;
		}
		
		//return null if can't find device; TODO log
		return null; 
	}
}
