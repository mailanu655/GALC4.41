package com.honda.galc.dao.jpa.pdda;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.UnitSafetyImageDao;
import com.honda.galc.dto.UnitOfOperation;
import com.honda.galc.entity.pdda.UnitSafetyImage;
import com.honda.galc.entity.pdda.UnitSafetyImageId;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class UnitSafetyImageDaoImpl extends BaseDaoImpl<UnitSafetyImage, UnitSafetyImageId>
		implements UnitSafetyImageDao {

	
	private static String GET_UNIT_SAFETYIMAGELIST_DIVISION_MODE = "SELECT UNITIMG.MAINTENANCE_ID AS MAINTENANCE_ID, UNIT.UNIT_NO AS UNIT_NO FROM VIOS.PVUSH1 UNITIMG   " +
										 " JOIN VIOS.PVUMX1 UNIT ON (UNITIMG.MAINTENANCE_ID = UNIT.MAINTENANCE_ID)  " +
										 " WHERE UNITIMG.MAINTENANCE_ID IN (SELECT DISTINCT (PDDAUNITREV.APVD_UNIT_MAINT_ID) FROM GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV    " +
										 " JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME AND PDDAUNITREV.OP_REV = STRUC.OP_REV AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID)  " + 
										 " JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV)   " +
										 " WHERE PRODSTRU.PRODUCT_ID=CAST(?1 AS CHARACTER(17)) AND STRUC.PROCESS_POINT_ID=CAST (?2 AS CHARACTER(16)) AND PDDAUNITREV.PDDA_REPORT IN ('PCF')) " +
										 " GROUP BY UNITIMG.MAINTENANCE_ID, UNIT.UNIT_NO " +
										 " ORDER BY UNITIMG.MAINTENANCE_ID " ;
	
	private static String GET_UNIT_SAFETYIMAGELIST_PROCESS_POINT_MODE = "SELECT UNITIMG.MAINTENANCE_ID AS MAINTENANCE_ID, UNIT.UNIT_NO AS UNIT_NO FROM VIOS.PVUSH1 UNITIMG   " +
			 " JOIN VIOS.PVUMX1 UNIT ON (UNITIMG.MAINTENANCE_ID = UNIT.MAINTENANCE_ID)  " +
			 " WHERE UNITIMG.MAINTENANCE_ID IN (SELECT DISTINCT (PDDAUNITREV.APVD_UNIT_MAINT_ID) FROM GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV    " +
			 " JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME AND PDDAUNITREV.OP_REV = STRUC.OP_REV AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID)  " + 
			 " JOIN GALADM.MC_PRODUCT_STRU_FOR_PROCESS_POINT_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV)   " +
			 " WHERE PRODSTRU.PRODUCT_ID=CAST(?1 AS CHARACTER(17)) AND STRUC.PROCESS_POINT_ID=CAST (?2 AS CHARACTER(16)) AND PDDAUNITREV.PDDA_REPORT IN ('PCF')) " +
			 " GROUP BY UNITIMG.MAINTENANCE_ID, UNIT.UNIT_NO " +
			 " ORDER BY UNITIMG.MAINTENANCE_ID " ;
	
	
	public List<UnitSafetyImage> findAllSafetyImages(int maintenanceId, String[] orderBy) {
		return findAll(Parameters.with("id.maintenanceId", maintenanceId), orderBy);
	}
	public List<UnitSafetyImage> findAllSafetyImages(int maintenanceId) {
		String[] orderBy = {"id.imageSeqNo"};
		return findAll(Parameters.with("id.maintenanceId", maintenanceId), orderBy);
	}
	
	@Transactional
	public ArrayList<UnitOfOperation> getUnitSafetyImageList(String productId ,String processPoint, String mode) throws SQLException {
		ArrayList<UnitOfOperation> unitImgLst = null;
		Parameters params = Parameters.with("1", productId);
		params.put("2", processPoint);

		List<Object[]> pddaUnitImgLst = null;
		
		if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.DIVISION_MODE.toString()))
			pddaUnitImgLst = findAllByNativeQuery(GET_UNIT_SAFETYIMAGELIST_DIVISION_MODE, params, Object[].class);
		else if (StringUtils.equalsIgnoreCase(mode, StructureCreateMode.PROCESS_POINT_MODE.toString()))
			pddaUnitImgLst = findAllByNativeQuery(GET_UNIT_SAFETYIMAGELIST_PROCESS_POINT_MODE, params, Object[].class);
		
		for(Object[] unitImg : pddaUnitImgLst){
			if(unitImgLst == null)
				unitImgLst = new ArrayList<UnitOfOperation>();
			
			UnitOfOperation unit = new UnitOfOperation();
			unit.setPddaMaintenanceId(unitImg[0].toString());
			unit.setUnitNo(unitImg[1].toString());
			
			unitImgLst.add(unit);
		}
		return unitImgLst;
	}
}
