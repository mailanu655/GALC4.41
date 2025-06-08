package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.dao.conf.SecurityGroupDao;
import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.dao.conf.UserSecurityGroupDao;
import com.honda.galc.entity.conf.SecurityGroup;
import com.honda.galc.entity.conf.User;
import com.honda.galc.entity.conf.UserSecurityGroup;
import com.honda.galc.system.config.web.forms.LDAPForm;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.SortedArrayList;

/**
 * @version 	1.0
 * @author
 */
public class LDAPSettingsAction extends ConfigurationAction

{
    public LDAPSettingsAction() {
        super();
    }
    
    private static final String ERRORS_GROUP = "UserErrors";
    
    private static final String MESSAGES_GROUP = "updateUserMessages";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        
        LDAPForm lDAPForm = (LDAPForm)form;
        String operation = lDAPForm.getOperation();
        
        try {
            if (request.isUserInRole("EditUsers"))
            {
                lDAPForm.setEditor(true);
            }
            if (operation != null && operation.equalsIgnoreCase("delete")
                    || (operation != null && operation.equalsIgnoreCase("apply"))) {
                if (!request.isUserInRole("EditUsers")){
                    String userId = lDAPForm.getUserID();
    	            List<UserSecurityGroup> userSecurityGroups = getDao(UserSecurityGroupDao.class).findAllByUserId(userId);
	                lDAPForm.setUserSecurityGroups(userSecurityGroups);
    	           
	                List<SecurityGroup> securityGroups = getDao(SecurityGroupDao.class).findAll();
	                lDAPForm.setSecurityGroups(securityGroups);
                }
            }
            lDAPForm.setFreshUserList(false);
            if (operation == null || operation.trim().equalsIgnoreCase("")) 
            {
                //do nothing here, just check operation is null or not
            }
            //enter detail information page of a specified user
            else if (operation.equalsIgnoreCase("query") 
                    || operation.equalsIgnoreCase("create")
                    || operation.equalsIgnoreCase("cancel"))
            {
                if (operation.equalsIgnoreCase("query")|| operation.equalsIgnoreCase("cancel"))
                {
                    lDAPForm.setCreateFlag("false");
                    
	                //initial detail information for specified user
	                String userId = lDAPForm.getUserID();
	                
	                User user = getDao(UserDao.class).findByKey(userId);
	                lDAPForm.setUser(user);
	                
	                //initial user security groups information
                	List<UserSecurityGroup> userSecurityGroups = getDao(UserSecurityGroupDao.class).findAllByUserId(userId);
	                lDAPForm.setUserSecurityGroups(userSecurityGroups);
                }else if (operation.equalsIgnoreCase("create")) {
                    lDAPForm.reset(mapping, request);
                    if (request.isUserInRole("EditUsers"))
                    {
                        lDAPForm.setEditor(true);
                    }
                    lDAPForm.setCreateFlag("true");
                }
                //initial common sercurity groups data
                List<SecurityGroup> securityGroups = getDao(SecurityGroupDao.class).findAll();
                lDAPForm.setSecurityGroups(new SortedArrayList<SecurityGroup>(securityGroups,"getGroupName"));
            }
            //delete user 
            else if (operation.equalsIgnoreCase("delete")) {
                
                if (!lDAPForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
                if (!lDAPForm.isDeleteConfirmed())
                {
                    throw new ConfigurationServicesException("CFGW1001");
                }

                String userId = lDAPForm.getUserID();
                //remove user and his or her security groups
                getDao(UserDao.class).removeByKey(userId);
                lDAPForm.reset(mapping, request);
                lDAPForm.setFreshUserList(true);
                messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2041", userId));
                
            }
            //create new user or update a user
            else if (operation.equalsIgnoreCase("apply")) {
                User user = lDAPForm.getUser();
                
                //password and confirmation password should be same
                String pwd = lDAPForm.getPasswd();
                String cofPwd = lDAPForm.getCnfPwd();
                if (pwd == null || cofPwd == null || !pwd.equals(cofPwd)) {
                    throw new ConfigurationServicesException("CFGW2043");
                }
                
                //set password update date
                String oldPassword = lDAPForm.getOldPassword();
                String passwd = lDAPForm.getPasswd();
                if ((oldPassword == null && passwd != null) 
                    || (oldPassword != null && passwd == null)
                    || (oldPassword != null && passwd != null && !oldPassword.equals(passwd))){
                	user.setPasswordUpdateDate(getCurrentDate());
                }
                
                String[] assignedSecurityGroups = lDAPForm.getAssignedSecurityGroup();
                
                List<UserSecurityGroup> userSecurityGroups = getDao(UserSecurityGroupDao.class).findAllByUserId(lDAPForm.getUserID());
                
                List<UserSecurityGroup> updatedSecurityGroups = new ArrayList<UserSecurityGroup>();
                List<UserSecurityGroup> removedSecurityGroups = new ArrayList<UserSecurityGroup>();
                
                for(UserSecurityGroup item : userSecurityGroups) {
                	if(!contains(item.getId().getSecurityGroup(),assignedSecurityGroups))
                		removedSecurityGroups.add(item);
                }
                
                for(String groupId : assignedSecurityGroups) {
                	updatedSecurityGroups.add(findUserSecurityGroup(lDAPForm.getUserID(), groupId, userSecurityGroups));
                }
                
               	if(getDao(UserDao.class).findByKey(lDAPForm.getUserID()) == null)
               		lDAPForm.setCreateFlag("false");
                
                if (lDAPForm.getCreateFlag() != null && lDAPForm.getCreateFlag().equalsIgnoreCase("true"))
                {
                	getDao(UserDao.class).insert(user);
                	getDao(UserSecurityGroupDao.class).saveAll(updatedSecurityGroups);
                     messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2042", lDAPForm.getUserID()));
                    lDAPForm.setCreateFlag("false");
                }else{
                    //update user
                	getDao(UserDao.class).update(user);
                	if(!removedSecurityGroups.isEmpty()) getDao(UserSecurityGroupDao.class).removeAll(removedSecurityGroups);
                	getDao(UserSecurityGroupDao.class).saveAll(updatedSecurityGroups);
                    
                	messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2040", lDAPForm.getUserID()));
                }
                
                lDAPForm.setOperation("query");
                lDAPForm.setFreshUserList(true);
                lDAPForm.setAssignedSecurityGroup(null);
                lDAPForm.setSelectedSecurityGroup(null);
                
                //initial detail information for specified user
                String userId = lDAPForm.getUserID();
                user = getDao(UserDao.class).findByKey(userId);
                lDAPForm.setUser(user);
                
                //initial user security groups information
               	List<UserSecurityGroup> groups = getDao(UserSecurityGroupDao.class).findAllByUserId(userId);
                lDAPForm.setUserSecurityGroups(groups);
 
                //initial common sercurity groups data
                List<SecurityGroup> tempSecurityGroups = getDao(SecurityGroupDao.class).findAll();
                lDAPForm.setSecurityGroups(new SortedArrayList<SecurityGroup>(tempSecurityGroups,"getGroupName"));
                
            }

            if (lDAPForm.getUserID() != null 
                    && lDAPForm.getUserID().trim().length()>0
                    && operation.equalsIgnoreCase("create"))
            {
	            //initial detail information for specified user
	            String userId = lDAPForm.getUserID();
	            User user = getDao(UserDao.class).findByKey(userId);
	            lDAPForm.setUser(user);
	            
	            //initial user security groups information
               	List<UserSecurityGroup> userSecurityGroups = getDao(UserSecurityGroupDao.class).findAllByUserId(userId);
                lDAPForm.setUserSecurityGroups(userSecurityGroups);
 
            }

        }
        
        catch (Exception e) {
            e.printStackTrace();
        	// Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",getRootCauseMessage(e)));
        }  

        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);

    }
    
    private boolean contains(String groupId,String[] groupIds) {
    	
    	for(String item : groupIds) {
    		if(item.equals(groupId)) return true;
    	}
    	return false;
    	
    }
    
    private UserSecurityGroup findUserSecurityGroup(String userId,String groupId, List<UserSecurityGroup> groups){
    	
    	for(UserSecurityGroup item : groups) {
    		if(item.getId().getSecurityGroup().equals(groupId)) return item;
    	}
    	
    	UserSecurityGroup userSecurityGroup = new UserSecurityGroup(userId,groupId);
    	userSecurityGroup.setCreateTimestamp(CommonUtil.getTimestampNow());
    	return userSecurityGroup;
    	
    }
    
    private String getCurrentDate() {
        String DATE_FORMAT = "yyyyMMdd";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance(); // today
        String result = sdf.format(c1.getTime());
        return result;
    }


}
