package com.honda.galc.dao.conf;


import com.honda.galc.entity.conf.UserSecurityGroup;
import com.honda.galc.entity.conf.UserSecurityGroupId;
import com.honda.galc.service.IDaoService;

import java.util.List;

/**
 * 
 * <h3>UserSecurityGroupDao Class description</h3>
 * <p> UserSecurityGroupDao description </p>
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
public interface UserSecurityGroupDao extends IDaoService<UserSecurityGroup, UserSecurityGroupId> {

    public UserSecurityGroup findById(String userId, String securityGroup);

    public List<UserSecurityGroup> findAllByUserId(String userId);

    public boolean isUserInSecurityGroup(String userId, String securityGroup);
    
    public void deleteAllByUserId(String userId);

}
