package com.honda.galc.client.qi.repairentry;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.qi.base.AbstractQiDefectProcessModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.qi.QiAppliedRepairMethodDao;
import com.honda.galc.dao.qi.QiDefectDeviceDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiDepartmentDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectIdMapDao;
import com.honda.galc.dao.qi.QiExternalSystemInfoDao;
import com.honda.galc.dao.qi.QiImageDao;
import com.honda.galc.dao.qi.QiPlantDao;
import com.honda.galc.dao.qi.QiRepairAreaDao;
import com.honda.galc.dao.qi.QiRepairAreaRowDao;
import com.honda.galc.dao.qi.QiRepairAreaSpaceDao;
import com.honda.galc.dao.qi.QiRepairAreaSpaceHistoryDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiRepairResultHistDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dao.qi.QiStationPreviousDefectDao;
import com.honda.galc.dao.qi.QiStationWriteUpDepartmentDao;
import com.honda.galc.dao.qics.DefectRepairResultDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.dto.rest.RepairDefectDto;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.qi.QiAppliedRepairMethod;
import com.honda.galc.entity.qi.QiAppliedRepairMethodId;
import com.honda.galc.entity.qi.QiDefectDevice;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiExternalSystemDefectIdMap;
import com.honda.galc.entity.qi.QiExternalSystemInfo;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaRowId;
import com.honda.galc.entity.qi.QiRepairAreaSpace;
import com.honda.galc.entity.qi.QiRepairAreaSpaceHistory;
import com.honda.galc.entity.qi.QiRepairAreaSpaceId;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiRepairResultHist;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.KickoutService;
import com.honda.galc.service.LotControlRepairService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.qics.DefectStatusHelper;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>RepairEntryModel Class description</h3>
 * <p>
 * RepairEntryModel description
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
 * @author L&T Infotech<br>
 *         17th Nov, 2016
 *
 *
 */
public class RepairEntryModel extends AbstractQiDefectProcessModel {
	
	// Constant definition
	// when click NO PROBLEM FOUND, update tracking status in product table and create tracking history in product history table
	// tracking history record use the process point with tracking flag and sequence number = PROCESS_POINT_SEQUENCE_NUMBER_NPF
	private static int PROCESS_POINT_SEQUENCE_NUMBER_NPF = 999; 
	boolean isShowKickoutPane;
	
	public RepairEntryModel() {
		super();
	}
	public List<QiRepairResultDto> findAllDefectsByProductId(String productId) {
		return getDao(QiRepairResultDao.class).findAllDefectsByProductId(productId);
	}

	public List<QiRepairResultDto> findAllRepairEntryDefectsByDefectId(long defectResultId) {
		return getDao(QiRepairResultDao.class).findAllRepairEntryDefectsByDefectResultId(defectResultId);
	}

	public QiDefectResult findMainDefectByDefectId(long defectResultId) {
		return getDao(QiDefectResultDao.class).findByKey(defectResultId);
	}

	public QiRepairResult createRepairResult(QiRepairResult qiRepairResult) {
		return getDao(QiRepairResultDao.class).createRepairResult(qiRepairResult, null);
	}
	
	public QiRepairResult findRepairResultById(long repairId) {
		return getDao(QiRepairResultDao.class).findByKey(repairId);
	}

	public void deleteRepairEntryDefect(long repairId) {
		getDao(QiRepairResultDao.class).removeByKey(repairId);
	}

	public void deleteAllRepairMethods(long repairId) {
		getDao(QiRepairResultDao.class).deleteAllRepairMethodsByRepairId(repairId);
	}

	public void updateRepairResult(QiRepairResult qiRepairResult) {
		getDao(QiRepairResultDao.class).update(qiRepairResult);
	}
	
	public void updateDefectResult(QiDefectResult qiDefectResult) {
		getDao(QiDefectResultDao.class).update(qiDefectResult);
	}
	
	public void updateRepairComment(QiDefectResult qiDefectResult) {
		getDao(QiDefectResultDao.class).update(qiDefectResult);
	}
	
	
	public void releaseKickout(String productId, long kickoutId, String repairMethod) {
		if(kickoutId > 0) {
		KickoutDto kickoutDto = new KickoutDto();
		List<KickoutDto> kickoutDtoList = new ArrayList<KickoutDto>();
		kickoutDto.setKickoutId(kickoutId);
		kickoutDto.setProductId(productId);
		kickoutDtoList.add(kickoutDto);
		
		DefaultDataContainer data = new DefaultDataContainer();
		data.put(DataContainerTag.APPLICATION_ID, getApplicationId());
		data.put(DataContainerTag.RELEASE_COMMENT, repairMethod);
		data.put(DataContainerTag.ASSOCIATE_NO, getUserId());
		data.put(DataContainerTag.KICKOUT_PRODUCTS, kickoutDtoList);
		
		KickoutService kickoutService = ServiceFactory.getService(KickoutService.class);
		kickoutService.releaseKickouts(data);
		}
	}

	/**
	 * This method will be used to update the status of all the associated
	 * repair methods.
	 * 
	 * @param repairId
	 * @param isCompletelyFixed
	 */
	public void updateAllRepairMethodStatus(long repairId, boolean isCompletelyFixed) {
		getDao(QiRepairResultDao.class).updateAllRepairMethodStatusById(repairId, isCompletelyFixed, getUserId());
	}

	/**
	 * This method will be used to fetch latest fixed repair time stamp for
	 * given repair defect.
	 * 
	 * @param repairId
	 * @return latestRepairTimestamp
	 */
	public Date findLatestRepairTimestamp(long repairId) {
		return getDao(QiRepairResultDao.class).findLatestRepairTimestampByRepairId(repairId);
	}

	/**
	 * This method is used to save ExceptionalOut object.
	 * 
	 * @param exceptionalOut
	 */
	public void saveExceptionalOutUnit(ExceptionalOut exceptionalOut) {
		getDao(ExceptionalOutDao.class).createExceptionalOut(exceptionalOut);
	}

	/**
	 * This method is used to change the status of main defect.
	 * 
	 * @param defectResultId
	 * @param currDefectStatus
	 */
	public void updateMainDefectResultStatus(long defectResultId, int currDefectStatus) {
		getDao(QiRepairResultDao.class).updateDefectResultStatusById(defectResultId, currDefectStatus, getUserId());
	}
	
	/**
	 * This method is used to find Entry Station by processPointId
	 * 
	 * @return
	 */
	public QiStationConfiguration findEntryStationConfigById(String propertyKey) {
		return getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getCurrentWorkingProcessPointId(), propertyKey);
	}
	
	/**
	 * This method finds color code for given process point id and writeup dept
	 * @param dept
	 */
	public String findColorCodeByWriteupDeptAndProcessPointId(String dept){
		return getDao(QiStationWriteUpDepartmentDao.class).findColorCodeByWriteupDeptAndProcessPointId(dept, getCurrentWorkingProcessPointId());
	}
	
	/**
	 * gets the repair methods on the current repair method entry panel
	 * 
	 * @return
	 */
	public List<QiAppliedRepairMethodDto> findCurrentMethods() {
		return getDao(QiAppliedRepairMethodDao.class).findAllCurrentRepairMethods(getCurrentWorkingProcessPointId());
	}

	/**
	 * gets the methods based on the filter applied
	 * 
	 * @param repairMethodFilter
	 * @return
	 */
	public List<QiAppliedRepairMethodDto> findRepairMethodByFilter(String repairMethodFilter) {
		return getDao(QiAppliedRepairMethodDao.class).findAllRepairMethodsByFilter(repairMethodFilter, getCurrentWorkingProcessPointId());
	}

	/**
	 * inserts the repair method data when the Add Repair Method button is
	 * clicked
	 * 
	 * @param qiAppliedRepairMethod
	 */

	public QiAppliedRepairMethod insertRepairMethod(QiAppliedRepairMethod qiAppliedRepairMethod) {
		return getDao(QiAppliedRepairMethodDao.class).insertRepairMethod(qiAppliedRepairMethod, getCurrentWorkingProcessPointId());
	}
	
	/**
	 * inserts the repair method data when the Add Repair Method button is
	 * clicked
	 * 
	 * @param qiAppliedRepairMethod
	 */

	public QiAppliedRepairMethod insertRepairMethod(QiAppliedRepairMethod qiAppliedRepairMethod, Timestamp repairTimestamp) {
		return getDao(QiAppliedRepairMethodDao.class).insertRepairMethod(qiAppliedRepairMethod, getCurrentWorkingProcessPointId(), repairTimestamp);
	}
	
	/**
	 * Method will fetch all the applied repair method for given repair Id.
	 * 
	 * @param repairId
	 * @return
	 */
	public List<QiAppliedRepairMethodDto> getAppliedRepairMethodData(Long repairId) {
		return getDao(QiAppliedRepairMethodDao.class).findAllAppliedRepairMethodDataByRepairId(repairId);
	}

	/**
	 * updates the fixed status when the user selects the yes radio button
	 * 
	 * @param currentDefectStatus
	 * @param repairId
	 */
	public void updateFixedStatus(int currentDefectStatus, long repairId) {
		getDao(QiAppliedRepairMethodDao.class).updateFixedStatus(currentDefectStatus, repairId, getUserId());

	}

	/**
	 * gets the max of the sequence column(REPAIR_METHOD_SEQ)
	 * 
	 * @return
	 */
	public Integer findMaxSequenceValue() {
		return getDao(QiAppliedRepairMethodDao.class).findCurrentSequence();
	}
	
	/**
	 * gets the max of the sequence column(REPAIR_METHOD_SEQ)
	 * 
	 * @return
	 */
	public Integer findMaxSequenceValue(Long repairId) {
		return getDao(QiAppliedRepairMethodDao.class).findCurrentSequence(repairId);
	}
	
	public List<QiAppliedRepairMethodDto> getAppliedRepairMethodHistoryData(long repairId, QiRepairResultDto qiRepairResultDto) {
		List<QiAppliedRepairMethodDto> qiAppliedRepairMethodDataList = getDao(QiAppliedRepairMethodDao.class).findAllAppliedRepairMethodDataByRepairId(repairId);
		return setEntryDept(qiAppliedRepairMethodDataList, qiRepairResultDto);
	}
	
	public List<QiAppliedRepairMethodDto> getAppliedRepairMethodHistoryData(List<Long> repairIds, QiRepairResultDto qiRepairResultDto) {
		List<QiAppliedRepairMethodDto> qiAppliedRepairMethodDataList = getDao(QiAppliedRepairMethodDao.class).findCommonAppliedRepairMethod(repairIds);
		return setEntryDept(qiAppliedRepairMethodDataList, qiRepairResultDto);
	}
	
	private List<QiAppliedRepairMethodDto> setEntryDept(List<QiAppliedRepairMethodDto> qiAppliedRepairMethodDataList, QiRepairResultDto qiRepairResultDto) {
		List<QiAppliedRepairMethodDto> qiAppliedRepairMethodDataCopyList = new ArrayList<QiAppliedRepairMethodDto>();
		for (QiAppliedRepairMethodDto qiAppliedRepairMethodDto : qiAppliedRepairMethodDataList) {
			qiAppliedRepairMethodDto.setEntryDept(qiRepairResultDto.getEntryDept());
			qiAppliedRepairMethodDataCopyList.add(qiAppliedRepairMethodDto);
		}
		return qiAppliedRepairMethodDataCopyList;
	}
	
	public void removeAppliedRepairMethodByRepairIdAndSeq(long repairId, Integer repairMethodSeq) {
		getDao(QiAppliedRepairMethodDao.class).deleteAppliedRepairMethodById(repairId, repairMethodSeq);
	}
	

	
	/**
	 * This method is used to get the Site Name
	 */
	public String getSiteName() {
        return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
	}

	
	public List<String> findAllPlantsForCurrentSite(String siteName) {
		return getDao(QiPlantDao.class).findAllPlantBySite(siteName);
	}
	
	
	public List<String> findAllRespDeptBySitePlant(String site, String plant) {
		return getDao(QiDepartmentDao.class).findAllActiveDepartmentsBySiteAndPlant(site, plant);
	}
	
	public List<String> findAllAvailableRepairAreasByEntrySitePlantLocation(String siteName,String plantName, char location){
		return getDao(QiRepairAreaDao.class).findAllBySitePlantLocation(siteName, plantName, location);
	}
	
	public List<QiRepairArea> findAllAvailRepairAreaBySiteLocation(String siteName, char location){
		return getDao(QiRepairAreaDao.class).findAllAvailRepairAreaBySiteLocation(siteName, location);
	}
	
	public List<String> findAllRepairAreasBySite(String siteName) {
		List<QiRepairArea> qiRepairAreas = getDao(QiRepairAreaDao.class).findAllBySite(siteName);
		List<String> repairAreaNames = new ArrayList<String>();
		for (QiRepairArea qiRepairArea : qiRepairAreas) {
			repairAreaNames.add(qiRepairArea.getRepairAreaName() + " - " + qiRepairArea.getRepairAreaDescription());
		}
		Collections.sort(repairAreaNames);
		return repairAreaNames;
	}

	public String findRepairAreaByDefectResultId(long defectResultId) {
		return getDao(QiDefectResultDao.class).findRepairAreaById(defectResultId);
	}

	public QiRepairAreaSpace findRepairAreaSpaceByProductId(String productId) {
		return getDao(QiRepairAreaSpaceDao.class).findByProductId(productId);
	}

	public List<Integer> findAllAvailableRowByRepairArea(String repairArea) {
		return getDao(QiRepairAreaRowDao.class).findAllAvailableByRepairArea(repairArea);
	}

	public List<Integer> findAllAvailableSpaceByRepairAreaRow(String repairArea, Integer repairAreaRow) {
		return getDao(QiRepairAreaSpaceDao.class).findAllAvailableByRepairAreaAndRow(repairArea, repairAreaRow);
	}

	public void updateRepairArea(int defectResultId, String updateUser) {
		getDao(QiDefectResultDao.class).updateRepairArea(defectResultId, updateUser);
	}

	public void clearRepairAreaSpace(QiRepairAreaSpaceId id, String updateUser) {
		QiRepairAreaSpace qiRepairAreaSpace = getDao(QiRepairAreaSpaceDao.class).findByKey(id);
		String productId = qiRepairAreaSpace.getProductId();
		Long defectResultId = qiRepairAreaSpace.getDefectResultId();
		if (productId != null || defectResultId != null) {
			getDao(QiRepairAreaSpaceDao.class).clearRepairAreaSpace(id, updateUser);
		}
		
		//remove repair area result from GAL177TBX if configured, primary key is product_id
		if (productId != null) {
			removeInRepairAreaProduct(productId);
		}
	}

	public void assignRepairArea(String plant, String dept, String repairArea, String updateUser, int defectResultId) {
		getDao(QiDefectResultDao.class).updateRepairArea(plant, dept, repairArea, updateUser, defectResultId);
	}

	public QiRepairAreaSpace findRepairAreaSpaceById(QiRepairAreaSpaceId qiRepairAreaSpaceId) {
		return getDao(QiRepairAreaSpaceDao.class).findByKey(qiRepairAreaSpaceId);
	}
	
	/**
	 * This method is used to get QiRepairArea by using selected repair area name.
	 * @return QiRepairArea
	 */
	public QiRepairArea findRepairAreaByName(String repairAreaName) {
		return getDao(QiRepairAreaDao.class).findByKey(repairAreaName);
	}

	public QiRepairArea findInTransitRepairAreaBySiteAndPlant(String site, String plant) {
		return getDao(QiRepairAreaDao.class).findBySiteAndPlant(site, plant);
	}

	/**
	 * This method finds property value against property key based on process point.
	 * @return QiEntryStationConfigManagement
	 */
	public QiStationConfiguration findPropertyKeyValueByProcessPoint(String propertyKey) {
		return getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getCurrentWorkingProcessPointId(), propertyKey);
	}

	public QiRepairAreaRow findRowById(QiRepairAreaRowId id) {
		return getDao(QiRepairAreaRowDao.class).findByKey(id);
	}
	
	public QiRepairAreaRow findFirstAvailableRowByRepairArea(QiRepairArea  repairArea) {
		return getDao(QiRepairAreaRowDao.class).findRepairAreaRowByRepairArea(repairArea);
	}
	
	public QiRepairAreaSpace findFirstAvailableSpaceByRow(QiRepairAreaRow  row) {
		return getDao(QiRepairAreaSpaceDao.class).findFirstAvailableByRow(row);
	}

	public QiStationConfiguration getStationConfigForRepairArea(String processPointId, String settingsName) {
		return getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(processPointId, settingsName);
	}

	public long findMaxRepairId(){
		return getDao(QiRepairResultDao.class).findMaxRepairId();
	}

	public void saveRepairAreaSpaceHistoryData(QiRepairAreaSpaceHistory repairAreaSpaceHistoryData) {
		getDao(QiRepairAreaSpaceHistoryDao.class).save(repairAreaSpaceHistoryData);
	}
	
	public List<String> findAllPlantsBySiteAndLoc(String siteName, char location) {
		return getDao(QiRepairAreaDao.class).findAllPlantsBySiteAndLoc(siteName, location);
	}
	
	public void replicateRepairResult(QiAppliedRepairMethodDto qiAppliedRepairMethodDto, String partDefectDesc, String repairProcessPointId) {
		getDao(QiAppliedRepairMethodDao.class).replicateRepairResult(qiAppliedRepairMethodDto, partDefectDesc, repairProcessPointId);
	}

	public void deleteOldDefectResult(long qiDefectResultId) {
		getDao(DefectResultDao.class).deleteByQiDefectResultId(qiDefectResultId);
	}
	
	public void deleteOldRepairResult(long qiRepairId) {
		getDao(DefectRepairResultDao.class).deleteByQiRepairId(qiRepairId);
	}
	
	public void updateOldDefectStatus(long qiDefectResultId, int defectStatus, java.util.Date repairTimestamp, String repairAssociateNo) {
		getDao(DefectResultDao.class).updateByQiDefectResultId(qiDefectResultId, defectStatus, repairTimestamp, repairAssociateNo);
	}
	
	public QiDefectResult findDefectResultById(long defectResultId) {
		return getDao(QiDefectResultDao.class).findByKey(defectResultId);
	}
	
	public void createRepairResultHistory(QiRepairResultHist qiRepairResultHist) {
		getDao(QiRepairResultHistDao.class).save(qiRepairResultHist);
	}
	
	public QiRepairResult findLatestFixedRepairResult(long defectResultId) {
		return getDao(QiRepairResultDao.class).findLatestFixedRepairResult(defectResultId);
	}
	
	public QiStationConfiguration findStationConfiguration(String propertyKey) {
		QiStationConfiguration data = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getCurrentWorkingProcessPointId(), propertyKey);
		return data;		
	}
	
	public List<QiStationConfiguration> findAllByProcessPointId(String productId) {
		List<QiStationConfiguration> qiStationConfigurationList  = getDao(QiStationConfigurationDao.class).findAllByProcessPointId(productId);
		return qiStationConfigurationList;		
	}
	
	public boolean isRepairMethodAssigned(long qiRepairId) {
		Integer sequence = getDao(QiAppliedRepairMethodDao.class).findCurrentSequence(qiRepairId);
		if (sequence != null && sequence > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static List<QiRepairResult> findAllRepairResultByDefectResultId(long defectResultId) {
		return getDao(QiRepairResultDao.class).findAllByDefectResultId(defectResultId);
	}	
	
	public static List<Line> findAllTrackingLinesByProductType(String productType) {
		return getDao(LineDao.class).findAllTrackingLinesByProductType(productType);
	}
	
	public static List<ProcessPoint> findAllTrackingPointsByLineAndProductType(String productType, String lineId) {
		return getDao(ProcessPointDao.class).findAllTrackingPointsByLineAndProductType(productType, lineId);
	}			
	
	public List<QiStationPreviousDefect> findAllByProcessPoint() {
		return getDao(QiStationPreviousDefectDao.class).findAllByProcessPoint(getCurrentWorkingProcessPointId());
	}
	
	public List<QiRepairResultDto> findAllDefectsByProductIdEntryDepts(String productId, String entryDepts) {
		return getDao(QiRepairResultDao.class).findAllDefectsByProductIdEntryDepts(productId, entryDepts);
	}
	
	public boolean isShowKickoutPane() {
		return this.isShowKickoutPane;
	}
	
	public void setShowKickoutPane(boolean isShowKickoutPane) {
		this.isShowKickoutPane = isShowKickoutPane;
	}
	
	public long getDefectTransactionGroupCount(long defectTransactionGroupId) {
		return getDao(QiDefectResultDao.class).findDefectTransactionGroupCount(defectTransactionGroupId);
	}   

	public QiImage getImageByImageName(String imageName){
		return getDao(QiImageDao.class).findImageByImageName(imageName);
	}
	public QiExternalSystemDefectIdMap findExternalSystemDefectIdMap(long defectResultId) {
		return getDao(QiExternalSystemDefectIdMapDao.class).findByDefectId(defectResultId);
	}
	public QiExternalSystemInfo findExternalSystemDefectInfo(short extSysId) {
		return getDao(QiExternalSystemInfoDao.class).findByExternalSystemId(extSysId);
	}
	public Timestamp getDatabaseTimestamp() {
		return getDao(QiDefectResultDao.class).getDatabaseTimeStamp();
	}	
	
	public void callExternalSystemService(QiRepairResult qiRepairResult)  {
		
		if(qiRepairResult == null)  return;
		try {
			QiExternalSystemDefectIdMap extDefectMap = findExternalSystemDefectIdMap(qiRepairResult.getDefectResultId());
			if (extDefectMap == null || extDefectMap.getId() == null)  return;
			QiExternalSystemInfo qiExtSytemInfo = findExternalSystemDefectInfo(extDefectMap.getId().getExternalSystemId());
			String extSysName = "";
			if (qiExtSytemInfo != null) {
				extSysName = qiExtSytemInfo.getExternalSystemName();
			}
			if (StringUtils.isBlank(extSysName)) {
				Logger.getLogger().error("External System Name not found");
				return;
			}
			//create repair dto
			RepairDefectDto repairDefectDto = new RepairDefectDto();
			repairDefectDto.setAssociateId(ApplicationContext.getInstance().getUserId());
			repairDefectDto.setCurrentDefectStatus(qiRepairResult.getCurrentDefectStatus());
			repairDefectDto.setRepairMethod(qiRepairResult.getRepairMethodNamePlan());
			repairDefectDto.setRepairReason("Qics Repair");
			repairDefectDto.setExternalSystemKey(extDefectMap.getId().getExternalSystemDefectKey());
			repairDefectDto.setExternalSystemName(extSysName);
			repairDefectDto.setProductType(qiRepairResult.getProductType());
			
			if(extSysName.trim().equalsIgnoreCase(QiExternalSystem.LOT_CONTROL.name()))  {
				try {
					ServiceFactory.getService(LotControlRepairService.class).repairBuildResult(repairDefectDto);
				} catch (Exception ex) {
					Logger.getLogger().error(ex, "Exception invoking QiHeadlessDefectServiceImpl");
				}		
			}
			else  {
				/**
				 *  TBD for Other external system.  To be implemented using device manager.
				 *  Each external service will be a device definition
				 *  Lot Control service will be setup the same way
				 */
			}
		} catch (Exception e) {
			getLogger().error(e, "DefectEntryModel:callExternalSystemService: Exception invoking external system service");
			throw e;
		}
	}
	
	public void callExternalSystemService(long defectResultId, long repairResultId)  {
		try {
			QiRepairResult qiRepairResult = findRepairResultById(repairResultId);
			if(qiRepairResult == null)  return;
			callExternalSystemService(qiRepairResult);
		} catch (Exception e) {
			getLogger().error(e, "DefectEntryModel:callExternalSystemService: Exception in qiRepairResult lookup");
			throw e;
		}
	}
	
	public void updateDefectStatus(long defectResultId)  {
		DefectStatusHelper.updateDefectStatus(defectResultId);
	}
	
	public QiDefectDevice getDefectDevice(long defectResultId) {
		QiDefectDeviceDao qiDefectDeviceDao = getDao(QiDefectDeviceDao.class);
		return qiDefectDeviceDao.findByKey(defectResultId);
	}
	
	public void updateAppliedRepairMethodSql(Long id, Integer seq, String text, String userId) {
		 getDao(QiAppliedRepairMethodDao.class).updateAppliedRepairMethodSql(id,seq,text,userId);
		
	}
	
	public void createNoProblemFoundAppliedRepairMethod(long repairId) {
		if (getDao(QiAppliedRepairMethodDao.class).areAllMethodsNotCompletelyFixed(repairId)) {
			
			//create a new instance
			QiAppliedRepairMethodId qiAppliedRepairMethodId = new QiAppliedRepairMethodId();
			qiAppliedRepairMethodId.setRepairId(repairId);
			
			Integer count = findMaxSequenceValue(repairId);
			count = count == null ? 0 : count;
			qiAppliedRepairMethodId.setRepairMethodSeq(count + 1);
			
			QiAppliedRepairMethod qiAppliedRepairMethod = new QiAppliedRepairMethod();
			qiAppliedRepairMethod.setId(qiAppliedRepairMethodId);
			qiAppliedRepairMethod.setRepairTime(0);
			qiAppliedRepairMethod.setApplicationId(getCurrentWorkingProcessPointId());
			qiAppliedRepairMethod.setComment("");
			qiAppliedRepairMethod.setRepairMethod(AbstractRepairEntryController.NO_PROBLEM_FOUND);
			qiAppliedRepairMethod.setIsCompletelyFixed(1);
			qiAppliedRepairMethod.setCreateUser(getUserId());
			
			insertRepairMethod(qiAppliedRepairMethod);
		}
	}
	
	
	//update tracking status in product table to next line ID
	//and create corresponding product history and update in process product table
	public void updateNextProcessTracking(BaseProduct product) {
		String nextLineId = getDao(LineDao.class).findNextLineId(product.getTrackingStatus());
		if (StringUtils.isBlank(nextLineId)) {
			return;
		}
		
		List<ProcessPoint> processPointList = getDao(ProcessPointDao.class).findTrackingPointsByLine(nextLineId);
		ProcessPoint processPoint = null;
		if (processPointList.size() == 0) {
			return;
		}

		processPoint = processPointList.get(0);
		if (processPointList.size() > 1) {
			for (ProcessPoint pp : processPointList) {
				if (pp.getSequenceNumber() == PROCESS_POINT_SEQUENCE_NUMBER_NPF) {
					processPoint = pp;
					break;
				}
			}
		}
		
		final ProductHistory productHistory = getProductHistoryForTrack(processPoint, product.getProductId());
		if (productHistory != null) {
			Runnable track = new Runnable() {
				@Override
				public void run() {
					ServiceFactory.getService(TrackingService.class).track(ProductType.getType(getProductType()), productHistory);
				}
			};
			track.run();
		}
	}
	
	private ProductHistory getProductHistoryForTrack(ProcessPoint processPoint, String productid) {
		ProductHistory productHistory = ProductTypeUtil.createProductHistory(productid, processPoint.getProcessPointId(), getProductType());

		if (productHistory != null) { 
			productHistory.setAssociateNo(getApplicationContext().getUserId());
		}
		return productHistory;
	}
	
	public static RepairEntryModel getInstance()  {
		return new RepairEntryModel();
	}
}
