package com.honda.galc.dao.lcvinbom;

import java.util.List;

import com.honda.galc.entity.lcvinbom.ModelPartApproval;
import com.honda.galc.service.IDaoService;

public interface ModelPartApprovalDao extends IDaoService<ModelPartApproval, Long> {
	public int removeByModelPartId(long modelPartId);
	public List<ModelPartApproval> getPendingApprovals();
	public boolean getApprovalStatusByModelPartId(long modelPartId);
	public ModelPartApproval findByModelPartId(long modelPartId);
	public int removeByModelPartIdAndProductionLot(long modelPartId, String currentStartingProductionLot);
}
