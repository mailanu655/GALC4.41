package com.honda.galc.client.teamleader.qi.stationconfig;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.QiModel;
import com.honda.galc.client.teamleader.qi.stationconfig.clonestation.CopySection;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.qi.QiBomQicsPartMappingDao;
import com.honda.galc.dao.qi.QiDepartmentDao;
import com.honda.galc.dao.qi.QiEntryModelDao;
import com.honda.galc.dao.qi.QiEntryModelGroupingDao;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiEntryScreenDefectCombinationDao;
import com.honda.galc.dao.qi.QiEntryScreenDeptDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiPlantDao;
import com.honda.galc.dao.qi.QiRepairAreaDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dao.qi.QiSiteDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dao.qi.QiStationEntryDepartmentDao;
import com.honda.galc.dao.qi.QiStationEntryScreenDao;
import com.honda.galc.dao.qi.QiStationPreviousDefectDao;
import com.honda.galc.dao.qi.QiStationResponsibilityDao;
import com.honda.galc.dao.qi.QiStationUpcPartDao;
import com.honda.galc.dao.qi.QiStationWriteUpDepartmentDao;
import com.honda.galc.dao.qi.QiTextEntryMenuDao;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiStationResponsibilityDto;
import com.honda.galc.dto.qi.QiTerminalDetailDto;
import com.honda.galc.dto.qi.QiWriteUpDepartmentDto;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.qi.QiBomQicsPartMapping;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiDepartmentId;
import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiEntryModelId;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiSite;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationConfigurationId;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.entity.qi.QiStationResponsibility;
import com.honda.galc.entity.qi.QiStationUpcPart;
import com.honda.galc.entity.qi.QiStationWriteUpDepartment;
import com.honda.galc.entity.qi.QiStationWriteUpDepartmentId;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * <h3>EntryStationConfigModel Class description</h3>
 * <p> EntryStationConfigModel: Model class for Entry Station </p>
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
 *
 *
 */
public class EntryStationConfigModel extends QiModel{

	/**This method is used to get site name
	 * @return
	 */
	public String findSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
	}
	
	/**
	 * This method is used to find all Qics Station(Process Point) based on Division.
	 * @param divisionId
	 * @return
	 */
	public List<ProcessPoint> findAllQicsStationByApplicationComponentDivision(String divisionId) {
		return getDao(ProcessPointDao.class).findAllByApplicationComponentDivision(divisionId);
		
	}
	
	/** 
	 * This method is used to find all plant by site name
	 * @param siteName
	 * @return list of plant
	 */
	public List<Plant> findAllBySiteName(String siteName) {
		return getDao(PlantDao.class).findAllBySite(siteName);
	}
	/**
	 * This method is used to find Product Type
	 * @return
	 */
	public List<String> findAllProductTypes() {
		return getDao(ProductTypeDao.class).findAllProductTypes();
	}
	
	/**
	 * This method is used to find EntryModel by productType
	 */
	public List<QiEntryModel> findAllEntryModelByProductType(String productType){
		return getDao(QiEntryModelDao.class).findAllMtcAssignedModelByProductType(productType);
	}
	
	/**
	 * This method is used to find EntryScreen by model
	 */
	public List<QiEntryScreenDto> findAllEntryScreenByEntryModelAndEntryDepartment(String entryModel,String entryDepartment){
		if(isVersionCreated(entryModel) || isScreenUsed(entryModel)) {
			return getDao(QiEntryScreenDao.class).findAllByModelAndDept(entryModel, entryDepartment, (short)1);
		} else {
			return getDao(QiEntryScreenDao.class).findAllByModelAndDept(entryModel, entryDepartment, (short)0);
		}
	}
	
	/**
	 * This method is used to find  division based on Site and Plant
	 */
	public List<Division> findAllDivisionBySiteAndPlant(String siteName,String plantName) {
		return getDao(DivisionDao.class).findById(siteName,plantName);
    }

	public Division findDivisionByDivisionId(String divisionId)  {
		return getDao(DivisionDao.class).findByKey(divisionId);		
	}
	/**
	 * This method is used to find EntryDept by process point
	 */
	public List<QiStationEntryDepartment> findAllEntryDeptInfoByProcessPoint(String processPointId){
		return getDao(QiStationEntryDepartmentDao.class).findAllEntryDeptInfoByProcessPoint(processPointId);
	}
	
	/**
	 * This method is used to find number of EntryDept by process point
	 */
	public long countEntryDeptInfoByProcessPoint(String processPointId){
		return getDao(QiStationEntryDepartmentDao.class).countEntryDeptInfoByProcessPoint(processPointId);
	}
	
	/**
	 * This method is used to find Writeup Dept by process point
	 */
	public List<QiStationWriteUpDepartment> findAllWriteUpDepartmentByQicsStation(String qicsStation, String site, String plant){
		return getDao(QiStationWriteUpDepartmentDao.class).findAllWriteUpDepartmentByQicsStation(qicsStation, site, plant);
	}
	
	/**
	 * This method is used to find number of Writeup Dept by process point
	 */
	public long countWriteUpDepartmentByQicsStation(String qicsStation, String site, String plant){
		return getDao(QiStationWriteUpDepartmentDao.class).countWriteUpDepartmentByQicsStation(qicsStation, site, plant);
	}

	/**
	 * This method is used to check if department is valid
	 */
	public boolean isValidDepartment(String dept){
		if(StringUtils.isEmpty(dept))  return false;
		List<Division> allDiv = getDao(DivisionDao.class).findAll();
		boolean found = false;
		if(allDiv != null && !allDiv.isEmpty())  {
			for (Division div : allDiv)  {
				if(div.getDivisionId().equalsIgnoreCase(dept))  {
					found = true;
				}
			}
		}
		return found;
	}

	/**
	 * This method is used to check if department is valid
	 */
	public boolean isValidWriteupDepartment(String division, String site, String plant){
		if(StringUtils.isEmpty(division) || StringUtils.isEmpty(site)  || StringUtils.isEmpty(plant))  return false;
		List<QiDepartment> allDept = getDao(QiDepartmentDao.class).findAll();
		boolean found = false;
		if(allDept != null && !allDept.isEmpty())  {
			for (QiDepartment thisDept : allDept)  {
				if (thisDept.getId().getDepartment().equalsIgnoreCase(division)
						&& thisDept.getId().getPlant().equalsIgnoreCase(plant)
						&& thisDept.getId().getSite().equalsIgnoreCase(site)) {
					found = true;
				}
			}
		}
		return found;
	}

	/**
	 * This method is used to check entry model/entry screen combination is valid
	 */
	public boolean isValidEntryModelScreen(String entryScreen, String entryModel){
		long numRows = getDao(QiEntryScreenDao.class).countByEntryScreenAndModel(entryScreen, entryModel);
		return numRows > 0;
	}

	/**
	 * This method is used to check entry model/entry screen combination is valid
	 */
	public boolean isValidPart(String partNo){
		long numRows = getDao(QiBomQicsPartMappingDao.class).countByMainPartNo(partNo);
		return numRows > 0;
	}

	/**
	 * This method is used to find EntryScreen by process point
	 */
	public List<QiEntryScreenDto> findAllEntryScreenInfoByProcessPoint(String entryModel,String entryDept,String qicsStation){
		return getDao(QiStationEntryScreenDao.class).findAllEntryScreenInfoByProcessPoint(entryModel,entryDept,qicsStation);
	}
	
	/**
	 * This method is used to find EntryScreen by process point
	 */
	public List<QiStationEntryScreen> findAllEntryScreenByProcessPointAndDivision(String qicsStation, String division){
		return getDao(QiStationEntryScreenDao.class).findAllEntryScreenByProcessPointAndDivision(qicsStation, division);
	}
	
	/**
	 * This method is used to find number of EntryScreen by process point
	 */
	public long countEntryScreenByProcessPointAndDivision(String qicsStation, String division){
		return getDao(QiStationEntryScreenDao.class).countEntryScreenByProcessPointAndDivision(qicsStation, division);
	}
	
	/**
	 * This method is used to remove Station Entry Departments
	 */
	public void removedStationEntryDepartments(List<QiStationEntryDepartment> removedStationEntryDepartmentList){
		getDao(QiStationEntryDepartmentDao.class).removeAll(removedStationEntryDepartmentList);
	}
	
	/**
	 * This method is used to create Station Entry Dept
	 */
	public void createStationEntryDepartments(List<QiStationEntryDepartment> stationEntryDepartmentList){
		getDao(QiStationEntryDepartmentDao.class).saveAll(stationEntryDepartmentList);
	}
	
	/**
	 * This method is used to remove StationWriteup Dept
	 */
	public void removedStationWriteUpDept(List<QiStationWriteUpDepartment> removedStationWriteUpDeptList){
		getDao(QiStationWriteUpDepartmentDao.class).removeAll(removedStationWriteUpDeptList);
	}
	
	/**
	 * This method is used to create StationWriteup Dept
	 */
	public void createStationWriteUpDept(List<QiStationWriteUpDepartment> stationWriteUpDeptList){
		getDao(QiStationWriteUpDepartmentDao.class).saveAll(stationWriteUpDeptList);
	}
	/**
	 * This method is used to remove StationEntryScreen
	 */
	public void removedStationEntryScreen(List<QiStationEntryScreen> removedStationEntryScreenList){
		getDao(QiStationEntryScreenDao.class).removeAll(removedStationEntryScreenList);
	}
	
	/**
	 * This method is used to create StationEntryScreens
	 */
	public void createStationEntryScreens(List<QiStationEntryScreen> stationStationEntryScreenList){
		getDao(QiStationEntryScreenDao.class).saveAll(stationStationEntryScreenList);
	}

	/**
	 * This method is used to fetch Entry Station by Id .
	 */
	public QiStationConfiguration findEntryStationById(QiStationConfigurationId qiStationConfigurationId) {
		return getDao(QiStationConfigurationDao.class).findByKey(qiStationConfigurationId);
	}
	
	/**
	 * This method is used to update Entry Station .
	 */
	public void updateEntryStation(QiStationConfiguration qiEntryStationConfigManagement) {
		getDao(QiStationConfigurationDao.class).save(qiEntryStationConfigManagement);
	}
	
	/**
	 * This method is used to find defaultEntryDepart
	 */
	public QiStationEntryDepartment findDefaultEntryDepartment(String processPointId) {
		return getDao(QiStationEntryDepartmentDao.class).findDefaultEntryDeptByProcessPoint(processPointId);
    }

	/**
	 * This method is used to delete EntryStationDefaultDefectStatus
	 */
	public void deleteEntryStationDefaultDefectStatus(QiStationConfiguration qiEntryStationConfigManagement) {
		getDao(QiStationConfigurationDao.class).remove(qiEntryStationConfigManagement);
	}
	
	/**
	 * This method is used to find all process point
	 */
	public List<QiStationConfiguration> findAllByProcessPoint(String processPointId) {
		return getDao(QiStationConfigurationDao.class).findAllByProcessPointId(processPointId);
	}
	
	/**
	 * This method is used to find details of Application
	 */
	public Application findAppByProcessPoint(String processPointId) {
		return getDao(ApplicationDao.class).findByKey(processPointId);
	}
	
	/**
	 * This method is used to get all terminals defined for Application
	 */
	public List<QiTerminalDetailDto> findAllTerminalDetailByApplicationId(String applicationId) {
		return getDao(TerminalDao.class).findAllTerminalDetailByApplicationId(applicationId);
	}
	
	/**
	 * This method is used to find all process point
	 */
	public long countStationConfigByProcessPoint(String processPointId) {
		return getDao(QiStationConfigurationDao.class).countByProcessPointId(processPointId);
	}
	
	public int  deleteSectionByProcessPoint(String sourcePP, CopySection whichSection) {
		int numDeleted = 0;
		if(whichSection == CopySection.ENTRY_DEPT)  {		
			numDeleted = getDao(QiStationEntryDepartmentDao.class).deleteByProcessPoint(sourcePP);
		}
		else if(whichSection == CopySection.LIMIT_RESP)  {
			numDeleted = getDao(QiStationResponsibilityDao.class).deleteByProcessPoint(sourcePP);
			
		}
		else if(whichSection == CopySection.PREV_DEFECT_VISIBLE)  {
			numDeleted = getDao(QiStationPreviousDefectDao.class).deleteByProcessPoint(sourcePP);
			
		}
		else if(whichSection == CopySection.SETTINGS)  {
			numDeleted = getDao(QiStationConfigurationDao.class).deleteByProcessPoint(sourcePP);
			
		}
		else if(whichSection == CopySection.WRITEUP_DEPT)  {
			numDeleted = getDao(QiStationWriteUpDepartmentDao.class).deleteByQicsStation(sourcePP);			
		}
		if(whichSection == CopySection.ENTRY_MODEL)  {		
			numDeleted = getDao(QiStationEntryScreenDao.class).deleteByProcessPoint(sourcePP);			
		}
		if(whichSection == CopySection.UPC)  {		
			numDeleted = getDao(QiStationUpcPartDao.class).deleteByProcessPoint(sourcePP);			
		}
		return numDeleted;
	}
	
	
	public void saveAllQiEntryDept(List<QiStationEntryDepartment> entityList)  {
		getDao(QiStationEntryDepartmentDao.class).saveAll(entityList);
	}
	
	public void saveAllQiStationResponsibility(List<QiStationResponsibility> entityList)  {
		getDao(QiStationResponsibilityDao.class).saveAll(entityList);
	}
	
	public void saveAllQiStationPreviousDefect(List<QiStationPreviousDefect> entityList)  {
		getDao(QiStationPreviousDefectDao.class).saveAll(entityList);
	}
	
	public void saveAllQiStationConfiguration(List<QiStationConfiguration> entityList)  {
		getDao(QiStationConfigurationDao.class).saveAll(entityList);
	}
	
	public void saveAllQiStationWriteUpDepartment(List<QiStationWriteUpDepartment> entityList)  {
		getDao(QiStationWriteUpDepartmentDao.class).saveAll(entityList);
	}
	
	public void saveAllQiStationEntryScreen(List<QiStationEntryScreen> entityList)  {
		getDao(QiStationEntryScreenDao.class).saveAll(entityList);
	}
	public void saveAllQiStationUpcPart(List<QiStationUpcPart> entityList)  {
		getDao(QiStationUpcPartDao.class).saveAll(entityList);
	}
		
	/**
	 * This method is used to update EntryStationSettings
	 */
	public void updateAllEntryStationSettings(List<QiStationConfiguration> qiEntryStationConfigManagementList) {
		getDao(QiStationConfigurationDao.class).saveAll(qiEntryStationConfigManagementList);
	}
	
	/**
	 * This method is used to find all dept by Site and Plant
	 */
	public List<String> findAllDeptBySite(String site){
		return getDao(QiStationWriteUpDepartmentDao.class).findAllDeptBySite(site);
	}
	
	/**
	 * This method is used to find all dept by Site and Plant
	 */
	public List<QiDepartment> findAllQiDepartmentBySite(String site){
		return getDao(QiDepartmentDao.class).findAllBySite(site);
	}
	
	/**
	 * This method is used to find first dept by Site and department
	 */
	public QiDepartment findFirstQiDepartmentBySiteAndDepartment(String site, String department){
		return getDao(QiDepartmentDao.class).findFirstBySiteAndDepartment(site, department);
	}
	
	/**
	 * This method is used to find defaultwriteUpDepartment
	 */
	public QiStationWriteUpDepartment findDefaultWriteUpDepartmentByProcessPointId(String processPointId) {
		return getDao(QiStationWriteUpDepartmentDao.class).findDefaultWriteUpDeptByProcessPoint(processPointId);
    }
	
	/**
	 * This method finds WriteUp Department base on Dept
	 * @return QiStationWriteUpDepartment
	 */
	public QiStationWriteUpDepartment findWriteUpDeptByDept(QiStationWriteUpDepartmentId qiStationWriteUpDepartmentId) {
		return getDao(QiStationWriteUpDepartmentDao.class).findWriteUpDeptByDept(qiStationWriteUpDepartmentId);
	}
	
	/**
	 * This method find all UPC Part(s)
	 * @return QiBomQicsPartMapping
	 */
	public List<QiBomQicsPartMapping> findAllAvailableUPCParts() {
		return getDao(QiBomQicsPartMappingDao.class).findAllSortedByBomQicsParts();
	}
	
	/**
	 * This method find all UPC Part(s) by Process pointid
	 * @return QiStationUpcPart
	 */
	public List<QiStationUpcPart> findAllStationUpcPartByProcessPointId(String processPointId) {
		return getDao(QiStationUpcPartDao.class).findAllByProcessPointId(processPointId);
	}
	
	/**
	 * This method to save UPC Part(s) to Process pointid
	 */
	public void  createStationUpcPart(List<QiStationUpcPart> stationUpcPartList) {
		 getDao(QiStationUpcPartDao.class).saveAll(stationUpcPartList);
	}
	
	/**
	 * This method to delete UPC Part(s) from Process pointid
	 */
	public void  deleteStationUpcPart(List<QiStationUpcPart> stationUpcPartList) {
		 getDao(QiStationUpcPartDao.class).removeAll(stationUpcPartList);
	}
	
	/**
	 * This method is used to find number of UPC parts by process point
	 */
	public long countUpcPartByProcessPoint(String processPointId){
		return getDao(QiStationUpcPartDao.class).countByProcessPoint(processPointId);
	}
	
	/**
	 * This method to find QiStationEntryScreen by Id and EntryScreen
	 */
	public QiStationEntryScreen findStationEntryScreenByIdAndEntryScreen(QiStationEntryScreen qiStationEntryScreen) {
		return getDao(QiStationEntryScreenDao.class).findStationEntryScreenByIdAndEntryScreen(qiStationEntryScreen);
	}
	
	/**
	 * This method is used to find ComponentProperty by Id
	 */
	public ComponentProperty findComponentPropertyById(ComponentPropertyId componentPropertyId) {
		return getDao(ComponentPropertyDao.class).findByKey(componentPropertyId);
	}

	public List<QiStationPreviousDefect> findAllEntryDeptDefectInfoByProcessPoint(String processPoint) {
		return getDao(QiStationPreviousDefectDao.class).findAllByProcessPoint(processPoint);
	}
	
	public long countEntryDeptDefectInfoByProcessPoint(String processPoint) {
		return getDao(QiStationPreviousDefectDao.class).countByProcessPoint(processPoint);
	}
	
	public void createStationEntryDepartmentDefects(List<QiStationPreviousDefect> addedStationEntryDepartmentList) {
		 getDao(QiStationPreviousDefectDao.class).saveAll(addedStationEntryDepartmentList);
	}
	
	public void removeStationEntryDepartmentDefects(List<QiStationPreviousDefect> removedStationEntryDepartmentDefectList) {
		getDao(QiStationPreviousDefectDao.class).removeAll(removedStationEntryDepartmentDefectList);
	}
	
	public String findLineIdByProcessPoint(String processPoint) {
		return getDao(ProcessPointDao.class).findByKey(StringUtils.trimToEmpty(processPoint)).getLineId();
	}

	public void removeStationEntryByProcessPoint(String qicsStation, String entryModel, String entryDept) {
		getDao(QiStationEntryScreenDao.class).removeByProcesspointModelAndDivision(qicsStation,entryModel,entryDept);
	}
	
	/**
	 * This method is used to find all Active Sites
	 * @return
	 */
	public List<QiSite> findAllActiveSite() {
		return getDao(QiSiteDao.class).findAllActive();
	}

	/**
	 * This method is used to find All Active Plant By Site List
	 * @param assignedSiteNameList
	 * @return
	 */
	public List<QiPlant> findAllActivePlantBySiteList(List<String> assignedSiteNameList) {
		return getDao(QiPlantDao.class).findAllActiveBySiteList(assignedSiteNameList);
	}
	
	/**
	 * This method is used to find All Active Department By Site List And Plant List
	 * @param assignedSiteNameList
	 * @param assignedPlantNameList
	 * @return
	 */
	public List<QiDepartment> findAllActiveDepartmentBySitePlantList(List<String> assignedSitePlantList) {
		return getDao(QiDepartmentDao.class).findAllActiveBySitePlantList(assignedSitePlantList);
	}

	/**
	 * This method is used to find All Active Responsible Level By Site List Plant List And Dept List
	 * @param assignedSiteNameList
	 * @param assignedPlantNameList
	 * @param assignedDeptNameList
	 * @return
	 */
	public List<QiResponsibleLevel> findAllRespLevelByFilter(List<String> assignedSitePlantDeptList) {
		return getDao(QiResponsibleLevelDao.class).findAllActiveBySitePlantAndDeptList(assignedSitePlantDeptList);
	}
	
	/**
	 * This method is used to find All Active Responsible Level By Site List Plant List And Dept List
	 * @param assignedSiteNameList
	 * @param assignedPlantNameList
	 * @param assignedDeptNameList
	 * @return
	 */
	public List<QiStationResponsibilityDto> findAllStationResponsibilityDtoByFilter(List<String> assignedSitePlantDeptList) {
		List<QiResponsibleLevel> respLevelList = findAllRespLevelByFilter(assignedSitePlantDeptList);
		List<QiStationResponsibilityDto> dtoList = new ArrayList<QiStationResponsibilityDto>();
		if(respLevelList == null || respLevelList.isEmpty())  {
			return dtoList;
		}
		else  {
			for(QiResponsibleLevel rLevel : respLevelList)  {
				QiStationResponsibilityDto dto = new QiStationResponsibilityDto(rLevel);
				QiDepartment qiDept = getDao(QiDepartmentDao.class).findBySitePlantAndDepartment(rLevel.getSite(), rLevel.getPlant(), rLevel.getDepartment());
				if(qiDept != null)  {
					dto.setDepartmentName(qiDept.getDepartmentName());
				}
				dtoList.add(dto);
			}
			return dtoList;
		}
	}
	
	/**
	 * This method is used to find All Active Responsible Level By Site List Plant List And Dept List
	 * @param assignedSiteNameList
	 * @param assignedPlantNameList
	 * @param assignedDeptNameList
	 * @return
	 */
	public QiDepartment findResponsibleLevelBySitePlantAndDepartment(String site, String plant, String department) {
		return getDao(QiDepartmentDao.class).findBySitePlantAndDepartment(site, plant, department);
	}
	
	/**
	 * This method is used to save all Station Responsibilities
	 * @param stationResponsibilityList
	 */
	public void saveAllStationResponsibilities(List<QiStationResponsibility> stationResponsibilityList) {
		getDao(QiStationResponsibilityDao.class).saveAll(stationResponsibilityList);
	}
	
	/**
	 * This method is used to delete All Station Responsibilities By ProcessPoint
	 * @param processPoint
	 */
	public void deleteAllAssignedRespByProcessPoint(String processPoint) {
		getDao(QiStationResponsibilityDao.class).deleteAllByProcessPoint(processPoint);
	}
	
	/**
	 * This method is used to find All Assigned Station Responsibilities By ProcessPoint
	 * @param processPoint
	 * @return
	 */
	public List<QiStationResponsibilityDto> findAllAssignedRespByProcessPoint(String processPoint) {
		List<QiStationResponsibilityDto> dtoList = getDao(QiStationResponsibilityDao.class).findAllAssignedRespByProcessPoint(processPoint);
		if(dtoList != null && !dtoList.isEmpty())  {
			for(QiStationResponsibilityDto dto : dtoList)  {
				QiDepartment qiDept = getDao(QiDepartmentDao.class).findBySitePlantAndDepartment(dto.getSite(), dto.getPlant(), dto.getDept());
				if(qiDept != null)  {
					dto.setDepartmentName(qiDept.getDepartmentName());
				}
			}
		}
		return dtoList;

	}
	public List<QiStationResponsibility> findAllStationResponsibilityByProcessPoint(String processPoint) {
		return getDao(QiStationResponsibilityDao.class).findAllByProcessPoint(processPoint);
	}
	public long countAssignedRespByProcessPoint(String processPoint) {
		return getDao(QiStationResponsibilityDao.class).countAssignedRespByProcessPoint(processPoint);
	}
		
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion, boolean isUpdateModel) {

		getDao(QiLocalDefectCombinationDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
		getDao(QiEntryScreenDefectCombinationDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
		getDao(QiEntryScreenDeptDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
		getDao(QiTextEntryMenuDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
		getDao(QiEntryScreenDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
		
		if (isUpdateModel) {
			getDao(QiEntryModelGroupingDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
			getDao(QiEntryModelDao.class).updateVersionValue(entryModel, oldVersion, newVersion, getUserId());
		}
	}
	
	public void updateScreenIsUsed(String entryModel, String entryScreen, short screenIsUsed) {
		getDao(QiEntryScreenDao.class).updateScreenIsUsed(entryModel, entryScreen, screenIsUsed);
	}
	
	public boolean isVersionCreated(String entryModel) {
		return (getDao(QiEntryModelDao.class).findEntryModelCount(entryModel) > 1);
	}
	
	public boolean isMTCAssignToModel(String entryModel, short version) {
		return (getDao(QiEntryModelGroupingDao.class).findAllByEntryModel(entryModel, version).size() > 0);
	}
	
	public boolean isScreenUsed(String entryModel) {
		return (getDao(QiEntryScreenDao.class).findAllScreenUsedInStationByModel(entryModel, (short)1).size() > 0);
	}
	
	public void removeVersionRelationship(String entryModel, short version)  {
		QiEntryModelId qiEntryModelId = new QiEntryModelId();
		qiEntryModelId.setEntryModel(entryModel);
		qiEntryModelId.setIsUsed((short)1);
		
		getDao(QiLocalDefectCombinationDao.class).removeByEntryModelAndVersion(entryModel, version);
		getDao(QiEntryScreenDefectCombinationDao.class).removeByEntryModelAndVersion(entryModel, version);
		getDao(QiEntryScreenDeptDao.class).removeByEntryModelAndVersion(entryModel, version);
		getDao(QiTextEntryMenuDao.class).removeByEntryModelAndVersion(entryModel, version);
		getDao(QiEntryScreenDao.class).removeByEntryModelAndVersion(entryModel, version);
		getDao(QiEntryModelGroupingDao.class).removeByEntryModelAndVersion(entryModel, version);
		getDao(QiEntryModelDao.class).removeByKey(qiEntryModelId);
	}
	public void removeEntryModelAndGroup(String entryModel) {
		QiEntryModelId qiEntryModelId = new QiEntryModelId();
		qiEntryModelId.setEntryModel(entryModel);
		qiEntryModelId.setIsUsed((short)1);
		getDao(QiEntryModelGroupingDao.class).removeByEntryModelAndVersion(entryModel, (short)1);
		getDao(QiEntryModelDao.class).removeByKey(qiEntryModelId);
	}
	
	public boolean checkScreenIsUsed(String entryModel, String entryScreen, short version) {
		QiEntryScreenId entryScreenId = new QiEntryScreenId();
		entryScreenId.setEntryModel(entryModel);
		entryScreenId.setEntryScreen(entryScreen);
		entryScreenId.setIsUsed(version);
		QiEntryScreen qiEntryScreen = getDao(QiEntryScreenDao.class).findByKey(entryScreenId);
		if( qiEntryScreen != null) {
			return (qiEntryScreen.getScreenIsUsed() == (short) 1);
		}
		else
			return false;
	}
	
	public boolean isScreenUsedByStation(String entryModel, String entryScreen) {
		return (getDao(QiStationEntryScreenDao.class).findAllByEntryScreen(entryModel, entryScreen).size() > 0);
	}
	
	/**
	 * This method is used to find EntryScreen by process point
	 */
	public List<QiStationEntryScreen> findAllByEntryModel(String entryModel){
		return getDao(QiStationEntryScreenDao.class).findAllByEntryModel(entryModel);
	}
	
	/**
	 * This method is used to find all repair area by site and plant
	 * @param siteName
	 * @param plantName
	 * @param location
	 * @return
	 */
	public List<String> findAllRepairAreasBySiteAndPlant(String siteName,String plantName, char location){
		return getDao(QiRepairAreaDao.class).findAllBySitePlantLocation(siteName,plantName, location);
	}
}
