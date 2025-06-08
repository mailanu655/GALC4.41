package com.honda.galc.vios.dto.mfg;

import java.util.List;
import java.util.Map;

import com.honda.galc.dto.IDto;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.entity.conf.MCOperationRevision;

public class MfgCtrlActiveDto implements IDto {
	private static final long serialVersionUID = -3211794550447442108L;
	private MCOperationRevision operation;
	private List<MCOperationChecker> operationCheckers;
	//Key for Mfg parts Map --> <part no>_<Part section code>_<part item no>
	private Map<String, List<MfgPartDto>> mfgPartsMap;
	
	public MCOperationRevision getOperation() {
		return operation;
	}
	public void setOperation(MCOperationRevision operation) {
		this.operation = operation;
	}
	public List<MCOperationChecker> getOperationCheckers() {
		return operationCheckers;
	}
	public void setOperationCheckers(List<MCOperationChecker> operationCheckers) {
		this.operationCheckers = operationCheckers;
	}
	public Map<String, List<MfgPartDto>> getMfgPartsMap() {
		return mfgPartsMap;
	}
	public void setMfgPartsMap(Map<String, List<MfgPartDto>> mfgPartsMap) {
		this.mfgPartsMap = mfgPartsMap;
	}
	public void clear() {
		this.operation=null;
		this.operationCheckers=null;
		if(this.mfgPartsMap!=null && this.mfgPartsMap.values()!=null) {
			for(List<MfgPartDto> mfgPartDCs: this.mfgPartsMap.values()) {
				if(mfgPartDCs!=null) {
					for(MfgPartDto mfgPartDC:mfgPartDCs) {
						if(mfgPartDC!=null) {
							mfgPartDC.clear();
						}
					}
				}
			}
			this.mfgPartsMap.clear();
		}
		this.mfgPartsMap=null;
	}
	
}
