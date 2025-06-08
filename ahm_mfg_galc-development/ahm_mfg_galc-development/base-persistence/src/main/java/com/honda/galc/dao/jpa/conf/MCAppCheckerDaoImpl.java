package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCAppCheckerDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.ApplicationCheckerDto;
import com.honda.galc.entity.conf.MCAppChecker;
import com.honda.galc.entity.conf.MCAppCheckerId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2014
 */
public class MCAppCheckerDaoImpl extends BaseDaoImpl<MCAppChecker, MCAppCheckerId> implements MCAppCheckerDao {

	private static String GET_APPLICATION_CHECKER = "SELECT PP.PROCESS_POINT_NAME, AC.* FROM MC_APP_CHECKER_TBX AC "
						+ " LEFT OUTER JOIN GAL214TBX PP ON "
						//Check for underscore
						+ " PP.PROCESS_POINT_ID = (CASE WHEN SUBSTR(AC.APPLICATION_ID, 1, 1) = '_' THEN SUBSTR(AC.APPLICATION_ID, 2) ELSE AC.APPLICATION_ID END) ";
	
	public final static String GET_MAX_CHECK_SEQ = "SELECT COALESCE(MAX(CHECK_SEQ) + 1,1) FROM GALADM.MC_APP_CHECKER_TBX WHERE APPLICATION_ID = ?1 and CHECK_POINT = ?2 ";
	
	public List<MCAppChecker> findAllByApplicationId(String applicationId) {
		Parameters params = Parameters.with("id.applicationId", applicationId);
		return findAll(params);
	}

	public List<MCAppChecker> findAllBy(String applicationId, String checkPoint) {
		Parameters params = Parameters.with("id.applicationId", applicationId);
		params.put("id.checkPoint", checkPoint);
		return findAll(params);
	}
	
	public List<ApplicationCheckerDto> findAllByApplicationAndChecker(String applicationId, String checker, String divisionId) {
		Parameters params = new Parameters();
		boolean whereClause = false;
		String sql = GET_APPLICATION_CHECKER;
		if(StringUtils.isNotBlank(applicationId)) {
			sql = sql + " WHERE UPPER(AC.APPLICATION_ID) = ?1 ";
			params.put("1", applicationId);
			whereClause = true;
		}
		if(StringUtils.isNotBlank(checker)) {
			sql = sql.concat(whereClause  ? " and AC.CHECKER = ?2  " : " WHERE AC.CHECKER = ?1 ");
			params.put(whereClause ? "2" : "1", checker);
			whereClause = true;
		}
		if(StringUtils.isNotBlank(divisionId)) {
			sql = sql.concat(whereClause  ? " and PP.DIVISION_ID = ?3  " : " WHERE PP.DIVISION_ID = ?1 ");
			params.put(whereClause ? "3" : "1", divisionId);
		}
		sql = sql.concat(" ORDER BY AC.APPLICATION_ID, AC.CHECK_SEQ");
		return findAllByNativeQuery(sql, params, ApplicationCheckerDto.class);
	}
	
	public int getMaxCheckSeqBy(String applicationId, String checkPoint) {
		Parameters params = Parameters.with("1", applicationId).put("2", checkPoint);
		return findFirstByNativeQuery(GET_MAX_CHECK_SEQ, params, Integer.class);
	}
	
	@Transactional
	@Override
	public void removeAndInsert(MCAppChecker insertAppChecker, MCAppChecker removeAppChecker) {
		remove(removeAppChecker);
		insert(insertAppChecker);
	}
}
