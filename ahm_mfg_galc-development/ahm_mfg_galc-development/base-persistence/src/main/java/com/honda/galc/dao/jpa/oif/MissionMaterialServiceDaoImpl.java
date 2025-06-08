package com.honda.galc.dao.jpa.oif;

import java.util.Date;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.oif.MissionMaterialServiceDao;
import com.honda.galc.entity.oif.MaterialServiceId;
import com.honda.galc.entity.oif.MaterialService;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3>
 * Material Service Impl.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Daniel Garcia</TD>
 * <TD>Nov. 19, 2014</TD>
 * <TD>1.0</TD>
 * <TD></TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class MissionMaterialServiceDaoImpl extends BaseDaoImpl<MaterialService, MaterialServiceId>  implements MissionMaterialServiceDao {

	
	
	//Query to select all records in MS_PMX_TBX and after we'll delete from MA_PMX_TBX
	private static final String selectOldRecordsPmx = new StringBuilder()
		.append("SELECT * FROM MS_PMX_TBX mspxm ")
		.append("	WHERE (mspxm.LOT_SIZE, mspxm.PRODUCTION_LOT, mspxm.PROCESS_POINT_ID) IN ")
		.append("		(	SELECT LOTS.COMPLETED, LOTS.PRODUCTION_LOT, LOTS.PROCESS_POINT_ID " )
		.append("				FROM ( SELECT COUNT(*) AS COMPLETED")
		.append("							,ms.PRODUCTION_LOT, ms.PROCESS_POINT_ID, MAX(ms.ACTUAL_TIMESTAMP) AS ACTUAL_TIMESTAMP") 
		.append("						FROM MS_PMX_TBX ms ")
		.append("						GROUP BY ms.PRODUCTION_LOT, ms.PROCESS_POINT_ID")
		.append("					) LOTS") 
		.append("					WHERE") 
		.append("					LOTS.ACTUAL_TIMESTAMP < ?1")
		.append("		 )").toString();
	
	//Query to return all Vins that will send to gccs	
	private static final String getTransmissionMSPriorityPlanSchedule = new StringBuilder()
		.append("SELECT ")
		.append("	HIST.PRODUCT_ID, ")
		.append("	RTRIM( PRODLOT.PLAN_CODE ) AS PLAN_CODE, ")
		.append("	PRODLOT.LINE_NO, ")
		.append("	RTRIM( HIST.PROCESS_POINT_ID ) AS PROCESS_POINT_ID, ")
		.append("	PRODLOT.PRODUCTION_DATE, ")
		.append("	MIN( HIST.ACTUAL_TIMESTAMP ) AS ACTUAL_TIMESTAMP, ")
		.append("	RTRIM( HIST.PRODUCT_SPEC_CODE ) AS PRODUCT_SPEC_CODE, ")
		.append("	LPAD( ")
		.append("		SUM( KDINFO.LOT_SIZE ), ")
		.append("		5, ")
		.append("		'0' ")
		.append("	) AS LOT_SIZE, ")
		.append("	LPAD( ")
		.append("		ROW_NUMBER() OVER(), ")
		.append("		3, ")
		.append("		'0' ")
		.append("	) AS ON_SEQ_NO, ")
		.append("	SUBSTR( ")
		.append("		PRODLOT.PROD_LOT_KD, ")
		.append("		9, ")
		.append("		12 ")
		.append("	) AS PRODUCTION_LOT, ")
		.append("	PRODLOT.KD_LOT_NUMBER, ")
		.append("	PRODLOT.PLAN_OFF_DATE, ")
		.append("	TO_CHAR( ")
		.append("		CURRENT TIMESTAMP, ")
		.append("		'yyyyMMddHHmmss' ")
		.append("	) AS CURRENT_TIMESTAMP, ")
		.append("	'Y' AS SENT_FLAG, ")
		.append("	PREPL.MBPN ")
		.append("FROM ")
		.append("	GAL217TBX PRODLOT, ")
		.append("	GAL215TBX HIST, ")
		.append("	GAL212TBX PREPL, ")
		.append("	( ")
		.append("		SELECT ")
		.append("			KDLOT.KD_LOT_NUMBER, ")
		.append("			SUM( KDLOT.LOT_SIZE ) AS LOT_SIZE ")
		.append("		FROM ")
		.append("			GAL217TBX KDLOT ")
		.append("		GROUP BY ")
		.append("			KDLOT.KD_LOT_NUMBER ")
		.append("	) KDINFO ")
		.append("WHERE ")
		.append("	HIST.PRODUCTION_LOT = PRODLOT.PRODUCTION_LOT ")
		.append("	AND HIST.ACTUAL_TIMESTAMP >= ?1 ")
		.append("	AND HIST.PROCESS_POINT_ID IN(@processPoint@) ")
		.append("	AND PRODLOT.PLANT_CODE = ?2 ")
		.append("	AND PREPL.PRODUCTION_LOT = PRODLOT.PRODUCTION_LOT ")
		.append("	AND KDINFO.KD_LOT_NUMBER = PRODLOT.KD_LOT_NUMBER ")
		.append("	AND NOT EXISTS( ")
		.append("		SELECT ")
		.append("			PRODUCT_ID, ")
		.append("			PROCESS_POINT_ID, ")
		.append("			ACTUAL_TIMESTAMP ")
		.append("		FROM ")
		.append("			GALADM.MS_PMX_TBX OSL_TBX ")
		.append("		WHERE ")
		.append("			OSL_TBX.PRODUCT_ID = HIST.PRODUCT_ID ")
		.append("			AND OSL_TBX.PROCESS_POINT_ID = HIST.PROCESS_POINT_ID ")
		.append("			AND OSL_TBX.ACTUAL_TIMESTAMP = HIST.ACTUAL_TIMESTAMP ")
		.append("	) ")
		.append("GROUP BY ")
		.append("	HIST.PRODUCT_ID, ")
		.append("	PRODLOT.PLAN_CODE, ")
		.append("	PRODLOT.LINE_NO, ")
		.append("	HIST.PROCESS_POINT_ID, ")
		.append("	PRODLOT.PRODUCTION_DATE, ")
		.append("	HIST.ACTUAL_TIMESTAMP, ")
		.append("	HIST.PRODUCT_SPEC_CODE, ")
		.append("	PRODLOT.PROD_LOT_KD, ")
		.append("	PRODLOT.KD_LOT_NUMBER, ")
		.append("	PRODLOT.PLAN_OFF_DATE, ")
		.append("	PREPL.MBPN ")
		.toString();
	

	final String getTransmissionMSInHouse = new StringBuilder()
		.append("SELECT   ")
		.append("	t215.PRODUCT_ID, ")  
		.append("	RTRIM(productionLot.PLAN_CODE) as PLAN_CODE, ")  
		.append("	productionLot.LINE_NO,   ")
		.append("	RTRIM(t215.PROCESS_POINT_ID) as PROCESS_POINT_ID, ")  
		.append("	productionLot.PRODUCTION_DATE ,  ")
		.append("	t215.ACTUAL_TIMESTAMP, ")
		.append("	RTRIM(t215.PRODUCT_SPEC_CODE) as PRODUCT_SPEC_CODE , ")  
		.append("	LPAD(SPLIT_LOTS.LOT_SIZE,5,'0') as LOT_SIZE, ")
		.append("	LPAD(COUNT(RESULT.PRODUCT_ID),3,'0') as ON_SEQ_NO, ")
		.append("	SUBSTR( productionLot.PROD_LOT_KD,9 ,12 ) as PRODUCTION_LOT, ")  
		.append("	productionLot.KD_LOT_NUMBER,   ")
		.append("	productionLot.PLAN_OFF_DATE ,  ")
		.append("	to_char(CURRENT TIMESTAMP, 'YYYYMMDDHH24MISS') as CURRENT_TIMESTAMP, ")						
		.append("	'Y' as SENT_FLAG, ")
		.append("	mbpn.MBPN			 ")		  					  
		.append("FROM ")
		.append("	GALADM.GAL217TBX productionLot,  GAL212TBX mbpn, ORDER_TBX order_tbx, ")
		.append("	( ")
		.append("		SELECT ")   
		.append("			history.PRODUCT_ID, ")    
		.append("			history.PROCESS_POINT_ID, ")  
		.append("			min(history.ACTUAL_TIMESTAMP) as ACTUAL_TIMESTAMP, ")
		.append("			history.PRODUCT_SPEC_CODE, ")
		.append("			history.PRODUCTION_LOT ")
		.append("		FROM  GALADM.GAL215TBX history ")
		.append("		GROUP BY history.PRODUCT_ID, ")
		.append("			history.PROCESS_POINT_ID, ")
		.append("			history.PRODUCT_SPEC_CODE, ")
		.append("			history.PRODUCTION_LOT ")
		.append("	)t215, ")
		.append("	( ")
		.append("		select ")
		.append("			order_tbx.PRIORITY_SEQ ")
		.append("	        ,order_tbx.PROD_ORDER_QTY as lot_size ")
		.append("		from ORDER_TBX order_tbx ")
		.append("		group by order_tbx.PRIORITY_SEQ, order_tbx.PROD_ORDER_QTY ")
		.append("	)SPLIT_LOTS, ")
		.append("	(            ")
		.append("		select ")
		.append("			min(PREV_RESULTS.ACTUAL_TIMESTAMP) as ACTUAL_TIMESTAMP ")					          
		.append("			,order_tbx.PRIORITY_SEQ ")
		.append("			,PREV_RESULTS.PROCESS_POINT_ID ")
		.append("			,PREV_RESULTS.PRODUCT_ID ")
		.append("			,SUBSTR(PREV_RESULTS.PRODUCTION_LOT, 11, 10) ")
		.append("		FROM GALADM.GAL215TBX PREV_RESULTS ")
		.append("			, ORDER_TBX order_tbx					 ")    	
		.append("		WHERE SUBSTR(PREV_RESULTS.PRODUCTION_LOT, 11, 10) = CAST(order_tbx.PRIORITY_SEQ as NVARCHAR(10)) ") 
		.append("		GROUP BY order_tbx.PRIORITY_SEQ  ")
		.append("			,PREV_RESULTS.PROCESS_POINT_ID ")
		.append("			,PREV_RESULTS.PRODUCT_ID ")
		.append("			,SUBSTR(PREV_RESULTS.PRODUCTION_LOT, 11, 10) ")
		.append("	)RESULT ")
		.append("	WHERE ")   
		.append("		t215.PRODUCTION_LOT=productionLot.PRODUCTION_LOT  AND ")  
		.append("		mbpn.PRODUCT_SPEC_CODE = productionLot.PRODUCT_SPEC_CODE and ")
		.append("		order_tbx.PRIORITY_SEQ = productionLot.LOT_NUMBER and ")
		.append("		productionLot.LOT_NUMBER = SPLIT_LOTS.PRIORITY_SEQ AND ")
		.append("		RESULT.PRIORITY_SEQ = productionLot.LOT_NUMBER AND ")
		.append("		RESULT.PROCESS_POINT_ID = t215.PROCESS_POINT_ID AND ")
		.append("		RESULT.ACTUAL_TIMESTAMP <= t215.ACTUAL_TIMESTAMP ")
		.append("		AND t215.ACTUAL_TIMESTAMP >= ?1 ")
		.append("		AND t215.PROCESS_POINT_ID IN (@processPoint@) ")  
		.append("		AND productionLot.PLANT_CODE= ?2 ")
		.append("		AND NOT EXISTS (  ")
		.append("			SELECT   ")
		.append("				PRODUCT_ID  ")
		.append("				PLAN_CODE,  ")
		.append("				LINE_NO,  ")
		.append("				PROCESS_POINT_ID, ") 
		.append("				PRODUCTION_DATE,  	 ")						
		.append("				PRODUCT_SPEC_CODE,   ")
		.append("				LOT_SIZE, ")  
		.append("				ON_SEQ_NO,   ")
		.append("				PRODUCTION_LOT,   ")
		.append("				KD_LOT_NUMBER,   ")
		.append("				PLAN_OFF_DATE  ")
		.append("			FROM GALADM.MS_PMX_TBX OSL_TBX ") 
		.append("			WHERE OSL_TBX.PRODUCT_ID = t215.PRODUCT_ID AND ") 
		.append("				OSL_TBX.PROCESS_POINT_ID = t215.PROCESS_POINT_ID AND ") 
		.append("				OSL_TBX.ACTUAL_TIMESTAMP = t215.ACTUAL_TIMESTAMP  ")
		.append("		)  ")
		.append("	GROUP BY t215.PRODUCT_ID, ")  
		.append("		productionLot.PLAN_CODE,   ")
		.append("		productionLot.LINE_NO,   ")
		.append("		t215.PROCESS_POINT_ID,   ")
		.append("		productionLot.PRODUCTION_DATE , ") 
		.append("		t215.ACTUAL_TIMESTAMP,  ")
		.append("		t215.PRODUCT_SPEC_CODE ,   ")
		.append("		productionLot.PROD_LOT_KD,   ")
		.append("		productionLot.KD_LOT_NUMBER,   ")
		.append("		productionLot.PLAN_OFF_DATE, ")
		.append("		SPLIT_LOTS.LOT_SIZE, ")
		.append("		mbpn.MBPN ")
		.toString();
	
	
	/**
	 * Execute the query to get all old records in MS_PMX_TBX
	 * @param  dateBefore		-- the date used to get records register before of that date
	 * @return list of all old records
	 */
	public List<MaterialService> selectOldRecordMaterialService(Date dateBefore) {
		final Parameters parameters = Parameters.with("1", dateBefore);	
		return findAllByNativeQuery(selectOldRecordsPmx, parameters);
	}
	
	//@Transactional(propagation = Propagation.REQUIRES_NEW)
	/**
	 * Execute the query to get all records that will send to material service with plan in house
	 * @param dateBefore		-- the date used to get records register after of that date
	 * @param processPoint		-- this is a filter to get only the records in that process point
	 * @param plantCode			-- this is the plant code
	 * @return list of all records that will save in MaterialService
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getTransmissionMaterialServiceInHouseSchedule(
			Date dateBefore, String processPoint, String plantCode) {
		final Parameters parameters = Parameters.with("1", dateBefore);		
		parameters.put("2", plantCode);		
		
		StringBuilder listProcessPoints = new StringBuilder()
		.append("\'")
		.append( processPoint.replaceAll(",", "\',\'") )		
		.append("\'");		
		return findResultListByNativeQuery(getTransmissionMSInHouse.replaceAll("@processPoint@", listProcessPoints.toString()), parameters);
	}

	/**
	 * Execute the query to get all records that will send to material service with priority plan schedule
	 * @param dateBefore		-- the date used to get records register after of that date
	 * @param processPoint		-- this is a filter to get only the records in that process point
	 * @param plantCode			-- this is the plant code
	 * @return list of all records that will save in MaterialService
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getTransmissionMaterialServicePriorityPlanSchedule(
			Date dateBefore, String processPoint, String plantCode) {
		final Parameters parameters = Parameters.with("1", dateBefore);		
		parameters.put("2", plantCode);		
		
		StringBuilder listProcessPoints = new StringBuilder()
		.append("\'")
		.append( processPoint.replaceAll(",", "\',\'") )		
		.append("\'");
		
		return findResultListByNativeQuery(getTransmissionMSPriorityPlanSchedule.replaceAll("@processPoint@", listProcessPoints.toString()), parameters);
	}
		
}
