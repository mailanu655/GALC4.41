/**
 * 
 */
package com.honda.galc.letxml.model;

import java.util.ArrayList;

import com.honda.galc.let.enums.LetStatus;
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

	public String getInCycleRetestNum() {
		return _inCycleRetestNum;
	}

	public void setInCycleRetestNum(String inCycleRetestNum) {
		_inCycleRetestNum = inCycleRetestNum;
	}

	public String getTestTime() {
		return _testTime;
	}

	public void setTestTime(String testTime) {
		_testTime = testTime;
	}

	public String getTestEndTime() {
		return _testEndTime;
	}

	public void setTestEndTime(String testEndTime) {
		_testEndTime = testEndTime;
	}

	public String getProcessInfo() {
		return _processInfo;
	}

	public void setProcessInfo(String processInfo) {
		_processInfo = processInfo;
	}

	public int getStatus() {
		return LetStatus.valueOf(_status).getStatus();
	}

	public void setStatus(String status) {
		_status = status;
	}
	public String getStatusName() {
	    return _status;
	}

	public ArrayList<TestParam> getTestParams() {
		if (_testParams == null) {
			_testParams = new ArrayList<TestParam>();
		}
		return _testParams;
	}

	public void setTestParams(ArrayList<TestParam> testParams) {
		_testParams = testParams;
	}

	public ArrayList<TestAttrib> getTestAttribs() {
		if (_testAttribs == null) {
			_testAttribs = new ArrayList<TestAttrib>();
		}
		return _testAttribs;
	}

	public void setTestAttribs(ArrayList<TestAttrib> testAttribs) {
		_testAttribs = testAttribs;
	}
	
	public ArrayList<FaultCode> getFaultCodes() {
		if (_faultCodes == null) {
			_faultCodes = new ArrayList<FaultCode>();
		}
		return _faultCodes;
	}

	public void setFaultCodes(ArrayList<FaultCode> faultCodes) {
		_faultCodes = faultCodes;
	}
}
