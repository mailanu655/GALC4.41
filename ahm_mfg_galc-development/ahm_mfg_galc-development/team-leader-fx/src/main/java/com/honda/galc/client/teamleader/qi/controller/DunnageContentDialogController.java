package com.honda.galc.client.teamleader.qi.controller;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.DunnageMaintModel;
import com.honda.galc.client.teamleader.qi.view.DunnageContentDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * 
 * <h3>DunnageContentDialogController Class description</h3>
 * <p> DunnageContentDialogController description </p>
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
 * April 29 2017
 *
 *
 */

public class DunnageContentDialogController extends QiDialogController<DunnageMaintModel, DunnageContentDialog> {
 
	public DunnageContentDialogController(DunnageMaintModel model, DunnageContentDialog dunnageContentDialog) {
			super();
			setModel(model);
			setDialog(dunnageContentDialog);
		}

	@Override
	public void handle(ActionEvent actionEvent) {
			if(actionEvent.getSource().equals(getDialog().getOkButton())){
				okButtonAction(actionEvent,getDialog().getOkButton());
			}
			else
			{
				cancelButtonAction(actionEvent);
			}
	}


		private void okButtonAction(ActionEvent actionEvent, LoggedButton loggedButton){
			getDialog().setButtonClickedname("Ok");
			if(StringUtils.isBlank(getDialog().getRowText())){
				getDialog().addMessage("\nPlease enter row", "error-message");
			}
			else if(StringUtils.isBlank(getDialog().getColumnText())){
				getDialog().addMessage("Please enter column", "error-message");
			}
			else if(StringUtils.isBlank(getDialog().getLayerText())){
				getDialog().addMessage("Please enter layer", "error-message");
			}
			Stage stage = (Stage) loggedButton.getScene().getWindow();
			stage.close();
		}


		/**
		 * When user clicks on cancel button in the popup screen cancelDefect method gets called.
		 */
		public void cancelButtonAction(ActionEvent event) {
			LoggedButton cancelBtn = getDialog().getCancelBtn();
			try {
				getDialog().setButtonClickedname("Cancel");
				Stage stage = (Stage) cancelBtn.getScene().getWindow();
				stage.close();
			} catch (Exception e) {
				handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
			}
		}


		@Override
		public void initListeners() {
			addTextFieldCommonListener(getDialog().getRowTextField().getControl(), true);
			addTextFieldCommonListener(getDialog().getColumnTextField().getControl(), true);
			addTextFieldCommonListener(getDialog().getLayerTextField().getControl(), true);
			getDialog().getRowTextField().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(8));
			getDialog().getColumnTextField().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(8));
			getDialog().getLayerTextField().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(8));
		}

	}

