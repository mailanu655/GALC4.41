package com.honda.galc.service.common;

import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.service.IoService;

/**
 * 
 * <h3>ProductHoldService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductHoldService description </p>
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
 * <TD>Mar 21, 2017</TD>
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
 * @since Mar 21, 2017
 */
public interface ProductHoldService extends IoService {

	void release(DataContainer dc);
	void qsrRelease(DataContainer dc);
	boolean isQsrHoldBySpecCheck(String productId, HoldResultType holdType, String holdReason);

}
