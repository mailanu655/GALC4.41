package com.honda.galc.dao.jpa.pdda;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.UnitImageDao;
import com.honda.galc.dto.PddaUnitImage;
import com.honda.galc.entity.pdda.UnitImage;
import com.honda.galc.entity.pdda.UnitImageId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class UnitImageDaoImpl extends BaseDaoImpl<UnitImage, UnitImageId>
		implements UnitImageDao {

	private static String GET_UNIT_CCPIMAGES = "SELECT UNITIMG.MAINTENANCE_ID AS MAINTENANCE_ID, UNITIMG.PLANT_LOC_CODE AS PLANT_LOC_CODE, UNITIMG.DEPT_CODE AS DEPT_CODE,  " +
	" UNITIMG.IMAGE_SEQ_NO AS IMAGE_SEQ_NO, UNITIMG.IMAGE_TIMESTAMP AS IMAGE_TIMESTAMP, UNITIMG.IMAGE AS IMAGE, UNITIMG.UNIT_IMAGE AS UNIT_IMAGE FROM VIOS.PVUMX1 UNIT " +
	" JOIN VIOS.PVUMI1 UNITIMG ON (UNIT.MAINTENANCE_ID = UNITIMG.MAINTENANCE_ID AND UNIT.PLANT_LOC_CODE = UNITIMG.PLANT_LOC_CODE AND UNIT.DEPT_CODE = UNITIMG.DEPT_CODE AND UNITIMG.UNIT_IMAGE LIKE 'CCP%')  " +
	" JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (UNIT.MAINTENANCE_ID = PDDAUNITREV.APVD_UNIT_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PCF') " +
	" JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME AND PDDAUNITREV.OP_REV = STRUC.OP_REV AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID " +
	" AND LOCATE(SUBSTR(STRUC.PRODUCT_SPEC_CODE,2,3),UNITIMG.UNIT_IMAGE)>0  " +
	" AND ( LOCATE(SUBSTR(STRUC.PRODUCT_SPEC_CODE,5,3),UNITIMG.UNIT_IMAGE)>0  " +
    "      or  LOCATE(SUBSTR(STRUC.PRODUCT_SPEC_CODE,2,3)||'ALL',UNITIMG.UNIT_IMAGE)>0 )) " + 
	" JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV) " +
	" WHERE PRODSTRU.PRODUCT_ID=?1 AND STRUC.PROCESS_POINT_ID=CAST (?2 AS CHARACTER(16))";
	private static String GET_UNIT_MAINIMAGE = "SELECT UNITIMG.MAINTENANCE_ID AS MAINTENANCE_ID, UNITIMG.PLANT_LOC_CODE AS PLANT_LOC_CODE, UNITIMG.DEPT_CODE AS DEPT_CODE,  " +
	" UNITIMG.IMAGE_SEQ_NO AS IMAGE_SEQ_NO, UNITIMG.IMAGE_TIMESTAMP AS IMAGE_TIMESTAMP, UNITIMG.IMAGE AS IMAGE, UNITIMG.UNIT_IMAGE AS UNIT_IMAGE FROM VIOS.PVUMX1 UNIT " +
	" JOIN VIOS.PVUMI1 UNITIMG ON (UNIT.MAINTENANCE_ID = UNITIMG.MAINTENANCE_ID AND UNIT.PLANT_LOC_CODE = UNITIMG.PLANT_LOC_CODE AND UNIT.DEPT_CODE = UNITIMG.DEPT_CODE AND UNITIMG.UNIT_IMAGE LIKE 'ASSY%')  " +
	" JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (UNIT.MAINTENANCE_ID = PDDAUNITREV.APVD_UNIT_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PCF') " +
	" JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME AND PDDAUNITREV.OP_REV = STRUC.OP_REV AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID ) " +
	" JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV) " +
	" WHERE PRODSTRU.PRODUCT_ID=?1 AND STRUC.PROCESS_POINT_ID=CAST (?2 AS CHARACTER(16)) FETCH FIRST 1 ROWS ONLY ";
	
	public List<UnitImage> findAllUnitImages(int maintenanceId, String[] orderBy) {
		return findAll(Parameters.with("id.maintenanceId", maintenanceId), orderBy);
	}

	public List<PddaUnitImage> findAllUnitImages(int maintenanceId) {
		
		String[] orderBy = {"id.imageSeqNo"};
		
		List<UnitImage> unitImageLst = findAll(Parameters.with("id.maintenanceId", maintenanceId), orderBy);
		
		ArrayList<PddaUnitImage> unitImgLst = null;
		
		for(UnitImage unitImg : unitImageLst){
			if(unitImgLst == null)
				unitImgLst = new ArrayList<PddaUnitImage>();
			
			PddaUnitImage unit = new PddaUnitImage();
			unit.setImage(unitImg.getImage());
			unit.setUnitImage(unitImg.getId().getUnitImage().trim());
				
			unitImgLst.add(unit);
			unitImg = null;
		}
		unitImageLst = null;
		return unitImgLst;
		
	}
	
	public List<PddaUnitImage> getUnitCCPImages(String productId,
			String processPoint) {

		ArrayList<PddaUnitImage> unitImgLst = null;
		Parameters params = Parameters.with("1", productId);
		params.put("2", processPoint);
		
		List<UnitImage> pddaUnitImgLst = findAllByNativeQuery(GET_UNIT_CCPIMAGES, params);
		for(UnitImage unitImg : pddaUnitImgLst){
			if(unitImgLst == null)
				unitImgLst = new ArrayList<PddaUnitImage>();
			
			PddaUnitImage unit = new PddaUnitImage();
			unit.setMaintenanceId(unitImg.getId().getMaintenanceId());
			unit.setImage(unitImg.getImage());
			unit.setUnitImage(unitImg.getId().getUnitImage());
			unitImgLst.add(unit);
			unitImg = null;
		}
		return unitImgLst;
		
	}

	public List<PddaUnitImage> getUnitMainImage(String productId,
			String processPoint) {

		ArrayList<PddaUnitImage> unitImgLst = null;
		Parameters params = Parameters.with("1", productId);
		params.put("2", processPoint);
		
		List<UnitImage> pddaUnitImgLst = findAllByNativeQuery(GET_UNIT_MAINIMAGE, params);
		for(UnitImage unitImg : pddaUnitImgLst){
			if(unitImgLst == null)
				unitImgLst = new ArrayList<PddaUnitImage>();
			
			PddaUnitImage unit = new PddaUnitImage();
			unit.setMaintenanceId(unitImg.getId().getMaintenanceId());
			unit.setImage(unitImg.getImage());
			unit.setUnitImage(unitImg.getId().getUnitImage());
			unitImgLst.add(unit);
			unitImg = null;
		}
		return unitImgLst;
		
	}

	
}
