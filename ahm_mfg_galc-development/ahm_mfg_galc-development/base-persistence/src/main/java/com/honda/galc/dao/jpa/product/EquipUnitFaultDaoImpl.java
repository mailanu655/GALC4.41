package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.EquipUnitFaultDao;
import com.honda.galc.entity.product.EquipUnitFault;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>EquipUnitFaultDaoImpl Class description</h3>
 * <p> EquipUnitFaultDaoImpl description </p>
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
public class EquipUnitFaultDaoImpl extends BaseDaoImpl<EquipUnitFault,Short>implements EquipUnitFaultDao{

	public EquipUnitFault findByUnitName(String unitName) {
		return findFirst(Parameters.with("unitName", unitName));
	}

	public Short minUnitId() {
		return min("unitId",Short.class);
	}

	public Short maxUnitId() {
		return max("unitId",Short.class);
	}



}
