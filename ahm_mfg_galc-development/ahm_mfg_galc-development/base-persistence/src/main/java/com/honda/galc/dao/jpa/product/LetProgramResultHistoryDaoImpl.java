package com.honda.galc.dao.jpa.product;


import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetProgramResultHistoryDao;
import com.honda.galc.entity.product.LetProgramResultHistory;
import com.honda.galc.entity.product.LetProgramResultHistoryId;
import com.honda.galc.service.Parameters;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public class LetProgramResultHistoryDaoImpl extends BaseDaoImpl<LetProgramResultHistory, LetProgramResultHistoryId> implements LetProgramResultHistoryDao {
	private static String GET_PROGRAM_RESULT_DATA = "SELECT PRODUCT_ID, TEST_SEQ, INSPECTION_PGM_ID, INSPECTION_PGM_STATUS, PROCESS_STEP, PROCESS_STATUS, PROCESS_START_TIMESTAMP, PROCESS_END_TIMESTAMP, LET_TERMINAL_ID, LET_RESULT_CAL, LET_RESULT_DCREV, SOFTWARE_VERSION, LET_OPERATOR_ID, IN_CYCLE_RETEST_NUM, CREATE_TIMESTAMP, UPDATE_TIMESTAMP FROM GAL703TBXV WHERE PRODUCT_ID = ?1 AND TEST_SEQ = ?2 AND INSPECTION_PGM_ID = ?3";
	
	
	public Object[] getProgramResultData(String productId ,Integer testSeq,Integer inspectionPgmId) 
	{
		Parameters param=Parameters.with("1",productId);
		param.put("2", testSeq);
		param.put("3", inspectionPgmId);
		return findFirstByNativeQuery(GET_PROGRAM_RESULT_DATA, param,Object[].class);

	}
}
