package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.EngineFiringResult;
import com.honda.galc.entity.product.EngineFiringResultId;
import com.honda.galc.service.IDaoService;


/**
 * 
 * <h3>EngineFiringResultDao Class description</h3>
 * <p> EngineFiringResultDao description </p>
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
 * Jul 28, 2011
 *
 *
 */
public interface EngineFiringResultDao extends IDaoService<EngineFiringResult, EngineFiringResultId> {

	List<EngineFiringResult> findAllByProductId(String productId);
	int insert(String productId, String firingTestType, String associateId, String notes, 
			boolean fireResult, int benchId);
	
}
