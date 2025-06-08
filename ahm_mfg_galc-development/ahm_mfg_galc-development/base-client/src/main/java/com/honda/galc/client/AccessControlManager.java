package com.honda.galc.client;

/**
 * 
 * <h3>AccessControlManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AccessControlManager description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by Meghana G</TH>
 * <TH>Update date Feb 9, 2011</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Modified the AccessControlManager to add functionality for LDAP Authentication.</TH>
 * </TR>
 *
 * </TABLE>
 *   
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.util.LDAPService;
import com.honda.galc.common.exception.LoginException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.AccessControlEntryDao;
import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.dao.conf.UserSecurityGroupDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.User;
import com.honda.galc.entity.conf.UserSecurityGroup;
import com.honda.galc.entity.conf.UserSecurityGroupId;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class AccessControlManager implements IAccessControlManager{

	private String currentUserName;
	private LoginStatus currentLoginStatus;
	private boolean LDAP_FLAG = false;
	private List<UserSecurityGroupId> currentSecurityGroups = new ArrayList<UserSecurityGroupId>();
	private int DB_USER_ID_LENGTH = 11;
	
	public String getUserName() {
		return currentUserName;
	}

	public boolean isAccessPermitted() {
		Application application = ApplicationContext.getInstance().getDefaultApplication();
		
		if(application == null) return true;
		return isAccessPermitted(application.getScreenId());

	}
	
	public boolean isAuthorized(String securityGroup) {
		if(getLDAPFlag()){
			List<String> authorizedGroups = Arrays.asList(securityGroup.split(","));
			for (UserSecurityGroupId securityGroupId : currentSecurityGroups){
				if (authorizedGroups.contains(securityGroupId.getSecurityGroup()))
					return true;
			}
			return false;
		}else{
			return ServiceFactory.getDao(UserSecurityGroupDao.class).isUserInSecurityGroup(currentUserName, securityGroup);
		}		 

	}

	public boolean isAccessPermitted(String screenId) {
		if(currentLoginStatus != null && currentLoginStatus == LoginStatus.LIGHT_SECURITY_OK) 
			// light security --- always has access to all applications of this terminal
			return true;
		else{    
			return ServiceFactory.getDao(AccessControlEntryDao.class).isAccessPermitted(currentSecurityGroups,screenId);
		}
	}

	public LoginStatus login(String hostName,String userName, String password) {
		
		if(isUserInLightSecurityGroup(hostName, userName, password)) {
			this.currentUserName = userName;
			this.currentLoginStatus = LoginStatus.LIGHT_SECURITY_OK;
			return LoginStatus.LIGHT_SECURITY_OK;
		}
		return login(userName,password);
		
	}
	
	private boolean getLDAPFlag(){
		//Fix to RGALCDEV-2293
		if (ApplicationContext.getInstance().getDefaultApplication() != null){
			String appId = ApplicationContext.getInstance().getDefaultApplication().getApplicationId();
			SystemPropertyBean property=PropertyService.getPropertyBean(SystemPropertyBean.class, appId);
			LDAP_FLAG = property.isLdap();
		}
		return LDAP_FLAG;
	}
	
	/**
	 * The method verifies LDAP_FLAG in DB and uses the LDAP Authentication if LDAP FLAG is true
	 * else it uses the Database Table GAL105TBX to authenticate users. 
	 */
	public LoginStatus login(String userName, String password) {
		if(getLDAPFlag()){
			return verifyLDAPUser(userName,password);
		}else{
			return verifyUser(userName,password);
		}		 
	}	
	
	/**
	 * This method verifies if the userName and password exists in the Database Table GAL105TBX.
	 * @param userName
	 * @param password
	 * @return
	 */
	private LoginStatus verifyUser(String userName, String password) {
		if (StringUtils.trim(userName).length() > this.DB_USER_ID_LENGTH) 
			return LoginStatus.USER_NOT_EXIST;
		User user = findUser(userName);
		String tmpUserName = currentUserName;
		 if(user == null) return LoginStatus.USER_NOT_EXIST;
		 if(password != null && password.equals(user.getPasswd())) {
			 if(user.isPasswordExpired()) return LoginStatus.PASSWORD_EXPIRED;
			 this.currentUserName = userName;
			 this.currentSecurityGroups  = getUserSecurityGroups(currentUserName);
			 if(!isAccessPermitted()) {
				 this.currentUserName = tmpUserName;
				 return LoginStatus.NO_ACCESS_PERMISSION;
			 }
			 return LoginStatus.OK;
		 }else return LoginStatus.PASSWORD_INCORRECT;
	}

	/**
	 * This method uses LDAP Service to perform LDAP Authentication.
	 * @param userName
	 * @param password
	 * @return
	 */
	private LoginStatus verifyLDAPUser(String userName, String password) {
		String tmpUserName = currentUserName;
		List<UserSecurityGroupId> groups = null;
		List <UserSecurityGroupId> tmpGroups = currentSecurityGroups;
		try{
		  groups = LDAPService.getInstance().authenticate(userName, password);
		}catch(LoginException le){
			try {
				LoginStatus loginStatus = LoginStatus.valueOf(le.getMessage());
				Logger.getLogger().warn(loginStatus.toString() + " (" + loginStatus.getMessage() + ") : " + userName);
				return loginStatus;
			}catch(IllegalArgumentException ex) {
				Logger.getLogger().error(le.getMessage());
				return LoginStatus.AUTHENTICATION_ERROR;
			}
		}
		if(groups == null) return LoginStatus.USER_NOT_EXIST;
		if(groups != null){
			this.currentUserName = userName;
			this.currentSecurityGroups = groups;
			 if(!isAccessPermitted()) {
				 this.currentUserName = tmpUserName;
				 this.currentSecurityGroups = tmpGroups;
				 return LoginStatus.NO_ACCESS_PERMISSION;
			 }
		}
		return LoginStatus.OK;
	}

	public boolean isUserInLightSecurityGroup(String hostName,String userName,String password) {
		
		if(!(hostName.equalsIgnoreCase(userName) && hostName.equalsIgnoreCase(password))) return false;
		String securityFlag = PropertyService.getProperty("LIGHT_CLIENT_SECURITY", hostName);
		if(securityFlag == null) {
			securityFlag = PropertyService.getProperty("LIGHT_CLIENT_SECURITY", "*");
		}
		return (securityFlag != null && Boolean.parseBoolean(securityFlag));

	}

    private User findUser(String userName) {
    	return  ServiceFactory.getDao(UserDao.class).findByKey(userName);
    }
    
    private List<UserSecurityGroupId> getUserSecurityGroups(String userName){
       List <UserSecurityGroup>groups = ServiceFactory.getDao(UserSecurityGroupDao.class).findAllByUserId(userName);
       List<UserSecurityGroupId> groupIds = new ArrayList<UserSecurityGroupId>();
       if(groups!=null){
    	   for (UserSecurityGroup group : groups) {
			groupIds.add(group.getId());
		}
       }
       return groupIds;
    }
    
    public LoginStatus loginWithoutPassword(String userName) {
		if(getLDAPFlag()){
			return verifyLDAPUserWithoutPassword(userName);
		}else{
			return verifyUserWithoutPassword(userName);
		}		 
	}
    
    private LoginStatus verifyUserWithoutPassword(String userName) {
		if (StringUtils.trim(userName).length() > this.DB_USER_ID_LENGTH) 
			return LoginStatus.USER_NOT_EXIST;
    	User user = findUser(userName);
		String tmpUserName = currentUserName;
		 if(user == null) return LoginStatus.USER_NOT_EXIST;
		
			 if(user.isPasswordExpired()) return LoginStatus.PASSWORD_EXPIRED;
			 this.currentUserName = userName;
			 this.currentSecurityGroups  = getUserSecurityGroups(currentUserName);
			 if(!isAccessPermitted()) {
				 this.currentUserName = tmpUserName;
				 return LoginStatus.NO_ACCESS_PERMISSION;
			 }
			 return LoginStatus.OK;
		 
	}
    
    private LoginStatus verifyLDAPUserWithoutPassword(String userName) {
		String tmpUserName = currentUserName;
		List<UserSecurityGroupId> groups = null;
		List <UserSecurityGroupId> tmpGroups = currentSecurityGroups;
		try{
		  groups = LDAPService.getInstance().authenticate_without_pasword(userName);
		}catch(LoginException le){
			if(le.getMessage().equalsIgnoreCase(LoginStatus.USER_NOT_EXIST.toString())){
				return LoginStatus.USER_NOT_EXIST;
			}else if(le.getMessage().equalsIgnoreCase(LoginStatus.PASSWORD_EXPIRED.toString())){
				return LoginStatus.PASSWORD_EXPIRED;
			}else if(le.getMessage().equalsIgnoreCase(LoginStatus.AUTHENTICATION_ERROR.toString())){
				return LoginStatus.AUTHENTICATION_ERROR;
			}else if(le.getMessage().equalsIgnoreCase(LoginStatus.PASSWORD_INCORRECT.toString())){
				return LoginStatus.PASSWORD_INCORRECT;
			}
		}
		
		boolean createLocalLdapRecord = true;
		if (ApplicationContext.getInstance().getDefaultApplication() != null){
			String appId = ApplicationContext.getInstance().getDefaultApplication().getApplicationId();
			SystemPropertyBean property=PropertyService.getPropertyBean(SystemPropertyBean.class, appId);
			if ( !property.isCreateLocalLdapRecord() ) createLocalLdapRecord = false;
		}

		//check if user exists in 105tbx
		// if not add user
		UserDao userDao = ServiceFactory.getDao(UserDao.class);
		User user = userDao.findByKey(userName);
		if(user == null && createLocalLdapRecord ) {
			User newUser = new User();
			newUser.setUserId(userName);
			String associateName = LDAPService.getInstance().getAssociateName(userName);
			associateName = StringUtils.isEmpty(associateName)?userName:associateName;
			newUser.setUserName(associateName);
			userDao.save(newUser);
		}
		if(groups == null) return LoginStatus.USER_NOT_EXIST;
		if(groups != null){
			this.currentUserName = userName;
			this.currentSecurityGroups = groups;
			 if(!isAccessPermitted()) {
				 this.currentUserName = tmpUserName;
				 this.currentSecurityGroups = tmpGroups;
				 return LoginStatus.NO_ACCESS_PERMISSION;
			 }
		}
		return LoginStatus.OK;
	}
}
