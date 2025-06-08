package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.BuildAttributeDefinition;
import com.honda.galc.service.IDaoService;

public interface BuildAttributeDefinitionDao extends IDaoService<BuildAttributeDefinition, String> {

	public List<BuildAttributeDefinition> findByFilter(String attributeGroup, String attribute, String attributeLabel, String autoUpdate);

	public long getCountOfAttributeGroup(String attributeGroup);

	public List<BuildAttributeDefinition> findAllByAttributeGroup(String attributeGroup);

	public List<String> findAllAttributes();

	public List<String> findAllAttributesByAttributeGroups(List<String> attributeGroups);

}
