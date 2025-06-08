package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.dto.ProductionLotBackout;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ProductionLotDao Class description</h3>
 * <p> ProductionLotDao description </p>
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
 * @author Jeffray Huang<br>
 * Apr 27, 2011
 *
 *
 */
@Deprecated
public interface ProductionLotDao extends IDaoService<ProductionLot, String> {
	public List<ProductionLot> findAll(String processLocation, int startPosition, int pageSize);	
	public List<ProductionLot> findAll(String processLocation, Date productionDate);
	public int findProdLotKdQty(ProductionLot productionLot);
	public int delete(String prodLotNumber);
	public void updateLotStatus(String productionLot, int status);
	public List<ProductionLot> findAllByProcessLoc(String processLocation) ;
	public ProductionLot getProductionLotForProductionPlan(String planCode, String kdLotNumber, String productSpecCode);
	public Map<String, List<String>> getProductionProgress(Integer prodProgressType, List<String> processPointOn, List<String> processPointOff, String div, List<String> lines, Boolean allowDBUpdate, Boolean useSequenceForBuildSequence, Integer sequenceNumberScale, Boolean excludeListedPlanCodes, List<String> planCodesToExclude);
	public List<Object[]> getProcessingBody(String department, List<String> processPoints, List<String> lines);
	public int getLotSize(String lot, String processLocation);
	public int getLotSizeByKdLot(String kdLot, String processLocation);
	public ProductionLot findLastSkippedLot(String ppId, String lastLot, String productionLot, String plantCode, String lineNo, String processLocation);

	public List<Object> getProductionProgress(final String processLocation, final String plantCode, final java.util.Date createTimestamp, final String processPointAmOn, final String processPointAmOff);

	public List<ProductionLot> findByKDLotAndPlanCode(String kdLot, String planCode);

	/**
	 * Method to get the Processing Body information receiving the process point and the tracking line id
	 * @param processPoint - String separated by comma
	 * @param lineId - String separated by comma
	 * @return
	 */
	public List<Object[]> getProcessingBody ( final String processPoints, final String lineIds );
	public void updateStartProductId(String productionLot, String StartProductId);

	public int deleteByPlanCodeSendStatus(String planCode, int sendStatus);

	/**
	 * Given a lot prefix and production date, returns true iff there is<br>
	 * at least one table record for any PRODUCTION_LOT<br>
	 * like the given lot prefix production date.<br>
	 * table must have a PRODUCT_ID column and productTable must have both a PRODUCT_ID and PRODUCTION_LOT column.
	 */
	public boolean isTableActiveForProductionDate(String column,String table, String lotPrefix, String productionDate, String productTable, List<String> initialProcessPointIds);

	/**
	 * Populates the given ProductionLotBackouts with the number of rows and the lot range start/end.
	 */
	public List<ProductionLotBackout> getPopulatedProductionLotBackouts(List<ProductionLotBackout> productionLotBackouts);

	/**
	 * Backs out the expected number of production lot data in the given tables for the given lot prefix lot date.<br>
	 * Returns the number of rows which were backed out.
	 */
	public int backoutProductionLot(List<ProductionLotBackout> productionLotBackouts);
	
	public Map<String,ProductionLot> findAllByLotNumber(List<String> productionLots);
	
	public List<String> findAllDemandType();
	public List<Object[]> getListOfIncompleteLots(String onPpId, String offPpId);
	
	public void updateColorDetails(String productCodeForUpdate,
			FrameSpecDto selectedFrameSpecDto, List<String> productionLots);
}
