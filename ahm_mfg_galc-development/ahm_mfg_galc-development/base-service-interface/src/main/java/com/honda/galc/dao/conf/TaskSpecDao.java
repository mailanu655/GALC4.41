package com.honda.galc.dao.conf;


import com.honda.galc.entity.conf.TaskSpec;
import com.honda.galc.service.IDaoService;

import java.util.List;


public interface TaskSpecDao extends IDaoService<TaskSpec, String> {

    public List<TaskSpec> findAllByProcessPointId(String processPointId);
}
