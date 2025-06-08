package com.honda.galc.qics.mobile.client.widgets.form;

import com.honda.galc.qics.mobile.client.widgets.MessageList;

public class IntegerMaxValueVerifier extends Verifier {

	/**
	 * The maximum integer value.
	 */
	private int maxValue;
	
	/**
	 * Instantiates a new integer max value verifier.
	 *
	 * @param messageList the message list
	 * @param maxValue the max value
	 */
	public IntegerMaxValueVerifier(MessageList messageList, int maxValue ) {
		super(messageList);
		this.maxValue = maxValue;
	}

	/* (non-Javadoc)
	 * @see com.honda.hma.qics.mobile.client.widgets.form.Verifier#validate(com.honda.hma.qics.mobile.client.widgets.form.StringItem)
	 */
	@Override
	public boolean validate(Verifiable stringItem) {
		assert stringItem != null;
		boolean valid = true;
		if ( stringItem.getValueAsString() != null ) {
			try {
				int v = Integer.parseInt(stringItem.getValueAsString());
				if ( v > maxValue ) {
					valid = false;
					addMessage( stringItem, "Maximum value of " + maxValue + " was exceeded");
				}
			} catch (NumberFormatException e) {
			}
		}
		return valid;
	}
	
}
