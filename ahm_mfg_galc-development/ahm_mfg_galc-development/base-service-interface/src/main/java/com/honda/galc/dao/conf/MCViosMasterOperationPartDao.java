package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.IViosDao;

/**
 * <h3>MCViosMasterOperationPartDao Class description</h3>
 * <p>
 * Interface for MCViosMasterOperationPartDaoImpl
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
 *        Nov 20, 2018
 */
public interface MCViosMasterOperationPartDao extends IDaoService<MCViosMasterOperationPart, 
MCViosMasterOperationPartId>, IViosDao<MCViosMasterOperationPart> {
	
	public List<MCViosMasterOperationPart> findAllFilteredPart(String viosPlatformId, String filter);
	
	public List<MCViosMasterOperationPart> findAllBy(String viosPlatformId, String unitNo);
	
	public void saveEntity(MCViosMasterOperationPart mcMasterPart);
	
}
