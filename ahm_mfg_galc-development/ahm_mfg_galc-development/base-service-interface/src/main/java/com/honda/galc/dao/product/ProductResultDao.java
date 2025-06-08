package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductResultId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ProductResultDao Class description</h3>
 * <p> ProductResultDao description </p>
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
 * <TD>YX</TD>
 * <TD>Aug 07, 2014</TD>
 * <TD>0.1</TD>
 * <TD>TASK0013687</TD>
 * <TD>Add method 'getPeriodPassingCountByPP' </TD> 
 * </TR> 
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Jun 17, 2011
 *
 *
 */
/**
 * * *
 * 
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public interface ProductResultDao extends
		ProductHistoryDao<ProductResult, ProductResultId>,
		IDaoService<ProductResult, ProductResultId> {

	List<ProductResult> findByProductAndProcessPoint(ProductResult productResult);

	List<ProductResult> findByProcessPoint(String processPointId);

	List<ProductResult> findByProductId(String productId);

	long findTotalProductProcessed(String productionLot, String processPointId);

	/**
	 * delete all product result data matching array of product ids
	 * 
	 * @param prodIds
	 */
	public int deleteProdIds(List<String> prodIds);

	public List<Object[]> findProdLotResults(String plantCode, String lineNo,
			String div, String processPointId);

	// TASK0013687
	/**
	 * get a process point passing count during a period of time group by model
	 * code and model type code
	 * 
	 * @param processPoint
	 * @param start
	 * @param end
	 * @return a list whose item is an array [model_code, model_type_code,
	 *         count]
	 */
	public List<Object[]> getPeriodPassingCountByPP(String offProcessPoints,String scrapProcessPoints,
			Date start, Date end);

	/**
	 * get lots matching kdLot OIF: NSE Production Progress (HMA)
	 */
	public List<ProductResult> getSubLots(String kdLot, String div, String ppId);

	public int getPassingCountByPPForEngines(String processPoint, Date start,
			Date end);

	public int getPassingCountByPPForFrames(String processPoint, Date start,
			Date end);
	
	public int findAlreadyProcessedProdAtPP(String processPointId,String actualTimestamp);

	public List<ProductResult> getSecondaryVinsByProcessPointAndDateRange(String ppId, String startTime, String endTime);
	
	/**
	 * Get the production progress for each process point (WE, PA, AF, AE, AM).
	 * @param processPoints
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Object []> getProductionResult ( final String processPoints
												, final String startDate
												, final String endDate);
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
	public List<Object []> getProductionResult ( final String processLocation
												, final String processPoints
												, final String startDate
												, final String endDate
												, final String trackingStatus);

	public List<Object[]> getProductionPeriod(Timestamp prodTimestamp, String processLocation1, String processLocation2);
	
	public List<Object[]> getAEProductionResult(Timestamp startTimestamp, Timestamp endTimestamp, String aeOffResCntPPId1, String aeOffResCntPPId2);
	
	public List<Object[]> getAFProductionResult(Timestamp startTimestamp, Timestamp endTimestamp, List<String> afOffResCntPPId);
	
	public List<Object[]> getPAProductionResult(Timestamp startTimestamp, Timestamp endTimestamp, List<String> paOffResCntPPId);
	
	public List<Object[]> getWEProductionResult(Timestamp startTimestamp, Timestamp endTimestamp, List<String> weOffResCntPPId);
	
	public List<Object[]> getIAProductionResult(Timestamp startTimestamp, Timestamp endTimestamp, String iaOffResCntPPId1, String iaOffResCntPPId2);
	
	public List<Object[]> getFrameScrap(Timestamp afStartTimestamp, Timestamp afEndTimestamp, List<String> scrapLineIds);
	
	public List<Object[]> getEngineScrap(Timestamp aeStartTimestamp, Timestamp aeEndTimestamp, List<String> scrapLineIds);
	
	public List<Object> getOffResultCnt(String productId, List<String> offResCntPPId);
	
	public List<Object[]> getProductSpecCnt(String processPoints, Timestamp startTime, Timestamp endTime, String planCd);
	
	public List<ProductResult> getStraggler(String processPoints, String productionLot);
	
	/**
	 * delete the history created with yms, this occur because when a vin has factory return is 
	 * necessary delete the previous process points. 
	 * @param processPints	-	String with all process points separated by comma i.e. PPXXXXX,PPXXXXX
	 * @param productId		-	The vin
	 * @return
	 */
	public int deleteHistoryByProcessPoint( final String processPoints, final String productId );

	/**
	 * Gets the actual product results of today.
	 *
	 * @param gpcsPlantCode the gpcs plant code
	 * @param divisionId the division id
	 * @param lineId the line id
	 * @param processPointId the process point id
	 * @param shift the shift. "shift" can be "01", "02", "03" or ""(for actual total of the shifts)
	 * @return the actual product results of today
	 */
	public List<ProductResult> getActualProductResultsOfToday(String gpcsPlantCode, String divisionId, String lineId, String processPointId, String shift);
	
	public ProductResult findLastProductForProcessPoint(String ppId);
	
	public int getAgedInventoryCount(String processPointIdOne, String processPointIdTwo, int ageInMins);
	
	public Timestamp getMaxActualTs(String productId, String pp);
	public int firstTimeInProcess(String productId, String processPointId);
	
	public Timestamp getInitialProcessTimestamp(String productId, List<String> processPointId);

	public void updateColorDetails(String productCodeForUpdate, FrameSpecDto selectedFrameSpecDto, List<String> productionLots);
	
	public List<ProductResult> findHistoryByProcessPointList(String productId, List<String> proocessPointList);
	
	public List<Object[]> findProductionCountGSAP(String customerProcessPoints, String startDate, String endDate);
	
	public List<Object[]> findProductionCountGSAP(String customerProcessPoints, String startDate, String endDate, String productId);
}