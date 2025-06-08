package com.honda.galc.dao.qics;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.qics.ReuseProductResult;
import com.honda.galc.entity.qics.ReuseProductResultId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>IqsDao Class description</h3>
 * <p> IqsDao description </p>
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
 * Apr 1, 2011
 *
 *
 */
public interface ReuseProductResultDao extends IDaoService<ReuseProductResult, ReuseProductResultId> {

	public int deleteAllByProductionLot(ProductType productType,String productionLot);

}
