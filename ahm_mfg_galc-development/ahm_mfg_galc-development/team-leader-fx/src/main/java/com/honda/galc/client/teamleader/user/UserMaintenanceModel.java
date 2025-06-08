package com.honda.galc.client.teamleader.user;

import static com.honda.galc.service.ServiceFactory.getDao;
import java.util.List;
import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.dao.conf.SecurityGroupDao;
import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.dao.conf.UserSecurityGroupDao;
import com.honda.galc.entity.conf.SecurityGroup;
import com.honda.galc.entity.conf.User;
import com.honda.galc.entity.conf.UserSecurityGroup;

/**
 * 
 * <h3>UserMaintenanceModel Class description</h3>
 * <p>
 * UserMaintenanceModel is used to perform the CRUD operation in DB tables.
 * </p>
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
 * @author LnTInfotech<br>
 * 
 */

public class UserMaintenanceModel extends AbstractModel {
	
	public UserMaintenanceModel() {
		super();
	}
	
	/**
	 * This method is get all user from database
	 */
	public List<User> findAllUser(){
		return removeAllNullValue(getDao(UserDao.class).findAllSortedByUserId());
	}
	
	
	/**
	 * This method is get all Security Group from database
	 */
	public List<SecurityGroup> findAllSecurityGroup(){
		return  getDao(SecurityGroupDao.class).findAllSecurityGroup ();
	}
	
	/**
	 * This method is get all user Security Group from database
	 */
	public List<UserSecurityGroup> findAllUserSecurityGroup( User  user){
		return getDao(UserSecurityGroupDao.class).findAllByUserId(user.getId());
	}
	
	/**
	 * This method is to save  user 
	 */
	public void createUser(final User user){
		getDao(UserDao.class).save(user);
	}
	
	/**
	 * This method is to update  user 
	 */
	public void updateUser(final User user){
		getDao(UserDao.class).update(user);
	}
	
	/**
	 * This method is to update  UserSecurityGroup 
	 */
	public void updateUserSecurityGroup(final UserSecurityGroup userSecurityGroup){
		getDao(UserSecurityGroupDao.class).update(userSecurityGroup);
	}
	
	/**
	 * This method is to assigned Security Group list to user 
	 */
	public void saveUserSecurityGroup(final UserSecurityGroup userSecurityGroup){
		getDao(UserSecurityGroupDao.class).save(userSecurityGroup);
	}
	
	/**
	 * This method is to remove user 
	 */
	public void deleteAllUserSecurityGroup( List<UserSecurityGroup> userList){
		getDao(UserSecurityGroupDao.class).removeAll(userList);
	}
	
	/**
	 * This method is to remove user 
	 */
	public void deleteUser(final User user){
		getDao(UserDao.class).remove(user);
	}
	
	/**
	 * This method is to remove User Security Group 
	 */
	public void deleteByUserSecurityGroup( UserSecurityGroup userSecurityGroup){
		getDao(UserSecurityGroupDao.class).removeByKey(userSecurityGroup.getId());
	}
	
	
	/**
	 * This method is to remove User Security Group 
	 */
	public void deleteAllUserSecurityGroup( User user){
		getDao(UserSecurityGroupDao.class).deleteAllByUserId(user.getId());
	}
	
	
	/**
	 * This method is to find filtered user 
	 */
	public List<User> getUsersByFilter( String filterString) {
		return getDao(UserDao.class).findAllUsersByFilter(filterString);
	}
	
	/**
	 * This method is remove null values in user 
	 */
	private List<User> removeAllNullValue( List<User> userList) {
		if(userList!=null && !userList.isEmpty()){
			for(User userObj : userList){
				if (userObj.getUserId()!=null)userObj.setUserId(userObj.getUserId().trim());
				if (userObj.getUserName()==null)userObj.setUserName("");
				if (userObj.getPasswd()==null)userObj.setPasswd("");
			}
		}
		return userList;
    }

	/**
	 * This method is overridden and not in use 
	 */
	@Override
	public void reset() {
	}
	
}
