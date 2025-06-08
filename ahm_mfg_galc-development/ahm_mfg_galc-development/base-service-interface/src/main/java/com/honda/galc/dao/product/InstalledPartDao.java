package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.honda.galc.data.InstalledPartDetail;
import com.honda.galc.data.MCInstalledPartDetailDto;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.InstalledPartDetailsDto;
import com.honda.galc.dto.SubAssemblyPartListDto;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;


/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface InstalledPartDao extends ProductBuildResultDao<InstalledPart, InstalledPartId> {

    public List<InstalledPart> findAllByPartNameAndSerialNumber(String partName, String partNumber);
   
    public List<InstalledPart> findAllValidParts(String productId, List<String> partNames);
    
    public List<InstalledPart> findAllByProductIdAndProcessPoint(String productId, String processPoint);
    
    public List<InstalledPart> findAllByProductId(String productId);
    
    public List<InstalledPart> findAllByPartSerialNumber(String serialNumber);
    
    public List<InstalledPart> findAllByProductIdAndPartSerialNo(String productId, String partNumber);
    
    public boolean isProcessed(String productId, List<String> partNames);
	
    public int deleteProdIds(List<String> prodIds);
	
    public void repairHeadless(String productId, String partName, String associateNo, String updateReason);
	
    public List<InstalledPartDetail> getBadMeasurements(String productId, String processPointId);
 
    public List<InstalledPartDetail> getMissingPartDetails(String productId, String processPointId);

    public List<InstalledPartDetail> getAllInstalledPartDetails(String productId, String productType, String processPointId, boolean partConfirmCheck, boolean useProcessPoint, boolean limitRulesByDivision,boolean enableRepairCheck);
	
	public List<InstalledPartDetailsDto> getInstalledPartDetails(String processPoint, String productId, ProductType productType, String productSpecCode);
	
	public int updateProductId(String productId, String oldProductId);
	
	public int moveAllData(String newProductId,String currentProductId);
	
	public List<InstalledPartDetail> getNGParts(String productId,String lineId);

	public InstalledPart findLatestInstalledByProcessPoint(String processPointId);
	
	public int updateInstalledPartStatus(String productId, List<String> partNames, int installedPartStatus);
	
	public String getPartSerialNumber(String productID, String partName);
	
	public String getLatestPartSerialNumber(String productID, String partName);
	
	public List<InstalledPart> saveAll(List<InstalledPart> partList,boolean isSaveHistory);
	
	public List<InstalledPart> findAllForDateRange(String partName, Timestamp startTimestamp, Timestamp endTimestamp);
	
	public List<MCInstalledPartDetailDto> getInstalledPartDetails(String sql, String productId);
	
	public InstalledPart findByProductIdAndPartialName(String productId,String partialPartName);
	
	public List<Object[]> findSubParts(String serialNumber, String productIdPrefix);
	
	public Boolean isPartSerialNumberExists(String partSerialNumber) ;
	
	public List<InstalledPart> findAllEngineByEngineProductIdAndPartNames(String productType, String engineProductId, List<String> partNames);
	
	public List<InstalledPart> findAllByProductIdAndPartNames(String productId, List<String> partNames);
	
	public List<SubAssemblyPartListDto> findSubPartsByProductId(String productId);
	
	public void deleteInstalledParts(String productId, List<String> partNames);
	public List<Object[]> findByPartName(String partName,Timestamp startTs, Timestamp endTs);

	/**
	 * Find all installed part have the same part serial number on the part name list
	 * @param partNames
	 * @param partSerialNumber
	 * @return
	 */
	public List<InstalledPart> findAllByPartNameAndSerialNumber(List<String> partNames, String partSerialNumber);
	
	public Map<String, Boolean> findSpecialUnitsInProcessPoint(String processPoint, String loggedUser,Integer noOfDays, List<MCOperationRevision> operationsInProcessPoint );

	public List<InstalledPart> findAllInstalledPartByCommonName(String productId, String commonName);
		
	public List<InstalledPart> findAllInstalledPartByCommonNameList(String productId, List<String> commonNameList, String productType);
		
	public String findOneInstalledPartByCommonName(String productID, String partName);
	
	public InstalledPart findByUnitOrCommonName(String productId, String partName);
	
	public List<InstalledPart> findLinkedParts(String childPartName, String productId);

	List<InstalledPart> findAllPartsWithDefect(String productId);

	InstalledPart findByRefId(Long refId);
	
	public List<Long> findDefectRefIds(List<String> productIdList, List<String> partNameList);

	public List<InstalledPart> findMbpnParentInstalledPart(String partSerialNumber, String subProductType);
}