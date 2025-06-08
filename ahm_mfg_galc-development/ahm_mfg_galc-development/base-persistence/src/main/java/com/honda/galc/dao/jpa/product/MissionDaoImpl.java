package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.product.MissionDao;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.entity.product.Mission;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3>
 * Mission DAO Impl.
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
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 2, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140902</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class MissionDaoImpl extends ProductDaoImpl<Mission> implements MissionDao {

	private static final String FIND_INVENTORY_COUNTS =
		"SELECT COUNT(*) as COUNT,LAST_PASSING_PROCESS_POINT_ID as PROCESS, SUBSTR(KD_LOT_NUMBER,1,6) as PLANT " + 
		"FROM GALADM.MISSION_TBX " +  
		"WHERE (DEFECT_STATUS IS NULL or DEFECT_STATUS < 3) AND AUTO_HOLD_STATUS <> 1 " + 
		"GROUP BY LAST_PASSING_PROCESS_POINT_ID,SUBSTR(KD_LOT_NUMBER,1,6) " + 
		"ORDER BY PROCESS,PLANT";
	
	//Query to get the necessary data to Warranty Interface
	private final static String transmissionWarrantyQuery = new StringBuilder()
	.append( "	select													" )
	.append( "	      mission.product_id								" )
	//change the date format to yyyy-MM-dd
	.append( "	      ,nvl(to_char(miss_off.actual_timestamp, 'yyyy-MM-dd'), '          ') as trans_build_date	" )
	.append( "	      ,nvl(to_char(miss_off.actual_timestamp, 'HH24MISS' ),'000000' ) as trans_build_time		" )
	.append( "	      ,productionLot.line_no																	" )
	.append( "	      ,productionLot.plant_code																	" )
	//.append( "	      ,'01' as shift																			" )
	.append( "		  ,(																						" )
    .append( "			select distinct sched.SHIFT																" )
    .append( "			from gal226tbx sched																	" )
    .append( "			where sched.START_TIMESTAMP <= miss_off.ACTUAL_TIMESTAMP and							" )
    .append( "			sched.END_TIMESTAMP >= miss_off.ACTUAL_TIMESTAMP and									" )
    .append( "			sched.PRODUCTION_DATE = date(miss_off.ACTUAL_TIMESTAMP) and								" )
    .append( "			sched.PROCESS_LOCATION = 'AM'															" )
    .append( "		  ) as shift																				" )
	.append( "	      ,spec_code.model_code as team_cd															" )
	.append( "	      ,spec_code.model_type_code as type_cd														" )
	.append( "	      ,miss_case_lot.production_lot as cast_lot_no												" )
	.append( "	      ,miss_torque_lot.production_lot as mach_lot_no											" )
	.append( "	      ,miss_torque_lot.production_lot as torque_cast_lot_no										" )
	.append( "	      ,miss_torque_lot.production_lot as torque_mach_lot_no										" )
	.append( "	      ,productionLot.kd_lot_number																" )
	.append( "	      ,productionLot.prod_lot_kd																" )
	.append( "	from 																							" )
	.append( "		  MISSION_TBX mission																		" )
	.append( "	  join GAL217TBX productionLot																	" )
	.append( "		  on productionLot.production_lot = mission.production_lot									" )
	.append( "	  join MISSION_SPEC_CODE_TBX spec_code															" )
	.append( "		  on spec_code.product_spec_code = mission.product_spec_code								" )
	.append( "	  join																							" )
	.append( "		  ( 																						" )
	.append( "		    SELECT distinct gal215.product_id														" )
	.append( "		    FROM GAL215TBX gal215																	" )
	.append( "		    JOIN MISSION_TBX mission ON gal215.PRODUCT_ID=mission.PRODUCT_ID 						" )
	.append( "		    WHERE gal215.process_point_id in ( :PROCESS_POINT ) 												" )
	.append( "		    AND mission.PRODUCTION_LOT NOT LIKE 'TEST%' 											" )
	.append( "		    and gal215.actual_timestamp > ?1 														" )
	.append( "		    union																					" )
	.append( "		    SELECT product_id 																		" )
	.append( "		    FROM MISSION_TBX mission 																" )
	.append( "		    WHERE TRACKING_STATUS IN ( :TRACKING_STATUS ) 														" )
	.append( "		    AND PRODUCTION_LOT NOT LIKE 'TEST%' 													" )
	.append( "		    and update_timestamp > ?2																" )
	.append( "		  )miss																						" )
	.append( "		on miss.product_id = mission.product_id														" )
	.append( "	  left join (																					" )
	.append( "			    SELECT min(prod_result.actual_timestamp) as actual_timestamp						" )
	.append( "			          ,prod_result.product_id														" )
	.append( "			    FROM GAL215TBX prod_result 															" )
	.append( "			    WHERE prod_result.process_point_id in ( :PROCESS_POINT	)											" )
	.append( "			    group by prod_result.product_id														" )
	.append( "			  ) miss_off																			" )
	.append( "			on miss_off.PRODUCT_ID = mission.PRODUCT_ID  											" )
	.append( "	  left join (																					" )
	.append( "			   		select																			" )
	.append( "						parts.PART_SERIAL_NUMBER as mission_serial_number							" )
	.append( "						,parts.PRODUCT_ID															" )
	.append( "						,'' as production_lot													 	" )
	.append( "						from GAL185TBX parts														" )
	.append( "					where trim(parts.PART_NAME) like ?3												" )
	.append( "				)miss_case_lot																		" )
	.append( "			on miss_case_lot.product_id = mission.product_id										" )
	.append( "   left join (																					" )
	.append( "			    select																				" )
	.append( "			  		parts.PART_SERIAL_NUMBER as mission_serial_number								" )
	.append( "			  		,parts.PRODUCT_ID																" )
	.append( "			        ,'' as production_lot 															" )
	.append( "				from GAL185TBX parts																" )										
	.append( "				where trim(parts.PART_NAME) like ?4													" )
	.append( "				)miss_torque_lot																	" )
	.append( "		  on miss_torque_lot.product_id = mission.product_id										" )
	.toString();

	private static final String FIND_VALID_PRODUCT_FOR_PROCESS_POINT = "SELECT A.* FROM MISSION_TBX A WHERE A.PRODUCT_ID = ?1 AND A.TRACKING_STATUS IN (" +
			"SELECT B.PREVIOUS_LINE_ID FROM GAL236TBX B WHERE B.LINE_ID = (" +
			"SELECT C.LINE_ID FROM GAL195TBX C WHERE EXISTS (" +
			"SELECT 1 FROM GAL214TBX D WHERE C.LINE_ID = D.LINE_ID AND D.PROCESS_POINT_ID = ?2 " +
			"FETCH FIRST ROW ONLY) FETCH FIRST ROW ONLY)) FETCH FIRST ROW ONLY";

	public List<InventoryCount> findAllInventoryCounts() {
		List<?> results =  findResultListByNativeQuery(FIND_INVENTORY_COUNTS, null);
		return toInventoryCounts(results);
	}

	public List<Mission> findAllByProductionLot(String productionLot) {
		return findAll(Parameters.with("productionLot", productionLot));
	}
	
	/**
	 * Execute the query to get all the necessary data to Warranty System
	 * @param tmPPOff			-- process point for Transmission OFF
	 * @param trackingStatus	-- the Line IDs for the Exceptional or Scrap
	 * @param torquePartName	-- the part name for mission torque case
	 * @param casePartName	 	-- the part name for mission case
	 * @param lastDateFilter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryTransmissionWarranty(	final String	tmPPOff,
													final String	trackingStatus,
													final String	torquePartName, 
													final String	casePartName,
													final String	lastDateFilter)
	{
		//create the parameters for the interface
		final Parameters parameters = Parameters.with("1", lastDateFilter );
		parameters.put( "2", lastDateFilter );
		parameters.put( "3", casePartName	);
		parameters.put( "4", torquePartName	);
		
		String trackingParam	=	StringUtil.toSqlInString( trackingStatus );
		String processPointParam=	StringUtil.toSqlInString( tmPPOff );
		String query			=	transmissionWarrantyQuery.replace( ":TRACKING_STATUS", trackingParam ).replaceAll( ":PROCESS_POINT", processPointParam );
		List<Object[]> result = findResultListByNativeQuery( query, parameters );
		return result;
	}

	@Override
	public boolean isProductTrackingStatusValidForProcessPoint(String productId, String processPointId) {
		Parameters params = Parameters.with("1", productId).put("2", processPointId);
		Mission result = findFirstByNativeQuery(FIND_VALID_PRODUCT_FOR_PROCESS_POINT, params);
		return result != null;
	}
}
