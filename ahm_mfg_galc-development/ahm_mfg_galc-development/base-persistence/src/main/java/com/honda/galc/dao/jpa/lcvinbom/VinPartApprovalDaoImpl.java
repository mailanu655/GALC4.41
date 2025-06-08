package com.honda.galc.dao.jpa.lcvinbom;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.lcvinbom.VinPartApprovalDao;
import com.honda.galc.entity.enumtype.VinBomApprovalStatus;
import com.honda.galc.entity.lcvinbom.VinPartApproval;
import com.honda.galc.service.Parameters;

public class VinPartApprovalDaoImpl extends BaseDaoImpl<VinPartApproval, Long> 
implements VinPartApprovalDao {

	@Override
	public List<VinPartApproval> findAllPending() {
		return findAll(Parameters.with("approveStatus", VinBomApprovalStatus.PENDING));
	}

	@Override
	@Transactional
	public void denyChange(long vinPartApprovalId, String approveAssociateNumber) {
		VinPartApproval vinPartApproval = findByKey(vinPartApprovalId);
		if (vinPartApproval != null) {
			vinPartApproval.setApproveStatus(VinBomApprovalStatus.DENIED);
			vinPartApproval.setApproveAssociateNumber(approveAssociateNumber);
			vinPartApproval.setApproveTimestamp(new Timestamp(System.currentTimeMillis()));
			save(vinPartApproval);
		}
	}

}
