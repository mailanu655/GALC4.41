package com.honda.galc.client;

import com.honda.galc.enumtype.LoginStatus;

public interface IAccessControlManager {
	
	public boolean isAccessPermitted();
	
	public boolean isAccessPermitted(String screenId);
	
	public boolean isAuthorized(String securityGroup);
	
	public String getUserName();
	
	public LoginStatus login(String userName, String password);

	// check light user security
	public LoginStatus login(String hostName,String userName, String password);
	
	public LoginStatus loginWithoutPassword(String userName);
	
}
