package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.product.HoldParm;
import com.honda.galc.service.IDaoService;
/**
 * 
 * <h3>HoldParmDao</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HoldParmDao description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Jun 28, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jun 28, 2012
 */
public interface HoldParmDao extends IDaoService<HoldParm, Integer>{
	List<HoldParm> findActiveHolds();
	List<HoldParm> findActiveHolds(String productType);
	List<HoldParm> findAll(int releaseFlag);
	List<HoldParm> findAllByQsrId(int qsrId);
	int releaseExpiredHolds(Date stopDate);	
}
