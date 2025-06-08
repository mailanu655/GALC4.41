package com.honda.galc.dao.jpa.product;


import static com.honda.galc.common.logging.Logger.getLogger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.dto.ProductHistoryDto;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductResultId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

/** * * 
 * * <Table border="1" sim="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>YX</TD>
 * <TD>Aug 07, 2014</TD>
 * <TD>0.1</TD>
 * <TD>TASK0013687</TD>
 * <TD>Add method 'getPeriodPassingCountByPP' </TD> 
 * </TR> 
 *
 * </TABLE>
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012
 */
public class ProductResultDaoImpl extends ProductHistoryDaoImpl<ProductResult,ProductResultId> implements ProductResultDao{

    private static final String PRODUCT_PROCESSED = "select count(1) from galadm.gal215tbx a where a.product_Id = ?1 and a.PROCESS_POINT_ID = ?2 and a.ACTUAL_TIMESTAMP <= ?3";
    private static final String FIND_PRODUCT_RESULTS = "select * from galadm.gal215tbx a where a.product_Id = ?1 and a.PROCESS_POINT_ID = ?2 for read only";
    private static final String FIND_PRODUCT_HISTORY = "select a.product_id, a.process_point_id, a.actual_timestamp, b.process_point_name from galadm.gal215tbx a left join gal214tbx b on a.process_point_id = b.process_point_id where a.product_Id = ?1 order by a.actual_timestamp desc WITH CS FOR READ ONLY";
    private static final String TOTAL_PRODUCT_PROCESSED = "select count(distinct product_id) from galadm.gal215tbx where production_lot = ?1 and process_point_id = ?2";
	private static final String IS_PROCESSING_MOST_RECENT = "select ph.process_point_id, ph.actual_timestamp from gal215tbx ph, gal214tbx pp, gal143tbx prod" +
																" where prod.PRODUCT_ID = ?1" +
																" and ph.product_id = prod.product_id" +
																" and ph.process_point_id = pp.process_point_id" +
																" and prod.last_passing_process_point_id = ph.PROCESS_POINT_ID" + 
																" and pp.tracking_point_flag = 1 " +
																" order by ph.actual_timestamp desc fetch first row only with cs for read only";

	private static final String MAX_TIMESTAMP_PROD_LOT = "SELECT max(result.ACTUAL_TIMESTAMP) AS max_time_stamp "
																+ "FROM gal215tbx result, gal217tbx lot "
																+ "WHERE result.process_point_id = ?1 AND result.Production_lot = lot.production_lot "
																+ "AND lot.DEMAND_TYPE = 'MP' AND lot.Process_Location = ?2 FOR READ ONLY";
	private static final String FIND_LOT_RESULT_PROD_LOT = "SELECT lot.production_lot, lot.kd_lot_number, lot.Process_Location, "
																+ "lot.Lot_Size, pp.Process_Point_ID, pp.Process_Point_Description "
																+ "FROM GAL215TBX result, gal217tbx lot, gal214tbx pp "
																+ "WHERE result.process_point_id = pp.process_point_id  "
																+ "AND lot.production_lot = result.production_lot "
																+ "AND lot.Plant_Code = ?1 AND lot.Line_No = ?2 AND lot.Process_Location = ?3 "
																+ "AND pp.Process_Point_ID = ?4 AND result.actual_timestamp = ?5 FOR READ ONLY";
	//TASK0013687
	private static final String PERIOD_PASSING_COUNT_BY_PP = "SELECT totals.model_year_code, totals.model_code, totals.model_type_code, sum (totals.count) AS prod_minus_scrap" 
																+ " FROM  (SELECT prod.model_year_code, prod.model_code, prod.model_type_code, count (prod.product_id) AS count"
               													+ "				FROM  (SELECT distinct framemtc.model_year_code, framemtc.model_code, framemtc.model_type_code, frame.product_id"
                         										+ "							FROM gal143tbx frame JOIN gal144tbx framemtc ON frame.product_spec_code = framemtc.product_spec_code"
                                              					+ "													JOIN gal215tbx result   ON result.product_id = frame.product_id"
                          										+ " 						WHERE  result.process_point_id = ?1"
                           										+ " 								AND result.actual_timestamp BETWEEN ?2 AND ?3) prod"
                            									+ "			GROUP BY prod.model_year_code, prod.model_code, prod.model_type_code"
             													+ "			UNION ALL"
             													+ "			SELECT scrap.model_year_code, scrap.model_code, scrap.model_type_code, count (scrap.product_id) * -1 AS count"
                 												+ "				FROM (SELECT distinct framemtc.model_year_code, framemtc.model_code, framemtc.model_type_code, exception.product_id"
                           										+ "							FROM gal136tbx exception JOIN gal143tbx frame ON exception.PRODUCT_ID = frame.PRODUCT_ID"
                           										+ "													JOIN gal144tbx framemtc ON frame.PRODUCT_SPEC_CODE = framemtc.PRODUCT_SPEC_CODE"
                                                         		+ "													JOIN (SELECT product_id product_id, MAX(actual_timestamp) actual_timestamp " 
                           										+ "                                                         	FROM gal215tbx WHERE process_point_id = ?4 GROUP BY product_id) result" 
                                                          		+ "													ON result.product_id = exception.product_id and exception.actual_timestamp >= result.actual_timestamp"
                                								+ "							WHERE exception.ACTUAL_TIMESTAMP BETWEEN ?5 AND ?6) scrap"
                                 								+ "			GROUP BY scrap.model_year_code, scrap.model_code, scrap.model_type_code) totals"
          														+ " GROUP BY totals.model_year_code, totals.model_code, totals.model_type_code"
          														+ " ORDER BY totals.model_year_code, totals.model_code, totals.model_type_code";
	private static final String PERIOD_PASSING_COUNT_BY_PP1 ="WITH SUMMARY(MODEL_YEAR_CODE, MODEL_CODE, MODEL_TYPE_CODE, COUNT)" + 
			" AS (" + 
			"  SELECT G144.MODEL_YEAR_CODE, G144.MODEL_CODE, G144.MODEL_TYPE_CODE, COUNT(*) AS COUNT" + 
			"  FROM GAL215TBX G215" + 
			"  JOIN gal144tbx G144 ON G215.product_spec_code = G144.product_spec_code        " + 
			"  WHERE  G215.PROCESS_POINT_ID IN (@OFFProcessPoints)" + 
			"  AND G215.PRODUCTION_DATE >= ?1" + 
			"  AND G215.PRODUCTION_DATE < ?2" + 
			"  AND G215.PROCESS_COUNT = 1 " + 
			"  GROUP BY MODEL_YEAR_CODE, MODEL_CODE, MODEL_TYPE_CODE  " + 
			"  UNION " + 
			"  SELECT G144.MODEL_YEAR_CODE, G144.MODEL_CODE, G144.MODEL_TYPE_CODE, COUNT(*) * -1 AS COUNT" + 
			"  FROM GAL215TBX G215" + 
			"  JOIN gal144tbx G144 ON G215.product_spec_code = G144.product_spec_code      " + 
			"  WHERE  G215.PROCESS_POINT_ID IN (@SCRAPProcessPoints)" + 
			"  AND G215.PRODUCTION_DATE >= ?1 AND G215.PRODUCTION_DATE < ?2" + 
			"  AND G215.PROCESS_COUNT = 1" + 
			"  AND EXISTS(" + 
			"  SELECT * FROM GAL215TBX G215B WHERE G215B.PRODUCT_ID = G215.PRODUCT_ID AND" + 
			"  PROCESS_POINT_ID IN (@OFFProcessPoints)" + 
			"   )" + 
			"   GROUP BY MODEL_YEAR_CODE, MODEL_CODE, MODEL_TYPE_CODE" + 
			"  )" + 
			" SELECT MODEL_YEAR_CODE, MODEL_CODE, MODEL_TYPE_CODE, SUM(COUNT) AS COUNT" + 
			"  FROM SUMMARY" + 
			" GROUP BY MODEL_YEAR_CODE, MODEL_CODE, MODEL_TYPE_CODE" + 
			"  ORDER BY 1,2,3";
	
	private static final String SUB_LOTS = "select result " 
						+ "FROM ProductResult result, ProductionLot lot  "
						+ "WHERE lot.productionLot=result.productionLot AND result.id.processPointId=:ppId "
						+ "AND lot.kdLotNumber LIKE :kdLot AND lot.processLocation = :processLocation"; 
	
	private static final String PASSING_COUNT_BY_PP_ENGINES = "SELECT  sum (totals.count) AS prod_minus_scrap"
			+ "     FROM  (SELECT  count (prod.product_id) AS count"
			+ "               FROM  (SELECT  engine.product_id"
			+ "                         FROM gal131tbx engine "
			+ "                                              JOIN gal215tbx result   ON result.product_id = engine.product_id"
			+ "                          WHERE  result.process_point_id = ?1"
			+ "                            AND result.actual_timestamp BETWEEN ?2 AND ?3) prod"

			+ "             UNION ALL"
			+ "             SELECT  count (scrap.product_id) * -1 AS count"
			+ "                 FROM (SELECT  exception.product_id"
			+ "                           FROM gal136tbx exception JOIN gal131tbx engine ON exception.PRODUCT_ID = engine.PRODUCT_ID"
			+ "                                                         "
			+ "                                WHERE exception.ACTUAL_TIMESTAMP BETWEEN ?4 AND ?5) scrap"
			+ "                                 ) totals";

	

	private static final String PASSING_COUNT_BY_PP_FRAMES = "SELECT  sum (totals.count) AS prod_minus_scrap"
			+ "     FROM  (SELECT  count (prod.product_id) AS count"
			+ "               FROM  (SELECT  frame.product_id"
			+ "                         FROM gal143tbx frame "
			+ "                                              JOIN gal215tbx result   ON result.product_id = frame.product_id"
			+ "                          WHERE  result.process_point_id = ?1"
			+ "                            AND result.actual_timestamp BETWEEN ?2 AND ?3) prod"

			+ "             UNION ALL"
			+ "             SELECT  count (scrap.product_id) * -1 AS count"
			+ "                 FROM (SELECT  exception.product_id"
			+ "                           FROM gal136tbx exception JOIN gal143tbx frame ON exception.PRODUCT_ID = frame.PRODUCT_ID"

			+ "                                WHERE exception.ACTUAL_TIMESTAMP BETWEEN ?4 AND ?5) scrap"
			+ "                                 ) totals";

	
	private static final String FIND_ALREADY_PRODUCT_PROCESSED = "select count(distinct product_id) from galadm.gal215tbx a where a.PROCESS_POINT_ID = ?1 and a.ACTUAL_TIMESTAMP > ?2";
	
	private static final String SELECT_BY_DATE_RANGE
	= "SELECT * "
	+ "FROM GAL215TBX A "
    + "WHERE A.PROCESS_POINT_ID = ?1 AND "
    + "A.ACTUAL_TIMESTAMP > ?2 AND "
    + "A.ACTUAL_TIMESTAMP <= ?3 ORDER BY ACTUAL_TIMESTAMP FOR READ ONLY";
	
	private static final String SELECT_STRAGGLER
	= "SELECT * from galadm.gal215tbx where PROCESS_POINT_ID =?1 AND PRODUCTION_LOT <>?2 AND ACTUAL_TIMESTAMP >( "+ 
      " select ACTUAL_TIMESTAMP from galadm.gal215tbx where PROCESS_POINT_ID =?3 and  "+
      " PRODUCTION_LOT =?4 order by ACTUAL_TIMESTAMP asc fetch first 1 rows only)";

	private static final String SELECT_PRODUCTION_PERIOD = "SELECT process_location, production_date, period, start_timestamp,end_timestamp from gal226tbx where production_date in (select DISTINCT production_date from gal226tbx where ?1 BETWEEN START_TIMESTAMP and END_TIMESTAMP and process_location = ?2) and process_location = ?3 order by period for read only";
	private static final String SELECT_AE_PRODUCTION_RESULT = "SELECT GAL217TBX.Plan_Code, GAL217TBX.Line_No, GAL217TBX.Process_Location, GAL215TBX.Product_ID, SUBSTR(GAL215TBX.Production_Lot,9,12), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),1,4), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),6,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),9,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),12,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),15,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),18,2), SUBSTR(CHAR(GAL215TBX.Product_Spec_Code),1,22), GAL217TBX.KD_Lot_Number ,0, 'N' FROM GAL215TBX, GAL217TBX WHERE  GAL215TBX.Process_Point_ID in (?3,?4) AND GAL215TBX.Production_Lot = GAL217TBX.Production_Lot AND GAL215TBX.Actual_Timestamp >=  ?1 AND GAL215TBX.Actual_Timestamp <= ?2 for read only";
	private static final String SELECT_AF_PRODUCTION_RESULT = "SELECT GAL217TBX.Plan_Code, GAL217TBX.Line_No, GAL217TBX.Process_Location, GAL215TBX.Product_ID, SUBSTR(GAL215TBX.Production_Lot,9,12), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),1,4), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),6,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),9,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),12,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),15,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),18,2), SUBSTR(CHAR(GAL215TBX.Product_Spec_Code),1,22), GAL217TBX.KD_Lot_Number ,GAL143TBX.AF_ON_Sequence_Number, 'N' FROM GAL215TBX, GAL217TBX, GAL143TBX  WHERE  GAL215TBX.Process_Point_ID in (@OF_PROCESSPOINTS@) AND GAL215TBX.Production_Lot=GAL217TBX.Production_Lot AND gal143TBX.product_id = gal215TBX.product_id AND GAL215TBX.Actual_Timestamp >=  ?1 AND GAL215TBX.Actual_Timestamp <= ?2 for read only";
	private static final String SELECT_PA_PRODUCTION_RESULT = "SELECT GAL217TBX.Plan_Code, GAL217TBX.Line_No, GAL217TBX.PA_Process_Location, GAL215TBX.Product_ID, SUBSTR(GAL215TBX.Production_Lot,9,12), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),1,4), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),6,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),9,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),12,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),15,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),18,2), SUBSTR(CHAR(GAL215TBX.Product_Spec_Code),1,22), GAL217TBX.KD_Lot_Number ,GAL143TBX.AF_ON_Sequence_Number, 'N' FROM GAL215TBX, GAL217TBX, GAL143TBX WHERE  GAL215TBX.Process_Point_ID in (@OF_PROCESSPOINTS@) AND GAL215TBX.Production_Lot=GAL217TBX.Production_Lot AND gal143TBX.product_id = gal215TBX.product_id AND GAL215TBX.Actual_Timestamp >=  ?1 AND GAL215TBX.Actual_Timestamp <= ?2 for read only";
	private static final String SELECT_WE_PRODUCTION_RESULT = "SELECT GAL217TBX.Plan_Code, GAL217TBX.Line_No, GAL217TBX.WE_Process_Location, GAL215TBX.Product_ID, SUBSTR(GAL215TBX.Production_Lot,9,12), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),1,4), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),6,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),9,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),12,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),15,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),18,2), SUBSTR(CHAR(GAL215TBX.Product_Spec_Code),1,22), GAL217TBX.KD_Lot_Number ,GAL143TBX.AF_ON_Sequence_Number, 'N' FROM GAL215TBX, GAL217TBX, GAL143TBX WHERE  GAL215TBX.Process_Point_ID in (@OF_PROCESSPOINTS@) AND GAL215TBX.Production_Lot=GAL217TBX.Production_Lot AND gal143TBX.product_id = gal215TBX.product_id AND GAL215TBX.Actual_Timestamp >=  ?1 AND GAL215TBX.Actual_Timestamp <= ?2 for read only";
	private static final String SELECT_IA_PRODUCTION_RESULT = "SELECT GAL217TBX.Plan_Code, GAL217TBX.Line_No, GAL217TBX.Process_Location, GAL215TBX.Product_ID, SUBSTR(GAL215TBX.Production_Lot,9,12), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),1,4), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),6,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),9,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),12,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),15,2), SUBSTR(CHAR(GAL215TBX.Actual_Timestamp),18,2), SUBSTR(CHAR(GAL215TBX.Product_Spec_Code),1,22), GAL217TBX.KD_Lot_Number ,0, 'N' FROM GAL215TBX, GAL217TBX WHERE  GAL215TBX.Process_Point_ID in (?3,?4) AND GAL215TBX.Production_Lot = GAL217TBX.Production_Lot AND GAL215TBX.Actual_Timestamp >=  ?1 AND GAL215TBX.Actual_Timestamp <= ?2 for read only";
	private static final String SELECT_FRAME_SCRAP = "SELECT GAL217TBX.Plan_Code, GAL217TBX.Line_No,GAL217TBX.Process_Location, GAL143TBX.Product_ID, SUBSTR(GAL143TBX.Production_Lot,9,12),SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),1,4), SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),6,2), SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),9,2), SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),12,2), SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),15,2), SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),18,2), GAL143TBX.Product_Spec_Code,GAL217TBX.KD_Lot_Number,GAL143TBX.AF_ON_Sequence_Number, 'Y' FROM GAL143TBX, GAL217TBX, GAL136TBX WHERE  GAL143TBX.Tracking_status in(:scrapLineId) AND GAL136TBX.ACTUAL_TIMESTAMP between ?1 and ?2 AND GAL143TBX.Production_Lot = GAL217TBX.Production_Lot And GAL136TBX.product_id = GAL143TBX.Product_ID And GAL136TBX.EXCEPTIONAL_OUT_COMMENT like 'SCRAP%'";
	private static final String SELECT_ENGINE_SCRAP = "SELECT GAL217TBX.Plan_Code, GAL217TBX.Line_No,GAL217TBX.Process_Location, GAL131TBX.Product_ID, SUBSTR(GAL131TBX.Production_Lot,9,12),SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),1,4), SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),6,2), SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),9,2), SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),12,2), SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),15,2), SUBSTR(CHAR(GAL136TBX.ACTUAL_TIMESTAMP),18,2), GAL131TBX.Product_Spec_Code,GAL217TBX.KD_Lot_Number,0,'Y' FROM GAL131TBX, GAL217TBX, GAL136TBX WHERE  GAL131TBX.Tracking_status in(:scrapLineId) AND GAL136TBX.ACTUAL_TIMESTAMP between ?1 and ?2 AND GAL131TBX.Production_Lot = GAL217TBX.Production_Lot AND GAL136TBX.product_id = GAL131TBX.Product_ID AND GAL136TBX.EXCEPTIONAL_OUT_COMMENT like 'SCRAP%'";
	private static final String SELECT_OFF_RESULT_CNT = "select count(*) from gal215tbx where process_point_id in (@OF_PROCESSPOINTS@) and product_id = ?1";
	private static final String SELECT_PRODUCT_SPEC_CNT = "select product_spec_code, count(distinct product_id) from galadm.gal215tbx where process_point_id in (@PROCESS_POINT@) and actual_timestamp > ?1 group by product_spec_code";
	private static final String SELECT_PRODUCT_SPEC_CNT_BY_PLANCD =
			" select A.product_spec_code, C.LOT_NUMBER,count(distinct A.product_id) from galadm.gal215tbx A " +
			" join galadm.GAL216TBX B on A.PRODUCT_ID=B.PRODUCT_ID " +
			" join galadm.GAL212TBX C on B.PRODUCTION_LOT=C.PRODUCTION_LOT " +
			"    and C.PRODUCT_SPEC_CODE=A.PRODUCT_SPEC_CODE " +
			" where A.process_point_id in (@PROCESS_POINT@) " +
			"    and A.actual_timestamp >= ?1 " +
			"    and A.actual_timestamp < ?2 " +
			"    and C.PLAN_CODE = ?3 " +
			"    and A.PROCESS_COUNT = 1" +
			" group by A.product_spec_code,C.LOT_NUMBER";
		
    
	//Production Result query general for all the process locations
	private static final String PRODUCTION_RESULT = new StringBuilder()
		.append( "select distinct "							)
		.append( "		gal217tbx.plan_code "				)
		.append( "		,gal217tbx.line_no "					)
		.append( "		,gal217tbx.process_location "		)
		.append( "		,gal215tbx.product_id "				)
		.append( "		,gal217tbx.lot_number "			)
		.append( "		,to_char(gal215tbx.ACTUAL_TIMESTAMP,'YYYYMMDDHH24MISS') as ACTUAL_TIMESTAMP " )
		.append( "		,gal217tbx.product_spec_code "		)
		.append( "		,gal217tbx.kd_lot_number "			)
		.append( "		,lpad(nvl((select frame.AF_ON_SEQUENCE_NUMBER	"	)
		.append( "			from gal143tbx frame		"	)
        .append( "          where frame.product_id = gal215tbx.PRODUCT_ID) " )
        .append( "			,0)				"				)
        .append( "		,5,0) as bos_serial_number	"		)
        .append( "		,gal212tbx.MBPN				"		) //Part number
        .append( "		,gal212tbx.HES_COLOR		"		) //Part color code
        .append( "from GAL215TBX gal215tbx			"		)
        .append( "	,GAL217TBX gal217tbx			"		)
        .append( "  ,GAL212TBX gal212tbx			"		)
        .append( "where	gal217tbx.production_Lot  = gal215tbx.production_Lot "	)
        .append( "and gal217tbx.production_Lot	  = gal212tbx.production_Lot "	)
        .append( "and gal215tbx.process_Point_Id  in (:PROCESS_POINT) "			)//replace the :PROCESS_POINT for the process point string separete by coma.
        .append( "and gal215tbx.actual_timestamp  >= ?1 "	)       
        .append( "and gal215tbx.actual_timestamp  <= ?2 "	)
		.toString();
	
	//Scrap query for Transmission (Mission_tbx)
	private static final String PRODUCTION_RESULT_MISSION_SCRAP = new StringBuilder()
		.append( "SELECT DISTINCT							"		)
		.append( "		gal217tbx.PLAN_CODE					"		)
        .append( "		,gal217tbx.LINE_NO					"		)
    	.append( "		,gal238tbx.GPCS_PROCESS_LOCATION as process_location "	)
    	.append( "		,mission.PRODUCT_ID "						)
    	.append( "		,gal217tbx.LOT_NUMBER "						)
    	.append( "      ,to_char(gal215tbx.ACTUAL_TIMESTAMP,'YYYYMMDDHH24MISS') as ACTUAL_TIMESTAMP" )
    	.append( "		,gal217tbx.PRODUCT_SPEC_CODE	"			)
    	.append( "      ,gal217tbx.KD_LOT_NUMBER	"				)
    	.append( "      ,lpad(nvl((select frame.AF_ON_SEQUENCE_NUMBER	"				)
    	.append( "					from gal143tbx frame "								)
    	.append( "					where frame.product_id = gal215tbx.PRODUCT_ID) "	)
    	.append( "	              ,0)	"							)
    	.append( "            ,5,0) as bos_serial_number	"		)
    	.append( "      ,gal136tbx.EXCEPTIONAL_OUT_COMMENT	"		)
    	.append( "		,gal212tbx.MBPN						"		)// Part number
    	.append( "		,gal212tbx.HES_COLOR				"		)// Part color code
    	.append( "FROM Mission_tbx mission	"						)
    	.append( "      ,Gal136tbx gal136tbx	"					)
    	.append( "      ,Gal214tbx gal214tbx	"					)
    	.append( "      ,Gal215tbx gal215tbx	"					)
    	.append( "      ,Gal217tbx gal217tbx	"					)
    	.append( "      ,Gal238tbx gal238tbx	"					)
    	.append( "		,Gal212tbx gal212tbx "						)
    	.append( "WHERE gal215tbx.product_Id	= gal136tbx.product_Id	"	)
    	.append( "  AND mission.product_Id		= gal136tbx.product_Id	"	)
    	.append( "  AND gal214tbx.process_Point_Id	= gal215tbx.process_Point_Id	")
    	.append( "  AND gal214tbx.division_Id	= gal238tbx.division_Id	"	)
    	.append( "  AND mission.production_Lot	= gal217tbx.production_Lot	"	)
    	.append( "	AND gal217tbx.production_Lot	= gal212tbx.production_Lot ")
    	.append( "  AND mission.tracking_Status	in (:TRACKING_STATUS)	"	)//:TRACKING_STATUS)
    	.append( "  AND gal215tbx.process_Point_Id  in (:PROCESS_POINT)	"	)//:PROCESS_POINT
    	.append( "  AND gal136tbx.actual_Timestamp between ?1 and ?2	"	)
		.toString();
	
	private static final String DELETE_HISTORY_BY_PROCESS_POINT= "DELETE FROM gal215tbx productResult WHERE productResult.product_Id = ?1 and productResult.process_Point_Id in (@processPoint@)";
	
	private static final String PRODUCT_RESULTS_OF_TODAY_BASE = "SELECT HIST.PRODUCT_ID, HIST.PRODUCT_SPEC_CODE, HIST.PROCESS_POINT_ID, MAX(HIST.ACTUAL_TIMESTAMP) AS ACTUAL_TIMESTAMP  FROM  "
		+ " 	(SELECT LINE_PROD_DATE.LINE_ID,LINE_PROD_DATE.LINE_NAME,LINE_PROD_DATE.PROCESS_POINT_TYPE,LINE_PROD_DATE.LINE_NO,LINE_PROD_DATE.PROCESS_LOCATION, LINE_PROD_DATE.PRODUCTION_DATE,PROD_SHIFT.SHIFT "
		+ " 		FROM (SELECT LINE.LINE_ID,LINE.LINE_NAME,PP.PROCESS_POINT_ID,PP.PROCESS_POINT_TYPE,PROD_DATE.LINE_NO,PROD_DATE.PROCESS_LOCATION,PROD_DATE.PRODUCTION_DATE  "
		+ " 			FROM GALADM.GAL214TBX PP INNER JOIN GALADM.GAL195TBX LINE ON PP.LINE_ID=LINE.LINE_ID  "
		+ " 			INNER JOIN GALADM.GAL128TBX DIV ON DIV.DIVISION_ID=LINE.DIVISION_ID  "
		+ " 			INNER JOIN GALADM.GAL238TBX MAP ON (MAP.DIVISION_ID=DIV.DIVISION_ID)  "
		+ " 			INNER JOIN GALADM.GAL226TBX PROD_DATE ON PROD_DATE.PLANT_CODE=?1 AND PROD_DATE.LINE_NO=MAP.GPCS_LINE_NO AND PROD_DATE.PROCESS_LOCATION=MAP.GPCS_PROCESS_LOCATION  "
		+ " 			AND PROD_DATE.START_TIMESTAMP <= CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP <= PROD_DATE.END_TIMESTAMP  "
		+ " 			WHERE PP.TRACKING_POINT_FLAG = 1  AND DIV.DIVISION_ID = ?2 AND LINE.LINE_ID = ?3 "
		+ " 			GROUP BY LINE.LINE_ID,LINE.LINE_NAME,PP.PROCESS_POINT_ID,PP.PROCESS_POINT_TYPE,PROD_DATE.LINE_NO,PROD_DATE.PROCESS_LOCATION,PROD_DATE.PRODUCTION_DATE) AS LINE_PROD_DATE  "
		+ " 		LEFT JOIN GALADM.GAL226TBX PROD_SHIFT ON PROD_SHIFT.PLANT_CODE=?4 AND LINE_PROD_DATE.LINE_NO=PROD_SHIFT.LINE_NO AND LINE_PROD_DATE.PROCESS_LOCATION=PROD_SHIFT.PROCESS_LOCATION AND LINE_PROD_DATE.PRODUCTION_DATE = PROD_SHIFT.PRODUCTION_DATE  "
		+ " 		GROUP BY LINE_PROD_DATE.LINE_ID,LINE_PROD_DATE.LINE_NAME,LINE_PROD_DATE.PROCESS_POINT_TYPE,LINE_PROD_DATE.LINE_NO,LINE_PROD_DATE.PROCESS_LOCATION,LINE_PROD_DATE.PRODUCTION_DATE,PROD_SHIFT.SHIFT) AS PROD_SHIFT_PLAN "
		+ " INNER JOIN GALADM.GAL226TBX PERIOD ON PERIOD.PLANT_CODE=?5 AND PERIOD.LINE_NO = PROD_SHIFT_PLAN.LINE_NO AND PERIOD.PROCESS_LOCATION = PROD_SHIFT_PLAN.PROCESS_LOCATION AND PERIOD.PRODUCTION_DATE=PROD_SHIFT_PLAN.PRODUCTION_DATE AND PERIOD.SHIFT=PROD_SHIFT_PLAN.SHIFT  "
		+ " INNER JOIN GALADM.GAL214TBX PP ON PROD_SHIFT_PLAN.LINE_ID=PP.LINE_ID "
		+ " INNER JOIN GALADM.GAL215TBX HIST ON PP.PROCESS_POINT_ID=HIST.PROCESS_POINT_ID AND HIST.ACTUAL_TIMESTAMP BETWEEN PERIOD.START_TIMESTAMP AND PERIOD.END_TIMESTAMP "
		+ " WHERE HIST.PROCESS_POINT_ID = ?6 ";
	
	private static final String PRODUCT_RESULTS_OF_TODAY_FOR_SHIFT = PRODUCT_RESULTS_OF_TODAY_BASE 
			+ " AND PROD_SHIFT_PLAN.SHIFT=?7 "
			+ " GROUP BY HIST.PRODUCT_ID, HIST.PRODUCT_SPEC_CODE, HIST.PROCESS_POINT_ID "
			+ " ORDER BY ACTUAL_TIMESTAMP ";
	
	private static final String PRODUCT_RESULTS_OF_TODAY = PRODUCT_RESULTS_OF_TODAY_BASE
			+ " GROUP BY HIST.PRODUCT_ID, HIST.PRODUCT_SPEC_CODE, HIST.PROCESS_POINT_ID "
			+ " ORDER BY ACTUAL_TIMESTAMP ";
	
	private static final String FIND_LAST_PRODUCT_ID_FOR_PROCESS_POINT="select result from ProductResult result where result.id.processPointId=:ppId order by result.id.actualTimestamp desc"; 
	
	private static final String FIND_AGED_INVENTORY_COUNT = "SELECT COUNT(DISTINCT PR1.PRODUCT_ID) FROM GALADM.GAL215TBX PR1 "
			+ "WHERE PR1.PROCESS_POINT_ID = ?1 "
			+ "AND PR1.PRODUCT_ID NOT IN (SELECT PR2.PRODUCT_ID FROM GALADM.GAL215TBX PR2 WHERE PR2.PROCESS_POINT_ID = ?2) "
			+ "AND CURRENT_TIMESTAMP >= (PR1.ACTUAL_TIMESTAMP + ?3 MINUTES)";
	
	private static final String FIND_PRODUCT_HISTORY_BY_PROCESS_POINT = "SELECT PRODUCT_ID, PRODUCT_SPEC_CODE, ASSOCIATE_NO, ACTUAL_TIMESTAMP FROM GAL215TBX WHERE PROCESS_POINT_ID = ?1 ";

	private static final String FIRST_TIME_IN_PROCESS = "SELECT COALESCE(MAX(PROCESS_COUNT),0) AS PROCESS_COUNT FROM GALADM.GAL215TBX WHERE PRODUCT_ID=?1 AND PROCESS_POINT_ID=?2 AND ACTUAL_TIMESTAMP=(SELECT MAX(ACTUAL_TIMESTAMP) FROM GALADM.GAL215TBX WHERE PRODUCT_ID=?1 AND PROCESS_POINT_ID=?2)";
	
	private static final String INITIAL_PROCESS_TIMESTAMP = "SELECT ACTUAL_TIMESTAMP FROM GAL215TBX WHERE PRODUCT_ID = ?1 AND PROCESS_POINT_ID in (@PROCESS_POINT@) ORDER BY ACTUAL_TIMESTAMP ASC";

	private static final String UPDATE_COLOR_DETAILS_6 = "Update GALADM.GAL215TBX set PRODUCT_SPEC_CODE=?1, ASSOCIATE_NO=?3 "
			+ "where PRODUCT_SPEC_CODE=?2 and PRODUCTION_LOT in (:productionLot)";
	private static final String FIND_PRODUCT_HISTORY_BY_PROCESS_POINT_LIST = "SELECT * FROM GAL215TBX WHERE PRODUCT_ID=?1 AND PROCESS_POINT_ID in (listOfProcessPoints) ORDER BY ACTUAL_TIMESTAMP DESC FOR READ ONLY"; 
	
	private static final String PRODUCTION_COUNT_GSAP = "select trim(g215.PRODUCT_ID) as VIN, trim(g215.PRODUCT_SPEC_CODE) as MANUFACTURING_SPEC_CODE, char(g215.ACTUAL_TIMESTAMP) as TRACKING_TS, g215.PROCESS_POINT_ID " 
			+ "from gal215tbx g215 "
			+ "join GAL143TBX g143 on G215.PRODUCT_ID = g143.PRODUCT_ID "
			+ "where g215.PROCESS_POINT_ID in (listOfcustomerProcessPoints) " 
			+ "and (g215.ACTUAL_TIMESTAMP > ?1 "
			+ "and g215.ACTUAL_TIMESTAMP < ?2) " 
			+ "order by g215.ACTUAL_TIMESTAMP";
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<ProductResult> findByProductAndProcessPoint(ProductResult productResult) {
		Parameters params = Parameters.with("1", productResult.getId().getProductId());
		params.put("2",  productResult.getId().getProcessPointId());
		return findAllByNativeQuery(FIND_PRODUCT_RESULTS, params, ProductResult.class);
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public int firstTimeInProcess(String productId, String processPointId) {
		Parameters params = Parameters.with("1", productId);
		params.put("2",  processPointId);
		return findFirstByNativeQuery(FIRST_TIME_IN_PROCESS, params, Integer.class);
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<ProductResult> findByProcessPoint(String processPointId) {
		Parameters params = Parameters.with("id.processPointId", processPointId);
		return findAll(params);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<ProductResult> findByProductId(String productId) {
		Parameters params = Parameters.with("1", productId);
		List<Object[]> tempList = findAllByNativeQuery(FIND_PRODUCT_HISTORY, params, Object[].class);
		List<ProductResult> productResultList = new ArrayList<ProductResult>();
		
		for(Object[] array :tempList) {
			ProductResult productResult = null;		       			
			productResult = new ProductResult();
			
			productResult.setProductId(getString(array[0]));
			productResult.setProcessPointId(getString(array[1]));
			productResult.setActualTimestamp(array[2]==null?null:(Timestamp)array[2]);
			productResult.setProcessPointName(getString(array[3]));
			
			productResultList.add(productResult);
		}
		return productResultList;
	}

    @Transactional
	public long findTotalProductProcessed(String productionLot, String processPointId) {
    	Parameters params = Parameters.with("1", productionLot);
		params.put("2", processPointId);

		return findFirstByNativeQuery(TOTAL_PRODUCT_PROCESSED, params, Long.class);
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

    public boolean isProductProcessed(String productId, String processPointId,
			String startTimestamp) {
		return isProductProcessed(productId, processPointId,startTimestamp,PRODUCT_PROCESSED);
	}

	@Override
	protected String getProductIdName() {
		return "productId";
	}
	
	@Override
	protected String getProductIdColumnName() {
		return "product_id";
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED, propagation=Propagation.NOT_SUPPORTED)
	public List<Object[]> findProdLotResults(String plantCode, String lineNo, String div, String processPointId) {
		Parameters params = Parameters.with("1", processPointId);
		params.put("2", div);
		String maxTimetamp = findFirstByNativeQuery(MAX_TIMESTAMP_PROD_LOT, params, String.class);
		List<Object[]> resultList = new ArrayList<Object[]>();
		if(maxTimetamp != null) {
			params = Parameters.with("1", plantCode);
			params.put("2", lineNo);
			params.put("3", div);
			params.put("4", processPointId);
			params.put("5", maxTimetamp);
			resultList = findAllByNativeQuery(FIND_LOT_RESULT_PROD_LOT, params, Object[].class);
		}
		return resultList;
	}

	@Override
	public boolean isMostRecent(String productId, Date actualTimestamp) {
		if (actualTimestamp == null) {
			return true;
		}

		Parameters params = Parameters.with("1", productId);
		getLogger().info("Attempting to process product " + productId + " with actual timestamp " + actualTimestamp);
		
		try {
			List<Object[]> results = findAllByNativeQuery(IS_PROCESSING_MOST_RECENT, params, Object[].class);
			if (results.size() > 0) {
				String lastPassingPPId = StringUtils.trimToEmpty(getString(results.get(0)[0]));
				Timestamp lastProcessedTime = (results.get(0)[1] == null? null : (Timestamp)results.get(0)[1]);

				if (lastProcessedTime != null) {
					getLogger().info("Product " + productId + " passed process point " + lastPassingPPId + " at " + lastProcessedTime);
					return (actualTimestamp.getTime() >= lastProcessedTime.getTime());
				} 
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Could not verify if product " + productId + " was processed at a process point later than " + actualTimestamp);
		}
		return true;
	}
	
	//TASK0013687
	/**
	 * @see ProductResultDao#getPeriodPassingCountByPP(String, Date, Date)
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED, propagation=Propagation.NOT_SUPPORTED)
	public List<Object[]> getPeriodPassingCountByPP(String offProcessPoints, String scrapProcessPoints,Date start, Date end) {
		Parameters params = Parameters.with("1", start);
		params.put("2", end);
		String sql = PERIOD_PASSING_COUNT_BY_PP1.replace("@OFFProcessPoints", StringUtil.toSqlInString(offProcessPoints));
		sql =  sql.replace("@SCRAPProcessPoints", StringUtil.toSqlInString(scrapProcessPoints));
				
		return findAllByNativeQuery(sql, params, Object[].class);
	}

	// NSE Production Progress
	/**
	 * @see ProductResultDao#getSubLots(String, Date, Date)
	 */
	public List<ProductResult> getSubLots(String kdLot, String div, String ppId) {
		Parameters params = Parameters.with("ppId", ppId);
		params.put("kdLot", kdLot.substring(0, 16) + "%");
		params.put("processLocation", div);
		return findAllByQuery(SUB_LOTS, params);
	}
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.NOT_SUPPORTED)
	public int getPassingCountByPPForFrames(String processPoint, Date start,
			Date end) {

		Parameters params = Parameters.with("1", processPoint);
		params.put("2", start);
		params.put("3", end);
		params.put("4", start);
		params.put("5", end);
		return findFirstByNativeQuery(PASSING_COUNT_BY_PP_FRAMES, params,
				Integer.class);

	}

	/**
	 * @see ProductResultDao#getPeriodPassingCountByPPForEngines(String, Date,
	 *      Date)
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.NOT_SUPPORTED)
	public int getPassingCountByPPForEngines(String processPoint, Date start,
			Date end) {
		Parameters params = Parameters.with("1", processPoint);
		params.put("2", start);
		params.put("3", end);
		params.put("4", start);
		params.put("5", end);
		return findFirstByNativeQuery(PASSING_COUNT_BY_PP_ENGINES, params,
				Integer.class);
	}
	
	public int findAlreadyProcessedProdAtPP(String processPointId,String actualTimestamp) {
		Parameters params = Parameters.with("1", processPointId);
		params.put("2", actualTimestamp);
		Integer count = findFirstByNativeQuery(FIND_ALREADY_PRODUCT_PROCESSED, params, Integer.class);
		return (count == null)? 0 : count.intValue();
	}
	
	// Secondary VIN
	/**
	 * @see ProductResultDao#getSecondaryVinsByProcessPointAndDateRange(String, Date, Date)
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<ProductResult> getSecondaryVinsByProcessPointAndDateRange(String ppId, String startTime, String endTime) {
		Parameters params = Parameters.with("1", ppId);
		params.put("2", startTime);
		params.put("3", endTime);
		return findAllByNativeQuery(SELECT_BY_DATE_RANGE, params);
	}

	public List<ProductResult> getStraggler(String ppId, String productionLot) {
		Parameters params = Parameters.with("1", ppId);
		params.put("2", productionLot);
		params.put("3", ppId);
		params.put("4", productionLot);
		return findAllByNativeQuery(SELECT_STRAGGLER, params);
	}

	
	/**
	 * Get the production progress for each process point (WE, PA, AF, AE, AM).
	 * @param processPoints
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object []> getProductionResult ( final String processPoints
												, final String startDate
												, final String endDate)
	{
		Parameters parameters 			= Parameters.with( "1", startDate );
		parameters.put( "2", endDate );
		final String processPointParameter = new StringBuilder()
											.append( "\'")
											.append( processPoints.replaceAll(",", "\',\'") )
											.append("\'")
											.toString();
		final String resultQuery		= PRODUCTION_RESULT.replaceAll( ":PROCESS_POINT", processPointParameter );
		List<Object[]> productionResult	= findResultListByNativeQuery( resultQuery, parameters );
		return productionResult;
	}
	/**
	 * Gets the Scrap production progress for product.
	 * AE = Engine data and use the scrap query for engine (gal131tbx)
	 * AF = Frame data and use the scrap query for frame (gal143tbx)
	 * AM = Transmission data an use the scrap query for transmission (mission_tbx)
	 * @param processLocation
	 * @param processPoints
	 * @param startDate
	 * @param endDate
	 * @param trackingStatus
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object []> getProductionResult ( final String processLocation
												, final String processPoints
												, final String startDate
												, final String endDate
												, final String trackingStatus)
	{
		Parameters parameters 			= Parameters.with( "1", startDate );
		parameters.put( "2", endDate );
		final String processPointParameter		= new StringBuilder().append( "\'")
												.append( processPoints.replaceAll(",", "\',\'") )
												.append("\'")
												.toString();
		final String trackingStatusParameter	= new StringBuilder().append( "\'")
												.append( trackingStatus.replaceAll(",", "\',\'") )
												.append("\'")
												.toString();
		String resultQuery = null;
		//Add the different process location and the respective query for scrap (engine or frame)
		if ( processLocation.equals( "AM" ))
		{
			resultQuery		= PRODUCTION_RESULT_MISSION_SCRAP.replaceAll(":PROCESS_POINT", processPointParameter)
							.replaceAll(":TRACKING_STATUS", trackingStatusParameter);
		}
		List<Object[]> productionResult		= findResultListByNativeQuery( resultQuery, parameters );
		return productionResult;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getProductionPeriod(Timestamp prodTimestamp, String processLocation1, String processLocation2) {
		Parameters parameters = Parameters.with("1", prodTimestamp);
		parameters.put("2", processLocation1);
		parameters.put("3", processLocation2);
		return findResultListByNativeQuery(SELECT_PRODUCTION_PERIOD, parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAEProductionResult(Timestamp startTimestamp, Timestamp endTimestamp, String aeOffResCntPPId1, String aeOffResCntPPId2) {
		Parameters parameters = Parameters.with("1", startTimestamp);
		parameters.put("2", endTimestamp);
		parameters.put("3", aeOffResCntPPId1);
		parameters.put("4", aeOffResCntPPId2);
		return findResultListByNativeQuery(SELECT_AE_PRODUCTION_RESULT, parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAFProductionResult(Timestamp startTimestamp, Timestamp endTimestamp, List<String> afOffResCntPPId) {
		Parameters parameters = Parameters.with("1", startTimestamp);
		parameters.put("2", endTimestamp);
		//parameters.put("3", );
		return findResultListByNativeQuery(SELECT_AF_PRODUCTION_RESULT.replaceAll("@OF_PROCESSPOINTS@", StringUtil.toSqlInString(afOffResCntPPId)), parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getPAProductionResult(Timestamp startTimestamp, Timestamp endTimestamp, List<String> paOffResCntPPId) {
		Parameters parameters = Parameters.with("1", startTimestamp);
		parameters.put("2", endTimestamp);
		//parameters.put("3", StringUtil.toSqlInString(paOffResCntPPId));
		return findResultListByNativeQuery(SELECT_PA_PRODUCTION_RESULT.replaceAll("@OF_PROCESSPOINTS@", StringUtil.toSqlInString(paOffResCntPPId)), parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getWEProductionResult(Timestamp startTimestamp, Timestamp endTimestamp, List<String> weOffResCntPPId) {
		Parameters parameters = Parameters.with("1", startTimestamp);
		parameters.put("2", endTimestamp);
		//parameters.put("3", StringUtil.toSqlInString(weOffResCntPPId));
		return findResultListByNativeQuery(SELECT_WE_PRODUCTION_RESULT.replaceAll("@OF_PROCESSPOINTS@", StringUtil.toSqlInString(weOffResCntPPId)), parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getIAProductionResult(Timestamp startTimestamp, Timestamp endTimestamp, String iaOffResCntPPId1, String iaOffResCntPPId2) {
		Parameters parameters = Parameters.with("1", startTimestamp);
		parameters.put("2", endTimestamp);
		parameters.put("3", iaOffResCntPPId1);
		parameters.put("4", iaOffResCntPPId2);
		return findResultListByNativeQuery(SELECT_IA_PRODUCTION_RESULT, parameters);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getFrameScrap(Timestamp afStartTimestamp, Timestamp afEndTimestamp, List<String> scrapLineIds) {
		Parameters parameters = Parameters.with("1", afStartTimestamp);
		parameters.put("2", afEndTimestamp);
		String str = StringUtil.toSqlInString(scrapLineIds);
		String sql = SELECT_FRAME_SCRAP.replace(":scrapLineId", str);
		return findResultListByNativeQuery(sql, parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getEngineScrap(Timestamp aeStartTimestamp, Timestamp aeEndTimestamp, List<String> scrapLineIds) {
		Parameters parameters = Parameters.with("1", aeStartTimestamp);
		parameters.put("2", aeEndTimestamp);
		String str = StringUtil.toSqlInString(scrapLineIds);
		String sql = SELECT_ENGINE_SCRAP.replace(":scrapLineId", str);
		return findResultListByNativeQuery(sql, parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getOffResultCnt(String productId, List<String> offResCntPPId) {
		Parameters parameters = Parameters.with("1", productId);
		//parameters.put("2", StringUtil.toSqlInString(offResCntPPId));
		return findResultListByNativeQuery(SELECT_OFF_RESULT_CNT.replaceAll("@OF_PROCESSPOINTS@",  StringUtil.toSqlInString(offResCntPPId)), parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getProductSpecCnt(String processPoints, Timestamp startTime, Timestamp endTime, String planCd) {
		Parameters parameters = Parameters.with("1", startTime)
								.put("2", endTime)
								.put("3", planCd);
		return findResultListByNativeQuery(SELECT_PRODUCT_SPEC_CNT_BY_PLANCD.replaceAll("@PROCESS_POINT@", processPoints), parameters);		
	}
	
	/**
	 * delete the history created with yms, this occur because when a vin has factory return is 
	 * necessary delete the previous process points. 
	 * @param processPints	-	String with all process points separated by comma i.e. PPXXXXX,PPXXXXX
	 * @param productId		-	The vin
	 * @return
	 */	
	@Transactional
	public int deleteHistoryByProcessPoint(final String processPoints, final String productId){
		Parameters params = Parameters.with("1", productId);			
		return executeNative(DELETE_HISTORY_BY_PROCESS_POINT.replace("@processPoint@", StringUtil.toSqlInString(processPoints.split(","))), params);
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.dao.product.ProductResultDao#getActualProductResultsOfToday(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<ProductResult> getActualProductResultsOfToday(
			String gpcsPlantCode, String divisionId, String lineId,
			String processPointId, String shift) {
		Parameters params = Parameters.with("1", gpcsPlantCode)
				.put("2", divisionId)
				.put("3", lineId)
				.put("4", gpcsPlantCode)
				.put("5", gpcsPlantCode)
				.put("6", processPointId);
		String sql = null;
		if (StringUtils.isBlank(shift)) {
			sql = PRODUCT_RESULTS_OF_TODAY;
		} else {
			sql = PRODUCT_RESULTS_OF_TODAY_FOR_SHIFT;
			params.put("7", shift);
		}
		return findAllByNativeQuery(sql, params, ProductResult.class);
	}
	
	
	public ProductResult findLastProductForProcessPoint(String ppId) {
		return findFirstByQuery(FIND_LAST_PRODUCT_ID_FOR_PROCESS_POINT, Parameters.with("ppId", ppId));
	}
	
	public int getAgedInventoryCount(String processPointIdOne, String processPointIdTwo, int ageInMins) {
		Parameters params = Parameters.with("1", processPointIdOne)
				.put("2", processPointIdTwo)
				.put("3", ageInMins);
		Integer count = findFirstByNativeQuery(FIND_AGED_INVENTORY_COUNT, params, Integer.class);
		return (count == null)? 0 : count.intValue();
	}
	/**
	 * @see ProductResultDao#getSubLots(String, Date, Date)
	 */
	public Timestamp getMaxActualTs(String productId, String pp) {
		Parameters params = Parameters.with("id.processPointId", pp);
		params.put("id.productId", productId);
		Timestamp actual = max("id.actualTimestamp", Timestamp.class, params);
		return actual;
	}

	public List<ProductHistoryDto> findAllByProcessPoint(String processPointId, int rowNumber) {
		String sql = FIND_PRODUCT_HISTORY_BY_PROCESS_POINT + " order by ACTUAL_TIMESTAMP desc fetch first " + rowNumber + " rows only";
		return findAllByNativeQuery(sql, Parameters.with("1", processPointId), ProductHistoryDto.class);
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED, propagation=Propagation.NOT_SUPPORTED)
	public Timestamp getInitialProcessTimestamp(String productId, List<String> processPointIdList) {
		String processPointIds = "\'" + processPointIdList.get(0);
		for (int i = 1; i < processPointIdList.size(); i++) {
			processPointIds += "\',\'" + processPointIdList.get(i);
		}
		processPointIds += "\'";
		Parameters params = Parameters.with("1", productId);
		Timestamp t =  findFirstByNativeQuery(INITIAL_PROCESS_TIMESTAMP.replaceAll("@PROCESS_POINT@", processPointIds), params, Timestamp.class);
		return t;
	}

	@Transactional
	public void updateColorDetails(String productCodeForUpdate,
			FrameSpecDto selectedFrameSpecDto, 
			List<String> productionLots) {
		
		Parameters params2 = Parameters.with("1", productCodeForUpdate)
				.put("2", selectedFrameSpecDto.getProductSpecCode());
		
		String sqlStr = UPDATE_COLOR_DETAILS_6;
		sqlStr = sqlStr.replaceAll(":productionLot", StringUtil.toSqlInString(productionLots));
		params2.put("3", selectedFrameSpecDto.getUserId());
		executeNative(sqlStr, params2);
	}
	
	public List<ProductResult> findHistoryByProcessPointList(String productId, List<String> proocessPointList) {
		Parameters params = Parameters.with("1", productId);
		String sql = FIND_PRODUCT_HISTORY_BY_PROCESS_POINT_LIST;
		sql = sql.replaceAll("listOfProcessPoints", StringUtil.toSqlInString(proocessPointList));
	
		return findAllByNativeQuery(sql, params, ProductResult.class);
	}

	@Override
	public List<Object[]> findProductionCountGSAP(String customerProcessPoints, String startDate, String endDate) {
		return findProductionCountGSAP(customerProcessPoints, startDate, endDate, null);
	}

	@Override
	public List<Object[]> findProductionCountGSAP(String customerProcessPoints, String startDate, String endDate, String productId) {
		Logger.getLogger().info("Started Fetching Production Count Details.........." );
		List<String> customerProcessPointsList = StringUtils.isBlank(customerProcessPoints) ? new ArrayList<>()
				: Arrays.asList(customerProcessPoints.split(","));

		String sql = PRODUCTION_COUNT_GSAP.replace("listOfcustomerProcessPoints", StringUtil.toSqlInString(customerProcessPointsList));

		if (!StringUtils.isEmpty(productId)) {
			sql = sql.replace("order by", "AND g215.PRODUCT_ID = '" + productId + "' ORDER BY");
			Logger.getLogger().info("Modified SQL query to include productId " + sql);
		}

		Parameters params = Parameters.with("1", startDate).put("2", endDate);
		return findAllByNativeQuery(sql, params, Object[].class);
	}
}
