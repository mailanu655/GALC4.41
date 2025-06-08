package com.honda.galc.dao.jpa.pdda;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.UnitDao;
import com.honda.galc.dao.pdda.UnitPpeImageDao;
import com.honda.galc.dto.UnitPpeImageDto;
import com.honda.galc.entity.pdda.Unit;
import com.honda.galc.entity.pdda.UnitPpeImage;
import com.honda.galc.entity.pdda.UnitPpeImageId;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class UnitPpeImageDaoImpl extends BaseDaoImpl<UnitPpeImage, UnitPpeImageId>
		implements UnitPpeImageDao {

	private static String FIND_APVD_UNITS = "SELECT PDDAUNITREV.OPERATION_NAME ,PDDAUNITREV.APVD_UNIT_MAINT_ID FROM GALADM.MC_PDDA_PLATFORM_TBX PDDAPLTFRM  " +
											 " JOIN GALADM.MC_VIOS_MASTER_PLATFORM_TBX VIOS_MAST_PLAT ON PDDAPLTFRM.PLANT_LOC_CODE=VIOS_MAST_PLAT.PLANT_LOC_CODE AND PDDAPLTFRM.DEPT_CODE=VIOS_MAST_PLAT.DEPT_CODE AND PDDAPLTFRM.MODEL_YEAR_DATE=VIOS_MAST_PLAT.MODEL_YEAR_DATE AND PDDAPLTFRM.PROD_SCH_QTY=VIOS_MAST_PLAT.PROD_SCH_QTY AND PDDAPLTFRM.PROD_ASM_LINE_NO=VIOS_MAST_PLAT.PROD_ASM_LINE_NO AND PDDAPLTFRM.VEHICLE_MODEL_CODE=VIOS_MAST_PLAT.VEHICLE_MODEL_CODE " +
			 								 " JOIN GALADM.MC_VIOS_MASTER_ASM_PROC_TBX VIOS_MAST_PROC ON VIOS_MAST_PLAT.VIOS_PLATFORM_ID=VIOS_MAST_PROC.VIOS_PLATFORM_ID AND PDDAPLTFRM.ASM_PROC_NO=VIOS_MAST_PROC.ASM_PROC_NO " +
											 " JOIN GALADM.MC_OP_REV_PLATFORM_TBX OPREVPLAT  ON (PDDAPLTFRM.PDDA_PLATFORM_ID = OPREVPLAT.PDDA_PLATFORM_ID)  " +
											 " JOIN GALADM.MC_OP_REV_TBX OPREV ON (OPREVPLAT.OPERATION_NAME = OPREV.OPERATION_NAME AND OPREVPLAT.OP_REV = OPREV.OP_REV " +
											 " AND OPREV.APPROVED <= CURRENT_TIMESTAMP AND OPREV.DEPRECATED IS NULL ) " +
											 " JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (OPREV.OPERATION_NAME = PDDAUNITREV.OPERATION_NAME AND OPREV.OP_REV = PDDAUNITREV.OP_REV) " + 
											 " JOIN VIOS.PVPPI1 UNITPPEIMG ON (UNITPPEIMG.MAINTENANCE_ID = PDDAUNITREV.APVD_PROC_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PPS')  " +
											 " WHERE VIOS_MAST_PROC.PROCESS_POINT_ID=CAST(?1 AS CHARACTER(16)) AND PDDAPLTFRM.APPROVED <= CURRENT_TIMESTAMP AND PDDAPLTFRM.DEPRECATED IS NULL " +
											 " GROUP BY PDDAUNITREV.OPERATION_NAME, PDDAUNITREV.APVD_UNIT_MAINT_ID ORDER BY PDDAUNITREV.OPERATION_NAME";
	
	public List<UnitPpeImageDto> findAllUnitPpeImgForProcessPoint(
			String processPoint) {
		Unit unit = null;
		Parameters params = Parameters.with("1", processPoint);
		
		ArrayList<UnitPpeImageDto> unitPpeImageArrLst = new ArrayList<UnitPpeImageDto>();
		List<Object[]> apvdUnitLst = findAllByNativeQuery(FIND_APVD_UNITS, params, Object[].class);
		
		for(Object[] apvdUnits : apvdUnitLst){
			
			unit = ServiceFactory.getDao(UnitDao.class).findByKey(Integer.parseInt(apvdUnits[1].toString()));
			Parameters ppeParams = Parameters.with("id.maintenanceId", unit.getMaintenanceId());
			List<UnitPpeImage> unitPpeImageLst = findAll(ppeParams);

			for(UnitPpeImage unitPpeImage : unitPpeImageLst){
				
				UnitPpeImageDto unitPpeImageDto = new UnitPpeImageDto();
				unitPpeImageDto.setUnitNo(unit.getUnitNo());
				unitPpeImageDto.setOperationName(apvdUnits[0].toString()); 
				unitPpeImageDto.setPpeRequired(unitPpeImage.getPpeRequired());
				unitPpeImageDto.setPotentialHazard(unitPpeImage.getPotentialHazard());
				unitPpeImageDto.setPpeUsage(unitPpeImage.getPpeUsage());
				unitPpeImageDto.setImage(unitPpeImage.getImage());
				
				unitPpeImageArrLst.add(unitPpeImageDto);
				unitPpeImage = null;
				
			}
			unit = null;
			unitPpeImageLst = null;
		}
		
		return unitPpeImageArrLst;
	}

}
