package com.honda.galc.client.qi.ipptagentry;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.qi.base.AbstractQiDialogController;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.product.IPPTag;
import com.honda.galc.entity.product.IPPTagId;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>IPPTagEntryUpdateDialogController</code> is the dialog class for IPP Tag Entry 
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>Mar 31, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author L&T Infotech
 */

public class IppTagEntryUpdateDialogController extends AbstractQiDialogController<IppTagEntryModel, IppTagEntryUpdateDialog>{

	private IPPTag ippTag;
	private IppTagEntryPanel ippTagEntryPanel;

	public IppTagEntryUpdateDialogController(IppTagEntryModel model,IppTagEntryUpdateDialog ippTagEntryUpdateDialog,IppTagEntryPanel ippTagEntryPanel, IPPTag ippTag) {
		super();
		setModel(model);
		setDialog(ippTagEntryUpdateDialog);
		this.ippTagEntryPanel = ippTagEntryPanel;
		this.ippTag = ippTag;
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();

			if(QiConstant.UPDATE.equals(loggedButton.getText())) updateBtnAction(actionEvent);
			else if(QiConstant.CANCEL.equals(loggedButton.getText())) cancelBtnAction(actionEvent);		
		}
	}

	/**
	 * This method is used to cancel the popup screen
	 */
	public void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}
	}

	/**
	 * This method is used to update ipp tag
	 */
	private void updateBtnAction(ActionEvent actionEvent) {
		try{
			boolean ippTagNo = false;
			String tagNumber = StringUtils.trim(getDialog().getIppNewTagNumberTextField().getText());
			if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getIppNewTagNumberTextField())){
				getDialog().displayValidationMessage(getDialog().getMsgLabel(), "Please enter Ipp Tag Number", "error-message");
				return;
			}
			if (isIppTagNumberExist(ippTag, tagNumber)) {
				getDialog().displayValidationMessage(getDialog().getMsgLabel(), "Ipp Tag Number "+tagNumber+" already exist", "error-message");
				return;
			}
			ippTagNo = MessageDialog.confirm(getDialog(), "Are you sure you want to update IPPTag ?");
			if(!ippTagNo)
				return;
			Stage stage = (Stage) getDialog().getUpdateIppBtn().getScene().getWindow();
			getModel().updateIppTagNumber(ippTag, tagNumber);
			stage.close();
		}
		catch(Exception e){
			handleException("An error occured in during updateBtnAction ", "Failed to perform updateBtnAction", e);
		}
	}

	/**
	 * This method is to check duplicate ipp tag number
	 */
	private boolean isIppTagNumberExist(IPPTag ippTag, String ippTagNo) {
		List<IPPTag> tags = ippTagEntryPanel.getIppTagTablePane().getTable().getItems();
		if (tags == null || tags.isEmpty()) {
			return false;
		}
		IPPTagId newId = new IPPTagId(ippTag.getProductId(), ippTagNo, ippTag.getDivisionId());
		for (IPPTag tag : tags) {
			if (tag == null) {
				continue;
			}
			if (newId.equals(tag.getId())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void initListeners() {
		getDialog().getIppNewTagNumberTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(10));
		getDialog().getIppNewTagNumberTextField().requestFocus();
		addTextFieldListener();
	}
	
	/**
	 * This method is textfield listener of Ipp tag entry update dialog
	 */
	private void addTextFieldListener(){
		getDialog().getIppNewTagNumberTextField().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				if(getModel().isIppTagNumberNumeric()){
					String newValue = QiCommonUtil.checkNumericInput(getDialog().getIppNewTagNumberTextField().getText());
					getDialog().getIppNewTagNumberTextField().setText(newValue);
				}
			}
		});
	}
	
}