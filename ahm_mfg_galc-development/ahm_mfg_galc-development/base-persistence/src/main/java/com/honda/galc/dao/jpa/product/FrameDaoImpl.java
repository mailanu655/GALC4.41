package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.dto.ProcessProductDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

/**
 *
 * <h3>FrameDaoImpl Class description</h3>
 * <p> FrameDaoImpl description </p>
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
 *
 * </TABLE>
 *
 * @author Jeffray Huang<br>
 * Apr 13, 2011
 *
 *
 */
/** * *
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class FrameDaoImpl extends ProductDaoImpl<Frame> implements FrameDao {

	private static final long serialVersionUID = 1L;


	private static final String UPDATE_PRODUCT_START_DATE =
			"update galadm.gal143tbx set product_start_date = " +
					"(SELECT S.PRODUCTION_DATE FROM GALADM.GAL226TBX S " +
					"INNER JOIN GALADM.GAL238TBX G ON S.LINE_NO=G.GPCS_LINE_NO AND S.PROCESS_LOCATION=G.GPCS_PROCESS_LOCATION " +
					"INNER JOIN GALADM.GAL214TBX P ON G.DIVISION_ID=P.DIVISION_ID " +
					"WHERE P.PROCESS_POINT_ID=?1 AND S.START_TIMESTAMP <= current_timestamp AND current_timestamp <= S.END_TIMESTAMP) " +
					"where product_id = ?2";

	private static final String FIND_PRODUCTS_BY_PRODUCT_SEQUENCE =
			"SELECT A.* FROM GALADM.GAL143TBX A,GALADM.PRODUCT_SEQUENCE_TBX B " +
					"WHERE A.PRODUCT_ID = B.PRODUCT_ID AND B.PROCESS_POINT_ID =?1  " +
					"ORDER BY B.REFERENCE_TIMESTAMP ASC";

	private static final String FIND_ALL_BY_SNPP = "SELECT A.* FROM GAL143TBX A WHERE " +
			"TRIM(A.PRODUCT_ID) LIKE ?1 AND A.TRACKING_STATUS IN (" +
			"SELECT B.PREVIOUS_LINE_ID FROM GAL236TBX B WHERE B.LINE_ID = (" +
			"SELECT C.LINE_ID FROM GAL195TBX C WHERE EXISTS (" +
			"SELECT 1 FROM GAL214TBX D WHERE C.LINE_ID = D.LINE_ID AND D.PROCESS_POINT_ID = ?2 " +
			"FETCH FIRST ROW ONLY) FETCH FIRST ROW ONLY)) ORDER BY A.PRODUCT_ID";

	private static final String FIND_PROCESSED_PRODUCTS_BY_IN_PROCESS_PRODUCT =
			"WITH SortedList (PRODUCT_ID, NEXT_PRODUCT_ID, Level) AS(" +
					"SELECT PRODUCT_ID, NEXT_PRODUCT_ID, 0 as Level FROM GALADM.GAL176tbx " +
					"WHERE PRODUCT_ID = ? " +
					"UNION ALL " +
					"SELECT ll.PRODUCT_ID, ll.NEXT_PRODUCT_ID, Level+1 as Level FROM GALADM.GAL176tbx ll, SortedList as s " +
					"WHERE ll.NEXT_PRODUCT_ID = s.PRODUCT_ID) " +
					"SELECT b.* FROM SortedList a, GALADM.GAL143TBX b WHERE a.PRODUCT_ID = b.PRODUCT_ID ";

	private static final String FIND_UPCOMING_PRODUCTS_BY_IN_PROCESS_PRODUCT =
			"WITH SortedList (PRODUCT_ID, NEXT_PRODUCT_ID, Level) AS(" +
					"SELECT PRODUCT_ID, NEXT_PRODUCT_ID, 0 as Level FROM GALADM.GAL176tbx " +
					"WHERE PRODUCT_ID = ? " +
					"UNION ALL " +
					"SELECT ll.PRODUCT_ID, ll.NEXT_PRODUCT_ID, Level+1 as Level FROM GALADM.GAL176tbx ll, SortedList as s " +
					"WHERE ll.PRODUCT_ID = s.NEXT_PRODUCT_ID) " +
					"SELECT b.* FROM SortedList a, GALADM.GAL143TBX b WHERE a.PRODUCT_ID = b.PRODUCT_ID " ;
	private static final String FIND_PROCESSED_PRODUCTS_BY_CARRIER_TBX = 
			"select COALESCE(b.PRODUCT_ID, a.PRODUCT_ID) AS PRODUCT_ID, b.ENGINE_SERIAL_NO, b.MISSION_SERIAL_NO, b.STRAIGHT_SHIP_PERCENTAGE, " + 
			"b.KEY_NO, b.SHORT_VIN, b.PRODUCTION_LOT, b.KD_LOT_NUMBER, b.PRODUCT_SPEC_CODE,b.PLAN_OFF_DATE, b.TRACKING_STATUS, b.PRODUCT_START_DATE, " +
			"b.ACTUAL_OFF_DATE, b.AUTO_HOLD_STATUS, b.PRODUCTION_DATE, b.LAST_PASSING_PROCESS_POINT_ID, b.ENGINE_STATUS, " +
			"COALESCE(b.AF_ON_SEQUENCE_NUMBER, a.CARRIER_ID) AS AF_ON_SEQUENCE_NUMBER, b.ACTUAL_MISSION_TYPE, b.CREATE_TIMESTAMP,b.UPDATE_TIMESTAMP, b.PURCHASE_CONTRACT_NUMBER " + 
			"from GALADM.PRODUCT_CARRIER_TBX a " + 
			"        left outer join GALADM.GAL143TBX b on a.product_id = b.PRODUCT_ID " + 
			"where a.PROCESS_POINT_ID = ?1 " + 
			"        and a.ON_TIMESTAMP <= (select max(z.ON_TIMESTAMP) from GALADM.PRODUCT_CARRIER_TBX z where z.PROCESS_POINT_ID = ?1 and z.PRODUCT_ID = ?2) " + 
			"order by a.ON_TIMESTAMP desc ";
	
	private static final String FIND_UPCOMING_PRODUCTS_BY_CARRIER_TBX = 
			"select COALESCE(b.PRODUCT_ID, a.PRODUCT_ID) AS PRODUCT_ID, b.ENGINE_SERIAL_NO, b.MISSION_SERIAL_NO, b.STRAIGHT_SHIP_PERCENTAGE, " + 
			"b.KEY_NO, b.SHORT_VIN, b.PRODUCTION_LOT, b.KD_LOT_NUMBER, b.PRODUCT_SPEC_CODE,b.PLAN_OFF_DATE, b.TRACKING_STATUS, b.PRODUCT_START_DATE, " +
			"b.ACTUAL_OFF_DATE, b.AUTO_HOLD_STATUS, b.PRODUCTION_DATE, b.LAST_PASSING_PROCESS_POINT_ID, b.ENGINE_STATUS, " +
			"COALESCE(b.AF_ON_SEQUENCE_NUMBER, a.CARRIER_ID) AS AF_ON_SEQUENCE_NUMBER, b.ACTUAL_MISSION_TYPE, b.CREATE_TIMESTAMP,b.UPDATE_TIMESTAMP, b.PURCHASE_CONTRACT_NUMBER " + 
			"from GALADM.PRODUCT_CARRIER_TBX a " + 
			"        left outer join GALADM.GAL143TBX b on a.product_id = b.PRODUCT_ID " + 
			"where a.PROCESS_POINT_ID = ?1 " + 
			"        and a.ON_TIMESTAMP >= (select max(z.ON_TIMESTAMP) from GALADM.PRODUCT_CARRIER_TBX z where z.PROCESS_POINT_ID = ?1 and z.PRODUCT_ID = ?2) " + 
			"order by a.ON_TIMESTAMP ";

	private static final String ORDER_BY = " ORDER BY Level asc fetch first ";
	private static final String ROWS_ONLY = " rows only with ur";

	private final String FIND_BY_AF_SEQ_NUM = "SELECT A.* FROM GALADM.GAL143TBX A " +
			"JOIN GALADM.GAL215TBX B ON A.PRODUCT_ID=B.PRODUCT_ID AND B.PROCESS_POINT_ID= ?2 " +
			"WHERE A.AF_ON_SEQUENCE_NUMBER = ?1 AND B.ACTUAL_TIMESTAMP >= " +
			"(SELECT max(C.ACTUAL_TIMESTAMP) FROM GALADM.GAL143TBX D JOIN GALADM.GAL215TBX C " +
			"ON C.PRODUCT_ID=D.PRODUCT_ID AND C.PROCESS_POINT_ID= ?2 " +
			"WHERE D.AF_ON_SEQUENCE_NUMBER = ?1) ORDER BY A.CREATE_TIMESTAMP";

	private final String FIND_BY_AF_SEQ_RANGE = " SELECT A.AF_ON_SEQUENCE_NUMBER, A.PRODUCT_ID, " +
			"A.PRODUCTION_DATE, A.PRODUCT_SPEC_CODE FROM GAL143TBX A JOIN GALADM.GAL215TBX B " +
			"ON A.PRODUCT_ID=B.PRODUCT_ID AND B.PROCESS_POINT_ID= ?3 " +
			"WHERE A.AF_ON_SEQUENCE_NUMBER >= ?1 AND A.AF_ON_SEQUENCE_NUMBER <= ?2 " +
			" AND B.ACTUAL_TIMESTAMP >= " +
			"(SELECT max(C.ACTUAL_TIMESTAMP) FROM GALADM.GAL143TBX D JOIN GALADM.GAL215TBX C " +
			"ON C.PRODUCT_ID=D.PRODUCT_ID AND C.PROCESS_POINT_ID= ?3 " +
			"WHERE D.AF_ON_SEQUENCE_NUMBER = ?1) ORDER BY A.AF_ON_SEQUENCE_NUMBER ";

	private static final String FIND_INVENTORY_COUNTS =
			"SELECT COUNT(*) as COUNT,LAST_PASSING_PROCESS_POINT_ID as PROCESS,  SUBSTR(KD_LOT_NUMBER,1,6) as PLANT " +
					"FROM GALADM.GAL143TBX " +
					"WHERE (DEFECT_STATUS IS NULL or DEFECT_STATUS < 3) AND AUTO_HOLD_STATUS <>1 " +
					"GROUP BY LAST_PASSING_PROCESS_POINT_ID,SUBSTR(KD_LOT_NUMBER,1,6) " +
					"ORDER BY PROCESS,PLANT";

	private final  String FIND_ELIGIBLE_PRODUCTS = "WITH ID_ORDERED (PRODUCT_ID, NEXT_PRODUCT_ID, ID_SEQ) "
			+ "AS (SELECT ID_START.PRODUCT_ID, ID_START.NEXT_PRODUCT_ID, 1 AS ID_SEQ "
			+ " FROM GALADM.GAL176TBX ID_START "
			+ "WHERE     ID_START.NEXT_PRODUCT_ID IS NULL "
			+ " AND ID_START.LINE_ID = ?1 "
			+ "  UNION ALL "
			+ " SELECT ID_NEXT.PRODUCT_ID, "
			+ " ID_NEXT.NEXT_PRODUCT_ID, "
			+ " s.ID_SEQ + 1 AS ID_SEQ "
			+ " FROM GALADM.GAL176TBX ID_NEXT, ID_ORDERED AS s "
			+ " WHERE ID_NEXT.NEXT_PRODUCT_ID = s.PRODUCT_ID) "
			+ "SELECT  "
			+ " NON_INSTALLED.PRODUCT_ID "
			+ " FROM (SELECT ROW_NUMBER () OVER (ORDER BY ID_ORDERED.ID_SEQ DESC) "
			+ "AS LINE_SEQ_NUM, "
			+ " ROW_NUMBER () "
			+ " OVER (PARTITION BY INSTALLED_PART.INSTALLED_PART_STATUS "
			+ " ORDER BY ID_ORDERED.ID_SEQ DESC) "
			+ " AS PRINT_ELIGIBLE_SEQ_NUM, "
			+ "FRAME.PRODUCT_ID, "
			+ "ID_ORDERED.NEXT_PRODUCT_ID, "
			+ "MTOC.PRODUCT_SPEC_CODE, "
			+ " RULES.PART_NAME, "
			+ " INSTALLED_PART.PART_NAME AS INSTALLED_PART_NAME, "
			+ " INSTALLED_PART.INSTALLED_PART_STATUS "
			+ " FROM ID_ORDERED "
			+ " JOIN GALADM.GAL143TBX FRAME "
			+ " ON ID_ORDERED.PRODUCT_ID = FRAME.PRODUCT_ID "
			+ "JOIN GALADM.GAL144TBX MTOC "
			+ " ON FRAME.PRODUCT_SPEC_CODE = MTOC.PRODUCT_SPEC_CODE "
			+ " JOIN GALADM.GAL246TBX RULES "
			+ " ON (   RULES.MODEL_YEAR_CODE = '*' "
			+ " OR MTOC.MODEL_YEAR_CODE = RULES.MODEL_YEAR_CODE) "
			+ " AND (   RULES.MODEL_CODE = '*' "
			+ " OR MTOC.MODEL_CODE = RULES.MODEL_CODE) "
			+ " AND (   RULES.MODEL_TYPE_CODE = '*' "
			+ " OR MTOC.MODEL_TYPE_CODE = RULES.MODEL_TYPE_CODE) "
			+ " AND (   RULES.MODEL_OPTION_CODE = '*'  "
			+ "   OR MTOC.MODEL_OPTION_CODE = RULES.MODEL_OPTION_CODE) "
			+ "AND (   RULES.EXT_COLOR_CODE = '*'  "
			+ " OR MTOC.EXT_COLOR_CODE = RULES.EXT_COLOR_CODE) "
			+ "AND (   RULES.INT_COLOR_CODE = '*'  "
			+ " OR MTOC.INT_COLOR_CODE = RULES.INT_COLOR_CODE) "
			+ "AND RULES.PROCESS_POINT_ID = ?4 "
			+ " AND RULES.PART_NAME = ?5 "
			+ " LEFT JOIN GALADM.GAL185TBX INSTALLED_PART "
			+ " ON     ID_ORDERED.PRODUCT_ID = INSTALLED_PART.PRODUCT_ID "
			+ "  AND INSTALLED_PART.PART_NAME = RULES.PART_NAME "
			+ "WHERE    INSTALLED_PART.INSTALLED_PART_STATUS IS NULL  "
			+ " OR INSTALLED_PART.INSTALLED_PART_STATUS < 0) AS NON_INSTALLED "
			+ "WHERE     NON_INSTALLED.LINE_SEQ_NUM <= ?2 "
			+ " AND NON_INSTALLED.PRINT_ELIGIBLE_SEQ_NUM <= ?3 "
			+ "AND NON_INSTALLED.INSTALLED_PART_STATUS IS NULL WITH CS FOR READ ONLY";

	private final String FIND_BY_MODEL_AND_SEQ_NUM = " SELECT T1.Product_ID From GALADM.GAL143TBX T1 "
			+ " WHERE SUBSTR(T1.Product_ID, 12, 6) = ?1 AND SUBSTR(T1.PRODUCT_SPEC_CODE,1,4)= ?2 ";

	private final String GET_PRODUCTS_WITHIN_AFON_RANGE = "SELECT Product_ID, Product_Spec_Code, char(Production_Date), Production_Lot FROM GAL143TBX  " +
			"WHERE  AF_ON_SEQUENCE_NUMBER >= ?1  AND AF_ON_SEQUENCE_NUMBER <= ?2  ORDER BY  Production_Date desc , AF_ON_SEQUENCE_NUMBER desc " ;

	private final String GET_PRODUCTS_WITHIN_RANGE = "SELECT Product_ID, Product_Spec_Code, char(Production_Date), Production_Lot FROM GAL143TBX ";

	private final String GET_PRODUCTS_WITHIN_RANGE_FILTER = "WHERE SUBSTR(Product_ID, 1, 2) = ?1 " +
			"AND SUBSTR(Product_ID, 12,6) >= ?2 " +
			"AND SUBSTR(Product_ID, 12,6) <= ?3 " +
			"AND SUBSTR(Product_ID, 10, 1) = ?4 " +
			"ORDER BY SUBSTR(Product_ID, 12, 6)" ;

	private final String GET_PRODUCTS_BY_ENG_MTOC = "SELECT A.PRODUCT_ID FROM GAL143TBX A INNER JOIN GAL144TBX B ON A.PRODUCT_SPEC_CODE = B.PRODUCT_SPEC_CODE" +
			" WHERE A.ENGINE_SERIAL_NO IS NULL AND B.ENGINE_MTO = ?1 AND A.PRODUCTION_DATE < VARCHAR_FORMAT((CURRENT DATE + ?2 DAYS),'YYYY-MM-DD') " +
			"AND TRACKING_STATUS NOT IN (:notSellableTrackingStatus)";

	private static final String COUNT_BY_AF_ON_SEQUENCE_NUMBER_RANGE = "select count(e) from Frame e where e.afOnSequenceNumber >= :startSequence  and e.afOnSequenceNumber <= :endSequence";

	private static final String SELECT_BY_AF_ON_SEQUENCE_NUMBER_RANGE = "select e from Frame e where e.afOnSequenceNumber >= :startSequence  and e.afOnSequenceNumber <= :endSequence order by e.afOnSequenceNumber";

	private static final String FIND_AF_ON_SEQUENCE_NUMBER_BY_SHORT_SEQUENCE = "select max(e.afOnSequenceNumber) from Frame e where mod(e.afOnSequenceNumber, :divider) = :shortSequence";

	private static String GET_FRAME_LINE_MTO_DATA="SELECT T1.PRODUCT_ID, T1.PRODUCT_SPEC_CODE, T2.ENGINE_MTO, T3.LINE_NAME, T4.STATUS, T1.KD_LOT_NUMBER, T1.LAST_PASSING_PROCESS_POINT_ID from GAL144TBX T2, GAL195TBX T3, GAL143TBX T1 left join GAL263TBX T4 on T1.PRODUCT_ID = T4.VIN where T1.PRODUCTION_LOT = ?1 and T1.PRODUCT_SPEC_CODE = T2.PRODUCT_SPEC_CODE and T1.TRACKING_STATUS = T3.LINE_ID and T1.TRACKING_STATUS != ?2 and T1.TRACKING_STATUS != ?3 order by T1.SHORT_VIN";

	private static String GET_SCRAPED_EXCEPTIONAL_COUNT="SELECT COUNT(*) From GAL143TBX T1, GAL217TBX T2 WHERE T2.PRODUCTION_LOT = ?1 AND (RTRIM(T1.Tracking_Status) = ?2 OR RTRIM(T1.Tracking_Status) = ?3) AND T1.PRODUCTION_LOT = T2.PRODUCTION_LOT";

	private static final String SELECT_VQ_VIN =
			"select T1.PRODUCT_ID,concat(concat(substr(T1.KD_LOT_NUMBER, 6,1),substr(T1.KD_LOT_NUMBER, 9,4))," +
					"concat(substr(T1.KD_LOT_NUMBER, 14,3),substr(T1.KD_LOT_NUMBER, 18,1))), " +
					"concat(substr(T1.production_lot,6,1),concat(SUBSTR(T1.production_Lot,11,10), ' ')), " +
					"t2.SALES_MODEL_CODE, " +
					"concat(t2.SALES_MODEL_TYPE_CODE, '  ')," +
					"'   '," +
					"concat(t2.SALES_EXT_COLOR_CODE,' ')," +
					"t2.SALES_INT_COLOR_CODE," +
					"substr(t2.product_spec_code,1,4)," +
					"t2.MODEL_TYPE_CODE," +
					"t2.MODEL_OPTION_CODE," +
					"t2.EXT_COLOR_CODE," +
					"t2.INT_COLOR_CODE," +
					"t1.ACTUAL_OFF_DATE," +
					"(select max(update_timestamp) " +
					"from galadm.gal143tbx " +
					"where tracking_status in (@TRACKING_STATUS@))," +
					"T2.product_spec_code from galadm.gal143tbx T1, galadm.GAL144TBX T2 where tracking_status in (@TRACKING_STATUS@) AND t1.product_spec_code = t2.product_spec_code";

	private static final String SELECT_EXCEPTIONAL_OUT_VINS_FOR_XM_RADIO =
			"SELECT a.product_id, " +
					"c.SERIES_DESCRIPTION," +
					"c.MODEL_YEAR_DESCRIPTION," +
					"b.actual_timestamp," +
					"b.part_serial_number, " +
					"c.model_code " +
					"from GAL143TBX a, GAL185TBX b, gal144tbx c " +
					"WHERE a.TRACKING_STATUS = ?1 " +
					"and c.sales_model_type_code in (@COUNTRY_CODE@) " +
					"and a.product_spec_code = c.product_spec_code " +
					"and a.product_id = b.product_id " +
					"and b.part_name like ?2 " +
					"and TRIM(b.part_serial_number) != '' " +
					"and a.UPDATE_TIMESTAMP >= ?3 " +
					"for read only";

	private static final String SELECT_EXOUT_XM_OR_TCU =
			"with XM (PRODUCT_ID,PART_NAME,PART_SERIAL_NUMBER,ACTUAL_TIMESTAMP)  "
					+ " as (select PRODUCT_ID,PART_NAME,PART_SERIAL_NUMBER,ACTUAL_TIMESTAMP  "
					+ " from galadm.gal185tbx where (@PART_NAMES@)  )"
					+ " select  "
					+ " a.product_id,  "  //index 0
					+ " c.SERIES_DESCRIPTION,c.MODEL_YEAR_DESCRIPTION,c.model_code, XM.PART_NAME, "  //1-4
					+ " XM.PART_SERIAL_NUMBER as XM_SN, XM.ACTUAL_TIMESTAMP AS XM_TS,   "  //5-6
					+ " (select max(actual_timestamp) as VQ_OFF_TS from galadm.gal215tbx H1 where H1.PRODUCT_ID=A.PRODUCT_ID and H1.PROCESS_POINT_ID = ?1), "  //7
					+ " (select max(actual_timestamp) as AF_OFF_TS from galadm.gal215tbx H2 where H2.PRODUCT_ID=A.PRODUCT_ID and H2.PROCESS_POINT_ID = ?2) "  //8
					+ " FROM XM  "
					+ " join galadm.gal143tbx a on XM.PRODUCT_ID=a.PRODUCT_ID "
					+ " join galadm.gal144tbx c on a.product_spec_code = c.product_spec_code  "
					+ " where c.sales_model_type_code in (@COUNTRY_CODE@)  "
					+ " and a.UPDATE_TIMESTAMP >= ?3  "
					+ " and a.TRACKING_STATUS = ?4   order by a.product_id";

	private static final String SELECT_VQ_SHIP_VINS_FOR_XM_RADIO =
			"SELECT a.product_id, " +
					"c.SERIES_DESCRIPTION, " +
					"c.MODEL_YEAR_DESCRIPTION, " +
					"b.actual_timestamp, " +
					"b.part_serial_number, " +
					"c.model_code " +
					"from GAL143TBX a, GAL185TBX b, gal144tbx c " +
					"where c.sales_model_type_code in (@COUNTRY_CODE@) " +
					"and a.product_spec_code = c.product_spec_code " +
					"and a.product_id = b.product_id " +
					"and b.part_name like ?1 " +
					"and TRIM(b.part_serial_number) != '' " +
					"and a.product_id in " +
					" (select distinct PRODUCT_ID from GAL215TBX " +
					" where PROCESS_POINT_ID = ?2 " +
					" and ACTUAL_TIMESTAMP >= ?3) " +
					" for read only";

	private static final String SELECT_XM_OR_TCU =
			"with XM (PRODUCT_ID,PART_NAME,PART_SERIAL_NUMBER,ACTUAL_TIMESTAMP)  "
					+ " as (select PRODUCT_ID,PART_NAME,PART_SERIAL_NUMBER,ACTUAL_TIMESTAMP  "
					+ " from galadm.gal185tbx where (@PART_NAMES@) )  "
					+ " select  "
					+ " a.product_id,  "  //index 0
					+ " c.SERIES_DESCRIPTION,c.MODEL_YEAR_DESCRIPTION,c.model_code,XM.PART_NAME,  "  //1-4
					+ " XM.PART_SERIAL_NUMBER as XM_SN, XM.ACTUAL_TIMESTAMP AS XM_TS,   "  //5-6
					+ " (select max(actual_timestamp) as VQ_OFF_TS from galadm.gal215tbx H1 where H1.PRODUCT_ID=A.PRODUCT_ID and H1.PROCESS_POINT_ID = ?1), "  //7
					+ " (select max(actual_timestamp) as AF_OFF_TS from galadm.gal215tbx H2 where H2.PRODUCT_ID=A.PRODUCT_ID and H2.PROCESS_POINT_ID = ?2) "  //8
					+ " FROM XM  "
					+ " join galadm.gal143tbx a on XM.PRODUCT_ID=a.PRODUCT_ID  "
					+ " join galadm.gal144tbx c on a.product_spec_code = c.product_spec_code  "
					+ " where c.sales_model_type_code in (@COUNTRY_CODE@)  "
					+ " and a.product_id in  "
					+ "  (select distinct PRODUCT_ID from galadm.GAL215TBX  "
					+ "  where PROCESS_POINT_ID = ?1  "
					+ "  and ACTUAL_TIMESTAMP >= ?3) order by a.product_id";

	private static final String SELECT_XM_OR_TCU_WITH_SUB_PRODUCT =
			"with XM (PRODUCT_ID, PART_NAME, PART_SERIAL_NUMBER, ACTUAL_TIMESTAMP) "
					+ " as (select part1.product_id, part2.PART_NAME AS sub_product, part2.PART_SERIAL_NUMBER, part2.ACTUAL_TIMESTAMP "
					+ " from gal185tbx part1 "
					+ " INNER JOIN gal185tbx part2 on part1.PART_SERIAL_NUMBER = part2.PRODUCT_ID "
					+ " where part1.PART_NAME in (@SUB_PRODUCT_NAMES@) "
					+ " and part2.PART_NAME in (@PART_NAMES@)) "
					+ " select "
					+ " a.product_id, "
					+ " c.series_description, "
					+ " c.model_year_description, "
					+ " c.model_code, "
					+ " XM.part_name, "
					+ " XM.PART_SERIAL_NUMBER as XM_SN, "
					+ " XM.ACTUAL_TIMESTAMP AS XM_TS, "
					+ " (select max(actual_timestamp) as VQ_OFF_TS from gal215tbx H1 where H1.PRODUCT_ID=A.PRODUCT_ID and H1.PROCESS_POINT_ID = ?1), "
					+ " (select max(actual_timestamp) as AF_OFF_TS from gal215tbx H2 where H2.PRODUCT_ID=A.PRODUCT_ID and H2.PROCESS_POINT_ID = ?2) "
					+ " FROM XM "
					+ " join gal143tbx a on XM.PRODUCT_ID=a.PRODUCT_ID "
					+ " join gal144tbx c on a.product_spec_code = c.product_spec_code "
					+ " where c.sales_model_type_code in (@COUNTRY_CODE@) "
					+ " and a.product_id in "
					+ " (select distinct PRODUCT_ID from GAL215TBX "
					+ "	where PROCESS_POINT_ID = ?1 "
					+ "	and ACTUAL_TIMESTAMP >= ?3) "
					+ " order by a.product_id";

	private static final String SELECT_XM_OR_TCU_WITH_SUB_SUB_PRODUCT =
			"with XM (PRODUCT_ID, PART_NAME, PART_SERIAL_NUMBER, ACTUAL_TIMESTAMP) "
					+ " as (select part1.product_id, part3.PART_NAME AS sub_product, part3.PART_SERIAL_NUMBER, part3.ACTUAL_TIMESTAMP "
					+ " from gal185tbx part1 "
					+ " INNER JOIN gal185tbx part2 on part1.PART_SERIAL_NUMBER = part2.PRODUCT_ID "
					+ " INNER JOIN GAL185TBX part3 on part3.PRODUCT_ID = part2.PART_SERIAL_NUMBER"
					+ " where part1.PART_NAME in (@SUB_PRODUCT_NAMES@) "
					+ " and part2.PART_NAME in (@SUB_SUB_PRODUCT_NAMES@) "
					+ " and part3.PART_NAME in (@PART_NAMES@)) "
					+ " select "
					+ " a.product_id, "
					+ " c.series_description, "
					+ " c.model_year_description, "
					+ " c.model_code, "
					+ " XM.part_name, "
					+ " XM.PART_SERIAL_NUMBER as XM_SN, "
					+ " XM.ACTUAL_TIMESTAMP AS XM_TS, "
					+ " (select max(actual_timestamp) as VQ_OFF_TS from gal215tbx H1 where H1.PRODUCT_ID=A.PRODUCT_ID and H1.PROCESS_POINT_ID = ?1), "
					+ " (select max(actual_timestamp) as AF_OFF_TS from gal215tbx H2 where H2.PRODUCT_ID=A.PRODUCT_ID and H2.PROCESS_POINT_ID = ?2) "
					+ " FROM XM "
					+ " join gal143tbx a on XM.PRODUCT_ID=a.PRODUCT_ID "
					+ " join gal144tbx c on a.product_spec_code = c.product_spec_code "
					+ " where c.sales_model_type_code in (@COUNTRY_CODE@) "
					+ " and a.product_id in "
					+ " (select distinct PRODUCT_ID from GAL215TBX "
					+ "	where PROCESS_POINT_ID = ?1 "
					+ "	and ACTUAL_TIMESTAMP >= ?3) "
					+ " order by a.product_id";

	private static final String SELECT_EXOUT_XM_OR_TCU_WITH_SUB_PRODUCT =
			"with XM (PRODUCT_ID, PART_NAME, PART_SERIAL_NUMBER, ACTUAL_TIMESTAMP) "
					+ " as (select part1.product_id, part2.PART_NAME AS sub_product, part2.PART_SERIAL_NUMBER, part2.ACTUAL_TIMESTAMP "
					+ " from gal185tbx part1 "
					+ " INNER JOIN gal185tbx part2 on part1.PART_SERIAL_NUMBER = part2.PRODUCT_ID "
					+ " where part1.PART_NAME in (@SUB_PRODUCT_NAMES@) "
					+ " and part2.PART_NAME in (@PART_NAMES@)) "
					+ " select  "
					+ " a.product_id,  "  //index 0
					+ " c.SERIES_DESCRIPTION,c.MODEL_YEAR_DESCRIPTION,c.model_code, XM.PART_NAME, "  //1-4
					+ " XM.PART_SERIAL_NUMBER as XM_SN, XM.ACTUAL_TIMESTAMP AS XM_TS,   "  //5-6
					+ " (select max(actual_timestamp) as VQ_OFF_TS from galadm.gal215tbx H1 where H1.PRODUCT_ID=A.PRODUCT_ID and H1.PROCESS_POINT_ID = ?1), "  //7
					+ " (select max(actual_timestamp) as AF_OFF_TS from galadm.gal215tbx H2 where H2.PRODUCT_ID=A.PRODUCT_ID and H2.PROCESS_POINT_ID = ?2) "  //8
					+ " FROM XM  "
					+ " join galadm.gal143tbx a on XM.PRODUCT_ID=a.PRODUCT_ID "
					+ " join galadm.gal144tbx c on a.product_spec_code = c.product_spec_code  "
					+ " where c.sales_model_type_code in (@COUNTRY_CODE@)  "
					+ " and a.UPDATE_TIMESTAMP >= ?3  "
					+ " and a.TRACKING_STATUS = ?4   order by a.product_id";

	private static final String SELECT_EXOUT_XM_OR_TCU_WITH_SUB_SUB_PRODUCT =
			"with XM (PRODUCT_ID, PART_NAME, PART_SERIAL_NUMBER, ACTUAL_TIMESTAMP) "
					+ " as (select part1.product_id, part3.PART_NAME AS sub_product, part3.PART_SERIAL_NUMBER, part3.ACTUAL_TIMESTAMP "
					+ " from gal185tbx part1 "
					+ " INNER JOIN gal185tbx part2 on part1.PART_SERIAL_NUMBER = part2.PRODUCT_ID "
					+ " INNER JOIN gal185tbx part3 on part3.PRODUCT_ID = part2.PART_SERIAL_NUMBER "
					+ " where part1.PART_NAME in (@SUB_PRODUCT_NAMES@) "
					+ " and part2.PART_NAME in (@SUB_SUB_PRODUCT_NAMES@) "
					+ " and part3.PART_NAME in (@PART_NAMES@)) "
					+ " select  "
					+ " a.product_id,  "  //index 0
					+ " c.SERIES_DESCRIPTION,c.MODEL_YEAR_DESCRIPTION,c.model_code, XM.PART_NAME, "  //1-4
					+ " XM.PART_SERIAL_NUMBER as XM_SN, XM.ACTUAL_TIMESTAMP AS XM_TS,   "  //5-6
					+ " (select max(actual_timestamp) as VQ_OFF_TS from galadm.gal215tbx H1 where H1.PRODUCT_ID=A.PRODUCT_ID and H1.PROCESS_POINT_ID = ?1), "  //7
					+ " (select max(actual_timestamp) as AF_OFF_TS from galadm.gal215tbx H2 where H2.PRODUCT_ID=A.PRODUCT_ID and H2.PROCESS_POINT_ID = ?2) "  //8
					+ " FROM XM  "
					+ " join galadm.gal143tbx a on XM.PRODUCT_ID=a.PRODUCT_ID "
					+ " join galadm.gal144tbx c on a.product_spec_code = c.product_spec_code  "
					+ " where c.sales_model_type_code in (@COUNTRY_CODE@)  "
					+ " and a.UPDATE_TIMESTAMP >= ?3  "
					+ " and a.TRACKING_STATUS = ?4   order by a.product_id";

	private static final String SELECT_EXOUT_XM_OR_TCU_WITH_EXT_REQUIRED1 =
			"with PRODUCT_ID_LIST as (select PRODUCT_ID FROM GAL143TBX FRAME join GAL144TBX FRAME_SPEC on FRAME.PRODUCT_SPEC_CODE = FRAME_SPEC.PRODUCT_SPEC_CODE "
					+ " where FRAME_SPEC.sales_model_type_code in (@COUNTRY_CODE@) "
					+ " and FRAME.UPDATE_TIMESTAMP >= ?3  "
					+ " and FRAME.TRACKING_STATUS = ?4 "
					+ " order by FRAME.PRODUCT_ID) "
					+ " , PARTS as  (select PRODUCT_ID, PART_SERIAL_NUMBER, PART_NAME, PART_ID, ACTUAL_TIMESTAMP from GAL185TBX INSTALLED_SN where product_id <> '' and ( ";

	private static final String SELECT_EXOUT_XM_OR_TCU_WITH_EXT_REQUIRED2 =
					")) select distinct SECOND_PART.PRODUCT_ID as PRODUCT_ID "
					+ " ,FRAME_SPEC.SERIES_DESCRIPTION as SERIAL_DESCRIPTION "
					+ " ,FRAME_SPEC.MODEL_YEAR_DESCRIPTION as MODEL_YEAR_DESCRIPTION "
					+ " ,FRAME_SPEC.MODEL_CODE as MODEL_CODE "
					+ " ,case when SECOND_PART.SUB_PART_1_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_1_NAME "
					+ " else case when SECOND_PART.SUB_PART_2_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_2_NAME "
					+ "	else case when SECOND_PART.SUB_PART_3_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_3_NAME "
					+ " else case when PART_A3.PART_NAME IN (@PART_NAMES@) then PART_A3.PART_NAME "
					+ " end "
					+ " end "
					+ " end "
					+ " end as PART_NAME "
					+ ", case when SECOND_PART.SUB_PART_1_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_1 "
					+ " else case when SECOND_PART.SUB_PART_2_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_2 "
					+ "	else case when SECOND_PART.SUB_PART_3_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_3 "
					+ "	else case when PART_A3.PART_NAME IN (@PART_NAMES@) then PART_A3.PART_NAME "
					+ "	end "
					+ "	end "
					+ " end "
					+ " end as XM_SN "
					+ " ,SECOND_PART.ACTUAL_TIMESTAMP "
					+ " ,(select max(ACTUAL_TIMESTAMP) as VQ_OFF_TS from GAL215TBX H1 where H1.PRODUCT_ID = FRAME.PRODUCT_ID and H1.PROCESS_POINT_ID = ?1) "
					+ " ,(select max(ACTUAL_TIMESTAMP) as AF_OFF_TS from GAL215TBX H1 where H1.PRODUCT_ID = FRAME.PRODUCT_ID and H1.PROCESS_POINT_ID = ?2) "
					+ " ,FRAME.PRODUCT_SPEC_CODE "
					+ " ,case when SECOND_PART.SUB_PART_1_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_1_PART_ID "
					+ " else case when SECOND_PART.SUB_PART_2_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_2_PART_ID "
					+ " else case when SECOND_PART.SUB_PART_3_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_3_PART_ID "
					+ " else case when PART_A3.PART_NAME IN (@PART_NAMES@) then PART_A3.PART_ID "
					+ " end "
					+ " end "
					+ " end "
					+ " end as XM_SN "
					+ " from(select INITIAL_PART.PRODUCT_ID as PRODUCT_ID, INITIAL_PART.SUB_PART_1 as SUB_PART_1, INITIAL_PART.SUB_PART_1_NAME as SUB_PART_1_NAME, INITIAL_PART.SUB_PART_1_PART_ID as SUB_PART_1_PART_ID, INITIAL_PART.SUB_PART_2 as SUB_PART_2,INITIAL_PART.SUB_PART_2_NAME as SUB_PART_2_NAME, INITIAL_PART.SUB_PART_2_PART_ID as SUB_PART_2_PART_ID, PART_A2.PART_SERIAL_NUMBER as SUB_PART_3, PART_A2.PART_NAME as SUB_PART_3_NAME, PART_A2.PART_ID as SUB_PART_3_PART_ID, INITIAL_PART.ACTUAL_TIMESTAMP as ACTUAL_TIMESTAMP "
					+ " from (select PART_07.PRODUCT_ID as PRODUCT_ID, PART_07.PART_SERIAL_NUMBER as SUB_PART_1, PART_07.SUB_PART_1_NAME as SUB_PART_1_NAME, PART_07.SUB_PART_1_PART_ID as SUB_PART_1_PART_ID, PART_A1.PART_SERIAL_NUMBER as SUB_PART_2, PART_A1.PART_NAME as SUB_PART_2_NAME, PART_A1.PART_ID as SUB_PART_2_PART_ID, PART_07.ACTUAL_TIMESTAMP as ACTUAL_TIMESTAMP "
			        + " from (select PRODUCT_ID_LIST.PRODUCT_ID as PRODUCT_ID, PARTS.PART_SERIAL_NUMBER as PART_SERIAL_NUMBER, PARTS.PART_NAME as SUB_PART_1_NAME, PARTS.PART_ID as SUB_PART_1_PART_ID, PARTS.ACTUAL_TIMESTAMP as ACTUAL_TIMESTAMP "
			        + " from PRODUCT_ID_LIST "
			        + " left join PARTS "
			        + " on PRODUCT_ID_LIST.PRODUCT_ID = PARTS.PRODUCT_ID ) as PART_07 "
			        + " left outer join PARTS PART_A1 "
			        + " on PART_07.PART_SERIAL_NUMBER = PART_A1.PRODUCT_ID ) as INITIAL_PART "
			        + " left outer join PARTS PART_A2 "
			        + " on INITIAL_PART.SUB_PART_2 = PART_A2.PRODUCT_ID ) as SECOND_PART "
			        + " left outer join PARTS PART_A3 "
			       	+ " on SECOND_PART.SUB_PART_3 = PART_A3.PRODUCT_ID "
			        + " join GAL143TBX FRAME "
			       	+ " on SECOND_PART.PRODUCT_ID = FRAME.PRODUCT_ID "
			        + " join GAL144TBX FRAME_SPEC on FRAME.PRODUCT_SPEC_CODE = FRAME_SPEC.PRODUCT_SPEC_CODE "
			        + " where SUB_PART_1 <>  SECOND_PART.PRODUCT_ID "
			        + " and SUB_PART_1 <> '' "
			        + " and (SUB_PART_1_NAME IN(@PART_NAMES@) "
			        + " or SUB_PART_2_NAME IN(@PART_NAMES@) "
			        + " or SUB_PART_3_NAME IN(@PART_NAMES@) "
			        + " or PART_A3.PART_NAME IN(@PART_NAMES@)) "
			        + " order by PRODUCT_ID ";

	private static final String SELECT_XM_OR_TCU_WITH_EXT_REQUIRED1 = "with PRODUCT_ID_LIST as (select PRODUCT_ID FROM GAL143TBX FRAME join GAL144TBX FRAME_SPEC on FRAME.PRODUCT_SPEC_CODE = FRAME_SPEC.PRODUCT_SPEC_CODE "
					+ " where FRAME_SPEC.sales_model_type_code in (@COUNTRY_CODE@) "
					+ " and FRAME.PRODUCT_ID IN (select distinct PRODUCT_ID from gal215tbx HISTORY where HISTORY.PROCESS_POINT_ID = ?1 and HISTORY. ACTUAL_TIMESTAMP >= ?3) "
					+ " order by FRAME.PRODUCT_ID) "
					+ " , PARTS as  (select PRODUCT_ID, PART_SERIAL_NUMBER, PART_NAME, PART_ID, ACTUAL_TIMESTAMP from GAL185TBX INSTALLED_SN where product_id <> ''" +
					" and ( ";

	private static final String SELECT_XM_OR_TCU_WITH_EXT_REQUIRED2 =
					 ")) select distinct SECOND_PART.PRODUCT_ID as PRODUCT_ID "
					+ " ,FRAME_SPEC.SERIES_DESCRIPTION as SERIAL_DESCRIPTION "
					+ " ,FRAME_SPEC.MODEL_YEAR_DESCRIPTION as MODEL_YEAR_DESCRIPTION "
					+ " ,FRAME_SPEC.MODEL_CODE as MODEL_CODE "
					+ " ,case when SECOND_PART.SUB_PART_1_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_1_NAME "
					+ " else case when SECOND_PART.SUB_PART_2_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_2_NAME "
					+ "	else case when SECOND_PART.SUB_PART_3_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_3_NAME "
					+ " else case when PART_A3.PART_NAME IN (@PART_NAMES@) then PART_A3.PART_NAME "
					+ " end "
					+ " end "
					+ " end "
					+ " end as PART_NAME "
					+ ", case when SECOND_PART.SUB_PART_1_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_1 "
					+ " else case when SECOND_PART.SUB_PART_2_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_2 "
					+ "	else case when SECOND_PART.SUB_PART_3_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_3 "
					+ "	else case when PART_A3.PART_NAME IN (@PART_NAMES@) then PART_A3.PART_NAME "
					+ "	end "
					+ "	end "
					+ " end "
					+ " end as XM_SN "
					+ " ,SECOND_PART.ACTUAL_TIMESTAMP "
					+ " ,(select max(ACTUAL_TIMESTAMP) as VQ_OFF_TS from GAL215TBX H1 where H1.PRODUCT_ID = FRAME.PRODUCT_ID and H1.PROCESS_POINT_ID = ?1) "
					+ " ,(select max(ACTUAL_TIMESTAMP) as AF_OFF_TS from GAL215TBX H1 where H1.PRODUCT_ID = FRAME.PRODUCT_ID and H1.PROCESS_POINT_ID = ?2) "
					+ " ,FRAME.PRODUCT_SPEC_CODE "
					+ " ,case when SECOND_PART.SUB_PART_1_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_1_PART_ID "
					+ " else case when SECOND_PART.SUB_PART_2_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_2_PART_ID "
					+ " else case when SECOND_PART.SUB_PART_3_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_3_PART_ID "
					+ " else case when PART_A3.PART_NAME IN (@PART_NAMES@) then PART_A3.PART_ID "
					+ " end "
					+ " end "
					+ " end "
					+ " end as XM_SN "
					+ " from(select INITIAL_PART.PRODUCT_ID as PRODUCT_ID, INITIAL_PART.SUB_PART_1 as SUB_PART_1, INITIAL_PART.SUB_PART_1_NAME as SUB_PART_1_NAME, "
					+ " INITIAL_PART.SUB_PART_1_PART_ID as SUB_PART_1_PART_ID, INITIAL_PART.SUB_PART_2 as SUB_PART_2,INITIAL_PART.SUB_PART_2_NAME as SUB_PART_2_NAME, "
					+ " INITIAL_PART.SUB_PART_2_PART_ID as SUB_PART_2_PART_ID, PART_A2.PART_SERIAL_NUMBER as SUB_PART_3, PART_A2.PART_NAME as SUB_PART_3_NAME, "
					+ " PART_A2.PART_ID as SUB_PART_3_PART_ID, INITIAL_PART.ACTUAL_TIMESTAMP as ACTUAL_TIMESTAMP "
					+ " from (select PART_07.PRODUCT_ID as PRODUCT_ID, PART_07.PART_SERIAL_NUMBER as SUB_PART_1, PART_07.SUB_PART_1_NAME as SUB_PART_1_NAME, PART_07.SUB_PART_1_PART_ID as SUB_PART_1_PART_ID, "
					+ " PART_A1.PART_SERIAL_NUMBER as SUB_PART_2, PART_A1.PART_NAME as SUB_PART_2_NAME, PART_A1.PART_ID as SUB_PART_2_PART_ID, PART_07.ACTUAL_TIMESTAMP as ACTUAL_TIMESTAMP "
			        + " from (select PRODUCT_ID_LIST.PRODUCT_ID as PRODUCT_ID, PARTS.PART_SERIAL_NUMBER as PART_SERIAL_NUMBER, PARTS.PART_NAME as SUB_PART_1_NAME, PARTS.PART_ID as SUB_PART_1_PART_ID, "
					+ " PARTS.ACTUAL_TIMESTAMP as ACTUAL_TIMESTAMP "
			        + " from PRODUCT_ID_LIST "
			        + " left join PARTS "
			        + " on PRODUCT_ID_LIST.PRODUCT_ID = PARTS.PRODUCT_ID ) as PART_07 "
			        + " left outer join PARTS PART_A1 "
			        + " on PART_07.PART_SERIAL_NUMBER = PART_A1.PRODUCT_ID ) as INITIAL_PART "
			        + " left outer join PARTS PART_A2 "
			        + " on INITIAL_PART.SUB_PART_2 = PART_A2.PRODUCT_ID ) as SECOND_PART "
			        + " left outer join PARTS PART_A3 "
			       	+ " on SECOND_PART.SUB_PART_3 = PART_A3.PRODUCT_ID "
			        + " join GAL143TBX FRAME "
			       	+ " on SECOND_PART.PRODUCT_ID = FRAME.PRODUCT_ID "
			        + " join GAL144TBX FRAME_SPEC on FRAME.PRODUCT_SPEC_CODE = FRAME_SPEC.PRODUCT_SPEC_CODE "
			        + " where SUB_PART_1 <>  SECOND_PART.PRODUCT_ID "
			        + " and SUB_PART_1 <> '' "
			        + " and (SUB_PART_1_NAME IN(@PART_NAMES@) "
			        + " or SUB_PART_2_NAME IN(@PART_NAMES@) "
			        + " or SUB_PART_3_NAME IN(@PART_NAMES@) "
			        + " or PART_A3.PART_NAME IN(@PART_NAMES@)) "
			        + " order by PRODUCT_ID ";

	private static final String SELECT_SALES_WARRANTY_FR =
			"select frame.product_id \n" +  //0
					",frame_mtoc.ext_color_code  \n" +
					",frame_mtoc.int_color_code \n" +
					",frame_mtoc.model_year_code  \n" +
					",frame_mtoc.model_code \n" +
					",frame_mtoc.model_type_code  \n" +
					",frame_mtoc.model_option_code \n" +
					",frame.production_lot \n" +
					",lpad(nvl(mod(frame.af_on_sequence_number,100000),0),5,0) as af_on_sequence_number \n" +
					",substr(frame.production_lot,6,1) as line_number \n" +
					",substr(frame.production_lot,1,4) as frame_plant_code \n" +
					",frame.kd_lot_number \n" +
					",frame.key_no \n" +
					",eng_frame.plant_code as engine_plant_code \n" + //40
					",trim(frame.engine_serial_no) as engine_serial_no \n" +
					",eng_frame.kd_lot_number as eng_kd_lot_number \n" +
					",nvl(to_char(eng_frame.actual_off_date,'yyyyMMdd'),'00000000') as AE_OFF_DATE"+
					",nvl(to_char(eng_frame.actual_off_date,'HH24MI'),'0000') as AE_OFF_TIME"+
					",eng_frame.MISSION_SERIAL_NO as MISSION_SERIAL_NO \n" +
					",substr(frame.production_lot,6,1) as af_line_number \n" +
					",substr(frame.production_lot,6,1) as we_line_number \n" +
					",substr(frame.production_lot,6,1) as pa_line_number \n" +
					",substr(frame.production_lot,6,1) as ship_line_number \n" +
					"{SALES_WARRANTY_HISTORY_COL_TMPL}" +
					"{SALES_WARRANTY_SCRAP_EXCEPT_TMPL}" +
					"from gal143tbx frame  \n" +
					"join gal144tbx frame_mtoc on frame.product_spec_code = frame_mtoc.product_spec_code \n" +

			"left join gal131tbx eng_frame on frame.engine_serial_no = eng_frame.product_id \n" +
			"join ( \n" +
			"select distinct history.product_id \n" +
			"from gal215tbx history \n" +
			"join gal143tbx frame on history.product_id=frame.product_id \n" +
			"where history.process_point_id in (:selectingProcessPointIds) \n" +
			"and frame.production_lot not like 'TEST%' \n" +
			"and (history.actual_timestamp >= ?1 or ?1 is null) \n" +
			"{SALES_WARRANTY_EXC_OUT_TMPL}" +
			") vins \n" +
			"on frame.product_id = vins.product_id \n" +
			"{SALES_WARRANTY_HISTORY_TBL_TMPL}" +

			"{SALES_WARRANTY_EXCLUDE_PLANT_CODES}" +

			"for read only ";
	private static final String SALES_WARRANTY_SCRAP_EXCEPT_TMPL =
			" ,nvl(to_char(SCRAPPED.actual_timestamp,'yyyyMMdd'), nvl(to_char(EXCEPTIONAL.actual_timestamp,'yyyyMMdd'),'        ')) AS SCRAPPED_EXCEPTIONAL_DATE" +
					" ,nvl(to_char(SCRAPPED.actual_timestamp,'HH24MI')  || 'S', nvl(to_char(EXCEPTIONAL.actual_timestamp,'HH24MI')  || 'E','    ')) as SCRAPPED_EXCEPTIONAL_TIME ";

	private static final String SALES_WARRANTY_EXC_OUT_TMPL = "union \nselect product_id from gal143tbx where tracking_status in (:notSellableTrackingStatus) and production_lot not like 'TEST%' and (update_timestamp >= ?1 or ?1 is null)" ;
	private static final String SALES_WARRANTY_HISTORY_COL_TMPL = ",{pp_type}.shift as {pp_type}_SHIFT,{pp_type}.actual_timestamp as {pp_type}_ACTUAL_TIMESTAMP,nvl(to_char({pp_type}.actual_timestamp,'yyyyMMdd'),'00000000') as {pp_type}_DATE ,nvl(to_char({pp_type}.actual_timestamp,'HH24MI'),'0000') as {pp_type}_TIME ";
	private static final String SALES_WARRANTY_HISTORY_TBL_TMPL = "left join (select distinct actual_timestamp, shift, product_id, prod_date from (select max(prod_result.actual_timestamp) as actual_timestamp, prod_result.product_id as product_id, max(prod_result.production_date) as prod_date, g212.line_no as line_no "
			+ " from gal215tbx prod_result, gal212tbx g212 where prod_result.process_point_id in (:processPointId) and process_count = 1 and "
			+ "prod_result.production_lot = g212.production_lot group by prod_result.product_id, g212.line_no) gal215, gal226tbx  "
			+ "where gal226tbx.production_date=prod_date and actual_timestamp between start_timestamp and end_timestamp AND  "
			+ "gal226tbx.line_no = gal215.line_no and gal226tbx.process_location = ':processLocation'){pp_type} on {pp_type}.product_id = frame.product_id";

	private static final String SALES_WARRANTY_EXCLUDE_PLANT_CODES = "WHERE trim(substr(eng_frame.production_lot,1,4)) NOT IN (:plantCodesToExclude)";
	private static final String GET_SALES_MODEL_TYPE_CODE = "select sales_model_type_code from galadm.gal143tbx a, galadm.gal144tbx b where a.product_id = ?1 and a.product_spec_code = b.product_spec_code";

	private static final String GET_PROVISIONING_INFO = "SELECT VARCHAR_FORMAT (INSTALLED_PART.CREATE_TIMESTAMP, 'MM/YYYY')  AS ProductionWeek, " +
			" SUBSTR( FRAME.PRODUCT_ID, LENGTH(FRAME.PRODUCT_ID) -5 ) AS ChassisNumber, FRAME_SPEC.SALES_MODEL_TYPE_CODE AS DomainName, FRAME_SPEC.SALES_MODEL_CODE AS VehicleModelCode , " +
			" FRAME_SPEC.PLANT_CODE_GPCS AS Plant, INSTALLED_PART.PART_SERIAL_NUMBER AS ControlBoxSerial FROM GAL143TBX FRAME" +
			" JOIN GAL144TBX FRAME_SPEC ON (FRAME.PRODUCT_SPEC_CODE = FRAME_SPEC.PRODUCT_SPEC_CODE)" +
			" JOIN GAL185TBX INSTALLED_PART ON (FRAME.PRODUCT_ID = INSTALLED_PART.PRODUCT_ID AND INSTALLED_PART.PART_NAME='INFOTAINMENT UNIT')" +
			" WHERE FRAME.PRODUCT_ID=?1";

	private static final String SELECT_BY_AF_ON_SEQ_RANGE_LINE_ID = "select e from Frame e where e.afOnSequenceNumber >= :startSequence  and e.afOnSequenceNumber <= :endSequence and e.trackingStatus=:lineId";

	private static final String FIND_ACTIVE_PRODUCT_FOR_PRODUCTION_DATE = "select * from GALADM.GAL143TBX where PRODUCTION_LOT like ?1 and LAST_PASSING_PROCESS_POINT_ID is not null";
	private static final String FIND_ACTIVE_PRODUCT_FOR_PRODUCTION_DATE_AND_PPIDS = "select * from GALADM.GAL143TBX where PRODUCTION_LOT like ?1 and (LAST_PASSING_PROCESS_POINT_ID is not null and LAST_PASSING_PROCESS_POINT_ID not in (@INITIAL_PROCESS_POINT_IDS@))";

	private static final String SELECT_BY_AF_ON_SEQUENCE_NUMBER_RANGE_NATIVE = "SELECT * FROM (SELECT E.*, ROW_NUMBER() OVER(ORDER BY AF_ON_SEQUENCE_NUMBER) AS rn FROM GAL143TBX E WHERE E.AF_ON_SEQUENCE_NUMBER >= ?1 AND E.AF_ON_SEQUENCE_NUMBER <= ?2) AS temp WHERE rn BETWEEN %d AND %d";
	private static final String FIND_ALL_BY_SHORT_VIN_NATIVE = "SELECT * FROM (SELECT E.*, ROW_NUMBER() OVER(ORDER BY PRODUCTION_DATE DESC) AS rn FROM GAL143TBX E WHERE SHORT_VIN = ?1) AS temp WHERE rn BETWEEN %d AND %d";
	private static final String FIND_ALL_BY_AF_ON_SEQUENCE_NUMBER_NATIVE ="SELECT * FROM (SELECT E.*, ROW_NUMBER() OVER() AS rn FROM GAL143TBX E WHERE AF_ON_SEQUENCE_NUMBER = ?1) AS temp WHERE rn BETWEEN %d AND %d";

	private static final String FIND_VALID_PRODUCT_FOR_PROCESS_POINT = "SELECT A.* FROM GAL143TBX A WHERE A.PRODUCT_ID = ?1 AND A.TRACKING_STATUS IN (" +
			"SELECT B.PREVIOUS_LINE_ID FROM GAL236TBX B WHERE B.LINE_ID = (" +
			"SELECT C.LINE_ID FROM GAL195TBX C WHERE EXISTS (" +
			"SELECT 1 FROM GAL214TBX D WHERE C.LINE_ID = D.LINE_ID AND D.PROCESS_POINT_ID = ?2 " +
			"FETCH FIRST ROW ONLY) FETCH FIRST ROW ONLY)) FETCH FIRST ROW ONLY";

	private static final String GET_SCHEDULE_QUANTITY = "SELECT SUM(SCHED.LOT_SIZE) AS SCHED_QTY FROM GAL143TBX FRAME JOIN GAL212TBX SCHED  ON SCHED.KD_LOT_NUMBER = FRAME.KD_LOT_NUMBER WHERE FRAME.PRODUCT_ID =?1 AND SCHED.PLAN_CODE =?2 AND SCHED.REMAKE_FLAG != 'Y' FOR READ ONLY";

	private static final String GET_PASS_QUANTITY = "SELECT COUNT(*) AS PASS_QTY FROM GAL143TBX FRAME1 JOIN GAL143TBX FRAME2 ON FRAME2.KD_LOT_NUMBER = FRAME1.KD_LOT_NUMBER JOIN GAL215TBX TRACK1 ON TRACK1.PRODUCT_ID = FRAME1.PRODUCT_ID AND TRACK1.PROCESS_POINT_ID = ?2 " +
			"JOIN GAL215TBX TRACK2 ON TRACK2.PRODUCT_ID = FRAME2.PRODUCT_ID AND TRACK2.PROCESS_POINT_ID = TRACK1.PROCESS_POINT_ID AND TRACK2.ACTUAL_TIMESTAMP <= TRACK1.ACTUAL_TIMESTAMP AND TRACK2.PROCESS_COUNT = 1 WHERE FRAME1.PRODUCT_ID = ?1 " +
			"AND TRACK1.ACTUAL_TIMESTAMP = (SELECT MAX(ACTUAL_TIMESTAMP) FROM GAL215TBX TRACK3 WHERE TRACK3.PRODUCT_ID = TRACK1.PRODUCT_ID AND TRACK3.PROCESS_POINT_ID = TRACK1.PROCESS_POINT_ID)";

	private static final String GET_CUTLOT_QUANTITY = "SELECT COUNT(*) AS PASS_REMAINING_QTY " +
			"FROM GALADM.GAL143TBX FRAME1, GALADM.GAL143TBX FRAME2, GALADM.GAL215TBX TRACK1SEQ, GALADM.GAL215TBX TRACK1CUR, GALADM.GAL215TBX TRACK2 " +
			"WHERE FRAME1.PRODUCT_ID = ?1 AND FRAME2.KD_LOT_NUMBER = FRAME1.KD_LOT_NUMBER AND TRACK1CUR.PRODUCT_ID = FRAME1.PRODUCT_ID AND TRACK1SEQ.PRODUCT_ID = FRAME1.PRODUCT_ID AND "+
			"TRACK2.PRODUCT_ID = FRAME2.PRODUCT_ID and track1CUR.process_point_id = ?2 and track1SEQ.process_point_id = ?3 and track2.process_point_id = ?3 AND NOT EXISTS "+
			"(SELECT * FROM GALADM.STRAGGLER_TBX STRAG WHERE STRAG.PRODUCT_ID = FRAME2.PRODUCT_ID AND STRAG.PP_DELAYED_AT = ?3) AND NOT EXISTS "+
			"(SELECT * FROM GALADM.GAL215TBX TRACK3 WHERE TRACK3.PRODUCT_ID = TRACK2.PRODUCT_ID AND PROCESS_POINT_ID = ?2 AND TRACK3.ACTUAL_TIMESTAMP <= TRACK1CUR.ACTUAL_TIMESTAMP)";

	private static final String GET_REBUILD_QUANTITY = "SELECT COUNT(*) FROM GAL212TBX S, GAL143TBX F where (PRODUCT_ID = ?1 and F.KD_LOT_NUMBER = S.KD_LOT_NUMBER and S.PLAN_CODE =?2 and S.REMAKE_FLAG = 'Y' )";

	private static final String FIND_LATEST_ASM_SEQ_NO =" SELECT af_on_sequence_number FROM gal143tbx WHERE product_id = (SELECT product_id FROM gal215tbx "+
			"  WHERE process_point_id = ?1 ORDER BY CREATE_TIMESTAMP DESC "+
			" FETCH FIRST 1 ROWS ONLY)";

	private static final String FIND_VIN_BY_LINE_REF = "SELECT e.* FROM gal143tbx e INNER JOIN gal215tbx h on e.PRODUCT_ID = h.PRODUCT_ID and h.PROCESS_POINT_ID= ?1 where MOD(e.AF_ON_SEQUENCE_NUMBER, ?2)= ?3 order by e.AF_ON_SEQUENCE_NUMBER desc fetch first 1 rows only with ur";

	/* SQL statements used for findByFieldRanges */
	private static final String FIND_BY_FIELD_RANGES_BASE = "SELECT G212.LINE_NO, G143.AF_ON_SEQUENCE_NUMBER, G143.PRODUCT_ID, G143.TRACKING_STATUS, G143.PRODUCT_SPEC_CODE, G143.ENGINE_SERIAL_NO, G144.ENGINE_MTO, G143.KD_LOT_NUMBER, G163.PARKING_LOCATION";
	private static final String FIND_BY_FIELD_RANGES_BASE_G215 = ", G215.PROCESS_POINT_ID, G215.ACTUAL_TIMESTAMP";
	private static final String FIND_BY_FIELD_RANGES_JOIN = " FROM GAL143TBX G143 JOIN GAL212TBX G212 ON G212.PRODUCTION_LOT = G143.PRODUCTION_LOT LEFT OUTER JOIN GAL163TBX G163 ON G163.VIN = G143.PRODUCT_ID JOIN GAL144TBX G144 ON G144.PRODUCT_SPEC_CODE = G143.PRODUCT_SPEC_CODE";
	private static final String FIND_BY_FIELD_RANGES_JOIN_G215 = " JOIN GAL215TBX G215 ON G215.PRODUCT_ID = G143.PRODUCT_ID";
	private static final String FIND_BY_FIELD_RANGES_WHERE_LINE_NO = "G212.LINE_NO = '@lineNo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_LINE_NO_RANGE = "G212.LINE_NO BETWEEN '@lineNoFrom' AND '@lineNoTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_AF_ON_SEQUENCE_NUMBER = "G143.AF_ON_SEQUENCE_NUMBER = @afOnSequenceNumber";
	private static final String FIND_BY_FIELD_RANGES_WHERE_AF_ON_SEQUENCE_NUMBER_RANGE = "G143.AF_ON_SEQUENCE_NUMBER BETWEEN @afOnSequenceNumberFrom AND @afOnSequenceNumberTo";
	private static final String FIND_BY_FIELD_RANGES_WHERE_AF_ON_SEQUENCE_NUMBER_RANGE_WRAP = "((G143.AF_ON_SEQUENCE_NUMBER BETWEEN @afOnSequenceNumberFrom AND @afOnSequenceNumberMax) OR (G143.AF_ON_SEQUENCE_NUMBER BETWEEN @afOnSequenceNumberMin AND @afOnSequenceNumberTo))";
	private static final String FIND_BY_FIELD_RANGES_WHERE_AF_ON_SEQUENCE_NUMBER_IN = "G143.AF_ON_SEQUENCE_NUMBER IN (@afOnSequenceNumber)";
	private static final String FIND_BY_FIELD_RANGES_WHERE_PRODUCT_ID = "G143.PRODUCT_ID = '@productId'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_PRODUCT_ID_RANGE = "SUBSTR(G143.PRODUCT_ID,12,6) BETWEEN '@productIdFrom' AND '@productIdTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_PRODUCT_ID_IN = "G143.PRODUCT_ID IN (@productId)";
	private static final String FIND_BY_FIELD_RANGES_WHERE_TRACKING_STATUS = "G143.TRACKING_STATUS = '@trackingStatus'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_TRACKING_STATUS_IN = "G143.TRACKING_STATUS IN (@trackingStatus)";
	private static final String FIND_BY_FIELD_RANGES_WHERE_PRODUCT_SPEC_CODE = "G143.PRODUCT_SPEC_CODE = '@productSpecCode'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_PRODUCT_SPEC_CODE_RANGE = "G143.PRODUCT_SPEC_CODE BETWEEN '@productSpecCodeFrom' AND '@productSpecCodeTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_MODEL_YEAR_CODE = "G144.MODEL_YEAR_CODE = '@modelYearCode'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_MODEL_YEAR_CODE_RANGE = "G144.MODEL_YEAR_CODE BETWEEN '@modelYearCodeFrom' AND '@modelYearCodeTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_MODEL_CODE = "G144.MODEL_CODE = '@modelCode'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_MODEL_CODE_RANGE = "G144.MODEL_CODE BETWEEN '@modelCodeFrom' AND '@modelCodeTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_MODEL_TYPE_CODE = "G144.MODEL_TYPE_CODE = '@modelTypeCode'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_MODEL_TYPE_CODE_RANGE = "G144.MODEL_TYPE_CODE BETWEEN '@modelTypeCodeFrom' AND '@modelTypeCodeTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_EXT_COLOR_CODE = "G144.EXT_COLOR_CODE = '@extColorCode'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_EXT_COLOR_CODE_RANGE = "G144.EXT_COLOR_CODE BETWEEN '@extColorCodeFrom' AND '@extColorCodeTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_INT_COLOR_CODE = "G144.INT_COLOR_CODE = '@intColorCode'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_INT_COLOR_CODE_RANGE = "G144.INT_COLOR_CODE BETWEEN '@intColorCodeFrom' AND '@intColorCodeTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_ENGINE_SERIAL_NO = "G143.ENGINE_SERIAL_NO = '@engineSerialNo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_ENGINE_SERIAL_NO_RANGE = "G143.ENGINE_SERIAL_NO BETWEEN '@engineSerialNoFrom' AND '@engineSerialNoTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_ENGINE_MTO = "G144.ENGINE_MTO = '@engineMto'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_ENGINE_MTO_RANGE = "G144.ENGINE_MTO BETWEEN '@engineMtoFrom' AND '@engineMtoTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_KD_LOT_NUMBER = "G143.KD_LOT_NUMBER = '@kdLotNumber'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_KD_LOT_NUMBER_RANGE = "G143.KD_LOT_NUMBER BETWEEN '@kdLotNumberFrom' AND '@kdLotNumberTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_PRODUCTION_LOT = "G143.PRODUCTION_LOT = '@productionLot'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_PRODUCTION_LOT_RANGE = "G143.PRODUCTION_LOT BETWEEN '@productionLotFrom' AND '@productionLotTo'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_PROCESS_POINT_ID = "G215.PROCESS_POINT_ID = '@processPointId'";
	private static final String FIND_BY_FIELD_RANGES_WHERE_PROCESS_POINT_ID_IN = "G215.PROCESS_POINT_ID IN (@processPointId)";
	private static final String FIND_BY_FIELD_RANGES_WHERE_ACTUAL_TIMESTAMP = "G215.ACTUAL_TIMESTAMP = {ts '@actualTimestamp'}";
	private static final String FIND_BY_FIELD_RANGES_WHERE_ACTUAL_TIMESTAMP_RANGE = "G215.ACTUAL_TIMESTAMP BETWEEN {ts '@actualTimestampFrom'} AND {ts '@actualTimestampTo'}";
	private static final String FIND_ALL_PROCESSED = "SELECT a.PRODUCT_ID,a.PRODUCT_SPEC_CODE,a.AF_ON_SEQUENCE_NUMBER as SEQUENCE_NUMBER,a.KD_LOT_NUMBER "+
			"From galadm.GAL143TBX AS a inner join galadm.GAL215TBX x on a.PRODUCT_ID = x.PRODUCT_ID where x.PROCESS_POINT_ID = ?1 and a.AF_ON_SEQUENCE_NUMBER IS NOT NULL "+
			" order by x.ACTUAL_TIMESTAMP  desc ";
	private static final String  FIND_ALL_BY_INSTALLED_PARTS =  "Select a.PRODUCT_ID "+
	         "From galadm.GAL143TBX AS a inner join galadm.GAL185TBX x on a.PRODUCT_ID = x.PRODUCT_ID where x.PART_NAME = ?1 and PART_ID= ?2 and INSTALLED_PART_STATUS != -9";

	private static final String FIND_VINS_IN_KDLOT_SORT_BY_AF_ON_TIMESTAMP =  "WITH afon AS (SELECT tracking.product_id, frame.af_on_sequence_number, tracking.actual_timestamp, ROW_NUMBER () " +
			"OVER (PARTITION BY tracking.PRODUCT_ID ORDER BY ACTUAL_TIMESTAMP DESC) AS ROWNUM " +
			"FROM galadm.gal215tbx tracking, galadm.gal143tbx frame WHERE tracking.product_id = frame.product_id " +
			"AND frame.kd_lot_number = ?1 AND tracking.process_point_id = ?2 ORDER BY tracking.actual_timestamp) " +
			"SELECT afon.product_id, afon.af_on_sequence_number FROM afon WHERE afon.rownum = 1 " +
			"AND afon.actual_timestamp <= (SELECT actual_timestamp FROM afon WHERE afon.PRODUCT_ID = ?3 AND afon.rownum = 1)";			

	private final String GET_PRODUCTS_WITHIN_SERIAL_RANGE = "SELECT * FROM GAL143TBX WHERE "+
			"SUBSTR(Product_ID, 12,6) >=  ?1 AND  SUBSTR(Product_ID, 12,6) <= ?2 ORDER BY SUBSTR(Product_ID, 12, 6)";
	private final String GET_PRODUCTS_WITHIN_PROD_LOT_RANGE = "SELECT * FROM GAL143TBX G143 JOIN GAL212TBX G212 ON G212.PRODUCTION_LOT = G143.PRODUCTION_LOT WHERE "+
			"G143.PRODUCTION_LOT BETWEEN ?1 AND ?2 AND G212.PLAN_CODE = ?3";
	private static final String FIND_NEXT_SEQUENCE_PLATE_NUMBER = " SELECT  min(t1.AF_ON_SEQUENCE_NUMBER+1 ) " + 
			" FROM GAL143tbx t1 " + 
			" WHERE  @UNIQUE_SPEC_CODE@ and NOT EXISTS " + 
			" (SELECT * FROM GAL143tbx t2 WHERE @UNIQUE_SPEC_CODE@  and   t2.AF_ON_SEQUENCE_NUMBER = t1.AF_ON_SEQUENCE_NUMBER + 1) ";
	private static final String SELECT_EXOUT_XM_OR_TCU_COMMON_NAME =
			"with XM (PRODUCT_ID,PART_NAME,PART_SERIAL_NUMBER,ACTUAL_TIMESTAMP,COMMON_NAME)  "
					+ " as (select ip.PRODUCT_ID,ip.PART_NAME,ip.PART_SERIAL_NUMBER,ip.ACTUAL_TIMESTAMP,op.COMMON_NAME  "
					+ " from galadm.gal185tbx ip JOIN galadm.mc_op_rev_tbx op "
					+ " ON ip.part_name=op.operation_name and ip.op_rev=op.op_rev "
					+ " where op.common_name in (@PART_NAMES@)  )"
					+ " select  "
					+ " a.product_id,  "  //index 0
					+ " c.SERIES_DESCRIPTION,c.MODEL_YEAR_DESCRIPTION,c.model_code, XM.PART_NAME, "  //1-4
					+ " XM.PART_SERIAL_NUMBER as XM_SN, XM.ACTUAL_TIMESTAMP AS XM_TS,   "  //5-6
					+ " (select max(actual_timestamp) as VQ_OFF_TS from galadm.gal215tbx H1 where H1.PRODUCT_ID=A.PRODUCT_ID and H1.PROCESS_POINT_ID = ?1), "  //7
					+ " (select max(actual_timestamp) as AF_OFF_TS from galadm.gal215tbx H2 where H2.PRODUCT_ID=A.PRODUCT_ID and H2.PROCESS_POINT_ID = ?2), "  //8
					+ " XM.COMMON_NAME "//9
					+ " FROM XM  "
					+ " join galadm.gal143tbx a on XM.PRODUCT_ID=a.PRODUCT_ID "
					+ " join galadm.gal144tbx c on a.product_spec_code = c.product_spec_code  "
					+ " where c.sales_model_type_code in (@COUNTRY_CODE@)  "
					+ " and a.UPDATE_TIMESTAMP >= ?3  "
					+ " and a.TRACKING_STATUS = ?4   order by a.product_id";

	private static final String SELECT_XM_OR_TCU_COMMON_NAME =
			"with XM (PRODUCT_ID,PART_NAME,PART_SERIAL_NUMBER,ACTUAL_TIMESTAMP,COMMON_NAME)  "
					+ " as (select DISTINCT ip.PRODUCT_ID,ip.PART_NAME,ip.PART_SERIAL_NUMBER,ip.ACTUAL_TIMESTAMP,op.COMMON_NAME  "
					+ " from galadm.gal215tbx history left outer join galadm.gal185tbx ip ON history.PRODUCT_ID=ip.PRODUCT_ID "
					+ " left outer join MC_OP_REV_TBX op ON ip.PART_NAME=op.OPERATION_NAME and ip.OP_REV=op.OP_REV "
					+ " where op.COMMON_NAME in (@PART_NAMES@)  AND history.PROCESS_POINT_ID = ?1 "
					+ " AND history.ACTUAL_TIMESTAMP >= ?3)  "
					+ " select  "
					+ " a.product_id,  "  //index 0
					+ " c.SERIES_DESCRIPTION,c.MODEL_YEAR_DESCRIPTION,c.model_code,XM.PART_NAME,  "  //1-4
					+ " XM.PART_SERIAL_NUMBER as XM_SN, XM.ACTUAL_TIMESTAMP AS XM_TS,   "  //5-6
					+ " (select max(actual_timestamp) as VQ_OFF_TS from galadm.gal215tbx H1 where H1.PRODUCT_ID=A.PRODUCT_ID and H1.PROCESS_POINT_ID = ?1), "  //7
					+ " (select max(actual_timestamp) as AF_OFF_TS from galadm.gal215tbx H2 where H2.PRODUCT_ID=A.PRODUCT_ID and H2.PROCESS_POINT_ID = ?2), "  //8
					+ " XM.COMMON_NAME "//9
					+ " FROM XM  "
					+ " join galadm.gal143tbx a on XM.PRODUCT_ID=a.PRODUCT_ID  "
					+ " join galadm.gal144tbx c on a.product_spec_code = c.product_spec_code  "
					+ " where c.sales_model_type_code in (@COUNTRY_CODE@)  "
					+ " order by a.product_id";
	
	private static final String GET_PRODUCT_LIST = "select g143.PRODUCT_ID from galadm.GAL143TBX g143 where g143.PRODUCT_SPEC_CODE = ?1 "
			+ "and g143.PRODUCTION_LOT in (:productionLot)";
	
	private static final String UPDATE_COLOR_DETAILS_3 = "Update GALADM.GAL143TBX set PRODUCT_SPEC_CODE = ?1 where PRODUCT_SPEC_CODE = ?2 and PRODUCTION_LOT in (:productionLot)";

	private static final String SELECT_TCU_FROM_LET = 	"SELECT A.INSPECTION_PARAM_VALUE " + 
			" FROM GAL704TBXV A,LET_PART_CHECK_SPEC_TBX B,GAL701TBX C,GAL143TBX D " + 
			" WHERE D.PRODUCT_ID = C.PRODUCT_ID " + 
			" AND A.INSPECTION_PGM_ID = B.INSPECTION_PROGRAM_ID " + 
			" AND A.INSPECTION_PARAM_ID = B.INSPECTION_PARAM_ID " + 
			" AND A.PRODUCT_ID = C.PRODUCT_ID " + 
			" AND A.TEST_SEQ = C.TEST_SEQ " + 
			" AND A.PRODUCT_ID = D.PRODUCT_ID " + 
			" AND C.TOTAL_RESULT_STATUS = '1' " + 
			" AND B.PART_NAME = ?1 " + 
			" AND D.PRODUCT_ID = ?2 " +
			" ORDER BY A.CREATE_TIMESTAMP DESC FETCH FIRST ROW ONLY "; 

	private final String FRAME_BY_KD_LOT = "SELECT A.* FROM GAL143TBX A  WHERE A.KD_LOT_NUMBER = ?1 AND ( A.TRACKING_STATUS NOT IN (:scrapLines) OR  A.TRACKING_STATUS IS NULL )";

	public static final String GET_PRODUCT_TRACKING_INFO = "SELECT g143.PRODUCT_ID as VIN "
	+ ", trim(g143.PRODUCT_SPEC_CODE) as PRODUCT_SPEC_CODE, g143.PRODUCTION_LOT "
	+ ", coalesce(g143.KD_LOT_NUMBER, 'None') as KD_LOT, g143.LAST_PASSING_PROCESS_POINT_ID as PP "
	+ ", coalesce(trim(PP.PROCESS_POINT_NAME), 'Waiting') as LOCATION "
	+ ", g143.UPDATE_TIMESTAMP as tr_timestamp, g143.TRACKING_STATUS from GAL143TBX g143 "
	+ "left join GAL144TBX g144 ON g143.PRODUCT_SPEC_CODE = g144.PRODUCT_SPEC_CODE "
	+ "      left join GAL214TBX PP on g143.LAST_PASSING_PROCESS_POINT_ID = PP.PROCESS_POINT_ID "
	+ "      where g144.MODEL_CODE in (:modelCodes) and g143.TRACKING_STATUS in (select Line_id from GAL195TBX where DIVISION_ID in (:departments)) "
	+ "order by g143.AF_ON_SEQUENCE_NUMBER";
	
	public List<Frame> findAllByProductionLot(String productionLot) {

		return findAll(Parameters.with("productionLot", productionLot));

	}

	
	
	private static final String SELECT_BY_PRODUCTION_LOT = "select e from Frame e where e.productionLot = :productionLot";
	@Override
	public List<Frame> findAllByProductionLot(String productionLot, int count) {

		return findAllByQuery(SELECT_BY_PRODUCTION_LOT, Parameters.with("productionLot", productionLot), count);

	}

	public List<Frame> findAllByLikeProductionLot(String productionLot) {

		Parameters params = Parameters.with("1", "%" + productionLot + "%");
		List<Frame> result = findAllByNativeQuery(FIND_ACTIVE_PRODUCT_FOR_PRODUCTION_DATE, params);
		return result;
	}

	public List<Frame> findAllByShortVin(String shortVin) {
		Parameters params = Parameters.with("shortVin", shortVin );
		return findAll(params);
	}

	public long countByShortVin(String shortVin) {
		return count(Parameters.with("shortVin", shortVin ));
	}

	public List<Frame> findPageByShortVin(String shortVin, int pageNumber, int pageSize) {
		Parameters params = Parameters.with("1", shortVin);
		int offset = Math.max(pageNumber, 0) * pageSize;
		String query = String.format(FIND_ALL_BY_SHORT_VIN_NATIVE, (pageNumber > 0) ? offset + 1 : offset, offset+pageSize);
		return findAllByNativeQuery(query, params);
		
	}

	public List<Frame> findAllByProductSequence(String processPointId, String currentProductId,
			int processedSize, int upcomingSize) {

		Parameters params = Parameters.with("1", processPointId);

		List<Frame> frames = new ArrayList<Frame>();

		List<Frame> allFrames  = findAllByNativeQuery(FIND_PRODUCTS_BY_PRODUCT_SEQUENCE, params);

		int index = -1;
		for(int i = 0;i< allFrames.size();i++) {
			if(allFrames.get(i).getProductId().equals(currentProductId)) {
				index = i;
				break;
			}
		}

		if (index == -1) return frames;

		int start = (index - processedSize < 0) ? 0 :index - processedSize;
		int end = (index + upcomingSize >=allFrames.size()) ? allFrames.size() -1 : index + upcomingSize;

		for(int i=start;i<=end;i++) {
			frames.add(allFrames.get(i));
		}

		return frames;

	}

	public void updateDefectStatus(String productId, DefectStatus status) {
		//No defect_status in Frame table
		//super.updateDefectStatus(productId, status);
	}

	@Override
	public List<Frame> findAllBySNPP(String SN, String PP) {
		Parameters params = Parameters.with("1", "%" + SN).put("2", PP);
		List<Frame> result = findAllByNativeQuery(FIND_ALL_BY_SNPP, params);
		return result;
	}

	public List<Frame> findAllByInProcessProduct(String currentProductId,
			int processedSize, int upcomingSize) {
		List<Frame> products = new ArrayList<Frame>();
		List<Frame> processedProducts  = findProcessedProducts(currentProductId, processedSize);
		// reverse the list and removed the current product id since it is in the upcomingEngines list
		for(int i = processedProducts.size() - 1; i >= 1; i--)
			products.add(processedProducts.get(i));
		List<Frame> upcomingProducts  = findUpcomingProducts(currentProductId, upcomingSize);
		for(Frame product : upcomingProducts) products.add(product);
		return products;
	}

	private List<Frame> findProcessedProducts(String currentProductId, int processedSize) {
		Parameters params = Parameters.with("1", currentProductId);
		return findAllByNativeQuery(
				FIND_PROCESSED_PRODUCTS_BY_IN_PROCESS_PRODUCT + getOrderBy(processedSize + 1), params);
	}

	private List<Frame> findUpcomingProducts(String currentProductId, int upcomingSize) {
		Parameters params = Parameters.with("1", currentProductId);
		return findAllByNativeQuery(
				FIND_UPCOMING_PRODUCTS_BY_IN_PROCESS_PRODUCT + getOrderBy(upcomingSize + 1), params);
	}

	private String getOrderBy(int size) {
		return ORDER_BY + size + ROWS_ONLY;
	}

	public List<InventoryCount> findAllInventoryCounts() {
		List<?> results =  findResultListByNativeQuery(FIND_INVENTORY_COUNTS, null);
		return toInventoryCounts(results);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateProductStartDate(String processPointId, String productId) {
		Parameters parameters = Parameters.with("1", processPointId);
		parameters.put("2", productId);

		executeNative(UPDATE_PRODUCT_START_DATE, parameters);
	}

	public Integer maxAfOnSequenceNumber() {
		return max("afOnSequenceNumber", Integer.class);
	}

	public List<Frame> findAllByLastPassingProcessPointId(String lastPassingProcessPointId) {
		return findAll(Parameters.with("lastPassingProcessPointId", lastPassingProcessPointId));
	}

	public List<Frame> findByAfOnSequenceNumber(int afOn) {

		return findAll(Parameters.with("afOnSequenceNumber", afOn));

	}

	public List<Frame> findPageByAfOnSequenceNumber(int afOn, int pageNumber, int pageSize) {
		Parameters params = Parameters.with("1", afOn);
		int offset = Math.max(pageNumber, 0) * pageSize;
		String query = String.format(FIND_ALL_BY_AF_ON_SEQUENCE_NUMBER_NATIVE, (pageNumber > 0) ? offset + 1 : offset, offset+pageSize);
		return findAllByNativeQuery(query, params);
	}

	public List<Frame> findByAfOnSequenceNumber(int afOn, String afOnProcessPointId) {
		Parameters params = Parameters.with("1", afOn).put("2", afOnProcessPointId);
		return findAllByNativeQuery(FIND_BY_AF_SEQ_NUM, params, Frame.class);
	}

	public long countByAfOnSequenceNumber(int startSequence, int endSequence) {
		return count(COUNT_BY_AF_ON_SEQUENCE_NUMBER_RANGE, Parameters.with("startSequence", startSequence).put("endSequence", endSequence));
	}

	public List<Frame> findAllByAfOnSequenceNumber(int startSequence, int endSequence) {
		return findAllByQuery(SELECT_BY_AF_ON_SEQUENCE_NUMBER_RANGE, Parameters.with("startSequence", startSequence).put("endSequence", endSequence));
	}

	@Override
	public List<Frame> findAllByAfOnSequenceNumber(int startSequence, int endSequence, int count) {
		return findAllByQuery(SELECT_BY_AF_ON_SEQUENCE_NUMBER_RANGE, Parameters.with("startSequence", startSequence).put("endSequence", endSequence), count);
	}

	private static final String SELECT_BY_PLANT_AF_ON_SEQUENCE_NUMBER_RANGE = 
			"select e from Frame e, ProcessPoint pp where e.lastPassingProcessPointId=pp.processPointId and pp.plantName = :plantName "
			+ " and e.afOnSequenceNumber >= :startSequence  and e.afOnSequenceNumber <= :endSequence order by e.afOnSequenceNumber";
	@Override
	public List<Frame> findAllByPlantAfOnSequenceNumber(String plantName, int startSequence, int endSequence, int count) {
		Parameters params = Parameters.with("startSequence",startSequence)
		.put("endSequence", endSequence)
		.put("plantName", plantName);

		return findAllByQuery(SELECT_BY_PLANT_AF_ON_SEQUENCE_NUMBER_RANGE,params, count);
	}

	public List<Frame> findPageByAfOnSequenceNumber(int startSequence, int endSequence, int pageNumber, int pageSize) {
		Parameters params = Parameters.with("1", startSequence).put("2", endSequence);
		int offset = Math.max(pageNumber, 0) * pageSize;
		String query = String.format(SELECT_BY_AF_ON_SEQUENCE_NUMBER_RANGE_NATIVE, (pageNumber > 0) ? offset + 1 : offset, offset+pageSize);
		return findAllByNativeQuery(query, params);
	}

	public Integer findAfOnSequenceNumberByShortSequence(int numberOfDigits, int shortSequence) {
		int divider = Double.valueOf(Math.pow(10, numberOfDigits)).intValue();
		String sql = FIND_AF_ON_SEQUENCE_NUMBER_BY_SHORT_SEQUENCE;
		//REMARK : had to replace ':divider' parameter manually here, as it is used in function and can not be set in prepared statement (exception is thrown)
		sql = sql.replace(":divider", String.valueOf(divider));
		Parameters params =  new Parameters();
		params.put("shortSequence", shortSequence);
		Number seq = findFirstByQuery(sql, Number.class,params);
		if (seq != null) {
			return Integer.valueOf(seq.intValue());
		}
		return null;
	}

	public List<Frame> findByProductionLot(String productionLot) {

		return findAll(Parameters.with("productionLot", productionLot));
	}

	public List<Frame> findBySeqRange(String min, String max) {
		return findBySeqRange(min, max, "PP10067");
	}

	public List<Frame> findBySeqRange(String min, String max, String afOnProcessPointId) {
		Parameters params = Parameters.with("1", min).put("2", max).put("3", afOnProcessPointId);
		return findAllByNativeQuery(FIND_BY_AF_SEQ_RANGE, params, Frame.class);
	}

	public List<Frame> findByTrackingStatus(String trackingStatus) {

		return findAll(Parameters.with("trackingStatus", trackingStatus));
	}

	public List<Frame> findByPartName(String lineId,
			int prePrintQty, int maxPrintPerCycle, String ppId,
			String partName) {
		Parameters parameters = Parameters.with("1", lineId.trim());
		parameters.put("2", prePrintQty);
		parameters.put("3", maxPrintPerCycle);
		parameters.put("4", ppId.trim());
		parameters.put("5", partName.trim());

		return findAllByNativeQuery(FIND_ELIGIBLE_PRODUCTS, parameters );
	}

	public List<Frame> findByModelAndSeqNumber(String model,String sequenceNumber){
		Parameters params = Parameters.with("1", sequenceNumber ).put("2", model);
		return findAllByNativeQuery(FIND_BY_MODEL_AND_SEQ_NUM, params, Frame.class);
	}

	@Override
	public List<Object[]> getProductsWithinRange(String startProductId, String stopProductId) {
		Parameters params = new Parameters();
		String sql = GET_PRODUCTS_WITHIN_RANGE;
		startProductId = StringUtils.trim(startProductId);
		stopProductId = StringUtils.trim(stopProductId);
		if (StringUtils.equals(startProductId, stopProductId)) {
			sql = sql + " where product_id = ?1 ";
			params.put("1", startProductId);
		} else {
			sql = sql + GET_PRODUCTS_WITHIN_RANGE_FILTER;
			params.put("1", startProductId.substring(0, 2));
			params.put("2", startProductId.substring(11, 17));
			params.put("3", stopProductId.substring(11, 17));
			params.put("4", startProductId.substring(9, 10));
		}
		return findAllByNativeQuery(sql, params, Object[].class);
	}

	/**
	 * find required engines to be installed for given MTOC in N days
	 * @param ProductSpecCode
	 * @param daysToCheck  N days
	 * @return List<String>
	 **/
	public List<String> getProductsByEngMTOC(String engMTOC,int daysToCheck, String[] notSellableTrackingStatus) {
		Parameters params = Parameters.with("1", engMTOC).put("2", daysToCheck);
		String sqlStr = GET_PRODUCTS_BY_ENG_MTOC;
		sqlStr = sqlStr.replaceAll(":notSellableTrackingStatus", StringUtil.toSqlInString(notSellableTrackingStatus));
		return findAllByNativeQuery(sqlStr, params, String.class);
	}

	public List<Object[]> getProductsWithinAfOnSeqRange(Integer startAfOnSeq,Integer stopAfOnSeq) {
		Parameters params = Parameters.with("1", startAfOnSeq ).put("2", stopAfOnSeq);
		return findAllByNativeQuery(GET_PRODUCTS_WITHIN_AFON_RANGE, params, Object[].class);
	}

	public List<Frame> findByEin(String ein) {
		Parameters params = Parameters.with("engineSerialNo", ein );
		return findAll(params);
	}

	public List<Frame> findAllByMissionSn(String msn) {
		Parameters params = Parameters.with("missionSerialNo", msn );
		return findAll(params);
	}

	public List<Object[]> getFrameLineMtoData(String productionLot,String scrapLineId,String exceptionalLineId)
	{
		Parameters params = Parameters.with("1", productionLot).put("2", scrapLineId).put("3", exceptionalLineId);
		return	executeNative(params,GET_FRAME_LINE_MTO_DATA);
	}

	public Integer getScrapedExceptionalCount(String productionLot, String scrapLineID, String exceptionalLineID) {
		Parameters params = Parameters.with("1", productionLot);
		params.put("2", scrapLineID);
		params.put("3", exceptionalLineID);
		Object[] objects = findFirstByNativeQuery(GET_SCRAPED_EXCEPTIONAL_COUNT, params,Object[].class);
		return objects[0] == null ? 0 : (Integer) objects[0];
	}

	public List<Frame> findByAfOnSequenceNumberOrderByPlanOffDate(int AfOn,boolean planOffDateOrderAsc) {
		return findAll(Parameters.with("afOnSequenceNumber", AfOn),new String[] {"planOffDate"},planOffDateOrderAsc);
	}

	public List<Object[]> getVinInVQ(String trackingStatus) {
		// create query
		Parameters params = null;
		// raw query results
		List<Object[]> vinInVQList = null;
		vinInVQList = findAllByNativeQuery(SELECT_VQ_VIN.replaceAll("@TRACKING_STATUS@", StringUtil.toSqlInString(trackingStatus)), params, Object[].class);
		return vinInVQList;
	}

	public List<Object[]> getExOutVinXmRadio(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, String trackingStatus){
		Parameters params = Parameters.with("1", vqOffPP);
		params.put("2", afOffPP);
		params.put("3", lastUpdate);
		params.put("4", trackingStatus);
		// raw query results
		List<Object[]> exOutVinList = null;
		String sql = SELECT_EXOUT_XM_OR_TCU.replaceAll("@PART_NAMES@", toSqlLikeString(xmPartNames,"PART_NAME"));
		exOutVinList = findAllByNativeQuery(sql.replaceAll("@COUNTRY_CODE@", StringUtil.toSqlInString(countryCode)), params, Object[].class);
		return exOutVinList;
	}

	public List<Object[]> getExtOutVinXmRadioWithExtRequired(String xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, String trackingStatus, List<String> productIdPrefix){
		Parameters params = Parameters.with("1", vqOffPP);
		params.put("2", afOffPP);
		params.put("3", lastUpdate);
		params.put("4", trackingStatus);
		// raw query results
		List<Object[]> exOutVinList = null;
		String framePrefixNotLikeQuery = null;

		for(String prefix : productIdPrefix) {
		if(framePrefixNotLikeQuery == null)
			framePrefixNotLikeQuery = "INSTALLED_SN.part_serial_number NOT LIKE ('"+prefix+"%')";
		else
			framePrefixNotLikeQuery = framePrefixNotLikeQuery + " AND INSTALLED_SN.part_serial_number NOT LIKE ('"+prefix+"%')";
		}
		String sql1 = SELECT_EXOUT_XM_OR_TCU_WITH_EXT_REQUIRED1.replaceAll("@COUNTRY_CODE@", StringUtil.toSqlInString(countryCode)) + framePrefixNotLikeQuery;
		String sql2 = SELECT_EXOUT_XM_OR_TCU_WITH_EXT_REQUIRED2.replaceAll("@PART_NAMES@", StringUtil.toSqlInString(xmPartNames));
		exOutVinList = findAllByNativeQuery(sql1 + sql2, params, Object[].class);
		return exOutVinList;
	}

	public List<Object[]> getVinXmRadioWithExtRequired(String xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, List<String> productIdPrefix){
		Parameters params = Parameters.with("1", vqOffPP);
		params.put("2", afOffPP);
		params.put("3", lastUpdate);
		// raw query results
		List<Object[]> vinList = null;
		String framePrefixNotLikeQuery = null;

		for(String prefix : productIdPrefix) {
		if(framePrefixNotLikeQuery == null)
			framePrefixNotLikeQuery = "INSTALLED_SN.part_serial_number NOT LIKE ('"+prefix+"%')";
		else
			framePrefixNotLikeQuery = framePrefixNotLikeQuery + " AND INSTALLED_SN.part_serial_number NOT LIKE ('"+prefix+"%')";
		}

		String sql1 = SELECT_XM_OR_TCU_WITH_EXT_REQUIRED1.replaceAll("@COUNTRY_CODE@", StringUtil.toSqlInString(countryCode)) + framePrefixNotLikeQuery;
		String sql2 = SELECT_XM_OR_TCU_WITH_EXT_REQUIRED2.replaceAll("@PART_NAMES@", StringUtil.toSqlInString(xmPartNames));

		vinList = findAllByNativeQuery(sql1 + sql2, params, Object[].class);
		return vinList;
	}

	public List<Object[]> getExOutVinXmRadioWithSubProduct(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, String trackingStatus, List<String> subProduct){
		Parameters params = Parameters.with("1", vqOffPP);
		params.put("2", afOffPP);
		params.put("3", lastUpdate);
		params.put("4", trackingStatus);

		// raw query results
		List<Object[]> exOutVinList = null;
		String sql = SELECT_EXOUT_XM_OR_TCU_WITH_SUB_PRODUCT.replaceAll("@PART_NAMES@", StringUtil.toSqlInString(xmPartNames));
		sql = sql.replaceAll("@SUB_PRODUCT_NAMES@", StringUtil.toSqlInString(subProduct));
		exOutVinList = findAllByNativeQuery(sql.replaceAll("@COUNTRY_CODE@", StringUtil.toSqlInString(countryCode)), params, Object[].class);
		return exOutVinList;
	}
	
	public List<Object[]> getExOutVinXmRadioWithSubSubProduct(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, String trackingStatus, List<String> subProduct, List<String> subSubProduct){
		Parameters params = Parameters.with("1", vqOffPP);
		params.put("2", afOffPP);
		params.put("3", lastUpdate);
		params.put("4", trackingStatus);

		List<Object[]> exOutVinList = null;
		String sql = SELECT_EXOUT_XM_OR_TCU_WITH_SUB_SUB_PRODUCT.replaceAll("@PART_NAMES@", StringUtil.toSqlInString(xmPartNames));
		sql = sql.replaceAll("@SUB_PRODUCT_NAMES@", StringUtil.toSqlInString(subProduct));
		sql = sql.replaceAll("@SUB_SUB_PRODUCT_NAMES@", StringUtil.toSqlInString(subSubProduct));
		exOutVinList = findAllByNativeQuery(sql.replaceAll("@COUNTRY_CODE@", StringUtil.toSqlInString(countryCode)), params, Object[].class);
		return exOutVinList;
	}

	public List<Object[]> getVQShipVinXmRadio(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode){
		Parameters params = Parameters.with("1", vqOffPP);
		params.put("2", afOffPP);
		params.put("3", lastUpdate);
		// raw query results
		List<Object[]> exOutVinList = null;
		String sql = SELECT_XM_OR_TCU.replaceAll("@PART_NAMES@", toSqlLikeString(xmPartNames,"PART_NAME"));
		exOutVinList = findAllByNativeQuery(sql.replaceAll("@COUNTRY_CODE@", StringUtil.toSqlInString(countryCode)), params, Object[].class);
		return exOutVinList;
	}
	
	public List<Object[]> getVQShipVinXmRadioFromLet(String partName, String productId){
		System.out.println("PartName from LET: " + partName);
		Parameters params = Parameters.with("1", partName);
		params.put("2", productId);
		List<Object[]> exOutVinList = null;
		System.out.println("SQL from LET: " + SELECT_TCU_FROM_LET);
		exOutVinList = findAllByNativeQuery(SELECT_TCU_FROM_LET, params, Object[].class);
		return exOutVinList;
	}

	public List<Object[]> getVIOSVQShipVinXmRadio(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode){
		Parameters params = Parameters.with("1", vqOffPP);
		params.put("2", afOffPP);
		params.put("3", lastUpdate);
		// raw query results
		List<Object[]> exOutVinList = null;
		String sql = SELECT_XM_OR_TCU_COMMON_NAME.replaceAll("@PART_NAMES@",  StringUtil.toSqlInString(xmPartNames));
		exOutVinList = findAllByNativeQuery(sql.replaceAll("@COUNTRY_CODE@", StringUtil.toSqlInString(countryCode)), params, Object[].class);
		return exOutVinList;
	}
	
	public List<Object[]> getVIOSExOutVinXmRadio(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, String trackingStatus){
		Parameters params = Parameters.with("1", vqOffPP);
		params.put("2", afOffPP);
		params.put("3", lastUpdate);
		params.put("4", trackingStatus);
		// raw query results
		List<Object[]> exOutVinList = null;
		String sql = SELECT_EXOUT_XM_OR_TCU_COMMON_NAME.replaceAll("@PART_NAMES@",  StringUtil.toSqlInString(xmPartNames));
		exOutVinList = findAllByNativeQuery(sql.replaceAll("@COUNTRY_CODE@", StringUtil.toSqlInString(countryCode)), params, Object[].class);
		return exOutVinList;
	}
	
	

	public List<Object[]> getVQShipVinXmRadioWithSubProduct(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, List<String> subProduct){
		Parameters params = Parameters.with("1", vqOffPP);
		params.put("2", afOffPP);
		params.put("3", lastUpdate);

		List<Object[]> vinList = null;
		String sql = SELECT_XM_OR_TCU_WITH_SUB_PRODUCT.replaceAll("@PART_NAMES@", StringUtil.toSqlInString(xmPartNames));
		sql = sql.replaceAll("@SUB_PRODUCT_NAMES@", StringUtil.toSqlInString(subProduct));
		vinList = findAllByNativeQuery(sql.replaceAll("@COUNTRY_CODE@", StringUtil.toSqlInString(countryCode)), params, Object[].class);
		return vinList;
	}

	public List<Object[]> getVQShipVinXmRadioWithSubSubProduct(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, List<String> subProduct, List<String> subSubProduct){
		Parameters params = Parameters.with("1", vqOffPP);
		params.put("2", afOffPP);
		params.put("3", lastUpdate);

		List<Object[]> vinList = null;
		String sql = SELECT_XM_OR_TCU_WITH_SUB_SUB_PRODUCT.replaceAll("@PART_NAMES@", StringUtil.toSqlInString(xmPartNames));
		sql = sql.replaceAll("@SUB_PRODUCT_NAMES@", StringUtil.toSqlInString(subProduct));
		sql = sql.replaceAll("@SUB_SUB_PRODUCT_NAMES@", StringUtil.toSqlInString(subSubProduct));
		vinList = findAllByNativeQuery(sql.replaceAll("@COUNTRY_CODE@", StringUtil.toSqlInString(countryCode)), params, Object[].class);
		return vinList;
	}
	
	public List<Map<String, Object>> findAllSalesWarrantyData(Timestamp startTimestamp, String[] selectingPps, String[] notSellableTrackingStatus, Map<String, String> processPointIds,Map<String, String> processLocations, String[] plantCodesToExclude) {
		String sqlStr = SELECT_SALES_WARRANTY_FR;
		sqlStr = sqlStr.replaceAll(":selectingProcessPointIds", StringUtil.toSqlInString(selectingPps));
		StringBuilder excOut = new StringBuilder();
		if (notSellableTrackingStatus != null && notSellableTrackingStatus.length > 0) {
			excOut.append(SALES_WARRANTY_EXC_OUT_TMPL.replaceAll(":notSellableTrackingStatus", StringUtil.toSqlInString(notSellableTrackingStatus))).append("\n");
		}
		sqlStr = sqlStr.replace("{SALES_WARRANTY_EXC_OUT_TMPL}", excOut.toString());

		StringBuilder historyColumns = new StringBuilder();
		StringBuilder historyTables = new StringBuilder();
		if (processPointIds != null && processLocations!= null) {
			for (String ppType : processPointIds.keySet()) {
				String ppId = processPointIds.get(ppType);
				if (StringUtils.isBlank(ppType) || StringUtils.isBlank(ppType)) {
					continue;
				}
				String processLocation = processLocations.get(ppType)!= null?processLocations.get(ppType):"AF";
				
				historyColumns.append(SALES_WARRANTY_HISTORY_COL_TMPL.replace("{pp_type}", ppType)).append("\n");
				historyTables.append(SALES_WARRANTY_HISTORY_TBL_TMPL.replace("{pp_type}", ppType).replace(":processPointId", StringUtil.toSqlInString(ppId)).replace(":processLocation", processLocation)).append("\n");
			}
		}

		sqlStr = sqlStr.replace("{SALES_WARRANTY_HISTORY_COL_TMPL}", historyColumns.toString());
		sqlStr = sqlStr.replace("{SALES_WARRANTY_HISTORY_TBL_TMPL}", historyTables.toString());

		if (processPointIds != null){
			if(processPointIds.keySet().contains("EXCEPTIONAL") && processPointIds.keySet().contains("SCRAPPED")){
				sqlStr = sqlStr.replace("{SALES_WARRANTY_SCRAP_EXCEPT_TMPL}", SALES_WARRANTY_SCRAP_EXCEPT_TMPL + "\n");
			} else {
				sqlStr = sqlStr.replace("{SALES_WARRANTY_SCRAP_EXCEPT_TMPL}", " ");
			}
		} else {
			sqlStr = sqlStr.replace("{SALES_WARRANTY_SCRAP_EXCEPT_TMPL}", " ");
		}

		StringBuilder strPlanToExclude = new StringBuilder();
		if (plantCodesToExclude!=null && plantCodesToExclude.length > 0) {
			strPlanToExclude.append(SALES_WARRANTY_EXCLUDE_PLANT_CODES.replace(":plantCodesToExclude", StringUtil.toSqlInString( plantCodesToExclude ))).append("\n");
		}
		sqlStr = sqlStr.replace("{SALES_WARRANTY_EXCLUDE_PLANT_CODES}", strPlanToExclude.toString());

		Parameters params = new Parameters();
		params.put("1", startTimestamp);
		return findResultMapByNativeQuery(sqlStr, params);
	}

	public String getSalesModelTypeCode(String productId) {
		Parameters params = Parameters.with("1", productId);
		return findFirstByNativeQuery(GET_SALES_MODEL_TYPE_CODE, params, String.class);
	}

	public Frame findByKDLotNumber(String kdLotNumber){
		return findFirst(Parameters.with("kdLotNumber", kdLotNumber));
	}

	public List<Frame> findAllByAfOnSeqRangeLineId(int startSequence, int endSequence,String lineId)
	{
		return findAllByQuery(SELECT_BY_AF_ON_SEQ_RANGE_LINE_ID, Parameters.with("startSequence", startSequence).put("endSequence", endSequence).put("lineId", lineId));
	}

	@Override
	public boolean isProductActiveForProductionDate(String lotPrefix, String productionDate, List<String> initialProcessPointIds) {
		String likeProductionLot;
		{
			StringBuilder likeProductionLotBuilder = new StringBuilder();
			likeProductionLotBuilder.append(lotPrefix);
			likeProductionLotBuilder.append(productionDate);
			likeProductionLotBuilder.append("%");
			likeProductionLot = likeProductionLotBuilder.toString();
		}
		Parameters params = Parameters.with("1", likeProductionLot);
		Object result;
		if (initialProcessPointIds == null || initialProcessPointIds.isEmpty()) {
			result = findFirstByNativeQuery(FIND_ACTIVE_PRODUCT_FOR_PRODUCTION_DATE, params, Object.class);
		} else {
			String inProcessPoints = StringUtil.toSqlInString(initialProcessPointIds);
			result = findFirstByNativeQuery(FIND_ACTIVE_PRODUCT_FOR_PRODUCTION_DATE_AND_PPIDS.replace("@INITIAL_PROCESS_POINT_IDS@", inProcessPoints), params, Object.class);
		}
		return (result != null);
	}

	@Override
	public boolean isProductTrackingStatusValidForProcessPoint(String productId, String processPointId) {
		Parameters params = Parameters.with("1", productId).put("2", processPointId);
		Frame result = findFirstByNativeQuery(FIND_VALID_PRODUCT_FOR_PROCESS_POINT, params);
		return result != null;
	}

	private String toSqlLikeString(List<String> params, String columnName) {
		StringBuilder sb = new StringBuilder();
		for(String s : params){
			if(sb.length() > 0) sb.append(" OR ");
			sb.append(columnName + " LIKE ");
			sb.append("\'%");
			sb.append(StringUtils.trim(s));
			sb.append("%\'");
		}

		return sb.toString();
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public int getScheduleQuantity(String productId, String planCode) {
		Parameters params = Parameters.with("1", productId);
		params.put("2", planCode);
		Integer scheduleQuantity = findFirstByNativeQuery(GET_SCHEDULE_QUANTITY, params, Integer.class);
		return scheduleQuantity == null ? 0 : scheduleQuantity;
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public int getPassQuantity(String productId, String processPointId) {
		Parameters params = Parameters.with("1", productId);
		params.put("2", processPointId);
		Integer passQuantity = findFirstByNativeQuery(GET_PASS_QUANTITY, params, Integer.class);
		return passQuantity == null ? 0 : passQuantity;
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public int getCutlot(String productId, String processPointId, String sequencingPPId) {
		Parameters params = Parameters.with("1", productId);
		params.put("2", processPointId);
		params.put("3", sequencingPPId);
		Integer cutlotQuantity = findFirstByNativeQuery(GET_CUTLOT_QUANTITY, params, Integer.class);
		return cutlotQuantity == null ? 0 : cutlotQuantity;
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public int getRebuild(String productId, String planCode) {
		Parameters params = Parameters.with("1", productId);
		params.put("2", planCode);
		Integer rebuildQuantity = findFirstByNativeQuery(GET_REBUILD_QUANTITY, params, Integer.class);
		return rebuildQuantity == null ? 0 : rebuildQuantity;
	}

	/** This method is used to return  most recent Asm seq no based on process point.
	 * @param processPointId
	 * @return String
	 */
	public List<Integer> findLatestAsmSeqNoByProcessPoint(String processPointId) {
		return findAllByNativeQuery(FIND_LATEST_ASM_SEQ_NO,Parameters.with("1", processPointId),Integer.class);
	}

	/* -------------------------------------------------------------------------------- */
	/* ---------------------------Begin find by field ranges--------------------------- */
	/* -------------------------------------------------------------------------------- */
	public List<Map<String,Object>> findByFieldRanges(DataContainer input) {
		final String[] lineNoRanges = getFindByFieldRanges(input, DataContainerTag.LINE_NO);
		final String[] afOnSequenceNumberRanges = getFindByFieldRanges(input, DataContainerTag.AF_ON_SEQUENCE_NUMBER);
		final String[] productIdRanges = getFindByFieldRanges(input, DataContainerTag.PRODUCT_ID);
		final String[] trackingStatusRanges = getFindByFieldRanges(input, DataContainerTag.TRACKING_STATUS);
		final String[] productSpecCodeRanges = getFindByFieldRanges(input, DataContainerTag.PRODUCT_SPEC_CODE);
		final String[] modelYearCodeRanges = getFindByFieldRanges(input, DataContainerTag.MODEL_YEAR_CODE);
		final String[] modelCodeRanges = getFindByFieldRanges(input, DataContainerTag.MODEL_CODE);
		final String[] modelTypeCodeRanges = getFindByFieldRanges(input, DataContainerTag.MODEL_TYPE_CODE);
		final String[] extColorCodeRanges = getFindByFieldRanges(input, DataContainerTag.EXT_COLOR_CODE);
		final String[] intColorCodeRanges = getFindByFieldRanges(input, DataContainerTag.INT_COLOR_CODE);
		final String[] engineSerialNoRanges = getFindByFieldRanges(input, DataContainerTag.ENGINE_SERIAL_NO);
		final String[] engineMtoRanges = getFindByFieldRanges(input, DataContainerTag.ENGINE_MTO);
		final String[] kdLotNumberRanges = getFindByFieldRanges(input, DataContainerTag.KD_LOT_NUMBER);
		final String[] productionLotRanges = getFindByFieldRanges(input, DataContainerTag.PRODUCTION_LOT);
		final String[] processPointIdRanges = getFindByFieldRanges(input, DataContainerTag.PROCESS_POINT_ID);
		final String[] actualTimestampRanges = getFindByFieldRanges(input, DataContainerTag.ACTUAL_TIMESTAMP);

		if (productSpecCodeRanges != null && (modelYearCodeRanges != null || modelCodeRanges != null || modelTypeCodeRanges != null || extColorCodeRanges != null || intColorCodeRanges != null)) {
			throw new TaskException("PRODUCT_SPEC_CODE cannot be specified if any of its components are specified");
		}
		if (actualTimestampRanges != null && processPointIdRanges == null) {
			throw new TaskException("At least one PROCESS_POINT_ID must be specified when at least one ACTUAL_TIMESTAMP is specified");
		}
		if (afOnSequenceNumberRanges == null && productIdRanges == null && trackingStatusRanges == null && engineSerialNoRanges == null && kdLotNumberRanges == null && productionLotRanges == null && processPointIdRanges == null) {
			throw new TaskException("At least one of the following must be specified: " +
					DataContainerTag.AF_ON_SEQUENCE_NUMBER + ", " +
					DataContainerTag.PRODUCT_ID + ", " +
					DataContainerTag.TRACKING_STATUS + ", " +
					DataContainerTag.ENGINE_SERIAL_NO + ", " +
					DataContainerTag.KD_LOT_NUMBER + ", " +
					DataContainerTag.PRODUCTION_LOT + ", " +
					DataContainerTag.PROCESS_POINT_ID);
		}

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(FIND_BY_FIELD_RANGES_BASE);
		if (processPointIdRanges != null) {
			sqlBuilder.append(FIND_BY_FIELD_RANGES_BASE_G215);
		}
		sqlBuilder.append(FIND_BY_FIELD_RANGES_JOIN);
		if (processPointIdRanges != null) {
			sqlBuilder.append(FIND_BY_FIELD_RANGES_JOIN_G215);
		}

		StringBuilder whereBuilder = new StringBuilder();
		appendFindByFieldRangesWhere(whereBuilder, "@lineNo", lineNoRanges, FIND_BY_FIELD_RANGES_WHERE_LINE_NO, FIND_BY_FIELD_RANGES_WHERE_LINE_NO_RANGE);
		if (afOnSequenceNumberRanges != null) {
			if (afOnSequenceNumberRanges.length == 1) {
				appendFindByFieldRangesWhere(whereBuilder, "@afOnSequenceNumber", afOnSequenceNumberRanges, FIND_BY_FIELD_RANGES_WHERE_AF_ON_SEQUENCE_NUMBER, null);
			} else if (afOnSequenceNumberRanges.length == 2) {
				final boolean isWrap = Integer.valueOf(afOnSequenceNumberRanges[0]) > Integer.valueOf(afOnSequenceNumberRanges[1]);
				if (isWrap) {
					appendFindByFieldRangesWhere(whereBuilder, "@afOnSequenceNumber", afOnSequenceNumberRanges, null, FIND_BY_FIELD_RANGES_WHERE_AF_ON_SEQUENCE_NUMBER_RANGE_WRAP, "99999", "0");
				} else {
					appendFindByFieldRangesWhere(whereBuilder, "@afOnSequenceNumber", afOnSequenceNumberRanges, null, FIND_BY_FIELD_RANGES_WHERE_AF_ON_SEQUENCE_NUMBER_RANGE);
				}
			} else if (afOnSequenceNumberRanges.length > 2) {
				appendFindByFieldRangesWhere(whereBuilder, "@afOnSequenceNumber", afOnSequenceNumberRanges, FIND_BY_FIELD_RANGES_WHERE_AF_ON_SEQUENCE_NUMBER_IN);
			}
		}
		if (productIdRanges != null) {
			if (productIdRanges.length == 1) {
				appendFindByFieldRangesWhere(whereBuilder, "@productId", productIdRanges, FIND_BY_FIELD_RANGES_WHERE_PRODUCT_ID, null);
			} else if (productIdRanges.length == 2) {
				if (productIdRanges[0].length() == 6 && productIdRanges[1].length() == 6) {
					appendFindByFieldRangesWhere(whereBuilder, "@productId", productIdRanges, null, FIND_BY_FIELD_RANGES_WHERE_PRODUCT_ID_RANGE);
				} else {
					appendFindByFieldRangesWhere(whereBuilder, "@productId", productIdRanges, FIND_BY_FIELD_RANGES_WHERE_PRODUCT_ID_IN);
				}
			} else if (productIdRanges.length > 2) {
				appendFindByFieldRangesWhere(whereBuilder, "@productId", productIdRanges, FIND_BY_FIELD_RANGES_WHERE_PRODUCT_ID_IN);
			}
		}
		if (trackingStatusRanges != null) {
			if (trackingStatusRanges.length == 1) {
				appendFindByFieldRangesWhere(whereBuilder, "@trackingStatus", trackingStatusRanges, FIND_BY_FIELD_RANGES_WHERE_TRACKING_STATUS, null);
			} else {
				appendFindByFieldRangesWhere(whereBuilder, "@trackingStatus", trackingStatusRanges, FIND_BY_FIELD_RANGES_WHERE_TRACKING_STATUS_IN);
			}
		}
		appendFindByFieldRangesWhere(whereBuilder, "@productSpecCode", productSpecCodeRanges, FIND_BY_FIELD_RANGES_WHERE_PRODUCT_SPEC_CODE, FIND_BY_FIELD_RANGES_WHERE_PRODUCT_SPEC_CODE_RANGE);
		appendFindByFieldRangesWhere(whereBuilder, "@modelYearCode", modelYearCodeRanges, FIND_BY_FIELD_RANGES_WHERE_MODEL_YEAR_CODE, FIND_BY_FIELD_RANGES_WHERE_MODEL_YEAR_CODE_RANGE);
		appendFindByFieldRangesWhere(whereBuilder, "@modelCode", modelCodeRanges, FIND_BY_FIELD_RANGES_WHERE_MODEL_CODE, FIND_BY_FIELD_RANGES_WHERE_MODEL_CODE_RANGE);
		appendFindByFieldRangesWhere(whereBuilder, "@modelTypeCode", modelTypeCodeRanges, FIND_BY_FIELD_RANGES_WHERE_MODEL_TYPE_CODE, FIND_BY_FIELD_RANGES_WHERE_MODEL_TYPE_CODE_RANGE);
		appendFindByFieldRangesWhere(whereBuilder, "@extColorCode", extColorCodeRanges, FIND_BY_FIELD_RANGES_WHERE_EXT_COLOR_CODE, FIND_BY_FIELD_RANGES_WHERE_EXT_COLOR_CODE_RANGE);
		appendFindByFieldRangesWhere(whereBuilder, "@intColorCode", intColorCodeRanges, FIND_BY_FIELD_RANGES_WHERE_INT_COLOR_CODE, FIND_BY_FIELD_RANGES_WHERE_INT_COLOR_CODE_RANGE);
		appendFindByFieldRangesWhere(whereBuilder, "@engineSerialNo", engineSerialNoRanges, FIND_BY_FIELD_RANGES_WHERE_ENGINE_SERIAL_NO, FIND_BY_FIELD_RANGES_WHERE_ENGINE_SERIAL_NO_RANGE);
		appendFindByFieldRangesWhere(whereBuilder, "@engineMto", engineMtoRanges, FIND_BY_FIELD_RANGES_WHERE_ENGINE_MTO, FIND_BY_FIELD_RANGES_WHERE_ENGINE_MTO_RANGE);
		appendFindByFieldRangesWhere(whereBuilder, "@kdLotNumber", kdLotNumberRanges, FIND_BY_FIELD_RANGES_WHERE_KD_LOT_NUMBER, FIND_BY_FIELD_RANGES_WHERE_KD_LOT_NUMBER_RANGE);
		appendFindByFieldRangesWhere(whereBuilder, "@productionLot", productionLotRanges, FIND_BY_FIELD_RANGES_WHERE_PRODUCTION_LOT, FIND_BY_FIELD_RANGES_WHERE_PRODUCTION_LOT_RANGE);
		if (processPointIdRanges != null) {
			if (processPointIdRanges.length == 1) appendFindByFieldRangesWhere(whereBuilder, "@processPointId", processPointIdRanges, FIND_BY_FIELD_RANGES_WHERE_PROCESS_POINT_ID, null);
			else appendFindByFieldRangesWhere(whereBuilder, "@processPointId", processPointIdRanges, FIND_BY_FIELD_RANGES_WHERE_PROCESS_POINT_ID_IN);
		}
		appendFindByFieldRangesWhere(whereBuilder, "@actualTimestamp", actualTimestampRanges, FIND_BY_FIELD_RANGES_WHERE_ACTUAL_TIMESTAMP, FIND_BY_FIELD_RANGES_WHERE_ACTUAL_TIMESTAMP_RANGE);

		sqlBuilder.append(" WHERE ");
		sqlBuilder.append(whereBuilder.toString());
		sqlBuilder.append(" ORDER BY G143.AF_ON_SEQUENCE_NUMBER");

		final String sql = sqlBuilder.toString();
		List<Object[]> results = executeNative(sql);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (results != null && !results.isEmpty()) {
			Iterator<Object[]> iterator = results.iterator();
			while (iterator.hasNext()) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				Object[] result = iterator.next();
				resultMap.put(DataContainerTag.LINE_NO, StringUtils.trim(ObjectUtils.toString(result[0], null)));
				resultMap.put(DataContainerTag.AF_ON_SEQUENCE_NUMBER, result[1]);
				{
					final String productId = StringUtils.trim(ObjectUtils.toString(result[2], null));
					resultMap.put(DataContainerTag.PRODUCT_ID, productId);
					resultMap.put(DataContainerTag.VIN_SERIAL, StringUtils.substring(productId, -6));
				}
				resultMap.put(DataContainerTag.TRACKING_STATUS, StringUtils.trim(ObjectUtils.toString(result[3], null)));
				resultMap.put(DataContainerTag.PRODUCT_SPEC_CODE, result[4]);
				resultMap.put(DataContainerTag.ENGINE_SERIAL_NO, StringUtils.trim(ObjectUtils.toString(result[5], null)));
				resultMap.put(DataContainerTag.ENGINE_MTO, StringUtils.trim(ObjectUtils.toString(result[6], null)));
				resultMap.put(DataContainerTag.KD_LOT_NUMBER, StringUtils.trim(ObjectUtils.toString(result[7], null)));
				resultMap.put(DataContainerTag.PARKING_LOCATION, StringUtils.trim(ObjectUtils.toString(result[8], null)));
				if (processPointIdRanges != null) {
					resultMap.put(DataContainerTag.PROCESS_POINT_ID, StringUtils.trim(ObjectUtils.toString(result[9], null)));
					resultMap.put(DataContainerTag.ACTUAL_TIMESTAMP, result[10]);
				}
				resultList.add(resultMap);
			}
		}
		return resultList;
	}

	private String[] getFindByFieldRanges(DataContainer input, String field) {
		String rangesCsv = input.getString(field);
		if (StringUtils.isEmpty(rangesCsv)) return null;
		String[] ranges = rangesCsv.split(",");
		switch (ranges.length) {
		case 0:
			return null;
		case 1:
			return ranges;
		case 2:
			return ranges;
		default:
			if (DataContainerTag.PROCESS_POINT_ID.equals(field)
					|| DataContainerTag.PRODUCT_ID.equals(field)
					|| DataContainerTag.AF_ON_SEQUENCE_NUMBER.equals(field)
					|| DataContainerTag.TRACKING_STATUS.equals(field)) {
				return ranges;
			}
			throw new TaskException("Invalid findByFieldRanges value for field " + field + ": " + rangesCsv);
		}
	}

	private void appendFindByFieldRangesWhere(StringBuilder whereBuilder, String fieldName, String[] ranges, String singleSql, String rangeSql) {
		appendFindByFieldRangesWhere(whereBuilder, fieldName, ranges, singleSql, rangeSql, null, null);
	}

	private void appendFindByFieldRangesWhere(StringBuilder whereBuilder, String fieldName, String[] ranges, String singleSql, String rangeSql, String rangeMax, String rangeMin) {
		if (ranges != null) {
			if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
			if (ranges.length == 1) {
				whereBuilder.append(singleSql.replace(fieldName, ranges[0]));
			} else {
				String fieldNameFrom = fieldName + "From";
				String fieldNameTo = fieldName + "To";
				if (rangeMax != null && rangeMin != null) {
					String fieldNameMax = fieldName + "Max";
					String fieldNameMin = fieldName + "Min";
					whereBuilder.append(rangeSql.replace(fieldNameFrom, ranges[0]).replace(fieldNameTo, ranges[1]).replace(fieldNameMax, rangeMax).replace(fieldNameMin, rangeMin));
				} else {
					whereBuilder.append(rangeSql.replace(fieldNameFrom, ranges[0]).replace(fieldNameTo, ranges[1]));
				}
			}
		}
	}

	private void appendFindByFieldRangesWhere(StringBuilder whereBuilder, String fieldName, String[] ranges, String inSql) {
		if (ranges != null) {
			if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
			whereBuilder.append(inSql.replace(fieldName, StringUtil.toSqlInString(ranges)));
		}
	}
	/* -------------------------------------------------------------------------------- */
	/* ----------------------------End find by field ranges---------------------------- */
	/* -------------------------------------------------------------------------------- */


	public Frame findFrameByLineRefNumber(int lineRef, String onProcessPointId, int refLength) {
		int divisor =(int) Math.pow(10, refLength);
		Parameters params = Parameters.with("1", onProcessPointId).put("2", divisor);
		params.put("3", lineRef);

		return findFirstByNativeQuery(FIND_VIN_BY_LINE_REF, params);
	}

	public List<ProcessProductDto> findAllProcessedProductsForProcessPoint(String ppId, int rowLimit) {
		Parameters params = Parameters.with("1", ppId);
		String sql = FIND_ALL_PROCESSED + "Fetch first "+ rowLimit +" rows Only";
		return findAllByNativeQuery(sql, params, ProcessProductDto.class);
	}

	
	public String findFirstByInstalledPart(String partName, String partId) {
        Parameters params = Parameters.with("1", partName);
        params.put("2", partId);
        return findFirstByNativeQuery(FIND_ALL_BY_INSTALLED_PARTS, params, String.class);
    }
	
	public List<Frame> findAllByProductCarrier(String processPointId, String currentProductId,
			int processedSize, int upcomingSize) {
		List<Frame> products = new ArrayList<Frame>();
		List<Frame> processedProducts  = findProcessedProductCarrierProducts(processPointId, currentProductId, processedSize);
		// reverse the list and removed the current product id since it is in the upcomingEngines list
		for(int i = processedProducts.size() - 1; i >= 1; i--)
			products.add(processedProducts.get(i));
		List<Frame> upcomingProducts  = findUpcomingProductCarrierProducts(processPointId, currentProductId, upcomingSize);
		for(Frame product : upcomingProducts) products.add(product);
		return products;
	}

	private List<Frame> findProcessedProductCarrierProducts(String processPointId, String currentProductId, int processedSize) {
		Parameters params = Parameters.with("1", processPointId);
		params.put("2", currentProductId);
		return findAllByNativeQuery(
				FIND_PROCESSED_PRODUCTS_BY_CARRIER_TBX + "fetch first " + (processedSize+1) + " rows ONLY", params);
	}

	private List<Frame> findUpcomingProductCarrierProducts(String processPointId, String currentProductId, int upcomingSize) {
		Parameters params = Parameters.with("1", processPointId);
		params.put("2", currentProductId);
		return findAllByNativeQuery(
				FIND_UPCOMING_PRODUCTS_BY_CARRIER_TBX + "fetch first " + (upcomingSize+1) + " rows ONLY", params);
	}

	@Override
	public List<Frame> getProductsWithinSerialRange(String startSeq, String stopSeq) {
		Parameters params = new Parameters();
		String sql = GET_PRODUCTS_WITHIN_SERIAL_RANGE;

			params.put("1", startSeq);
			params.put("2", stopSeq);

		return findAllByNativeQuery(sql, params);
	}

	@Override
	public List<Frame> findAllByProductionLotRangeAndPlanCode(String startLot, String endLot,String planCode) {
		Parameters params = new Parameters();
		String sql = GET_PRODUCTS_WITHIN_PROD_LOT_RANGE;

			params.put("1", startLot);
			params.put("2", endLot);
			params.put("3", planCode);

		return findAllByNativeQuery(sql, params);
	}

	public List<Object[]> findVinsInKdLotSortByAfOnTimestamp(String kdLotNumber, String processPointId, String productId) {
		Parameters params = Parameters.with("1", kdLotNumber).put("2", processPointId).put("3", productId);
		return findAllByNativeQuery(FIND_VINS_IN_KDLOT_SORT_BY_AF_ON_TIMESTAMP, params, Object[].class);
	}

	public List<Frame> findByRanges(String startAfSeq, String endAfSeq, String startProdLot, String endProdLot,
				String startVinSeq, String endVinSeq, List<String> modelYearCode, List<String> modelCode, List<String> modelTypeCode,
				List<String> destCode, String planCode,List<String> trackingStatuses) {

			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("SELECT * FROM GAL143TBX G143 JOIN GAL212TBX G212 ON G212.PRODUCTION_LOT = G143.PRODUCTION_LOT JOIN GAL144TBX G144 ON G144.PRODUCT_SPEC_CODE = G143.PRODUCT_SPEC_CODE");

			String filterCriteria = prepareFilter(startAfSeq,endAfSeq,startProdLot,endProdLot,startVinSeq,endVinSeq, modelYearCode,modelCode, modelTypeCode,destCode,planCode,trackingStatuses);

			sqlBuilder.append(" WHERE ");
			sqlBuilder.append(filterCriteria);
			sqlBuilder.append(" ORDER BY G143.AF_ON_SEQUENCE_NUMBER");

			final String sql = sqlBuilder.toString();

			List<Frame> results = findAllByNativeQuery(sql, null, Frame.class);


			return results;
		}

		public List<Frame> findProductsBySpecCode(List<String> productIds,List<String> modelYearCode, List<String> modelCode, List<String> modelTypeCode){
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("SELECT * FROM GAL143TBX G143 JOIN GAL144TBX G144 ON G144.PRODUCT_SPEC_CODE = G143.PRODUCT_SPEC_CODE");

			StringBuilder whereBuilder = new StringBuilder();
				if(!productIds.isEmpty()) {

					 whereBuilder.append("G143.PRODUCT_ID IN ("+StringUtil.toSqlInString(productIds)+")");
				}

				if(!modelYearCode.isEmpty()) {
					if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
					 whereBuilder.append("G144.MODEL_YEAR_CODE IN ("+StringUtil.toSqlInString(modelYearCode)+")");
				}
				if(!modelCode.isEmpty()) {
					if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
					 whereBuilder.append("G144.MODEL_CODE IN ("+StringUtil.toSqlInString(modelCode)+")");
				}
				if(!modelTypeCode.isEmpty()) {
					if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
					 whereBuilder.append("G144.MODEL_TYPE_CODE IN ("+StringUtil.toSqlInString(modelTypeCode)+")");
				}




			sqlBuilder.append(" WHERE ");
			sqlBuilder.append(whereBuilder.toString());
			sqlBuilder.append(" ORDER BY G143.PRODUCT_ID");

			final String sql = sqlBuilder.toString();

			List<Frame> results = findAllByNativeQuery(sql, null, Frame.class);


			return results;
		}

		@Override
		public long count(String startAfSeq, String endAfSeq, String startProdLot, String endProdLot,
				String startVinSeq, String endVinSeq, List<String> modelYearCode, List<String> modelCode,
				List<String> modelTypeCode, List<String> destCode, String planCode,List<String> trackingStatuses) {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("SELECT COUNT(G143.PRODUCT_ID) FROM GAL143TBX G143 JOIN GAL212TBX G212 ON G212.PRODUCTION_LOT = G143.PRODUCTION_LOT JOIN GAL144TBX G144 ON G144.PRODUCT_SPEC_CODE = G143.PRODUCT_SPEC_CODE");

			String filterCriteria = prepareFilter(startAfSeq,endAfSeq,startProdLot,endProdLot,startVinSeq,endVinSeq, modelYearCode,modelCode, modelTypeCode,destCode,planCode, trackingStatuses);

			sqlBuilder.append(" WHERE ");
			sqlBuilder.append(filterCriteria);


			final String sql = sqlBuilder.toString();
			return countByNativeSql(sql, null);

		}

		protected String prepareFilter(String startAfSeq, String endAfSeq, String startProdLot, String endProdLot,
				String startVinSeq, String endVinSeq, List<String> modelYearCode, List<String> modelCode,
				List<String> modelTypeCode, List<String> destCode, String planCode,List<String> trackingStatuses) {

			StringBuilder whereBuilder = new StringBuilder();

			 if (!StringUtils.isEmpty(startAfSeq) && !StringUtils.isEmpty(endAfSeq)) {
				 if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
				 whereBuilder.append("G143.AF_ON_SEQUENCE_NUMBER BETWEEN "+startAfSeq+" AND "+endAfSeq);
			 }

			 if (!StringUtils.isEmpty(startVinSeq) && !StringUtils.isEmpty(endVinSeq)) {
				if (startVinSeq.length() == 6 && endVinSeq.length() == 6) {
					if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
					 whereBuilder.append("SUBSTR(G143.PRODUCT_ID,12,6) BETWEEN '"+startVinSeq+"' AND '"+endVinSeq+"'");
				}
			}

			if(!modelYearCode.isEmpty()) {
				if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
				 whereBuilder.append("G144.MODEL_YEAR_CODE IN ("+StringUtil.toSqlInString(modelYearCode)+")");
			}
			if(!modelCode.isEmpty()) {
				if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
				 whereBuilder.append("G144.MODEL_CODE IN ("+StringUtil.toSqlInString(modelCode)+")");
			}
			if(!modelTypeCode.isEmpty()) {
				if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
				 whereBuilder.append("G144.MODEL_TYPE_CODE IN ("+StringUtil.toSqlInString(modelTypeCode)+")");
			}
			if(destCode!= null && !destCode.isEmpty()) {
				if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
				 whereBuilder.append("G144.SALES_MODEL_TYPE_CODE IN ("+StringUtil.toSqlInString(destCode)+")");
			}
			if (!StringUtils.isEmpty(startProdLot) && !StringUtils.isEmpty(endProdLot)) {
				if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
				 whereBuilder.append("G143.PRODUCTION_LOT BETWEEN '"+startProdLot+"' AND '"+endProdLot+"'");
			}
			if (!StringUtils.isEmpty(planCode)) {
				if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
				 whereBuilder.append("G212.PLAN_CODE = '"+planCode+"'");
			}
			if(!trackingStatuses.isEmpty()) {
				if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
				 whereBuilder.append("(G143.TRACKING_STATUS IS NULL OR G143.TRACKING_STATUS NOT IN ("+StringUtil.toSqlInString(trackingStatuses)+"))");
			}

			return whereBuilder.toString();
	}


		@Override
		public Integer findNextSequencePlateNumber(String uniqueSpeCode) {
			
			String sql = FIND_NEXT_SEQUENCE_PLATE_NUMBER.replaceAll("@UNIQUE_SPEC_CODE@", toSqlLikeString(Arrays.asList(uniqueSpeCode),"PRODUCT_SPEC_CODE"));
			return findFirstByNativeQuery(sql, null, Integer.class);
			
		}
		

		@Override
		public List<Frame> getProductsBySpecCode(String productSpecCode,List<String> productionLots) {
			Parameters parameters = Parameters.with("1",productSpecCode);
			String strString = GET_PRODUCT_LIST;
			strString = strString.replace(":productionLot",  StringUtil.toSqlInString(productionLots));
			List<Frame> frameList = findAllByNativeQuery(strString, parameters);
			return frameList;
		}
		
		@Transactional
		public void updateFrameDetails(String productCodeForUpdate,
				FrameSpecDto selectedFrameSpecDto, List<String> productionLots) {
			
			Parameters params2 = Parameters.with("1", productCodeForUpdate)
					.put("2", selectedFrameSpecDto.getProductSpecCode());
			
			
			String sqlStr = UPDATE_COLOR_DETAILS_3;
			sqlStr = sqlStr.replaceAll(":productionLot", StringUtil.toSqlInString(productionLots));
			executeNative(sqlStr, params2);
		}
		

		public Frame findByKDLotNumber(String kdLotNumber, List<String> scrapLines) {
			Parameters params = Parameters.with("1", kdLotNumber);
			String sqlStr = FRAME_BY_KD_LOT;
			if(scrapLines != null  && scrapLines.size() >0) {
				sqlStr = sqlStr.replaceAll(":scrapLines", StringUtil.toSqlInString(scrapLines));
			}
			
			return findFirstByNativeQuery(sqlStr,params,Frame.class);
		}
		
		@Override
		public List<Frame> findAllByKDLotNumber(String kdLotNumber) {
			return findAll(Parameters.with("kdLotNumber", kdLotNumber));
		}
		@Override
		public List<Object[]> fetchTrackingDetailsByModelDeptAndLoc(String modelCode, String departments, String requestingLocation) {
		    return fetchTrackingDetailsByModelDeptAndVin(modelCode, departments, requestingLocation, null);
		}
		
		@Override
		public List<Object[]> fetchTrackingDetailsByModelDeptAndVin(String modelCode, String departments, String requestingLocation, String productId) {
			Logger.getLogger().info("Started Fetching Tracking Details.........." );
			List<String> modelCodesList = new ArrayList<>();
			List<String> departmentsList = new ArrayList<>();
	
			Map<String, String> modelCodesMap = PropertyService.getPropertyMap("EXTERNAL_GATEWAY", "ALLOWED_MODELS");
			String availableModels = modelCodesMap.get(requestingLocation);
	
			if (!StringUtils.isEmpty(availableModels)) {
				modelCodesList = Arrays.asList(availableModels.split(","));
			}
	
			if (!StringUtils.isEmpty(departments)) {
				departmentsList = Arrays.asList(departments.split(","));
			}
	
			List<String> filteredModels = new ArrayList<String>();
	
			if (modelCode.contains("*")) {
				filteredModels.addAll(modelCodesList);
				Logger.getLogger().info("Wildcard found in modelCode, adding all available models to filteredModels. " + filteredModels);
			} else {
				for (String model : modelCodesList) {
					if (modelCode.contains(model)) {
						filteredModels.add(model);
						Logger.getLogger().info("Adding model " + model + " to filtered models");
					}
				}
			}
			
		    String sqlStr = GET_PRODUCT_TRACKING_INFO.replaceAll(":modelCodes", StringUtil.toSqlInString(filteredModels))
					.replaceAll(":departments", StringUtil.toSqlInString(departmentsList));

		    Logger.getLogger().info("Constructed SQL query (without product ID): {} " + sqlStr);
		        if (!StringUtils.isEmpty(productId) && !productId.trim().isEmpty()) {
		            sqlStr = sqlStr.replace("order by", "AND g143.PRODUCT_ID = '" + productId + "' ORDER BY");
			    Logger.getLogger().info("Modified SQL query to include productId " + sqlStr);
		        }

			List<Object[]> result = findAllByNativeQuery(sqlStr, null, Object[].class);
			Logger.getLogger().info("Query executed, number of results fetched: " + result.size());
			
			return result;
		}
}
