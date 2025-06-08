package com.honda.galc.dao.jpa.oif;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.oif.FrameShipConfirmationDao;
import com.honda.galc.dto.oif.FrameShipConfirmationDTO;
import com.honda.galc.entity.oif.FrameShipConfirmation;
import com.honda.galc.entity.oif.FrameShipConfirmationId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

public class FrameShipConfirmationDaoImpl extends BaseDaoImpl<FrameShipConfirmation, FrameShipConfirmationId> implements FrameShipConfirmationDao {
		
	private static final String DELETE_RECORDS_BY_DATE="DELETE FROM AEP_STAT_OUT_TBX where to_char(to_date((EVENT_DATE||' '||EVENT_TIME), 'YY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') < ?1 AND SENT_FLAG = ?2 ";
	
	 final String INSERT_RECORDS_INEXISTENT = new StringBuilder()
		.append("INSERT INTO AEP_STAT_OUT_TBX ")  
		.append("	( ")
		.append("		SELECT ")     
		.append("			gal215.PROCESS_POINT_ID, ")
		.append("			to_char(max(gal215.ACTUAL_TIMESTAMP), 'YYMMDD') as EVENT_DATE, ")
		.append("			to_char(max(gal215.ACTUAL_TIMESTAMP), 'HH24MISS') as EVENT_TIME, ")
		.append("			gal143.ENGINE_SERIAL_NO as ENGINE_ID,    ")
		.append("			gal215.PRODUCT_ID,    ")
		.append("			CONCAT(gal144.MODEL_YEAR_CODE, gal144.MODEL_CODE) as FRAME_MODEL, ")   
		.append("			gal144.MODEL_TYPE_CODE as FRAME_TYPE,    ")
		.append("			gal144.EXT_COLOR_CODE as EXT_COLOR,    ")
		.append("			gal144.INT_COLOR_CODE as INT_COLOR,    ")
		.append("			CASE WHEN length(gal144.MODEL_OPTION_CODE) >= ?4 THEN NULL ELSE gal144.MODEL_OPTION_CODE END as FRAME_OPTION,    ")
		.append("			'I' as SENT_FLAG,    ")
		.append("			?3 as RECORD_TYPE,   ")
		.append("			CURRENT TIMESTAMP,   ")   
		.append("			CURRENT TIMESTAMP   ")
		.append("		FROM     ")
		.append("			GAL215TBX gal215, ")    
		.append("			GAL143TBX gal143,   ")
		.append("			GAL144TBX gal144,   ")
		.append("			GAL217TBX gal217,   ")
		.append("			GAL131TBX gal131   ")
		.append("		WHERE      ")
		.append("			gal215.PRODUCT_ID = gal143.PRODUCT_ID ")    
		.append("			AND gal143.PRODUCT_SPEC_CODE = gal144.PRODUCT_SPEC_CODE ")  
		.append("			AND gal215.PRODUCTION_LOT = gal217.PRODUCTION_LOT   ")
		.append("			AND gal143.PRODUCT_ID = gal131.VIN ")  
		.append("			AND gal215.PROCESS_POINT_ID IN (@processPoint@)    ")
		.append("			AND gal215.ACTUAL_TIMESTAMP >= ?1 ")  	  
		.append("			AND gal131.PLANT_CODE = ?2   ")// --AEP	  
		.append("			AND gal143.ENGINE_SERIAL_NO IS NOT NULL ") 
		.append("			AND NOT EXISTS (    ")
		.append("				SELECT     ")
		.append("					PROCESS_POINT_ID, ")   
		.append("					EVENT_DATE,    ")
		.append("					EVENT_TIME,    ")
		.append("					ENGINE_ID,    ")
		.append("					PRODUCT_ID,    ")
		.append("					FRAME_MODEL,   ")
		.append("					FRAME_TYPE,    ")
		.append("					EXT_COLOR,    ")
		.append("					INT_COLOR,    ")
		.append("					FRAME_OPTION   ")
		.append("				FROM AEP_STAT_OUT_TBX aepTbx ")  
		.append("				WHERE   ")
		.append("				aepTbx.PRODUCT_ID = gal215.PRODUCT_ID ")  
		.append("			    AND PROCESS_POINT_ID IN (@processPoint@)    ")
		.append("			) ")
		.append("           group by gal215.PROCESS_POINT_ID, gal143.ENGINE_SERIAL_NO, gal215.PRODUCT_ID,  ")	
		.append("                  CONCAT(gal144.MODEL_YEAR_CODE, gal144.MODEL_CODE), gal144.MODEL_OPTION_CODE,  ")
		.append("                   gal144.EXT_COLOR_CODE, gal144.INT_COLOR_CODE,  gal144.MODEL_TYPE_CODE ")
		.append("	) ")
		.toString();
	
	private static final String SELECT_ALL_BY_FLAG = new StringBuilder()
		.append("SELECT ")
		.append("	aepTbx.RECORD_TYPE, ")  
		.append("	aepTbx.EVENT_DATE,   ")
		.append("	aepTbx.EVENT_TIME,   ")
		.append("	aepTbx.ENGINE_ID,   ")
		.append("	aepTbx.PRODUCT_ID,   ")
		.append("	aepTbx.FRAME_MODEL,   ")
		.append("	aepTbx.FRAME_TYPE,   ")
		.append("	aepTbx.EXT_COLOR,   ")
		.append("	aepTbx.INT_COLOR,   ")
		.append("	aepTbx.FRAME_OPTION,  ")
		.append("	aepTbx.PROCESS_POINT_ID,  ")
		.append("	NVL(gal131.MISSION_SERIAL_NO,'')as MISSION_SERIAL_NO,  ")
		.append("	'' as FILLER  ")
		.append("FROM AEP_STAT_OUT_TBX aepTbx, ")
		.append("	GAL143TBX gal143, ")
		.append("	GAL144TBX gal144, ")
		.append("	GAL131TBX gal131  ")
		.append("WHERE SENT_FLAG = ?1 ")
		.append("	and gal143.product_id = aepTbx.product_id ")
		.append("	and gal144.PRODUCT_SPEC_CODE = gal143.PRODUCT_SPEC_CODE ")
		.append("	and gal131.PLANT_CODE = ?2 ")
		.append("	and RECORD_TYPE = ?3 ")
		.append("	and gal131.PRODUCT_ID=aepTbx.ENGINE_ID ")
		.append("	and gal131.VIN = aepTbx.product_id ")
		.append("ORDER BY ENGINE_ID ASC")
		.toString();
		
	
	/**
	 * Delete records that have an date less that the parameter
	 * @param filterDate	-	date that is used as filter for delete old records 
	 * @return
	 */
	@Transactional
	public Integer deleteByDate (final Timestamp filterDate, final String flag)
	{	
		Parameters parameters = Parameters.with("1", filterDate);
		parameters.put("2", flag);
		return executeNativeUpdate(DELETE_RECORDS_BY_DATE, parameters);
	}
	
	/**
	 * insert the records that don't exist in AEP_STAT_OUT_TBX from two days before to the current date 
	 * @param processPoints		-	this is the list of process point for get information from product history
	 * @param dateTime			-	this is the time stamp with two days before 
	 * @param plantCodeForAEP	-	this is the plant code for the AEP plant
	 */
	@Transactional
	public void insertRecordInexistent(final String[] processPoints, final Timestamp dateTime, final String plantCodeForAEP, final String recordValue,final int frameOption)
	{
		Parameters parameters = Parameters.with("1", dateTime);
		parameters.put("2", plantCodeForAEP);
		parameters.put("3", recordValue);
		parameters.put("4", frameOption);
		executeNativeUpdate(INSERT_RECORDS_INEXISTENT.replace("@processPoint@", StringUtil.toSqlInString(processPoints)),parameters);
	}
	
	/**
	 * return all records by sent_flag
	 * @param sentFlag		-	the status of sent flag
	 * @param plantCodeAep	-	the code for aep plant
	 * @return
	 */	
	public List<FrameShipConfirmationDTO> selectByFlag(final String sentFlag, final String recordType,final String plantCodeAep)
	{
		
		Parameters parameters = Parameters.with("1", sentFlag);
		parameters.put("2", plantCodeAep);
		parameters.put("3", recordType);
		return findAllByNativeQuery(SELECT_ALL_BY_FLAG,parameters, FrameShipConfirmationDTO.class);
		//return findResultListByNativeQuery(SELECT_ALL_BY_FLAG, parameters);
	}
		
}