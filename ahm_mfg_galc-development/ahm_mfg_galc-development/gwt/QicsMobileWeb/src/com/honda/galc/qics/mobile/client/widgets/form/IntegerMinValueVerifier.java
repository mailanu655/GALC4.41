package com.honda.galc.qics.mobile.client.widgets.form;

import com.honda.galc.qics.mobile.client.widgets.MessageList;

/**
 * The Class IntegerMinValueVerifier.
 */
public class IntegerMinValueVerifier extends Verifier {

	/** Minimum value allowed. */
	private int minValue;
	
	/**
	 * Instantiates a new integer min value verifier.
	 *
	 * @param messageList the message list
	 * @param minValue the min value
	 */
	public IntegerMinValueVerifier(MessageList messageList, int minValue ) {
		super(messageList);
		this.minValue = minValue;
	}

	/* (non-Javadoc)
	 * @see com.honda.hma.qics.mobile.client.widgets.form.Verifier#validate(com.honda.hma.qics.mobile.client.widgets.form.StringItem)
	 */
	@Override
	public boolean validate(Verifiable item) {
		boolean valid = true;
		if ( item.getValueAsString() != null ) {
			try {
				int v = Integer.parseInt(item.getValueAsString());
				if ( v < minValue ) {
					valid = false;
					addMessage( item, "Minimum value of " + minValue + 
							" is not achieved. Current value is " + v + ".");
				}
			} catch (NumberFormatException e) {
			}
		}
		return valid;
	}
	
}
