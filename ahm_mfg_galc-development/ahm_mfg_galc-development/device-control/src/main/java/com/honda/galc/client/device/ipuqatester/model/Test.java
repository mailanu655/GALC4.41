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
@XStreamAlias("TEST")
public class Test {

	@XStreamAlias("Test")
	@XStreamAsAttribute
	private String _id = "";
	
	@XStreamAlias("InCycleRetestNum")
	@XStreamAsAttribute
	private String _inCycleRetestNum = "";
	
	@XStreamAlias("TestTime")
	@XStreamAsAttribute
	private String _testTime = "";
	
	@XStreamAlias("TestEndTime")
	@XStreamAsAttribute
	private String _testEndTime = "";
	
	@XStreamAlias("ProcessInfo")
	@XStreamAsAttribute
	private String _processInfo = "";
	
	@XStreamAlias("Status")
	@XStreamAsAttribute
	private String _status = "";
	
	@XStreamAlias("TEST_PARAMS")
	private ArrayList<TestParam> _testParams = new ArrayList<TestParam>();
	
	@XStreamAlias("TEST_ATTRIBUTES")
	private ArrayList<TestAttrib> _testAttribs = new ArrayList<TestAttrib>();
	
	@XStreamAlias("FAULT_CODES")
	private ArrayList<FaultCode> _faultCodes = new ArrayList<FaultCode>();
	
	/** Need to allow bean to be created via reflection */
	public Test() {}
	
	public String getId() {
		return _id;
	}

	public void setId(String id) {
		_id = id;
	}

	public String _getInCycleRetestNum() {
		return _inCycleRetestNum;
	}

	public void setInCycleRetestNum(String inCycleRetestNum) {
		_inCycleRetestNum = inCycleRetestNum;
	}

	public String _getTestTime() {
		return _testTime;
	}

	public void setTestTime(String testTime) {
		_testTime = testTime;
	}

	public String _getTestEndTime() {
		return _testEndTime;
	}

	public void setTestEndTime(String testEndTime) {
		_testEndTime = testEndTime;
	}

	public String _getProcessInfo() {
		return _processInfo;
	}

	public void setProcessInfo(String processInfo) {
		_processInfo = processInfo;
	}

	public boolean isStatusPass() {
		return _status.equalsIgnoreCase("Pass") ? true : false;
	}

	public void setStatus(String status) {
		_status = status;
	}

	public ArrayList<TestParam> getTestParams() {
		return _testParams;
	}

	public void setTestParams(ArrayList<TestParam> testParams) {
		_testParams = testParams;
	}

	public ArrayList<TestAttrib> getTestAttribs() {
		return _testAttribs;
	}

	public void setTestAttribs(ArrayList<TestAttrib> testAttribs) {
		_testAttribs = testAttribs;
	}
	
	public ArrayList<FaultCode> getFaultCodes() {
		return _faultCodes;
	}

	public void setFaultCodes(ArrayList<FaultCode> faultCodes) {
		_faultCodes = faultCodes;
	}
}
