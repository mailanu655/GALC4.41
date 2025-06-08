package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementCheckerId;
import com.honda.galc.service.IDaoService;

/**
 * <h3>MCViosMasterOperationMeasurementCheckerDao Class description</h3>
 * <p>
 * Interface for MCViosMasterOperationMeasurementCheckerDaoImpl
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
public interface MCViosMasterOperationMeasurementCheckerDao extends IDaoService<MCViosMasterOperationMeasurementChecker, MCViosMasterOperationMeasurementCheckerId> {

	public List<MCViosMasterOperationMeasurementChecker> findAllmeasurements(String viosPlatformId, String unitNo, int measSeq);

	public void deleteAndInsertAll(String viosPlatformId, String unitNo, int measurementSeqNum,
			List<MCViosMasterOperationMeasurementChecker> opMeasCheckerList);

	public List<MCViosMasterOperationMeasurementChecker> findAllBy(String viosPlatformId, String unitNo);

	public List<MCViosMasterOperationMeasurementChecker> findAllData(String viosPlatformId);

	public void saveEntity(MCViosMasterOperationMeasurementChecker mcViosmOpsMeasChecker);

	public List<MCViosMasterOperationMeasurementChecker> findAllBy(String viosPlatformId, String unitNo,
			int measurementSeqNum);

	public void deleteAllBy(String viosPlatformId, String unitNo, int measurementSeqNum);

}
