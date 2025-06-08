package com.honda.galc.dao.jpa.conf;

import java.util.ArrayList;
import java.util.List;
import com.honda.galc.dao.conf.DCZoneDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.DCZone;
import com.honda.galc.entity.conf.DCZoneId;
import com.honda.galc.service.Parameters;

public class DCZoneDaoImpl extends BaseDaoImpl<DCZone, DCZoneId> implements DCZoneDao{

	public List<DCZone> findAllByZoneId(String zoneId) {		
    	return findAll(Parameters.with("id.zoneId", zoneId));
	}
	
	public List<DCZone> findAllRepairableByZoneId(String zoneId){
		List<DCZone> allByZoneId = findAllByZoneId(zoneId);
		List<DCZone> allRepairableByZoneId = new ArrayList<DCZone>();
		for (DCZone zone : allByZoneId)
			if (zone.getRepairable())
				allRepairableByZoneId.add(zone);
		return allRepairableByZoneId;
	}
	
    public List<DCZone> findAllByProcessPointId(String processPointId) {
    	return findAll(Parameters.with("id.processPointId", processPointId));
    }
}
