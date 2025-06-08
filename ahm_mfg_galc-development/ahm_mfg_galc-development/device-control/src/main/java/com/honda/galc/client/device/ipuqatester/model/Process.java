/**
 * 
 */
package com.honda.galc.client.device.ipuqatester.model;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author Subu Kathiresan
 * @date Feb 2, 2012
 */
@XStreamAlias("PROCESS")
public class Process {

	@XStreamAlias("Process")
	@XStreamAsAttribute
	private String _id = "";
	
	@XStreamAlias("StartTimeUTC")
	@XStreamAsAttribute
	private String _startTimeUTC = "";
	
	@XStreamAlias("FinishTime")
	@XStreamAsAttribute
	private String _finishTime = "";
	
	@XStreamAlias("Cal")
	@XStreamAsAttribute
	private String _cal = "";
	
	@XStreamAlias("DCRev")
	@XStreamAsAttribute
	private String _dcRev = "";
	
	@XStreamAlias("SWVer")
	@XStreamAsAttribute
	private String _softwareVersion = "";
	
	@XStreamAlias("Status")
	@XStreamAsAttribute
	private String _status = "";
	
	@XStreamAlias("OperatorID")
	@XStreamAsAttribute
	private String _operatorId = "";
	
	@XStreamAlias("Cell")
	@XStreamAsAttribute
	private String _cell = "";

	@XStreamAlias("TESTS")
	private ArrayList<Test> _tests = new ArrayList<Test>();
	
	/** Need to allow bean to be created via reflection */
	public Process() {}
	
	public String getId() {
		return _id;
	}

	public void setId(String id) {
		_id = id;
	}

	public String getStartTimeUTC() {
		return _startTimeUTC;
	}

	public void setStartTimeUTC(String startTimeUTC) {
		_startTimeUTC = startTimeUTC;
	}

	public String getFinishTime() {
		return _finishTime;
	}

	public void setFinishTime(String finishTime) {
		_finishTime = finishTime;
	}

	public String getCal() {
		return _cal;
	}

	public void setCal(String cal) {
		_cal = cal;
	}

	public String getDcRev() {
		return _dcRev;
	}

	public void setDcRev(String dcRev) {
		_dcRev = dcRev;
	}

	public String getSoftwareVersion() {
		return _softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		_softwareVersion = softwareVersion;
	}

	public boolean isStatusPass() {
		return _status.equalsIgnoreCase("Pass") ? true : false;
	}

	public void setStatus(String status) {
		_status = status;
	}

	public String getOperatorId() {
		return _operatorId;
	}

	public void setOperatorId(String operatorId) {
		_operatorId = operatorId;
	}

	public String getCell() {
		return _cell;
	}

	public void setCell(String cell) {
		_cell = cell;
	}
	
	public void setTests(ArrayList<Test> tests) {
		_tests = tests;
	}

	public ArrayList<Test> getTests() {
		return _tests;
	}
}
