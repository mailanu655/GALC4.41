package com.honda.galc.client.teamlead;

import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import com.honda.galc.client.dto.ChangeFormDTO;

public class ChangeFormsTable extends BaseFXMLTable<ChangeFormDTO> {

	TableView<ChangeFormDTO> tblView = null;
	TableView<?> childTblView = null;
	List childList1 = null;
	List childList2 = null;

	public ChangeFormsTable() {
		super();

	}

	public ChangeFormsTable(TableView<ChangeFormDTO> t,
			Map<String, TableColumn<ChangeFormDTO, String>> columnMap) {

		super(t, columnMap, false);
		tblView = t;
		setContextMenu();

	}

	public void init(TableView<?> childTable, List... lists) {
		this.childTblView = childTable;

		tblView.setEditable(false);
		tblView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tblView.setItems(FXCollections.observableList(lists[0]));
	}

	protected void setContextMenu() {

		tblView.setRowFactory(new Callback<TableView<ChangeFormDTO>, TableRow<ChangeFormDTO>>() {
			public TableRow<ChangeFormDTO> call(TableView<ChangeFormDTO> tableView) {
				final TableRow<ChangeFormDTO> row = new TableRow<ChangeFormDTO>();
				rowMenu = new ContextMenu();
			return row;
			}
		});

	}

}
