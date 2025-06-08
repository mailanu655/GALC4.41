package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetPassCriteriaMtoDao;
import com.honda.galc.entity.product.LetPassCriteriaMto;
import com.honda.galc.entity.product.LetPassCriteriaMtoId;
import com.honda.galc.service.Parameters;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public class LetPassCriteriaMtoDaoImpl extends BaseDaoImpl<LetPassCriteriaMto, LetPassCriteriaMtoId> implements LetPassCriteriaMtoDao {

	private static String GET_LET_PASS_CRITERIA_FOR_MTO="SELECT MODEL_YEAR_CODE,MODEL_CODE,MODEL_TYPE_CODE,MODEL_OPTION_CODE,EFFECTIVE_TIME FROM GALADM.GAL716TBX WHERE (MODEL_YEAR_CODE = ?1 OR MODEL_YEAR_CODE = '*' ) AND (MODEL_CODE = ?2 OR MODEL_CODE = '*' ) AND (MODEL_TYPE_CODE = ?3 OR MODEL_TYPE_CODE = '*' ) AND (MODEL_OPTION_CODE = ?4 OR MODEL_OPTION_CODE = '*' ) AND ?5 BETWEEN EFFECTIVE_TIME AND EXPIRATION_TIME ORDER BY CONDITION_WEIGHT DESC,EFFECTIVE_TIME DESC FETCH FIRST 1 ROW ONLY";
	private static String GET_LET_PASS_CRITERIA_PROGRAM_DATA = "SELECT GAL718.CRITERIA_PGM_NAME FROM GALADM.GAL717TBX AS GAL717 INNER JOIN GALADM.GAL718TBX AS GAL718 ON GAL717.CRITERIA_PGM_ID = GAL718.CRITERIA_PGM_ID WHERE MODEL_YEAR_CODE = ?1 AND MODEL_CODE = ?2 AND MODEL_TYPE_CODE = ?3 AND MODEL_OPTION_CODE = ?4 AND EFFECTIVE_TIME = ?5 FOR FETCH ONLY";
	private static String GET_LET_PASS_CRITERIA_FOR_MTO_EFFTIME="SELECT T718.CRITERIA_PGM_ID, T718.CRITERIA_PGM_NAME, ( CASE WHEN T717.INSPECTION_DEVICE_TYPE IS NULL THEN T718.INSPECTION_DEVICE_TYPE ELSE T717.INSPECTION_DEVICE_TYPE END ) AS INSPECTION_DEVICE_TYPE, ( CASE WHEN T717.CRITERIA_PGM_ATTRIBUTE IS NULL THEN T718.CRITERIA_PGM_ATTRIBUTE ELSE T717.CRITERIA_PGM_ATTRIBUTE END ) AS CRITERIA_PGM_ATTRIBUTE FROM GAL718TBX T718 LEFT OUTER JOIN GALADM.GAL717TBX T717 ON T718.CRITERIA_PGM_ID = T717.CRITERIA_PGM_ID AND T717.MODEL_YEAR_CODE = ?1 AND T717.MODEL_CODE = ?2 AND T717.MODEL_TYPE_CODE = ?3 AND T717.MODEL_OPTION_CODE = ?4 AND T717.EFFECTIVE_TIME = ?5 ORDER BY T718.CRITERIA_PGM_NAME FOR FETCH ONLY";
	private static String GET_LET_PASS_CRITERIA_PROGRAM="SELECT T717.CRITERIA_PGM_ID, T718.CRITERIA_PGM_NAME, T730.BG_COLOR FROM GAL717TBX T717 INNER JOIN GAL718TBX T718 ON T717.CRITERIA_PGM_ID = T718.CRITERIA_PGM_ID INNER JOIN GAL730TBX T730 ON T717.INSPECTION_DEVICE_TYPE = T730.INSPECTION_DEVICE_TYPE AND T717.CRITERIA_PGM_ATTRIBUTE = T730.CRITERIA_PGM_ATTRIBUTE WHERE T717.MODEL_YEAR_CODE = ?1 AND T717.MODEL_CODE = ?2 AND T717.MODEL_TYPE_CODE = ?3 AND T717.MODEL_OPTION_CODE = ?4 AND T717.EFFECTIVE_TIME = ?5 ORDER BY T718.CRITERIA_PGM_NAME FOR FETCH ONLY";
	private static String GET_LET_PASS_CRITERIA_PROGRAM_GREATER_EQUAL__EFFTIME = "select e from LetPassCriteriaMto e where e.id.modelYearCode=:modelYearCode and e.id.modelCode=:modelCode and e.id.modelTypeCode=:modelTypeCode and e.id.modelOptionCode=:modelOptionCode and e.endTimestamp>=:endTimestamp";
	private static String UPDATE_LET_PASS_CRITERIA_EXPTIME="update LetPassCriteriaMto e set e.endTimestamp=:newEndTimestamp where e.id.modelYearCode=:modelYearCode and e.id.modelCode=:modelCode and e.id.modelTypeCode=:modelTypeCode and e.id.modelOptionCode=:modelOptionCode and e.id.effectiveTime=:effectiveTime and e.endTimestamp=:endTimestamp";
	private static String UPDATE_LET_PASS_CRITERIA_EFFTIME="update LetPassCriteriaMto e set e.id.effectiveTime=:newEffectiveTime where e.id.modelYearCode=:modelYearCode and e.id.modelCode=:modelCode and e.id.modelTypeCode=:modelTypeCode and e.id.modelOptionCode=:modelOptionCode and e.id.effectiveTime=:effectiveTime and e.endTimestamp=:endTimestamp";



	public Object[] findLetPassCriteriaForMto(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime ) 
	{      
		Parameters params = Parameters.with("1", modelYearCode).put("2", modelCode).put("3", modelTypeCode).put("4", modelOptionCode).put("5", effectiveTime);			

		return	findFirstByNativeQuery(GET_LET_PASS_CRITERIA_FOR_MTO, params, Object[].class);        	
	}



	public List<Object[]> findLetPassCriteriaProgramData(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime ) 
	{
		Parameters params = Parameters.with("1", modelYearCode).put("2", modelCode).put("3", modelTypeCode).put("4", modelOptionCode).put("5", effectiveTime);			

		return	findAllByNativeQuery(GET_LET_PASS_CRITERIA_PROGRAM_DATA, params, Object[].class);
	}

	public List<Object[]> findLetPassCriteriaForMtoEffTime(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime ) 
	{
		Parameters params = Parameters.with("1", modelYearCode).put("2", modelCode).put("3", modelTypeCode).put("4", modelOptionCode).put("5", effectiveTime);			

		return	findAllByNativeQuery(GET_LET_PASS_CRITERIA_FOR_MTO_EFFTIME, params, Object[].class);
	}


	public List<Object[]> findLetPassCritPgrm(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime ) 
	{
		Parameters params = Parameters.with("1", modelYearCode).put("2", modelCode).put("3", modelTypeCode).put("4", modelOptionCode).put("5", effectiveTime);			

		return	findAllByNativeQuery(GET_LET_PASS_CRITERIA_PROGRAM, params, Object[].class);
	}

	public List<Object[]> findLetPassCriteriaBySearchParams(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,String checkStr,String startRow,String endRow)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("WITH TMP( ");
		sbSql.append("ROWNUM, ");
		sbSql.append("MODEL_YEAR_CODE, ");
		sbSql.append("MODEL_CODE, ");
		sbSql.append("MODEL_TYPE_CODE, ");
		sbSql.append("MODEL_OPTION_CODE, ");
		sbSql.append("EFFECTIVE_TIME) AS ( ");
		sbSql.append("SELECT ");
		sbSql.append("ROWNUMBER() OVER ");
		sbSql.append("(ORDER BY CONDITION_WEIGHT DESC, ");
		sbSql.append("MODEL_YEAR_CODE, ");
		sbSql.append("MODEL_CODE, ");
		sbSql.append("MODEL_TYPE_CODE, ");
		sbSql.append("MODEL_OPTION_CODE, ");
		sbSql.append("EFFECTIVE_TIME DESC) AS ROWNUM, ");
		sbSql.append("MODEL_YEAR_CODE, ");
		sbSql.append("MODEL_CODE, ");
		sbSql.append("MODEL_TYPE_CODE, ");
		sbSql.append("MODEL_OPTION_CODE, ");
		sbSql.append("EFFECTIVE_TIME ");
		sbSql.append("FROM GAL716TBX ");
		String strWhere = "";
		String work = "";
		work = modelYearCode;
		if (work != null && !"".equals(work)) {
			if ("".equals(strWhere)) {
				strWhere += "WHERE ";
			}
			strWhere += "(MODEL_YEAR_CODE='"+ work+ "' OR MODEL_YEAR_CODE='*') ";
		}
		work = modelCode;
		if (work != null && !"".equals(work)) {
			if ("".equals(strWhere)) {
				strWhere += "WHERE ";
			} else {
				strWhere += "AND ";
			}
			strWhere += "(MODEL_CODE='" + work + "' OR MODEL_CODE='*') ";
		}
		work = modelTypeCode;
		if (work != null && !"".equals(work)) {
			if ("".equals(strWhere)) {
				strWhere += "WHERE ";
			} else {
				strWhere += "AND ";
			}
			strWhere += "(MODEL_TYPE_CODE='"+ work+ "' OR MODEL_TYPE_CODE='*') ";
		}
		work = modelOptionCode;
		if (work != null && !"".equals(work)) {
			if ("".equals(strWhere)) {
				strWhere += "WHERE ";
			} else {
				strWhere += "AND ";
			}
			strWhere += "(MODEL_OPTION_CODE='"+ work+ "' OR MODEL_OPTION_CODE='*') ";
		}
		work = checkStr;
		if (work != null && !"".equals(work) && "0".equals(work)) {
			if ("".equals(strWhere)) {
				strWhere += "WHERE ";
			} else {
				strWhere += "AND ";
			}
			strWhere += "(EXPIRATION_TIME > '"+ sdf.format(cal.getTime())+ "' ";
			strWhere += "OR EXPIRATION_TIME IS NULL) ";
		}
		sbSql.append(strWhere);
		sbSql.append(") ");
		sbSql.append("SELECT ");
		sbSql.append("TMP.ROWNUM, ");
		sbSql.append("T716.MODEL_YEAR_CODE, ");
		sbSql.append("T716.MODEL_CODE, ");
		sbSql.append("T716.MODEL_TYPE_CODE, ");
		sbSql.append("T716.MODEL_OPTION_CODE, ");
		sbSql.append("T716.EFFECTIVE_TIME, ");
		sbSql.append("T716.EXPIRATION_TIME, ");
		sbSql.append("T716.CONDITION_WEIGHT ");
		sbSql.append("FROM TMP ");
		sbSql.append("INNER JOIN GAL716TBX T716 ");
		sbSql.append("ON  TMP.MODEL_YEAR_CODE = T716.MODEL_YEAR_CODE ");
		sbSql.append("AND TMP.MODEL_CODE = T716.MODEL_CODE ");
		sbSql.append("AND TMP.MODEL_TYPE_CODE = T716.MODEL_TYPE_CODE ");
		sbSql.append("AND TMP.MODEL_OPTION_CODE = T716.MODEL_OPTION_CODE ");
		sbSql.append("AND TMP.EFFECTIVE_TIME = T716.EFFECTIVE_TIME ");
		sbSql.append("WHERE TMP.ROWNUM BETWEEN ");
		sbSql.append(startRow).append(" ");
		sbSql.append("AND ");
		sbSql.append(endRow).append(" ");
		sbSql.append("ORDER BY CONDITION_WEIGHT DESC, ");
		sbSql.append("MODEL_YEAR_CODE, ");
		sbSql.append("MODEL_CODE, ");
		sbSql.append("MODEL_TYPE_CODE, ");
		sbSql.append("MODEL_OPTION_CODE, ");
		sbSql.append("EFFECTIVE_TIME DESC ");
		return	findAllByNativeQuery(sbSql.toString(), null, Object[].class);
	}

	public Integer getYMTOSearchCount(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,String checkStr ) 
	{      
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT COUNT(MODEL_YEAR_CODE) CNT FROM GAL716TBX ");
		String strWhere = "";
		String work = "";
		work = modelYearCode;
		if (work != null && !"".equals(work)) {
			if ("".equals(strWhere)) {
				strWhere += "WHERE ";
			}
			strWhere += "(MODEL_YEAR_CODE='"+ work+ "' OR MODEL_YEAR_CODE='*') ";
		}
		work = modelCode;
		if (work != null && !"".equals(work)) {
			if ("".equals(strWhere)) {
				strWhere += "WHERE ";
			} else {
				strWhere += "AND ";
			}
			strWhere += "(MODEL_CODE='" + work + "' OR MODEL_CODE='*') ";
		}
		work = modelTypeCode;
		if (work != null && !"".equals(work)) {
			if ("".equals(strWhere)) {
				strWhere += "WHERE ";
			} else {
				strWhere += "AND ";
			}
			strWhere += "(MODEL_TYPE_CODE='"+ work+ "' OR MODEL_TYPE_CODE='*') ";
		}
		work = modelOptionCode;
		if (work != null && !"".equals(work)) {
			if ("".equals(strWhere)) {
				strWhere += "WHERE ";
			} else {
				strWhere += "AND ";
			}
			strWhere += "(MODEL_OPTION_CODE='"+ work+ "' OR MODEL_OPTION_CODE='*') ";
		}
		work = checkStr;
		if (work != null && !"".equals(work) && "0".equals(work)) {
			if ("".equals(strWhere)) {
				strWhere += "WHERE ";
			} else {
				strWhere += "AND ";
			}
			strWhere += "(EXPIRATION_TIME > '"+ sdf.format(cal.getTime())+ "' ";
			strWhere += "OR EXPIRATION_TIME IS NULL) ";
		}
		sbSql.append(strWhere);
		Object[] array=	findFirstByNativeQuery(sbSql.toString(), null, Object[].class);    
		return (Integer)array[0];
	}

	public List<LetPassCriteriaMto> findLetPassCriteriaGreaterEqualEffecTime(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime ) 
	{
		Parameters params = Parameters.with("modelYearCode", modelYearCode).put("modelCode", modelCode).put("modelTypeCode", modelTypeCode).put("modelOptionCode", modelOptionCode).put("endTimestamp", effectiveTime);
		return	findAllByQuery(GET_LET_PASS_CRITERIA_PROGRAM_GREATER_EQUAL__EFFTIME, params);
	}

	@Transactional
	public int updateLetPassCriteriaExpTime(Timestamp newExpTime,String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effectiveTime,Timestamp oldExpTime ) 
	{

		Parameters params = Parameters.with("newEndTimestamp",newExpTime).put("modelYearCode", modelYearCode).put("modelCode", modelCode).put("modelTypeCode", modelTypeCode).put("modelOptionCode", modelOptionCode).put("effectiveTime", effectiveTime).put("endTimestamp", oldExpTime);			
		return	executeUpdate(UPDATE_LET_PASS_CRITERIA_EXPTIME, params);
	}

	@Transactional
	public int updateLetPassCriteriaEffTime(Timestamp newEffTime,String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp oldEffTime,Timestamp expTime ) 
	{

		Parameters params = Parameters.with("newEffectiveTime",newEffTime).put("modelYearCode", modelYearCode).put("modelCode", modelCode).put("modelTypeCode", modelTypeCode).put("modelOptionCode", modelOptionCode).put("effectiveTime", oldEffTime).put("endTimestamp", expTime);			
		return	executeUpdate(UPDATE_LET_PASS_CRITERIA_EFFTIME, params);
	}

	@Transactional
	public int deleteLetPassCriteriaMto(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effTime ) 
	{
		Parameters params = Parameters.with("id.modelYearCode", modelYearCode).put("id.modelCode", modelCode).put("id.modelTypeCode", modelTypeCode).put("id.modelOptionCode", modelOptionCode).put("id.effectiveTime", effTime);			
		return	delete(params);
	}

}
