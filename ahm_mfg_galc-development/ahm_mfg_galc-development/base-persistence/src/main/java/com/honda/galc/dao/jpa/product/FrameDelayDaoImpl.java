
package com.honda.galc.dao.jpa.product;


import java.util.List;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.FrameDelayDao;
import com.honda.galc.entity.product.FrameDelay;
import com.honda.galc.entity.product.FrameDelayId;
import com.honda.galc.service.Parameters;


public class FrameDelayDaoImpl extends BaseDaoImpl<FrameDelay,FrameDelayId> implements FrameDelayDao {

	private static final String GET_STRAGGLER_LIST = "SELECT frame.PRODUCT_ID AS CURRENT_VIN, pp.PROCESS_POINT_ID AS PP_DELAYED_AT,"
		+ "frame.LAST_PASSING_PROCESS_POINT_ID AS PP_CURRENTLY_AT,pp.CURRENT_PROD_LOT AS CURRENT_PRODUCTION_LOT "
		+ "FROM GALADM.GAL214TBX pp  LEFT JOIN GALADM.GAL143TBX frame "
		+ "ON pp.CURRENT_PROD_LOT = frame.PRODUCTION_LOT "
		+ "LEFT JOIN GALADM.GAL215TBX pphist "
		+ "ON pp.PROCESS_POINT_ID = pphist.PROCESS_POINT_ID "
		+ "AND frame.PRODUCT_ID = pphist.PRODUCT_ID "
		+ "WHERE pp.PROCESS_POINT_ID = ?1 AND pphist.ACTUAL_TIMESTAMP  IS NULL";

	public List<FrameDelay> findDelayedVinList(String vin, String processPointId)
	{
		return this.findAll(Parameters.with("id.currentVin", vin).put("id.ppDelayedAt", processPointId).put("currentDelayedFlag", 1));
	}

	public List<Object[]> getStragglerList(String processPointId)
	{
		Parameters params = Parameters.with("1", processPointId);
		return findAllByNativeQuery(GET_STRAGGLER_LIST, params, Object[].class);
	}
}

