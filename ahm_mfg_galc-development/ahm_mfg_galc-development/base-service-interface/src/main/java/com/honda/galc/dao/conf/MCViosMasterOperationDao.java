package com.honda.galc.dao.conf;

import java.util.List;
import java.util.Map;

import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.IViosDao;
/**
 * <h3>MCViosMasterOperationDao Class description</h3>
 * <p>
 * Interface for MCViosMasterOperationDaoImpl
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
 *        Oct 4, 2018
 */
public interface MCViosMasterOperationDao extends IDaoService<MCViosMasterOperation, MCViosMasterOperationId>, 
IViosDao<MCViosMasterOperation> {
	
	public List<MCViosMasterOperation> findAllFilteredOperations(String viosPlatformId, String filter);
	
	public void saveEntity(MCViosMasterOperation opsObject);
	
	public List<String> findAllUnitNoBy(String viosPlatformId);
	public Map<String, MCViosMasterOperation> findAllUnitbyCommonName(String viosPlatformId, int conrodCount, boolean isConrod);
	public List<MCViosMasterOperation> findAllUnitbyCommonName(String viosPlatformId, String commonName, int commonNameOpCount);
}
