package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.qi.QiExternalSystemDataDao;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.entity.qi.QiExternalSystemData;
import com.honda.galc.entity.qi.QiExternalSystemDataId;
import com.honda.galc.service.HeadlessNaqService;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>ExternalSystemErrorMaintModel Class description</h3>
 * <p>
 * ExternalSystemErrorMaintModel is used to trigger database queries to  load headless error data and perform reprocess and delete action.
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
 * @author LnTInfotech<br>
 * 
 */
public class ExternalSystemErrorMaintModel extends QiModel {
	
	/** This method finds all Plant by site name for which headless error data exists
	 * @return List<String>
	*/
	public List<String> findAllBySite(String siteName) {
		return getDao(DivisionDao.class).findAllPlantBySiteAndExternalSystemData(siteName);
	}
	
	/** This method finds all Product Type for which headless error data exists
	 * @return List<String>
	 */
	public List<String> findAllProductTypes() {
		return getDao(QiExternalSystemDataDao.class).findAllProductTypeByExternalSystemData();
	}
	
	/** This method finds all external System by product type for which headless error data exists
	 * @return List<String>
	 */
	public List<String> findAllExternalSystemList(String productType) {
		return getDao(QiExternalSystemDataDao.class).findAllExternalSystemList(productType);
	}
	
	/** This method finds all external System errors 
	 * @return List<String>
	 */
	public List<QiExternalSystemData> findAllExternalSystemData(String externalSystemName){
		return getDao(QiExternalSystemDataDao.class).findAllExternalSystemData(externalSystemName);
	}
	
	public List<QiExternalSystemData> findAllExternalSystemData(String siteName, String plantName, String productType, String externalSystemName) {
		return getDao(QiExternalSystemDataDao.class).findAllExternalSystemData(siteName, plantName, productType, externalSystemName);
	}
	
	/** This method is used to delete external system data
	 */
	public void deleteExternalSystemData(QiExternalSystemDataId id) {
		getDao(QiExternalSystemDataDao.class).removeByKey(id);
	}

	/** This method is used to reprocess external system data
	 */
	public int reprocessData(DefectMapDto data) {
		return ServiceFactory.getService(HeadlessNaqService.class).saveDefectData(data);
	}
}
