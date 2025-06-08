package com.honda.galc.client.qi.repairentry;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiDefectProcessModel;
import com.honda.galc.client.qi.base.AbstractQiDialogController;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qi.QiAppliedRepairMethod;
import com.honda.galc.entity.qi.QiAppliedRepairMethodId;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiExternalSystemDefectIdMap;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.service.property.PropertyService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * <h3>AddRepairMethodDialogController Class description</h3>
 * <p>
 * AddRepairMethodDialogController description
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
 *         Dec 20, 2016
 *
 *
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class AddRepairMethodDialogController extends AbstractQiDialogController<RepairEntryModel, AddRepairMethodDialog> {
	protected String repairMethod;

	//qiRepairResultDto is being used as a global variable and it's reference is being changed when repair methods are added
	//so, we will preserve the the original in originalSelectedRepairResultDto
	private QiRepairResultDto qiRepairResultDto;
	private QiRepairResultDto originalSelectedRepairResultDto; //original selected item on repair entry screen
	private QiAppliedRepairMethodDto qiAppliedRepairMethodDto; 
	private List<QiRepairResultDto> allSelectedDefects; //For Multi-Select Repairs
	private final Date sessionTimestamp;
	private volatile boolean isDirty = false;
	protected ProductModel productModel;
	private AbstractRepairEntryController<AbstractQiDefectProcessModel, AbstractQiProcessView<?,?>> parentController;
	
	public AddRepairMethodDialogController(RepairEntryModel entryModel, AddRepairMethodDialog addRepairMethodDialog,QiRepairResultDto newQiRepairResultDto, List<QiRepairResultDto> allSelectedDefects, Date sessionTimestamp,
			AbstractRepairEntryController<AbstractQiDefectProcessModel, AbstractQiProcessView<?,?>> repairEntryController) {
		super();
		this.sessionTimestamp = sessionTimestamp;
		this.setQiRepairResultDto(newQiRepairResultDto);
		//save reference to the original selected item
		this.setOriginalSelectedRepairResultDto(newQiRepairResultDto);
		this.setAllSelectedDefects(allSelectedDefects);
		this.parentController = repairEntryController;
		setModel(entryModel);
		setDialog(addRepairMethodDialog);
		EventBusUtil.register(this);
	}
	@Override 
	public void close() {
		super.close();
		EventBusUtil.unregister(this);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if (getDialog().getDeleteButton().getText().equalsIgnoreCase(loggedButton.getText())) {
				deleteSelectedRepairMethodRow(actionEvent);
			} else if (getDialog().getAddButton().getText().equalsIgnoreCase(loggedButton.getText())) {
				//check current tracking status again
				if (getModel().isPreviousLineInvalid()) {
					publishProductPreviousLineInvalidEvent();
					return;
				}
				addRepairMethod();
			} else if (getDialog().getReturnToRepairScreenBtn().getText().equalsIgnoreCase(loggedButton.getText())) {
				returnToRepairScreen();
			}else if (getDialog().getReturnToHomeScreenBtn().getText().equalsIgnoreCase(loggedButton.getText())) {
				returnToHomeScreenBtnAction();
			}else if (getDialog().getClearMethodFilterTxtBtn().getText().equalsIgnoreCase(loggedButton.getText())) {
				clearMethodFilterTxt();
			} else if (getDialog().getSaveButton().getText().equalsIgnoreCase(loggedButton.getText())) {
				saveComment();
			}
		} 
	}

	private void saveComment() {
		QiAppliedRepairMethodDto selectedItem = getDialog().getHistoryRepairMethodDataPane().getTable().getSelectionModel().getSelectedItem();
		String comment = getDialog().getCommentArea().getText();
		String updateUser = ClientMainFx.getInstance().getAccessControlManager().getUserName().toUpperCase();
		Long repairId;
		Integer repairMethodSeq;

		if (getDialog().getRepairIds().size() == 0) { //single
			repairId = selectedItem.getRepairId();
			repairMethodSeq = selectedItem.getRepairMethodSeq();
			getModel().updateAppliedRepairMethodSql(repairId, repairMethodSeq, comment, updateUser);
			getDialog().getHistoryRepairMethodDataPane().getTable().getItems().clear();
			getDialog().getHistoryRepairMethodDataPane().setData(getModel().getAppliedRepairMethodHistoryData(repairId, qiRepairResultDto)); 
		} else { //bulk
			List<Long> repairIdList = selectedItem.getRepairIdList();
			List<Integer> repairMethodSeqList = selectedItem.getRepairMethodSeqList();
			for (int i = 0; i < repairIdList.size(); i++) {
				repairId = repairIdList.get(i);
				repairMethodSeq = repairMethodSeqList.get(i);
				getModel().updateAppliedRepairMethodSql(repairId, repairMethodSeq, comment, updateUser);
			}
			getDialog().getHistoryRepairMethodDataPane().getTable().getItems().clear();
			getDialog().getHistoryRepairMethodDataPane().setData(getModel().getAppliedRepairMethodHistoryData(getDialog().getRepairIds(), qiRepairResultDto)); 
		}
	}

	/**
	 * 
	 */
	private void findRepairMethodsByFilter() {
		getDialog().findFilterData(StringUtils.trim(getDialog().getMethodFilterTextField().getText()));
	}

	/**
	 * This method will insert {@link QiAppliedRepairMethod} object.
	 * 
	 * @param appliedRepairMethodDto
	 */
	protected void saveAppliedRepairMethod(QiAppliedRepairMethodDto appliedRepairMethodDto, String partDefectDesc, Timestamp repairTimesamp) {
		
		QiProgressBar qiProgressBar = null;
		try {
			qiProgressBar = QiProgressBar.getInstance("Adding Repair Method.", "Adding Repair Method.",
					getModel().getProductId(),getDialog().getStage(),true);	
			qiProgressBar.showMe();
			QiAppliedRepairMethod qiAppliedRepairMethod = populateAppliedMethodEntity(appliedRepairMethodDto);
			
			Integer count = getModel().findMaxSequenceValue(qiAppliedRepairMethod.getId().getRepairId());
			count = count == null ? 0 : count;
			qiAppliedRepairMethod.getId().setRepairMethodSeq(count + 1);
			QiAppliedRepairMethod appliedRepairMethod = getModel().insertRepairMethod(qiAppliedRepairMethod, repairTimesamp);
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
		finally {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
		
		try {
			getModel().updateDefectStatus(qiRepairResultDto.getDefectResultId());
			QiExternalSystemDefectIdMap extDefectMap = getModel()
					.findExternalSystemDefectIdMap(qiRepairResultDto.getDefectResultId());
			if (extDefectMap != null && extDefectMap.getId() != null && !extDefectMap.isExtSysRepairReqd())  {
				getModel().callExternalSystemService(qiRepairResultDto.getDefectResultId(), qiRepairResultDto.getRepairId());
			}
		} catch (Exception e) {
			handleException("Exception trying to check external system info", "Unable to call lot control service", e);
		}	
	}
	
	/**
	 * This method will act as a cancel event.
	 */
	private void returnToRepairScreen(){
		Stage stage = (Stage) getDialog().getReturnToRepairScreenBtn().getScene().getWindow();
		stage.close();
	}

	protected void addRepairMethod() {
		if(getDialog().getToggleGroup().getSelectedToggle().isSelected()){
			if(((LoggedRadioButton)getDialog().getToggleGroup().getSelectedToggle()).getText().equalsIgnoreCase("Yes") 
					|| getDialog().isNoProblemFound()==true){
				for (Toggle toggle : getDialog().getToggleGroup().getToggles()) {
					((LoggedRadioButton)toggle).setDisable(true);
				}
				
				if (getDialog().isNoProblemFound() == false) { 
					getDialog().getCurrentRepairMethodDataPane().getTable().setDisable(true);
					getDialog().getSpinner().setDisable(true);
					getDialog().getCommentArea().setDisable(true);
					getDialog().getMethodFilterTextField().setDisable(true);
				} else { //right click on context menu
					getDialog().getCurrentRepairMethodDataPane().getTable().setDisable(false);
					getDialog().getSpinner().setDisable(false);
					getDialog().getCommentArea().setDisable(false);
					getDialog().getMethodFilterTextField().setDisable(false);
				}
				
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
			//For Multi-Select Repair
			for(QiRepairResultDto selectedDefect : actualProblems) {
				this.setQiRepairResultDto(selectedDefect);
				appliedRepairMethodDto = prepareAppliedDataToRender();
				repairMethod = appliedRepairMethodDto.getRepairMethod();
				if(null == AbstractRepairEntryView.getParentCachedDefectList() || AbstractRepairEntryView.getParentCachedDefectList().isEmpty()){
					saveAppliedRepairMethod(appliedRepairMethodDto, getQiRepairResultDto().getDefectDesc(), repairTimestamp);
				}
			}
		}else if (actualProblems != null && actualProblems.size() > 0){//For single repair method
			this.setQiRepairResultDto(actualProblems.get(0));			
			appliedRepairMethodDto = prepareAppliedDataToRender();
			repairMethod = appliedRepairMethodDto.getRepairMethod();
			if(null == AbstractRepairEntryView.getParentCachedDefectList() || AbstractRepairEntryView.getParentCachedDefectList().isEmpty()){
				saveAppliedRepairMethod(appliedRepairMethodDto, getQiRepairResultDto().getDefectDesc(), repairTimestamp);
			}
		}
		getDialog().getHistoryRepairMethodDataPane().getTable().getItems().add(0, appliedRepairMethodDto);
		getDialog().getAddButton().setDisable(true);				
		getDialog().getCommentArea().clear();
		getDialog().getSpinner().getValueFactory().setValue(0);
		getDialog().getCurrentRepairMethodDataPane().getTable().getSelectionModel().clearSelection();
	}

	/**
	 * this method refreshes the Repair Entry screen for the child entries when the user selects yes
	 */
	protected void updateFixedStatusForChildEntries() {
		if(((LoggedRadioButton)getDialog().getToggleGroup().getSelectedToggle()).getText().equalsIgnoreCase("Yes")){
			getDialog().getModel().updateFixedStatus(DefectStatus.FIXED.getId(),getQiRepairResultDto().getRepairId());
			getDialog().setCompletelyFixed(true);

		}
	}
	
	/**
	 * this method refreshes the Repair Entry screen when the user selects yes
	 */
	protected void updateFixedStatusForMainDefect() {
		if(((LoggedRadioButton)getDialog().getToggleGroup().getSelectedToggle()).getText().equalsIgnoreCase("Yes") && isAllActualDefectFixed()){
		   getDialog().getModel().updateMainDefectResultStatus(getQiRepairResultDto().getDefectResultId(), DefectStatus.FIXED.getId());
			QiDefectResult qiDefectResult = getModel().findDefectResultById(getQiRepairResultDto().getDefectResultId());
			getDialog().setParentDefectFixed(true);
		}
	}
	
	/**
	 * Method will check if all the actual defects are fixed.
	 * 
	 * @return isAllActualDefectFixed
	 */
	protected boolean isAllActualDefectFixed() {
		List<QiRepairResultDto> actualDefectList =	getModel().findAllRepairEntryDefectsByDefectId(getQiRepairResultDto().getDefectResultId());
		if(this.getModel().getProductModel().isTrainingMode()) {
			//Update child defect with cached defects
			actualDefectList= updateChildrenDefectResultsForTrainingMode(getQiRepairResultDto().getDefectResultId(), actualDefectList);
		}
		boolean isAllActualDefectFixed = true;
		for (QiRepairResultDto qiRepairResultDto : actualDefectList) {
			if (qiRepairResultDto.getCurrentDefectStatus() != DefectStatus.FIXED.getId()) {
				isAllActualDefectFixed = false;
				break;
			}
		}
		return isAllActualDefectFixed;
	}
	
	private List<QiRepairResultDto> updateChildrenDefectResultsForTrainingMode(
			long defectResultId, List<QiRepairResultDto> childrenDefectResults){
		Map<QiRepairResultDto, Integer> cachedDefectsForTM = this.getModel().getProductModel().getCachedDefectsForTraingMode();
		if(cachedDefectsForTM != null && cachedDefectsForTM.size() > 0) {	
			for(Map.Entry<QiRepairResultDto, Integer> entry : cachedDefectsForTM.entrySet()) {
				QiRepairResultDto cachedDefect = (QiRepairResultDto) entry.getKey();
				Integer type = entry.getValue();
				if((defectResultId == cachedDefect.getDefectResultId()) && 
						(type == QiConstant.ADD_CHILD_DEFECT_FOR_TM || type == QiConstant.DELETE_CHILD_DEFECT_FOR_TM || type == QiConstant.UPDATE_CHILD_DEFECT_FOR_TM)) {
					if(type == QiConstant.ADD_CHILD_DEFECT_FOR_TM)
						childrenDefectResults.add(cachedDefect);
					else if(type == QiConstant.DELETE_CHILD_DEFECT_FOR_TM) {
						if(childrenDefectResults != null && childrenDefectResults.size() > 0) {
							for(QiRepairResultDto defect : childrenDefectResults) {
								if(defect.getRepairId() == cachedDefect.getRepairId()) {
									childrenDefectResults.remove(defect);	
									break;
								}
							}
						}
					}else if (type == QiConstant.UPDATE_CHILD_DEFECT_FOR_TM) {
						if(childrenDefectResults != null && childrenDefectResults.size() > 0) {
							for(QiRepairResultDto defect : childrenDefectResults) {
								if(defect.getRepairId() == cachedDefect.getRepairId()) {
									childrenDefectResults.remove(defect);
									childrenDefectResults.add(cachedDefect);
									break;
								}
							}
						}
					}
				}
				
			}
		}
		
		return childrenDefectResults;
	}

	/**
	 * this method allows the user to delete the selected repair method
	 * 
	 * @param event
	 */

	private void deleteSelectedRepairMethodRow(ActionEvent event) {		
		boolean isOk = MessageDialog.confirm(getDialog(), "Are you sure you want to delete the selected method?");
		if(!isOk)
			return;		
		
		//check current tracking status again
		if (getModel().isPreviousLineInvalid()) {
			publishProductPreviousLineInvalidEvent();
			return;
		}
		
		QiAppliedRepairMethodDto selectedItem = getDialog().getHistoryRepairMethodDataPane().getTable().getSelectionModel().getSelectedItem();
		QiProgressBar  qiProgressBar = QiProgressBar.getInstance("Deleting selected Repair Method.", "Deleting selected Repair Method.",getModel().getProductId(),getDialog().getStage(),true);
		try {
			qiProgressBar.showMe();
			deleteSelectedRepairMethodRow(selectedItem);
		} finally {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
	}

	/**
	 * this method allows the user to delete the selected repair method
	 * 
	 * @param event
	 */

	private void deleteSelectedRepairMethodRow(QiAppliedRepairMethodDto selectedItem) {		
		
		Long repairId = 0L;
		Integer repairMethodSeq = 0;
		try {
			getDialog().getHistoryRepairMethodDataPane().getTable().getItems().remove(selectedItem);
		
			// call to delete from database
			if (selectedItem.getRepairMethodSeq() != null && selectedItem.getRepairMethodSeq() > 0) {
				if (null == AbstractRepairEntryView.getParentCachedDefectList() || AbstractRepairEntryView.getParentCachedDefectList().isEmpty()) {
					
					if (getDialog().getRepairIds().size() == 0) { //single 
						repairId = selectedItem.getRepairId();
						repairMethodSeq = selectedItem.getRepairMethodSeq();
						getModel().removeAppliedRepairMethodByRepairIdAndSeq(repairId, repairMethodSeq);
					} else { //bulk
						List<Long> repairIdList = selectedItem.getRepairIdList();
						List<Integer> repairMethodSeqList = selectedItem.getRepairMethodSeqList();
						for (int i = 0; i < repairIdList.size(); i++) {
							repairId = repairIdList.get(i);
							repairMethodSeq = repairMethodSeqList.get(i);
							getModel().removeAppliedRepairMethodByRepairIdAndSeq(repairId, repairMethodSeq);
						}
					}
				}
			}
			getDialog().getDeleteButton().setDisable(true);
			getDialog().getHistoryRepairMethodDataPane().getTable().getSelectionModel().clearSelection();
		}
		catch (Exception ex)  {
			StringBuilder sb = new StringBuilder();
			sb.append("Unable to delete repair method for repairId: ").append(repairId);
			getLogger().error("Unable to delete repair method: ");
		}
	}

	@Override
	public void initListeners() {
		currentRepairMethodTableListener();
		currentRepairMethodEditableSpinnerListener();
		historyRepairMethodTableListener();	
		addMethodFilterTextFieldListener();
		refreshRepairComment();
	}	
	
	private void checkToEnableSaveButton() {
		if (getDialog().getHistoryRepairMethodDataPane().getTable().getSelectionModel().isEmpty()) {
			getDialog().getSaveButton().setDisable(true);
			getDialog().getCommentArea().setDisable(true);
		} else {
			getDialog().getSaveButton().setDisable(false);
			getDialog().getCommentArea().setDisable(false);
		}
	}
	
	private void refreshRepairComment() {
		QiAppliedRepairMethodDto selectedItem = getDialog().getHistoryRepairMethodDataPane().getTable().getSelectionModel().getSelectedItem();
		String repairComment = null;
		
		if (selectedItem!=null) {
			repairComment = selectedItem.getComment();
			getDialog().getCommentArea().setText(repairComment);
		} else {
			getDialog().getCommentArea().clear();
		}
	}

	/**
	 * this method gets called when an entry is made inside the spinner
	 */
	private void currentRepairMethodEditableSpinnerListener() {		
		getDialog().getSpinner().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				getSpinnerValue();
			}
		});
	}
	
	/**
	 * this method gets the spinner value 
	 */
	private void getSpinnerValue() {
        if (!getDialog().getSpinner().isEditable()) return;
        String text = checkValue(getDialog().getSpinner().getEditor().getText());
        SpinnerValueFactory<Integer> valueFactory = getDialog().getSpinner().getValueFactory();
        if (valueFactory != null) {
            StringConverter<Integer> converter = valueFactory.getConverter();
            if (converter != null) {
                Integer value = converter.fromString(text);
                valueFactory.setValue(value);
            }
        }
    }
	
	public String checkValue(String value) {
	    Integer defaultValue =PropertyService.getPropertyBean(QiPropertyBean.class, getModel().getCurrentWorkingProcessPointId()).getDefaultRepairTime();
		if (value == null) {
	        return defaultValue.toString();
	    }
	    try {  
	    	Integer.parseInt(value);
	        return value;  
	    } catch(NumberFormatException nfe) {
	    	 return defaultValue.toString();
	    }  
	}

	/**
	 * this method gets called when the repair method is selected
	 * 
	 */
	private void currentRepairMethodTableListener() {
		getDialog().getCurrentRepairMethodDataPane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiAppliedRepairMethodDto>() {
					public void changed(ObservableValue<? extends QiAppliedRepairMethodDto> arg0,QiAppliedRepairMethodDto arg1, QiAppliedRepairMethodDto arg2) {
						
						QiAppliedRepairMethodDto selectedCurrentRepairMethod = getDialog().getCurrentRepairMethodDataPane().getTable().getSelectionModel().getSelectedItem();
						QiAppliedRepairMethodDto selectedHistoryRepairMethod = getDialog().getHistoryRepairMethodDataPane().getTable().getSelectionModel().getSelectedItem();
						
						if (selectedCurrentRepairMethod != null && selectedHistoryRepairMethod != null) {
							getDialog().getHistoryRepairMethodDataPane().getTable().getSelectionModel().clearSelection();
						}
						
						if(arg2 != null && arg2.getRepairMethod() != null && arg2.getRepairMethod().equalsIgnoreCase(getDialog().getDefaultRepairMethod())){
							getDialog().getSpinner().getValueFactory().setValue(getDialog().getDefaultRepairTime());
						}else{
							getDialog().getSpinner().getValueFactory().setValue(0);
						}
						checkToEnableAddButton();
						if(arg2 != null && arg2.getRepairMethod() != null)
						{
							Logger.getLogger().check("Selected Repair Method:"+arg2.getRepairMethod());
						}
					}
				});
		
		getDialog().getCurrentRepairMethodDataPane().getTable().setRowFactory(new Callback<TableView<QiAppliedRepairMethodDto>, TableRow<QiAppliedRepairMethodDto>>() {
			public TableRow<QiAppliedRepairMethodDto> call(TableView<QiAppliedRepairMethodDto> tableView) {
				final TableRow<QiAppliedRepairMethodDto> row = new TableRow<QiAppliedRepairMethodDto>();
				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						final int index = row.getIndex();
						if (index >= 0 && index < getDialog().getCurrentRepairMethodDataPane().getTable().getItems().size() 
								&& getDialog().getCurrentRepairMethodDataPane().getTable().getSelectionModel().isSelected(index)) {
							getDialog().getCurrentRepairMethodDataPane().getTable().getSelectionModel().clearSelection(index);
							event.consume();
						}
					}
				});
				return row;
			}
		});
	}
	
	/**
	 * this method gets called for the Problem Repair Method History table 
	 * 
	 */
	private void historyRepairMethodTableListener(){	
		getDialog().getHistoryRepairMethodDataPane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiAppliedRepairMethodDto>() {
			public void changed(ObservableValue<? extends QiAppliedRepairMethodDto> arg0, QiAppliedRepairMethodDto arg1, QiAppliedRepairMethodDto arg2) {
				
				QiAppliedRepairMethodDto selectedCurrentRepairMethod = getDialog().getCurrentRepairMethodDataPane().getTable().getSelectionModel().getSelectedItem();
				QiAppliedRepairMethodDto selectedHistoryRepairMethod = getDialog().getHistoryRepairMethodDataPane().getTable().getSelectionModel().getSelectedItem();
				
				if (selectedCurrentRepairMethod != null && selectedHistoryRepairMethod != null) {
					getDialog().getCurrentRepairMethodDataPane().getTable().getSelectionModel().clearSelection();
				}
				
				// call to Enable/Disable delete button on selections
				if (	selectedHistoryRepairMethod != null
						&&	selectedHistoryRepairMethod.getIsCompletelyFixed() != 1 
						&&	(getModel().getUserId().equalsIgnoreCase(selectedHistoryRepairMethod.getCreateUser()) //same user
							|| (sessionTimestamp.compareTo(selectedHistoryRepairMethod.getRepairTimestamp()) <= 0) //different user within current session
							)
				)
				{
					getDialog().getDeleteButton().setDisable(false);
				} else if(null!=AbstractRepairEntryView.getParentCachedDefectList() && !AbstractRepairEntryView.getParentCachedDefectList().isEmpty()
						&& selectedHistoryRepairMethod != null  && selectedHistoryRepairMethod.getIsCompletelyFixed() != 1) { // enable the delete button during training mode
					getDialog().getDeleteButton().setDisable(false);
				}
				else {
					getDialog().getDeleteButton().setDisable(true);
				}
				refreshRepairComment();
				checkToEnableSaveButton();
			}
		});
		
		getDialog().getHistoryRepairMethodDataPane().getTable().setRowFactory(new Callback<TableView<QiAppliedRepairMethodDto>, TableRow<QiAppliedRepairMethodDto>>() {
			public TableRow<QiAppliedRepairMethodDto> call(TableView<QiAppliedRepairMethodDto> tableView) {
				final TableRow<QiAppliedRepairMethodDto> row = new TableRow<QiAppliedRepairMethodDto>();
				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						final int index = row.getIndex();
						if (index >= 0 && index < getDialog().getHistoryRepairMethodDataPane().getTable().getItems().size() 
								&& getDialog().getHistoryRepairMethodDataPane().getTable().getSelectionModel().isSelected(index)) {
							getDialog().getHistoryRepairMethodDataPane().getTable().getSelectionModel().clearSelection(index);
							event.consume();
						}
					}
				});
				return row;
			}
		});
	}
	
	/**
	 * this method checks the condition to enable the Add Repair Method button
	 */
	private void checkToEnableAddButton() {
		if (!getDialog().getCurrentRepairMethodDataPane().getTable().getSelectionModel().isEmpty()) {
			getDialog().getAddButton().setDisable(false);
			getDialog().getCommentArea().setDisable(false);
			getDialog().getCompletelyFixBox().setDisable(false);
			getDialog().getSpinnerBox().setDisable(false);
		} else {
			getDialog().getAddButton().setDisable(true);
			getDialog().getCommentArea().setDisable(true);
			getDialog().getCompletelyFixBox().setDisable(true);
			getDialog().getSpinnerBox().setDisable(true);
		}
	}

	/**
	 * this method displays the below data when the Add Repair Method button is clicked
	 * @param repairTimestamp 
	 * 
	 * @return
	 */
	protected QiAppliedRepairMethodDto prepareAppliedDataToRender() {
		boolean selectedRadioValue=false;
		if(((LoggedRadioButton)getDialog().getToggleGroup().getSelectedToggle()).getText().equalsIgnoreCase("Yes")){
			selectedRadioValue=true;
		}
		QiAppliedRepairMethodDto appliedRepairMethodDto = new QiAppliedRepairMethodDto();
		appliedRepairMethodDto.setRepairId(getQiRepairResultDto().getRepairId());
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
	 * This method will be used to populate QiAppliedRepairMethod entity from
	 * dto.
	 * 
	 * @param appliedRepairMethodDto
	 * @return appliedRepairMethod
	 */

	protected QiAppliedRepairMethod populateAppliedMethodEntity(QiAppliedRepairMethodDto appliedRepairMethodDto) {

		QiAppliedRepairMethod appliedRepairMethod = new QiAppliedRepairMethod();
		appliedRepairMethod.setRepairTime(appliedRepairMethodDto.getRepairTime());
		QiAppliedRepairMethodId id = new QiAppliedRepairMethodId();
		id.setRepairId(getQiRepairResultDto().getRepairId());
		appliedRepairMethod.setId(id);
		appliedRepairMethod.setApplicationId(appliedRepairMethodDto.getApplicationId());
		appliedRepairMethod.setComment(appliedRepairMethodDto.getComment());
		appliedRepairMethod.setRepairMethod(appliedRepairMethodDto.getRepairMethod());
		appliedRepairMethod.setIsCompletelyFixed(appliedRepairMethodDto.getIsCompletelyFixed());
		appliedRepairMethod.setCreateUser(getModel().getUserId());
		return appliedRepairMethod;
	}
	
	/**
	 * Method will be executed on click of return to home screen button.
	 * 
	 */
	private void returnToHomeScreenBtnAction() {
		LoggedButton returnToHomeScreenBtn = getDialog().getReturnToHomeScreenBtn();
		EventBusUtil.publish(new ProductEvent(ViewId.REPAIR_ENTRY.getViewLabel(), ProductEventType.PRODUCT_DEFECT_DONE));
		Stage stage = (Stage) returnToHomeScreenBtn.getScene().getWindow();
		stage.close();
	}
	
	protected QiAppliedRepairMethod updateCommentMethodEntity(QiAppliedRepairMethodDto appliedRepairMethodDto) {

		QiAppliedRepairMethod appliedRepairMethod = new QiAppliedRepairMethod();

		appliedRepairMethod.setComment(appliedRepairMethodDto.getComment());
		appliedRepairMethod.setUpdateUser(ClientMainFx.getInstance().getAccessControlManager().getUserName());

		return appliedRepairMethod;
	}
	
	
	private void clearMethodFilterTxt(){
		getDialog().getMethodFilterTextField().clear();
		findRepairMethodsByFilter();
	}
	
	private void addMethodFilterTextFieldListener(){
		 getDialog().getMethodFilterTextField().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				if (getDialog().getMethodFilterTextField().isFocused())
					findRepairMethodsByFilter();
			}
		});
	}

	public QiRepairResultDto getQiRepairResultDto() {
		return this.qiRepairResultDto;
	}

	public void setQiRepairResultDto(QiRepairResultDto qiRepairResultDto) {
		this.qiRepairResultDto = qiRepairResultDto;
	}

	public QiRepairResultDto getOriginalSelectedRepairResultDto() {
		return originalSelectedRepairResultDto;
	}
	public void setOriginalSelectedRepairResultDto(QiRepairResultDto originalSelectedRepairResultDto) {
		this.originalSelectedRepairResultDto = originalSelectedRepairResultDto;
	}
	public List<QiRepairResultDto> getAllSelectedDefects() {
		return allSelectedDefects;
	}

	public void setAllSelectedDefects(List<QiRepairResultDto> allSelectedDefects) {
		this.allSelectedDefects = allSelectedDefects;
	}

	public QiAppliedRepairMethodDto getQiAppliedRepairMethodDto() {
		return qiAppliedRepairMethodDto;
	}

	public void setQiAppliedRepairMethodDto(QiAppliedRepairMethodDto qiAppliedRepairMethodDto) {
		this.qiAppliedRepairMethodDto = qiAppliedRepairMethodDto;
	}
	/**
	 * @return the productModel
	 */
	public ProductModel getProductModel() {
		return productModel;
	}
	/**
	 * @param productModel the productModel to set
	 */
	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
	}
	/**
	 * @return the saveFlag
	 */
	public boolean isDirty() {
		return isDirty;
	}
	/**
	 * @param saveFlag the saveFlag to set
	 */
	public void setDirty(boolean saveFlag) {
		this.isDirty = saveFlag;
	}
	/**
	 * @return the parentController
	 */
	public AbstractRepairEntryController<AbstractQiDefectProcessModel, AbstractQiProcessView<?, ?>> getParentController() {
		return parentController;
	}
	/**
	 * @param parentController the parentController to set
	 */
	public void setParentController(
			AbstractRepairEntryController<AbstractQiDefectProcessModel, AbstractQiProcessView<?, ?>> parentController) {
		this.parentController = parentController;
	}
}
