package com.honda.galc.dao.qics;

import java.util.List;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.qics.RepairResult;
import com.honda.galc.entity.qics.RepairResultId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>RepairResultDao Class description</h3>
 * <p> RepairResultDao description </p>
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
public interface RepairResultDao extends IDaoService<RepairResult, RepairResultId> {

	public int deleteAllByProductionLot(ProductType productType,String productionLot);
	public List<RepairResult> findAllByProductIdandDefectId(String productId, int defectResultId);

}
