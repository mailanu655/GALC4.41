package com.honda.galc.dao.qics;

import com.honda.galc.entity.qics.DefectRepairResult;
import com.honda.galc.entity.qics.DefectRepairResultId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>DefectResultDao Class description</h3>
 * <p> DefectResultDao description </p>
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
public interface DefectRepairResultDao extends IDaoService<DefectRepairResult, DefectRepairResultId> {

	
	public Integer findMaxRepairId();
	
	public void deleteByQiRepairId(long qiRepairId); 	
}
