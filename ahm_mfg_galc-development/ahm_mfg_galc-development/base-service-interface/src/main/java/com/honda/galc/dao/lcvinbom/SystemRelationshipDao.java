package com.honda.galc.dao.lcvinbom;

import java.util.List;

import com.honda.galc.dto.lcvinbom.SystemrelationshipDto;
import com.honda.galc.entity.lcvinbom.SystemRelationship;
import com.honda.galc.entity.lcvinbom.SystemRelationshipId;
import com.honda.galc.service.IDaoService;

public interface SystemRelationshipDao extends IDaoService<SystemRelationship, SystemRelationshipId> {
	public void saveSystemRelationship(List<SystemrelationshipDto> systemRelationshipDto);
	public List<SystemRelationship> getAllMultiRelationship(String currentUser);
	public void deleteSystemRelationship(List<SystemrelationshipDto> systemRelationshipDto);
	List<SystemRelationship> findAllSystemRelationship();
	List<String> findVinbomSystemNames(String splitSystemName);
}
