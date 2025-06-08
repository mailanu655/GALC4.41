package com.honda.galc.dao.conf;

import com.honda.galc.entity.conf.DCZone;
import com.honda.galc.entity.conf.DCZoneId;
import com.honda.galc.service.IDaoService;

import java.util.List;

public interface DCZoneDao extends IDaoService<DCZone, DCZoneId> {  
	public List<DCZone> findAllByZoneId(String zoneId);   
	public List<DCZone> findAllRepairableByZoneId(String zoneId);
    public List<DCZone> findAllByProcessPointId(String processPointId);
}
