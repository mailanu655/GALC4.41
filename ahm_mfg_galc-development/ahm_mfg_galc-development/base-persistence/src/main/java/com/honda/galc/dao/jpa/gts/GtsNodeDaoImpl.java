package com.honda.galc.dao.jpa.gts;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsNodeDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsNodeId;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsNodeDaoImpl Class description</h3>
 * <p> GtsNodeDaoImpl description </p>
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
 * May 21, 2015
 *
 *
 */
public class GtsNodeDaoImpl extends BaseDaoImpl<GtsNode,GtsNodeId> implements GtsNodeDao{

	public List<GtsNode> findAll(String trackingArea) {
		return findAll(Parameters.with("id.trackingArea", trackingArea));
	}
	
	@Transactional
	public int updateNodeStatus(String trackingArea, String nodeName, int status) {
		Parameters params = Parameters.with("id.trackingArea", trackingArea).put("nodeName", nodeName);
		return update(Parameters.with("status",status),params);
	}
	
	@Transactional
	public void remove(GtsNode node) {
		super.remove(node);
	}
	
	@Transactional
	public GtsNode insert(GtsNode node){
		node.getId().setNodeId(getNextNodeId(node.getId().getTrackingArea()));
		GtsNode newNode = super.insert(node);
		return newNode;
	}

	public Integer getNextNodeId(String areaName){
		Parameters params = Parameters.with("id.trackingArea", areaName);
		Integer maxSeq = max("id.nodeId", Integer.class, params);
		return maxSeq == null ? 1 : maxSeq + 1;
	}

	public GtsNode findByNodeName(String trackingArea, String nodeName) {
		Parameters params = Parameters.with("id.trackingArea", trackingArea).put("nodeName", nodeName);
		return findFirst(params);
	}
	
}
