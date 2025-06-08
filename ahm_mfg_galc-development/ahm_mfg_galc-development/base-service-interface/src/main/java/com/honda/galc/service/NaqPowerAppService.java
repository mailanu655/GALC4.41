package com.honda.galc.service;

import com.honda.galc.dto.DefectMapDto;

public interface NaqPowerAppService extends IService{
	
	public String saveDefectData(DefectMapDto data);
}
