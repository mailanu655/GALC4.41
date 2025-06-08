package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.pdda.ChangeFormUnit;
import com.honda.galc.entity.pdda.ChangeFormUnitId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.vios.dto.MCViosMasterProcessDto;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface ChangeFormUnitDao extends IDaoService<ChangeFormUnit, ChangeFormUnitId> {
	
    public int getUnmappedChangeFormProcessCount();

	public List<ChangeFormUnit> getAllUnmappedChangeFormProcesses();
	
	public List<ChangeFormUnit> getAllUnmappedChangeFormProcesses(String inClause);
	
    public int getUnmappedChangeFormUnitCount();
	
	public List<ChangeFormUnit> getAllUnmappedChangeFormUnits();
	
	public List<ChangeFormUnit> getAllUnmappedChangeFormUnits(String inClause);
	
	public List<ChangeFormUnit> findAllChangeFormUnitIdForRev(long revId);
	
	public int getUnmappedChangeFormUnitCountForRev(int revId);
	
	public int getUnmappedChangeFormProcessCountForRev(long revid);
	
	public ChangeFormUnit findMaintenanceIdForReportType(String unitNo, String reportType, int platformId );
	
	public List<ChangeFormUnit> findAllForChangeForm(int ChangeFormId);
	public List<ChangeFormUnit> findAllForChangeForm(int ChangeFormId, String asmProcNo);
	public List<ChangeFormUnit> findAllForChangeForm(int ChangeFormId,
			String asmProcNo, String unitNo);
	
	public List<MCViosMasterProcessDto> findAllProcessesByRevId(long revId, boolean isMapped);
	
	public List<String> findAllUnmappedPddaProcessBy(long revId);
	
	public List<String> findAllProcessByPlatform(MCViosMasterPlatform platform);
	
	public List<MCViosMasterProcessDto> findAllUnmappedProcessByPlatform(MCViosMasterPlatform platform);
	
	public List<String> findAllUnitNoByPlatform(MCViosMasterPlatform platform);
}
