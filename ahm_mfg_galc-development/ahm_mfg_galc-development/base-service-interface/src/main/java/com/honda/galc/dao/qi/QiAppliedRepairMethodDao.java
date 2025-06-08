package com.honda.galc.dao.qi;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.entity.qi.QiAppliedRepairMethod;
import com.honda.galc.service.IDaoService;
/**
 * <h3>QiAppliedRepairMethodDao Class description</h3>
 * <p> QiAppliedRepairMethodDao description </p>
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
 * Dec 20, 2016
 *
 *
 */
public interface QiAppliedRepairMethodDao extends IDaoService<QiAppliedRepairMethod,String> {

	public List<QiAppliedRepairMethodDto> findAllCurrentRepairMethods(String qicsStaion);

	public List<QiAppliedRepairMethodDto> findAllRepairMethodsByFilter(String filter, String qicsStaion);
	
	public void updateFixedStatus(Integer currentDefectStatus,long repairId, String userId);
	
	public Integer findCurrentSequence();
	
	public Integer findCurrentSequence(long repairId);

	public List<QiAppliedRepairMethodDto> findAllAppliedRepairMethodDataByRepairId(long repairId);
	
	public void deleteAppliedRepairMethodById(long repairId, Integer repairMethodSeq);
	
	public void replicateRepairResult(QiAppliedRepairMethodDto qiAppliedRepairMethodDto, String partDefectDesc, String repairProcessPointId);
	
	public List<QiAppliedRepairMethod> saveAllRepairMethods(List<QiAppliedRepairMethod> qiAppliedRepairMethodList, String applicationId);
	
	public QiAppliedRepairMethod insertRepairMethod(QiAppliedRepairMethod qiAppliedRepairMethod, String applicationId);
	
	public void updateAppliedRepairMethodSql(Long id, Integer seq, String text, String userId);
	
	public void updateNotCompletelyFixed(String repairIdsString, String userId);
	
	public boolean areAllMethodsNotCompletelyFixed(long repairId);
	
	public List<QiAppliedRepairMethodDto> findCommonAppliedRepairMethod(List<Long> repairIds);

	QiAppliedRepairMethod insertRepairMethod(QiAppliedRepairMethod qiAppliedRepairMethod, String applicationId, Timestamp repairTimestamp);

	public void deleteAppliedRepairMethodByRepairIdNPF(long repairId,String repairMethod);
}
