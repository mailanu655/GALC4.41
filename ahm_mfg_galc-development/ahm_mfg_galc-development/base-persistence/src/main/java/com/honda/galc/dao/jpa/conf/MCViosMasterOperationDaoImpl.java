package com.honda.galc.dao.jpa.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.MCViosMasterOperationDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationId;
import com.honda.galc.service.Parameters;

/**
 * <h3>MCViosMasterOperationDaoImpl Class description</h3>
 * <p>
 * DaoImpl class for MCViosMasterOperation
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
 *        Oct 4, 2018
 */
public class MCViosMasterOperationDaoImpl extends BaseDaoImpl<MCViosMasterOperation, MCViosMasterOperationId> implements MCViosMasterOperationDao {
	
	private static final String FIND_ALL_FILTERED_OPERATIONS = "SELECT * FROM galadm.MC_VIOS_MASTER_OP_TBX op "
			+ "WHERE op.VIOS_PLATFORM_ID = ?1 AND (UPPER(op.UNIT_NO) LIKE ?2 OR UPPER(op.COMMON_NAME) LIKE ?2 OR UPPER(op.OP_VIEW) LIKE ?2 OR UPPER(op.OP_PROCESSOR) LIKE ?2) ORDER BY op.UNIT_NO";
	
	private static final String FIND_ALL_UNIT_NO_BY_PLATFORM_ID = "SELECT DISTINCT UNIT_NO FROM galadm.MC_VIOS_MASTER_OP_TBX where VIOS_PLATFORM_ID = ?1 ORDER BY UNIT_NO";
	
	
	private static final String FIND_ALL_BY_PLATFORM_ID = "SELECT * FROM galadm.MC_VIOS_MASTER_OP_TBX WHERE VIOS_PLATFORM_ID = ?1";
	
	
	@Override
	public List<MCViosMasterOperation> findAllFilteredOperations(String viosPlatformId, String filter) {
		Parameters params = Parameters.with("1", viosPlatformId).put("2", "%"+filter+"%");
		return findAllByNativeQuery(FIND_ALL_FILTERED_OPERATIONS, params);
	}

	@Override
	public List<String> findAllUnitNoBy(String viosPlatformId) {
		Parameters params = Parameters.with("1", viosPlatformId);
		return findAllByNativeQuery(FIND_ALL_UNIT_NO_BY_PLATFORM_ID, params, String.class);
	}
	
	@Override
	public List<MCViosMasterOperation> findAllData(String viosPlatformId) {
		Parameters params = Parameters.with("1", viosPlatformId);
		return findAllByNativeQuery(FIND_ALL_BY_PLATFORM_ID, params, MCViosMasterOperation.class);
	}
	
	@Transactional
	@Override
	public void saveEntity(MCViosMasterOperation opsObject) {
		try {
			removeByKey(opsObject.getId());
			insert(opsObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	
	public Map<String, MCViosMasterOperation> findAllUnitbyCommonName(String viosPlatformId, int conrodCount, boolean isConrod) {
		
		String conrodString = "";
		
		for (int i = 1; i <= conrodCount; i++) {
			conrodString+= Delimiter.QUOTE+ApplicationConstants.CONROD_+i+Delimiter.QUOTE;
			if(i < conrodCount)
				conrodString+= Delimiter.COMMA;
		}
		
		if(isConrod)
			conrodString += Delimiter.COMMA+Delimiter.QUOTE+ApplicationConstants.CRANKSHAFT_1+Delimiter.QUOTE;
		Parameters params = Parameters.with("1", viosPlatformId);
		String FIND_ALL_BY_COMMON_NAME =  FIND_ALL_BY_PLATFORM_ID +" and COMMON_NAME in ("+conrodString+")";
		Map<String, MCViosMasterOperation> mapOfUnits = new HashMap<String, MCViosMasterOperation>();
		List<MCViosMasterOperation> listOfUnits = findAllByNativeQuery(FIND_ALL_BY_COMMON_NAME, params, MCViosMasterOperation.class);
		for (MCViosMasterOperation mcViosMasterOperation : listOfUnits) {
			if(mcViosMasterOperation.getCommonName()!=null)
				mapOfUnits.put(mcViosMasterOperation.getCommonName(), mcViosMasterOperation);
		}
		
		return mapOfUnits;
	}

		public List<MCViosMasterOperation> findAllUnitbyCommonName(String viosPlatformId, String commonName, int commonNameOpCount) {
		String commonNameStr = "";
		
		for(int i = 1; i <= commonNameOpCount; i++) {
			commonNameStr += Delimiter.QUOTE + commonName+i + Delimiter.QUOTE;
			if(i < commonNameOpCount) {
				commonNameStr += Delimiter.COMMA;
			}
		}
		
		Parameters params = Parameters.with("1", viosPlatformId);
		String FIND_ALL_BY_COMMON_NAME =  FIND_ALL_BY_PLATFORM_ID + " and COMMON_NAME in (" + commonNameStr + ")";
		List<MCViosMasterOperation> listOfUnits = findAllByNativeQuery(FIND_ALL_BY_COMMON_NAME, params, MCViosMasterOperation.class);
		
		return listOfUnits;		
	}
}