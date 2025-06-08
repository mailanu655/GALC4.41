package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.product.ConrodBuildResultDao;
import com.honda.galc.entity.product.ConrodBuildResult;
import com.honda.galc.entity.product.ConrodBuildResultId;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.service.Parameters;

public class ConrodBuildResultDaoImpl extends
		ProductBuildResultDaoImpl<ConrodBuildResult, ConrodBuildResultId>
		implements ConrodBuildResultDao {

	public static final String SELECT_BY_PRODUCT_ID_AND_PART_NAMES = "select e from ConrodBuildResult e where e.id.conrodId = :productId and e.id.partName in (:partNames)";
	
    private static final String FIND_DEFECT_REF_ID = 
    		"SELECT p FROM ConrodBuildResult p "
    		+ "WHERE p.id.conrodId = :productId and p.id.partName = :partName"
    		+ " and p.defectRefId is not null and p.defectRefId > 0";
    
    private static final String FIND_ALL_WITH_DEFECTS = 
    		"SELECT p FROM ConrodBuildResult p "
    		+ "WHERE p.id.conrodId = :productId and "
    		+ " p.defectRefId > 0 ";
    
	public ConrodBuildResult findById(String productId, String partName) {
		return findByKey(new ConrodBuildResultId(productId,partName));
	}

	public List<ConrodBuildResult> findAllByPartNameAndSerialNumber(
			String partName, String partNumber) {
		Parameters parameters = Parameters.with("id.partName", partName);
		parameters.put("resultValue", partNumber);
		return findAll(parameters);
	}

	public List<ConrodBuildResult> findAllByProductId(String productId) {
		return findAll(Parameters.with("id.conrodId", productId));
	}

	public List<ConrodBuildResult> findAllByProductIdAndPartNames(String productId, List<String> partNames) {
		Parameters parameters = Parameters.with("productId", productId).put("partNames", partNames);
		return findAllByQuery(SELECT_BY_PRODUCT_ID_AND_PART_NAMES, parameters);
	}
	
	public List<ConrodBuildResult> findAllByPartNameAndSerialNumber(List<String> partNames, String partSerialNumber){
		Parameters params = Parameters.with("partNames", partNames);
		params.put("resultValue", partSerialNumber);
		return findAllByQuery(FIND_DUPLICATE_PART_SERTIAL_NUMNER_BY_PARTNAMES,params);
	}
	
	@Override
	public List<Long> findDefectRefIds(List<String> productIdList, List<String> partNameList) {
		List<Long> defectRefIdList = new ArrayList<Long>();
		for (int i = 0; i < productIdList.size(); i++) {
			Parameters params = Parameters.with("productId", productIdList.get(i));
			params.put("partName", partNameList.get(i));
			List<ConrodBuildResult> brList = findAllByQuery(FIND_DEFECT_REF_ID, params);
			if(brList != null && !brList.isEmpty())  {
				for(ConrodBuildResult hbr : brList)  {
					if(hbr != null)  {
						defectRefIdList.add(hbr.getDefectRefId());
					}
				}
			}
		}
		return defectRefIdList;
	}
	
	private static final String FIND_DUPLICATE_PART_SERTIAL_NUMNER_BY_PARTNAMES = "select p from ConrodBuildResult p where p.resultValue=:resultValue and p.id.partName in (:partNames) and p.installedPartStatusId <> -9";
	
	@Override
	public List<ConrodBuildResult> findAllPartsWithDefect(String productId) {
		Parameters parameters = Parameters.with("productId", productId);
		return findAllByQuery(FIND_ALL_WITH_DEFECTS, parameters);
	}
	
}
