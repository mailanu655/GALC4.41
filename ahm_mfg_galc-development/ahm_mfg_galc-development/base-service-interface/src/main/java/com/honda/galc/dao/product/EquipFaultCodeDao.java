package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.EquipFaultCode;
import com.honda.galc.entity.product.EquipFaultCodeId;
import com.honda.galc.service.IDaoService;
/**
 * 
 * <h3>EquipFaultCodeDao Class description</h3>
 * <p> EquipFaultCodeDao description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Dec 17, 2012
 *
 *
 */
public interface EquipFaultCodeDao extends IDaoService<EquipFaultCode, EquipFaultCodeId>{
	
	public List<EquipFaultCode> findAllByGroupId(Short groupId);
	
	public List<EquipFaultCode> findAllManualImported();
	
}
