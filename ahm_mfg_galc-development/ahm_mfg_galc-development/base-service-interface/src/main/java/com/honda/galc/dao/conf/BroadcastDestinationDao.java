package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.BroadcastDestinationId;
import com.honda.galc.service.IDaoService;

public interface BroadcastDestinationDao extends IDaoService<BroadcastDestination, BroadcastDestinationId> {

    public List<BroadcastDestination> findAllByProcessPointId(String processPointId);
    public List<BroadcastDestination> findAllByProcessPointId(String processPointId, boolean autoEnabled);
    public List<BroadcastDestination> findAllByReqestId(String requestId);
    public List<BroadcastDestination> findAllByProcessPointIdAndCheckPoint(String processPointId, CheckPoints checkPoint);
    
}
