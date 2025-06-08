/**
 * 
 */
package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.dao.product.BaseOrderStructureDao;
import com.honda.galc.dto.MfgCtrlMadeFrom;
import com.honda.galc.dto.StructureDetailsDto;
import com.honda.galc.dto.StructureUnitDetailsDto;
import com.honda.galc.entity.conf.MCOrderStructure;
import com.honda.galc.entity.conf.MCOrderStructureId;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCOrderStructureDao extends
		BaseOrderStructureDao<MCOrderStructure, MCOrderStructureId> {

	public List<MCOrderStructure> findAllByProductSpecCode(
			String productSpecCode);

	public List<MCOrderStructure> findAllByStructureRevision(
			long structureRevision);

	public List<MfgCtrlMadeFrom> getMadeFromByOrderNoAndProdSpecCode(
			String orderNo, String prodSpecCode);

	public List<MCOrderStructure> getUnmappedOrderIds();

	public MCOrderStructure findByKey(String orderId, String modeId);
	
	public List<StructureDetailsDto> findByOrderNumberAndDivision(String orderNo, String divisionId);
	
	public List<MCOrderStructure> findOrderByStructureRevAndDivId(long structureRev, String divisionId);
	
	public List<StructureUnitDetailsDto> findAllUnitDetailsByOrderNoAndDivision(String orderNo, String divisionId);

}
