package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.qi.QiDefectDao;
import com.honda.galc.dao.qi.QiDepartmentDao;
import com.honda.galc.dao.qi.QiEntryModelDao;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiEntryScreenDeptDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiLocalThemeDao;
import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.dao.qi.QiPddaResponsibilityDao;
import com.honda.galc.dao.qi.QiPlantDao;
import com.honda.galc.dao.qi.QiRepairAreaDao;
import com.honda.galc.dao.qi.QiRepairMethodDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dao.qi.QiSiteDao;
import com.honda.galc.dao.qi.QiStationEntryScreenDao;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.qi.QiDefect;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiLocalTheme;
import com.honda.galc.entity.qi.QiPddaResponsibility;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiPlantId;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairMethod;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class PdcLocalAttributeMaintModel extends QiModel {
	
	public PdcLocalAttributeMaintModel(){
		super();
	}
	
	public List<PdcRegionalAttributeMaintDto> findAllPdcLocalAttributes(String entryScreen, String entryModel, short assigned, String filterValue){
		if (StringUtils.isBlank(entryScreen) || StringUtils.isBlank(entryModel)) {
			return new ArrayList<PdcRegionalAttributeMaintDto>();
		} else {
			return getDao(QiPartDefectCombinationDao.class).findAllPdcLocalAttributes(entryScreen, entryModel, assigned, filterValue);
		}
	}
	
	public String getSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
	}

	public List<String> findAllBySite(String siteName) {
		return getDao(DivisionDao.class).findPlantBySite(siteName);
	}
	
	public List<String> findAllProductTypes() {
		return getDao(ProductTypeDao.class).findAllProductTypes();
	}
	
	public List<String> findAllByPlantAndProductType(String plant, String selectedProductType) {
		return getDao(QiEntryModelDao.class).findAllByPlantAndProductType(plant, selectedProductType);
	}
	
	public List<String> findAllByPlantProductTypeAndEntryModel(String plant, String selectedProductType, String entryModel) {
		return getDao(QiEntryScreenDeptDao.class).findAllByPlantProductTypeAndEntryModel(plant, selectedProductType, entryModel);
	}
	
	public List<Division> findAllDivisionByPlantProductTypeAndEntryModel(String plant, String selectedProductType, String entryModel) {
		return getDao(QiEntryScreenDeptDao.class).findAllDivisionByPlantProductTypeAndEntryModel(plant, selectedProductType, entryModel);
	}
	
	public List<String> findAllActiveSites() {
		return getDao(QiSiteDao.class).findAllActiveSites();
	}
	
	public List<String> findAllActivePlantsBySite(String site) {
		return getDao(QiPlantDao.class).findAllActivePlantsBySite(site);
	}
	
	public List<String> findAllActiveDepartmentsBySiteAndPlant(String site, String plant) {
		return getDao(QiDepartmentDao.class).findAllActiveDepartmentsBySiteAndPlant(site, plant);
	}
	
	public List<QiResponsibleLevel> findAllBySitePlantAndDepartment(String site, String plant, String department, short level, Integer upperResponsibleLevelId) {
		return getDao(QiResponsibleLevelDao.class).findAllBySitePlantAndDepartment(site, plant, department, level, upperResponsibleLevelId);
	}
	
	public List<QiLocalTheme> findAllActiveTracking() {
		return getDao(QiLocalThemeDao.class).findAllActiveLocalTheme();
	}
	
	public List<QiRepairArea> findAllRepairArea() {
		return getDao(QiRepairAreaDao.class).findAllRepairArea();
	}
	
	public List<QiRepairMethod> findAllRepairMethod() {
		return getDao(QiRepairMethodDao.class).findAllActiveRepairMethods();
	}
	
	public void assignAttributes(List<QiLocalDefectCombination> defectCombinations){
		getDao(QiLocalDefectCombinationDao.class).insertAll(defectCombinations);
	}
	
	public QiLocalDefectCombination findByRegionalDefectCombinationId(Integer regionalDefectCombinationId, String entryScreen, String entryModel, short version) {
		return getDao(QiLocalDefectCombinationDao.class).findByRegionalDefectCombinationId(regionalDefectCombinationId, entryScreen, entryModel, version);
	}
	
	public void updateAttributes(List<QiLocalDefectCombination> defectCombinations){
		getDao(QiLocalDefectCombinationDao.class).updateAll(defectCombinations);
	}
	
	public QiResponsibleLevel findBySitePlantDepartmentLevelName(String site, String plant, String department, String levelName){
		return getDao(QiResponsibleLevelDao.class).findBySitePlantDepartmentAndLevelName(site, plant, department, levelName);
	}
	
	public QiResponsibleLevel findById(Integer responsibleLevelId){
		return getDao(QiResponsibleLevelDao.class).findByKey(responsibleLevelId);
	}
	
	public List<BigDecimal> findAllModelYear(){
		return getDao(QiPddaResponsibilityDao.class).findAllModelYear();
	}
	
	public List<String> findAllByModelYear(BigDecimal modelYear){
		return getDao(QiPddaResponsibilityDao.class).findAllByModelYear(modelYear);
	}
	
	public List<String> findAllByModelYearAndVMC(String modelYear, String vmc){
		return getDao(QiPddaResponsibilityDao.class).findAllByModelYearAndVMC(modelYear, vmc);
	}
	
	public List<String> findAllByModelYearVMCAndProcesNo(String modelYear, String vmc, String processNo){
		return getDao(QiPddaResponsibilityDao.class).findAllByModelYearVMCAndProcessNumber(modelYear, vmc, processNo);
	}
	
	public QiPddaResponsibility findByModelYearVMCProcesNoAndUnitNumber(String modelYear, String vmc, String processNo, String unitNo){
		return getDao(QiPddaResponsibilityDao.class).findByModelYearVMCProcessNoAndUnitNumber(modelYear, vmc, processNo, unitNo);
	}
	
	public QiPddaResponsibility findByPddaId(Integer pddaResponsibilityId){
		return getDao(QiPddaResponsibilityDao.class).findByKey(pddaResponsibilityId);
	}

	public String getAuditPrimaryKeyValue(Integer regionalDefectCombinationId) {
		return getDao(QiPartDefectCombinationDao.class).fetchAuditPrimaryKeyValue(regionalDefectCombinationId);
		
	}
	
	/**
	 * This method finds Defect by defect Type Name
	 * @param defectTypeName
	 * @return QiDefect
	 */
	public QiDefect findByDefectTypeName(String defectTypeName) {
		return getDao(QiDefectDao.class).findByKey(defectTypeName)	;
	}
	
	/**
	 * This method finds Repair Area by Repair Area Name
	 * @param repairAreaName
	 * @return QiRepairArea
	 */
	public QiRepairArea findRepairAreaByName(String repairAreaName) {
		return getDao(QiRepairAreaDao.class).findByKey(repairAreaName)	;
	}
	
	/**
	 * This method finds all responsible level name for a particular level
	 * @param site
	 * @param plant
	 * @param department
	 * @param level
	 * @return List<QiResponsibleLevel>
	 */
	public List<QiResponsibleLevel> findAllBySitePlantDepartmentLevel(String site, String plant, String department,short level){
		return getDao(QiResponsibleLevelDao.class).findAllBySitePlantDepartmentLevel(site, plant, department,level);
	}
	
	/**
	 * This method responsibility by responsible level id
	 * @param responsibleLevelId
	 * @return QiResponsibleLevel
	 */
	public QiResponsibleLevel findByResponsibleLevelId(Integer responsibleLevelId){
		return getDao(QiResponsibleLevelDao.class).findByResponsibleLevelId(responsibleLevelId);
	}
	
	/**
	 * This method gets all level 2 s having a level 1 with the given responsibility level 1 name
	 * @param site
	 * @param plant
	 * @param department
	 * @param level
	 * @return List<QiResponsibleLevel>
	 */
	public List<QiResponsibleLevel> findAllLevel2HavingSameLevel1(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel2HavingSameLevel1(site, plant, department, lvl1Name);
	}
	
	/**
	 * This method gets all level 3 s having a level 1 with the given responsibility level 1 name
	 * @param site
	 * @param plant
	 * @param department
	 * @param level
	 * @return List<QiResponsibleLevel>
	 */
	public List<QiResponsibleLevel> findAllLevel3HavingSameLevel1(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel3HavingSameLevel1(site, plant, department, lvl1Name);
	}
	
	/**
	 * This method gets all level 3 s having a level 1 with the given responsibility level 1 name
	 * @param site
	 * @param plant
	 * @param department
	 * @param level
	 * @return List<QiResponsibleLevel>
	 */
	public List<QiResponsibleLevel> findAllLevel1HavingSameLevel2(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel1HavingSameLevel2(site, plant, department, lvl1Name);
	}
	
	/**
	 * This method gets all level 2 s having a level 1 with the given responsibility level 1 name
	 * @param site
	 * @param plant
	 * @param department
	 * @param level
	 * @return List<QiResponsibleLevel>
	 */
	public List<QiResponsibleLevel> findAllLevel3HavingSameLevel2(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel3HavingSameLevel2(site, plant, department, lvl1Name);
	}
	
	/**
	 * This method gets all level 3 s having a level 1 with the given responsibility level 1 name
	 * @param site
	 * @param plant
	 * @param department
	 * @param level
	 * @return List<QiResponsibleLevel>
	 */
	public List<QiResponsibleLevel> findAllLevel1HavingSameLevel3(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel1HavingSameLevel3(site, plant, department, lvl1Name);
	}
	
	/**
	 * This method gets all level 2 s having a level 1 with the given responsibility level 1 name
	 * @param site
	 * @param plant
	 * @param department
	 * @param level
	 * @return List<QiResponsibleLevel>
	 */
	public List<QiResponsibleLevel> findAllLevel2HavingSameLevel3(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel2HavingSameLevel3(site, plant, department, lvl1Name);
	}
	
	/**
	 * This method finds Entry Screen by Plant,ProductType,Entry Model, Entry Dept
	 * @param plant
	 * @param selectedproductType
	 * @param entrymodel
	 * @param entryDept
	 * @return List<String>
	 */
	public List<String> findAllByPlantProductTypeEntryModelAndEntryDept(String plant, String selectedProductType, String entryModel,String entryDept) {
		return getDao(QiEntryScreenDao.class).findAllByPlantProductTypeEntryModelAndEntryDept(plant, selectedProductType, entryModel,entryDept);
	}
	
	public List<BigDecimal> findAllModelYearByPlantSiteDeptLine( String respPlant, String respSite,String respDept, String respLevel1) {
		String pddaLine = findBySiteAndPlant(respPlant, respSite);
		return getDao(QiPddaResponsibilityDao.class).findAllModelYearByPlantSiteDeptLine(respPlant, respSite,respDept, respLevel1, pddaLine);		
	}
	
	public List<String>  findAllVehicleModelCodeByPlantSiteDeptLineModelYear(String respPlant, String respSite, String respDept,String respLevel1,BigDecimal modelYear) {
		String pddaLine = findBySiteAndPlant(respPlant, respSite);
		return getDao(QiPddaResponsibilityDao.class).findAllVehicleModelCodeByPlantSiteDeptLineModelYear( respPlant, respSite,respDept, respLevel1, pddaLine,modelYear);		
	}
	
	public List<String>  findAllProcessNoByPlantSiteDeptLineModelYearVMC(String respPlant, String respSite, String respDept,
			String respLevel1,BigDecimal modelYear,String vehicleModelCode) {
		String pddaLine = findBySiteAndPlant(respPlant, respSite);
		return getDao(QiPddaResponsibilityDao.class).findAllProcessNoByPlantSiteDeptLineModelYearVMC( respPlant, respSite,respDept, respLevel1,pddaLine,modelYear,vehicleModelCode);		
	}
	
	public List<String>  findAllUnitNoByPlantSiteDeptLineModelYearVMCProcessNo(String respPlant, String respSite, String respDept,
			String respLevel1,BigDecimal modelYear,String vehicleModelCode,String processNumber) {
		String pddaLine = findBySiteAndPlant(respPlant, respSite);
		return getDao(QiPddaResponsibilityDao.class).findAllUnitNoByPlantSiteDeptLineModelYearVMCProcessNo( respPlant, respSite,respDept, respLevel1,pddaLine,modelYear,vehicleModelCode,processNumber);		
	}
	
	public QiPddaResponsibility  findUnitDescByPlantSiteDeptLineModelYearVMCProcessNoUnitNo(String respPlant, String respSite, String respDept,
			String respLevel1,BigDecimal modelYear,String vehicleModelCode,String processNumber,String unitNumber) {
		String pddaLine = findBySiteAndPlant(respPlant, respSite);
		return getDao(QiPddaResponsibilityDao.class).findByPlantSiteDeptLineModelYearVMCProcessNoUnitNo(respPlant, respSite,respDept, respLevel1,pddaLine,modelYear,vehicleModelCode,processNumber,unitNumber);		
	}

	private String findBySiteAndPlant(String respPlant, String respSite) {
		QiPlant plant=getDao(QiPlantDao.class).findByKey(new QiPlantId(respSite, respPlant));
		String pddaLine=plant.getPddaLine();
		return pddaLine;
	}
	
	public List<QiStationEntryScreen> findAllByEntryScreenModelAndDept(String entryScreenName,String entryModel,String entryDept){
		return getDao(QiStationEntryScreenDao.class).findAllByEntryScreenModelAndDept(entryScreenName,entryModel,entryDept,StringUtils.EMPTY);
		
	}
	
	/**
	 * This method is used to check version created or not.
	 */
	public boolean isVersionCreated(String entryModel) {
		return (getDao(QiEntryModelDao.class).findEntryModelCount(entryModel) > 1);
	}
	
	public QiPddaResponsibility updatePddaModelYear(QiPddaResponsibility pddaRespForPdc) {
		return getDao(QiPddaResponsibilityDao.class).update(pddaRespForPdc);
		
	}

	public QiLocalDefectCombination findByLocalDefectCombId(Integer localAttributeId) {
		return getDao(QiLocalDefectCombinationDao.class).findByKey(localAttributeId);
	}

	public QiLocalDefectCombination updatePddaRespId(QiLocalDefectCombination localDefectComb) {
		return getDao(QiLocalDefectCombinationDao.class).update(localDefectComb);
		
	}

	public List<QiPddaResponsibility> findByNewModelYear(BigDecimal modelYear, QiPddaResponsibility oldQiPddaResponsibility) {
		return getDao(QiPddaResponsibilityDao.class).findByNewModelYear(modelYear, oldQiPddaResponsibility);
		
	}

	public QiResponsibleLevel findLevel1BySitePlantDepartmentLevelNameAndLevel(String site,String plant, String dept, String level1) {
			return getDao(QiResponsibleLevelDao.class).findBySitePlantDepartmentLevelNameAndLevel(site,plant, dept,level1,(short) 1);
	}
	
	public QiResponsibleLevel findBySitePlantDeptLvlNameUpperLvlNameAndLvl(String site,String plant, String dept, String lvlName, String upperLvlName, int lvl, int upperLvl) {
		return getDao(QiResponsibleLevelDao.class).findBySitePlantDeptLvlAndNameAndUpperLvlAndName(site, plant, dept, lvlName, upperLvlName, (short)lvl, (short) upperLvl);
	}
	public QiResponsibleLevel findResponsibleLevel1BySitePlantDeptL1L2L3Names(String site,String plant, String dept, String l1Name, String l2Name, String l3Name) {
		return getDao(QiResponsibleLevelDao.class).findResponsibleLevel1BySitePlantDeptL1L2L3Names(site, plant, dept,  l1Name, l2Name, l3Name);
	}

}