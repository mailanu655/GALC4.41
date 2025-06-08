package com.honda.galc.client.teamleader.qi.controller;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import com.honda.galc.client.teamleader.qi.model.IqsMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.IqsValidationResultDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiIqsValidationResultDto;
import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.entity.qi.QiIqsCategory;
import com.honda.galc.entity.qi.QiIqsQuestion;
import com.honda.galc.entity.qi.QiIqsQuestionId;
import com.honda.galc.entity.qi.QiIqsVersion;

/**
 * 
 * <h3>IqsValidationResultDialogController Class description</h3>
 * <p> IqsValidationResultDialogController description </p>
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
 * Aug 1, 2016
 *
 *
 */

public class IqsValidationResultDialogController extends QiDialogController<IqsMaintenanceModel, IqsValidationResultDialog>{

	private List<QiIqsValidationResultDto> iqsDtoList;

	public IqsValidationResultDialogController(IqsMaintenanceModel model, IqsValidationResultDialog dialog, List<QiIqsValidationResultDto> iqsDtoList) {
		super();
		setModel(model);
		setDialog(dialog);
		this.setIqsDtoList(iqsDtoList);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
			if(actionEvent.getSource().equals(getDialog().getCreateBtn())) importBtnAction(actionEvent);
			else cancelBtnAction(actionEvent);
	}
	/**
	 * When user clicks on create button in the popup screen createDefect method gets called.
	 */
	private void importBtnAction(ActionEvent event) {
		LoggedButton createBtn = getDialog().getCreateBtn();
		try {
			for(QiIqsValidationResultDto iqsDto :iqsDtoList){
				if(iqsDto.getComments().equalsIgnoreCase(QiConstant.VALID)){
					if(!getModel().isIqsVersionExists(iqsDto.getIqsVersion())){
						QiIqsVersion iqsVersion = new QiIqsVersion();
						iqsVersion.setIqsVersion(iqsDto.getIqsVersion());
						iqsVersion.setCreateUser(getUserId());
						getModel().createIqsVersion(iqsVersion);
					}
					if(!getModel().isIqsCategoryExists(iqsDto.getIqsCategory())){
						QiIqsCategory iqsCategory = new QiIqsCategory();
						iqsCategory.setIqsCategory(iqsDto.getIqsCategory());
						iqsCategory.setCreateUser(getUserId());
						getModel().createIqsCategory(iqsCategory);
					}
					if(!getModel().isIqsQuestionExists(Integer.parseInt(iqsDto.getIqsQuestionNo()),iqsDto.getIqsQuestion())){
						QiIqsQuestion iqsQuestion = new QiIqsQuestion();
						QiIqsQuestionId iqsQuestionId = new QiIqsQuestionId();
						iqsQuestion.setId(iqsQuestionId);
						iqsQuestion.getId().setIqsQuestionNo(Integer.parseInt(iqsDto.getIqsQuestionNo()));
						iqsQuestion.getId().setIqsQuestion(iqsDto.getIqsQuestion());
						iqsQuestion.setCreateUser(getUserId());
						getModel().createIqsQuestion(iqsQuestion);
					}

					QiIqs iqs = new QiIqs();
					iqs.setIqsVersion(iqsDto.getIqsVersion());
					iqs.setIqsCategory(iqsDto.getIqsCategory());
					iqs.setIqsQuestionNo(Integer.parseInt(iqsDto.getIqsQuestionNo()));
					iqs.setIqsQuestion(iqsDto.getIqsQuestion());
					iqs.setActive(true);
					getModel().AssociateIqsData(iqs);
				}
			}
			Stage stage = (Stage) createBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured during create action ", "Failed to perform create action", e);
		}
	}

	/**
	 * When user clicks on cancel button in the popup screen cancelDefect method gets called.
	 */
	private void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}
	}

	@Override
	public void initListeners() {
	}

	public List<QiIqsValidationResultDto> getIqsDtoList() {
		return iqsDtoList;
	}

	public void setIqsDtoList(List<QiIqsValidationResultDto> iqsDtoList) {
		this.iqsDtoList = iqsDtoList;
	}

}
