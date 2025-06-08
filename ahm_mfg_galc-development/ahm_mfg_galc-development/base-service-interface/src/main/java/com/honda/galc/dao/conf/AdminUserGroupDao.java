package com.honda.galc.dao.conf;


import java.util.List;

import com.honda.galc.entity.conf.AdminUserGroup;
import com.honda.galc.entity.conf.AdminUserGroupId;
import com.honda.galc.service.IDaoService;

public interface AdminUserGroupDao extends IDaoService<AdminUserGroup, AdminUserGroupId> {

		public List<AdminUserGroup> findAllByUserId(String userId);
		public void deleteAllByUserId(String userId);
}
