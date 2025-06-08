package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LotTraceabilityDao;
import com.honda.galc.entity.product.LotTraceability;
import com.honda.galc.entity.product.LotTraceabilityId;

/**
 * 
 * <h3>LotTraceabilityDaoImpl Class description</h3>
 * <p> LotTraceabilityDaoImpl description </p>
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
 * @author Vivek Bettada<br>
 * Mar 03, 2015
 *
 *
 */
public class LotTraceabilityDaoImpl extends BaseDaoImpl<LotTraceability,LotTraceabilityId> implements LotTraceabilityDao {

    private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.honda.galc.dao.product.LotTraceabilityDao#findById(java.lang.String, java.lang.String)
	 */
	public LotTraceability findById(String lsn, String kdLot) {
		return findByKey(new LotTraceabilityId(lsn, kdLot));
	}

}
