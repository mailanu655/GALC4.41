package com.honda.galc.device.simulator.utils;

/**
 * @author Subu Kathiresan
 * @date Jan 27, 2012
 */
public enum Environment {
	HMIN_LOCAL	("http://localhost:9080/BaseWeb/HttpServiceHandler"),
	HMIN_SBX	("http://galchminline1nasb.hmin.am.honda.com/BaseWeb/HttpServiceHandler"), 
	HMIN_DEV	("http://galchminline1nadev.hmin.am.honda.com/BaseWeb/HttpServiceHandler"), 
	HMIN_QA 	("http://galchminline1naqa.hmin.am.honda.com/BaseWeb/HttpServiceHandler"),
	HMIN_UA 	("http://galchminline1naua.hmin.am.honda.com/BaseWeb/HttpServiceHandler"),
	
	HCM_SBX 	("http://10.167.200.200/BaseWeb/HttpServiceHandler"), 
	HCM_DEV 	("http://10.167.200.200/BaseWeb/HttpServiceHandler"), 
	HCM_QA 		("http://10.167.200.200/BaseWeb/HttpServiceHandler"),
	HCM_UA 		("http://10.167.200.200/BaseWeb/HttpServiceHandler"),
	
	HMA_LOCAL 	("http://localhost:80/BaseWeb/HttpServiceHandler"),
	HMA_SBX 	("http://qhma1was.hma.am.honda.com:8005/BaseWeb/HttpServiceHandler"), 
	HMA_DEV 	("http://qhma1was.hma.am.honda.com:8005/BaseWeb/HttpServiceHandler"), 
	HMA_QA 		("http://qhma1was.hma.am.honda.com:8005/BaseWeb/HttpServiceHandler"),
	HMA_UA 		("http://qhma1was.hma.am.honda.com:8005/BaseWeb/HttpServiceHandler"),
	
	HCL_SBX		("http://qhcl1was.hdm.am.honda.com/BaseWeb/HttpServiceHandler"),
	HCL_DEV		("http://qhcl1was.hdm.am.honda.com/BaseWeb/HttpServiceHandler"),
	HCL_QA		("http://qhcl1was.hdm.am.honda.com/BaseWeb/HttpServiceHandler"),
	HCL_UA		("http://qhcl1was.hdm.am.honda.com/BaseWeb/HttpServiceHandler"),
	
	PMC_DEV		("http://dpmc1was.ham.am.honda.com:8005/BaseWeb/HttpServiceHandler"),
	PMC_QA		("http://qpmc1was.ham.am.honda.com:8005/BaseWeb/HttpServiceHandler"),
	PMC_PROD	("http://ppmc1was.ham.am.honda.com:8005/BaseWeb/HttpServiceHandler"),
	
	QA_HMA	("http://dreg1was1.hma.am.honda.com:21150/BaseWeb/HttpServiceHandler"),
	QA_HCM	("http://dreg1was1.hma.am.honda.com:21250/BaseWeb/HttpServiceHandler"),
	QA_HMI	("http://dreg1was1.hma.am.honda.com:21350/BaseWeb/HttpServiceHandler"),
	QA_2SD	("http://dreg1was1.hma.am.honda.com:21550/BaseWeb/HttpServiceHandler"),
	QA_HCL	("http://dreg1was1.hma.am.honda.com:21450/BaseWeb/HttpServiceHandler"),
	QA_HMA5	("http://dreg1was1.hma.am.honda.com:21650/BaseWeb/HttpServiceHandler");
	
	private final String url;
	
	private Environment(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
