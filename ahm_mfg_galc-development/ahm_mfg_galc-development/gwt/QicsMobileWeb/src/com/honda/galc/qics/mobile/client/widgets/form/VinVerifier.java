package com.honda.galc.qics.mobile.client.widgets.form;

import com.honda.galc.qics.mobile.client.widgets.MessageList;

public class VinVerifier extends RegexVerifier {
	
	public VinVerifier( MessageList messageList ) {
		super( messageList, "^[A-Z0-9]{17}$", "must be 17 alpha-numeric characters.");
	}

}
