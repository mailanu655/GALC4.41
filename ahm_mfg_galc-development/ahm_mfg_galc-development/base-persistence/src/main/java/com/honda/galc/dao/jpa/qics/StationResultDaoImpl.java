package com.honda.galc.dao.jpa.qics;

import java.sql.Date;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.StationResultDao;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.entity.qics.StationResultId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>StationResultDaoImpl Class description</h3>
 * <p> StationResultDaoImpl description </p>
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
 * Feb 28, 2011
 *
 *
 */
public class StationResultDaoImpl extends BaseDaoImpl<StationResult,StationResultId> implements StationResultDao{
	
	public List<StationResult> findAllByProductionDate(Date productionDate) {
		return findAll(Parameters.with("id.productionDate", productionDate),
					   new String[]{"id.applicationId", "id.shift"},true);
	}

	public List<StationResult> findAllByApplicationIdAndProductionDate(
			Date productionDate, String appId) {
		return findAll(Parameters.with("id.productionDate", productionDate).put("id.applicationId", appId),
				   new String[]{"id.applicationId", "id.shift"},true);
	}
	
}
