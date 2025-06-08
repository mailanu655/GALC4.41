package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.EquipmentGroupDao;
import com.honda.galc.entity.product.EquipmentGroup;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>EquipmentGroupDaoImpl Class description</h3>
 * <p> EquipmentGroupDaoImpl description </p>
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
public class EquipmentGroupDaoImpl extends BaseDaoImpl<EquipmentGroup,Short>implements EquipmentGroupDao{
	private final static String FIND_ALL_BY_UNIT_ID =
		"SELECT A.* FROM GALADM.EQUIP_GROUP_TBX A,GALADM.EQUIP_GROUP_UNIT_TBX B " + 
		"WHERE B.UNIT_ID = ?1 AND A.GROUP_ID = B.GROUP_ID AND A.FAULT_COUNT > 0";  

	public List<EquipmentGroup> findAllByUnitId(Short unitId) {
		return findAllByNativeQuery(FIND_ALL_BY_UNIT_ID, Parameters.with("1", unitId));
	}

}
