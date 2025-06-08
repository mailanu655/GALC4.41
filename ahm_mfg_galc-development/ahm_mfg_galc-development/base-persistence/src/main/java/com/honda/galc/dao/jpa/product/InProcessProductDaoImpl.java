package com.honda.galc.dao.jpa.product;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dto.InProcessProductMaintenanceDTO;
import com.honda.galc.dto.LineDetail;
import com.honda.galc.dto.LineOverview;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

/**
 * 
 * <h3>InProcessProductDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> InProcessProductDaoImpl description </p>
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
 * @author Paul Chou
 * Sep 29, 2010
 *
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class InProcessProductDaoImpl extends BaseDaoImpl<InProcessProduct, String> implements InProcessProductDao {
	private int DEFAULT_STALE_TOLERANCE = 30000;
	private Hashtable<String, LineOverview> lineMap = new Hashtable<String, LineOverview>();

	private static final String FIND_LAST_FOR_LINE1 = "select * from galadm.gal176tbx where LINE_ID = ?1 "
			+ "and NEXT_PRODUCT_ID is null order by UPDATE_TIMESTAMP desc";

	private static final String ACQUIRE_TARGET_LINE_LOCK = "SELECT * FROM GAL176TBX WHERE LINE_ID = ?1 AND NEXT_PRODUCT_ID IS NULL WITH RR USE AND KEEP EXCLUSIVE LOCKS";

	private static final String FIND_INPROCESS_PRODUCTS_TO_RESEQUENCE = "SELECT * FROM GAL176TBX WHERE PRODUCT_ID = ?1 OR NEXT_PRODUCT_ID = ?2 FOR UPDATE";

	private static final String FIND_INPROCESS_PRODUCTS_FOR_UPDATE = "SELECT * FROM GAL176TBX WHERE PRODUCT_ID = ?1 FOR UPDATE";
	
	private static final String FIND_LAST_FOR_LINE = "select p from InProcessProduct p where p.lineId = :lineId and p.nextProductId is null order by p.updateTimestamp desc";

	private static final String GET_VIN_SEQ = "WITH VIN_SEQ (PRODUCT_ID, NEXT_PRODUCT_ID, ID_SEQ) "
			+ "AS (SELECT a.PRODUCT_ID, a.NEXT_PRODUCT_ID, 1 AS ID_SEQ FROM GAL176TBX a "
			+ "WHERE a.PRODUCT_ID = ?1 "
			+ // START WITH THE TARGET VIN
			"UNION ALL SELECT b.PRODUCT_ID, b.NEXT_PRODUCT_ID, N.ID_SEQ + 1 AS ID_SEQ FROM GAL176TBX b, VIN_SEQ AS N "
			+ "WHERE b.PRODUCT_ID = N.NEXT_PRODUCT_ID) select PRODUCT_ID from VIN_SEQ WITH CS FOR READ ONLY";

	private static final String LOOK_AHEAD_FOR_PRODUCT_ID = "with vin_seq (product_id, next_product_id, depth) as "
			+ "(select a.product_id, a.next_product_id, 1 as depth "
			+ "from gal176tbx a "
			+ "where a.product_id = ?1 "
			+ "union all "
			+ "select b.product_id, b.next_product_id, seq.depth + 1 as depth "
			+ "from gal176tbx b, vin_seq seq "
			+ "where b.product_id = seq.next_product_id and seq.depth < 10000) "
			+ "select product_id from vin_seq where product_id = ?2 with cs for read only";

	private static final String FIND_SEQUENCE_LIST = " with x (product_id,next_product_id, line_id ) as    "
			+ " (   "
			+ "   select root.product_id,root.next_product_id, root.line_id   "
			+ "     from gal176tbx root, gal195tbx b "
			+ "    where root.next_product_id is null   "
			+ "      and root.LINE_ID = b.line_id "
			+ "      and b.DIVISION_ID = ?1 "
			+ "    union all  "
			+ "   select sub.product_id,sub.next_product_id, sub.line_id  "
			+ "     from gal176tbx sub, x super  "
			+ "    where sub.next_product_id =  super.product_id   "
			+ " ) select x.product_id,x.next_product_id,x.line_id from x,gal195tbx y where  x.line_id = y.line_id order by y.line_sequence_number "
			+ " with cs for read only";

	private final static String FIND_FIRST_UNCONFIRMED_TEST = "WITH VIN_SEQ(PRODUCT_ID, NEXT_PRODUCT_ID, ID_SEQ) "
			+ "AS (SELECT a.PRODUCT_ID, a.NEXT_PRODUCT_ID, 1 AS ID_SEQ "
			+ "FROM GAL176TBX a "
			+ "WHERE a.NEXT_PRODUCT_ID is null and a.LINE_ID = ?1 "
			+ // LINE ID BEING SEARCHED
			"UNION ALL "
			+ "SELECT b.PRODUCT_ID, b.NEXT_PRODUCT_ID, N.ID_SEQ + 1 AS ID_SEQ "
			+ "FROM GAL176TBX b, VIN_SEQ AS N "
			+ "WHERE b.NEXT_PRODUCT_ID = N.PRODUCT_ID) "
			+ "select z.PRODUCT_ID "
			+ "from (select VIN_SEQ.PRODUCT_ID, VIN_SEQ.ID_SEQ, MAX(c.ACTUAL_TIMESTAMP) as test_timestamp, "
			+ "max(d.ACTUAL_TIMESTAMP) as confirm_timestamp "
			+ "from VIN_SEQ "
			+ "join gal215tbx c "
			+ "on c.PRODUCT_ID = VIN_SEQ.PRODUCT_ID and c.PROCESS_POINT_ID = ?2 "
			+ // PROCESS POINT RUNNING TEST
			"left join gal215tbx d "
			+ "on d.PRODUCT_ID = VIN_SEQ.PRODUCT_ID and d.PROCESS_POINT_ID = ?3 "
			+ // PROCESS POINT CONFIRMING TEST
			"group by VIN_SEQ.PRODUCT_ID, VIN_SEQ.ID_SEQ) z "
			+ "where z.test_timestamp > z.confirm_timestamp or z.confirm_timestamp is null "
			+ "order by z.ID_SEQ desc";

	private final static String GET_LINE_OVERVIEW = "WITH SORTEDLIST(PRODUCT_ID, LAST_PASSING_PROCESS_POINT_ID, Level) "
			+ "AS (SELECT TAIL.PRODUCT_ID, TAIL.LAST_PASSING_PROCESS_POINT_ID, 0 as Level "
			+ "FROM GAL176TBX TAIL "
			+ "WHERE TAIL.NEXT_PRODUCT_ID is null "
			+ "AND TAIL.LINE_ID = ?1 "
			+ "UNION ALL "
			+ "SELECT LL.PRODUCT_ID, LL.LAST_PASSING_PROCESS_POINT_ID, S.Level + 1 as Level "
			+ "FROM GAL176TBX LL, SORTEDlIST AS S "
			+ "WHERE LL.NEXT_PRODUCT_ID = S.PRODUCT_ID "
			+ "AND LL.LINE_ID = ?1) "
			+ "SELECT CAST(ROWNUMBER() OVER(ORDER BY LEVEL DESC)AS INT) AS ROWNUM, "
			+ "FRAME.AF_ON_SEQUENCE_NUMBER, "
			+ "MAX(FRAME.AF_ON_SEQUENCE_NUMBER) OVER (ORDER BY LEVEL DESC ROWS BETWEEN 1 FOLLOWING AND 1 FOLLOWING), "
			+ "SORTEDLIST.PRODUCT_ID, "
			+ "MAX(FRAME.PRODUCT_SPEC_CODE), "
			+ "MAX(PRODUCT.PROD_LOT_KD), "
			+ "MAX(PRODUCT.PROD_LOT_KD) OVER (ORDER BY LEVEL DESC ROWS BETWEEN 1 PRECEDING AND 1 PRECEDING), "
			+ "COUNT(DISTINCT DEFECT.DEFECTRESULTID), "
			+ "COUNT(DISTINCT CASE WHEN (PARTS.INSTALLED_PART_STATUS < 1 OR PARTS.INSTALLED_PART_STATUS IS NULL) AND RULES.PART_CONFIRM_FLAG = 1 THEN RULES.PART_NAME END), "
			+ "COUNT(DISTINCT MEASUREMENT_ATTEMPT_ID), "
			+ "MAX(PROCESS.PROCESS_POINT_NAME), MAX(PROCESS.ALL_SEQ_NUM), MAX(SORTEDLIST.LAST_PASSING_PROCESS_POINT_ID) "
			+ "FROM SORTEDLIST "
			+ "LEFT JOIN GAL143TBX FRAME ON SORTEDLIST.PRODUCT_ID = FRAME.PRODUCT_ID "
			+ "LEFT JOIN GAL217TBX PRODUCT ON FRAME.PRODUCTION_LOT = PRODUCT.PRODUCTION_LOT "
			+ "LEFT JOIN GAL125TBX DEFECT ON SORTEDLIST.PRODUCT_ID = DEFECT.PRODUCT_ID AND DEFECT.DEFECT_STATUS = 0 "
			+ "LEFT JOIN GAL144TBX MTOC ON FRAME.PRODUCT_SPEC_CODE = MTOC.PRODUCT_SPEC_CODE "
			+ "LEFT JOIN HMIN_MEASUREMENT_ATTEMPTTBX MEASUREMENT ON SORTEDLIST.PRODUCT_ID = MEASUREMENT.PRODUCT_ID AND MEASUREMENT.LAST_ATTEMPT = 1 AND MEASUREMENT.MEASUREMENT_STATUS < 1 "
			+ "LEFT JOIN (SELECT P.PROCESS_POINT_ID, "
			+ "P.PROCESS_POINT_NAME, "
			+ "(COALESCE(D.SEQUENCE_NUMBER,0)+1)*1000000+(COALESCE(L.LINE_SEQUENCE_NUMBER,0)+1)*1000+(COALESCE(P.SEQUENCE_NUMBER,0)+1) ALL_SEQ_NUM "
			+ "FROM GALADM.GAL214TBX P "
			+ "LEFT JOIN GALADM.GAL195TBX L "
			+ "ON P.LINE_ID = L.LINE_ID "
			+ "LEFT JOIN GALADM.GAL128TBX D "
			+ "ON L.DIVISION_ID=D.DIVISION_ID "
			+ "ORDER BY D.SEQUENCE_NUMBER,L.LINE_SEQUENCE_NUMBER,P.SEQUENCE_NUMBER) PROCESS "
			+ "ON PROCESS.PROCESS_POINT_ID = SORTEDLIST.LAST_PASSING_PROCESS_POINT_ID "
			+ "LEFT JOIN GALADM.GAL246TBX RULES "
			+ "ON (   RULES.MODEL_YEAR_CODE = '*' "
			+ "OR MTOC.MODEL_YEAR_CODE = RULES.MODEL_YEAR_CODE) "
			+ "AND (   RULES.MODEL_CODE = '*' "
			+ "OR MTOC.MODEL_CODE = RULES.MODEL_CODE) "
			+ "AND (   RULES.MODEL_TYPE_CODE = '*' "
			+ "OR MTOC.MODEL_TYPE_CODE = RULES.MODEL_TYPE_CODE) "
			+ "AND (   RULES.MODEL_OPTION_CODE = '*' "
			+ "OR MTOC.MODEL_OPTION_CODE = RULES.MODEL_OPTION_CODE) "
			+ "AND (   RULES.EXT_COLOR_CODE = '*' "
			+ "OR MTOC.EXT_COLOR_CODE = RULES.EXT_COLOR_CODE) "
			+ "AND (   RULES.INT_COLOR_CODE = '*' "
			+ "OR MTOC.INT_COLOR_CODE = RULES.INT_COLOR_CODE) "
			+ "LEFT JOIN GAL261TBX LCRPARTS on RULES.PART_NAME=LCRPARTS.PART_NAME AND RULES.PART_CONFIRM_FLAG = 1 "
			+ "LEFT JOIN GAL185TBX PARTS ON SORTEDLIST.PRODUCT_ID = PARTS.PRODUCT_ID AND RULES.PART_NAME = PARTS.PART_NAME "
			+ "LEFT JOIN (SELECT P.PROCESS_POINT_ID, "
			+ "P.PROCESS_POINT_NAME, "
			+ "(COALESCE(D.SEQUENCE_NUMBER,0)+1)*1000000+(COALESCE(L.LINE_SEQUENCE_NUMBER,0)+1)*1000+(COALESCE(P.SEQUENCE_NUMBER,0)+1) ALL_SEQ_NUM "
			+ "FROM GALADM.GAL214TBX P "
			+ "LEFT JOIN GALADM.GAL195TBX L "
			+ "ON P.LINE_ID = L.LINE_ID "
			+ "LEFT JOIN GALADM.GAL128TBX D "
			+ "ON L.DIVISION_ID=D.DIVISION_ID "
			+ "ORDER BY D.SEQUENCE_NUMBER,L.LINE_SEQUENCE_NUMBER,P.SEQUENCE_NUMBER) RULESPROCESS "
			+ "ON RULESPROCESS.PROCESS_POINT_ID = RULES.PROCESS_POINT_ID "
			+ "WHERE RULESPROCESS.ALL_SEQ_NUM<=PROCESS.ALL_SEQ_NUM "
			+ "GROUP BY LEVEL, FRAME.AF_ON_SEQUENCE_NUMBER, SORTEDLIST.PRODUCT_ID, PRODUCT.PROD_LOT_KD "
			+ "ORDER BY LEVEL DESC " + "WITH CS FOR READ ONLY";

	private final String FIND_FIRST_FIVE_PRODUCTS_BY_SEQ_NUM = "WITH X (PRODUCT_ID, NEXT_PRODUCT_ID, LINE_ID, LINE_PRODUCT_SEQ) AS "
			+ "(SELECT ROOT.PRODUCT_ID,ROOT.NEXT_PRODUCT_ID, ROOT.LINE_ID, 1 AS LINE_PRODUCT_SEQ  FROM GALADM.GAL176TBX ROOT  "
			+ "WHERE ROOT.NEXT_PRODUCT_ID IS NULL  AND ROOT.LINE_ID IN (SELECT L.LINE_ID  FROM galadm.gal214tbx p LEFT JOIN galadm.gal195tbx l "
			+ " ON p.LINE_ID = l.LINE_ID  LEFT JOIN GALADM.GAL128TBX D ON L.DIVISION_ID = D.DIVISION_ID  WHERE D.PLANT_NAME = ?3  "
			+ "AND (coalesce (d.SEQUENCE_NUMBER, 0) + 1) * 1000000  +   (  coalesce (l.LINE_SEQUENCE_NUMBER, 0) + 1) "
			+ " * 1000  + (coalesce (p.SEQUENCE_NUMBER, 0) + 1) < (SELECT     (  coalesce (  d.SEQUENCE_NUMBER, 0) "
			+ " + 1) * 1000000  +   (  coalesce ( l.LINE_SEQUENCE_NUMBER,0) + 1) * 1000 + "
			+ " (  coalesce ( p.SEQUENCE_NUMBER, 0) + 1) ALL_SEQ_NUM FROM galadm.gal214tbx p LEFT JOIN galadm.gal195tbx l "
			+ "ON p.LINE_ID = l.LINE_ID LEFT JOIN GALADM.GAL128TBX D ON L.DIVISION_ID = D.DIVISION_ID "
			+ "WHERE D.PLANT_NAME = ?3 AND P.PROCESS_POINT_ID = ?2)GROUP BY L.LINE_ID) UNION ALL "
			+ "SELECT SUB.PRODUCT_ID,SUB.NEXT_PRODUCT_ID,SUB.LINE_ID,SUPER.LINE_PRODUCT_SEQ + 1 AS LINE_PRODUCT_SEQ FROM GALADM.GAL176TBX SUB, "
			+ " X SUPER WHERE SUB.NEXT_PRODUCT_ID = SUPER.PRODUCT_ID)SELECT DIV.SEQUENCE_NUMBER, "
			+ "DIV.DIVISION_NAME,LINE.LINE_SEQUENCE_NUMBER, LINE.LINE_NAME, X.LINE_ID,X.LINE_PRODUCT_SEQ,X.PRODUCT_ID  FROM X "
			+ "LEFT JOIN GALADM.GAL195TBX LINE ON X.LINE_ID = LINE.LINE_ID  LEFT JOIN GALADM.GAL128TBX DIV ON LINE.DIVISION_ID = DIV.DIVISION_ID  "
			+ "WHERE X.PRODUCT_ID LIKE ?1 ORDER BY DIV.SEQUENCE_NUMBER DESC, LINE.LINE_SEQUENCE_NUMBER DESC, X.LINE_PRODUCT_SEQ "
			+ "DESC FETCH FIRST 5 ROWS ONLY  WITH CS FOR READ ONLY";

	private static final String GET_LOT_SEQ_VIOS_FRAME = "WITH VIN_SEQ (PRODUCT_ID, NEXT_PRODUCT_ID, ID_SEQ, product_spec_code, PRODUCTION_LOT,kd_lot_number, ext_color_code, int_color_code, "
			+ " af_on_sequence_number,LOT_SIZE) AS (SELECT a.PRODUCT_ID, a.NEXT_PRODUCT_ID, 1,  frame.product_spec_code, frame.PRODUCTION_LOT,frame.kd_lot_number, mtoc.ext_color_code, mtoc.int_color_code, "
			+ " frame.af_on_sequence_number AS ID_SEQ,lot.LOT_SIZE FROM GAL176TBX a, gal143tbx frame, gal144tbx mtoc,gal217tbx lot WHERE a.PRODUCT_ID = "
			+ " ?1  and frame.product_id=a.product_id and  frame.product_spec_code=mtoc.product_spec_code and frame.PRODUCTION_LOT=lot.PRODUCTION_LOT "
			+ " UNION ALL SELECT b.PRODUCT_ID, b.NEXT_PRODUCT_ID, N.ID_SEQ + 1 AS ID_SEQ, f.product_spec_code, f.PRODUCTION_LOT,f.kd_lot_number,"
			+ " m.ext_color_code, m.int_color_code, f.af_on_sequence_number,L.LOT_SIZE FROM GAL176TBX b, "
			+ " VIN_SEQ AS N , gal143tbx f, gal144tbx m,gal217tbx L  WHERE b.PRODUCT_ID = N.NEXT_PRODUCT_ID and f.product_id=b.product_id "
			+ " and f.product_spec_code=m.product_spec_code and f.PRODUCTION_LOT=L.PRODUCTION_LOT )  select PRODUCT_ID, product_spec_code, PRODUCTION_LOT,kd_lot_number,"
			+ " ext_color_code, int_color_code, af_on_sequence_number,LOT_SIZE from VIN_SEQ WITH CS FOR READ ONLY ";

	private static final String GET_LOT_SEQ_VIOS_ENGINE = "WITH VIN_SEQ (PRODUCT_ID, NEXT_PRODUCT_ID, ID_SEQ, product_spec_code, PRODUCTION_LOT,kd_lot_number, LOT_SIZE)"
			+ "  AS (SELECT a.PRODUCT_ID, a.NEXT_PRODUCT_ID, 1,  engine.product_spec_code, engine.PRODUCTION_LOT,engine.kd_lot_number,  "
			+ " lot.LOT_SIZE AS ID_SEQ FROM GAL176TBX a, gal131tbx engine, gal217tbx lot WHERE a.PRODUCT_ID = "
			+ " ?1  and engine.product_id=a.product_id and  engine.PRODUCTION_LOT=lot.PRODUCTION_LOT "
			+ " UNION ALL SELECT b.PRODUCT_ID, b.NEXT_PRODUCT_ID, N.ID_SEQ + 1 AS ID_SEQ, f.product_spec_code, f.PRODUCTION_LOT,f.kd_lot_number,"
			+ "  L.LOT_SIZE FROM GAL176TBX b, "
			+ " VIN_SEQ AS N , gal131tbx f, gal217tbx L  WHERE b.PRODUCT_ID = N.NEXT_PRODUCT_ID and f.product_id=b.product_id "
			+ " and f.PRODUCTION_LOT=L.PRODUCTION_LOT )  select PRODUCT_ID, product_spec_code, PRODUCTION_LOT,kd_lot_number,"
			+ " LOT_SIZE from VIN_SEQ WITH CS FOR READ ONLY ";

	private static final String GET_INVENTORY_FOR_LINE = "WITH VIN_SEQ (LINE_ID, PRODUCT_ID, PRODUCT_SPEC_CODE, NEXT_PRODUCT_ID, ID_SEQ) AS  "
			+ "  (SELECT a.LINE_ID, a.PRODUCT_ID, a.PRODUCT_SPEC_CODE, a.NEXT_PRODUCT_ID, 1 AS ID_SEQ FROM GAL176TBX a WHERE a.LINE_ID = ? AND a.NEXT_PRODUCT_ID IS NULL  "
			+ "  UNION ALL  "
			+ "  SELECT b.LINE_ID, b.PRODUCT_ID, b.PRODUCT_SPEC_CODE, b.NEXT_PRODUCT_ID, N.ID_SEQ + 1 AS ID_SEQ FROM GAL176TBX b, VIN_SEQ AS N WHERE b.LINE_ID = ? AND b.NEXT_PRODUCT_ID = N.PRODUCT_ID)  "
			+ "  SELECT INPP.LINE_ID, INPP.PRODUCT_ID, INPP.PRODUCT_SPEC_CODE, INPP.NEXT_PRODUCT_ID, INPP.ID_SEQ FROM GALADM.GAL117TBX SITE "
			+ " 	INNER JOIN GALADM.GAL128TBX DIV ON SITE.SITE_NAME = DIV.SITE_NAME "
			+ " 	INNER JOIN GALADM.GAL195TBX LINE ON DIV.DIVISION_ID = LINE.DIVISION_ID "
			+ " 	INNER JOIN VIN_SEQ INPP ON INPP.LINE_ID=LINE.LINE_ID "
			+ " WHERE DIV.PLANT_NAME = ? AND DIV.DIVISION_ID = ? AND LINE.LINE_ID = ? "
			+ " ORDER BY ID_SEQ DESC WITH CS FOR READ ONLY";
	
	private static final String GET_NEXT_PRODUCT_SEQUENCE_BY_PLANT="WITH X (PRODUCT_ID, NEXT_PRODUCT_ID, LINE_ID, LINE_PRODUCT_SEQ)"
			+ " AS (SELECT ROOT.PRODUCT_ID,ROOT.NEXT_PRODUCT_ID,ROOT.LINE_ID,1 AS LINE_PRODUCT_SEQ  FROM GALADM.GAL176TBX ROOT"
			+ " WHERE ROOT.NEXT_PRODUCT_ID IS NULL AND ROOT.LINE_ID IN (SELECT L.LINE_ID FROM galadm.gal214tbx p LEFT JOIN galadm.gal195tbx l ON p.LINE_ID = l.LINE_ID"
			+ " LEFT JOIN GALADM.GAL128TBX D ON L.DIVISION_ID =D.DIVISION_ID"
			+ " WHERE D.PLANT_NAME = ?2 AND  (coalesce (d.SEQUENCE_NUMBER, 0) + 1) * 1000000 +   (  coalesce (l.LINE_SEQUENCE_NUMBER, 0) + 1) * 1000"
			+ " + (coalesce (p.SEQUENCE_NUMBER, 0) + 1) < (SELECT     (coalesce ( d.SEQUENCE_NUMBER,0) + 1) * 1000000"
			+ " + (coalesce (l.LINE_SEQUENCE_NUMBER, 0) + 1) * 1000 + (coalesce (p.SEQUENCE_NUMBER,0) + 1) ALL_SEQ_NUM"
			+ " FROM galadm.gal214tbx p LEFT JOIN galadm.gal195tbx l ON p.LINE_ID = l.LINE_ID  LEFT JOIN GALADM.GAL128TBX D ON L.DIVISION_ID =D.DIVISION_ID"
			+ " WHERE D.PLANT_NAME = ?2 AND P.PROCESS_POINT_ID = ?1 ) GROUP BY L.LINE_ID)"
			+ " UNION ALL"
			+ " SELECT SUB.PRODUCT_ID, SUB.NEXT_PRODUCT_ID, SUB.LINE_ID,  SUPER.LINE_PRODUCT_SEQ + 1 AS LINE_PRODUCT_SEQ"
			+ " FROM GALADM.GAL176TBX SUB, X SUPER WHERE SUB.NEXT_PRODUCT_ID = SUPER.PRODUCT_ID)"
			+ " SELECT DIV.SEQUENCE_NUMBER,DIV.DIVISION_NAME, LINE.LINE_SEQUENCE_NUMBER,LINE.LINE_NAME,X.LINE_ID, X.LINE_PRODUCT_SEQ,X.PRODUCT_ID"
			+ " FROM X LEFT JOIN GAL195TBX LINE ON X.LINE_ID = LINE.LINE_ID"
			+ " LEFT JOIN GALADM.GAL128TBX DIV ON LINE.DIVISION_ID = DIV.DIVISION_ID"
			+ " ORDER BY DIV.SEQUENCE_NUMBER DESC,  LINE.LINE_SEQUENCE_NUMBER DESC, X.LINE_PRODUCT_SEQ DESC fetch first 1 row only WITH CS FOR READ ONLY";
	
	private static final String GET_INVENTORY_FOR_PROCESS_POINT_IDS = "select inventory.PRODUCT_ID, resultsA.ACTUAL_TIMESTAMP, resultsA.PRODUCT_SPEC_CODE from GALADM.GAL176TBX inventory"
			+ " join GALADM.GAL214TBX ppid on ppid.LINE_ID = inventory.LINE_ID"
			+ " join GALADM.GAL215TBX resultsA on resultsA.PRODUCT_ID = inventory.PRODUCT_ID"
			+ " where ppid.PROCESS_POINT_ID in (@PPIDS@)"
			+ " and resultsA.PROCESS_POINT_ID = ppid.PROCESS_POINT_ID"
			+ " and resultsA.ACTUAL_TIMESTAMP ="
			+ " (select max(ACTUAL_TIMESTAMP) from GALADM.GAL215TBX resultsB"
			+ " where resultsB.PRODUCT_ID = resultsA.PRODUCT_ID)"
			+ " order by resultsA.ACTUAL_TIMESTAMP desc";
	
	private static final String GET_CURE_TIMER_INVENTORY = "select distinct inventory.PRODUCT_ID, max(parts.ACTUAL_TIMESTAMP) as ACTUAL_TIMESTAMP, inventory.PRODUCT_SPEC_CODE from GALADM.GAL176TBX inventory"
			+ " join GALADM.GAL185TBX parts on inventory.PRODUCT_ID = parts.PRODUCT_ID"
			+ " join GALADM.GAL214TBX ppid on ppid.LINE_ID = inventory.LINE_ID"
			+ " join GALADM.GAL215TBX results on results.PRODUCT_ID = inventory.PRODUCT_ID"
			+ " where inventory.LINE_ID in (@LINES@)"
			+ " and ("
			+ "@LIKE@"
			+ ")"
			+ "@EXISTS@"
			+ " and not exists (select 1 from GALADM.GAL215TBX resultsB where resultsB.PRODUCT_ID = inventory.PRODUCT_ID and resultsB.PROCESS_POINT_ID in (@PPIDS@))"
			+ " and results.PROCESS_POINT_ID = ppid.PROCESS_POINT_ID"
			+ " group by inventory.PRODUCT_ID, inventory.PRODUCT_SPEC_CODE";
	
	private static final String GET_FRAME_SEQ_LIST_BY_DIV_ID_LINE_ID= " with x (product_id,next_product_id, line_id,line_name,AF_ON_SEQUENCE_NUMBER,ID_SEQ ) as "    
			+ " (select root.product_id,root.next_product_id, root.line_id,b.line_name,f.AF_ON_SEQUENCE_NUMBER, 1 AS ID_SEQ "
			+ " from gal176tbx root, gal195tbx b ,gal143tbx f "
			+ " where root.next_product_id is null  and  root.product_id=f.product_id  and root.LINE_ID = b.line_id  and b.DIVISION_ID = ? and b.line_id= ? "
			+ " union all   "
			+ " select sub.product_id,sub.next_product_id, sub.line_id ,super.line_name, g.AF_ON_SEQUENCE_NUMBER,super.ID_SEQ + 1 AS ID_SEQ "
			+ " from gal176tbx sub, x super , gal143tbx g  where sub.next_product_id =  super.product_id  and  sub.product_id =  g.product_id  ) " 
			+ " select x.product_id,x.next_product_id,x.line_id,y.line_name,AF_ON_SEQUENCE_NUMBER,x.ID_SEQ from x,gal195tbx y where  x.line_id = y.line_id order by x.ID_SEQ desc  with cs for read only ";
	
	private static final String GET_NON_FRAME_SEQ_LIST_BY_DIV_ID_LINE_ID= " with x (product_id,next_product_id, line_id,line_name,ID_SEQ ) as "    
			+ " (select root.product_id,root.next_product_id, root.line_id,b.line_name, 1 AS ID_SEQ "
			+ " from gal176tbx root, gal195tbx b  "
			+ " where root.next_product_id is null  and root.LINE_ID = b.line_id  and b.DIVISION_ID = ? and b.line_id= ? "
			+ " union all   "
			+ " select sub.product_id,sub.next_product_id, sub.line_id ,super.line_name, super.ID_SEQ + 1 AS ID_SEQ "
			+ " from gal176tbx sub, x super   where sub.next_product_id =  super.product_id  ) " 
			+ " select x.product_id,x.next_product_id,x.line_id,y.line_name,x.ID_SEQ from x,gal195tbx y where  x.line_id = y.line_id order by x.ID_SEQ desc  with cs for read only ";
	
	private static final String FIND_UPCOMING_PRODUCTS = "WITH SortedList(PRODUCT_ID, NEXT_PRODUCT_ID, Level)"
			+ " AS (SELECT a.PRODUCT_ID, a.NEXT_PRODUCT_ID, 1 AS Level"
			+ " FROM GAL176TBX a"
			+ " WHERE a.PRODUCT_ID= ?1 "
			+ " UNION ALL"
			+ " SELECT b.PRODUCT_ID, b.NEXT_PRODUCT_ID, N.Level + 1 AS Level"
			+ " FROM GAL176TBX b, SortedList AS n"
			+ " WHERE n.NEXT_PRODUCT_ID = b.PRODUCT_ID )"
			+ " select PRODUCT_ID from SortedList order by Level asc with ur for read only";
	
	private static final String FIND_FIRST_FOR_LINE = "SELECT * FROM GALADM.GAL176TBX a "  
	        + "WHERE LINE_ID = ?1 AND "
	        + "NOT EXISTS(SELECT PRODUCT_ID FROM GALADM.GAL176TBX WHERE NEXT_PRODUCT_ID = a.PRODUCT_ID)";
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public InProcessProduct findByKey(String id) {
		return super.findByKey(id);
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public InProcessProduct findLastForLine(String lineId) {
		Parameters parameters = Parameters.with("lineId", lineId);
		return findFirstByQuery(FIND_LAST_FOR_LINE, parameters);
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public InProcessProduct findLastForLine1(String lineId) {
		Parameters parameters = Parameters.with("1", lineId);
		return findFirstByNativeQuery(FIND_LAST_FOR_LINE1, parameters);
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public boolean isProductIdAheadOfExpectedProductId(String expectedProductId, String productId) {
		Parameters parameters = new Parameters();
		parameters.put("1", expectedProductId);
		parameters.put("2", productId);
		return findFirstByNativeQuery(LOOK_AHEAD_FOR_PRODUCT_ID, parameters, String.class) != null;
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public List<InProcessProduct> findByNextProductId(String productId) {
		Parameters parameters = Parameters.with("nextProductId", productId);
		return findAll(parameters);
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public List<InProcessProduct> findSequenceListByDivision(String divisionId) {
		Parameters parameters = new Parameters();
		parameters.put("1", divisionId);
		return findAllByNativeQuery(FIND_SEQUENCE_LIST, parameters);
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public List<String> getVinSequence(String startingVin, int howMany) {
		Parameters parameters = Parameters.with("1", startingVin.trim());
		return findAllByNativeQuery(GET_VIN_SEQ, parameters, String.class, howMany);
	}

	@Transactional
	public int deleteProdIds(List<String> prodIds) {
		int count = 0;
		for (String prodId : prodIds) {
			count = count + delete(Parameters.with("productId", prodId));
		}
		return count;
	}

	/**
	 * Current line (if different from target line) = X
	 * Target line 									= Y
	 * Current product								= n
	 * Last product in target line 					= m
	 * ------------------------------------------------
	 *  				Product in line X
	 *  -----------------------------------------------		  
	 * 			Before						After
	 *  -----------------------------------------------
	 * 		X.n-1 --> X.n				X.n-1 --> X.n+1
	 * 		X.n   --> X.n+1	   
	 * 									Y.m   --> Y.n
	 * 		Y.m   --> null				Y.n	  --> null
	 *  
	 *  -----------------------------------------------
	 *  				Product in line Y
	 *  -----------------------------------------------
	 *  	Y.n-1 --> Y.n				Y.n-1 --> Y.n+1
	 *  	Y.n   --> Y.n+1
	 *  								Y.m   --> Y.n
	 *  	Y.m   --> null				Y.n	  --> null
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void addToLine(BaseProduct product, ProcessPoint processPoint) {
		String processPointId = StringUtils.trimToEmpty(processPoint.getProcessPointId());
		List<InProcessProduct> recordsToUpdate = null;
		InProcessProduct newInPP = null;
		String nPlus1Id = null;
		InProcessProduct nMinus1 = null;
		boolean nFound = false;

		Parameters params = Parameters.with("1", processPoint.getLineId());
		List<InProcessProduct> newLineTail = findAllByNativeQuery(ACQUIRE_TARGET_LINE_LOCK, params, InProcessProduct.class);

		recordsToUpdate = findItemsToResequence(product.getProductId());
		for (InProcessProduct inPP : recordsToUpdate) {
			if (inPP.getProductId().equals(product.getProductId())) {
				nPlus1Id = StringUtils.trimToNull(inPP.getNextProductId()); 			// retrieved n, remember n+1
				addProductToTailOfNewLine(product, newLineTail, processPoint, inPP); 	// Y.n --> null
				nFound = true;
			} else if (StringUtils.trimToEmpty(inPP.getNextProductId()).equals(product.getProductId())) {
				nMinus1 = inPP; 														// retrieved n-1, remember n-1
			}
		}

		if (nMinus1 != null) {
			nMinus1.setNextProductId(nPlus1Id); 										// link n-1 --> n+1
			getLogger(processPointId).info("Linking " + nMinus1.getProductId() + " --> " + nPlus1Id);
		}

		if (!nFound) {
			newInPP = new InProcessProduct(product); 									// create new n, Y.n --> null
			if (newInPP != null) {
				addProductToTailOfNewLine(product, newLineTail, processPoint, newInPP);
				save(newInPP);
			}
		}

		if (recordsToUpdate != null) {
			saveAll(recordsToUpdate);
		}
		if (newLineTail != null) {
			saveAll(newLineTail);
		}

		getLogger(processPointId).info(this.getClass().getSimpleName() 
						+ ": Added product " + StringUtils.trimToEmpty(product.getProductId())
						+ " to line " + StringUtils.trimToEmpty(processPoint.getLineId())
						+ " at process point " + processPointId);
	}

	private void addProductToTailOfNewLine(BaseProduct product,	List<InProcessProduct> newLineTail, ProcessPoint processPoint, InProcessProduct inPP) {
		String processPointId = StringUtils.trimToEmpty(processPoint.getProcessPointId());
		for (InProcessProduct tail : newLineTail) { 												// should only be one record
			if (!product.getProductId().equals(StringUtils.trimToEmpty(tail.getProductId()))) { 	// avoid linking Y.m --> Y.m
				tail.setNextProductId(product.getProductId()); 										// retrieved Y.m, link Y.m --> Y.n
				getLogger(processPointId).info("Linking " + tail.getProductId() + " --> " + product.getProductId());
			}
		}
		inPP.setLastPassingProcessPointId(processPoint.getProcessPointId());
		inPP.setLineId(processPoint.getLineId());
		inPP.setNextProductId(null);
		getLogger(processPointId).info("New tail for line " + processPoint.getLineId() + ": " + inPP.getProductId() + " --> null");
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void productFactoryExit(BaseProduct product, ProcessPoint processPoint) {
		String nPlus1Id = null;
		InProcessProduct nMinus1 = null;
		InProcessProduct n = null;

		getLogger(processPoint.getProcessPointId()).info("Executing productFactoryExit for product " + product.getProductId());
		for (InProcessProduct inPP : findItemsToResequence(product.getProductId())) {
			if (inPP.getProductId().equals(product.getProductId())) {			
				n = inPP;														// retrieved n, remember n
				nPlus1Id = StringUtils.trimToNull(inPP.getNextProductId()); 	// remember n+1
			} else if (StringUtils.trimToEmpty(inPP.getNextProductId()).equals(product.getProductId())) {
				nMinus1 = inPP; 												// retrieved n-1, remember n-1
			}
		}
		
		if (nMinus1 != null) {
			nMinus1.setNextProductId(nPlus1Id); 								// link n-1 --> n+1
			getLogger(processPoint.getProcessPointId()).info("Linking " + nMinus1.getProductId() + " --> " + nPlus1Id);
			save(nMinus1);
		}	
		if (n != null) {
			getLogger(processPoint.getProcessPointId()).info("Deleting " + n.getProductId());
			remove(n);															// delete n
		}
	}
	
	@Transactional
	public int deleteProdId(String prodId) {
		return delete(Parameters.with("productId", prodId));
	}

	public String findFirstUnconfirmed(String lineId, String testPP, String confirmPP) {
		Parameters parameters = Parameters.with("1", lineId.trim());
		parameters.put("2", testPP.trim());
		parameters.put("3", confirmPP.trim());
		return findFirstByNativeQuery(FIND_FIRST_UNCONFIRMED_TEST, parameters, String.class);
	}

	@Transactional
	public List<InProcessProduct> findItemsToResequence(String productId) {
		Parameters params = Parameters.with("1", productId);
		params.put("2", productId);

		return findAllByNativeQuery(FIND_INPROCESS_PRODUCTS_TO_RESEQUENCE, params, InProcessProduct.class);
	}

	/**
	 * Return the InProcessProduct after obtaining a row update lock
	 */
	public InProcessProduct findForUpdate(String productId) {
		Parameters params = Parameters.with("1", productId);
		return findFirstByNativeQuery(FIND_INPROCESS_PRODUCTS_FOR_UPDATE, params);
	}

	public String getProductSpecCode(String productId) {
		return findByKey(productId).getProductSpecCode();
	}

	public LineOverview getLineOverviewData(String lineId) {

		LineOverview lineOverview = new LineOverview(lineId);
		try {
			List<Object[]> lineData = getLineData(lineId);

			for (Object[] result : lineData) {
				LineDetail detail = new LineDetail();
				detail.setSequenceNumber(getInt(result[0]));
				detail.setAfOnSequenceNumber(getInt(result[1]));
				detail.setNextAfOnSequenceNumber(getInt(result[2]));
				detail.setProductId(getString(result[3]));
				detail.setProductSpecCode(getString(result[4]));
				detail.setProductionLotKd(getString(result[5]));
				detail.setNextProductionLotKd(getString(result[6]));
				detail.setNonRepairedDefects(getInt(result[7]));
				detail.setMissingInstalledParts(getInt(result[8]));
				detail.setBadInstalledParts(getInt(result[9]));
				detail.setProcessPointName(getString(result[10]));
				detail.setProcessSequence(getInt(result[11]));
				detail.setProcessPointId(getString(result[12]));
				lineOverview.getLineDetails().add(detail);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return lineOverview;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	private List<Object[]> getLineData(String lineId) {

		getLogger().info("About to execute getLineData() query");
		Parameters parameters = Parameters.with("1", lineId.trim());
		List<Object[]> lineData = findAllByNativeQuery(GET_LINE_OVERVIEW, parameters, Object[].class);
		getLogger().info("Finished executing getLineDataProgress() query");

		return lineData;
	}

	private void refresh(String lineId) {
		refresh(DEFAULT_STALE_TOLERANCE, lineId);
	}

	private synchronized void refresh(int staleTolerance, String lineId) {
		try {
			if ((getLineOverviewCurrent(lineId).getLastUpdated().getTime() + staleTolerance) <= new Date().getTime()) {
				setLineOverviewCurrent(getLineOverviewData(lineId), lineId);
				getLogger().debug("Updated LineOverview");
			} else {
				getLogger().debug("Skipping LineOverview update. Last Updated at: "	
							+ getLineOverviewCurrent(lineId).getLastUpdated().toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to refresh LineOverview data: " + (ex.getMessage() == null ? "" : ex.getMessage()));
		}
	}

	private LineOverview getLineOverviewCurrent(String lineId) {
		if (lineMap.get(lineId) != null)
			return lineMap.get(lineId);
		else
			return new LineOverview(lineId);
	}

	private void setLineOverviewCurrent(LineOverview lineOverviewCurrent, String lineId) {
		lineOverviewCurrent.setLastUpdated(new Date());
		lineMap.put(lineId, lineOverviewCurrent);
	}

	public ArrayList<LineDetail> getLineDetail(String lineId) {
		refresh(lineId);
		return getLineOverviewCurrent(lineId).getLineDetails();
	}

	public List<Object[]> findFirstFiveProducts(String SN, String processPointId, String plantName) {
		Parameters params = Parameters.with("1", "%" + SN.trim());
		params.put("2", processPointId);
		params.put("3", plantName);
		List<Object[]> data = executeNative(params,	FIND_FIRST_FIVE_PRODUCTS_BY_SEQ_NUM);
		getLogger().info("Finished executing query ");
		return data;
	}
	
	public List<InProcessProduct> findHeadProducts(String lineId) {
		Parameters parameters = Parameters.with("1", lineId);
		return findAllByNativeQuery(FIND_FIRST_FOR_LINE, parameters);
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public List<Map<String, String>> getVinSequenceVIOS(String startingVin,	int howMany, String productType) {

		String sql = "";
		if (productType.equals("FRAME")) {
			sql = GET_LOT_SEQ_VIOS_FRAME;
		} else if (productType.equals("ENGINE")) {
			sql = GET_LOT_SEQ_VIOS_ENGINE;
		}
		Parameters parameters = Parameters.with("1", startingVin.trim());
		List<InProcessProduct> list = findAllByNativeQuery(sql, parameters, howMany);

		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		String prevMTOC = null, color = "black";
		int counter = 0;
		for (InProcessProduct product : list) {
			counter++;
			if (counter == 1) {
				color = "yellow";
			}
			if (counter > 1) {
				color = product.getProductSpecCode().equals(prevMTOC) ? "black"	: "green";
			}
			if (counter > 0) {
				prevMTOC = product.getProductSpecCode();
			}
			Map<String, String> aProduct = new java.util.HashMap<String, String>();
			aProduct.put("productid", product.getProductId());
			aProduct.put("mto", product.getProductSpecCode());
			aProduct.put("color", color);

			returnList.add(aProduct);
		}
		return returnList;
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public List<Map<String, String>> getLotSequenceVIOS(String startingVin, String productType) {

		int LOOK_AHEAD_COUNT = 60, counter = 0, lotCount = 0;
		boolean lotChanged = false;
		String sql = "";
		
		if (productType.equals("FRAME")) {
			sql = GET_LOT_SEQ_VIOS_FRAME;
		} else if (productType.equals("ENGINE")) {
			sql = GET_LOT_SEQ_VIOS_ENGINE;
		} else {
			return new ArrayList<Map<String, String>>();
		}
		
		String prevProductLOT = null, prevKDLOT = null, prevBOS = null, prevINTCOLOR = null, prevEXTCOLOR = null, prevAWAY = null, prevLOTQTY = null, prevMTO = null, prevPRODUCTID = null;
		Parameters parameters = Parameters.with("1", startingVin.trim());
		List<Object[]> list = findAllByNativeQuery(sql, parameters, Object[].class, LOOK_AHEAD_COUNT);
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

		for (Object[] product : list) {
			counter++;

			if (counter > 1) {
				lotChanged = ((String) product[2]).equals(prevProductLOT) ? false : true;
			}
			if (counter > 0) {
				prevPRODUCTID = (String) product[0];
				prevMTO = product[1].toString();
				prevProductLOT = (String) product[2];
				prevKDLOT = (String) product[3];
				prevAWAY = String.valueOf(counter);

				if (productType.equals("FRAME")) {
					prevEXTCOLOR = (String) product[4];
					prevINTCOLOR = (String) product[5];
					prevBOS = product[6].toString();
					prevLOTQTY = product[7].toString();
				}
				if (productType.equals("ENGINE")) {
					prevLOTQTY = product[4].toString();
				}
			}

			if (lotChanged || counter == 1) {
				lotCount++;
				Map<String, String> aRecord = new java.util.HashMap<String, String>();
				aRecord.put("away", prevAWAY);
				aRecord.put("bos", prevBOS);
				aRecord.put("kdlot", prevKDLOT);
				aRecord.put("lotqty", prevLOTQTY);
				aRecord.put("mto", prevMTO);
				aRecord.put("extcolor", prevEXTCOLOR);
				aRecord.put("intcolor", prevINTCOLOR);
				aRecord.put("color", "black");
				aRecord.put("productid", prevPRODUCTID);

				returnList.add(aRecord);
			}
			if (lotCount > 2)
				break;
		}
		return returnList;
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.dao.product.InProcessProductDao#getInventoryForLine(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<InProcessProduct> getInventoryForLine(String plantName,
			String divisionId, String lineId) {
		Parameters parameters = Parameters.with("1", lineId)
				.put("2", lineId)
				.put("3", plantName)
				.put("4", divisionId)
				.put("5", lineId);
		return findAllByNativeQuery(GET_INVENTORY_FOR_LINE,parameters, InProcessProduct.class);
	}
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public InProcessProduct findNextProductSeqByPlantName(String processPointId,String plantName){
		{
			Parameters parameters = new Parameters();
			parameters.put("1", processPointId);
			parameters.put("2", plantName);
			return findFirstByNativeQuery(GET_NEXT_PRODUCT_SEQUENCE_BY_PLANT, parameters);
		}
	}
	
	public List<Object[]> getInventoryForProcessPointIds(List<String> processPointIds) {
		if (processPointIds == null) return null;
		return executeNative(null, GET_INVENTORY_FOR_PROCESS_POINT_IDS.replace("@PPIDS@", StringUtil.toSqlInString(processPointIds)));
	}
	
	public List<Object[]> getCureTimerInventory(List<String> triggerLineIds, List<String> triggerOnParts, List<String> triggerOffProcessPointIds) {
		if (triggerLineIds == null || triggerOnParts == null || triggerOffProcessPointIds == null) return null;
		String lines = StringUtil.toSqlInString(triggerLineIds);
		String ppids = StringUtil.toSqlInString(triggerOffProcessPointIds);
		String like = null;
		String exists = null;
		{
			StringBuilder likeBuilder = new StringBuilder();
			StringBuilder existsBuilder = new StringBuilder();
			for (int i = 0; i < triggerOnParts.size(); i++) {
				String part = triggerOnParts.get(i);
				if (i > 0) likeBuilder.append(" or ");
				likeBuilder.append("parts.PART_NAME like \'");
				likeBuilder.append(part);
				likeBuilder.append("%\'");

				existsBuilder.append(" and exists (select 1 from GALADM.GAL185TBX parts");
				existsBuilder.append(i);
				existsBuilder.append(" where parts");
				existsBuilder.append(i);
				existsBuilder.append(".PRODUCT_ID = inventory.PRODUCT_ID and parts");
				existsBuilder.append(i);
				existsBuilder.append(".PART_NAME like \'");
				existsBuilder.append(part);
				existsBuilder.append("%\')");
			}

			like = likeBuilder.toString();
			exists = existsBuilder.toString();
		}
		return executeNative(null, GET_CURE_TIMER_INVENTORY.replace("@LINES@", lines).replace("@LIKE@", like).replace("@EXISTS@", exists).replace("@PPIDS@", ppids));
	}
	
	public List<InProcessProductMaintenanceDTO> findSeqListByDivIdLineId(String divisionId, String lineId,boolean isFrame) {
		Parameters parameters = Parameters.with("1", divisionId.trim());
		parameters.put("2", lineId.trim());
		if(isFrame)
			return findAllByNativeQuery(GET_FRAME_SEQ_LIST_BY_DIV_ID_LINE_ID, parameters, InProcessProductMaintenanceDTO.class);
		else    			
			return findAllByNativeQuery(GET_NON_FRAME_SEQ_LIST_BY_DIV_ID_LINE_ID, parameters, InProcessProductMaintenanceDTO.class);
	}
	
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRES_NEW)
	public int updateLastPassingProcessPoint(String productId, String lastPassingProcessPointId) {
		return update(Parameters.with("lastPassingProcessPointId", lastPassingProcessPointId),
					Parameters.with("productId", productId));
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public List<String> findIncomingExpectedProductIds(String productId) {
		Parameters parameters = Parameters.with("1", productId);
		return findAllByNativeQuery(FIND_UPCOMING_PRODUCTS, parameters, String.class);
	}
}
