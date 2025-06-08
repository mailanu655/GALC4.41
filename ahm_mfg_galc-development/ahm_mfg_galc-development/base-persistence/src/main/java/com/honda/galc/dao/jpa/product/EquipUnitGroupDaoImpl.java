package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.EquipUnitGroupDao;
import com.honda.galc.entity.product.EquipUnitGroup;
import com.honda.galc.entity.product.EquipUnitGroupId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>EquipUnitGroupDaoImpl Class description</h3>
 * <p> EquipUnitGroupDaoImpl description </p>
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
public class EquipUnitGroupDaoImpl extends BaseDaoImpl<EquipUnitGroup,EquipUnitGroupId>implements EquipUnitGroupDao{

	@Transactional
	public void deleteAllForGroup(short groupId) {
		delete(Parameters.with("id.groupId", groupId));
	}

	
}
