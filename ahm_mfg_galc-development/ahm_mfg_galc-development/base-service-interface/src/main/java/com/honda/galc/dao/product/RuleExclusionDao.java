/**
 * 
 */
package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.RuleExclusion;
import com.honda.galc.entity.product.RuleExclusionId;
import com.honda.galc.service.IDaoService;

/**
 * @author VF031824
 *
 */
public interface RuleExclusionDao extends IDaoService<RuleExclusion, String> {
	
	List<RuleExclusion> findAllByPartName(String partName);
	
	List<RuleExclusion> findAllById(RuleExclusionId id, String productType);

}
