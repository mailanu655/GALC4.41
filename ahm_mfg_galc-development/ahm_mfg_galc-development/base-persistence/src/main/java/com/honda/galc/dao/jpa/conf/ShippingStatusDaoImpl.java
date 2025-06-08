package com.honda.galc.dao.jpa.conf;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

public class ShippingStatusDaoImpl extends BaseDaoImpl<ShippingStatus,String> implements ShippingStatusDao {
	
	final String GET_VIN_WITH_OPEN_STATUS = "SELECT a FROM ShippingStatus a WHERE a.status > 0 AND a.status < 4 AND a.actualTimestamp <= :cutoffTimestamp"; 

	final String GET_VIN_BY_STATUS = "SELECT a FROM ShippingStatus a WHERE a.status IN (@status@) AND a.vin NOT IN (@vinList@) AND a.actualTimestamp <= :cutTime";
	final String GET_VIN_IN_OTHER_STATUS = "SELECT a FROM ShippingStatus a WHERE a.status IN (@status@) AND a.vin IN (@vinList@) AND a.actualTimestamp <= :cutTime";
	final String GET_VIN_COUNT_OPEN_STATUS = "SELECT COUNT(a.VIN) FROM GAL263TBX a WHERE a.status IN (@status@) AND a.actual_Timestamp <= ?1";

	private static final String SELECT_UNINVOICED = 
			"select s from ShippingStatus s"
			+ " where s.status = :status and s.invoiced <> 'Y'";

	private static final String SELECT_UNINVOICED_BY_TS = 
			"select A.* from GAL263TBX A "
			+ " where A.STATUS = ?1 and A.invoiced <> 'Y' "
			+ " and A.VIN in "
			+ " (select B.PRODUCT_ID from GAL215TBX B where B.ACTUAL_TIMESTAMP >= ?2 "
			+ " and B.ACTUAL_TIMESTAMP <= ?3 and B.PROCESS_POINT_ID=?4) ";

	
	private static final String SELECT_VIN_PRICE = 
			"select price from FrameMTOCPriceMasterSpec p, ShippingStatus s, Frame f, FrameSpec c "
			+	" where s.vin = :vin and "
			+	" f.productId = s.vin and "
			+	" :prodDateTs >= p.effectiveDate and "
			+	" and f.productId = s.vin " 
			+	" and f.productSpecCode = p.productSpecCode "
			+	" and p.modelYearCode = c.modelYearCode, "
			+	" and p.modelCode = c.modelCode, "
			+	" and p.modelTypeCode = c.modelTypeCode, "
			+	" and p.modelOptionCode = c.modelOptionCode, "
			+	" and p.extColorCode = c.extColorCode, "
			+	" and p.IntColorCode = c.IntColorCode, "
			+	" order by p.effectiveDate desc fetch first row only";

	private static final String GET_INVOICED_VINS_WITH_DETAILS1 = "SELECT FRAME.PRODUCT_ID, SUBSTR( FRAME.PRODUCT_ID, LENGTH(FRAME.PRODUCT_ID) -5 ) AS ChassisNumber, " +
			" INSTALLED_PART.PART_SERIAL_NUMBER AS ControlBoxSerial, RTRIM(FRAME_SPEC.PLANT_CODE_GPCS) AS Plant, VARCHAR_FORMAT (INSTALLED_PART.CREATE_TIMESTAMP, 'MM/DD/YYYY')  AS ProductionWeek, " +
			" FRAME_SPEC.SALES_MODEL_TYPE_CODE AS DomainName, SUBSTR (FRAME_SPEC.PRODUCT_SPEC_CODE, 1,7) AS VehicleModelCode, FRAME_SPEC.MODEL_YEAR_DESCRIPTION AS VehicleModelYear, " +
			" SHIP_STATUS.UPDATE_TIMESTAMP AS ShippedTimeStamp, INSTALLED_PART.PART_NAME FROM GAL143TBX FRAME " +
			" JOIN GAL144TBX FRAME_SPEC ON (FRAME.PRODUCT_SPEC_CODE = FRAME_SPEC.PRODUCT_SPEC_CODE)  " +
			" JOIN GAL185TBX INSTALLED_PART ON (FRAME.PRODUCT_ID = INSTALLED_PART.PRODUCT_ID AND (";
	
	private static final String GET_INVOICED_VINS_WITH_DETAILS2 = " ))  " +
            " JOIN GAL263TBX SHIP_STATUS ON (FRAME.PRODUCT_ID = SHIP_STATUS.VIN  AND SHIP_STATUS.STATUS=4 ) " +
			" WHERE SHIP_STATUS.UPDATE_TIMESTAMP >= TIMESTAMP_FORMAT(?1,'YYYY/MM/DD HH24:MI:SS')  " +
			" AND SHIP_STATUS.UPDATE_TIMESTAMP < TIMESTAMP_FORMAT(?2,'YYYY/MM/DD HH24:MI:SS') " +
			" ORDER BY SHIP_STATUS.UPDATE_TIMESTAMP";
	
	private static final String GET_INVOICED_VINS_WITH_SUB_PART_DETAILS1 = "SELECT FRAME.PRODUCT_ID, SUBSTR( FRAME.PRODUCT_ID, LENGTH(FRAME.PRODUCT_ID) -5 ) AS ChassisNumber, " +
			" INSTALLED_PART2.PART_SERIAL_NUMBER AS ControlBoxSerial, RTRIM(FRAME_SPEC.PLANT_CODE_GPCS) AS Plant, VARCHAR_FORMAT (INSTALLED_PART1.CREATE_TIMESTAMP, 'MM/DD/YYYY')  AS ProductionWeek, " +
			" FRAME_SPEC.SALES_MODEL_TYPE_CODE AS DomainName, SUBSTR (FRAME_SPEC.PRODUCT_SPEC_CODE, 1,7) AS VehicleModelCode, FRAME_SPEC.MODEL_YEAR_DESCRIPTION AS VehicleModelYear, " +
			" SHIP_STATUS.UPDATE_TIMESTAMP AS ShippedTimeStamp, INSTALLED_PART2.PART_NAME, INSTALLED_PART1.PART_NAME FROM GAL143TBX FRAME " +
			" JOIN GAL144TBX FRAME_SPEC ON (FRAME.PRODUCT_SPEC_CODE = FRAME_SPEC.PRODUCT_SPEC_CODE)  " +
			" JOIN GAL185TBX INSTALLED_PART1 ON (FRAME.PRODUCT_ID = INSTALLED_PART1.PRODUCT_ID AND (" ;
			
			
	private static final String GET_INVOICED_VINS_WITH_SUB_PART_DETAILS2 = " )) JOIN GAL185TBX INSTALLED_PART2 ON (INSTALLED_PART1.PART_SERIAL_NUMBER = INSTALLED_PART2.PRODUCT_ID AND (";
	
	private static final String GET_INVOICED_VINS_WITH_DETAILS_WITH_EXT_REQ1 = "with PRODUCT_ID_LIST as (select VIN as PRODUCT_ID from GAL263TBX SHIP_STATUS where  AND SHIP_STATUS.INVOICED='Y' and SHIP_STATUS.STATUS=4 "
			+ " AND SHIP_STATUS.UPDATE_TIMESTAMP  >= TIMESTAMP_FORMAT(?1,'YYYY/MM/DD HH24:MI:SS') "
			+ " AND SHIP_STATUS.UPDATE_TIMESTAMP  < TIMESTAMP_FORMAT(?2,'YYYY/MM/DD HH24:MI:SS')) "
			+ ", PARTS as  (select PRODUCT_ID, PART_SERIAL_NUMBER, PART_NAME, CREATE_TIMESTAMP from GAL185TBX INSTALLED_SN where product_id <> '' and ( ";
	
	private static final String GET_INVOICED_VINS_WITH_DETAILS_WITH_EXT_REQ2 = 
			 ")) "
			+ " select distinct SECOND_PART.PRODUCT_ID as PRODUCT_ID "
			+ ", SUBSTR( SECOND_PART.PRODUCT_ID, LENGTH(SECOND_PART.PRODUCT_ID) -5 ) AS ChassisNumber "
			+ ", case when SECOND_PART.SUB_PART_1_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_1 "  
			+ "	else case when SECOND_PART.SUB_PART_2_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_2 "
			+ "	else case when SECOND_PART.SUB_PART_3_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_3 "
			+ "	else case when PART_A3.PART_NAME IN (@PART_NAMES@) then PART_A3.PART_NAME "
			+ "	end "
			+ "	end "
			+ "	end "
			+ " end as ControlBoxSerial "
			+ " , RTRIM(FRAME_SPEC.PLANT_CODE_GPCS) AS Plant "
			+ " , VARCHAR_FORMAT (SECOND_PART.CREATE_TIMESTAMP, 'MM/DD/YYYY')  AS ProductionWeek "
			+ " , FRAME_SPEC.SALES_MODEL_TYPE_CODE AS DomainName"
			+ " , SHIP_STATUS.UPDATE_TIMESTAMP AS ShippedTimeStamp "
			+ " , case when SECOND_PART.SUB_PART_1_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_1_NAME " 
			+ "	else case when SECOND_PART.SUB_PART_2_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_2_NAME " 
			+ "	else case when SECOND_PART.SUB_PART_3_NAME IN (@PART_NAMES@) then SECOND_PART.SUB_PART_3_NAME "
			+ "	else case when PART_A3.PART_NAME IN (@PART_NAMES@) then PART_A3.PART_NAME "
			+ "	end "
			+ "	end "
			+ "	end "
			+ " end as PART_NAME "
			+ " , SECOND_PART.CREATE_TIMESTAMP as PRODUCT_ID_TS "
			+ " from(select INITIAL_PART.PRODUCT_ID as PRODUCT_ID, INITIAL_PART.SUB_PART_1 as SUB_PART_1, INITIAL_PART.SUB_PART_1_NAME as SUB_PART_1_NAME, INITIAL_PART.SUB_PART_2 as SUB_PART_2,INITIAL_PART.SUB_PART_2_NAME as SUB_PART_2_NAME, PART_A2.PART_SERIAL_NUMBER as SUB_PART_3, PART_A2.PART_NAME as SUB_PART_3_NAME, INITIAL_PART.CREATE_TIMESTAMP as CREATE_TIMESTAMP "
			+ " from (select PART_07.PRODUCT_ID as PRODUCT_ID, PART_07.PART_SERIAL_NUMBER as SUB_PART_1, PART_07.SUB_PART_1_NAME as SUB_PART_1_NAME, PART_A1.PART_SERIAL_NUMBER as SUB_PART_2, PART_A1.PART_NAME as SUB_PART_2_NAME, PART_07.CREATE_TIMESTAMP as CREATE_TIMESTAMP "
		    + " from (select PRODUCT_ID_LIST.PRODUCT_ID as PRODUCT_ID, PARTS.PART_SERIAL_NUMBER as PART_SERIAL_NUMBER, PARTS.PART_NAME as SUB_PART_1_NAME, PARTS.CREATE_TIMESTAMP as CREATE_TIMESTAMP "
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
		    + " join GAL144tbx FRAME_SPEC on FRAME.PRODUCT_SPEC_CODE = FRAME_SPEC.PRODUCT_SPEC_CODE "
		    + " join GAL263TBX SHIP_STATUS on FRAME.PRODUCT_ID = SHIP_STATUS.VIN "
		    + " where SUB_PART_1 <>  SECOND_PART.PRODUCT_ID "
		    + " and SUB_PART_1 <> '' "
		    + " and (SUB_PART_1_NAME IN(@PART_NAMES@) "
		    + " or SUB_PART_2_NAME IN(@PART_NAMES@) "
		    + " or SUB_PART_3_NAME IN(@PART_NAMES@) "
		    + " or PART_A3.PART_NAME IN(@PART_NAMES@)) "
		    + " order by PRODUCT_ID, ControlBoxSerial, PRODUCT_ID_TS ";
	
	/**
	 * get all records after current date for specified plant
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.NOT_SUPPORTED)
	public List<ShippingStatus> getVinsWithOpenStatus(Timestamp cutoffTimestamp) {
		Parameters parameters = Parameters.with("cutoffTimestamp", cutoffTimestamp);	
		return findAllByQuery(GET_VIN_WITH_OPEN_STATUS, parameters);
	}
	
	/**
	 * get matched records
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.NOT_SUPPORTED)
	public List<ShippingStatus> getSelectedVins(List<String> vins) {
		List<ShippingStatus> result = new ArrayList<ShippingStatus>(); 
		for(String vin : vins) {
			ShippingStatus status = findByKey(vin);
			if(status != null) {
				result.add(status);
			}
		}
		return result;
	}

	/**
	 * get factory returns
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.NOT_SUPPORTED)
	public List<ShippingStatus> getfactoryReturns() {
		return findAll(Parameters.with("status", -1));
	}

	public List<ShippingStatus> getVinByStatus(String status, List<String> vin, Date dateTime) {
		Parameters parameters = Parameters.with("cutTime", dateTime);		
		return findAllByQuery(GET_VIN_BY_STATUS.replace("@vinList@", StringUtil.toSqlInString(vin)).replace("@status@", status), parameters);		
	}
	
	public List<ShippingStatus> getVinInOtherStatus(String status, List<String> vin, Date dateTime) {
		Parameters parameters = Parameters.with("cutTime", dateTime);
		return findAllByQuery(GET_VIN_IN_OTHER_STATUS.replace("@vinList@", StringUtil.toSqlInString(vin)).replace("@status@", status), parameters);
	}
	
	public Integer countVinOpenStatus(String status, Date dateTime){
		Parameters params = Parameters.with("1", dateTime);
		return (Integer) findResultListByNativeQuery(GET_VIN_COUNT_OPEN_STATUS.replaceAll("@status@", status), params).get(0);
	}

	public List<ShippingStatus> findNotInvoicedByShippingStatus(int shippingStatus) {
		Parameters p = Parameters.with("status", shippingStatus);
		return findAllByQuery(SELECT_UNINVOICED, p);
	}

	public List<ShippingStatus> findNotInvoicedBy75ATimestamp(int shippingStatus, Timestamp startTs, Timestamp endTs, String pp) {
		Parameters p = Parameters.with("1", shippingStatus);
		p.put("2", startTs);
		p.put("3", endTs);
		p.put("4",  pp);
		return findAllByNativeQuery(SELECT_UNINVOICED_BY_TS, p);
	}

	public List<Object[]> getInvoicedVindDetails(String fromTimeStamp, String toTimeStamp, String partNames) {
		
		String PARTNAME_LIKE_QUERY = null;
		
		Parameters params = Parameters.with("1", fromTimeStamp);
		params.put("2", toTimeStamp);
		
		String[] partNameLst = StringUtils.split(partNames, ",");
		
		for(String partName : partNameLst){
			
			if(PARTNAME_LIKE_QUERY == null)
				PARTNAME_LIKE_QUERY = "INSTALLED_PART.PART_NAME LIKE ('"+partName+"%')";
			else
				PARTNAME_LIKE_QUERY = PARTNAME_LIKE_QUERY + " OR INSTALLED_PART.PART_NAME LIKE ('"+partName+"%')";
			
		}
		
		return findAllByNativeQuery(GET_INVOICED_VINS_WITH_DETAILS1 + PARTNAME_LIKE_QUERY  + GET_INVOICED_VINS_WITH_DETAILS2, params, Object[].class);
	}
	
	public List<Object[]> getInvoicedVindDetailsWithExtRequired(String fromTimeStamp, String toTimeStamp, String partNames, List<String> framePrefix) {
		
		Parameters params = Parameters.with("1", fromTimeStamp);
		params.put("2", toTimeStamp);
		
		String framePrefixNotLikeQuery = null;
		for(String prefix : framePrefix) {
			if(framePrefixNotLikeQuery == null) 
				framePrefixNotLikeQuery = "INSTALLED_SN.part_serial_number NOT LIKE ('"+prefix+"%')";
			else
				framePrefixNotLikeQuery = framePrefixNotLikeQuery + " AND INSTALLED_SN.part_serial_number NOT LIKE ('"+prefix+"%')";	
		}
		String sql1 = GET_INVOICED_VINS_WITH_DETAILS_WITH_EXT_REQ1 + framePrefixNotLikeQuery;
		
		return findAllByNativeQuery(sql1 + GET_INVOICED_VINS_WITH_DETAILS_WITH_EXT_REQ2.replaceAll("@PART_NAMES@", StringUtil.toSqlInString(partNames)), params, Object[].class);
	}
	
	public List<Object[]> getInvoicedVindDetailsWithSubPart(String fromTimeStamp, String toTimeStamp, String partNames, String subPartNames) {
		
		String PART_NAME_LIKE_QUERY = null;
		String SUB_PART_NAME_LIKE_QUERY = null;
		
		Parameters params = Parameters.with("1", fromTimeStamp);
		params.put("2", toTimeStamp);
		
		String[] partNameList = StringUtils.split(partNames, ",");
		String[] subPartNamesList = StringUtils.split(subPartNames, ",");
		
		for(String partName : partNameList) {
		if(PART_NAME_LIKE_QUERY == null) 
			PART_NAME_LIKE_QUERY = "INSTALLED_PART1.PART_NAME LIKE ('"+partName+"%')";
		else
			PART_NAME_LIKE_QUERY = PART_NAME_LIKE_QUERY + " OR INSTALLED_PART1.PART_NAME LIKE ('"+partName+"%')";
		}
		
		for(String subPartName : subPartNamesList) {
			if(SUB_PART_NAME_LIKE_QUERY == null) 
				SUB_PART_NAME_LIKE_QUERY =  "INSTALLED_PART2.PART_NAME LIKE ('"+subPartName+"%')";
			else
				SUB_PART_NAME_LIKE_QUERY = SUB_PART_NAME_LIKE_QUERY + " OR INSTALLED_PART2.PART_NAME LIKE ('"+subPartName+"%')";
		}
		
		return findAllByNativeQuery(GET_INVOICED_VINS_WITH_SUB_PART_DETAILS1 + PART_NAME_LIKE_QUERY  + GET_INVOICED_VINS_WITH_SUB_PART_DETAILS2 + 
				SUB_PART_NAME_LIKE_QUERY + GET_INVOICED_VINS_WITH_DETAILS2, params, Object[].class);
	}
}