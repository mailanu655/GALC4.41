package com.honda.galc.client.teamlead;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.dto.ChangeFormDTO;
import com.honda.galc.client.dto.MCRevisionDTO;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

public class NewChangeFormsTable extends BaseFXMLTable<ChangeFormDTO> {

	TableView<ChangeFormDTO> tblView = null;
	TableView<MCRevisionDTO> revisionsTblView = null;
	ChangeFormDetails chgFrmDetails;
	MfgControlMaintenancePropertyBean propertyBean;

	public NewChangeFormsTable() {
		super();

	}

	public NewChangeFormsTable(TableView<ChangeFormDTO> t, Map<String, TableColumn<ChangeFormDTO, String>> columnMap, TableView<MCRevisionDTO> revisionsTblView, 
			MfgControlMaintenancePropertyBean propertyBean, boolean isOneClick) {

		super(t, columnMap, false);
		tblView = t;
		this.revisionsTblView = revisionsTblView;
		this.propertyBean = propertyBean;
		if(!isOneClick)
			setContextMenu();

	}

	public void init(ChangeFormDetails chgFrmDetails) {
		this.chgFrmDetails = chgFrmDetails;
		tblView.setEditable(false);
		tblView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tblView.setItems(FXCollections.observableList(chgFrmDetails.getChangeForms()));
	}

	protected void setContextMenu() {

		tblView.setRowFactory(new Callback<TableView<ChangeFormDTO>, TableRow<ChangeFormDTO>>() {
			public TableRow<ChangeFormDTO> call(TableView<ChangeFormDTO> tableView) {
				final TableRow<ChangeFormDTO> row = new TableRow<ChangeFormDTO>() {
					@Override
					protected void updateItem(ChangeFormDTO item,
							boolean empty) {
						super.updateItem(item, empty);

						if(item != null) {
							
							Tooltip tooltip = new Tooltip();
		                    tooltip.setText("Right Click To Assign Changeforms To  Revisions");
		                    setTooltip(tooltip);
						}
					}
				};
				rowMenu = new ContextMenu();
				MenuItem assignToReviewMenuItem = UiFactory.createMenuItem(
						"Assign Revision to Change Forms");

				assignToReviewMenuItem
						.setOnAction(new EventHandler<ActionEvent>() {

							public void handle(ActionEvent event) {
								//ChangeFormDTO rec = row.getItem();
								List<ChangeFormDTO> selectedList = tblView
										.getSelectionModel().getSelectedItems();

								List<Integer> list2 = new ArrayList<Integer>();
								List<ChangeFormDTO> selectedData = new ArrayList<ChangeFormDTO>();

								for (ChangeFormDTO form : selectedList) {
									list2.add(form.getChangeFormId());
									selectedData.add(form);
								}

								new RevisionAssignmentWizard(list2, selectedData, getUserId(), revisionsTblView, tblView, propertyBean);

							}
						});

				rowMenu.getItems().add(assignToReviewMenuItem);
				// only display context menu for non-null items:
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(rowMenu).otherwise((ContextMenu) null));

				return row;
			}
		});

	}

}
