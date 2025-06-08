package com.honda.galc.qics.mobile.client.widgets.form;

import com.honda.galc.qics.mobile.client.widgets.MessageList;


/**
 * The Class MaxLengthVerifier.
 */
public class MaxLengthVerifier extends Verifier {

	/**
	 * Max number of characters allowed.
	 */
	private int maxLength = 10;
	
	/**
	 * Instantiates a new max length verifier.
	 *
	 * @param messageList the message list
	 * @param maxLength the max length
	 */
	public MaxLengthVerifier(MessageList messageList, int maxLength ) {
		super(messageList);
		this.maxLength = maxLength;
	}

	/**
	 * Validate.
	 *
	 * @param item the item
	 * @return true, if successful
	 */
	@Override
	public boolean validate(Verifiable item) {
		boolean valid = true;
		if ( item.getValueAsString().length() > maxLength ) {
			valid = false;
			addMessage( item, "Maximum length of " + maxLength + 
					" exceeded. Current length is " + item.getValueAsString().length() + ".");
		}
		return valid;
	}
	
}
