package com.honda.galc.dao.jpa.product;


import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.BlockBuildResultId;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.service.Parameters;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class BlockBuildResultDaoImpl extends ProductBuildResultDaoImpl<BlockBuildResult,BlockBuildResultId> implements BlockBuildResultDao {

    private static final long serialVersionUID = 1L;
    public static final String SELECT_BY_PRODUCT_ID_AND_PART_NAMES = "select e from BlockBuildResult e where e.id.blockId = :productId and e.id.partName in (:partNames)";
    
    private static final String FIND_DUPLICATE_PART_SERTIAL_NUMNER_BY_PARTNAMES = "select p from BlockBuildResult p where p.resultValue=:resultValue and p.id.partName in (:partNames) and p.installedPartStatusId <> -9";
    
    private static final String FIND_DEFECT_REF_ID = 
    		"SELECT p FROM BlockBuildResult p "
    		+ "WHERE p.id.blockId = :productId and p.id.partName = :partName"
    		+ " and p.defectRefId is not null and p.defectRefId > 0";
    
    private static final String FIND_ALL_WITH_DEFECTS = 
    		"SELECT p FROM BlockBuildResult p "
    		+ "WHERE p.id.blockId = :productId and "
    		+ " p.defectRefId > 0 ";
    
	public List<BlockBuildResult> findAllByProductId(String productId){
		
		return findAll(Parameters.with("id.blockId", productId));
	}

	public BlockBuildResult findById(String productId, String partName) {
		return findByKey(new BlockBuildResultId(productId,partName));
	}
	
	public List<BlockBuildResult> findAllByPartNameAndSerialNumber(String partName, String partNumber) {
		Parameters parameters = Parameters.with("id.partName", partName);
		parameters.put("resultValue", partNumber);
		return findAll(parameters);
	}
	
	public List<BlockBuildResult> findAllByProductIdAndPartNames(String productId, List<String> partNames) {
		Parameters parameters = Parameters.with("productId", productId).put("partNames", partNames);
		return findAllByQuery(SELECT_BY_PRODUCT_ID_AND_PART_NAMES, parameters);
	}
	
	public List<BlockBuildResult> findAllByPartNameAndSerialNumber(List<String> partNames, String partSerialNumber){
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
			List<BlockBuildResult> brList = findAllByQuery(FIND_DEFECT_REF_ID, params);
			if(brList != null && !brList.isEmpty())  {
				for(BlockBuildResult hbr : brList)  {
					if(hbr != null)  {
						defectRefIdList.add(hbr.getDefectRefId());
					}
				}
			}
		}
		return defectRefIdList;
	}
	@Override
	public List<BlockBuildResult> findAllPartsWithDefect(String productId) {
		Parameters parameters = Parameters.with("productId", productId);
		return findAllByQuery(FIND_ALL_WITH_DEFECTS, parameters);
	}
	
	
}
