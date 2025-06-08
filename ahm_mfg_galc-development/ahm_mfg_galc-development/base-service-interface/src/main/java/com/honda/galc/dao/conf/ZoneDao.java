package com.honda.galc.dao.conf;


import com.honda.galc.entity.conf.Zone;
import com.honda.galc.service.IDaoService;

import java.util.List;


public interface ZoneDao extends IDaoService<Zone, String> {

    public List<Zone> findAllByDivisionId(String divisionId);
}
