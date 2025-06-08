package com.honda.galc.service.recipe;

import java.io.Serializable;
/**
 * 
 * <h3>NGPartInfo</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> NGPartInfo description </p>
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
 * <TD>Jackie</TD>
 * <TD>May 30, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Jackie
 * @since May 30, 2014
 */
public class NGPartInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String partName;
	private String partSerialNumber;
	private int measurementCount;
	private String torque0;
	private String torque1;
	private String torque2;
	private String torque3;
	private String torque4;
	private String torque5;
	private String torque6;
	private String torque7;
	private String torque8;
	private String torque9;
	private String processPointId;
	private String processPointName;

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartSerialNumber() {
		return partSerialNumber;
	}

	public void setPartSerialNumber(String partSerialNumber) {
		this.partSerialNumber = partSerialNumber;
	}

	public int getMeasurementCount() {
		return measurementCount;
	}

	public void setMeasurementCount(int measurementCount) {
		this.measurementCount = measurementCount;
	}

	public String getTorque0() {
		return torque0;
	}

	public void setTorque0(String torgue0) {
		this.torque0 = torgue0;
	}

	public String getTorque1() {
		return torque1;
	}

	public void setTorque1(String torgue1) {
		this.torque1 = torgue1;
	}

	public String getTorque2() {
		return torque2;
	}

	public void setTorque2(String torgue2) {
		this.torque2 = torgue2;
	}

	public String getTorque3() {
		return torque3;
	}

	public void setTorque3(String torgue3) {
		this.torque3 = torgue3;
	}

	public String getTorque4() {
		return torque4;
	}

	public void setTorque4(String torgue4) {
		this.torque4 = torgue4;
	}

	public String getTorque5() {
		return torque5;
	}

	public void setTorque5(String torgue5) {
		this.torque5 = torgue5;
	}

	public String getTorque6() {
		return torque6;
	}

	public void setTorque6(String torgue6) {
		this.torque6 = torgue6;
	}

	public String getTorque7() {
		return torque7;
	}

	public void setTorque7(String torgue7) {
		this.torque7 = torgue7;
	}

	public String getTorque8() {
		return torque8;
	}

	public void setTorque8(String torgue8) {
		this.torque8 = torgue8;
	}

	public String getTorque9() {
		return torque9;
	}

	public void setTorque9(String torgue9) {
		this.torque9 = torgue9;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getProcessPointName() {
		return processPointName;
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}
}
