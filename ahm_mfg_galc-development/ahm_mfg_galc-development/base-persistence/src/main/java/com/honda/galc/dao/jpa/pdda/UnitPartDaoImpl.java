package com.honda.galc.dao.jpa.pdda;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.UnitPartDao;
import com.honda.galc.entity.pdda.UnitPart;
import com.honda.galc.entity.pdda.UnitPartId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class UnitPartDaoImpl extends BaseDaoImpl<UnitPart, UnitPartId>
		implements UnitPartDao {

	private static final String GET_UNIT_PARTS_FOR_MAINTENANCE_ID = "SELECT DISTINCT * "
			+ "FROM  VIOS.PVUPM1  A  " + "WHERE A.MAINTENANCE_ID=?1 ";

	private final String GET_PART_BLOCK = "SELECT A.PART_BLOCK_CODE FROM GALADM.BOM_TBX A," +
			" GALADM.MC_OP_PART_REV_TBX B  where A.MTC_MODEL=CAST(?1 AS CHARACTER(4)) AND A.MTC_TYPE=CAST(?2 AS CHARACTER(3)) AND B.OPERATION_NAME=CAST(?3 AS CHARACTER(32))" +
			" AND B.PART_ID=CAST(?4 AS CHARACTER(5)) AND B.PART_REV=CAST(?5 AS INTEGER) AND A.PART_NO=CAST(?6 AS CHARACTER(18)) AND A.PART_SECTION_CODE=B.PART_SECTION_CODE" +
			" AND A.PART_ITEM_NO=B.PART_ITEM_NO AND A.EFF_END_DATE>CURRENT_DATE  FETCH FIRST 1 ROWS ONLY";
	private final String GET_PART_DELIVERY_LOC ="Select distinct part.delivery_location from vios.pvupm1 part inner join GALADM.MC_PDDA_UNIT_REV_TBX pddaUnit "+
			" on part.MAINTENANCE_ID = pddaUnit.APVD_UNIT_MAINT_ID "+
			" Where part.mtc_model=?1 and part.mtc_type=?2 and "+
			" pddaUnit.OPERATION_NAME=?3 and pddaUnit.op_rev=?4 and "+
			" part.part_no=?5 and part.part_item_no=?6 and part.part_section_code=?7  FETCH First 1 Rows ONLY ";


	public List<UnitPart> findAllByMaintenanceId(int maintId) {
		Parameters param = Parameters.with("1", maintId);
		return  findAllByNativeQuery(GET_UNIT_PARTS_FOR_MAINTENANCE_ID,
				param);
	}

	public List<UnitPart> findAllByMaintenanceIdAndMTOC(int maintId,String modelCode,String modelTypeCode) {
		Parameters params = Parameters.with("id.maintenanceId", maintId);
		params.put("id.mtcModel", modelCode);
		params.put("id.mtcType", modelTypeCode);
		//TODO:  add FIF option here
		return  findAll(params);
}
	
	public String findPartBlockCode(String ymto, String opName, String partId, int partRev, String partNo) {
		//Quick Fix for not showing parts widget in AEP L5 : START
		if(ymto!=null && ymto.length()>=7) {
			Parameters params = Parameters.with("1", ymto.substring(0, 4));
			params.put("2", ymto.substring(4, 7));
			params.put("3", opName);
			params.put("4", partId);
			params.put("5", partRev);
			params.put("6", partNo);
			return findFirstByNativeQuery(GET_PART_BLOCK, params, String.class);
		}
		return null;
		//Quick Fix for not showing parts widget in AEP L5 : END
		
	}
	
	
	public String findDeliveryLocation(String model, String modelType, String opName, int opRev, String partNo, String partItemNo, String partSecCode) {
		Parameters params = Parameters.with("1", model);
		params.put("2", modelType);
		params.put("3", opName);
		params.put("4", opRev);
		params.put("5", partNo);
		params.put("6", partItemNo);
		params.put("7", partSecCode);
		return findFirstByNativeQuery(GET_PART_DELIVERY_LOC,params,String.class);
	}

}
