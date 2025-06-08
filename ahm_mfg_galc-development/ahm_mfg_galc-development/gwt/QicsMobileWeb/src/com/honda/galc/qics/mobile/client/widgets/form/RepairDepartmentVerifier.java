package com.honda.galc.qics.mobile.client.widgets.form;

import com.honda.galc.qics.mobile.client.widgets.MessageList;

/** 
 * Class is used to verify a Process Point entry.
 * 
 * @author vfc01346
 *
 */
public class RepairDepartmentVerifier extends RegexVerifier {
	
	/**
	 * Instantiates a new process point verifier with no message list
	 */
	public RepairDepartmentVerifier() {
		this( null );
	}
	
	/**
	 * Instantiates a new process point verifier.
	 *
	 * @param messageList the message list
	 */
	public RepairDepartmentVerifier( MessageList messageList ) {
		super( messageList, "^[A-Z0-9]{1,16}$", "must be 1 to 16 alpha-numeric characters.");
	}

}

