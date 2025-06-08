/**
 * 
 */
package com.honda.galc.client.qi.repairentry;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.qi.base.AbstractQiDialogController;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.TeamRotationDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairResult;

/**
 * @author vf031824
 *
 */
public abstract class AbstractScrapDialogController  extends AbstractQiDialogController<RepairEntryModel, AbstractScrapDialog> {

	protected String associateId = "";
	/**
	 * @param model
	 * @param view
	 */
	public AbstractScrapDialogController(RepairEntryModel model, AbstractScrapDialog view, String associateId) {
		setModel(model);
		setDialog(view);
		setAssociateId(associateId);
	}
	
	/**
	 * Method to create the scrap service call DataContainer
	 * @param repairResultDto
	 * @param naqScrappedProductList
	 * @return
	 */
	protected DefaultDataContainer createRequestDc(QiRepairResultDto repairResultDto, List<String> naqScrappedProductList) {
		DefaultDataContainer requestDc = new DefaultDataContainer();
		requestDc.put(TagNames.PRODUCT_ID.name(), naqScrappedProductList);
		requestDc.put(TagNames.REASON.name(), repairResultDto.getDefectTypeName());
		requestDc.put(TagNames.APPLICATION_ID.name(), getModel().getCurrentWorkingProcessPointId());
		requestDc.put(TagNames.PROCESS_POINT_ID.name(), getModel().getCurrentWorkingProcessPointId());
		requestDc.put(TagNames.PRODUCT_TYPE.name(), getModel().getProductType());
		//we could use getModel().getUserId() as in the userid set for the defect and actual problem,
		//however this gets the ApplicationContext's userid; the user id should come from the current window's application context
		requestDc.put(TagNames.ASSOCIATE_ID.name(), getAssociateId());
		requestDc.put(TagNames.CURRENT_DATE.name(), getModel().getProductionDate());
		requestDc.put(TagNames.PROCESS_LOCATION.name(), repairResultDto.getInspectionPartLocationName());
		requestDc.put(TagNames.COMMENT.name(), getDialog().getScrapReasonTextArea().getText());
		return requestDc;
	}
	
	public void updateQiRepairResult(QiRepairResultDto repairResultDto){
		QiRepairResult qiRepairResult = null;
		qiRepairResult = getModel().findRepairResultById(repairResultDto.getRepairId());
		qiRepairResult.setCurrentDefectStatus((short) DefectStatus.NON_REPAIRABLE.getId());
		qiRepairResult.setComment(getDialog().getScrapReasonTextArea().getText());
		qiRepairResult.setUpdateUser(getUserId());
		qiRepairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
		Timestamp timestamp = getDao(QiDefectResultDao.class).getDatabaseTimeStamp();
		qiRepairResult.setActualTimestamp(timestamp);
		qiRepairResult.setGroupTimestamp(timestamp);
		
		String applicationId = getModel().getCurrentWorkingProcessPointId();
		ProcessPoint processPoint = getDao(ProcessPointDao.class).findByKey(applicationId);
		if (processPoint != null) {
			GpcsDivision gpcsDivision = getDao(GpcsDivisionDao.class).findByKey(processPoint.getDivisionId());
				
			if (gpcsDivision != null) {
				DailyDepartmentSchedule schedule = getDao(DailyDepartmentScheduleDao.class).findByActualTime(
						gpcsDivision.getGpcsLineNo(), gpcsDivision.getGpcsProcessLocation(), 
						gpcsDivision.getGpcsPlantCode(), timestamp);
				
				if (schedule != null) {
					qiRepairResult.setProductionDate(schedule.getId().getProductionDate());
					qiRepairResult.setShift(schedule.getId().getShift());
					TeamRotation teamRotation= getDao(TeamRotationDao.class).findTeamDetails(
							gpcsDivision, schedule.getId().getShift(), schedule.getId().getProductionDate());
					if(teamRotation != null) {
						qiRepairResult.setTeam(StringUtils.trimToEmpty(teamRotation.getId().getTeam()));
					}
				}
			}
		}
		getModel().updateRepairResult(qiRepairResult);
	}
	
	public void updateQiDefectResult(QiRepairResultDto qiDefectResultDto) {
		QiDefectResult qiDefectResult = null;
		qiDefectResult = getModel().findMainDefectByDefectId(qiDefectResultDto.getDefectResultId());
		qiDefectResult.setCurrentDefectStatus((short) DefectStatus.NON_REPAIRABLE.getId());
		qiDefectResult.setComment(getDialog().getScrapReasonTextArea().getText());
		qiDefectResult.setUpdateUser(getUserId());
		getModel().updateDefectResult(qiDefectResult);
		ProductSearchResult.updateDefectResult(qiDefectResult, 0);
	}
	
	public void updateCacheforTrainingMode(QiRepairResultDto qiDefectResultDto) {
		qiDefectResultDto.setCurrentDefectStatus((short)DefectStatus.NON_REPAIRABLE.getId());	
		qiDefectResultDto.setRepairId(0);
		Timestamp timestamp = getDao(QiDefectResultDao.class).getDatabaseTimeStamp();
		qiDefectResultDto.setActualTimestamp(timestamp);
		ProductSearchResult.updateDefectsProcessingMap(qiDefectResultDto);
		getModel().getProductModel().getCachedDefectsForTraingMode().put(qiDefectResultDto, QiConstant.UPDATE_PARENT_DEFECT_FOR_TM);
	}
	
	public QiRepairResultDto createAcutalProblem(QiRepairResultDto currentDefect) {
		QiRepairResultDto addedChildDefectDto = getNewAcutalProblem(currentDefect);
		ProductSearchResult.addChildDefect(addedChildDefectDto);
		return addedChildDefectDto;
	}
	
	public QiRepairResultDto getNewAcutalProblem(QiRepairResultDto currentDefect) {
		QiDefectResult mainDefect = getModel().findMainDefectByDefectId(currentDefect.getDefectResultId());
		// call to persist QiRepairResult
		QiRepairResult qiRepairResult = new QiRepairResult(mainDefect); 

		String site = getModel().getCurrentWorkingProcessPoint().getSiteName();
		String plant = getModel().getCurrentWorkingProcessPoint().getPlantName();
		short lineNo = getModel().getEntryProdLineNo();

		qiRepairResult.setEntrySiteName(site);
		qiRepairResult.setEntryPlantName(plant);
		qiRepairResult.setEntryProdLineNo(lineNo);
		qiRepairResult.setEntryDept(getModel().getCurrentWorkingEntryDept());
		qiRepairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
		qiRepairResult.setTerminalName(getModel().getTerminalName());
		qiRepairResult.setCreateUser(getModel().getUserId());
		qiRepairResult.setActualProblemSeq((short) (currentDefect.getChildRepairResultList() != null
				? currentDefect.getChildRepairResultList().size() + 1 : 1));
		qiRepairResult = getModel().createRepairResult(qiRepairResult);

		QiRepairResultDto addedChildDefectDto = new QiRepairResultDto(mainDefect, qiRepairResult.getRepairId());
		addedChildDefectDto.setDivisionName(currentDefect.getDivisionName());
		addedChildDefectDto.setActualTimestamp(new Timestamp(qiRepairResult.getActualTimestamp().getTime()));
		return addedChildDefectDto;
	}
	
	public boolean isChildDefect(QiRepairResultDto defectResult) {
		return defectResult.getRepairId() != 0;
	}

	public String getAssociateId() {
		return associateId == null ? null : associateId.trim().toUpperCase();
	}

	public void setAssociateId(String scrapUserId) {
		this.associateId = scrapUserId;
	}


}
