package com.honda.galc.dao.jpa.product;


import java.sql.Date;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.Head;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>HeadDaoImpl Class description</h3>
 * <p> HeadDaoImpl description </p>
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
 * Jun 28, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class HeadDaoImpl extends DiecastDaoImpl<Head>implements HeadDao {

    private static final long serialVersionUID = 1L;

	private static final String FIND_ALL_DATES = 
		"SELECT DISTINCT DATE(UPDATE_TIMESTAMP) AS PRODUCTION_DATE from GALADM.HEAD_TBX " +  
		"WHERE LAST_PASSING_PROCESS_POINT_ID = 'MEAEP16601' OR (LAST_PASSING_PROCESS_POINT_ID LIKE 'AE%' OR DEFECT_STATUS IN(3,4)) " + 
		"AND UPDATE_TIMESTAMP < CURRENT TIMESTAMP ORDER BY PRODUCTION_DATE ASC";

    private static final String FIND_ALL_BY_QSR_ID = "select p from Head p, HoldResult h where h.id.productId =  p.headId and h.qsrId = :qsrId";
    private static final String FIND_ALL_BY_PROCESS_POINT_ID_AND_TIME = "select p from Head p, HeadHistory h where h.id.processPointId = :processPointId and h.id.headId = p.headId and h.id.actualTimestamp >= :startTime and h.id.actualTimestamp <= :endTime";	
	
	private static final String RELEASE_PRODUCT_HOLD_WITH_CHECK = "update galadm.head_tbx set hold_status = 0  where head_id = ?1 and (select count(*) from galadm.gal147tbx where product_id = ?1 and (release_flag != 1 or release_flag is null)) = 0";

	private static final String FIND_INVENTORY_COUNTS =
		"SELECT COUNT(*) as COUNT,LAST_PASSING_PROCESS_POINT_ID as PROCESS,  '' as PLANT " + 
		"FROM GALADM.HEAD_TBX " +  
		"WHERE (DEFECT_STATUS IS NULL or DEFECT_STATUS < 3) AND HOLD_STATUS <>1 " + 
		"GROUP BY LAST_PASSING_PROCESS_POINT_ID " + 
		"ORDER BY PROCESS,PLANT";

	private static final String FIND_VALID_PRODUCT_FOR_PROCESS_POINT = "SELECT A.* FROM HEAD_TBX A WHERE A.HEAD_ID = ?1 AND A.TRACKING_STATUS IN (" +
			"SELECT B.PREVIOUS_LINE_ID FROM GAL236TBX B WHERE B.LINE_ID = (" +
			"SELECT C.LINE_ID FROM GAL195TBX C WHERE EXISTS (" +
			"SELECT 1 FROM GAL214TBX D WHERE C.LINE_ID = D.LINE_ID AND D.PROCESS_POINT_ID = ?2 " +
			"FETCH FIRST ROW ONLY) FETCH FIRST ROW ONLY)) FETCH FIRST ROW ONLY";

	public Head findByDCSerialNumber(String dcNumber) {
		
		return findFirst(Parameters.with("dcSerialNumber", dcNumber));
		
	}

	public Head findByMCSerialNumber(String mcNumber) {
		
		return findFirst(Parameters.with("mcSerialNumber", mcNumber));
		
	}

	public List<Head> findAllByEngineSerialNumber(String engineId) {
		
		return findAll(Parameters.with("engineSerialNumber", engineId));
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<Date> findAllProductionDates() {
		return findAllByNativeQuery(FIND_ALL_DATES, new Parameters(), Date.class);
	}
	
	@Transactional
	public void updateTrackingAttributes(Head product){
		update(Parameters.with("lastPassingProcessPointId", product.getLastPassingProcessPointId())
				.put("trackingStatus", product.getTrackingStatus()),
				Parameters.with("headId", product.getProductId()));
	}
	
	@Transactional
	public void updateDefectStatus(String productId, DefectStatus status) {
		update(Parameters.with("defectStatus", status.getId()), Parameters.with("headId", productId));
		
	}
	
	/**
	 * Method for updating when capacity constraint is not being applied
	 * @param productId
	 * @param dunnage
	 * @return
	 */
	@Transactional
	public int updateDunnage(String productId,String dunnageId) {
		return update(Parameters.with("dunnage", dunnageId), Parameters.with("headId", productId));
	}
	
	@Transactional
	public void updateHoldStatus(String productId, int status) {
		update(Parameters.with("holdStatus", status), Parameters.with("headId", productId));
		
	}
	
	@Transactional
	public void updateEngineFiringFlag(String productId, short flag) {
		update(Parameters.with("engineFiringFlagValue", flag), Parameters.with("headId", productId));
	}
	
	@Transactional
	public void updateEngineSerialNumber(String productId, String esn) {
		update(Parameters.with("engineSerialNumber", esn),Parameters.with("headId", productId));
	}
	
	@Override
	@Transactional
	public int releaseHoldWithCheck(String productId) {
		return executeNativeUpdate(RELEASE_PRODUCT_HOLD_WITH_CHECK, Parameters.with("1", productId));
	}
	
	public List<InventoryCount> findAllInventoryCounts() {
		List<?> results =  findResultListByNativeQuery(FIND_INVENTORY_COUNTS, null);
		return toInventoryCounts(results);
	}

	public List<Head> findAllByProductionLot(String productionLot) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	public int removeDunnage(String productId) {
		return update(Parameters.with("dunnage", null), Parameters.with("headId", productId));
	}
	
	@Override
	protected String getJpqlFindAllByQsrId() {
		return FIND_ALL_BY_QSR_ID;
	}
	
	@Override
	protected String getJpqlFindAllByProcessPointIdAndTime() {
		return FIND_ALL_BY_PROCESS_POINT_ID_AND_TIME;
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
				Parameters.with("headId", productId));
	}

	@Override
	public boolean isProductTrackingStatusValidForProcessPoint(String productId, String processPointId) {
		Parameters params = Parameters.with("1", productId).put("2", processPointId);
		Head result = findFirstByNativeQuery(FIND_VALID_PRODUCT_FOR_PROCESS_POINT, params);
		return result != null;
	}
	
	@Override
	protected String getProductIdName() {
		return "headId";
	}
}
