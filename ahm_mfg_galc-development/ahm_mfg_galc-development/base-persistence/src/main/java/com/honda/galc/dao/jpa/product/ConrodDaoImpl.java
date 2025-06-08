package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.Conrod;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.Parameters;

public class ConrodDaoImpl extends DiecastDaoImpl<Conrod> implements ConrodDao {

	private static final String RELEASE_PRODUCT_HOLD_WITH_CHECK = "update galadm.conrod_tbx set hold_status = 0  where conrod_id = ?1 and (select count(*) from galadm.gal147tbx where product_id = ?1 and (release_flag != 1 or release_flag is null)) = 0";

	private static final String FIND_INVENTORY_COUNTS =
		"SELECT COUNT(*) as COUNT,LAST_PASSING_PROCESS_POINT_ID as PROCESS,  '' as PLANT " + 
		"FROM GALADM.CRANKSHAFT_TBX " +  
		"WHERE (DEFECT_STATUS IS NULL or DEFECT_STATUS < 3) AND HOLD_STATUS <>1 " + 
		"GROUP BY LAST_PASSING_PROCESS_POINT_ID " + 
		"ORDER BY PROCESS,PLANT";

    private static final String FIND_ALL_BY_QSR_ID = "select p from Block p, HoldResult h where h.id.productId =  p.blockId and h.qsrId = :qsrId";
    private static final String FIND_ALL_BY_PROCESS_POINT_ID_AND_TIME = "select p from Block p, BlockHistory h where h.id.processPointId = :processPointId and h.id.blockId = p.blockId and h.id.actualTimestamp >= :startTime and h.id.actualTimestamp <= :endTime";    

    private static final String FIND_VALID_PRODUCT_FOR_PROCESS_POINT = "SELECT A.* FROM CONROD_TBX A WHERE A.CONROD_ID = ?1 AND A.TRACKING_STATUS IN (" +
			"SELECT B.PREVIOUS_LINE_ID FROM GAL236TBX B WHERE B.LINE_ID = (" +
			"SELECT C.LINE_ID FROM GAL195TBX C WHERE EXISTS (" +
			"SELECT 1 FROM GAL214TBX D WHERE C.LINE_ID = D.LINE_ID AND D.PROCESS_POINT_ID = ?2 " +
			"FETCH FIRST ROW ONLY) FETCH FIRST ROW ONLY)) FETCH FIRST ROW ONLY";

    @Autowired
	private InstalledPartDao installedPartDao;
	
	@Transactional
	public void updateEngineSerialNumber(String productId, String esn) {
		update(Parameters.with("engineSerialNumber", esn),Parameters.with("conrodId", productId));
	}

	@Transactional
	public int releaseHoldWithCheck(String productId) {
		return executeNativeUpdate(RELEASE_PRODUCT_HOLD_WITH_CHECK, Parameters.with("1", productId));
	}

	@Transactional
	public void updateTrackingAttributes(Conrod product) {
		update(Parameters.with("lastPassingProcessPointId", product.getLastPassingProcessPointId())
				.put("trackingStatus", product.getTrackingStatus()),
				Parameters.with("conrodId", product.getProductId()));
	}

	@Transactional
	public void updateDefectStatus(String productId, DefectStatus status) {
		update(Parameters.with("defectStatus", status.getId()), Parameters.with("conrodId", productId));
	}

	@Transactional
	public void updateHoldStatus(String productId, int status) {
		update(Parameters.with("holdStatus", status), Parameters.with("conrodId", productId));
	}

	public List<InventoryCount> findAllInventoryCounts() {
		List<?> results =  findResultListByNativeQuery(FIND_INVENTORY_COUNTS, null);
		return toInventoryCounts(results);
	}

	@Transactional
	public int removeDunnage(String productId) {
		return update(Parameters.with("dunnage", null), Parameters.with("conrodId", productId));
	}

	@Transactional
	public Conrod save(Conrod conrod, InstalledPart installedPart) {
		getInstalledPartDao().save(installedPart);
		return save(conrod);
	}

	public List<Conrod> findAllByEngineSerialNumber(String engineId) {
		return findAll(Parameters.with("engineSerialNumber", engineId));
	}

	public InstalledPartDao getInstalledPartDao() {
		return installedPartDao;
	}

	public void setInstalledPartDao(InstalledPartDao installedPartDao) {
		this.installedPartDao = installedPartDao;
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
	 * Method for updating when capacity constraint is not being applied
	 * @param productId
	 * @param dunnage
	 * @return
	 */
	@Transactional
	public int updateDunnage(String productId,String dunnageId) {
		return update(Parameters.with("dunnage", dunnageId), Parameters.with("conrodId", productId));
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
				Parameters.with("conrodId", productId));
	}	

	@Override
	public boolean isProductTrackingStatusValidForProcessPoint(String productId, String processPointId) {
		Parameters params = Parameters.with("1", productId).put("2", processPointId);
		Conrod result = findFirstByNativeQuery(FIND_VALID_PRODUCT_FOR_PROCESS_POINT, params);
		return result != null;
	}
	
	@Override
	protected String getProductIdName() {
		return "conrodId";
	}
}
