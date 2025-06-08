package com.honda.galc.dao.jpa.product;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dto.EngineNumberingDto;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

/**
 * 
 * <h3>EngineDaoImpl Class description</h3>
 * <p> EngineDaoImpl description </p>
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
public class EngineDaoImpl extends ProductDaoImpl<Engine> implements EngineDao {

    private static final long serialVersionUID = 1L;
     
	private static final String FIND_PRODUCTS_BY_PRODUCT_SEQUENCE = 
		"SELECT A.* FROM GALADM.GAL131TBX A,GALADM.PRODUCT_SEQUENCE_TBX B " +
		"WHERE A.PRODUCT_ID = B.PRODUCT_ID AND B.PROCESS_POINT_ID =?1  " + 
		"ORDER BY B.REFERENCE_TIMESTAMP ASC";
	
	private static final String FIND_PROCESSED_PRODUCTS_BY_IN_PROCESS_PRODUCT = 
		"WITH SortedList (PRODUCT_ID, NEXT_PRODUCT_ID, Level) AS(" + 
		"SELECT PRODUCT_ID, NEXT_PRODUCT_ID, 0 as Level FROM GALADM.GAL176tbx " + 
	    "WHERE PRODUCT_ID = ? " + 
	    "UNION ALL " + 
	    "SELECT ll.PRODUCT_ID, ll.NEXT_PRODUCT_ID, Level+1 as Level FROM GALADM.GAL176tbx ll, SortedList as s " +
	    "WHERE ll.NEXT_PRODUCT_ID = s.PRODUCT_ID) " + 
	    "SELECT b.* FROM SortedList a, GALADM.GAL131TBX b WHERE a.PRODUCT_ID = b.PRODUCT_ID "; 
		
	private static final String FIND_UPCOMING_PRODUCTS_BY_IN_PROCESS_PRODUCT = 
		"WITH SortedList (PRODUCT_ID, NEXT_PRODUCT_ID, Level) AS(" + 
		"SELECT PRODUCT_ID, NEXT_PRODUCT_ID, 0 as Level FROM GALADM.GAL176tbx " + 
	    "WHERE PRODUCT_ID = ? " + 
	    "UNION ALL " + 
	    "SELECT ll.PRODUCT_ID, ll.NEXT_PRODUCT_ID, Level+1 as Level FROM GALADM.GAL176tbx ll, SortedList as s " +
	    "WHERE ll.PRODUCT_ID = s.NEXT_PRODUCT_ID) " + 
	    "SELECT b.* FROM SortedList a, GALADM.GAL131TBX b WHERE a.PRODUCT_ID = b.PRODUCT_ID " ;
	
	private static final String ORDER_BY = " ORDER BY Level asc fetch first ";
	private static final String ROWS_ONLY = " rows only with ur";
	
	private static final String FIND_ENGINES_BY_LINE_IDS = "select e from Engine e where e.trackingStatus in ";
	
	private static final String FIND_INVENTORY_COUNTS =
		"SELECT COUNT(*) as COUNT,LAST_PASSING_PROCESS_POINT_ID as PROCESS, SUBSTR(KD_LOT_NUMBER,1,6) as PLANT " + 
		"FROM GALADM.GAL131TBX " +  
		"WHERE (DEFECT_STATUS IS NULL or DEFECT_STATUS < 3) AND AUTO_HOLD_STATUS <>1 " + 
		"GROUP BY LAST_PASSING_PROCESS_POINT_ID,SUBSTR(KD_LOT_NUMBER,1,6) " + 
		"ORDER BY PROCESS,PLANT";
	
	private static final String FIND_HOLD_COUNTS =
		"SELECT COUNT(*) as COUNT,LAST_PASSING_PROCESS_POINT_ID as PROCESS, SUBSTR(KD_LOT_NUMBER,1,6) as PLANT " + 
		"FROM GALADM.GAL131TBX " +  
		"WHERE (DEFECT_STATUS IS NULL or DEFECT_STATUS < 3) AND AUTO_HOLD_STATUS = 1 " +
		"GROUP BY LAST_PASSING_PROCESS_POINT_ID,SUBSTR(KD_LOT_NUMBER,1,6) " + 
		"ORDER BY PROCESS,PLANT";
	
	private final String FIND_ELIGIBLE_PRODUCTS = "WITH ID_ORDERED (PRODUCT_ID, NEXT_PRODUCT_ID, ID_SEQ) "
			+ "AS (SELECT ID_START.PRODUCT_ID, ID_START.NEXT_PRODUCT_ID, 1 AS ID_SEQ "
			+ "  FROM GAL176TBX ID_START "
			+ " WHERE     ID_START.NEXT_PRODUCT_ID IS NULL "
			+ "    AND ID_START.LINE_ID = ?1 "
			+ " UNION ALL "
			+ "  SELECT ID_NEXT.PRODUCT_ID, "
			+ "   ID_NEXT.NEXT_PRODUCT_ID, "
			+ "   s.ID_SEQ + 1 AS ID_SEQ "
			+ "   FROM GAL176TBX ID_NEXT, ID_ORDERED AS s "
			+ "  WHERE ID_NEXT.NEXT_PRODUCT_ID = s.PRODUCT_ID) "
			+ " SELECT "
			+

			" NON_INSTALLED.PRODUCT_ID "
			+

			" FROM (SELECT ROW_NUMBER () OVER (ORDER BY ID_ORDERED.ID_SEQ DESC) "
			+ "      AS LINE_SEQ_NUM, "
			+ "  ROW_NUMBER () "
			+ "  OVER (PARTITION BY INSTALLED_PART.INSTALLED_PART_STATUS "
			+ "       ORDER BY ID_ORDERED.ID_SEQ DESC) "
			+ "    AS PRINT_ELIGIBLE_SEQ_NUM, "
			+ " ENGINE.PRODUCT_ID, "
			+ " ID_ORDERED.NEXT_PRODUCT_ID, "
			+ "  MTOC.PRODUCT_SPEC_CODE, "
			+ "  RULES.PART_NAME, "
			+ " INSTALLED_PART.PART_NAME AS INSTALLED_PART_NAME, "
			+ " INSTALLED_PART.INSTALLED_PART_STATUS "
			+ " FROM ID_ORDERED "
			+ " JOIN GAL131TBX ENGINE "
			+ "   ON ID_ORDERED.PRODUCT_ID = ENGINE.PRODUCT_ID "
			+ " JOIN GAL133TBX MTOC "
			+ "   ON ENGINE.PRODUCT_SPEC_CODE = MTOC.PRODUCT_SPEC_CODE "
			+ " JOIN GAL246TBX RULES "
			+ "  ON (   RULES.MODEL_YEAR_CODE = '*' "
			+ "      OR MTOC.MODEL_YEAR_CODE = RULES.MODEL_YEAR_CODE) "
			+ "  AND (   RULES.MODEL_CODE = '*' "
			+ "      OR MTOC.MODEL_CODE = RULES.MODEL_CODE) "
			+ "  AND (   RULES.MODEL_TYPE_CODE = '*' "
			+ "      OR MTOC.MODEL_TYPE_CODE = RULES.MODEL_TYPE_CODE) "
			+ " AND (   RULES.MODEL_OPTION_CODE = '*' "
			+ "     OR MTOC.MODEL_OPTION_CODE = RULES.MODEL_OPTION_CODE) "
			+ "    AND RULES.PROCESS_POINT_ID = ?4 "
			+ "  AND RULES.PART_NAME = ?5 "
			+ "  LEFT JOIN GAL185TBX INSTALLED_PART "
			+ "    ON     ID_ORDERED.PRODUCT_ID = INSTALLED_PART.PRODUCT_ID "
			+ "     AND INSTALLED_PART.PART_NAME = RULES.PART_NAME "
			+ "  WHERE    INSTALLED_PART.INSTALLED_PART_STATUS IS NULL "
			+ "  OR INSTALLED_PART.INSTALLED_PART_STATUS < 0) AS NON_INSTALLED "
			+ "WHERE     NON_INSTALLED.LINE_SEQ_NUM <= ?2 "
			+ "  AND NON_INSTALLED.PRINT_ELIGIBLE_SEQ_NUM <= ?3 "
			+ " AND NON_INSTALLED.INSTALLED_PART_STATUS IS NULL ";

	private static String GET_ENGINE_LINE_SHIPMENT_STATUS_DATA="SELECT T1.PRODUCT_ID, T1.PRODUCT_SPEC_CODE, T2.LINE_NAME, T3.STATUS, T1.KD_LOT_NUMBER, T1.LAST_PASSING_PROCESS_POINT_ID from GAL195TBX T2, GAL131TBX T1 left join GAL263TBX T3 on T1.PRODUCT_ID = T3.VIN where T1.TRACKING_STATUS = T2.LINE_ID and T1.PRODUCTION_LOT = ?1 and T1.TRACKING_STATUS != ?2 and T1.TRACKING_STATUS != ?3 ";

	private static final String UPDATE_TRACK_STATUS_BY_PRODUCT_IDS = "UPDATE GALADM.GAL131TBX set TRACKING_STATUS= ?1 " 
		+ "WHERE product_id IN "; 
	
	private static final String FIND_ENGINES_AND_BEGIN_CURETIME_BY_LINE_IDS =
		"select t131.PRODUCT_ID as productId, t131.KD_LOT_NUMBER as kdLotNumber, t185.ACTUAL_TIMESTAMP as cureTimeBegin, t131.TRACKING_STATUS as trackingStatus " + 
		"FROM GALADM.GAL131TBX t131 " +  
		"join gal185tbx t185 on (t131.product_id=t185.product_id) " +  
		"WHERE 1=1 " + 
		"AND t185.part_name = ?1 " + 
		"AND t131.TRACKING_STATUS in ";
	
	private static final String FIND_ENGINES_RECENTLY_STAMPED = 
		"SELECT a.PRODUCT_ID,a.PRODUCT_SPEC_CODE, b.PART_SERIAL_NUMBER, c.LOT_SIZE,a.PRODUCTION_LOT,c.START_PRODUCT_ID,a.LAST_PASSING_PROCESS_POINT_ID " +
		" FROM GALADM.gal131tbx a,GALADM.GAL185TBX AS b, GALADM.GAL212TBX AS c" +  
		" WHERE (a.LAST_PASSING_PROCESS_POINT_ID =?1 or a.LAST_PASSING_PROCESS_POINT_ID =?2)"+ 
		"	and a.PRODUCT_ID = b.PRODUCT_ID and b.PART_NAME='BLOCK MC' and a.PRODUCTION_LOT = c.PRODUCTION_LOT "+
		"ORDER BY a.LAST_PASSING_PROCESS_POINT_ID asc, a.UPDATE_TIMESTAMP desc";
	
	private static final String FIND_VALID_EINS =
        "SELECT a.PRODUCT_ID, b.MODEL_YEAR_CODE, b.MODEL_CODE, b.MODEL_TYPE_CODE, b.MODEL_OPTION_CODE, a.CREATE_TIMESTAMP " +
        "FROM GALADM.GAL131TBX a, GALADM.GAL133TBX b " + 
        "WHERE a.PRODUCT_SPEC_CODE = b.PRODUCT_SPEC_CODE " +
        "AND a.TRACKING_STATUS IS NULL " +
        "ORDER BY a.CREATE_TIMESTAMP DESC";
	
	private static final String FIND_AVAIL_EINS_BY_MTOC = 
			"SELECT PRODUCT_ID FROM GAL131TBX WHERE VIN IS NULL AND PLANT_CODE = ?1 AND PRODUCT_SPEC_CODE = ?2";

	private static final String FIND_NEXT_ENGINE = 
		"(SELECT PRODUCT_ID FROM GALADM.GAL131TBX " +
			"WHERE PRODUCTION_LOT = " +
				"(SELECT PRODUCTION_LOT FROM GALADM.GAL131TBX " +
					"WHERE PRODUCT_ID =?1) AND PRODUCT_ID > ?1)" +
		"UNION " +
		"(SELECT PRODUCT_ID FROM GALADM.GAL131TBX " +
			"WHERE PRODUCTION_LOT = " +
				"(SELECT NEXT_PRODUCTION_LOT FROM GALADM.GAL212TBX " +
				"WHERE PRODUCTION_LOT = " +
					"(SELECT PRODUCTION_LOT FROM GALADM.GAL131TBX WHERE PRODUCT_ID = ?1)) "+
				" AND (SELECT COUNT(*) FROM GALADM.GAL131TBX WHERE PRODUCTION_LOT = " +
					"(SELECT PRODUCTION_LOT FROM GALADM.GAL131TBX WHERE PRODUCT_ID =?1) AND PRODUCT_ID > ?1) = 0 )" +
		"ORDER BY PRODUCT_ID ASC FETCH FIRST 1 ROWS ONLY WITH UR"; 
	
	private static final String FIND_MTOCS = 
		"SELECT DISTINCT a.PRODUCT_SPEC_CODE FROM GALADM.GAL131TBX A,GALADM.GTS_CARRIER_TBX b " + 
		"WHERE b.TRACKING_AREA = ?1 and a.PRODUCT_ID = b.PRODUCT_ID " +
		"ORDER BY PRODUCT_SPEC_CODE";


	private static final String SELECT_SALES_WARRANTY_FR_ENG = 
		"select engine.product_id as EIN \n" + 
		",engine.actual_timestamp as AE_OFF_ACTUAL_TIMESTAMP, nvl(to_char(engine.actual_timestamp,'yyyyMMdd'),'00000000') as AE_OFF_DATE, nvl(to_char(engine.actual_timestamp,'HH24MISS'),'000000') as AE_OFF_TIME \n" + 
		",block.BLOCK_NO, head.HEAD_NO, transmission.MISSION_SERIAL_NO \n" +
		"from ( \n" +
		"select min(h.actual_timestamp) as actual_timestamp, e.product_id \n" + 
		"from gal131tbx e  \n" +
		"left join gal215tbx h on h.product_id = e.product_id and h.process_point_id = ?1 \n" +
		"where e.product_id in (:productId) group by e.product_id \n" +
		") engine  \n" +
		"left join (select parts.part_serial_number as block_no, parts.product_id from gal185tbx parts where parts.part_name = ?2 ) block on block.product_id = engine.product_id \n" +
		"left join (select parts.part_serial_number as head_no, parts.product_id from gal185tbx parts where parts.part_name = ?3) head on head.product_id = engine.product_id \n" +
		"left join (select parts.part_serial_number as mission_serial_no ,parts.product_id from gal185tbx parts where parts.part_name = ?4) transmission on transmission.product_id = engine.product_id \n" +
		"for read only";

	private static final String UPDATE_LAST_PASSING_PROCESS_POINT_ID_BY_PRODUCT_IDS = "UPDATE GALADM.GAL131TBX set LAST_PASSING_PROCESS_POINT_ID = ?1 " 
		+ "WHERE product_id IN "; ;
		
	private static final String FIND_ALL_PLANT_CODES = 
		"SELECT DISTINCT(PLANT_CODE) FROM GALADM.GAL131TBX";
	
	private static final String FIND_VALID_PRODUCT_FOR_PROCESS_POINT = "SELECT A.* FROM GAL131TBX A WHERE A.PRODUCT_ID = ?1 AND A.TRACKING_STATUS IN (" +
			"SELECT B.PREVIOUS_LINE_ID FROM GAL236TBX B WHERE B.LINE_ID = (" +
			"SELECT C.LINE_ID FROM GAL195TBX C WHERE EXISTS (" +
			"SELECT 1 FROM GAL214TBX D WHERE C.LINE_ID = D.LINE_ID AND D.PROCESS_POINT_ID = ?2 " +
			"FETCH FIRST ROW ONLY) FETCH FIRST ROW ONLY)) FETCH FIRST ROW ONLY";
	
	public List<Engine> findAllByProductSequence(String processPointId, String currentProductId,
			int processedSize, int upcomingSize) {
		
		Parameters params = Parameters.with("1", processPointId);
		
		List<Engine> engines = new ArrayList<Engine>();
		
		List<Engine> allEngines  = findAllByNativeQuery(FIND_PRODUCTS_BY_PRODUCT_SEQUENCE, params);
		
		int index = -1;
		for(int i = 0;i< allEngines.size();i++) {
			if(allEngines.get(i).getProductId().equals(currentProductId)) {
				index = i;
				break;
			}
		}
		
		if (index == -1) return engines;
		
		int start = (index - processedSize < 0) ? 0 :index - processedSize;
		int end = (index + upcomingSize >=allEngines.size()) ? allEngines.size() -1 : index + upcomingSize;
		
		for(int i=start;i<=end;i++)
			engines.add(allEngines.get(i));
		
		return engines;

	}




	public List<Engine> findAllByProductionLot(String productionLot) {
		
		return findAll(Parameters.with("productionLot", productionLot));
		
	}

	public List<Engine> findAllByLineIds(String[] lineIds) {
		
		return findAllByQuery(FIND_ENGINES_BY_LINE_IDS + convert(lineIds));
		
	}
	
	private String convert(String[] lineIds) {
		String lines = "(";
		boolean isFirst = true;
		for(String item : lineIds) {
			if(isFirst) isFirst = false;
			else lines += ",";
			lines += "'" + item +"'";
		}
		lines += ")";
		return lines;
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateFiringFlag(String productId, boolean engineFiringFlag) {
		update(Parameters.with("engineFiringFlag", engineFiringFlag), Parameters.with("productId", productId));
	}
	
	@Transactional
	public void updateEngineFiringFlag(String productId, short flag){
		update(Parameters.with("engineFiringFlag", flag), Parameters.with("productId", productId));
	}

	public List<Engine> findAllByInProcessProduct(String currentProductId,
			int processedSize, int upcomingSize) {
		List<Engine> products = new ArrayList<Engine>();
		List<Engine> processedProducts  = findProcessedProducts(currentProductId, processedSize);
		// reverse the list and removed the current product id since it is in the upcomingEngines list
		for(int i = processedProducts.size() - 1; i >= 1; i--)
			products.add(processedProducts.get(i));
		List<Engine> upcomingProducts  = findUpcomingProducts(currentProductId, upcomingSize);
		for(Engine product : upcomingProducts) products.add(product);
		return products;
	}
	
	private List<Engine> findProcessedProducts(String currentProductId, int processedSize) {
		Parameters params = Parameters.with("1", currentProductId);
		return findAllByNativeQuery(
				FIND_PROCESSED_PRODUCTS_BY_IN_PROCESS_PRODUCT + getOrderBy(processedSize + 1), params);
	}

	private List<Engine> findUpcomingProducts(String currentProductId, int upcomingSize) {
		Parameters params = Parameters.with("1", currentProductId);
		return findAllByNativeQuery(
				FIND_UPCOMING_PRODUCTS_BY_IN_PROCESS_PRODUCT + getOrderBy(upcomingSize + 1), params);
	}
	
	public List<InventoryCount> findAllInventoryCounts() {
		List<?> results =  findResultListByNativeQuery(FIND_INVENTORY_COUNTS, null);
		List<?> holdResults =  findResultListByNativeQuery(FIND_HOLD_COUNTS, null);
		return toInventoryCounts(results, holdResults);
	}
	
	private String getOrderBy(int size) {
		return ORDER_BY + size + ROWS_ONLY;
	}

	public List<Engine> findByTrackingStatus(String trackingStatus) {
		
		return findAll(Parameters.with("trackingStatus", trackingStatus));
	}

	public List<Engine> findByPartName(String lineId, int prePrintQty,
			int maxPrintCycle, String ppid, String partName) {
		Parameters parameters = Parameters.with("1", lineId.trim());
		parameters.put("2", prePrintQty);
		parameters.put("3", maxPrintCycle);
		parameters.put("4", ppid.trim());
		parameters.put("5", partName.trim());
		
		return findAllByNativeQuery(FIND_ELIGIBLE_PRODUCTS, parameters );
	}

	@Override
	public List<Object[]> getProductsWithinRange(String startProductId,String stopProductId) {
		String sql ="SELECT T1.Product_ID, T1.Product_Spec_Code,char(T1.Production_Date), T1.Production_Lot FROM gal131tbx T1 ";
		startProductId = StringUtils.trim(startProductId);
		stopProductId = StringUtils.trim(stopProductId);
		if (StringUtils.equals(startProductId, stopProductId)) {
			sql = sql + " where product_id = '" + startProductId + "'";
		} else {
			sql = sql + "WHERE  SUBSTR(T1.Product_ID, 9, 4) >= '"+startProductId.substring(8, 12)+"' " +
				"and SUBSTR(T1.Product_ID, 9,4) <= '"+stopProductId.substring(8, 12)+"' " +
				"AND SUBSTR(T1.Product_ID, 1,8) = '"+startProductId.substring(0, 8)+"' " +
				"ORDER BY SUBSTR(T1.Product_ID, 9,4)";
		}
		return findAllByNativeQuery(sql, null, Object[].class);
	}

	public List<Engine> findAllByMissionSn(String msn) {
		Parameters params = Parameters.with("missionSerialNo", msn );
		return findAll(params);
	}
	
	
	public List<Object[]> getEngineLineShipmentStatusData(String productionLot,String scrapLineId,String exceptionalLineId)
	{
		Parameters params = Parameters.with("1", productionLot).put("2", scrapLineId).put("3", exceptionalLineId);
		return	executeNative(params,GET_ENGINE_LINE_SHIPMENT_STATUS_DATA);
	}

	public List<Engine> findEnginesAndBeginCureTimeByLineIdsAndPartName(String[] lineIds, String partName) {
		List<Engine> elist = new ArrayList<Engine>();
		
		StringBuffer execSql = new StringBuffer();
		execSql.append(FIND_ENGINES_AND_BEGIN_CURETIME_BY_LINE_IDS).append(convert(lineIds)).append(" order by t185.ACTUAL_TIMESTAMP desc ");
		
		Parameters params = Parameters.with("1", partName);
		List<Object[]> objsResultList = findAllByNativeQuery(execSql.toString(), params, Object[].class);
		if(!objsResultList.isEmpty()){
			for(Object[] objs : objsResultList){
				Engine engine = new Engine();
				engine.setProductId((String)objs[0]);
				engine.setKdLotNumber((String)objs[1]);
				engine.setCureTimeBegin((Timestamp)objs[2]);
				engine.setTrackingStatus((String)objs[3]);
				elist.add(engine);
			}
		}
		return elist;
	}

	@Transactional
	public void updateTrackStatusByProductIds(String[] productIds, String updatedTrackStatus) {
		Parameters params = Parameters.with("1", updatedTrackStatus);
		executeNativeUpdate(UPDATE_TRACK_STATUS_BY_PRODUCT_IDS + convert(productIds), params);
	}
	
	@Transactional
	public void updateLastPassingProcessPointIdByProductIds(String[] productIds, String lastPassingProcessPontId) {
		Parameters params = Parameters.with("1", lastPassingProcessPontId);
		executeNativeUpdate(UPDATE_LAST_PASSING_PROCESS_POINT_ID_BY_PRODUCT_IDS + convert(productIds), params);
	}

	public List<EngineNumberingDto> findAllRecentStampedEngines(String onProcessPointId, String nextProcessPointId) {
		Parameters params = Parameters.with("1",onProcessPointId).put("2", nextProcessPointId);
		return findAllByNativeQuery(FIND_ENGINES_RECENTLY_STAMPED,params, EngineNumberingDto.class);
//		List<Object[]> results = findAllByNativeQuery(FIND_ENGINES_RECENTLY_STAMPED,params, Object[].class);
//		List<EngineNumbering> engineNumbers = new ArrayList<EngineNumbering>();
//		
//		for(Object[] object : results) {
//			EngineNumbering engine = new EngineNumbering();
//			engine.setProductId((String)object[0]);
//			engine.setProductSpecCode((String)object[1]);
//			engine.setMcNumber((String)object[2]);
//			engine.setLotSize((Integer)object[3]);
//			engine.setProductionLot((String)object[4]);
//			engine.setStartProductId((String)object[5]);
//			engine.setProcessPointId((String)object[6]);
//			engineNumbers.add(engine);
//		}
//		return engineNumbers;
		
	}

	/**
	 * find future engine numbers from Engine table 
     * @param startTime
     * @return List<Object[]>
     */
	@Transactional
    public List<Object[]> findValidEins() {
    	return findAllByNativeQuery(FIND_VALID_EINS, null, Object[].class);
    }

    
	public Engine findNextProduct(String productId) {
		Parameters params = Parameters.with("1", productId);
		return findFirstByNativeQuery(FIND_NEXT_ENGINE, params);
	}
	
	public List<String> findAllEngineMTOCInTrackingArea(String trackingArea){
		Parameters params = Parameters.with("1", trackingArea);
		return findAllByNativeQuery(FIND_MTOCS, params,String.class);
	}
    
	public List<Map<String, Object>> findAllSalesWarrantyFrData(List<String>  eins, String processPointId, String blockPartName, String headPartName, String transmissionPartName) {
		String sql = SELECT_SALES_WARRANTY_FR_ENG;
		sql = sql.replace(":productId", StringUtil.toSqlInString(eins));
		Parameters params = Parameters.with("1", processPointId).put("2", blockPartName).put("3", headPartName).put("4", transmissionPartName);
		List<Map<String, Object>> result = findResultMapByNativeQuery(sql, params);
		return result;
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public Engine saveEngine(Engine engine) {
		Engine e = save(engine);
		return e;
	}


	/**
	 * find number of available engines to be installed for given MTOC
     * @param Plantcode
     * @param ProductSpecCode
     * @return List<String>
     */

	public List<String> findAvailEnginesByMTOC(String plantCode,
			String productSpecCode) {
		Parameters params = Parameters.with("1", plantCode).put("2", productSpecCode);
    	return findAllByNativeQuery(FIND_AVAIL_EINS_BY_MTOC, params, String.class);
    
	}

	public Engine findEngineByVin(String vin) {
		Parameters params = Parameters.with("vin", vin );
		return findFirst(params);
	}

	public List<String> findAllEnginePlantCodes() {
		return findAllByNativeQuery(FIND_ALL_PLANT_CODES, null, String.class);
	}
	
	@Override
	public boolean isProductTrackingStatusValidForProcessPoint(String productId, String processPointId) {
		Parameters params = Parameters.with("1", productId).put("2", processPointId);
		Engine result = findFirstByNativeQuery(FIND_VALID_PRODUCT_FOR_PROCESS_POINT, params);
		return result != null;
	}
	
}
