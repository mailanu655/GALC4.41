package com.honda.galc.oif.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiDepartmentDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiPddaResponsibilityDao;
import com.honda.galc.dao.qi.QiPddaResponsibleLoadTriggerDao;
import com.honda.galc.dao.qi.QiPddaStagingDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.dto.qi.QiPlantDepartmentDto;
import com.honda.galc.entity.enumtype.PddaStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiPddaResponsibility;
import com.honda.galc.entity.qi.QiPddaResponsibleLoadTrigger;
import com.honda.galc.entity.qi.QiPddaResponsibleLoadTriggerId;
import com.honda.galc.entity.qi.QiPddaStaging;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifServiceEMailHandler;
import com.honda.galc.util.AuditLoggerUtil;

public class PddaResponsibilityTask extends OifTask<Object> implements IEventTaskExecutable {

	public PddaResponsibilityTask(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {
		try {
			Timestamp now = getCurrentTime(true); 
			getLogger().info("Started processing Pdda Responsibility Task");
			processPddaResponsibilityRecords();
			updateLastProcessTimestamp(now);
			sendEmailReport();
			getLogger().info("Finished processing Pdda Responsibility Task");
		} catch (Exception e) {
			logException(e, "Error processing Pdda Responsibility Task");
		}
	}

	private Logger getLogger() {
		return Logger.getLogger(componentId);
	}

	/** This method is used to fetch records from vios.PALSQ1 table and update,insert or delete the same in QiPddaResponsibility.
	 */
	private void processPddaResponsibilityRecords() {
		try {
			QiPddaResponsibilityDao	qiPddaResponsibilityDao= ServiceFactory.getDao(QiPddaResponsibilityDao.class);
			QiPddaStagingDao qiPddaStagingDao= ServiceFactory.getDao(QiPddaStagingDao.class);

			// retrieve updated records in the VIOS.PALSQ1 table 
			List<QiPddaStaging> qiPddaStagingList = qiPddaStagingDao.findAllByDataColByQics();
			for (QiPddaStaging qiPddaStaging : qiPddaStagingList) {
				QiPddaResponsibility qiPddaResponsibility = setQiPddaResponsibilityData(qiPddaStaging);
				if (qiPddaResponsibility == null) {
					continue;
				}
				String status=qiPddaStaging.getStatus();
				String prodUnitNo=qiPddaStaging.getProdUnitIdNo();
				QiPddaResponsibility existingPddaResp=qiPddaResponsibilityDao.findByCompanyPlantDeptModelYearVMCUnitNoPddaLine(qiPddaResponsibility);
				switch(PddaStatus.getType(status.trim())){
				case PROD : case ADD_PROD :
					if(existingPddaResp==null){
						qiPddaResponsibilityDao.insert(qiPddaResponsibility);
						updateStagingTable(qiPddaStaging);
					}
					else{
						performUpdate(existingPddaResp,qiPddaResponsibility,prodUnitNo);
						updateStagingTable(qiPddaStaging);
					}
					break;
				case MOVE_PROD: case MOVE_PURG: 
					if(existingPddaResp!=null){
						performUpdate(existingPddaResp,qiPddaResponsibility,prodUnitNo);
						updateStagingTable(qiPddaStaging);
					}
					else
						getLogger().info("No row exists to update."+qiPddaResponsibility.getPclToQicsSeqKey());
					break;
				case DELT_PURG: case PROD_PURG:
					if(existingPddaResp!=null){
						performDelete(existingPddaResp);
						updateStagingTable(qiPddaStaging);
					}
					else
						getLogger().info("No row exists to delete."+qiPddaResponsibility.getPclToQicsSeqKey());
					break;
				}

			}
		} catch (Exception e) {
			logException(e, "Error processing Pdda Responsibility records");
		}
	}

	/** This method is used to set pdda staging data into QiPddaResponsibility.
	 * @param qiPddaStaging
	 * @return QiPddaResponsibility
	 */
	private QiPddaResponsibility setQiPddaResponsibilityData(QiPddaStaging qiPddaStaging) {
		try {
			QiPddaResponsibility qipddaresponsibility=new QiPddaResponsibility();
			qipddaresponsibility.setPclToQicsSeqKey(qiPddaStaging.getPclToQicsSeq());
			qipddaresponsibility.setResponsibleCompany(qiPddaStaging.getCompanyCode());
			qipddaresponsibility.setShortPlantCode(qiPddaStaging.getPlantLocationCode());
			qipddaresponsibility.setResponsibleLevel1(qiPddaStaging.getTeamNumber());
			qipddaresponsibility.setResponsibleLevel1Desc(qiPddaStaging.getTeamDescription());
			qipddaresponsibility.setProdSchQty(qiPddaStaging.getProdSchQuantity());
			qipddaresponsibility.setModelYear(qiPddaStaging.getModelYearDate());
			qipddaresponsibility.setVehicleModelCode(qiPddaStaging.getVehicleModelCode());
			qipddaresponsibility.setPddaLine(qiPddaStaging.getProdAsmLineNo());
			qipddaresponsibility.setProcessNumber(qiPddaStaging.getAsmProcNo());
			qipddaresponsibility.setUnitNumber(qiPddaStaging.getUnitNumber());
			qipddaresponsibility.setUnitRank(qiPddaStaging.getUnitRank());
			qipddaresponsibility.setBasePartNumber(qiPddaStaging.getBasePartNumber());
			qipddaresponsibility.setUnitDescription(qiPddaStaging.getUnitDescription());
			qipddaresponsibility.setProcessName(qiPddaStaging.getAsmProcName());
			qipddaresponsibility.setProductType("FRAME");
			qipddaresponsibility.setRowType("P");
			qipddaresponsibility.setCreateUser("PDDA");
			QiPlantDepartmentDto qiPlantDepartmentDto = ServiceFactory.getDao(QiDepartmentDao.class).findByPddaData(
							qiPddaStaging.getPlantLocationCode(),
							qiPddaStaging.getProdAsmLineNo(),
							qiPddaStaging.getDepartmentCode(),
							qiPddaStaging.getTeamNumber());
			
			if (qiPlantDepartmentDto != null && qiPlantDepartmentDto.getPlant() != null && qiPlantDepartmentDto.getSite() != null
					&& qiPlantDepartmentDto.getEntryPlant() != null && qiPlantDepartmentDto.getEntrySite() != null 
					&& qiPlantDepartmentDto.getDepartment() != null && qiPlantDepartmentDto.getPddaDept() != null) {
				qipddaresponsibility.setResponsiblePlant(qiPlantDepartmentDto.getPlant());
				qipddaresponsibility.setResponsibleSite(qiPlantDepartmentDto.getSite());
				qipddaresponsibility.setEntryPlant(qiPlantDepartmentDto.getEntryPlant());
				qipddaresponsibility.setEntrySite(qiPlantDepartmentDto.getEntrySite());
				qipddaresponsibility.setResponsibleDept(qiPlantDepartmentDto.getDepartment());
				qipddaresponsibility.setPddaDeptCode(qiPlantDepartmentDto.getPddaDept());
			} else {
				getLogger().info("No responsible plant/dept found for PDDA PLANT_LOC_CODE:" + qiPddaStaging.getPlantLocationCode() +
						", PROD_ASM_LINE_NO:" + qiPddaStaging.getProdAsmLineNo() + ",DEPT_CODE:" + qiPddaStaging.getDepartmentCode() +
						",TEAM_NO:" + qiPddaStaging.getTeamNumber());
				return null; //can't process
			}
			return qipddaresponsibility;
		} catch (Exception e) {
			logException(e, "Error setting Pdda Responsibility data");
			return null;
		}
	}

	/** This method is to perform delete operation and audit log.
	 * @param existingQiPddaResponsibility
	 */
	private void performDelete(QiPddaResponsibility existingQiPddaResponsibility) {
		try {
			QiPddaResponsibilityDao	qiPddaResponsibilityDao= ServiceFactory.getDao(QiPddaResponsibilityDao.class);
			QiPddaResponsibleLoadTriggerDao	qiPddaResponsibleLoadTriggerDao= ServiceFactory.getDao(QiPddaResponsibleLoadTriggerDao.class);
			QiLocalDefectCombinationDao	qiLocalDefectCombinationDao= ServiceFactory.getDao(QiLocalDefectCombinationDao.class);
			StringBuilder updateReason=new StringBuilder();
			updateReason.append(" PDDA row type Deleted responsibility for [").append(existingQiPddaResponsibility.getPddaResponsibilityId())
			.append("] that is ").append(existingQiPddaResponsibility.getResponsibleCompany()).append("-"+existingQiPddaResponsibility.getResponsiblePlant()).append("-").append(
					existingQiPddaResponsibility.getProductType()).append("-").append(existingQiPddaResponsibility.getPddaLine()  ).append(
							"� reason given[Daily Job � PCL to QICS Key:").append(existingQiPddaResponsibility.getPclToQicsSeqKey() ).append("]");
			String primaryKeyValue=existingQiPddaResponsibility.getResponsibleCompany()+" "+ existingQiPddaResponsibility.getEntryPlant()+" "+existingQiPddaResponsibility.getProductType()+" "+
					existingQiPddaResponsibility.getRowType()
			+" "+existingQiPddaResponsibility.getResponsiblePlant()+" "+ existingQiPddaResponsibility.getResponsibleDept()+" "+ existingQiPddaResponsibility.getVehicleModelCode()
			+ " "+existingQiPddaResponsibility.getModelYear()+" "+ existingQiPddaResponsibility.getProcessNumber()+" "+existingQiPddaResponsibility.getUnitNumber()
			+" "+existingQiPddaResponsibility.getResponsibleLevel1()+" "+ existingQiPddaResponsibility.getResponsibleLevel2()+" "+ existingQiPddaResponsibility.getPddaLine();
			qiPddaResponsibilityDao.remove(existingQiPddaResponsibility);

			// Delete references in Local attribute table
			List<QiLocalDefectCombination> qilocaldefectList=qiLocalDefectCombinationDao.findAllByPddaResponsibilityId(existingQiPddaResponsibility.getPddaResponsibilityId());
			for(QiLocalDefectCombination local:qilocaldefectList){
				local.setPddaResponsibilityId(null);
				qiLocalDefectCombinationDao.update(local)	;	    		
			}
			// Delete references in Resp Load trigger
			QiPddaResponsibleLoadTriggerId qiPddaresponsibleLoadTriggerId=new QiPddaResponsibleLoadTriggerId();
			qiPddaresponsibleLoadTriggerId.setPclToQicsSeqKey(existingQiPddaResponsibility.getPclToQicsSeqKey());
			qiPddaresponsibleLoadTriggerId.setPddaResponsibilityId(existingQiPddaResponsibility.getPddaResponsibilityId());
			QiPddaResponsibleLoadTrigger qiPddaRespLoadTrigger=qiPddaResponsibleLoadTriggerDao.findByKey(qiPddaresponsibleLoadTriggerId);
			if(qiPddaRespLoadTrigger!=null)
				qiPddaResponsibleLoadTriggerDao.remove(qiPddaRespLoadTrigger);	    		
			// perform audit log
			AuditLoggerUtil.logAuditInfo(existingQiPddaResponsibility, null,updateReason.toString(), "Loading PDDA", primaryKeyValue, "PDDA");
		} catch (Exception e) {
			logException(e, "Error performing Pdda Responsibility delete");
		}
	}

	/** This method is to perform update operation and audit log.
	 * @param existingPddaRespToUpdate
	 * @param qipddaresponsibility
	 * @param prodUnitNo
	 */
	private void performUpdate(QiPddaResponsibility existingPddaRespToUpdate,QiPddaResponsibility qipddaresponsibility,String prodUnitNo) {
		
		if (StringUtils.trim(prodUnitNo).length() > ProductNumberDef.VIN.getLength()) {
			return; //invalid VIN, skip it
		}
		try {
			QiPddaResponsibilityDao	qiPddaResponsibilityDao= ServiceFactory.getDao(QiPddaResponsibilityDao.class);
			QiPddaResponsibility cloned=(QiPddaResponsibility) existingPddaRespToUpdate.deepCopy();
			if(!(existingPddaRespToUpdate.getResponsibleLevel1().equalsIgnoreCase(qipddaresponsibility.getResponsibleLevel1())
					&& existingPddaRespToUpdate.getBasePartNumber().equalsIgnoreCase(qipddaresponsibility.getBasePartNumber()))){
				QiPddaResponsibleLoadTriggerDao qiPddaResponsibleLoadTriggerDao=ServiceFactory.getDao(QiPddaResponsibleLoadTriggerDao.class);
				QiPddaResponsibleLoadTriggerId qiPddaresponsibleLoadTriggerId=new QiPddaResponsibleLoadTriggerId();
				qiPddaresponsibleLoadTriggerId.setPclToQicsSeqKey(existingPddaRespToUpdate.getPclToQicsSeqKey());
				qiPddaresponsibleLoadTriggerId.setPddaResponsibilityId(existingPddaRespToUpdate.getPddaResponsibilityId());	
				// check if trigger exists
				QiPddaResponsibleLoadTrigger qiPddaResponsibleLoadTrigger=qiPddaResponsibleLoadTriggerDao.findByKey(qiPddaresponsibleLoadTriggerId);
				if(qiPddaResponsibleLoadTrigger==null){
					insertQiPddaResponsibleLoadTrigger(existingPddaRespToUpdate,qiPddaResponsibleLoadTriggerDao,qiPddaresponsibleLoadTriggerId);
				}
				else{
					return;
				}
			}
			updateQiPddaResponsibility(existingPddaRespToUpdate,qipddaresponsibility, qiPddaResponsibilityDao);
			peformAudit(existingPddaRespToUpdate, qipddaresponsibility, cloned);
			updateLocalAttributeByPddaResponsibilityId(existingPddaRespToUpdate,qipddaresponsibility);
		} catch (Exception e) {
			logException(e, "Error performing Pdda Responsibility update");
		}
	}



	/** This method is to update pdda staging table once transaction is successful.
	 * @param pddaStaging
	 */
	private void updateStagingTable(QiPddaStaging pddaStaging) {
		try {
			QiPddaStagingDao dao=ServiceFactory.getDao(QiPddaStagingDao.class);
			pddaStaging.setDataColByQics("Y");
			dao.update(pddaStaging);
		} catch (Exception e) {
			logException(e, "Error updating Pdda Staging table");
		}
	}

	/** This method is to send email report for failed transactions.
	 */
	private void sendEmailReport() {
		try {
			QiPddaStagingDao qiPddaStagingDao= ServiceFactory.getDao(QiPddaStagingDao.class);
			int dataCollectedErrorsCount=qiPddaStagingDao.findAllByDataColByQics().size();
			QiPddaResponsibleLoadTriggerDao qiPddaResponsibleLoadTriggerDao=ServiceFactory.getDao(QiPddaResponsibleLoadTriggerDao.class);
			int confirmTriggersCount=qiPddaResponsibleLoadTriggerDao.findAllByAdminConfirmedFix().size();
			OifServiceEMailHandler emailHandler = new OifServiceEMailHandler(getName());
			StringBuilder emailMessage =new StringBuilder();
			if(dataCollectedErrorsCount>0){
				emailMessage.append(" PDDA to QICS unprocessed data count :"+dataCollectedErrorsCount +".");
				emailMessage.append("\n");
				emailMessage.append(" Source: Responsibilty Load Data Collected Errors");
				emailMessage.append("\n");
			}
			if(confirmTriggersCount>0){
				emailMessage.append(" Please Confirm PDDA to QICS Load Triggers for "+confirmTriggersCount+ " records. ");
				emailMessage.append("\n");
				emailMessage.append(" Source: Responsibility Load Unconfirmed Triggers ");
			}
			if(dataCollectedErrorsCount>0 || confirmTriggersCount>0){
				emailHandler.delivery("PDDA responsibility to QICS",emailMessage.toString());
			}
		} catch (Exception e) {
			logException(e, "Error sending Pdda Responsibility email report");
		}
	}

	/**
	 * @param existingPddaRespToUpdate
	 * @param qipddaresponsibility
	 * @param cloned
	 */
	private void peformAudit(QiPddaResponsibility existingPddaRespToUpdate,QiPddaResponsibility qipddaresponsibility,
			QiPddaResponsibility cloned) {
		try {
			// perform Audit
			StringBuilder updateReason=new StringBuilder();
			updateReason.append(" PDDA row type Updated responsibility for [").append(existingPddaRespToUpdate.getPddaResponsibilityId()).append("] that is ").append(
					existingPddaRespToUpdate.getResponsibleCompany()).append("-").append(existingPddaRespToUpdate.getResponsiblePlant()).append("-").append(
							existingPddaRespToUpdate.getProductType()).append("-").append(existingPddaRespToUpdate.getPddaLine()  ).append(
									" responsible [").append(existingPddaRespToUpdate.getResponsibleLevel1()).append("-"+existingPddaRespToUpdate.getResponsibleLevel1Desc()).append("-"
											).append(existingPddaRespToUpdate.getProcessNumber()).append("-"+existingPddaRespToUpdate.getProcessName()).append("-").append(
													existingPddaRespToUpdate.getUnitDescription()).append("-").append(existingPddaRespToUpdate.getUnitRank()).append("-").append(existingPddaRespToUpdate.getBasePartNumber()+"-").append(
															existingPddaRespToUpdate.getProdSchQty()).append("]" ).append(
																	" � reason given[Daily Job � PCL to QICS Key:").append(qipddaresponsibility.getPclToQicsSeqKey() ).append("]");
			String primaryKeyValue=existingPddaRespToUpdate.getResponsibleCompany()+" "+ existingPddaRespToUpdate.getEntryPlant()+" "+existingPddaRespToUpdate.getProductType()+" "+qipddaresponsibility.getRowType()
			+" "+existingPddaRespToUpdate.getResponsiblePlant()+" "+ existingPddaRespToUpdate.getResponsibleDept()+" "+ existingPddaRespToUpdate.getVehicleModelCode()
			+ " "+existingPddaRespToUpdate.getModelYear()+" "+ existingPddaRespToUpdate.getProcessNumber()+" "+existingPddaRespToUpdate.getUnitNumber()
			+" "+existingPddaRespToUpdate.getResponsibleLevel1()+" "+ existingPddaRespToUpdate.getResponsibleLevel2()+" "+ existingPddaRespToUpdate.getPddaLine();
			AuditLoggerUtil.logAuditInfo(cloned, existingPddaRespToUpdate, updateReason.toString(), "Loading PDDA",primaryKeyValue,"PDDA");
		} catch (Exception e) {
			logException(e, "Error performing Pdda Responsibility audit");
		}
	}

	/**
	 * @param existingPddaRespToUpdate
	 * @param qipddaresponsibility
	 * @param qiPddaResponsibilityDao
	 */
	private void updateQiPddaResponsibility(QiPddaResponsibility existingPddaRespToUpdate,QiPddaResponsibility qipddaresponsibility,
			QiPddaResponsibilityDao qiPddaResponsibilityDao) {
		try {
			existingPddaRespToUpdate.setBasePartNumber(qipddaresponsibility.getBasePartNumber());
			existingPddaRespToUpdate.setResponsibleLevel1(qipddaresponsibility.getResponsibleLevel1());
			existingPddaRespToUpdate.setResponsibleLevel1Desc(qipddaresponsibility.getResponsibleLevel1Desc());
			existingPddaRespToUpdate.setProcessNumber(qipddaresponsibility.getProcessNumber());
			existingPddaRespToUpdate.setProcessName(qipddaresponsibility.getProcessName());
			existingPddaRespToUpdate.setProdSchQty(qipddaresponsibility.getProdSchQty());
			existingPddaRespToUpdate.setUnitDescription(qipddaresponsibility.getUnitDescription());
			existingPddaRespToUpdate.setUnitRank(qipddaresponsibility.getUnitRank());
			existingPddaRespToUpdate.setPclToQicsSeqKey(qipddaresponsibility.getPclToQicsSeqKey());
			existingPddaRespToUpdate.setUpdateUser("PDDA");
			existingPddaRespToUpdate.setResponsibleLevel2(qipddaresponsibility.getResponsibleLevel2());
			// update data
			qiPddaResponsibilityDao.update(existingPddaRespToUpdate);
		} catch (Exception e) {
			logException(e, "Error updating Pdda Responsibility");
		}
	}

	/**
	 * @param existingPddaRespToUpdate
	 * @param qiPddaResponsibleLoadTriggerDao
	 * @param qiPddaresponsibleLoadTriggerId
	 */
	private void insertQiPddaResponsibleLoadTrigger(QiPddaResponsibility existingPddaRespToUpdate,QiPddaResponsibleLoadTriggerDao qiPddaResponsibleLoadTriggerDao,
			QiPddaResponsibleLoadTriggerId qiPddaresponsibleLoadTriggerId) {
		try {
			QiPddaResponsibleLoadTrigger qiPddaresponsibleLoadTrigger=new QiPddaResponsibleLoadTrigger();
			qiPddaresponsibleLoadTrigger.setId(qiPddaresponsibleLoadTriggerId);
			qiPddaresponsibleLoadTrigger.setAdminConfirmedFix((short)0);
			qiPddaresponsibleLoadTrigger.setPrevBasePartNo(existingPddaRespToUpdate.getBasePartNumber());
			qiPddaresponsibleLoadTrigger.setPrevProcessName(existingPddaRespToUpdate.getProcessName());
			qiPddaresponsibleLoadTrigger.setPrevProcessNumber(existingPddaRespToUpdate.getProcessNumber());
			qiPddaresponsibleLoadTrigger.setPrevRespLevel1(existingPddaRespToUpdate.getResponsibleLevel1());
			qiPddaresponsibleLoadTrigger.setPrevRespLevel1Desc(existingPddaRespToUpdate.getResponsibleLevel1Desc());
			qiPddaresponsibleLoadTrigger.setPrevUnitDesc(existingPddaRespToUpdate.getUnitDescription());
			qiPddaresponsibleLoadTrigger.setPrevPdaaLine(existingPddaRespToUpdate.getPddaLine());
			qiPddaresponsibleLoadTrigger.setCreateUser("PDDA");
			// insert trigger
			qiPddaResponsibleLoadTriggerDao.insert(qiPddaresponsibleLoadTrigger);
		} catch (Exception e) {
			logException(e, "Error inserting Pdda Responsibility load trigger");
		}
	}

	private void updateLocalAttributeByPddaResponsibilityId(QiPddaResponsibility existingPddaRespToUpdate,QiPddaResponsibility qipddaresponsibility) {
		try {
			QiLocalDefectCombinationDao dao=ServiceFactory.getDao(QiLocalDefectCombinationDao.class);
			QiResponsibleLevelDao respLevelDao=ServiceFactory.getDao(QiResponsibleLevelDao.class);
			List<QiLocalDefectCombination> localDefectCombList=dao.findAllByPddaResponsibilityId(existingPddaRespToUpdate.getPddaResponsibilityId());
			QiResponsibleLevel responsibleLevel=respLevelDao.findBySitePlantDepartmentAndLevelName(qipddaresponsibility.getResponsibleSite(), qipddaresponsibility.getResponsiblePlant(),
					qipddaresponsibility.getResponsibleDept(), qipddaresponsibility.getResponsibleLevel1());
			for(QiLocalDefectCombination localDefectComb:localDefectCombList){
				localDefectComb.setResponsibleLevelId(responsibleLevel.getResponsibleLevelId());
				dao.update(localDefectComb);
			}
		} catch (Exception e) {
			logException(e, "Error updating Local Attribute by Pdda Responsibility");
		}
	}

	private void logException(final Exception e, final String message) {
		if (e instanceof TaskException) {
			throw (TaskException) e;
		}
		getLogger().error(e, message);
		throw new TaskException(message, e);
	}
}
