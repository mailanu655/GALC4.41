package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.BuildAttributeGroupDefinition;
import com.honda.galc.service.IDaoService;

public interface BuildAttributeGroupDefinitionDao extends IDaoService<BuildAttributeGroupDefinition, String> {

	public List<BuildAttributeGroupDefinition> findByFilter(String attributeGroup, String screenId);

	public String findScreenIdForGroup(String attributeGroup);

	public BuildAttributeGroupDefinition findByAttributeGroupAndScreenId(String attributeGroup, String screenId);

	public List<BuildAttributeGroupDefinition> findAllByScreenId(String screenId);

	public List<BuildAttributeGroupDefinition> findAllInOrder();

	public List<String> findAllAttributeGroups();

}
