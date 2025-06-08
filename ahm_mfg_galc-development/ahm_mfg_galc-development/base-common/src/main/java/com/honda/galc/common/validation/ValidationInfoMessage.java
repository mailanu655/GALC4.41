/**
 * 
 */
package com.honda.galc.common.validation;

import java.io.Serializable;

/**
 * @author Subu Kathiresan
 * @author Todd Roling
 * Oct 31, 2011
 */
public class ValidationInfoMessage implements Serializable  {

	private ValidationType _type = ValidationType.Unknown;
	private String _message = "";
	private IValidationLevel _level = null;
	private static final long serialVersionUID = 1L;   
	   
	public ValidationInfoMessage(ValidationType type, String message) {
		super();
		_type = type;
		_message = message;
	}
	
	public ValidationType getType() {
		return _type;
	}

	public void setType(ValidationType type) {
		_type = type;
	}

	public String getMessage() {
		return _message;
	}

	public void setMessage(String message) {
		_message = message;
	}

	public IValidationLevel getLevel() {
		return _level;
	}

	public void setLevel(IValidationLevel level) {
		_level = level;
	}
}
