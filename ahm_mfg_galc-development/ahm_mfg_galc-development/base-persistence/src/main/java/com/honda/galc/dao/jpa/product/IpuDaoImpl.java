package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.IpuDao;
import com.honda.galc.entity.product.Ipu;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>IpuDaoImpl</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Jul 14, 2015
 */
public class IpuDaoImpl extends BaseSubProductDaoImpl<Ipu> implements IpuDao {

	public final static String IPU_QA_SELECT_CSV = 
			"select COALESCE(VARCHAR(results.PRODUCT_ID), '') || ',' || COALESCE(VARCHAR(results.IPU_SN), '') || ',' || COALESCE(VARCHAR(results.BATTERY), '') || ',' || "
			+"year(results.START_TIMESTAMP) || '/' || "
			+"month(results.START_TIMESTAMP) || '/' || "
			+"day(results.START_TIMESTAMP) || ' ' || "
			+"hour(results.START_TIMESTAMP) || ':' || "
			+"minute(results.START_TIMESTAMP) || ',' ||  "
			+"year(results.START_UTC) || '/' || "
			+"month(results.START_UTC) || '/' || "
			+"day(results.START_UTC) || ' ' || "
			+"hour(results.START_UTC) || ':' || "
			+"minute(results.START_UTC) || ',' ||  "
			+"COALESCE(VARCHAR(results.TOTAL_STATUS), '') || ',' || COALESCE(VARCHAR(results.CELL_VOLTAGE_STATUS), '') || ',' || "
			+"COALESCE(VARCHAR(results.VBC1), '') || ',' || COALESCE(VARCHAR(results.VBC2), '') || ',' || COALESCE(VARCHAR(results.VBC3), '')|| ',' || COALESCE(VARCHAR(results.VBC4), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC5), '')|| ',' || COALESCE(VARCHAR(results.VBC6), '')|| ',' || COALESCE(VARCHAR(results.VBC7), '')|| ',' || COALESCE(VARCHAR(results.VBC8), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC9), '')|| ',' || COALESCE(VARCHAR(results.VBC10), '')|| ',' || COALESCE(VARCHAR(results.VBC11), '')|| ',' || COALESCE(VARCHAR(results.VBC12), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC13), '')|| ',' || COALESCE(VARCHAR(results.VBC14), '')|| ',' || COALESCE(VARCHAR(results.VBC15), '')|| ',' || COALESCE(VARCHAR(results.VBC16), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC17), '')|| ',' || COALESCE(VARCHAR(results.VBC18), '')|| ',' || COALESCE(VARCHAR(results.VBC19), '')|| ',' || COALESCE(VARCHAR(results.VBC20), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC21), '')|| ',' || COALESCE(VARCHAR(results.VBC22), '')|| ',' || COALESCE(VARCHAR(results.VBC23), '')|| ',' || COALESCE(VARCHAR(results.VBC24), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC25), '')|| ',' || COALESCE(VARCHAR(results.VBC26), '')|| ',' || COALESCE(VARCHAR(results.VBC27), '')|| ',' || COALESCE(VARCHAR(results.VBC28), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC29), '')|| ',' || COALESCE(VARCHAR(results.VBC30), '')|| ',' || COALESCE(VARCHAR(results.VBC31), '')|| ',' || COALESCE(VARCHAR(results.VBC32), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC33), '')|| ',' || COALESCE(VARCHAR(results.VBC34), '')|| ',' || COALESCE(VARCHAR(results.VBC35), '')|| ',' || COALESCE(VARCHAR(results.VBC36), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC37), '')|| ',' || COALESCE(VARCHAR(results.VBC38), '')|| ',' || COALESCE(VARCHAR(results.VBC39), '')|| ',' || COALESCE(VARCHAR(results.VBC40), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC41), '')|| ',' || COALESCE(VARCHAR(results.VBC42), '')|| ',' || COALESCE(VARCHAR(results.VBC43), '')|| ',' || COALESCE(VARCHAR(results.VBC44), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC45), '')|| ',' || COALESCE(VARCHAR(results.VBC46), '')|| ',' || COALESCE(VARCHAR(results.VBC47), '')|| ',' || COALESCE(VARCHAR(results.VBC48), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC49), '')|| ',' || COALESCE(VARCHAR(results.VBC50), '')|| ',' || COALESCE(VARCHAR(results.VBC51), '')|| ',' || COALESCE(VARCHAR(results.VBC52), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC53), '')|| ',' || COALESCE(VARCHAR(results.VBC54), '')|| ',' || COALESCE(VARCHAR(results.VBC55), '')|| ',' || COALESCE(VARCHAR(results.VBC56), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC57), '')|| ',' || COALESCE(VARCHAR(results.VBC58), '')|| ',' || COALESCE(VARCHAR(results.VBC59), '')|| ',' || COALESCE(VARCHAR(results.VBC60), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC61), '')|| ',' || COALESCE(VARCHAR(results.VBC62), '')|| ',' || COALESCE(VARCHAR(results.VBC63), '')|| ',' || COALESCE(VARCHAR(results.VBC64), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC65), '')|| ',' || COALESCE(VARCHAR(results.VBC66), '')|| ',' || COALESCE(VARCHAR(results.VBC67), '')|| ',' || COALESCE(VARCHAR(results.VBC68), '')|| ',' || "
			+"COALESCE(VARCHAR(results.VBC69), '')|| ',' || COALESCE(VARCHAR(results.VBC70), '')|| ',' || COALESCE(VARCHAR(results.VBC71), '')|| ',' || COALESCE(VARCHAR(results.VBC72), '') as "
			+"RETURN_LINE "
			+"from (select ipu.PRODUCT_ID, trim(aa.PRODUCT_ID) as IPU_SN,  battery.PART_SERIAL_NUMBER as BATTERY"
			+",aa.START_TIMESTAMP,(aa.START_TIMESTAMP-current timezone) as START_UTC,dd.PROCESS_END_TIMESTAMP"
			+",CASE WHEN aa.TOTAL_RESULT_STATUS = 0 THEN 'Fail' "
	        +" WHEN aa.TOTAL_RESULT_STATUS = 1 THEN 'Pass' END AS TOTAL_STATUS"
	        +",CASE WHEN dd.INSPECTION_PGM_STATUS = 0 THEN 'Fail' "
	        +" WHEN dd.INSPECTION_PGM_STATUS = 1 THEN 'Pass' END AS CELL_VOLTAGE_STATUS"
			+",cc.INSPECTION_PARAM_NAME,bb.INSPECTION_PARAM_VALUE"
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 0 following and 0 following) as VBC1 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 1 following and 1 following) as VBC10 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 2 following and 2 following) as VBC11 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 3 following and 3 following) as VBC12 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 4 following and 4 following) as VBC13 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 5 following and 5 following) as VBC14 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 6 following and 6 following) as VBC15 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 7 following and 7 following) as VBC16 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 8 following and 8 following) as VBC17 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 9 following and 9 following) as VBC18 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 10 following and 10 following) as VBC19 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 11 following and 11 following) as VBC2 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 12 following and 12 following) as VBC20 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 13 following and 13 following) as VBC21 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 14 following and 14 following) as VBC22 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 15 following and 15 following) as VBC23 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 16 following and 16 following) as VBC24 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 17 following and 17 following) as VBC25 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 18 following and 18 following) as VBC26 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 19 following and 19 following) as VBC27 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 20 following and 20 following) as VBC28 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 21 following and 21 following) as VBC29 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 22 following and 22 following) as VBC3 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 23 following and 23 following) as VBC30 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 24 following and 24 following) as VBC31 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 25 following and 25 following) as VBC32 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 26 following and 26 following) as VBC33 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 27 following and 27 following) as VBC34 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 28 following and 28 following) as VBC35 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 29 following and 29 following) as VBC36 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 30 following and 30 following) as VBC37 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 31 following and 31 following) as VBC38 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 32 following and 32 following) as VBC39 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 33 following and 33 following) as VBC4 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 34 following and 34 following) as VBC40 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 35 following and 35 following) as VBC41 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 36 following and 36 following) as VBC42 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 37 following and 37 following) as VBC43 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 38 following and 38 following) as VBC44 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 39 following and 39 following) as VBC45 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 40 following and 40 following) as VBC46 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 41 following and 41 following) as VBC47 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 42 following and 42 following) as VBC48 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 43 following and 43 following) as VBC49 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 44 following and 44 following) as VBC5 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 45 following and 45 following) as VBC50 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 46 following and 46 following) as VBC51 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 47 following and 47 following) as VBC52 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 48 following and 48 following) as VBC53 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 49 following and 49 following) as VBC54 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 50 following and 50 following) as VBC55 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 51 following and 51 following) as VBC56 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 52 following and 52 following) as VBC57 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 53 following and 53 following) as VBC58 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 54 following and 54 following) as VBC59 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 55 following and 55 following) as VBC6 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 56 following and 56 following) as VBC60 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 57 following and 57 following) as VBC61 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 58 following and 58 following) as VBC62 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 59 following and 59 following) as VBC63 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 60 following and 60 following) as VBC64 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 61 following and 61 following) as VBC65 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 62 following and 62 following) as VBC66 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 63 following and 63 following) as VBC67 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 64 following and 64 following) as VBC68 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 65 following and 65 following) as VBC69 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 66 following and 66 following) as VBC7 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 67 following and 67 following) as VBC70 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 68 following and 68 following) as VBC71 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 69 following and 69 following) as VBC72 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 70 following and 70 following) as VBC8 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 71 following and 71 following) as VBC9 "
			+"from GALADM.GAL701TBX aa "
			+"left join GALADM.GAL703TBXV dd on dd.PRODUCT_ID = aa.PRODUCT_ID  "
			+"and dd.END_TIMESTAMP = aa.END_TIMESTAMP "
			+"and aa.TEST_SEQ = dd.TEST_SEQ "
			+"left join GALADM.GAL704TBXV bb on dd.PRODUCT_ID = bb.PRODUCT_ID "
			+"and dd.END_TIMESTAMP = bb.END_TIMESTAMP "
			+"and bb.TEST_SEQ = dd.TEST_SEQ "
			+"and bb.INSPECTION_PGM_ID = dd.INSPECTION_PGM_ID "
			+"<LEFT> join GALADM.GAL185TBX ipu on ipu.PART_NAME in (@IPU_PART_NAMES@) "
			+"and trim(ipu.PART_SERIAL_NUMBER) = trim(aa.PRODUCT_ID)  "
			+"left join GALADM.GAL185TBX battery on trim(battery.PRODUCT_ID) = trim(aa.PRODUCT_ID)  "
			+"and battery.PART_NAME in (@BATTERY_PART_NAMES@) "
			+"join GALADM.GAL715TBX cc on bb.INSPECTION_PARAM_ID = cc.INSPECTION_PARAM_ID "
			+"and cc.INSPECTION_PARAM_NAME like 'VBC%'  and cc.INSPECTION_PARAM_NAME not like 'VBC0%' "
			+"where <which_ts> BETWEEN ?1 AND ?2 "
			+") results " 
			+"where results.INSPECTION_PARAM_NAME = 'VBC1' ";


	public final static String IPU_QA_SELECT = "select PRODUCT_ID,IPU_SN , START_TIMESTAMP, START_UTC, PROCESS_END_TIMESTAMP, TOTAL_STATUS, CELL_VOLTAGE_STATUS, INSPECTION_PARAM_NAME\r\n" + 
			"INSPECTION_PARAM_VALUE, VBC1, VBC10, VBC11, VBC12, VBC13, VBC14, VBC15, VBC16, VBC17, VBC18, VBC19, VBC2, VBC20, VBC21, VBC22, VBC23, VBC24, VBC25, VBC26, VBC27, VBC28, VBC29, VBC3, VBC30, VBC31, VBC32, VBC33, VBC34, VBC35, VBC36, VBC37, VBC38, VBC39, VBC4, VBC40, VBC41, VBC42, VBC43, VBC44, VBC45, VBC46, VBC47, VBC48, VBC49, VBC5, VBC50, VBC51, VBC52, VBC53, VBC54, VBC55, VBC56, VBC57, VBC58, VBC59, VBC6, VBC60, VBC61, VBC62, VBC63, VBC64, VBC65, VBC66, VBC67, VBC68, VBC69, VBC7, VBC70, VBC71, VBC72, VBC8, VBC9\r\n" + 
			"from  ( "
			+ "select ipu.PRODUCT_ID, trim(aa.PRODUCT_ID) as IPU_SN, battery.PART_SERIAL_NUMBER as BATTERY"
			+",aa.START_TIMESTAMP,(aa.START_TIMESTAMP-current timezone) as START_UTC,dd.PROCESS_END_TIMESTAMP"
			+",CASE WHEN aa.TOTAL_RESULT_STATUS = 0 THEN 'Fail' "
	        +" WHEN aa.TOTAL_RESULT_STATUS = 1 THEN 'Pass' END AS TOTAL_STATUS"
	        +",CASE WHEN dd.INSPECTION_PGM_STATUS = 0 THEN 'Fail' "
	        +" WHEN dd.INSPECTION_PGM_STATUS = 1 THEN 'Pass' END AS CELL_VOLTAGE_STATUS"
			+",cc.INSPECTION_PARAM_NAME,bb.INSPECTION_PARAM_VALUE"
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 0 following and 0 following) as VBC1 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 1 following and 1 following) as VBC10 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 2 following and 2 following) as VBC11 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 3 following and 3 following) as VBC12 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 4 following and 4 following) as VBC13 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 5 following and 5 following) as VBC14 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 6 following and 6 following) as VBC15 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 7 following and 7 following) as VBC16 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 8 following and 8 following) as VBC17 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 9 following and 9 following) as VBC18 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 10 following and 10 following) as VBC19 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 11 following and 11 following) as VBC2 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 12 following and 12 following) as VBC20 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 13 following and 13 following) as VBC21 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 14 following and 14 following) as VBC22 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 15 following and 15 following) as VBC23 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 16 following and 16 following) as VBC24 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 17 following and 17 following) as VBC25 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 18 following and 18 following) as VBC26 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 19 following and 19 following) as VBC27 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 20 following and 20 following) as VBC28 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 21 following and 21 following) as VBC29 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 22 following and 22 following) as VBC3 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 23 following and 23 following) as VBC30 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 24 following and 24 following) as VBC31 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 25 following and 25 following) as VBC32 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 26 following and 26 following) as VBC33 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 27 following and 27 following) as VBC34 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 28 following and 28 following) as VBC35 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 29 following and 29 following) as VBC36 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 30 following and 30 following) as VBC37 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 31 following and 31 following) as VBC38 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 32 following and 32 following) as VBC39 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 33 following and 33 following) as VBC4 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 34 following and 34 following) as VBC40 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 35 following and 35 following) as VBC41 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 36 following and 36 following) as VBC42 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 37 following and 37 following) as VBC43 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 38 following and 38 following) as VBC44 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 39 following and 39 following) as VBC45 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 40 following and 40 following) as VBC46 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 41 following and 41 following) as VBC47 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 42 following and 42 following) as VBC48 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 43 following and 43 following) as VBC49 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 44 following and 44 following) as VBC5 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 45 following and 45 following) as VBC50 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 46 following and 46 following) as VBC51 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 47 following and 47 following) as VBC52 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 48 following and 48 following) as VBC53 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 49 following and 49 following) as VBC54 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 50 following and 50 following) as VBC55 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 51 following and 51 following) as VBC56 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 52 following and 52 following) as VBC57 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 53 following and 53 following) as VBC58 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 54 following and 54 following) as VBC59 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 55 following and 55 following) as VBC6 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 56 following and 56 following) as VBC60 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 57 following and 57 following) as VBC61 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 58 following and 58 following) as VBC62 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 59 following and 59 following) as VBC63 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 60 following and 60 following) as VBC64 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 61 following and 61 following) as VBC65 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 62 following and 62 following) as VBC66 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 63 following and 63 following) as VBC67 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 64 following and 64 following) as VBC68 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 65 following and 65 following) as VBC69 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 66 following and 66 following) as VBC7 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 67 following and 67 following) as VBC70 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 68 following and 68 following) as VBC71 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 69 following and 69 following) as VBC72 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 70 following and 70 following) as VBC8 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 71 following and 71 following) as VBC9 "
			+"from GALADM.GAL701TBX aa "
			+"left join GALADM.GAL703TBXV dd on dd.PRODUCT_ID = aa.PRODUCT_ID  "
			+"and dd.END_TIMESTAMP = aa.END_TIMESTAMP "
			+"and aa.TEST_SEQ = dd.TEST_SEQ "
			+"left join GALADM.GAL704TBXV bb on dd.PRODUCT_ID = bb.PRODUCT_ID "
			+"and dd.END_TIMESTAMP = bb.END_TIMESTAMP "
			+"and bb.TEST_SEQ = dd.TEST_SEQ "
			+"and bb.INSPECTION_PGM_ID = dd.INSPECTION_PGM_ID "
			+"left join GALADM.GAL185TBX ipu on ipu.PART_NAME = ?3 "
			+"and ipu.PART_SERIAL_NUMBER = trim(aa.PRODUCT_ID)  "
			+"left join GALADM.GAL185TBX battery on trim(battery.PRODUCT_ID) = trim(aa.PRODUCT_ID)  "
			+"and battery.PART_NAME = ?4 "
			+"join GALADM.GAL715TBX cc on bb.INSPECTION_PARAM_ID = cc.INSPECTION_PARAM_ID "
			+"and cc.INSPECTION_PARAM_NAME like 'VBC%'  and cc.INSPECTION_PARAM_NAME not like 'VBC0%' "
			+"where dd.END_TIMESTAMP BETWEEN ?1 AND ?2 "
			+") results " 
			+"where results.INSPECTION_PARAM_NAME = 'VBC1' ";


		public final static String IPU_LET_SELECT_CSV = 
				"select COALESCE(VARCHAR(results.PRODUCT_ID), '') || ',' || COALESCE(VARCHAR(results.IPU_SN), '') || ',' || COALESCE(VARCHAR(results.BATTERY), '') || ',' || "
				+"year(results.START_TIMESTAMP) || '/' || "
				+"month(results.START_TIMESTAMP) || '/' || "
				+"day(results.START_TIMESTAMP) || ' ' || "
				+"hour(results.START_TIMESTAMP) || ':' || "
				+"minute(results.START_TIMESTAMP) || ',' ||  "
				+"year(results.START_UTC) || '/' || "
				+"month(results.START_UTC) || '/' || "
				+"day(results.START_UTC) || ' ' || "
				+"hour(results.START_UTC) || ':' || "
				+"minute(results.START_UTC) || ',' ||  "
				+"COALESCE(VARCHAR(results.TOTAL_STATUS), '') || ',' || COALESCE(VARCHAR(results.CELL_VOLTAGE_STATUS), '') || ',' || "
				+"COALESCE(VARCHAR(results.VBC1), '') || ',' || COALESCE(VARCHAR(results.VBC2), '') || ',' || COALESCE(VARCHAR(results.VBC3), '')|| ',' || COALESCE(VARCHAR(results.VBC4), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC5), '')|| ',' || COALESCE(VARCHAR(results.VBC6), '')|| ',' || COALESCE(VARCHAR(results.VBC7), '')|| ',' || COALESCE(VARCHAR(results.VBC8), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC9), '')|| ',' || COALESCE(VARCHAR(results.VBC10), '')|| ',' || COALESCE(VARCHAR(results.VBC11), '')|| ',' || COALESCE(VARCHAR(results.VBC12), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC13), '')|| ',' || COALESCE(VARCHAR(results.VBC14), '')|| ',' || COALESCE(VARCHAR(results.VBC15), '')|| ',' || COALESCE(VARCHAR(results.VBC16), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC17), '')|| ',' || COALESCE(VARCHAR(results.VBC18), '')|| ',' || COALESCE(VARCHAR(results.VBC19), '')|| ',' || COALESCE(VARCHAR(results.VBC20), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC21), '')|| ',' || COALESCE(VARCHAR(results.VBC22), '')|| ',' || COALESCE(VARCHAR(results.VBC23), '')|| ',' || COALESCE(VARCHAR(results.VBC24), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC25), '')|| ',' || COALESCE(VARCHAR(results.VBC26), '')|| ',' || COALESCE(VARCHAR(results.VBC27), '')|| ',' || COALESCE(VARCHAR(results.VBC28), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC29), '')|| ',' || COALESCE(VARCHAR(results.VBC30), '')|| ',' || COALESCE(VARCHAR(results.VBC31), '')|| ',' || COALESCE(VARCHAR(results.VBC32), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC33), '')|| ',' || COALESCE(VARCHAR(results.VBC34), '')|| ',' || COALESCE(VARCHAR(results.VBC35), '')|| ',' || COALESCE(VARCHAR(results.VBC36), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC37), '')|| ',' || COALESCE(VARCHAR(results.VBC38), '')|| ',' || COALESCE(VARCHAR(results.VBC39), '')|| ',' || COALESCE(VARCHAR(results.VBC40), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC41), '')|| ',' || COALESCE(VARCHAR(results.VBC42), '')|| ',' || COALESCE(VARCHAR(results.VBC43), '')|| ',' || COALESCE(VARCHAR(results.VBC44), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC45), '')|| ',' || COALESCE(VARCHAR(results.VBC46), '')|| ',' || COALESCE(VARCHAR(results.VBC47), '')|| ',' || COALESCE(VARCHAR(results.VBC48), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC49), '')|| ',' || COALESCE(VARCHAR(results.VBC50), '')|| ',' || COALESCE(VARCHAR(results.VBC51), '')|| ',' || COALESCE(VARCHAR(results.VBC52), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC53), '')|| ',' || COALESCE(VARCHAR(results.VBC54), '')|| ',' || COALESCE(VARCHAR(results.VBC55), '')|| ',' || COALESCE(VARCHAR(results.VBC56), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC57), '')|| ',' || COALESCE(VARCHAR(results.VBC58), '')|| ',' || COALESCE(VARCHAR(results.VBC59), '')|| ',' || COALESCE(VARCHAR(results.VBC60), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC61), '')|| ',' || COALESCE(VARCHAR(results.VBC62), '')|| ',' || COALESCE(VARCHAR(results.VBC63), '')|| ',' || COALESCE(VARCHAR(results.VBC64), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC65), '')|| ',' || COALESCE(VARCHAR(results.VBC66), '')|| ',' || COALESCE(VARCHAR(results.VBC67), '')|| ',' || COALESCE(VARCHAR(results.VBC68), '')|| ',' || "
				+"COALESCE(VARCHAR(results.VBC69), '')|| ',' || COALESCE(VARCHAR(results.VBC70), '')|| ',' || COALESCE(VARCHAR(results.VBC71), '')|| ',' || COALESCE(VARCHAR(results.VBC72), '') as "
				+"RETURN_LINE "
				+"from (select aa.PRODUCT_ID, trim(ipu.PART_SERIAL_NUMBER) as IPU_SN, battery.PART_SERIAL_NUMBER as BATTERY"
				+",aa.START_TIMESTAMP,(aa.START_TIMESTAMP-current timezone) as START_UTC,dd.PROCESS_END_TIMESTAMP"
				+",CASE WHEN aa.TOTAL_RESULT_STATUS = 0 THEN 'Fail' "
		        +" WHEN aa.TOTAL_RESULT_STATUS = 1 THEN 'Pass' END AS TOTAL_STATUS"
		        +",CASE WHEN dd.INSPECTION_PGM_STATUS = 0 THEN 'Fail' "
		        +" WHEN dd.INSPECTION_PGM_STATUS = 1 THEN 'Pass' END AS CELL_VOLTAGE_STATUS"
				+",cc.INSPECTION_PARAM_NAME,bb.INSPECTION_PARAM_VALUE"
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 0 following and 0 following) as VBC1 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 1 following and 1 following) as VBC10 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 2 following and 2 following) as VBC11 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 3 following and 3 following) as VBC12 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 4 following and 4 following) as VBC13 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 5 following and 5 following) as VBC14 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 6 following and 6 following) as VBC15 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 7 following and 7 following) as VBC16 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 8 following and 8 following) as VBC17 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 9 following and 9 following) as VBC18 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 10 following and 10 following) as VBC19 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 11 following and 11 following) as VBC2 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 12 following and 12 following) as VBC20 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 13 following and 13 following) as VBC21 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 14 following and 14 following) as VBC22 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 15 following and 15 following) as VBC23 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 16 following and 16 following) as VBC24 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 17 following and 17 following) as VBC25 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 18 following and 18 following) as VBC26 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 19 following and 19 following) as VBC27 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 20 following and 20 following) as VBC28 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 21 following and 21 following) as VBC29 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 22 following and 22 following) as VBC3 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 23 following and 23 following) as VBC30 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 24 following and 24 following) as VBC31 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 25 following and 25 following) as VBC32 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 26 following and 26 following) as VBC33 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 27 following and 27 following) as VBC34 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 28 following and 28 following) as VBC35 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 29 following and 29 following) as VBC36 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 30 following and 30 following) as VBC37 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 31 following and 31 following) as VBC38 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 32 following and 32 following) as VBC39 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 33 following and 33 following) as VBC4 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 34 following and 34 following) as VBC40 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 35 following and 35 following) as VBC41 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 36 following and 36 following) as VBC42 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 37 following and 37 following) as VBC43 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 38 following and 38 following) as VBC44 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 39 following and 39 following) as VBC45 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 40 following and 40 following) as VBC46 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 41 following and 41 following) as VBC47 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 42 following and 42 following) as VBC48 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 43 following and 43 following) as VBC49 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 44 following and 44 following) as VBC5 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 45 following and 45 following) as VBC50 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 46 following and 46 following) as VBC51 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 47 following and 47 following) as VBC52 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 48 following and 48 following) as VBC53 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 49 following and 49 following) as VBC54 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 50 following and 50 following) as VBC55 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 51 following and 51 following) as VBC56 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 52 following and 52 following) as VBC57 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 53 following and 53 following) as VBC58 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 54 following and 54 following) as VBC59 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 55 following and 55 following) as VBC6 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 56 following and 56 following) as VBC60 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 57 following and 57 following) as VBC61 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 58 following and 58 following) as VBC62 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 59 following and 59 following) as VBC63 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 60 following and 60 following) as VBC64 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 61 following and 61 following) as VBC65 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 62 following and 62 following) as VBC66 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 63 following and 63 following) as VBC67 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 64 following and 64 following) as VBC68 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 65 following and 65 following) as VBC69 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 66 following and 66 following) as VBC7 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 67 following and 67 following) as VBC70 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 68 following and 68 following) as VBC71 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 69 following and 69 following) as VBC72 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 70 following and 70 following) as VBC8 "
				+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 71 following and 71 following) as VBC9 "
				+"from GALADM.GAL701TBX aa "
				+"left join GALADM.GAL703TBXV dd on dd.PRODUCT_ID = aa.PRODUCT_ID  "
				+"and dd.END_TIMESTAMP = aa.END_TIMESTAMP "
				+"and aa.TEST_SEQ = dd.TEST_SEQ "
				+"left join GALADM.GAL704TBXV bb on dd.PRODUCT_ID = bb.PRODUCT_ID "
				+"and dd.END_TIMESTAMP = bb.END_TIMESTAMP "
				+"and bb.TEST_SEQ = dd.TEST_SEQ "
				+"and bb.INSPECTION_PGM_ID = dd.INSPECTION_PGM_ID "
				+"join GALADM.GAL715TBX cc on bb.INSPECTION_PARAM_ID = cc.INSPECTION_PARAM_ID "
				+"and cc.INSPECTION_PARAM_NAME like 'VBC0%' "
				+"left join GALADM.GAL185TBX ipu on aa.PRODUCT_ID = ipu.PRODUCT_ID "
				+"and ipu.PART_NAME in (@IPU_PART_NAMES@) "
				+"left join GALADM.GAL185TBX battery on trim(battery.PRODUCT_ID) = trim(ipu.PART_SERIAL_NUMBER)  "
				+"and battery.PART_NAME in (@BATTERY_PART_NAMES@) "
				+"where aa.PRODUCT_ID in "
				+"(SELECT frame.PRODUCT_ID "
				+"FROM galadm.MC_STRUCTURE_TBX struct "
				+"INNER JOIN GALADM.GAL144TBX mtoc ON struct.PRODUCT_SPEC_CODE = mtoc.PRODUCT_SPEC_CODE "
				+"INNER JOIN GALADM.GAL143TBX frame ON frame.PRODUCT_SPEC_CODE = mtoc.PRODUCT_SPEC_CODE "
				+"INNER JOIN GALADM.GAL215TBX vqship ON frame.PRODUCT_ID = vqship.PRODUCT_ID "
				+"where (vqship.PROCESS_POINT_ID in (?3)) "
				+"AND struct.OPERATION_NAME = ipu.PART_NAME "
				+"AND vqship.ACTUAL_TIMESTAMP BETWEEN ?1 AND ?2 "
				+")"
				+") results "
				+"where results.INSPECTION_PARAM_NAME = 'VBC001'";

	public final static String IPU_LET_SELECT = "select PRODUCT_ID,IPU_SN , START_TIMESTAMP, START_UTC, PROCESS_END_TIMESTAMP, TOTAL_STATUS, CELL_VOLTAGE_STATUS, INSPECTION_PARAM_NAME\r\n" + 
			"INSPECTION_PARAM_VALUE, VBC1, VBC10, VBC11, VBC12, VBC13, VBC14, VBC15, VBC16, VBC17, VBC18, VBC19, VBC2, VBC20, VBC21, VBC22, VBC23, VBC24, VBC25, VBC26, VBC27, VBC28, VBC29, VBC3, VBC30, VBC31, VBC32, VBC33, VBC34, VBC35, VBC36, VBC37, VBC38, VBC39, VBC4, VBC40, VBC41, VBC42, VBC43, VBC44, VBC45, VBC46, VBC47, VBC48, VBC49, VBC5, VBC50, VBC51, VBC52, VBC53, VBC54, VBC55, VBC56, VBC57, VBC58, VBC59, VBC6, VBC60, VBC61, VBC62, VBC63, VBC64, VBC65, VBC66, VBC67, VBC68, VBC69, VBC7, VBC70, VBC71, VBC72, VBC8, VBC9\r\n" + 
			"from  ("
			+ "select aa.PRODUCT_ID, trim(ipu.PART_SERIAL_NUMBER) as IPU_SN, battery.PART_SERIAL_NUMBER as BATTERY"
			+",aa.START_TIMESTAMP,(aa.START_TIMESTAMP-current timezone) as START_UTC,dd.PROCESS_END_TIMESTAMP"
			+",CASE WHEN aa.TOTAL_RESULT_STATUS = 0 THEN 'Fail' "
	        +" WHEN aa.TOTAL_RESULT_STATUS = 1 THEN 'Pass' END AS TOTAL_STATUS"
	        +",CASE WHEN dd.INSPECTION_PGM_STATUS = 0 THEN 'Fail' "
	        +" WHEN dd.INSPECTION_PGM_STATUS = 1 THEN 'Pass' END AS CELL_VOLTAGE_STATUS"
			+",cc.INSPECTION_PARAM_NAME,bb.INSPECTION_PARAM_VALUE"
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 0 following and 0 following) as VBC1 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 1 following and 1 following) as VBC10 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 2 following and 2 following) as VBC11 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 3 following and 3 following) as VBC12 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 4 following and 4 following) as VBC13 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 5 following and 5 following) as VBC14 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 6 following and 6 following) as VBC15 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 7 following and 7 following) as VBC16 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 8 following and 8 following) as VBC17 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 9 following and 9 following) as VBC18 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 10 following and 10 following) as VBC19 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 11 following and 11 following) as VBC2 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 12 following and 12 following) as VBC20 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 13 following and 13 following) as VBC21 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 14 following and 14 following) as VBC22 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 15 following and 15 following) as VBC23 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 16 following and 16 following) as VBC24 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 17 following and 17 following) as VBC25 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 18 following and 18 following) as VBC26 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 19 following and 19 following) as VBC27 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 20 following and 20 following) as VBC28 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 21 following and 21 following) as VBC29 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 22 following and 22 following) as VBC3 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 23 following and 23 following) as VBC30 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 24 following and 24 following) as VBC31 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 25 following and 25 following) as VBC32 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 26 following and 26 following) as VBC33 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 27 following and 27 following) as VBC34 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 28 following and 28 following) as VBC35 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 29 following and 29 following) as VBC36 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 30 following and 30 following) as VBC37 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 31 following and 31 following) as VBC38 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 32 following and 32 following) as VBC39 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 33 following and 33 following) as VBC4 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 34 following and 34 following) as VBC40 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 35 following and 35 following) as VBC41 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 36 following and 36 following) as VBC42 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 37 following and 37 following) as VBC43 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 38 following and 38 following) as VBC44 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 39 following and 39 following) as VBC45 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 40 following and 40 following) as VBC46 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 41 following and 41 following) as VBC47 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 42 following and 42 following) as VBC48 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 43 following and 43 following) as VBC49 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 44 following and 44 following) as VBC5 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 45 following and 45 following) as VBC50 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 46 following and 46 following) as VBC51 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 47 following and 47 following) as VBC52 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 48 following and 48 following) as VBC53 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 49 following and 49 following) as VBC54 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 50 following and 50 following) as VBC55 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 51 following and 51 following) as VBC56 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 52 following and 52 following) as VBC57 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 53 following and 53 following) as VBC58 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 54 following and 54 following) as VBC59 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 55 following and 55 following) as VBC6 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 56 following and 56 following) as VBC60 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 57 following and 57 following) as VBC61 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 58 following and 58 following) as VBC62 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 59 following and 59 following) as VBC63 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 60 following and 60 following) as VBC64 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 61 following and 61 following) as VBC65 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 62 following and 62 following) as VBC66 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 63 following and 63 following) as VBC67 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 64 following and 64 following) as VBC68 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 65 following and 65 following) as VBC69 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 66 following and 66 following) as VBC7 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 67 following and 67 following) as VBC70 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 68 following and 68 following) as VBC71 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 69 following and 69 following) as VBC72 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 70 following and 70 following) as VBC8 "
			+",max(bb.INSPECTION_PARAM_VALUE) over (partition by aa.PRODUCT_ID, dd.PROCESS_END_TIMESTAMP order by cc.INSPECTION_PARAM_NAME rows between 71 following and 71 following) as VBC9 "
			+"from GALADM.GAL701TBX aa "
			+"left join GALADM.GAL703TBXV dd on dd.PRODUCT_ID = aa.PRODUCT_ID  "
			+"and dd.END_TIMESTAMP = aa.END_TIMESTAMP "
			+"and aa.TEST_SEQ = dd.TEST_SEQ "
			+"left join GALADM.GAL704TBXV bb on dd.PRODUCT_ID = bb.PRODUCT_ID "
			+"and dd.END_TIMESTAMP = bb.END_TIMESTAMP "
			+"and bb.TEST_SEQ = dd.TEST_SEQ "
			+"and bb.INSPECTION_PGM_ID = dd.INSPECTION_PGM_ID "
			+"join GALADM.GAL715TBX cc on bb.INSPECTION_PARAM_ID = cc.INSPECTION_PARAM_ID "
			+"and cc.INSPECTION_PARAM_NAME like 'VBC0%' "
			+"left join GALADM.GAL185TBX ipu on aa.PRODUCT_ID = ipu.PRODUCT_ID "
			+"and ipu.PART_NAME = ?4 "
			+"left join GALADM.GAL185TBX battery on trim(battery.PRODUCT_ID) = trim(ipu.PART_SERIAL_NUMBER)  "
			+"and battery.PART_NAME = ?5 "
			+"where aa.PRODUCT_ID in "
			+"(SELECT frame.PRODUCT_ID "
			+"FROM galadm.MC_STRUCTURE_TBX struct "
			+"INNER JOIN GALADM.GAL144TBX mtoc ON struct.PRODUCT_SPEC_CODE = mtoc.PRODUCT_SPEC_CODE "
			+"INNER JOIN GALADM.GAL143TBX frame ON frame.PRODUCT_SPEC_CODE = mtoc.PRODUCT_SPEC_CODE "
			+"INNER JOIN GALADM.GAL215TBX vqship ON frame.PRODUCT_ID = vqship.PRODUCT_ID "
			+"where (vqship.PROCESS_POINT_ID in (?3)) "
			+"AND struct.OPERATION_NAME = ipu.PART_NAME "
			+"AND vqship.ACTUAL_TIMESTAMP BETWEEN ?1 AND ?2"
			+")"
			+") results "
			+"where results.INSPECTION_PARAM_NAME = 'VBC001'";
	
	/* (non-Javadoc)
	 * @see com.honda.galc.dao.product.IpuDao#getIPULETData(java.sql.Timestamp, java.sql.Timestamp)
	 * This method returns LET battery test results as a list of values.  his is typically
	 * at the end when final LET results are collected.  While not currently used,
	 * it may be needed if results have to be processed or stored in a different format
	 */
	public List<Object[]> getIpuLetData(Timestamp startTS, Timestamp endTS,String pp,
			String ipuPartName,  String batteryPartName) {
		Parameters params = Parameters.with("1", startTS);
		params.put("2", endTS);
		params.put("3", pp);
		params.put("4", ipuPartName);
		params.put("5", batteryPartName);
		List<Object[]> ipuList = null; 
		ipuList = findAllByNativeQuery(IPU_LET_SELECT, params, Object[].class);
		return ipuList;
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.dao.product.IpuDao#getIPULETDataCsv(java.sql.Timestamp, java.sql.Timestamp)
	 * This method returns IPU LET battery test results in a comma separated format.  This is typically
	 * at the end when final LET results are collected.
	 */
	public List<String> getIpuLetDataCsv(Timestamp startTS, Timestamp endTS, String pp,
			List<String> ipuPartName,  List<String> batteryPartName
			) {
		Parameters params = Parameters.with("1", startTS);
		params.put("2", endTS);
		params.put("3", pp);
		List<String> ipuList = null; 
		String qryIpuLet = IPU_LET_SELECT_CSV.replaceAll("@IPU_PART_NAMES@", StringUtil.toSqlInString(ipuPartName));
		qryIpuLet = qryIpuLet.replaceAll("@BATTERY_PART_NAMES@", StringUtil.toSqlInString(batteryPartName));
		ipuList = findAllByNativeQuery(qryIpuLet, params, String.class);
		return ipuList;
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.dao.product.IpuDao#getIPUQAData(java.sql.Timestamp, java.sql.Timestamp)
	 * This method returns IPU_QA battery test results as a list of values.  While not currently used,
	 * it may be needed if results have to be processed or stored in a different format
	 */
	public List<Object[]> getIpuQaData(Timestamp startTS, Timestamp endTS, String ipuPartName, String batteryPartName) {
		Parameters params = Parameters.with("1", startTS);
		params.put("2", endTS);
		params.put("3", ipuPartName);
		params.put("4", batteryPartName);
		List<Object[]> ipuList = null; 
		ipuList = findAllByNativeQuery(IPU_QA_SELECT, params, Object[].class);
		return ipuList;
	}
	
	/* (non-Javadoc)
	 * @see com.honda.galc.dao.product.IpuDao#getIPUQADataCsv(java.sql.Timestamp, java.sql.Timestamp)
	 * This method returns IPU_QA battery test results in a comma separated format
	 * if isVinRequired is true, then a an inner join is used to only retrieve those IPU results
	 * that have been installed, i.e., there is a 185 entry with that IPU SN = part serial no. => this is the case for PMC
	 * if, not, then a left join is used to retrieve the VIN if available.
	 */
	public List<String> getIpuQaDataCsv(Timestamp startTS, Timestamp endTS, List<String> ipuPartName, List<String> batteryPartName, boolean isVinRequired) {
		Parameters params = Parameters.with("1", startTS);
		params.put("2", endTS);
		List<String> ipuList = null; 
		String qryIpuQa = IPU_QA_SELECT_CSV.replaceAll("@IPU_PART_NAMES@", StringUtil.toSqlInString(ipuPartName));
		qryIpuQa = qryIpuQa.replaceAll("@BATTERY_PART_NAMES@", StringUtil.toSqlInString(batteryPartName));
		String left_join = isVinRequired ? "" : "left";
		String which_ts = isVinRequired ? "ipu.ACTUAL_TIMESTAMP":"dd.END_TIMESTAMP";
		qryIpuQa = qryIpuQa.replaceFirst("<LEFT>", left_join).replaceFirst("<which_ts>", which_ts);
		ipuList = findAllByNativeQuery(qryIpuQa, params, String.class);
		return ipuList;
	}
	
	/* (non-Javadoc)
	 * @see com.honda.galc.dao.product.IpuDao#getIPUQADataCsv(java.sql.Timestamp, java.sql.Timestamp)
	 * This method returns IPU_QA battery test results in a comma separated format
	 * if isVinRequired is true, then a an inner join is used to only retrieve those IPU results
	 * that have been installed, i.e., there is a 185 entry with that IPU SN = part serial no. => this is the case for PMC
	 * if, not, then a left join is used to retrieve the VIN if available.
	 */
	protected String parseList(String in)  {
		if(StringUtils.isEmpty(in))  return "";
		List<String> all = Arrays.asList(in.split("\\s*,\\s*"));
		StringBuilder sqlVal = new StringBuilder();
		ListIterator<String> lit = all.listIterator();
		if(lit != null)  {
			while(lit.hasNext())  {
				String val = lit.next();
				sqlVal.append("'").append(val).append("'");
				if(lit.hasNext())  sqlVal.append(",");
			}
		}
		return sqlVal.toString();
	}


}
