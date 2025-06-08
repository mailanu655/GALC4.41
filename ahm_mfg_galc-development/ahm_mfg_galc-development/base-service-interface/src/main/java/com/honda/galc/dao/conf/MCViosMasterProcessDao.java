package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCViosMasterProcess;
import com.honda.galc.entity.conf.MCViosMasterProcessId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.IViosDao;
import com.honda.galc.vios.dto.MCViosMasterProcessDto;
/**
 * <h3>MCViosMasterProcessDao Class description</h3>
 * <p>
 * Interface for MCViosMasterProcessDaoImpl
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
 *        Aug 28, 2018
 */
public interface MCViosMasterProcessDao extends IDaoService<MCViosMasterProcess, MCViosMasterProcessId>, IViosDao<MCViosMasterProcess> {
	
	public int getMaxProcesSeqNumBy(String viosPlatformId, String processPointId);
	
	public void resequenceProcesses(String viosPlatformId, String processPointId, int fromValue);
	
	public MCViosMasterProcess resequenceAndInsert(MCViosMasterProcess process);
	
	public void resequenceAndDelete(MCViosMasterProcess process);
	
	public void resequenceAndMove(String oldProcessPointId, MCViosMasterProcess newProcess);
	
	public List<MCViosMasterProcessDto> findAllByProcessPoint(String viosPlatformId, String processPoint);
	
	public List<MCViosMasterProcessDto> findAllByProcessNo(String viosPlatformId, String processNo);
	
	public MCViosMasterProcess findBy(String viosPlatformId, String processNo, String processPoint);
	
	public int getCountByViosPlatformId(String viosPlatformId);
	
	public int getMappedCountBy(String viosPlatformId, String processNo);
	
	public void deleteByProcessPointId(String processNo, String viosPlatform);

}
