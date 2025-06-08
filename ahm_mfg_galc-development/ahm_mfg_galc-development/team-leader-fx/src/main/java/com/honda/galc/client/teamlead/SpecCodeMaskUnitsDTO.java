package com.honda.galc.client.teamlead;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class SpecCodeMaskUnitsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public SpecCodeMaskUnitsDTO() {
	}

	public SpecCodeMaskUnitsDTO(String specCodeMask, String units) {
		super();
		this.specCodeMask = specCodeMask;
		this.units = units;
	}

	private String specCodeMask;
	private String units;

	public String getSpecCodeMask() {
		return StringUtils.trim(specCodeMask);
	}

	public void setSpecCodeMask(String specCodeMask) {
		this.specCodeMask = specCodeMask;
	}

	public String getUnits() {
		return StringUtils.trim(units);
	}

	public void setUnits(String units) {
		this.units = units;
	}

}
