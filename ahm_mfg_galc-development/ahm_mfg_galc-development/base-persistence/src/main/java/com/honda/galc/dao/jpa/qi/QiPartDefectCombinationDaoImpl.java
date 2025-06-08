package com.honda.galc.dao.jpa.qi;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.dto.qi.QiDefectCombinationResultDto;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiPartDefectCombinationDto;
import com.honda.galc.dto.qi.QiRegionalAttributeDto;
import com.honda.galc.dto.qi.QiRegionalPartDefectLocationDto;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QIPartDefectCombinationDaoImpl Class description</h3>
 * <p> QIPartDefectCombination Dao Impl </p>
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
 * @author L&T Infotech<br>
 * Aug 26, 2016
 *
 *
 */

public class QiPartDefectCombinationDaoImpl extends BaseDaoImpl<QiPartDefectCombination, Integer> implements QiPartDefectCombinationDao {
	
	private static final String UPDATE_PART_DEFECT_COMB_STATUS ="update GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX set ACTIVE = ?1 , UPDATE_USER = ?2 " +
			"where REGIONAL_DEFECT_COMBINATION_ID = ?3";
	
	private static final String CHECK_PART_DEFECT_COMB = "select * from GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX where " +
			"PART_LOCATION_ID=?1 and " +
			"COALESCE(DEFECT_TYPE_NAME,'')=?2 and " +
			"COALESCE(DEFECT_TYPE_NAME2,'')=?3 and " +
			"PRODUCT_KIND=?4 and " +
			"REGIONAL_DEFECT_COMBINATION_ID!=?5";
	private final static String FIND_PART_LOCATION_ID  ="select distinct e.PART_LOCATION_ID from QI_REGIONAL_DEFECT_COMBINATION_TBX e where e.PRODUCT_KIND =?1 and  e.PART_LOCATION_ID=?2 ";
	private final static String INACTIVATE_PART = "update QI_REGIONAL_DEFECT_COMBINATION_TBX e set e.ACTIVE = 0 ,UPDATE_USER=?3 where e.PART_LOCATION_ID= ?1 and e.PRODUCT_KIND =?2";
	private final static String GET_PART_LOCATION_ID  ="select e from QiPartDefectCombination e where e.productKind= :productKind and e.partLocationId in (:partLocIdList)";
	private final static String GET_ALL_ASSIGNED_PART_LOCATION_ID  ="select e from QiPartDefectCombination e where e.productKind= :productKind and e.partLocationId in (:partLocIdList) "
			+" and ((e.iqsId is not null or  e.iqsId <> 0 ) and (e.themeName is not null))";
	
	private static final String FIND_REGIONAL_DEFECT_BY_ENTRY_SCREEN = "SELECT * from " +
			"(SELECT distinct REGIONAL.REGIONAL_DEFECT_COMBINATION_ID,  PARTLOC.INSPECTION_PART_NAME,  " +
			"REPLACE(REPLACE(REPLACE(PARTLOC.INSPECTION_PART_NAME || ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART_LOCATION_NAME, '') || ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART2_NAME,'')|| ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART3_NAME, '') || ' ' || " +
			"COALESCE (REGIONAL.DEFECT_TYPE_NAME, '') || ' ' ||  " +
			"COALESCE (REGIONAL.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ') AS FULL_PART_DESC  "+
			"FROM " +
			"GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX REGIONAL," +
			"GALADM.QI_PART_LOCATION_COMBINATION_TBX PARTLOC " +
			"WHERE " +
			"PARTLOC.PART_LOCATION_ID = REGIONAL.PART_LOCATION_ID " +
			"AND REGIONAL.PRODUCT_KIND = PARTLOC.PRODUCT_KIND " +
			"AND REGIONAL.ACTIVE = 1 " +
			"AND REGIONAL.REGIONAL_DEFECT_COMBINATION_ID NOT IN (SELECT REGIONAL_DEFECT_COMBINATION_ID FROM GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX WHERE ENTRY_SCREEN = ?3 AND ENTRY_MODEL = ?4 AND IS_USED = ?5) " +
			"AND REGIONAL.PRODUCT_KIND = ?1 order by  PARTLOC.INSPECTION_PART_NAME, FULL_PART_DESC) where " +
			"FULL_PART_DESC LIKE ?2"; 
	
	private static final String FIND_REGIONAL_DEFECT_BY_ENTRY_SCREEN_FOR_IMAGE = "SELECT * from " +
			"(SELECT distinct REGIONAL.REGIONAL_DEFECT_COMBINATION_ID,  PARTLOC.INSPECTION_PART_NAME, " +
			"REPLACE(REPLACE(REPLACE(PARTLOC.INSPECTION_PART_NAME || ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART_LOCATION_NAME, '') || ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART2_NAME,'')|| ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART3_NAME, '') || ' ' || " +
			"COALESCE (REGIONAL.DEFECT_TYPE_NAME, '') || ' ' ||  " +
			"COALESCE (REGIONAL.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ') AS FULL_PART_DESC ,ENTRY.ENTRY_SCREEN " +
			"from GALADM.QI_IMAGE_SECTION_TBX IMAGE," +
			"GALADM.QI_ENTRY_SCREEN_TBX ENTRY ," +
			"GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX REGIONAL, " +
			"GALADM.QI_PART_LOCATION_COMBINATION_TBX PARTLOC " +
			"where IMAGE.IMAGE_NAME = ENTRY.IMAGE_NAME " +
			"and ENTRY.ACTIVE =1 " +
			"AND ENTRY.IS_IMAGE=1  " +
			"AND PARTLOC.ACTIVE =1 " +
			"AND REGIONAL.ACTIVE =1 " +
			"AND REGIONAL.PRODUCT_KIND = ?1  " +
			"AND ENTRY.ENTRY_SCREEN = ?3 AND ENTRY.ENTRY_MODEL = ?4 AND ENTRY.IS_USED = ?5 " +
			"AND REGIONAL.PART_LOCATION_ID = IMAGE.PART_LOCATION_ID " +
			"AND PARTLOC.PART_LOCATION_ID =  REGIONAL.PART_LOCATION_ID " +
			"AND REGIONAL.REGIONAL_DEFECT_COMBINATION_ID NOT IN (SELECT REGIONAL_DEFECT_COMBINATION_ID FROM GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX WHERE ENTRY_SCREEN = ?3 AND ENTRY_MODEL = ?4 AND IS_USED = ?5) order by  PARTLOC.INSPECTION_PART_NAME, FULL_PART_DESC) " +
			"where FULL_PART_DESC LIKE ?2";
	
	
	private static final String FIND_REGIONAL_DEFECT = "SELECT * from " +
			"(SELECT distinct REGIONAL.REGIONAL_DEFECT_COMBINATION_ID,  PARTLOC.INSPECTION_PART_NAME,  " +
			"REPLACE(REPLACE(REPLACE(PARTLOC.INSPECTION_PART_NAME || ' ' ||  " +
			"COALESCE  (PARTLOC.INSPECTION_PART_LOCATION_NAME, '') || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART_LOCATION2_NAME, '') || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART2_NAME,'')|| ' ' ||  " +
			"COALESCE  (PARTLOC.INSPECTION_PART2_LOCATION_NAME, '') || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART3_NAME, '') || ' ' || " +
			"COALESCE (REGIONAL.DEFECT_TYPE_NAME, '') || ' ' ||  " +
			"COALESCE (REGIONAL.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ') AS FULL_PART_DESC  "+
			"FROM " +
			"GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX REGIONAL, " +
			"GALADM.QI_PART_LOCATION_COMBINATION_TBX PARTLOC " +
			"WHERE " +
			"PARTLOC.PART_LOCATION_ID = REGIONAL.PART_LOCATION_ID " +
			"AND  REGIONAL.PRODUCT_KIND = PARTLOC.PRODUCT_KIND  " +
			"AND REGIONAL.ACTIVE = 1 " +
			"AND REGIONAL.PRODUCT_KIND = ?1 order by  PARTLOC.INSPECTION_PART_NAME, FULL_PART_DESC) " +
			"where (FULL_PART_DESC LIKE ?2 OR INSPECTION_PART_NAME LIKE ?2)";
	
	
	
	private static final String FIND_ENTRY_SCREEN_DEFECT = "SELECT * from " +
			"(SELECT distinct regionalDefectComb.REGIONAL_DEFECT_COMBINATION_ID,  PARTLOC.INSPECTION_PART_NAME,  " +
			"REPLACE(REPLACE(REPLACE(PARTLOC.INSPECTION_PART_NAME || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART_LOCATION_NAME, '') || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART_LOCATION2_NAME, '') || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART2_NAME,'')|| ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART2_LOCATION_NAME, '') || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || " +
			"COALESCE (PARTLOC.INSPECTION_PART3_NAME, '') || ' ' || " +
			"COALESCE (regionalDefectComb.DEFECT_TYPE_NAME, '') || ' ' ||  " +
			"COALESCE (regionalDefectComb.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ') AS FULL_PART_DESC  "+
			"from " +
			"GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX regionalDefectComb, " +
			"GALADM.QI_PART_LOCATION_COMBINATION_TBX  PARTLOC , " +
			"GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX entryDefect " +
			"where " +
			"regionalDefectComb.PART_LOCATION_ID  = PARTLOC.PART_LOCATION_ID  " +
			"AND regionalDefectComb.ACTIVE = 1 " +
			"AND entryDefect.REGIONAL_DEFECT_COMBINATION_ID = regionalDefectComb.REGIONAL_DEFECT_COMBINATION_ID " +
			"AND regionalDefectComb.PRODUCT_KIND = ?1 AND entryDefect.ENTRY_SCREEN = ?2 AND entryDefect.ENTRY_MODEL = ?4 AND entryDefect.IS_USED = ?5 order by PARTLOC.INSPECTION_PART_NAME, FULL_PART_DESC)" +
			"where FULL_PART_DESC LIKE ?3";
	
	private static final String FIND_ENTRY_SCREEN_DEFECT_BY_MENU = "SELECT * from " +
			"(SELECT distinct regionalDefectComb.REGIONAL_DEFECT_COMBINATION_ID,  PARTLOC.INSPECTION_PART_NAME,  " +
			"REPLACE(REPLACE(REPLACE(PARTLOC.INSPECTION_PART_NAME || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART_LOCATION_NAME, '') || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART_LOCATION2_NAME, '') || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART2_NAME,'')|| ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART2_LOCATION_NAME, '') || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' ||  " +
			"COALESCE (PARTLOC.INSPECTION_PART3_NAME, '') || ' ' || " +
			"COALESCE (regionalDefectComb.DEFECT_TYPE_NAME, '') || ' ' ||  " +
			"COALESCE (regionalDefectComb.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ') AS FULL_PART_DESC  "+
			"from " +
			"GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX regionalDefectComb, " +
			"GALADM.QI_PART_LOCATION_COMBINATION_TBX  PARTLOC , " +
			"GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX entryDefect 	" +
			"where  " +
			"regionalDefectComb.PART_LOCATION_ID  = PARTLOC.PART_LOCATION_ID " +
			"AND regionalDefectComb.ACTIVE = 1 " +
			"AND entryDefect.REGIONAL_DEFECT_COMBINATION_ID = regionalDefectComb.REGIONAL_DEFECT_COMBINATION_ID " +
			"AND regionalDefectComb.PRODUCT_KIND = ?1	" +
			"AND entryDefect.ENTRY_SCREEN = ?2 AND entryDefect.TEXT_ENTRY_MENU = ?3 AND entryDefect.ENTRY_MODEL = ?5 AND entryDefect.IS_USED = ?6  order by PARTLOC.INSPECTION_PART_NAME, FULL_PART_DESC)" +
			"where FULL_PART_DESC LIKE ?4";
	
	private static final String FIND_DEFECT_IN_PART_DEFECT_COMBINATION = 	"select e from QiPartDefectCombination e where " +
			"(e.defectTypeName = :defectName or  " +
			"e.defectTypeName2 = :defectName ) and e.productKind= :productKind";
	
	private static final String FIND_PART_LOC_COMB_IN_PART_DEFECT_COMBINATION = "select e from QiPartDefectCombination e where " +
	        "e.partLocationId = :partLocId and e.productKind= :productKind";
			
	private String GET_REGIONAL_ATTRIBUTE = "SELECT * FROM (SELECT pdc.REGIONAL_DEFECT_COMBINATION_ID, "
			+" REPLACE(REPLACE(REPLACE(plc.INSPECTION_PART_NAME || ' ' || COALESCE "
			+" (plc.INSPECTION_PART_LOCATION_NAME, '') || ' ' || COALESCE "
			+" (plc.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE "
			+" (plc.INSPECTION_PART2_NAME,'')|| ' ' || COALESCE "
			+" (plc.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE "
			+" (plc.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || COALESCE "
			+" (plc.INSPECTION_PART3_NAME, ''),' ','{}'),'}{',''),'{}',' ') AS FULL_PART_NAME, "
			+" pdc.DEFECT_TYPE_NAME AS DEFECT_TYPE_NAME, "
			+" pdc.DEFECT_TYPE_NAME2 AS DEFECT_TYPE_NAME2, "
			+" pdc.REPORTABLE AS REPORTABLE, "
			+" pdc.ACTIVE, "
			+" pdc.THEME_NAME AS THEME_NAME, pdc.CREATE_USER, plc.PART_LOCATION_ID, "
			+" iqs.IQS_ID AS IQS_ID, "
			+" iqs.IQS_VERSION AS IQS_VERSION, "
			+" iqs.IQS_CATEGORY AS IQS_CATEGORY, "
			+" iqs.IQS_QUESTION AS IQS_QUESTION, "
			+" iqs.IQS_QUESTION_NO AS IQS_QUESTION_NO, "
			+" pdc.UPDATE_TIMESTAMP "			
			+" FROM galadm.QI_PART_LOCATION_COMBINATION_TBX plc JOIN galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX pdc"
			+" ON plc.PART_LOCATION_ID=pdc.PART_LOCATION_ID "
			+" LEFT OUTER JOIN GALADM.QI_IQS_TBX iqs on  pdc.IQS_ID = iqs.IQS_ID WHERE  pdc.ACTIVE=1 and pdc.PRODUCT_KIND =?3 ) "
			+" where (FULL_PART_NAME like ?1 OR DEFECT_TYPE_NAME like ?1 OR DEFECT_TYPE_NAME2 like ?1 OR IQS_VERSION like ?1 "
			+" OR IQS_CATEGORY like ?1 OR IQS_QUESTION like ?1 OR THEME_NAME like ?1 "
			+" DEFECT_REPORTABLE ) ";

	
	private String GET_LOCAL_ATTRIBUTES =" select * from (SELECT distinct RDC.REGIONAL_DEFECT_COMBINATION_ID AS REGIONAL_DEFECT_COMBINATION_ID, " 
			 + " RDC.DEFECT_TYPE_NAME AS DEFECT_TYPE_NAME, RDC.DEFECT_TYPE_NAME2 AS DEFECT_TYPE_NAME2, "
			 + " (CASE WHEN LDCT.REPORTABLE IS NULL THEN RDC.REPORTABLE ELSE LDCT.REPORTABLE END) AS REPORTABLE, RDC.THEME_NAME AS THEME_NAME, "   
			 + " IT.IQS_VERSION AS IQS_VERSION, IT.IQS_CATEGORY AS IQS_CATEGORY, IT.IQS_QUESTION AS IQS_QUESTION, IT.IQS_QUESTION_NO AS IQS_QUESTION_NO, " 
			 + " PLC.PART_LOCATION_ID, RDC.ACTIVE, RDC.PRODUCT_KIND , RDC.IQS_ID as IQS_ID, ESDC.TEXT_ENTRY_MENU, LDCT.PDDA_RESPONSIBILITY_ID AS PDDA_RESPONSIBILITY_ID , "
			 + " (REPLACE(REPLACE(REPLACE(PLC.INSPECTION_PART_NAME || ' ' || COALESCE "
			 + " (PLC.INSPECTION_PART_LOCATION_NAME, '') || ' ' || COALESCE "
		     + " (PLC.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE "
			 + " (PLC.INSPECTION_PART2_NAME,'')|| ' ' || COALESCE " 
		     + " (PLC.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE  "
		     + "(PLC.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || COALESCE  "
		     + " (PLC.INSPECTION_PART3_NAME, ''),' ','{}'),'}{',''),'{}',' ')) AS FULL_PART_NAME , "
		     + "  REPLACE(REPLACE(REPLACE(RDC.DEFECT_TYPE_NAME || '-'|| COALESCE  (RDC.DEFECT_TYPE_NAME2,'' ) ,' ','{}'),'}{',''),'{}',' ') AS DEFECT_NAME ,"
			 + " REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(trim(RT.SITE) || '-'|| COALESCE  (trim(RT.PLANT),'' ) || '-'|| COALESCE  (trim(RT.DEPT),'' ) || '-'|| COALESCE (case when RT.LEVEL =1 THEN (select trim(L3.RESPONSIBLE_LEVEL_NAME) from galadm.QI_RESPONSIBLE_LEVEL_TBX L3 where L3.RESPONSIBLE_LEVEL_ID =( "
			 + " select trim(L2.UPPER_RESPONSIBLE_LEVEL_ID) from galadm.QI_RESPONSIBLE_LEVEL_TBX L2 " 
			 + " where L2.RESPONSIBLE_LEVEL_ID =RT.UPPER_RESPONSIBLE_LEVEL_ID)) ELSE '' end,'' )||'-'|| " 
			 + " COALESCE  ((case when RT.UPPER_RESPONSIBLE_LEVEL_ID >0 THEN "
			 + " (select trim(L2.RESPONSIBLE_LEVEL_NAME) from galadm.QI_RESPONSIBLE_LEVEL_TBX L2 where L2.RESPONSIBLE_LEVEL_ID =RT.UPPER_RESPONSIBLE_LEVEL_ID) ELSE '' end),'' )||'-'|| "
			 + " COALESCE  (RT.RESPONSIBLE_LEVEL_NAME ,'' ),' ','{}'),'}{',''),'{}',' '),'--','-'),'--','-')  AS RESPONSIBILITY ,LDCT.LOCAL_THEME AS LOCAL_THEME ,"
			 + "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(COALESCE(trim(PDDA.MODEL_YEAR) ,'') || '-'|| "
			 + "COALESCE  (trim(pdda.VEHICLE_MODEL_CODE),'' ) || '-'|| COALESCE  (trim(pdda.PROCESS_NUMBER),'' ) || '-'|| "
			 + "COALESCE (pdda.UNIT_NUMBER,''),' ','{}'),'}{',''),'{}',' '),'--','-'),'--','*-*'),'*-*','') as PDDA_INFO "
			 + " , case when LDCT.LOCAL_DEFECT_COMBINATION_ID  is not null then LDCT.IS_USED else ESDC.IS_USED end as IS_USED , LDCT.DEFECT_CATEGORY_NAME AS DEFECT_CATEGORY,LDCT.ENTRY_SITE_NAME as ENTRY_SITE ,LDCT.ENTRY_PLANT_NAME as ENTRY_PLANT,LDCT.REPAIR_AREA_NAME as REPAIR_AREA, LDCT.REPAIR_METHOD as REPAIR_METHOD,LDCT.REPAIR_METHOD_TIME as REPAIR_TIME , LDCT.ENGINE_FIRING_FLAG as ENGINE_FIRING_FLAG , LDCT.RESPONSIBLE_LEVEL_ID  , LDCT.LOCAL_DEFECT_COMBINATION_ID as LOCAL_ATTRIBUTE_ID "
			 + " ,PLC.INSPECTION_PART_NAME,PLC.INSPECTION_PART_LOCATION_NAME ,PLC.INSPECTION_PART_LOCATION2_NAME ,PLC.INSPECTION_PART2_NAME ,PLC.INSPECTION_PART2_LOCATION_NAME ,PLC.INSPECTION_PART3_NAME "
			 + " ,LDCT.UPDATE_TIMESTAMP "
			 + " ,RDC.REPORTABLE AS REGIONAL_REPORTABLE, DT.DEFECT_CATEGORY_NAME AS REGIONAL_DEFECT_CATEGORY, LDCT.ESTIMATED_TIME_TO_FIX "
			 + " FROM QI_REGIONAL_DEFECT_COMBINATION_TBX RDC  "
			 + " JOIN QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX ESDC ON ESDC.REGIONAL_DEFECT_COMBINATION_ID = RDC.REGIONAL_DEFECT_COMBINATION_ID "
			 + " JOIN QI_PART_LOCATION_COMBINATION_TBX PLC ON PLC.PART_LOCATION_ID = RDC.PART_LOCATION_ID  " 
			 + " JOIN QI_DEFECT_TBX DT ON DT.DEFECT_TYPE_NAME = RDC.DEFECT_TYPE_NAME "
			 + " LEFT OUTER JOIN QI_LOCAL_DEFECT_COMBINATION_TBX LDCT ON LDCT.REGIONAL_DEFECT_COMBINATION_ID = ESDC.REGIONAL_DEFECT_COMBINATION_ID  and LDCT.ENTRY_SCREEN = ESDC.ENTRY_SCREEN and LDCT.ENTRY_MODEL = ESDC.ENTRY_MODEL "  
			 + " LEFT OUTER JOIN QI_IQS_TBX IT ON IT.IQS_ID = RDC.IQS_ID  "
			 + " LEFT OUTER JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX RT ON RT.RESPONSIBLE_LEVEL_ID = LDCT.RESPONSIBLE_LEVEL_ID"
			 + " LEFT OUTER JOIN galadm.QI_PDDA_RESPONSIBILITY_TBX PDDA ON PDDA.PDDA_RESPONSIBILITY_ID = LDCT.PDDA_RESPONSIBILITY_ID "
			 + " WHERE RDC.ACTIVE = 1  " 
			 + "  AND ESDC.ENTRY_SCREEN = ?1 AND ESDC.ENTRY_MODEL = ?2 " 
			 + "  AND ((RDC.IQS_ID IS NOT NULL OR  RDC.IQS_ID <> 0 ) AND (RDC.THEME_NAME IS NOT NULL)))";
	
	private static final String FIND_AUDIT_PRIMARY_KEY_COMBINATION = "SELECT PLCT.INSPECTION_PART_NAME ,"
			+ " PLCT.INSPECTION_PART_LOCATION_NAME, PLCT.INSPECTION_PART_LOCATION2_NAME, PLCT.INSPECTION_PART2_NAME, PLCT.INSPECTION_PART2_LOCATION_NAME,  "
			+ " PLCT.INSPECTION_PART2_LOCATION2_NAME, PLCT.INSPECTION_PART3_NAME, RDCT.DEFECT_TYPE_NAME, RDCT.DEFECT_TYPE_NAME2  FROM GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX RDCT "
			+ " LEFT OUTER JOIN GALADM.QI_PART_LOCATION_COMBINATION_TBX  PLCT on RDCT.PART_LOCATION_ID = PLCT.PART_LOCATION_ID "
			+ " WHERE RDCT.REGIONAL_DEFECT_COMBINATION_ID = ?1";
	private final static String FIND_ALL_REGIONAL_ATTRIBUTES_BY_PART_DEFECT_ID  ="select e from QiPartDefectCombination e where e.regionalDefectCombinationId in (:partDefectIdList) and e.themeName is not null and e.iqsId<>0";
	private final static String FIND_ALL_REGIONAL_ATTRIBUTES_BY_PART_LOCATION_ID  ="select e from QiPartDefectCombination e where e.partLocationId in (:partLocationIdList) and e.themeName is not null and e.iqsId<>0";
	private final static String FIND_NAQ_DEFCT_BY_REGIONAL_ID= "SELECT DISTINCT c.PART_LOCATION_ID AS PART_LOCATION_ID, " +
			"COALESCE(c.INSPECTION_PART_NAME,'') AS INSPECTION_PART_NAME,COALESCE(c.INSPECTION_PART_LOCATION_NAME,'') AS INSPECTION_PART_LOCATION_NAME, " +
			"COALESCE(c.INSPECTION_PART_LOCATION2_NAME,'') AS INSPECTION_PART_LOCATION2_NAME,COALESCE(c.INSPECTION_PART2_NAME,'') AS INSPECTION_PART2_NAME, "+
			"COALESCE(c.INSPECTION_PART2_LOCATION_NAME,'') AS INSPECTION_PART2_LOCATION_NAME,COALESCE(c.INSPECTION_PART2_LOCATION2_NAME,'') AS INSPECTION_PART2_LOCATION2_NAME, "+ 
			"COALESCE(c.INSPECTION_PART3_NAME,'') AS INSPECTION_PART3_NAME,COALESCE(d.DEFECT_TYPE_NAME,'') AS DEFECT_TYPE_NAME, "+
			"COALESCE(d.DEFECT_TYPE_NAME2,'') AS DEFECT_TYPE_NAME2, COALESCE(c.PRODUCT_KIND,'') AS PRODUCT_KIND, "+
			"d.REGIONAL_DEFECT_COMBINATION_ID AS REGIONAL_DEFECT_COMBINATION_ID "+
			" FROM galadm.QI_PART_LOCATION_COMBINATION_TBX c "+
		    "JOIN galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX d ON (c.PART_LOCATION_ID = d.PART_LOCATION_ID) AND (c.PRODUCT_KIND = d.PRODUCT_KIND) "+
			" WHERE c.ACTIVE=1 and d.ACTIVE=1 and REGIONAL_DEFECT_COMBINATION_ID=?1 ORDER BY d.REGIONAL_DEFECT_COMBINATION_ID ASC";
	
	private static final String FIND_REGIONAL_ATTRIBUTE_BY_PART_LOCATION_ID_LIST = "SELECT pdc.REGIONAL_DEFECT_COMBINATION_ID,"
			+" REPLACE(REPLACE(REPLACE(plc.INSPECTION_PART_NAME || ' ' || COALESCE"
			+" (plc.INSPECTION_PART_LOCATION_NAME, '') || ' ' || COALESCE"
			+" (plc.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE"
			+" (plc.INSPECTION_PART2_NAME,'')|| ' ' || COALESCE"
			+" (plc.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE"
			+" (plc.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || COALESCE"
			+" (plc.INSPECTION_PART3_NAME, ''),' ','{}'),'}{',''),'{}',' ') AS FULL_PART_NAME,"
			+" pdc.DEFECT_TYPE_NAME, pdc.DEFECT_TYPE_NAME2, pdc.REPORTABLE, pdc.ACTIVE, pdc.THEME_NAME,"
			+" pdc.CREATE_USER, plc.PART_LOCATION_ID, iqs.IQS_ID, iqs.IQS_VERSION,"
			+" iqs.IQS_CATEGORY, iqs.IQS_QUESTION, iqs.IQS_QUESTION_NO, pdc.UPDATE_TIMESTAMP"			
			+" FROM galadm.QI_PART_LOCATION_COMBINATION_TBX plc JOIN galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX pdc"
			+" ON plc.PART_LOCATION_ID = pdc.PART_LOCATION_ID"
			+" LEFT OUTER JOIN GALADM.QI_IQS_TBX iqs on pdc.IQS_ID = iqs.IQS_ID" 
			+" WHERE pdc.ACTIVE=1 and pdc.PRODUCT_KIND =?1 and plc.PART_LOCATION_ID in ($PLC_ID_LIST$)";
	
	private static final String FIND_PART_DEFECT_BY_PART_LOCATION_ID_LIST = "SELECT pdc.REGIONAL_DEFECT_COMBINATION_ID,"
			+" REPLACE(REPLACE(REPLACE(plc.INSPECTION_PART_NAME || ' ' || COALESCE"
			+" (plc.INSPECTION_PART_LOCATION_NAME, '') || ' ' || COALESCE"
			+" (plc.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE"
			+" (plc.INSPECTION_PART2_NAME,'')|| ' ' || COALESCE"
			+" (plc.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE"
			+" (plc.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || COALESCE"
			+" (plc.INSPECTION_PART3_NAME, ''),' ','{}'),'}{',''),'{}',' ') AS FULL_PART_DESC,"
			+" pdc.DEFECT_TYPE_NAME, pdc.DEFECT_TYPE_NAME2, pdc.ACTIVE"			
			+" FROM galadm.QI_PART_LOCATION_COMBINATION_TBX plc JOIN galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX pdc"
			+" ON plc.PART_LOCATION_ID = pdc.PART_LOCATION_ID"
			+" WHERE pdc.PRODUCT_KIND =?1 and plc.PART_LOCATION_ID in ($PLC_ID_LIST$)";
	
	private static final String FIND_REGIONAL_ATTRIBUTE_BY_PART_LOCATION_ID_LIST_BY_FILTER =
			"select * from (select "
			+ "pdc.REGIONAL_DEFECT_COMBINATION_ID,"
			+ "REPLACE(REPLACE(REPLACE(plc.inspection_part_name || ' ' || "
			+ "COALESCE (plc.inspection_part_location_name, '') || ' ' || "
			+ "COALESCE (plc.inspection_part_location2_name, '') || ' ' || " 
			+ "COALESCE (plc.inspection_part2_name,'') || ' ' || "
			+ "COALESCE (plc.inspection_part2_location_name, '') || ' ' ||"
			+ "COALESCE (plc.inspection_part2_location2_name, '') || ' ' ||"
			+ "COALESCE (plc.inspection_part3_name, '')"
			+ ",' ','{}'),'}{',''),'{}',' ') as full_part_name,"
			+" pdc.DEFECT_TYPE_NAME, pdc.DEFECT_TYPE_NAME2, pdc.REPORTABLE, pdc.ACTIVE, pdc.THEME_NAME,"
			+" pdc.CREATE_USER, plc.PART_LOCATION_ID, iqs.IQS_ID, iqs.IQS_VERSION,"
			+" iqs.IQS_CATEGORY, iqs.IQS_QUESTION, iqs.IQS_QUESTION_NO, pdc.UPDATE_TIMESTAMP,"			
			+ " COALESCE (pdc.DEFECT_TYPE_NAME, '') || ' ' || "
			+ " COALESCE (pdc.DEFECT_TYPE_NAME2, '') as defect_type, "
			+ " REPLACE(REPLACE(REPLACE( "
			+ " COALESCE (iqs.IQS_VERSION, '') || ' ' || "
			+ " COALESCE (iqs.IQS_CATEGORY, '') || ' ' || "
			+ " COALESCE (iqs.IQS_QUESTION,'') "
			+ " ,' ','{}'),'}{',''),'{}',' ') as iqs_parts, "
			+ " thm.THEME_NAME as theme "
			+ " from qi_part_location_combination_tbx plc "
			+ " join QI_REGIONAL_DEFECT_COMBINATION_TBX pdc on plc.PART_LOCATION_ID=pdc.PART_LOCATION_ID "
			+ " left outer join QI_IQS_TBX iqs on pdc.IQS_ID = iqs.IQS_ID "
			+ " left outer join QI_THEME_TBX thm on pdc.THEME_NAME=thm.THEME_NAME "
			+ " where "
			+ " plc.active = 1 and plc.product_kind = ?1 and plc.PART_LOCATION_ID in ($PLC_ID_LIST$) <addl_where> ) ";
	
	private static final String FIND_PART_DEFECT_BY_PART_LOCATION_ID_LIST_BY_FILTER =
			"select * from (select "
			+ "pdc.REGIONAL_DEFECT_COMBINATION_ID,"
			+ "REPLACE(REPLACE(REPLACE(plc.inspection_part_name || ' ' || "
			+ "COALESCE (plc.inspection_part_location_name, '') || ' ' || "
			+ "COALESCE (plc.inspection_part_location2_name, '') || ' ' || " 
			+ "COALESCE (plc.inspection_part2_name,'') || ' ' || "
			+ "COALESCE (plc.inspection_part2_location_name, '') || ' ' ||"
			+ "COALESCE (plc.inspection_part2_location2_name, '') || ' ' ||"
			+ "COALESCE (plc.inspection_part3_name, '')"
			+ ",' ','{}'),'}{',''),'{}',' ') as full_part_desc,"
			+" pdc.DEFECT_TYPE_NAME, pdc.DEFECT_TYPE_NAME2,pdc.ACTIVE, "
			+ " COALESCE (pdc.DEFECT_TYPE_NAME, '') || ' ' || "
			+ " COALESCE (pdc.DEFECT_TYPE_NAME2, '') as defect_type "
			+ " from qi_part_location_combination_tbx plc "
			+ " join QI_REGIONAL_DEFECT_COMBINATION_TBX pdc on plc.PART_LOCATION_ID=pdc.PART_LOCATION_ID "
			+ " where "
			+ " plc.active = 1 and plc.product_kind = ?1 and plc.PART_LOCATION_ID in ($PLC_ID_LIST$)  <addl_where> ) ";
	
	/**
	 * This method is used to update Part Defect Combination status.
	 */
	@Transactional
	public void updatePartDefectCombStatus(Integer partDefectId, short active, String updateUser) {
		Parameters params = Parameters.with("1", active)
				.put("2", updateUser)
				.put("3", partDefectId);
		executeNativeUpdate(UPDATE_PART_DEFECT_COMB_STATUS, params);
	}
	/**
	 * This method is used to check if Part Defect Combination already exist or not.
	 */
	public boolean checkPartDefectCombination(QiPartDefectCombination comb) {
		Parameters params = Parameters.with("1", comb.getPartLocationId())
				.put("2", comb.getDefectTypeName())
				.put("3", comb.getDefectTypeName2())
				.put("4", comb.getProductKind())
				.put("5", (comb.getRegionalDefectCombinationId() == null) ? 0 : comb.getRegionalDefectCombinationId().intValue());
		return !findAllByNativeQuery(CHECK_PART_DEFECT_COMB, params, QiPartDefectCombination.class).isEmpty();
	}
	
	/**
	 * This method is used to get Part Defect combination details.
	 */
	@SuppressWarnings("unchecked")
	public List<QiRegionalPartDefectLocationDto> getPartDefectDetails(String filterData, String productkind) {
		List<QiRegionalPartDefectLocationDto> entryModelDtoList = new ArrayList<QiRegionalPartDefectLocationDto>();
		Parameters params = Parameters.with("1",productkind).put("2", "%"+filterData+"%");
		List<Object[]> regionalDefectCombObjList = findResultListByNativeQuery(FIND_REGIONAL_DEFECT, params);
		setEntryModelDtoList(entryModelDtoList, regionalDefectCombObjList);
		return entryModelDtoList;
	}
	/**
	 * This method is used to set Entry model dto list.
	 * @param entryModelDtoList
	 * @param regionalDefectCombObjList
	 */
	private void setEntryModelDtoList(List<QiRegionalPartDefectLocationDto> entryModelDtoList,List<Object[]> regionalDefectCombObjList) {
		QiRegionalPartDefectLocationDto qiRegionalPartDefectLocationDto;
		if(regionalDefectCombObjList!=null && !regionalDefectCombObjList.isEmpty()){
			for(Object[] entryModelObj : regionalDefectCombObjList){
				qiRegionalPartDefectLocationDto = new QiRegionalPartDefectLocationDto();
				qiRegionalPartDefectLocationDto.setRegionalDefectCombinationId(Integer.parseInt(null!=entryModelObj[0] ? entryModelObj[0].toString() : "0"));
				qiRegionalPartDefectLocationDto.setInspectionPartName(null!=entryModelObj[1] ? entryModelObj[1].toString() : "");
				qiRegionalPartDefectLocationDto.setDefectTypeName(null!=entryModelObj[2] ? entryModelObj[2].toString() : ""+" "+ null!=entryModelObj[3] ? entryModelObj[3].toString() : "0");
				entryModelDtoList.add(qiRegionalPartDefectLocationDto);
			}
		}
	}
	/**
	 * This method is used to get assigned defect details.
	 */
	@SuppressWarnings("unchecked")
	public List<QiRegionalPartDefectLocationDto> getAssignedDefectDetails(
			String filterData,String productKind,String entryScreen,String selectedTextEntryMenu, String entryModel, short isUsedVersion) {
		List<QiRegionalPartDefectLocationDto> entryModelDtoList = new ArrayList<QiRegionalPartDefectLocationDto>();
		Parameters params = Parameters.with("1",productKind).put("2", entryScreen).put("3", selectedTextEntryMenu)
				.put("4", "%"+filterData+"%").put("5", entryModel).put("6", isUsedVersion);
		List<Object[]> regionalDefectCombObjList = findResultListByNativeQuery(FIND_ENTRY_SCREEN_DEFECT_BY_MENU, params);
		setEntryModelDtoList(entryModelDtoList, regionalDefectCombObjList);
		return entryModelDtoList;
	}
	
	@SuppressWarnings("unchecked")
	public List<QiRegionalPartDefectLocationDto> getAssignedDefectDetails(
			String filterData,String productKind, String entryScreen, String entryModel, short isUsedVersion) {
		List<QiRegionalPartDefectLocationDto> entryModelDtoList = new ArrayList<QiRegionalPartDefectLocationDto>();
		Parameters params = Parameters.with("1",productKind).put("2", entryScreen).put("3", "%"+filterData+"%").put("4", entryModel).put("5", isUsedVersion);
		List<Object[]> regionalDefectCombObjList = findResultListByNativeQuery(FIND_ENTRY_SCREEN_DEFECT, params);
		setEntryModelDtoList(entryModelDtoList, regionalDefectCombObjList);
		return entryModelDtoList;
	}
	
	/**
	 * This method is used to get Part Defect details.
	 */
	@SuppressWarnings("unchecked")
	public List<QiRegionalPartDefectLocationDto> getPartDefectDetailsByImage(
			String filterData, String productkind,  QiEntryScreenDto qiEntryScreenDto) {
		List<QiRegionalPartDefectLocationDto> entryModelDtoList = new ArrayList<QiRegionalPartDefectLocationDto>();
		Parameters params = Parameters.with("1",productkind).put("2", "%"+filterData+"%").put("3", qiEntryScreenDto.getEntryScreen())
				.put("4", qiEntryScreenDto.getEntryModel()).put("5", qiEntryScreenDto.getIsUsedVersion());
		
		List<Object[]> regionalDefectCombObjList = findResultListByNativeQuery(FIND_REGIONAL_DEFECT_BY_ENTRY_SCREEN_FOR_IMAGE, params);
		setEntryModelDtoList(entryModelDtoList, regionalDefectCombObjList);
		return entryModelDtoList;
	}
	
	/**
	 * This method is used to get Part Defect details.
	 */
	@SuppressWarnings("unchecked")
	public List<QiRegionalPartDefectLocationDto> getPartDefectDetailsByText(
			String filterData, String productkind,  QiEntryScreenDto qiEntryScreenDto) {
		List<QiRegionalPartDefectLocationDto> entryModelDtoList = new ArrayList<QiRegionalPartDefectLocationDto>();
		Parameters params = Parameters.with("1",productkind).put("2", "%"+filterData+"%").put("3", qiEntryScreenDto.getEntryScreen())
				.put("4", qiEntryScreenDto.getEntryModel()).put("5", qiEntryScreenDto.getIsUsedVersion());
		
		List<Object[]> regionalDefectCombObjList = findResultListByNativeQuery(FIND_REGIONAL_DEFECT_BY_ENTRY_SCREEN, params);
		setEntryModelDtoList(entryModelDtoList, regionalDefectCombObjList);
		return entryModelDtoList;
	}
	
	/**
	 * This method is used to find defect in Part Defect Combination.
	 * @param defectName
	 * @param productKind
	 */
	public List<QiPartDefectCombination> findDefectInPartDefectCombination(String defectName, String productKind){
		Parameters params = Parameters.with("defectName", defectName)
				.put("productKind", productKind);
		return findAllByQuery(FIND_DEFECT_IN_PART_DEFECT_COMBINATION, params);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> findPartLocationIdsInPartDefectCombination(int partLocationId,String productKind) {
		Parameters params = Parameters.with("1", productKind).put("2", partLocationId);
		return findResultListByNativeQuery(FIND_PART_LOCATION_ID,params);
	}
	@Transactional
	public void inactivatePartinPartDefectCombination(int partLocationId,String productKind,String userId){
		Parameters params = Parameters.with("1", partLocationId).put("2",productKind).put("3",userId);
		executeNativeUpdate(INACTIVATE_PART,params);	
	}
	public List<QiPartDefectCombination> findPartLocationIdsInPartDefectCombination(List<Integer> partLocIdList,String productKind) {
		Parameters params = Parameters.with("productKind", productKind).put("partLocIdList", partLocIdList);
		return findAllByQuery(GET_PART_LOCATION_ID,params);
	}
	
	/**
	 * This method is used to find Part Loc Comb in Part Defect Combination.
	 * @param partLocId
	 * @param productKind
	 */
	public List<QiPartDefectCombination> findPartLocCombInPartDefectCombination(int partLocId, String productKind){
		Parameters params = Parameters.with("partLocId", partLocId)
				.put("productKind", productKind);
		return findAllByQuery(FIND_PART_LOC_COMB_IN_PART_DEFECT_COMBINATION, params); 
	}
	
	/**
	 * This method is used to find Part Defect Combination based on iqs id.
	 * @param qiIqs
	 */
	public List<QiPartDefectCombination> findAllByIqsId(Integer qiIqs){
		return (List<QiPartDefectCombination>) findAll(Parameters.with("iqsId", qiIqs));
	}
	
	public List<QiPartDefectCombination> findAllByThemeName(String themeName) {
		return findAll(Parameters.with("themeName", themeName));
	}
	
	@SuppressWarnings("unchecked")
	public List<QiRegionalAttributeDto> findAllByFilter(String filterValue, short active, String productKind) {
		String regionalAttributeSql = GET_REGIONAL_ATTRIBUTE;
		List<QiRegionalAttributeDto> qiDtoList = new ArrayList<QiRegionalAttributeDto>();
		QiRegionalAttributeDto regionalAttrDto = new QiRegionalAttributeDto();
		Parameters params = Parameters.with("1", "%" + filterValue + "%").put("3", productKind);;
		if(filterValue.equalsIgnoreCase("YES") || filterValue.equalsIgnoreCase("NO"))	{
			regionalAttributeSql = regionalAttributeSql.replace("DEFECT_REPORTABLE", " OR REPORTABLE = ?2 ") ;
			params.put("2", filterValue.equalsIgnoreCase("YES") ? (short)0 : (short)1);
		} else {
			regionalAttributeSql = regionalAttributeSql.replace("DEFECT_REPORTABLE", "") ;
		}
		if(active == 1)
			regionalAttributeSql = regionalAttributeSql.concat(" AND ((IQS_ID IS NOT NULL OR  IQS_ID <> 0 ) AND (THEME_NAME IS NOT NULL))" );
		else if(active == 0)
			regionalAttributeSql = regionalAttributeSql.concat(" AND (( IQS_ID IS NULL OR IQS_ID = 0 ) OR ( THEME_NAME IS NULL ))");
		
		regionalAttributeSql = regionalAttributeSql.concat(" ORDER BY FULL_PART_NAME");
		List<Object[]> regionalAttrList = findResultListByNativeQuery(regionalAttributeSql, params);
		regionalAttrDto.setRegionalAttributeDtoList(qiDtoList, regionalAttrList);
		return qiDtoList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PdcRegionalAttributeMaintDto> findAllPdcLocalAttributes(String entryScreen, String entryModel, short assigned, String filterValue) {
		String localAttributeSql = GET_LOCAL_ATTRIBUTES;
		List<PdcRegionalAttributeMaintDto> qiDtoList = new ArrayList<PdcRegionalAttributeMaintDto>();
		PdcRegionalAttributeMaintDto localAttrDto = new PdcRegionalAttributeMaintDto();
		Parameters params = Parameters.with("1", entryScreen);
		params.put("2", entryModel);
		
		boolean hasFilter = false;
		if (!StringUtils.isBlank(filterValue)) {
			hasFilter = true;
		}		
		
		if (hasFilter) {
			localAttributeSql = localAttributeSql.concat(" WHERE (DEFECT_TYPE_NAME LIKE ?3 OR "
					+ "DEFECT_TYPE_NAME2 LIKE ?3 OR THEME_NAME LIKE ?3 OR IQS_VERSION LIKE ?3 OR IQS_CATEGORY LIKE ?3 OR "
					+ "IQS_QUESTION LIKE ?3 OR FULL_PART_NAME LIKE ?3 OR LOCAL_THEME LIKE ?3 OR RESPONSIBILITY LIKE ?3 OR PDDA_INFO LIKE ?3 OR " 
                    + "TEXT_ENTRY_MENU LIKE ?3 OR DEFECT_CATEGORY LIKE ?3 OR REPAIR_AREA LIKE ?3 OR REPAIR_METHOD LIKE ?3 OR REPAIR_TIME LIKE ?3 ");	
			params.put("3", "%" + filterValue + "%");
		} 
		
		if(filterValue.equalsIgnoreCase("YES") || filterValue.equalsIgnoreCase("NO"))	{
			if(filterValue.equalsIgnoreCase("YES"))
				localAttributeSql = localAttributeSql.concat("OR REPORTABLE in (0) ") ;
			else
				localAttributeSql = localAttributeSql.concat("OR REPORTABLE in (1,2) ") ;
		} 
		
		if(assigned == 1) {
			if (hasFilter) {
				localAttributeSql = localAttributeSql.concat(") AND (");
			} else {
				localAttributeSql = localAttributeSql.concat(" WHERE ");
			}
			localAttributeSql = localAttributeSql.concat("REGIONAL_DEFECT_COMBINATION_ID IN (" 
					+ "select LD.REGIONAL_DEFECT_COMBINATION_ID from QI_LOCAL_DEFECT_COMBINATION_TBX LD where LD.ENTRY_SCREEN = ?1 and LD.ENTRY_MODEL =?2)" );
		} else if(assigned == 0) {
			if (hasFilter) {
				localAttributeSql = localAttributeSql.concat(") AND (");
			} else {
				localAttributeSql = localAttributeSql.concat(" WHERE ");
			}
			localAttributeSql = localAttributeSql.concat("REGIONAL_DEFECT_COMBINATION_ID NOT IN ("
					+ "select LD.REGIONAL_DEFECT_COMBINATION_ID from QI_LOCAL_DEFECT_COMBINATION_TBX LD where LD.ENTRY_SCREEN = ?1 and LD.ENTRY_MODEL = ?2)" );
		} 
		if (hasFilter) {
			localAttributeSql = localAttributeSql.concat(")");
		}

		localAttributeSql = localAttributeSql.concat(" ORDER BY FULL_PART_NAME");
		List<Object[]> localAttrList = findResultListByNativeQuery(localAttributeSql, params);
		localAttrDto.setLocalAttributeDtoList(qiDtoList, localAttrList);
		return qiDtoList;
	}
	
	/**
	 * This method will be used to derive referenced field values from different
	 * table for auditing purpose. All the values will be separated by space.
	 * 
	 */
	public String fetchAuditPrimaryKeyValue(int regionalDefectCombinationId) {

		Parameters params = Parameters.with("1", regionalDefectCombinationId);
		return fetchAuditPrimaryKeyValue(FIND_AUDIT_PRIMARY_KEY_COMBINATION,params);
	}
	public List<QiPartDefectCombination> findAllRegionalAttributesByPartDefectId(List<Integer> partDefectIdList) {
		Parameters params = Parameters.with("partDefectIdList", partDefectIdList);
		return findAllByQuery(FIND_ALL_REGIONAL_ATTRIBUTES_BY_PART_DEFECT_ID,params);
	}

	public List<QiPartDefectCombination> findAllRegionalAttributesByPartLocationId(List<Integer> partLocationIdList) {
		Parameters params = Parameters.with("partLocationIdList", partLocationIdList);
		return findAllByQuery(FIND_ALL_REGIONAL_ATTRIBUTES_BY_PART_LOCATION_ID,params);
	}
	
	public QiDefectCombinationResultDto findById(int regionalDefectCombinationId) {
		Parameters params = Parameters.with("1", regionalDefectCombinationId);
		return findFirstByNativeQuery(FIND_NAQ_DEFCT_BY_REGIONAL_ID, params,QiDefectCombinationResultDto.class);
	}
	
	public List<QiPartDefectCombination> findAllPLCIdsByPartLocId(List<Integer> partLocIdList,String productKind) {
		Parameters params = Parameters.with("productKind", productKind).put("partLocIdList", partLocIdList);
		return findAllByQuery(GET_ALL_ASSIGNED_PART_LOCATION_ID,params);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<QiRegionalAttributeDto> findRegionalAttributeByPartLocationIdList(List<Integer> partLocationIdList, short assigned, String productKind, String searchText, int which) {
		String regionalAttributeSql = FIND_REGIONAL_ATTRIBUTE_BY_PART_LOCATION_ID_LIST_BY_FILTER;
		List<QiRegionalAttributeDto> qiDtoList = new ArrayList<QiRegionalAttributeDto>();

		String addlWhere = "";
		if(assigned == 1)
			addlWhere =  " AND (pdc.IQS_ID IS NOT NULL OR pdc.IQS_ID <> 0 ) AND (pdc.THEME_NAME IS NOT NULL) " ;
		else if(assigned == 0)
			addlWhere = " AND (pdc.IQS_ID IS NULL OR pdc.IQS_ID = 0 OR pdc.THEME_NAME IS NULL) ";
		regionalAttributeSql = regionalAttributeSql.replace("<addl_where>", addlWhere );
		
		String filterClause = " ";
		
		String myFilter = "%"+searchText+"%";
		if(which == 1)  {  //PART_NAME
			// do nothing, already filtered showing part names
		}
		else if(which == 2)  {  //defect_type
			filterClause =  " where defect_type like ?2 ";
		}
		else if(which == 3)  {  //iqs
			filterClause =  " where iqs_parts like ?2 ";
		}
		else if(which == 4)  {  //theme
			filterClause =  " where  theme like ?2 ";
		}
		else if(which == 5)  {  //reportable
			filterClause =  " where REPORTABLE = 1 ";
		}
		else if(which == 6)  {  //non-reportable
			filterClause =  " where (REPORTABLE = 0 or REPORTABLE IS NULL) ";
		}
		Parameters params = Parameters.with("1", productKind).put("2", myFilter);
		regionalAttributeSql = regionalAttributeSql.concat(filterClause);
		
		String plcIdString = "";
		for (Integer plcId : partLocationIdList) {
			plcIdString += plcId + ",";
		}
		plcIdString = plcIdString.substring(0, plcIdString.length() - 1);
		regionalAttributeSql = regionalAttributeSql.replace("$PLC_ID_LIST$", plcIdString);
		
		regionalAttributeSql = regionalAttributeSql.concat(" ORDER BY FULL_PART_NAME, DEFECT_TYPE_NAME");
		List<Object[]> regionalAttrList = findResultListByNativeQuery(regionalAttributeSql, params);
		new QiRegionalAttributeDto().setRegionalAttributeDtoList(qiDtoList, regionalAttrList);
		return qiDtoList;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<QiPartDefectCombinationDto> findPartDefectByPartLocationIdList(List<Integer> partLocationIdList,
			short active, String productKind, String searchText, int which) {
		String sql = FIND_PART_DEFECT_BY_PART_LOCATION_ID_LIST_BY_FILTER;
		List<QiPartDefectCombinationDto> qiDtoList = new ArrayList<QiPartDefectCombinationDto>();
		
		String addlWhere = "";
		if (active == 1)
			addlWhere = " AND pdc.ACTIVE = 1";
		else if (active == 0)
			addlWhere = " AND pdc.ACTIVE = 0";
		sql = sql.replace("<addl_where>", addlWhere);

		String filterClause = " ";
		
		String myFilter = "%"+searchText+"%";
		if(which == 1)  {  //PART_NAME
			// do nothing, already filtered showing part names
		}
		else if(which == 2)  {  //defect_type
			filterClause =  " where defect_type like ?2 ";
		}
		sql = sql.concat(filterClause);
		
		String plcIdString = "";
		for (Integer plcId : partLocationIdList) {
			plcIdString += plcId + ",";
		}
		plcIdString = plcIdString.substring(0, plcIdString.length() - 1);
		sql = sql.replace("$PLC_ID_LIST$", plcIdString);
		sql = sql.concat(" ORDER BY FULL_PART_DESC, DEFECT_TYPE_NAME");

		Parameters params = Parameters.with("1", productKind).put("2", myFilter);
		
		List<Object[]> objectList = findResultListByNativeQuery(sql, params);
		new QiPartDefectCombinationDto().setDtoList(qiDtoList, objectList);
		return qiDtoList;
	}
}