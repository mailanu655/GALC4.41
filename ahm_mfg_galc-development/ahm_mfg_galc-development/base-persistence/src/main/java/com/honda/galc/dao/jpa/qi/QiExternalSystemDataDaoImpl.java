package com.honda.galc.dao.jpa.qi;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiExternalSystemDataDao;
import com.honda.galc.entity.qi.QiExternalSystemData;
import com.honda.galc.entity.qi.QiExternalSystemDataId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiExternalSystemDataDaoImpl Class description</h3>
 * <p> QiExternalSystemDataDaoImpl used to fire queries to load, reprocess or delete data  </p>
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
public class QiExternalSystemDataDaoImpl extends BaseDaoImpl<QiExternalSystemData, QiExternalSystemDataId> implements QiExternalSystemDataDao {
	
	private final static String FIND_EXTERNAL_SYSTEM_NAME  = "SELECT DISTINCT p.EXTERNAL_SYSTEM_NAME from GALADM.QI_EXTERNAL_SYSTEM_DATA_TBX p JOIN galadm.PRODUCT_TYPE_TBX e on p.PRODUCT_TYPE=e.PRODUCT_TYPE where e.PRODUCT_TYPE=?1";
	
	private final static String FIND_PRODUCT_TYPE_BY_EXTERNAL_SYSTEM_DATA  = "SELECT DISTINCT e.PRODUCT_TYPE from galadm.QI_EXTERNAL_SYSTEM_DATA_TBX e JOIN GALADM.PRODUCT_TYPE_TBX p on p.PRODUCT_TYPE=e.PRODUCT_TYPE";
	
	private final static String FIND_ALL_BY_SITE_PLANT_PT_SYSTEM = "select e from QiExternalSystemData e, ProcessPoint pp where e.entrySite = :entrySite and pp.processPointId = e.id.processPointId and pp.plantName = :plantName and e.productType = :productType and e.id.externalSystemName = :externalSystemName";
	
	/** This method finds all external system data
	 * @return List<String>
	 */
	public List<QiExternalSystemData> findAllExternalSystemData(String externalSystemName) {
		return findAll(Parameters.with("id.externalSystemName", externalSystemName));
	}

	public List<QiExternalSystemData> findAllExternalSystemData(String siteName, String plantName, String productType, String externalSystemName) {
		Parameters params = new Parameters();
		params.put("entrySite", siteName);
		params.put("plantName", plantName);
		params.put("productType", productType);
		params.put("externalSystemName", externalSystemName);
		return findAllByQuery(FIND_ALL_BY_SITE_PLANT_PT_SYSTEM, params);
	}
	
	/** This method finds all external system names for which headless error data exists
	 * @return List<String>
	 */
	public List<String> findAllExternalSystemList(String productType) {
		Parameters params = Parameters.with("1", productType);
		return findAllByNativeQuery(FIND_EXTERNAL_SYSTEM_NAME, params, String.class);
	}
	
	/** This method is used to delete record
	 * @return List<String>
	 */
	@Transactional
	public void removeSelectedExternalSystemErrorData(QiExternalSystemData selectedData) {
		if(selectedData == null)  return;
		QiExternalSystemDataId id = selectedData.getId();
		Parameters params = Parameters.with("id.externalSystemName", id.getExternalSystemName())
				.put("id.externalPartCode", id.getExternalPartCode())
				.put("id.externalDefectCode", id.getExternalDefectCode())
				.put("id.productId", id.getProductId())
				.put("id.processPointId", id.getProcessPointId())
				.put("id.entryTimestamp", id.getEntryTimestamp());
		delete(params);

	}
	
	/**
	 * This method finds external system defect data based on external part code ,external defect code
	 * and external system name
	 * @param partCode
	 * @param defectCode
	 * @param externalSystemName
	 * @param productId
	 * @param processPointId
	 * @return QiExternalSystemData
	 */
	public QiExternalSystemData findByExternalSystemData(String partCode,String defectCode, String externalSystemName,String productId ,String processPointId) {
		Parameters params = Parameters.with("id.externalPartCode", partCode).put("id.externalDefectCode",defectCode).put("id.externalSystemName", externalSystemName).
				put("id.productId",productId).put("id.processPointId", processPointId);
		return findFirst( params);
	}
	
	/** This method finds all Product Type for which headless error data exists
	 * @return List<String>
	 */
	public List<String> findAllProductTypeByExternalSystemData() {
		return findResultListByNativeQuery(FIND_PRODUCT_TYPE_BY_EXTERNAL_SYSTEM_DATA,null);
	}
	
	@Override
	public boolean isExternalSystemNameUsed(String externalSystemName) {
		Parameters params = Parameters.with("id.externalSystemName", externalSystemName);
		return findFirst(params) != null? true : false;
	}
}
