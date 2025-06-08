package com.honda.galc.dao.product;


import java.util.List;

import com.honda.galc.entity.product.RepairProcessPoint;
import com.honda.galc.entity.product.RepairProcessPointId;
import com.honda.galc.service.IDaoService;


/**
 * 
 * 
 * 
 */
public interface RepairProcessPointDao extends IDaoService<RepairProcessPoint, RepairProcessPointId> {
	
	
	public List<RepairProcessPoint> findAllRepairPartsForProcessPoint(String processPointId);
	public List<RepairProcessPoint> findAllRepairPartsByProcessPointAndPartName(String processPointId, String partName);
	public int updateSequenceNo(String processPointId,	String partName, Integer seqNo);
	public List<String> findRepairProcessPointForPartName(String partName);
	public List<RepairProcessPoint> findAllRepairPartsByPartNameAndPartId(String partName,String partId);
	public List<Object[]> findRepairPartData(String processPointId);
	
}