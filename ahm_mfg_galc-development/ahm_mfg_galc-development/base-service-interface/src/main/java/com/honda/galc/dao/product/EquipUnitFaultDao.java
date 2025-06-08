package com.honda.galc.dao.product;

import com.honda.galc.entity.product.EquipUnitFault;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>EquipUnitFaultDao Class description</h3>
 * <p> EquipUnitFaultDao description </p>
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
public interface EquipUnitFaultDao extends IDaoService<EquipUnitFault, Short>{
	public EquipUnitFault findByUnitName(String unitName);
	
	public Short minUnitId();
	
	public Short maxUnitId();
}
