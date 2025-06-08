package com.honda.galc.dao.jpa.qi;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiResultReasonForChangeDao;
import com.honda.galc.entity.qi.QiResultReasonForChange;
import com.honda.galc.entity.qi.QiResultReasonForChangeId;

/**
 * 
 * <h3>QiResultReasonForChangeDaoImpl Class description</h3>
 * <p>
 * QiResultReasonForChangeDaoImpl description
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

public class QiResultReasonForChangeDaoImpl extends BaseDaoImpl<QiResultReasonForChange, QiResultReasonForChangeId>
		implements QiResultReasonForChangeDao {

	@Override
	@Transactional
	public List<QiResultReasonForChange> saveAll(List<QiResultReasonForChange> entities) {
		Timestamp timestamp = getDatabaseTimeStamp();
		for (QiResultReasonForChange entity : entities) {
			entity.getId().setChangeTimestamp(timestamp);
		}
		return super.saveAll(entities);
	}
}
