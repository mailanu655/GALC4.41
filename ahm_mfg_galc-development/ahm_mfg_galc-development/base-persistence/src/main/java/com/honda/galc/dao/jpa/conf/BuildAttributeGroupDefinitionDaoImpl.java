package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.BuildAttributeGroupDefinitionDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.BuildAttributeGroupDefinition;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

public class BuildAttributeGroupDefinitionDaoImpl extends BaseDaoImpl<BuildAttributeGroupDefinition, String> implements BuildAttributeGroupDefinitionDao {

	private static final String FIND_BY_FILTER_BASE = "SELECT * FROM GALADM.BUILD_ATTRIBUTE_GROUP_DEF_TBX";
	private static final String FIND_BY_FILTER_ATTRIBUTE_GRP = " ATTRIBUTE_GRP LIKE '@ATTRIBUTE_GRP'";
	private static final String FIND_BY_FILTER_SCREEN_ID = " SCREEN_ID LIKE '@SCREEN_ID'";
	private static final String FIND_BY_FILTER_ORDER_BY = " ORDER BY ATTRIBUTE_GRP ASC";

	private static final String FIND_SCREEN_ID_FOR_GROUP = "select a.screenId from BuildAttributeGroupDefinition a where a.attributeGroup = :attributeGroup";

	private static final String FIND_ALL_GROUPS = "select distinct a.attributeGroup from BuildAttributeGroupDefinition a order by a.attributeGroup";

	@Override
	public List<BuildAttributeGroupDefinition> findByFilter(String attributeGroup, String screenId) {
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(FIND_BY_FILTER_BASE);
		final StringBuilder whereBuilder = new StringBuilder();
		appendFindByFilterWhere(whereBuilder, "@ATTRIBUTE_GRP", attributeGroup + "%", FIND_BY_FILTER_ATTRIBUTE_GRP);
		appendFindByFilterWhere(whereBuilder, "@SCREEN_ID", screenId + "%", FIND_BY_FILTER_SCREEN_ID);
		sqlBuilder.append(whereBuilder.toString());
		sqlBuilder.append(FIND_BY_FILTER_ORDER_BY);
		final String sql = sqlBuilder.toString();
		return findAllByNativeQuery(sql, null, BuildAttributeGroupDefinition.class);
	}

	private void appendFindByFilterWhere(StringBuilder whereBuilder, String field, String fieldValue, String sql) {
		if (StringUtils.isEmpty(fieldValue)) return;
		whereBuilder.append(whereBuilder.length() == 0 ? " WHERE" : " AND");
		whereBuilder.append(sql.replace(field, fieldValue));
	}

	@Override
	public String findScreenIdForGroup(String attributeGroup) {
		Parameters params = Parameters.with("attributeGroup", attributeGroup);
		String screenId = this.findFirstByQuery(FIND_SCREEN_ID_FOR_GROUP, String.class, params);
		return screenId;
	}

	@Override
	public BuildAttributeGroupDefinition findByAttributeGroupAndScreenId(String attributeGroup, String screenId) {
		return this.findFirst(Parameters.with("attributeGroup", attributeGroup).put("screenId", screenId));
	}

	@Override
	public List<BuildAttributeGroupDefinition> findAllByScreenId(String screenId) {
		return this.findAll(Parameters.with("screenId", screenId));
	}

	@Override
	public List<BuildAttributeGroupDefinition> findAllInOrder() {
		return this.findAll(null, new String[] { "attributeGroup" }, true);
	}

	@Override
	public List<String> findAllAttributeGroups() {
		List<String> attributeGroups = this.findByQuery(FIND_ALL_GROUPS, String.class);
		if (attributeGroups == null || attributeGroups.isEmpty()) {
			return null;
		}
		return StringUtil.trimStringList(attributeGroups);
	}

}
