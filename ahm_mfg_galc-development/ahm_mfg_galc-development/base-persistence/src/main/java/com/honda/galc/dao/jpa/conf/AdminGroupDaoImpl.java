package com.honda.galc.dao.jpa.conf;


import java.util.List;

import com.honda.galc.dao.conf.AdminGroupDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.AdminGroup;


public class AdminGroupDaoImpl extends BaseDaoImpl<AdminGroup,String> implements AdminGroupDao {
	
	private static String FIND_ALL_WILDCARD = "Select a from AdminGroup a where a.groupId like ";

	public List<AdminGroup> findAllMatchGroupId(String wildcard) {
		
		return findAllByQuery(FIND_ALL_WILDCARD + "'" + wildcard + "'");
		
	}
    
}
