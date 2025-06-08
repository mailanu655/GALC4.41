package com.honda.galc.qics.mobile.client.widgets.form;

import com.honda.galc.qics.mobile.client.widgets.MessageList;

/** 
 * Class is used to verify a Process Point entry.
 * 
 * @author vfc01346
 *
 */
public class ProcessPointVerifier extends RegexVerifier {
	
	/**
	 * Instantiates a new process point verifier with no message list
	 */
	public ProcessPointVerifier() {
		this( null );
	}
	
	/**
	 * Instantiates a new process point verifier.
	 *
	 * @param messageList the message list
	 */
	public ProcessPointVerifier( MessageList messageList ) {
		super( messageList, "^[A-Z0-9]{1,16}$", "must be 1 to 16 alpha-numeric characters.");
	}

}

