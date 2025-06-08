package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.dto.LotSequenceDto;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.PreProductionLotStatus;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.StringUtil;
/**
 *
 * @author Gangadhararao Gadde
 * @date apr 6, 2016
 */
public class PreProductionLotDaoImpl extends
BaseDaoImpl<PreProductionLot, String> implements PreProductionLotDao {
	private final Logger logger = Logger.getLogger("PreProductionLotDaoImpl");
	private static final String UPDATE_NEXT_PRODUCTION_LOT = "update PreProductionLot e set e.nextProductionLot = :nextProductionLot where e.productionLot = :productionLot";
	// private static String FIND_ALL_INCOMPLETE_PRODUCTION_LOT =
	// "select l from PreProductionLot l where l.processLocation = :processLocation and (l.sendStatusId = :wating or l.sendStatusId = :inProcess) and l.holdStatus = :holdStatus";
	private static final String FIND_FIRST_PROCESSED = "select e from PreProductionLot e where e.processLocation = :processLocation"
			+ " and not exists(select a from PreProductionLot a where a.nextProductionLot = e.productionLot)";

	private static final String FIND_SAME_KDLOT = "select * from galadm.gal212tbx a,galadm.gal217tbx b where a.PRODUCTION_LOT = b.PRODUCTION_LOT and substr(b.KD_LOT_NUMBER,1,17) = ?1";
	private static final String FIND_UNSENT_LOTS = "select e from PreProductionLot e where e.processLocation = :processLocation and e.sendStatusId < 2";

	private static final String UPDATE_STAMPED_COUNT = "update galadm.gal212tbx set STAMPED_COUNT = ?2 where PRODUCTION_LOT = ?1";
	private static final String UPDATE_STAMPED_COUNT_AUTO = "update galadm.gal212tbx set stamped_count = (select count(send_status) from galadm.gal216tbx where production_lot = ?1 and send_status = 2) where PRODUCTION_LOT = ?1";
	private static final String UPDATE_SEND_STATUS = "UPDATE galadm.gal212tbx SET send_status = ?2 WHERE production_lot = ?1";
	private static final String UPDATE_SENT_TIMESTAMP = "UPDATE galadm.gal212tbx SET sent_timestamp = current_timestamp WHERE production_lot = ?1";
	private static final String UPDATE_NEXT_PROD_LOT = "update galadm.gal212tbx set NEXT_PRODUCTION_LOT = ?2 where NEXT_PRODUCTION_LOT = ?1";
	private static final String UPDATE_START_PRODUCT_ID = "update galadm.gal212tbx set START_PRODUCT_ID = ?2 where PRODUCTION_LOT = ?1";
	private static final String FIND_MAX_SEQ = "SELECT MAX(SEQUENCE) From GALADM.GAL212TBX T1 WHERE T1.PLAN_CODE = ?1";
	private static final String FIND_MAX_SEQ_BY_NOT_SEND_STATUS = "SELECT MAX(SEQUENCE) From GALADM.GAL212TBX T1 WHERE T1.PLAN_CODE = ?1 and SEND_STATUS <> ?2";

	private static final String FIND_ALL_FOR_PROCESS_LOCATION = "WITH Sch( PRODUCTION_LOT, LOT_SIZE, START_PRODUCT_ID, SEND_STATUS, STAMPED_COUNT, HOLD_STATUS, SENT_TIMESTAMP, NEXT_PRODUCTION_LOT, LOT_NUMBER, PLANT_CODE, PROCESS_LOCATION, LINE_NO, "
			+ "PRODUCT_SPEC_CODE, CREATE_TIMESTAMP, UPDATE_TIMESTAMP, BUILD_SEQ_NOT_FIXED_FLAG, BUILD_SEQUENCE_NUMBER, PLAN_CODE, KD_LOT_NUMBER, SEQUENCE, REMAKE_FLAG, STAMPING_FLAG, "
			+ "CARRY_IN_OUT_FLAG, CARRY_IN_OUT_UNITS, MBPN, HES_COLOR, DEMAND_TYPE, ord) AS "
			+ "(SELECT b.PRODUCTION_LOT, b.LOT_SIZE, b.START_PRODUCT_ID, b.SEND_STATUS, b.STAMPED_COUNT, b.HOLD_STATUS, b.SENT_TIMESTAMP, b.NEXT_PRODUCTION_LOT, b.LOT_NUMBER, b.PLANT_CODE, b.PROCESS_LOCATION, "
			+ "b.LINE_NO, b.PRODUCT_SPEC_CODE, b.CREATE_TIMESTAMP, b.UPDATE_TIMESTAMP, b.BUILD_SEQ_NOT_FIXED_FLAG, b.BUILD_SEQUENCE_NUMBER, b.PLAN_CODE, b.KD_LOT_NUMBER, b.SEQUENCE, b.REMAKE_FLAG, b.STAMPING_FLAG, "
			+ "b.CARRY_IN_OUT_FLAG, b.CARRY_IN_OUT_UNITS, b.MBPN, b.HES_COLOR, b.DEMAND_TYPE, 0 AS ord  FROM GAL212TBX b "
			+ "WHERE b.next_production_lot IS NULL AND b.process_location = ?1 "
			+ "UNION ALL "
			+ "SELECT a.PRODUCTION_LOT, a.LOT_SIZE, a.START_PRODUCT_ID, a.SEND_STATUS, a.STAMPED_COUNT, a.HOLD_STATUS, a.SENT_TIMESTAMP, a.NEXT_PRODUCTION_LOT, a.LOT_NUMBER, a.PLANT_CODE, a.PROCESS_LOCATION, a.LINE_NO, "
			+ "a.PRODUCT_SPEC_CODE, a.CREATE_TIMESTAMP, a.UPDATE_TIMESTAMP, a.BUILD_SEQ_NOT_FIXED_FLAG, a.BUILD_SEQUENCE_NUMBER, a.PLAN_CODE, a.KD_LOT_NUMBER, a.SEQUENCE, a.REMAKE_FLAG, a.STAMPING_FLAG, "
			+ "a.CARRY_IN_OUT_FLAG, a.CARRY_IN_OUT_UNITS, a.MBPN, a.HES_COLOR, a.DEMAND_TYPE, ord + 1 AS ord   FROM GAL212TBX a, "
			+ "Sch s "
			+ "WHERE s.PRODUCTION_LOT = a.NEXT_PRODUCTION_LOT AND a.process_location = ?1"
			+ ") "
			+ "SELECT PRODUCTION_LOT, LOT_SIZE, START_PRODUCT_ID, SEND_STATUS, STAMPED_COUNT, HOLD_STATUS, SENT_TIMESTAMP, NEXT_PRODUCTION_LOT, LOT_NUMBER, PLANT_CODE, PROCESS_LOCATION, LINE_NO, "
			+ "PRODUCT_SPEC_CODE, CREATE_TIMESTAMP, UPDATE_TIMESTAMP, BUILD_SEQ_NOT_FIXED_FLAG, BUILD_SEQUENCE_NUMBER, PLAN_CODE, KD_LOT_NUMBER, SEQUENCE, REMAKE_FLAG, STAMPING_FLAG, "
			+ "CARRY_IN_OUT_FLAG, CARRY_IN_OUT_UNITS, MBPN, HES_COLOR, DEMAND_TYPE, ord  FROM SCH "
			+ "ORDER BY ord DESC, HOLD_STATUS DESC";

	private static final String FIND_ALL_NOT_MATCH_KNUCKLE = "SELECT * FROM GALADM.GAL212TBX a "
			+ "WHERE a.SEND_STATUS = 0 AND a.PROCESS_LOCATION ='KN'"
			+ "AND EXISTS("
			+ "SELECT b.PRODUCT_ID from GALADM.SUB_PRODUCT_TBX b, GALADM.GAL259TBX c "
			+ "WHERE a.PRODUCTION_LOT = b.PRODUCTION_LOT and  b.SUB_ID =?1 AND c.ATTRIBUTE = ?2"
			+ "AND (c.PRODUCT_SPEC_CODE = SUBSTR(b.PRODUCT_SPEC_CODE,1,9) or c.PRODUCT_SPEC_CODE = SUBSTR(b.PRODUCT_SPEC_CODE,1,4)) "
			+ "AND SUBSTR(b.PRODUCT_ID,1,10)  NOT IN("
			+ "SELECT d.ATTRIBUTE_VALUE FROM GALADM.GAL259TBX d "
			+ "WHERE d.ATTRIBUTE = ?3 "
			+ "AND (d.PRODUCT_SPEC_CODE = SUBSTR(b.PRODUCT_SPEC_CODE,1,9) OR d.PRODUCT_SPEC_CODE = SUBSTR(b.PRODUCT_SPEC_CODE,1,4))))";

	private static final String FIND_NEXT_WELD_ON_PRODUCT_USING_SEND_STATUS = "WITH ID_ORDERED (PRODUCTION_LOT, NEXT_PRODUCTION_LOT, ID_SEQ) AS "
			+ "(SELECT ID_START_PRE_PROD_LOT.PRODUCTION_LOT, ID_START_PRE_PROD_LOT.NEXT_PRODUCTION_LOT, 1 AS ID_SEQ FROM GALADM.GAL212TBX ID_START_PRE_PROD_LOT "
			+ "WHERE ID_START_PRE_PROD_LOT.NEXT_PRODUCTION_LOT IS NULL AND ID_START_PRE_PROD_LOT.HOLD_STATUS = 1 UNION ALL SELECT ID_NEXT_PRE_PROD_LOT.PRODUCTION_LOT, ID_NEXT_PRE_PROD_LOT.NEXT_PRODUCTION_LOT,"
			+ "S.ID_SEQ + 1 AS ID_SEQ FROM GALADM.GAL212TBX ID_NEXT_PRE_PROD_LOT, ID_ORDERED AS S WHERE ID_NEXT_PRE_PROD_LOT.NEXT_PRODUCTION_LOT = S.PRODUCTION_LOT AND ID_NEXT_PRE_PROD_LOT.HOLD_STATUS = 1) "
			+ "SELECT PROD_STAMP_SEQ.PRODUCT_ID FROM GALADM.GAL216TBX PROD_STAMP_SEQ INNER JOIN ID_ORDERED ON PROD_STAMP_SEQ.PRODUCTION_LOT = ID_ORDERED.PRODUCTION_LOT "
			+ "INNER JOIN GALADM.GAL143TBX FRAME ON PROD_STAMP_SEQ.PRODUCT_ID = FRAME.PRODUCT_ID INNER JOIN GALADM.GAL144TBX FRAME_MTOC ON FRAME.PRODUCT_SPEC_CODE = FRAME_MTOC.PRODUCT_SPEC_CODE "
			+ "WHERE FRAME_MTOC.MODEL_CODE NOT IN (SELECT P.PROPERTY_VALUE FROM GALADM.GAL489TBX P WHERE p.COMPONENT_ID = 'VIN_STAMP_DASH' AND p.PROPERTY_KEY LIKE 'MODELS_MAP%') "
			+ "AND PROD_STAMP_SEQ.SEND_STATUS <= ?1 ORDER BY ID_SEQ DESC, PROD_STAMP_SEQ.STAMPING_SEQUENCE_NO FETCH FIRST 1 ROW ONLY WITH CS FOR READ ONLY";

	private static final String FIND_NEXT_WELD_ON_PRODUCT_MASTER_QRY = "WITH NEXT_PRODUCT (PRODUCTION_LOT, NEXT_PRODUCTION_LOT, ID_SEQ, START_SEQ, INPUT_PRODUCT, ERROR_CODE_INIT) "
			+ "AS (SELECT START_PRE_PROD_LOT.PRODUCTION_LOT, START_PRE_PROD_LOT.NEXT_PRODUCTION_LOT, 1 AS ID_SEQ, 100 + COALESCE(ID_START_PROD_STAMP_SEQ.STAMPING_SEQUENCE_NO,0)"
			+ // ALWAYS RETURN EVEN IF NOT A VALID PRODUCT
			",INPUT.INPUT_PRODUCT, CASE WHEN FRAME.PRODUCT_ID IS NULL THEN 1 WHEN MTOC.PRODUCT_SPEC_CODE IS NULL THEN 1 "
			+ // MAYBE CREATE ANOTHER ERROR CODE BUT VERY UNLIKELY AND REALLY IS
			// INVALID VIN
			"WHEN ID_START_PROD_STAMP_SEQ.PRODUCT_ID IS NULL THEN 2 ELSE NULL END AS ERROR_CODE_INIT FROM (SELECT @LAST_VIN@ "
			+ // input product
			"AS INPUT_PRODUCT FROM SYSIBM.SYSDUMMY1) AS INPUT "
			+ // USE SYSDUMMY1 SO THAT WE CAN ALWAYS RETURN THE INPUT_PRODUCT
			// AND REFERENCE IN JOINS
			"LEFT JOIN GALADM.GAL143TBX FRAME ON INPUT.INPUT_PRODUCT=FRAME.PRODUCT_ID LEFT JOIN GALADM.GAL144TBX MTOC ON FRAME.PRODUCT_SPEC_CODE=MTOC.PRODUCT_SPEC_CODE "
			+ "LEFT JOIN (GALADM.GAL212TBX START_PRE_PROD_LOT INNER JOIN GALADM.GAL216TBX ID_START_PROD_STAMP_SEQ ON START_PRE_PROD_LOT.PRODUCTION_LOT = ID_START_PROD_STAMP_SEQ.PRODUCTION_LOT) "
			+ "ON FRAME.PRODUCT_ID=ID_START_PROD_STAMP_SEQ.PRODUCT_ID UNION ALL "
			+ "SELECT NEXT_PRE_PROD_LOT.PRODUCTION_LOT, NEXT_PRE_PROD_LOT.NEXT_PRODUCTION_LOT, N.ID_SEQ + 1 AS ID_SEQ, N.START_SEQ, N.INPUT_PRODUCT, N.ERROR_CODE_INIT "
			+ "FROM GALADM.GAL212TBX NEXT_PRE_PROD_LOT, NEXT_PRODUCT AS N WHERE NEXT_PRE_PROD_LOT.PRODUCTION_LOT = N.NEXT_PRODUCTION_LOT), "
			+ // CREATE ANOTHER TEMP TABLE TO CALCULATE NEXT EXPECTED
			"NEXT_EXPECTED (PRODUCTION_LOT, NEXT_PRODUCTION_LOT, ID_SEQ) AS "
			+ "(SELECT START_PRE_PROD_LOT.PRODUCTION_LOT, START_PRE_PROD_LOT.NEXT_PRODUCTION_LOT, 1 AS ID_SEQ FROM GALADM.GAL212TBX START_PRE_PROD_LOT "
			+ "WHERE START_PRE_PROD_LOT.NEXT_PRODUCTION_LOT IS NULL AND START_PRE_PROD_LOT.HOLD_STATUS = 1 "
			+ // START AT END OF LIST AND GO BACK
			"UNION ALL SELECT NEXT_PRE_PROD_LOT.PRODUCTION_LOT, NEXT_PRE_PROD_LOT.NEXT_PRODUCTION_LOT, N.ID_SEQ + 1 AS ID_SEQ FROM GALADM.GAL212TBX NEXT_PRE_PROD_LOT, NEXT_EXPECTED AS N "
			+ "WHERE NEXT_PRE_PROD_LOT.NEXT_PRODUCTION_LOT = N.PRODUCTION_LOT AND NEXT_PRE_PROD_LOT.HOLD_STATUS = 1) SELECT NEXT_PRODUCT.INPUT_PRODUCT, NEXT_SEQ.PRODUCT_ID AS NEXT_PRODUCT_ID, NEXT_SEND.NEXT_EXPECTED_PRODUCT "
			+ ",CASE WHEN NEXT_PRODUCT.ERROR_CODE_INIT>0 THEN NEXT_PRODUCT.ERROR_CODE_INIT "
			+ "WHEN NEXT_SEND.NEXT_EXPECTED_PRODUCT=NEXT_SEQ.PRODUCT_ID THEN 0 "
			+ // NORMAL CASE "NO ERROR" MATCHES NEXT EXPECTED
			"WHEN NEXT_SEQ.PRODUCT_ID IS NULL THEN 3 "
			+ // NO MORE VINS IN SCHEDULE
			"WHEN NEXT_SEQ.SEND_STATUS = 2 THEN 4 "
			+ // NEXT VIN HAS ALREADY BEEN STAMPED
			"WHEN NEXT_SEQ.SEND_STATUS = 1 AND @SEND_STATUS@ = 0 THEN 5 "
			+ // NEXT VIN HAS ALREADY BEEN SENT
			"WHEN NEXT_SEND.NEXT_EXPECTED_PRODUCT<>NEXT_SEQ.PRODUCT_ID THEN 6 "
			+ // NEXT VIN DOES NOT MATCH EXPECTED SO VINS HAVE BEEN SKIPPED
			"ELSE NULL "
			+ // ERROR_CODE NOT DEFINED
			"END AS ERROR_CODE FROM NEXT_PRODUCT LEFT JOIN (SELECT PROD_STAMP_SEQ.PRODUCT_ID AS NEXT_EXPECTED_PRODUCT "
			+ "FROM NEXT_EXPECTED INNER JOIN GALADM.GAL216TBX PROD_STAMP_SEQ ON PROD_STAMP_SEQ.PRODUCTION_LOT = NEXT_EXPECTED.PRODUCTION_LOT "
			+ "INNER JOIN GALADM.GAL143TBX FRAME ON PROD_STAMP_SEQ.PRODUCT_ID = FRAME.PRODUCT_ID INNER JOIN GALADM.GAL144TBX FRAME_MTOC "
			+ "ON FRAME.PRODUCT_SPEC_CODE = FRAME_MTOC.PRODUCT_SPEC_CODE WHERE FRAME_MTOC.MODEL_CODE NOT IN "
			+ // EXLUDE ANY VINS THAT SHOULD ONLY BE STAMPED ON DASH
			"(SELECT P.PROPERTY_VALUE FROM GALADM.GAL489TBX P WHERE P.COMPONENT_ID = 'VIN_STAMP_DASH' AND P.PROPERTY_KEY LIKE 'MODELS_MAP%') "
			+ "AND PROD_STAMP_SEQ.SEND_STATUS <= @SEND_STATUS@ "
			+ // 0 for schedule request and 1 for confirmation
			"ORDER BY ID_SEQ DESC, PROD_STAMP_SEQ.STAMPING_SEQUENCE_NO "
			+ // ORDER BY PRODUCTION LOT STAMP SEQUENCE BUT DESC SINCE STARTING
			// AT END OF LIST
			"FETCH FIRST 1 ROW ONLY) AS NEXT_SEND ON 1=1 "
			+ // ONLY RETURN THE NEXT EXPECTED PRODUCT ACCORDING TO STAMP
			// SEQUENCE NOT SENT AND USED LEFT JOIN SO EVEN IF NO RECORD IT
			// WILL STILL RETURN A ROW AND THIS REQUIRES AN ON SO JUST USE
			// 1=1 SINCE NO VALID WAY TO JOIN
			"LEFT JOIN (SELECT PROD_STAMP_SEQ.PRODUCTION_LOT, PROD_STAMP_SEQ.PRODUCT_ID,PROD_STAMP_SEQ.SEND_STATUS "
			+ "FROM NEXT_PRODUCT INNER JOIN GALADM.GAL216TBX PROD_STAMP_SEQ ON PROD_STAMP_SEQ.PRODUCTION_LOT = NEXT_PRODUCT.PRODUCTION_LOT "
			+ "INNER JOIN GALADM.GAL143TBX FRAME ON PROD_STAMP_SEQ.PRODUCT_ID = FRAME.PRODUCT_ID INNER JOIN GALADM.GAL144TBX FRAME_MTOC "
			+ "ON FRAME.PRODUCT_SPEC_CODE = FRAME_MTOC.PRODUCT_SPEC_CODE WHERE ID_SEQ * 100 + PROD_STAMP_SEQ.STAMPING_SEQUENCE_NO > NEXT_PRODUCT.START_SEQ "
			+ // ONLY RETURN PRODUCTS AFTER THE INPUT_PRODUCT
			"AND FRAME_MTOC.MODEL_CODE NOT IN "
			+ // EXLUDE ANY VINS THAT SHOULD ONLY BE STAMPED ON DASH
			"(SELECT P.PROPERTY_VALUE FROM GALADM.GAL489TBX P WHERE P.COMPONENT_ID = 'VIN_STAMP_DASH' AND P.PROPERTY_KEY LIKE 'MODELS_MAP%') "
			+ "ORDER BY ID_SEQ, PROD_STAMP_SEQ.STAMPING_SEQUENCE_NO " + // ORDER
			// BY
			// PRODUCTION
			// LOT
			// STAMP
			// SEQUENCE
			"FETCH FIRST 1 ROW ONLY) AS NEXT_SEQ ON 1=1 " + // ONLY RETURN THE
			// NEXT PRODUCT
			// ACCORDING TO
			// STAMP SEQUENCE
			// AND USED LEFT
			// JOIN SO EVEN IF
			// NO RECORD IT WILL
			// STILL RETURN A
			// ROW AND THIS
			// REQUIRES AN ON SO
			// JUST USE 1=1
			// SINCE NO VALID
			// WAY TO JOIN
			"WHERE NEXT_PRODUCT.ID_SEQ=1 WITH CS FOR READ ONLY"; // ONLY RETURN
	// THE 1ST
	// ROW SO WE
	// CAN
	// ALWAYS
	// GET THE
	// INPUT_PRODUCT

	private static int KSN_DIGITS = 6;
	private static String FIND_UPCOMING_PREPRODUCTION_LOTS = "WITH SortedList (PRODUCTION_LOT, NEXT_PRODUCTION_LOT, Level) "
			+ "AS( "
			+ "SELECT PRODUCTION_LOT, NEXT_PRODUCTION_LOT, 0 as Level "
			+ "FROM GALADM.GAL212TBX "
			+ "WHERE NEXT_PRODUCTION_LOT IS NULL AND HOLD_STATUS = 1 "
			+ "UNION ALL "
			+ "SELECT ll.PRODUCTION_LOT, ll.NEXT_PRODUCTION_LOT,Level+1 as Level "
			+ "FROM GALADM.GAL212tbx ll, SortedList as s "
			+ "WHERE ll.NEXT_PRODUCTION_LOT = s.PRODUCTION_LOT and (ll.SEND_STATUS != 2) "
			+ ") "
			+ "SELECT b.* "
			+ "FROM SortedList a, GALADM.GAL212TBX b "
			+ "WHERE a.PRODUCTION_LOT = b.PRODUCTION_LOT "
			+ "ORDER BY Level desc ";
	private static final String FIND_DISTINCT_LINES = "SELECT Distinct T1.Line_No From GAL212TBX T1 WHERE T1.Plant_Code = ?1 AND T1.PROCESS_LOCATION = ?2 ORDER BY T1.Line_No";
	private static final String FIND_DISTINCT_PLANT_CODES = "SELECT DISTINCT T1.Plant_Code From GAL212TBX T1, GAL237TBX T2 WHERE T1.Plant_Code = T2.GPCS_Plant_Code GROUP BY T2.Plant_Name,T1.Plant_Code ORDER BY T1.Plant_Code";
	private static final String FIND_LOT_BY_NEXT_PRODUCTION_LOT = "SELECT * From GAL212TBX T1 WHERE T1.Next_Production_Lot = ?1  AND T1.Send_Status < 2";
	private static final String FIND_LOT_BY_SEQ = "SELECT * From GAL212TBX T1 WHERE T1.SEQUENCE > ?1  AND T1.Send_Status < 2 order by T1.SEQUENCE fetch first 1 rows only";
	private static final String FIND_NEXT_PRODUCTION_LOT_BY_LOT = "SELECT * From GAL212TBX T1 WHERE T1.Send_Status < 2 AND T1.PRODUCTION_LOT = (Select Next_Production_Lot From GAL212TBX T2 WHERE T2.Production_Lot= ?1)";
	private static final String FIND_LOTS_ON_HOLD = "SELECT * From GAL212TBX T1 WHERE T1.PROCESS_LOCATION = ?1 AND T1.Plant_Code = ?2 AND T1.Line_No = ?3  AND ((T1.Hold_Status = 0 ) OR (T1.Hold_Status = 2))";

	//	private static String FIND_LAST_CHANGE_TIMESTAMP = "WITH CHANGED_TIME (TIMESTAMP) AS("
	//			+ "SELECT MAX(CREATE_TIMESTAMP) AS TIMESTAMP from gal212tbx where SEND_STATUS = 0 and PROCESS_LOCATION =?1 "
	//			+ "UNION ALL "
	//			+ "SELECT MAX(UPDATE_TIMESTAMP) AS TIMESTAMP from gal212tbx where SEND_STATUS = 0 and PROCESS_LOCATION =?1 "
	//			+ ")" + "SELECT MAX(TIMESTAMP) FROM CHANGED_TIME";

	private static final String FIND_LAST_STAMPED_LOT = "select max(production_lot) from gal212tbx where send_status = 2 and process_location = ?1  and sent_timestamp = (select max(sent_timestamp) from gal212tbx where send_status =2 and process_location = ?1) for read only";

	private static final String FIND_SPLIT_LOT = "SELECT PRODUCTION_LOT FROM GAL217TBX WHERE PROD_LOT_KD=?1 AND PRODUCTION_LOT<>PROD_LOT_KD";

	private static final String UPDATE_NEXT_LOT_HOLD_STATUS = "UPDATE GAL212TBX Set Next_Production_Lot = ?1 , Hold_Status = ?2 WHERE Production_Lot = ?3 ";

	private static String FIND_NON_PROCESSED_LOTS = "SELECT a.* FROM GALADM.GAL212TBX a "
			+ "WHERE a.SEND_STATUS = 0 AND a.PROCESS_LOCATION= ?1 AND a.LOT_SIZE * 2 = "
			+ "(SELECT COUNT(*) FROM GALADM.SUB_PRODUCT_TBX "
			+ "WHERE PRODUCTION_LOT = a.PRODUCTION_LOT AND LAST_PASSING_PROCESS_POINT_ID= ?2)";

	private static final String FIND_LAST_LOT = "SELECT ppl FROM PreProductionLot ppl WHERE ppl.productionLot = " +
			" (SELECT MAX(ppl.productionLot) FROM PreProductionLot ppl " +
			"WHERE ppl.nextProductionLot = :nextProductionLot " +
			"AND ppl.processLocation = :processLocation " +
			"AND ppl.holdStatus = :holdStatus " +
			"AND substr(ppl.productionLot, 1, 6) = :planCode6) ";

	private static final String FIND_INCOMPLETE_CHILD_LOTS = "select *from gal212tbx where plan_code = ?1 and process_location = ?2 and send_status = ?3 and kd_lot_number not in (select kd_lot_number from gal212tbx where plan_code = ?4 and process_location = ?5)";

	private static final String FIND_REPLICATED_LOT = "select * from gal212tbx " +
            " where plan_code = ?1 " +
	           " and process_location = ?2 " +
            " and SEND_STATUS = 0 " +
            " and ((create_timestamp >= ?3) OR (update_timestamp >= ?4))";
	
	private static final String FIND_BY_PRC_LOC_AND_LOT_NUMBER =" Select * from gal212tbx where process_location = ?1 and LOT_NUMBER = ?2 order by SEQUENCE";
	
	// error messages
	private static final String REQUESTED_NO_OF_LOTS_UNAVAILABLE_CODE = "40";
	private static final String REQUESTED_NO_OF_LOTS_UNAVAILABLE_MSG = "Requested no. of lots not available.";
	private static final String RECV_PROD_DOES_NOT_MATCH_EXP_PROD_CODE = "41";
	private static final String RECV_PROD_DOES_NOT_MATCH_EXP_PROD_MSG = "Received product id does not match expected product id.";

	private static final String FIND_CURRENT_BLOCK_LOAD_LOT =
			"SELECT * FROM GALADM.GAL212TBX a WHERE a.PRODUCTION_LOT IN ( " +
					"SELECT PRODUCTION_LOT FROM GALADM.BLOCK_LOAD_TBX WHERE STATUS<'9' GROUP BY PRODUCTION_LOT) AND NOT EXISTS( " +
					"SELECT * FROM GALADM.BLOCK_LOAD_TBX WHERE a.NEXT_PRODUCTION_LOT = PRODUCTION_LOT)";

	private static final String FIND_FIRST_AVAILABLE_LOT =
			"SELECT * FROM GALADM.gal212tbx a WHERE a.PROCESS_LOCATION = ?1 AND a.SEND_STATUS = 0 AND a.HOLD_STATUS = 1" +
					" AND (NOT EXISTS(SELECT * FROM GALADM.GAL212TBX WHERE NEXT_PRODUCTION_LOT = a.PRODUCTION_LOT)" +
					" OR EXISTS (SELECT PRODUCTION_LOT from galadm.gal212tbx where PROCESS_LOCATION = ?1 AND NEXT_PRODUCTION_LOT = a.PRODUCTION_LOT and SEND_STATUS > 0 ))";

	private static final String FIND_ALL_FOR_ALL_PLAN_CODE = "SELECT * FROM GALADM.GAL212TBX WHERE PLAN_CODE IS NOT NULL";
	private static final String FIND_ALL_FOR_PLAN_CODES = "select e from PreProductionLot e where e.planCode in (:planCodes)";
	private static final String FIND_CURRENT_LOT = "select e from PreProductionLot e where  e.planCode in (:planCode) and e.sendStatusId = :sendStatusId and e.holdStatus = :holdStatus order by e.sequence asc";
	private static final String FIND_UPCOMING_LOT = "select e from PreProductionLot e where  e.planCode in (:planCodes) and e.sendStatusId in (:sendStatusIds) and e.holdStatus = :holdStatus order by e.sequence asc";
	private static final String FIND_LAST_UPDATE_TIMESTAMP = "select e from PreProductionLot e where e.planCode in (:planCode) and e.sendStatusId = :sendStatusId order by e.updateTimestamp desc";

	private static final String FIND_ALL_PLAN_CODE = "select distinct plan_code from galadm.gal212tbx where plan_code is not null";

	private static final String FIND_NEXT_ORDER_BY_LAST_ORDER_AND_PLAN_CODE_SQL = "select o from PreProductionLot o where (o.sendStatusId = "
			+ PreProductionLotSendStatus.WAITING.getId() + " or o.sendStatusId = " + PreProductionLotSendStatus.SENT.getId() + ") and o.planCode = :planCode and o.sequence > "
			+ "(select o2.sequence from PreProductionLot o2 where o2.productionLot = :orderNo and o2.planCode = :planCode) order by o.sequence";

	private static final String FIND_NEXT_ORDER_BY_LAST_LOT_NUMBER_AND_PLAN_CODE_SQL = "select o from PreProductionLot o where (o.sendStatusId = "
			+ PreProductionLotSendStatus.WAITING.getId() + " or o.sendStatusId = " + PreProductionLotSendStatus.SENT.getId() + ") and o.planCode = :planCode and o.sequence > "
			+ "(select o2.sequence from PreProductionLot o2 where o2.lotNumber = :lotNumber and o2.planCode = :planCode) order by o.sequence";

	private static final String FIND_BY_LOT_NUMBER_AND_SPEC_CODE = "select * from gal212tbx where LOT_NUMBER like '%@LOT_NUMBER@%' and PRODUCT_SPEC_CODE like '%@PROD_SPEC_CODE@%' and process_location = '@DEST_PROC_LOC@'";
	
	private static final String FIND_BY_LOT_NUMBER = "select * from gal212tbx where LOT_NUMBER like '%@LOT_NUMBER@%' and  PRODUCTION_LOT != '@PARENT_PROD_LOT_NUMBER@' order by SEND_STATUS desc";

	//Query used for get information about the production report
	private static final String TM_PRODUCTION_RESULT = new StringBuilder()
	.append("select g212.PLAN_CODE ")
	.append("	,g212.MBPN as MFG_BASIC_PART_NO ")
	.append("	,g212.HES_COLOR as PART_CLR_CD ")
	.append("	,LPAD(count(distinct g215.PRODUCT_ID),5,'0') as PROD_QTY   ")
	.append("from gal215tbx g215 join gal212tbx g212 on g215.PRODUCT_SPEC_CODE = g212.PRODUCT_SPEC_CODE ")
	.append("where ")
	.append("	g215.PROCESS_POINT_ID in (@PROCESS_POINTS@) and  ")
	.append("	g215.ACTUAL_TIMESTAMP > ?1 and ")//'2014-10-01 10:20:00' ")
	.append("	g212.PLANT_CODE = ?2 ")
	.append("group by PLAN_CODE, MBPN, HES_COLOR ")
	.toString();

	private static final String UNMAPPED_ORDER_IDS = "SELECT PROD_LOT.PRODUCTION_LOT  FROM    GALADM.GAL212TBX PROD_LOT       LEFT JOIN          GALADM.MC_ORDER_STRUCTURE_TBX ORD_STUR" +
			"       ON (PROD_LOT.PRODUCTION_LOT = ORD_STUR.ORDER_NO)  WHERE ORD_STUR.ORDER_NO IS NULL GROUP BY PROD_LOT.PRODUCTION_LOT, ORD_STUR.ORDER_NO";

	private static final String FIND_PARENT_ORDER = "WITH X (PRODUCTION_LOT,NEXT_PRODUCTION_LOT,LOT_SIZE,SEND_STATUS,STAMPED_COUNT,PRODUCT_SPEC_CODE,PLAN_CODE,LINE_PRODUCT_SEQ,CREATE_TIMESTAMP,UPDATE_TIMESTAMP)\n" + 
			"    AS (SELECT ROOT.PRODUCTION_LOT,\n" + 
			"        ROOT.NEXT_PRODUCTION_LOT,\n" + 
			"        ROOT.LOT_SIZE,\n" + 
			"        ROOT.SEND_STATUS,\n" + 
			"        ROOT.STAMPED_COUNT,\n" + 
			"        ROOT.PRODUCT_SPEC_CODE,\n" + 
			"        ROOT.PLAN_CODE,\n" + 
			"        1 AS LINE_PRODUCT_SEQ,\n" + 
			"        ROOT.CREATE_TIMESTAMP,\n" + 
			"        ROOT.UPDATE_TIMESTAMP\n" + 
			"        FROM GAL212TBX ROOT\n" + 
			"        WHERE ROOT.NEXT_PRODUCTION_LOT IS NULL\n" + 
			"            AND ROOT.PLAN_CODE = ?1\n" + 
			"            AND ROOT.PROCESS_LOCATION = ?2\n" + 
			"        UNION ALL\n" + 
			"        SELECT SUB.PRODUCTION_LOT,\n" + 
			"        SUB.NEXT_PRODUCTION_LOT,\n" + 
			"        SUB.LOT_SIZE,\n" + 
			"        SUB.SEND_STATUS,\n" + 
			"        SUB.STAMPED_COUNT,\n" + 
			"        SUB.PRODUCT_SPEC_CODE,\n" + 
			"        SUB.PLAN_CODE,\n" + 
			"        SUPER.LINE_PRODUCT_SEQ + 1 AS LINE_PRODUCT_SEQ,\n" + 
			"        SUB.CREATE_TIMESTAMP,\n" + 
			"        SUB.UPDATE_TIMESTAMP\n" + 
			"        FROM GAL212TBX SUB, X SUPER\n" + 
			"        WHERE SUB.NEXT_PRODUCTION_LOT = SUPER.PRODUCTION_LOT)\n" + 
			"SELECT X.PRODUCTION_LOT,\n" + 
			"X.NEXT_PRODUCTION_LOT,\n" + 
			"A.KD_LOT_NUMBER,\n" + 
			"A.PRODUCTION_DATE,\n" + 
			"X.LOT_SIZE,\n" + 
			"X.PRODUCT_SPEC_CODE,\n" + 
			"X.PLAN_CODE,\n" + 
			"X.LINE_PRODUCT_SEQ\n" + 
			"FROM X\n" + 
			"    INNER JOIN GAL217TBX A ON A.PRODUCTION_LOT = X.PRODUCTION_LOT\n" + 
			"WHERE A.KD_LOT_NUMBER in (select kd_lot_number from gal212tbx where plan_code = ?3 and process_location = ?4 and send_status = 0)\n" + 
			"ORDER BY X.PLAN_CODE, X.LINE_PRODUCT_SEQ DESC\n" + 
			"WITH CS\n" + 
			"FOR READ ONLY\n";
	
	private static final String FIND_DUPLICATE_SCHEDULE = "select production_lot, sequence, send_status, hold_status, next_production_lot\n" + 
			"from gal212tbx\n" + 
			"where plan_code = ?1\n" + 
			"        and process_location = ?2\n" + 
			"        and (sequence in (select sequence from gal212tbx where plan_code = ?1 and process_location = ?2 and send_status <> 2 group by sequence having count(sequence) > 1)\n" + 
			"                or next_production_lot in (select next_production_lot from gal212tbx where plan_code = ?1 and process_location = ?2 and send_status <> 2 group by next_production_lot having count(next_production_lot) > 1))\n" + 
			"order by sequence, production_lot";

	private static final String ALL_NEW_ENGINE_SHIPPING_LOTS =
			"WITH SortedList (PRODUCTION_LOT, NEXT_PRODUCTION_LOT, LOT_SIZE,Level) " +
					"AS ( " +
					"	SELECT PRODUCTION_LOT, NEXT_PRODUCTION_LOT, LOT_SIZE,0 as Level " +
					"	FROM GALADM.GAL212tbx " +
					"	WHERE PRODUCTION_LOT = ( " +
					"       SELECT MAX(PRODUCTION_LOT) " +
					"       FROM GALADM.VANNING_SCHEDULE_TBX " +
					"       WHERE TRAILER_ID = (SELECT MAX(TRAILER_ID) FROM GALADM.TRAILER_INFO_TBX where STATUS >=4 ) " +
					"   ) " +
					"   UNION ALL " +
					"   SELECT ll.PRODUCTION_LOT, ll.NEXT_PRODUCTION_LOT, ll.LOT_SIZE,Level+1 as Level "  +
					"   FROM GALADM.GAL212tbx ll, SortedList as s " +
					"   WHERE ll.PRODUCTION_LOT = s.NEXT_PRODUCTION_LOT " +
					")" +
					"SELECT a.PRODUCTION_LOT, a.LOT_SIZE,b.KD_LOT_NUMBER,b.PRODUCT_SPEC_CODE,b.PLAN_OFF_DATE " +
					"FROM SortedList a, GALADM.GAL217TBX b " +
					"WHERE a.PRODUCTION_LOT = b.PRODUCTION_LOT AND " +
					"    not EXISTS " +
					"        (SELECT PRODUCTION_LOT " +
					"         FROM GALADM.VANNING_SCHEDULE_TBX " +
					"         WHERE PRODUCTION_LOT = a.PRODUCTION_LOT ) " +
					"ORDER BY Level";

	private static final String ALL_UPCOMING_LOTS =
			"WITH SortedList (PRODUCTION_LOT,Level) " +
					"AS (" +
					"	SELECT PRODUCTION_LOT,0 as Level " +
					"	FROM GALADM.GAL212tbx a " +
					"	WHERE HOLD_STATUS = 1 AND (SEND_STATUS = 0 OR SEND_STATUS = 1) AND PROCESS_LOCATION =?1 AND NEXT_PRODUCTION_LOT IS NULL " +
					"	UNION ALL"+
					"	SELECT ll.PRODUCTION_LOT,Level+1 as Level "+
					"	FROM GALADM.GAL212tbx ll, SortedList as s "+
					"	WHERE ll.NEXT_PRODUCTION_LOT = s.PRODUCTION_LOT and (ll.SEND_STATUS = 0 OR ll.SEND_STATUS = 1)" +
					")"+
					"SELECT a.*" +
					"FROM GALADM.GAL212TBX a, SortedList b " +
					"WHERE a.PRODUCTION_LOT = b.PRODUCTION_LOT " +
					"ORDER BY b.Level desc";

	private static final String ALL_PREVIOUS_LOTS =
			"WITH SortedList (PRODUCTION_LOT,Level) " +
					"AS (" +
					"	SELECT PRODUCTION_LOT,0 as Level " +
					"	FROM GALADM.GAL212tbx a " +
					"	WHERE NEXT_PRODUCTION_LOT = ?1 " +
					"	UNION ALL"+
					"	SELECT ll.PRODUCTION_LOT,Level+1 as Level "+
					"	FROM GALADM.GAL212tbx ll, SortedList as s "+
					"	WHERE ll.NEXT_PRODUCTION_LOT = s.PRODUCTION_LOT AND Level < ?2" +
					")"+
					"SELECT a.*" +
					"FROM GALADM.GAL212TBX a, SortedList b " +
					"WHERE a.PRODUCTION_LOT = b.PRODUCTION_LOT " +
					"ORDER BY b.Level desc";

	private static final String UPCOMING_LOTS =
			"WITH recursiveBOM " +
					"( PRODUCTION_LOT, NEXT_PRODUCTION_LOT, LOT_NUMBER,PRODUCT_SPEC_CODE,KD_LOT_NUMBER,LOT_SIZE ) " +
					"AS ( " +
					"   SELECT " +
					"   parent.PRODUCTION_LOT, parent.NEXT_PRODUCTION_LOT,parent.LOT_NUMBER,parent.PRODUCT_SPEC_CODE,parent.KD_LOT_NUMBER,parent.LOT_SIZE " +
					"   FROM GAL212TBX parent " +
					"   WHERE PRODUCTION_LOT=?1 " +
					"   UNION  ALL " +
					"   SELECT " +
					"   child.PRODUCTION_LOT, child.NEXT_PRODUCTION_LOT, child.LOT_NUMBER,child.PRODUCT_SPEC_CODE,child.KD_LOT_NUMBER,child.LOT_SIZE " +
					"   from recursiveBOM parent, gal212tbx child " +
					"   where parent.NEXT_PRODUCTION_LOT = child.PRODUCTION_LOT " +
					") " +
					"Select " +
					"	COALESCE('%s','') || ',' || COALESCE(VARCHAR(parent.LOT_NUMBER),'') || ',' || " +
					"	COALESCE(VARCHAR(parent.KD_LOT_NUMBER),'') || ',' || COALESCE(VARCHAR(parent.PRODUCT_SPEC_CODE),'') || ',' || " +
					"	COALESCE(VARCHAR(parent.LOT_SIZE),'') " +
					"FROM recursiveBOM parent ";


	private static final String FIND_DISTINCT_SPEC_CODES_FOR_PLAN_CODE = "Select DISTINCT(e.PRODUCT_SPEC_CODE) from GALADM.GAL212TBX e where e.PLAN_CODE in (?1)";

	private static final String FIND_ALL_FOR_PLAN_CODES_ORDER_BY_SEQNUM = "select e from PreProductionLot e where e.planCode = :planCode order by e.sequence asc";

	private static final String CHANGE_212_TO_UNSENT = "update PreProductionLot ppl set ppl.sendStatusId = 0, ppl.sentTimestamp = null where ppl.productionLot = :productionLot " +
			"and ppl.sendStatusId = 1 and ppl.holdStatus = 1 and ppl.stampedCount = 0 and ppl.processLocation = :processLocation";

	private static final String CHANGE_216_TO_UNSENT = "update ProductStampingSequence seq set seq.sendStatus = 0 "
			+ "where exists ( select sch from PreProductionLot sch where sch.productionLot = :productionLot "
			+ "and sch.sendStatusId = 1 and  sch.holdStatus = 1 and sch.stampedCount = 0 and sch.processLocation = :processLocation) "
			+ "and seq.id.productionLot = :productionLot and seq.sendStatus = 1";

	private static final String CHANGE_212_TO_UNSENT_BY_PROCESS_LOCATION = "update PreProductionLot ppl set ppl.sendStatusId = 0, ppl.sentTimestamp = null where " +
			" ppl.sendStatusId = 1 and ppl.holdStatus = 1 and ppl.processLocation = :processLocation and ppl.planCode= :planCode ";

	private static final String CHANGE_216_TO_UNSENT_BY_PROCESS_LOCATION = "update ProductStampingSequence seq set seq.sendStatus = 0 "
			+ "where exists ( select sch from PreProductionLot sch where "
			+ "sch.sendStatusId = 1 and  sch.holdStatus = 1 and sch.processLocation = :processLocation and sch.planCode= :planCode ) "
			+ "and seq.sendStatus = 1";

	private static final String IS_LOT_PREFIX_EXIST = "select e from PreProductionLot e where e.productionLot like :lotPrefix";

	private static final String IS_LAST_PRODUCTION_DATE = "select e from PreProductionLot e where e.productionLot > :maxLotDate and e.productionLot <= :maxLot and e.productionLot like :lotPrefix";

	private static final String NEXTLOTNUMBERBYPRODUCTIONLOT =
			"select cast(max(ppl.lot_number) as varchar(12)) " +
					"from galadm.gal212tbx ppl " +
					"inner join (select lot_number, plan_code from galadm.gal212tbx where production_lot = ?1) a " +
					"   on ppl.plan_code = a.plan_code " +
					"  and ppl.lot_number between a.lot_number and concat(substr(a.lot_number, 1, 8), 9999) " +
					"group by ppl.plan_code";
	private static final String FIND_UPCOMING_FOR_PLAN_CODES_ORDER_BY_SEQNUM = "select e from PreProductionLot e where e.planCode = :planCode and e.holdStatus <> 0 and e.sendStatusId <>2 order by e.sequence asc";
	private static final String FIND_UPCOMING_FOR_PRODUCT_SPEC_CODES_ORDER_BY_SEQNUM = "select e from PreProductionLot e where e.productSpecCode = :productSpecCode and e.sendStatusId <>2 order by e.sequence asc";
	private static final String FIND_A_NUMBER_OF_PROCESSED_FOR_PLAN_CODES_ORDER_BY_SEQNUM = "select e.* from galadm.gal212tbx e where e.PLAN_CODE = ?1 and e.SEND_STATUS =2 and e.HOLD_STATUS <> 0 order by e.sequence desc fetch first ";

	private static final String FIND_PARENT_BY_SEQUENCE =
			"SELETE * FROM GALADM.GAL212TBX " +
			"WHERE SEQUENCE < (SELECT SEQUENCE FROM GALADM.GAL212TBX WHERE PRODUCTION_LOT = ?1) AND " +
            "PLAN_CODE = (SELECT PLAN_CODE FROM GALADM.GAL212TBX WHERE PRODUCTION_LOT = ?1)and HOLD_STATUS = 1 " +
			"ORDER BY SEQUENCE DESC";

	private static final String FIND_LAST_UPDATETIMESTAMP = "SELECT COALESCE(CREATE_TIMESTAMP, UPDATE_TIMESTAMP) as UPDATE_TIMESTAMP FROM GALADM.GAL212TBX " +
			"WHERE SEND_STATUS = 0 AND PROCESS_LOCATION = ?1 ORDER BY UPDATE_TIMESTAMP DESC";

	private static final String FIND_NEXT_LOT_BY_SEQUENCE_AND_PLAN_CODE = "SELECT * From GAL212TBX T1 WHERE T1.SEQUENCE > ?1 AND T1.PLAN_CODE=?2 " +
			" and T1.HOLD_STATUS="+PreProductionLotStatus.RELEASE.getId()+" "+
			" order by T1.SEQUENCE ";

	private static final String GET_KD_LOT_SIZE = "SELECT COALESCE(SUM(LOT_SIZE),0) AS LOT_SIZE,COALESCE(SUM(STAMPED_COUNT),0) AS STAMP_COUNT FROM GALADM.GAL212TBX "
			+ "WHERE KD_LOT_NUMBER LIKE ?1 AND PROCESS_LOCATION = ?2 FOR READ ONLY WITH UR";

	private static final String FIND_SCHEDULE_FOR_PLAN_CODE = "WITH Sch( PRODUCTION_LOT, LOT_SIZE, START_PRODUCT_ID, SEND_STATUS, STAMPED_COUNT, HOLD_STATUS, SENT_TIMESTAMP, NEXT_PRODUCTION_LOT, LOT_NUMBER, PLANT_CODE, PROCESS_LOCATION, LINE_NO, "
	        + "PRODUCT_SPEC_CODE, CREATE_TIMESTAMP, UPDATE_TIMESTAMP, BUILD_SEQ_NOT_FIXED_FLAG, BUILD_SEQUENCE_NUMBER, PLAN_CODE, KD_LOT_NUMBER, SEQUENCE, REMAKE_FLAG, STAMPING_FLAG, "
	        + "CARRY_IN_OUT_FLAG, CARRY_IN_OUT_UNITS, MBPN, HES_COLOR, DEMAND_TYPE, ord) AS "
	        + "(SELECT b.PRODUCTION_LOT, b.LOT_SIZE, b.START_PRODUCT_ID, b.SEND_STATUS, b.STAMPED_COUNT, b.HOLD_STATUS, b.SENT_TIMESTAMP, b.NEXT_PRODUCTION_LOT, b.LOT_NUMBER, b.PLANT_CODE, b.PROCESS_LOCATION, "
	        + "b.LINE_NO, b.PRODUCT_SPEC_CODE, b.CREATE_TIMESTAMP, b.UPDATE_TIMESTAMP, b.BUILD_SEQ_NOT_FIXED_FLAG, b.BUILD_SEQUENCE_NUMBER, b.PLAN_CODE, b.KD_LOT_NUMBER, b.SEQUENCE, b.REMAKE_FLAG, b.STAMPING_FLAG, "
	        + "b.CARRY_IN_OUT_FLAG, b.CARRY_IN_OUT_UNITS, b.MBPN, b.HES_COLOR, b.DEMAND_TYPE, 0 AS ord  FROM GAL212TBX b "
	        + "WHERE b.next_production_lot IS NULL AND b.process_location=?1 AND b.plan_code= ?2"
	        + "UNION ALL "
	        + "SELECT a.PRODUCTION_LOT, a.LOT_SIZE, a.START_PRODUCT_ID, a.SEND_STATUS, a.STAMPED_COUNT, a.HOLD_STATUS, a.SENT_TIMESTAMP, a.NEXT_PRODUCTION_LOT, a.LOT_NUMBER, a.PLANT_CODE, a.PROCESS_LOCATION, a.LINE_NO, "
	        + "a.PRODUCT_SPEC_CODE, a.CREATE_TIMESTAMP, a.UPDATE_TIMESTAMP, a.BUILD_SEQ_NOT_FIXED_FLAG, a.BUILD_SEQUENCE_NUMBER, a.PLAN_CODE, a.KD_LOT_NUMBER, a.SEQUENCE, a.REMAKE_FLAG, a.STAMPING_FLAG, "
	        + "a.CARRY_IN_OUT_FLAG, a.CARRY_IN_OUT_UNITS, a.MBPN, a.HES_COLOR, a.DEMAND_TYPE, ord + 1 AS ord   FROM GAL212TBX a, "
	        + "Sch s "
	        + "WHERE s.PRODUCTION_LOT = a.NEXT_PRODUCTION_LOT AND a.process_location=?1 and a.plan_code = ?2"
	        + ") "
	        + "SELECT PRODUCTION_LOT, LOT_SIZE, START_PRODUCT_ID, SEND_STATUS, STAMPED_COUNT, HOLD_STATUS, SENT_TIMESTAMP, NEXT_PRODUCTION_LOT, LOT_NUMBER, PLANT_CODE, PROCESS_LOCATION, LINE_NO, "
	        + "PRODUCT_SPEC_CODE, CREATE_TIMESTAMP, UPDATE_TIMESTAMP, BUILD_SEQ_NOT_FIXED_FLAG, BUILD_SEQUENCE_NUMBER, PLAN_CODE, KD_LOT_NUMBER, SEQUENCE, REMAKE_FLAG, STAMPING_FLAG, "
	        + "CARRY_IN_OUT_FLAG, CARRY_IN_OUT_UNITS, MBPN, HES_COLOR, DEMAND_TYPE, ord  FROM SCH "
	        + "ORDER BY ord DESC, HOLD_STATUS DESC";

	private static final String FIND_ALL_BY_PLAN_CODE_CREATE_DATE = "select e from PreProductionLot e where e.planCode = :planCode  and e.createTimestamp >= :createDate ";

	private final String GET_PRODUCTION_LOT_DETAILS = "select d.PRODUCTION_LOT, d.SEND_STATUS from galadm.GAL216TBX c "
			+ "join galadm.GAL212TBX d on c.PRODUCTION_LOT = d.PRODUCTION_LOT where c.PRODUCT_ID = ?1";

	private static final String UPDATE_COLOR_DETAILS_1 = "Update GALADM.GAL212TBX set PRODUCT_SPEC_CODE = ?1 where PRODUCT_SPEC_CODE = ?2 and PRODUCTION_LOT in (:productionLot)";

	private static String FIND_NON_SHIPPED_LOTS_BY_PLAN_CODE = "SELECT gal212.PRODUCTION_LOT FROM gal212tbx gal212, gal217tbx gal217 where gal212.PRODUCTION_LOT=gal217.PRODUCTION_LOT and gal212.PLAN_CODE= ?1 and gal217.LOT_STATUS <> 4";
	
	private static final String IS_MODEL_EXIST = "select e from PreProductionLot e where e.productSpecCode like :specCodePrefix";
	
	private static final String FIND_BY_PROD_DATE_AND_LINE = "select ppl from PreProductionLot ppl, ProductionLot pl where ppl.productionLot = pl.productionLot and ppl.processLocation = :processLocation and pl.productionDate = :productionDate and ppl.lineNo = :lineNo order by ppl.sequence";
	
	private static final String IS_LOT_STAMPED = "select e from PreProductionLot e where e.productionLot like :lotPrefix and e.stampedCount > 0";
	
	private static final String FIND_STARTING_PROD_LOT_CHECK = "WITH Sch( PRODUCTION_LOT, LOT_SIZE, START_PRODUCT_ID, SEND_STATUS, STAMPED_COUNT, HOLD_STATUS, SENT_TIMESTAMP, NEXT_PRODUCTION_LOT, LOT_NUMBER, PLANT_CODE, PROCESS_LOCATION, LINE_NO, "
			+ "PRODUCT_SPEC_CODE, CREATE_TIMESTAMP, UPDATE_TIMESTAMP, BUILD_SEQ_NOT_FIXED_FLAG, BUILD_SEQUENCE_NUMBER, PLAN_CODE, KD_LOT_NUMBER, SEQUENCE, REMAKE_FLAG, STAMPING_FLAG, "
			+ "CARRY_IN_OUT_FLAG, CARRY_IN_OUT_UNITS, MBPN, HES_COLOR, DEMAND_TYPE, ord) AS "
			+ "(SELECT b.PRODUCTION_LOT, b.LOT_SIZE, b.START_PRODUCT_ID, b.SEND_STATUS, b.STAMPED_COUNT, b.HOLD_STATUS, b.SENT_TIMESTAMP, b.NEXT_PRODUCTION_LOT, b.LOT_NUMBER, b.PLANT_CODE, b.PROCESS_LOCATION, "
			+ "b.LINE_NO, b.PRODUCT_SPEC_CODE, b.CREATE_TIMESTAMP, b.UPDATE_TIMESTAMP, b.BUILD_SEQ_NOT_FIXED_FLAG, b.BUILD_SEQUENCE_NUMBER, b.PLAN_CODE, b.KD_LOT_NUMBER, b.SEQUENCE, b.REMAKE_FLAG, b.STAMPING_FLAG, "
			+ "b.CARRY_IN_OUT_FLAG, b.CARRY_IN_OUT_UNITS, b.MBPN, b.HES_COLOR, b.DEMAND_TYPE, 0 AS ord  FROM GAL212TBX b "
			+ "WHERE b.next_production_lot IS NULL AND b.process_location= ?1 AND b.plan_code= ?2 AND b.HOLD_STATUS = '1' "
			+ "UNION ALL "
			+ "SELECT a.PRODUCTION_LOT, a.LOT_SIZE, a.START_PRODUCT_ID, a.SEND_STATUS, a.STAMPED_COUNT, a.HOLD_STATUS, a.SENT_TIMESTAMP, a.NEXT_PRODUCTION_LOT, a.LOT_NUMBER, a.PLANT_CODE, a.PROCESS_LOCATION, a.LINE_NO, "
			+ "a.PRODUCT_SPEC_CODE, a.CREATE_TIMESTAMP, a.UPDATE_TIMESTAMP, a.BUILD_SEQ_NOT_FIXED_FLAG, a.BUILD_SEQUENCE_NUMBER, a.PLAN_CODE, a.KD_LOT_NUMBER, a.SEQUENCE, a.REMAKE_FLAG, a.STAMPING_FLAG, "
			+ "a.CARRY_IN_OUT_FLAG, a.CARRY_IN_OUT_UNITS, a.MBPN, a.HES_COLOR, a.DEMAND_TYPE, ord + 1 AS ord   FROM GAL212TBX a, "
			+ "Sch s WHERE s.PRODUCTION_LOT = a.NEXT_PRODUCTION_LOT AND a.process_location= ?1 and a.plan_code = ?2 AND a.HOLD_STATUS = '1') "
			+ "SELECT CASE WHEN ((SELECT ord FROM SCH where PRODUCTION_LOT = ?3) <= (SELECT ord FROM SCH where PRODUCTION_LOT = ?4)) THEN 'Passed' ELSE 'Not Passed' END AS Pass_State FROM SYSIBM.SYSDUMMY1";
	
	private static final String FIND_STARTING_PROD_LOT_CHECK_NEW = "select case when ((select COALESCE(min(actual_timestamp), case when not exists (select * from gal212tbx where production_lot = ?2) "
			+ "then '1999-01-01 00:00:00.000000' else '9999-01-01 00:00:00.000000' end) from gal215tbx where production_lot = ?2 and process_point_id = ?1) < current_timestamp) "
			+ "THEN 'Passed' ELSE 'Not Passed' END AS Pass_State FROM SYSIBM.SYSDUMMY1 ";
	
	private static final String SELECT_PRODUCTION_PROGRESS = "WITH SortedList (PRODUCTION_LOT, NEXT_PRODUCTION_LOT, LOT_SIZE,SEND_STATUS,STAMPED_COUNT, hold_status, PROCESS_LOCATION,Level)AS ("
            +" SELECT PRODUCTION_LOT, NEXT_PRODUCTION_LOT, LOT_SIZE,SEND_STATUS,STAMPED_COUNT, hold_status, PROCESS_LOCATION,1000000 as Level "
            +" FROM GAL212tbx WHERE PROCESS_LOCATION ='AF'  and HOLD_STATUS = 1 AND NEXT_PRODUCTION_LOT IS NULL "
            +" UNION ALL "
            +" SELECT ll.PRODUCTION_LOT, ll.NEXT_PRODUCTION_LOT, ll.LOT_SIZE,ll.SEND_STATUS,ll.STAMPED_COUNT,ll.hold_status,ll.PROCESS_LOCATION,Level-1 as Level "
            +" FROM GAL212tbx ll, SortedList as s "
            +" WHERE ll.NEXT_PRODUCTION_LOT = s.PRODUCTION_LOT ) "
            +" SELECT a.Production_Lot, a.Lot_Size, a.Plan_Code, a.Line_No, a.Process_Location, a.Product_Spec_Code, "
            +" (SELECT COUNT(Production_lot) from gal215tbx where Process_Point_ID in (@ON_PROCESSPOINTS@) and gal215tbx.production_lot = a.production_lot and GAL215TBX.PROCESS_COUNT = 1 and GAL215TBX.ACTUAL_TIMESTAMP < ?1 ) as production_on_count, "
            +" (SELECT COUNT(Production_lot) from gal215tbx where Process_Point_ID in (@OF_PROCESSPOINTS@) and gal215tbx.production_lot = a.production_lot and GAL215TBX.PROCESS_COUNT = 1 and GAL215TBX.ACTUAL_TIMESTAMP < ?1) as production_off_count, "
            +" (SELECT COUNT(*) from GAL215TBX B Where B.PROCESS_POINT_ID in (@EXCEPTION_PROCESSPOINTS@) and B.production_lot = a.production_lot AND B.ACTUAL_TIMESTAMP < ?1 "  
            +" AND NOT EXISTS (SELECT * FROM GAL215TBX G215A WHERE G215A.PRODUCT_ID = B.PRODUCT_ID AND G215A.PROCESS_POINT_ID IN (@ON_PROCESSPOINTS@) and G215A.actual_Timestamp < ?1 )) AS EXCEPTION_ON_COUNT,"
            +" (SELECT COUNT(*) from GAL215TBX B Where B.PROCESS_POINT_ID in (@EXCEPTION_PROCESSPOINTS@) and B.production_lot = a.production_lot AND B.ACTUAL_TIMESTAMP < ?1 "  
            +" AND NOT EXISTS (SELECT * FROM GAL215TBX G215A WHERE G215A.PRODUCT_ID = B.PRODUCT_ID AND G215A.PROCESS_POINT_ID IN (@OF_PROCESSPOINTS@) AND G215A.ACTUAL_TIMESTAMP < ?1 )) AS EXCEPTION_OFF_COUNT,"
            +" a.hold_status, a.lot_number, b.level, a.kd_lot_number, c.LOT_STATUS FROM GAL212TBX a INNER JOIN GAL217TBX c ON a.Production_Lot = c.Production_Lot "
            +" left outer join sortedlist b on a.production_lot = b.production_lot and a.process_location = b.process_location "
            +" WHERE a.Process_Location ='@PROCESS_LOCATION@'"
            +" AND c.Production_Lot = a.Production_Lot "
            +" AND c.Lot_Status != 3 and c.Lot_Status !=4 "
            +"  for read only ";

	private static final String GET_UPCOMPING_LOTS = "With ScheduledLots (PRODUCTION_LOT,PROCESS_LOCATION, NEXT_PRODUCTION_LOT, LOT_SIZE, PRODUCT_SPEC_CODE, PLAN_CODE, KD_LOT_NUMBER, " + 
			"       REMAKE_FLAG, DEMAND_TYPE, NOTES, HOLD_STATUS,  GPCS_LOT_SEQ, LOTSEQ ) " + 
			"       AS (SELECT b.PRODUCTION_LOT,b.PROCESS_LOCATION, b.NEXT_PRODUCTION_LOT, b.LOT_SIZE, b.PRODUCT_SPEC_CODE, b.PLAN_CODE, b.KD_LOT_NUMBER, " + 
			"       b.REMAKE_FLAG, B.DEMAND_TYPE, B.NOTES, B.HOLD_STATUS, B.SEQUENCE,   1 as LOTSEQ " + 
			"       FROM GAL212TBX B " + 
			"       where hold_status = 1 and b.plan_code = ?1 and production_lot = ( " + 
			"       SELECT   G215.PRODUCTION_LOT FROM GAL215TBX G215 " + 
			"       JOIN GAL212TBX G212 ON G212.PRODUCTION_LOT = G215.PRODUCTION_LOT AND REMAKE_FLAG = 'N' AND DEMAND_TYPE = 'MP' " + 
			"       where process_point_id = ?2 AND NOT EXISTS " + 
			"       (SELECT * FROM GAL215TBX G215B WHERE G215B.PRODUCT_ID != G215.PRODUCT_ID AND G215B.PROCESS_POINT_ID = G215.PROCESS_POINT_ID " + 
			"       AND G215B.PRODUCTION_LOT = G215.PRODUCTION_LOT AND G215B.ACTUAL_TIMESTAMP < G215.ACTUAL_TIMESTAMP) order by actual_timestamp desc fetch first 1 rows only) " + 
			"       UNION ALL " + 
			"       SELECT a.PRODUCTION_LOT,a.PROCESS_LOCATION, a.NEXT_PRODUCTION_LOT, a.LOT_SIZE, a.PRODUCT_SPEC_CODE, a.PLAN_CODE, a.KD_LOT_NUMBER, " + 
			"        a.REMAKE_FLAG, a.DEMAND_TYPE, a.NOTES, a.HOLD_STATUS, a.SEQUENCE, " + 
			"       s.LOTSEQ + 1 " + 
			"     FROM GAL212TBX A,  ScheduledLots s " + 
			"       WHERE A.PRODUCTION_LOT = s.next_production_lot and a.hold_status = 1 and a.plan_code = ?1), " + 
			"     HeldLots (PRODUCTION_LOT,PROCESS_LOCATION, NEXT_PRODUCTION_LOT, LOT_SIZE, PRODUCT_SPEC_CODE, PLAN_CODE, KD_LOT_NUMBER, " + 
			"        REMAKE_FLAG, DEMAND_TYPE, NOTES, HOLD_STATUS, GPCS_LOT_SEQ, LOTSEQ) AS " + 
			"      (SELECT c.PRODUCTION_LOT,c.PROCESS_LOCATION, c.NEXT_PRODUCTION_LOT, c.LOT_SIZE, c.PRODUCT_SPEC_CODE, c.PLAN_CODE, c.KD_LOT_NUMBER, " + 
			"        c.REMAKE_FLAG, c.DEMAND_TYPE, c.NOTES, c.HOLD_STATUS, c.SEQUENCE, ROW_NUMBER() OVER (ORDER BY PRODUCTION_LOT) + 10000 " + 
			"       from gal212tbx c WHERE hold_status = 0 and plan_code = ?1) , " + 
			"       AllLots (PRODUCTION_LOT,PROCESS_LOCATION, NEXT_PRODUCTION_LOT, LOT_SIZE, PRODUCT_SPEC_CODE, PLAN_CODE, KD_LOT_NUMBER, " + 
			"        REMAKE_FLAG, DEMAND_TYPE, NOTES, HOLD_STATUS, GPCS_LOT_SEQ, LOTSEQ) " + 
			"        AS  (select * from ScheduledLots union select * from heldlots) " + 
			"SELECT G143.PRODUCT_ID, LOTSEQ * 100.0 + g216.stamping_sequence_no * .001 as build_seq, " + 
			"    SL.GPCS_LOT_SEQ, " +
			"    SL.PRODUCTION_LOT, " + 
			"    SL.LOT_SIZE, " + 
			"    SL.PRODUCT_SPEC_CODE, " + 
			"    SL.PLAN_CODE, " + 
			"    SL.KD_LOT_NUMBER, " + 
			"    SL.REMAKE_FLAG, " + 
			"    SL.DEMAND_TYPE, " + 
			"    SL.NOTES, " + 
			"    SL.HOLD_STATUS, "+
			"    SL.PROCESS_LOCATION, "+
			"    '' AS COMMENTS, " + 
			"	 G143.PRODUCTION_DATE," + 
			"	 G144.BOUNDARY_MARK_REQUIRED FROM AllLots  SL " + 
			"JOIN GAL143TBX G143 ON G143.PRODUCTION_LOT = SL.PRODUCTION_LOT " + 
			"JOIN GAL144TBX G144 ON G144.PRODUCT_SPEC_CODE = G143.PRODUCT_SPEC_CODE "+ 
			"JOIN GAL216TBX G216 ON G216.PRODUCT_ID = G143.PRODUCT_ID ORDER BY 2 ";
	
	private static final String GET_UPCOMPING_ENGINE_LOTS = "With ScheduledLots (PRODUCTION_LOT,PROCESS_LOCATION, NEXT_PRODUCTION_LOT, LOT_SIZE, PRODUCT_SPEC_CODE, PLAN_CODE, KD_LOT_NUMBER, " + 
			"       REMAKE_FLAG, DEMAND_TYPE, NOTES, HOLD_STATUS,  GPCS_LOT_SEQ, LOTSEQ ) " + 
			"       AS (SELECT b.PRODUCTION_LOT,b.PROCESS_LOCATION, b.NEXT_PRODUCTION_LOT, b.LOT_SIZE, b.PRODUCT_SPEC_CODE, b.PLAN_CODE, b.KD_LOT_NUMBER, " + 
			"       b.REMAKE_FLAG, B.DEMAND_TYPE, B.NOTES, B.HOLD_STATUS, B.SEQUENCE,   1 as LOTSEQ " + 
			"       FROM GAL212TBX B " + 
			"       where hold_status = 1 and b.plan_code = ?1 and production_lot = ( " + 
			"       SELECT   G215.PRODUCTION_LOT FROM GAL215TBX G215 " + 
			"       JOIN GAL212TBX G212 ON G212.PRODUCTION_LOT = G215.PRODUCTION_LOT AND REMAKE_FLAG = 'N' AND DEMAND_TYPE = 'MP' " + 
			"       where process_point_id = ?2 AND NOT EXISTS " + 
			"       (SELECT * FROM GAL215TBX G215B WHERE G215B.PRODUCT_ID != G215.PRODUCT_ID AND G215B.PROCESS_POINT_ID = G215.PROCESS_POINT_ID " + 
			"       AND G215B.PRODUCTION_LOT = G215.PRODUCTION_LOT AND G215B.ACTUAL_TIMESTAMP < G215.ACTUAL_TIMESTAMP) order by actual_timestamp desc fetch first 1 rows only) " + 
			"       UNION ALL " + 
			"       SELECT a.PRODUCTION_LOT,a.PROCESS_LOCATION, a.NEXT_PRODUCTION_LOT, a.LOT_SIZE, a.PRODUCT_SPEC_CODE, a.PLAN_CODE, a.KD_LOT_NUMBER, " + 
			"        a.REMAKE_FLAG, a.DEMAND_TYPE, a.NOTES, a.HOLD_STATUS, a.SEQUENCE, " + 
			"       s.LOTSEQ + 1 " + 
			"     FROM GAL212TBX A,  ScheduledLots s " + 
			"       WHERE A.PRODUCTION_LOT = s.next_production_lot and a.hold_status = 1 and a.plan_code = ?1), " + 
			"     HeldLots (PRODUCTION_LOT,PROCESS_LOCATION, NEXT_PRODUCTION_LOT, LOT_SIZE, PRODUCT_SPEC_CODE, PLAN_CODE, KD_LOT_NUMBER, " + 
			"        REMAKE_FLAG, DEMAND_TYPE, NOTES, HOLD_STATUS, GPCS_LOT_SEQ, LOTSEQ) AS " + 
			"      (SELECT c.PRODUCTION_LOT,c.PROCESS_LOCATION, c.NEXT_PRODUCTION_LOT, c.LOT_SIZE, c.PRODUCT_SPEC_CODE, c.PLAN_CODE, c.KD_LOT_NUMBER, " + 
			"        c.REMAKE_FLAG, c.DEMAND_TYPE, c.NOTES, c.HOLD_STATUS, c.SEQUENCE, ROW_NUMBER() OVER (ORDER BY PRODUCTION_LOT) + 10000 " + 
			"       from gal212tbx c WHERE hold_status = 0 and plan_code = ?1) , " + 
			"       AllLots (PRODUCTION_LOT,PROCESS_LOCATION, NEXT_PRODUCTION_LOT, LOT_SIZE, PRODUCT_SPEC_CODE, PLAN_CODE, KD_LOT_NUMBER, " + 
			"        REMAKE_FLAG, DEMAND_TYPE, NOTES, HOLD_STATUS, GPCS_LOT_SEQ, LOTSEQ) " + 
			"        AS  (select * from ScheduledLots union select * from heldlots) " + 
			"SELECT G131.PRODUCT_ID, LOTSEQ * 100.0 + g216.stamping_sequence_no * .001 as build_seq, " + 
			"    SL.GPCS_LOT_SEQ, " +
			"    SL.PRODUCTION_LOT, " + 
			"    SL.LOT_SIZE, " + 
			"    SL.PRODUCT_SPEC_CODE, " + 
			"    SL.PLAN_CODE, " + 
			"    SL.KD_LOT_NUMBER, " + 
			"    SL.REMAKE_FLAG, " + 
			"    SL.DEMAND_TYPE, " + 
			"    SL.NOTES, " + 
			"    SL.HOLD_STATUS, "+
			"    SL.PROCESS_LOCATION, "+
			"    '' AS COMMENTS, " + 
			"	 G131.PRODUCTION_DATE" + 
			"   FROM AllLots  SL " + 
			"JOIN GAL131TBX G131 ON G131.PRODUCTION_LOT = SL.PRODUCTION_LOT " + 
			"JOIN GAL216TBX G216 ON G216.PRODUCT_ID = G131.PRODUCT_ID ORDER BY 2 ";

	private static final String UPDATE_LOT_STATUS = "UPDATE galadm.gal217tbx set lot_status = ?2 where production_lot = ?1";

	private static final String REMOVE_REPLICATE_LOTS = "delete from PreProductionLot p where p.productionLot = :productionLot and p.processLocation = :processLocation and p.sendStatusId = 0 and p.holdStatus = 1";

	private static final String FETCH_LOT_INFORMATION = "SELECT PRODSEQ.PRODUCT_ID, "
			+ "trim(LOT.PRODUCT_SPEC_CODE) as PRODUCT_SPEC_CODE, LOT.PRODUCTION_LOT, "
			+ "coalesce(LOT.KD_LOT_NUMBER, 'None') as KD_LOT_NUMBER, "
			+ "substring(LOT.KD_LOT_NUMBER,6,1) || substring(LOT.KD_LOT_NUMBER,9,4) || substring(LOT.KD_LOT_NUMBER,14,3) || substring(LOT.KD_LOT_NUMBER,18,1) as SHORT_KD_LOT, "
			+ "trim(SPEC.SALES_MODEL_CODE) as SALES_MODEL_CODE, "
			+ "trim(SPEC.SALES_MODEL_TYPE_CODE) as SALES_MODEL_TYPE_CODE, "
			+ "trim(SPEC.SALES_EXT_COLOR_CODE) as SALES_EXT_COLOR_CODE, "
			+ "trim(SPEC.SALES_INT_COLOR_CODE) as SALES_INT_COLOR_CODE, "
			+ "coalesce(trim(SPEC.EXT_COLOR_DESCRIPTION), 'N/A') as EXT_COLOR_DESCRIPTION, "
			+ "trim(FRAMESPEC.ENGINE_SERIAL_NO) as ENGINE_SERIAL_NO, LOT.PLAN_OFF_DATE, "
			+ "FRAMESPEC.ACTUAL_OFF_DATE  as ACTUAL_AF_OFF FROM GAL212TBX LOT left JOIN GAL144TBX SPEC "
			+ "ON LOT.PRODUCT_SPEC_CODE = SPEC.PRODUCT_SPEC_CODE left join GAL216TBX PRODSEQ "
			+ "ON LOT.PRODUCTION_LOT = PRODSEQ.PRODUCTION_LOT left join GAL143TBX FRAMESPEC "
			+ "ON PRODSEQ.PRODUCT_ID = FRAMESPEC.PRODUCT_ID where PRODSEQ.PRODUCT_ID = ?1";
	
	@Autowired
	private SubProductDao subProductDao;

	@Autowired
	private ProductionLotDao productionLotDao;

	public List<PreProductionLot> findAllPreProductionLotsByProcessLocation(
			String processLocation) {

		Parameters params = Parameters.with("processLocation", processLocation)
				.put("sendStatusId", 0);
		List<PreProductionLot> preProductionLots = findAll(params);

		if (preProductionLots.isEmpty())
			return preProductionLots;

		return sort(preProductionLots);

	}
	
	public List<PreProductionLot> findAllByProdDateAndLine(String processLocation, Date productionDate, String lineNo) {
		Parameters params = Parameters.with("processLocation", processLocation)
				.put("productionDate", productionDate)
				.put("lineNo", lineNo);
				
		return findAllByQuery(FIND_BY_PROD_DATE_AND_LINE, params);
	}

	public List<PreProductionLot> findAllByProcessLocation(String processLocation) {
		Parameters params = Parameters.with("processLocation", processLocation);
		return findAll(params);
	}

	public List<PreProductionLot> findAllSortedLotsByProcessLocation(String processLocation, boolean isAscending) {
		return findAll(Parameters.with("processLocation", processLocation),new String[] {"lotNumber"},isAscending);
	}

	public List<PreProductionLot> findAllForProcessLocation(String processLocation) {
		return findAllByNativeQuery(FIND_ALL_FOR_PROCESS_LOCATION, Parameters.with("1", processLocation));
	}

	public List<PreProductionLot> findAllPreProductionLotsByProductSpecCode(String productSpecCode){
		Parameters params = Parameters.with("productSpecCode", productSpecCode);
		return findAll(params);
	}
	private List<PreProductionLot> sort(List<PreProductionLot> prodLots) {
		PreProductionLot endProdLot = findLastPreProductionLot(prodLots);
		if (endProdLot == null) {
			// throw exception
		}

		List<PreProductionLot> orderedProdLots = new ArrayList<PreProductionLot>();

		while (endProdLot != null) {
			orderedProdLots.add(0, endProdLot);
			endProdLot = findParent(endProdLot, prodLots);
		}

		if (orderedProdLots.size() != prodLots.size()) {
			// throw exception
		}

		return orderedProdLots;
	}

	private PreProductionLot findLastPreProductionLot(
			List<PreProductionLot> prodLots) {

		for (PreProductionLot prodLot : prodLots) {
			if (prodLot.getNextProductionLot() == null && prodLot.getHoldStatus() != PreProductionLotStatus.HOLD.getId())
				return prodLot;
		}
		return null;
	}

	private PreProductionLot findParent(PreProductionLot prodLot,
			List<PreProductionLot> prodLots) {
		for (PreProductionLot item : prodLots) {
			if (prodLot.getProductionLot().equalsIgnoreCase(
					item.getNextProductionLot()))
				return item;
		}
		return null;
	}

	/**
	 * find last pre production lot
	 */
	public PreProductionLot findLastPreProductionLotByProcessLocation(
			String processLocation) {

		Parameters params = Parameters.with("processLocation", processLocation)
				.put("sendStatusId", 0).put("nextProductionLot", null)
				.put("holdStatus", 1);

		List<PreProductionLot> preProdLots = findAll(params);

		if (preProdLots.size() > 1) {
			// more than 1 list
			// throw exception
		} else if (preProdLots.size() == 0)
			return null;

		return preProdLots.get(0);
	}

	/**
	 *
	 */
	public List<PreProductionLot> findAllWithSameKdLotCurrentlyProcessed(
			String lastProductionLot) {

		List<PreProductionLot> preProdLots = new ArrayList<PreProductionLot>();

		if (StringUtils.isEmpty(lastProductionLot))
			return preProdLots;
		PreProductionLot preProductionLot = findFirstProcessed(lastProductionLot);
		if (preProductionLot == null)
			return preProdLots;
		PreProductionLot tempProdLot = preProductionLot;
		do {
			preProdLots.add(preProductionLot);
			preProductionLot = findParent(preProductionLot.getProductionLot());
		} while (preProductionLot != null
				&& preProductionLot.isSameKdLot(tempProdLot));
		return preProdLots;

	}

	private PreProductionLot findFirstProcessed(String lastProductionLot) {

		PreProductionLot preProductionLot;

		do {
			preProductionLot = findParent(lastProductionLot);
			if (preProductionLot != null) {
				if (preProductionLot.getSendStatusId() > 0)
					return preProductionLot;
				else
					lastProductionLot = preProductionLot.getProductionLot();
			}
		} while (preProductionLot != null);
		return null;
	}

	public PreProductionLot findParent(String productionLot) {

		return findFirst(Parameters.with("nextProductionLot", productionLot));

	}

	public PreProductionLot findParentBySequence(String productionLot){
		return findFirstByNativeQuery(FIND_PARENT_BY_SEQUENCE, Parameters.with("1", productionLot));
	}


	@Transactional
	public void updateAllNextProductionLots(
			List<PreProductionLot> changedPreProductionLots) {

		for (PreProductionLot prodLot : changedPreProductionLots) {
			executeUpdate(
					UPDATE_NEXT_PRODUCTION_LOT,
					Parameters
					.with("productionLot", prodLot.getProductionLot())
					.put("nextProductionLot",
							prodLot.getNextProductionLot()));
		}
	}

	public PreProductionLot findFirstForKnuckleShipping() {

		PreProductionLot preProdLot = findFirstByQuery(FIND_FIRST_PROCESSED,
				Parameters.with("processLocation",
						PreProductionLot.PROCESS_LOCATION_KNUCKLE));
		return (preProdLot != null && preProdLot.getSendStatusId() >= 1) ? preProdLot
				: null;

	}

	public PreProductionLot findNext(String productionLot) {

		PreProductionLot preProdLot = findByKey(productionLot);
		return preProdLot != null && preProdLot.getNextProductionLot() != null ? findByKey(preProdLot
				.getNextProductionLot()) : null;

	}

	public PreProductionLot findCurrentPreProductionLot(String processLocation) {

		Parameters params = Parameters.with("processLocation", processLocation)
				.put("sendStatusId", 1).put("holdStatus", 1);
		PreProductionLot inProcess = findFirst(params);
		if (inProcess != null)
			return inProcess;

		Parameters params1 = Parameters
				.with("processLocation", processLocation)
				.put("sendStatusId", 0).put("holdStatus", 1);

		return processLots(params1);

	}

	// similar to the above method but using a different condition
	public PreProductionLot getCurrentPreProductionLotByStartProductId(
			String startProductId) {

		Parameters params = Parameters.with("startProductId", startProductId);
		PreProductionLot inProcess = findFirst(params);
		return inProcess;

	}

	public PreProductionLot getCurrentPreProductionLotByLotNumber(
			String lotNumber, String processLocation) {

		Parameters params = Parameters.with("lotNumber", lotNumber).put(
				"processLocation", processLocation);
		PreProductionLot inProcess = findFirst(params);
		return inProcess;

	}

	public PreProductionLot getMergedPreProductionLotBySpecCode(PreProductionLot productionLot,boolean isNextProductionLot) {
		int lotSize = productionLot.getLotSize();
		int preLotSize = productionLot.getLotSize();
		String strProductionLot = productionLot.getProductionLot();
		String strLotNumber = productionLot.getLotNumber();
		String strStartProductId = productionLot.getStartProductId();
		double seq = productionLot.getSequence();
		PreProductionLot preProductionLot;

		do {
			if (isNextProductionLot) {
				Parameters params = Parameters.with("1", strProductionLot);
				preProductionLot = findFirstByNativeQuery(FIND_NEXT_PRODUCTION_LOT_BY_LOT, params, PreProductionLot.class);
			}
			else {
				Parameters params = Parameters.with("1", seq);
				preProductionLot = findFirstByNativeQuery(FIND_LOT_BY_SEQ, params, PreProductionLot.class);
			}
			if (preProductionLot != null
					&&productionLot.getProductSpecCode().equals(preProductionLot.getProductSpecCode())
					&&isStartProductIdMatch(strStartProductId,preProductionLot.getStartProductId(),preLotSize)) {
				preLotSize = preProductionLot.getLotSize();
				lotSize = lotSize +preProductionLot.getLotSize();
				strProductionLot = preProductionLot.getProductionLot();
				strLotNumber = preProductionLot.getLotNumber();
				seq = preProductionLot.getSequence();
				strStartProductId = preProductionLot.getStartProductId();
			} else {
				productionLot.setLotSize(lotSize);
				productionLot.setProductionLot(strProductionLot);
				productionLot.setLotNumber(strLotNumber);
				return productionLot;
			}
		} while(true);
	}

	public PreProductionLot getMergedSubAssyPreProductionLotByBuildAttribute(PreProductionLot productionLot,String specCodeFromBuildAttr,boolean isNextProductionLot) {
		int lotSize = productionLot.getLotSize();
		int preLotSize = productionLot.getLotSize();
		String strProductionLot = productionLot.getProductionLot();
		String strLotNumber = productionLot.getLotNumber();
		String strStartProductId = productionLot.getStartProductId();
		double seq = productionLot.getSequence();
		PreProductionLot preProductionLot;
		BuildAttributeCache bc  = new BuildAttributeCache();
		String tempSubProductSpecCode = bc.findAttributeValue(productionLot.getProductSpecCode(), specCodeFromBuildAttr);
		do {
			if (isNextProductionLot) {
				Parameters params = Parameters.with("1", strProductionLot);
				preProductionLot = findFirstByNativeQuery(FIND_NEXT_PRODUCTION_LOT_BY_LOT, params, PreProductionLot.class);
			}
			else {
				Parameters params = Parameters.with("1", seq);
				preProductionLot = findFirstByNativeQuery(FIND_LOT_BY_SEQ, params, PreProductionLot.class);
			}
			String SubProductSpecCode = bc.findAttributeValue(preProductionLot.getProductSpecCode(), specCodeFromBuildAttr);
			if (preProductionLot != null
					&&tempSubProductSpecCode.equals(SubProductSpecCode)
					&&isStartProductIdMatch(strStartProductId,preProductionLot.getStartProductId(),preLotSize)) {
				preLotSize = preProductionLot.getLotSize();
				lotSize = lotSize +preProductionLot.getLotSize();
				strProductionLot = preProductionLot.getProductionLot();
				strLotNumber = preProductionLot.getLotNumber();
				seq = preProductionLot.getSequence();
				strStartProductId = preProductionLot.getStartProductId();
			} else {
				productionLot.setLotSize(lotSize);
				productionLot.setProductionLot(strProductionLot);
				productionLot.setLotNumber(strLotNumber);
				return productionLot;
			}
		} while(true);
	}

	private PreProductionLot processLots(Parameters params1) {
		List<PreProductionLot> waitingLots = findAll(params1);
		PreProductionLot endProdLot = findLastPreProductionLot(waitingLots);

		PreProductionLot parentLot = endProdLot;

		while (parentLot != null) {
			endProdLot = parentLot;
			parentLot = findParent(endProdLot, waitingLots);
		}

		return endProdLot;
	}

	private boolean isStartProductIdMatch(String startProductId,String nextStartProductId,int lotSize) {
		int startProductIdSn = Integer.parseInt(startProductId.substring(startProductId.length()-5, startProductId.length()));
		int nextStartProductIdSn = Integer.parseInt(nextStartProductId.substring(nextStartProductId.length()-5, nextStartProductId.length()));
		if (startProductIdSn + lotSize == nextStartProductIdSn) return true;
		return false;
	}

	public List<PreProductionLot> getPreProductionLotsForSameKdLot(
			String productionLotId) {
		List<PreProductionLot> lotsForSameKdLot = new ArrayList<PreProductionLot>();
		PreProductionLot fistLot = findByKey(productionLotId);
		lotsForSameKdLot.add(fistLot);

		if (StringUtils.isEmpty(fistLot.getNextProductionLot()))
			return lotsForSameKdLot;

		PreProductionLot nextLot = findByKey(fistLot.getNextProductionLot());
		int kdLotNumberLength = fistLot.getKdLot().length();
		while (StringUtils.substring(fistLot.getKdLot(), 0,
				kdLotNumberLength - 1).equals(
						StringUtils.substring(nextLot.getKdLot(), 0,
								kdLotNumberLength - 1))) {

			lotsForSameKdLot.add(nextLot);
			fistLot = nextLot;
			nextLot = findByKey(fistLot.getNextProductionLot());

		}

		return lotsForSameKdLot;
	}

	public List<PreProductionLot> findAllWithSameKdLot(String kdLot) {
		return findAllByNativeQuery(FIND_SAME_KDLOT,
				Parameters.with("1", StringUtils.substring(kdLot, 0, 17)));
	}

	public List<PreProductionLot> findAllWithSameKdLotFromProductionLot(
			String productionLot) {

		PreProductionLot currentLot = findByKey(productionLot);
		List<PreProductionLot> sortedLots = new ArrayList<PreProductionLot>();
		if (currentLot == null)
			return sortedLots;
		sortedLots.add(currentLot);

		PreProductionLot lot = currentLot;
		boolean isSameKdLot = false;
		do {
			lot = findParent(lot.getProductionLot());
			isSameKdLot = lot != null && lot.isSameKdLot(currentLot);
			if (isSameKdLot)
				sortedLots.add(0, lot);
		} while (isSameKdLot);

		lot = currentLot;
		do {
			lot = findNext(lot.getProductionLot());
			isSameKdLot = lot != null && lot.isSameKdLot(currentLot);
			if (isSameKdLot)
				sortedLots.add(lot);
		} while (isSameKdLot);

		return sortedLots;
	}

	public List<PreProductionLot> getUnSentLots(String processLocation) {
		return findAllByQuery(FIND_UNSENT_LOTS,
				Parameters.with("processLocation", processLocation));
	}

	public List<PreProductionLot> findAllWithIncorrectKsns() {

		List<PreProductionLot> preProdLots = new ArrayList<PreProductionLot>();
		Parameters paramsLeft = Parameters.with("1", Product.SUB_ID_LEFT)
				.put("2", BuildAttributeTag.KNUCKLE_LEFT_SIDE)
				.put("3", BuildAttributeTag.KNUCKLE_LEFT_SIDE);

		preProdLots.addAll(findAllByNativeQuery(FIND_ALL_NOT_MATCH_KNUCKLE,
				paramsLeft));

		Parameters paramsRight = Parameters.with("1", Product.SUB_ID_RIGHT)
				.put("2", BuildAttributeTag.KNUCKLE_RIGHT_SIDE)
				.put("3", BuildAttributeTag.KNUCKLE_RIGHT_SIDE);

		List<PreProductionLot> itemsRight = findAllByNativeQuery(
				FIND_ALL_NOT_MATCH_KNUCKLE, paramsRight);

		for (PreProductionLot item : itemsRight) {
			if (!preProdLots.contains(item))
				preProdLots.add(item);
		}

		return preProdLots;

	}

	@Transactional
	public void recreateKnuckleSerialNumbers(SubProduct subProduct,
			String partNumber) {

		List<SubProduct> subProducts = new ArrayList<SubProduct>();
		PreProductionLot preProdLot = findByKey(subProduct.getProductionLot());
		int sn = findNextSerialNumber(partNumber);

		for (int i = 0; i < preProdLot.getLotSize(); i++) {

			SubProduct item = subProduct.clone();
			String productId = partNumber
					+ StringUtils.leftPad(Integer.toString(sn + i), KSN_DIGITS,
							"0");
			item.setProductId(productId);
			subProducts.add(item);

		}

		String startNumber = subProduct.getSubId()
				+ StringUtils.leftPad("" + sn, KSN_DIGITS, "0");
		String numbers[] = preProdLot.getStartProductId().split("\\*");
		String startKsn = subProduct.getSubId().equals(Product.SUB_ID_LEFT) ? startNumber
				+ "*" + numbers[1]
						: numbers[0] + "*" + startNumber;

				preProdLot.setStartProductId(startKsn);

				// delete old ksns
				subProductDao.deleteAll(preProdLot.getProductionLot(),
						subProduct.getSubId());

				// save new ksns
				subProductDao.saveAll(subProducts);

				ProductionLot prodLot = productionLotDao.findByKey(preProdLot
						.getProductionLot());
				prodLot.setStartProductId(startKsn);
				// update start product id of production lot
				productionLotDao.update(prodLot);
				// update start product id of preProduction lot
				update(preProdLot);

	}

	private int findNextSerialNumber(String partNumberPrefix) {

		String productId = subProductDao.findMaxProductId(partNumberPrefix);
		if (productId == null)
			return 1;
		else
			return Integer.parseInt(StringUtils.trim(productId
					.substring(partNumberPrefix.length()))) + 1;

	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String findNextWeldOnProductId(int sendStatus) {
		Parameters parameters = Parameters.with("1", sendStatus);
		return findFirstByNativeQuery(
				FIND_NEXT_WELD_ON_PRODUCT_USING_SEND_STATUS, parameters,
				String.class);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String findNextWeldOnProductId(int sendStatus, String component) {
		Parameters parameters = Parameters.with("1", sendStatus);
		String qry = FIND_NEXT_WELD_ON_PRODUCT_USING_SEND_STATUS;
		if (!StringUtils.isEmpty(component)) qry = qry.replace("VIN_STAMP_DASH", component);
		return findFirstByNativeQuery(qry, parameters,
				String.class);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Object[] findNextWeldOnProductId(String currentProductId,
			int sendStatus) {
		if (currentProductId == null) {
			currentProductId = "";
		}

		String qry = FIND_NEXT_WELD_ON_PRODUCT_MASTER_QRY.replace("@LAST_VIN@",
				"'" + currentProductId.trim() + "'");
		qry = qry.replace("@SEND_STATUS@", new Integer(sendStatus).toString());
		return executeNative(qry).get(0);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Object[] findNextWeldOnProductId(String currentProductId,
			int sendStatus, String component) {
		if (currentProductId == null) {
			currentProductId = "";
		}

		String qry = FIND_NEXT_WELD_ON_PRODUCT_MASTER_QRY.replace("@LAST_VIN@",
				"'" + currentProductId.trim() + "'");
		qry = qry.replace("@SEND_STATUS@", new Integer(sendStatus).toString());
		if (!StringUtils.isEmpty(component)) qry = qry.replace("VIN_STAMP_DASH", component);
		return executeNative(qry).get(0);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateStampedCount(String productionLot, int stampedCount) {
		Parameters parameters = Parameters.with("1", productionLot);
		parameters.put("2", stampedCount);

		executeNative(UPDATE_STAMPED_COUNT, parameters);

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateStampedCount(String productionLot) {
		Parameters parameters = Parameters.with("1", productionLot);
		

		executeNative(UPDATE_STAMPED_COUNT_AUTO, parameters);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateSendStatus(String productionLot, int status) {
		Parameters parameters = Parameters.with("1", productionLot);
		parameters.put("2", status);

		executeNative(UPDATE_SEND_STATUS, parameters);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateSentTimestamp(String productionLot) {
		Parameters parameters = Parameters.with("1", productionLot);

		executeNative(UPDATE_SENT_TIMESTAMP, parameters);
	}

	@Transactional
	public int delete(String prodLotNumber) {
		PreProductionLot preProductionLot = findByKey(prodLotNumber);

		if(preProductionLot == null) return 0; //handle corrupted data

		if(!StringUtils.isEmpty(preProductionLot.getNextProductionLot()) &&
				!StringUtils.isEmpty(preProductionLot.getProcessLocation()))
			return deleteFromLinkList(prodLotNumber, preProductionLot);
		else
			return delete(Parameters.with("productionLot", prodLotNumber));
	}

	private int deleteFromLinkList(String prodLotNumber, PreProductionLot preProductionLot) {
		Parameters parameters = Parameters.with("1", prodLotNumber);
		parameters.put("2", preProductionLot.getNextProductionLot());

		executeNative(UPDATE_NEXT_PROD_LOT, parameters);

		return delete(Parameters.with("productionLot", prodLotNumber));
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateStartProductId(String productionLot, String StartProductId) {
		Parameters parameters = Parameters.with("1", productionLot);
		parameters.put("2", StartProductId);

		executeNative(UPDATE_START_PRODUCT_ID, parameters);

	}

	@Transactional
	public void appendPreProductionLot(PreProductionLot previousLot,
			PreProductionLot currentLot) {
		save(currentLot);
		if (previousLot != null) {
			previousLot.setNextProductionLot(currentLot.getProductionLot());
			update(previousLot);
		}
	}

	public List<PreProductionLot> findUpcomingPreProductionLots(int count) {
		return findAllByNativeQuery(FIND_UPCOMING_PREPRODUCTION_LOTS, null,
				count);
	}


	public PreProductionLot findLastLot(String processLocation) {
		return findFirst(Parameters.with("processLocation", processLocation)
				.put("nextProductionLot", null).put("holdStatus", 1));
	}

	@Transactional
	public void movePreProductionLots(String startProductionLot,
			String processLocation) {

		PreProductionLot currentLot = findByKey(startProductionLot);
		if (currentLot == null)
			return;

		PreProductionLot lastLot = findLastLot(processLocation);

		// set next production lot of the previous lot as null
		PreProductionLot firstLot1 = findParent(startProductionLot);
		if (firstLot1 != null) {
			firstLot1.setNextProductionLot(null);
			update(firstLot1);
		}

		// update the process location of the linked lots starting from the
		// current lot
		List<PreProductionLot> lots = findAllLots(currentLot);

		for (PreProductionLot item : lots) {
			item.setProcessLocation(processLocation);
		}

		saveAll(lots);

		// update next production lot of the new process location
		if (lastLot != null)
			lastLot.setNextProductionLot(startProductionLot);

	}

	private List<PreProductionLot> findAllLots(PreProductionLot startLot) {
		List<PreProductionLot> lots = new ArrayList<PreProductionLot>();
		PreProductionLot lot = startLot;
		do {
			lots.add(lot);
			lot = findNext(lot.getProductionLot());
		} while (lot != null);

		return lots;
	}

	public Date findLastUpdateTimestamp(String processLocation) {
		return findFirstByNativeQuery(FIND_LAST_UPDATETIMESTAMP,
				Parameters.with("1", processLocation),Date.class);
	}


	public Date findLastUpdateTimestampByProcessLocation(String processLocation) {
		Parameters params = Parameters.with("sendStatusId", 0).put(
				"processLocation", processLocation);
		PreProductionLot lot = findFirst(params,
				new String[] { "updateTimestamp" }, false);
		return lot == null ? null : lot.getUpdateTimestamp();
	}

	public List<Object[]> findDistinctPlantCodes() {

		return findAllByNativeQuery(FIND_DISTINCT_PLANT_CODES, null,
				Object[].class);
	}

	public List<Object[]> findDistinctLines(String plantCode,
			String processLocation) {

		return findAllByNativeQuery(FIND_DISTINCT_LINES,
				Parameters.with("1", plantCode).put("2", processLocation),
				Object[].class);
	}

	public List<PreProductionLot> findLotsWithNullNextProdLot(String plantCode,
			String lineNo, String processLocation) {
		Parameters params = Parameters.with("processLocation", processLocation)
				.put("plantCode", plantCode).put("lineNo", lineNo)
				.put("nextProductionLot", null).put("holdStatus", 1);

		return findAll(params);
	}

	public List<PreProductionLot> findLotByNextProductionLot(
			String nextProductionLot) {
		return findAllByNativeQuery(FIND_LOT_BY_NEXT_PRODUCTION_LOT,
				Parameters.with("1", nextProductionLot), PreProductionLot.class);
	}

	public List<PreProductionLot> findLotsOnHold(String plantCode,
			String lineNo, String processLocation) {
		return findAllByNativeQuery(
				FIND_LOTS_ON_HOLD,
				Parameters.with("1", processLocation).put("2", plantCode)
				.put("3", lineNo), PreProductionLot.class);

	}

	public List<PreProductionLot> findAllSentLots(String processLocation) {
		Parameters params = Parameters.with("processLocation", processLocation)
				.put("sendStatusId", 1).put("holdStatus", 1);

		return findAll(params);
	}

	public String findLastStampedLot(String processLocation)

	{
		Parameters params = Parameters.with("1", processLocation);
		return findFirstByNativeQuery(FIND_LAST_STAMPED_LOT, params,
				String.class);
	}

	public String findSplitLot(String productionLot) {
		Parameters params = Parameters.with("1", productionLot);
		return findFirstByNativeQuery(FIND_SPLIT_LOT, params, String.class);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateNextLotAndHoldStatus(String nextProductionLot,
			int holdStatus, String productionLot) {
		Parameters parameters = Parameters.with("1", nextProductionLot)
				.put("2", holdStatus).put("3", productionLot);
		executeNative(UPDATE_NEXT_LOT_HOLD_STATUS, parameters);
	}

	public List<PreProductionLot> findAllNonProcessedLots(
			String processLocation, String msProcessPointId) {

		Parameters params = Parameters.with("1", processLocation).put("2",
				msProcessPointId);
		return findAllByNativeQuery(FIND_NON_PROCESSED_LOTS, params);

	}

	public List<HashMap<String, Object>> getNextProductionSchedule(
			DefaultDataContainer dc) {

		String processPointId = dc.getString(TagNames.PROCESS_POINT_ID.name());
		String currentStartProductId = dc.getString(TagNames.START_PRODUCT_ID
				.name());
		int noOfLots = Integer.parseInt(dc.getString(TagNames.NUMBER_OF_LOTS
				.name()));
		List<HashMap<String, Object>> retList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> tempMap = null;

		ExpectedProduct ep = ServiceFactory.getDao(ExpectedProductDao.class)
				.findForProcessPointAndProduct(processPointId,
						currentStartProductId);
		if (ep == null) {
			// received product id does not match expected product id
			tempMap = new HashMap<String, Object>();
			tempMap.put(TagNames.ALC_INFO_CODE.name(),
					RECV_PROD_DOES_NOT_MATCH_EXP_PROD_CODE);
			tempMap.put(TagNames.ALC_INFO_MSG.name(),
					RECV_PROD_DOES_NOT_MATCH_EXP_PROD_MSG);
			retList.add(tempMap);
			return retList;
		}

		PreProductionLot currentLot = getCurrentPreProductionLotByStartProductId(currentStartProductId);

		PreProductionLot iterLot = currentLot;
		int counter = 0;
		while (counter < noOfLots) {
			PreProductionLot tempLot = iterLot != null ? findNext(iterLot
					.getProductionLot()) : null;
			if (tempLot != null) {

				iterLot = tempLot;
				if (tempLot.getHoldStatus() != 0) {
					tempMap = new HashMap<String, Object>();
					tempMap.put(TagNames.PRODUCT_SPEC_CODE.name(),
							tempLot.getProductSpecCode());
					tempMap.put(TagNames.START_PRODUCT_ID.name(),
							tempLot.getStartProductId());
					tempMap.put(TagNames.KD_LOT_SIZE.name(),
							tempLot.getLotSize());

					// get the mission type i.e. TRANSMISSION field from 133
					// table
					EngineSpec es = ServiceFactory.getDao(EngineSpecDao.class)
							.findByKey(tempLot.getProductSpecCode());
					String missionType = es != null
							&& es.getTransmission() != null ? es
									.getTransmission() : "N";
									tempMap.put(TagNames.MISSION_TYPE.name(), missionType);

									retList.add(tempMap);
									counter++;
				}
			} else {
				// requested lots not available
				retList.clear();
				tempMap = new HashMap<String, Object>();
				tempMap.put(TagNames.ALC_INFO_CODE.name(),
						REQUESTED_NO_OF_LOTS_UNAVAILABLE_CODE);
				tempMap.put(TagNames.ALC_INFO_MSG.name(),
						REQUESTED_NO_OF_LOTS_UNAVAILABLE_MSG);
				retList.add(tempMap);
				return retList;
			}
		}

		// update 135 table
		ep.setProductId(tempMap.get(TagNames.START_PRODUCT_ID.name())
				.toString());
		ServiceFactory.getDao(ExpectedProductDao.class).save(ep);
		return retList;
	}

	@Transactional
	public PreProductionLot getLastLot(String processLocation, String planCode6) {
		Parameters params = Parameters.with("processLocation", processLocation);
		params.put("planCode6", planCode6);
		params.put("nextProductionLot", null);
		params.put("holdStatus", 1);
		return findFirstByQuery(FIND_LAST_LOT, params);
	}

	@Transactional
	public List<PreProductionLot> getTailsByPlantLineLocation(String plantCode, String lineNo, String processLocation) {
		Parameters params = Parameters.with("processLocation", processLocation);
		params.put("plantCode", plantCode);
		params.put("lineNo", lineNo);
		params.put("nextProductionLot", null);
		params.put("holdStatus", 1);
		return findAll(params);
	}

	@Transactional
	public int countByPlantLineLocation(String plantCode, String lineNo, String processLocation) {
		int result = (int)count(Parameters.with("processLocation", processLocation)
				.put("plantCode", plantCode)
				.put("lineNo",lineNo));
		return result;
	}

	public PreProductionLot findByStartProductId(String startSN) {
		return findFirst(Parameters.with("startProductId", startSN));
	}

	public PreProductionLot findCurrentProductLotAtBlockLoad() {
		return findFirstByNativeQuery(FIND_CURRENT_BLOCK_LOAD_LOT, null);
	}

	public PreProductionLot findFirstAvailableLot(String processLocation) {
		return findFirstByNativeQuery(FIND_FIRST_AVAILABLE_LOT, Parameters.with("1", processLocation));
	}

	public Double findMaxSequence(String planCode) {
		try {
			return findFirstByNativeQuery(FIND_MAX_SEQ, Parameters.with("1", planCode), Double.class);
		} catch (Exception e) {
			return 0.0;
		}
	}

	public Double findMaxSequenceWhereNotSendStatus(String planCode, int sendStatus) {
		try {
			Parameters p = Parameters.with("1", planCode);
			p.put("2", sendStatus);
			return findFirstByNativeQuery(FIND_MAX_SEQ_BY_NOT_SEND_STATUS, p, Double.class);
		} catch (Exception e) {
			return 0.0;
		}
	}


	public List<PreProductionLot> findAllByPlanCode(String planCode) {
		if(StringUtils.isEmpty(planCode)){//All Plan Codes
			return findAllByNativeQuery(FIND_ALL_FOR_ALL_PLAN_CODE, null);
		} else if(!planCode.contains(Delimiter.COMMA)){//One PlanCode
			Parameters params = Parameters.with("planCode", planCode);
			return findAll(params, new String[]{"sequence"});
		} else { //Selected Plan Codes
			Parameters params = Parameters.with("planCodes", CommonUtil.toList(planCode));
			return  findAllByQuery(FIND_ALL_FOR_PLAN_CODES,params);

		}
	}

	public List<String> findDistinctSpecCodeByPlanCode(String planCode) {
		if (!planCode.contains(Delimiter.COMMA)) {
			Parameters params = Parameters.with("1", planCode);
			return findAllByNativeQuery(FIND_DISTINCT_SPEC_CODES_FOR_PLAN_CODE,
					params, String.class);
		} else {
			List<PreProductionLot> findAllByPlanCode = findAllByPlanCode(planCode);
			Set<String> distinctSpecCodes = new HashSet<String>();
			for (PreProductionLot lot : findAllByPlanCode) {
				distinctSpecCodes.add(lot.getProductSpecCode());
			}
			return new ArrayList<String>(distinctSpecCodes);
		}
	}

	public Date findLastUpdateTimestampByPlanCode(String planCode) {

		Parameters params = Parameters.with("planCode",  CommonUtil.toList(planCode));
		params.put("sendStatusId", 0);
		PreProductionLot lot = findFirstByQuery(FIND_LAST_UPDATE_TIMESTAMP, params);
		return lot == null ? null : lot.getUpdateTimestamp();
	}

	public PreProductionLot findCurrentPreProductionLotByPlanCode(String planCode) {
		Parameters params = Parameters.with("planCode", CommonUtil.toList(planCode));
		params.put("sendStatusId", 1).put("holdStatus", 1);
		PreProductionLot currentLot = findFirstByQuery(FIND_CURRENT_LOT,params);

		if(currentLot != null) return currentLot;

		params.put("sendStatusId", 0);
		return findFirstByQuery(FIND_CURRENT_LOT,params);
	}

	public List<String> getAllPlanCodes() {

		return findAllByNativeQuery(FIND_ALL_PLAN_CODE, null, String.class);
	}


	/**
	 * This method is used for get information about Transmission Production Report
	 * @param lastDate			-	The last date that was executed the interface
	 * @param processPointDcMc	-	Process points that are used for DC M Case
	 * @param processPointDcTc	-	Process points that are used for DC Torque Case
	 * @param processPointMcMc	-	Process points that are used for MC M Case
	 * @param processPointMcTc	-	Process points that are used for DC Torque Case
	 * @param processPointMcP	-	Process points that are used for DC Pulley
	 * @return List with query result
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getTmProductionReport(final String lastDate, final String processPointDcMc, final String processPointDcTc,
			final String processPointMcMc, final String processPointMcTc, final String processPointMcP, final String plantCode) {
		StringBuilder processPoints = new StringBuilder()
		.append("\'")
		.append( processPointDcMc.replaceAll(",", "\',\'") )
		.append("\',\'")
		.append( processPointDcTc.replaceAll(",", "\',\'") )
		.append("\',\'")
		.append( processPointMcMc.replaceAll(",", "\',\'") )
		.append("\',\'")
		.append( processPointMcTc.replaceAll(",", "\',\'") )
		.append("\',\'")
		.append( processPointMcP.replaceAll(",", "\',\'") )
		.append("\'")
		;
		Parameters parameters = Parameters.with("1", lastDate);
		parameters.put("2", plantCode);
		return findResultListByNativeQuery(TM_PRODUCTION_RESULT.replaceAll("@PROCESS_POINTS@", processPoints.toString()), parameters);
	}


	public List<PreProductionLot> findReplicateSourceByFilters(
			String sourcePlanCode, Map<String, String> filters) {
		String query = "SELECT GAL212TBX.* FROM GAL212TBX INNER JOIN GAL217TBX ON GAL212TBX.PRODUCTION_LOT = GAL217TBX.PRODUCTION_LOT"
				+ " WHERE GAL212TBX.PLAN_CODE = '" + sourcePlanCode + "'";

		if (filters != null && !filters.isEmpty()) {
			for (String key : filters.keySet()) {
				query += " AND " + key + filters.get(key);
			}
		}
		return sort(findAllByNativeQuery(query, null));
	}

	public List<PreProductionLot> findReplicateSourceOnHoldByFilters(
			String sourcePlanCode, Map<String, String> filters) {
		String query = "SELECT GAL212TBX.* FROM GAL212TBX INNER JOIN GAL217TBX ON GAL212TBX.PRODUCTION_LOT = GAL217TBX.PRODUCTION_LOT"
				+ " WHERE GAL212TBX.PLAN_CODE = '" + sourcePlanCode + "' AND HOLD_STATUS = 0";

		if (filters != null && !filters.isEmpty()) {
			for (String key : filters.keySet()) {
				query += " AND " + key + filters.get(key);
			}
		}
		query += " ORDER BY SEQUENCE ASC ";
		return findAllByNativeQuery(query, null);
	}
	public List<PreProductionLot> findReplicateNotHoldSourceByFilters(
			String sourcePlanCode, Map<String, String> filters,String demandTypes) {
		String query = "SELECT GAL212TBX.* FROM GAL212TBX INNER JOIN GAL217TBX ON GAL212TBX.PRODUCTION_LOT = GAL217TBX.PRODUCTION_LOT"
				+ " WHERE GAL212TBX.PLAN_CODE = '" + sourcePlanCode + "' AND GAL212TBX.DEMAND_TYPE IN (" +demandTypes+ ") " ;

		if (filters != null && !filters.isEmpty()) {
			for (String key : filters.keySet()) {
				query += " AND " + key + filters.get(key);
			}
		}
		return sort(findAllByNativeQuery(query, null));
	}

	public List<PreProductionLot> findReplicateHoldSourceByFilters(
			String sourcePlanCode, Map<String, String> filters,String demandTypes, String lastProdLotComp) {
		String query = "SELECT GAL212TBX.* FROM GAL212TBX INNER JOIN GAL217TBX ON GAL212TBX.PRODUCTION_LOT = GAL217TBX.PRODUCTION_LOT"
				+ " WHERE GAL212TBX.PLAN_CODE = '" + sourcePlanCode + "' AND GAL212TBX.DEMAND_TYPE NOT IN (" +demandTypes+ ")  AND GAL212TBX.SEQUENCE > "
				+ "( SELECT SEQUENCE FROM GAL212TBX WHERE GAL212TBX.PRODUCTION_LOT = '" + lastProdLotComp + "') ";

		if (filters != null && !filters.isEmpty()) {
			for (String key : filters.keySet()) {
				query += " AND " + key + filters.get(key);
			}
		}
		query += " ORDER BY SEQUENCE ASC ";
		return findAllByNativeQuery(query, null);
		//return sort(findAllByNativeQuery(query, null));
	}

	public PreProductionLot findLast(String planCode) {
		return findFirst(Parameters.with("planCode", planCode).put(
				"nextProductionLot", null));
	}

	@SuppressWarnings("unchecked")
	public List<String> getUnmappedOrderIds(){
		return findResultListByNativeQuery(UNMAPPED_ORDER_IDS, null);
	}

	public List<ProductionLot> findAllNewEngineShippingLots() {
		return findAllByNativeQuery(ALL_NEW_ENGINE_SHIPPING_LOTS, null,ProductionLot.class);
	}

	public List<PreProductionLot> findAllUpcomingLots(String processLocation) {
		return findAllByNativeQuery(ALL_UPCOMING_LOTS, Parameters.with("1", processLocation));
	}

	public List<PreProductionLot> findAllPreviousLots(String productionLot,int count){
		return findAllByNativeQuery(ALL_PREVIOUS_LOTS, Parameters.with("1", productionLot).put("2",count));
	}

	public List<PreProductionLot> findAllOnHoldLots(String processLocation) {
		Parameters params = Parameters.with("processLocation", processLocation);
		params.put("holdStatus", 0);
		return findAll(params,new String[]{"productionLot"});
	}

	public PreProductionLot getNextPreProductionLot(String planCode, int orderStatusId) {
		String[] orderBy = {"sequence"};
		Parameters params = Parameters.with("planCode", planCode);
		params.put("sendStatusId", orderStatusId);
		return findFirst(params,orderBy, true);
	}

	public PreProductionLot getNextByLastPreProductionLot(String orderNo, String planCode) {
		return findFirstByQuery(FIND_NEXT_ORDER_BY_LAST_ORDER_AND_PLAN_CODE_SQL, Parameters.with("orderNo", orderNo).put("planCode", planCode));
	}

	public PreProductionLot findByPlanCodeAndProdSpecCode(String productionLot,
			String planCode, String prodSpecCode) {
		return findFirst(Parameters.with("productionLot", productionLot).put("planCode", planCode).put("productSpecCode", prodSpecCode));
	}

	public PreProductionLot findByProductionLotAndPlanCode(
			String productionLot, String planCode) {
		return super.findFirst(Parameters.with("productionLot", productionLot).put("planCode",planCode));
	}

	public List<PreProductionLot> findAllByPlanCodeOrderBySeqNum(String planCode)
	{
		Parameters params = Parameters.with("planCode", planCode);
		return  findAllByQuery(FIND_ALL_FOR_PLAN_CODES_ORDER_BY_SEQNUM,params);
	}

	public List<PreProductionLot> findAllBySendStatusAndPlanCode(
			int sendStatus, String planCode) {
		if (planCode == null) {
			return null;
		}
		Parameters params = Parameters.with("planCode", planCode)
				.put("sendStatusId", sendStatus);
		List<PreProductionLot> preProductionLots = findAll(params);
		return preProductionLots;
	}
	public List<PreProductionLot> findAllWaitingByPlanCode(
			String planCode) {

		List<PreProductionLot> preProductionLots = findAllBySendStatusAndPlanCode(PreProductionLotSendStatus.WAITING.getId(),planCode);
		return preProductionLots;
	}

	@Transactional
	public int changeToUnsent(String productionLot, String processLocation) {
		Parameters parameters = Parameters.with("productionLot", productionLot);
		parameters.put("processLocation", processLocation);
		executeUpdate(CHANGE_216_TO_UNSENT, parameters);
		return executeUpdate(CHANGE_212_TO_UNSENT, parameters);
	}

	@Transactional
	public int changeToUnsentService(String processLocation, String planCode, String ppId) {
		Parameters parameters = Parameters.with("processLocation", processLocation);
		parameters.put("planCode", planCode);
		ServiceFactory.getDao(ExpectedProductDao.class).removeByKey(ppId);
		executeUpdate(CHANGE_216_TO_UNSENT_BY_PROCESS_LOCATION, parameters);
		int count = executeUpdate(CHANGE_212_TO_UNSENT_BY_PROCESS_LOCATION, parameters);
		Logger.getLogger().info("Updated rows in GAL212TBX: " + count);
		if(count > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	public PreProductionLot getNextByLastLotNumber(String lotNumber,String planCode) {
		return findFirstByQuery(FIND_NEXT_ORDER_BY_LAST_LOT_NUMBER_AND_PLAN_CODE_SQL, Parameters.with("lotNumber", lotNumber).put("planCode", planCode));
	}

	public PreProductionLot getByLotNumberAndPlanCode(String lotNumber, String planCode) {
		Parameters params = Parameters.with("lotNumber", lotNumber)
				.put("planCode", planCode);

		return findFirst(params);
	}

	public PreProductionLot findFirstUpcomingLotByPlanCode(String planCode) {
		Parameters params = Parameters.with("planCodes", CommonUtil.toList(planCode));
		//Creating list of send status for upcoming lots
		List<Integer> sendStatusList = new ArrayList<Integer>();
		sendStatusList.add(PreProductionLotSendStatus.WAITING.getId());
		sendStatusList.add(PreProductionLotSendStatus.INPROGRESS.getId());
		sendStatusList.add(PreProductionLotSendStatus.SENT.getId());
		params.put("sendStatusIds", sendStatusList);
		params.put("holdStatus", 1);
		return findFirstByQuery(FIND_UPCOMING_LOT,params);
	}

	public List<PreProductionLot> findByKDLotAndPlanCode(String kdLot, String planCode) {
		Parameters params = Parameters.with("kdLotNumber", kdLot).put("planCode", planCode);
		return findAll(params);
	}

	@Transactional
	public int deleteByPlanCodeSendStatus(String planCode, int sendStatus) {
		Parameters params = Parameters.with("planCode", planCode);
		params.put("sendStatusId", sendStatus);

		return delete(params);
	}

	@Transactional
	public int deleteByPlanCodeSendStatusDemandType(String planCode, int sendStatus, String demandType) {
		Parameters params = Parameters.with("planCode", planCode);
		params.put("sendStatusId", sendStatus);
		params.put("demandType", demandType);
		return delete(params);
	}
	public List<PreProductionLot> getTailsByPlanCode(String planCode) {
		Parameters params = Parameters.with("planCode", planCode);
		params.put("nextProductionLot", null);
		params.put("holdStatus", 1);

		return findAll(params);
	}

	public PreProductionLot findLastLotByPlanCode(String planCode) {
		return findFirst(Parameters.with("planCode", planCode)
				.put("nextProductionLot", null).put("holdStatus", 1));
	}

	public boolean isLotPrefixExist(String lotPrefix) {
		String likeLotPrefix;
		{
			StringBuilder likeLotPrefixBuilder = new StringBuilder();
			likeLotPrefixBuilder.append(lotPrefix);
			likeLotPrefixBuilder.append("%");
			likeLotPrefix = likeLotPrefixBuilder.toString();
		}
		Parameters params = Parameters.with("lotPrefix", likeLotPrefix);
		PreProductionLot result = findFirstByQuery(IS_LOT_PREFIX_EXIST, params);
		return result != null;
	}

	public boolean isLastProductionDate(String lotPrefix, String productionDate) {
		String lastProductionLotForProductionDate;
		{
			StringBuilder lastProductionLotForProductionDateBuilder = new StringBuilder();
			lastProductionLotForProductionDateBuilder.append(lotPrefix);
			lastProductionLotForProductionDateBuilder.append(productionDate);
			lastProductionLotForProductionDateBuilder.append("9999");
			lastProductionLotForProductionDate = lastProductionLotForProductionDateBuilder.toString();
		}
		String lastProductionLotPossible;
		{
			StringBuilder lastProductionLotPossibleBuilder = new StringBuilder();
			lastProductionLotPossibleBuilder.append(lotPrefix);
			lastProductionLotPossibleBuilder.append("99999999"); // yyyyMMdd
			lastProductionLotPossibleBuilder.append("9999");
			lastProductionLotPossible = lastProductionLotPossibleBuilder.toString();
		}
		String likeLotPrefix;
		{
			StringBuilder likeLotPrefixBuilder = new StringBuilder();
			likeLotPrefixBuilder.append(lotPrefix);
			likeLotPrefixBuilder.append("%");
			likeLotPrefix = likeLotPrefixBuilder.toString();
		}
		Parameters params = Parameters.with("maxLotDate", lastProductionLotForProductionDate);
		params.put("maxLot", lastProductionLotPossible);
		params.put("lotPrefix", likeLotPrefix);
		PreProductionLot result = findFirstByQuery(IS_LAST_PRODUCTION_DATE, params);
		return (result == null);
	}

	public PreProductionLot findByProcessLocationKdLotNumberAndSpecCode(String processLocation, String kdLotNumber, String productSpecCode) {
		return findFirst(Parameters.with("processLocation", processLocation)
				.put("kdLotNumber", kdLotNumber)
				.put("productSpecCode", productSpecCode));
	}

	public int countByPlanCode(String planCode) {
		return (int)count(Parameters.with("planCode", planCode));
	}

	public int countByPlanCodeNextProductionLot(String planCode, String nextProductionLot) {
		return (int)count(Parameters.with("planCode", planCode).put("nextProductionLot", nextProductionLot));
	}

	public String getNextLotNumberByProductionLot(String ProductionLot) {
		Parameters params = Parameters.with("1", ProductionLot);

		return findFirstByNativeQuery(NEXTLOTNUMBERBYPRODUCTIONLOT, params, String.class);
	}

	public void deleteByPlanCodeSendStatusNotHoldDemandType(String planCode, int sendStatus, String notHoldLotDemandTypes) {
		if(StringUtils.isEmpty(notHoldLotDemandTypes)){//All Plan Codes
			deleteByPlanCodeSendStatus(planCode,sendStatus);
		} else {
			List<String> demandTypesList = CommonUtil.toList(notHoldLotDemandTypes);
			for (String demandType : demandTypesList) {
				deleteByPlanCodeSendStatusDemandType(planCode, sendStatus, demandType);
			}
		}

	}

	public List<PreProductionLot> findAllPreviousLotsByPlanCode(String planCode, int processedRowCount) {
		Parameters params = Parameters.with("1", planCode);

		String sql = FIND_A_NUMBER_OF_PROCESSED_FOR_PLAN_CODES_ORDER_BY_SEQNUM + processedRowCount + " row only";
		return findAllByNativeQuery(sql, params);
	}

	public List<PreProductionLot> findAllUpcomingLotsByPlanCode(String planCode) {
		Parameters params = Parameters.with("planCode", planCode);
		return  findAllByQuery(FIND_UPCOMING_FOR_PLAN_CODES_ORDER_BY_SEQNUM,params);
	}

	public List<PreProductionLot> findAllUpcomingLotsByProductSpecCode(String productSpecCode) {
		Parameters params = Parameters.with("productSpecCode", productSpecCode);
		return  findAllByQuery(FIND_UPCOMING_FOR_PRODUCT_SPEC_CODES_ORDER_BY_SEQNUM,params);
	}

	public List<PreProductionLot> findAllOnHoldLotsByPlanCode(String planCode) {
		Parameters params = Parameters.with("planCode", planCode);
		params.put("holdStatus", 0);
		return findAll(params,new String[]{"productionLot"});
	}

	public PreProductionLot findByNextProdLot(String nextProductionLot)
	{
		return findFirst(Parameters.with("nextProductionLot", nextProductionLot));
	}

	public List<PreProductionLot> findCurrentDayLastServiceLots(String serviceLotPrefix, String processLocation)
	{
		String date = (new SimpleDateFormat("yyyyMMdd")).format(new java.util.Date());
		Parameters params = Parameters.with("lotPrefix", (serviceLotPrefix+processLocation+date+"%").trim());
		return findAllByQuery(IS_LOT_PREFIX_EXIST, params);

	}

	public List<String> findUpcomingLotsForProcessPointCsv(String productionLot,int count, String processPoint){
		Parameters parameters = Parameters.with("1", productionLot);
		parameters.put("2", processPoint);
		List<String> upcomingPreProductionLots = null;
		upcomingPreProductionLots = findAllByNativeQuery(String.format(UPCOMING_LOTS,processPoint), parameters, String.class, count);
		return upcomingPreProductionLots;
	}

	public PreProductionLot findNextLotBySequence(PreProductionLot currentLot) {

		Parameters parameters = Parameters.with("1", currentLot.getSequence());
		parameters.put("2", currentLot.getPlanCode());
		return findFirstByNativeQuery(FIND_NEXT_LOT_BY_SEQUENCE_AND_PLAN_CODE, parameters);

	}

	public PreProductionLot findCurrentLotByPlanCodeOrderByLinkedList(String planCode)
	{
		PreProductionLot currentLot=null;
		Parameters params = Parameters.with("planCode", CommonUtil.toList(planCode));
		params.put("sendStatusId", 1).put("holdStatus", 1);
		currentLot=findCurrentLotBySendStatus(params);
		if(currentLot==null){
			params.put("sendStatusId", 0);
			currentLot=findCurrentLotBySendStatus(params);
		}
		return currentLot;
	}

	public PreProductionLot findCurrentLotBySendStatus(Parameters params)
	{
		List<PreProductionLot> lotList=findAllByQuery(FIND_CURRENT_LOT,params);
		//if only one lot was returned by query, return it
		if(lotList.size()==1)
			return lotList.get(0);
		//if more than one lot was returned by query, sort it by linked list and return the first in the sorted linked list
		else if(lotList.size()>1){
			lotList = sort(lotList);
			if(lotList.size()>0){
				return lotList.get(0);
			}
		}
		return null;
	}

	public Object[] getKdStampCountLotSize(String kdLot, String processLocation) {
		Parameters params = Parameters.with("1", kdLot.substring(0, 16) + "%");
		params.put("2", processLocation);
		return findFirstByNativeQuery(GET_KD_LOT_SIZE, params, Object[].class);
	}


	public List<PreProductionLot> findAllByPlanCodeSort(String planCode) {
		return sort(findAllByPlanCode(planCode));
	}

	@Override
	public List<PreProductionLot> findAllForProcessLocationAndPlanCode(String processLocation, String planCode) {
		Parameters params = Parameters.with("1", processLocation);
		params.put("2",planCode);
		return findAllByNativeQuery(FIND_SCHEDULE_FOR_PLAN_CODE, params);
	}

	public List<PreProductionLot> findAllNonMassProductionLotsByPlanCodeAndCreateDate(String planCode, Timestamp createDate){
		Parameters params = Parameters.with("planCode", planCode);
		params.put("createDate",createDate);
		
		String demandTypeCriteria = " and e.demandType <> 'MP' order by e.sequence asc";
	
		return findAllByQuery(FIND_ALL_BY_PLAN_CODE_CREATE_DATE+ demandTypeCriteria , params);
	}
	
	public List<PreProductionLot> findAllMassProductionLotsByPlanCodeAndCreateDate(String planCode, Timestamp createDate){
		Parameters params = Parameters.with("planCode", planCode);
		params.put("createDate",createDate);
		
		String demandTypeCriteria = " and e.demandType = 'MP' order by e.sequence asc";
	
		return findAllByQuery(FIND_ALL_BY_PLAN_CODE_CREATE_DATE+ demandTypeCriteria , params);
	}

	private static String FIND_BY_STATUS_CODE_AND_LIKE_SPECs = "select distinct PRODUCT_SPEC_CODE FROM gal212tbx where SEND_STATUS < ?1 AND STAMPED_COUNT=0 AND (PRODUCT_SPEC_CODE like ?2 REPLACE_WITH_EXTRA_SPEC_CODES)";
	public List<String> findDistinctProdSpecCodeBySpecCodeAndStatus(List<String> specCodeList, Integer sendStatusCode){
		Parameters params = Parameters.with("1", sendStatusCode);
		if(specCodeList.size() > 1) {
			for(Integer i=1; i<specCodeList.size(); i++) {
				FIND_BY_STATUS_CODE_AND_LIKE_SPECs = FIND_BY_STATUS_CODE_AND_LIKE_SPECs.replace("REPLACE_WITH_EXTRA_SPEC_CODES", "OR PRODUCT_SPEC_CODE like ?"+ (i+2) + " REPLACE_WITH_EXTRA_SPEC_CODES");
			}
		}
		FIND_BY_STATUS_CODE_AND_LIKE_SPECs = FIND_BY_STATUS_CODE_AND_LIKE_SPECs.replace("REPLACE_WITH_EXTRA_SPEC_CODES", "");
		for(Integer i=0; i<specCodeList.size(); i++) {
			params.put(""+(i+2), specCodeList.get(i) + "%");
		}
		return findAllByNativeQuery(FIND_BY_STATUS_CODE_AND_LIKE_SPECs, params, String.class);
	}
	
	public List<PreProductionLot> getProductionLots(String productId) {
		Parameters parameters = Parameters.with("1", productId);
		List<PreProductionLot> frameSpecDtoList = findAllByNativeQuery(GET_PRODUCTION_LOT_DETAILS, parameters);
		return frameSpecDtoList;
	}
	
	@Transactional
	public void updatePreProdLotDetails(String productCodeForUpdate,
			FrameSpecDto selectedFrameSpecDto, List<String> productionLots) {
		
		Parameters params2 = Parameters.with("1", productCodeForUpdate)
				.put("2", selectedFrameSpecDto.getProductSpecCode());
		
		String sqlStr = UPDATE_COLOR_DETAILS_1;
		sqlStr = sqlStr.replaceAll(":productionLot", StringUtil.toSqlInString(productionLots));
		executeNative(sqlStr, params2);
	}
	
	public boolean findByProdSpecCode(String productSpecCode) {
		Parameters parameters = Parameters.with("productSpecCode", productSpecCode);
		List<PreProductionLot> preProdLot = findAll(parameters);
		
		if(preProdLot.size()>0)
			return true;
		else
			return false;
	}
	
	public List<String> getNonShippedProductionLotsByPlanCode(String planeCode) {
		Parameters params = Parameters.with("1", planeCode);
		List<String> productionLotList = findAllByNativeQuery(FIND_NON_SHIPPED_LOTS_BY_PLAN_CODE, params, String.class);
		return productionLotList;
	}
	
	public boolean isModelInSchedule(String modelPrefix) {
		String likeModelPrefix;
		{
			StringBuilder likeModelPrefixBuilder = new StringBuilder();
			likeModelPrefixBuilder.append(modelPrefix);
			likeModelPrefixBuilder.append("%");
			likeModelPrefix = likeModelPrefixBuilder.toString();
		}
		Parameters params = Parameters.with("specCodePrefix", likeModelPrefix);
		PreProductionLot result = findFirstByQuery(IS_MODEL_EXIST, params);
		return result != null;
	}
	
	public boolean isLotStamped(String lotPrefix, String productionDate) {
				
		String likeLotPrefix;
		{
			StringBuilder likeLotPrefixBuilder = new StringBuilder();
			likeLotPrefixBuilder.append(lotPrefix);
			likeLotPrefixBuilder.append(productionDate);
			likeLotPrefixBuilder.append("%");
			likeLotPrefix = likeLotPrefixBuilder.toString();
		}
		Parameters params = Parameters.with("lotPrefix", likeLotPrefix);
		
		
		PreProductionLot result = findFirstByQuery(IS_LOT_STAMPED, params);
		if(result != null)logger.info(result.toString());
		return !(result == null);
	}
	
	@Deprecated
	public boolean isPassedProductionLot (String processLocation, String planCode, String startProdLot, String currentProdLot) {
		Parameters parameters = Parameters.with("1", processLocation)
				.put("2", planCode).put("3", startProdLot).put("4", currentProdLot);
		if(StringUtils.equals(startProdLot, currentProdLot)) {
			return true;
		} else {
			String isPassedLot = findFirstByNativeQuery(FIND_STARTING_PROD_LOT_CHECK, parameters, String.class);
			if(StringUtils.equals(isPassedLot, "Passed")) 
				return true;
			else
				return false;
		}
	}
	
	public boolean isPassedProductionLot (String processPointId, String startProdLot) {
		Parameters parameters = Parameters.with("1", processPointId)
				.put("2", startProdLot);
		String isPassedLot = findFirstByNativeQuery(FIND_STARTING_PROD_LOT_CHECK_NEW, parameters, String.class);
		if(StringUtils.equals(isPassedLot, "Passed")) 
			return true;
		else
			return false;
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public Map<String, List<String>> getProductionProgress(Integer prodProgressType, List<String> processPointOn,
			List<String> processPointOff, String div, List<String> lines, Boolean allowDBUpdate,
			Boolean useSequenceForBuildSequence, Integer sequenceNumberScale, Boolean excludeListedPlanCodes,
			List<String> planCodesToExclude,List<String> exceptionProcessPoints,Timestamp runTimestamp) {
		int vLotSize = 0;
		int vRemainingLot = 0;
		int vCountOn = 0;
		int vCountOff = 0;
		int vScrap = 0;
		int vScrapOn = 0;
		int vScrapOff = 0;
		String vProductionLot = null;

		
		StringBuffer qry = new StringBuffer();
		
		qry = new StringBuffer(SELECT_PRODUCTION_PROGRESS);
		
		Parameters params = Parameters.with("1", runTimestamp);
		String query = qry.toString().replace("@ON_PROCESSPOINTS@",StringUtil.toSqlInString(processPointOn));
		query = query.replace("@OF_PROCESSPOINTS@",StringUtil.toSqlInString(processPointOff));
		query = query.replace("@EXCEPTION_PROCESSPOINTS@",StringUtil.toSqlInString(exceptionProcessPoints));
		query = query.replace("@PROCESS_LOCATION@","AF");
		
				
		logger.info("The query for " + div + ": " + query);

		StringBuffer vRecord = new StringBuffer();
		Map<String, List<String>> retMap = new HashMap<String, List<String>>();

		List<String> gpp102Records = new ArrayList<String>();
		List<String> giv707Records = new ArrayList<String>();
		List<String> updatedRecords = new ArrayList<String>();
		//Get Production Progress
		try {

			java.sql.Timestamp vCreateTimestamp = new java.sql.Timestamp((new java.util.Date()).getTime());
			String aCreateTimestamp =
					vCreateTimestamp.toString().substring(0, 4)
					+ vCreateTimestamp.toString().substring(5, 7)
					+ vCreateTimestamp.toString().substring(8, 10)
					+ vCreateTimestamp.toString().substring(11, 13)
					+ vCreateTimestamp.toString().substring(14, 16)
					+ vCreateTimestamp.toString().substring(17, 19);

			@SuppressWarnings("unchecked")
			List<Object[]> prodProgressList = findResultListByNativeQuery(query,params);

			if(prodProgressList != null && !prodProgressList.isEmpty()) {
				for (Object[] objArr : prodProgressList) {
					// Clean up the string buffer
					vRecord.delete(0, vRecord.length());
					// Result set returns the following fields :
					// 0 - Production_Lot
					// 1 - Lot_Size
					// 2 - Plan_Code
					// 3 - Line_No
					// 4 - Process_Location
					// 5 - Product_Spec_Code
					// 6 - Number of vins with the production lot that are "ON"  in GALADM.gal215tbx
					// 7 - Number of vins with the production lot that are "OFF"  in GALADM.gal215tbx
					// 8 - Number of scrap and exception with the production lot that are "ON" in GALADM.gal215tbx
					//9 - Number of scrap and exception with the production lot that are "OFF" in GALADM.gal215tbx
					//10 - Hold Status
					//11 - Lot number
					//12 - sequence
					vLotSize = Integer.parseInt(objArr[1].toString());
					vRemainingLot = vLotSize;
					vCountOn = Integer.parseInt(objArr[6].toString());
					vCountOff = Integer.parseInt(objArr[7].toString());
					vScrapOn = Integer.parseInt(objArr[8].toString());
					vScrapOff = Integer.parseInt(objArr[9].toString());
					//productionLot
					vProductionLot = objArr[0].toString();
					for (int index = 1; index <= 2; index++) {
						// Building count number for ON
						vRecord.delete(0, vRecord.length());
						switch (index) {
						case 1 :
						{
							vRemainingLot = vLotSize - vCountOn - vScrapOn;
							break;
						}
						case 2 :
						{
							vRemainingLot = vLotSize - vCountOff -  vScrapOff;
							break;
						}
						default :
						{
							//This should not happen
							break;
						}
						}
						if (vRemainingLot < 0) {
							logger.error("Production lot " + vProductionLot + " has remaining lot < 0. vRemaining Lot=" + vRemainingLot + "; vLotSize=" + vLotSize + "; vCountOn=" + vCountOn + "; vCountOff=" + vCountOff + "; scrapCount= " + vScrap + "; scrapCountOn= " + vScrapOn + "; scrapCountOff= " + vScrapOff);
						}
						if (vRemainingLot <= 0) {
							if (allowDBUpdate && index == 2 && (div.equals("AE") || div.equals("AF"))) // if OFF
							{
								try {
									logger.info("***Updating lot status to 4 with SQL = " + UPDATE_LOT_STATUS + "; existing lot status = " + objArr[13]);

									Parameters parameters = Parameters.with("1", vProductionLot);
									parameters.put("2", 4);
									executeNativeUpdate(UPDATE_LOT_STATUS, parameters);
									updatedRecords.add("Production lot = " + vProductionLot + "; previous lot status = " + objArr[13] + ", updated lot status = 4"); 
								} catch (Exception e) {
									logger.error("Unable to update production lot to status=4. Production lot= " + vProductionLot);
								}
							}
							//If the number of units produced is equal to the number of lot size. Do not 
							//include the production lot in the report. Therefore, no AFOn and AFOff result
							//will be written to the output file. Skip and read next record
							continue;
						}
						if(prodProgressType == 102) {
							// 2 - Plan_Code, 3 - Line_No, 4 - Process_Location, 5 - Product_Spec_Code
							vRecord.append(objArr[2].toString());
							vRecord.append(objArr[3].toString());
							vRecord.append(div);
							vRecord.append(index);
							vRecord.append(vProductionLot.substring(8, 20));
							vRecord.append(objArr[5].toString().substring(0,22));
							vRecord.append(prefixLeadingZeros(String.valueOf(vLotSize), 5));// lot size
							vRecord.append(prefixLeadingZeros(String.valueOf(vRemainingLot), 5));//remaining qty
							vRecord.append(aCreateTimestamp);
							String hs = objArr[10].toString();//11 - Hold Status
							if(hs.equals("1")){
								vRecord.append("N");
								
									if(objArr[12] != null){
										vRecord.append(prefixLeadingZeros(String.valueOf(objArr[12].toString()),12));//generated sequence
									}else{
										vRecord.append(prefixLeadingZeros("",12));
									}
								
							}
							else{
								vRecord.append("Y");
								vRecord.append(prefixLeadingZeros(objArr[11].toString(),12));//lot number
							}
							vRecord.append("             ");
							
							gpp102Records.add(vRecord.toString());
							vRecord.delete(0, vRecord.length());
						}
						if(prodProgressType == 707) {

							//create merge record for GIV707
							// Result set returns the following fields :
							
							// 2 - Plan_Code
							// 3 - Line_No
							// 4 - Process_Location
							// 5 - Product_Spec_Code
							//10 - Hold Status
							//11 - Lot number
							
							StringBuffer vMergeRecord = new StringBuffer();
							vMergeRecord.append(objArr[2].toString());
							vMergeRecord.append(objArr[3].toString());
							vMergeRecord.append(div);
							vMergeRecord.append(index);
							vMergeRecord.append(vProductionLot.substring(8, 20));
							vMergeRecord.append(objArr[5].toString().substring(0, 22));
							vMergeRecord.append(objArr[11].toString());
							vMergeRecord.append("                                                          ");
							vMergeRecord.append(prefixLeadingZeros(String.valueOf(vLotSize),5));
							vMergeRecord.append(prefixLeadingZeros(String.valueOf(vRemainingLot),5));
							vMergeRecord.append(aCreateTimestamp);

							giv707Records.add(vMergeRecord.toString());
							vMergeRecord.delete(0,vMergeRecord.length());
						}
					}
				}
			}
			retMap.put("GPP102", gpp102Records);
			retMap.put("GIV707", giv707Records);
			retMap.put("UPDATED_PROD_LOTS", updatedRecords);
		} catch (Exception e) {
			logger.error("Exception Occured: " + e.getMessage());
			e.printStackTrace();
			retMap = null;
		}
		return retMap;
	}
	
	private String prefixLeadingZeros(String valueStr, int totalLength) throws Exception {
		StringBuilder paddedValue = new StringBuilder();
		if(valueStr.length() > totalLength)
			throw new Exception("Value length greater than allowable length.");
		else {
			int padLength = totalLength - valueStr.length();
			for(int i=0; i<padLength; i++) {
				paddedValue.append("0");
			}
			paddedValue.append(valueStr);
		}
		return paddedValue.toString();
	}
	
	@Override
	public List<PreProductionLot> findIncompleteChildLots(String planCode, String processLocation, String sendStatus,String planCodeSec, String processLocationSec) {

		Parameters params = Parameters.with("1", planCode)
						.put("2", processLocation)
						.put("3", sendStatus)
						.put("4", planCodeSec)
						.put("5", processLocationSec);

		return findAllByNativeQuery(FIND_INCOMPLETE_CHILD_LOTS, params);
	}
	
	@Override
	public List<PreProductionLot> findReplicatedLot(String planCode, String processLocation, Timestamp lookBackDate) {
		
		Parameters params = Parameters.with("1", planCode)
				.put("2", processLocation)
				.put("3", lookBackDate)
				.put("4", lookBackDate);
		
		return findAllByNativeQuery(FIND_REPLICATED_LOT, params);
	}

	@Override
	public List<PreProductionLot> findBylotnumberAndProdSpecCodeAndDestProcLoc(String lotNumber, String prodSpecCode, String destProcLoc) {
			String query = FIND_BY_LOT_NUMBER_AND_SPEC_CODE.replace("@LOT_NUMBER@", lotNumber).replace("@PROD_SPEC_CODE@", prodSpecCode).replace("@DEST_PROC_LOC@", destProcLoc);
			
			return findAllByNativeQuery(query, null);
		}
	
	
	@Override
	public List<PreProductionLot> findBylotnumber(String lotNumber, String parentProdLot) {
			String query = FIND_BY_LOT_NUMBER.replace("@LOT_NUMBER@", lotNumber).replace("@PARENT_PROD_LOT_NUMBER@", parentProdLot);
			
			return findAllByNativeQuery(query, null);
		}
	
	public List<PreProductionLot> findByProcessLocationAndLotNumber(String processLocation, String lotNumber) {
		return findAllByNativeQuery(FIND_BY_PRC_LOC_AND_LOT_NUMBER, Parameters.with("1", processLocation).put("2", lotNumber));
	}

	@Override
	public List<PreProductionLot> findParentLotOrder(String srcPlanCode, String srcProcLoc, String sourcePlanCode, String sourceProcLoc) {
		
		Parameters params = Parameters.with("1", srcPlanCode)
				.put("2", srcProcLoc)
				.put("3", sourcePlanCode)
				.put("4", sourceProcLoc);
		
		return findAllByNativeQuery(FIND_PARENT_ORDER, params);
	}

	@Override
	public List<PreProductionLot> findDuplicateSchedule(String destPlanCode, String destProcLoc) {
		Parameters params = Parameters.with("1", destPlanCode)
				.put("2", destProcLoc);
		return findAllByNativeQuery(FIND_DUPLICATE_SCHEDULE, params);		
	}
	
	@Override
	public List<LotSequenceDto> getUpcomingProductLotSequence(String planCode, String processPointId) {
		return getUpcomingProductLotSequence(planCode,processPointId, ProductType.FRAME.name());
	}
	
	@Override
	public List<LotSequenceDto> getUpcomingProductLotSequence(String planCode, String processPointId, String productType) {
		Parameters params = Parameters.with("1", planCode)
				.put("2", processPointId);
		if(productType.equalsIgnoreCase(ProductType.ENGINE.getProductName())) {
			return findAllByNativeQuery(GET_UPCOMPING_ENGINE_LOTS, params, LotSequenceDto.class);
		}
		return findAllByNativeQuery(GET_UPCOMPING_LOTS, params, LotSequenceDto.class);
	}
	
	@Override
	@Transactional
	public void removeReplicateLots(String processLocation, String productionLot) {
		Parameters params = Parameters.with("processLocation",processLocation)
				.put("productionLot", productionLot);
		executeUpdate(REMOVE_REPLICATE_LOTS, params);
	}

	@Override
	public List<Object[]> fetchLotInfoByProductId(String productId) {
		Logger.getLogger().info("Started Fetching Lot info for product " + productId);
		Parameters params = Parameters.with("1", productId);
		String sqlStr = FETCH_LOT_INFORMATION;
		Logger.getLogger().info("Constructed SQL query: {} " + sqlStr);
		return findAllByNativeQuery(sqlStr, params, Object[].class);
	}
}
