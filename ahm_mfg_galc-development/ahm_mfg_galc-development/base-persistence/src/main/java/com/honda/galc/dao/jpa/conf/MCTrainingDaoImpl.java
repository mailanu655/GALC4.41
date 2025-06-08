package com.honda.galc.dao.jpa.conf;

import java.util.List;

import com.honda.galc.dao.conf.MCTrainingDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MCTraining;
import com.honda.galc.entity.conf.MCTrainingId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date May 30, 2014
 */
public class MCTrainingDaoImpl extends BaseDaoImpl<MCTraining, MCTrainingId> implements MCTrainingDao {
	
	
	private final String FIND_MOST_RECENT_ENTRY = "SELECT * FROM GALADM.MC_TRAINING_TBX TRAINING  " +
											" WHERE PROCESS_POINT_ID =?1 AND ASSOCIATE_NO =?2 AND PDDA_PLATFORM_ID = ?3 " + 
											" AND SPEC_CODE_TYPE = ?4 AND SPEC_CODE_MASK = ?5 " +
											" ORDER BY TRAINING.EXPIRED DESC " +
											" FETCH FIRST ROW ONLY ";
	
	
	private final String FIND_TRAINING_FOR_USER_PP_PRODSPEC = "SELECT * FROM MC_TRAINING_TBX TRAINING WHERE  TRAINING.ASSOCIATE_NO=?1 AND TRAINING.PROCESS_POINT_ID=?2 " +
											" AND LOCATE (REPLACE( TRAINING.SPEC_CODE_MASK,'*','') , CAST(?3 AS VARCHAR(30)))= 1  " +
											" AND TRAINING.TRAINED < CURRENT_TIMESTAMP AND (TRAINING.EXPIRED IS NULL OR TRAINING.EXPIRED < CURRENT_TIMESTAMP)";	
	
	public MCTraining findMostRecentEntry(String processPt, String associateNo,
			Integer pddaPlatformId, String specCodeType, String specCodeMask) {
		
		Parameters params = new Parameters();
		params.put("1", processPt);
		params.put("2", associateNo);
		params.put("3", pddaPlatformId);
		params.put("4", specCodeType);
		params.put("5", specCodeMask);
		
		return findFirstByNativeQuery(FIND_MOST_RECENT_ENTRY, params);
	}

	public boolean validateUserTraining(String associateNo, String processPt, String prodSpecCode) {
		Parameters params = new Parameters();
		params.put("1", associateNo.trim());
		params.put("2", processPt.trim());
		params.put("3", prodSpecCode.trim());
		List<MCTraining> resultLst = findAllByNativeQuery(FIND_TRAINING_FOR_USER_PP_PRODSPEC, params);
		
		return (resultLst.size()>0)? true : false;
	}

}
