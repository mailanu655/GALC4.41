package com.honda.galc.vios.dto.mfg;

import java.util.List;
import java.util.Map;

import com.honda.galc.dto.IDto;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;

public class MfgCtrlViosDto implements IDto {
	
	private static final long serialVersionUID = 1L;
	
	private MCViosMasterOperation masterOp;
	private List<MCViosMasterOperationChecker> masterOpCheckers;
	//Key for Mfg parts Map --> <part no>_<Part section code>_<part item no>
	private Map<String, MfgViosPartDto> masterPartsMap;
	private List<MCViosMasterOperationMeasurement> masterOpMeasList;
	private List<MCViosMasterOperationMeasurementChecker> masterOpMeasCheckerList;
	
	public MCViosMasterOperation getMasterOp() {
		return masterOp;
	}

	public void setMasterOp(MCViosMasterOperation masterOp) {
		this.masterOp = masterOp;
	}

	public List<MCViosMasterOperationChecker> getMasterOpCheckers() {
		return masterOpCheckers;
	}

	public void setMasterOpCheckers(List<MCViosMasterOperationChecker> masterOpCheckers) {
		this.masterOpCheckers = masterOpCheckers;
	}

	public Map<String, MfgViosPartDto> getMasterPartsMap() {
		return masterPartsMap;
	}

	public void setMasterPartsMap(Map<String, MfgViosPartDto> masterPartsMap) {
		this.masterPartsMap = masterPartsMap;
	}
	
	public List<MCViosMasterOperationMeasurement> getMasterOpMeasList() {
		return masterOpMeasList;
	}

	public void setMasterOpMeasList(List<MCViosMasterOperationMeasurement> masterOpMeasList) {
		this.masterOpMeasList = masterOpMeasList;
	}

	public List<MCViosMasterOperationMeasurementChecker> getMasterOpMeasCheckerList() {
		return masterOpMeasCheckerList;
	}

	public void setMasterOpMeasCheckerList(List<MCViosMasterOperationMeasurementChecker> masterOpMeasCheckerList) {
		this.masterOpMeasCheckerList = masterOpMeasCheckerList;
	}

	public void clear() {
		this.masterOp=null;
		this.masterOpCheckers=null;
		if(this.masterPartsMap!=null && this.masterPartsMap.values()!=null) {
			for(MfgViosPartDto mfgPartDC: this.masterPartsMap.values()) {
				if(mfgPartDC!=null) {
					mfgPartDC.clear();
				}
			}
			this.masterPartsMap.clear();
		}
		this.masterPartsMap=null;
		this.masterOpMeasList=null;
		this.masterOpMeasCheckerList=null;
	}
	
}
