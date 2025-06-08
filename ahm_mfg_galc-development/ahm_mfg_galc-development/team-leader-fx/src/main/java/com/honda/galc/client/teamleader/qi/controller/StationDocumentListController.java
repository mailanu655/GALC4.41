package com.honda.galc.client.teamleader.qi.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import com.honda.galc.client.teamleader.qi.model.StationDocumentListModel;
import com.honda.galc.client.teamleader.qi.view.StationDocumentListPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.qi.QiDocument;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * 
 * <h3>StationDocumentListController Class description</h3>
 * <p>
 * StationDocumentListController description
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

public class StationDocumentListController extends
		AbstractQiController<StationDocumentListModel, StationDocumentListPanel> implements EventHandler<ActionEvent> {

	public StationDocumentListController(StationDocumentListModel model, StationDocumentListPanel view) {
		super(model, view);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
	}

	@Override
	public void addContextMenuItems() {
	}

	/**
	 * This method is used to add listener on main panel table.
	 */
	@Override
	public void initEventHandlers() {
		getView().getAssignedDocumentTablePane().getTable().getSelectionModel().selectedItemProperty()
		.addListener(new ChangeListener<QiDocument>() {
			public void changed(ObservableValue<? extends QiDocument> observableValue, QiDocument oldValue,
					QiDocument newValue) {

				if (null != newValue) {
					Logger.getLogger().check(newValue.getDocumentName() + " : Assigned document is selected");
					clearDisplayMessage();
					selectDocumentAction(newValue.getDocumentLink());
				}
			}

		});
	}

	/**
	 * This method is used to launch browser to view/down document
	 */
	private void selectDocumentAction(String documentLink) {
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI(documentLink));
			} else {
				displayErrorMessage("Browser is not supported", "Browser is not supported");
			}
		} catch (IOException ioe) {
			displayErrorMessage(ioe.getMessage(), "Document Link is not valid");
		} catch (Exception e) {
			displayErrorMessage(e.getMessage(), "Failed to launch the Document Link in browser");
		}
	}
}
