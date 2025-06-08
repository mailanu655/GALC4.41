/**
 * 
 */
package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.dao.product.BaseOrderStructureDao;
import com.honda.galc.dto.MfgCtrlMadeFrom;
import com.honda.galc.dto.StructureDetailsDto;
import com.honda.galc.dto.StructureUnitDetailsDto;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPoint;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPointId;

/**
 * @author Fredrick Yessaian
 * @date Apr 01,2016
 */
public interface MCOrderStructureForProcessPointDao extends 
	BaseOrderStructureDao<MCOrderStructureForProcessPoint, MCOrderStructureForProcessPointId> {
	
	public List<MfgCtrlMadeFrom> getMadeFrom(String orderNo, String processPointId);
	
	public List<MfgCtrlMadeFrom> getMadeFromByOrderNoAndProdSpecCode(String orderNo, String prodSpecCode);
	
	public MCOrderStructureForProcessPoint findByKey(String orderId, String modeId);
	
	public List<StructureDetailsDto> findByOrderNumberDivisionAndProcessPoint(String orderNo, String divisionId, String processPointId);
	
	public List<StructureUnitDetailsDto> findAllByOrderNoDivisionAndProcessPoint(String orderNo, String divisionId, String processPoint);
}
