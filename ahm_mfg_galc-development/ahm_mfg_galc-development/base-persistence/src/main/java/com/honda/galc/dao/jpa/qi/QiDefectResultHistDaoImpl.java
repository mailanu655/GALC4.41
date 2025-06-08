package com.honda.galc.dao.jpa.qi;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiDefectResultHistDao;
import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiDefectResultHistDaoImpl Class description</h3>
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
 * @author L&T Infotech<br>
 */

public class QiDefectResultHistDaoImpl extends BaseDaoImpl<QiDefectResultHist, Integer> implements QiDefectResultHistDao{
	
	public QiDefectResultHist findFirstDefectResultHistory(long defectResultId) {
		return findFirst(Parameters.with("id.defectResultId", defectResultId), new String[]{"id.changeTimestamp"}, false);
	}
}
