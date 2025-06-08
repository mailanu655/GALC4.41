package com.honda.galc.common.exception;


public class ConfigurationServicesException extends SystemException {

	public static final long serialVersionUID = -1994631066864868318L; // @JM006.1
	
	/**
	 * Constructs a SystemException with the specified MessageID and null as its nested exception.
	 * @param aMessageID Message ID
	 */
	public ConfigurationServicesException(String message) {
		super(message);
	}
	/**
	 * Constructs a SystemException with the specified MessageID and null as its nested exception.
	 * @param aMessageID Message ID
	 * @param aDetail Nested exception
	 */
	public ConfigurationServicesException(String message, Throwable aDetail) {
		super(message, aDetail);
	}
	

}
