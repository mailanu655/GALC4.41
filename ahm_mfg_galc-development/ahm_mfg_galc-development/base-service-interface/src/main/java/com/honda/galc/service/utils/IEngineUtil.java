package com.honda.galc.service.utils;

import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;

public interface IEngineUtil {

	boolean determineEngineStatus(Engine engine, String partName, InstalledPartStatus status);

	boolean determineMissionStatus(Engine engine, String partName, InstalledPartStatus status);
	
	Engine updateEngineSpec(Frame frame, Engine engine);
}
