package com.honda.galc.service;

/**
 * 
 * <h3>ProductionResultService</h3>
 * <p> ProductionResultService is for GIV705 </p>
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
 * @author Ratul Chakravarty<br>
 * June 20, 2014
 *
 */


import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ProductionResultService extends IService {
	public Map<String, List<String>> getProductionResults(String productionTimestampStartStr,String productionTimestampEndStr, String serviceId, boolean isJapanVINLeftJustified);
	public boolean isProductionDay(String processLocation, String productionTimestampStr);
	public boolean isProductionDayByProperty(String serviceId, String productionTimestampStr);
	public Map<String, List<Object[]>> getAsynchProductionReport(String componentId, Timestamp startTime, Timestamp endTime);
	
	/**
	 * Get the production progress for each process point (WE, PA, AF, AE, AM).
	 * @param processPoints
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Object[]> getProductionResult( final String processPoints, final String startDate, final String endDate );
	
	/**
	 * Gets the Scrap production progress for product.
	 * AE = Engine data and use the scrap query for engine (gal131tbx)
	 * AF = Frame data and use the scrap query for frame (gal143tbx)
	 * AM = Transmission data an use the scrap query for transmission (mission_tbx)
	 * @param processLocation
	 * @param processPoints
	 * @param startDate
	 * @param endDate
	 * @param trackingStatus
	 * @return
	 */
	public List<Object[]> getProductionResult ( final String processLocation
												, final String processPoints
												, final String startDate
												, final String endDate
												, final String trackingStatus );
}
