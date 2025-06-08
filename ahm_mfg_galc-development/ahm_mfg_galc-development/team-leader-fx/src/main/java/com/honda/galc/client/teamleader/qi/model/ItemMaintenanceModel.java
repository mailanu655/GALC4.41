package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.qi.QiBomQicsPartMappingDao;
import com.honda.galc.dao.qi.QiDefectCategoryDao;
import com.honda.galc.dao.qi.QiDefectDao;
import com.honda.galc.dao.qi.QiEntryScreenDefectCombinationDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dao.qi.QiImageSectionDao;
import com.honda.galc.dao.qi.QiInspectionLocationDao;
import com.honda.galc.dao.qi.QiInspectionPartDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.dao.qi.QiPartLocationCombinationDao;
import com.honda.galc.entity.qi.QiBomQicsPartMapping;
import com.honda.galc.entity.qi.QiDefect;
import com.honda.galc.entity.qi.QiDefectCategory;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiImageSection;
import com.honda.galc.entity.qi.QiInspectionLocation;
import com.honda.galc.entity.qi.QiInspectionPart;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiPartLocationCombination;

/**
 * 
 * <h3>QIMaintenanceController Class description</h3>
 * <p> QIMaintenanceController description </p>
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
 * April 20, 2016
 *
 *
 */
public class ItemMaintenanceModel extends QiModel {

	private QiDefect qiDefect;
	private QiInspectionLocation qiLocation;

	private String buttonValue;

	private String contextClick;

	private QiPartLocationCombination selectedComb;

	private QiInspectionPart qiInspectionPart;
	public ItemMaintenanceModel() {
		super();
	}

	/**
	 * This method is used to fetch the Parts based on the filtered data .
	 * @param descriptionId
	 * @param partLocComb
	 */
	public List<QiInspectionPart> findPartsByFilter(String queryString) {
		return getDao(QiInspectionPartDao.class).findPartsByFilter(queryString, getProductKind());
	}

	public List<QiInspectionPart> findPartsByFilter(String queryString,String status) {
		return getDao(QiInspectionPartDao.class).findPartsByFilter(queryString,status, getProductKind());
	}
	/**
	 * This method is used to delete the Part
	 * @param part
	 */
	public void deletePart(QiInspectionPart part) {
		getDao(QiInspectionPartDao.class).remove(part);
	}

	/**
	 * This method is used to create the Part and checked the duplicate Part
	 * @param part
	 * @return
	 */
	public QiInspectionPart createPart(QiInspectionPart part) {
		part.setAppCreateTimestamp(new Date());
		return((QiInspectionPart)getDao(QiInspectionPartDao.class).insert(part));
	}
	/**
	 * It is used to fetch all locations based on filter
	 * @param filterValue
	 */
	public List<QiInspectionLocation> getLocationByFilter(String filterValue) {
		return getDao(QiInspectionLocationDao.class).findLocationByFilter(filterValue, getProductKind());
	}

	/**
	 * Filter data on the basis of Defect Name, Defect Abbr, Description and Defect Category
	 * @param filterValue
	 */ 

	public List<QiDefect> getDefectByFilter(String filterValue, List<Short> statusList) {
		return getDao(QiDefectDao.class).findDefectByFilter(filterValue, getProductKind(), statusList);
	}

	/**
	 * This method gets called when user clicks on Create Defect
	 * @param defect
	 */ 
	public QiDefect createDefect(QiDefect defect) {
		defect.setAppCreateTimestamp(new Date());
		return (QiDefect) getDao(QiDefectDao.class).insert(defect);
	}

	/**
	 * This method gets called when user clicks on Update Defect
	 * @param defect
	 */
	public void updateDefect(QiDefect defect, String oldDefectName) {
		defect.setAppUpdateTimestamp(new Date());
		getDao(QiDefectDao.class).updateDefect(defect,oldDefectName);
	}

	/**
	 * Activate or Inactivate Part Location Combination
	 */
	public void updatePartLocCombStatus(Integer partLocationId, short active)
	{
		getDao(QiPartLocationCombinationDao.class).updatePartLocCombStatus(partLocationId, active, getUserId());
	}

	/**
	 * Check if Part Location Combinations exists of not 
	 */
	public boolean checkPartLocComb(QiPartLocationCombination partLocComb) {
		return getDao(QiPartLocationCombinationDao.class).checkPartLocComb(partLocComb.getInspectionPartName(), partLocComb.getInspectionPartLocationName(), 
				partLocComb.getInspectionPartLocation2Name(), partLocComb.getInspectionPart2Name(), partLocComb.getInspectionPart2LocationName(), 
				partLocComb.getInspectionPart2Location2Name(), partLocComb.getInspectionPart3Name(), getProductKind(), partLocComb.getPartLocationId());
	}

	/**
	 * Find filtered Part Location Combinations 
	 */
	public List<QiPartLocationCombination> getPartLocCombByFilter(String partLocCombFilter, List<Short> statusList) {
		return getDao(QiPartLocationCombinationDao.class).findFilteredPartLocComb(partLocCombFilter, getProductKind(), statusList);
	}
	/**
	 * Create Part Location Combinations 
	 */
	public QiPartLocationCombination createPartLocComb(QiPartLocationCombination comb) {
		return (QiPartLocationCombination) getDao(QiPartLocationCombinationDao.class).insert(comb);
	}
	/**
	 * Checks if Parts and Locations in Part Location Combination are active 
	 */
	public boolean checkPartLocationCombinationForUpdate(QiPartLocationCombination partLocComb)
	{
		QiInspectionPart part1 = getDao(QiInspectionPartDao.class).findByKey(StringUtils.trimToEmpty(partLocComb.getInspectionPartName()));
		QiInspectionPart part2 = getDao(QiInspectionPartDao.class).findByKey(StringUtils.trimToEmpty(partLocComb.getInspectionPart2Name()));
		QiInspectionPart part3 = getDao(QiInspectionPartDao.class).findByKey(StringUtils.trimToEmpty(partLocComb.getInspectionPart3Name()));
		QiInspectionLocation loc1 = getDao(QiInspectionLocationDao.class).findByKey(StringUtils.trimToEmpty(partLocComb.getInspectionPartLocationName()));
		QiInspectionLocation loc2 = getDao(QiInspectionLocationDao.class).findByKey(StringUtils.trimToEmpty(partLocComb.getInspectionPartLocation2Name()));
		QiInspectionLocation loc3 = getDao(QiInspectionLocationDao.class).findByKey(StringUtils.trimToEmpty(partLocComb.getInspectionPart2LocationName()));
		QiInspectionLocation loc4 = getDao(QiInspectionLocationDao.class).findByKey(StringUtils.trimToEmpty(partLocComb.getInspectionPart2Location2Name()));
		
		return ((partLocComb.getInspectionPartName().equals(StringUtils.EMPTY) ? true : (part1==null ? false : part1.isActive()))
				&& (partLocComb.getInspectionPartLocationName().equals(StringUtils.EMPTY) ? true : (loc1==null ? false : loc1.isActive()))
				&& (partLocComb.getInspectionPartLocation2Name().equals(StringUtils.EMPTY) ? true : (loc2==null ? false : loc2.isActive()))
				&& (partLocComb.getInspectionPart2Name().equals(StringUtils.EMPTY) ? true : (part2==null ? false : part2.isActive()))
				&& (partLocComb.getInspectionPart2LocationName().equals(StringUtils.EMPTY) ? true : (loc3==null ? false : loc3.isActive()))
				&& (partLocComb.getInspectionPart2Location2Name().equals(StringUtils.EMPTY) ? true :(loc4==null ? false : loc4.isActive()))
				&& (partLocComb.getInspectionPart3Name().equals(StringUtils.EMPTY) ? true : (part3==null ? false : part3.isActive())));
	}
	public QiInspectionPart findPartByKey(String inspectionPartName)
	{
		return getDao(QiInspectionPartDao.class).findByKey(inspectionPartName);
	}
	public QiInspectionLocation findLocationByKey(String inspectionLocationName)
	{
		return getDao(QiInspectionLocationDao.class).findByKey(inspectionLocationName);
	}
	public QiPartLocationCombination updatePartLocComb(QiPartLocationCombination comb) {
		return (QiPartLocationCombination) getDao(QiPartLocationCombinationDao.class).save(comb);
	}

	public List<QiInspectionPart> getActivePrimaryPartNames() {
		return getDao(QiInspectionPartDao.class).findActivePrimaryInspectionPartsByProductKind(getProductKind());
	}

	public List<QiInspectionPart> getActiveSecondaryPartNames() {
		return getDao(QiInspectionPartDao.class).findActiveSecondaryInspectionPartsByProductKind(getProductKind());
	}

	public List<QiInspectionPart> getFilteredActivePrimaryPartNames(String filterData) {
		return getDao(QiInspectionPartDao.class).findFilteredActivePrimaryInspectionParts(filterData, getProductKind());
	}

	public List<QiInspectionPart> getFilteredActiveSecondaryPartNames(String filterData) {
		return getDao(QiInspectionPartDao.class).findFilteredActiveSecondaryInspectionParts(filterData, getProductKind());
	}

	public List<QiInspectionPart> getActivePartNames() {
		return getDao(QiInspectionPartDao.class).findActiveInspectionPartsByProductKind(getProductKind());
	}

	public List<QiInspectionPart> getFilteredActivePartNames(String filterData) {
		return getDao(QiInspectionPartDao.class).findFilteredActiveInspectionParts(filterData, getProductKind());
	}
	/**
	 * Find All Active location Names 
	 */
	public List<QiInspectionLocation> getActiveLocationNames() {
		return getDao(QiInspectionLocationDao.class).findActiveInspectionLocationsByProductKind(getProductKind());
	}

	/**
	 * Find All filtered active location name 
	 * @param filterData
	 */
	public List<QiInspectionLocation> getFilteredActiveLocationNames(String filterData) {
		return getDao(QiInspectionLocationDao.class).findFilteredActiveInspectionLocations(filterData, getProductKind());
	}

	/**
	 * Find All primary and active location name
	 */
	public List<QiInspectionLocation> getActivePrimaryLocationNames() {
		return getDao(QiInspectionLocationDao.class).findActivePrimaryInspectionLocationsByProductKind(getProductKind());
	}
	/**
	 * Find All filtered primary and active location name
	 * @param filterData
	 */
	public List<QiInspectionLocation> getFilteredActivePrimaryLocationNames(String filterData) {
		return getDao(QiInspectionLocationDao.class).findFilteredActivePrimaryInspectionLocations(filterData, getProductKind());
	}

	/**
	 * This method gets called when user clicks on reactivate/inactivate menu item to change defect status
	 * @param name
	 * @param active
	 */
	public void updateDefectStatus(String name, short active)
	{
		getDao(QiDefectDao.class).updateDefectStatus(name, active, getUserId());
	}
	/**
	 * Update location status
	 * @param name
	 * @param active
	 */
	public void updateLocationStatus(String name, short active)
	{
		getDao(QiInspectionLocationDao.class).updateLocationStatus(name, active, getUserId());
	}

	/**
	 * This method gets called when user clicks on reactivate/inactivate menu item
	 */
	public void updatePartStatus(String name, short active)
	{
		getDao(QiInspectionPartDao.class).updatePartStatus(name, active , getUserId());
	}

	/**
	 * Create new Location
	 * @param location
	 */
	public QiInspectionLocation createLocation(QiInspectionLocation location) {
		location.setAppCreateTimestamp(new Date());
		return((QiInspectionLocation)getDao(QiInspectionLocationDao.class).insert(location));
	}
	/**
	 * Find Defect Category dropdown 
	 */
	public List<QiDefectCategory> populateDefectCategory() {
		return getDao(QiDefectCategoryDao.class).findAllDefectCategory();
	}
	/**
	 * Update Location userid
	 * @param location
	 */
	public void updateLocation(QiInspectionLocation location, String oldLocname) {
		getDao(QiInspectionLocationDao.class).updateLocation(location, oldLocname);
	}

	/**
	 * Update New Part  
	 */
	public QiInspectionPart updatePart(QiInspectionPart qiInspectionPart) {
		qiInspectionPart.setAppUpdateTimestamp(new Date());
		return (QiInspectionPart) getDao(QiInspectionPartDao.class).save(qiInspectionPart);
	}

	public List<QiPartLocationCombination>  checkPartInPartLocCombination(String partName)
	{
		return getDao(QiPartLocationCombinationDao.class).checkPartInPartLocCombination(partName, getProductKind());
	}
	public void inactivatePart(String partName)
	{
		getDao(QiInspectionPartDao.class).inactivatePart(partName,getUserId());
	}

	/**
	 * Update New Part Location 
	 * @param qiInspectionPart
	 */ 
	public QiPartLocationCombination updatePartLocationCombination(QiPartLocationCombination qiInspectionPart) {
		return (QiPartLocationCombination) getDao(QiPartLocationCombinationDao.class).save(qiInspectionPart);
	}
	/**
	 * Update partLocationCombination entry for associated locations
	 * @param locationName
	 */
	public List<QiPartLocationCombination> checkLocationInPartLocCombination(String locationName) {
		return getDao(QiPartLocationCombinationDao.class).checkLocationInPartLocCombination(locationName, getProductKind());
	}
	/**
	 * delete location entry 
	 * @param location
	 */ 
	public void deleteLocation(QiInspectionLocation location) {
		getDao(QiInspectionLocationDao.class).remove(location);
	}
	/**
	 * delete defect  
	 * @param defect
	 */ 
	public void deleteDefect(QiDefect defect) {
		getDao(QiDefectDao.class).remove(defect);
	}
	/**
	 * deactivates the location
	 * @param locationName
	 */
	public void inactivateLocation(String locationName)
	{
		getDao(QiInspectionLocationDao.class).inactivateLocation(locationName,getUserId());
	}
	public QiDefect getQiDefect() {
		return qiDefect;
	}
	public void setQiDefect(QiDefect qiDefect) {
		this.qiDefect = qiDefect;
	}
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	public String getContextClick() {
		return contextClick;
	}
	public void setContextClick(String contextClick) {
		this.contextClick = contextClick;
	}
	public QiPartLocationCombination getSelectedComb() {
		return selectedComb;
	}
	public void setSelectedComb(QiPartLocationCombination selectedComb) {
		this.selectedComb = selectedComb;
	}

	public QiInspectionPart getQiInspectionPart() {
		return qiInspectionPart;
	}
	public void setQiInspectionPart(QiInspectionPart qiInspectionPart) {
		this.qiInspectionPart = qiInspectionPart;
	}
	public QiInspectionLocation getQiLocation() {
		return qiLocation;
	}
	public void setQiLocation(QiInspectionLocation qiLocation) {
		this.qiLocation = qiLocation;
	}

	/**
	 * This method checks whether same Defect name exists in the database
	 * @param defect
	 */
	public boolean isDefectExists(String defect) {
		return getDao(QiDefectDao.class).findByKey(defect) != null;
	}
	/**
	 * This method checks whether same location name exists in the database
	 * @param location
	 */
	public boolean isLocationExists(String location) {
		return getDao(QiInspectionLocationDao.class).findByKey(location) != null;
	}
	/**
	 * To get Details For location
	 * @param inspectionLocName
	 */
	public QiInspectionLocation getDetailsForLocName(String inspectionLocName){
		return getDao(QiInspectionLocationDao.class).findByKey(inspectionLocName);

	}
	/**
	 * This Method is used to update the PartName
	 * @param location
	 */
	public void updatePart(QiInspectionPart inspectionPart, String oldPartName) {
		getDao(QiInspectionPartDao.class).updatePart(inspectionPart, oldPartName);
	}

	/**
	 * This method checks whether same Part name exists in the database
	 * @param partName
	 */
	public boolean isPartExists(String partName) {
		return getDao(QiInspectionPartDao.class).findByKey(partName) != null;
	}

	
	/**
	 * This method is used to list of get partLocationId based on partLocationId associated with PLC
	 * @param partLocationId
	 * @return
	 */
	public List<Integer> findPartLocationIdsInPartDefectCombination(int partLocationId){
		return getDao(QiPartDefectCombinationDao.class).findPartLocationIdsInPartDefectCombination(partLocationId,getProductKind());
	}
	/**
	 * This method is used to inactivate Part in PDC
	 * @param partLocationId
	 */
	public void inactivatePartinPartDefectCombination(int partLocationId){
		getDao(QiPartDefectCombinationDao.class).inactivatePartinPartDefectCombination(partLocationId,getProductKind(),getUserId());
	}
	/**
	 * This method is used to list of imageSection based on list of partLocationId associated with PLC
	 * @param partLocationIdList
	 * @return
	 */
	public List<QiImageSection> findPartLocationIdsInImageSection(List<Integer> partLocationIdList){
		return getDao(QiImageSectionDao.class).findPartLocationIdInImageSection(partLocationIdList);
	}
	/**
	 * This method is used to list of get partLocationId based on list of partLocationId associated with PLC
	 * @param partLocationIdList
	 * @return
	 */
	public List<QiPartDefectCombination> findPartLocationIdsInPartDefectCombination(List<Integer> partLocationIdList){
		return getDao(QiPartDefectCombinationDao.class).findPartLocationIdsInPartDefectCombination(partLocationIdList,getProductKind());
	}
	/**
	 * Find defect in  partDefectCombination 
	 * @param defectName
	 */
	public List<QiPartDefectCombination> findDefectInPartDefectCombination(String defectName) {
		return getDao(QiPartDefectCombinationDao.class).findDefectInPartDefectCombination(defectName, getProductKind());
	}

	/**
	 * Update New Part Defect Combination 
	 * @param qiPartDefectComb
	 */ 
	public QiPartDefectCombination updatePartDefectCombination(QiPartDefectCombination qiPartDefectComb) {
		return (QiPartDefectCombination) getDao(QiPartDefectCombinationDao.class).save(qiPartDefectComb);
	}
	
	/**
	 * Find Part Loc Comb in  partDefectCombination 
	 * @param PartLocId
	 */
	public List<QiPartDefectCombination> findPartLocCombInPartDefectCombination(int PartLocId) {
		return getDao(QiPartDefectCombinationDao.class).findPartLocCombInPartDefectCombination(PartLocId, getProductKind());
	}

	/**
	 * Update New Part Defect Combination List 
	 * @param qiPartDefectCombList
	 */ 
	public void updatePartDefectCombination(List<QiPartDefectCombination> qiPartDefectCombList) {
		getDao(QiPartDefectCombinationDao.class).updateAll(qiPartDefectCombList);
	}
	/**
	 * It is used to fetch all locations based on filter and status
	 * @param filterValue
	 */
	public List<QiInspectionLocation> getLocationByFilter(String filterValue,String status) {
		return getDao(QiInspectionLocationDao.class).findLocationByFilter(filterValue,status, getProductKind());
	}
	/**
	 * This method is used to list of imageSection based on list of partLocationId associated with PLC
	 * @param partLocationIdList
	 * @return
	 */
	public List<QiImageSection> findPartLocationIdInImageSection(List<Integer> partLocationIdList){
		return getDao(QiImageSectionDao.class).findPartLocationIdInImageSection(partLocationIdList);
	}
	/**
	 * This method is used to list of get partLocationId based on list of partLocationId associated with PLC
	 * @param partLocationIdList
	 * @return
	 */
	public List<QiPartDefectCombination> findPartLocationIdInPartDefectCombination(List<Integer> partLocationIdList){
		return getDao(QiPartDefectCombinationDao.class).findPartLocationIdsInPartDefectCombination(partLocationIdList,getProductKind());
	}
	/**
	 * This method is used to find the list of BomQicsPart based on PartName
	 * @param bomQicsPartMappingId
	 * @return
	 */
	public List<QiBomQicsPartMapping> findAllByPartName(String partName){
		return getDao(QiBomQicsPartMappingDao.class).findAllByPartName(partName);
	}
	/**
	 * This Method is used to update the PartName
	 * @param location
	 */
	public void updatePartInBomQicsPartMapping(QiInspectionPart inspectionPart, String oldPartName) {
		getDao(QiBomQicsPartMappingDao.class).updatePartInBomQicsPartMapping(inspectionPart, oldPartName,getUserId());
	}

	public List<QiLocalDefectCombination> findAllLocalAttributesByPartLocationId(List<Integer> partLocationIdList) {
		return getDao(QiLocalDefectCombinationDao.class).findAllLocalAttributesByPartLocationId(partLocationIdList);
	}

	public List<QiEntryScreenDefectCombination> findAllEntryScreensByPartLocationId(List<Integer> partLocationIdList) {
		return getDao(QiEntryScreenDefectCombinationDao.class).findAllEntryScreensByPartLocationId(partLocationIdList);
	}

	public List<QiPartDefectCombination> findAllRegionalAttributesByPartLocationId(List<Integer> partLocationIdList) {
		return getDao(QiPartDefectCombinationDao.class).findAllRegionalAttributesByPartLocationId(partLocationIdList);
	}
	
	public List<QiEntryScreenDefectCombination> findDefectInEntryScreenDefectCombination(Integer regionalDefectCombinationId) {
		return getDao(QiEntryScreenDefectCombinationDao.class).findAllByRegionalDefectCombinationId(regionalDefectCombinationId);
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
	 * This method finds localDefectCombinationId List mapping data based on local combination id
	 * @param localCombinationId
	 * @return List<QiExternalSystemDefectMap>ExternalSystemDefectMap
	 */
	public List<QiExternalSystemDefectMap> findAllByLocalCombinationId(List<Integer> localCombinationIdList) {
		String localCombIdListValue=StringUtils.join(localCombinationIdList, ',');
		return getDao(QiExternalSystemDefectMapDao.class).findAllByLocalCombinationId(localCombIdListValue);
	}
	
	
	/**
	 * This method finds external mapping data based on regional combination id
	 * @param regionalCombinationIdList
	 * @return List<QiExternalSystemDefectMap>ExternalSystemDefectMap
	 */
	public List<QiExternalSystemDefectMap> findAllByRegionalCombinationId(List<Integer>  regionalCombinationIdList) {
		String regionalCombIdListValue=StringUtils.join(regionalCombinationIdList, ',');
		return getDao(QiExternalSystemDefectMapDao.class).findAllByRegionalCombinationId(regionalCombIdListValue);
	}
	
	/**
	 * This method is used to list of get partLocationId based on list of partLocationId associated with PLC
	 * @param partLocationIdList
	 * @return
	 */
	public List<QiPartDefectCombination> findAllPLCIdsByPartLocId(List<Integer> partLocationIdList){
		return getDao(QiPartDefectCombinationDao.class).findAllPLCIdsByPartLocId(partLocationIdList,getProductKind());
	}
	
}


