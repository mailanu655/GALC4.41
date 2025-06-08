package com.honda.galc.dao.jpa.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCViosMasterProcessDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MCViosMasterProcess;
import com.honda.galc.entity.conf.MCViosMasterProcessId;
import com.honda.galc.service.Parameters;
import com.honda.galc.vios.dto.MCViosMasterProcessDto;

/**
 * <h3>MCViosMasterProcessDaoImpl Class description</h3>
 * <p>
 * DaoImpl class for MCViosMasterProcess
 * </p>
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
 * @author Hemant Kumar<br>
 *         Aug 28, 2018
 */
public class MCViosMasterProcessDaoImpl extends BaseDaoImpl<MCViosMasterProcess, MCViosMasterProcessId>
		implements MCViosMasterProcessDao {

	private static final String FIND_ALL_BY_PROCESS_POINT = "SELECT ASM_PROC_NO, PROCESS_POINT_ID, PROCESS_SEQ_NUM FROM galadm.MC_VIOS_MASTER_ASM_PROC_TBX WHERE VIOS_PLATFORM_ID=?1 AND PROCESS_POINT_ID=?2 ORDER BY PROCESS_SEQ_NUM";

	private static final String FIND_ALL_BY_PROCESS_NO = "SELECT ASM_PROC_NO, PROCESS_POINT_ID, PROCESS_SEQ_NUM FROM galadm.MC_VIOS_MASTER_ASM_PROC_TBX "
			+ "WHERE VIOS_PLATFORM_ID=?1 "
			+ "AND PROCESS_POINT_ID IN (SELECT PROCESS_POINT_ID FROM galadm.MC_VIOS_MASTER_ASM_PROC_TBX WHERE VIOS_PLATFORM_ID=?1 AND ASM_PROC_NO=?2) "
			+ "ORDER BY PROCESS_SEQ_NUM";

	private static final String GET_IN_USE_PROCESSES = "SELECT DISTINCT a.* FROM galadm.MC_VIOS_MASTER_ASM_PROC_TBX a "
			+ "JOIN galadm.MC_VIOS_MASTER_PLATFORM_TBX b on a.VIOS_PLATFORM_ID=b.VIOS_PLATFORM_ID "
			+ "JOIN galadm.MC_PDDA_PLATFORM_TBX c on b.PLANT_LOC_CODE=c.PLANT_LOC_CODE AND b.DEPT_CODE=c.DEPT_CODE AND b.MODEL_YEAR_DATE=c.MODEL_YEAR_DATE AND b.PROD_SCH_QTY=c.PROD_SCH_QTY "
			+ "AND b.PROD_ASM_LINE_NO=c.PROD_ASM_LINE_NO AND b.VEHICLE_MODEL_CODE=c.VEHICLE_MODEL_CODE AND a.ASM_PROC_NO=c.ASM_PROC_NO "
			+ "WHERE a.VIOS_PLATFORM_ID=?1 AND a.ASM_PROC_NO=?2 and c.DEPRECATED IS NULL AND c.APPROVED IS NOT NULL";

	private static final String FIND_ALL_BY_PLATFORM_ID = "SELECT * FROM galadm.MC_VIOS_MASTER_ASM_PROC_TBX WHERE VIOS_PLATFORM_ID = ?1 order by PROCESS_POINT_ID, PROCESS_SEQ_NUM ";

	
	@Override
	public int getMaxProcesSeqNumBy(String viosPlatformId, String processPointId) {
		Integer maxSeq = max("processSeqNum", Integer.class,
				Parameters.with("id.viosPlatformId", viosPlatformId).put("processPointId", processPointId));
		return maxSeq == null ? 0 : maxSeq.intValue();
	}
	
	@Transactional
	@Override
	public void deleteByProcessPointId(String processPointId, String viosPlatform) {
		Parameters params = Parameters.with("processPointId", processPointId).put("id.viosPlatformId", viosPlatform);
		delete(params);
	}

	@Override
	public void resequenceProcesses(String viosPlatformId, String processPointId, int fromValue) {
		List<MCViosMasterProcess> processList = findAll(
				Parameters.with("id.viosPlatformId", viosPlatformId).put("processPointId", processPointId),
				new String[] { "processSeqNum" });
		int i = fromValue;
		for (MCViosMasterProcess process : processList) {
			if (process.getProcessSeqNum() >= fromValue) {
				i++;
				process.setProcessSeqNum(i);
				save(process);
			}
		}
	}

	@Transactional
	@Override
	public MCViosMasterProcess resequenceAndInsert(MCViosMasterProcess process) {
		resequenceProcesses(process.getId().getViosPlatformId(), process.getProcessPointId(),
				process.getProcessSeqNum());
		return insert(process);
	}

	@Transactional
	@Override
	public void resequenceAndDelete(MCViosMasterProcess process) {
		remove(process);
		resequenceProcesses(process.getId().getViosPlatformId(), process.getProcessPointId(), 0);
	}

	@Transactional
	@Override
	public void resequenceAndMove(String oldProcessPointId, MCViosMasterProcess newProcess) {
		save(newProcess);
		resequenceProcesses(newProcess.getId().getViosPlatformId(), oldProcessPointId, 0);
	}

	@Override
	public List<MCViosMasterProcessDto> findAllByProcessPoint(String viosPlatformId, String processPoint) {
		Parameters params = Parameters.with("1", viosPlatformId).put("2", processPoint);
		return findAllByNativeQuery(FIND_ALL_BY_PROCESS_POINT, params, MCViosMasterProcessDto.class);
	}

	@Override
	public List<MCViosMasterProcessDto> findAllByProcessNo(String viosPlatformId, String processNo) {
		Parameters params = Parameters.with("1", viosPlatformId).put("2", processNo);
		return findAllByNativeQuery(FIND_ALL_BY_PROCESS_NO, params, MCViosMasterProcessDto.class);
	}

	@Override
	public MCViosMasterProcess findBy(String viosPlatformId, String processNo, String processPoint) {
		Parameters params = Parameters.with("id.viosPlatformId", viosPlatformId).put("id.asmProcNo", processNo)
				.put("processPointId", processPoint);
		return findFirst(params);
	}

	@Override
	public int getCountByViosPlatformId(String viosPlatformId) {
		List<MCViosMasterProcess> processList = findAll(Parameters.with("id.viosPlatformId", viosPlatformId));
		return processList.size();
	}

	@Override
	public int getMappedCountBy(String viosPlatformId, String processNo) {
		Parameters params = Parameters.with("1", viosPlatformId).put("2", processNo);
		List<MCViosMasterProcess> processList = findAllByNativeQuery(GET_IN_USE_PROCESSES, params);
		return processList.size();
	}

	public List<MCViosMasterProcess> findAllData(String viosPlatformId) {
		Parameters params = Parameters.with("1", viosPlatformId);
		return findAllByNativeQuery(FIND_ALL_BY_PLATFORM_ID, params, MCViosMasterProcess.class);
	}
	

}
