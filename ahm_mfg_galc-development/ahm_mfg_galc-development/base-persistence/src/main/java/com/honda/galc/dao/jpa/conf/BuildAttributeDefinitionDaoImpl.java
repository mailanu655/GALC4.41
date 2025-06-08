package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.BuildAttributeDefinitionDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.BuildAttributeDefinition;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

public class BuildAttributeDefinitionDaoImpl extends BaseDaoImpl<BuildAttributeDefinition, String> implements BuildAttributeDefinitionDao {

	private static final String FIND_BY_FILTER_BASE = "SELECT * FROM GALADM.BUILD_ATTRIBUTE_DEF_TBX";
	private static final String FIND_BY_FILTER_ATTRIBUTE_GRP = " ATTRIBUTE_GRP = '@ATTRIBUTE_GRP'";
	private static final String FIND_BY_FILTER_ATTRIBUTE = " ATTRIBUTE LIKE '@ATTRIBUTE'";
	private static final String FIND_BY_FILTER_ATTRIBUTE_LABEL = " ATTRIBUTE_LABEL LIKE '@ATTRIBUTE_LABEL'";
	private static final String FIND_BY_FILTER_AUTO_UPDATE = " AUTO_UPDATE = '@AUTO_UPDATE'";
	private static final String FIND_BY_FILTER_ORDER_BY = " ORDER BY ATTRIBUTE_GRP ASC, ATTRIBUTE ASC";

	private static final String GET_COUNT_OF_ATTRIBUTE_GROUP = "select count(a.attributeGroup) from BuildAttributeDefinition a where a.attributeGroup = :attributeGroup";

	private static final String FIND_ALL_ATTRIBUTES = "select distinct a.attribute from BuildAttributeDefinition a order by a.attribute";

	private static final String FIND_ALL_ATTRIBUTES_BY_ATTRIBUTE_GROUPS = "SELECT A.ATTRIBUTE FROM GALADM.BUILD_ATTRIBUTE_DEF_TBX A, GALADM.BUILD_ATTRIBUTE_GROUP_DEF_TBX B WHERE A.ATTRIBUTE_GRP = B.ATTRIBUTE_GRP AND B.ATTRIBUTE_GRP IN (@ATTRIBUTE_GRPS@) ORDER BY A.ATTRIBUTE";

	@Override
	public List<BuildAttributeDefinition> findByFilter(String attributeGroup, String attribute, String attributeLabel, String autoUpdate) {
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(FIND_BY_FILTER_BASE);
		final StringBuilder whereBuilder = new StringBuilder();
		appendFindByFilterWhere(whereBuilder, "@ATTRIBUTE_GRP", attributeGroup, FIND_BY_FILTER_ATTRIBUTE_GRP);
		appendFindByFilterWhere(whereBuilder, "@ATTRIBUTE", attribute + "%", FIND_BY_FILTER_ATTRIBUTE);
		appendFindByFilterWhere(whereBuilder, "@ATTRIBUTE_LABEL", attributeLabel + "%", FIND_BY_FILTER_ATTRIBUTE_LABEL);
		appendFindByFilterWhere(whereBuilder, "@AUTO_UPDATE", autoUpdate, FIND_BY_FILTER_AUTO_UPDATE);
		sqlBuilder.append(whereBuilder.toString());
		sqlBuilder.append(FIND_BY_FILTER_ORDER_BY);
		final String sql = sqlBuilder.toString();
		return findAllByNativeQuery(sql, null, BuildAttributeDefinition.class);
	}

	private void appendFindByFilterWhere(StringBuilder whereBuilder, String field, String fieldValue, String sql) {
		if (StringUtils.isEmpty(fieldValue)) return;
		whereBuilder.append(whereBuilder.length() == 0 ? " WHERE" : " AND");
		whereBuilder.append(sql.replace(field, fieldValue));
	}

	@Override
	public long getCountOfAttributeGroup(String attributeGroup) {
		return count(GET_COUNT_OF_ATTRIBUTE_GROUP, Parameters.with("attributeGroup", attributeGroup));
	}

	@Override
	public List<BuildAttributeDefinition> findAllByAttributeGroup(String attributeGroup) {
		return this.findAll(Parameters.with("attributeGroup", attributeGroup));
	}

	@Override
	public List<String> findAllAttributes() {
		List<String> attributes = this.findByQuery(FIND_ALL_ATTRIBUTES, String.class);
		if (attributes == null || attributes.isEmpty()) {
			return null;
		}
		return StringUtil.trimStringList(attributes);
	}

	@Override
	public List<String> findAllAttributesByAttributeGroups(List<String> attributeGroups) {
		List<String> attributes = this.findAllByNativeQuery(FIND_ALL_ATTRIBUTES_BY_ATTRIBUTE_GROUPS.replace("@ATTRIBUTE_GRPS@", StringUtil.toSqlInString(attributeGroups)), null, String.class);
		if (attributes == null || attributes.isEmpty()) {
			return null;
		}
		return StringUtil.trimStringList(attributes);
	}

}
