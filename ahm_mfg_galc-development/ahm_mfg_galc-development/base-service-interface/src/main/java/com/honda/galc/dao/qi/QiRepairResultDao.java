package com.honda.galc.dao.qi;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiRepairResultDao Class description</h3>
 * <p>
 * QiRepairResultDao is used to declare the methods required for rapir entry
 * defect screen
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
 *         Nov 23, 2016
 * 
 */
public interface QiRepairResultDao extends IDaoService<QiRepairResult, Long> {

	public List<QiRepairResultDto> findAllRepairEntryDefectsByDefectResultId(long defectResultId);

	public Date findLatestRepairTimestampByRepairId(long repairId);

	public void deleteAllRepairMethodsByRepairId(long repairId);

	public void updateAllRepairMethodStatusById(long repairId, boolean isCompletelyFixed, String userId);
	
	public void updateDefectResultStatusById(long defectResultId, int currDefectStatus, String userId);
	
	public List<QiRepairResultDto> findAllDefectsByProductId(String productId);
	
	public List<QiRepairResult> findAllByFilter(String searchString,String filterData, int defectDataRange ,int searchResultLimit);

	public void updateIncidentIdByDefectResultId(Integer qiIncidentId, String user,String defectResultIdSet, Timestamp updatedTimestamp);
	
	public long findMaxRepairId();
	
	public List<QiRepairResult> findAllByDefectResultId(long defectResultId) ;
	
	public List<QiDefectResultDto> findAllByShiftAndDate(String shift,
			java.sql.Date productionDate, String applicationId, String productType) ;
	
	public QiRepairResult findLatestFixedRepairResult(long defectResultId);	
	
	public QiRepairResult createRepairResult(QiRepairResult qiRepairResult, QiDefectResult previousQiDefectResult);
	
	public List<QiRepairResultDto> findAllDefectsByProductIdEntryDepts(String productId, String entryDepts);
	
	public List<QiRepairResult> createRepairResults(List<QiRepairResult> qiRepairResultList);
	
	public List<QiRepairResultDto> findAllDefectsByProductIds(List<String> productIds);
	
	public void deleteRepairResultByDefectResultId(long defectResultId);

	List<QiRepairResult> findAllByDefectIdAndCurrentStatus(long defectResultId, DefectStatus currentStatus);

	List<QiRepairResult> findFixedByDefectResultId(long defectResultId);

	List<QiRepairResult> findNotFixedByDefectResultId(long defectResultId);

	int findMaxSequenceByDefectResultId(long defectResultId);

	public int findCurrentDefectStatusCount(String productId);
	
	public List<QiRepairResult> findAllByProductIdAndCurrentDefectStatus(String productId, short defectStatus);
	
	public List<QiRepairResult> findAllByProductId(String productId, List<Short> statusList);
	
	public List<QiRepairResultDto> findAllDefectsByProductIdsNoImage(List<String> productIds);

	public void deleteRepairResultByDefectResultIdNPF(long defectResultId,String defectTypeName);
}
