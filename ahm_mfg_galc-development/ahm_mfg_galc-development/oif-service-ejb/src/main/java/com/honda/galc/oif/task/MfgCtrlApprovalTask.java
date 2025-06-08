package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.constant.RevisionType;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.MCRevisionDao;
import com.honda.galc.dao.conf.MCViosMasterMBPNMatrixDataDao;
import com.honda.galc.dao.pdda.ChangeFormDao;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.conf.MCRevision;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.pdda.GenericPddaDaoService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.task.TaskUtils;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;



public class MfgCtrlApprovalTask extends OifTask<Object> implements IEventTaskExecutable{

	private MfgControlMaintenancePropertyBean propertyBean = null;
	private String userId = "";
	private List<Integer> changeForms = null;
	private long previousRevId = -1;
	private String currentChangeForm;
	
	private enum RunningStatus {FINISHED, RUNNING};
	private String platform;
	private StringBuffer mailContent = new StringBuffer();
	private long startTime;
	
	public MfgCtrlApprovalTask(String name) {
		super(name);
		errorsCollector = new OifErrorsCollector(name);
		changeForms = new ArrayList<Integer>();
	}

	public void execute(Object[] args) {
		
		try {
			startTime = System.nanoTime();
			logger.info("Starting MfgCtrlApprovalTask. Start Time: "+System.currentTimeMillis());
			Map<String,String> map  = TaskUtils.unpackExtraArgs(args);	
			platform = getPlatform(map);
			userId = map.get("userId");
			updateRunningStatusProperty(RunningStatus.RUNNING, platform);
			
			propertyBean = PropertyService.getPropertyBean(MfgControlMaintenancePropertyBean.class);
			mailContent.append("<html><body>");
			List<Integer>  sucessChangeForms = new ArrayList<Integer>();
			StringBuffer failedChangeFormsException = new StringBuffer();
			List<ChangeForm> newchangeFormsList = ServiceFactory.getDao(ChangeFormDao.class).findAllByPddaPlatform(map.get("plantCode"), map.get("deptCode"), new BigDecimal(map.get("modelYear")), 
					new BigDecimal(map.get("productionRate")), map.get("line"), map.get("vmc"));
			
			logger.info("Change Form List Size: "+newchangeFormsList.size());
			
				if(validateChangeForms(newchangeFormsList)) {

						List<Integer> processedChangeForms = new ArrayList<Integer>();
						
						for (Integer changeForm : changeForms) {
							logger.info("Processing ChangeForm: "+changeForm);
							try {
								if(!processedChangeForms.contains(changeForm)) {
									currentChangeForm = String.valueOf(changeForm);
									List<Integer> chronoOrderedChangeForms = new ArrayList<Integer>();
									chronoOrderedChangeForms.add(changeForm);
									chronoOrderedChangeForms =  getRecordsToProcess(changeForm, chronoOrderedChangeForms);
									currentChangeForm = chronoOrderedChangeForms.toString();
									 RevisionType rType = ServiceFactory.getService(GenericPddaDaoService.class).getRevisionType(changeForm);
									//perform approval
									logger.info("Performing Approval...");
									MCRevision revision =  performApproval(chronoOrderedChangeForms, rType.getRevType());									
									for (Integer processedChangeForm : chronoOrderedChangeForms) {
										processedChangeForms.add(processedChangeForm);
									} 
									if(previousRevId == -1)
										previousRevId = revision.getId();
									else
										previousRevId = revision.getId()-1;
									
									if(!(previousRevId == revision.getId()) || changeForm == changeForms.get(changeForms.size()-1) ) {
										logger.info("Approval Completed. Updating Rev Status to Approved");
										revision.setStatus(RevisionStatus.APPROVED.getRevStatus());
										ServiceFactory.getDao(MCRevisionDao.class).update(revision);
									}
								}
								sucessChangeForms.add(changeForm);
							} catch (Exception e) {
								logger.error("Exception occured in approval process :: "+getExceptionLoggerPrints(e));
								failedChangeFormsException.append("<i>Exception "+e+" in Approval Process while processing Change Form "+currentChangeForm+"</i><br>");
								setJobStatus(OifRunStatus.FAILURE);
								setFailedCount(sucessChangeForms.size());
							}
							
						}
						if(sucessChangeForms.size() > 0) {
							
							Map<Integer,ChangeForm> changeFormMap = new HashMap<Integer, ChangeForm>();
							for (ChangeForm changeForm : newchangeFormsList) {
								changeFormMap.put(changeForm.getId(), changeForm);
							}
							mailContent.append("<h4>MFG Approval Job completed Succesfully for below changeforms</h4><table  border='1'>");
							mailContent.append("<tr><th>Change Form </th><th>Control Number</th></tr>");
							System.out.println("sucessChangeForms "+sucessChangeForms.toString()+" changeFormMap "+changeFormMap);
							for (Integer changeForm : sucessChangeForms) {
								mailContent.append("<tr><td>"+changeForm+"</td><td>"+changeFormMap.get(changeForm).getControlNo()+"</td></tr>");
							}
							mailContent.append("</table><br><br>");
						}
						mailContent.append(failedChangeFormsException);
				} 
			
		} catch(Exception e) {
			mailContent.append("<h4>Exception "+e+" in Approval Process while processing Change Form "+currentChangeForm+"</h4><br>");
			logger.error(" Exception occured in approval process :: "+getExceptionLoggerPrints(e));
			setJobStatus(OifRunStatus.FAILURE);
		} finally {
			updateRunningStatusProperty(RunningStatus.FINISHED, platform);
			mailContent.append("<br><br><i>job ran by <b>"+userId+"</b> for platform <b>"+platform+"</b></i><br><br>");
			mailContent.append("<i>Time taken by job to complete proccessing :  "+((System.nanoTime()-startTime)/1000000000)+" secs.</i></body></html>");
			errorsCollector.infoMessage(mailContent.toString());
			errorsCollector.sendEmail(userId);
		}
		logger.info("Ending MfgCtrlApprovalTask. End Time: "+System.currentTimeMillis());
	}							

	private String getPlatform(Map<String,String> map) {
		MCViosMasterPlatform platform = new MCViosMasterPlatform();
		platform.setPlantLocCode(map.get("plantCode"));
		platform.setDeptCode(map.get("deptCode"));
		platform.setModelYearDate(new BigDecimal(map.get("modelYear")));
		platform.setProdSchQty(new BigDecimal(map.get("productionRate")));
		platform.setProdAsmLineNo(map.get("line"));
		platform.setVehicleModelCode( map.get("vmc"));
		return platform.getGeneratedId();
	}
	
	private void updateRunningStatusProperty(RunningStatus status, String platform) {
		ComponentStatusId cpIdKd = new ComponentStatusId(componentId, "RUNNING_STATUS{"+platform+"}");
		ComponentStatus cpKdLot = getDao(ComponentStatusDao.class).findByKey(cpIdKd);
		if (cpKdLot == null) {
			cpKdLot = new ComponentStatus();
			cpKdLot.setId(cpIdKd);
		}
		cpKdLot.setChangeUserId(userId);
		cpKdLot.setStatusValue(status.toString());
		getDao(ComponentStatusDao.class).save(cpKdLot);
	}

	
	private List<Integer>  getRecordsToProcess(int changeForm, List<Integer> changeFormsTOProcessFirst) {
			List<Integer> changefm = getUnprocessedChangeForms(changeForm);
			
			if(changefm.size() > 0) {
				for (Integer cf : changefm) {
					changeFormsTOProcessFirst.add(cf);
					if(!changeFormsTOProcessFirst.contains(cf))
						getRecordsToProcess(cf, changeFormsTOProcessFirst);
				}
			}

		return changeFormsTOProcessFirst;
	}

	@Transactional
	private MCRevision performApproval(List<Integer> changeForms, String revType) {

		MCRevision revision = ServiceFactory.getService(GenericPddaDaoService.class).createRevisionForOneClickApproval(changeForms, userId, "Appproval process started on "+new Date(), revType);
		addChildRecords(revision);
		ServiceFactory.getService(GenericPddaDaoService.class).performApproval(revision.getId(),this.userId);
		revision.setStatus(RevisionStatus.BATCH_PENDING.getRevStatus());
		updateMBPNMatrix();
		revision.setDescription("Approval completed on "+new Date());
		ServiceFactory.getDao(MCRevisionDao.class).update(revision);
		return revision;

	}
	
	private void updateMBPNMatrix() {
		List<MCViosMasterMBPNMatrixData> mbpnMasterdataList =
				ServiceFactory.getService(MCViosMasterMBPNMatrixDataDao.class).findAllData(platform, StringUtils.EMPTY);
		
		for (MCViosMasterMBPNMatrixData mcmbpnMasterData : mbpnMasterdataList) {
			ServiceFactory.getDao(MCViosMasterMBPNMatrixDataDao.class).updateMBPNMasterRevision(mcmbpnMasterData, platform);
		}
	}
	protected void addChildRecords(MCRevision revision) {
			//Get Model Year Code Map for populating matrix
		String productType = PropertyService.getPropertyBean(SystemPropertyBean.class).getProductType();
		ServiceFactory.getService(MCRevisionDao.class).setRevisionStatus(revision.getId(), RevisionStatus.BATCH_PENDING);
		Map<String, String> yearCodeMap = ServiceFactory.getService(GenericPddaDaoService.class).getYearDescriptionCodeMap(productType);
		RevisionType.PDDA_STD.getRevType();
		if(revision.getType().equalsIgnoreCase(RevisionType.PDDA_MASS.getRevType()) 
				|| revision.getType().equalsIgnoreCase(RevisionType.PDDA_STD.getRevType())) {
			// Add all unmapped processes in MC_PDDA_PLATFORM_TBX
			List<String> processList = ServiceFactory.getDao(ChangeFormUnitDao.class).findAllUnmappedPddaProcessBy(revision.getId());
			for(String process : processList) {
				ServiceFactory.getService(GenericPddaDaoService.class).addMCPddaPlatformRecord(process, revision.getId(), this.userId);
			}
			ServiceFactory.getService(GenericPddaDaoService.class).createMfgCtrlRecordsForOneClick(revision.getId(), yearCodeMap, this.userId);
			ServiceFactory.getService(MCRevisionDao.class).updateRevisionDescription(revision.getId(), "Added Platform record "+new Date(),RevisionStatus.BATCH_PENDING );
		}
		
	}

	private boolean validateChangeForms(List<ChangeForm> newchangeFormsList) {
		logger.info("ChangeForm validation started...");
		boolean status = true;
		if (newchangeFormsList.size() != 0) {
			Set<String> revSet = new HashSet<String>();
			
			for (ChangeForm changeForm : newchangeFormsList) {
			    RevisionType rType = ServiceFactory.getService(GenericPddaDaoService.class).getRevisionType(changeForm.getId());
				revSet.add(rType.getRevType());
		       if(rType.getRevType().equalsIgnoreCase(RevisionType.PDDA_MASS.getRevType()) || rType.getRevType().equalsIgnoreCase(RevisionType.PDDA_STD.getRevType())) {
		    	   changeForms.add(changeForm.getId());
		       } 
		       
			}
			
			for (String revisionType : revSet) {
				if(revisionType.equalsIgnoreCase(RevisionType.INVALID.getRevType())) {
					//send mail
					status = false;
					mailContent.append("<h4>PDDA Approval is neither mass nor standard</h4>");
					logger.info("PDDA Approval is neither mass nor standard");
					return status;
				}
			}
		}
		logger.info("ChangeForm validation ended. Status: "+status);
		return status;
	}
	
	private List<Integer> getUnprocessedChangeForms(Integer changeForm) {
		List<Integer> unprocessedChangeFormsInt = new ArrayList<Integer>();
			List<String> unprocessedChangeForms = new ArrayList<String>();;
			List<Integer> selectedChangeFormIds = new ArrayList<Integer>();
			selectedChangeFormIds.add(changeForm);
			if(propertyBean.isCheckChronologicalOrder()) {
				unprocessedChangeForms = ServiceFactory.getDao(ChangeFormDao.class).getUnprocessedChangeForms(selectedChangeFormIds);
				if(unprocessedChangeForms!=null && !unprocessedChangeForms.isEmpty()) {
					unprocessedChangeForms = Arrays.asList(StringUtils.join(unprocessedChangeForms, Delimiter.COMMA).split(Delimiter.COMMA));
					
					for (String newChangeForm : unprocessedChangeForms) {
						unprocessedChangeFormsInt.add(Integer.parseInt(newChangeForm)); 
					}
					
				}
			}
		
		return unprocessedChangeFormsInt;
	}

	private String getExceptionLoggerPrints(Exception e) {
		StringBuilder builder = new StringBuilder();
		for (StackTraceElement stackTraceElement : e.getStackTrace()) {
			builder.append(stackTraceElement.toString());
			builder.append(System.getProperty("line.separator"));
		}
		return builder.toString();
	}
	    
}
