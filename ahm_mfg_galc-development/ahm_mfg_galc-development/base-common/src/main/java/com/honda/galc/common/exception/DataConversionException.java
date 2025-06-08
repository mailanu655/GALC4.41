package com.honda.galc.common.exception;

public class DataConversionException extends BaseException {

    
    private static final long serialVersionUID = 1L;
    
    public DataConversionException(String message) {
        super(message);
    }
    
    public DataConversionException(String message, Throwable aDetail) {
		super(message, aDetail);
	}
    

}
