package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiResultReasonForChange;
import com.honda.galc.entity.qi.QiResultReasonForChangeId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiReasonForChangeDetailDao Class description</h3>
 * <p>
 * QiReasonForChangeDetailDao description
 * </p>
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
 * @author Justin Jiang<br>
 *         November 3, 2020
 *
 */

public interface QiResultReasonForChangeDao extends IDaoService<QiResultReasonForChange, QiResultReasonForChangeId> {
	public List<QiResultReasonForChange> saveAll(List<QiResultReasonForChange> entities);
}
