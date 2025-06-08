package com.honda.galc.dao.jpa.product;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartDao;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Part;
import com.honda.galc.entity.product.PartId;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

public class PartDaoImpl extends BaseDaoImpl<Part,PartId> implements PartDao {

    private static final long serialVersionUID = 1L;
	private static final String UPDATE_206_ALLOW_DUPLICATE ="UPDATE Part p set p.allowDuplicates = :uniqueFlag ";
	private static final Integer UNIQUE_FLAG_CHECKED = 1;
	private static final Integer UNIQUE_FLAG_NOT_CHECKED = 0;
    
	public List<Part> findAllByPartName(String partName) {
		
		return findAll(Parameters.with("id.partName", partName));

	}


	public List<Part> findPartByLotCtrRule(LotControlRule rule) {
		Parameters params = prepareLotCtrRuleParameters(rule);
		return findAllByNamedQuery("Part.getPartByLotCtrRule", params);
		
	}

	@Transactional
	public void updateAllowDuplicate(String partName, int uniqueFlag) {
		Boolean update206AllowUpdate = false;
		if(UNIQUE_FLAG_NOT_CHECKED == uniqueFlag) {
			List<LotControlRule> rules = ServiceFactory.getDao(LotControlRuleDao.class).findRulesByPartNameAndUniqueFlag(partName, 1);
			if(null != rules && rules.size() > 0) {
				update206AllowUpdate = true;
			}
		}
		Parameters params = new Parameters();
		if(UNIQUE_FLAG_CHECKED == uniqueFlag || update206AllowUpdate == true) {
			params = Parameters.with("uniqueFlag", UNIQUE_FLAG_CHECKED);
		} else {
			params = Parameters.with("uniqueFlag", UNIQUE_FLAG_NOT_CHECKED);
		}
		executeUpdate(UPDATE_206_ALLOW_DUPLICATE, params);
	}

	private Parameters prepareLotCtrRuleParameters(LotControlRule rule) {
		Parameters params = Parameters.with("partName", rule.getId().getPartName());
		params.put("modelYear", rule.getId().getModelYearCode());
		params.put("modelCode", rule.getId().getModelCode());
		params.put("modelType", rule.getId().getModelTypeCode());
		params.put("optionCode", rule.getId().getModelOptionCode());
		params.put("extColor", rule.getId().getExtColorCode());
		params.put("intColor", rule.getId().getIntColorCode());
		
		return params;
	}

}
