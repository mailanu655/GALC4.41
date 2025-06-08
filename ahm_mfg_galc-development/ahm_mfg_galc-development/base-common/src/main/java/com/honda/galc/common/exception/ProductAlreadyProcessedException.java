package com.honda.galc.common.exception;

/**
 * @author Subu Kathiresan
 * @date Jul 11, 2017
 */
public class ProductAlreadyProcessedException extends TaskException {

	private static final long serialVersionUID = 1L;

    public ProductAlreadyProcessedException(String message) {
        super(message); 
    }
}
