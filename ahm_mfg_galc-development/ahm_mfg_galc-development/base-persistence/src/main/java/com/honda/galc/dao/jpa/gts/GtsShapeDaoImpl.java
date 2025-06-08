package com.honda.galc.dao.jpa.gts;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsShapeDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsShape;
import com.honda.galc.entity.gts.GtsShapeId;
import com.honda.galc.notification.GtsNotificationSender;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsShapeDaoImpl Class description</h3>
 * <p> GtsShapeDaoImpl description </p>
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
 * Jun 5, 2015
 *
 *
 */
public class GtsShapeDaoImpl extends BaseDaoImpl<GtsShape,GtsShapeId> implements GtsShapeDao{

	public List<GtsShape> findAll(String trackingArea) {
		return findAll(Parameters.with("id.trackingArea", trackingArea));
	}
	
	@Transactional
	public GtsShape update(GtsShape shape) {
		GtsShape updated = super.update(shape);
		GtsNotificationSender.getNotificationService(shape.getId().getTrackingArea()).shapeUpdated(shape);
		return updated;
	}
	
	@Transactional
	public void remove(GtsShape shape) {
		super.remove(shape);
		GtsNotificationSender.getNotificationService(shape.getId().getTrackingArea()).shapeRemoved(shape);
	}
	
	@Transactional
	public GtsShape insert(GtsShape shape){
		Integer maxId = getNextShapeId(shape.getId().getTrackingArea());
		shape.getId().setShapeId(maxId);
		GtsShape newShape = super.insert(shape);
		GtsNotificationSender.getNotificationService(shape.getId().getTrackingArea()).shapeCreated(shape);
		return newShape;
	}
	
	public Integer getNextShapeId(String areaName){
		Parameters params = Parameters.with("id.trackingArea", areaName);
		Integer maxSeq = max("id.shapeId", Integer.class, params);
		return maxSeq == null ? 1 : maxSeq + 1;
	}
	
}
