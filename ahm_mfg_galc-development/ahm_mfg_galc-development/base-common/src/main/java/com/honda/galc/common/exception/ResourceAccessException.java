package com.honda.galc.common.exception;

public class ResourceAccessException extends RuntimeException {

    
    private static final long serialVersionUID = 1L;
    
    public ResourceAccessException(String message) {
        super(message);
    }
    
    public ResourceAccessException(String message, Throwable aDetail) {
		super(message, aDetail);
	}
    

}
