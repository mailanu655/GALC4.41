
package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.data.ProductType;
import com.honda.galc.dto.StragglerCodeAssignmentDto;
import com.honda.galc.entity.product.Straggler;
import com.honda.galc.entity.product.StragglerId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 05, 2015
 */
public interface StragglerDao extends IDaoService<Straggler, StragglerId> {

	/**
	 * Find all current stragglers for the given PP_DELAYED_AT
	 */
	public List<Straggler> findCurrentByPpDelayedAt(String ppDelayedAt);
	/**
	 * Find all Straggler Code Assignment data for the give PP_DELAYED_AT and product type
	 */
	public List<StragglerCodeAssignmentDto> findStragglerCodeAssignmentData(String ppDelayedAt, ProductType productType, boolean findCurrent, boolean findNonCurrent);
	public List<Straggler> findStragglerProductList(String vin, String processPointId);
	public List<Object[]> findPrevUnProcessedLots(String plantName,String processPointId,String currentProdLot, String stragglerType);
	public List<Object[]> findPrevUnProcessedLotsByKDLot(String plantName,String processPointId,String currentProdLot, String stragglerType);

}
