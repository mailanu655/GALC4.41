package com.honda.galc.client.teamlead;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.honda.galc.client.dto.ChangeFormUnitDTO;
import com.honda.galc.client.utils.UiFactory;


public class PendingUnitsTable extends BaseFXMLTable<ChangeFormUnitDTO> {

	TableView<ChangeFormUnitDTO> tblView = null;
	TableView<?> childTblView = null;
	
	MfgMaintFXMLPane controller;
	
	public PendingUnitsTable() {
		super();

	}

	public PendingUnitsTable(MfgMaintFXMLPane controller,TableView<ChangeFormUnitDTO> t,
			Map<String, TableColumn<ChangeFormUnitDTO, String>> columnMap) {

		super(t, columnMap, false);
		tblView = t;
		this.controller = controller;
		setContextMenu();
	}

	
	public void init(TableView<?> childTable, List... lists) {
		this.childTblView = childTable;
		tblView.setEditable(false);
		tblView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tblView.setItems(FXCollections.observableList(lists[0]));	
	}
	
	
	protected void setContextMenu() {
		

		tblView.setRowFactory(new Callback<TableView<ChangeFormUnitDTO>, TableRow<ChangeFormUnitDTO>>() {
			public TableRow<ChangeFormUnitDTO> call(
					TableView<ChangeFormUnitDTO> tableView) {
				final TableRow<ChangeFormUnitDTO> row = new TableRow<ChangeFormUnitDTO>() {
					@Override
					protected void updateItem(ChangeFormUnitDTO item,
							boolean empty) {
						super.updateItem(item, empty);

						if(item != null) {
							
							Tooltip tooltip = new Tooltip();
		                    tooltip.setText("Right Click To Map PDDA Unit To GALC Operation");
		                    setTooltip(tooltip);
						}
					}
				};
				rowMenu = new ContextMenu();
				
				MenuItem mapProcessItem = UiFactory.createMenuItem(
						"Map PDDA Unit to GALC Operation");
					mapProcessItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						List<ChangeFormUnitDTO> selectedItems = new ArrayList<ChangeFormUnitDTO>(tblView.getSelectionModel().getSelectedItems());
						for(ChangeFormUnitDTO rec: selectedItems) {
						}
						tblView.getItems().removeAll(selectedItems);
					}
				});
				
				MenuItem mapAllProcessItem = UiFactory.createMenuItem(
						"Map All PDDA Units to GALC Operation");
					mapAllProcessItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						for(ChangeFormUnitDTO rec: tblView.getItems()) {
						}
						tblView.getItems().clear();
					}
				});

				rowMenu.getItems().addAll(mapProcessItem, mapAllProcessItem);
				// only display context menu for non-null items:
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(rowMenu).otherwise((ContextMenu) null));
				return row;
			}
		});

	}
}
