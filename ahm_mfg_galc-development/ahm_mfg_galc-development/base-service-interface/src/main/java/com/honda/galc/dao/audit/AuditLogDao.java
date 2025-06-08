package com.honda.galc.dao.audit;

import java.util.List;

import com.honda.galc.entity.audit.AuditLog;
import com.honda.galc.entity.audit.AuditLogId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>AuditLogDao Class description</h3>
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
public interface AuditLogDao extends IDaoService<AuditLog, AuditLogId> {

public List<AuditLog> findPdcHistory(long localDefectCombinationId);

}
