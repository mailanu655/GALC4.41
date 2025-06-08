package com.honda.galc.dao.product;

import com.honda.galc.entity.product.EquipUnitGroup;
import com.honda.galc.entity.product.EquipUnitGroupId;
import com.honda.galc.service.IDaoService;
/**
 * 
 * <h3>EquipUnitGroupDao Class description</h3>
 * <p> EquipUnitGroupDao description </p>
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
public interface EquipUnitGroupDao extends IDaoService<EquipUnitGroup, EquipUnitGroupId>{
	
	public void deleteAllForGroup(short groupId);
}
