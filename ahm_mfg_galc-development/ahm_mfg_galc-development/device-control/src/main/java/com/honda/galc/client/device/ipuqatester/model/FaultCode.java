/**
 * 
 */
package com.honda.galc.client.device.ipuqatester.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author Subu Kathiresan
 * @date Mar 27, 2012
 *
 */
@XStreamAlias("FAULT_CODE")
public class FaultCode {

	@XStreamAlias("FaultCode")
	@XStreamAsAttribute
	private String _faultCode = "";

	@XStreamAlias("ShortDesc")
	@XStreamAsAttribute
	private String _shortDesc = "";
	
	@XStreamAlias("Testtime")
	@XStreamAsAttribute
	private String _testTime = "";

	/** Need to allow bean to be created via reflection */
	public FaultCode() {}
	
	public String getFaultCode() {
		return _faultCode;
	}

	public void setFaultCode(String faultCode) {
		_faultCode = faultCode;
	}
	
	public String getShortDesc() {
		return _shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		_shortDesc = shortDesc;
	}
	
	public String getTestTime() {
		return _testTime;
	}

	public void setTestTime(String testTime) {
		_testTime = testTime;
	}
}
