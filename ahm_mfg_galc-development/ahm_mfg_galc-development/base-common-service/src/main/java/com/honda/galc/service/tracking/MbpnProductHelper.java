package com.honda.galc.service.tracking;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.product.BaseOrderStructureDao;
import com.honda.galc.dao.product.OrderDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductPriorityPlanDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.MfgCtrlMadeFrom;
import com.honda.galc.entity.conf.BaseMCOrderStructure;
import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.product.Order;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductPriorityPlan;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * @author Paul Chou
 * @date Feb. 15, 2014
 * 
 */
public class MbpnProductHelper {
	
	public static String getPlanCode(String ProcessPoint) {
		return StringUtils.trimToEmpty(PropertyService.getProperty(ProcessPoint, TagNames.PLAN_CODE.name(), null));

	}
	
	/**
	 * ORDER_TBX will be replaced by GAL212TBX. This function is replaced by {@link #getCurrentPreProductionLot(String)}
	 * @param processPointId
	 * @return
	 */
	@Deprecated
	public static Order getCurrentOrder(String processPointId) {
		return ServiceFactory.getDao(OrderDao.class).getCurrentOrder(getPlanCode(processPointId));
	}
	
	public static List<Order> getUpComingOrders(String processPointId) {
		List<Order> orders = new ArrayList<Order>();
		orders.add(getCurrentOrder(processPointId));
		orders.addAll(ServiceFactory.getDao(OrderDao.class).getUpComingOrders(getPlanCode(processPointId)));
		return orders;
	}
	
	public static List<Order> getProcessedOrders(String processPointId) {
		String planCode = getPlanCode(processPointId);
		return ServiceFactory.getDao(OrderDao.class).getProcessedOrders(planCode);
	}

	public static List<ProductPriorityPlan> getProductsByPlanStatus(String processPointId, PlanStatus status, int maxResults) {
		String planCode = getPlanCode(processPointId);
		return ServiceFactory.getDao(ProductPriorityPlanDao.class).getProductsByPlanStatus(planCode, status, maxResults);
	}

	public static String getProdTypeByOrderNoAndProdSpecCode(String orderNo, String prodSpecCode) {
		
		BaseOrderStructureDao<? extends BaseMCOrderStructure, ?> dao = ServiceFactory.getDao(getStructureCreateMode().getOrderStructureDaoClass());
		List<MfgCtrlMadeFrom> madeFroms = dao.getMadeFromByOrderNoAndProdSpecCode(orderNo, prodSpecCode);
		
		if (null == madeFroms || madeFroms.isEmpty()) {
			return null;
		}
		MfgCtrlMadeFrom madeFrom = madeFroms.get(0);
		if (null == madeFrom) {
			return null;
		}
		return madeFrom.getOperationType();
	}
	
	public static StructureCreateMode getStructureCreateMode() {
		String structureMode = PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString());
		return structureMode == null ? StructureCreateMode.DIVISION_MODE : StructureCreateMode.get(structureMode);
	}
	
	/**
	 * Gets the current pre production lot.
	 *
	 * @param processPointId the process point id
	 * @return the current pre production lot
	 */
	public static PreProductionLot getCurrentPreProductionLot(String processPointId) {
		return ServiceFactory.getDao(PreProductionLotDao.class).findCurrentPreProductionLotByPlanCode(getPlanCode(processPointId));
	}

}
