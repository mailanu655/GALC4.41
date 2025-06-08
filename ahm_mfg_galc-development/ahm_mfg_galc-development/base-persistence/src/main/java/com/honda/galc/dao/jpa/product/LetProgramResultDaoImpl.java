package com.honda.galc.dao.jpa.product;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetProgramResultDao;
import com.honda.galc.data.LetInspectionDownloadDto;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.entity.enumtype.LetProgramResultEnum;
import com.honda.galc.entity.enumtype.LetProgramResultValueEnum;
import com.honda.galc.entity.product.LetProgramResult;
import com.honda.galc.entity.product.LetProgramResultId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 25, 2013
 */

@SuppressWarnings(value = { "all" })
public class LetProgramResultDaoImpl extends BaseDaoImpl<LetProgramResult, LetProgramResultId> implements LetProgramResultDao 
{
	
	private final String SQL_T703_HEAD = "SELECT A.PRODUCT_ID, "
		+ "B.MODEL_YEAR_CODE, B.MODEL_CODE, B.MODEL_TYPE_CODE, B.MODEL_OPTION_CODE, "
		+ "C.TEST_SEQ, C.SEQ_STEP_FILE, C.CONT_STEP_FILE, C.END_TIMESTAMP, "
		+ "D.INSPECTION_PGM_STATUS, D.PROCESS_STEP, D.PROCESS_END_TIMESTAMP, D.LET_TERMINAL_ID, D.LET_RESULT_CAL, C.BASE_RELEASE, C.PRODUCTION "
		+ "FROM GAL143TBX A, GAL144TBX B, GAL701TBX C, ";

	private final String SQL_T704_HEAD = "SELECT A.PRODUCT_ID, "
		+ "C.TEST_SEQ, "
		+ "E.INSPECTION_PARAM_NAME, "
		+ "D.INSPECTION_PARAM_VALUE, "
		+ "D.INSPECTION_PARAM_UNIT, "
		+ "D.INSPECTION_PARAM_TYPE, "
		+ "D.LOW_LIMIT, "
		+ "D.HIGH_LIMIT "
		+ "FROM GAL143TBX A, GAL144TBX B, GAL701TBX C, ";

	private final String SQL_T703_JOIN = "WHERE A.PRODUCT_SPEC_CODE = B.PRODUCT_SPEC_CODE"
		+ " AND A.PRODUCT_ID = C.PRODUCT_ID"
		+ " AND C.END_TIMESTAMP = D.END_TIMESTAMP"
		+ " AND C.PRODUCT_ID = D.PRODUCT_ID"
		+ " AND C.TEST_SEQ = D.TEST_SEQ ";

	private final String SQL_T704_JOIN = ", GAL715TBX E "
		+ "WHERE A.PRODUCT_SPEC_CODE = B.PRODUCT_SPEC_CODE"
		+ " AND A.PRODUCT_ID = C.PRODUCT_ID"
		+ " AND C.END_TIMESTAMP = D.END_TIMESTAMP"
		+ " AND C.PRODUCT_ID = D.PRODUCT_ID"
		+ " AND C.TEST_SEQ = D.TEST_SEQ "
		+ " AND D.INSPECTION_PARAM_ID = E.INSPECTION_PARAM_ID ";

	private final String SQL_T703_ORDER = " ORDER BY C.END_TIMESTAMP ";
	private DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat format2 = new SimpleDateFormat("yyyyMMdd");
	
	private static final String LET_CONNECTED_RESULT_TMP_TBL = 
			"all_results as (select ip.inspection_pgm_name, let.* from gal703tbxv "
			+ "let join gal714tbx ip on ip.inspection_pgm_id = let.inspection_pgm_id where let.product_id = ?1) " 
			+ ",last_result as (select ar.* from all_results ar where (ar.end_timestamp, ar.product_id, ar.inspection_pgm_id) " 
			+ "		in (select max(all.end_timestamp), all.product_id, all.inspection_pgm_id from all_results all group by all.product_id, all.inspection_pgm_id))"
            + ",triggering_result as ( \n"
	        + "    select  \n"
	        + "    cp.triggering_pgm_id, trg_ip.inspection_pgm_name as triggering_pgm_name, trg_lr.inspection_pgm_id as triggering_inspection_pgm_id \n"
	        + "    ,cp.criteria_pgm_id, ter_cp.criteria_pgm_name as terminating_pgm_name, ter_lr.inspection_pgm_id as terminating_inspection_pgm_id \n"	
	        + "    ,trg_lr.* \n"
	        + "    from let_connected_program_tbx cp \n"
	        + "    join gal714tbx trg_ip on trg_ip.inspection_pgm_id = cp.triggering_pgm_id \n"
	        + "    join gal718tbx ter_cp on ter_cp.criteria_pgm_id = cp.criteria_pgm_id \n"
	        + "    left join last_result trg_lr on trg_lr.inspection_pgm_id = trg_ip.inspection_pgm_id \n"
	        + "    left join last_result ter_lr on ter_lr.inspection_pgm_name = ter_cp.criteria_pgm_name \n"
	        + ")\n"
	        + ",last_triggering_result as ( \n"
	        + "    select * from triggering_result where (process_end_timestamp,product_id, criteria_pgm_id) \n" 
	        + "    in (select max(process_end_timestamp), product_id, criteria_pgm_id from triggering_result where product_id = ?1  group by  product_id, criteria_pgm_id) \n" 
	        + ") \n"
	        + ",connected_result as ( \n"
	        + "    select \n" 
	        + "    case \n"
	        + "        when trg_lr.triggering_pgm_name is not null and lr.inspection_pgm_status = 1 and trg_lr.process_end_timestamp > lr.process_end_timestamp then '" + LetInspectionStatus.PASS_TERMINATED.getId() + "' \n"
	        + "        else lr.inspection_pgm_status \n"
	        + "    end as connected_inspection_pgm_status \n"
	        + "    ,trg_lr.triggering_pgm_id, trg_lr.triggering_pgm_name, trg_lr.triggering_inspection_pgm_id, trg_lr.process_end_timestamp as triggering_process_end_timestamp \n"
	        + "    ,lr.* \n"
	        + "    from last_result lr \n" 
	        + "    left join last_triggering_result trg_lr on trg_lr.terminating_inspection_pgm_id = lr.inspection_pgm_id \n"
	        + ")";
	private static final String CHECK_LET_RESULT = 
			"with " + LET_CONNECTED_RESULT_TMP_TBL + " \n"
			+ ",required_program_mto as ( \n"
			+ "    select criteria_pgm.criteria_pgm_id, criteria_pgm.criteria_pgm_name,  pgm_category.pgm_category_name \n" 
            + "    from gal143tbx prd \n"  
            + "    inner join gal144tbx mtoc on prd.product_spec_code = mtoc.product_spec_code \n"    
            + "    inner join gal717tbx criteria on criteria.model_code = mtoc.model_code and (criteria.model_option_code = mtoc.model_option_code or criteria.model_option_code = '*') and criteria.model_type_code = mtoc.model_type_code and criteria.model_year_code = mtoc.model_year_code \n"  
            + "    inner join gal716tbx criteria_mto on criteria_mto.model_year_code = criteria.model_year_code and criteria_mto.model_option_code = criteria.model_option_code and criteria_mto.model_type_code = criteria.model_type_code and criteria_mto.model_code = criteria.model_code and criteria_mto.effective_time = criteria.effective_time and current_timestamp >= criteria_mto.effective_time and current_timestamp < criteria_mto.expiration_time \n"  
            + "    inner join gal718tbx criteria_pgm on criteria_pgm.criteria_pgm_id = criteria.criteria_pgm_id \n"    
            + "    inner join gal730tbx pgm_category on pgm_category.inspection_device_type = criteria_pgm.inspection_device_type and pgm_category.criteria_pgm_attribute = criteria_pgm.criteria_pgm_attribute \n"  
            + "    where prd.product_id = ?1 \n"
            + ") \n"
            + "select criteria_pgm_name \n"
            + ",connected_inspection_pgm_status as result \n"
            + ",criteria_pgm_id, pgm_category_name, connected_inspection_pgm_status, inspection_pgm_status \n"
            + "from \n" 
            + "required_program_mto rp \n"
            + "left join connected_result  con_r on con_r.inspection_pgm_name = rp.criteria_pgm_name \n"
            + "where (connected_inspection_pgm_status is null or connected_inspection_pgm_status <> '" + LetInspectionStatus.Pass.getId() + "') \n";
	private static final String GET_LET_CONNECTED_RESULT_PROGRAM_DATA_FOR_PRODUCT_ID = 
			"with " + LET_CONNECTED_RESULT_TMP_TBL + " \n"
			+ "select \n"
            + "case \n"  
            + "    when connected_inspection_pgm_status = '" + LetInspectionStatus.PASS_TERMINATED.getId() + "' then '" + LetInspectionStatus.PASS_TERMINATED.getLabel() + "' \n"
            + "    else r.process_step \n"
            + "end as process_step \n"
			+ ",r.test_seq , r.process_end_timestamp, r.inspection_pgm_id, r.inspection_pgm_name \n" 
			+ ", connected_inspection_pgm_status as inspection_pgm_status"
			+ ", test.base_release \n"  
			+ ",r.connected_inspection_pgm_status ,r.triggering_pgm_name, r.triggering_process_end_timestamp \n"
            + "from gal701tbx test \n"
            + "left join connected_result r on r.product_id = test.product_id and test.test_seq = r.test_seq \n" 
            + "where test.product_id = ?1 \n"
            + "order by substr(r.process_step , 1, 1 ) desc, substr(r.process_step , 2 ), r.inspection_pgm_name for read only \n";	
	
	private static final String GET_CONNECTED_INSPECTION_STATUS = "with " + LET_CONNECTED_RESULT_TMP_TBL + " \n" 
			+ "select connected_inspection_pgm_status from connected_result where product_id = ?1 and inspection_pgm_id = ?2 ";
	

	public List<Object[]> fetchLetProgramResultDownloadData(LetInspectionDownloadDto dto)	
	{
		Vector vecPhy703TblName = new Vector();
		LetProgramResultEnum[] letProgramList=LetProgramResultEnum.values();
		for(LetProgramResultEnum letProgramResultEnum:letProgramList)
		{
			vecPhy703TblName.add(letProgramResultEnum.name());
		}
		StringBuffer sbfConditions=buildDynamicSqlConditions(dto);
        StringBuffer sbfCreateSQL1 = new StringBuffer(SQL_T703_HEAD);
		sbfCreateSQL1.append("( ");
		for (Iterator itrPhy703TblName = vecPhy703TblName.iterator(); itrPhy703TblName.hasNext();) 
		{
			sbfCreateSQL1.append("SELECT END_TIMESTAMP, "
					+ "PRODUCT_ID, "
					+ "TEST_SEQ, "
					+ "INSPECTION_PGM_ID, "
					+ "INSPECTION_PGM_STATUS, "
					+ "PROCESS_STEP, "
					+ "PROCESS_END_TIMESTAMP,"
					+ "LET_TERMINAL_ID, "
					+ "LET_RESULT_CAL "
					+ "FROM " + itrPhy703TblName.next());
			if (itrPhy703TblName.hasNext()) 
			{
				sbfCreateSQL1.append(" UNION ALL ");
			}
		}
		sbfCreateSQL1.append(") D " + SQL_T703_JOIN);
		sbfCreateSQL1.append(sbfConditions + SQL_T703_ORDER + " FOR READ ONLY WITH UR");     
		return findAllByNativeQuery(sbfCreateSQL1.toString(), null,Object[].class);
		
	}
	
	public StringBuffer buildDynamicSqlConditions(LetInspectionDownloadDto dto)
	{
		StringBuffer sbfConditions = new StringBuffer();
		if (!dto.getFrameNo().equals("")) {
			String strFNo = dto.getFrameNo();
			sbfConditions.append("AND A.PRODUCT_ID IN (SELECT PRODUCT_ID FROM GAL700TBX WHERE ");
			if (!dto.getProductId().equals("")) {
				String strProductID = dto.getProductId();
				int prodLengh = strProductID.length();
				if (strProductID.indexOf("?") == -1 && strProductID.indexOf("*") == -1) {
					sbfConditions.append("PRODUCT_ID = '" + strProductID + "' OR ");
				} else if (prodLengh <= 11) {
					sbfConditions.append("PRODUCT_ID LIKE '" + strProductID.replace('?', '_').replace('*', '%') + "' OR ");
					String strProductID_01 = strProductID;
					for (int i = 0; i < 17 - prodLengh; i++) 
					{
						strProductID_01 = " " + strProductID_01;
					}
					sbfConditions.append("PRODUCT_ID LIKE '" + strProductID_01.replace('?', '_').replace('*', '%') + "' OR ");
				} else {
					sbfConditions.append("PRODUCT_ID LIKE '" + strProductID.replace('?', '_').replace('*', '%') + "' OR ");
				}
			}
			if (strFNo.indexOf("?") == -1 && strFNo.indexOf("*") == -1) {
				sbfConditions.append("SERIES_FRAME_NO = '" + strFNo + "')");
			} else {
				sbfConditions.append("SERIES_FRAME_NO LIKE '" + strFNo.replace('?', '_').replace('*', '%') + "')");
			}
		} else if (!dto.getProductId().equals("")) {
			String strProductID = dto.getProductId();
			if (strProductID.indexOf("?") == -1 && strProductID.indexOf("*") == -1) {
				sbfConditions.append("AND A.PRODUCT_ID = '" + strProductID + "' ");
			} else {
				sbfConditions.append("AND A.PRODUCT_ID LIKE '" + strProductID.replace('?', '_').replace('*', '%') + "' ");
			}
		}
		if (!dto.getLineNo().equals("")) {
			sbfConditions.append("AND C.LET_LINE_NO = '" + dto.getLineNo() + "' ");
		}
		if (!dto.getModelYearCode().equals("")) {
			sbfConditions.append("AND B.MODEL_YEAR_CODE = '" + dto.getModelYearCode() + "' ");
		}
		if (!dto.getModelCode().equals("")) {
			sbfConditions.append("AND B.MODEL_CODE = '" + dto.getModelCode() + "' ");
		}
		if (!dto.getModelTypeCode().equals("")) {
			sbfConditions.append("AND B.MODEL_TYPE_CODE = '" + dto.getModelTypeCode()+ "' ");
		}
		if (!dto.getModelOptionCode().equals("")) {
			sbfConditions.append("AND B.MODEL_OPTION_CODE = '" + dto.getModelOptionCode() + "' ");
		}
		if (dto.getEndTimestampStartDate()!=null) {
			sbfConditions.append("AND C.END_TIMESTAMP >= '"+ dto.getEndTimestampStartDate() + "' ");
		}
		if (dto.getEndTimestampEndDate()!=null) {
			sbfConditions.append("AND C.END_TIMESTAMP <= '"+ dto.getEndTimestampEndDate() + "' ");
		}
		if (!dto.getProduction().equals("")) {
			sbfConditions.append("AND C.PRODUCTION = '" + dto.getProduction() + "' ");
		}
		sbfConditions.append("AND D.INSPECTION_PGM_ID = " + dto.getProgramId());
		
		return sbfConditions;
	}

	public List<Object[]> fetchLetProgramResultValueDownloadData(LetInspectionDownloadDto dto){
		 StringBuffer sbfConditions=buildDynamicSqlConditions(dto);
			LetProgramResultValueEnum[] letProgramValueList=LetProgramResultValueEnum.values();
			Vector vecPhy704TblName = new Vector();    			
			for(LetProgramResultValueEnum letProgramResultValueEnum:letProgramValueList)
			{
				vecPhy704TblName.add(letProgramResultValueEnum.name());
			}

			StringBuffer sbfCreateSQL2 = new StringBuffer(SQL_T704_HEAD);
			sbfCreateSQL2.append("( ");
			for (Iterator itrPhy704TblName = vecPhy704TblName.iterator(); itrPhy704TblName.hasNext();) {
				sbfCreateSQL2.append("SELECT END_TIMESTAMP, PRODUCT_ID, TEST_SEQ, INSPECTION_PGM_ID, INSPECTION_PARAM_ID, INSPECTION_PARAM_VALUE, INSPECTION_PARAM_UNIT, INSPECTION_PARAM_TYPE, LOW_LIMIT, HIGH_LIMIT FROM " + itrPhy704TblName.next());
				if (itrPhy704TblName.hasNext()) {
					sbfCreateSQL2.append(" UNION ALL ");
				}
			}
			sbfCreateSQL2.append(") D " + SQL_T704_JOIN);
			sbfCreateSQL2.append(sbfConditions + " FOR READ ONLY WITH UR");            
			return findAllByNativeQuery(sbfCreateSQL2.toString(), null,Object[].class);
	}

	public List<Object[]> getLetResultProgramDataForProductId(String productId)	{
		String sql = GET_LET_CONNECTED_RESULT_PROGRAM_DATA_FOR_PRODUCT_ID;	
		Parameters params = Parameters.with("1", productId);
		return	findAllByNativeQuery(sql, params, Object[].class);
	}
	
	public List<Map<String, Object>> findAllOutstandingPrograms(String productId, String letProgramCategory) {
		String sql = CHECK_LET_RESULT;
		if (StringUtils.isNotBlank(letProgramCategory)) {
			letProgramCategory = letProgramCategory.trim();
			letProgramCategory = StringUtil.toSqlInString(letProgramCategory);
			sql = sql + "and pgm_category_name in (" + letProgramCategory + ") \n";
		}
		sql = sql + "order by result, criteria_pgm_name";		
		Parameters params = new Parameters();
		params.put("1", productId);		
		return findResultMapByNativeQuery(sql, params);
	}
	
	public boolean isProgramResultPassTerminated(String productId, int inspectionPgmId) {
		String sql = GET_CONNECTED_INSPECTION_STATUS;			
		Parameters params = Parameters.with("1", productId);
		params.put("2", inspectionPgmId);
		Object result = findFirstByNativeQuery(sql, params, String.class);
		if (result == null) {
			return false;
		}
		String passIdStr = String.valueOf(LetInspectionStatus.PASS_TERMINATED.getId());
		if (passIdStr.equals(result)) {
			return true;
		} else { 
			return false;
		}
	}	
}
