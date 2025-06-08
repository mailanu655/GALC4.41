package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.SecurityGroup;
import com.honda.galc.service.IDaoService;


public interface SecurityGroupDao extends IDaoService<SecurityGroup, String> {

	public List<SecurityGroup> findAllMatchGroupId(String wildcard);
	
	public List<SecurityGroup> findAllSecurityGroup ();
	
}
