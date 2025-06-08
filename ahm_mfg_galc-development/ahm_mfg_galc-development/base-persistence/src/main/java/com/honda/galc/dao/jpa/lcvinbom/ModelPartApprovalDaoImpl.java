package com.honda.galc.dao.jpa.lcvinbom;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.lcvinbom.ModelPartApprovalDao;
import com.honda.galc.entity.enumtype.VinBomApprovalStatus;
import com.honda.galc.entity.lcvinbom.ModelPartApproval;
import com.honda.galc.service.Parameters;

public class ModelPartApprovalDaoImpl extends BaseDaoImpl<ModelPartApproval, Long> 
	implements ModelPartApprovalDao {

	@Override
	public int removeByModelPartId(long modelPartId) {
		Parameters params = Parameters.with("modelPartId", modelPartId);
		return delete(params);
	}

	@Override
	public List<ModelPartApproval> getPendingApprovals() {
		Parameters params = Parameters.with("approveStatus",VinBomApprovalStatus.PENDING);
		return findAll(params);
	}
	
	@Override
	public boolean getApprovalStatusByModelPartId(long modelPartId) {
		Parameters params = Parameters.with("modelPartId", modelPartId);
		ModelPartApproval partApproval =  findFirst(params);
		
		return partApproval == null?false:(partApproval.getApproveStatus()==VinBomApprovalStatus.APPROVED);
	}

	@Override
	public ModelPartApproval findByModelPartId(long modelPartId) {
		return findFirst(Parameters.with("modelPartId", modelPartId), new String[]{"createTimestamp"}, false);
	}

	@Override
	public int removeByModelPartIdAndProductionLot(long modelPartId, String currentStartingProductionLot) {
		Parameters params = Parameters.with("modelPartId", modelPartId);
		params.put("newStartingProductionLot", currentStartingProductionLot);
		return delete(params);
	}

}
