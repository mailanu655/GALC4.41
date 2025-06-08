package com.honda.galc.qics.mobile.client.widgets.form;

import com.honda.galc.qics.mobile.client.widgets.MessageList;

/**
 * The Class RegexVerifier verifies that a StringItem matches the supplied REGEX pattern.
 */
public class RegexVerifier extends Verifier {
	
	public static final String CONSTRAINED_ENTRY_PATTERN = "^[\\s\\w()@#()+-.,]*$";
	public static final String ALPHANUMERIC_PATTERN = "^[a-zA-Z\\d]*$";
	
	/** The pattern. */
	private String pattern;
	
	/** The message. */
	private String message;
	
	/**
	 * Instantiates a new regex verifier.
	 *
	 * @param messageList the message list
	 * @param pattern the pattern like "^[A-Z0-9]{17}$"
	 * @param message the message to display when pattern does not match
	 */
	public RegexVerifier(MessageList messageList, String pattern, String message ) {
		super(messageList);
		this.pattern = pattern;
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see com.honda.hma.qics.mobile.client.widgets.form.Verifier#validate(com.honda.hma.qics.mobile.client.widgets.form.StringItem)
	 */
	@Override
	public boolean validate(Verifiable item) {
		boolean valid = true;
		if ( item.getValueAsString() != null ) {
			if ( !item.getValueAsString().matches(pattern)) {
				addMessage( item, message );
				valid = false;
			}
		}
		return valid;
	}
	
}
