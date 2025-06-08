package com.honda.galc.client.teamleader.datamigration;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.client.teamleader.qi.model.QiModel;
import com.honda.galc.dao.qi.QiMappingCombinationDao;
import com.honda.galc.dao.qi.QiOldCombinationDao;
import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.dto.qi.DataMigrationDto;
import com.honda.galc.dto.qi.QiDefectCombinationResultDto;
import com.honda.galc.entity.qi.QiMappingCombination;
import com.honda.galc.entity.qi.QiOldCombination;
/**
 * 
 * <h3>DataMigrationAssociationMaintenanceModel Class description</h3>
 * <p>DataMigrationAssociationMaintenanceModel is used to populate data and create or delete defect combination association 
 * between NAQ Defect description and NGLC/Old GALC defect combination
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
 *        MAY 14, 2017
 * 
 */
public class DataMigrationAssociationMaintenanceModel extends QiModel {

	/**
	 * This method is used to filter NAQ DefectCombinations
	 * @param filterString
	 * @return
	 */
	public List<DataMigrationDto> findAllNaqDefectCombinationByFilter(String filterString) {
		return getDao(QiOldCombinationDao.class).findAllNaqDefectCombinationByFilter(filterString);
	}

	
	/**
	 * This method is used to filter NGLC DefectCombinations
	 * @param filterString
	 * @return
	 */
	public List<DataMigrationDto> findAllOldCombinationByFilter(String filterString) {
		return getDao(QiOldCombinationDao.class).findAllOldCombinationByFilter(filterString);
	}

		
	/**
	 * This method is used to filter Associated DefectCombination result
	 * @param id
	 * @return
	 */
	public List<QiMappingCombination> findAllAssociatedDefectCombinationResultById(Integer id) {
		return getDao(QiMappingCombinationDao.class).findAllAssociatedDefectCombinationResultById(id);
	}

	/**
	 * This method is used to filter Associated DefectCombination result
	 * @param filter
	 * @return
	 */
	public List<QiMappingCombination> findAllAssociatedDefectCombinationResultByFilter(Integer id,String filter) {
		return getDao(QiMappingCombinationDao.class).findAllAssociatedDefectCombinationResultByFilter(id,filter);
	}
	
	/**
	 * This method is used to to get all Associated DefectCombinations
	 * @return
	 */
	public List<QiMappingCombination> findAllAssociatedDefectCombinationResult() {
		return getDao(QiMappingCombinationDao.class).findAll();
	}
	
	
	/**
	 * This method is used to create Defect Combinations Association
	 * @param qiBomQicsPartMappingList
	 */
	public void createAllDefectCombinationsAssociation(List<QiMappingCombination> qiBomQicsPartMappingList) {
		getDao(QiMappingCombinationDao.class).saveAll(qiBomQicsPartMappingList);
	}

	/**
	 * This method is used to delete the Association
	 * @param associatedBomPartList
	 */
	public void deleteAllDefectCombinationsAssociation(List<QiMappingCombination> existingAssociatedDefectCombinationList) {
		getDao(QiMappingCombinationDao.class).removeAll(existingAssociatedDefectCombinationList);
	}


	public QiOldCombination findByCombination(String oldCombCode) {
		return getDao(QiOldCombinationDao.class).findByKey(oldCombCode);
	}


	public QiDefectCombinationResultDto findByRegionalDefectCombinationId(int regionalDefectCombinationId) {
		return getDao(QiPartDefectCombinationDao.class).findById(regionalDefectCombinationId);
	}

}
