package com.honda.galc.dao.product;

import java.util.List;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.ExceptionalOutId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ExceptionalOutDao Class description</h3>
 * <p> ExceptionalOutDao description </p>
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
 * @author Jeffray Huang<br>
 * Jun 3, 2012
 *
 *
 */
public interface ExceptionalOutDao extends IDaoService<ExceptionalOut, ExceptionalOutId> {
	public int deleteAllByProductId(String productId);
	public List<ExceptionalOut> findAllByProductId(String productId);
	public ExceptionalOut findMostRecentByProductId(String productId);
	public ExceptionalOut createExceptionalOut(ExceptionalOut exceptionalOut);
}
