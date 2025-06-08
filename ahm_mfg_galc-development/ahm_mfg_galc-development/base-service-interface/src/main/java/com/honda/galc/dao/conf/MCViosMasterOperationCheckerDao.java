package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationCheckerId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.IViosDao;
/**
 * <h3>MCViosMasterOperationCheckerDao Class description</h3>
 * <p>
 * Interface for MCViosMasterOperationCheckerDaoImpl
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
public interface MCViosMasterOperationCheckerDao extends 
IDaoService<MCViosMasterOperationChecker, MCViosMasterOperationCheckerId>, IViosDao<MCViosMasterOperationChecker> {
	
	public void deleteAndInsertAll(String viosPlatformId, String unitNo, List<MCViosMasterOperationChecker> opCheckerList);
	
	public void deleteAllBy(String viosPlatformId, String unitNo);
	
	public List<MCViosMasterOperationChecker> findAllData(String viosPlatformId);
	
	public void saveEntity(MCViosMasterOperationChecker masterOpChecker);
	
	public List<MCViosMasterOperationChecker> findAllBy(String viosPlatformId, String unitNo);
	
}
