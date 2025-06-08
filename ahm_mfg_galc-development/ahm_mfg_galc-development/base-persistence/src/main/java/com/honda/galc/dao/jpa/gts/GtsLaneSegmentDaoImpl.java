package com.honda.galc.dao.jpa.gts;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsLaneSegmentDao;
import com.honda.galc.dao.gts.GtsLaneSegmentMapDao;
import com.honda.galc.dao.gts.GtsNodeDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsLaneSegment;
import com.honda.galc.entity.gts.GtsLaneSegmentId;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsNodeId;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsLaneSegmentDaoImpl Class description</h3>
 * <p> GtsLaneSegmentDaoImpl description </p>
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
 * May 27, 2015
 *
 *
 */
public class GtsLaneSegmentDaoImpl extends BaseDaoImpl<GtsLaneSegment,GtsLaneSegmentId> implements GtsLaneSegmentDao{
	
	private static final String FIND_ALL_BY_NODE_ID = "select e from GtsLaneSegment e where e.id.trackingArea = :trackingArea and (e.inputNodeId = :nodeId or e.outputNodeId = :nodeId)";
	
	@Autowired
	GtsNodeDao nodeDao;
	
	@Autowired
	GtsLaneSegmentMapDao laneSegmentMapDao;
	
	public List<GtsLaneSegment> findAll(String trackingArea) {
		return findAll(Parameters.with("id.trackingArea",trackingArea));
	}
	
	@Transactional
	@Override
	public GtsLaneSegment update(GtsLaneSegment laneSegment) {
		GtsLaneSegment updated = super.update(laneSegment);
		List<GtsLaneSegment> updatedList = new ArrayList<GtsLaneSegment>();
		updatedList.add(updated);
		return updated;
	}
	
	@Transactional
	@Override
	public void updateAll(List<GtsLaneSegment> laneSegments) {
		super.updateAll(laneSegments);
	}
	
	 /**
     * Remove a lane segment
     * 1 -- remove lane segment from GBS_LANE_SEGMENT table
     * 2 -- if the input node or the output node of the lane segment is not connected to any other lane segment remove the node
     * 3 -- Remove lane segment from GBS_LANE_SEGMENT_MAP table
     * 4 -- update lane segment seq in the GBS_LANE_SEGMENT_MAP for the lane
     * 5 -- update capacity of the lane
     * @param segment
     * @throws SystemException
     */
   
	@Transactional
	@Override
	public void remove(GtsLaneSegment laneSegment) {
		if(!laneSegment.getLaneSegmentMaps().isEmpty()) laneSegmentMapDao.removeAll(laneSegment.getLaneSegmentMaps());
		
		List<GtsLaneSegment> segments = findAllByNodeId(laneSegment.getId().getTrackingArea(),laneSegment.getInputNodeId());
		
		if(segments.size() <= 1) nodeDao.remove(laneSegment.getInputNode());
		
		segments = findAllByNodeId(laneSegment.getId().getTrackingArea(),laneSegment.getOutputNodeId());
		
		if(segments.size() <= 1) nodeDao.remove(laneSegment.getOutputNode());
		
		super.remove(laneSegment);
		
	}
	
	
	private List<GtsLaneSegment> findAllByNodeId(String trackingArea, int nodeId) {
		return findAllByQuery(FIND_ALL_BY_NODE_ID,Parameters.with("trackingArea",trackingArea).put("nodeId", nodeId));
	}
	
	@Transactional
	@Override
	public GtsLaneSegment insert(GtsLaneSegment laneSegment){
		GtsLaneSegment newLaneSegment = super.insert(laneSegment);
		return newLaneSegment;
	}
	
	/**
     * create a lane segment which connects to an existing lane segment if input node id or output node id is set in the segment arguement
     * Otherwise, create a separate lane segment
     * request is normally from the editing client
     * @param segment - contains either input node id or output node id 
     * @return
     * 
     */
    
    @Transactional
	public GtsLaneSegment createLaneSegment(GtsLaneSegment segment){
        if(segment.getInputNodeId() != -1){
        	GtsNode inNode = nodeDao.findByKey(new GtsNodeId(segment.getId().getTrackingArea(),segment.getInputNodeId()));
            segment.setControlPoint1(inNode.getX(), inNode.getY());
        }else {
        	addNode(segment,true);
        };
        
        if(segment.getOutputNodeId() != -1){
        	GtsNode inNode = nodeDao.findByKey(new GtsNodeId(segment.getId().getTrackingArea(),segment.getOutputNodeId()));
            segment.setControlPoint1(inNode.getX(), inNode.getY());
        }else {
        	addNode(segment,false);
        };
        
        int nextLaneSegmentId = getNextLaneSegmentId(segment.getId().getTrackingArea());
        segment.getId().setLaneSegmentId(nextLaneSegmentId);
        return insert(segment);
        
    } 
    
    private void addNode(GtsLaneSegment segment, boolean isInputNode) {
    	int nodeId = nodeDao.getNextNodeId(segment.getId().getTrackingArea());
        GtsNode node = new GtsNode(segment.getId().getTrackingArea(),nodeId);
    	if(isInputNode) {
    		node.setX(segment.getControlPointX1());
            node.setY(segment.getControlPointY1());
            segment.setInputNodeId(nodeId);
            segment.setInputNode(node);
    	}else {
    		node.setX(segment.getControlPointX2());
    	    node.setY(segment.getControlPointY2());
    	    segment.setOutputNodeId(nodeId);
    	    segment.setOutputNode(node);
    	}
    	
    	nodeDao.insert(node);
	    
    }
    
    public Integer getNextLaneSegmentId(String areaName){
		Parameters params = Parameters.with("id.trackingArea", areaName);
		Integer maxSeq = max("id.laneSegmentId", Integer.class, params);
		return maxSeq == null ? 1 : maxSeq + 1;
	}

	@Transactional
    public GtsLaneSegment replaceNode(GtsLaneSegment segment, int fromId,int toId) {
		GtsNode node = null;
		if(segment.getInputNodeId() == fromId){
			segment.setInputNodeId(toId);
			node = segment.getInputNode();
		}else if(segment.getOutputNodeId() == fromId) {
			segment.setOutputNodeId(toId);
			node = segment.getOutputNode();
		}
		
		if(node != null) {
			super.update(segment);
			nodeDao.remove(node);
		}
		
		return segment;
	}

}
