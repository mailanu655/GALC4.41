package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiExternalSystemData;
import com.honda.galc.entity.qi.QiExternalSystemDataId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiExternalSystemDataDao Class description</h3>
 * <p> QiExternalSystemDataDao used to fire queries to load, reprocess or delete data </p>
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
 * @author L&T Infotech<br>
 * May 11, 2017
 */
public interface QiExternalSystemDataDao extends IDaoService<QiExternalSystemData, QiExternalSystemDataId> {
	
	public List<String> findAllExternalSystemList(String productType);
	public List<QiExternalSystemData> findAllExternalSystemData(String externalSystemName);
	public List<QiExternalSystemData> findAllExternalSystemData(String siteName, String plantName, String productType, String externalSystemName);
	public void removeSelectedExternalSystemErrorData(QiExternalSystemData selectedData);
	public QiExternalSystemData findByExternalSystemData(String partCode,String defectCode, String externalSystemName,String productId ,String processPointId);
	public List<String> findAllProductTypeByExternalSystemData();
	public boolean isExternalSystemNameUsed(String externalSystemName);

}
