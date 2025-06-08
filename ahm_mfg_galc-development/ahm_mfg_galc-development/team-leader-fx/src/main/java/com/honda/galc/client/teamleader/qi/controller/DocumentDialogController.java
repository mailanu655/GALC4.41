package com.honda.galc.client.teamleader.qi.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.DocumentMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.DocumentDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiDocument;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * 
 * <h3>DocumentDialogController Class description</h3>
 * <p>
 * DocumentDialogController description
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
 * @author Justin Jiang<br>
 *         February 20, 2020
 *
 */

public class DocumentDialogController extends QiDialogController<DocumentMaintenanceModel, DocumentDialog> {

	private QiDocument document;
	private QiDocument documentCloned;

	public DocumentDialogController(DocumentMaintenanceModel model, DocumentDialog dialog, QiDocument document) {
		super();
		setModel(model);
		setDialog(dialog);
		this.document = document;
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if (actionEvent.getSource().equals(getDialog().getCreateBtn()))
			createBtnAction(actionEvent);
		else if (actionEvent.getSource().equals(getDialog().getUpdateButton()))
			updateBtnAction(actionEvent);
		else if (actionEvent.getSource().equals(getDialog().getCancelBtn()))
			cancelBtnAction(actionEvent);
		else if (actionEvent.getSource().equals(getDialog().getTestLinkBtn()))
			testLinkBtnAction(actionEvent);
	}

	private void createBtnAction(ActionEvent event) {
		LoggedButton createBtn = getDialog().getCreateBtn();
		String documentName = StringUtils.trim(getDialog().getDocumentNameTextField().getText());
		String documentLink = StringUtils.trim(getDialog().getDocumentLinkTextField().getText());
		String description = StringUtils.trim(getDialog().getDescriptionTextArea().getText());
		if (QiCommonUtil.isMandatoryFieldEmpty(getDialog().getDocumentNameTextField())) {
			displayErrorMessage("Mandatory field is empty", "Please enter Document Name");
			return;
		}
		if (QiCommonUtil.isMandatoryFieldEmpty(getDialog().getDocumentLinkTextField())) {
			displayErrorMessage("Mandatory field is empty", "Please enter Document Link");
			return;
		}

		if (!isValidDocumentLink(documentLink)) {
			return;
		}

		document = new QiDocument();
		try {
			document.setCreateUser(getUserId());
			document.setDocumentName(documentName);
			document.setDocumentLink(documentLink);
			document.setDescription(description);
			if (((DocumentMaintenanceModel) getModel()).isDocumentExisting(documentName, documentLink)) {
				displayErrorMessage("Already exists! Enter different Document Name/Link",
						"Already exists! Enter different Document Name/Link");
			} else {
				((DocumentMaintenanceModel) getModel()).createDocument(document);
				Stage stage = (Stage) createBtn.getScene().getWindow();
				stage.close();
			}
		} catch (Exception e) {
			handleException("An error occured in creating Document", StringUtils.EMPTY, e);
		}
	}

	private void updateBtnAction(ActionEvent event) {
		clearDisplayMessage();

		/** Mandatory Check for Document name */
		if (QiCommonUtil.isMandatoryFieldEmpty(getDialog().getDocumentNameTextField())) {
			displayErrorMessage("Mandatory field is empty", "Please enter Document Name");
			return;
		}
		/** Mandatory Check for Document link */
		if (QiCommonUtil.isMandatoryFieldEmpty(getDialog().getDocumentLinkTextField())) {
			displayErrorMessage("Mandatory field is empty", "Please enter Document Link");
			return;
		}
		/** Mandatory Check for Reason for Change */
		if (QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())) {
			displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
			return;
		}
		try {
			updateDocument();
		} catch (Exception e) {
			handleException("An error occured in update document ", "Failed to Update Document", e);
		}
	}

	private void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action Failed to perform cancel action",
					StringUtils.EMPTY, e);
		}
	}

	private void testLinkBtnAction(ActionEvent event) {
		String documentLink = StringUtils.trim(getDialog().getDocumentLinkTextField().getText());
		if (StringUtils.isEmpty(documentLink)) {
			displayErrorMessage("Document Link is empty", "Please enter Document Link");
			return;
		}

		if (!documentLink.toUpperCase().contains("ISOQUEST")) {
			displayErrorMessage("Document does not link to ISOQuest", "Please enter Document Link to ISOQuest only");
			return;
		}

		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI(documentLink));
			} else {
				displayErrorMessage("Browser is not supported", "Browser is not supported");
			}
		} catch (IOException ioe) {
			displayErrorMessage(ioe.getMessage(), "Please enter valid Document Link");
		} catch (Exception e) {
			displayErrorMessage(e.getMessage(), "Failed to launch the Document Link in browser");
		}
	}

	private void validationForTextfield() {
		getDialog().getDocumentNameTextField().addEventFilter(KeyEvent.KEY_TYPED,
				QiCommonUtil.restrictLengthOfTextFields(128));
		getDialog().getDocumentLinkTextField().addEventFilter(KeyEvent.KEY_TYPED,
				QiCommonUtil.restrictLengthOfTextFields(256));
		getDialog().getDescriptionTextArea().addEventFilter(KeyEvent.KEY_TYPED,
				QiCommonUtil.restrictLengthOfTextFields(256));
		getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED,
				QiCommonUtil.restrictLengthOfTextFields(256));
	}

	@Override
	public void initListeners() {
		validationForTextfield();
	}

	private void updateDocument() {
		try {
			LoggedButton updateBtn = getDialog().getUpdateButton();
			String documentName = StringUtils.trim(getDialog().getDocumentNameTextField().getText());
			String documentLink = StringUtils.trim(getDialog().getDocumentLinkTextField().getText());
			if (((DocumentMaintenanceModel) getModel()).isDocumentExisting(documentName, documentLink)) {
				displayErrorMessage("Already exists! Enter different Document Name/Link",
						"Already exists! Enter different Document Name/Link");
				return;
			}

			if (!isValidDocumentLink(documentLink)) {
				return;
			}

			if (isUpdated(getQiDocument())) {
				return;
			}

			String description = StringUtils.trim(getDialog().getDescriptionTextArea().getText());
			documentCloned = (QiDocument) document.deepCopy();
			document.setDocumentName(documentName);
			document.setDocumentLink(documentLink);
			document.setDescription(description);
			document.setUpdateUser(getUserId());
			((DocumentMaintenanceModel) getModel()).updateDocument(document);

			// call Auditlog utility to capture old and new data
			AuditLoggerUtil.logAuditInfo(documentCloned, document, getDialog().getReasonForChangeTextArea().getText(),
					getDialog().getScreenName(), getUserId());

			Stage stage = (Stage) updateBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("Error in updateDocument method", "Falied to update Document", e);
		}
	}

	public QiDocument getQiDocument() {
		return document;
	}

	public void setQiDocument(QiDocument document) {
		this.document = document;
	}

	private boolean isValidDocumentLink(String documentLink) {
		// Get the keyword to validate document link
		String[] documentLinkKeywordArray = PropertyService.getPropertyBean(QiPropertyBean.class)
				.getDocumentLinkKeyword();
		boolean isValidDocumentLink = false;
		if (documentLinkKeywordArray.length > 0) {
			String documentLinkKeyword = "";
			for (int i = 0; i < documentLinkKeywordArray.length; i++) {
				if (i == 0) {
					documentLinkKeyword += "[" + documentLinkKeywordArray[i];
				} else {
					documentLinkKeyword += "][" + documentLinkKeywordArray[i];
				}
				if (documentLink.toUpperCase().contains(documentLinkKeywordArray[i].toUpperCase())) {
					isValidDocumentLink = true;
					break;
				}
			}
			documentLinkKeyword += "]";
			if (!isValidDocumentLink) {
				displayErrorMessage("Link doesn't contain keyword:" + documentLinkKeyword,
						"Link doesn't contain keyword:" + documentLinkKeyword);
			}
		} else {
			isValidDocumentLink = true;
		}
		return isValidDocumentLink;
	}
}
