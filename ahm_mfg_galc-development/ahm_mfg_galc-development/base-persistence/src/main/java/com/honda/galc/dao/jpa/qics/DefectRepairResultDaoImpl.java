package com.honda.galc.dao.jpa.qics;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.DefectRepairResultDao;
import com.honda.galc.entity.qics.DefectRepairResult;
import com.honda.galc.entity.qics.DefectRepairResultId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>DefectResultDaoImpl Class description</h3>
 * <p> DefectResultDaoImpl description </p>
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
 * Feb 28, 2011
 *
 *
 */
public class DefectRepairResultDaoImpl extends BaseDaoImpl<DefectRepairResult,DefectRepairResultId> implements DefectRepairResultDao{
	

	@Override
	@Transactional
	public DefectRepairResult save( DefectRepairResult defectRepairResult ) {
		if ( defectRepairResult.getId().getRepairId() == 0 ) {
			Integer	maxId = max("id.repairId", Integer.class);
			maxId = maxId == null ? 1 : maxId + 1;
			defectRepairResult.getId().setRepairId(maxId);
		}
		super.save(defectRepairResult);
		return defectRepairResult;		
	}
	
	public Integer findMaxRepairId()
	{
		Integer maxRepairId = max("id.repairId", Integer.class);
		if (maxRepairId == null) {
			maxRepairId = 0;
		}
		return ++maxRepairId;
	}
	
	@Transactional 
	public void deleteByQiRepairId(long qiRepairId) {
		delete(Parameters.with("naqRepairId", qiRepairId));
	}
}
