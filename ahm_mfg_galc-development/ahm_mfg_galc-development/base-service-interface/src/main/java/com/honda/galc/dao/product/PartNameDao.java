package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dto.ExtRequiredPartDto;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>PartNameDao Class description</h3>
 * <p> PartNameDao description </p>
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
 * Mar 26, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface PartNameDao extends IDaoService<PartName, String> {

	PartName findPartNameByLotCtrRule(LotControlRule rule);

	List<LotControlRule> findPartNameForLotCtrRules(List<LotControlRule> rules);

	List<PartName> findAllByProductType(String productType);
	
	List<PartName> findAllByProductTypeAndExternalReq(String productType);
	
	List<PartName> findAllByProductTypeAndLETReq(String productType);
	
	List<ExtRequiredPartDto> findAllWithExtRequired(String productType);
	
	List<String> findAllProductTypesWithExtRequired();
	
	List<String> findPartNamesForMbpnByMainNo(String mainNo);
}
