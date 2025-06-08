package com.honda.galc.dao.jpa.pdda;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.ProcessPpeImageDao;
import com.honda.galc.dto.ProcessPpeImageDto;
import com.honda.galc.entity.pdda.ProcessPpeImage;
import com.honda.galc.entity.pdda.ProcessPpeImageId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class ProcessPpeImageDaoImpl extends BaseDaoImpl<ProcessPpeImage, ProcessPpeImageId>
		implements ProcessPpeImageDao {
	
	private static String PROCESS_PPEIMG_FOR_PROCESS_POINT = "SELECT PROCESSPPEIMG.MAINTENANCE_ID AS MAINTENANCE_ID, PROCESSPPEIMG.PLANT_LOC_CODE AS PLANT_LOC_CODE, PROCESSPPEIMG.DEPT_CODE AS DEPT_CODE,  " +
										" PROCESSPPEIMG.IMAGE_SEQ_NO AS IMAGE_SEQ_NO, PROCESSPPEIMG.IMAGE_TIMESTAMP AS IMAGE_TIMESTAMP, PROCESSPPEIMG.PPE_ID AS PPE_ID,  " +
										" PROCESSPPEIMG.PPE_REQUIRED AS PPE_REQUIRED, PROCESSPPEIMG.IMAGE AS IMAGE, PROCESSPPEIMG.POTENTIAL_HAZARD AS POTENTIAL_HAZARD, PROCESSPPEIMG.PPE_USAGE AS PPE_USAGE  " +
										" FROM VIOS.PVPPI1 PROCESSPPEIMG WHERE MAINTENANCE_ID IN (SELECT PDDAUNITREV.APVD_PROC_MAINT_ID FROM GALADM.MC_PDDA_PLATFORM_TBX PDDAPLTFRM " +
										" JOIN GALADM.MC_VIOS_MASTER_PLATFORM_TBX VIOS_MAST_PLAT ON PDDAPLTFRM.PLANT_LOC_CODE=VIOS_MAST_PLAT.PLANT_LOC_CODE AND PDDAPLTFRM.DEPT_CODE=VIOS_MAST_PLAT.DEPT_CODE AND PDDAPLTFRM.MODEL_YEAR_DATE=VIOS_MAST_PLAT.MODEL_YEAR_DATE AND PDDAPLTFRM.PROD_SCH_QTY=VIOS_MAST_PLAT.PROD_SCH_QTY AND PDDAPLTFRM.PROD_ASM_LINE_NO=VIOS_MAST_PLAT.PROD_ASM_LINE_NO AND PDDAPLTFRM.VEHICLE_MODEL_CODE=VIOS_MAST_PLAT.VEHICLE_MODEL_CODE " +
										" JOIN GALADM.MC_VIOS_MASTER_ASM_PROC_TBX VIOS_MAST_PROC ON VIOS_MAST_PLAT.VIOS_PLATFORM_ID=VIOS_MAST_PROC.VIOS_PLATFORM_ID AND PDDAPLTFRM.ASM_PROC_NO=VIOS_MAST_PROC.ASM_PROC_NO " +
										" JOIN GALADM.MC_OP_REV_PLATFORM_TBX OPREVPLAT  ON (PDDAPLTFRM.PDDA_PLATFORM_ID = OPREVPLAT.PDDA_PLATFORM_ID) " +
										" JOIN GALADM.MC_OP_REV_TBX OPREV ON (OPREVPLAT.OPERATION_NAME = OPREV.OPERATION_NAME AND OPREVPLAT.OP_REV = OPREV.OP_REV AND OPREV.APPROVED <= CURRENT_TIMESTAMP AND OPREV.DEPRECATED IS NULL) " +
										" JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (OPREV.OPERATION_NAME = PDDAUNITREV.OPERATION_NAME AND OPREV.OP_REV = PDDAUNITREV.OP_REV) " +
										" JOIN VIOS.PVPPI1 UNITPPEIMG ON (UNITPPEIMG.MAINTENANCE_ID = PDDAUNITREV.APVD_PROC_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PPS') " +
										" WHERE VIOS_MAST_PROC.PROCESS_POINT_ID=CAST(?1 AS CHARACTER(16))  AND PDDAPLTFRM.APPROVED <= CURRENT_TIMESTAMP AND PDDAPLTFRM.DEPRECATED IS NULL " +
										" GROUP BY PDDAUNITREV.APVD_PROC_MAINT_ID)";
	

	public List<ProcessPpeImageDto> findAllPpeImageForProcessPoint(
			String processPoint) {
		
		Parameters params = Parameters.with("1", processPoint);
		ArrayList<ProcessPpeImageDto> ppeImgArrLst = new ArrayList<ProcessPpeImageDto>();
		
		List<ProcessPpeImage> ppeImgLstjpa = findAllByNativeQuery(PROCESS_PPEIMG_FOR_PROCESS_POINT, params);
		
		for(ProcessPpeImage processPpeImage : ppeImgLstjpa){
			ProcessPpeImageDto processPpeImageDto = new ProcessPpeImageDto();
			
			
			processPpeImageDto.setPpeRequired(processPpeImage.getPpeRequired());
			processPpeImageDto.setPotentialHazard(processPpeImage.getPotentialHazard());
			processPpeImageDto.setPpeUsage(processPpeImage.getPpeUsage());
			processPpeImageDto.setImage(processPpeImage.getImage());
			
			ppeImgArrLst.add(processPpeImageDto);
			processPpeImage = null;
			processPpeImageDto = null;
		}
		
		return ppeImgArrLst;
	}
	
}
