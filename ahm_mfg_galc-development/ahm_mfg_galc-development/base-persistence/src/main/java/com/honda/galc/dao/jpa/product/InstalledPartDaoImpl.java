package com.honda.galc.dao.jpa.product;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.InstalledPartHistoryDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.data.InstalledPartDetail;
import com.honda.galc.data.MCInstalledPartDetailDto;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.InstalledPartDetailsDto;
import com.honda.galc.dto.SubAssemblyPartListDto;
import com.honda.galc.entity.IEntity;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.StringUtil;

/**
 * 
 * <h3>InstalledPartDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> InstalledPartDaoImpl description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Mar 26, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 26, 2012
 */
public class InstalledPartDaoImpl extends ProductBuildResultDaoImpl<InstalledPart,InstalledPartId> implements InstalledPartDao{
	
	private static final String COMMA =","; 

    private static final String FIND_ALL_VALID_PARTS = "SELECT p FROM InstalledPart p" +
    		" WHERE p.id.productId = :productId" +
    		" AND p.installedPartStatusId = 1" +
    		" AND p.id.partName in (:partNames)";
    
    private static final String FIND_INSTALLED_PART_FOR_PRODUCT = "SELECT p FROM InstalledPart p WHERE p.id.productId = :productId " +
    		"and p.id.partName in (:partNames) and p.installedPartStatusId <> -9";
    
    private static final String FIND_ALL_WITH_PART_DEFECTS = 
    		"SELECT p FROM InstalledPart p WHERE p.id.productId = :productId " +
    		"and p.defectRefId > 0";
    
    private static final String FIND_ALL_WITH_DEFECTS = 
    		"SELECT p FROM InstalledPart p "
    		+ "WHERE p.id.productId = :productId and "
    		+ " (p.defectRefId > 0 "
    		+ " OR p.id.partName IN"
    		+ "(SELECT DISTINCT m.id.partName from Measurement m "
    		+ "   WHERE m.id.productId = :productId and m.defectRefId > 0 ))";
    
    private static final String FIND_ENGINE_INSTALLED_PART_FOR_PRODUCTS_BY_TYPE = 
    		"SELECT p FROM InstalledPart p "+ 
			"    WHERE p.id.productId = :engineProductId " + 
			"    	AND p.id.partName IN (:partNames) " +	
			"		AND p.productType = :productType";

	@SuppressWarnings("unused")
	private static final String GET_ALL_INSTALLED_PART_DETAILS = 
		 "SELECT ip.PRODUCT_ID, c.PART_NAME, ip.PART_NAME, ip.PART_SERIAL_NUMBER," +
		 	"ip.INSTALLED_PART_REASON,ip.INSTALLED_PART_STATUS, part.MEASUREMENT_COUNT," +
		 	"a.MEASUREMENT_VALUE,a.MEASUREMENT_SEQUENCE_NUMBER," +
		 	"pp.PROCESS_POINT_ID,pp.PROCESS_POINT_NAME, part.PART_ID, part.PART_SERIAL_NUMBER_MASK, n.WINDOW_LABEL, ip.ASSOCIATE_NO, ip.ACTUAL_TIMESTAMP, " +
		 	" c.PART_CONFIRM_FLAG, a.MEASUREMENT_STATUS, " +
		 	"c.SEQUENCE_NUMBER,n.REPAIR_CHECK " +
		"FROM GALADM.GAL144TBX m " +
		"INNER JOIN GALADM.GAL143TBX f ON f.PRODUCT_SPEC_CODE = m.PRODUCT_SPEC_CODE " +
		"INNER JOIN GALADM.GAL246TBX c ON " +
			"(c.EXT_COLOR_CODE = m.EXT_COLOR_CODE or c.EXT_COLOR_CODE='*') AND " +
			"(c.INT_COLOR_CODE = m.INT_COLOR_CODE or c.INT_COLOR_CODE='*') AND " +
			"(c.MODEL_CODE = m.MODEL_CODE or c.MODEL_CODE='*') AND " +
			"(c.MODEL_OPTION_CODE = m.MODEL_OPTION_CODE or c.MODEL_OPTION_CODE='*') AND " +
			"(c.MODEL_TYPE_CODE = m.MODEL_TYPE_CODE or c.MODEL_TYPE_CODE='*') AND " +
			"(c.MODEL_YEAR_CODE = m.MODEL_YEAR_CODE or c.MODEL_YEAR_CODE='*') AND " +
		"INNER JOIN GALADM.GAL245TBX d ON " +
			"(c.EXT_COLOR_CODE = d.EXT_COLOR_CODE or c.EXT_COLOR_CODE='*') AND " +
			"(c.INT_COLOR_CODE = d.INT_COLOR_CODE or c.INT_COLOR_CODE='*') AND " +
			"(c.MODEL_CODE = m.MODEL_CODE or c.MODEL_CODE='*') AND " +
			"(c.MODEL_OPTION_CODE = d.MODEL_OPTION_CODE or c.MODEL_OPTION_CODE = '*') AND " +
			"(c.MODEL_TYPE_CODE = d.MODEL_TYPE_CODE or c.MODEL_TYPE_CODE = '*') AND " +
			"(c.MODEL_YEAR_CODE = m.MODEL_YEAR_CODE or c.MODEL_YEAR_CODE='*') AND " +
			"d.PART_NAME = c.PART_NAME " +
		"INNER JOIN GALADM.GAL261TBX n on c.PART_NAME = n.PART_NAME " +
		"INNER JOIN GALADM.GAL214TBX pp on c.PROCESS_POINT_ID = pp.PROCESS_POINT_ID " +
		"INNER JOIN GALADM.GAL195TBX l on pp.LINE_ID = l.LINE_ID " +
		"INNER JOIN GALADM.GAL128TBX d ON l.DIVISION_ID = d.DIVISION_ID "+
		"LEFT JOIN GALADM.GAL185TBX ip on f.PRODUCT_ID = ip.PRODUCT_ID and c.PART_NAME=ip.PART_NAME " +
		"INNER JOIN GALADM.PART_SPEC_TBX part on d.PART_ID = part.PART_ID and c.part_name = part.PART_NAME " +
		"LEFT JOIN GALADM.GAL198TBX a ON f.PRODUCT_ID=a.PRODUCT_ID and c.PART_NAME=a.PART_NAME " +
		"WHERE f.PRODUCT_ID = " ;
	
	private static final String GET_ALL_INSTALLED_PART_DETAILS_USING_PRODUCT_SPEC = 
			"select " + 
					"	ip.PRODUCT_ID," + 
					"	x.PART_NAME," + 
					"	ip.PART_NAME," + 
					"	ip.PART_SERIAL_NUMBER," + 
					"	ip.INSTALLED_PART_REASON," + 
					"	ip.INSTALLED_PART_STATUS," + 
					"	part.MEASUREMENT_COUNT," + 
					"	a.MEASUREMENT_VALUE," + 
					"	a.MEASUREMENT_SEQUENCE_NUMBER," + 
					"	pp.PROCESS_POINT_ID," + 
					"	pp.PROCESS_POINT_NAME," + 
					"	part.PART_ID," + 
					"	part.PART_SERIAL_NUMBER_MASK," + 
					"	n.WINDOW_LABEL," + 
					"	ip.ASSOCIATE_NO," + 
					"	ip.ACTUAL_TIMESTAMP," + 
					"	x.PART_CONFIRM_FLAG," + 
					"	a.MEASUREMENT_STATUS," + 
					"	x.SEQUENCE_NUMBER," + 
					"	n.REPAIR_CHECK " + 
					"from (" + 
					"    select" + 
					"        f.product_id" + 
					"        ,c.part_name" + 
					"        ,c.process_point_id" + 
					"        ,c.part_confirm_flag" + 
					"        ,c.sequence_number" + 
					"        ,row_number() over(partition by part_name order by nullif(c.int_color_code, '*'), nullif(c.ext_color_code, '*'), nullif(c.model_option_code, '*'), nullif(c.model_type_code, '*'), nullif(c.model_code, '*'), nullif(c.model_year_code, '*')) seq " + 
			"FROM @productSpec@ m " +
			"INNER JOIN @product@ f ON f.@specCodeColumn@ = m.PRODUCT_SPEC_CODE ";
			
	private static final String GET_ALL_INSTALLED_PART_DETAILS_USING_PRODUCT_SPEC2 = 
			" INNER JOIN GALADM.GAL261TBX n on x.PART_NAME = n.PART_NAME " +
			"INNER JOIN GALADM.GAL214TBX pp on x.PROCESS_POINT_ID = pp.PROCESS_POINT_ID " +
			"INNER JOIN GALADM.GAL195TBX l on pp.LINE_ID = l.LINE_ID " +
			"INNER JOIN GALADM.GAL128TBX d ON l.DIVISION_ID = d.DIVISION_ID "+
			"LEFT JOIN GALADM.gal185tbx ip on x.PRODUCT_ID = ip.PRODUCT_ID and x.PART_NAME=ip.PART_NAME " +
			"LEFT JOIN GALADM.PART_SPEC_TBX part on ip.PART_ID = part.PART_ID and x.part_name = part.PART_NAME " +
			"LEFT JOIN GALADM.gal198tbx a ON x.PRODUCT_ID=a.PRODUCT_ID and x.PART_NAME=a.PART_NAME " ;
	
	private static final String GET_BAD_MEASUREMENTS = "SELECT PP.PROCESS_POINT_NAME, C.PART_NAME, " +
		"A.MEASUREMENT_SEQUENCE_NUMBER, A.MEASUREMENT_ATTEMPT, A.MEASUREMENT_VALUE, " +
		"A.MEASUREMENT_STATUS " +
		"FROM GALADM.GAL144TBX M " +
		"INNER JOIN GALADM.GAL143TBX F ON F.PRODUCT_SPEC_CODE=M.PRODUCT_SPEC_CODE " +
		"INNER JOIN GALADM.GAL246TBX C ON " +
		"(C.EXT_COLOR_CODE=M.EXT_COLOR_CODE OR C.EXT_COLOR_CODE='*') AND " +
		"(C.INT_COLOR_CODE=M.INT_COLOR_CODE OR C.INT_COLOR_CODE='*') AND " +
		"(c.MODEL_CODE = m.MODEL_CODE or c.MODEL_CODE='*') AND " +
		"(C.MODEL_OPTION_CODE=M.MODEL_OPTION_CODE OR C.MODEL_OPTION_CODE='*') AND " +
		"(C.MODEL_TYPE_CODE=M.MODEL_TYPE_CODE OR C.MODEL_TYPE_CODE='*') AND " +
		"(c.MODEL_YEAR_CODE = m.MODEL_YEAR_CODE or c.MODEL_YEAR_CODE='*') " +
		"LEFT JOIN GALADM.GAL214TBX PP ON C.PROCESS_POINT_ID=PP.PROCESS_POINT_ID " +
		"LEFT JOIN GALADM.GAL185TBX IP ON F.PRODUCT_ID=IP.PRODUCT_ID AND C.PART_NAME=IP.PART_NAME " +
		"LEFT JOIN GALADM.HMIN_MEASUREMENT_ATTEMPTTBX A ON F.PRODUCT_ID=A.PRODUCT_ID AND C.PART_NAME=A.PART_NAME AND A.LAST_ATTEMPT=1 " +
		"LEFT JOIN GALADM.MEASUREMENT_SPEC_TBX S ON A.PART_NAME = S.PART_NAME AND IP.PART_ID = S.PART_ID AND A.MEASUREMENT_SEQUENCE_NUMBER = S.MEASUREMENT_SEQ_NUM "+
		"WHERE F.PRODUCT_ID = ?1 " +
		"AND A.MEASUREMENT_STATUS < 1 " +
		"AND (SELECT (COALESCE(D.SEQUENCE_NUMBER,0)+1)*1000000+(COALESCE(L.LINE_SEQUENCE_NUMBER,0)+1)*1000+(COALESCE(P.SEQUENCE_NUMBER,0)+1) ALL_SEQ_NUM " +
		"FROM GALADM.GAL214TBX P " +
		"LEFT JOIN GALADM.GAL195TBX L " +
		"ON P.LINE_ID=L.LINE_ID " +
		"LEFT JOIN GALADM.GAL128TBX D " +
		"ON L.DIVISION_ID=D.DIVISION_ID " +
		"LEFT JOIN GALADM.GAL117TBX S " +
		"ON D.SITE_NAME=S.SITE_NAME " +
		"WHERE P.PROCESS_POINT_ID = PP.PROCESS_POINT_ID)<=(SELECT (COALESCE(D.SEQUENCE_NUMBER,0)+1)*1000000+(COALESCE(L.LINE_SEQUENCE_NUMBER,0)+1)*1000+(COALESCE(P.SEQUENCE_NUMBER,0)+1) ALL_SEQ_NUM " +
		"FROM GALADM.GAL214TBX P " +
		"LEFT JOIN GALADM.GAL195TBX L " +
		"ON P.LINE_ID=L.LINE_ID " +
		"LEFT JOIN GALADM.GAL128TBX D " +
		"ON L.DIVISION_ID=D.DIVISION_ID " +
		"LEFT JOIN GALADM.GAL117TBX S " +
		"ON D.SITE_NAME=S.SITE_NAME " +
		"WHERE P.PROCESS_POINT_ID = ?2) " +
		"WITH CS FOR READ ONLY";
	
	private static final String GET_MISSING_PARTS = "SELECT PP.PROCESS_POINT_NAME, C.PART_NAME,C.PART_CONFIRM_FLAG " +
		"FROM GALADM.GAL144TBX M " +
		"INNER JOIN GALADM.GAL143TBX F ON F.PRODUCT_SPEC_CODE=M.PRODUCT_SPEC_CODE " +
		"INNER JOIN GALADM.GAL246TBX C ON " +
		"(C.EXT_COLOR_CODE=M.EXT_COLOR_CODE OR C.EXT_COLOR_CODE='*') AND " +
		"(C.INT_COLOR_CODE=M.INT_COLOR_CODE OR C.INT_COLOR_CODE='*') AND " +
		"(c.MODEL_CODE = m.MODEL_CODE or c.MODEL_CODE='*') AND " +
		"(C.MODEL_OPTION_CODE=M.MODEL_OPTION_CODE OR C.MODEL_OPTION_CODE='*') AND " +
		"(C.MODEL_TYPE_CODE=M.MODEL_TYPE_CODE OR C.MODEL_TYPE_CODE='*') AND " +
		"(c.MODEL_YEAR_CODE = m.MODEL_YEAR_CODE or c.MODEL_YEAR_CODE='*') " +
		"LEFT JOIN GALADM.GAL214TBX PP ON C.PROCESS_POINT_ID=PP.PROCESS_POINT_ID " +
		"LEFT JOIN GALADM.GAL195TBX L ON PP.LINE_ID=L.LINE_ID " +
		"LEFT JOIN GALADM.GAL261TBX LCP ON LCP.PART_NAME = C.PART_NAME " +
		"LEFT JOIN GALADM.GAL185TBX IP ON F.PRODUCT_ID=IP.PRODUCT_ID AND C.PART_NAME=IP.PART_NAME " +
		"WHERE F.PRODUCT_ID = ?1 " +
		"AND (IP.INSTALLED_PART_STATUS < 1 OR IP.INSTALLED_PART_STATUS IS NULL) " +
		"AND (SELECT (COALESCE(D.SEQUENCE_NUMBER,0)+1)*1000000+(COALESCE(L.LINE_SEQUENCE_NUMBER,0)+1)*1000+(COALESCE(P.SEQUENCE_NUMBER,0)+1) ALL_SEQ_NUM " +
		"FROM GALADM.GAL214TBX P " +
		"LEFT JOIN GALADM.GAL195TBX L " +
		"ON P.LINE_ID=L.LINE_ID " +
		"LEFT JOIN GALADM.GAL128TBX D " +
		"ON L.DIVISION_ID=D.DIVISION_ID " +
		"LEFT JOIN GALADM.GAL117TBX S " +
		"ON D.SITE_NAME=S.SITE_NAME " +
		"WHERE P.PROCESS_POINT_ID = PP.PROCESS_POINT_ID)<=(SELECT (COALESCE(D.SEQUENCE_NUMBER,0)+1)*1000000+(COALESCE(L.LINE_SEQUENCE_NUMBER,0)+1)*1000+(COALESCE(P.SEQUENCE_NUMBER,0)+1) ALL_SEQ_NUM " +
		"FROM GALADM.GAL214TBX P " +
		"LEFT JOIN GALADM.GAL195TBX L " +
		"ON P.LINE_ID=L.LINE_ID " +
		"LEFT JOIN GALADM.GAL128TBX D " +
		"ON L.DIVISION_ID=D.DIVISION_ID " +
		"LEFT JOIN GALADM.GAL117TBX S " +
		"ON D.SITE_NAME=S.SITE_NAME " +
		"WHERE P.PROCESS_POINT_ID = ?2)" +
		"ORDER BY COALESCE(C.PART_CONFIRM_FLAG, 0) DESC " +
		"WITH CS FOR READ ONLY";
	
	private static final String UPDATE_PRODUCT_ID = "update InstalledPart e set e.id.productId = :productId where e.id.productId = :oldProductId";		
	
	private static final String GET_NG_PART ="SELECT ip.process_point_id,"+ 
			" ip.part_name,ip.part_serial_number,pp.process_point_name,m.measurement_value,m.measurement_sequence_number "+
			" FROM GALADM.gal185tbx ip "+
			" INNER JOIN GALADM.GAL198TBX m ON ip.product_id=m.product_id AND ip.part_name=m.part_name AND ip.product_id = ?1 "+ 
			" LEFT JOIN GALADM.GAL214TBX pp ON ip.process_point_id=pp.process_point_id "+
			" WHERE ip.part_name in "+
					"(SELECT distinct(t1.part_name) FROM GALADM.GAL198TBX t1,GALADM.gal185tbx t2 "+
					"WHERE t1.product_id=t2.product_id AND t1.part_name=t2.part_name AND (t1.measurement_status=0 OR t2.installed_part_status=0) AND t2.product_id=?2 ) ";
	
	private static final String OPTIONAL_SQL = " AND pp.process_point_id IN(SELECT process_point_id FROM GALADM.gal214tbx WHERE line_id =?3 )";
	
	
    private static final String  FIND_LATEST_PRODUCT_ID = " select * " +
                                       "   from gal185tbx " +
                                       "  where process_point_id = ?1 " + 
                                       "    and create_timestamp =  (select max(a.create_timestamp) from gal185tbx a where a.process_point_id = ?1) ";
    
    private static final String UPDATE_INSTALLED_PART_FOR_PRODUCT = "update InstalledPart e set e.installedPartStatusId = :installedstatus where e.id.productId = :productId and e.id.partName in (:partNames)";
	
	private static final String SELECT_PART_SERIAL_NUMBER = 
			"select * " +
			"from gal185tbx " +
			"where product_ID = ?1 " +
			"and part_name = ?2";

    private static final String FIND_ALL_FOR_TIME_RANGE = "select e from InstalledPart e where e.id.partName = :partName and e.createTimestamp between :startTime and :endTime  "; 
        
    private static final String GET_OUTSTANDING_MCOPERATIONS = "SELECT DISTINCT A.PRODUCT_ID ,B.PROCESS_POINT_ID,D.OP_DESC "+
				" ,A.PRODUCT_SPEC_CODE    ,A.STRUCTURE_REV "+
				" ,A.DIVISION_ID    ,D.OPERATION_NAME    ,D.OP_REV	,D.OP_TYPE, C.OP_SEQ_NUM "+
				" FROM GALADM.MC_PRODUCT_STRUCTURE_TBX A "+
	" INNER JOIN GALADM.MC_STRUCTURE_TBX B ON A.PRODUCT_SPEC_CODE=B.PRODUCT_SPEC_CODE AND A.STRUCTURE_REV=B.STRUCTURE_REV AND A.DIVISION_ID=B.DIVISION_ID"+
	" INNER JOIN GALADM.MC_OP_REV_PLATFORM_TBX C ON (C.OPERATION_NAME=B.OPERATION_NAME) AND (C.OP_REV=B.OP_REV) AND (C.PDDA_PLATFORM_ID = B.PDDA_PLATFORM_ID)"+
	" INNER JOIN GALADM.MC_OP_REV_TBX D ON (D.OPERATION_NAME=C.OPERATION_NAME) AND (D.OP_REV=C.OP_REV)    "+    
	" LEFT OUTER JOIN GALADM.GAL185TBX E ON D.OPERATION_NAME=E.PART_NAME AND A.PRODUCT_ID=E.PRODUCT_ID "+
	" LEFT OUTER JOIN GALADM.MC_OP_PART_REV_TBX F ON (F.OPERATION_NAME = B.OPERATION_NAME) AND (F.PART_ID = B.PART_ID) AND (F.PART_REV = B.PART_REV) "+
	" LEFT OUTER JOIN GALADM.MC_OP_MEAS_TBX G ON (G.OPERATION_NAME = B.OPERATION_NAME) AND (G.PART_ID = B.PART_ID) AND (G.PART_REV = B.PART_REV) " +
	" LEFT OUTER JOIN GALADM.GAL198TBX H ON (H.PART_NAME=G.OPERATION_NAME)  AND (A.PRODUCT_ID = H.PRODUCT_ID) "+
	" AND (H.MEASUREMENT_SEQUENCE_NUMBER=G.OP_MEAS_SEQ_NUM)   "+
	" WHERE (A.PRODUCT_ID = ?1) "+ 
	" AND (F.PART_TYPE = 'MFG' OR F.PART_TYPE IS NULL)     "+
	" AND ((E.INSTALLED_PART_STATUS <> 1 OR E.INSTALLED_PART_STATUS IS NULL) OR (H.MEASUREMENT_STATUS <> 1 OR (H.MEASUREMENT_STATUS IS NULL AND G.OP_MEAS_SEQ_NUM IS NOT NULL))) "+
	" AND (D.OP_CHECK = 1) ";
    
    private static final String FIND_INSTALLED_PART_BY_PRODUCT_AND_PARTIAL_PART_NAME = "select * from GAL185TBX " +
    		"where product_id= ?1 and part_name like CONCAT(?2,'%')  "; 
    
	private static final String GET_INSTALLED_PARTS_FOR_PRODUCT = "WITH INSP AS (SELECT i.part_name," +
		    " i.part_id, i.installed_part_status, i.part_serial_number, (SELECT count (*)" +
		    " FROM GALADM.MEASUREMENT_SPEC_TBX mspec WHERE mspec.part_name = i.part_name" +
		    " AND mspec.part_id = i.part_id) AS required_measurement_count," +
		    " (SELECT count (*) AS num_of_measurements FROM @measTable@ meas" +
		    " WHERE meas.product_id = ?1 AND meas.part_name = i.part_name" +
		    " AND meas.measurement_status = 1 @lastAttemptCheck@) AS actual_measurement_count  FROM GALADM.GAL185TBX i" +
		    " WHERE i.product_id = ?2), SEQ AS (SELECT l.line_Id, p.process_point_id," +
		    " (coalesce (d.SEQUENCE_NUMBER, 0) + 1) * 1000000 + (coalesce (l.LINE_SEQUENCE_NUMBER, 0) + 1) * 1000" +
		    " + (coalesce (p.SEQUENCE_NUMBER, 0) + 1) sequence_num FROM GALADM.GAL214TBX p" +
		    " LEFT JOIN GALADM.GAL195tbx l ON p.LINE_ID = l.LINE_ID" +
		    " LEFT JOIN GALADM.GAL128TBX d ON l.DIVISION_ID = d.DIVISION_ID" +
		    " LEFT JOIN GALADM.GAL117TBX s ON d.SITE_NAME = s.SITE_NAME" +
		    " WHERE l.line_id NOT IN (@lineList@) and l.division_id NOT IN (@divisionList@))" +
		    " SELECT DISTINCT r.part_name, r.part_confirm_flag," +
		    " CASE WHEN r.part_name IN (SELECT DISTINCT PART_NAME FROM INSP) THEN 'INSTALLED' ELSE 'NOT_INSTALLED' END AS INSTALLED," +
		    " CASE WHEN r.part_name = (SELECT part_name FROM INSP WHERE INSP.part_name = r.part_name" +
		    " AND (INSP.ACTUAL_MEASUREMENT_COUNT = INSP.REQUIRED_MEASUREMENT_COUNT OR INSP.REQUIRED_MEASUREMENT_COUNT = 0)" +
		    " AND insp.installed_part_status = 1)" +
		    " THEN 'GOOD' WHEN r.part_name = (SELECT part_name FROM INSP WHERE INSP.part_name = r.part_name)" +
		    " THEN 'BAD' END AS status, p.product_type, p.sub_product_type," +
		    " (SELECT DISTINCT part_serial_number FROM INSP WHERE part_name = p.part_name) AS part_serial_number" +
		    " FROM GALADM.GAL246TBX r, GALADM.GAL261TBX p WHERE r.process_point_id IN (SELECT SEQ.process_point_id FROM SEQ" +
		    " WHERE SEQ.sequence_num < (SELECT SEQ.sequence_num FROM SEQ WHERE SEQ.process_point_id = ?3)" +
		    " ORDER BY SEQ.sequence_num ASC) AND r.part_name = p.part_name AND ";  
	
	 private static final String FIND_SUB_PARTS_BY_PART_SERIAL_NUMBER = " select distinct INITIAL_PART.PRODUCT_ID as PRODUCT_ID "
	 			+" , case when INITIAL_PART.FINAL_PART  is null then 'None Found'  else  INITIAL_PART.FINAL_PART end as  FINAL_PART "
	 			+" , case when INITIAL_PART.SUB_PART  is null then 'None Found' else  INITIAL_PART.SUB_PART end as  SUB_PART_1 "
	 			+" , case when temp.PART_SERIAL_NUMBER  is null then 'None Found' else  temp.PART_SERIAL_NUMBER end as SUB_PART_2 "
	 			+" from (select PART_07.PRODUCT_ID as PRODUCT_ID, PART_07.PART_SERIAL_NUMBER as FINAL_PART, PART_A1.PART_SERIAL_NUMBER as SUB_PART, PART_07.ACTUAL_TIMESTAMP as ACTUAL_TIMESTAMP "
	 			+" from (select PRODUCT_ID_LIST.PRODUCT_ID as PRODUCT_ID, PARTS.PART_SERIAL_NUMBER as PART_SERIAL_NUMBER, PARTS.ACTUAL_TIMESTAMP as ACTUAL_TIMESTAMP "
	 			+" from PRODUCT_ID_LIST left join PARTS on PRODUCT_ID_LIST.PRODUCT_ID = PARTS.PRODUCT_ID "
	 			+" where PRODUCT_ID_list.PRODUCT_ID = ?1 ) as PART_07 "
	 			+" left outer join PARTS PART_A1 on PART_07.PART_SERIAL_NUMBER =PART_A1.PRODUCT_ID ) as INITIAL_PART "
	 			+" left outer join PARTS TEMP on INITIAL_PART.SUB_PART = TEMP.PRODUCT_ID "
	 			+" order by PRODUCT_ID, FINAL_PART, SUB_PART_1 ";
	 private static final String GET_FRONT_REAR_DATA = "select b.MODEL_YEAR_CODE, b.MODEL_CODE, b.MODEL_TYPE_CODE, a.PRODUCT_ID, "
			+ "a.PRODUCTION_DATE, 'SHIP DATE' as SHIP_DATE, c.PART_SERIAL_NUMBER, c.ACTUAL_TIMESTAMP from GAL143TBX a "
			+ "join GAL144TBX b on a.PRODUCT_SPEC_CODE = b.PRODUCT_SPEC_CODE join GAL185TBX c on a.PRODUCT_ID = c.PRODUCT_ID and "
			+ "c.PART_NAME = ?1 Where date(ACTUAL_TIMESTAMP) between ?2 and ?3";	 
	 private static final String FIND_SUB_PARTS_BY_PRDUCT_ID = "with rquery (product_id, part_name, part_serial_number, part_id, PRODUCT_TYPE)"
			  +" as"
			  +" (select base.product_id, base.part_name, base.part_serial_number,base.part_id, base.PRODUCT_TYPE"
              +" from galadm.gal185tbx base join galadm.gal261tbx part on base.part_name = part.part_name"
              +" where product_id = ?1 and part.sub_product_type is not null"
              +" union all"
              +" select product_id, part_name, part_serial_number, part_id, PRODUCT_TYPE"
              +" from rquery"
              +" where part_serial_number = product_id"
              +" )"
              +" select * from rquery where product_id = ?1";
	 
	 private static final String FIND_DUPLICATE_PART_SERIAL_NUMBER = "Select count(p) from InstalledPart p "+
			 " Where p.partSerialNumber = :partSerialNumber ";
	 
	 private static final String FIND_DUPLICATE_PART_SERTIAL_NUMNER_BY_PARTNAMES = "select p from InstalledPart p where p.partSerialNumber=:partSerialNumber and p.id.partName in (:partNames) and p.installedPartStatusId <> -9";
	
	 private static final String FIND_UNIT_DONE_BY_USER_AND_PROCESS_POINT = "SELECT  count(*) from GALADM.GAL185TBX p "+
			 " WHERE p.part_name = ?4 AND p.op_rev = ?5 "+
			 " AND p.associate_No = ?2 AND p.process_point_id= ?1 "+
			 " AND CURRENT_TIMESTAMP < p.ACTUAL_TIMESTAMP+ ?3 days ";
	 private static final String FIND_UNIT_DONE_BY_USER_AND_PROCESS_POINT_AND_PART = FIND_UNIT_DONE_BY_USER_AND_PROCESS_POINT +
			 " AND p.part_rev = ?6 AND p.part_id = ?7 ";

	 private static final String FIND_ALL_BY_COMMON_NAME = "SELECT a.* FROM galadm.GAL185TBX a " + 
			 " join galadm.MC_OP_REV_TBX b on a.PART_NAME=b.OPERATION_NAME and a.OP_REV=b.OP_REV " +
			 " where a.PRODUCT_ID=?1 and b.COMMON_NAME=?2 " +
			 " and COALESCE(a.PART_SERIAL_NUMBER,'') <> '' " +
			 " order by a.ACTUAL_TIMESTAMP desc";
	 
	 private static final String FIND_ALL_BY_COMMON_NAME_LIST = "SELECT a.* FROM galadm.GAL185TBX a " + 
			 " join galadm.MC_OP_REV_TBX b on a.PART_NAME=b.OPERATION_NAME and a.OP_REV=b.OP_REV " +
			 " where  a.INSTALLED_PART_STATUS <> -9 and a.PRODUCT_ID=?1 and b.COMMON_NAME in (@commonNameList@) ";
	 
	 private static final String FIND_ALL_PART_NAME_BY_COMMON_NAME = "SELECT a.* FROM galadm.GAL185TBX a " + 
			 " join galadm.MC_OP_REV_TBX b on a.PART_NAME=b.OPERATION_NAME and a.OP_REV=b.OP_REV " +
			 " where  a.INSTALLED_PART_STATUS <> -9 and a.PRODUCT_ID=?1 and b.COMMON_NAME=?2  ";
	 private static final String FIND_INSTALLED_PART_BY_UNIT_OR_COMMON_NAME = "select ip.* from gal185tbx ip left outer join mc_op_rev_tbx op "+
			 " on ip.part_name=op.operation_name and ip.op_rev=op.op_rev "+
			 " where ip.product_id = ?1 and ( UPPER(op.common_name)=UPPER(?2) "+
			 " or op.OPERATION_NAME like (?2) ) ";
	 private static final String FIND_BY_PART_NAME = "SELECT a.* FROM galadm.GAL185TBX a "+
	         " where a.PART_NAME =?1 and a.PRODUCT_ID =?2" ;
	 
	 private static final String FIND_DEFECT_REF_ID = 
		"SELECT A.DEFECT_REF_ID FROM GALADM.GAL185TBX A WHERE A.PRODUCT_ID = ?1 AND A.PART_NAME = ?2 " +
		"AND A.DEFECT_REF_ID IS NOT NULL AND A.DEFECT_REF_ID != 0 UNION " +
		"SELECT B.DEFECT_REF_ID FROM GALADM.GAL198TBX B WHERE B.PRODUCT_ID = ?1 AND B.PART_NAME = ?2 " + 
		"AND B.DEFECT_REF_ID IS NOT NULL AND B.DEFECT_REF_ID != 0";
	 
	 private static final String FIND_MBPN_PARENT_INSTALLED_PART =
		"select i.* from galadm.gal185tbx i " +
		"inner join galadm.gal261tbx p on i.PART_NAME = p.PART_NAME "+
		"where i.PART_SERIAL_NUMBER=?1 and p.SUB_PRODUCT_TYPE=?2 ";
	 
	 
	@Autowired
    private MeasurementDao measurementDao;
    
    @Autowired
    private InstalledPartHistoryDao installedPartHistoryDao;
   
	public List<InstalledPart> findAllByPartNameAndSerialNumber(String partName,
			String partNumber) {
		Parameters parameters = Parameters.with("id.partName", partName);
		parameters.put("partSerialNumber", partNumber);
		return findAll(parameters);
	}
	
	public List<InstalledPart> findAllByProductId(String productId){
		Parameters parameters = Parameters.with("id.productId", productId);
		return findAll(parameters);
		
	}
	
	public List<InstalledPart> findAllByPartSerialNumber(String serialNumber){
		Parameters parameters = Parameters.with("partSerialNumber", serialNumber);
		return findAll(parameters);
		
	}
	
	public List<InstalledPart> findAllByProductIdAndProcessPoint(String productId, String processPoint) {
		Parameters parameters = Parameters.with("id.productId", productId);
		parameters.put("processPointId", processPoint);
		return findAll(parameters);
	}
	
	public List<InstalledPart> findAllValidParts(String productId, List<String> partNames) {
		Parameters parameters = Parameters.with("productId", productId);
		parameters.put("partNames", partNames);
		return findAllByQuery(FIND_ALL_VALID_PARTS, parameters);
	}
	
	@Override
	public List<InstalledPart> findAllPartsWithDefect(String productId) {
		Parameters parameters = Parameters.with("productId", productId);
		return findAllByQuery(FIND_ALL_WITH_DEFECTS, parameters);
	}
	
	@Override
	public InstalledPart findByRefId(Long refId) {
		Parameters parameters = Parameters.with("defectRefId", refId);
		return findFirst(parameters);
	}
	
	public List<InstalledPart> findAllByProductIdAndPartNames(String productId, List<String> partNames) {
		Parameters parameters = Parameters.with("productId", productId);
		parameters.put("partNames", partNames);
		return findAllByQuery(FIND_INSTALLED_PART_FOR_PRODUCT, parameters);
	}
	
	public List<InstalledPart> findAllEngineByEngineProductIdAndPartNames(String productType, String engineProductId, List<String> partNames) {
		Parameters parameters = Parameters.with("productType", productType);
		parameters.put("engineProductId", engineProductId);
		parameters.put("partNames", partNames);
		return findAllByQuery(FIND_ENGINE_INSTALLED_PART_FOR_PRODUCTS_BY_TYPE, parameters);
	}
	
	public List<InstalledPart> findAllByProductIdAndPartSerialNo(String productId, String partNumber) {
		Parameters parameters = Parameters.with("id.productId", productId);
		parameters.put("partSerialNumber", partNumber);
		return findAll(parameters);
	}

	public boolean isProcessed(String productId, List<String> partNames) {
        List<InstalledPart> processedProductList = findAllByProductIdAndPartNames(productId, partNames);
        return processedProductList.size() > 0;
	}
	
	@Transactional 
	public int deleteProdIds(List <String> prodIds)  
	{
        int count = 0;
		for( String prodId : prodIds )
		{
			count = count + delete(Parameters.with("id.productId", prodId));
		}
        return count;
	}	
	@Transactional
	public void repairHeadless(String productId, String partName, String associateNo, String updateReason) {
		InstalledPart part = findByKey(new InstalledPartId(productId, partName));
		if(part == null){
			Logger.getLogger().warn("Failed to find installed part:", productId, partName);
			part = new InstalledPart();
			part.setId(new InstalledPartId(productId, partName));
			part.setAssociateNo(associateNo);
			part.setInstalledPartReason("");
			
		} 
		
		part.setInstalledPartReason(updateReason);
		part.setAssociateNo(associateNo);
		part.setInstalledPartStatus(InstalledPartStatus.OK);
		part.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		
		save(part);
		
		//this section previously set the measurement status to OK
		//when installed part status was collected as Good, but the
		//measurements were NG (or not sent, as with click wrench).
		//This is now done as part of the collector work flow (NALC-58)
					
		}
		
	public InstalledPart findById(String productId, String partName) {
		return findByKey(new InstalledPartId(productId,partName));
	}
	
	public List<InstalledPartDetail> getBadMeasurements(String productId, String processPointId)
	{
		Parameters parameters = Parameters.with("1", productId.trim());
		parameters.put("2", processPointId.trim());
		List<Object[]> badMeasurementsObjects = findAllByNativeQuery(GET_BAD_MEASUREMENTS, parameters, Object[].class);
		
		List<InstalledPartDetail> badMeasurements = new ArrayList<InstalledPartDetail>();
    	try {

    		for(Object[] result: badMeasurementsObjects) {
    			InstalledPartDetail detail = new InstalledPartDetail();
    			detail.setProcessPointName(getString(result[0]));
    			detail.setRulePartName(getString(result[1]));
    			detail.setMeasurrementSequenceNumber(getInt(result[2]));
    			detail.setMeasurementAttempt(getInt(result[3]));
    			detail.setMeasurementValue(result[4]==null?null:BigDecimal.valueOf(((Double)result[4]).doubleValue()));
    			detail.setMeasurementStatus(getInt(result[5]));
    			badMeasurements.add(detail);
    		}	
    	} catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	
    	return badMeasurements;
    	
	}
	
	public List<InstalledPartDetail> getMissingPartDetails(String productId, String processPointId)
	{
		Parameters parameters = Parameters.with("1", productId.trim());
		parameters.put("2", processPointId.trim());
		List<Object[]> missingPartsObjects = findAllByNativeQuery(GET_MISSING_PARTS, parameters, Object[].class);
		
		List<InstalledPartDetail> missingParts = new ArrayList<InstalledPartDetail>();
    	try {

    		for(Object[] result: missingPartsObjects) {
    			InstalledPartDetail detail = new InstalledPartDetail();
    			detail.setProcessPointName(getString(result[0]));
    			detail.setRulePartName(getString(result[1]));
    			detail.setPartConfirmCheck(getInt(result[2]));
    			missingParts.add(detail);
    		}	
    	} catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	
    	return missingParts;
    	
	}

	public List<InstalledPartDetail> getAllInstalledPartDetails(String productId, String productType, String processPointId, boolean partConfirmCheck,
			boolean useProcessPoint, boolean limitRulesByDivision, boolean enableRepairCheck) {

		String sql = GET_ALL_INSTALLED_PART_DETAILS_USING_PRODUCT_SPEC;

		if (ProductTypeUtil.isMbpnProduct(productType)) {
			sql +="INNER JOIN GALADM.GAL246TBX c ON " +
					"m.PRODUCT_SPEC_CODE like CONCAT(trim(c.PRODUCT_SPEC_CODE), '%')";
		}else {
			sql += " INNER JOIN GALADM.GAL246TBX c ON " +
					"(c.MODEL_CODE = m.MODEL_CODE or c.MODEL_CODE='*') AND " +
					"(c.MODEL_OPTION_CODE = m.MODEL_OPTION_CODE or c.MODEL_OPTION_CODE='*') AND " +
					"(c.MODEL_TYPE_CODE = m.MODEL_TYPE_CODE or c.MODEL_TYPE_CODE='*') AND " +
					"(c.MODEL_YEAR_CODE = m.MODEL_YEAR_CODE or c.MODEL_YEAR_CODE='*') ";
			if(!productType.equalsIgnoreCase(ProductType.ENGINE.name())) {
				sql +=	"AND (c.EXT_COLOR_CODE = m.EXT_COLOR_CODE or c.EXT_COLOR_CODE='*') AND " +
						"(c.INT_COLOR_CODE = m.INT_COLOR_CODE or c.INT_COLOR_CODE='*') ";
			}
		}

		if (limitRulesByDivision) {
			sql += " INNER JOIN GALADM.GAL214TBX pp on c.PROCESS_POINT_ID = pp.PROCESS_POINT_ID " + 
		    	   " INNER JOIN GALADM.GAL195TBX l on pp.LINE_ID = l.LINE_ID " + 
		    	   " INNER JOIN GALADM.GAL128TBX d ON l.DIVISION_ID = d.DIVISION_ID " + 
		    	   " AND EXISTS (SELECT * FROM GAL215TBX X " + 
		    	   " INNER JOIN GALADM.GAL214TBX Z ON X.PROCESS_POINT_ID = Z.PROCESS_POINT_ID " + 
		    	   " WHERE Z.PROCESS_POINT_TYPE = 1 AND D.DIVISION_ID = Z.DIVISION_ID AND X.PRODUCT_ID = f.product_id) ";  
		}

		sql += " WHERE f.PRODUCT_ID = ?1" + 
		" order by part_name, seq) x "; 

		sql += GET_ALL_INSTALLED_PART_DETAILS_USING_PRODUCT_SPEC2;

		sql += "WHERE x.seq = 1 ";

		if(partConfirmCheck && enableRepairCheck ) 
		{
			sql += " AND (x.PART_CONFIRM_FLAG=1 OR n.REPAIR_CHECK=1) ";
		}else if ((!partConfirmCheck && enableRepairCheck ) )
		{
			sql += " AND n.REPAIR_CHECK=1 ";
		}else if (partConfirmCheck && !enableRepairCheck) 
		{
			sql += " AND x.PART_CONFIRM_FLAG=1 ";
		}
		if (!ProductTypeUtil.isMbpnProduct(productType)) {
			sql += " AND n.PRODUCT_TYPE = '" + productType + "'";
		}

		if(StringUtils.isNotEmpty(processPointId)) {
			String processPointIdStr = " AND (((coalesce(d.SEQUENCE_NUMBER,0)+1)*1000000+(coalesce(l.LINE_SEQUENCE_NUMBER,0)+1)*1000+(coalesce(pp.SEQUENCE_NUMBER,0)+1)) <= "+
					"(select (coalesce(dd.SEQUENCE_NUMBER,0)+1)*1000000+(coalesce(ll.LINE_SEQUENCE_NUMBER,0)+1)*1000+(coalesce(p.SEQUENCE_NUMBER,0)+1) ALL_SEQ_NUM "+
					"from galadm.gal214tbx p left join galadm.gal195tbx ll on p.LINE_ID=ll.LINE_ID left JOIN GALADM.GAL128TBX dd ON ll.DIVISION_ID=dd.DIVISION_ID "+
					"left JOIN GALADM.GAL117TBX S ON dd.SITE_NAME=S.SITE_NAME WHERE P.PROCESS_POINT_ID='" + processPointId + "')) ";
			String processPointIdListStr = " AND pp.PROCESS_POINT_ID IN (" + StringUtil.toSqlInString(processPointId) + ")";
			sql += useProcessPoint ? processPointIdListStr : processPointIdStr;
		}
		sql += " ORDER BY ((coalesce(d.SEQUENCE_NUMBER,0)+1)*1000000+(coalesce(l.LINE_SEQUENCE_NUMBER,0)+1)*1000+(coalesce(pp.SEQUENCE_NUMBER,0)+1)), x.SEQUENCE_NUMBER, x.PART_NAME, a.MEASUREMENT_SEQUENCE_NUMBER" ;

		StringBuilder strQ = new StringBuilder(sql
				.replace("@product@", CommonUtil.getTableName(ProductTypeUtil.getTypeUtil(productType).getProductClass()))
				.replace("@productSpec@", getProductSpecTableName(productType))
				.replace("@specCodeColumn@",  ProductTypeUtil.isMbpnProduct(productType) ? "CURRENT_PRODUCT_SPEC_CODE" : "PRODUCT_SPEC_CODE")
				.replace("@history@",  CommonUtil.getTableName(ProductTypeUtil.getTypeUtil(productType).getProductHistoryClass())));

		Parameters parameters = Parameters.with("1", productId.trim());

		List<Object[]> tempList=findAllByNativeQuery(strQ.toString(), parameters, Object[].class);
		List<InstalledPartDetail> installedPartDetailList=new ArrayList<InstalledPartDetail>();

		for(Object[] array :tempList) {
			InstalledPartDetail installedPartDetail = null;		       			
			installedPartDetail = new InstalledPartDetail();

			installedPartDetail.setProductId(array[0]==null?null:(String)array[0]);
			installedPartDetail.setRulePartName(array[1]==null?null:(String)array[1]);
			installedPartDetail.setPartName(array[2]==null?null:(String)array[2]);
			installedPartDetail.setPartSerialNumber(array[3]==null?null:(String)array[3]);
			installedPartDetail.setInstalledPartReason(array[4]==null?null:(String)array[4]);
			installedPartDetail.setInstalledPartStatus(array[5]==null?null:(Integer)array[5]);
			installedPartDetail.setMeasurementCount(array[6]==null?null:(Integer)array[6]);
			if(array[7] instanceof java.lang.Double)
				installedPartDetail.setMeasurementValue(array[7]==null?null:BigDecimal.valueOf(((Double)array[7]).doubleValue()));
			else
				installedPartDetail.setMeasurementValue(array[7]==null?null:(BigDecimal)array[7]);
			installedPartDetail.setMeasurrementSequenceNumber(array[8]==null?null:(Integer)array[8]);
			installedPartDetail.setProcessPointId(array[9]==null?null:(String)array[9]);
			installedPartDetail.setProcessPointName(array[10]==null?null:(String)array[10]);
			installedPartDetail.setPartId(array[11]==null?null:(String)array[11]);
			installedPartDetail.setPartSerialMask(array[12]==null?null:(String)array[12]);
			installedPartDetail.setWindowLabel(array[13]==null?null:(String)array[13]);
			installedPartDetail.setAssociate(array[14]==null?null:(String)array[14]);
			installedPartDetail.setActualTimestamp(array[15]==null?null:(Timestamp)array[15]);
			installedPartDetail.setPartConfirmCheck(array[16]==null?0:(Integer)array[16]);
			installedPartDetail.setMeasurementStatus(array[17]==null?null:(Integer)array[17]);
			installedPartDetail.setRepairCheck(array[19]==null?0:(Integer)array[19]);
			installedPartDetailList.add(installedPartDetail);			
		}
		return installedPartDetailList;
	}
	
	public InstalledPart findLatestInstalledByProcessPoint(String processPointId)  {
		Parameters params = Parameters.with("1", processPointId);
		InstalledPart installedPart  = findFirstByNativeQuery(FIND_LATEST_PRODUCT_ID, params);
		return installedPart;
	}
	
	@Transactional 
	public int updateProductId(String productId, String oldProductId) {
		//TODO review and update history (185, 198)
		Parameters params = Parameters.with("productId", productId);
		params.put("oldProductId", oldProductId);
		return executeUpdate(UPDATE_PRODUCT_ID, params);
	}

	@Transactional 
	public int moveAllData(String newProductId, String currentProductId) {
		measurementDao.moveAllData(newProductId, currentProductId);
		installedPartHistoryDao.moveAllData(newProductId, currentProductId);
		Parameters params = Parameters.with("productId", newProductId);
		params.put("oldProductId", currentProductId);
		return executeUpdate(UPDATE_PRODUCT_ID, params);
	}

	public List<InstalledPartDetail> getNGParts(String productId, String lineId) {
		Parameters parameters = Parameters.with("1", productId.trim());
		parameters.put("2",productId.trim());
		String sql = GET_NG_PART;
		if(lineId !=null && !"".equals(lineId.trim()) && !"0".equals(lineId)){
			sql += OPTIONAL_SQL;
			parameters.put("3", lineId.trim());
		}
		
		List<Object[]> ngMeasurementsObjects = findAllByNativeQuery(sql, parameters, Object[].class);
		
		List<InstalledPartDetail> ngMeasurements = new ArrayList<InstalledPartDetail>();
    	try {

    		for(Object[] result: ngMeasurementsObjects) {
    			InstalledPartDetail detail = new InstalledPartDetail();
    			detail.setProcessPointId(getString(result[0]));
    			detail.setPartName(getString(result[1]));
    			detail.setPartSerialNumber(getString(result[2]));
    			detail.setProcessPointName(getString(result[3]));
    			detail.setMeasurementValue(result[4]==null?null:BigDecimal.valueOf(((Double)result[4]).doubleValue()));
    			detail.setMeasurrementSequenceNumber(getInt(result[5]));
    			ngMeasurements.add(detail);
    		}	
    	} catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	
    	return ngMeasurements;
	}
	
	//updates installed part status to NG
	@Transactional
	public int updateInstalledPartStatus(String productId, List<String> partNames, int installedPartStatus) {
		if(StringUtils.isNotEmpty(productId) && partNames != null && !partNames.isEmpty()){
			Parameters parameters = Parameters.with("productId", productId);
			parameters.put("installedstatus", installedPartStatus);
	        parameters.put("partNames", partNames);
	        return executeUpdate(UPDATE_INSTALLED_PART_FOR_PRODUCT, parameters);
		}
		else{
			return 0;
		}
		
	}
	
	public String getPartSerialNumber(String productID, String partName){
		Parameters params = Parameters.with("1", productID);
		params.put("2", partName);
		InstalledPart installedPart  = findFirstByNativeQuery(SELECT_PART_SERIAL_NUMBER, params);
		String partSerialNumber = "";
		if (installedPart != null) {
			partSerialNumber = installedPart.getPartSerialNumber();
			if (partSerialNumber == null) {
				partSerialNumber = "";
			}
		}
		return partSerialNumber;
	}
	
	public String getLatestPartSerialNumber(String productId, String partName){
		Parameters params = Parameters.with("id.productId", productId);
		params.put("id.partName", partName);
		InstalledPart installedPart = findFirst(params,new String[]{"actualTimestamp"},false);
		String partSerialNumber = "";
		if (installedPart != null) {
			partSerialNumber = installedPart.getPartSerialNumber();
			if (partSerialNumber == null) {
				partSerialNumber = "";
			}
		}
		return partSerialNumber;
	}
	
    @Transactional
    public List<InstalledPart> saveAll(List<InstalledPart> installedParts) {
    	return saveAll(installedParts, true);
    }
    
	@Transactional
	public List<InstalledPart> saveAll(List<InstalledPart> installedParts, boolean isSaveHistory) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	   	List<InstalledPart> results = new ArrayList<InstalledPart>();
	   	for(InstalledPart part: installedParts) {
   		    if (part.getActualTimestamp() == null || part.getActualTimestamp().after(timestamp)) {
   		    	part.setActualTimestamp(timestamp);
   		    }
	   		results.add(save(part));
	   		if(isSaveHistory){
	   			installedPartHistoryDao.savePartHistory(part);
	   		}
	   		if(part.getMeasurements() != null && !part.getMeasurements().isEmpty()) {
	   			measurementDao.saveAll(part.getMeasurements(), isSaveHistory);
	   		}
	   	}
	   	return results;
	}

	@Transactional
	public List<InstalledPart> findAllForDateRange(String partName, Timestamp startTimestamp, Timestamp endTimestamp) {
		Parameters params = Parameters.with("partName", partName);
		params.put("startTime", startTimestamp);
		params.put("endTime", endTimestamp);
		return findAllByQuery(FIND_ALL_FOR_TIME_RANGE, params);
	}
	
	public List<MCInstalledPartDetailDto> getInstalledPartDetails(String sql, String productId) {
		Parameters params = Parameters.with("1", productId);
		return findAllByNativeQuery(sql, params, MCInstalledPartDetailDto.class);	
	}
		
	public InstalledPart findByProductIdAndPartialName(String productId,String partialPartName){
		Parameters params = Parameters.with("1", productId);
		params.put("2", partialPartName);
		return findFirstByNativeQuery(FIND_INSTALLED_PART_BY_PRODUCT_AND_PARTIAL_PART_NAME, params);
	}
	
	public List<InstalledPartDetailsDto> getInstalledPartDetails(String processPointId, String productId, ProductType productType, String productSpecCode) {
		
		// retrieve list of divisions to exclude
		List<String> excludeDivisions = new ArrayList<String>(Arrays.asList(getProductChkPropertyBean(processPointId).getInstalledPartsCheckExcludeDivisions()));
		
		// retrieve list of lines to exclude
		List<String> excludeLines = new ArrayList<String>(Arrays.asList(getProductChkPropertyBean(processPointId).getInstalledPartsCheckExcludeLines()));
		
		String specCodeMatchWhereClause = ProductTypeUtil.getProductSpecDao(productType).getSpecCodeMatchSql(productSpecCode);
		StringBuilder strQ = new StringBuilder(GET_INSTALLED_PARTS_FOR_PRODUCT
				.replace("@divisionList@", StringUtil.toSqlInString(excludeDivisions))
				.replace("@lineList@", StringUtil.toSqlInString(excludeLines))
				.replace("@measTable@", getMeasurementTable(processPointId))
				.replace("@lastAttemptCheck@", getLastMeasurementAttemptCheckSql(processPointId)));
		
		return findAllByNativeQuery(strQ.toString()	+ specCodeMatchWhereClause + " WITH CS FOR READ ONLY",
				Parameters.with("1", productId.trim())
				.put("2", productId.trim())
				.put("3", processPointId.trim()),
				InstalledPartDetailsDto.class);
	}
	
	private ProductCheckPropertyBean getProductChkPropertyBean(String processPointId) {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPointId);
	}
	
	private String getMeasurementTable(String processPointId) {
		String measTableName = null;
		try {
			measTableName = StringUtils.trimToNull(getProductChkPropertyBean(processPointId).getMeasurementTable());
		} catch (Exception ex) {
			Logger.getLogger().warn(StringUtils.trimToEmpty(ex.getMessage()));
		}
		
		if (measTableName == null) {
			return CommonUtil.getTableName(Measurement.class);
		}
		return measTableName;
	}
	
	private String getLastMeasurementAttemptCheckSql(String processPointId) {
		if (getProductChkPropertyBean(processPointId).isCheckLastMeasurementAttempt()) {
			return " AND meas.last_attempt = 1";
		}
		return "";
	}
	
	public List<Object[]> findSubParts(String serialNumber, String productIdPrefix){
		Parameters params = Parameters.with("1", serialNumber);
		StringBuffer sql = new StringBuffer();
		sql.append("with PRODUCT_ID_LIST as (select distinct PRODUCT_ID from GALADM.GAL216TBX ");
		if(StringUtils.isNotEmpty(productIdPrefix)){
			sql.append(" where ");
			String[] tmpPrefix = productIdPrefix.split(COMMA);
			String subQuery = "";
			int i = 0;
			for(String tmp : tmpPrefix){
				subQuery+= " PRODUCT_ID like '"+tmp+"%'";
				if(i++ != tmpPrefix.length-1) subQuery += " OR ";

			}
			sql.append(subQuery);
			sql.append(" ) ");
			//Add production_lot
			sql.append(" , PARTS as  (select PRODUCT_ID, PART_SERIAL_NUMBER, ACTUAL_TIMESTAMP from GALADM.GAL185TBX INSTALLED_SN ");
			sql.append(" where ");
			sql.append(StringUtils.replace(subQuery, "PRODUCT_ID", "PART_SERIAL_NUMBER"));
		
		}else{
			sql.append(" ) ");
			sql.append(" , PARTS as  (select PRODUCT_ID, PART_SERIAL_NUMBER, ACTUAL_TIMESTAMP from GALADM.GAL185TBX INSTALLED_SN ");
		}
		
		sql.append(" ) ");
		sql.append(FIND_SUB_PARTS_BY_PART_SERIAL_NUMBER);
		
		return findAllByNativeQuery(sql.toString(), params,Object[].class);
	}
	
	public List<SubAssemblyPartListDto> findSubPartsByProductId(String productId) {
		Parameters params = Parameters.with("1", productId);
		return findAllByNativeQuery(FIND_SUB_PARTS_BY_PRDUCT_ID, params, SubAssemblyPartListDto.class);	
	}
	
	/**
	 * returns the first InstalledPart found in the table
	 */
	public Boolean isPartSerialNumberExists(String partSerialNumber)
	{
		return count(FIND_DUPLICATE_PART_SERIAL_NUMBER,Parameters.with("partSerialNumber", partSerialNumber))>0;
	}
	
	@Transactional
	public void deleteInstalledParts(String productId, List<String> partNames) {
		Parameters params = Parameters.with("id.productId", productId).put("id.partName", partNames);
		delete(params);
		measurementDao.deleteAllByProductIdAndPartNames(productId, partNames);
		
	}
	@Override
	public List<Object[]> findByPartName(String partName,Timestamp startTs, Timestamp endTs){
		Parameters params = Parameters.with("1", partName);
		params.put("2", startTs);
		params.put("3", endTs);
		return findAllByNativeQuery(GET_FRONT_REAR_DATA, params, Object[].class);
	}
	@Override
	public List<InstalledPart> findAllByPartNameAndSerialNumber(List<String> partNames, String partSerialNumber) {
		Parameters params = Parameters.with("partNames", partNames);
		params.put("partSerialNumber", partSerialNumber);
		return findAllByQuery(FIND_DUPLICATE_PART_SERTIAL_NUMNER_BY_PARTNAMES,params);
	}
	@Override
	public Map<String, Boolean> findSpecialUnitsInProcessPoint(String processPoint,String loggedUser,Integer noOfDays, List<MCOperationRevision> operationsInProcessPoint) {
		Map<String,Boolean> specialOperations = new HashMap<String,Boolean>();
		
		
		for(MCOperationRevision operation : operationsInProcessPoint){
			
			String query = FIND_UNIT_DONE_BY_USER_AND_PROCESS_POINT ;
			
			Parameters params = Parameters.with("1", processPoint);
			params.put("2",loggedUser);
			params.put("3",noOfDays);
			params.put("4",operation.getId().getOperationName());
			params.put("5",operation.getId().getOperationRevision());
			if(operation.getSelectedPart() != null){
				params.put("6",operation.getSelectedPart().getId().getPartRevision());
				params.put("7",operation.getSelectedPart().getId().getPartId());
				query = FIND_UNIT_DONE_BY_USER_AND_PROCESS_POINT_AND_PART;
			}
				
			
			if(findFirstByNativeQuery(query, params,Integer.class) <=0) 
				specialOperations.put(operation.getId().getOperationName(),true);
			else
				specialOperations.put(operation.getId().getOperationName(),false);
		}
		
		return specialOperations;
	}
	
	private String getProductSpecTableName(String productType) {
			if(productType.equalsIgnoreCase(ProductType.FRAME.name()))
				return CommonUtil.getTableName((Class<? extends IEntity>) FrameSpec.class);
			else if(productType.equalsIgnoreCase(ProductType.ENGINE.name()))
				return CommonUtil.getTableName((Class<? extends IEntity>) EngineSpec.class);
			else if(ProductTypeUtil.isMbpnProduct(productType)) {
				return CommonUtil.getTableName((Class<? extends IEntity>) Mbpn.class);
			}
			return null;
	}
	@Override
	public List<InstalledPart> findAllInstalledPartByCommonName(String productId, String commonName) {
		Parameters params = Parameters.with("1", productId)
								.put("2", commonName);
		return findAllByNativeQuery(FIND_ALL_BY_COMMON_NAME, params);
	}
	
	@Override
	public List<InstalledPart> findAllInstalledPartByCommonNameList(String productId, List<String> commonNameList, String productType) {
		Parameters params = Parameters.with("1", productId);
		StringBuilder finalQuery = new StringBuilder(StringUtils.replace(FIND_ALL_BY_COMMON_NAME_LIST, 
				"@commonNameList@", StringUtil.toSqlInString(commonNameList)));
		if(StringUtils.isNotEmpty(productType)) {
			params.put("2", productType);
			finalQuery.append(" and a.PRODUCT_TYPE=?2 ");
		}
		finalQuery.append(" order by a.ACTUAL_TIMESTAMP desc");
		return findAllByNativeQuery(finalQuery.toString(), params);
	}
	
	@Override
	public String findOneInstalledPartByCommonName(String productId, String commonName){
		Parameters params = Parameters.with("1", productId)
				.put("2", commonName);
		List<InstalledPart> partNames = findAllByNativeQuery(FIND_ALL_PART_NAME_BY_COMMON_NAME, params);
		if(partNames.isEmpty()) {
			return commonName;
		}
	    String partName = partNames.get(0).getPartName().trim();
	    return partName;
	}
	
	@Override
	public InstalledPart findByUnitOrCommonName(String productId, String partName) {
		Parameters params = Parameters.with("1", productId);
		params.put("2",partName);
		return findFirstByNativeQuery(FIND_INSTALLED_PART_BY_UNIT_OR_COMMON_NAME, params);
	}
	
	@Override
	public List<InstalledPart> findLinkedParts(String childPartName, String productId) {
		Parameters params = Parameters.with("1", childPartName);
		params.put("2", productId);
		return findAllByNativeQuery(FIND_BY_PART_NAME,params, InstalledPart.class);
	}
	
	@Override
	public List<Long> findDefectRefIds(List<String> productIdList, List<String> partNameList) {
		List<Long> defectRefIdList = new ArrayList<Long>();
		for (int i = 0; i < productIdList.size(); i++) {
			Parameters params = Parameters.with("1", productIdList.get(i));
			params.put("2", partNameList.get(i));
			defectRefIdList.addAll(findAllByNativeQuery(FIND_DEFECT_REF_ID, params, Long.class));
		}
		return defectRefIdList;
	}
	@Override
	public List<InstalledPart> findMbpnParentInstalledPart(String partSerialNumber, String subProductType) {
		Parameters params = Parameters.with("1", partSerialNumber);
		params.put("2", subProductType);
		return findAllByNativeQuery(FIND_MBPN_PARENT_INSTALLED_PART, params);
	}
}