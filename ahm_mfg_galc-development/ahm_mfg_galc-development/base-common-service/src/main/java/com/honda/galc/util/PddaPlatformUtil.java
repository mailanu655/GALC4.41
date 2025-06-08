package com.honda.galc.util;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.vios.dto.PddaPlatformDto;

public class PddaPlatformUtil {
	
	public static boolean isBlankPddaPlatform(PddaPlatformDto pddaPlatformDto) {
		if(pddaPlatformDto == null)
			return true;

		if(StringUtils.isBlank(pddaPlatformDto.getPlantLocCode()) &&
				StringUtils.isBlank(pddaPlatformDto.getDeptCode()) &&
				StringUtils.isBlank(pddaPlatformDto.getProdAsmLineNo()) &&
				StringUtils.isBlank(pddaPlatformDto.getVehicleModelCode()) &&
				pddaPlatformDto.getModelYearDate() <= 0.0 &&
				pddaPlatformDto.getProdSchQty() <= 0.0) {
			return true;
		}
		return false;
	}
}
