package com.honda.galc.dao.jpa.gts;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsDecisionPointDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsDecisionPoint;
import com.honda.galc.entity.gts.GtsDecisionPointId;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsDecisionPointDaoImpl Class description</h3>
 * <p> GtsDecisionPointDaoImpl description </p>
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
 * Jun 25, 2015
 *
 *
 */
public class GtsDecisionPointDaoImpl extends BaseDaoImpl<GtsDecisionPoint,GtsDecisionPointId> implements GtsDecisionPointDao{

	public List<GtsDecisionPoint> findAll(String trackingArea) {
		return findAll(Parameters.with("id.trackingArea", trackingArea));
	}

	public Integer getNextId(String areaName){
		Parameters params = Parameters.with("id.trackingArea", areaName);
		Integer maxSeq = max("id.decisionPointId", Integer.class, params);
		return maxSeq == null ? 1 : maxSeq + 1;
	}
	
	@Transactional
	public GtsDecisionPoint insert(GtsDecisionPoint decisionPoint){
		decisionPoint.getId().setDecisionPointId(getNextId(decisionPoint.getId().getTrackingArea()));
		GtsDecisionPoint newDecisionPoint = super.insert(decisionPoint);
//		GtsNotificationSender.getNotificationService(node.getId().getTrackingArea()).shapeCreated(node);
		return newDecisionPoint;
	}


	
}
