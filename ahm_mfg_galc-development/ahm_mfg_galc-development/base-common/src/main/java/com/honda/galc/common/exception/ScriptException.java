package com.honda.galc.common.exception;

public class ScriptException extends SystemException{
	private static final long serialVersionUID = 1L;

	public ScriptException(String messageID) {
		super(messageID);
	}
	
	public ScriptException(String aMessageID, Throwable aDetail) {
		super(aMessageID, aDetail);
	}

	
}
