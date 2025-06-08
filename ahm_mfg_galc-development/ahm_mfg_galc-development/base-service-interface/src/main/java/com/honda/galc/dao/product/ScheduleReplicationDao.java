package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ScheduleReplication;
import com.honda.galc.entity.product.ScheduleReplicationId;
import com.honda.galc.service.IDaoService;

public interface ScheduleReplicationDao extends IDaoService<ScheduleReplication, ScheduleReplicationId> {

	List<ScheduleReplication> findBySourceLocation(String string);

	List<ScheduleReplication> findBySourceLocAndDestLoc(String sourceProcessLocation, String destProcessLocation);
	
	List<ScheduleReplication> findBySourceLocAndDestLoc(String sourceProcessLocation, String destProcessLocation, String count);
	
	boolean isScheduleReplicationExist(String sourceProcessLocation, String destProcessLocation, String destSpecCode);

	List<List<String>> findAllDistinctSrcLocDestSpecCodeDestProcLoc();

}
