package com.honda.galc.dao.jpa.product;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dto.ExtRequiredPartDto;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.service.Parameters;


/**
 * 
 * <h3>PartNameDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartNameDaoImpl description </p>
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
 * @author Paul Chou
 * Mar 16, 2011
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class PartNameDaoImpl extends BaseDaoImpl<PartName,String> implements PartNameDao {
	
	private static String FIND_ALL_WITH_EXT_REQUIRED = "select p.PART_NAME, p.EXTERNAL_REQUIRED, p.LET_CHECK_REQUIRED from galadm.gal261tbx p "
			+ "where p.product_type = ?1";
	
	private static String FIND_ALL_PRODUCT_TYPES_WITH_EXT_REQUIRED = "select distinct p.productType from PartName p where p.extRequired = 1";
	
	private static String FIND_DISTINCT_PART_NAMES_FOR_MBPN_BY_MAIN_NO = "select distinct p.part_name from galadm.gal245tbx p where p.product_spec_code like ?1";
	
    private static final long serialVersionUID = 1L;
    
	public PartName findPartNameByLotCtrRule(LotControlRule rule) {
		
		PartName result = findByKey(rule.getId().getPartName());
		
		return result;
	}

	public List<LotControlRule> findPartNameForLotCtrRules(List<LotControlRule> rules) {
		for(LotControlRule rule : rules){
			rule.setPartName(findPartNameByLotCtrRule(rule));
		}
		
		return rules;
	}

	public List<PartName> findAllByProductType(String productType) {
		return findAll(Parameters.with("productType", productType), new String[]{"partName"});
	}
	
	public List<PartName> findAllByProductTypeAndExternalReq(String productType) {
		return this.findAll(Parameters.with("productType", productType).put("extRequired", 1));
	}
	
	public List<PartName> findAllByProductTypeAndLETReq(String productType) {
		return this.findAll(Parameters.with("productType", productType).put("LETRequired", 1));
	}
	
	public List<ExtRequiredPartDto> findAllWithExtRequired(String productType){
		Parameters params = Parameters.with("1", productType);
		return findAllByNativeQuery(FIND_ALL_WITH_EXT_REQUIRED, params, ExtRequiredPartDto.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findAllProductTypesWithExtRequired() {
		return findResultListByQuery(FIND_ALL_PRODUCT_TYPES_WITH_EXT_REQUIRED, null);
	}
	
	public List<String> findPartNamesForMbpnByMainNo(String mainNo) {
		Parameters params = Parameters.with("1", mainNo + "%");

		return findAllByNativeQuery(FIND_DISTINCT_PART_NAMES_FOR_MBPN_BY_MAIN_NO, params, String.class);
	}
}
