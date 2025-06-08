package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.AccessControlEntry;
import com.honda.galc.entity.conf.AccessControlEntryId;
import com.honda.galc.entity.conf.UserSecurityGroupId;
import com.honda.galc.service.IDaoService;


public interface AccessControlEntryDao extends IDaoService<AccessControlEntry, AccessControlEntryId> {

	/**
	 * test if the userId has the access to the screen Id
	 * @param userId
	 * @param screenId
	 * @return
	 */
	public boolean isAccessPermitted(String userId, String screenId);
	
	public boolean isAccessPermitted(List<UserSecurityGroupId>groups, String screenId);
	
	public List<AccessControlEntry> findAllByScreenId(String screenId);
	
}
