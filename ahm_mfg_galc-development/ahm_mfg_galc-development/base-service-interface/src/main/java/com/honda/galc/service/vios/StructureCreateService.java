package com.honda.galc.service.vios;

import com.honda.galc.entity.conf.MCOrderStructure;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPoint;
import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.service.IService;
import com.honda.galc.vios.dto.PddaPlatformDto;

public interface StructureCreateService extends IService {
	public MCOrderStructure findOrCreateOrderStructure(String orderNo, String divisionId, PddaPlatformDto pddaPlatform) throws Exception;
	public MCOrderStructureForProcessPoint findOrCreateOrderStructureForProcessPoint(String orderNo, String divisionId, String processPointId, String mode, PddaPlatformDto pddaPlatform) throws Exception;
	public MCStructure findOrCreateStructure(String productSpecCode, String divisionId, PddaPlatformDto pddaPlatform) throws Exception;
	public MCStructure findOrCreateStructure(String productSpecCode, String divisionId, String processPointId, String mode, PddaPlatformDto pddaPlatform) throws Exception;
}