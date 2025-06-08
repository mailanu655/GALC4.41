package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.entity.enumtype.LetProgramResultEnum;
import com.honda.galc.entity.product.LetProgramResultValueId;
import com.honda.galc.entity.product.LetResult;
import com.honda.galc.entity.product.LetResultId;
import com.honda.galc.service.Parameters;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public class LetResultDaoImpl extends BaseDaoImpl<LetResult, LetResultId> implements LetResultDao {

	private static String GET_PASS_CRITERIA_PROGRAM_DATA_FOR_MTO= "SELECT A.INSPECTION_DEVICE_TYPE, A.CRITERIA_PGM_ATTRIBUTE, B.CRITERIA_PGM_NAME, C.BG_COLOR FROM GAL717TBX A INNER JOIN GAL718TBX B ON A.CRITERIA_PGM_ID = B.CRITERIA_PGM_ID INNER JOIN GAL730TBX C ON A.INSPECTION_DEVICE_TYPE = C.INSPECTION_DEVICE_TYPE AND A.CRITERIA_PGM_ATTRIBUTE = C.CRITERIA_PGM_ATTRIBUTE WHERE A.MODEL_YEAR_CODE = ?1 AND A.MODEL_CODE = ?2 AND A.MODEL_TYPE_CODE = ?3 AND A.MODEL_OPTION_CODE = ?4 AND A.EFFECTIVE_TIME = ?5 FOR READ ONLY";
	private static String GET_LET_INSPECTION_RESULTS= "SELECT T715.INSPECTION_PARAM_NAME, T704.INSPECTION_PARAM_VALUE, T704.INSPECTION_PARAM_UNIT FROM GAL704TBXV T704 INNER JOIN GAL703TBXV T703 ON T703.END_TIMESTAMP = T704.END_TIMESTAMP AND T703.PRODUCT_ID = T704.PRODUCT_ID AND T703.TEST_SEQ = T704.TEST_SEQ AND T703.INSPECTION_PGM_ID = T704.INSPECTION_PGM_ID INNER JOIN GAL715TBX T715 ON T704.INSPECTION_PARAM_ID = T715.INSPECTION_PARAM_ID WHERE T703.PRODUCT_ID = ?1 AND T703.TEST_SEQ = ?2 AND T703.INSPECTION_PGM_ID = ?3 ORDER BY T704.INSPECTION_PARAM_TYPE, T715.INSPECTION_PARAM_NAME FOR READ ONLY";
	private static String GET_LET_PRODUCT= "SELECT PRODUCT_ID FROM GAL700TBX WHERE SERIES_FRAME_NO = ?1 FOR READ ONLY";
	private static String GET_LET_PRODUCT_SERIES_FRAME= "SELECT SERIES_FRAME_NO FROM GAL700TBX WHERE PRODUCT_ID = ?1 FOR READ ONLY";   
	private static String GET_VIN_MTO_DATA= "SELECT B.PRODUCT_SPEC_CODE AS MTO,C.MODEL_CODE AS MODEL,C.MODEL_YEAR_CODE AS YEAR,C.MODEL_TYPE_CODE AS TYPE,C.MODEL_OPTION_CODE AS OPTION,C.EXT_COLOR_CODE AS EXT_COLOR,C.INT_COLOR_CODE AS INT_COLOR, B.PLAN_OFF_DATE AS PLAN_OFF_DATE FROM GAL143TBX AS B LEFT JOIN GAL144TBX AS C ON (B.PRODUCT_SPEC_CODE = C.PRODUCT_SPEC_CODE) WHERE B.PRODUCT_ID = ?1 FOR READ ONLY";
	private static String GET_INSPECTIONT_RESULT = "SELECT T715.INSPECTION_PARAM_NAME, T704.INSPECTION_PARAM_VALUE, T704.INSPECTION_PARAM_UNIT FROM GAL704TBXV T704 INNER JOIN GAL703TBXV T703 ON T703.END_TIMESTAMP = T704.END_TIMESTAMP AND T703.PRODUCT_ID = T704.PRODUCT_ID AND T703.TEST_SEQ = T704.TEST_SEQ AND T703.INSPECTION_PGM_ID = T704.INSPECTION_PGM_ID INNER JOIN GAL715TBX T715 ON T704.INSPECTION_PARAM_ID = T715.INSPECTION_PARAM_ID WHERE T703.PRODUCT_ID = '?1' AND T703.TEST_SEQ = ?2 AND T703.INSPECTION_PGM_ID = ?3 ORDER BY T704.INSPECTION_PARAM_TYPE, T715.INSPECTION_PARAM_NAME FOR READ ONLY";
	private static String GET_LET_RESULT_BY_PRODUCT_ID= "SELECT B.TEST_SEQ, B.END_TIMESTAMP FROM GAL143TBX A, GAL701TBX B WHERE A.PRODUCT_ID = ?1 AND A.PRODUCT_ID = B.PRODUCT_ID ORDER BY B.END_TIMESTAMP FOR READ ONLY";
	private static String GET_FAULT_DATA_BY_PRODUCT_ID_TEST_SEQ= "SELECT C.INSPECTION_PGM_NAME, B.INSPECTION_PGM_ID FROM (SELECT PRODUCT_ID, TEST_SEQ FROM GAL701TBX WHERE PRODUCT_ID = ?1 AND TEST_SEQ = ?2) A JOIN GAL703TBXV B ON A.PRODUCT_ID = B.PRODUCT_ID AND A.TEST_SEQ = B.TEST_SEQ JOIN GAL714TBX C ON B.INSPECTION_PGM_ID = C.INSPECTION_PGM_ID WHERE B.INSPECTION_PGM_STATUS = '0' ORDER BY C.INSPECTION_PGM_NAME FOR READ ONLY";
	private static String GET_LET_PROGRAM_RESULT_BY_PRODUCTID_SEQNUM= "SELECT PRODUCT_ID,TEST_SEQ,INSPECTION_PGM_ID,INSPECTION_PGM_STATUS,PROCESS_STEP,PROCESS_STATUS,PROCESS_START_TIMESTAMP,PROCESS_END_TIMESTAMP,LET_TERMINAL_ID,LET_RESULT_CAL,LET_RESULT_DCREV,SOFTWARE_VERSION,LET_OPERATOR_ID,IN_CYCLE_RETEST_NUM,CREATE_TIMESTAMP,UPDATE_TIMESTAMP FROM GAL703TBXV WHERE PRODUCT_ID = ?1 AND TEST_SEQ = ?2";
	private static String DELETE_PROGRAM_RESULT_BY_PRODUCTID_SEQNUM="DELETE FROM GAL703TBXV WHERE PRODUCT_ID = ?1 AND TEST_SEQ = ?2";
	private static String GET_LET_PROGRAM_RESULT_VALUE_BY_PRODUCTID_SEQNUM="SELECT PRODUCT_ID,TEST_SEQ,INSPECTION_PGM_ID,INSPECTION_PARAM_ID,INSPECTION_PARAM_TYPE,INSPECTION_PARAM_VALUE,INSPECTION_PARAM_UNIT,CREATE_TIMESTAMP,UPDATE_TIMESTAMP FROM GAL704TBXV WHERE END_TIMESTAMP = (SELECT END_TIMESTAMP FROM GAL701TBX WHERE PRODUCT_ID = ?1 AND TEST_SEQ = ?2) AND PRODUCT_ID = ?1 AND TEST_SEQ = ?2";
	private static String GET_LET_PROGRAM_RESULT_VALUE_BY_PRODUCTID_PARAMID_TMPL="select v.end_timestamp,v.product_id,v.test_seq,v.inspection_pgm_id,v.inspection_param_id,v.inspection_param_type,v.inspection_param_value,v.inspection_param_unit,v.create_timestamp,v.update_timestamp from gal704tbxv v join gal701tbx p on p.product_id = v.product_id and p.test_seq = v.test_seq and p.end_timestamp = v.end_timestamp where v.product_id = ?1 and v.inspection_param_id = ?2 ";
	private static String GET_LET_PROGRAM_RESULT_VALUE_BY_PRODUCTID_PARAMID = GET_LET_PROGRAM_RESULT_VALUE_BY_PRODUCTID_PARAMID_TMPL + " order by v.end_timestamp desc";
	private static String GET_LET_PROGRAM_RESULT_VALUE_BY_PRODUCTID_PARAMID_ENDTIME=GET_LET_PROGRAM_RESULT_VALUE_BY_PRODUCTID_PARAMID_TMPL + " and v.end_timestamp between ?3 and ?4 order by v.end_timestamp desc";	
	private static String GET_LET_PROGRAM_RESULT_VALUE_BY_PARAMID_ENDTIME="SELECT END_TIMESTAMP,PRODUCT_ID,TEST_SEQ,INSPECTION_PGM_ID,INSPECTION_PARAM_ID,INSPECTION_PARAM_TYPE,INSPECTION_PARAM_VALUE,INSPECTION_PARAM_UNIT,CREATE_TIMESTAMP,UPDATE_TIMESTAMP FROM GAL704TBXV WHERE INSPECTION_PARAM_ID = ?1 AND END_TIMESTAMP BETWEEN ?2 AND ?3 ORDER BY END_TIMESTAMP DESC";
	private static String GET_LET_PROGRAM_RESULT_VALUE_BY_ID="SELECT END_TIMESTAMP, PRODUCT_ID,TEST_SEQ,INSPECTION_PGM_ID,INSPECTION_PARAM_ID,INSPECTION_PARAM_TYPE,INSPECTION_PARAM_VALUE,INSPECTION_PARAM_UNIT,CREATE_TIMESTAMP,UPDATE_TIMESTAMP FROM GAL704TBXV WHERE END_TIMESTAMP =?1 AND PRODUCT_ID = ?2 AND TEST_SEQ = ?3 AND INSPECTION_PGM_ID=?4 AND INSPECTION_PARAM_ID=?5 AND INSPECTION_PARAM_TYPE=?6 ";
	private static String DELETE_PROGRAM_RESULT_VALUE_BY_PRODUCTID_SEQNUM=" DELETE FROM GAL704TBXV WHERE END_TIMESTAMP = (SELECT END_TIMESTAMP FROM GAL701TBX WHERE PRODUCT_ID = ?1 AND TEST_SEQ = ?2) AND PRODUCT_ID = ?1 AND TEST_SEQ = ?2";
	private static String GET_DISTINCT_LET_PROGRAMS_BY_PRODUCTID = "SELECT DISTINCT C.INSPECTION_PGM_NAME, B.INSPECTION_PGM_ID FROM (SELECT PRODUCT_ID, TEST_SEQ FROM GAL701TBX WHERE PRODUCT_ID = ?1) A LEFT JOIN GAL703TBXV B ON A.PRODUCT_ID = B.PRODUCT_ID AND A.TEST_SEQ = B.TEST_SEQ LEFT JOIN GAL714TBX C ON B.INSPECTION_PGM_ID = C.INSPECTION_PGM_ID ORDER BY C.INSPECTION_PGM_NAME FOR READ ONLY";
	private static String GET_LET_MANUAL_INPUT_RESULT_DATA_TMPL= "select a.test_seq, b.process_end_timestamp as end_timestamp, b.inspection_pgm_status, d.inspection_param_name, c.inspection_param_id, c.inspection_param_value, c.inspection_param_type, b.process_step, a.production from gal701tbx a left join gal703tbxv b on b.end_timestamp = a.end_timestamp and b.product_id = a.product_id and b.test_seq = a.test_seq and b.inspection_pgm_id = ?2 left join gal704tbx_%s c on c.end_timestamp = b.end_timestamp and c.product_id = b.product_id and c.test_seq = b.test_seq  and c.inspection_pgm_id = b.inspection_pgm_id left join gal715tbx d on d.inspection_param_id = c.inspection_param_id where a.product_id = ?1 ";
	private static String GET_LET_MANUAL_INPUT_RESULT_DATA = String.format(GET_LET_MANUAL_INPUT_RESULT_DATA_TMPL, "01")
		                               + "\n union all \n" + String.format(GET_LET_MANUAL_INPUT_RESULT_DATA_TMPL, "02")
		                               + "\n union all \n" + String.format(GET_LET_MANUAL_INPUT_RESULT_DATA_TMPL, "03")
		                               + "\n union all \n" + String.format(GET_LET_MANUAL_INPUT_RESULT_DATA_TMPL, "04")
		                               + "\n union all \n" + String.format(GET_LET_MANUAL_INPUT_RESULT_DATA_TMPL, "05")
		                               + "\n union all \n" + String.format(GET_LET_MANUAL_INPUT_RESULT_DATA_TMPL, "06")
		                               + "\n order by end_timestamp, inspection_param_type, inspection_param_name  for read only";
	private static String GET_INSPECTION_DETAIL_RESULTS_TMPL = "select a.test_seq, b.end_timestamp, b.inspection_pgm_status, b.process_step, d.inspection_param_name, c.inspection_param_type, c.inspection_param_id, c.inspection_param_value, c.inspection_param_unit, a.base_release, a.production, c.low_limit, c.high_limit from gal701tbx a left join gal703tbxv b on b.end_timestamp = a.end_timestamp and b.product_id = a.product_id and b.test_seq = a.test_seq and b.inspection_pgm_id = ?2 left join gal704tbx_%s c on c.end_timestamp = b.end_timestamp and c.product_id = b.product_id and c.test_seq = b.test_seq  and c.inspection_pgm_id = b.inspection_pgm_id left join gal715tbx d on d.inspection_param_id = c.inspection_param_id where a.product_id = ?1 ";
	private static String GET_INSPECTION_DETAIL_RESULTS = String.format(GET_INSPECTION_DETAIL_RESULTS_TMPL, "01")
		                            + "\n union all \n" + String.format(GET_INSPECTION_DETAIL_RESULTS_TMPL, "02")
		                            + "\n union all \n" + String.format(GET_INSPECTION_DETAIL_RESULTS_TMPL, "03")
		                            + "\n union all \n" + String.format(GET_INSPECTION_DETAIL_RESULTS_TMPL, "04")
		                            + "\n union all \n" + String.format(GET_INSPECTION_DETAIL_RESULTS_TMPL, "05")
		                            + "\n union all \n" + String.format(GET_INSPECTION_DETAIL_RESULTS_TMPL, "06")
                                    + "\n order by end_timestamp, test_seq, inspection_param_type, inspection_param_name for read only";
	private static String UPDATE_PROGRAM_RESULT_BY_END_TIMESTAMP = "UPDATE GAL703TBXV SET INSPECTION_PGM_STATUS = ?4 WHERE END_TIMESTAMP = (SELECT END_TIMESTAMP FROM GAL701TBX WHERE PRODUCT_ID = ?1 AND TEST_SEQ = ?2 ) AND PRODUCT_ID = ?1 AND TEST_SEQ = ?2 AND INSPECTION_PGM_ID = ?3";
	private static String UPDATE_LET_RESULT_PRODUCTION = "update LetResult e set e.production=:production where e.id.productId=:productId and e.id.testSeq=:testSeq" ; 
	private static String GET_LET_INSPECTION_RESULT_BY_PRG_ID_AND_PARAM_ID = "SELECT inspection_param_value FROM gal704tbxv where PRODUCT_ID = ?1 and test_seq = (SELECT max(test_seq) FROM gal703tbxv where inspection_pgm_id = ?2 and PRODUCT_ID = ?1 and inspection_pgm_status= ?4) and inspection_param_id = ?3 and INSPECTION_PGM_ID = ?2 for read only with UR ";
	
	private static String LOTOUT_LIST_BODY_SQL =
		"SELECT "
		+ "   T701.PRODUCT_ID, "
		+ "   T144.PRODUCT_SPEC_CODE "
		+ "FROM "
		+ "  ( "
		+ "    SELECT "
		+ "      TMP02.PRODUCT_ID, "
		+ "      TMP02.INSPECTION_PGM_ID, "
		+ "      MAX(TMP02.END_TIMESTAMP) END_TIMESTAMP "
		+ "    FROM "
		+ "      GAL701TBX TMP01 INNER JOIN <%GAL703TBX%> TMP02 ON TMP01.PRODUCT_ID = TMP02.PRODUCT_ID "
		+ "    WHERE "
		+ "      TMP01.END_TIMESTAMP BETWEEN '<%DATE_FROM%>' AND '<%DATE_TO%>' "
		+ "    GROUP BY "
		+ "      TMP02.PRODUCT_ID, "
		+ "      TMP02.INSPECTION_PGM_ID "
		+ "    ) "
		+ "  T701 INNER JOIN <%GAL703TBX%> T703 ON T701.PRODUCT_ID = T703.PRODUCT_ID AND "
		+ "    T703.INSPECTION_PGM_ID = T701.INSPECTION_PGM_ID AND "
		+ "    T703.END_TIMESTAMP = T701.END_TIMESTAMP "
		+ "  INNER JOIN GAL143TBX T143 ON T703.PRODUCT_ID = T143.PRODUCT_ID "
		+ "  INNER JOIN GAL144TBX T144 ON T143.PRODUCT_SPEC_CODE = T144.PRODUCT_SPEC_CODE "
		+ "  INNER JOIN GAL714TBX T714 ON T703.INSPECTION_PGM_ID = T714.INSPECTION_PGM_ID "
		+ "WHERE "
		+ "  T703.INSPECTION_PGM_STATUS <> '1' "
		+ "  <%WHERE_INS_PGM_ID%> "
		+ "  <%WHERE_MTO%> "
		+ "GROUP BY T701.PRODUCT_ID,T144.PRODUCT_SPEC_CODE ";
	private static String LOTOUT_LIST_HEAD_SQL =
		"SELECT "
		+ "  TMP.PRODUCT_ID,"
		+ "  TMP.PRODUCT_SPEC_CODE "
		+ "FROM ( ";
	private static String LOTOUT_LIST_FOOTER_SQL =
		") TMP " + "ORDER BY " + "  TMP.PRODUCT_ID " + "FOR READ ONLY ";
	private static final String FAULT_RESULT_CNT_BODY_SQL =
		"SELECT "
		+ "   T701.PRODUCT_ID "
		+ "FROM "
		+ "  ( "
		+ "    SELECT "
		+ "      TMP02.PRODUCT_ID, "
		+ "      TMP02.INSPECTION_PGM_ID, "
		+ "      MAX(TMP02.END_TIMESTAMP) END_TIMESTAMP "
		+ "    FROM "
		+ "      GAL701TBX TMP01 INNER JOIN <%GAL703TBX%> TMP02 ON TMP01.PRODUCT_ID = TMP02.PRODUCT_ID "
		+ "    WHERE "
		+ "      TMP01.END_TIMESTAMP BETWEEN '<%DATE_FROM%>' AND '<%DATE_TO%>' "
		+ "    GROUP BY "
		+ "      TMP02.PRODUCT_ID, "
		+ "      TMP02.INSPECTION_PGM_ID "
		+ "    ) "
		+ "  T701 INNER JOIN <%GAL703TBX%> T703 ON T701.PRODUCT_ID = T703.PRODUCT_ID AND "
		+ "    T703.INSPECTION_PGM_ID = T701.INSPECTION_PGM_ID AND "
		+ "    T703.END_TIMESTAMP = T701.END_TIMESTAMP "
		+ "  INNER JOIN GAL143TBX T143 ON T703.PRODUCT_ID = T143.PRODUCT_ID "
		+ "  INNER JOIN GAL144TBX T144 ON T143.PRODUCT_SPEC_CODE = T144.PRODUCT_SPEC_CODE "
		+ "  INNER JOIN GAL714TBX T714 ON T703.INSPECTION_PGM_ID = T714.INSPECTION_PGM_ID "
		+ "WHERE "
		+ "  T703.INSPECTION_PGM_STATUS <> '1' "
		+ "  <%WHERE_INS_PGM_ID%> "
		+ "  <%WHERE_MTO%> "
		+ "GROUP BY T701.PRODUCT_ID ";

	private static final String FAULT_RESULT_CNT_FOOTER_SQL =
		") TMP " + "FOR READ ONLY ";
	private static final String FAULT_RESULT_CNT_HEAD_SQL =
		"SELECT " + "  COUNT(PRODUCT_ID) CNT " + "FROM ( ";

	private static final String FAULT_RESULT_LIST_BODY_SQL =
		"SELECT "
		+ "   T701.PRODUCT_ID, "
		+ "   T703.PROCESS_STEP, "
		+ "   T714.INSPECTION_PGM_NAME, "
		+ "   T703.INSPECTION_PGM_STATUS, "
		+ "   T144.PRODUCT_SPEC_CODE "
		+ "FROM "
		+ "  ( "
		+ "    SELECT "
		+ "      TMP02.PRODUCT_ID, "
		+ "      TMP02.INSPECTION_PGM_ID, "
		+ "      MAX(TMP02.END_TIMESTAMP) END_TIMESTAMP "
		+ "    FROM "
		+ "      GAL701TBX TMP01 INNER JOIN <%GAL703TBX%> TMP02 ON TMP01.PRODUCT_ID = TMP02.PRODUCT_ID "
		+ "    WHERE "
		+ "      TMP01.END_TIMESTAMP BETWEEN '<%DATE_FROM%>' AND '<%DATE_TO%>' "
		+ "    GROUP BY "
		+ "      TMP02.PRODUCT_ID, "
		+ "      TMP02.INSPECTION_PGM_ID "
		+ "    ) "
		+ "  T701 INNER JOIN <%GAL703TBX%> T703 ON T701.PRODUCT_ID = T703.PRODUCT_ID AND "
		+ "    T703.INSPECTION_PGM_ID = T701.INSPECTION_PGM_ID AND "
		+ "    T703.END_TIMESTAMP = T701.END_TIMESTAMP "
		+ "  INNER JOIN GAL143TBX T143 ON T703.PRODUCT_ID = T143.PRODUCT_ID "
		+ "  INNER JOIN GAL144TBX T144 ON T143.PRODUCT_SPEC_CODE = T144.PRODUCT_SPEC_CODE "
		+ "  INNER JOIN GAL714TBX T714 ON T703.INSPECTION_PGM_ID = T714.INSPECTION_PGM_ID "
		+ "WHERE "
		+ "  T703.INSPECTION_PGM_STATUS <> '1' "
		+ "  <%WHERE_INS_PGM_ID%> "
		+ "  <%WHERE_MTO%> ";
	private static final String FAULT_RESULT_LIST_FOOTER_SQL =
		") TMP "
		+ "ORDER BY "
		+ "  TMP.PRODUCT_ID, "
		+ "  SUBSTR(TMP.PROCESS_STEP , 1,1 ) DESC, "
		+ "  SUBSTR(TMP.PROCESS_STEP , 2 ) "
		+ "FOR READ ONLY ";
	private static final String FAULT_RESULT_LIST_HEAD_SQL =
		"SELECT "
		+ "  TMP.PRODUCT_ID, "
		+ "  TMP.PROCESS_STEP, "
		+ "  TMP.INSPECTION_PGM_NAME, "
		+ "  TMP.INSPECTION_PGM_STATUS, "
		+ "  TMP.PRODUCT_SPEC_CODE "
		+ "FROM ( ";
	private static final String WHERE_YEAR_SQL ="  AND T144.MODEL_YEAR_CODE = '<%YEAR%>' ";
	private static final String WHERE_MODEL_SQL = "  AND T144.MODEL_CODE = '<%MODEL%>' ";
	private static final String WHERE_TYPE_SQL = "  AND T144.MODEL_TYPE_CODE = '<%TYPE%>' ";
	private static final String WHERE_OPTION_SQL ="  AND T144.MODEL_OPTION_CODE = '<%OPTION%>' ";
	private static final String SQL_TAG_DATE_FROM = "<%DATE_FROM%>";
	private static final String SQL_TAG_DATE_TO = "<%DATE_TO%>";
	private static final String SQL_TAG_GAL703TBL = "<%GAL703TBX%>";
	private static final String SQL_TAG_INS_PGM_ID = "<%INS_PGM_ID%>";
	private static final String SQL_TAG_MODEL = "<%MODEL%>";
	private static final String SQL_TAG_OPTIN = "<%OPTION%>";
	private static final String SQL_TAG_TYPE = "<%TYPE%>";
	private static final String SQL_TAG_WHERE_INS_PGM_ID = "<%WHERE_INS_PGM_ID%>";
	private static final String SQL_TAG_WHERE_MTO = "<%WHERE_MTO%>";
	private static final String SQL_TAG_YEAR = "<%YEAR%>";
	private static final String SQL_UNION = "  UNION ALL ";
	private static final String WHERE_INS_PGM_SQL = "  AND T714.INSPECTION_PGM_ID = <%INS_PGM_ID%> ";
	private static final String INSERT_IF_NOT_EXIST = "insert into gal701tbx ( \n" 
        + "product_id, test_seq, build_code, test_id, seq_step_file \n"
        + ", start_timestamp, end_timestamp, cont_step_file, seq_range, let_mfg_area_code \n"
        + ", let_mfg_no, let_line_no, production, total_result_status, base_release \n"
        + ") select \n"
        + "?1, (select count(*) + 1 from gal701tbx where product_id = ?1) \n"
        + ",?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15 \n"
        + "from sysibm.sysdummy1 \n"
        + "where (select count(*) from gal701tbx where product_id = ?1 and end_timestamp = ?7) = 0";
	
	public int getProductCount(String productId) 
	{
		long productCount = count(Parameters.with("id.productId", productId));
		return (int) productCount;
	}

	public Timestamp getMaxEndTimestamp(String productId) 
	{      
		Parameters params = Parameters.with("id.productId", productId);					
		return max("endTimestamp",Timestamp.class,params);
	}

	public List<Object[]> getLetPassCriteriaProgramDataForMto(String yearCode,String modelCode,String typeCode,String optionCode,Timestamp timestamp)
	{
		Parameters params = Parameters.with("1", yearCode).put("2", modelCode).put("3", typeCode).put("4", optionCode).put("5", timestamp);
		return	findAllByNativeQuery(GET_PASS_CRITERIA_PROGRAM_DATA_FOR_MTO, params, Object[].class);
	}

	public List<Object[]> getLetInspectionResults(String strProductId,String strTestSeq,String strInspectionPgmId)
	{
		Parameters params = Parameters.with("1", strProductId).put("2", Integer.parseInt(strTestSeq)).put("3", Integer.parseInt(strInspectionPgmId));
		return	findAllByNativeQuery(GET_LET_INSPECTION_RESULTS, params, Object[].class);
	}

	public Object[] getLetProduct(String seriesFrame )
	{
		Parameters params = Parameters.with("1", seriesFrame);
		return	findFirstByNativeQuery(GET_LET_PRODUCT, params, Object[].class);
	}

	public Object[] getLetProductSeriesFrame(String productId)
	{
		Parameters params = Parameters.with("1", productId);
		return	findFirstByNativeQuery(GET_LET_PRODUCT_SERIES_FRAME, params, Object[].class);
	}

	public List<Object[]> getVinMtoData(String strProductId)
	{
		Parameters params = Parameters.with("1", strProductId);
		return	findAllByNativeQuery(GET_VIN_MTO_DATA, params, Object[].class);
	}

	public List<Object[]> getLetResultByProductId(String strProductId)
	{
		Parameters params = Parameters.with("1", strProductId);
		return	findAllByNativeQuery(GET_LET_RESULT_BY_PRODUCT_ID, params, Object[].class);
	}

	public List<Object[]> getFaultDataByProductIdTestSeq(String strProductId,int testSeq)
	{
		Parameters params = Parameters.with("1", strProductId).put("2", testSeq);
		return	findAllByNativeQuery(GET_FAULT_DATA_BY_PRODUCT_ID_TEST_SEQ, params, Object[].class);
	}

	public List<Object[]> getInspectionResult(String productId,Integer seqNum,Integer inspPgmId) 
	{
		Parameters params = Parameters.with("1", productId).put("2", seqNum).put("3",inspPgmId);
		return findAllByNativeQuery(GET_INSPECTIONT_RESULT, params, Object[].class);
	}

	public List<LetResult> findLetResultByProductIdSeq(String productId,Integer testSeq) 
	{
		Parameters params = Parameters.with("id.productId", productId).put("id.testSeq", testSeq);
		return findAll(params);
	}		

	public Object[] getLetProgramResultByProductIdSeqNum(String productId,Integer testSeq) 
	{      
		Parameters params = Parameters.with("1", productId).put("2", testSeq);			
		return	findFirstByNativeQuery(GET_LET_PROGRAM_RESULT_BY_PRODUCTID_SEQNUM, params, Object[].class);        	
	}

	@Transactional
	public int deleteLetProgramResultByProductIdSeqNum(String productId,Integer testSeq) 
	{
		Parameters params = Parameters.with("1", productId).put("2", testSeq);	
		return executeNativeUpdate(DELETE_PROGRAM_RESULT_BY_PRODUCTID_SEQNUM,params);
	}

	public Object[] getLetProgramResultValueByProductIdSeqNum(String productId,Integer testSeq) 
	{      
		Parameters params = Parameters.with("1", productId).put("2", testSeq);			
		return	findFirstByNativeQuery(GET_LET_PROGRAM_RESULT_VALUE_BY_PRODUCTID_SEQNUM, params, Object[].class);        	
	}
	
	public List<Object[]> getLetProgramResultValueByProductIdParamId(String productId,Integer paramId)
	{      
		Parameters params = Parameters.with("1", productId).put("2", paramId);			
		return	findAllByNativeQuery(GET_LET_PROGRAM_RESULT_VALUE_BY_PRODUCTID_PARAMID, params, Object[].class);        	
	}
	
	public List<Object[]> getLetProgramResultValueByProductIdParamIdEndTime(
			String productId, Integer paramId, Timestamp startTime,
			Timestamp endTime) {
		Parameters params = Parameters.with("1", productId).
			put("2", paramId).
			put("3", startTime).
			put("4", endTime);			
		return	findAllByNativeQuery(GET_LET_PROGRAM_RESULT_VALUE_BY_PRODUCTID_PARAMID_ENDTIME, params, Object[].class);      
	}

	public List<Object[]> getLetProgramResultValueByParamIdEndTime(Integer paramId,
			Timestamp startTime, Timestamp endTime) {
		Parameters params = Parameters.with("1", paramId).
		put("2", startTime).
		put("3", endTime);			
		return	findAllByNativeQuery(GET_LET_PROGRAM_RESULT_VALUE_BY_PARAMID_ENDTIME, params, Object[].class);    
	}
	
	public Object[] getLetProgramResultValueById(LetProgramResultValueId id) {
		Parameters params = Parameters.with("1", id.getEndTimestamp()).
		put("2", id.getProductId()).
		put("3", id.getTestSeq()).	
		put("4", id.getInspectionPgmId()).	
		put("5", id.getInspectionParamId()).	
		put("6", id.getInspectionParamType());	
		return	findFirstByNativeQuery(GET_LET_PROGRAM_RESULT_VALUE_BY_ID, params, Object[].class);    
	}
	
	@Transactional
	public int deleteLetProgramResultValueByProductIdSeqNum(String productId,Integer testSeq) 
	{
		Parameters params = Parameters.with("1", productId).put("2", testSeq);	
		return executeNativeUpdate(DELETE_PROGRAM_RESULT_VALUE_BY_PRODUCTID_SEQNUM,params);
	}

	public LetResult findLetResultByProductIdSeqNum(String productId,Integer testSeq) 
	{
		Parameters params = Parameters.with("id.productId",productId).put("id.testSeq", testSeq);
		return findFirst(params);		
	}

	public LetResult findFirstByProductIdTimestamp(String productId, Timestamp endTimestamp) {
		Parameters params = Parameters.with("id.productId", productId).put("endTimestamp", endTimestamp);
		return findFirst(params);
	}

	@Transactional
	public boolean insertIfNotExist(LetResult letResult) {
	    String sql = INSERT_IF_NOT_EXIST;
	    Parameters params = new Parameters();
	    params.put("1", letResult.getId().getProductId());
	    params.put("3", letResult.getBuildCode());
	    params.put("4", letResult.getTestId());
	    params.put("5", letResult.getSeqStepFile());
	    params.put("6", letResult.getStartTimestamp());
	    params.put("7", letResult.getEndTimestamp());
	    params.put("8", letResult.getContStepFile());
	    params.put("9", letResult.getSeqRange());
	    params.put("10", letResult.getLetMfgAreaCode());
	    params.put("11", letResult.getLetMfgNo());
	    params.put("12", letResult.getLetLineNo());
	    params.put("13", letResult.getProduction());
	    params.put("14", letResult.getTotalResultStatus());
	    params.put("15", letResult.getBaseRelease());
	    int insertedCount = executeNative(sql, params);
	    if (insertedCount > 0) {
	        return true;
	    } else {
	        return false;
	    }
	}

	@Transactional
	public int deleteLetResultByProductIdSeqNum(String productId,Integer testSeq) 
	{
		Parameters params = Parameters.with("id.productId", productId).put("id.testSeq", testSeq);	
		return delete(params);
	}

	public List<Object[]> findDistinctLetResultByProductIdSeq(String productId) 
	{
		Parameters params = Parameters.with("1", productId);
		return findAllByNativeQuery(GET_DISTINCT_LET_PROGRAMS_BY_PRODUCTID, params, Object[].class);
	}


	public List<Object[]> getLetManualInputResultData(String productId,Integer programId) {
		Parameters params = Parameters.with("1", productId).put("2",programId);
		return findAllByNativeQuery(GET_LET_MANUAL_INPUT_RESULT_DATA, params, Object[].class);
	}

	public List<Object[]> getInspectionDetailResults(String productId, Integer programId) {
		Parameters params = Parameters.with("1", productId).put("2", programId);
		return findAllByNativeQuery(GET_INSPECTION_DETAIL_RESULTS, params, Object[].class);
	}
	

	@Transactional
	public int updateLetResultProduction(String productId ,Integer testSeq,String production)
	{
		Parameters param=Parameters.with("productId",productId);
		param.put("testSeq", testSeq);
		param.put("production", production);
		return executeUpdate(UPDATE_LET_RESULT_PRODUCTION,param);
	}

	@Transactional
	public int updateProgramResultByEndTimestamp(String productId ,Integer testSeq,Integer inspectionPgmId,String processStatus)
	{
		Parameters param=Parameters.with("1",productId);
		param.put("2", testSeq);
		param.put("3", inspectionPgmId);
		param.put("4", processStatus);
		return executeNativeUpdate(UPDATE_PROGRAM_RESULT_BY_END_TIMESTAMP,param);
	}

	public List<Object[]> getLotOutListData(String reqYear,String reqModel,String reqType,String reqOption,String sqlDateFrom,String sqlDateTo,Integer pgmId){
		String sql=createLetFaultResultDownloadSQL(LOTOUT_LIST_BODY_SQL,LOTOUT_LIST_HEAD_SQL,LOTOUT_LIST_FOOTER_SQL,reqYear, reqModel, reqType, reqOption, sqlDateFrom, sqlDateTo, pgmId);
		return findAllByNativeQuery(sql, null, Object[].class);
	}

	public int getFaultListCntData(String reqYear,String reqModel,String reqType,String reqOption,String sqlDateFrom,String sqlDateTo,Integer pgmId)
	{
		String sql=createLetFaultResultDownloadSQL(FAULT_RESULT_CNT_BODY_SQL,FAULT_RESULT_CNT_HEAD_SQL,FAULT_RESULT_CNT_FOOTER_SQL,reqYear, reqModel, reqType, reqOption, sqlDateFrom, sqlDateTo, pgmId);
		return findFirstByNativeQuery(sql, null, Integer.class);
	}

	public List<Object[]> getFaultResultListData(String reqYear,String reqModel,String reqType,String reqOption,String sqlDateFrom,String sqlDateTo,Integer pgmId)
	{
		String sql=createLetFaultResultDownloadSQL(FAULT_RESULT_LIST_BODY_SQL,FAULT_RESULT_LIST_HEAD_SQL,FAULT_RESULT_LIST_FOOTER_SQL,reqYear, reqModel, reqType, reqOption, sqlDateFrom, sqlDateTo, pgmId);
		return findAllByNativeQuery(sql, null, Object[].class);
	}

	public String getInspectionResult(String productId, int pgmId, int paramId, int pgmStatus) {
		Parameters params = new Parameters();
		params.put("1", productId);
		params.put("2", pgmId);
		params.put("3", paramId);
		params.put("4", pgmStatus);
		
		return findFirstByNativeQuery(GET_LET_INSPECTION_RESULT_BY_PRG_ID_AND_PARAM_ID, params, String.class);
	}
	
	private String createLetFaultResultDownloadSQL(String body, String head, String footer,String reqYear,String reqModel,String reqType,String reqOption,String sqlDateFrom,String sqlDateTo,Integer pgmId) 
	{
		StringBuffer bufWhereMto = new StringBuffer();
		if (reqYear.length() > 0) {
			bufWhereMto.append(WHERE_YEAR_SQL.replaceFirst(SQL_TAG_YEAR, handleSQLEscapeSequence(reqYear)));
		}
		if (reqModel.length() > 0) {
			bufWhereMto.append(WHERE_MODEL_SQL.replaceFirst(SQL_TAG_MODEL,handleSQLEscapeSequence(reqModel)));
		}
		if (reqType.length() > 0) {
			bufWhereMto.append(WHERE_TYPE_SQL.replaceFirst(SQL_TAG_TYPE, handleSQLEscapeSequence(reqType)));
		}
		if (reqOption.length() > 0) {
			bufWhereMto.append(WHERE_OPTION_SQL.replaceFirst(SQL_TAG_OPTIN,handleSQLEscapeSequence(reqOption)));
		}
		String insPgmId = "";
		if (pgmId!=null) {
			insPgmId =WHERE_INS_PGM_SQL.replaceFirst(SQL_TAG_INS_PGM_ID, pgmId.toString());
		}
		String template = null;
		StringBuffer bufSql = new StringBuffer(head);
		List<String> gal703tblList = new ArrayList<String>();
		LetProgramResultEnum[] letProgramList=LetProgramResultEnum.values();
		for(LetProgramResultEnum letProgramResultEnum:letProgramList)
		{
			gal703tblList.add(letProgramResultEnum.name());
		}
		int rows = gal703tblList.size();
		for (int i = 0; i < rows; i++) {
			if (i > 0) {
				bufSql.append(SQL_UNION);
			}
			template = body;
			template =template.replaceAll(SQL_TAG_GAL703TBL,(String) gal703tblList.get(i));
			template = template.replaceFirst(SQL_TAG_DATE_FROM, sqlDateFrom);
			template = template.replaceFirst(SQL_TAG_DATE_TO, sqlDateTo);
			template = template.replaceFirst(SQL_TAG_YEAR, reqYear);
			template = template.replaceFirst(SQL_TAG_MODEL, reqModel);
			template = template.replaceFirst(SQL_TAG_WHERE_MTO,bufWhereMto.toString());
			template =template.replaceFirst(SQL_TAG_WHERE_INS_PGM_ID, insPgmId);
			bufSql.append(template);
		}
		bufWhereMto = null;
		bufSql.append(footer);
		return bufSql.toString();
	}
	
	private  String handleSQLEscapeSequence(String input) {

	        String result = "";

	        for (int i = 0; i < input.length(); i++) {
	            switch (input.charAt(i)) {
	                case '\'':
	                    result += "''";
	                    break;
	                case '\\':
	                    result += "\\\\";
	                    break;
	                default:
	                    result += input.charAt(i);
	            }
	        }

	        return result;
	    }
}
