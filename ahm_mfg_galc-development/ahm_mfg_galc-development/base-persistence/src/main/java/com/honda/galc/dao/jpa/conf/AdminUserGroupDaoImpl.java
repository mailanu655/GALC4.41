package com.honda.galc.dao.jpa.conf;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.AdminUserGroupDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.AdminUserGroup;
import com.honda.galc.entity.conf.AdminUserGroupId;
import com.honda.galc.service.Parameters;


public class AdminUserGroupDaoImpl extends BaseDaoImpl<AdminUserGroup,AdminUserGroupId> implements AdminUserGroupDao {

	public List<AdminUserGroup> findAllByUserId(String userId) {
		
		return findAll(Parameters.with("id.userId", userId),new String[] {"id.groupId"},true);
		
	}

	@Transactional
	public void deleteAllByUserId(String userId) {
		
		delete(Parameters.with("id.userId", userId));
		
	}
	
	

}
