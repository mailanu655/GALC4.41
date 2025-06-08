package com.honda.galc.client;

import static com.honda.galc.common.logging.Logger.getLogger;
import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.dao.conf.AccessControlEntryDao;
import com.honda.galc.dao.conf.SecurityGroupDao;
import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.dao.conf.UserSecurityGroupDao;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.conf.AccessControlEntry;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.SecurityGroup;
import com.honda.galc.entity.conf.User;
import com.honda.galc.entity.conf.UserSecurityGroup;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class CachedAccessControlManager extends PersistentCache implements IAccessControlManager{
	
	private String userName = null;
	private LoginStatus currentLoginStatus;
	
	public CachedAccessControlManager () {
		load();
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private void load() {
		if(!ServiceFactory.isServerAvailable()) return;
		loadUsers();
		loadSecurityGroupList();
		loadAccessControlList();
		loadUserSecurityGroups();
	}
	
	private void loadUsers() {
			
			List<User> users = null;
			
			try{
			    users = getDao(UserDao.class).findAll();
			}catch(Exception e) {
	            getLogger().error("Cannot load \"Users\" data from server. Server is not available! Using cache data");
			    return;
			}
			
			put("users", users);
	}
	 
	public List<User> getUsers() {
		return this.getList("users", User.class);
	}
	 
	private void loadAccessControlList() {
		List<AccessControlEntry> accessControlList = null;
		try{
			accessControlList = getDao(AccessControlEntryDao.class).findAll();
		}catch(Exception e) {
			getLogger().error("Cannot load \"AccessControlEntry\" data from server. Server is not availabe! Using cache data");
			return;
		}
		put("accessContrlList",accessControlList);
	}
	
	public List<AccessControlEntry> getAccessControlList() {
		return this.getList("accessContrlList", AccessControlEntry.class);
	}
	
	private void loadSecurityGroupList() {
		List<SecurityGroup> securityGroups = null;
		try{
			securityGroups = getDao(SecurityGroupDao.class).findAll();
		}catch(Exception e){
			getLogger().error("Cannot load\"SecurityGroups\" data from server. Server is not avilable! Using cache data");
			return;
		}
		put("securityGroupList", securityGroups);
	}
	
	public List<SecurityGroup> getSecurityGroups() {
		return this.getList("securityGroupList", SecurityGroup.class);
	}
	 
	private void loadUserSecurityGroups() {
		List<UserSecurityGroup> userSecurityGroups = null;
		try{
			userSecurityGroups = getDao(UserSecurityGroupDao.class).findAll();
		}catch(Exception e){
			getLogger().error("Cannot load\"UserSecurityGroups\" data from server. Server is not avilable! Using cache data");
			return;
		}
		put("userSecurityGroups", userSecurityGroups);
	}
	
	public List<UserSecurityGroup> getUserSecurityGroups() {
		return this.getList("userSecurityGroups", UserSecurityGroup.class);
	}
	
	public LoginStatus login(String hostName, String userName, String password) {

		if(isUserInLightSecurityGroup(hostName, userName, password)) {
			currentLoginStatus = LoginStatus.LIGHT_SECURITY_OK;
		} else currentLoginStatus = login(userName,password);
		return currentLoginStatus;
		
	}
	
	public LoginStatus login(String userName, String password) {
		 User user = findUser(userName);
		 if(user == null) return LoginStatus.USER_NOT_EXIST;
		 if(password != null && password.equals(user.getPasswd())) {
			 if(user.isPasswordExpired()) return LoginStatus.PASSWORD_EXPIRED;
			 this.userName = userName;
			 return LoginStatus.OK;
		 }else return LoginStatus.PASSWORD_INCORRECT;
	}
	/**
	 * check if it has access permission to access default application
	 * @return
	 */
	public boolean isAccessPermitted() {
		
		Application application = ApplicationContext.getInstance().getDefaultApplication();
		
		if(application == null) return true;
		return isAccessPermitted(application.getScreenId());
		
	}
	
	/*check to see if the current user has the permission to access the screenId
	 * (non-Javadoc)
	 * @see com.honda.galc.client.IAccessControlManager#isAccessPermitted(java.lang.String)
	 */
	public boolean isAccessPermitted(String screenId) {
		
		if(currentLoginStatus != null && currentLoginStatus == LoginStatus.LIGHT_SECURITY_OK) {
			// light security --- always has access to all applications of this terminal
			return true;
		}else {
			List<AccessControlEntry> accessControlList = getAccessControlList(this.userName);
		
			for(AccessControlEntry entry : accessControlList) {
				if(entry.getId().getScreenId().equals(screenId)) return true;
			}
			return false;
		}
		
	}
	
	public boolean isUserInLightSecurityGroup(String hostName,String userName,String password) {
		
		if(!(hostName.equalsIgnoreCase(userName) && hostName.equalsIgnoreCase(password))) return false;
		String securityFlag = PropertyService.getProperty("LIGHT_CLIENT_SECURITY", hostName);
		if(securityFlag == null) {
			securityFlag = PropertyService.getProperty("LIGHT_CLIENT_SECURITY", "*");
		}
		return (securityFlag != null && Boolean.parseBoolean(securityFlag));

	}

	public List<AccessControlEntry> getAccessControlList(String userName) {
		
		List<UserSecurityGroup> userSecurityGroupList = findUserSecurityGroupList(userName);
		List<AccessControlEntry> accessControlList = new ArrayList<AccessControlEntry>();
		for(UserSecurityGroup userSecurityGroup : userSecurityGroupList) {
			accessControlList.addAll(findAccessControlList(userSecurityGroup.getSecurityGroupId()));
		}
		
		return accessControlList;
		
	}

	private User findUser(String userName) {
		for(User user : getUsers()) {
			if(user.getUserId().equals(userName)) return user;
		}
		return null;
	}
	
	private List<UserSecurityGroup> findUserSecurityGroupList(String userName) {
		List<UserSecurityGroup> userSecurityGroupList = new ArrayList<UserSecurityGroup>();
		for(UserSecurityGroup userSecurityGroup : getUserSecurityGroups()) {
			if(userSecurityGroup.getId().getUserId().equals(userName)) 
				userSecurityGroupList.add(userSecurityGroup);
		}
		return userSecurityGroupList;
	}
	
	private List<AccessControlEntry> findAccessControlList(String securityGroupName) {
		List<AccessControlEntry> accessControlList = new ArrayList<AccessControlEntry>();
		for(AccessControlEntry entry : getAccessControlList()) {
			if(entry.getId().getSecurityGroup().equals(securityGroupName))
				accessControlList.add(entry);
		}
		return accessControlList;
	}

	public boolean isAuthorized(String securityGroup) {
		for(UserSecurityGroup userSecurityGroup : getUserSecurityGroups()) {
			if(userSecurityGroup.getId().getUserId().equals(userName) &&
					userSecurityGroup.getSecurityGroupId().equals(securityGroup)) 
				return true;
		}
		return false;
	}

	public LoginStatus loginWithoutPassword(String userName) {
		User user = findUser(userName);
		if (user == null)
			return LoginStatus.USER_NOT_EXIST;
		else {
			this.userName = userName;
			return LoginStatus.OK;

		}
	}

}
