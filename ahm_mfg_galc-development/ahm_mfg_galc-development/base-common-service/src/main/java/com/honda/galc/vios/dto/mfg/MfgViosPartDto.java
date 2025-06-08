package com.honda.galc.vios.dto.mfg;

import java.util.List;

import com.honda.galc.dto.IDto;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;

public class MfgViosPartDto implements IDto {
	
	private static final long serialVersionUID = 1L;
	
	private MCViosMasterOperationPart masterOpPart; 
	private List<MCViosMasterOperationPartChecker> masterOpPartCheckers;
	
	
	public MCViosMasterOperationPart getMasterOpPart() {
		return masterOpPart;
	}

	public void setMasterOpPart(MCViosMasterOperationPart masterOpPart) {
		this.masterOpPart = masterOpPart;
	}

	public List<MCViosMasterOperationPartChecker> getMasterOpPartCheckers() {
		return masterOpPartCheckers;
	}

	public void setMasterOpPartCheckers(List<MCViosMasterOperationPartChecker> masterOpPartCheckers) {
		this.masterOpPartCheckers = masterOpPartCheckers;
	}

	public void clear() {
		this.masterOpPart=null;
		this.masterOpPartCheckers=null;
	}
	
}
