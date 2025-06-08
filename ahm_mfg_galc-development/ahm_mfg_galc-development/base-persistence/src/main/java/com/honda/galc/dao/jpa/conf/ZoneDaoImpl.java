package com.honda.galc.dao.jpa.conf;


import java.util.List;

import com.honda.galc.dao.conf.ZoneDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.Zone;
import com.honda.galc.service.Parameters;


public class ZoneDaoImpl extends BaseDaoImpl<Zone,String> implements ZoneDao {
    
    public List<Zone> findAllByDivisionId(String divisionId) {
          Parameters params = Parameters.with("divisionId", divisionId);
          return findAll(params);
    }

}
