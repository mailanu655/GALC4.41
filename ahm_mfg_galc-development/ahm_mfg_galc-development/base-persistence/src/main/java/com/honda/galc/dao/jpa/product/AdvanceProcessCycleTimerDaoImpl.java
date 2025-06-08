/**
 * 
 */
package com.honda.galc.dao.jpa.product;



import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.AdvanceProcessCycleTimerDao;
import com.honda.galc.entity.product.AdvanceProcessCycleTimer;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>AdvanceProcessCycleTimerDaoImpl.java</h3>
 * <h3> Configuration for Advance Process Cycle Timer </h3>
 * <h4> Description </h4>
 * <p> AnnualCalendarDaoImpl.java description </p>
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
 * <TD>SK</TD>
 * <TD>May 16, 2018</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author vfc91343
 * @created May 16, 2018
 */
public class AdvanceProcessCycleTimerDaoImpl extends BaseDaoImpl<AdvanceProcessCycleTimer, String>
		implements AdvanceProcessCycleTimerDao {

	

	@Override
	public List<AdvanceProcessCycleTimer> findByLineId(String lineId) {
		Parameters params = new Parameters();
		params.put("lineId", lineId);
		String[] orderBy = {"lineId"};
		return findAll(params, orderBy, true);
	}
	
	public Integer getNextId() {
		Integer maxSeq = max("id", Integer.class);
		return maxSeq == null ? 1 : maxSeq + 1;
	}
}
