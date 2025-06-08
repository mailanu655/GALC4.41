package com.honda.galc.dao.jpa.conf;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.constant.RevisionType;
import com.honda.galc.dao.conf.MCPddaChangeDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MCPddaChange;
import com.honda.galc.entity.conf.MCPddaChangeId;
import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public class MCPddaChangeDaoImpl extends
		BaseDaoImpl<MCPddaChange, MCPddaChangeId> implements MCPddaChangeDao {
	
	private static final String FIND_UNAPPROVED_REV = "SELECT DISTINCT PC.REV_ID FROM GALADM.MC_PDDA_CHG_TBX PC "
			+ "JOIN GALADM.MC_REV_TBX R ON PC.REV_ID=R.REV_ID "
			+ "JOIN VIOS.PVCFR1 C ON PC.CHANGE_FORM_ID=C.CHANGE_FORM_ID "
			+ "JOIN VIOS.PVCFU1 CFU ON PC.CHANGE_FORM_ID=CFU.CHANGE_FORM_ID "
			+ "WHERE "
			+ "PC.REV_ID < CAST(?1 AS BIGINT) AND "
			+ "R.REV_STATUS <> '"+RevisionStatus.APPROVED.getRevStatus()+"' AND "
			+ "C.PLANT_LOC_CODE=?2 AND C.DEPT_CODE=?3 AND C.MODEL_YEAR_DATE=?4 AND C.PROD_SCH_QTY=?5 AND C.PROD_ASM_LINE_NO=?6 AND C.VEHICLE_MODEL_CODE=?7 AND "
			+ "CFU.UNIT_NO IN (SELECT CFU1.UNIT_NO FROM GALADM.MC_PDDA_CHG_TBX PC1 JOIN VIOS.PVCFU1 CFU1 ON PC1.CHANGE_FORM_ID=CFU1.CHANGE_FORM_ID WHERE PC1.REV_ID=CAST(?1 AS BIGINT)) "
			+ "UNION "
			+ "SELECT DISTINCT REV.REV_ID FROM GALADM.MC_PDDA_PLATFORM_TBX P "
			+ "JOIN GALADM.MC_REV_TBX REV ON P.REV_ID=REV.REV_ID "
			+ "WHERE "
			+ "P.REV_ID < CAST(?1 AS BIGINT) AND "
			+ "REV.REV_TYPE='"+RevisionType.PLATFORM_CHG.getRevType()+"' AND "
			+ "REV.REV_STATUS <> '"+RevisionStatus.APPROVED.getRevStatus()+"' AND "
			+ "P.PLANT_LOC_CODE=?2 AND P.DEPT_CODE=?3 AND P.MODEL_YEAR_DATE=?4 AND P.PROD_SCH_QTY=?5 AND P.PROD_ASM_LINE_NO=?6 AND P.VEHICLE_MODEL_CODE=?7 AND "
			+ "P.ASM_PROC_NO IN (SELECT P1.ASM_PROC_NO FROM GALADM.MC_PDDA_PLATFORM_TBX P1 WHERE P1.REV_ID=CAST(?1 AS BIGINT))";

	public List<Long> getUnapprovedOldRevisions(long revisionId) {
		List<Long> revisionList = new ArrayList<Long>();
		MCPddaChange pddaChange = findFirst(Parameters.with("id.revisionId", revisionId));
		if(pddaChange!=null) {
			ChangeForm changeForm = pddaChange.getChangeForm();
			if(changeForm!=null) {
				Parameters params = Parameters.with("1", revisionId);
				params.put("2", changeForm.getPlantLocCode());
				params.put("3", changeForm.getDeptCode());
				params.put("4", changeForm.getModelYearDate());
				params.put("5", changeForm.getProdSchQty());
				params.put("6", changeForm.getProdAsmLineNo());
				params.put("7", changeForm.getVehicleModelCode());
				revisionList= findAllByNativeQuery(FIND_UNAPPROVED_REV, params, Long.class);
			}
		}
		return revisionList;
	}

}
