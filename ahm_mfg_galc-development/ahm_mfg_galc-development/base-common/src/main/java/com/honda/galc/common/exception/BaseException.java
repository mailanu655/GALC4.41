package com.honda.galc.common.exception;


public class BaseException extends RuntimeException {
	
	public     static final long serialVersionUID = 8381320680649696091L; // @JM006.1
	

	/**
	 * Prohibits constructing empty exceptions
	 */
	@SuppressWarnings("unused")
	private BaseException() {
		// Do not create empty exceptions
	}
	/**
	 * Constructs a BaseException with the specified MessageID and null as its nested exception.
	 * @param aMessageID Message ID of Logger.
	 */
	public BaseException(String aMessage) {
		super(aMessage); 
	}
	
	public BaseException(String aMessage, Throwable throwable) {
        super(aMessage,throwable); 
    }
    
}
