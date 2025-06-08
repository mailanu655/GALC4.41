package com.honda.galc.notification.service;

import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;

import com.honda.galc.service.IService;
/**
 * 
 * <h3>INotificationService Class description</h3>
 * <p> INotificationService description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Aug 29, 2013
 *
 *
 */
public interface INotificationService extends IService{
	
	@SuppressWarnings("serial")
	public final static Map<String,String> BROADCAST_SERVICIES  =
		unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("IProductOnNotification","Product On");
                    put("IProductPassedNotification","Product processed");
                    put("IProductReceivedNotification","Product received");
                    put("IProductProcessNotification","Product processing");
                    
                }
            });
}
