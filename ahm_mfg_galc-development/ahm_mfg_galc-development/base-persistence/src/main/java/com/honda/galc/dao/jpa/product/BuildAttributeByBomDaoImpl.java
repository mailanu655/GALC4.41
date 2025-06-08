package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.BuildAttributeByBomDao;
import com.honda.galc.entity.product.BuildAttributeByBom;
import com.honda.galc.entity.product.BuildAttributeByBomId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

public class BuildAttributeByBomDaoImpl extends BaseDaoImpl<BuildAttributeByBom, BuildAttributeByBomId> implements BuildAttributeByBomDao {

	private static final String FIND_BY_FILTER_BASE = "SELECT A.* FROM GALADM.BUILD_ATTRIBUTE_BY_BOM_TBX A";
	private static final String FIND_BY_FILTER_ATTRIBUTE_GRP_JOIN = " LEFT JOIN GALADM.BUILD_ATTRIBUTE_DEF_TBX B ON A.ATTRIBUTE = B.ATTRIBUTE";
	private static final String FIND_BY_FILTER_ATTRIBUTE_GRP = " B.ATTRIBUTE_GRP = '@ATTRIBUTE_GRP'";
	private static final String FIND_BY_FILTER_SYSTEM_JOIN = " LEFT JOIN GALADM.MODEL_GROUP_TBX C ON A.MODEL_GROUP = C.MODEL_GROUP";
	private static final String FIND_BY_FILTER_SYSTEM = " C.SYSTEM = '@SYSTEM@'";
	private static final String FIND_BY_FILTER_MODEL_GROUP = " A.MODEL_GROUP = '@MODEL_GROUP'";
	private static final String FIND_BY_FILTER_ATTRIBUTE = " A.ATTRIBUTE = '@ATTRIBUTE'";
	private static final String FIND_BY_FILTER_PART_NO = " A.PART_NO LIKE '@PART_NO'";
	private static final String FIND_BY_FILTER_ORDER_BY = " ORDER BY A.PART_NO ASC";

	private static final String FIND_ALL_ATTRIBUTE_FOR_ATTRIBUTE_GROUP_AND_MODEL_GROUP = "SELECT DISTINCT A.ATTRIBUTE FROM GALADM.BUILD_ATTRIBUTE_BY_BOM_TBX A, GALADM.BUILD_ATTRIBUTE_DEF_TBX B WHERE B.ATTRIBUTE_GRP = ?1 AND A.ATTRIBUTE = B.ATTRIBUTE AND A.MODEL_GROUP = ?2 ORDER BY A.ATTRIBUTE ASC";

	private static final String GET_COUNT_OF_ATTRIBUTE = "select count(a.id.attribute) from BuildAttributeByBom a where a.id.attribute = :attribute";

	private static final String FIND_BY_MODEL_YEAR_AND_PART_NO = "SELECT DISTINCT PART_NO, ATTRIBUTE_VALUE FROM GALADM.BUILD_ATTRIBUTE_BY_BOM_TBX "  
			+" JOIN  GALADM.MODEL_GROUPING_TBX on GALADM.BUILD_ATTRIBUTE_BY_BOM_TBX.MODEL_GROUP = GALADM.MODEL_GROUPING_TBX.MODEL_GROUP AND GALADM.MODEL_GROUPING_TBX.SYSTEM = ?3 "  
			+" AND  SUBSTR(GALADM.MODEL_GROUPING_TBX.MTC_MODEL, 1,1) = (SELECT DISTINCT MODEL_YEAR_CODE FROM GALADM.GAL144TBX WHERE GALADM.GAL144TBX.MODEL_YEAR_DESCRIPTION = ?1)"  
			+" WHERE GALADM.BUILD_ATTRIBUTE_BY_BOM_TBX.PART_NO = SUBSTR(?2,1,LENGTH(TRIM(GALADM.BUILD_ATTRIBUTE_BY_BOM_TBX.PART_NO)))";
	
	@Override
	public List<BuildAttributeByBom> findAllByAttribute(String attribute) {
		return findAll(Parameters.with("id.attribute",attribute));
	}

	@Override
	public long getCountOfAttribute(String attribute) {
		return count(GET_COUNT_OF_ATTRIBUTE, Parameters.with("attribute", attribute));
	}

	@Override
	public BuildAttributeByBom findfirstByAttributeAndValue(String attribute, String attributeValue) {
		return findFirst(Parameters.with("id.attribute",attribute).put("attributeValue", attributeValue));
	}

	@Override
	public List<BuildAttributeByBom> findByFilter(String attributeGroup, String system, String modelGroup, String attribute, String partNoPrefix) {
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(FIND_BY_FILTER_BASE);
		if (!StringUtils.isEmpty(attributeGroup)) sqlBuilder.append(FIND_BY_FILTER_ATTRIBUTE_GRP_JOIN);
		if (!StringUtils.isEmpty(system)) sqlBuilder.append(FIND_BY_FILTER_SYSTEM_JOIN);
		final StringBuilder whereBuilder = new StringBuilder();
		appendFindByFilterWhere(whereBuilder, "@ATTRIBUTE_GRP", attributeGroup, FIND_BY_FILTER_ATTRIBUTE_GRP);
		appendFindByFilterWhere(whereBuilder, "@SYSTEM@", system, FIND_BY_FILTER_SYSTEM);
		appendFindByFilterWhere(whereBuilder, "@MODEL_GROUP", modelGroup, FIND_BY_FILTER_MODEL_GROUP);
		appendFindByFilterWhere(whereBuilder, "@ATTRIBUTE", attribute, FIND_BY_FILTER_ATTRIBUTE);
		appendFindByFilterWhere(whereBuilder, "@PART_NO", StringUtils.isEmpty(partNoPrefix) ? null : partNoPrefix + "%", FIND_BY_FILTER_PART_NO);
		sqlBuilder.append(whereBuilder.toString());
		sqlBuilder.append(FIND_BY_FILTER_ORDER_BY);
		final String sql = sqlBuilder.toString();
		return findAllByNativeQuery(sql, null, BuildAttributeByBom.class);
	}

	private void appendFindByFilterWhere(StringBuilder whereBuilder, String field, String fieldValue, String sql) {
		if (StringUtils.isEmpty(fieldValue)) return;
		whereBuilder.append(whereBuilder.length() == 0 ? " WHERE" : " AND");
		whereBuilder.append(sql.replace(field, fieldValue));
	}

	@Override
	public List<String> findAllAttributeForAttributeGroupAndModelGroup(String attributeGroup, String modelGroup) {
		Parameters params = Parameters.with("1", attributeGroup).put("2", modelGroup);
		List<String> attributes = this.findAllByNativeQuery(FIND_ALL_ATTRIBUTE_FOR_ATTRIBUTE_GROUP_AND_MODEL_GROUP, params, String.class);
		if (attributes == null || attributes.isEmpty()) {
			return null;
		}
		return StringUtil.trimStringList(attributes);
	}

	@Override
	public List<Object[]> findAllAttributeForPartNoAndModelYear(String modelYearCode, String partNo,String system) {
		Parameters params = Parameters.with("1", modelYearCode).put("2", partNo).put("3", system);
		return this.findAllByNativeQuery(FIND_BY_MODEL_YEAR_AND_PART_NO, params,Object[].class);
	}
}
