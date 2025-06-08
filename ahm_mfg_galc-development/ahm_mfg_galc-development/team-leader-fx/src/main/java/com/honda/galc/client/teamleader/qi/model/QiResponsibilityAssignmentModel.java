package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.qi.QiCompanyDao;
import com.honda.galc.dao.qi.QiDepartmentDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiPlantDao;
import com.honda.galc.dao.qi.QiProductKindDao;
import com.honda.galc.dao.qi.QiReportingTargetDao;
import com.honda.galc.dao.qi.QiResponsibilityMappingDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dao.qi.QiSiteDao;
import com.honda.galc.dao.qi.QiStationResponsibilityDao;
import com.honda.galc.entity.qi.QiCompany;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiDepartmentId;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiPlantId;
import com.honda.galc.entity.qi.QiReportingTarget;
import com.honda.galc.entity.qi.QiResponsibilityMapping;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiSite;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class QiResponsibilityAssignmentModel extends QiModel{

	public QiResponsibilityAssignmentModel() {

	}

	/** This method will return list all companies
	 */
	public List<QiCompany> findAllCompany() {
		return getDao(QiCompanyDao.class).findAll();
	}
	
	/** This method will return list all sites
	 */
	public List<QiSite> findAllSites() {
		return getDao(QiSiteDao.class).findAll();
	}
	
	/** This method will save the company
	 *
	 * @param company
	 * @return
	 */
	public void saveCompany(QiCompany company) {
		 getDao(QiCompanyDao.class).save(company);
	}
	
	/** This method will  update the company
	 *
	 * @param newCompanyName
	 * @param oldCompanyName
	 * @param companyDescription
	 * @param active
	 * @param updateUser
	 */
	public void updateCompany(String newCompanyName, String oldCompanyName,  String companyDescription, int active, String updateUser) {
		getDao(QiCompanyDao.class).updateCompany(newCompanyName, oldCompanyName, companyDescription, active, updateUser);
	}
	
	
	/** This method will Update companyName for QiSite on change of companyName in QiCompanyPanel
	 * 
	 * @param newCompanyName
	 * @param updateUser
	 * @param oldCompanyName
	 */
	public void updateCompanyForSite(String newCompanyName, String updateUser, String oldCompanyName) {
		getDao(QiSiteDao.class).updateSiteByCompanyName(newCompanyName, updateUser, oldCompanyName);
	}
	
	/** This method will save the plant
	 * 
	 * @param plant
	 * @return
	 */
	public QiPlant savePlant(QiPlant plant) {
		return getDao(QiPlantDao.class).save(plant);
	}
	
	/** This method will find all PRODUCT_KIND
	 */
	public List<String> findAllProductKindList() {
		return getDao(QiProductKindDao.class).findAllList();
	}

	/** This method will find all plants
	 */
	public List<QiPlant> findAllPlants() {
		return getDao(QiPlantDao.class).findAll();
	}

	/** This method will find the plant by plantName
	 * 
	 * @param plantName
	 * @return
	 */
	public QiPlant findPlantById(QiPlantId plantId) {
		return getDao(QiPlantDao.class).findByKey(plantId);
	}
	
	/** update the plant
	 *
	 * @param newPlant
	 * @param oldPlantName
	 
	 */
	public void updatePlant(QiPlant newPlant, String oldPlantName) {
		getDao(QiPlantDao.class).updatePlant(newPlant, oldPlantName);
	}
	
	/**  This method will save the site
	 *
	 */
	public QiSite saveSite(QiSite site) {
		return getDao(QiSiteDao.class).save(site);
	}
	
	/** This method will return Site object by using Site name
	 * 
	 * @param siteId
	 * @return
	 */
	public QiSite findSiteById(String siteId) {
		return getDao(QiSiteDao.class).findByKey(siteId);
	}
	
	/** This method will in-activate the company 
	 * @param companyName
	 */
	public void inactivateCompany(String companyName){
		getDao(QiCompanyDao.class).inactivateCompany(companyName);
	}
	
	/** This method will in-activate the plant
	 * @param id
	 */
	public void inactivatePlant(QiPlantId id){
		getDao(QiPlantDao.class).inactivatePlant(id);
	}
	
	
	/** This method will in-activate the Department
	 * @param id
	 */
	public void inactivatDepartment(QiDepartmentId id){
		getDao(QiDepartmentDao.class).inactivateDepartment(id);
	}
	
	/** This method will in-activate the Responsible level
	 * @param id
	 */
	public void inactivateResponsibleLevel(int id){
		getDao(QiResponsibleLevelDao.class).inactivateResponsibleLevel(id);
	}
	
	
	/** This method will return Company object by using Company Name 
	 * @param companyName
	 * @return
	 */
	public QiCompany findCompanyById(String companyName) {
		return getDao(QiCompanyDao.class).findByKey(companyName);
	}
	
	/** This method will update the site
	 *
	 * @param newSiteName
	 * @param oldSiteName
	 * @param SiteDescription
	 * @param active
	 * @param updateUser
	 */	
	public void updateSite(String newSiteName, String oldSiteName,  String siteDescription, int active, String updateUser) {
		getDao(QiSiteDao.class).updateSite(newSiteName, oldSiteName, siteDescription, active, updateUser);
	}
	
	/** This method will find all Department
	 */
	public List<QiDepartment> findAllDepartments() {
		return getDao(QiDepartmentDao.class).findAll();
	}
	
	/** This method will save the Department
	 */
	public QiDepartment saveDepartment(QiDepartment dept) {
		return getDao(QiDepartmentDao.class).insert(dept);
	}
	
	
	/** This method will return Company object by using Department Id 
	 * @param deptId
	 * @return
	 */
	public QiDepartment findDepartmentById(QiDepartmentId deptId ) {
		return getDao(QiDepartmentDao.class).findByKey(deptId);
	}
	
	/** update the department
	 *
	 * @param oldDepartmentId
	 * @param newDept
	 */
	public void updateDepartment(QiDepartmentId oldDepartmentId, QiDepartment newDept) {
		getDao(QiDepartmentDao.class).updateDepartment(oldDepartmentId, newDept);
	}
	
	/** Find all Responsible Level
	 */
	public List<QiResponsibleLevel> findAllResponsibleLevels() {
		return getDao(QiResponsibleLevelDao.class).findAll();
	}
	
	/** This method will save the Responsible Level
	 */
	public QiResponsibleLevel saveResponsibleLevel(QiResponsibleLevel responsibleLevel) {
		return getDao(QiResponsibleLevelDao.class).save(responsibleLevel);
	}

	/** This method will update the plant name associated with department on happening of rename on plant name on plant panel
	 * 
	 * @param updateUser
	 * @param newPlantName
	 * @param id
	 */
	public void updatePlantForDept(String updateUser,String newPlantName, QiDepartmentId id) {
		getDao(QiDepartmentDao.class).updatePlantById(updateUser, newPlantName, id);
	}

	/** This method will update the plant name associated with responsible level on happening of rename on plant name on plant panel
	 * 
	 * @param newPlantName
	 * @param updateUser
	 * @param id
	 */
	public void updatePlantForResponsibleLevel(String newPlantName, String updateUser, int id) {
		getDao(QiResponsibleLevelDao.class).updatePlantById(newPlantName, updateUser, id);
	}

	/** This method will in-activate the site
	 * 
	 * @param site
	 */
	public void inactivateSite(String site) {
		getDao(QiSiteDao.class).inactivateSite(site);
	}

	/** This method will update the department associated with responsible level on happening of rename on department name on department panel
	 * 
	 * @param deptId
	 * @param updateUser
	 * @param id
	 */
	public void updateDeptForResponsibleLevel(String department, String updateUser, int id) {
		getDao(QiResponsibleLevelDao.class).updateDeptById(department, updateUser, id);
	}

	/** This method will update the site name associated with plant on happening of rename on site name on site panel
	 * 
	 * @param updateUser
	 * @param siteName
	 * @param id
	 */
	public void updateSiteForPlant(String updateUser, String siteName, QiPlantId id) {
		getDao(QiPlantDao.class).updateSiteById(updateUser, siteName, id);
	}

	/** This method will update the site name associated with department on happening of rename on site name on site panel
	 * 
	 * @param updateUser
	 * @param siteName
	 * @param id
	 */
	public void updateSiteForDept(String updateUser, String siteName, QiDepartmentId id) {
		getDao(QiDepartmentDao.class).updateSiteById(updateUser, siteName, id);
	}

	/** This method will update the site name associated with responsible level on happening of rename on site name on site panel
	 * 
	 * @param siteName
	 * @param updateUser
	 * @param id
	 */
	public void updateSiteForResponsibleLevel(String siteName, String updateUser, Integer id) {
		getDao(QiResponsibleLevelDao.class).updateSiteById(siteName, updateUser, id);
	}
	
	/**
	 * @param responsibleLevelName
	 * @param dept
	 * @return
	 */
	public QiResponsibleLevel findResponsibleLevelByNameAndDeptId(String responsibleLevelName,String dept) {
		return getDao(QiResponsibleLevelDao.class).findByNameAndDeptId(responsibleLevelName, dept);
	}
	
	/**
	 * @param responsibleLevelName
	 * @return
	 */
	public QiResponsibleLevel findResponsibleLevelByName(int responsibleLevelName) {
		return getDao(QiResponsibleLevelDao.class).findByKey(responsibleLevelName);
	}

	/**
	 * @param id
	 * @return
	 */
	public List<QiResponsibleLevel> findResponsibleLevelListByUpperResponsibleLevel(int id) {
		return getDao(QiResponsibleLevelDao.class).findAllByUpperResponsibleLevel(id);
	}
	
	/** Find the responsible level based on site,plant,department,levelName,level
	 * 
	 * @param site
	 * @param plant
	 * @param department
	 * @param levelName
	 * @param level
	 * @return QiResponsibleLevel 
	 */
	public QiResponsibleLevel findBySitePlantDepartmentLevelNameAndLevel(String site,String plant,String department,String levelName, short level) {
		return getDao(QiResponsibleLevelDao.class).findBySitePlantDepartmentLevelNameAndLevel(site, plant, department, levelName, level);
	}
	
	/** Find the responsible level based on site,plant,department,levelName,level,uppperResponsibleLevel
	 * 
	 * @param site
	 * @param plant
	 * @param department
	 * @param levelName
	 * @param level
	 * @param upperResponsibleIdlevel
	 * @return QiResponsibleLevel 
	 */
	public QiResponsibleLevel findBySitePlantDepartmentLevelNameLevelAndUpperResponsibleLevelId(String site,String plant,String department,String levelName, short level,int upperResponsibleId) {
		return getDao(QiResponsibleLevelDao.class).findBySitePlantDepartmentLevelNameLevelAndUpperResponsibleLevelId(site, plant, department, levelName, level,upperResponsibleId);
	}
	
	/** Find the responsible level based on id
	 * 
	 * @param responsibleLevelId
	 * @return QiResponsibleLevel 
	 */
	public QiResponsibleLevel findResponsibleLevelById(int responsibleLevelId) {
		return getDao(QiResponsibleLevelDao.class).findByKey(responsibleLevelId);
	}
	
	/** Update the responsible level
	 *
	 * @param responsibleLevelName
	 * @param responsibleLevelDesc
	 * @param upperResponsibleLevelId
	 * @param active
	 * @param updateUser
	 * @param responsibleLevelId
	 */	
	public void updateResponsibleLevel(String responsibleLevelName, String responsibleLevelDesc,int upperResponsibleLevelId, int responsibleLevelId, int active, String updateUser) {
		 getDao(QiResponsibleLevelDao.class).updateResponsibleLevel(responsibleLevelName, responsibleLevelDesc, upperResponsibleLevelId, responsibleLevelId, active, updateUser);
	}
	
	/**Find the responsible level based on site,plant,department,level
	 * 
	 * @param site
	 * @param plant
	 * @param department
	 * @param level
	 * @return List QiResponsibleLevel 
	 */
	public List<QiResponsibleLevel> findAllResponsibleLevelBySitePlantDepartmentAndLevel(String site,String plant,String department,short level) {
		return getDao(QiResponsibleLevelDao.class).findBySitePlantDepartmentAndLevel(site, plant, department, level);
	}
	
	/** Find all Responsible Levels by level
	 */
	public List<QiResponsibleLevel> findResponsibleLevelsByLevel(short level) {
		return getDao(QiResponsibleLevelDao.class).findAllByLevel(level);
	}

	/** This method will return list of site objects 
	 * 
	 * @param companyName
	 * @return
	 */
	public List<QiSite> findAllSiteByCompany(String companyName) {
		return getDao(QiSiteDao.class).findAllByCompany(companyName);
	}

	/** This method will return list of plant objects
	 * 
	 * @param siteName
	 * @return
	 */
	public List<QiPlant> findAllPlantBySite(String siteName) {
		return getDao(QiPlantDao.class).findAllBySite(siteName);
	}

	/** This method will return list of Department objects 
	 * 
	 * @param siteName
	 * @param plantName
	 * @return
	 */
	public List<QiDepartment> findAllDepartmentBySiteAndPlant(String siteName, String plantName) {
		return getDao(QiDepartmentDao.class).findAllBySiteAndPlant(siteName, plantName);
	}

	/** This method will return list of responsible level objects
	 * 
	 * @param site
	 * @param plant
	 * @param dept
	 * @return
	 */
	public List<QiResponsibleLevel> findAllResponsibleLevelBySitePlantDepartment(String site, String plant, String dept) {
		return getDao(QiResponsibleLevelDao.class).findAllBySitePlantDepartment(site, plant, dept);
	}
	
	/**This method is used to get site name
	 * @return
	 */
	public String findSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
		
	}

	
	public List<QiReportingTarget> findAllByDepartmentAndDate(String deptName) {
		return getDao(QiReportingTargetDao.class).findAllByDepartmentAndDate(deptName);
	}
	
	public List<QiLocalDefectCombination> findAllBySiteAndPlant(String site, String plantName) {
		return getDao(QiLocalDefectCombinationDao.class).findAllBySiteAndPlant(site, plantName);
	}
	
	public List<QiLocalDefectCombination> findAllBySitePlantAndDepartment(String site, String plantName, String department) {
		return getDao(QiLocalDefectCombinationDao.class).findAllBySitePlantAndDepartment(site, plantName, department);
	}
	
	public List<QiLocalDefectCombination> findAllByResponsibleLevel(String site, 
			String plantName, String department, String responsibility, short level) {
			return getDao(QiLocalDefectCombinationDao.class).findAllByResponsibleLevel(site, plantName, department, responsibility, level);
	}

	public List<String> findAllAssignedSites() {
		return getDao(QiSiteDao.class).findAllAssignedSites();
	}

	public List<String> findAllAssignedPlants() {
		return getDao(QiPlantDao.class).findAllAssignedPlants();
	}

	public List<String> findAllAssignedDepts() {
		return getDao(QiDepartmentDao.class).findAllAssignedDepts();
	}

	public List<QiResponsibleLevel> findAllAssignedRespLevels(String site, String plant, String dept,  String respName) {
		return getDao(QiResponsibleLevelDao.class).findAllAssignedRespLevelsBySitePlantDeptName(site, plant, dept, respName);
	}
	
	public List<QiResponsibleLevel> findAllRespLevels(String site, String plant, String dept,  String respName) {
		return getDao(QiResponsibleLevelDao.class).findAllRespLevelsBySitePlantDeptandName(site, plant, dept, respName);
	}
	
	public long countAssignedToStationByRespId(int responsibleLevelId) {
		return getDao(QiStationResponsibilityDao.class).countByResponsibleLevelId(responsibleLevelId);
	}
	
	public long countAssignedToStationByRespLevelAndId(int responsibleLevelId, short level) {
		long howMany = 0;
		if(level == 1)  {
			howMany = getDao(QiStationResponsibilityDao.class).countByResponsibleLevelId(responsibleLevelId);
		}
		if(level == 2)  {
			howMany = getDao(QiStationResponsibilityDao.class).countByResponsibleLevel2(responsibleLevelId);
		}
		else if(level == 3)  {
			howMany = getDao(QiStationResponsibilityDao.class).countByResponsibleLevel3(responsibleLevelId);
		}
		return howMany;
	}
	
	public long countAssignedToLocalDefectCombinationByRespId(int responsibleLevelId) {
		return getDao(QiLocalDefectCombinationDao.class).countByResponsibleLevelId(responsibleLevelId);
	}
	
	public long countAssignedToLocalDefectCombinationByRespLevelAndId(int responsibleLevelId, short level) {
		long howMany = 0;
		if(level == 1)  {
			howMany = getDao(QiLocalDefectCombinationDao.class).countByResponsibleLevelId(responsibleLevelId);
		}
		if(level == 2)  {
			howMany = getDao(QiLocalDefectCombinationDao.class).countByResponsibleLevel2(responsibleLevelId);
		}
		else if(level == 3)  {
			howMany = getDao(QiLocalDefectCombinationDao.class).countByResponsibleLevel3(responsibleLevelId);
		}
		return howMany;
	}
	
	public boolean insertResponsibleMapping(int defRespLevelId, int altRespLevelId, String plantCode, String userId) {
		return getDao(QiResponsibilityMappingDao.class).insertResponsibleMapping(defRespLevelId, altRespLevelId, plantCode, userId);
		
	}
	
	public void deleteResponsibleMapping(int defRespLevelId, String plantCode) {
		getDao(QiResponsibilityMappingDao.class).deleteResponsibleMapping(defRespLevelId, plantCode);
		
	}
	
	public QiResponsibleLevel getResponsibleLevelId(String site, String plant, String dept, String level, String respLevelName) {
		return getDao(QiResponsibleLevelDao.class).getResponsibleLevelId(site, plant, dept, level, respLevelName);
		
	}

	public List<String> findAllEnginePlantCodes() {
		return getDao(EngineDao.class).findAllEnginePlantCodes();
	}
 
	public List<QiResponsibilityMapping> findAll() {
		return getDao(QiResponsibilityMappingDao.class).findAll();
		
	}
	
}
