package com.honda.galc.client.qics.view.action;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.event.CaretListener;

import com.honda.galc.client.qics.view.dialog.ProductCheckResultsDialog;
import com.honda.galc.client.qics.view.screen.IdlePanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.exception.TaskException;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Implementation of <code>Action</code> interface for <i>Idel Panel</i>
 * submit action. *
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class IdlePanelSubmitAction extends AbstractPanelAction {

	private static final long serialVersionUID = 1L;

	private CaretListener[] caretListeners;

	public IdlePanelSubmitAction(QicsPanel qicsPanel) {
		super(qicsPanel);
	}

	@Override
	protected void beforeExecute(ActionEvent e) {
		removeCaretListeners();
		super.beforeExecute(e);
		getQicsFrame().clearStatusMessage();
	}

	@Override
	public void execute(ActionEvent e) {

		getQicsFrame().clearStatusMessage();
		getQicsFrame().setTimeout();
		TextFieldState.EDIT.setState(getQicsPanel().getProductNumberTextField());

		String inputNumber = getQicsPanel().getProductNumberTextField().getText();
		if (inputNumber != null) {
			inputNumber = inputNumber.trim();
			getQicsPanel().getProductNumberTextField().setText(inputNumber);
		}

		if (!getQicsController().getProductTypeData().isNumberValid(inputNumber)) {
			setErrorMessage("The product number is invalid.");
			getQicsController().playNgSound();
			return;
		}

		try {
			getQicsController().submitProductForProcessing(inputNumber);
		}catch (TaskException ex) {
			setErrorMessage(ex.getMessage());
			getQicsController().playNgSound();
			return;
		}
			
		getQicsController().setRefreshProductPreCheckResults(false);

		if (getQicsFrame().displayDelayedMessage()) {
			getQicsController().playNgSound();
			return;
		}

		getQicsController().validateProduct();

		getQicsPanel().getProductNumberTextField().setText("");
		getQicsFrame().clearMessage();

		getQicsController().setRefreshProductPreCheckResults(false);//avoid for checking twice
		getQicsFrame().displayMainView();

		boolean errorFlag = getQicsFrame().displayDelayedMessage();
		
		if (errorFlag || !getQicsController().isProductProcessable()) {
			if(getQicsController().isProductScrap()) {
				getQicsController().playScrapSound();
			} else {
				getQicsController().playNgSound();
			}			
		} else if (getQicsController().isProductPreCheckResultsExist()){
			getQicsController().playWarnSound();
		} else {
			getQicsController().playOkSound();
		}
	}

	protected void afterExecute(ActionEvent e) {
		super.afterExecute(e);
		addCaretListeners();
		getQicsFrame().clearStatusMessage();
	}

	protected void processProductCheckResults(Map<String, Object> productCheckResults) {
		if (productCheckResults == null || productCheckResults.isEmpty()) {
			return;
		}
		ProductCheckResultsDialog dialog = new ProductCheckResultsDialog(getQicsFrame(), productCheckResults);
		dialog.setLocationRelativeTo(getQicsFrame());
		dialog.setTitle("  Product Check Results");
		dialog.setMessage("  Press OK to continue.");

		dialog.setVisible(true);
	}

	@Override
	protected void setErrorMessage(String errorMessageId) {
		super.setErrorMessage(errorMessageId);
		TextFieldState.ERROR.setState(getQicsPanel().getProductNumberTextField());
		getQicsPanel().getProductNumberTextField().setColor(Color.red);
		getQicsPanel().getProductNumberTextField().selectAll();
	}

	protected void removeCaretListeners() {
		caretListeners = getQicsPanel().getProductNumberTextField().getCaretListeners();
		if (caretListeners == null || caretListeners.length == 0) {
			return;
		}

		for (CaretListener caretListener : caretListeners) {
			getQicsPanel().getProductNumberTextField().removeCaretListener(caretListener);
		}
	}

	protected void addCaretListeners() {
		if (caretListeners == null || caretListeners.length == 0) {
			return;
		}

		for (CaretListener caretListener : caretListeners) {
			if (caretListener != null) {
				getQicsPanel().getProductNumberTextField().addCaretListener(caretListener);
			}
		}
	}

	@Override
	protected IdlePanel getQicsPanel() {
		return (IdlePanel) super.getQicsPanel();
	}
}
