package com.honda.galc.dao.jpa.lcvinbom;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.honda.galc.entity.lcvinbom.ModelPartApproval;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.lcvinbom.ModelPartDao;
import com.honda.galc.dto.lcvinbom.ModelPartLotFilterDto;
import com.honda.galc.dto.lcvinbom.VinBomDesignChangeDto;
import com.honda.galc.entity.enumtype.VinBomActiveStatus;
import com.honda.galc.entity.lcvinbom.ModelPart;

import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

public class ModelPartDaoImpl extends BaseDaoImpl<ModelPart, Long> 
implements ModelPartDao {

	private static final String FIND_SYSTEM_NAMES_BY_PART_NUMBER = "SELECT DISTINCT LET_SYSTEM_NAME FROM MODEL_PART WHERE DC_PART_NUMBER = ?1 ";
	
	private static final String FIND_ALL_APPROVED_INACTIVE_BY_PROD_SPEC_AND_SYSTEM = "SELECT A.* FROM LCVINBOM.MODEL_PART A	WHERE A.PRODUCT_SPEC_WILDCARD = SUBSTR(?1,1,7) AND A.LET_SYSTEM_NAME = ?2 AND EXISTS (SELECT * FROM LCVINBOM.MODEL_PART_APPROVAL C WHERE A.MODEL_PART_ID = C.MODEL_PART_ID AND C.APPROVE_STATUS = 1) AND A.ACTIVE = 0";
	
	private static final String FIND_ALL_ACTIVE_BY_PROD_SPEC_AND_SYSTEM = "SELECT * FROM LCVINBOM.MODEL_PART A WHERE A.PRODUCT_SPEC_WILDCARD = SUBSTR(?1,1,7) AND A.LET_SYSTEM_NAME = ?2 AND A.ACTIVE = 1";

	private static final String FIND_ALL_PENDING_INACTIVE = "SELECT A.* FROM LCVINBOM.MODEL_PART A LEFT JOIN LCVINBOM.MODEL_PART_APPROVAL B ON A.MODEL_PART_ID = B.MODEL_PART_ID WHERE (B.APPROVE_STATUS = 0 or B.APPROVE_STATUS is NULL)";

	private static final String DCMS_URL = "dcmsRestUrl/getDesignChangesByPlantAndEffectiveBeginDate";
	
	private static final String FIND_DISTINCT_MODEL_PARTS = "select distinct let_system_name,dc_number, dc_part_number from lcvinbom.model_part ";
	
	private static final String FIND_ALL_INACTIVE_BY_PROD_SPEC_AND_SYSTEM = "SELECT A.* FROM LCVINBOM.MODEL_PART A WHERE A.PRODUCT_SPEC_WILDCARD like ?1 AND A.LET_SYSTEM_NAME = ?2 AND A.ACTIVE = 0";
	
	private static final String FIND_ALL_DISTINCT_YMT_CODE = "SELECT DISTINCT PRODUCT_SPEC_WILDCARD FROM LCVINBOM.MODEL_PART"; 
	
	private static final String FIND_ALL_DISTINCT_MODELS = "select distinct concat(substr(product_spec_wildcard, 1,1),substr(product_spec_wildcard, 2,3)) as Model from lcvinbom.model_part"; 
	
	private static final String FILTER_RULE_MAINTAINANCE_RECORDS = "select * from lcvinbom.model_part";
	
	private static final String FILTER_MODAL_PART = "SELECT * FROM LCVINBOM.MODEL_PART where Active in (0,1,2) and PRODUCT_SPEC_WILDCARD = ?1 and LET_SYSTEM_NAME = ?2";
	
	private static final String FIND_ALL_ACTIVE_MODEL_PARTS_BY_YMT = "SELECT * FROM LCVINBOM.MODEL_PART A WHERE A.PRODUCT_SPEC_WILDCARD = ?1 and A.LET_SYSTEM_NAME = ?2 AND A.ACTIVE = 1";


	@Override
	public List<VinBomDesignChangeDto> getDesignChangeByPlantAndEffectiveDate(String plantCode,
			Date effectiveBeginDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSystemNamesByPartNumber(String partNumber) {
		Parameters parameters = Parameters.with("1", partNumber);
		return findAllByNativeQuery(FIND_SYSTEM_NAMES_BY_PART_NUMBER, parameters, String.class);
	}

	@Override
	public List<ModelPart> findAllActiveInterchangeble() {
		Parameters params = Parameters.with("active", VinBomActiveStatus.ACTIVE).put("interchangeable", true);
		return findAll(params);
	}

	@Override
	@Transactional
	public ModelPart setInterchangableInactive(long modelPartId) {
		ModelPart modelPart = findByKey(modelPartId);
		if (modelPart != null) {
			modelPart.setActive(VinBomActiveStatus.DEPRECATED);
			return save(modelPart);
		}
		return null;
	}

	@Override
	public List<ModelPart> getInactivewithRules() {
		Parameters params = Parameters.with("active", VinBomActiveStatus.INACTIVE);
		return findAll(params);
	}
	
	@Override
	public List<ModelPart> getPendingInactivewithRules() {
		return findAllByNativeQuery(FIND_ALL_PENDING_INACTIVE,null);
	}

	@Override
	public List<ModelPart> findAllBy(String productSpecWildcard, String letSystemName, String dcPartNumber) {
		Parameters params = Parameters.with("productSpecWildcard", productSpecWildcard).put("letSystemName", letSystemName)
				.put("dcPartNumber", dcPartNumber);
		return findAll(params);
	}

	@Override
	public List<ModelPart> filterRulesAlreadyAssigned(String dcNumber, String dcPartNumber, String productSpecWildcard,
			String letSystemName) {
		Parameters params = Parameters.with("dcNumber", dcNumber).put("dcPartNumber", dcPartNumber)
				.put("productSpecWildcard", productSpecWildcard).put("letSystemName", letSystemName);
		return findAll(params);
	}


	@Override
	public List<ModelPart> getApprovedInactiveByProductSpecAndSystemName(String productSpecCode, String systemName) {
		Parameters parameters = Parameters.with("1", productSpecCode);
		parameters.put("2",systemName);
		return findAllByNativeQuery(FIND_ALL_APPROVED_INACTIVE_BY_PROD_SPEC_AND_SYSTEM, parameters);
	}
	
	@Override
	public List<ModelPart> getActiveByProductSpecAndSystemName(String productSpecCode, String systemName) {
		Parameters parameters = Parameters.with("1", productSpecCode);
		parameters.put("2",systemName);
		return findAllByNativeQuery(FIND_ALL_ACTIVE_BY_PROD_SPEC_AND_SYSTEM, parameters);
	}
	
	@Override
	public List<ModelPart> filterByCriteria(String dcNumber, String dcPartNumber, String systemName, List<String> models, List<String> active) {
		String sql = FILTER_RULE_MAINTAINANCE_RECORDS;

		String likeQuery = "";

		if (!models.isEmpty()) {
			for (String model : models) {
				if (likeQuery.isEmpty()) {
					likeQuery = likeQuery + "product_spec_wildcard like '" + model + "%'";
				} else {
					likeQuery = likeQuery + " or " + "product_spec_wildcard like '" + model + "%'";
				}
			}

			if (!likeQuery.isEmpty()) {
				likeQuery = "(" + likeQuery + ")";
			}
		}

		String activeLike = "";

		if (!active.isEmpty()) {
			activeLike = activeLike + "Active in (" + StringUtil.toSqlInString(active) + ") ";
			if (!likeQuery.isEmpty()) {
				activeLike = activeLike + "" + " and ";
			}
		}

		String filterPart = "";
		String whereClause = " where ";
		String andOper = "";
		if (!models.isEmpty() || !active.isEmpty()) {
			andOper = " and ";
		}

		if (!StringUtils.isEmpty(dcNumber)) {
			filterPart = whereClause + filterPart + "dc_number = '" + dcNumber + "'" + andOper;
			whereClause = "";
		}

		if (!StringUtils.isEmpty(dcPartNumber)) {
			if (!filterPart.isEmpty() && (models.isEmpty() && active.isEmpty())) {
				filterPart = filterPart + " and ";
			}
			filterPart = whereClause + filterPart + "dc_part_number = '" + dcPartNumber + "'" + andOper;
			whereClause = "";
		}

		if (!StringUtils.isEmpty(systemName)) {
			if (!filterPart.isEmpty() && (models.isEmpty() && active.isEmpty())) {
				filterPart = filterPart + " and ";
			}
			filterPart = whereClause + filterPart + "let_system_name = '" + systemName + "'" + andOper;
			whereClause = "";
		}

		sql = sql + " " + whereClause + filterPart + activeLike + likeQuery;

		return findAllByNativeQuery(sql, null, ModelPart.class);
	}

	@Override
	public List<ModelPartLotFilterDto> findAllDistinctModelParts() {
		// TODO Auto-generated method stub
		return findAllByNativeQuery(FIND_DISTINCT_MODEL_PARTS,null,ModelPartLotFilterDto.class);
	}

	@Override
	public List<ModelPart> getInactiveByProductSpecAndSystemName(String productSpecCode, String systemName) {
		Parameters parameters = Parameters.with("1","'%"+ productSpecCode+"%'");
		parameters.put("2",systemName);
		return findAllByNativeQuery(FIND_ALL_INACTIVE_BY_PROD_SPEC_AND_SYSTEM, parameters);
	}
	
	@Override
	public List<String> findAllDistinctYMTCode() {
		List<String> listYMTCodes = findAllByNativeQuery(FIND_ALL_DISTINCT_YMT_CODE, null, String.class);
		return listYMTCodes;
	}
	
    @Override
	public List<String> findAllDistinctModels() {
		List<String> models = findAllByNativeQuery(FIND_ALL_DISTINCT_MODELS, null, String.class);
		return models;
	}

	@Override
	public List<ModelPart> filterModalPart(String systemName, String modelType, String ymtCode) {
		String prodSpecWildCard = ymtCode + "" + modelType;
		Parameters parameters = Parameters.with("1", prodSpecWildCard);
		parameters.put("2", systemName);

		return findAllByNativeQuery(FILTER_MODAL_PART, parameters);
	}

@Override
public List<ModelPart> getModelPartByYmt(String productSpecCode, String systemName)
 {
		Parameters parameters = Parameters.with("1", productSpecCode);
		parameters.put("2",systemName);
		return findAllByNativeQuery(FIND_ALL_ACTIVE_MODEL_PARTS_BY_YMT, parameters);
}


@Override
@Transactional
public ModelPart setActiveState(long modelPartId,VinBomActiveStatus vinBomActiveStatus) 
{
	ModelPart modelPart = findByKey(modelPartId);
	if (modelPart != null) {
		modelPart.setActive(vinBomActiveStatus);
		return save(modelPart);
}
return null;
}

}
