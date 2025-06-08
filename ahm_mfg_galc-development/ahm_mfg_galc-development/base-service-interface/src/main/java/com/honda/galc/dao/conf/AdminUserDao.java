package com.honda.galc.dao.conf;


import java.util.List;

import com.honda.galc.entity.conf.AdminUser;
import com.honda.galc.service.IDaoService;

public interface AdminUserDao extends IDaoService<AdminUser, String> {
	
	public List<AdminUser> findAllMatchUserId(String wildcard);
	public AdminUser update(AdminUser user, boolean updatePassword);
	
}
