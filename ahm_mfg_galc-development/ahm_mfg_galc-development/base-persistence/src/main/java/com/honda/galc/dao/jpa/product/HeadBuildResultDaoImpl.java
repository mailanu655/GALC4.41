package com.honda.galc.dao.jpa.product;


import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.product.HeadBuildResultDao;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.HeadBuildResultId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>HeadBuildResultDaoImpl Class description</h3>
 * <p> HeadBuildResultDaoImpl description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 22, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class HeadBuildResultDaoImpl extends ProductBuildResultDaoImpl<HeadBuildResult,HeadBuildResultId> implements HeadBuildResultDao{

    private static final long serialVersionUID = 1L;
    public static final String SELECT_BY_PRODUCT_ID_AND_PART_NAMES = "select e from HeadBuildResult e where e.id.headId = :productId and e.id.partName in (:partNames)";
    private static final String FIND_DEFECT_REF_ID = 
    		"SELECT p FROM HeadBuildResult p "
    		+ "WHERE p.id.headId = :productId and p.id.partName = :partName"
    		+ " and p.defectRefId is not null and p.defectRefId > 0";
    
    private static final String FIND_ALL_WITH_DEFECTS = 
    		"SELECT p FROM HeadBuildResult p "
    		+ "WHERE p.id.headId = :productId and "
    		+ " p.defectRefId > 0 ";
    
	public List<HeadBuildResult> findAllByProductId(String productId){

		return findAll(Parameters.with("id.headId", productId));
		
	}

	public HeadBuildResult findById(String productId, String partName) {
		return findByKey(new HeadBuildResultId(productId,partName));
	}

	public List<HeadBuildResult> findAllByPartNameAndSerialNumber(String partName, String partNumber) {
		Parameters parameters = Parameters.with("id.partName", partName);
		parameters.put("resultValue", partNumber);
		return findAll(parameters);
	}

	public List<HeadBuildResult> findAllByProductIdAndPartNames(String productId, List<String> partNames) {
		Parameters parameters = Parameters.with("productId", productId).put("partNames", partNames);
		return findAllByQuery(SELECT_BY_PRODUCT_ID_AND_PART_NAMES, parameters);
	}
	
	public List<HeadBuildResult> findAllByPartNameAndSerialNumber(List<String> partNames, String partSerialNumber){
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
			List<HeadBuildResult> buildResultList = findAllByQuery(FIND_DEFECT_REF_ID, params);
			if(buildResultList != null && !buildResultList.isEmpty())  {
				for(HeadBuildResult hbr : buildResultList)  {
					if(hbr != null)  {
						defectRefIdList.add(hbr.getDefectRefId());
					}
				}
			}
		}
		return defectRefIdList;
	}
	
	private static final String FIND_DUPLICATE_PART_SERTIAL_NUMNER_BY_PARTNAMES = "select p from HeadBuildResult p where p.resultValue=:resultValue and p.id.partName in (:partNames) and p.installedPartStatusId <> -9";
	
	@Override
	public List<HeadBuildResult> findAllPartsWithDefect(String productId) {
		Parameters parameters = Parameters.with("productId", productId);
		return findAllByQuery(FIND_ALL_WITH_DEFECTS, parameters);
	}
	
}
