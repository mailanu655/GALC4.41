package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.qi.QiIqsDao;
import com.honda.galc.dao.qi.QiIqsVersionDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.dao.qi.QiPartLocationCombinationDao;
import com.honda.galc.dao.qi.QiThemeDao;
import com.honda.galc.dto.qi.QiPartLocationCombinationDto;
import com.honda.galc.dto.qi.QiRegionalAttributeDto;
import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.entity.qi.QiIqsVersion;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiTheme;

/**
 * 
 * <h3>PdcRegionalAttributeMaintModel Class description</h3>
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

public class PdcRegionalAttributeMaintModel extends QiModel {

	public PdcRegionalAttributeMaintModel() {
		super();
	}
	
	/**
	 * Find all pdc regional attribute.
	 *
	 * @param filterValue the filter value
	 * @param active the active
	 * @return the list
	 */
	public List<QiRegionalAttributeDto> findAllPdcRegionalAttributes(List<QiPartLocationCombinationDto> selectedParts, short assigned, String searchText, int whichFilter){
		List<Integer> partLocationIdList = new ArrayList<Integer>();
		for (QiPartLocationCombinationDto selectedPart : selectedParts) {
			partLocationIdList.add(selectedPart.getPartLocationId());
		}
		return getDao(QiPartDefectCombinationDao.class).findRegionalAttributeByPartLocationIdList(partLocationIdList, assigned, getProductKind(), searchText, whichFilter);
	}
	
	/**
	 * Assign and update attributes.
	 *
	 * @param defectCombinationList the defect combination list
	 */
	public void assignAndUpdateAttributes(List<QiPartDefectCombination> defectCombinationList) {
		getDao(QiPartDefectCombinationDao.class).updateAll(defectCombinationList);
	}
	
	/**
	 * Gets the all themes.
	 *
	 * @return the all themes
	 */
	public List<QiTheme> getAllThemes(){
		return getDao(QiThemeDao.class).findAllActiveThemes();
	}
	
	/**
	 * Gets the all iqs version.
	 *
	 * @return the all iqs version
	 */
	public List<QiIqsVersion> getAllIqsVersion() {
		return getDao(QiIqsVersionDao.class).findAllIqsVersion();
	}
	
	/**
	 * Find all iqs category by version.
	 *
	 * @param iqsVersion the iqs version
	 * @return the list
	 */
	public List<String> findAllCategoriesByVersion(String iqsVersion) {
		return getDao(QiIqsDao.class).findAllCategoriesByVersion(iqsVersion);
	}
	
	/**
	 * Find iqs by category.
	 *
	 * @param iqsCategory the iqs category
	 * @return the qi iqs
	 */
	public QiIqs findIqs(QiIqs iqs) {
		return getDao(QiIqsDao.class).find(iqs);
	}
	
	/**
	 * Find iqs question no by category.
	 *
	 * @param iqsCategory the iqs category
	 * @return the qi iqs
	 */
	public List<QiIqs> findAllByVersionAndCategory(String iqsVersion, String iqsCategory) {  
		return getDao(QiIqsDao.class).findAllByVersionAndCategory(iqsVersion, iqsCategory);
	}
	

	/**
	 * This method is used to derive combination of primary key values for audit
	 * purpose.
	 * 
	 * @param regionalDefectCombinationId
	 * @return auditPrimaryKeyValue
	 */
	public String getAuditPrimaryKeyValue(int partLocationId) {
		return getDao(QiPartDefectCombinationDao.class).fetchAuditPrimaryKeyValue(partLocationId);
	}
	
	/**
	 * This method finds all local attribute assigned for a regionalDefectCombinationId and update its reportable value
	 * @param defectCombinationList
	 */
	public void updateLocalAttribute(List<QiPartDefectCombination> defectCombinationList) {
		List<QiLocalDefectCombination> localDefectCombinationsList=new ArrayList<QiLocalDefectCombination>();
		for(QiPartDefectCombination qiPartDefectCombination:defectCombinationList){
			List<QiLocalDefectCombination> associatedLocalData=getDao(QiLocalDefectCombinationDao.class).findAllByRegionalDefectCombinationId(qiPartDefectCombination.getRegionalDefectCombinationId());
			for(QiLocalDefectCombination qiLocalDefectCombination: associatedLocalData){
				qiLocalDefectCombination.setReportable(qiPartDefectCombination.getReportable());
				qiLocalDefectCombination.setUpdateUser(getUserId());
				localDefectCombinationsList.add(qiLocalDefectCombination);
			}
		}
		if(localDefectCombinationsList.size()>0)
			updateAllLocalAttributes(localDefectCombinationsList);
	}
	
	/**
	 * This method updates all local attribute 
	 * @param qiLocalDefectCombinationList 
	 */
	private void updateAllLocalAttributes(List<QiLocalDefectCombination> qiLocalDefectCombinationList) {
		getDao(QiLocalDefectCombinationDao.class).updateAll(qiLocalDefectCombinationList);
	}
	

	/**
	 * This method local attribute data
	 * @param regionalCombinationId
	 * @return QiPartDefectCombination
	 */
	public QiPartDefectCombination findByKey(Integer regionalCombinationId){
		return getDao(QiPartDefectCombinationDao.class).findByKey(regionalCombinationId);
	}
	
	public List<QiPartLocationCombinationDto> findActivePartLocCombByFilter(String filterValue, int searchByFilter){
		return getDao(QiPartLocationCombinationDao.class).findFullPartNameByWhichFilter(filterValue, (short)1, searchByFilter, getProductKind());
	}
	
	public List<QiPartLocationCombinationDto> findUnassignedPartLocComb(){
		return getDao(QiPartLocationCombinationDao.class).findUnassignedFullPartName((short)1, getProductKind());
	}
}
