package com.honda.galc.dao.jpa.product;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ProductPriorityPlanDao;
import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.product.ProductPriorityPlan;
import com.honda.galc.entity.product.ProductPriorityPlanId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @author Gangadhararao Gadde 
 * @date Nov 1, 2012
 */
public class ProductPriorityPlanDaoImpl extends BaseDaoImpl<ProductPriorityPlan, ProductPriorityPlanId> implements ProductPriorityPlanDao {

	private final String GET_PRODUCTS_TO_LOAD = "select a.product_id, substr(a.product_id, 17) as prod_type, a.tracking_sequence_no, b.product_spec_code " 
		+ " from product_priority_plan_tbx a, order_tbx b where a.plan_status_id = " 
		+ PlanStatus.ASSIGNED.getId() 
		+ " and a.order_no = b.order_no order by a.tracking_sequence_no asc";
	
	private final String SQL_COUNT_ACTUAL = "select count(e.orderNo) " 
		+ " from ProductPriorityPlan e "
		+ " where e.planStatusId > ?1 " 
		+ " and e.orderNo = ?2 ";

	
	private final String SQL_findOrderNoAndActualByDivisionIdAndPlanStatus =
		"select tppp.order_no as orderNo, count(tppp.order_no) as countActual " + 
		"FROM gal214tbx t214 " +  
		"join gal235tbx t235 on (t214.line_id=t235.zone_id) " +  
		"join mbpn_product_tbx tmp on (tmp.last_passing_process_point=t214.process_point_id) " +  
		"join product_priority_plan_tbx tppp on (tmp.current_order_no=tppp.order_no) " +  
		"WHERE 1=1 " + 
		"AND t235.division_id=?1 " +
		"AND tppp.plan_status_id>?2 " + 
		"group by tppp.order_no ";
	
	private final String FIND_PLAN_CODE_PRODUCTS_BY_STATUS =
		"select * from galadm.PRODUCT_PRIORITY_PLAN_TBX p " +
		"join galadm.order_tbx o  " +
		"on p.order_no = o.order_no and o.plan_code= ?1 " +
		"where p.PLAN_STATUS_ID = ?2";
		
	
	
	
	@Transactional
	public int doLoad(String containerId, String containerPos, Double sequenceNo) {
		return update(Parameters.with("planStatusId", PlanStatus.LOADED.getId()).put("containerId", containerId).put("containerPos", containerPos), 
				Parameters.with("id.trackingSequenceNo", sequenceNo));
	}
	
	@Transactional
	public int doUnload(String containerId) {
		return update(Parameters.with("planStatusId", PlanStatus.UNLOADED.getId()), 
				Parameters.with("containerId", containerId));
	}
	
	public List<Object[]> getAvailableProductsToLoad(int maxResults) {
		return findAllByNativeQuery(GET_PRODUCTS_TO_LOAD, null, Object[].class, maxResults);
	}
	
	public ProductPriorityPlan getNextProduct(String orderNumber, PlanStatus planStatus) {
		Parameters params = Parameters.with("orderNo", orderNumber);
		params.put("planStatusId", planStatus.getId());
		String[] orderBy = {"id.trackingSequenceNo"};
		return findFirst(params, orderBy, true);
	}
	
	public List<ProductPriorityPlan> getProductsByOrderNumber(String orderNumber) {
		Parameters params = Parameters.with("orderNo", orderNumber);
		String[] orderBy = {"id.trackingSequenceNo"};
		return findAll(params, orderBy, true);
	}

	public List<ProductPriorityPlan> getProductsByOrderNumber(String orderNumber, PlanStatus planStatus) {
		Parameters params = Parameters.with("orderNo", orderNumber);
		params.put("planStatusId", planStatus.getId());
		String[] orderBy = {"id.trackingSequenceNo"};
		return findAll(params, orderBy, true);
	}

	public List<ProductPriorityPlan> getProductsByPlanStatus(PlanStatus planStatus, int maxProducts) {
		Parameters params = Parameters.with("planStatusId", planStatus.getId());
		return findAll(params, 0, maxProducts);
	}

	public List<ProductPriorityPlan> getLastLoadedProducts(String containerId) {
		Parameters params = Parameters.with("containerId", containerId);
		params.put("planStatusId", PlanStatus.LOADED.getId());
		return findAll(params);
	}

	public Long countActual(String orderNumber, PlanStatus planStatus) {
		Query query = entityManager.createQuery(SQL_COUNT_ACTUAL);
		query.setParameter(1, planStatus.getId());
		query.setParameter(2, orderNumber);
		return (Long)query.getSingleResult();
	}

	public List<Object[]> findOrderNoAndActualByDivisionIdAndPlanStatus(String divisionId, PlanStatus planStatus){
		Parameters params = new Parameters();
		params.put("1", divisionId);
		params.put("2", planStatus.getId());
		return findAllByNativeQuery(SQL_findOrderNoAndActualByDivisionIdAndPlanStatus, params, Object[].class);
	}

	public List<ProductPriorityPlan> getProductsByPlanStatus(String planCode, PlanStatus planStatus, int maxProducts) {
		if(StringUtils.isEmpty(planCode)){
			return getProductsByPlanStatus(planStatus, maxProducts);
		} else {
			Parameters params = new Parameters();
			params.put("1", planCode);
			params.put("2", planStatus.getId());
			return findAllByNativeQuery(FIND_PLAN_CODE_PRODUCTS_BY_STATUS, params, ProductPriorityPlan.class);
		}
	}

	public Double findMaxSequence(String trackingStatus) {
		try {
			return max("id.trackingSequenceNo", Double.class, Parameters.with("id.trackingStatus",trackingStatus));
		} catch (Exception e) {
			return 0.0;
		}
	}
}
