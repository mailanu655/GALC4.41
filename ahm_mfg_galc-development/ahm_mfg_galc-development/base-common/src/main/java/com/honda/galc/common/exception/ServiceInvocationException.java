package com.honda.galc.common.exception;

public class ServiceInvocationException extends ServiceException {

    
    private static final long serialVersionUID = 1L;
    
    public ServiceInvocationException(String message) {
        super(message);
    }
    
    public ServiceInvocationException(String message, Throwable aDetail) {
		super(message, aDetail);
	}
    

}
