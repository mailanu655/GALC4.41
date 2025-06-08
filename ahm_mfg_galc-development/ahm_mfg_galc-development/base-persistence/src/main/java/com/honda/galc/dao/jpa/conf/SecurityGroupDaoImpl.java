package com.honda.galc.dao.jpa.conf;

import java.util.List;

import com.honda.galc.dao.conf.SecurityGroupDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.SecurityGroup;



public class SecurityGroupDaoImpl extends BaseDaoImpl<SecurityGroup,String> implements SecurityGroupDao {

    private static final String FIND_ALL_WILDCARD = "Select a from SecurityGroup a where a.securityGroup like ";

    /**
	 * This method is to search All SecurityGroup using wildcard
	 */
	public List<SecurityGroup> findAllMatchGroupId(final String wildcard) {
		return findAllByQuery(FIND_ALL_WILDCARD + "'" + wildcard + "'");
	}
	
	/**
	 * This method is to find  find And Sort All SecurityGroup
	 */
	public List<SecurityGroup> findAllSecurityGroup (){
		return findAll(null,new String[]{"securityGroup"},true);
	}
}
