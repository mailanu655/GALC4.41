package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.HoldReasonDao;
import com.honda.galc.entity.product.HoldReason;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HoldReasonDaoImpl</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Prasanna Parvathaneni
 */
public class HoldReasonDaoImpl extends BaseDaoImpl<HoldReason, Integer>
		implements HoldReasonDao {

	private static final long serialVersionUID = 1L;

	@Transactional
	public HoldReason insert(HoldReason entity) {
		Integer max = max("reasonId", Integer.class);
		int maxId = max == null ? 1 : max + 1;
		entity.setReasonId(maxId);
		return super.insert(entity);
	}

	public HoldReason findByReasonId(String reasonId) {		
		return findFirst(Parameters.with("reasonId", reasonId));
	}

	public List<HoldReason> findAllByDivisionId(String divisionId) {
		return findAll(Parameters.with("divisionId",divisionId));
	}

	public HoldReason findByHoldReason(String holdReason) {
		return findFirst(Parameters.with("holdReason", holdReason));
	}

}
