package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.dto.ProcessPointEfficiencyDto;
import com.honda.galc.entity.conf.MCOpEfficiencyHistory;
import com.honda.galc.service.IDaoService;

/**
 * @author Alok Ghode
 * @date Dec 18, 2015
 */
public interface MCOpEfficiencyHistoryDao extends IDaoService<MCOpEfficiencyHistory, Long> {

	public void updateProductIncomplete(String productId, String processPointId);
	
	public List<MCOpEfficiencyHistory> getLatestCompleted(String plantName, String divisionId);
	
	public double getEfficiencyInSeconds(String productId, String processPointId, String hostName);
	
	public List<ProcessPointEfficiencyDto> getCompletedUnitTotalTime(String plantName, String divisionId);
	
	public double getAccumulatedTimeInSeconds(String productId, String processPointId, String hostName) ;
	

	public void saveSummaryEffHistory(String productId, String hostName, String processPointId, String UserId, Integer sumunitTotalTime);
	
	public Double getActualTimeInSeconds(String productId, String processPointId, String hostName);
	
}