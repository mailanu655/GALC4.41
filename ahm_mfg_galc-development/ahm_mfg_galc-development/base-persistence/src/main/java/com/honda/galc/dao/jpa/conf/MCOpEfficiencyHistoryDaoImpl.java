package com.honda.galc.dao.jpa.conf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCOpEfficiencyHistoryDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.ProcessPointEfficiencyDto;
import com.honda.galc.entity.conf.MCOpEfficiencyHistory;
import com.honda.galc.enumtype.OperationEfficiencyStatus;
import com.honda.galc.service.Parameters;

/**
 * @author Alok Ghode
 * @date Dec 18, 2015
 */
public class MCOpEfficiencyHistoryDaoImpl extends BaseDaoImpl<MCOpEfficiencyHistory, Long> implements MCOpEfficiencyHistoryDao {
	
	private static String FIND_LATEST_COMPLETED = "SELECT OE.* FROM GALADM.MC_OP_EFFICIENCY_HIST_TBX OE JOIN" +
			" (SELECT ROE1.PROCESS_POINT_ID, ROE1.HOST_NAME, MAX(ROE1.END_TIMESTAMP) END_TIMESTAMP" +
			"       FROM GALADM.MC_OP_EFFICIENCY_HIST_TBX ROE1" +
			"       WHERE ROE1.END_TIMESTAMP = " +
			"                (SELECT MAX(ROE2.END_TIMESTAMP) FROM GALADM.MC_OP_EFFICIENCY_HIST_TBX ROE2" +
			"                        WHERE ROE1.PROCESS_POINT_ID=ROE2.PROCESS_POINT_ID AND ROE1.HOST_NAME=ROE2.HOST_NAME AND ROE1.PRODUCT_ID=ROE2.PRODUCT_ID)" +
			"        AND ROE1.STATUS='"+ OperationEfficiencyStatus.PRODUCT_COMPLETE.name()+"' " + 
			"        AND ROE1.PROCESS_POINT_ID IN" +
			"                (SELECT PP.PROCESS_POINT_ID FROM GALADM.GAL214TBX PP" +
			"                        JOIN GALADM.GAL128TBX DIV ON PP.DIVISION_ID = DIV.DIVISION_ID" +
			"                        WHERE DIV.PLANT_NAME = ?1 AND DIV.DIVISION_ID = ?2)" +
			" GROUP BY ROE1.PROCESS_POINT_ID, ROE1.HOST_NAME) RECENT_COMPLETED ON" +
			"        OE.PROCESS_POINT_ID=RECENT_COMPLETED.PROCESS_POINT_ID AND OE.HOST_NAME=RECENT_COMPLETED.HOST_NAME AND OE.END_TIMESTAMP=RECENT_COMPLETED.END_TIMESTAMP";
	
	
	private static String FIND_UNIT_TIME = "SELECT E.PRODUCT_ID, E.PROCESS_POINT_ID, E.HOST_NAME, E.ASM_PROC_NO, E.UNIT_NO, E.UNIT_TOT_TIME, SUM(E.ACTUAL_TIME)" +
			" FROM GALADM.MC_OP_EFFICIENCY_HIST_TBX E" +
			" WHERE E.PRODUCT_ID= ?1 AND E.PROCESS_POINT_ID= ?2 AND E.HOST_NAME= ?3" +
			" GROUP BY E.PRODUCT_ID, E.PROCESS_POINT_ID, E.HOST_NAME, E.ASM_PROC_NO, E.UNIT_NO, E.UNIT_TOT_TIME";
	
	private static String FIND_COMPLETED_UNIT_TOTAL_TIME = "SELECT OE1.PROCESS_POINT_ID, OE1.HOST_NAME, SUM(OE1.UNIT_TOT_TIME) AS UNIT_TIME FROM GALADM.MC_OP_EFFICIENCY_HIST_TBX OE1" +
			" WHERE OE1.END_TIMESTAMP = (SELECT MAX(OE2.END_TIMESTAMP)" +
            " FROM GALADM.MC_OP_EFFICIENCY_HIST_TBX OE2" +
            " WHERE OE1.PRODUCT_ID=OE2.PRODUCT_ID AND OE1.PROCESS_POINT_ID=OE2.PROCESS_POINT_ID AND OE1.HOST_NAME=OE2.HOST_NAME AND OE1.ASM_PROC_NO=OE2.ASM_PROC_NO AND OE1.UNIT_NO=OE2.UNIT_NO)" +
            " AND OE1.STATUS IN ('"+ OperationEfficiencyStatus.PRODUCT_COMPLETE.name()+"','"+ OperationEfficiencyStatus.UNIT_COMPLETE.name()+"')" +
            " AND OE1.START_TIMESTAMP > CURRENT_DATE" +
            " AND OE1.PROCESS_POINT_ID IN" +
            " (SELECT PP.PROCESS_POINT_ID FROM GALADM.GAL214TBX PP" +
            " JOIN GALADM.GAL128TBX DIV ON PP.DIVISION_ID = DIV.DIVISION_ID" +
            " WHERE DIV.PLANT_NAME = ?1 AND DIV.DIVISION_ID = ?2)" +
            " GROUP BY OE1.PROCESS_POINT_ID, OE1.HOST_NAME";
	private static String FIND_ACTUAL_TIME_IN_SECONDS = " SELECT  SUM(HISTORY.ACTUAL_TIME) AS ACTUAL_TIME "+
            " FROM GALADM.MC_OP_EFFICIENCY_HIST_TBX HISTORY "+
			" WHERE HISTORY.PRODUCT_ID= ?1 "+
            " AND HISTORY.HOST_NAME= ?2 "+
			" AND HISTORY.PROCESS_POINT_ID=?3 ";
	
	private static String GET_SUMMARY_EFFICIENCY_HISTORY_DETAIL="SELECT PRODUCT_ID, HOST_NAME, PROCESS_POINT_ID, SUM(ACTUAL_TIME) as ACTUAL_TIME, MIN(START_TIMESTAMP) as START_TIMESTAMP, MAX(END_TIMESTAMP)"+
			"from GALADM.MC_OP_EFFICIENCY_HIST_TBX OE1 where OE1.PRODUCT_ID=?1 "+
			"AND OE1.HOST_NAME=?2 AND OE1.PROCESS_POINT_ID=?3 "+
			"AND OE1.STATUS NOT IN('"+OperationEfficiencyStatus.PRODUCT_COMPLETE.name()+"','"+OperationEfficiencyStatus.PRODUCT_INCOMPLETE.name()+"')"+
			"GROUP by PRODUCT_ID, HOST_NAME, PROCESS_POINT_ID";
	
	@Transactional
	public void updateProductIncomplete(String productId, String processPointId) {
		Parameters parameters = Parameters.with("productId", productId);	
		parameters.put("processPointId", processPointId);
		parameters.put("status", OperationEfficiencyStatus.PRODUCT_COMPLETE);
		List<MCOpEfficiencyHistory> opEfficiencyHistRecords = findAll(parameters);
		if(opEfficiencyHistRecords!=null) {
			for(MCOpEfficiencyHistory opEfficiencyHist: opEfficiencyHistRecords) {
				opEfficiencyHist.setStatus(OperationEfficiencyStatus.PRODUCT_INCOMPLETE);
				update(opEfficiencyHist);
			}
		}
	}
	
	public List<MCOpEfficiencyHistory> getLatestCompleted(String plantName, String divisionId) {
		Parameters params = Parameters.with("1", plantName)
				.put("2", divisionId);
		return findAllByNativeQuery(FIND_LATEST_COMPLETED, params);
	}
	
	public double getEfficiencyInSeconds(String productId, String processPointId, String hostName) {
		return getEfficiencyOrAccumulatedTime(productId,processPointId,hostName,false);
	}
	
	private double getEfficiencyOrAccumulatedTime(String productId, String processPointId, String hostName, boolean bAccumulated) {
		Parameters params = Parameters.with("1", productId)
				.put("2", processPointId).put("3", hostName);
		double unitTime = 0.0;
		double actualTime = 0.0;
		List<Object[]> opEffList = findAllByNativeQuery(FIND_UNIT_TIME, params, Object[].class);
		if(opEffList!=null) {
			for(Object[] opEff: opEffList) {
				unitTime += (Double)opEff[5];
				actualTime += (Double)opEff[6];
			}
		}
		if(bAccumulated){
			//Return (expected - actual) time
			return (unitTime - actualTime);
		}else{
			//Return (Actual - Expected) time
			return (actualTime - unitTime);
		}
	}
	
	public double getAccumulatedTimeInSeconds(String productId, String processPointId, String hostName) {
		return getEfficiencyOrAccumulatedTime(productId,processPointId,hostName,true);
	}
	
	public List<ProcessPointEfficiencyDto> getCompletedUnitTotalTime(String plantName, String divisionId) {
		Parameters params = Parameters.with("1", plantName)
				.put("2", divisionId);
		List<ProcessPointEfficiencyDto> ppEffList =  findAllByNativeQuery(FIND_COMPLETED_UNIT_TOTAL_TIME, params, ProcessPointEfficiencyDto.class);
		if(ppEffList == null) {
			ppEffList = new ArrayList<ProcessPointEfficiencyDto>();
		}
		return ppEffList;
	}
	
	//Insert the Summary Record
	@Transactional
	public void saveSummaryEffHistory(String productId, String hostName, String processPointId, String userId, Integer sumunitTotalTime){
		
		Parameters params = Parameters.with("1", productId);
				params.put("2", hostName);
				params.put("3", processPointId);
		List<Object[]> opEffHistorySummary=executeNative(params,GET_SUMMARY_EFFICIENCY_HISTORY_DETAIL);
		
		if(opEffHistorySummary.size()>0){
		Object[] opEffHistSummaryRecord=opEffHistorySummary.get(0);

 		MCOpEfficiencyHistory opEfficiencyHist=	new MCOpEfficiencyHistory();
		opEfficiencyHist.setProductId(productId);
		opEfficiencyHist.setHostName(hostName);
		opEfficiencyHist.setProcessPointId(processPointId);
		opEfficiencyHist.setAsmProcNo("ALL");
		opEfficiencyHist.setUnitNo("ALL");
		opEfficiencyHist.setUnitTotTime(sumunitTotalTime.doubleValue());
		opEfficiencyHist.setActualTime((Double)opEffHistSummaryRecord[3]);
		opEfficiencyHist.setStartTimestamp((Date)opEffHistSummaryRecord[4]);
		opEfficiencyHist.setEndTimestamp((Date)opEffHistSummaryRecord[5]);
		opEfficiencyHist.setStatus(OperationEfficiencyStatus.PRODUCT_COMPLETE);
		opEfficiencyHist.setAssociateNo(userId);
		save(opEfficiencyHist);
		}
	}
	
	public Double getActualTimeInSeconds(String productId, String processPointId, String hostName) {
		Parameters params = Parameters.with("1", productId);
		params.put("2", hostName);
		params.put("3", processPointId);
		return findFirstByNativeQuery(FIND_ACTUAL_TIME_IN_SECONDS, params, Double.class);
	}
}
