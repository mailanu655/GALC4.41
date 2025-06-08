package com.honda.galc.oif.values;

import org.apache.struts.util.LabelValueBean;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class OifStartMenuItem extends LabelValueBean {

	private static final long serialVersionUID = 1L;

	private boolean startTsRequired = false;
	
	private String warning = "";
	
	public OifStartMenuItem(String arg0, String arg1) {
		super(arg0, arg1);
	}

	public boolean isStartTsRequired() {
		return startTsRequired;
	}

	public void setStartTsRequired(boolean startTsRequired) {
		this.startTsRequired = startTsRequired;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

}
