package com.honda.galc.client.qi.repairentry;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.data.QiCommonDefectResult;
import com.honda.galc.client.qi.base.AbstractQiDefectProcessModel;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qi.QiAppliedRepairMethod;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.service.property.PropertyService;

import javafx.scene.control.Toggle;

public class BulkAddRepairMethodDialogController extends AddRepairMethodDialogController {

	private QiRepairResultDto selectedChildDefect;

	public BulkAddRepairMethodDialogController(RepairEntryModel entryModel, AddRepairMethodDialog addRepairMethodDialog,
			QiRepairResultDto qiRepairResultDto, List<QiRepairResultDto> allSelectedDefects, Date sessionTimestamp,
			AbstractRepairEntryController<AbstractQiDefectProcessModel, AbstractQiProcessView<?,?>> repairEntryController) {
		super(entryModel, addRepairMethodDialog, qiRepairResultDto, allSelectedDefects, sessionTimestamp, repairEntryController);
		this.setAllSelectedDefects(allSelectedDefects);
	}

	@Override
	protected void addRepairMethod() {
		if(getDialog().getToggleGroup().getSelectedToggle().isSelected()){
			if(((LoggedRadioButton)getDialog().getToggleGroup().getSelectedToggle()).getText().equalsIgnoreCase("Yes")){
				for (Toggle toggle : getDialog().getToggleGroup().getToggles()) {
					((LoggedRadioButton)toggle).setDisable(true);
				}
				getDialog().getCurrentRepairMethodDataPane().getTable().setDisable(true);
				getDialog().getSpinner().setDisable(true);
				getDialog().getCommentArea().setDisable(true);
				getDialog().getMethodFilterTextField().setDisable(true);
			}	
		}
		
		QiAppliedRepairMethodDto appliedRepairMethodDto = new QiAppliedRepairMethodDto();
		boolean isMainDefect = getOriginalSelectedRepairResultDto().getRepairId() == 0;
		List<QiRepairResultDto> actualProblems = getAllSelectedDefects();
		if(actualProblems == null)  {
			actualProblems= new ArrayList<QiRepairResultDto>();
		}
		if(actualProblems.isEmpty())  {
			actualProblems.add(getQiRepairResultDto());
		}
		if(isMainDefect)  {
			actualProblems = new ArrayList<>();
			List<QiRepairResultDto> repairedMainDefects = getParentController().markDefectAsActualProblem(false);
			if(repairedMainDefects != null && !repairedMainDefects.isEmpty())  {
				for(QiRepairResultDto thisDefect : repairedMainDefects)  {
					if(thisDefect != null && thisDefect.getChildRepairResultList() != null && !thisDefect.getChildRepairResultList().isEmpty())  {
						actualProblems.add(thisDefect.getChildRepairResultList().get(0));
					}
				}
			}
		}
		Timestamp repairTimestamp = getModel().getDatabaseTimestamp();
		if(actualProblems != null && actualProblems.size() > 1) {
			List<QiCommonDefectResult> repairedCommonDefects = new ArrayList<>();
			//For Multi-Select Repair
			for(QiRepairResultDto selectedDefect : actualProblems) {
				QiCommonDefectResult commonDefect = new QiCommonDefectResult(selectedDefect);
				if(!isMainDefect || !repairedCommonDefects.contains(commonDefect))  {
					appliedRepairMethodDto = addRepairMethod(selectedDefect, repairTimestamp);
					if(isMainDefect)  {
						if(appliedRepairMethodDto.getIsCompletelyFixed() == 1)  commonDefect.setCurrentDefectStatus(((Integer)DefectStatus.FIXED.getId()).shortValue());
						repairedCommonDefects.add(commonDefect);
					}
				}
			}
		} else if(actualProblems != null && actualProblems.size() > 0){
			appliedRepairMethodDto = addRepairMethod(actualProblems.get(0), repairTimestamp);
		}
		
		getDialog().getHistoryRepairMethodDataPane().getTable().getItems().clear();
		getDialog().getHistoryRepairMethodDataPane().setData(getModel().getAppliedRepairMethodHistoryData(getDialog().getRepairIds(), getQiRepairResultDto()));

		getDialog().getAddButton().setDisable(true);				
		getDialog().getCommentArea().clear();
		getDialog().getSpinner().getValueFactory().setValue(0);
		getDialog().getCurrentRepairMethodDataPane().getTable().getSelectionModel().clearSelection();
	}
	
	/**
	 * This method is to one single selected defect, including insert a record to Qi_Applied_Repair_Method table 
	 * and Update child and/or main defect status from Not Fixed to Fixed
	 * @param selectedDefect
	 * @param appliedRepairMethodDto
	 */
	private QiAppliedRepairMethodDto addRepairMethod(QiRepairResultDto selectedDefect, Timestamp commonRepairTimestamp) {
		QiAppliedRepairMethodDto appliedRepairMethodDto = new QiAppliedRepairMethodDto();
		List<QiRepairResultDto> repairResults = new ArrayList<QiRepairResultDto>();
		QiCommonDefectResult selectedCommonDefect = new QiCommonDefectResult(selectedDefect);
		if(isChildDefect(selectedDefect)) {
			setSelectChildDefect(selectedDefect);
			if(getProductModel().isSingleProduct())  {
				repairResults.add(ProductSearchResult.getDefectResultByDefectId(selectedDefect.getDefectResultId(), selectedDefect.getProductId()));
			}
			else  {
				QiRepairResultDto repairedParentDefect = ProductSearchResult.getDefectResultByDefectId(selectedDefect.getDefectResultId(), selectedDefect.getProductId());
				repairResults = ProductSearchResult.getCommonDefects(repairedParentDefect);
			}
		} else {
			if(getProductModel().isSingleProduct())  {
				repairResults.add(selectedDefect);
			}
			else  {
				repairResults = ProductSearchResult.getCommonDefects(selectedDefect);				
			}
		}
		//note that repairResults here is actually a list of main defects
		Iterator<QiRepairResultDto> iter = repairResults.iterator();
		Timestamp repairTimestamp = getModel().getDatabaseTimestamp();
		if(commonRepairTimestamp != null)  repairTimestamp = commonRepairTimestamp;
		while(iter.hasNext()) {
			QiRepairResultDto currentDefect = iter.next();
			setQiRepairResultDto(currentDefect);
			QiRepairResultDto childDefect = ProductSearchResult.getChildDefect(currentDefect, selectedCommonDefect);
			if(childDefect == null)  continue;
			setSelectChildDefect(childDefect);  //using a class level global variable

			appliedRepairMethodDto = prepareAppliedDataToRender();
			repairMethod = appliedRepairMethodDto.getRepairMethod();
			if(null == AbstractRepairEntryView.getParentCachedDefectList() || AbstractRepairEntryView.getParentCachedDefectList().isEmpty()){
				if(!getModel().getProductModel().isTrainingMode()) {
					saveAppliedRepairMethod(appliedRepairMethodDto, currentDefect.getDefectDesc(), repairTimestamp);
				} else {
					if(((LoggedRadioButton)getDialog().getToggleGroup().getSelectedToggle()).getText().equalsIgnoreCase("Yes")){
						//Update Child defect status to Fixed 
						QiRepairResultDto dto = getSelectedChildDefect();  //using a class level global variable
						dto.setCurrentDefectStatus((short)DefectStatus.FIXED.getId());
						Timestamp timestamp = getDao(QiDefectResultDao.class).getDatabaseTimeStamp();
						dto.setRepairTimestamp(timestamp);
						ProductSearchResult.updateDefectsProcessingMap(dto);
						getModel().getProductModel().getCachedDefectsForTraingMode().put(dto, QiConstant.UPDATE_CHILD_DEFECT_FOR_TM);
												
						if(isAllActualDefectFixed()) { 
							//If all child defects (from database and cached defects for training mode) are fixed, then update the parent defect to Fixed
							currentDefect.setCurrentDefectStatus((short)DefectStatus.FIXED.getId());
							currentDefect.setRepairTimestamp(timestamp);
							ProductSearchResult.updateDefectsProcessingMap(currentDefect);
							getModel().getProductModel().getCachedDefectsForTraingMode().put(currentDefect, QiConstant.UPDATE_PARENT_DEFECT_FOR_TM);						
						}
						
					}
				}
			}
		}
		return appliedRepairMethodDto;
	}
	
	/**
	 * this method displays the below data when the Add Repair Method button is clicked
	 * @param repairTimestamp 
	 * 
	 * @return
	 */
	@Override
	protected QiAppliedRepairMethodDto prepareAppliedDataToRender() {
		boolean selectedRadioValue=false;
		if(((LoggedRadioButton)getDialog().getToggleGroup().getSelectedToggle()).getText().equalsIgnoreCase("Yes")){
			selectedRadioValue=true;
		}
		QiAppliedRepairMethodDto appliedRepairMethodDto = new QiAppliedRepairMethodDto();
		appliedRepairMethodDto.setRepairId(getSelectedChildDefect()== null ? 0 : getSelectedChildDefect().getRepairId());
		appliedRepairMethodDto.setApplicationId(getModel().getCurrentWorkingProcessPointId());
		appliedRepairMethodDto.setCreateUser(getModel().getUserId());
		appliedRepairMethodDto.setUpdateUser("");
		appliedRepairMethodDto.setRepairMethod(getDialog().getCurrentRepairMethodDataPane().getTable().getSelectionModel().getSelectedItem().getRepairMethod());
		String entryDepartment = getQiRepairResultDto().getEntryDept() != null ? getQiRepairResultDto().getEntryDept() : "";
		appliedRepairMethodDto.setEntryDept(entryDepartment);		
		appliedRepairMethodDto.setRepairTime(getDialog().getSpinner().getValueFactory().getValue());
		appliedRepairMethodDto.setDefectStatus(selectedRadioValue);
		appliedRepairMethodDto.setIsCompletelyFixed(selectedRadioValue ? 1 : 0);	
		String commentText = getDialog().getCommentArea().getText();
		commentText = commentText != null ? commentText : StringUtils.EMPTY;
		appliedRepairMethodDto.setComment(commentText);
		
		return appliedRepairMethodDto;
	}

	/**
	 * This method will insert {@link QiAppliedRepairMethod} object.
	 * 
	 * @param appliedRepairMethodDto
	 */
	@Override
	protected void saveAppliedRepairMethod(QiAppliedRepairMethodDto appliedRepairMethodDto, String partDefectDesc,Timestamp repairTimesamp) {
		getQiRepairResultDto().setRepairId(appliedRepairMethodDto.getRepairId());
		QiAppliedRepairMethod qiAppliedRepairMethod = populateAppliedMethodEntity(appliedRepairMethodDto);
		getQiRepairResultDto().setRepairId(0);
		Integer count = getModel().findMaxSequenceValue(qiAppliedRepairMethod.getId().getRepairId());
		count = count == null ? 0 : count;
		qiAppliedRepairMethod.getId().setRepairMethodSeq(count + 1);
		QiAppliedRepairMethod appliedRepairMethod = getModel().insertRepairMethod(qiAppliedRepairMethod, repairTimesamp);

		//add repair ID when applying repair method to multiple selected defects
		long repairId = appliedRepairMethod.getId().getRepairId();
		if (!getDialog().getRepairIds().contains(repairId)) {
			getDialog().getRepairIds().add(repairId);
		}

		setDirty(true);
		appliedRepairMethodDto.setRepairTimestamp(appliedRepairMethod.getRepairTimestamp());
		appliedRepairMethodDto.setRepairMethodSeq(appliedRepairMethod.getId().getRepairMethodSeq());

		updateFixedStatusForChildEntries();
		updateFixedStatusForMainDefect();
		try {
			String message = getModel().addUnitToConfiguredRepairArea(getQiRepairResultDto().getDefectResultId());
			if(!StringUtils.isEmpty(message)) {
				EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.DIALOG_INFO));
			}
		} catch (Exception e) {
			handleException("Exception occured in saveAppliedRepairMethod() method", "Failed to add unit to configured Repair Area", e);
		}

		//replicate repair result to GAL222TBX for completely fixed only
		if (PropertyService.getPropertyBean(QiPropertyBean.class, getQiRepairResultDto().getApplicationId()).isReplicateDefectRepairResult()) {
			if (appliedRepairMethodDto.getIsCompletelyFixed() == 1) {
				getModel().replicateRepairResult(appliedRepairMethodDto, partDefectDesc, getQiRepairResultDto().getApplicationId());
			}
		}
	}

	/**
	 * this method refreshes the Repair Entry screen when the user selects yes
	 */
	@Override
	protected void updateFixedStatusForMainDefect() {
		if(((LoggedRadioButton)getDialog().getToggleGroup().getSelectedToggle()).getText().equalsIgnoreCase("Yes") && isAllActualDefectFixed()){
			getDialog().getModel().updateMainDefectResultStatus(getQiRepairResultDto().getDefectResultId(), DefectStatus.FIXED.getId());
			ProductSearchResult.updateDefectRepairStatusFixed(getQiRepairResultDto());

			getDialog().setParentDefectFixed(true);
		}
	}

	/**
	 * this method refreshes the Repair Entry screen for the child entries when the user selects yes
	 */
	@Override
	protected void updateFixedStatusForChildEntries() {
		if(((LoggedRadioButton)getDialog().getToggleGroup().getSelectedToggle()).getText().equalsIgnoreCase("Yes")){
			getDialog().getModel().updateFixedStatus(DefectStatus.FIXED.getId(),getSelectedChildDefect().getRepairId());
			ProductSearchResult.updateChildDefectRepairStatusFixed(getSelectedChildDefect(), getSelectedChildDefect().getRepairId());
			getDialog().setCompletelyFixed(true);
		}
	}

	private boolean isChildDefect(QiRepairResultDto repairResult) {
		return repairResult.getRepairId() != 0;
	}

	public QiRepairResultDto getSelectedChildDefect() {
		return this.selectedChildDefect;
	}

	public void setSelectChildDefect(QiRepairResultDto selectedChildDefect) {
		this.selectedChildDefect = selectedChildDefect;
	}

	public void setSelectedChildDefect(QiRepairResultDto selectedChildDefect) {
		this.selectedChildDefect = selectedChildDefect;
	}		
}
