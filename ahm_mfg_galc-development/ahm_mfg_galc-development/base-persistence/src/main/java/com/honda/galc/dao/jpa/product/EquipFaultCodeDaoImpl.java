package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.EquipFaultCodeDao;
import com.honda.galc.entity.product.EquipFaultCode;
import com.honda.galc.entity.product.EquipFaultCodeId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>EquipFaultCodeDaoImpl Class description</h3>
 * <p> EquipFaultCodeDaoImpl description </p>
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
public class EquipFaultCodeDaoImpl extends BaseDaoImpl<EquipFaultCode,EquipFaultCodeId>implements EquipFaultCodeDao{

	private final static String FIND_ALL_BY_GROUP_ID =
		"SELECT A.* FROM GALADM.EQUIP_FAULT_CODE_TBX A,GALADM.EQUIP_UNIT_FAULT_TBX B,GALADM.EQUIP_GROUP_UNIT_TBX C " + 
		"WHERE C.GROUP_ID = ?1 AND A.UNIT_ID = C.UNIT_ID AND A.UNIT_ID = B.UNIT_ID AND " +
		"A.FAULT_CODE = B.CURRENT_FAULT_CODE AND B.CURRENT_FAULT_CODE > 0 AND C.PRIORITY > 0 AND A.IS_CONTROLLABLE > 0 " + 
		"ORDER BY C.PRIORITY DESC FOR READ ONLY";

	private final static String FIND_ALL_MANUAL_IMPORTED =
		"SELECT a.* FROM GALADM.EQUIP_FAULT_CODE_TBX a " +
		"LEFT OUTER JOIN GALADM.EQUIP_UNIT_FAULT_TBX b on b.UNIT_ID = a.UNIT_ID " +
		"WHERE b.IS_MANUAL_IMPORT = 1";
	
	public List<EquipFaultCode> findAllByGroupId(Short groupId) {
		return findAllByNativeQuery(FIND_ALL_BY_GROUP_ID, Parameters.with("1", groupId));
	}

	public List<EquipFaultCode> findAllManualImported() {
		return findAllByNativeQuery(FIND_ALL_MANUAL_IMPORTED,null);
	}

}
