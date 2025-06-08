package com.honda.galc.client.teamlead.vios;

import com.honda.galc.vios.dto.PddaPlatformDto;

public class ViosPlatform {
	
	private static ViosPlatform viosPlatform;
	private PddaPlatformDto pddaPlatformDto;

	private ViosPlatform() {
		super();
	}
	
	public static ViosPlatform getInstance() {
		if(viosPlatform == null) {
			viosPlatform = new ViosPlatform();
			viosPlatform.setPddaPlatformDto(new PddaPlatformDto());
		}
		return viosPlatform;
	}
	
	public PddaPlatformDto getPddaPlatformDto() {
		return pddaPlatformDto;
	}

	private void setPddaPlatformDto(PddaPlatformDto pddaPlatformDto) {
		this.pddaPlatformDto = pddaPlatformDto;
	}

	public void setViosPlatform(String plantLocCode, String deptCode, float modelYearDate,
			float prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		pddaPlatformDto.setPlantLocCode(plantLocCode);
		pddaPlatformDto.setDeptCode(deptCode);
		pddaPlatformDto.setModelYearDate(modelYearDate);
		pddaPlatformDto.setProdSchQty(prodSchQty);
		pddaPlatformDto.setProdAsmLineNo(prodAsmLineNo);
		pddaPlatformDto.setVehicleModelCode(vehicleModelCode);
	}
	
}
