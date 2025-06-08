package com.honda.galc.dao.jpa.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.dto.ProductionLotBackout;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.StringUtil;

/**
 * 
 * <h3>ProductionLotDaoImpl Class description</h3>
 * <p> ProductionLotDaoImpl description </p>
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
 * Apr 27, 2011
 */

@Deprecated
public class ProductionLotDaoImpl extends BaseDaoImpl<ProductionLot,String> implements ProductionLotDao{
	private final Logger logger = Logger.getLogger("ProductionLotDaoImpl");

	private static final String UPDATE_LOT_STATUS = "UPDATE galadm.gal217tbx set lot_status = ?2 where production_lot = ?1";
	private static final String UPDATE_START_PRODUCT_ID = "UPDATE galadm.gal217tbx set START_PRODUCT_ID = ?2 where production_lot = ?1";
	private static final String GET_LOT_SIZE = "SELECT SUM(LOT_SIZE) FROM gal217tbx "
			+ "WHERE kd_lot_number LIKE ?1 AND PROCESS_LOCATION = ?2 FOR READ ONLY";

	private static final String SELECT_AE_1_PRODUCTION_PROGRESS = "SELECT GAL217TBX.Production_Lot, GAL217TBX.Lot_Size, GAL217TBX.Plan_Code, GAL217TBX.Line_No, GAL217TBX.Process_Location, GAL217TBX.Product_Spec_Code, " +
			"(SELECT COUNT(Production_lot) from gal215tbx where Process_Point_ID in (@ON_PROCESSPOINTS@) and gal215tbx.production_lot = gal217tbx.production_lot ) as production_on_count, " +
			"(SELECT COUNT(Production_lot) from gal215tbx where  Process_Point_ID in (@OF_PROCESSPOINTS@) and gal215tbx.production_lot = gal217tbx.production_lot ) as production_off_count, " ;
			
	private static final String SELECT_AE_2_PRODUCTION_PROGRESS ="(SELECT COUNT(*) from GAL215TBX a, GAL131TBX b Where a.Product_id = b.Product_id and a.Process_Point_ID in (@ON_PROCESSPOINTS@) and b.Tracking_Status in (@TRACKING_STATUSES@)";
	private static final String SELECT_AE_3_PRODUCTION_PROGRESS = " and a.production_lot = b.Production_lot and a.production_lot = gal217tbx.production_lot) as exception_on_count, " +
			"(SELECT COUNT(*) from GAL215TBX a, GAL131TBX b Where a.Product_id = b.Product_id and a.Process_Point_ID in (@OF_PROCESSPOINTS@) and b.Tracking_Status in (@TRACKING_STATUSES@)";
	private static final String SELECT_AE_4_PRODUCTION_PROGRESS = " and a.production_lot = b.Production_lot and a.production_lot = gal217tbx.production_lot) as exception_off_count, " +
			"gal212tbx.hold_status, gal217tbx.lot_number, gal217tbx.kd_lot_number, gal217tbx.LOT_STATUS, gal212tbx.sequence " +
			"FROM GAL217TBX, GAL212TBX WHERE (GAL217TBX.Lot_Status != 4 and GAL212TBX.Process_Location = 'AF' AND ((GAL212TBX.HOLD_STATUS = 1) OR (GAL212TBX.HOLD_STATUS = 0)) AND GAL217TBX.Lot_Number = GAL212TBX.Lot_Number AND GAL217TBX.Process_Location='AE' ) "+
			" OR (GAL217TBX.Lot_Status != 4 and GAL212TBX.process_location = 'AE' AND GAL217TBX.process_location = 'AE' AND ((GAL212TBX.HOLD_STATUS = 1) OR (GAL212TBX.HOLD_STATUS = 0)) AND GAL217TBX.Lot_Number = GAL212TBX.Lot_Number AND GAL212TBX.lot_number not in "+
			"(SELECT GAL212TBX.lot_number FROM gal212tbx WHERE GAL212TBX.process_location = 'AF') ) for read only";
	private static final String SELECT_AE_4_PRODUCTION_PROGRESS_PLANTS_TO_EXCLUDE_1 = " and a.production_lot = b.Production_lot and a.production_lot = gal217tbx.production_lot) as exception_off_count, " +
			"gal212tbx.hold_status, gal217tbx.lot_number, gal217tbx.kd_lot_number, gal217tbx.LOT_STATUS, gal212tbx.sequence " +
			"FROM GAL217TBX, GAL212TBX WHERE (GAL217TBX.Lot_Status != 4 and GAL212TBX.Process_Location = 'AF' AND ((GAL212TBX.HOLD_STATUS = 1) OR (GAL212TBX.HOLD_STATUS = 0)) AND "+
			"GAL217TBX.Lot_Number = GAL212TBX.Lot_Number AND GAL217TBX.Process_Location='AE' ) OR (GAL217TBX.Lot_Status != 4 and GAL212TBX.process_location = 'AE' AND GAL217TBX.process_location = 'AE' "+
			"AND ((GAL212TBX.HOLD_STATUS = 1) OR (GAL212TBX.HOLD_STATUS = 0)) AND GAL217TBX.Lot_Number = GAL212TBX.Lot_Number AND GAL212TBX.lot_number not in "+
			"(SELECT GAL212TBX.lot_number FROM gal212tbx WHERE GAL212TBX.process_location = 'AF') ) AND GAL217TBX.PLAN_CODE not in (@PLAN_CODES@) for read only";

	private static final String SELECT_AF_PRODUCTION_PROGRESS = "SELECT GAL217TBX.Production_Lot, GAL217TBX.Lot_Size, GAL217TBX.Plan_Code, GAL217TBX.Line_No, GAL217TBX.Process_Location, GAL217TBX.Product_Spec_Code, " +
			"(SELECT COUNT(Production_lot) from gal215tbx where Process_Point_ID in (@ON_PROCESSPOINTS@) and gal215tbx.production_lot = gal217tbx.production_lot ) as production_on_count, " +
			"(SELECT COUNT(Production_lot) from gal215tbx where  Process_Point_ID in (@OF_PROCESSPOINTS@) and gal215tbx.production_lot = gal217tbx.production_lot ) as production_off_count, " +
			"(SELECT COUNT(*) from GAL143TBX Where Tracking_Status in (@TRACKING_STATUSES@) and GAL143TBX.production_lot = gal217tbx.production_lot "+
			" AND NOT EXISTS (SELECT * FROM GAL215TBX G215A WHERE G215A.PRODUCT_ID = GAL143TBX.PRODUCT_ID AND G215A.PROCESS_POINT_ID IN (@ON_PROCESSPOINTS@))) AS EXCEPTION_ON_COUNT, "+
			"(SELECT COUNT(*) from GAL143TBX Where Tracking_Status in (@TRACKING_STATUSES@) and GAL143TBX.production_lot = gal217tbx.production_lot "+
			" AND NOT EXISTS (SELECT * FROM GAL215TBX G215A WHERE G215A.PRODUCT_ID = GAL143TBX.PRODUCT_ID AND G215A.PROCESS_POINT_ID IN (@OF_PROCESSPOINTS@))) AS EXCEPTION_OFF_COUNT, "+
			"gal212tbx.hold_status, gal217tbx.lot_number, gal217tbx.kd_lot_number, gal217tbx.LOT_STATUS, gal212tbx.sequence " +
			"FROM GAL217TBX,GAL212TBX WHERE GAL217TBX.Process_Location ='AF' AND GAL217TBX.Production_Lot = GAL212TBX.Production_Lot "+
			"AND ((GAL212TBX.HOLD_STATUS = 1) OR (GAL212TBX.HOLD_STATUS = 0)) AND GAL217TBX.Lot_Status != 3 and GAL217TBX.Lot_Status !=4 for read only";
	
	private static final String SELECT_PA_PRODUCTION_PROGRESS = "SELECT GAL217TBX.Production_Lot, GAL217TBX.Lot_Size, GAL217TBX.Plan_Code, GAL217TBX.PA_Line_No, GAL217TBX.PA_Process_Location, GAL217TBX.Product_Spec_Code, " +
			"(SELECT COUNT(Production_lot) from gal215tbx where Process_Point_ID in (@ON_PROCESSPOINTS@) and gal215tbx.production_lot = gal217tbx.production_lot ) as production_on_count, " +
			"(SELECT COUNT(Production_lot) from gal215tbx where  Process_Point_ID in (@OF_PROCESSPOINTS@) and gal215tbx.production_lot = gal217tbx.production_lot ) as production_off_count, " +
			"(SELECT COUNT(*) from GAL143TBX Where Tracking_Status in (@TRACKING_STATUSES@) and GAL143TBX.production_lot = gal217tbx.production_lot "+
			" AND NOT EXISTS (SELECT * FROM GAL215TBX G215A WHERE G215A.PRODUCT_ID = GAL143TBX.PRODUCT_ID AND G215A.PROCESS_POINT_ID IN (@ON_PROCESSPOINTS@))) AS EXCEPTION_ON_COUNT, "+
			"(SELECT COUNT(*) from GAL143TBX Where Tracking_Status in (@TRACKING_STATUSES@) and GAL143TBX.production_lot = gal217tbx.production_lot "+
			" AND NOT EXISTS (SELECT * FROM GAL215TBX G215A WHERE G215A.PRODUCT_ID = GAL143TBX.PRODUCT_ID AND G215A.PROCESS_POINT_ID IN (@OF_PROCESSPOINTS@))) AS EXCEPTION_OFF_COUNT, "+
			"gal212tbx.hold_status, gal217tbx.lot_number, gal217tbx.kd_lot_number, gal217tbx.LOT_STATUS, gal212tbx.sequence " +
			"FROM GAL217TBX,GAL212TBX WHERE GAL217TBX.PA_Process_Location ='PA' AND GAL217TBX.Production_Lot = GAL212TBX.Production_Lot "+
			"AND ((GAL212TBX.HOLD_STATUS = 1) OR (GAL212TBX.HOLD_STATUS = 0)) AND GAL217TBX.Lot_Status != 3 and GAL217TBX.Lot_Status !=4 for read only";

	private static final String SELECT_WE_PRODUCTION_PROGRESS = "SELECT GAL217TBX.Production_Lot, GAL217TBX.Lot_Size, GAL217TBX.Plan_Code, GAL217TBX.WE_Line_No, GAL217TBX.WE_Process_Location, GAL217TBX.Product_Spec_Code, " +
			"(SELECT COUNT(Production_lot) from gal215tbx where Process_Point_ID in (@ON_PROCESSPOINTS@) and gal215tbx.production_lot = gal217tbx.production_lot ) as production_on_count, " +
			"(SELECT COUNT(Production_lot) from gal215tbx where  Process_Point_ID in (@OF_PROCESSPOINTS@) and gal215tbx.production_lot = gal217tbx.production_lot ) as production_off_count, " +
			"(SELECT COUNT(*) from GAL143TBX Where Tracking_Status in (@TRACKING_STATUSES@) and GAL143TBX.production_lot = gal217tbx.production_lot "+
			" AND NOT EXISTS (SELECT * FROM GAL215TBX G215A WHERE G215A.PRODUCT_ID = GAL143TBX.PRODUCT_ID AND G215A.PROCESS_POINT_ID IN (@ON_PROCESSPOINTS@))) AS EXCEPTION_ON_COUNT, "+
			"(SELECT COUNT(*) from GAL143TBX Where Tracking_Status in (@TRACKING_STATUSES@) and GAL143TBX.production_lot = gal217tbx.production_lot "+
			" AND NOT EXISTS (SELECT * FROM GAL215TBX G215A WHERE G215A.PRODUCT_ID = GAL143TBX.PRODUCT_ID AND G215A.PROCESS_POINT_ID IN (@OF_PROCESSPOINTS@))) AS EXCEPTION_OFF_COUNT, "+
			"gal212tbx.hold_status, gal217tbx.lot_number, gal217tbx.kd_lot_number, gal217tbx.LOT_STATUS, gal212tbx.sequence " +
			"FROM GAL217TBX,GAL212TBX WHERE GAL217TBX.WE_Process_Location ='WE' AND GAL217TBX.Production_Lot = GAL212TBX.Production_Lot "+
			"AND ((GAL212TBX.HOLD_STATUS = 1) OR (GAL212TBX.HOLD_STATUS = 0)) AND GAL217TBX.Lot_Status != 3 and GAL217TBX.Lot_Status !=4 for read only";
	
	private static final String SELECT_IA_1_PRODUCTION_PROGRESS = "SELECT GAL217TBX.Production_Lot, GAL217TBX.Lot_Size, GAL217TBX.Plan_Code, GAL217TBX.Line_No, GAL217TBX.Process_Location, GAL217TBX.Product_Spec_Code, " +
			"(SELECT COUNT(Production_lot) from gal215tbx where Process_Point_ID in (@ON_PROCESSPOINTS@) and gal215tbx.production_lot = gal217tbx.production_lot ) as production_on_count, " +
			"(SELECT COUNT(Production_lot) from gal215tbx where  Process_Point_ID in (@OF_PROCESSPOINTS@) and gal215tbx.production_lot = gal217tbx.production_lot ) as production_off_count, " ;
			
	private static final String SELECT_IA_2_PRODUCTION_PROGRESS ="(SELECT COUNT(*) from GAL215TBX a, GAL131TBX b Where a.Product_id = b.Product_id and a.Process_Point_ID in (@ON_PROCESSPOINTS@) and b.Tracking_Status in (@TRACKING_STATUSES@)";
	private static final String SELECT_IA_3_PRODUCTION_PROGRESS = " and a.production_lot = b.Production_lot and a.production_lot = gal217tbx.production_lot) as exception_on_count, " +
			"(SELECT COUNT(*) from GAL215TBX a, GAL131TBX b Where a.Product_id = b.Product_id and a.Process_Point_ID in (@OF_PROCESSPOINTS@) and b.Tracking_Status in (@TRACKING_STATUSES@)";
	private static final String SELECT_IA_4_PRODUCTION_PROGRESS = " and a.production_lot = b.Production_lot and a.production_lot = gal217tbx.production_lot) as exception_off_count, " +
			"gal212tbx.hold_status, gal217tbx.lot_number, gal217tbx.kd_lot_number, gal217tbx.LOT_STATUS, gal212tbx.sequence " +
			"FROM GAL217TBX, GAL212TBX WHERE (GAL217TBX.Lot_Status != 4 and GAL212TBX.Process_Location = 'IA' AND ((GAL212TBX.HOLD_STATUS = 1) OR (GAL212TBX.HOLD_STATUS = 0)) AND GAL217TBX.Lot_Number = GAL212TBX.Lot_Number AND GAL217TBX.Process_Location='IA' ) "+
			" OR (GAL217TBX.Lot_Status != 4 and GAL212TBX.process_location = 'IA' AND GAL217TBX.process_location = 'IA' AND ((GAL212TBX.HOLD_STATUS = 1) OR (GAL212TBX.HOLD_STATUS = 0)) AND GAL217TBX.Lot_Number = GAL212TBX.Lot_Number AND GAL212TBX.lot_number not in "+
			"(SELECT GAL212TBX.lot_number FROM gal212tbx WHERE GAL212TBX.process_location = 'IA') ) for read only";
	private static final String SELECT_IA_4_PRODUCTION_PROGRESS_PLANTS_TO_EXCLUDE_1 = " and a.production_lot = b.Production_lot and a.production_lot = gal217tbx.production_lot) as exception_off_count, " +
			"gal212tbx.hold_status, gal217tbx.lot_number, gal217tbx.kd_lot_number, gal217tbx.LOT_STATUS, gal212tbx.sequence " +
			"FROM GAL217TBX, GAL212TBX WHERE (GAL217TBX.Lot_Status != 4 and GAL212TBX.Process_Location = 'IA' AND ((GAL212TBX.HOLD_STATUS = 1) OR (GAL212TBX.HOLD_STATUS = 0)) AND "+
			"GAL217TBX.Lot_Number = GAL212TBX.Lot_Number AND GAL217TBX.Process_Location='IA' ) OR (GAL217TBX.Lot_Status != 4 and GAL212TBX.process_location = 'IA' AND GAL217TBX.process_location = 'IA' "+
			"AND ((GAL212TBX.HOLD_STATUS = 1) OR (GAL212TBX.HOLD_STATUS = 0)) AND GAL217TBX.Lot_Number = GAL212TBX.Lot_Number AND GAL212TBX.lot_number not in "+
			"(SELECT GAL212TBX.lot_number FROM gal212tbx WHERE GAL212TBX.process_location = 'IA') ) AND GAL217TBX.PLAN_CODE not in (@PLAN_CODES@) for read only";
	private static final String SKIPPED_LOTS = "select lot " 
			+ " from ProductResult result, ProductionLot lot  "
			+ " where lot.productionLot=result.productionLot AND result.id.processPointId=:ppId and lot.productionLot>:lastLot and lot.productionLot<:productionLot "
			+ " and lot.demandType = :demandType and lot.plantCode = :plantCode And lot.processLocation = :processLocation And lot.lineNo = :lineNo "
			+ " group by result.id.processPointId, lot.productionLot, lot.kdLotNumber, lot.processLocation, lot.demandType, lot.plantCode, lot.lineNo "
			+ " order by lot.productionLot desc ";

	private static final String DELETE_BY_PLANCODE_SENDSTATUS = "delete from galadm.gal217tbx where plan_code = ?1 and exists (select 1 from galadm.gal212tbx where production_lot = gal217tbx.production_lot and send_status = ?2 )";

	private static final String FIND_ACTIVE_FOR_PRODUCTION_DATE = "select * from GALADM.@PRODUCT_TABLE@ a where a.@COLUMN@ like ?1 and exists (select 1 from GALADM.@TABLE@ b where a.PRODUCT_ID = b.PRODUCT_ID fetch first row only)";
	private static final String FIND_ACTIVE_FOR_PRODUCTION_DATE_AND_PPIDS = "select * from GALADM.@PRODUCT_TABLE@ a where a.@COLUMN@ like ?1 and exists (select 1 from GALADM.@TABLE@ b where a.PRODUCT_ID = b.PRODUCT_ID and b.PROCESS_POINT_ID not in (@INITIAL_PROCESS_POINT_IDS@) fetch first row only)";

	private static final String GET_PRODUCTION_LOT_BACKOUT_DATA = "SELECT COUNT(*), MIN(@COLUMN@), MAX(@COLUMN@) FROM GALADM.@TABLE@ where @COLUMN@ between ?1 and ?2";

	private static final String BACKOUT_PRODUCTION_LOT_DATA = "DELETE FROM GALADM.@TABLE@ where @COLUMN@ between ?1 and ?2";

	private static final String BACKOUT_PRODUCTION_LOT_DATA_TAIL_RESET = "UPDATE GALADM.GAL212TBX SET NEXT_PRODUCTION_LOT = NULL WHERE NEXT_PRODUCTION_LOT between ?1 and ?2";

	private static final String BACKOUT_IN_PROCESS_PRODUCT_DATA_TAIL_RESET = "UPDATE GALADM.GAL176TBX A SET A.NEXT_PRODUCT_ID = NULL WHERE A.PRODUCTION_LOT NOT BETWEEN ?1 AND ?2 AND EXISTS (SELECT 1 FROM GALADM.GAL176TBX B WHERE A.NEXT_PRODUCT_ID = B.PRODUCT_ID AND B.PRODUCTION_LOT BETWEEN ?1 AND ?2 FETCH FIRST ROW ONLY)";

	private static final String GET_NON_COMPLETED_LOTS = "select substr(PRODUCT_ID,10,8) AS PRODUCT_ID_SUB_STRING,"
			+ "substr(a.START_PRODUCT_ID,10,8) AS START_PRODUCT_ID,PLAN_CODE, LINE_NO, PROCESS_LOCATION, "
			+ "case c.process_point_id when ?1 then 'ON' when ?2 then 'OFF' end as OnOff, b.AF_ON_SEQUENCE_NUMBER, "
			+ "b.PRODUCT_SPEC_CODE, b.PRODUCT_ID, date(b.CREATE_TIMESTAMP) as Create_Date, "
			+ "time(b.CREATE_TIMESTAMP) as Create_Time,  c.Process_point_id, a.LOT_SIZE, "
			+ "c.count_past_process, (a.LOT_SIZE-c.COUNT_PAST_PROCESS) AS LOT_SIZE_CALC /*, a.*, b.*, c.* */ "
			+ "from GALADM.GAL212TBX a join GALADM.GAL143TBX b on a.PRODUCTION_LOT = b.PRODUCTION_LOT "
			+ "and substr(PRODUCT_ID,10,8) = substr(a.START_PRODUCT_ID,10,8) join "
			+ "( select PROCESS_POINT_ID, PRODUCTION_LOT, count(PRODUCT_ID) as count_past_process from GALADM.GAL215TBX "
			+ "where PROCESS_POINT_ID in (?3, ?4) "
			+ "group by PRODUCTION_LOT, PROCESS_POINT_ID ) c on a.PRODUCTION_LOT = c.PRODUCTION_LOT "
			+ "order by b.CREATE_TIMESTAMP desc";
	//	#---------------------------------------------------------------------------------------------
	//	# Description : SQL statements used to generate the Processing Body Report(GIV706)
	//	#---------------------------------------------------------------------------------------------
	//	 Get In-Process bodies for AE 
	private static final String SELECT_PROCESSING_BODY = 
			(new StringBuffer("SELECT GAL217TBX.Plan_Code, GAL217TBX.Line_No, ")) 
			.append("GAL217TBX.Process_Location, GAL176TBX.Product_ID, ") 
			.append("SUBSTR(GAL176TBX.Production_Lot,9,12), GAL215TBX.Actual_Timestamp, ") 
			.append("SUBSTR(CHAR(GAL215TBX.Product_Spec_Code),1,22), GAL217TBX.KD_Lot_Number ")
			.append("FROM GAL176TBX, GAL215TBX, GAL217TBX ")
			.append("WHERE GAL176TBX.Production_Lot=GAL217TBX.Production_Lot AND ")
			.append("GAL176TBX.Product_ID=GAL215TBX.Product_ID ").toString(); 
	private static final String PA_SELECT_PROCESSING_BODY = 
			(new StringBuffer("SELECT GAL217TBX.Plan_Code, GAL217TBX.PA_Line_No, ")) 
			.append("GAL217TBX.PA_Process_Location, GAL176TBX.Product_ID, ") 
			.append("SUBSTR(GAL176TBX.Production_Lot,9,12), GAL215TBX.Actual_Timestamp, ") 
			.append("SUBSTR(CHAR(GAL215TBX.Product_Spec_Code),1,22), GAL217TBX.KD_Lot_Number ")
			.append("FROM GAL176TBX, GAL215TBX, GAL217TBX ")
			.append("WHERE GAL176TBX.Production_Lot=GAL217TBX.Production_Lot AND ")
			.append("GAL176TBX.Product_ID=GAL215TBX.Product_ID ").toString(); 
	private static final String WE_SELECT_PROCESSING_BODY = 
			(new StringBuffer("SELECT GAL217TBX.Plan_Code, GAL217TBX.WE_Line_No, ")) 
			.append("GAL217TBX.WE_Process_Location, GAL176TBX.Product_ID, ") 
			.append("SUBSTR(GAL176TBX.Production_Lot,9,12), GAL215TBX.Actual_Timestamp, ") 
			.append("SUBSTR(CHAR(GAL215TBX.Product_Spec_Code),1,22), GAL217TBX.KD_Lot_Number ")
			.append("FROM GAL176TBX, GAL215TBX, GAL217TBX ")
			.append("WHERE GAL176TBX.Production_Lot=GAL217TBX.Production_Lot AND ")
			.append("GAL176TBX.Product_ID=GAL215TBX.Product_ID ").toString(); 
	private static final String IA_SELECT_PROCESSING_BODY = 
			(new StringBuffer("SELECT GAL212TBX.Plan_Code, GAL212TBX.Line_No, ")) 
			.append("GAL212TBX.Process_Location, GAL176TBX.Product_ID, ") 
			.append("SUBSTR(GAL176TBX.Production_Lot,9,12), GAL215TBX.Actual_Timestamp, ") 
			.append("SUBSTR(CHAR(GAL215TBX.Product_Spec_Code),1,22), GAL212TBX.KD_Lot_Number ")
			.append("FROM GAL176TBX, GAL215TBX, GAL212TBX ")
			.append("WHERE GAL176TBX.Production_Lot=GAL212TBX.Production_Lot AND ")
			.append("GAL176TBX.Product_ID=GAL215TBX.Product_ID ").toString(); 

	private static final String PROCESSING_BODY_ORDER_CLAUSE = " ORDER BY GAL215TBX.Actual_Timestamp ASC for read only";


	private static final String SELECT_PRODUCTION_PROGRESS = new StringBuilder()
	.append("select ")
	/*0*/.append("	gal212.PLAN_CODE ")
	.append("	,gal212.LINE_NO ")
	.append("	,gal212.PROCESS_LOCATION ")//1 y 2 -On/Off Flag
	.append("	,gal217.KD_LOT_NUMBER ")
	.append("	,gal217.LOT_NUMBER ")
	/*5*/.append("	,gal212.MBPN ")
	.append("	,gal212.HES_COLOR ")
	.append("	,gal217.PRODUCT_SPEC_CODE ")//mtoc
	.append("	,gal217.LOT_SIZE ")
	.append("	,VARCHAR_FORMAT(gal217.CREATE_TIMESTAMP,'yyyyMMdd') as create_date ")
	/*10*/.append("	,VARCHAR_FORMAT(gal217.CREATE_TIMESTAMP, 'HH24MIss') as create_time ")
	.append("	,(select count(gal215.PRODUCT_ID) from GAL215TBX gal215 where gal215.PROCESS_POINT_ID = ?4 and gal215.PRODUCTION_LOT   = gal217.PRODUCTION_LOT) as productionCountOn ")//processPointAmOn
	.append("	,(select count(gal215.PRODUCT_ID) from GAL215TBX gal215 where gal215.PROCESS_POINT_ID = ?5 and gal215.PRODUCTION_LOT   = gal217.PRODUCTION_LOT) as productionCountOf ")//processPointAmOff
	.append("	,(select count(gal136.PRODUCT_ID) from GAL143TBX gal143 join GAL136TBX gal136 on gal143.PRODUCTION_LOT = gal217.PRODUCTION_LOT and gal136.product_id = gal143.PRODUCT_ID) as scrapCount ")
	.append("	,(select count(gal215.PRODUCT_ID) from GAL215TBX gal215 join GAL136TBX gal136 on gal215.PRODUCT_ID = gal136.product_id  where gal215.PROCESS_POINT_ID = ?4 and gal215.PRODUCTION_LOT = gal217.PRODUCTION_LOT)as exceptionalCountOn ")//processPointAmOn
	/*15*/.append("	,(select count(gal215.PRODUCT_ID) from GAL215TBX gal215 join GAL136TBX gal136 on gal215.PRODUCT_ID = gal136.product_id  where gal215.PROCESS_POINT_ID = ?5 and gal215.PRODUCTION_LOT = gal217.PRODUCTION_LOT) as exceptionalCountOf ")//processPointAmOff
	.append("	, gal217.PRODUCTION_LOT ")
	.append("from GAL212TBX gal212, GAL217TBX gal217 ")
	.append("where gal212.PROCESS_LOCATION = ?1 ") //processLocation		
	.append("	and gal212.HOLD_STATUS      in ( '0','1' ) ")//0 - hold, 1 - no hold holdStatus
	/*20*/.append("	and gal212.PRODUCTION_LOT   = gal217.PRODUCTION_LOT ")
	.append("	and gal217.PLANT_CODE       = ?2 ") //plantCode
	.append("	and gal217.CREATE_TIMESTAMP > ?3 ") //createTimestamp
	.append("   and gal217.LOT_STATUS      <> '4' ")//-- :LOT_STATUS '4'--status 4 es que salieron AF off")
	.append("	and gal217.LOT_SIZE <> ( select count( * ) from GAL143TBX GAL143TBX, GAL136TBX GAL136TBX ")
	.append("where GAL143TBX.KD_LOT_NUMBER = gal217.KD_LOT_NUMBER ")
	.append("	and GAL143TBX.PRODUCT_ID    = GAL136TBX.PRODUCT_ID )--excluye los vins en scrap ")
	.append("order by gal212.PRODUCTION_LOT ")
	.toString();

	private final static String PROCESSING_BODY	= new StringBuilder()
	.append( "SELECT distinct "								)
	.append( "		productionLot.Plan_Code	"				)
	.append( "		,divisionGPCS.gpcs_line_no as line_no " )
	.append( "		,divisionGPCS.gpcs_process_location as process_location " )
	.append( "		,tracking.product_id	"				)
	.append( "		,productionLot.lot_number	"			)
	.append( "		,to_char(max(history.actual_timestamp), 'YYYYMMDDHH24MISS') as actual_timestamp ")
	.append( "		,productionLot.product_spec_code "		)
	.append( "		,productionLot.kd_lot_number "			)
	.append( "		,preProductionLot.mbpn as partNumber "	)
	.append( "		,preProductionLot.hes_color as partColorCode "	)
	.append( "FROM	GAL215TBX history				"		)
	.append( "		,GAL176TBX tracking 			"		)
	.append( "		,GAL217TBX productionLot 		"		)
	.append( "		,GAL212TBX preProductionLot 	"		)
	.append( "		,GAL238TBX divisionGPCS 		"		)
	.append( "		,GAL214TBX processPoint 		"		)
	.append( "WHERE history.product_id	= tracking.product_id "	)
	.append( " and history.process_point_id = processPoint.process_point_id "		)
	.append( " and productionLot.production_lot  = tracking.production_lot "		)
	.append( " and productionLot.production_lot  = preProductionLot.production_lot ")
	.append( " and processPoint.division_id      = divisionGPCS.division_id "		)
	.append( " and history.process_point_id      in ( :PROCESS_POINT ) "			)
	.append( " and tracking.line_id              in ( :LINE_ID ) "					)
	.append( "group by  productionLot.Plan_Code "									)
	.append( "			,divisionGPCS.gpcs_line_no "								)
	.append( "			,divisionGPCS.gpcs_process_location "						)
	.append( "			,tracking.product_id "										)
	.append( "			,productionLot.lot_number "									)
	.append( "			,productionLot.product_spec_code "							)
	.append( "			,productionLot.kd_lot_number "								)
	.append( "			,preProductionLot.mbpn "									)
	.append( "			,preProductionLot.hes_color "								)
	.append( "order by gpcs_process_location " 									)
	.append ("			,actual_timestamp asc "										)
	.append( "for read only "														)
	.toString();

	private static final String FIND_ALL_DEMAND_TYPE = "select distinct DEMAND_TYPE from galadm.GAL217TBX";
	
	private static final String UPDATE_COLOR_DETAILS_2 = "Update GALADM.GAL217TBX set PRODUCT_SPEC_CODE = ?1 where PRODUCT_SPEC_CODE = ?2 and PRODUCTION_LOT in (:productionLot)";


	public List<ProductionLot> findAll(String processLocation, int startPosition, int pageSize) {
		return findAll(Parameters.with("processLocation", processLocation), startPosition,  pageSize);
	}

	public List<ProductionLot> findAll(String processLocation,
			Date productionDate) {
		return findAll(Parameters.with("processLocation", processLocation).put("productionDate", productionDate));
	}


	public int findProdLotKdQty(ProductionLot productionLot) {		
		int prodLotKdQty = 0;
		List<ProductionLot> productionLots = findAll(Parameters.with("prodLotKd", productionLot.getProdLotKd()));

		if(productionLots != null) {
			for(ProductionLot prodLot : productionLots)
				prodLotKdQty += prodLot.getLotSize();
		}

		return prodLotKdQty;
	}        


	@Transactional
	public int delete(String prodLotNumber)
	{
		return delete(Parameters.with("productionLot", prodLotNumber));
	}        

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateLotStatus(String productionLot, int status) {
		Parameters parameters = Parameters.with("1", productionLot);
		parameters.put("2", status);

		executeNative(UPDATE_LOT_STATUS, parameters);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateStartProductId(String productionLot, String StartProductId) {
		Parameters parameters = Parameters.with("1", productionLot);
		parameters.put("2", StartProductId);

		executeNative(UPDATE_START_PRODUCT_ID, parameters);

	}


	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ProductionLot getProductionLotForProductionPlan(String planCode, String kdLotNumber, String productSpecCode) {
		Parameters parameters = Parameters.with("planCode", planCode);
		parameters.put("kdLotNumber", kdLotNumber);	
		parameters.put("productSpecCode", productSpecCode);	
		return findFirst(parameters);
	}

	public List<ProductionLot> findAllByProcessLoc(String processLocation) 
	{
		return findAll(Parameters.with("processLocation", processLocation),new String[] {"lotNumber"},true);
	}

	/**
	 * This method takes and integer and a total length value and returns a string value equal to totalLength prefixed with leading zeros.
	 * e.g. calling prefixLeadingZeros(45, 5) will return a string 00045.
	 * @param value
	 * 		the value to be prefixed with leading zeros
	 * @param totalLength
	 * 		the total length of the returned value
	 * @return a zero prefixed string value
	 * @throws Exception
	 * @author Ratul Chakravarty
	 */
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

	/**
	 * Get production progress records for value 102/707 and div AE/AF/PA/WE
	 * @param Integer value
	 * 		denotes the type of file being generated i.e. either a value of 102 means GPP102 & a value of 707 means GIV707
	 * @param String div
	 * 		denotes the division or department for which records are being fetch. Accepted values are AE,AF,PA,WE
	 * @param Boolean value
	 * 		denotes whether to allow data update or not
	 * @return Map<String, List<String>>
	 * 		It is a map of lists of strings. Each string is represents a record arranged as per required format.
	 * @author Ratul Chakravarty
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public Map<String, List<String>> getProductionProgress(Integer prodProgressType, List<String> processPointOn, List<String> processPointOff, String div, List<String> lines, Boolean allowDBUpdate, Boolean useSequenceForBuildSequence, Integer sequenceNumberScale, Boolean excludeListedPlanCodes, List<String> planCodesToExclude) {
		int vLotSize = 0;
		int vRemainingLot = 0;
		int vCountOn = 0;
		int vCountOff = 0;
		int vScrap = 0;
		int vScrapOn = 0;
		int vScrapOff = 0;
		String vProductionLot = null;

		
		StringBuffer qry = new StringBuffer();
		if(div.equals("AE")) {
			qry = new StringBuffer(SELECT_AE_1_PRODUCTION_PROGRESS);
			qry.append(SELECT_AE_2_PRODUCTION_PROGRESS);
			qry.append(SELECT_AE_3_PRODUCTION_PROGRESS);
			if (excludeListedPlanCodes) {
				if (planCodesToExclude != null && planCodesToExclude.size() > 0) {
					qry.append(SELECT_AE_4_PRODUCTION_PROGRESS_PLANTS_TO_EXCLUDE_1);
				} else {
					logger.error("Plan code list to exclude is empty or null (the corresponding property is not present), proceding to get Production Progress normally ...");
					qry.append(SELECT_AE_4_PRODUCTION_PROGRESS);
				}
			} else {
				// work normally, do not exclude any plan code
				qry.append(SELECT_AE_4_PRODUCTION_PROGRESS);
			}
		}
		else if(div.equals("AF")) {
			qry = new StringBuffer(SELECT_AF_PRODUCTION_PROGRESS);
			
		}
		else if(div.equals("PA")) {
			qry = new StringBuffer(SELECT_PA_PRODUCTION_PROGRESS);
			
		}
		else if(div.equals("WE")) {
			qry = new StringBuffer(SELECT_WE_PRODUCTION_PROGRESS);
			
		}
		else if(div.equals("IA")) {
			qry = new StringBuffer(SELECT_IA_1_PRODUCTION_PROGRESS);
			qry.append(SELECT_IA_2_PRODUCTION_PROGRESS);
			qry.append(SELECT_IA_3_PRODUCTION_PROGRESS);
			if (excludeListedPlanCodes) {
				if (planCodesToExclude != null && planCodesToExclude.size() > 0) {
					qry.append(SELECT_IA_4_PRODUCTION_PROGRESS_PLANTS_TO_EXCLUDE_1);
				} else {
					logger.error("Plan code list to exclude is empty or null (the corresponding property is not present), proceding to get Production Progress normally ...");
					qry.append(SELECT_IA_4_PRODUCTION_PROGRESS);
				}
			} else {
				// work normally, do not exclude any plan code
				qry.append(SELECT_IA_4_PRODUCTION_PROGRESS);
			}
		}

		String query = qry.toString().replace("@ON_PROCESSPOINTS@",StringUtil.toSqlInString(processPointOn));
		query = query.replace("@OF_PROCESSPOINTS@",StringUtil.toSqlInString(processPointOff));
		query = query.replace("@TRACKING_STATUSES@",StringUtil.toSqlInString(lines));
		query = query.replace("@PLAN_CODES@", StringUtil.toSqlInString(planCodesToExclude));

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
			List<Object[]> prodProgressList = findResultListByNativeQuery(query,null);

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
					// 8 - Number of scrap and exception with the production lot that are found in galadm.gal143tbx
					// 9 - Number of scrap and exception with the production lot that are "ON" in GALADM.gal215tbx
					//10 - Number of scrap and exception with the production lot that are "OFF" in GALADM.gal215tbx
					//11 - Hold Status
					//12 - Lot number
					//15 - sequence
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

							vRecord.append(objArr[2].toString());
							vRecord.append(objArr[3].toString());
							vRecord.append(objArr[4].toString());
							vRecord.append(index);
							vRecord.append(vProductionLot.substring(8, 20));
							vRecord.append(objArr[5].toString().substring(0,22));
							vRecord.append(prefixLeadingZeros(String.valueOf(vLotSize), 5));
							vRecord.append(prefixLeadingZeros(String.valueOf(vRemainingLot), 5));
							vRecord.append(aCreateTimestamp);
							String hs = objArr[10].toString();
							if(hs.equals("1")){
								vRecord.append("N");
								if(useSequenceForBuildSequence){
									if(objArr[14] != null){
										//set scale to "SEQUENCE_NUMBER_SCALE" from GAL489TBX , default is true and remove decimal point
										BigDecimal seqNum = (new BigDecimal(objArr[14].toString())).setScale(sequenceNumberScale, RoundingMode.HALF_EVEN);
										seqNum =seqNum.multiply((new BigDecimal(10).pow(sequenceNumberScale)));
										vRecord.append(prefixLeadingZeros(String.valueOf(seqNum.toBigInteger()),12));
									}else{
										vRecord.append(prefixLeadingZeros("",12));
									}
								}else{
									//TODO: Add when needed, Should be set based on linked list order
									vRecord.append(prefixLeadingZeros("",12));
								}
							}
							else{
								vRecord.append("Y");
								vRecord.append(prefixLeadingZeros(objArr[11].toString(),12));
							}
							vRecord.append("             ");
							
							gpp102Records.add(vRecord.toString());
							vRecord.delete(0, vRecord.length());
						}
						if(prodProgressType == 707) {

							//create merge record for GIV707
							// Result set returns the following fields :
							// 0 - Production_Lot
							// 1 - Lot_Size
							// 2 - Plan_Code
							// 3 - Line_No
							// 4 - Process_Location
							// 5 - Product_Spec_Code
							// 6 - Number of vins with the production lot that are "ON"  in GALADM.gal215tbx
							// 7 - Number of vins with the production lot that are "OFF"  in GALADM.gal215tbx
							// 8 - Number of scrap and exception with the production lot that are found in galadm.gal143tbx
							// 9 - Number of scrap and exception with the production lot that are "ON" in GALADM.gal215tbx
							//10 - Number of scrap and exception with the production lot that are "OFF" in GALADM.gal215tbx
							//11 - Hold Status
							//12 - Lot number
							//13 - KD Lot Number
							//Get Lot size, number of production on, number of production off,
							// number of scrap, number exception on, and number of exception off
							StringBuffer vMergeRecord = new StringBuffer();
							vMergeRecord.append(objArr[2].toString());
							vMergeRecord.append(objArr[3].toString());
							vMergeRecord.append(objArr[4].toString());
							vMergeRecord.append(index);
							vMergeRecord.append(vProductionLot.substring(8, 20));
							vMergeRecord.append(objArr[5].toString().substring(0, 22));
							vMergeRecord.append(objArr[12].toString());
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

	@Transactional
	public long countByLotAndLocation(String lotNo, String processLocation) {
		long result = count(Parameters.with("processLocation", processLocation).put("lotNo", lotNo));
		return result;
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public int getLotSize(String lot, String processLocation) {
		Parameters params = Parameters.with("1", lot.substring(0, 16) + "%");
		params.put("2", processLocation);
		Integer lotSize = findFirstByNativeQuery(GET_LOT_SIZE, params, Integer.class);
		return lotSize == null ? 0 : lotSize;
	}

	/**
	 * Get processing body records for div AE/AF/PA/WE
	 * @param String div
	 * 		division or department for which records are being fetch. Accepted values are AE,AF,PA,WE
	 * @return List<String>
	 * 		Each string represents a record arranged per required format.
	 * @author Larry Karpov
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public List<Object[]> getProcessingBody(String department, List<String> processPoints, List<String> lines) {
		StringBuffer qry = new StringBuffer();
		if(department.equals("AE")) {
			qry.append(SELECT_PROCESSING_BODY);
		} else if(department.equals("AF")) {
			qry.append(SELECT_PROCESSING_BODY);
		} else if(department.equals("PA")) {
			qry.append(PA_SELECT_PROCESSING_BODY);
		} else if(department.equals("WE")) {
			qry.append(WE_SELECT_PROCESSING_BODY);
		} else if(department.equals("IA")) {
					qry.append(IA_SELECT_PROCESSING_BODY);
		} else {
			logger.error("Unknown department: " + department);
			return null;
		}
		qry.append(" AND GAL215TBX.Process_Point_ID in (" + StringUtil.toSqlInString(processPoints) + ")");
		qry.append(" AND GAL176TBX.LINE_ID IN (@TRACKING_STATUS@)");
		qry.append(" AND GAL215TBX.ACTUAL_TIMESTAMP = (SELECT MAX(G215.ACTUAL_TIMESTAMP) FROM GAL215TBX G215 WHERE G215.PRODUCT_ID = GAL215TBX.PRODUCT_ID " + 
				"  AND G215.PROCESS_POINT_ID IN ("+StringUtil.toSqlInString(processPoints)+"))");
		qry.append(PROCESSING_BODY_ORDER_CLAUSE);
		logger.info("Query: " + qry.toString());

		//Get Processing Body
		List<Object[]> processingBodyList = null; 
		try {
			String query = qry.toString();
			query = query.replace("@TRACKING_STATUS@", StringUtil.toSqlInString(lines));
			processingBodyList = findResultListByNativeQuery(query, null);
		} catch (Exception e) {
			Logger.getLogger().error("Exception Occured:- " + e.getMessage());
		}

		return processingBodyList;
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED, propagation=Propagation.NOT_SUPPORTED)
	public ProductionLot findLastSkippedLot(String ppId, String lastLot, String productionLot, String plantCode, String lineNo, String processLocation) {
		Parameters params = Parameters.with("ppId", ppId);
		params.put("lastLot", lastLot);
		params.put("productionLot", productionLot);
		params.put("demandType", "MP");
		params.put("plantCode", plantCode);
		params.put("processLocation", processLocation);
		params.put("lineNo", lineNo);
		return findFirstByQuery(SKIPPED_LOTS, params);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED, propagation=Propagation.NOT_SUPPORTED)
	public int getLotSizeByKdLot(String kdLot, String processLocation) {
		int result = 0;
		Parameters params = Parameters.with("kdLotNumber", kdLot.substring(0, 16) + "%");
		List<ProductionLot> lots = findAll(params); 
		for(ProductionLot lot : lots) {
			result += lot.getLotSize();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getProductionProgress(final String processLocation, 
			final String plantCode, final java.util.Date createTimestamp, final String processPointAmOn, final String processPointAmOff) {
		Parameters parameters = Parameters.with("1", processLocation);

		//parameters.put("2", holdStatus);
		parameters.put("2", plantCode);
		parameters.put("3", createTimestamp);
		parameters.put("4", processPointAmOn);
		parameters.put("5", processPointAmOff);
		return findResultListByNativeQuery(SELECT_PRODUCTION_PROGRESS, parameters);
	}

	public List<ProductionLot> findByKDLotAndPlanCode(String kdLot, String planCode) {
		Parameters params = Parameters.with("kdLotNumber", kdLot).put("planCode", planCode);
		List<ProductionLot> prodLots = this.findAll(params);
		return prodLots;
	}

	/**
	 * Method to get the Processing Body information receiving the process point and the tracking line id
	 * @param processPoint - String separated by comma
	 * @param lineId - String separated by comma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getProcessingBody ( final String processPoints, final String lineIds )
	{
		final String processPointParameter		= StringUtil.toSqlInString(	processPoints	);
		final String trackingStatusParameter	= StringUtil.toSqlInString(	lineIds			); 

		String query	=	PROCESSING_BODY.replace( ":PROCESS_POINT", processPointParameter)
				.replace(":LINE_ID", trackingStatusParameter);
		List<Object[]> precessingBody	=	findResultListByNativeQuery( query, null );

		return precessingBody;
	}

	@Transactional
	public int deleteByPlanCodeSendStatus(final String planCode, final int sendStatus) {
		Parameters params = Parameters.with("1", planCode);
		params.put("2", sendStatus);

		return executeNative(DELETE_BY_PLANCODE_SENDSTATUS, params);
	}

	@Override
	public boolean isTableActiveForProductionDate(String column,String table, String lotPrefix, String productionDate, String productTable, List<String> initialProcessPointIds) {
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
			result = findFirstByNativeQuery(FIND_ACTIVE_FOR_PRODUCTION_DATE.replace("@TABLE@", table).replace("@PRODUCT_TABLE@", productTable).replace("@COLUMN@", column), params, Object.class);
		} else {
			String inProcessPoints = StringUtil.toSqlInString(initialProcessPointIds);
			result = findFirstByNativeQuery(FIND_ACTIVE_FOR_PRODUCTION_DATE_AND_PPIDS.replace("@TABLE@", table).replace("@PRODUCT_TABLE@", productTable).replace("@COLUMN@", column).replace("@INITIAL_PROCESS_POINT_IDS@", inProcessPoints), params, Object.class);
		}
		return (result != null);
	}

	@Override
	public List<ProductionLotBackout> getPopulatedProductionLotBackouts(List<ProductionLotBackout> productionLotBackouts) {
		for (ProductionLotBackout productionLotBackout : productionLotBackouts) {
			Parameters productionLotParameters = Parameters.with("1", productionLotBackout.getLotMin()).put("2", productionLotBackout.getLotMax());
			List<Object[]> productionLotBackoutData = null;
			
			
			 productionLotBackoutData = executeNative(productionLotParameters, GET_PRODUCTION_LOT_BACKOUT_DATA.replace("@TABLE@", productionLotBackout.getTable()).replace("@COLUMN@", productionLotBackout.getColumn()));
		
			if (productionLotBackoutData != null && productionLotBackoutData.size() == 1) {
				productionLotBackout.setRows((Integer) productionLotBackoutData.get(0)[0]);
				productionLotBackout.setLotRangeStart((String) productionLotBackoutData.get(0)[1]);
				productionLotBackout.setLotRangeEnd((String) productionLotBackoutData.get(0)[2]);
			} else {
				throw new RuntimeException("Received " + (productionLotBackoutData == null ? 0 : productionLotBackoutData.size()) + " production lot backout data records for table " + productionLotBackout.getTable());
			}
		}
		return productionLotBackouts;
	}

	@Override
	@Transactional
	public int backoutProductionLot(List<ProductionLotBackout> productionLotBackouts) {
		int totalDeleteCount = 0;
		Parameters productionLotTailUpdateParameters = null;

		for (ProductionLotBackout productionLotBackout : productionLotBackouts) {
			if (productionLotBackout.getTable().equals(CommonUtil.getTableName(Frame.class))) {
				productionLotTailUpdateParameters = Parameters.with("1", productionLotBackout.getLotMin()).put("2", productionLotBackout.getLotMax());
			}
			if (productionLotBackout.getTable().equals(CommonUtil.getTableName(InProcessProduct.class))) {
				Parameters inProcessProductTailUpdateParameters = Parameters.with("1", productionLotBackout.getLotMin()).put("2", productionLotBackout.getLotMax());
				int inProcessProductTailUpdateCount = executeNative(BACKOUT_IN_PROCESS_PRODUCT_DATA_TAIL_RESET, inProcessProductTailUpdateParameters);
				if (inProcessProductTailUpdateCount > 1) {
					throw new RuntimeException("Expected at most 1 InProcessProduct tail record reset but executed " + inProcessProductTailUpdateCount);
				}
			}
			Logger.getLogger().info("Backing out records from " + productionLotBackout.getTable() + " for " + productionLotBackout.getLotMin() + " - " + productionLotBackout.getLotMax());
			int deleteCount = 0;
			Parameters productionLotParameters = Parameters.with("1", productionLotBackout.getLotMin()).put("2", productionLotBackout.getLotMax());
			deleteCount = executeNative(BACKOUT_PRODUCTION_LOT_DATA.replace("@TABLE@", productionLotBackout.getTable()).replace("@COLUMN@", productionLotBackout.getColumn()), productionLotParameters);
			if (deleteCount != productionLotBackout.getRows()) {
				throw new RuntimeException("Expected " + productionLotBackout.getRows() + " backouts for " + productionLotBackout.getTable() + " but backed out " + deleteCount);
			}
			Logger.getLogger().info("Backed out " + deleteCount + " records from " + productionLotBackout.getTable() + " for " + productionLotBackout.getLotMin() + " - " + productionLotBackout.getLotMax());
			totalDeleteCount += deleteCount;
		}

		if (productionLotTailUpdateParameters == null) {
			productionLotTailUpdateParameters = Parameters.with("1", productionLotBackouts.get(0).getLotMin()).put("2", productionLotBackouts.get(0).getLotMax());
		}
		int productionLotTailUpdateCount = executeNative(BACKOUT_PRODUCTION_LOT_DATA_TAIL_RESET, productionLotTailUpdateParameters);
		if (productionLotTailUpdateCount != 1) throw new RuntimeException("Expected 1 PreProductionLot tail record reset but executed " + productionLotTailUpdateCount);
		Logger.getLogger().info("Executed " + productionLotTailUpdateCount + " tail record reset");
		return totalDeleteCount;
	}

	public Map<String,ProductionLot> findAllByLotNumber(List<String> lotNumbers) {
		Map<String,ProductionLot> productionLots = new HashMap<String,ProductionLot>();
		if(lotNumbers != null && !lotNumbers.isEmpty()){
			for(String lotNumber : lotNumbers){
				productionLots.put(lotNumber, findByKey(lotNumber));
			}	
		}
		
		return productionLots;
	}

	public List<String> findAllDemandType() {
		return findAllByNativeQuery(FIND_ALL_DEMAND_TYPE, null, String.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getListOfIncompleteLots(String onPpId, String offPpId) {
		List<Object[]> nonCompletedLots = null; 
		try {
			Parameters parameters = Parameters.with("1", onPpId);
			parameters.put("2", offPpId);
			parameters.put("3", onPpId);
			parameters.put("4", offPpId);
			nonCompletedLots = findResultListByNativeQuery(GET_NON_COMPLETED_LOTS, parameters);
		} catch (Exception e) {
			Logger.getLogger().error("Exception Occured:- " + e.getMessage());
		}

		return nonCompletedLots;
	}

	@Transactional
	public void updateColorDetails(String productCodeForUpdate,
			FrameSpecDto selectedFrameSpecDto, List<String> productionLots) {
		
		Parameters params2 = Parameters.with("1", productCodeForUpdate)
				.put("2", selectedFrameSpecDto.getProductSpecCode());
		String sqlStr = UPDATE_COLOR_DETAILS_2;
		sqlStr = sqlStr.replaceAll(":productionLot", StringUtil.toSqlInString(productionLots));
		executeNative(sqlStr, params2);
	}
}
