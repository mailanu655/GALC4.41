package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCViosMasterOperationCheckerDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationCheckerId;
import com.honda.galc.service.Parameters;

/**
 * <h3>MCViosMasterOperationCheckerDaoImpl Class description</h3>
 * <p>
 * DaoImpl class for MCViosMasterOperationChecker
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
 *         Oct 4, 2018
 */
public class MCViosMasterOperationCheckerDaoImpl
		extends BaseDaoImpl<MCViosMasterOperationChecker, MCViosMasterOperationCheckerId>
		implements MCViosMasterOperationCheckerDao {

	
	private static final String FIND_ALL_BY_PLATFORM_ID = "SELECT * FROM galadm.MC_VIOS_MASTER_OP_CHECKER_TBX WHERE VIOS_PLATFORM_ID = ?1";
	
	@Transactional
	@Override
	public void saveEntity(MCViosMasterOperationChecker masterOpChecker) {
		try {
			removeByUnitNoAndPlatform(masterOpChecker.getId().getViosPlatformId(), masterOpChecker.getId().getUnitNo(), masterOpChecker.getId().getCheckSeq());
			insert(masterOpChecker);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void deleteAndInsertAll(String viosPlatformId, String unitNo,
			List<MCViosMasterOperationChecker> opCheckerList) {
		delete(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo));
		if(opCheckerList.size() > 0)
			saveAll(opCheckerList);
		
	}

	@Override
	public void deleteAllBy(String viosPlatformId, String unitNo) {
		delete(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo));
		
	}

	@Override
	public List<MCViosMasterOperationChecker> findAllData(String viosPlatformId) {
		Parameters params = Parameters.with("1", viosPlatformId);
		return findAllByNativeQuery(FIND_ALL_BY_PLATFORM_ID, params, MCViosMasterOperationChecker.class);
	}


	@Override
	public List<MCViosMasterOperationChecker> findAllBy(String viosPlatformId, String unitNo) {
		return findAll(Parameters.with("id.viosPlatformId", viosPlatformId).put("id.unitNo", unitNo),
				new String[] { "id.checkSeq" });
	}
	
	@Transactional
	public void removeByUnitNoAndPlatform(String viosplatform, String unitNo, int checkSeq) {
		Parameters params = Parameters.with("id.viosPlatformId", viosplatform).put("id.unitNo", unitNo).put("id.checkSeq", checkSeq);
		delete(params);
	}

}
