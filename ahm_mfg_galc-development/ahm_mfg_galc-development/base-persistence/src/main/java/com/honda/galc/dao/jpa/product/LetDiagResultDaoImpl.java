package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetDiagResultDao;
import com.honda.galc.entity.product.LetDiagResult;
import com.honda.galc.entity.product.LetDiagResultId;
import com.honda.galc.service.Parameters;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 04, 2013
 */
public class LetDiagResultDaoImpl extends BaseDaoImpl<LetDiagResult, LetDiagResultId> implements LetDiagResultDao {



	private static String GET_LET_DIAG_NAMES_WITH_DETAILS = "SELECT DISTINCT T728.DIAG_TEST_ID, T714.INSPECTION_PGM_NAME, T727.DIAG_TEST_STATUS FROM GAL727TBX AS T727 "
			+ "INNER JOIN GAL726TBX AS T726 ON T726.LET_TERMINAL_ID = T727.LET_TERMINAL_ID "
			+ "INNER JOIN GAL714TBX AS T714 ON T714.INSPECTION_PGM_ID = T727.DIAG_TEST_ID "
			+ "INNER JOIN GAL728TBX AS T728 ON T728.DIAG_TEST_ID = T727.DIAG_TEST_ID "
			+ "WHERE T726.END_TIMESTAMP = ?1 AND T726.LET_TERMINAL_ID = ?2 ORDER BY T728.DIAG_TEST_ID FOR FETCH ONLY";

	private static String GET_LET_DIAG_PARAM_VALUES = "SELECT T728.DIAG_TEST_ID, T715_01.INSPECTION_PARAM_NAME AS FAULT_CODE, T715_02.INSPECTION_PARAM_NAME AS SHORT_DESC "
			+ "FROM GAL728TBX AS T728 "
			+ "INNER JOIN GAL726TBX AS T726 ON T726.LET_TERMINAL_ID = T728.LET_TERMINAL_ID "
			+ "INNER JOIN GAL715TBX AS T715_01 ON T715_01.INSPECTION_PARAM_ID = T728.FAULT_CODE_ID "
			+ "INNER JOIN GAL715TBX AS T715_02 ON T715_02.INSPECTION_PARAM_ID = T728.SHORT_DESC_ID "
			+ "WHERE T726.END_TIMESTAMP = ?1 AND T726.LET_TERMINAL_ID = ?2 ORDER BY T728.DIAG_TEST_ID FOR FETCH ONLY";

	private static String GET_LET_DIAG_RESULT_NAMES_DATA="SELECT T726.END_TIMESTAMP, (CASE WHEN DIAG_TEST_STATUS = '0' THEN 'FAIL' WHEN DIAG_TEST_STATUS = '1' THEN 'PASS' ELSE DIAG_TEST_STATUS END) AS TEST_STATUS, T729_01.DIAG_PARAM_VALUE AS TEST_ID, T729_02.DIAG_PARAM_VALUE AS FAULT_CODE, T729_03.DIAG_PARAM_VALUE AS SHORT_DESC, T727.DIAG_TEST_ID AS FLG FROM GAL726TBX T726 INNER JOIN GAL727TBX T727 ON T726.LET_TERMINAL_ID = ?3 AND T726.END_TIMESTAMP = T727.END_TIMESTAMP AND T726.END_TIMESTAMP BETWEEN ?1 AND ?2 AND T726.LET_TERMINAL_ID = T727.LET_TERMINAL_ID LEFT JOIN GAL728TBX T728 ON T727.END_TIMESTAMP = T728.END_TIMESTAMP AND T727.LET_TERMINAL_ID = T728.LET_TERMINAL_ID AND T727.DIAG_TEST_ID = T728.DIAG_TEST_ID LEFT JOIN GAL729TBX T729_01 ON T727.DIAG_TEST_ID = T729_01.DIAG_PARAM_ID AND T729_01.DIAG_PARAM_TYPE = 'T' LEFT JOIN GAL729TBX T729_02 ON T728.FAULT_CODE_ID = T729_02.DIAG_PARAM_ID AND T729_02.DIAG_PARAM_TYPE = 'F' LEFT JOIN GAL729TBX T729_03 ON T728.SHORT_DESC_ID = T729_03.DIAG_PARAM_ID AND T729_03.DIAG_PARAM_TYPE = 'S' ORDER BY T726.END_TIMESTAMP, T727.DIAG_TEST_ID FOR FETCH ONLY";
	
	private static String GET_LET_DIAG_COLUMN_LIST = "SELECT INSPECTION_PGM_NAME FROM GAL714TBX WHERE " +
							"INSPECTION_PGM_ID IN (SELECT INSPECTION_PGM_ID FROM GAL703TBXV where " +
							"END_TIMESTAMP in (SELECT END_TIMESTAMP FROM GAL726TBX where (END_TIMESTAMP between ?1 and ?2) LETTERMINALID)) ";
	
	private static String GET_LET_DIAG_VIEW_LIST = "SELECT t.INSPECTION_PGM_NAME, v.INSPECTION_PGM_ID, v.INSPECTION_PGM_STATUS FROM galadm.GAL703TBXV v, galadm.GAL714TBX t " +
			"WHERE v.INSPECTION_PGM_ID = t.INSPECTION_PGM_ID AND v.END_TIMESTAMP = ?1 AND v.LET_TERMINAL_ID = ?2 ORDER BY v.INSPECTION_PGM_ID";
	
	
	public List<LetDiagResult> getLetDiagRsltBtwStartEndTmp(Timestamp startTimestamp,Timestamp endTimestamp,String terminalId) 
	{
		String GET_LET_DIAG_RESULT_BETWEEN_START_END_TIMESTMP="select e from LetDiagResult e where e.id.endTimestamp  between  :startTs and :endTs  ";
		String orderByClause=" order by e.id.endTimestamp, e.id.letTerminalId";
		Parameters params = Parameters.with("startTs", startTimestamp).put("endTs",endTimestamp);
		if (terminalId != null && !terminalId.equals(""))
		{
			params.put("3",terminalId);
			GET_LET_DIAG_RESULT_BETWEEN_START_END_TIMESTMP=GET_LET_DIAG_RESULT_BETWEEN_START_END_TIMESTMP.concat(" and e.id.letTerminalId=?3 ");
		}
		GET_LET_DIAG_RESULT_BETWEEN_START_END_TIMESTMP = GET_LET_DIAG_RESULT_BETWEEN_START_END_TIMESTMP.concat(orderByClause);
		return findAllByQuery(GET_LET_DIAG_RESULT_BETWEEN_START_END_TIMESTMP, params);
	}

	public List<Object[]> getLetDiagNamesWithDetails(String endTimestamp,String terminalId)
	{
		Parameters params = Parameters.with("1", endTimestamp).put("2",terminalId);
		return findAllByNativeQuery(GET_LET_DIAG_NAMES_WITH_DETAILS,params,Object[].class);

	}

	public List<Object[]> getMaxFaultCodeCountWithTestId(String startDate,String endDate,String terminalId)
	{
		String GET_MAX_FAULT_CODE_COUNT_WITH_TEST_ID = "SELECT DIAG_TEST_ID, MAX(FAULT_CODE_COUNT) FROM (SELECT END_TIMESTAMP, LET_TERMINAL_ID, DIAG_TEST_ID, COUNT(FAULT_CODE_ID) AS FAULT_CODE_COUNT FROM GAL728TBX WHERE END_TIMESTAMP BETWEEN ?1 AND ?2 ";
		String groupByClause=" GROUP BY END_TIMESTAMP, LET_TERMINAL_ID, DIAG_TEST_ID) AS T1 GROUP BY DIAG_TEST_ID FOR FETCH ONLY";
		Parameters params = Parameters.with("1", startDate).put("2",endDate);
		if (terminalId != null && !terminalId.equals(""))
		{
			params.put("3",terminalId);
			GET_MAX_FAULT_CODE_COUNT_WITH_TEST_ID=GET_MAX_FAULT_CODE_COUNT_WITH_TEST_ID.concat(" AND LET_TERMINAL_ID=?3 ");
		}
		GET_MAX_FAULT_CODE_COUNT_WITH_TEST_ID=GET_MAX_FAULT_CODE_COUNT_WITH_TEST_ID.concat(groupByClause);
		return findAllByNativeQuery(GET_MAX_FAULT_CODE_COUNT_WITH_TEST_ID, params,Object[].class);
	}

	public List<Object[]> getLetDiagParamValues(String endTimestamp,String terminalId)
	{
		Parameters params = Parameters.with("1", endTimestamp).put("2",terminalId);
		return findAllByNativeQuery(GET_LET_DIAG_PARAM_VALUES,params,Object[].class);

	}
	
	public List<Object[]> getLetDiagResultTerminalIdList(String startDate,String endDate,String terminalId)
	{
		String GET_LET_DIAG_RESULT_TERMINAL_ID_LIST = "SELECT LET_TERMINAL_ID FROM GAL726TBX T726 WHERE END_TIMESTAMP BETWEEN ?1 AND ?2 ";
		String groupByClause=" GROUP BY LET_TERMINAL_ID ORDER BY LET_TERMINAL_ID FOR FETCH ONLY";
		Parameters params = Parameters.with("1", startDate).put("2",endDate);
		if (terminalId != null && !terminalId.equals(""))
		{
			params.put("3",terminalId);
			GET_LET_DIAG_RESULT_TERMINAL_ID_LIST=GET_LET_DIAG_RESULT_TERMINAL_ID_LIST.concat(" AND LET_TERMINAL_ID =?3 ");
		}
		GET_LET_DIAG_RESULT_TERMINAL_ID_LIST=GET_LET_DIAG_RESULT_TERMINAL_ID_LIST.concat(groupByClause);
		return findAllByNativeQuery(GET_LET_DIAG_RESULT_TERMINAL_ID_LIST, params,Object[].class);

	}
	
	public List<Object[]> getLetDiagResultNameData(String startDate, String endDate, String terminalId)
	{
		Parameters params = Parameters.with("1", startDate).put("2",endDate).put("3", terminalId);
		return findAllByNativeQuery(GET_LET_DIAG_RESULT_NAMES_DATA,params,Object[].class);

	}
	
	public List<Object[]> getLetDiagRsltView(Timestamp endTimestamp, String terminalId) 
	{
		Parameters params = Parameters.with("1", endTimestamp).put("2", terminalId);
		return findAllByNativeQuery(GET_LET_DIAG_VIEW_LIST, params,Object[].class);
	}
	
	public List<Object[]> getLetDiagColumns(Timestamp startTimestamp, Timestamp endTimestamp, String terminalId) {
		String sql = "";
		Parameters params = Parameters.with("1", startTimestamp).put("2",endTimestamp);
		if (StringUtils.isNotBlank(terminalId))	{
			params.put("3",terminalId);
			sql = GET_LET_DIAG_COLUMN_LIST.replace("LETTERMINALID", " and LET_TERMINAL_ID = ?3 ");
		} else {
			sql = GET_LET_DIAG_COLUMN_LIST.replace("LETTERMINALID", "");
		}
		return findAllByNativeQuery(sql, params,Object[].class);
	}
}
