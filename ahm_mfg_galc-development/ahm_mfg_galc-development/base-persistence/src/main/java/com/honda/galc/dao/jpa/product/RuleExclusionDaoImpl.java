/**
 * 
 */
package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.RuleExclusionDao;
import com.honda.galc.entity.product.RuleExclusion;
import com.honda.galc.entity.product.RuleExclusionId;
import com.honda.galc.service.Parameters;

import java.util.List;

/**
 * @author vf031824
 *
 */
public class RuleExclusionDaoImpl extends BaseDaoImpl<RuleExclusion, String> implements RuleExclusionDao{

	private final static String FIND_ALL_MATCH_RULES = "SELECT r.part_name, r.product_spec_code, r.create_timestamp, r.update_timestamp from galadm.rule_exclusion_tbx r join galadm.gal261tbx p on r.part_name = p.part_name where p.product_type = ?2 and r.product_spec_code ";


	public List<RuleExclusion> findAllByPartName(String partName) {
		return findAll(Parameters.with("id.partName", partName));
	}

	public List<RuleExclusion> findAllById(RuleExclusionId id, String productType) {
		Parameters params = new Parameters(); 
		
		StringBuilder sb = new StringBuilder(FIND_ALL_MATCH_RULES);
		sb.append((id.getProductSpecCode().endsWith("%")? "like " : "= "));
		sb.append("?1");
		sb.append(((id.getPartName() != null && id.getPartName().length() > 0)? " AND r.part_name= ?3 " : ""));
		sb.append(" order by r.product_spec_code asc");

		params.put("1", id.getProductSpecCode());
		params.put("2", productType);
		if(id.getPartName() != null && id.getPartName().length() > 0) params.put("3", id.getPartName());

		return findAllByNativeQuery(sb.toString(), params);
	}
}