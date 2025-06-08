package com.honda.galc.client;



import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.enumtype.ScreenAccessLevel;

public interface IAccessControlManager {
	
	public boolean isAccessPermitted();
	
	public boolean isAccessPermitted(String screenId);
	
	public boolean isAuthorized(String securityGroup);
	
	public String getUserName();
	
	public LoginStatus login(String userName, String password);

	// check light user security
	public LoginStatus login(String hostName,String userName, String password);
	
	public void  setUserName(String userName);
	
	public String mapProxCardNumber(Integer cardNumber);
	
	public LoginStatus login_without_password(String userName);
	
	public ScreenAccessLevel getHighestAccessLevel(String screenId);

	public String getAssociateName(String userId);
	
}
