package com.honda.galc.notification.service;



/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface ILogLevelNotification extends INotificationService {
	
	void execute(String componentId, String level);

}
