package com.honda.galc.common.exception;

public class OifServiceException extends SystemException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OifServiceException(String aMessageID) {
		super(aMessageID);
	}

	
	public OifServiceException(String aMessageID, Throwable aDetail) {
		super(aMessageID, aDetail);
	}


}
