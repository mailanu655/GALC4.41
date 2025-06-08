package com.honda.galc.client.teamleader.qi.defectResult;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.QiModel;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.qi.QiAppliedRepairMethodDao;
import com.honda.galc.dao.qi.QiDefectIqsScoreDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiDefectResultHistDao;
import com.honda.galc.dao.qi.QiDepartmentDao;
import com.honda.galc.dao.qi.QiEntryModelDao;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiIncidentDao;
import com.honda.galc.dao.qi.QiLocalThemeDao;
import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.dao.qi.QiPddaResponsibilityDao;
import com.honda.galc.dao.qi.QiPlantDao;
import com.honda.galc.dao.qi.QiReasonForChangeDetailDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiRepairResultHistDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dao.qi.QiSiteDao;
import com.honda.galc.dao.qi.QiStationEntryScreenDao;
import com.honda.galc.dao.qi.QiStationWriteUpDepartmentDao;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.qi.QiDefectIqsScore;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.entity.qi.QiIncident;
import com.honda.galc.entity.qi.QiLocalTheme;
import com.honda.galc.entity.qi.QiPddaResponsibility;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiRepairResultHist;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>DefectResultMaintModel Class description</h3>
 * <p> DefectResultMaintModel description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * </TR> 
 *
 * </TABLE>
 *   
 * @author L&T Infotech<br>
 *
 *
 */
public class DefectResultMaintModel extends QiModel{
	
	private static final short DEFAULT_PROD_LINE_NO = 1;
	
	private String plant;

	
	public DefectResultMaintModel() {
		super();
	}
	
	/**This method is used to get site name
	 * @return String
	 */
	public String findSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
		
	}
	
	/** 
	 * This method is used to find all plant by site name
	 * @param siteName
	 * @return list of plant
	 */
	public List<Plant> findAllBySite(String siteName) {
		return getDao(PlantDao.class).findAllBySite(siteName);
	}
	
	/** 
	 * This method is used to find all Product type by site name
	 * @param siteName
	 * @return list of plant
	 */
	public List<String> findAllProductTypes() {
		return getDao(ProductTypeDao.class).findAllProductTypes();
	}
	
	/**This method is used to get all active local themes
	 * @return List<T>
	 */
	public List<QiLocalTheme> findAllActiveLocalTheme() {
		return getDao(QiLocalThemeDao.class).findAllActiveLocalTheme();
	}
	
	/**This method is used to get all Active department by site and plant
	 * @return List<String>
	*/
	public List<String> findAllBySiteAndPlant(String site, String plant) {
		return getDao(QiDepartmentDao.class).findAllActiveDepartmentsBySiteAndPlant(site, plant);
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
	 * This method is used to find all dept by Site and Plant
	 */
	public List<String> findAllDeptBySiteAndPlant(String site, String plant){
		return getDao(QiStationWriteUpDepartmentDao.class).findAllDeptBySiteAndPlant(site,plant);
	}
	
	public List<String> findAllByProductType(String productType) {
		return getDao(QiEntryScreenDao.class).findAllByProductType(productType);
	}
	
	/**This method is used to get all entry model by product type
	 * @return List<String>
	*/
	public List<String> findEntryModelsByProductType(String productType) {
		return getDao(QiEntryModelDao.class).findEntryModelsByProductType(productType);
	}
	
	/**This method is used to get all shifts
	 * @return List<String>
	*/
	public List<String> findAllShifts() {
		return getDao(DailyDepartmentScheduleDao.class).findAllShifts();
	}
	
	/**This method is used to get all division ids
	 * @return List<String>
	*/
	public List<String> findAllDivisionId() {
		return getDao(DivisionDao.class).findAllDivisionId();
	}
	
	/**
	 * This method sets the Available Mtc Model based on product type selection
	 * and filter.
	 */
	public List<String> findAvailableMtcModelData(String filter, String productType) {
		List<QiMtcToEntryModelDto> availableMtcList = new ArrayList<QiMtcToEntryModelDto>();
		availableMtcList = ProductTypeUtil.getProductSpecDao(productType).findAllMtcModelYearCodesByFilter(filter, productType);
		List<String> defectResultYearModelList = getDao(QiDefectResultDao.class).findAllYearModel(productType);
		List<String> activeList = new ArrayList<String>();
		for (QiMtcToEntryModelDto dto : availableMtcList) {
			dto.setProductType(productType);
			String mtcModel = dto.getAvailablelMtcModels();
			if (defectResultYearModelList.contains(mtcModel)) {
				activeList.add(mtcModel+"-"+dto.getModelDescription());
			}	
		}
		return activeList;
	}
	
	/**This method is used to get all defect Result
	 * @return List<String>
	*/
	public List<QiDefectResult> findAllSearchResultFromDefectResult(String searchString,String filterData,int defectDataRange,int searchResultLimit){
		return getDao(QiDefectResultDao.class).findAllByFilter(searchString,filterData,defectDataRange,searchResultLimit);
	}
	
	/**This method is used to get all repair result
	 * @param defectDataRange 
	 * @return List<String>
	*/
	public List<QiRepairResult> findAllSearchResultFromRepairResult(String searchString,String filterData, int defectDataRange, int searchResultLimit){
		return getDao(QiRepairResultDao.class).findAllByFilter(searchString,filterData,defectDataRange,searchResultLimit);
	}
	
	/**This method is used to get all sites
	 * @return List<String>
	*/
	public List<String> findAllQiSite() {
		return getDao(QiSiteDao.class).findAllSiteName();
	}
	
	/**This method is used to get entry screen by entry model
	 * @return List<String>
	*/
	public List<String> findAllByEntryModel(String entryModel) {
		return getDao(QiEntryScreenDao.class).findAllEntryScreensByPlantAndEntryModelAndPdc(plant, entryModel);
	}

	/**This method is used to get entry model by product type
	 * @return List<String>
	*/
	public List<String> findAllEntryModelByProductType(String productType) {
		return getDao(QiEntryModelDao.class).findEntryModelsByProductType(productType);
	}

	/**This method is used to get all pdc local attributes by entry screen
	 * @return List<PdcRegionalAttributeMaintDto>
	*/
	public List<PdcRegionalAttributeMaintDto> findAllPdcLocalAttributes(String entryScreen, String entryModel, short assigned, String filterValue){
		if (StringUtils.isBlank(entryScreen) || StringUtils.isBlank(entryModel)) {
			return new ArrayList<PdcRegionalAttributeMaintDto>();
		} else {
			return getDao(QiPartDefectCombinationDao.class).findAllPdcLocalAttributes(entryScreen, entryModel, assigned, filterValue);
		}
	}
	
	/**This method is used to get all defect Results
	 * @return List<QiDefectResultDto>
	*/
	public List<QiDefectResultDto> findAllDefectResults(String searchString,String filterData) {
		return getDao(QiDefectResultDao.class).findAllBySearchFilter(searchString,filterData);
	}
	
	/**This method is used to get all incident title
	 * @return List<String>
	*/
	public List<String> findAllQiIncidentTitle() {
		return getDao(QiIncidentDao.class).findAllIncidentTitle();
	}
	
	/**This method is used to create incident
	*/
	public void createQiIncident(QiIncident qiIncident) {
		getDao(QiIncidentDao.class).insert(qiIncident);
	}
	
	/**This method is used update incident
	*/
	public void updateQiIncident(QiIncident qiIncident) {
		getDao(QiIncidentDao.class).update(qiIncident);
	}
	
	/**This method is used to update incidents to Defect Result
	*/
	public void updateIncidentIdToQiDefectResult(Integer qiIncidentId, String user,String defectResultIdSet, Timestamp updatedTimestamp) {
		getDao(QiDefectResultDao.class).updateIncidentIdByDefectResultId(qiIncidentId, user,defectResultIdSet,updatedTimestamp);
	}
	
	/**This method is used to update incidents to Repair Result
	*/
	public void updateIncidentIdToQiRepairResult(Integer qiIncidentId, String user,String defectResultIdSet, Timestamp updatedTimestamp) {
		getDao(QiRepairResultDao.class).updateIncidentIdByDefectResultId(qiIncidentId, user,defectResultIdSet,updatedTimestamp);
	}

	
	/**This method is used to update defect results
	*/
	public void updateAllDefectResult(List<QiDefectResult> updatedDefectResultList){
		getDao(QiDefectResultDao.class).updateAll(updatedDefectResultList);
	}
	
	
	/**
	 * This method is used to update all the RepairResult data
	 * @param updatedRepairResultList
	 */
	public void updateAllRepairResult(List<QiRepairResult> updatedRepairResultList){
		getDao(QiRepairResultDao.class).updateAll(updatedRepairResultList);
	}
	
	public void updateRepairResult(QiRepairResult updatedRepairResult){
		getDao(QiRepairResultDao.class).update(updatedRepairResult);
	}

	/**
	 * This method is used to delete the selected DefectResult row from DefectResult tableView
	 * @param defectResultList
	 */
	public void deleteDefectResult(long defectResultId, String reasonForChange, String correctionRequester, String updateUser){
		getDao(QiDefectResultDao.class).deleteDefectResult(defectResultId, reasonForChange, correctionRequester, updateUser);
	}
	
	/**
	 * This method is used to find the active plants based on Site
	 * @param site
	 * @return List<String>
	 */
	public List<String> findAllActivePlantsbySite(String site) {
		return getDao(QiPlantDao.class).findAllActivePlantsBySite(site);
	}
	
	/**This method is used to insert in Defect Result History table
	*/
	public void createDefectResultHist(List<QiDefectResultHist> defectResultHistList) {
		for (QiDefectResultHist qiDefectResultHist : defectResultHistList) {
			getDao(QiDefectResultHistDao.class).insert(qiDefectResultHist);
		}
		 
	}
	
	/**This method is used to insert in  Repair Result History table
	*/
	public void createRepairResultHist(List<QiRepairResultHist> repairResultHistList) {
		 getDao(QiRepairResultHistDao.class).insertAll(repairResultHistList);
	}

	/**This method is used to get all incidents and date
	 * @return QiIncident
	*/
	public QiIncident findByIncidentTitleAndDate(String incidentTitle, String incidentDate) {
		 return getDao(QiIncidentDao.class).findByIncidentTitleAndDate(incidentTitle, incidentDate);
	}
	
	/**This method is used to get all proces points by entry screen
	 * @return List<String>
	*/
	public List<String> findAllProcessPointByEntryScreen(String entryScreen){
		return getDao(QiStationEntryScreenDao.class).findAllProcessPointByEntryScreen(entryScreen);
	}

	/**This method is used to get pdda responsibility by id
	 * @return QiPddaResponsibility
	*/
	public QiPddaResponsibility findByPddaResponsibilityId(Integer pddaResponsibilityId) {
		return getDao(QiPddaResponsibilityDao.class).findByKey(pddaResponsibilityId);
	}
	
	/**This method is used to get Repair Result by defectResultId
	 * @return List<QiRepairResult>
	*/
	public List<QiRepairResult> findAllByDefectResultId(long defectResultId) {

		return getDao(QiRepairResultDao.class).findAllByDefectResultId(defectResultId);
	}
	
	/**
	 * This method is used to get DefectResult object by id
	 * @param defectResultId
	 * @return
	 */
	public QiDefectResult findDefectResultById(long defectResultId) {
		return getDao(QiDefectResultDao.class).findByKey(defectResultId);
	}
	
	/**
	 * This method is used to get Entry Plant Prod Line No
	 * @return
	 */
	
	public short getCurrentWorkingProdLineNo(String processPointId) {

		if(StringUtils.isBlank(processPointId))  return DEFAULT_PROD_LINE_NO;
        short prodLineNo = DEFAULT_PROD_LINE_NO;
        try {
        	ProcessPoint pp = getDao(ProcessPointDao.class).findById(processPointId);
             if(pp != null) {
                GpcsDivision gpcs = ServiceFactory.getDao(GpcsDivisionDao.class).findByKey(pp.getDivisionId());
		        if(gpcs != null) {
				 prodLineNo = Short.parseShort(gpcs.getGpcsLineNo());
		        }
             }
        }catch(Exception e) {
	    	 prodLineNo = 1;
	    	 getLogger().error(e, "Exception when getting Production Line No",
					this.getClass().getSimpleName());
         }
       return prodLineNo;
	}
	
	public List<String> findAllNAQProcessPointId() {
		return ServiceFactory.getDao(ProcessPointDao.class).findAllNAQProcessPointId();
	}
	
	public List<QiAppliedRepairMethodDto> findAppliedMethodByRepairId(long repairId) {
		return getDao(QiAppliedRepairMethodDao.class).findAllAppliedRepairMethodDataByRepairId(repairId);
	}
	
	public short getMaxActualProblemSeq(long defectResultId) {
		short maxActualProblemSeq = 0;
		List<QiRepairResult> qiRepairResultList = findAllByDefectResultId(defectResultId);
		if (qiRepairResultList.size() > 0) {
			for (QiRepairResult qiRepairResult : qiRepairResultList) {
				if (qiRepairResult.getActualProblemSeq() > maxActualProblemSeq) {
					maxActualProblemSeq = qiRepairResult.getActualProblemSeq();
				}
			}
		}
		return maxActualProblemSeq;
	}
	
	public List<QiRepairResult> findAllRepairResultByDefectResultId(long defectResultId) {
		return getDao(QiRepairResultDao.class).findAllByDefectResultId(defectResultId);
	}
	
	public QiDefectResult findByDefectResultId(long defectResultId) {
		return getDao(QiDefectResultDao.class).findByKey(defectResultId);
	}
	
	public void updateDefectResult(QiDefectResult updatedDefectResult){
		getDao(QiDefectResultDao.class).update(updatedDefectResult);
	}

	public List<String> findAllByPlantAndProductType(String plant, String selectedProductType) {
		return getDao(QiEntryModelDao.class).findAllByPlantAndProductType(plant,selectedProductType);
	}
	
	/** 
	 * This method is used to find ComponentProperty by Id
	 */
	public ComponentProperty findComponentPropertyById(ComponentPropertyId componentPropertyId) {
		return getDao(ComponentPropertyDao.class).findByKey(componentPropertyId);
	}

	public List<String> findAllDivisionIdAndName() {

		List<String> deptList = new ArrayList<>();

		List<Object[]> entryDeptList = getDao(DivisionDao.class).findDivisionIdAndName();

		entryDeptList.forEach(entryDept-> {

		deptList.add(String.valueOf(entryDept[0]).trim()+"-"+entryDept[1]);

		});

		return deptList;
	}

	public QiDefectIqsScore findQiDefectIqsScore(long defectResultId) {
		return getDao(QiDefectIqsScoreDao.class).findByKey(defectResultId);
	}
	
	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}
	
	public List<String> findAllReasonForChangeDept(String site, String plant) {
		List<String> deptList = getDao(QiReasonForChangeDetailDao.class).findDeptBySitePlant(site, plant);
		return deptList;
	}
	
	public List<String> findReasonForChangeCategory(String site, String plant, String dept) {
		List<String> deptList = getDao(QiReasonForChangeDetailDao.class).findCategoryBySitePlantDept(site, plant, dept);
		return deptList;
	}
	
	public List<String> findReasonForChangeDetail(String site, String plant, String dept, String category) {
		List<String> deptList = getDao(QiReasonForChangeDetailDao.class).findDetailBySitePlantDeptCategory(site, plant, dept, category);
		return deptList;
	}
}