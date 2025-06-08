/**
 * 
 */
package com.honda.galc.test.data.setup;

/**
 * @author Subu Kathiresan
 * @date Jan 27, 2012
 */
public enum Environment {
	LOCAL	("http://localhost:9081/BaseWeb/HttpServiceHandler"),
	
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
	HCL_UA		("http://qhcl1was.hdm.am.honda.com/BaseWeb/HttpServiceHandler");
	
	private final String url;
	
	private Environment(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
