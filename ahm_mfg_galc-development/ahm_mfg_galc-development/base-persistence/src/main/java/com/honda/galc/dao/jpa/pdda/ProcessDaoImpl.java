package com.honda.galc.dao.jpa.pdda;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.ProcessDao;
import com.honda.galc.entity.pdda.Process;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class ProcessDaoImpl extends BaseDaoImpl<Process,Integer>
		implements ProcessDao {

	private static final String GET_PROCESSES_FOR_CHG_FRM = "SELECT DISTINCT PR.* FROM GALADM.MC_PDDA_CHG_TBX PC "
			+ "JOIN VIOS.PVCFU1 CFU ON PC.CHANGE_FORM_ID = CFU.CHANGE_FORM_ID "
			+ "JOIN VIOS.PVPMX1 PR ON CFU.APVD_PROC_MAINT_ID = PR.MAINTENANCE_ID "
			+ "AND PC.REV_ID = CAST(?1 AS BIGINT) "
			+ " AND PR.ASM_PROC_NO = CAST(?2 AS CHARACTER(5)) "
			+ " ORDER BY PR.MAINTENANCE_ID";
	
	public List<Process> getAllBy(long revId, String asmProcNo) {
		Parameters param = Parameters.with("1", revId);
		param.put("2", asmProcNo);
		return findAllByNativeQuery(GET_PROCESSES_FOR_CHG_FRM, param);
	}
}
