package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.IDaoService;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface LotControlRuleDao extends IDaoService<LotControlRule, LotControlRuleId> {

    List<LotControlRule> findAllByProcessPoint(String ppId);
    
    /*
     * find lot control rule by the combination of processPoint, partName and MTOC
     */
    List<LotControlRule> findAllById(LotControlRuleId id);

    /*
     * Find all lot control rules matching and the given process point, part name and YMTO/MTOC with 
     * wild card
     */
	@SuppressWarnings("unchecked")
	List findAllRulesAndProcessPoints(LotControlRuleId id,String productType);
	

	/**
	 * This method is to be used in ManualLotControlRepair client instead off findAllRulesAndProcessPoints(LotControlRuleId ruleId, String productType). 
	 * It partially assembles objects to improve performance. 
	 * This method does not load image property in PartSpec as it is not used in ManualLotControlRepair client.
	 * This methotd should be treated as 'Read Only' and it should not be used to retrieve entities for updates.
	 * @param ruleId
	 * @param productType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	List findAllRulesAndProcessPointsAssemble(LotControlRuleId ruleId, String productType);
	
	/*
     * Find all lot control rules matching and the given process point, part name and productSpecCode 
     */
	@SuppressWarnings("unchecked")
	List findAllRulesAndProcessPointsForMbpn(LotControlRuleId id,String productType);
	
	@SuppressWarnings("unchecked")
	List findAllRulesAndProcessPoints(LotControlRuleId id);
    
	List<LotControlRule> findAllByModelYear(String year);
    
    public List<LotControlRule> findAllForRule(String processPointId);
    
	/*
	 * Find all lot control rules referencing a particular part
	 */
	List<LotControlRule> findAllByPartName(PartName partName);
	
	List<LotControlRule> findAllByPartName(String partName);
	
	/**
	 * Find process point id by MTOC and part name.
	 *
	 * @param mtoc the MTOC
	 * @param partName the part name
	 * @return the process point id
	 */
	public String findProcessPointIdByMtocAndPartName(String mtoc, String partName);
	public List<LotControlRule> findRuleByMtocAndPartName(PartByProductSpecCode part, String partName);
	public List<LotControlRule> findRuleByYmtociAndPartName(PartByProductSpecCode part, String partName);
	public List<LotControlRule> findRuleByMbpnAndPartName(PartByProductSpecCode part, String partName);
	
	/*
	 * Finds all lot control rules by process point id and product spec
	 */
	public List<LotControlRule> findAllByProcessPointAndProductSpec(String processPointId, ProductSpec spec);
	
	public List<LotControlRule> findAllByProcessPointAndProductSpec(String processPointId, String productType, ProductSpec spec);
	
	public List<String> findAllPartNames(List<String> processPointId, ProductSpec spec, boolean partConfirmCheck);

	List<LotControlRule> findAllById(LotControlRuleId id, String productType);
	
	public List<LotControlRule> findAllByProcessPointIdAndPartName(String processPoint, String partName);
	
	public long count(LotControlRuleId id, List<String> processPointIds);

	/**
	 * Find all existing group id for the given product type
	 * @param product
	 */
	public List<String> findAllGroupIdsByProductType(String productType);

	/**
	 * Find all exiting Rules belong to the given group, product type and given product spec code
	 * 
	 * @param currentGroupId
	 * @param currentProductType
	 * @param specCode
	 */
	public List<LotControlRule> findAllByProducttyepAndSpecCode(String productType, String specCode);
	
	
	/**
	 * Find all existing rules containing a specific part id (PART_ID, PART_NAME)
	 * 
	 * @param partId
	 * @param partName
	 * @return A list containing all the LotControlRules that match the given part id.
	 */
	public List<LotControlRule> findAllByPartId(String partId, String partName);
	
	public List<LotControlRule> getLotControlRuleByProductSpecCodeProcessId(String processPointId, String productSpecCode, Integer sequenceNumber);
	
	public List<LotControlRule> findRulesByPartNameAndUniqueFlag(String partName, int uniqueFlag);
	
	public List<LotControlRule> findAllByProductSpecCode(String productSpecCode);
	
	/**
	 * Find all existing rules for the given PRODUCT_SPEC_CODE field.
	 */
	public List<LotControlRule> findAllForProductSpecCode(String productSpecCode);
}
