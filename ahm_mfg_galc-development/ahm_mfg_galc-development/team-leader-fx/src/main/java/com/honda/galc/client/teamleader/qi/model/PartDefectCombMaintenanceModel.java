package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.qi.QiDefectDao;
import com.honda.galc.dao.qi.QiEntryScreenDefectCombinationDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.dao.qi.QiPartLocationCombinationDao;
import com.honda.galc.dto.qi.QiPartDefectCombinationDto;
import com.honda.galc.dto.qi.QiPartLocationCombinationDto;
import com.honda.galc.entity.qi.QiDefect;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiPartLocationCombination;

/**
 * 
 * <h3>PartDefectCombMaintenanceModel Class description</h3>
 * <p> PartDefectCombMaintenanceModel description </p>
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
 * Aug 26, 2016
 *
 *
 */
public class PartDefectCombMaintenanceModel extends QiModel {

	public PartDefectCombMaintenanceModel() {
		super();
	}
	/**
	 * Find Part Location Combination by Key.
	 * @param key
	 * @return
	 */
	public QiPartLocationCombination findPartLocCombByKey(Integer key) {
		return getDao(QiPartLocationCombinationDao.class).findByKey(key);
	}
	/**
	 * Find Part Defect Combination by Key.
	 * @param key
	 * @return
	 */
	public QiPartDefectCombination findPartDefectCombByKey(Integer key) {
		return getDao(QiPartDefectCombinationDao.class).findByKey(key);
	}
	/**
	 * Find Defect by Key.
	 * @param key
	 * @return
	 */
	public QiDefect findDefectByKey(String key) {
		return getDao(QiDefectDao.class).findByKey(key);
	}
	
	/**
	 * Find filtered Part Location Combinations 
	 */
	public List<QiPartLocationCombination> getPartLocCombByFilter(String partLocCombFilter, List<Short> statusList) {
		return getDao(QiPartLocationCombinationDao.class).findFilteredPartLocComb(partLocCombFilter, getProductKind(), statusList);
	}
	
	/**
	 * Find filtered Part Defect Combinations 
	 */
	public List<QiPartDefectCombinationDto> findAllPartDefectCombByFilter(List<QiPartLocationCombinationDto> selectedParts, short status, String searchText, int whichFilter) {
		List<Integer> partLocationIdList = new ArrayList<Integer>();
		for (QiPartLocationCombinationDto selectedPart : selectedParts) {
			partLocationIdList.add(selectedPart.getPartLocationId());
		}
		return getDao(QiPartDefectCombinationDao.class).findPartDefectByPartLocationIdList(partLocationIdList, status, getProductKind(), searchText, whichFilter);
	}
	/**
	 * This method is used to create Part Defect Combination.
	 * @param qiPartDefectCombinationList
	 * @return
	 */
	public List<QiPartDefectCombination> createPartDefectComb(List<QiPartDefectCombination> qiPartDefectCombinationList){
		return getDao(QiPartDefectCombinationDao.class).saveAll(qiPartDefectCombinationList);
	}
	/**
	 * This method is used to update Part Defect Combination.
	 * @param qiPartDefectCombination
	 * @return
	 */
	public QiPartDefectCombination updatePartDefectComb(QiPartDefectCombination qiPartDefectCombination){
		return (QiPartDefectCombination) getDao(QiPartDefectCombinationDao.class).save(qiPartDefectCombination);
	}
	/**
	 * Filter active primary defects on the basis of Defect Name, Defect Abbr, Description and Defect Category
	 * @param filterValue
	 */ 
	public List<QiDefect> getActivePrimaryDefectByFilter(String filterValue) {
		return getDao(QiDefectDao.class).findActivePrimaryDefectByFilter(filterValue, getProductKind());
	}
	/**
	 * Filter active secondary defects on the basis of Defect Name, Defect Abbr, Description and Defect Category
	 * @param filterValue
	 */ 

	public List<QiDefect> getActiveSecondaryDefectByFilter(String filterValue) {
		return getDao(QiDefectDao.class).findActiveSecondaryDefectByFilter(filterValue, getProductKind());
	}
	/**
	 * This method is used to Activate or Inactivate Part Defect Combination
	 */
	public void updatePartDefectCombStatus(Integer partDefectId, short active)
	{
		getDao(QiPartDefectCombinationDao.class).updatePartDefectCombStatus(partDefectId, active, getUserId());
	}
	/**
	 * Returns true if same Part Defect Combination already exist.
	 * @param comb
	 * @return
	 */
	public boolean checkPartDefectCombination(QiPartDefectCombination comb) 
	{
		return getDao(QiPartDefectCombinationDao.class).checkPartDefectCombination(comb);
	}
	
	/**
	 * This method is used to derive combination of primary key values for audit
	 * purpose.
	 * 
	 * @param regionalDefectCombinationId
	 * @return auditPrimaryKeyValue
	 */
	public String getAuditPrimaryKeyValue(int regionalDefectCombinationId) {
		return getDao(QiPartDefectCombinationDao.class).fetchAuditPrimaryKeyValue(regionalDefectCombinationId);
	}


	public List<QiEntryScreenDefectCombination> findAllEntryScreensByPartDefectId(List<Integer> partDefectIdList) {
		return getDao(QiEntryScreenDefectCombinationDao.class).findAllEntryScreensByPartDefectId(partDefectIdList);
	}

	public List<QiLocalDefectCombination> findAllLocalAttributesByPartDefectId(List<Integer> partDefectIdList) {
		return getDao(QiLocalDefectCombinationDao.class).findAllLocalAttributesByPartDefectId(partDefectIdList);
	}
	public List<QiPartDefectCombination> findAllRegionalAttributesByPartDefectId(List<Integer> partDefectIdList) {
		return getDao(QiPartDefectCombinationDao.class).findAllRegionalAttributesByPartDefectId(partDefectIdList);
	}
	
	/**
	 * This method finds external mapping data based on local comb id
	 * @param localCombinationId
	 * @return List<QiExternalSystemDefectMap>ExternalSystemDefectMap
	 */
	public List<QiExternalSystemDefectMap> findAllByLocalCombinationId(List<Integer> localCombinationIdList) {
		String localCombIdListValue=StringUtils.join(localCombinationIdList, ',');
		return getDao(QiExternalSystemDefectMapDao.class).findAllByLocalCombinationId(localCombIdListValue);
	}
	
	public List<QiPartLocationCombinationDto> findActivePartLocCombByFilter(String filterValue, int whichFilter){
		return getDao(QiPartLocationCombinationDao.class).findFullPartNameByWhichFilter(filterValue, (short)1, whichFilter, getProductKind());
	}
}
