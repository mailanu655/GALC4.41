package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.IViosDao;

/**
 * <h3>MCViosMasterOperationMeasurementDao Class description</h3>
 * <p>
 * Interface for MCViosMasterOperationMeasurementDaoImpl
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
public interface MCViosMasterOperationMeasurementDao
		extends IDaoService<MCViosMasterOperationMeasurement, MCViosMasterOperationMeasurementId>,
		IViosDao<MCViosMasterOperationMeasurement> {

	public List<MCViosMasterOperationMeasurement> findAllFilteredMeas(String viosPlatformId, String filter);
	
	public void saveEntity(MCViosMasterOperationMeasurement mcMeansObj) ;
	
	public List<MCViosMasterOperationMeasurement> findAllBy(String viosPlatformId, String unitNo);
	
	public void uploadMeasurementByQty(MCViosMasterOperationMeasurement mcViosMasterOpMeas);
	
	public int getFirstMeasurementSequenceNumber(String viosPlatformId, String unitNo);

}
