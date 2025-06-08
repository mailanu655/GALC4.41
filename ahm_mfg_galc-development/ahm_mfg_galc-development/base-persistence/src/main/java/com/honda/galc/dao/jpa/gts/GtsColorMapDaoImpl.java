package com.honda.galc.dao.jpa.gts;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsColorMapDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsColorMap;
import com.honda.galc.entity.gts.GtsColorMapId;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsColorMapDaoImpl Class description</h3>
 * <p> GtsColorMapDaoImpl description </p>
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
public class GtsColorMapDaoImpl extends BaseDaoImpl<GtsColorMap,GtsColorMapId> implements GtsColorMapDao{
	
	public List<GtsColorMap> findAll(String trackingArea){
		return findAll(Parameters.with("id.trackingArea", trackingArea));
	}
	
	public Integer getNextId(String areaName){
		Parameters params = Parameters.with("id.trackingArea", areaName);
		Integer maxSeq = max("id.colorId", Integer.class, params);
		return maxSeq == null ? 1 : maxSeq + 1;
	}
	
	@Transactional
	public GtsColorMap insert(GtsColorMap colorMap){
		colorMap.getId().setColorId(getNextId(colorMap.getId().getTrackingArea()));
		GtsColorMap newColorMap = super.insert(colorMap);
//		GtsNotificationSender.getNotificationService(node.getId().getTrackingArea()).shapeCreated(node);
		return newColorMap;
	}
	
}
