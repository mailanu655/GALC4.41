package com.honda.galc.client.qi.homescreen;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.WebStartClientDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.dao.product.StationUserDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dao.qi.QiStationEntryDepartmentDao;
import com.honda.galc.dao.qics.StationResultDao;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.WebStartClient;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.StationUser;
import com.honda.galc.entity.product.StationUserId;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.entity.qics.StationResultId;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.MultiLineHelper;

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
public class HomeScreenModel extends QiProcessModel {

	public HomeScreenModel() {
		super();
	}

	public StationResult getInspectedDetails(String applicationId, String shift, Date productionDate) {
		
		StationResultId id = new StationResultId();
		id.setApplicationId(applicationId);
		id.setShift(shift);
		id.setProductionDate(productionDate);
		
		
		return getDao(StationResultDao.class).findByKey(id);
	}


	public WebStartClient getStationDetails(String ipAddress) {
		return getDao(WebStartClientDao.class).findByKey(ipAddress);
	}

	public List<QiDefectResultDto> getProcessedProductData(String applicationId, Timestamp startTimestamp, int maxRecords) {
		String productType = ClientMainFx.getInstance().getApplicationContext().getApplicationPropertyBean().getProductType();
		ProcessPointType processPointType = ClientMainFx.getInstance().getApplicationContext().getProcessPoint().getProcessPointType();
		ProductHistoryDao<? extends ProductHistory,?> productHistoryDao = ProductTypeUtil.getProductHistoryDao(productType);
		
		StringBuilder processPointIds = new StringBuilder();
		processPointIds.append("'").append(getProcessPointId());
		MultiLineHelper qiMultiLineHelper = MultiLineHelper.getInstance(getProcessPointId());
		if (qiMultiLineHelper.isMultiLine()) {
			List<String> validProcessPoints = qiMultiLineHelper.getValidProcessPoints();
			for (int i = 0; i < validProcessPoints.size(); i++) {
				if (!validProcessPoints.get(i).equals(applicationId)) 
					processPointIds.append("','").append(validProcessPoints.get(i));
			}			
		}
		processPointIds.append("'");
		List<QiDefectResultDto> processedProducts = productHistoryDao.findAllDefectHistoryByMultiStationsAndStartTime(processPointIds.toString(), startTimestamp, 
				maxRecords, processPointType.equals(ProcessPointType.QICSRepair));
			
		return processedProducts;
	}


	public ProcessPoint getProcessPointDetails() {
		return getDao(ProcessPointDao.class).findById(getProcessPointId());
	}

	public List<StationUser> getAllUser() {
		return getDao(StationUserDao.class).findAllStationUsersByHost(ClientMainFx.getInstance().getApplicationContext().getApplicationId());
	}

	
	/**
	 * This method is used to find default Entry Department configured from station configuration screen based on process point.
	 * @param processpointId
	 * @return QiStationEntryDepartment
	 */
	public QiStationEntryDepartment findDefaultEntryDeptByProcessPoint(String processPointId){
		return getDao(QiStationEntryDepartmentDao.class).findDefaultEntryDeptByProcessPoint(processPointId);
	}
	
	/**
	 * This method is used to find all Entry Department configured from station configuration screen based on process point.
	 * @param processpointId
	 * @return List<QiStationEntryDepartment>
	 */
	public List<QiStationEntryDepartment> findAllEntryDeptByProcessPoint(String processPointId){
		return getDao(QiStationEntryDepartmentDao.class).findAllEntryDeptInfoByProcessPoint(processPointId);
	}
	

	/**
	 * This method finds property value against property key based on process point.
	 * @return QiEntryStationConfigManagement
	 */
	public QiStationConfiguration findPropertyKeyValueByProcessPoint(String processPoint,String propertyKey) {
		return getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(processPoint, propertyKey);
	}
	

	/**
	 * This method finds recent users based on process point.
	 * @return  List<QiStationUser>
	 */
	public List<StationUser> findAllRecentUsersByHostName(String hostName,Timestamp loginTimeStamp ,int maxNoOfUser) {
		return getDao(StationUserDao.class).findAllRecentUsersByHostName(hostName, loginTimeStamp, maxNoOfUser);
	}
	
	public List<QiDefectResult> getCurrentDefectStatus(String productId) {
		return getDao(QiDefectResultDao.class).findAllCurrentDefectStatusByProductId(productId);
	}

	public List<ProductSequence> getProductQueueForStationId(String stationId) {
		return getDao(ProductSequenceDao.class).findAll(stationId);
	}

	public QiStationConfiguration findStationConfiguration(String propertyKey) {
		QiStationConfiguration data = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getProcessPointId(), propertyKey);
		return data;		
	}
	
	public StationResult addStationResultForShiftAndProdDate(Date productionDate,
			String shift, String applicationId) {
		StationResult stationResult = new StationResult();
		StationResultId stationResultId = new StationResultId();
		stationResultId.setShift(shift);
		stationResultId.setProductionDate(productionDate);
		stationResultId.setApplicationId(applicationId);
		stationResult.setId(stationResultId);
		
		return getDao(StationResultDao.class).save(stationResult);
		
	}

	private void sortMaxRecord(List<QiDefectResultDto> processedProducts, int maxRecords) {
		for (int i = 0; i < processedProducts.size() - 1; i++) {
			QiDefectResultDto qiDefectResultDto1 = processedProducts.get(i);
			for (int j = i + 1; j < processedProducts.size(); j++) {
				QiDefectResultDto qiDefectResultDto2 = processedProducts.get(j);
				if (qiDefectResultDto1.getCreateTime().before(qiDefectResultDto2.getCreateTime())) {
					processedProducts.set(i, qiDefectResultDto2);
					processedProducts.set(j, qiDefectResultDto1);
					qiDefectResultDto1 = qiDefectResultDto2;
				}
			}
		}
		
		for (int i = processedProducts.size() - 1; i >= maxRecords; i--) {
			processedProducts.remove(i);
		}
	}
	
	public void updateLastLogInUser(String userId) {
		StationUser qiStationUser = new StationUser();
		StationUserId id = new StationUserId(ClientMainFx.getInstance().currentApplicationId,userId);
		Timestamp dbTimestamp = getDao(ProcessPointDao.class).getDatabaseTimeStamp();
		qiStationUser.setLoginTimestamp(dbTimestamp);
		qiStationUser.setId(id);
		getDao(StationUserDao.class).updateTimeStampByStationUser(qiStationUser);
	}
	
	public int getProcessingLimit() {
		return getProperty().getProductProcessLimit();
	}
	
	/**
	 * This method is to find the device_id for this product, will used in the export CSV file function
	 * @param productId, processpointId
	 * @return
	 */
	public String getDeviceId(String productId, String processPointId) {
		String productType = ClientMainFx.getInstance().getApplicationContext().getApplicationPropertyBean().getProductType();
		ProductHistoryDao<? extends ProductHistory,?> productHistoryDao = ProductTypeUtil.getProductHistoryDao(productType);
		List<? extends ProductHistory> historyList = productHistoryDao.findAllByProductAndProcessPoint(productId, processPointId);
		if(historyList != null && historyList.size() > 0)
			return historyList.get(0).getDeviceId();
		else return null;
	}
	
	/**
	 * This method is to find the Count of defects have the same defect_transaction_group_id
	 * @param groupTransactionId
	 * @return
	 */
	public int getNumberOfParts(long groupTransactionId) {
		String prodType = ClientMainFx.getInstance().getApplicationContext().getApplicationPropertyBean().getProductType();
		List<QiDefectResult> defects = getDao(QiDefectResultDao.class).findAllByGroupTransIdProdType(groupTransactionId,prodType);
		return defects == null ? 0 : defects.size();
	}
	
	public QiDefectResult getDefectResultById(long defectResultId) {
		return getDao(QiDefectResultDao.class).findByKey(defectResultId);
	}
}

