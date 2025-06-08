package com.honda.galc.dao.conf;


import java.util.List;

import com.honda.galc.entity.conf.AdminGroup;
import com.honda.galc.service.IDaoService;

public interface AdminGroupDao extends IDaoService<AdminGroup, String> {
	public List<AdminGroup> findAllMatchGroupId(String wildcard);
}
