package com.honda.galc.dao.jpa.conf;



import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.UserSecurityGroupDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.UserSecurityGroup;
import com.honda.galc.entity.conf.UserSecurityGroupId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>UserSecurityGroupDaoImpl Class description</h3>
 * <p> UserSecurityGroupDaoImpl description </p>
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
 * Mar 28, 2011
 *
 *
 */
public class UserSecurityGroupDaoImpl extends BaseDaoImpl<UserSecurityGroup,UserSecurityGroupId> implements UserSecurityGroupDao {

    private static final long serialVersionUID = 1L;
 
    public UserSecurityGroup findById(String userId, String securityGroup) {
        if (userId == null || userId.trim().length() < 1) {
            throw new NullPointerException("UserId is NULL or empty!");
        }
        
        return findByKey(new UserSecurityGroupId(userId, securityGroup));
        
   }

    public List<UserSecurityGroup> findAllByUserId(String uid) {

    	return findAll(Parameters.with("id.userId", uid));
    	
    }

	public boolean isUserInSecurityGroup(String userId, String securityGroup) {
		Parameters params = Parameters.with("id.userId", userId)
		                    .put("id.securityGroup", securityGroup);

		List<UserSecurityGroup> acls = this.findAll(params);
        
		return acls != null && !acls.isEmpty();
	}

	@Transactional
	public void deleteAllByUserId(String userId) {
		
		delete(Parameters.with("id.userId", userId));
		
	}
}
