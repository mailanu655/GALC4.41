package com.honda.galc.dao.qi;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiMostFrequentDefectsDto;
import com.honda.galc.dto.qi.QiRecentDefectDto;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiDefectResultDao Class description</h3>
 * <p>
 * QiDefectResultDao
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
 *        Nov 26, 2016
 * 
 */
public interface QiDefectResultDao extends IDaoService<QiDefectResult, Long> {

	public List<QiDefectResult> findAllNotRepairedDefects(String productId);

	public List<QiDefectResult> findAllNotRepairedNotKickoutDefects(String productId);
	
	public List<QiDefectResult> findAllNotRepairedKickoutDefects(String productId);

	public List<QiDefectResult> findAllByProductId(String productId, List<Short> statusList);

	public List<QiDefectResult> findAllByProductId(String productId);

	public boolean checkDefectResultExist(QiDefectResult defectResult);
	
	public List<QiRecentDefectDto> findAllByMtcModelAndEntryDept(String mtcModel, String entryDept, String productId, Integer recentDefectRange,String processPointId, String productType);
	
	public String findRepairAreaById(long defectResultId);

	public void updateRepairArea(long defectResultId, String updateUser);

	public void updateRepairArea(String plant, String dept, String repairArea, String updateUser, long defectResultId);
	
	public boolean isRepairAreaUsed(String repairAreaName);
	
	public void updateAllByRepairArea(String repairAreaName,String oldRepairAreaName,String updateUser);
	
	List<QiDefectResult> findAllCurrentDefectStatusByProductId(String productId);
	
	public List<QiDefectResult> findAllCurrentDefectStatusByProductIds(List<String> productIds);

	public DefectMapDto findDefectResultByLocalCombinationId(int localDefectCombinationId);
	
	List<QiDefectResult> findAllByExternalSystemDataAndProductId(String externalPartCode, String externalSystemName ,String productId) ;
	
	public QiDefectResult findFirstMatch(QiDefectResult defectResult, String applicationId);
	
	public void updateIncidentIdByDefectResultId(Integer qiIncidentId, String user, String defectResultIdSet, Timestamp updatedTimestamp);
	
	public List<QiDefectResult> findAllByFilter(String searchString,String filterData,int defectDataRange,int searchResultLimit);
	
	public void deleteDefectResult(long defectResultId, String reasonForChange, String correctionRequester, String updateUser);
	
	public List<QiDefectResultDto> findAllBySearchFilter(String searchString,String filterData);
	
	public List<QiDefectResult> findAllByProductIdAndEntryDept(String productId, String entryDept) ;
	
	public List<QiDefectResult> findAllByImageNameAndEntryDept(String productId,String imageName, String entryDept);
	
	public List<QiDefectResult> findAllByAfOnSeqNo(int prodAssSeqNo, int mostRecentAssSeqNo);
	
	public List<QiDefectResult> findAllByProductIdAndTimeStamp(String productId) ;
	
	public List<QiDefectResult> findAllByProductIdAndCurrentDefectStatus(String productId, short defectStatus);
	
	public List<QiDefectResult> findAllByProductIdAndApplicationId(String productId, String applicationId);

	public int updateResponsibleAssociateByDefectId(int defectResultId, String responsibleAssociate, String userId);
	
	public  int updateGdpDefects(String productId,String userId);

	public Long findNotFixedDefectCountByProductId(String productId);

	/**
	 * find all exist year model code in Defect Result table
	 * @return
	 */
	public List<String> findAllYearModel(String productType);
	
	public QiDefectResult createQiDefectResult(QiDefectResult qiDefectResult, QiDefectResult previousQiDefectResult);
	public List<QiMostFrequentDefectsDto> findMostFrequentDefectsByProcessPointEntryScreenDuration(String processPoint, String entryScreen, String entryModel, String entryDept, Date actualTs);
	
	public Long getNextDefectTransactionGrounpId(QiDefectResult qiDefectResult);
	
	public List<QiDefectResult> findAllByGroupTransIdProdType(long transactionId,String prodType);
	
	public List<String> findProductIdsByGroupTransId(long transactionId,String prodType);
	
	public List<String> findProductIdsByGroupTransIdProdType(long transactionId,String prodType, int defectStatus);
	
	public List<String> findOutstandingProductIdsByGroupTransId(long transactionId);
	
	public List<QiDefectResult> findAllByGroupTransId(long transactionId);

	public List<QiDefectResult> findAllByCreateUser(String createUser);
	
	public List<QiDefectResult> findAllByDefectType(String defectType);

	public long findMaxDefectResultId();
	
	public long findDefectTransactionGroupCount(long defectTransactionGroupId);

	List<QiDefectResult> findDuplicateDefects(QiDefectResult defectResult);

	List<QiDefectResult> findAllByProductIdAndPartDefect(QiDefectResult defectResult);

	int updateTrpuDefects(String productId, String userId);

	List<QiDefectResult> findMatchingDefectsByNotFixed(QiDefectResult defectResult);
	
	public List<QiDefectResult> findByProductIdAndDefectType(String defectTypeName, String productId, String defectStatus) ;

	public QiDefectResult createDefect(String productId, String defectTypeName,String processPoint, String createUser, String dept, String div);

	public List<QiDefectResult> checkLETDefectExists(String productId,String defectTypeName);
	
	public QiDefectResult createLETDefect(String productId,String productType, String defectTypeName,String processPoint,String createUser,String writeUpDept, String entryDept, boolean isGDPProcessPoint,boolean isTrpuProcessPoint,boolean isGlobalGDPEnabled);
	
}

