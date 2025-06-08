package com.honda.galc.common.exception;

/**
 * @author Subu Kathiresan
 * @date Apr 14, 2015
 */
public class LetPersistenceException extends BaseException {

    private static final long serialVersionUID = 1L;
    
    public LetPersistenceException(String message) {
        super(message);
    }
    
    public LetPersistenceException(String message, Throwable aDetail) {
		super(message, aDetail);
	}
}
