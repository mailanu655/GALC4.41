package com.honda.galc.common.exception;

public class ServiceException extends BaseException {

    
    private static final long serialVersionUID = 1L;
    
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String message, Throwable aDetail) {
		super(message, aDetail);
	}
}
