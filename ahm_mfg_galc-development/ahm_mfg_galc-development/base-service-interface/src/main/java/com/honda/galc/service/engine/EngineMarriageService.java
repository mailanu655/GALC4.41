package com.honda.galc.service.engine;

import java.util.List;

import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.IService;

public interface EngineMarriageService extends IService {

	void init(List<InstalledPart> InstalledParts, String partName);
	
	Engine assignMissionType(List<InstalledPart> installedParts, String processPointId, String applicationId);
	
	Engine assignMission(List<InstalledPart> installedParts, String processPointId, String applicationId);
	
	Engine deassignMission(List<InstalledPart> installedParts, String processPointId, String applicationId);
	
	Engine deassignMissionType(List<InstalledPart> installedParts, String processPointId, String applicationId);
	
	void assignEngineAndFrame(List<InstalledPart> installedParts);
	
	void updateEngineAndFrame(Engine engine , Frame frame, ProductBuildResult result);

	Frame deassignEngineAndFrame(List<InstalledPart> installedParts, String processPointId, String applicationId);
}