package com.honda.galc.service;

import java.util.List;

import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.qi.QiDefectResult;

public interface NaqBevService extends IService {
	
	public List<QiDefectResult> getDefectByProcessPointIdAndProductId(String processPointId, String productId);
	public List<QiDefectResult> getDefects(String processPointId, String productId, String defectStatus);
	public List<QiDefectResult> getDefectByProductId(String productId);
	public List<QiDefectResult> getDefectByProductIdAndDefectStatus(String productId, String defectStatus);

	public List<QiDefectResultDto> getPdcByPPAndProductId(String processPointId, String productId);
	public List<QiDefectResultDto> getPdcByPPAndYearModel(String processPointId, String YearModel);
	
	public String createDefect(String productId, String defectCombinationId, String defectStatus, String processPointId, String associateId, String comment);
	public List<String> getRepairMethodByProcessPoint(String processPointId);

	public String repairDefect(String defectResultId, String productId, String repairProcessPointId, String defectStatus, String repairMethod,String associateId, String comment);
	public String repairDefectByDefectType(String productId, String repairProcessPointId, String defectTypeName, String repairMethod,String associateId, String comment);
	public String repairDefects(String productId, String processPointId, String repairProcessPointId, String repairMethod,String associateId, String comment);
	
}

