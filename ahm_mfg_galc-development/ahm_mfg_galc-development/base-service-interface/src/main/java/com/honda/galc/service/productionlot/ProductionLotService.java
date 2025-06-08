package com.honda.galc.service.productionlot;

import java.util.List;
import java.util.Map;

import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.IService;

public interface ProductionLotService extends IService {
	public List<Object[]> getProcessingBody(String componentId);
	public Map<String, List<String>> getProductionProgress(String componentId, Integer prodProgressType, Boolean allowDBUpdate);
	
	/**
	 * Method to get the Processing Body information receiving the process point and the tracking line id
	 * @param processPoints
	 * @param lineIds
	 * @return
	 */
	public List<Object[]> getProcessingBody ( final String processPoints, final String lineIds);

	/**
	 * Method to get production progress with process point On and Off
	 * @param processLocation
	 * @param plantCode
	 * @param createTimestamp
	 * @param processPointAmOn
	 * @param processPointAmOff
	 * @return
	 */
	public List<Object> getProductionProgress(final String processLocation, final String plantCode, final java.util.Date createTimestamp, final String processPointAmOn, final String processPointAmOff);
	
	/**
	 * return and object of ProductionLot by product_id
	 * @param productId
	 * @return
	 */
	public ProductionLot getProductionLot(final String productId);
	
	/**
	 * update production Lot
	 * @param productionLot
	 */
	public void updateProductionLot(final ProductionLot productionLot);
}
