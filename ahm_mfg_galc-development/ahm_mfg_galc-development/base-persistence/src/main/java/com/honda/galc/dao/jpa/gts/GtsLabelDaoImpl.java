package com.honda.galc.dao.jpa.gts;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsLabelDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsLabel;
import com.honda.galc.entity.gts.GtsLabelId;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsLabelDaoImpl Class description</h3>
 * <p> GtsLabelDaoImpl description </p>
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
public class GtsLabelDaoImpl extends BaseDaoImpl<GtsLabel,GtsLabelId> implements GtsLabelDao{

	public List<GtsLabel> findAll(String trackingArea) {
		return findAll(Parameters.with("id.trackingArea", trackingArea));
	}
	
	@Transactional
	public GtsLabel update(GtsLabel label) {
		GtsLabel updated = super.update(label);
		return updated;
	}
	
	@Transactional
	public void remove(GtsLabel label) {
		super.remove(label);
	}
	
	@Transactional
	public GtsLabel insert(GtsLabel label){
		Integer maxId = getNextLabelId(label.getId().getTrackingArea());
		label.getId().setLabelId(maxId);
		GtsLabel newLabel = super.insert(label);
		return newLabel;
	}
	
	public Integer getNextLabelId(String areaName){
		Parameters params = Parameters.with("id.trackingArea", areaName);
		Integer maxSeq = max("id.labelId", Integer.class, params);
		return maxSeq == null ? 1 : maxSeq + 1;
	}
}
