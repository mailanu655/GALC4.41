package com.honda.galc.client.qi.ipptagentry;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.scene.layout.BorderPane;

import com.honda.galc.client.qi.base.QiFxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.product.IPPTag;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>IPPTagEntryUpdateDialog</code> is the dialog class for IPP Tag Entry 
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
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author L&T Infotech
 */


public class IppTagEntryUpdateDialog extends QiFxDialog<IppTagEntryModel> {

	private IPPTag ippTag;
	private IppTagEntryUpdateDialogController ippTagEntryUpdateDialogController;
	private LoggedLabel ippOldTagNumberLabel;
	private LoggedLabel ippNewTagNumberLabel;
	private LoggedTextField ippOldTagNumberTextField;
	private LoggedTextField ippNewTagNumberTextField;
	private LoggedButton updateIppBtn;
	private LoggedButton cancelBtn;
	private LoggedLabel msgLabel;

	public IppTagEntryUpdateDialog(String title, IPPTag ippTag, IppTagEntryPanel ippTagEntryPanel,IppTagEntryModel model, String applicationId) {
		super(title, applicationId, model);
		this.ippTagEntryUpdateDialogController = new IppTagEntryUpdateDialogController(model,this,ippTagEntryPanel,ippTag);
		this.ippTag = ippTag;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		initComponents();
		initData();
		ippTagEntryUpdateDialogController.initListeners();
	}

	private void initComponents(){
		MigPane migPane = new MigPane("insets 5", "[left,grow]", "");
		ippOldTagNumberLabel = UiFactory.createLabelWithStyle("IPPTagNumber", "Tag", "display-label-16");
		ippOldTagNumberTextField = UiFactory.createTextField("ippOldTagNumberTextField", null, TextFieldState.DISABLED, 130);
		updateIppBtn = createBtn(QiConstant.UPDATE, getIppTagEntryUpdateDialogController());
		migPane.add(ippOldTagNumberLabel);
		migPane.add(ippOldTagNumberTextField);
		migPane.add(updateIppBtn,"wrap");
		ippNewTagNumberLabel = UiFactory.createLabelWithStyle("IPPTagNumber", "New Tag ", "display-label-16");
		ippNewTagNumberTextField = UiFactory.createTextField("ippNewTagNumberTextField", null, TextFieldState.EDIT, 130);
		cancelBtn = createBtn(QiConstant.CANCEL, getIppTagEntryUpdateDialogController());
		migPane.add(ippNewTagNumberLabel);
		migPane.add(ippNewTagNumberTextField);
		migPane.add(cancelBtn,"wrap");
		msgLabel = UiFactory.createLabel("messageLabel");
		msgLabel.setWrapText(true);
		msgLabel.setPrefWidth(300);
		migPane.add(msgLabel,"span2");
		migPane.setPrefSize(400,110);
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);
	}
	
	
	private void initData() {
		ippOldTagNumberTextField.setText(ippTag.getId().getIppTagNo());
	}
	
	public IppTagEntryUpdateDialogController getIppTagEntryUpdateDialogController() {
		return ippTagEntryUpdateDialogController;
	}

	public void setIppTagEntryUpdateDialogController(
			IppTagEntryUpdateDialogController ippTagEntryUpdateDialogController) {
		this.ippTagEntryUpdateDialogController = ippTagEntryUpdateDialogController;
	}

	public LoggedButton getUpdateIppBtn() {
		return updateIppBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public LoggedLabel getIppOldTagNumberLabel() {
		return ippOldTagNumberLabel;
	}

	public LoggedLabel getIppNewTagNumberLabel() {
		return ippNewTagNumberLabel;
	}

	public LoggedTextField getIppOldTagNumberTextField() {
		return ippOldTagNumberTextField;
	}

	public LoggedTextField getIppNewTagNumberTextField() {
		return ippNewTagNumberTextField;
	}
	
	public LoggedLabel getMsgLabel() {
		return msgLabel;
	}

}