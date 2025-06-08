package com.honda.galc.client.teamlead;

import java.util.List;
import java.util.Map;

import com.honda.galc.client.dto.MCOperationMatrixDTO;
import com.honda.galc.client.dto.MCOperationPartRevisionDTO;
import com.honda.galc.client.dto.MCOperationRevisionDTO;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.service.ServiceFactory;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

public class MCOperationsTable extends BaseFXMLTable<MCOperationRevisionDTO> {

	TableView<MCOperationRevisionDTO> tblView = null;
	TableView<MCOperationPartRevisionDTO> childTblView = null;
	TableView<MCOperationMatrixDTO> childTblView2 = null;
	MfgMaintFXMLPane controller;

	public MCOperationsTable() {
		super();
	}

	public MCOperationsTable(MfgMaintFXMLPane controller,
			TableView<MCOperationRevisionDTO> t,
			Map<String, TableColumn<MCOperationRevisionDTO, String>> columnMap) {

		super(t, columnMap, false);
		tblView = t;
		this.controller = controller;
		onRowSelection();
		setContextMenu();
		onTabChanged();
	}

	public void init(TableView<MCOperationPartRevisionDTO> childTable1,
			TableView<MCOperationMatrixDTO> childTable2, List<MCOperationRevisionDTO> list) {

		this.childTblView = childTable1;
		this.childTblView2 = childTable2;
		tblView.setEditable(false);

		tblView.setItems(FXCollections.observableList(list));
	}

	@Override
	public void onRowSelection() {
		tblView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<MCOperationRevisionDTO> () {

					public void changed(ObservableValue<? extends MCOperationRevisionDTO> arg0, MCOperationRevisionDTO arg1,
							MCOperationRevisionDTO arg2) {

						MCOperationRevisionDTO selected = tblView
								.getSelectionModel().getSelectedItem();
						if (selected == null)
							return;

						List<MCOperationPartRevision> partsList = ServiceFactory
								.getDao(MCOperationPartRevisionDao.class)
								.findAllPartsForOperationAndProcessPoint(
										MCOperationsTable.this.controller
												.getSelectedProcessPoint(),
										selected.getOperationName(),
										Integer.valueOf(selected
												.getOpRevision()));

						childTblView.setItems(FXCollections.observableList(DTOConverter
								.convertMcoperationPartRevision(partsList)));
						

						List<MCOperationMatrix> matrixList = ServiceFactory
								.getDao(MCOperationMatrixDao.class)
								.findAllMatrixForOperationAndProcessPoint(
										MCOperationsTable.this.controller
												.getSelectedProcessPoint(),
										selected.getOperationName(),
										Integer.valueOf(selected
												.getOpRevision()));

						childTblView2.setItems(FXCollections
								.observableList(DTOConverter
										.convertMcoperationMatrix(matrixList)));
					}
				});
	}

	protected void setContextMenu() {
		tblView.setRowFactory(new Callback<TableView<MCOperationRevisionDTO>, TableRow<MCOperationRevisionDTO>>() {
			public TableRow<MCOperationRevisionDTO> call(
					TableView<MCOperationRevisionDTO> tableView) {
				final TableRow<MCOperationRevisionDTO> row = new TableRow<MCOperationRevisionDTO>() {
					@Override
					protected void updateItem(MCOperationRevisionDTO item,
							boolean empty) {
						super.updateItem(item, empty);

						if (item != null) {
							Tooltip tooltip = new Tooltip();
							tooltip.setText("RIGHT CLICK TO EDIT THIS RECORD");
							setTooltip(tooltip);
						}
					}
				};

				rowMenu = new ContextMenu();

				MenuItem editRecordItem = UiFactory.createMenuItem("Edit this record");

				editRecordItem.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent event) {
						new OpRevEdit(tblView, row);
					}
				});

				rowMenu.getItems().add(editRecordItem);
				// only display context menu for non-null items:
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(rowMenu).otherwise((ContextMenu) null));
				return row;
			}
		});
	}
	
	public void onTabChanged() {
		controller.getMainTabPane().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Tab>() {
					@Override
					public void changed(ObservableValue<? extends Tab> ov,
							Tab t, Tab t1) {

						MCOperationRevisionDTO selected = tblView
								.getSelectionModel().getSelectedItem();
						if (selected != null) {

							List<MCOperationPartRevision> partsList = ServiceFactory
									.getDao(MCOperationPartRevisionDao.class)
									.findAllPartsForOperationAndProcessPoint(
											MCOperationsTable.this.controller
													.getSelectedProcessPoint(),
											selected.getOperationName(),
											Integer.valueOf(selected
													.getOpRevision()));

							childTblView.setItems(FXCollections.observableList(DTOConverter
									.convertMcoperationPartRevision(partsList)));

						}
					}
				});
	}
}
