package com.honda.galc.dao.jpa.conf;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.conf.AccessControlEntryDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.AccessControlEntry;
import com.honda.galc.entity.conf.AccessControlEntryId;
import com.honda.galc.entity.conf.UserSecurityGroupId;
import com.honda.galc.service.Parameters;


public class AccessControlEntryDaoImpl extends BaseDaoImpl<AccessControlEntry,AccessControlEntryId> implements AccessControlEntryDao {

    private static final long serialVersionUID = 1L;
    
	private final String SELECT_ENTRY = "select b from UserSecurityGroup a,AccessControlEntry b where a.id.userId = :userId and a.id.securityGroup = b.id.securityGroup and b.id.screenId= :screenId and b.operation > 0";

	public boolean isAccessPermitted(String userId, String screenId) {

		Parameters params = Parameters.with("userId", userId).put("screenId", screenId);

		List<AccessControlEntry> acls = this.findAllByQuery(SELECT_ENTRY, params);
        
		return acls != null && !acls.isEmpty();
		
	}

	public boolean isAccessPermitted(List<UserSecurityGroupId>groups, String screenId) {
		
		List<AccessControlEntry> acls = new ArrayList<AccessControlEntry>();
        for (UserSecurityGroupId group : groups) {
        	String securityGroup = group.getSecurityGroup();
        	AccessControlEntry entry = this.findByKey(new AccessControlEntryId(screenId,securityGroup));	
        	if(entry != null && entry.getOperation() > 0) {
        		acls.add(entry);
        	}
		}
		return acls != null && !acls.isEmpty();		
	}

	public List<AccessControlEntry> findAllByScreenId(String screenId) {
		return this.findAll(Parameters.with("id.screenId", screenId));
	}


}
