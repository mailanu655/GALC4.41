package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.teamleader.qi.model.DocumentMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.DocumentDialog;
import com.honda.galc.client.teamleader.qi.view.DocumentMaintenancePanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiDocument;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * 
 * <h3>DocumentMaintenanceController Class description</h3>
 * <p>
 * DocumentMaintenanceController description
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

public class DocumentMaintenanceController extends
		AbstractQiController<DocumentMaintenanceModel, DocumentMaintenancePanel> implements EventHandler<ActionEvent> {

	private QiDocument qiDocument;

	public DocumentMaintenanceController(DocumentMaintenanceModel model, DocumentMaintenancePanel view) {
		super(model, view);
	}

	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if (actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();

			if (QiConstant.CREATE.equals(menuItem.getText())) {
				createDocument();
			} else if (QiConstant.UPDATE.equals(menuItem.getText())) {
				updateDocument(qiDocument);
			} else if (QiConstant.DELETE.equals(menuItem.getText())) {
				deleteDocument(qiDocument);
			}
		}
		if (actionEvent.getSource() instanceof LoggedTextField) {
			getView().reload(getView().getFilterTextData());
		}
	}

	private void createDocument() {
		DocumentDialog dialog = new DocumentDialog(QiConstant.CREATE, new QiDocument(), getModel(), getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		getView().reload(getView().getFilterTextData());
	}

	private void updateDocument(QiDocument document) {
		DocumentDialog dialog = new DocumentDialog(QiConstant.UPDATE, document, getModel(), getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		getView().reload(getView().getFilterTextData());
	}

	private void deleteDocument(QiDocument document) {
		String message = "";
		if (getModel().isDocumentAssigned(document.getDocumentId())) {
			message = "The document is assigned to station. ";
		}

		ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
		if (dialog.showReasonForChangeDialog(message + "Are you sure to delete the document link?")) {
			try {
				QiDocument documentCloned = (QiDocument) document.deepCopy();
				getModel().deleteDocument(document.getDocumentId());

				// call Auditlog utility to capture delete
				AuditLoggerUtil.logAuditInfo(documentCloned, null, dialog.getReasonForChangeText(),
						getView().getScreenName(), getUserId());
			} catch (Exception e) {
				displayErrorMessage("An error occured in deleting document.");
			}
		}
		getView().reload(getView().getFilterTextData());
	}

	public void addContextMenuItems() {
		clearDisplayMessage();
		List<String> menuItemsList = new ArrayList<String>();
		if (qiDocument != null) {
			menuItemsList.add(QiConstant.CREATE);
			menuItemsList.add(QiConstant.UPDATE);
			menuItemsList.add(QiConstant.DELETE);
		} else {
			menuItemsList.add(QiConstant.CREATE);
		}
		getView().getDocumentTablePane()
				.createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
	}

	@Override
	public void initEventHandlers() {
		if (isFullAccess()) {
			addDocumentTableListener();
		}
	}

	private void addDocumentTableListener() {
		getView().getDocumentTablePane().getTable().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<QiDocument>() {
					public void changed(ObservableValue<? extends QiDocument> arg0, QiDocument arg1, QiDocument arg2) {
						qiDocument = getView().getDocumentTablePane().getSelectedItem();
						addContextMenuItems();
					}
				});

		getView().getDocumentTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}
}
