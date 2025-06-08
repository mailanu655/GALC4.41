package com.honda.galc.entity.enumtype;

/** * * 
* @author Fredrick Yessaian 
* @since Sep 03, 2014
*/

public enum MassMessageSeverity {
	NORMAL("NORMAL"), WARNING("WARNING"), CRITICAL("CRITICAL");

	private String severity;
	
	private MassMessageSeverity(String severity) {
		this.severity = severity;
	}
	
	public String getSeverity() {
		return severity;
	}
	
}
