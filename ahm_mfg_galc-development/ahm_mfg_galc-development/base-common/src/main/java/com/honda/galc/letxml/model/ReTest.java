package com.honda.galc.letxml.model;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RE_TEST")
public class ReTest extends Test{
	
	@XStreamAlias("RE_TEST_ATTRIBUTES")
	public ArrayList<ReTestAttribute> _reTestAttribs = new ArrayList<ReTestAttribute>();
	
	@XStreamAlias("RE_TEST_PARAMS")
	public ArrayList<ReTestParam> _reTestParams = new ArrayList<ReTestParam>();

	@XStreamAlias("RE_FAULT_CODES")
	public ArrayList<ReFaultCode> _reFaultCode = new ArrayList<ReFaultCode>();

	public ArrayList<ReTestAttribute> getReTestAttribs() {
        if (_reTestAttribs == null) {
            _reTestAttribs = new ArrayList<ReTestAttribute>();
        }	    
		return _reTestAttribs;
	}

	public void setReTestAttribs(ArrayList<ReTestAttribute> testAttribs) {
		_reTestAttribs = testAttribs;
	}
	
	public ArrayList<ReTestParam> getReTestParams() {
        if (_reTestParams == null) {
            _reTestParams = new ArrayList<ReTestParam>();
        }	    
		return _reTestParams;
	}

	public void setReTestParams(ArrayList<ReTestParam> testParams) {
		_reTestParams = testParams;
	}

	public ArrayList<ReFaultCode> getReFaultCode() {
        if (_reFaultCode == null) {
            _reFaultCode = new ArrayList<ReFaultCode>();
        }	    
		return _reFaultCode;
	}

	public void setReFaultCode(ArrayList<ReFaultCode> faultCode) {
		_reFaultCode = faultCode;
	}
	
	
}
