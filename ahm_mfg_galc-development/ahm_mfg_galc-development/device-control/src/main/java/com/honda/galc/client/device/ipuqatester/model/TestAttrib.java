/**
 * 
 */
package com.honda.galc.client.device.ipuqatester.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author Subu Kathiresan
 * @date Feb 2, 2012
 */
@XStreamAlias("TEST_ATTRIBUTE")
public class TestAttrib {

	@XStreamAlias("Att")
	@XStreamAsAttribute
	private String _att = "";

	@XStreamAlias("Val")
	@XStreamAsAttribute
	private String _val = "";
	
	@XStreamAlias("ExpectedVal")
	@XStreamAsAttribute
	private String _expectedVal = "";
	
	/** Need to allow bean to be created via reflection */
	public TestAttrib() {}
	
	public String getAtt() {
		return _att;
	}

	public void setAtt(String attribute) {
		_att = attribute;
	}

	public String getVal() {
		return _val;
	}

	public void setVal(String val) {
		_val = val;
	}

	public String getExpectedVal() {
		return _expectedVal;
	}

	public void setExpectedVal(String expectedVal) {
		_expectedVal = expectedVal;
	}
}
