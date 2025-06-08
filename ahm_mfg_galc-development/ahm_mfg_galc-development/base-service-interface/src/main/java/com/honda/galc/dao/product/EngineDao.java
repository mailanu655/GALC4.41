package com.honda.galc.dao.product;

import java.util.List;
import java.util.Map;

import com.honda.galc.dto.EngineNumberingDto;
import com.honda.galc.entity.product.Engine;



/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface EngineDao extends ProductDao<Engine> {

	public List<Engine> findAllByProductSequence(String processPointId,String currentProductId,int processedSize,int upcomingSize);
	public List<Engine> findAllByInProcessProduct(String currentProductId,int processedSize,int upcomingSize);
	
	public List<Engine> findAllByLineIds(String[] lineIds);
	public List<Engine> findAllByMissionSn(String msn);
	
	/**
	 * Update the specific Engine's Firing Flag
	 * @param productId
	 * @param engineFiringFlag
	 */
	public void updateFiringFlag(String productId, boolean engineFiringFlag);
	public List<Engine> findByTrackingStatus(String trackingStatus) ;
	public List<Engine> findByPartName(String lineId, int prePrintQty, int maxPrintCycle, String ppid, String partName);

	public void updateEngineFiringFlag(String productId, short flag);
	public List<Object[]> getEngineLineShipmentStatusData(String productionLot,String scrapLineId,String exceptionalLineId);
	
	public List<Engine> findEnginesAndBeginCureTimeByLineIdsAndPartName(String[] lineIds, String partName);

	public void updateTrackStatusByProductIds(String[] productIds, String updatedTrackStatus);
	
	public List<EngineNumberingDto> findAllRecentStampedEngines(String onProcessPointId, String nextProcessPointId);
	
	public List<Object[]> findValidEins();
	
	public List<String> findAvailEnginesByMTOC(String plantCode, String productSpecCode);
	
	public Engine findNextProduct(String productId);
	
	public List<String> findAllEngineMTOCInTrackingArea(String trackingArea);
	
	public List<Map<String, Object>> findAllSalesWarrantyFrData(List<String>  eins, String processPointId, String blockPartName, String headPartName, String transmissionPartName);
	
	public Engine saveEngine(Engine engine);
	
	public void updateLastPassingProcessPointIdByProductIds(String[] productIds, String lastPassingProcessPontId);
	
	public Engine findEngineByVin(String vin);
	
	public List<String> findAllEnginePlantCodes(); 

}
