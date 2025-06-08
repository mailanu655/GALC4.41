package com.honda.galc.dao.jpa.conf;

import java.util.List;


import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.BroadcastDestinationId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>BroadcastDestinationDaoImpl Class description</h3>
 * <p> BroadcastDestinationDaoImpl description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * May 17, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class BroadcastDestinationDaoImpl extends BaseDaoImpl<BroadcastDestination, BroadcastDestinationId> implements BroadcastDestinationDao {

    public List<BroadcastDestination> findAllByProcessPointId(String processPointId) {
        return findAll(Parameters.with("id.processPointId", processPointId));
    }

	public List<BroadcastDestination> findAllByProcessPointId(String processPointId, boolean autoEnabled) {
		 return findAll(Parameters.with("id.processPointId", processPointId).put("autoEnabled", autoEnabled),new String[] {"id.sequenceNumber"});
	}

	public List<BroadcastDestination> findAllByReqestId(String requestId) {
		return findAll(Parameters.with("requestId", requestId),new String[]{"id.sequenceNumber"},true);
	}
	
	public List<BroadcastDestination> findAllByProcessPointIdAndCheckPoint(String processPointId, CheckPoints checkPoint) {
		return findAll(Parameters.with("id.processPointId", processPointId).put("checkPoint", checkPoint), new String[] { "id.sequenceNumber" }, true);
	}
}
