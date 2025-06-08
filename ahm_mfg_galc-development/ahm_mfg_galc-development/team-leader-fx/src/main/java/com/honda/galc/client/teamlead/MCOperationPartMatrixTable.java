package com.honda.galc.client.teamlead;

import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import com.honda.galc.client.dto.MCOperationMatrixDTO;

public class MCOperationPartMatrixTable extends BaseFXMLTable<MCOperationMatrixDTO> {

	TableView<MCOperationMatrixDTO> operationsMatrixTblView = null;
	TableView<?> childTblView = null;
	List childList1 = null;
	List childList2 = null;

	//
	public MCOperationPartMatrixTable() {
		super();

	}

	public MCOperationPartMatrixTable(
			TableView<com.honda.galc.client.dto.MCOperationMatrixDTO> t,
			Map<String, TableColumn<MCOperationMatrixDTO, String>> columnMap) {

		super(t, columnMap, false);
		operationsMatrixTblView = t;

	}

	public void init(TableView<?> childTable, List... lists) {
		this.childTblView = childTable;
		// childList1 = lists[1];
		// childList2 = lists[2];

		operationsMatrixTblView.setEditable(false);

		operationsMatrixTblView
				.setItems(FXCollections.observableList(lists[0]));

	}

	@Override
	public void onRowSelection() {

		operationsMatrixTblView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener() {

					public void changed(ObservableValue arg0, Object arg1,
							Object arg2) {

					

					}

				});

	}

}
