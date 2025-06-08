package com.honda.galc.dao.jpa.lcvinbom;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.lcvinbom.SystemRelationshipDao;
import com.honda.galc.dto.lcvinbom.SystemrelationshipDto;
import com.honda.galc.entity.lcvinbom.SystemRelationship;
import com.honda.galc.entity.lcvinbom.SystemRelationshipId;
import com.honda.galc.service.Parameters;

public class SystemRelationshipDaoImpl extends BaseDaoImpl<SystemRelationship, SystemRelationshipId>
		implements SystemRelationshipDao {

	private static final String FIND_ALL_BYSYSTEM_RELATION_BY_USER = "select * from lcvinbom.SYSTEM_RELATIONSHIP "	
			+ "where update_user = @USER_ID@";
	
	private static final String FIND_BY_SYSTEM_RELATIONSHIP = "select distinct BEAM_SYSTEM_NAME, LET_SYSTEM_NAME from lcvinbom.SYSTEM_RELATIONSHIP";
	
	private static final String FIND_BEAM_SYSTEM = "select BEAM_SYSTEM_NAME from lcvinbom.SYSTEM_RELATIONSHIP where LET_SYSTEM_NAME = ?1"; 

	@Override
	public void saveSystemRelationship(List<SystemrelationshipDto> systemRelationshipDto) {
		
	}

	@Override
	public List<SystemRelationship> getAllMultiRelationship(String userId) {
		String sql = FIND_ALL_BYSYSTEM_RELATION_BY_USER.replace("@USER_ID@", "'"+userId+"'");
		return findAllByNativeQuery(sql, null, SystemRelationship.class);
	}
	
	@Override
	public List<SystemRelationship> findAllSystemRelationship() {
		// TODO Auto-generated method stub
		return findAllByNativeQuery(FIND_BY_SYSTEM_RELATIONSHIP,null,SystemRelationship.class);
	}

	@Override
	public void deleteSystemRelationship(List<SystemrelationshipDto> systemRelationshipDto) {
		
	}

	@Override
	public List<String> findVinbomSystemNames(String splitSystemName) {
		Parameters params = Parameters.with("1",splitSystemName);
		return findAllByNativeQuery(FIND_BEAM_SYSTEM, params, String.class);
	}
}