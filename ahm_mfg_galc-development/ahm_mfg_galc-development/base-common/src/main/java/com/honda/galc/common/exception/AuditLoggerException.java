package com.honda.galc.common.exception;

/**
 * Custom exception for audit logger framework.
 * 
 * @author L&T InfoTech
 *
 */
public class AuditLoggerException extends BaseException {

	private static final long serialVersionUID = 1L;

	public AuditLoggerException(String aMessage) {
		super(aMessage);
	}

	public AuditLoggerException(String aMessage, Throwable throwable) {
		super(aMessage, throwable);
	}

}
