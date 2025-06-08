package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCViosMasterOperationPartCheckerDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationPartCheckerId;
import com.honda.galc.service.Parameters;

/**
 * <h3>MCViosMasterOperationPartCheckerDaoImpl Class description</h3>
 * <p>
 * DaoImpl class for MCViosMasterOperationPartChecker
 * </p>
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
 * @author Hemant Kumar<br>
 *         Nov 20, 2018
 */
public class MCViosMasterOperationPartCheckerDaoImpl
		extends BaseDaoImpl<MCViosMasterOperationPartChecker, MCViosMasterOperationPartCheckerId>
		implements MCViosMasterOperationPartCheckerDao {

	private static final String FIND_ALL_BY_PLATFORM_ID = "SELECT * FROM galadm.MC_VIOS_MASTER_OP_PART_CHECKER_TBX WHERE VIOS_PLATFORM_ID = ?1";


	@Transactional
	public void removeByUnitNoAndPartNo(String viosPatform, String unitNo, int checkSeq, String PartNo) {
		delete(Parameters.with("id.viosPlatformId", viosPatform).put("id.unitNo", unitNo).put("id.checkSeq", checkSeq).put("id.partNo", PartNo));
	}


	@Override
	public List<MCViosMasterOperationPartChecker> findAllData(String viosPlatformId) {
		Parameters params = Parameters.with("1", viosPlatformId);
		return findAllByNativeQuery(FIND_ALL_BY_PLATFORM_ID, params, MCViosMasterOperationPartChecker.class);
	}

	@Transactional
	@Override
	public void deleteAndInsertAll(String viosPlatformId, String unitNo, String partNo, List<MCViosMasterOperationPartChecker> opPartCheckerList) 
	{
		delete(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo).put("id.partNo", partNo));
		if(opPartCheckerList.size() > 0)
		saveAll(opPartCheckerList);
		
	}


	@Override
	public void deleteAllBy(String viosPlatformId, String unitNo, String partNo) {
		delete(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo).put("id.partNo", partNo));
	}


	@Override
	public List<MCViosMasterOperationPartChecker> findAllBy(String viosPlatformId, String unitNo, String partNo) {
		return findAll(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo).put("id.partNo", partNo), new String[] { "id.checkSeq" });
	}

	@Override
	public List<MCViosMasterOperationPartChecker> findAllBy(String viosPlatformId, String unitNo) {
		return findAll(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo), new String[] { "id.checkSeq" });
	}

	@Override
	public void saveEntity(MCViosMasterOperationPartChecker mcPartChecker) {
		try {
			
			insert(mcPartChecker);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
	
	
