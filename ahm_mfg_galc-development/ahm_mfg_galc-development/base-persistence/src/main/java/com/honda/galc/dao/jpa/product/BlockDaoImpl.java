package com.honda.galc.dao.jpa.product;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>BlockDaoImpl Class description</h3>
 * <p> BlockDaoImpl description </p>
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
 * Jun 5, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class BlockDaoImpl extends DiecastDaoImpl<Block> implements BlockDao {

    private static final long serialVersionUID = 1L;

    private static final String FIND_ALL_DATES = 
		"SELECT DISTINCT DATE(UPDATE_TIMESTAMP) AS PRODUCTION_DATE from GALADM.BLOCK_TBX " +  
		"WHERE LAST_PASSING_PROCESS_POINT_ID = 'MEAEP16601' OR (LAST_PASSING_PROCESS_POINT_ID LIKE 'AE%' OR DEFECT_STATUS IN(3,4)) " + 
		"AND UPDATE_TIMESTAMP < CURRENT TIMESTAMP ORDER BY PRODUCTION_DATE ASC";

    private static final String FIND_ALL_BY_QSR_ID = "select p from Block p, HoldResult h where h.id.productId =  p.blockId and h.qsrId = :qsrId";
    private static final String FIND_ALL_BY_PROCESS_POINT_ID_AND_TIME = "select p from Block p, BlockHistory h where h.id.processPointId = :processPointId and h.id.blockId = p.blockId and h.id.actualTimestamp >= :startTime and h.id.actualTimestamp <= :endTime";    
    
	private static final String RELEASE_PRODUCT_HOLD_WITH_CHECK = "update galadm.block_tbx set hold_status = 0  where block_id = ?1 and (select count(*) from galadm.gal147tbx where product_id = ?1 and (release_flag != 1 or release_flag is null)) = 0";

	private static final String FIND_INVENTORY_COUNTS =
		"SELECT COUNT(*) as COUNT,LAST_PASSING_PROCESS_POINT_ID as PROCESS,  '' as PLANT " + 
		"FROM GALADM.BLOCK_TBX " +  
		"WHERE (DEFECT_STATUS IS NULL or DEFECT_STATUS < 3) AND HOLD_STATUS <>1 " + 
		"GROUP BY LAST_PASSING_PROCESS_POINT_ID " + 
		"ORDER BY PROCESS,PLANT";

	private static final String FIND_VALID_PRODUCT_FOR_PROCESS_POINT = "SELECT A.* FROM BLOCK_TBX A WHERE A.BLOCK_ID = ?1 AND A.TRACKING_STATUS IN (" +
			"SELECT B.PREVIOUS_LINE_ID FROM GAL236TBX B WHERE B.LINE_ID = (" +
			"SELECT C.LINE_ID FROM GAL195TBX C WHERE EXISTS (" +
			"SELECT 1 FROM GAL214TBX D WHERE C.LINE_ID = D.LINE_ID AND D.PROCESS_POINT_ID = ?2 " +
			"FETCH FIRST ROW ONLY) FETCH FIRST ROW ONLY)) FETCH FIRST ROW ONLY";
	
	private static final String FIND_PRODUCTS_BY_PRODUCT_SEQUENCE = 
			"SELECT A.* FROM GALADM.BLOCK_TBX A,GALADM.PRODUCT_SEQUENCE_TBX B " +
			"WHERE A.BLOCK_ID = B.PRODUCT_ID AND B.PROCESS_POINT_ID =?  " + 
			"ORDER BY B.REFERENCE_TIMESTAMP ASC";
	
	private static final String FIND_PROCESSED_PRODUCTS_BY_IN_PROCESS_PRODUCT = 
			"WITH SortedList (PRODUCT_ID, NEXT_PRODUCT_ID, Level) AS(" + 
			"SELECT PRODUCT_ID, NEXT_PRODUCT_ID, 0 as Level FROM GALADM.GAL176tbx " + 
		    "WHERE PRODUCT_ID = ? " + 
		    "UNION ALL " + 
		    "SELECT ll.PRODUCT_ID, ll.NEXT_PRODUCT_ID, Level+1 as Level FROM GALADM.GAL176tbx ll, SortedList as s " +
		    "WHERE ll.NEXT_PRODUCT_ID = s.PRODUCT_ID) " + 
		    "SELECT b.* FROM SortedList a, GALADM.BLOCK_TBX b WHERE a.PRODUCT_ID = b.BLOCK_ID "; 
			
	private static final String FIND_UPCOMING_PRODUCTS_BY_IN_PROCESS_PRODUCT = 
		"WITH SortedList (PRODUCT_ID, NEXT_PRODUCT_ID, Level) AS(" + 
		"SELECT PRODUCT_ID, NEXT_PRODUCT_ID, 0 as Level FROM GALADM.GAL176tbx " + 
	    "WHERE PRODUCT_ID = ? " + 
	    "UNION ALL " + 
	    "SELECT ll.PRODUCT_ID, ll.NEXT_PRODUCT_ID, Level+1 as Level FROM GALADM.GAL176tbx ll, SortedList as s " +
	    "WHERE ll.PRODUCT_ID = s.NEXT_PRODUCT_ID) " + 
	    "SELECT b.* FROM SortedList a, GALADM.BLOCK_TBX b WHERE a.PRODUCT_ID = b.BLOCK_ID ";

	private static final String ORDER_BY = " ORDER BY Level asc fetch first ";
	private static final String ROWS_ONLY = " rows only with ur";

	@Autowired
	private InstalledPartDao installedPartDao;
	
    public Block findByDCSerialNumber(String dcNumber) {
		
		return findFirst(Parameters.with("dcSerialNumber", dcNumber));
		
	}

	public Block findByMCSerialNumber(String mcNumber) {
		
		return findFirst(Parameters.with("mcSerialNumber", mcNumber));
		
	}

	public List<Block> findAllByEngineSerialNumber(String engineId) {
		
		return findAll(Parameters.with("engineSerialNumber", engineId));
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<Date> findAllProductionDates() {
		return findAllByNativeQuery(FIND_ALL_DATES, new Parameters(), Date.class);
	}
	
	@Transactional
	public void updateTrackingAttributes(Block product){
		update(Parameters.with("lastPassingProcessPointId", product.getLastPassingProcessPointId())
				.put("trackingStatus", product.getTrackingStatus()),
				Parameters.with("blockId", product.getProductId()));
	}
	
	@Transactional
	public void updateDefectStatus(String productId, DefectStatus status) {
		update(Parameters.with("defectStatus", status.getId()), Parameters.with("blockId", productId));
		
	}
	
	/**
	 * Method for updating when capacity constraint is not being applied
	 * @param productId
	 * @param dunnage
	 * @return
	 */
	@Transactional
	public int updateDunnage(String productId,String dunnageId) {
		return update(Parameters.with("dunnage", dunnageId), Parameters.with("blockId", productId));
	}
	
	@Transactional
	public void updateHoldStatus(String productId, int status) {
		update(Parameters.with("holdStatus", status), Parameters.with("blockId", productId));
		
	}
	
	@Transactional
	public void updateEngineSerialNumber(String productId, String esn) {
		update(Parameters.with("engineSerialNumber", esn),Parameters.with("blockId", productId));
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<InventoryCount> findAllInventoryCounts() {
		List<?> results =  findResultListByNativeQuery(FIND_INVENTORY_COUNTS, null);
		return toInventoryCounts(results);
	}

	public List<Block> findAllByProductionLot(String productionLot) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	public Block save(Block block, InstalledPart installedPart) {
		getInstalledPartDao().save(installedPart);
		return save(block);
	}	
	
	@Override
	@Transactional
	public int releaseHoldWithCheck(String productId) {
		return executeNativeUpdate(RELEASE_PRODUCT_HOLD_WITH_CHECK, Parameters.with("1", productId));
	}	

	@Transactional
	public int removeDunnage(String productId) {
		return update(Parameters.with("dunnage", null), Parameters.with("blockId", productId));
	}
	
	// === get/set === //
	@Override
	protected String getJpqlFindAllByQsrId() {
		return FIND_ALL_BY_QSR_ID;
	}	
	
	@Override
	protected String getJpqlFindAllByProcessPointIdAndTime() {
		return FIND_ALL_BY_PROCESS_POINT_ID_AND_TIME;
	}	
	
	public InstalledPartDao getInstalledPartDao() {
		return installedPartDao;
	}

	public void setInstalledPartDao(InstalledPartDao installedPartDao) {
		this.installedPartDao = installedPartDao;
	}	
	
	/**
	 * Method for updating tracking status to next Line if property is set
	 * @param productId
	 * @param nextTrackingStatus
	 * @return
	 */
	@Transactional
	public void updateNextTracking(String productId, String nextTrackingStatus){
		update(Parameters.with("trackingStatus", nextTrackingStatus),
				Parameters.with("blockId", productId));
	}
	
	@Override
	public boolean isProductTrackingStatusValidForProcessPoint(String productId, String processPointId) {
		Parameters params = Parameters.with("1", productId).put("2", processPointId);
		Block result = findFirstByNativeQuery(FIND_VALID_PRODUCT_FOR_PROCESS_POINT, params);
		return result != null;
	}
	
	public List<Block> findAllByInProcessProduct(String currentProductId,
			int processedSize, int upcomingSize) {
		List<Block> products = new ArrayList<Block>();
		List<Block> processedProducts  = findProcessedProducts(currentProductId, processedSize);
		// reverse the list and removed the current product id since it is in the upcomingBlock list
		for(int i = processedProducts.size() - 1; i >= 1; i--)
			products.add(processedProducts.get(i));
		List<Block> upcomingProducts  = findUpcomingProducts(currentProductId, upcomingSize);
		for(Block product : upcomingProducts) products.add(product);
		return products;
	}
	
	private List<Block> findProcessedProducts(String currentProductId, int processedSize) {
		Parameters params = Parameters.with("1", currentProductId);
		return findAllByNativeQuery(
				FIND_PROCESSED_PRODUCTS_BY_IN_PROCESS_PRODUCT + getOrderBy(processedSize + 1), params);
	}
	
	private List<Block> findUpcomingProducts(String currentProductId, int upcomingSize) {
		Parameters params = Parameters.with("1", currentProductId);
		return findAllByNativeQuery(
				FIND_UPCOMING_PRODUCTS_BY_IN_PROCESS_PRODUCT + getOrderBy(upcomingSize + 1), params);
	}
	
	private String getOrderBy(int size) {
		return ORDER_BY + size + ROWS_ONLY;
	}
	
	public List<Block> findAllByProductSequence(String processPointId, String currentProductId, int processedSize,
			int upcomingSize) {
		Parameters params = Parameters.with("1", processPointId);
		
		List<Block> blocks = new ArrayList<Block>();
		
		List<Block> allBlocks  = findAllByNativeQuery(FIND_PRODUCTS_BY_PRODUCT_SEQUENCE, params);
		
		int index = -1;
		for(int i = 0; i< allBlocks.size(); i++) {
			if(allBlocks.get(i).getProductId().equals(currentProductId)) {
				index = i;
				break;
			}
		}
		
		if (index == -1) return blocks;
		
		int start = (index - processedSize < 0) ? 0 :index - processedSize;
		int end = (index + upcomingSize >=allBlocks.size()) ? allBlocks.size() -1 : index + upcomingSize;
		
		for(int i = start; i <= end; i++)
			blocks.add(allBlocks.get(i));
		
		return blocks;
	}
	
	@Override
	protected String getProductIdName() {
		return "blockId";
	}
}
