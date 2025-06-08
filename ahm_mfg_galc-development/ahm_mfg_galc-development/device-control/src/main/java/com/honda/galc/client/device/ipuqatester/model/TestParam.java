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
@XStreamAlias("TEST_PARAM")
public class TestParam {
	
	@XStreamAlias("Param")
	@XStreamAsAttribute
	private String _param = "";
	
	@XStreamAlias("Val")
	@XStreamAsAttribute
	private String _val = "";
	
	@XStreamAlias("LoLimit")
	@XStreamAsAttribute
	private String _loLimit = "";
	
	@XStreamAlias("HiLimit")
	@XStreamAsAttribute
	private String _hiLimit = "";
	
	@XStreamAlias("Unit")
	@XStreamAsAttribute
	private String _unit = "";

	/** Need to allow bean to be created via reflection */
	public TestParam() {}
	
	public String getParam() {
		return _param;
	}

	public void setParam(String param) {
		_param = param;
	}

	public String getVal() {
		return _val;
	}

	public void setVal(String val) {
		_val = val;
	}

	public String getLoLimit() {
		return _loLimit;
	}

	public void setLoLimit(String lowLimit) {
		_loLimit = lowLimit;
	}
	
	public String getHiLimit() {
		return _hiLimit;
	}

	public void setHiLimit(String highLimit) {
		_hiLimit = highLimit;
	}

	public String getUnit() {
		return _unit;
	}

	public void setUnit(String unit) {
		_unit = unit;
	}
}
