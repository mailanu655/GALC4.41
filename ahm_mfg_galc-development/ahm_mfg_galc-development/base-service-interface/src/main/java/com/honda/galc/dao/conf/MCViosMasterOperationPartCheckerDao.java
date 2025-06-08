package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationPartCheckerId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.IViosDao;

/**
 * <h3>MCViosMasterOperationPartCheckerDao Class description</h3>
 * <p>
 * Interface for MCViosMasterOperationPartCheckerDaoImpl
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
 *         Nov 20, 2018
 */
public interface MCViosMasterOperationPartCheckerDao
		extends IDaoService<MCViosMasterOperationPartChecker, MCViosMasterOperationPartCheckerId>,
		IViosDao<MCViosMasterOperationPartChecker> {

	public void deleteAndInsertAll(String viosPlatformId, String unitNo, String partNo, List<MCViosMasterOperationPartChecker> opPartCheckerList);

	public void deleteAllBy(String viosPlatformId, String unitNo, String partNo);
	
	public void saveEntity(MCViosMasterOperationPartChecker mcPartChecker);
	
	public List<MCViosMasterOperationPartChecker> findAllBy(String viosPlatformId, String unitNo, String partNo);
	
	public List<MCViosMasterOperationPartChecker> findAllBy(String viosPlatformId, String unitNo);
}
