package com.honda.galc.client.teamleader.qi.defectResult;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.QiDialogController;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.qi.QiDefectIqsScoreDao;
import com.honda.galc.dao.qi.QiReasonForChangeDetailDao;
import com.honda.galc.dao.qi.QiResponsibilityMappingDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dao.qi.QiResultReasonForChangeDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiReportable;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.qi.QiDefectIqsScore;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiRepairResultHist;
import com.honda.galc.entity.qi.QiResponsibilityMapping;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiResultReasonForChange;
import com.honda.galc.entity.qi.QiResultReasonForChangeId;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * 
 * <h3>DefectDataReviewController Class description</h3>
 * <p>
 * DefectDataReviewController class is used to update values for gathered defects and RepairResult in the Defect Results table and  in the Repair table.
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
 * 
 */

public class DefectDataReviewController extends QiDialogController<DefectResultMaintModel, DefectDataReviewDialog>{
	
	private String typeOfChange;
	private static final String NO_PROBLEM_FOUND = "NO PROBLEM FOUND";
	
	public DefectDataReviewController(DefectResultMaintModel model,DefectDataReviewDialog defectDataReviewDialog,String typeOfChange) {
		super();
		setModel(model);
		setDialog(defectDataReviewDialog);
		this.typeOfChange = typeOfChange;
	}

	/**
	 * This method is used to perform the actions like 'Apply Changes' and 'Cancel'
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(getDialog().getCancelButton()))
			cancelBtnAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getChangeButton()))
			updateResults(actionEvent);
	}

	/**
	 * This method is used to update records in DefectResult and 
	 * @param actionEvent
	 */
	private void updateResults(ActionEvent actionEvent) {
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChange())){
			getDialog().getReasonForChangeErrorMessage().setStyle("-fx-text-background-color: red;");
			getDialog().getReasonForChangeErrorMessage().setText("Please enter Reason for Change.");
			return;
		}else if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getApproverTextField().getControl())){
			getDialog().getReasonForChangeErrorMessage().setStyle("-fx-text-background-color: red;");
			getDialog().getReasonForChangeErrorMessage().setText("Please enter Correction Requester Name.");
			return;
		}
		try {
			if(typeOfChange.equals(QiConstant.DEFECT_TYPE)){
				//create defect result history
				List<QiDefectResultHist> defectResultHistList = new ArrayList<QiDefectResultHist>();
				for (QiDefectResult defectResult : getDialog().getCurrentDefectResultTablePane().getTable().getItems()) {
					QiDefectResultHist history=new QiDefectResultHist(defectResult);
					history.setChangeUser(getUserId());
					defectResultHistList.add(history);
				}
				getModel().createDefectResultHist(defectResultHistList);
				 
				//update defect result
				List<QiDefectResult> updatedDefectResultList = new ArrayList<QiDefectResult>();
				List<QiRepairResult> noProblemFoundRepairResultList = new ArrayList<QiRepairResult>();
				
				for (QiDefectResult updatedDefectResult : getDialog().getUpdatedResultListFromPopUp()) {
					if (isFrameQicsEngineSource()) {
						String altRespLevel = getAltEngineSource(updatedDefectResult);
						if (altRespLevel != null) updatedDefectResult.setResponsibleLevel1(altRespLevel);
					}
					updatedDefectResult.setCorrectionRequestBy(getDialog().getApproverTextField().getControl().getText().toString());
					updatedDefectResult.setReasonForChange(getDialog().getReasonForChange().getText());
					updatedDefectResult.setUpdateUser(getUserId());
					
					//apply No Problem Found
					if (getDialog().isNoProblemFound()) {
						short maxActualProblemSeq = getModel().getMaxActualProblemSeq(updatedDefectResult.getDefectResultId());
					
						// call to populate and save QiRepairResult
						QiRepairResult qiRepairResult = new QiRepairResult(updatedDefectResult); 
						qiRepairResult.setCreateUser(getModel().getUserId());
						qiRepairResult.setActualProblemSeq(++maxActualProblemSeq);
						qiRepairResult.setDefectTypeName(NO_PROBLEM_FOUND);
						qiRepairResult.setDefectTypeName2("");
						qiRepairResult.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
						qiRepairResult.setProductionDate(new java.util.Date()); //use Calendar date for maintenance client
						qiRepairResult.setApplicationId(getModel().getApplicationId());
						noProblemFoundRepairResultList.add(qiRepairResult);
					}
					
					updatedDefectResultList.add(updatedDefectResult);
				}
				getModel().updateAllDefectResult(updatedDefectResultList);
				updateIqsScore(updatedDefectResultList);
				if (noProblemFoundRepairResultList.size() > 0) {
					getModel().updateAllRepairResult(noProblemFoundRepairResultList);
				}
				
				//create reason for change detail for defect result
				createReasonForChangeDetailResult(updatedDefectResultList, null);
				
				//update the legacy records if replicate property is turned on
				if (PropertyService.getPropertyBean(QiPropertyBean.class, getModel().getApplicationId()).isReplicateDefectRepairResult()) {
					replicateDefectResult(updatedDefectResultList);
				}
				
			}else if(typeOfChange.equals(QiConstant.REPAIR_TYPE)){
				List<QiRepairResultHist> repairResultHistList = new ArrayList<QiRepairResultHist>();
				for (QiRepairResult repairResult : getDialog().getCurrentRepairResultTablePane().getTable().getItems()) {
					QiRepairResultHist history=new QiRepairResultHist(repairResult);
					history.setChangeUser(getUserId());
					repairResultHistList.add(history);
				}
				getModel().createRepairResultHist(repairResultHistList);
				
				//update repair result
				List<QiRepairResult> updatedRepairResultList = new ArrayList<QiRepairResult>();
				for (QiRepairResult updatedRepairResult : getDialog().getUpdatedRepairResultList()) {
					if (isFrameQicsEngineSource()) {
						String altRespLevel = getAltEngineSource(updatedRepairResult);
						if (altRespLevel != null) updatedRepairResult.setResponsibleLevel1(altRespLevel);
					}
					updatedRepairResult.setCorrectionRequestBy(getDialog().getApproverTextField().getControl().getText().toString());
					updatedRepairResult.setReasonForChange(getDialog().getReasonForChange().getText());
					updatedRepairResult.setUpdateUser(getUserId());
					
					//apply No Problem Found
					if (getDialog().isNoProblemFound()) {
					
						//update selected repair result
						updatedRepairResult.setDefectTypeName(NO_PROBLEM_FOUND);
						updatedRepairResult.setDefectTypeName2("");
						updatedRepairResult.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
						updatedRepairResult.setApplicationId(getModel().getApplicationId());
						updatedRepairResult.setReportable((short)QiReportable.NON_REPORTABLE_BY_NO_PROBLEM_FOUND.getId());
						getModel().updateRepairResult(updatedRepairResult);
						
						List<QiRepairResult> repairResultList = getModel().findAllRepairResultByDefectResultId(updatedRepairResult.getDefectResultId());
						boolean areAllRepairResultsFixed = true;
						boolean areAllRepairResultsNoProblemFound = true;
						for (QiRepairResult rr : repairResultList) {
							if (rr.getCurrentDefectStatus() != DefectStatus.FIXED.getId()) {
								areAllRepairResultsFixed = false;
								break;
							}
							if (!rr.getDefectTypeName().equals(NO_PROBLEM_FOUND)) {
								areAllRepairResultsNoProblemFound = false;
								break;
							}
						}
						
						if (areAllRepairResultsFixed) {
							QiDefectResult mainDefect =  getModel().findByDefectResultId(updatedRepairResult.getDefectResultId());
							mainDefect.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
							mainDefect.setUpdateUser(getModel().getUserId());
							if (areAllRepairResultsNoProblemFound) {
								mainDefect.setReportable((short)QiReportable.NON_REPORTABLE_BY_NO_PROBLEM_FOUND.getId());
							}
							getModel().updateDefectResult(mainDefect); //update defect result of repair result
						}
					} else { //non No Problem Found 
						updatedRepairResultList.add(updatedRepairResult);
					}
				}
				if (updatedRepairResultList.size() > 0) {
					getModel().updateAllRepairResult(updatedRepairResultList);
				}
				
				//create reason for change detail for repair result
				createReasonForChangeDetailResult(null, updatedRepairResultList);
			}
			LoggedButton changeBtn = getDialog().getChangeButton();
			Stage stage= (Stage) changeBtn.getScene().getWindow();
			stage.close();
			getDialog().setUserOperationMessage("Updated Successfully");
			
		} catch (Exception e) {
			handleException("An error occurred at updateResults method ", "Unable to update the DefectResult",  e);
		}
	}	
	
	/**
	 * Method to update legacy table when replication flag is on for defect entry
	 * 
	 * @param savedQiDefectResultList
	 */
	public void replicateDefectResult(List<QiDefectResult> savedQiDefectResultList) {
		List<DefectResult> defectResults = new ArrayList<DefectResult>();
		for (QiDefectResult qiDefectResult : savedQiDefectResultList) {
			DefectResult defectResult = getDao(DefectResultDao.class).findByQiDefectId(qiDefectResult.getDefectResultId());
			if (defectResult != null) {
				defectResult.setResponsibleDept(qiDefectResult.getResponsibleDept());
				defectResult.setResponsibleZone(qiDefectResult.getResponsibleLevel1());
				defectResult.setOutstandingFlag(DefectStatus.getOldOutstandingFlag(qiDefectResult.getOriginalDefectStatus()));
				defectResult.setDefectStatus(DefectStatus.getOldDefectStatus(qiDefectResult.getCurrentDefectStatus()));
				defectResults.add(defectResult);
			}			
		}
		if (defectResults.size() > 0) {
			getDao(DefectResultDao.class).updateAll(defectResults);
		}
	}

	private void updateIqsScore(List<QiDefectResult> defectResultList) {
		List<QiDefectIqsScore> iqsScoreList = new ArrayList<QiDefectIqsScore>();
		for(QiDefectResult defectResult : defectResultList) {
			if(defectResult.getIqsScore() > 0) {
				iqsScoreList.add(new QiDefectIqsScore(defectResult));
			}
		}
		if(!iqsScoreList.isEmpty()) {
			ServiceFactory.getDao(QiDefectIqsScoreDao.class).saveAll(iqsScoreList);
		}
	}
	
	private void createReasonForChangeDetailResult(List<QiDefectResult> defectResultList, List<QiRepairResult> repairResultList) {
		
		String detail = getDialog().getSelectedReasonForChangeDetail();
	
		if (StringUtils.isBlank(detail)) {
			return;
		}
		
		int detailId = ServiceFactory.getDao(QiReasonForChangeDetailDao.class).findBySitePlantDeptCategoryDetail(
				getModel().findSiteName(), getModel().getPlant(), getDialog().getSelectedReasonForChangeDept(), 
				getDialog().getSelectedReasonForChangeCategory(), detail);
		
		if (detailId == -1) {
			return;
		}
		
		List<QiResultReasonForChange> qiResultReasonForChangeList = new ArrayList<QiResultReasonForChange>();
		
		if (defectResultList != null) {
			for (QiDefectResult defectResult : defectResultList) {
				QiResultReasonForChangeId qiResultReasonForChangeId = new QiResultReasonForChangeId();
				QiResultReasonForChange qiResultReasonForChange = new QiResultReasonForChange();
				qiResultReasonForChangeId.setDefectRepairId(defectResult.getDefectResultId());
				qiResultReasonForChangeId.setDetailId(detailId);
				qiResultReasonForChangeId.setDefectResult(true);
				qiResultReasonForChange.setId(qiResultReasonForChangeId);
				qiResultReasonForChange.setCreateUser(getModel().getUserId());
				qiResultReasonForChangeList.add(qiResultReasonForChange);
			}
		} else if (repairResultList != null) {
			for (QiRepairResult repairResult : repairResultList) {
				QiResultReasonForChangeId qiResultReasonForChangeId = new QiResultReasonForChangeId();
				QiResultReasonForChange qiResultReasonForChange = new QiResultReasonForChange();
				qiResultReasonForChangeId.setDefectRepairId(repairResult.getRepairId());
				qiResultReasonForChangeId.setDetailId(detailId);
				qiResultReasonForChangeId.setDefectResult(false);
				qiResultReasonForChange.setId(qiResultReasonForChangeId);
				qiResultReasonForChange.setCreateUser(getModel().getUserId());
				qiResultReasonForChangeList.add(qiResultReasonForChange);
			}
		}
		if (!qiResultReasonForChangeList.isEmpty()) {
			ServiceFactory.getDao(QiResultReasonForChangeDao.class).saveAll(qiResultReasonForChangeList);
		}
	}
	
	@Override
	public void initListeners() {
		
	}
	/**
	 * This method is used to perform the cancel operation
	 * @param actionEvent
	 */
	private void cancelBtnAction(ActionEvent actionEvent) {
		LoggedButton cancelButton = getDialog().getCancelButton();
		try {
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action.", "Failed to perform cancel action.", e);
		}finally{
			actionEvent.consume();
		}

	}
	
	private boolean isFrameQicsEngineSource() {
		return getModel().getApplicationContext().getApplicationPropertyBean().isQicsEngineSource() &&
				getModel().getApplicationContext().getApplicationPropertyBean().getProductType().equals(ProductType.FRAME.toString());
	}
	
	private String getAltEngineSource(QiRepairResult defectResult) {
		List<QiResponsibilityMapping> listRespLevel = ServiceFactory.getDao(QiResponsibilityMappingDao.class).findAll();
		if (listRespLevel.isEmpty()) return null;
		QiResponsibleLevel respLevel = ServiceFactory.getDao(QiResponsibleLevelDao.class).
				findBySitePlantDepartmentAndLevelName(
						defectResult.getResponsibleSite(),
						defectResult.getResponsiblePlant(),
						defectResult.getResponsibleDept(),
						defectResult.getResponsibleLevel1());
		if (respLevel == null) return null;
		String frameId = defectResult.getProductId();
		Frame vehicle = ServiceFactory.getDao(FrameDao.class).findByKey(frameId);
		if (vehicle.getEngineSerialNo() == null || vehicle.getEngineSerialNo().isEmpty()) return null;
		Engine engine = ServiceFactory.getDao(EngineDao.class).findByKey(vehicle.getEngineSerialNo());
		if (engine == null) return null;
		for (QiResponsibilityMapping each : listRespLevel) {
			if (respLevel.getResponsibleLevelId().equals(each.getDefaultRespLevel()) &&
					each.getPCode().equals(engine.getPlantCode())) {
				QiResponsibleLevel altRespLevel = ServiceFactory.getDao(QiResponsibleLevelDao.class).findByResponsibleLevelId(each.getAlternateDefault());
				if (altRespLevel == null) break;
				return (altRespLevel.getResponsibleLevelName());
			}
		}
		return null;
	}
	
	private String getAltEngineSource(QiDefectResult defectResult) {
		List<QiResponsibilityMapping> listRespLevel = ServiceFactory.getDao(QiResponsibilityMappingDao.class).findAll();
		if (listRespLevel.isEmpty()) return null;
		QiResponsibleLevel respLevel = ServiceFactory.getDao(QiResponsibleLevelDao.class).
				findBySitePlantDepartmentAndLevelName(
						defectResult.getResponsibleSite(),
						defectResult.getResponsiblePlant(),
						defectResult.getResponsibleDept(),
						defectResult.getResponsibleLevel1());
		if (respLevel == null) return null;
		String frameId = defectResult.getProductId();
		Frame vehicle = ServiceFactory.getDao(FrameDao.class).findByKey(frameId);
		if (vehicle.getEngineSerialNo() == null || vehicle.getEngineSerialNo().isEmpty()) return null;
		Engine engine = ServiceFactory.getDao(EngineDao.class).findByKey(vehicle.getEngineSerialNo());
		if (engine == null) return null;
		for (QiResponsibilityMapping each : listRespLevel) {
			if (respLevel.getResponsibleLevelId().equals(each.getDefaultRespLevel()) &&
					each.getPCode().equals(engine.getPlantCode())) {
				QiResponsibleLevel altRespLevel = ServiceFactory.getDao(QiResponsibleLevelDao.class).findByResponsibleLevelId(each.getAlternateDefault());
				if (altRespLevel == null) break;
				return (altRespLevel.getResponsibleLevelName());
			}
		}
		return null;
	}
}
