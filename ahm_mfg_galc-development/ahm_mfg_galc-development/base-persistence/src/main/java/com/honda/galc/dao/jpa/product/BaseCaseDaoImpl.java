package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dto.InventoryCount;
import com.honda.galc.entity.product.Case;
import com.honda.galc.service.Parameters;
/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>BaseCaseDaoImpl</code> is a parameterized super class for Case type/sub entities.
 * REMARK : this class is needed as parameterized super class for CaseDaoImpl, MissionCaseDaoImpl and TorqueCaseDaoImpl.
 * Currently we still need to have concrete CaseDaoImpl as it is used by CaseTracker. When CaseTracker or Trackers in general are updated to 
 * to do dynamic lookups for ProductDaos by ProductType(currenlty daos are injected or looked up by Class), then we can make CaseDaoImpl parameterized and  moved code back from BaseCaseDaoImpl to CaseDaoImpl,
 * and BaseCaseDaoImpl could  be deleted. 
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * @created Jun 29, 2015
 */

public abstract class BaseCaseDaoImpl <T extends Case> extends DiecastDaoImpl<T> {

	private static final String FIND_INVENTORY_COUNTS =
		"SELECT COUNT(*) as COUNT,LAST_PASSING_PROCESS_POINT_ID as PROCESS,  '' as PLANT " + 
		"FROM GALADM.CASE_TBX " +  
		"WHERE (DEFECT_STATUS IS NULL or DEFECT_STATUS < 3) AND HOLD_STATUS <> 1 " + 
		"GROUP BY LAST_PASSING_PROCESS_POINT_ID " + 
		"ORDER BY PROCESS,PLANT";
	private static final String RELEASE_PRODUCT_HOLD_WITH_CHECK = "update galadm.case_tbx set hold_status = 0  where product_id = ?1 and (select count(*) from galadm.gal147tbx where product_id = ?1 and (release_flag != 1 or release_flag is null)) = 0";
	
	// Remark: OpenJpa ignores Discriminator  when used finder by PK/EntityManager.find(entityClass, pk).
	// - additional type check needs to be applied.
	@Override
	public T findByKey(String id) {
		T entity = super.findByKey(id);
		if (getEntityClass().isInstance(entity)) {
			return entity;
		} else {
			return null;
		}
	}
	
	public List<InventoryCount> findAllInventoryCounts() {
		List<?> results =  findResultListByNativeQuery(FIND_INVENTORY_COUNTS, null);
		return toInventoryCounts(results);
	}

	@SuppressWarnings("boxing")
	@Transactional
	public void updateHoldStatus(String productId, int status) {
		update(Parameters.with("holdStatus", status), Parameters.with("productId", productId));
	}

	@Override
	@Transactional
	public int releaseHoldWithCheck(String productId) {
		return executeNativeUpdate(RELEASE_PRODUCT_HOLD_WITH_CHECK, Parameters.with("1", productId));
	}
	
	@Override
	protected String getProductIdName() {
		return "productId";
	}
}
