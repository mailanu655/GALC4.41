package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.User;
import com.honda.galc.service.IDaoService;


public interface UserDao extends IDaoService<User, String> {

	
	List<User> findAllUsersByFilter(final String filterString);

	List<User> findAllSortedByUserId();
	
	public List<String> findAllUser();
}
