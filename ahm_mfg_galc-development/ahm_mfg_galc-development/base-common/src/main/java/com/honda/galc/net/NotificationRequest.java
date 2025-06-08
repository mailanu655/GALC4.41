package com.honda.galc.net;

/**
 * @author Subu Kathiresan
 * @date Sep 8, 2015
 */
public class NotificationRequest extends Request {

	private static final long serialVersionUID = 5585244166545384755L;

	public NotificationRequest(String command) {
		super(command);
	}

    public NotificationRequest(String target, String command, Object[] params) {
    	super(target, command, params);
    }
    
	private String notificationHandlerClass = "";
    
    public String getNotificationHandlerClass() {
		return notificationHandlerClass;
	}

	public void setNotificationHandlerClass(String notificationHandlerClass) {
		this.notificationHandlerClass = notificationHandlerClass;
	}
}
