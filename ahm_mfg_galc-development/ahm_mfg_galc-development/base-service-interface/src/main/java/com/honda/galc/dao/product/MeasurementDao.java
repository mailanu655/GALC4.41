package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.service.IDaoService;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface MeasurementDao extends IDaoService<Measurement, MeasurementId> {

	List<InstalledPart> findMeasurementsForInstalledParts(List<InstalledPart> installedParts);

	/**
	 * Find all the measurements for the installed parts, optionally order by the measurement sequence ascendingly.
	 * @param installedParts
	 * @param orderByMeasSeq
	 * @return
	 */
	List<InstalledPart> findMeasurementsForInstalledParts(List<InstalledPart> installedParts, boolean orderByMeasSeq);

	List<Measurement> findAllByProductId(String productId);
	
	void deleteAll(String productId, String partName);

	/** delete all measurement data matching array of product ids
	 * 
	 * @param prodIds
	 */
	public int deleteProdIds(List <String> prodIds);

	List<Measurement> findAll(String productId, String partName);
	/**
	 * find all measurement data by productId and partName, order by measurement sequence number.
	 * @param productId
	 * @param partName
	 * @param isAscending
	 * @return
	 */
	public List<Measurement> findAllOrderBySequence(String productId, String partName, boolean isAscending);
	
	public int updateProductId(String productId, String oldProductId);
	
	public int moveAllData(String newProductId,String currentProductId);
	
	public List<Measurement> saveAll(List<Measurement> measurements);
	
	public List<Measurement> saveAll(List<Measurement> measurements, boolean isSaveHistory);
	
	public List<Measurement> findAllByProductIdAndPartNames(String productId,List<String> partNames);
	
	public void deleteAllByProductIdAndPartNames(String productId,List<String> partNames);

	Measurement findByRefId(Long refId);
	
}
