package com.honda.galc.dao.jpa.gts;

import java.util.List;

import com.honda.galc.dao.gts.GtsMoveDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsMoveId;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsMoveDaoImpl Class description</h3>
 * <p> GtsMoveDaoImpl description </p>
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
public class GtsMoveDaoImpl extends BaseDaoImpl<GtsMove,GtsMoveId> implements GtsMoveDao{

	public void moveCarrier(String trackingArea, String sourceLane,String destination) {

		
		
	}

	public List<GtsMove> findAll(String trackingArea) {
		return findAll(Parameters.with("id.trackingArea", trackingArea));
	}

}
