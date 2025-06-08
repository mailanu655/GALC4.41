package com.honda.galc.dto.lcvinbom;

import com.honda.galc.dto.IDto;

public class VinBomPartSetDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	String associate;
	VinBomPartDto[] vinBomPartList;
	
	
	public VinBomPartDto[] getVinBomPartList() {
		return vinBomPartList;
	}
	public void setVinBomPartList(VinBomPartDto[] vinBomPartList) {
		this.vinBomPartList = vinBomPartList;
	}
	public String getAssociate() {
		return associate;
	}
	public void setAssociate(String associate) {
		this.associate = associate;
	}
}
