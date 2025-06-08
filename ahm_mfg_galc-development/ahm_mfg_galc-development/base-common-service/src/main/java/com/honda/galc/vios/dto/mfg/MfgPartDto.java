package com.honda.galc.vios.dto.mfg;

import java.util.List;

import com.honda.galc.dto.IDto;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCPartChecker;

public class MfgPartDto implements IDto {
	private static final long serialVersionUID = 2188709968410210843L;
	private MCOperationPartRevision mfgPart; //Also need to populate mc_op_part and matrix -- same as actual adding of mfg parts
	private List<MCPartChecker> partCheckers;
	private List<MCOperationMeasurement> measurements;
	private List<MCMeasurementChecker> measCheckers;
	
	public MCOperationPartRevision getMfgPart() {
		return mfgPart;
	}
	public void setMfgPart(MCOperationPartRevision mfgPart) {
		this.mfgPart = mfgPart;
	}
	public List<MCPartChecker> getPartCheckers() {
		return partCheckers;
	}
	public void setPartCheckers(List<MCPartChecker> partCheckers) {
		this.partCheckers = partCheckers;
	}
	public List<MCOperationMeasurement> getMeasurements() {
		return measurements;
	}
	public void setMeasurements(List<MCOperationMeasurement> measurements) {
		this.measurements = measurements;
	}
	public List<MCMeasurementChecker> getMeasCheckers() {
		return measCheckers;
	}
	public void setMeasCheckers(List<MCMeasurementChecker> measCheckers) {
		this.measCheckers = measCheckers;
	}
	public void clear() {
		this.mfgPart=null;
		this.partCheckers=null;
		this.measurements=null;
		this.measCheckers=null;
	}
	
}
