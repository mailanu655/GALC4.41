package com.honda.galc.system.oif.svc.common;

/**
 * Insert the type's description here. Creation date: (1/22/02 1:15:47 PM)
 * 
 * @author: Administrator
 */
public abstract class OifServiceObject {
	protected static final String MSG_GEN_ERR_ID = "HCMOIFE0301";
	protected static final String MSG_GEN_INFO_ID = "HCMOIFI0302";
	private IOifService parentHandler = null;

	/**
	 * Insert the method's description here. Creation date: (1/22/02 3:14:37 PM)
	 */
	public OifServiceObject(IOifService aHandler) {

		parentHandler = aHandler;
	}

	/**
	 * Insert the method's description here. Creation date: (1/22/02 3:13:07 PM)
	 */
	public IOifService getParentHandler() {
		return parentHandler;

	}

	/**
	 * Insert the method's description here. Creation date: (1/22/02 3:13:07 PM)
	 * @param msgId - message ID
	 * @param logMethod - log method
	 */
	public void log(String msgId, String logMethod, String aMessage) {
		parentHandler.log(msgId, logMethod, aMessage);
	}

	/**
	 * Insert the method's description here. Creation date: (1/22/02 3:13:07 PM)
	 * @param msgId - message ID
	 * @param logMethod - log method
	 */
	public void logError(String msgId, String logMethod, String aMessage) {
		parentHandler.logError(msgId, logMethod, aMessage);
	}

	public boolean isDebug() {
		return parentHandler.isDebug();
	}
}
