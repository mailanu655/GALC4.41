package com.honda.galc.common.exception;

public class PrintingException extends SystemException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PrintingException(String aMessageID) {
		super(aMessageID);
	}

	
	public PrintingException(String aMessageID, Throwable aDetail) {
		super(aMessageID, aDetail);
	}


}
