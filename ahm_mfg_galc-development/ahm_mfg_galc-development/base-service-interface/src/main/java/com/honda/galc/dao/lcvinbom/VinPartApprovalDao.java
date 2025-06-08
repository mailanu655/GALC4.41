package com.honda.galc.dao.lcvinbom;

import java.util.List;

import com.honda.galc.entity.lcvinbom.VinPartApproval;
import com.honda.galc.service.IDaoService;

public interface VinPartApprovalDao extends IDaoService<VinPartApproval, Long> {
	List<VinPartApproval> findAllPending();
	void denyChange(long vinPartApprovalId, String approveAssociateNumber);
}
