package com.honda.galc.dao.jpa;

import java.util.List;

import com.honda.galc.dao.audit.AuditLogDao;
import com.honda.galc.entity.audit.AuditLog;
import com.honda.galc.entity.audit.AuditLogId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>AuditLogDaoImpl Class description</h3>
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
 * <TR>
 * <TD>1.0</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 * </TABLE>
 * 
 * @author L&T Infotech<br>
 *         October 10, 2016
 *
 *
 */
public class AuditLogDaoImpl extends BaseDaoImpl<AuditLog, AuditLogId> implements AuditLogDao {

	private static final String FIND_PDC_HISTORY = "select e " +
			" from AuditLog e " +
			" where e.system='QICS' "+
			" and e.maintenanceScreen in ('Local Attribute', 'Loading PDDA') " + 
			" and e.id.columnName <> 'PCL_TO_QICS_SEQ_KEY' " +
			" and e.primaryKey = :pk " +
			" order by e.id.actualTimeStamp desc";
	
	
	public List<AuditLog> findPdcHistory(long localDefectCombinationId) {
		Parameters params = Parameters.with("pk",  String.valueOf(localDefectCombinationId));
		java.util.List<AuditLog> pdcHistory = findAllByQuery(FIND_PDC_HISTORY, params);
		return pdcHistory;
	}
}
